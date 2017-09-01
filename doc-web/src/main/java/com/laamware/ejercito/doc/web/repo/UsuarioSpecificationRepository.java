package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jgarcia
 */
@Repository
public interface UsuarioSpecificationRepository extends JpaRepository<Usuario, Integer>, JpaSpecificationExecutor<Usuario> {

}
