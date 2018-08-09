package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.EmailDTO;
import java.util.ArrayList;
import java.util.List;
import javax.jms.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    
    @Value("${com.mil.imi.sicdi.mail.prueba}")
    private Boolean mailTest;
    
    /***
     * Envia el correo elétronico a la cola de AtiveMQ
     * @param emailDTO mensaje a enviar
     */
    public void enviarCorreo(final EmailDTO emailDTO){
        
        jmsMessagingTemplate.convertAndSend(this.queue, emailDTO_ToJsonString(emailDTO));
    }
    
    public String emailDTO_ToJsonString(final EmailDTO emailDTO){
        String emailCp = "";
        if (emailDTO.getCopiaDestino() != null && !emailDTO.getCopiaDestino().isEmpty()) {
            List<String> listaCp = emailDTO.getCopiaDestino();
            for (int i = 0; i < listaCp.size()-1; i++) {
                emailCp += listaCp.get(i)+";";
            }
            emailCp += listaCp.get(listaCp.size()-1);
        }
        
        String body = emailDTO.getCuerpo().replaceAll("\"", "\\\\\"");
        body = body.replaceAll("'", "\\\\\"");
        
        List<String> correosAutorizados = new ArrayList<>(); 
        correosAutorizados.add("jcespedeso@imi.mil.co"); 
        correosAutorizados.add("mmendozar@imi.mil.co"); 
        correosAutorizados.add("aherreram@imi.mil.co"); 
        correosAutorizados.add("rgomezg@imi.mil.co"); 
        correosAutorizados.add("egonzalezm@imi.mil.co"); 
        correosAutorizados.add("mrianom@imi.mil.co"); 
        correosAutorizados.add("cmartinezcas@imi.mil.co"); 
        correosAutorizados.add("dquijanor@imi.mil.co"); 
        correosAutorizados.add("sdelgadom@imi.mil.co"); 
        correosAutorizados.add("samuel.delgado@controltechcg.com"); 
        String destino = emailDTO.getDestino();
        
        if (mailTest) {
            destino = "sdelgadom@imi.mil.co"; 
            if (correosAutorizados.contains(emailDTO.getDestino())) { 
                destino = emailDTO.getDestino(); 
            } 
        }

        
        String jsonString = "{"
                + "'destino':'"+destino
                + "', 'copiaDestinos':'"+emailCp
                + "', 'asunto':'"+emailDTO.getAsunto()
                + "', 'cabecera':'"+emailDTO.getCabecera()
                + "', 'cuerpo':'"+body.replaceAll("[\r][\n]", " ")
                + "', 'piePagina':'"+emailDTO.getPiePagina()
                + "'}";
        return jsonString;
    }
}
