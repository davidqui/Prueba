package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.laamware.ejercito.doc.web.entity.Clasificacion;
import org.springframework.data.jpa.repository.Query;

public interface ClasificacionRepository extends
        GenJpaRepository<Clasificacion, Integer> {

    List<Clasificacion> findByActivo(boolean b, Sort sort);

    /**
     * Busca la clasificación activa con el mínimo peso de orden.
     *
     * @return Clasificación con mínimo peso.
     */
    /*
     * 2017-09-18 jgarcia@controltechcg.com Issue #120 (SICDI-Controltech) 
     * feature-120: Presentar clasificación en acta de transferencia.
     */
    @Query(nativeQuery = true, value = ""
            + " SELECT "
            + "     * "
            + " FROM "
            + "     CLASIFICACION "
            + " WHERE "
            + "     CLA_ORDEN = (SELECT MIN(CLA_ORDEN) FROM CLASIFICACION WHERE ACTIVO = 1) "
            + "     AND ACTIVO = 1 "
            + "")
    public Clasificacion findMinOrderActivo();

    /**
     * Busca una clasificación activa por su ID.
     *
     * @param id ID de la clasificación.
     * @return Instancia de la clasificación correspondiente al ID, si y solo
     * si, esta se encuentra activa; de lo contrario, {@code null}.
     */
    /*
     * 2018-05-16 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public Clasificacion findOneByIdAndActivoTrue(Integer id);

}
