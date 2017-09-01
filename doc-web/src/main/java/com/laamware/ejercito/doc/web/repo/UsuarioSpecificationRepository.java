package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA con manejo de especificaciones para {@link Usuario}.
 *
 * @author jgarcia@controltechcg.com
 * @since Sep 01, 2017
 * @version 1.0.0 (feature-120).
 */
@Repository
public interface UsuarioSpecificationRepository extends JpaRepository<Usuario, Integer>, JpaSpecificationExecutor<Usuario> {

}
