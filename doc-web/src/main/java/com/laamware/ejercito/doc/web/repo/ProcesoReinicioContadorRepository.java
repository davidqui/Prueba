package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.ProcesoReinicioContador;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProcesoReinicioContadorRepository extends JpaRepository<ProcesoReinicioContador, Integer> {

    @Query(value = "select * from PROCESO_REINICIO_CONTADOR where to_char(FECHA_HORA_EJECUCION,'yyyy') = :year", nativeQuery = true)
    List<ProcesoReinicioContador> findProcesoReinicioContadorporAño(@Param("year") int year);
}
