package com.laamware.ejercito.doc.web.serv;


import com.laamware.ejercito.doc.web.repo.ExpedienteTransicionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Servicio para las operaciones de las transiciones de los expedientes.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @since Jul 27, 208 Issue #181 (SICDI-Controltech) feature-181
 */
@Service
public class ExpedienteTransicionService {

    /**
     * Repositorio de transiciones de expedientes.
     */
    @Autowired
    private ExpedienteTransicionRepository expedienteEstadoRepository;
}
