package com.laamware.ejercito.doc.web.jobs;

import com.laamware.ejercito.doc.web.dto.EmailDTO;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Notificacion;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.serv.MailQueueService;
import com.laamware.ejercito.doc.web.serv.NotificacionService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

/**
 * Tarea agendada para el proceso de notificación vía correo electrónico de
 * forma automatizada.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 06/12/2018 Issue #169 (SICDI-Controltech) feature-169
 */
@Component
public class NotificacionJobTask {

    private static final Logger LOG = Logger.getLogger(NotificacionJobTask.class.getName());
    
    private static final int TEMPLATE_DOCUMENTO_A_VENCER = 40;
    private static final int TEMPLATE_DOCUMENTO_A_VENCER_HOY = 50;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Value("${com.mil.imi.sicdi.job.notificacion.plazo-vencido.activo}")
    private Boolean jobNotificacionPlazoVencidoActivo;

    @Value("${com.mil.imi.sicdi.job.notificacion.plazo-vencido.dias-anticipacion}")
    private Integer jobNotificacionPlazoVencidoDiasAnticipacion;
    
    /*
     * 2018-06-15 samuel.delgado@controltechcg.com Issue #169 (SICDI-Controltech)
     * feature-169: Servicio de notificaciones de correo eléctronico.
     */
    @Autowired
    private MailQueueService mailQueueService;
    
    /*
     * 2018-06-15 samuel.delgado@controltechcg.com Issue #169 (SICDI-Controltech)
     * feature-169: Servicio de notificaciones.
     */
    @Autowired
    private NotificacionService notificacionService;

    /**
     * Ejecuta la tarea de notificación de documentos vencidos.
     */
    @Scheduled(cron = "${com.mil.imi.sicdi.job.notificacion.plazo-vencido.cron}")
    public void notificarDocumentosVencidos() {
        LOG.info("com.laamware.ejercito.doc.web.jobs.NotificacionJobTask.notificarDocumentosVencidos()");
        if (!jobNotificacionPlazoVencidoActivo) {
            LOG.info("El Job de Notificaciones de documentos con plazo vencido no se encuentra activo.");
            return;
        }

        notificarDocumentosAVencer();
        notificarDocumentosVenceHoy();
    }
    
    private void notificarDocumentosAVencer() {
        LOG.log(Level.INFO, "PlazoVencido - DiasAnticipacion = {0}", jobNotificacionPlazoVencidoDiasAnticipacion);

        final List<Documento> documentosAVencer = documentoRepository.findAllDocumentosPlazoAVencer(jobNotificacionPlazoVencidoDiasAnticipacion);
        LOG.log(Level.INFO, "documentosAVencer = {0}", documentosAVencer.size());

        for (int i = 0; i < documentosAVencer.size(); i++) {
            final Documento documento = documentosAVencer.get(i);
            try {
                notificarDocumentoAVencer(documento);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, documento.getId(), ex);
            }
        }
    }
    
    /*
     * 2018-06-15 samuel.delgado@controltechcg.com Issue #169 (SICDI-Controltech)
     * feature-169: se agrega el envio de la notificación.
     */
    private void notificarDocumentoAVencer(final Documento documento) {
        try {
            List<Notificacion> notificaciones = notificacionService.fingByTypoNotificacionId(TEMPLATE_DOCUMENTO_A_VENCER);
            
            if (notificaciones != null && !notificaciones.isEmpty()) {
                Notificacion notificacion = notificaciones.get(0);
                Instancia instancia = documento.getInstancia();
                Usuario usuarioAsignado = instancia.getAsignado();

                Map<String, Object> model = new HashMap();
                model.put("usuario", usuarioAsignado);
                model.put("instancia", instancia);
                model.put("documento", documento);

                Template t = new Template(notificacion.toString(), new StringReader(notificacion.getTemplate()));
                String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                EmailDTO mensaje = new EmailDTO(usuarioAsignado.getEmail(), null,
                        notificacion.getAsunto(), "", html, "", null);
                mailQueueService.enviarCorreo(mensaje);
                System.out.println("*** " + documento.getAsunto());
            }
        } catch (IOException | TemplateException ex) {
            LOG.log(Level.SEVERE, documento.getId(), ex);
        }
    }

    private void notificarDocumentosVenceHoy() {
        final List<Documento> documentosVencidos = documentoRepository.findAllDocumentosPlazoVenceHoy();
        LOG.log(Level.INFO, "documentosVencidos = {0}", documentosVencidos);

        for (int i = 0; i < documentosVencidos.size(); i++) {
            final Documento documento = documentosVencidos.get(i);
            try {
                notificarDocumentoVencido(documento);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, documento.getId(), ex);
            }
        }
    }
    
    /*
     * 2018-06-15 samuel.delgado@controltechcg.com Issue #169 (SICDI-Controltech)
     * feature-169: se agrega el envio de la notificación.
     */
    private void notificarDocumentoVencido(final Documento documento) {
        try {
            List<Notificacion> notificaciones = notificacionService.fingByTypoNotificacionId(TEMPLATE_DOCUMENTO_A_VENCER_HOY);
            
            if (notificaciones != null && !notificaciones.isEmpty()) {
                Notificacion notificacion = notificaciones.get(0);
                Instancia instancia = documento.getInstancia();
                Usuario usuarioAsignado = instancia.getAsignado();

                Map<String, Object> model = new HashMap();
                model.put("usuario", usuarioAsignado);
                model.put("instancia", instancia);
                model.put("documento", documento);

                Template t = new Template(notificacion.toString(), new StringReader(notificacion.getTemplate()));
                String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                EmailDTO mensaje = new EmailDTO(usuarioAsignado.getEmail(), null,
                        notificacion.getAsunto(), "", html, "", null);
                mailQueueService.enviarCorreo(mensaje);
                System.out.println("*** " + documento.getAsunto());
            }
        } catch (IOException | TemplateException ex) {
            LOG.log(Level.SEVERE, documento.getId(), ex);
        }
    }

}
