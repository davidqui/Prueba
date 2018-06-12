package com.laamware.ejercito.doc.web.jobs;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    @Autowired
    private DocumentoRepository documentoRepository;

    @Value("${com.mil.imi.sicdi.job.notificacion.plazo-vencido.activo}")
    private Boolean jobNotificacionPlazoVencidoActivo;

    @Value("${com.mil.imi.sicdi.job.notificacion.plazo-vencido.dias-anticipacion}")
    private Integer jobNotificacionPlazoVencidoDiasAnticipacion;

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

    private void notificarDocumentoAVencer(final Documento documento) {
        System.out.println("*** " + documento.getAsunto());
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

    private void notificarDocumentoVencido(final Documento documento) {
        System.out.println("*** " + documento.getAsunto());
    }

}
