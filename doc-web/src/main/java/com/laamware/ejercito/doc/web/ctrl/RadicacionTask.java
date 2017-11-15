package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.Radicacion;
import com.laamware.ejercito.doc.web.repo.RadicacionRepository;
import com.laamware.ejercito.doc.web.serv.RadicadoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    private RadicacionRepository radicacionRepository;

    @Scheduled(cron = "0 0 0 16 11 ?")
    //0 0 0 1 1 ? Cada año el primero de enero a las 12:00 am
    public void reportCurrentTime() {
        List<Radicacion> radicaciones = radicacionRepository.findAll();
        for (Radicacion s : radicaciones) {
            log.info("The time is now {}", dateFormat.format(new Date()) + "-----" + s.getSecuencia());
            radicadoService.reiniciarsecuencia(s.getSecuencia());
        }

        log.info("The time is now {}", dateFormat.format(new Date()));
    }
}
