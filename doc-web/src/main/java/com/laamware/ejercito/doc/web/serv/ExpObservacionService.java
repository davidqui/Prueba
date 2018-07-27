package com.laamware.ejercito.doc.web.serv;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laamware.ejercito.doc.web.repo.ExpObservacionRepository;


/**
 * Servicio para las observaciones de los expedientes.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @since Jul 27, 208 Issue #181 (SICDI-Controltech) feature-181
 */
@Service
public class ExpObservacionService {

    /**
     * Repositorio de las observaciones de los expedientes.
     */
    @Autowired
    private ExpObservacionRepository expObservacionRepository;
}
