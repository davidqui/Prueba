
package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.EmailDTO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Servicio encarga de enviar las notificaciones de correo eléctronico
 * @author samuel.delgado@controtechcg.com
 * @since 1.8
 * @version 06/07/2018
 */
@Service
public class CorreoNotificacionService {

    @Value("${com.mil.imi.sicdi.mail.username}")
    private String senderMail;
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    /**
     * Envia un correo de una notificación
     * @param mensaje 
     * @throws javax.mail.MessagingException 
     */
    public void enviarNotificacion(EmailDTO mensaje) throws MessagingException{
        System.out.println("TEST EMAIL"+ mensaje.toString());
        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        
        message.setSubject(mensaje.getAsunto());
        
        message.setFrom(senderMail);
        
        if (mensaje.getDestino() != null)
            message.setTo(mensaje.getDestino());
        
        //se agrega el cuerpo del mensaje
        String bodyHtml = "";
        if (mensaje.getCabecera() != null)
            bodyHtml += mensaje.getCabecera();
        if (mensaje.getCuerpo()!= null)
            bodyHtml += mensaje.getCuerpo();
        if (mensaje.getPiePagina()!= null)
            bodyHtml += mensaje.getPiePagina();
        
        message.setText(bodyHtml);
        // Enviar Correo
        this.javaMailSender.send(mimeMessage);
    }
}
