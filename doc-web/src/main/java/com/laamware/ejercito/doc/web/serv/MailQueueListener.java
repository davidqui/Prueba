
package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.EmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public void receptorNotificaciones(EmailDTO mensaje){
        try {
            correoNotificacionService.enviarNotificacion(mensaje);
        } catch (Exception e) {
            //TODO reintentar enviar
            e.printStackTrace();
        }
    }
}
