package com.laamware.ejercito.doc.web;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Configuración para correo electrónico.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 06/07/2018 Issue #169 (SICDI-Controltech) feature-169
 */
@Configuration
public class MailConfig {

    @Value("${com.mil.imi.sicdi.mail.host}")
    private String mailHost;

    @Value("${com.mil.imi.sicdi.mail.port}")
    private Integer mailPort;

    @Value("${com.mil.imi.sicdi.mail.username}")
    private String mailUsername;

    @Value("${com.mil.imi.sicdi.mail.password}")
    private String mailPassword;

    @Value("${mail.transport.protocol}")
    private String mailTransportProtocol;

    @Value("${mail.smtp.auth}")
    private Boolean mailSMTPAuth;

    @Value("${mail.smtp.starttls.enable}")
    private Boolean mailSMTPStartTLSEnable;

    @Value("${mail.smtp.ssl.trust}")
    private String mailSMTP_SSLTrust;

    @Value("${mail.debug}")
    private Boolean mailDebug;

    /**
     * Bean del transmisor de correo electrónico.
     *
     * @return Bean del transmisor de correo.
     */
    @Bean
    public JavaMailSender javaMailSender() {
        final JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(mailHost);
        sender.setPort(mailPort);

        if (mailSMTPAuth) {
            sender.setUsername(mailUsername);
            sender.setPassword(mailPassword);
        }

        sender.setJavaMailProperties(getProperties());
        return sender;
    }

    private Properties getProperties() {
        final Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", mailTransportProtocol);
        properties.setProperty("mail.smtp.auth", mailSMTPAuth.toString());
        properties.setProperty("mail.smtp.starttls.enable", mailSMTPStartTLSEnable.toString());

        if (mailSMTP_SSLTrust != null && !mailSMTP_SSLTrust.trim().isEmpty()) {
            properties.setProperty("mail.smtp.ssl.trust", mailSMTP_SSLTrust);
        }

        properties.setProperty("mail.debug", mailDebug.toString());

        return properties;
    }
}
