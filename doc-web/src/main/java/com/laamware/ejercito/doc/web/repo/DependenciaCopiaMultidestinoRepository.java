package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.DependenciaCopiaMultidestino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para {@link DependenciaCopiaMultidestino}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/10/2018 (SICDI-Controltech Issue #156)
 */
@Repository
public interface DependenciaCopiaMultidestinoRepository extends JpaRepository<DependenciaCopiaMultidestino, Integer> {

}
