package com.laamware.ejercito.doc.web.serv;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laamware.ejercito.doc.web.repo.ExpDocumentoRepository;


/**
 * Servicio para los documentos de los expedientes.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @since Jul 27, 208 Issue #181 (SICDI-Controltech) feature-181
 */
@Service
public class ExpDocumentoService {

    /**
     * Repositorio de los documentos de los expedientes.
     */
    @Autowired
    private ExpDocumentoRepository expDocumentoRepository;
}