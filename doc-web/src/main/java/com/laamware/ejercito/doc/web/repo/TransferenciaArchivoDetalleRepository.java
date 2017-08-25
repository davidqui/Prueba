package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.TransferenciaArchivoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de persistencia para {@link TransferenciaArchivoDetalle}.
 *
 * @author jgarcia@controltechcg.com
 * @since Ago 25, 2017
 * @version 1.0.0 (feature-120).
 */
public interface TransferenciaArchivoDetalleRepository extends JpaRepository<TransferenciaArchivoDetalle, Integer> {

}
