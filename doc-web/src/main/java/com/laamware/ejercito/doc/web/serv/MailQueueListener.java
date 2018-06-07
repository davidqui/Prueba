
package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.EmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONObject;
import org.springframework.jms.annotation.JmsListener;

/**
 * Servicio para la recepción de elementos entrantes a la cola de correos.
 * @author samuel.delgado@controltechcg.com
 * @since 1.8
 * @version 06/07/2018 Issue #169 (SICDI-Controltech) feature-169
 */
@Component
public class MailQueueListener {
    
    @Autowired
    private CorreoNotificacionService correoNotificacionService;
    
    /***
     * Recive los elementos de la cola de correo de ActiveMQ
     * @param mensaje 
     */
    @JmsListener(destination = "MAIL_ACTIVEMQ_QUEUE")
    public void receptorNotificaciones(String mensaje){
        try {
            correoNotificacionService.enviarNotificacion(JsonStringToEmailDTO(mensaje));
        } catch (Exception e) {
            //TODO reintentar enviar
            e.printStackTrace();
        }
    }
    
    
    public EmailDTO JsonStringToEmailDTO(String message){
        JSONObject obj = new JSONObject(message);
        EmailDTO emailMessage = new EmailDTO(
                                            obj.getString("remitente"), 
                                            obj.getString("destino"), 
                                            null,
                                            obj.getString("asunto"),
                                            obj.getString("cabecera"), 
                                            obj.getString("cuerpo"), 
                                            obj.getString("piePagina"),
                                            null);
        
        return emailMessage;
    }
            
}
