package com.laamware.ejercito.doc.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapConfiguration {

	@Value("${docweb.ldap.connection}")
	private String ldapConnectionString;
	
	@Value("${docweb.ldap.user}")
	private String ldapUser;
	
	@Value("${docweb.ldap.pass}")
	private String ldapPass;
	
    @Bean
    public LdapContextSource contextSource () {
        LdapContextSource contextSource= new LdapContextSource();
        contextSource.setUserDn("EJC-CAIMI-NAL");
        contextSource.setUrl(ldapConnectionString);
        contextSource.setUserDn(ldapUser);
        contextSource.setPassword(ldapPass);
        
        //HashMap<String, Object> props = new HashMap<String,Object>();
        //props.put("java.naming.referral", "follow");
        //contextSource.setBaseEnvironmentProperties(props);
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
    	LdapTemplate tmp = new LdapTemplate(contextSource());
    	tmp.setIgnorePartialResultException(true);
    	return tmp;
    }

}
