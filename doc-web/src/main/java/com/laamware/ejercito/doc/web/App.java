package com.laamware.ejercito.doc.web;

import com.laamware.ejercito.doc.web.serv.SpringSecurityAuditorAware;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import java.sql.SQLException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import oracle.ucp.jdbc.PoolDataSourceImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableJpaRepositories
@EnableTransactionManagement
@EnableJpaAuditing()
@EnableAsync
@EnableScheduling
@EnableEncryptableProperties
public class App {

    @Autowired
    private OracleRACConfiguration racConfiguration;
    
    public static void main(String[] args) {
        // 2018-03-21 edison.gonzalez@controltechcg.com Issue #154: Ajuste
        // para ejecutar la aplicación para encriptar texto.
        if (args != null && args.length > 0) {
            new JasyptEncrypt().exec(args);
            return;
        }
        SpringApplication.run(App.class, args);
    }

    @Bean
    public AuditorAware<Integer> auditorAware() {
        return new SpringSecurityAuditorAware();
    }
        
    @Bean
    @Primary
    public DataSource ucpPoolDataSource() throws SQLException {
        // 2018-03-21 edison.gonzalez@controltechcg.com Issue #154: Ajuste
        // para tomar los atributos de la conexion de la base de datos de la clase
        // OracleRACConfiguration.
        PoolDataSource poolDataSource = (PoolDataSourceImpl) PoolDataSourceFactory.getPoolDataSource();
        poolDataSource.setConnectionFactoryClassName(racConfiguration.getConnectionFactoryClassName());
        poolDataSource.setURL(racConfiguration.getUrl());
        poolDataSource.setUser(racConfiguration.getUser());
        poolDataSource.setPassword(racConfiguration.getPassword());
        poolDataSource.setFastConnectionFailoverEnabled(racConfiguration.getFastConnectionFailoverEnabled());
        poolDataSource.setConnectionPoolName(racConfiguration.getPoolName());
        poolDataSource.setValidateConnectionOnBorrow(racConfiguration.getValidateConnectionOnBorrow());
        poolDataSource.setInitialPoolSize(racConfiguration.getInitialPoolSize());
        poolDataSource.setMaxPoolSize(racConfiguration.getMaxPoolSize());
        poolDataSource.setMaxConnectionReuseTime(racConfiguration.getMaxConnectionReuseTime());
        poolDataSource.setMaxConnectionReuseCount(racConfiguration.getMaxConnectionReuseCount());
        poolDataSource.setAbandonedConnectionTimeout(racConfiguration.getAbandonedConnectionTimeout());
        poolDataSource.setTimeToLiveConnectionTimeout(racConfiguration.getTimeToLiveConnectionTimeout());
        poolDataSource.setConnectionWaitTimeout(racConfiguration.getConnectionWaitTimeout());
        poolDataSource.setInactiveConnectionTimeout(racConfiguration.getInactiveConnectionTimeout());
        poolDataSource.setTimeoutCheckInterval(racConfiguration.getTimeoutCheckInterval());
        return poolDataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.laamware.ejercito.doc.web.entity");
        factory.setDataSource(dataSource);
        Properties props = new Properties();
        props.put("hibernate.ddl.auto", "none");
        props.put("hibernate.hbm2ddl.auto", "");
        //props.put("hibernate.show_sql", "true");
        factory.setJpaProperties(props);
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf, final DataSource dataSource) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    @Bean
    public SessionFactory sessionFactory(HibernateEntityManagerFactory hemf) {
        return hemf.getSessionFactory();
    }
    
}
