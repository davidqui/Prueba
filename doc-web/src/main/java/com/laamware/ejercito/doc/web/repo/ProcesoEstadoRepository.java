
package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author egonzalezm
 * @since 1.8
 * @version 09/04/2018
 */
public interface ProcesoEstadoRepository extends JpaRepository<Estado, Integer> {

    @Query(nativeQuery = true, value = ""
            + "select * from PROCESO_ESTADO WHERE PRO_ID = :pro_id and pes_inicial = 1")
    public Estado findEstadoInicialByProId(@Param("pro_id") Integer pro_id);
}
