package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.ProcesoReinicioContDetalle;
import com.laamware.ejercito.doc.web.entity.ProcesoReinicioContador;
import com.laamware.ejercito.doc.web.entity.SecuenciaRadicacion;
import com.laamware.ejercito.doc.web.repo.ProcesoReinicioContDetalleRepository;
import com.laamware.ejercito.doc.web.repo.ProcesoReinicioContadorRepository;
import com.laamware.ejercito.doc.web.repo.SecuenciaRadicacionRepository;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para proceso de reinicio de secuencias de los numeros de radicacion.
 *
 * @author edison.gonzalez@controltechcg.com
 * @since Nov 17, 2017
 * @version 1.0.0 (feature-138).
 */
@Service
public class ProcesoReinicioContadorService {

    /**
     * Log del servicio.
     */
    private static final Logger log = LoggerFactory.getLogger(ProcesoReinicioContadorService.class);

    /**
     * Servicios del numero de radicado.
     */
    @Autowired
    private RadicadoService radicadoService;

    /**
     * Repositorios de las secuencias del numero de radicacion.
     */
    @Autowired
    private SecuenciaRadicacionRepository secuenciaRadicacionRepository;

    /**
     * Repositorios del encabezado del proceso de reinicio de secuencias.
     */
    @Autowired
    private ProcesoReinicioContadorRepository procesoReinicioContadorRepository;

    /**
     * Repositorios del detalle del proceso de reinicio de secuencias.
     */
    @Autowired
    private ProcesoReinicioContDetalleRepository procesoReinicioContDetalleRepository;

    /**
     * Metodo que se encarga de reiniciar las secuencias utilizadas en los
     * numeros de radicacion de los documentos.
     */
    @Transactional
    public void reiniciarSecuenciasRadicacion() {
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
            log.info("Iniciando el proceso en la maquina[" + hostname + "], IP[" + hostAddress + "]...");
        } else {
            log.info("Error obteniendo la dirección del servidor");
        }

        ProcesoReinicioContador prc = new ProcesoReinicioContador();
        prc.setFechaHoraEjecucion(new Date());
        prc.setIpEjecucion("[" + hostname + "] - [" + hostAddress + "]");
        procesoReinicioContadorRepository.save(prc);
        log.info("Guardando el encabezado del proceso de reinicio de las secuencias...");

        List<SecuenciaRadicacion> secuencias = secuenciaRadicacionRepository.findAll();
        for (SecuenciaRadicacion s : secuencias) {
            log.info("Reiniciando la secuencia [" + s.getSeqNombre() + "]...");
            ProcesoReinicioContDetalle prcd = new ProcesoReinicioContDetalle();
            prcd.setSecuencia(s);
            prcd.setSecuenciaNombre(s.getSeqNombre());
            prcd.setProReinicioContador(prc);
            radicadoService.reiniciarsecuencia(s.getSeqNombre(), prcd);

            procesoReinicioContDetalleRepository.save(prcd);
        }
    }

    /**
     * Metodo que verifica si el proceso de reinicio de la secuencias lo ejecuto
     * el servidor principal.
     *
     * @return Variable que identifica si el proceso se realizo.
     */
    public Boolean verificaProcesoServidorPrincipal() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int year = cal.get(Calendar.YEAR);
        List<ProcesoReinicioContador> list = procesoReinicioContadorRepository.findProcesoReinicioContadorporAño(year);
        return list.size() > 0;
    }
}
