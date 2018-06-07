package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.EmailDTO;
import javax.jms.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Servicio para envío de DTOs de correo a la cola correspondiente en el
 * ActiveMQ.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 06/07/2018 Issue #169 (SICDI-Controltech) feature-169
 */
@Service
public class MailQueueService {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    
    @Autowired
    private Queue queue;
    
    /***
     * Envia el correo elétronico a la cola de AtiveMQ
     * @param emailDTO mensaje a enviar
     */
    public void enviarCorreo(final EmailDTO emailDTO){
        jmsMessagingTemplate.convertAndSend(this.queue, emailDTO);
    }
}
