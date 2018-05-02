package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.HistoricoDominio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de persistencia para {@link HistoricoDominio}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 05/02/2018 Issue #159 (SICDI-Controltech) feature-159
 */
@Repository
public interface HistoricoDominioRepository extends JpaRepository<HistoricoDominio, Integer> {

}
