package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Transicion;
import com.laamware.ejercito.doc.web.repo.TransicionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio de lógica de negocio para {@link Transicion}.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/18/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
@Service
public class TransicionService {

    @Autowired
    private TransicionRepository transicionRepository;

    /**
     * Busca una transición por el ID.
     *
     * @param id ID.
     * @return Instancia de la transición, o {@code null} si no hay
     * correspondencia.
     */
    public Transicion find(Integer id) {
        return transicionRepository.findOne(id);
    }

}
