package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Trd;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.Query;

public interface TrdRepository extends GenJpaRepository<Trd, Integer> {

    static final Sort DEFAULT_SORT = new Sort(Direction.ASC, "nombre");

    List<Trd> findBySerie(Integer serie, Sort sort);

    List<Trd> findByActivoAndSerieNotNull(Boolean activo, Sort sort);

    List<Trd> findByActivo(boolean b, Sort sort);

    List<Trd> findByActivoAndSerieNull(boolean b, Sort sort);

    Trd findByCodigo(String ser);

    Trd findById(Integer ser);

    @Query(nativeQuery = true, value = "select distinct serie.* from DEPENDENCIA_TRD dt join trd subserie on subserie.trd_id = dt.TRD_ID join trd serie on subserie.trd_serie = serie.trd_id where dt.DEP_ID = ? order by serie.TRD_NOMBRE")
    List<Trd> findSeriesByDependencia(Integer dependenciaId);

    @Query(nativeQuery = true, value = "SELECT distinct T.* FROM TRD T JOIN DEPENDENCIA_TRD DT ON DT.TRD_ID=T.TRD_ID WHERE TRD_SERIE=? AND DT.DEP_ID = ? AND DT.TRD_ID=T.TRD_ID order by T.TRD_NOMBRE, T.TRD_CODIGO")
    List<Trd> findSubseries(Integer serieId, Integer idDependencia);

    /**
     * Lista todas las subseries TRD activas.
     *
     * @return Lista de las subseries TRD activas.
     */
    /*
     * 2018-05-21 jgarcia@controltechcg.com Issue #170 (SICDI-Controltech)
     * feature-170.
     */
    public List<Trd> findAllByActivoTrueAndSerieNotNull();
}
