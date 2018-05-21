package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Tipologia;
import com.laamware.ejercito.doc.web.repo.TipologiaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Servicio de lógica de negocio para {@link Tipologia}.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/15/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
@Service
public class TipologiaService {

    @Autowired
    private TipologiaRepository tipologiaRepository;

    /**
     * Lista las tipologías activas ordenadas por el nombre.
     *
     * @return Lista de tipologías activas, ordenadas por nombre.
     */
    public List<Tipologia> listarActivas() {
        return tipologiaRepository.findByActivo(true, new Sort(Sort.Direction.ASC, "nombre"));
    }

    /**
     * Busca una tipología por su ID.
     *
     * @param id ID.
     * @return Instancia de la tipología, o {@code null} en caso que no haya
     * correspondencia en el sistema.
     */
    public Tipologia find(final Integer id) {
        return tipologiaRepository.findOne(id);
    }

}
