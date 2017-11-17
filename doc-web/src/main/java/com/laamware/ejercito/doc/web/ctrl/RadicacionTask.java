package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.serv.ProcesoReinicioContadorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Componente que controla la ejecución de la tarea programada del reinicio de
 * las secuencias del numero de radicacion.
 *
 * @author edison.gonzalez@controltechcg.com
 * @since Nov 17, 2017
 * @version 1.0.0 (feature-138).
 */
@Component
public class RadicacionTask {

    /**
     * Log del componente.
     */
    private static final Logger log = LoggerFactory.getLogger(RadicacionTask.class);

    /**
     * Servicio del proceso de reinicio de los numeros de radicado.
     */
    @Autowired
    private ProcesoReinicioContadorService procesoReinicioContadorService;

    /**
     * Metodo que se encarga de ejecutar la tarea programada de reinicio de las
     * secuencias de los numeros de radicacion, basado en el parametro de configuracion
     * que se encuentra en el aplication.properties.
     */
    @Scheduled(cron = "${job.reinicioSecuenciasRadicacion.cron}")
    //0 0 0 1 1 ? Cada año el primero de enero a las 12:00 am
    public void reportCurrentTime() {
        log.info("Inicia el proceso de reinicio de secuencias de radicado...");
        procesoReinicioContadorService.reiniciarSecuenciasRadicacion();
        log.info("Termina el proceso de reinicio de secuencias de radicado...");
    }
}
