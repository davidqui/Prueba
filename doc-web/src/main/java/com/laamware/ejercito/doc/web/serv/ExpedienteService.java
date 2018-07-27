package com.laamware.ejercito.doc.web.serv;


import com.laamware.ejercito.doc.web.repo.ExpedienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Servicio para las operaciones de los expedientes.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @since Jul 27, 208 Issue #181 (SICDI-Controltech) feature-181
 */
@Service
public class ExpedienteService {

    /**
     * Repositorio de expedientes.
     */
    @Autowired
    private ExpedienteRepository expedienteRepository;
}
