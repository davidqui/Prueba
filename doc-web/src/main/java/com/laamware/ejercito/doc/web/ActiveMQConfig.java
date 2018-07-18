package com.laamware.ejercito.doc.web;

import javax.jms.Queue;
import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

/**
 * Configuración para Apache Active MQ.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 06/07/2018 Issue #169 (SICDI-Controltech) feature-169
 */
@Configuration
@EnableJms
public class ActiveMQConfig {

    private enum QueueName {
        MAIL_ACTIVEMQ_QUEUE("com.laamware.ejercito.doc.web.dto.notifications");

        private final String dtoPackage;

        private QueueName(String dtoPackage) {
            this.dtoPackage = dtoPackage;
        }

        String getDtoPackage() {
            return dtoPackage;
        }
    }

    @Value("${spring.activemq.broker-url}")
    private String activeMQBrokerURL;

    /**
     * Bean de la cola para correo electrónico.
     *
     * @return Bean de ActiveMQ.
     */
    @Bean
    public Queue mailActiveMQQueue() {
        return new ActiveMQQueue(QueueName.MAIL_ACTIVEMQ_QUEUE.name());
    }

    /**
     * Bean de la fábrica de conexiones al ActiveMQ.
     *
     * @return Bean de fábrica de conexiones.
     */
    @Bean
    public ActiveMQXAConnectionFactory mailActiveMQXAConnectionFactory() {
        final ActiveMQXAConnectionFactory connectionFactory = new ActiveMQXAConnectionFactory(activeMQBrokerURL);
        // TODO: Revisar el trusted packages.
        return connectionFactory;
    }
}
