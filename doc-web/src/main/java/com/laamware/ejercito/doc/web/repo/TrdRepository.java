package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Trd;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
     * Valida si el usuario tiene asignada la subserie TRD dentro de una serie
     * TRD específica.
     *
     * @param subserieTrdID ID de la subserie TRD.
     * @param serieTrdID ID de la serie TRD.
     * @param usuarioID ID del usuario.
     * @return 1 en caso que el usuario tenga asignada la subserie dentro de la
     * serie especificada de forma activa; de lo contrario, 0.
     */
    /*
     * 2018-05-16 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    @Query(nativeQuery = true, value = ""
            + "SELECT\n"
            + "  CASE\n"
            + "    WHEN COUNT(1) > 0 THEN 1\n"
            + "    ELSE 0\n"
            + "  END\n"
            + "FROM dependencia_trd\n"
            + "JOIN trd\n"
            + "  ON (\n"
            + "  trd.trd_id = dependencia_trd.trd_id\n"
            + "  )\n"
            + "JOIN dependencia\n"
            + "  ON (\n"
            + "  dependencia.dep_id = dependencia_trd.dep_id\n"
            + "  )\n"
            + "JOIN usuario\n"
            + "  ON (\n"
            + "  usuario.dep_id = dependencia.dep_id\n"
            + "  )\n"
            + "WHERE dependencia_trd.activo = 1\n"
            + "AND trd.activo = 1\n"
            + "AND trd.trd_id = :trd_id\n"
            + "AND trd.trd_serie = :trd_serie\n"
            + "AND dependencia.activo = 1\n"
            + "AND usuario.usu_id = :usu_id"
            + "")
    public BigInteger validateSubserieTrdForUser(@Param("trd_id") Integer subserieTrdID, @Param("trd_serie") Integer serieTrdID, @Param("usu_id") Integer usuarioID);

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
