package com.laamware.ejercito.doc.web.serv;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laamware.ejercito.doc.web.repo.ParNombreExpedienteRepository;

/**
 * Servicio para las operaciones de los nombres de los expedientes.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @since Jul 27, 208 Issue #181 (SICDI-Controltech) feature-181
 */
@Service
public class ParNombreExpedienteService {

    /**
     * Repositorio de nombre de expedientes.
     */
    @Autowired
    private ParNombreExpedienteRepository parNombreExpedienteRepository;
}
