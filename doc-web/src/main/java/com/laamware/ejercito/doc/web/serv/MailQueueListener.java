package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.EmailDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.jms.annotation.JmsListener;

/**
 * Servicio para la recepción de elementos entrantes a la cola de correos.
 *
 * @author samuel.delgado@controltechcg.com
 * @since 1.8
 * @version 06/07/2018 Issue #169 (SICDI-Controltech) feature-169
 */
@Component
public class MailQueueListener {

    private static final Logger LOG = Logger.getLogger(MailQueueListener.class.getName());

    @Autowired
    private CorreoNotificacionService correoNotificacionService;

    /**
     * *
     * Recive los elementos de la cola de correo de ActiveMQ
     *
     * @param mensaje
     */
    @JmsListener(destination = "MAIL_ACTIVEMQ_QUEUE")
    public void receptorNotificaciones(String mensaje) {
        try {
            correoNotificacionService.enviarNotificacion(JsonStringToEmailDTO(mensaje));
        } catch (MessagingException e) {
            //TODO reintentar enviar
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public EmailDTO JsonStringToEmailDTO(String message) {
        System.out.println(message);

        JSONObject obj = new JSONObject(message);
        String cuerpo = obj.getString("cuerpo");

        List<String> destinatarioCopia = new ArrayList<>();

        String[] dtc = obj.getString("copiaDestinos").split(";");
        destinatarioCopia.addAll(Arrays.asList(dtc));

        EmailDTO emailMessage = new EmailDTO(
                obj.getString("remitente"),
                obj.getString("destino"),
                destinatarioCopia,
                obj.getString("asunto"),
                obj.getString("cabecera"),
                cuerpo,
                obj.getString("piePagina"),
                null);

        return emailMessage;
    }

}
