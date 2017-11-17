package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.ProcesoReinicioContDetalle;
import com.laamware.ejercito.doc.web.entity.ProcesoReinicioContador;
import com.laamware.ejercito.doc.web.entity.SecuenciaRadicacion;
import com.laamware.ejercito.doc.web.repo.ProcesoReinicioContDetalleRepository;
import com.laamware.ejercito.doc.web.repo.ProcesoReinicioContadorRepository;
import com.laamware.ejercito.doc.web.repo.SecuenciaRadicacionRepository;
import com.laamware.ejercito.doc.web.serv.RadicadoService;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author egonzalezm
 */
@Component
public class RadicacionTask {

    private static final Logger log = LoggerFactory.getLogger(RadicacionTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private RadicadoService radicadoService;

    @Autowired
    private SecuenciaRadicacionRepository secuenciaRadicacionRepository;

    @Autowired
    private ProcesoReinicioContadorRepository procesoReinicioContadorRepository;

    @Autowired
    private ProcesoReinicioContDetalleRepository procesoReinicioContDetalleRepository;

    @Scheduled(cron = "*/30 * * * * *")
    //0 0 0 1 1 ? Cada año el primero de enero a las 12:00 am
    public void reportCurrentTime() {
        log.info("Inicia el proceso de reinicio de secuencias de radicado...");

        InetAddress addr = null;
        String hostname = "";
        String hostAddress = "";

        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            log.error("Error encontrando la direccion IP de la maquina", ex);
        }
        
        if (addr != null) {
            hostname = addr.getHostName();
            hostAddress = addr.getHostAddress();
            log.info("Iniciando el proceso en la maquina[" + hostname + "], IP[" + hostAddress + "]");
        }

        ProcesoReinicioContador prc = new ProcesoReinicioContador();
        prc.setFechaHoraEjecucion(new Date());
        prc.setIpEjecucion(hostname + "-" + hostAddress);

        procesoReinicioContadorRepository.save(prc);

        List<SecuenciaRadicacion> secuencias = secuenciaRadicacionRepository.findAll();
        for (SecuenciaRadicacion s : secuencias) {

            ProcesoReinicioContDetalle prcd = new ProcesoReinicioContDetalle();
            prcd.setSecuencia(s);
            prcd.setSecuenciaNombre(s.getSeqNombre());
            prcd.setProReinicioContador(prc);
            log.info("The time is now {}", dateFormat.format(new Date()) + "-----" + s.getSeqNombre());
            radicadoService.reiniciarsecuencia(s.getSeqNombre(), prcd);

            procesoReinicioContDetalleRepository.save(prcd);
        }

        log.info("The time is now {}", dateFormat.format(new Date()));

    }
}
