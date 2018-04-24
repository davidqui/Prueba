package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Cargo;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.Query;

import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.entity.Usuario;
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
     * Obtiene la lista de todas las series TRD que tienen registros de
     * documentos archivados para el usuario.
     *
     * @param usuarioID ID del usuario.
     * @return Lista de todas las series TRD que tienen registros de documentos
     * archivados para el usuario.
     */
    /*
     * 2018-04-24 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
     * feature-151.
     */
    @Query(nativeQuery = true, value = ""
            + "SELECT *\n"
            + "FROM trd\n"
            + "JOIN\n"
            + "  (SELECT serie.trd_id,\n"
            + "          count(1)\n"
            + "   FROM trd serie\n"
            + "   JOIN trd subserie ON (subserie.trd_serie = serie.trd_id)\n"
            + "   JOIN documento_dependencia ON (documento_dependencia.trd_id = subserie.trd_id)\n"
            + "   WHERE serie.trd_serie IS NULL\n"
            + "     AND documento_dependencia.activo = 1\n"
            + "     AND documento_dependencia.quien = :usuario\n"
            + "   GROUP BY serie.trd_id\n"
            + "   HAVING count(1) > 0) series_documentos_archivo ON (series_documentos_archivo.trd_id = trd.trd_id)"
            + "")
    public List<Trd> findAllSeriesWithArchivoByUsuario(@Param("usuario") Integer usuarioID);

    /**
     * Obtiene la lista de todas las series TRD que tienen registros de
     * documentos archivados para el usuario y el cargo.
     *
     * @param usuarioID ID del usuario.
     * @param cargoID ID del cargo.
     * @return Lista de todas las series TRD que tienen registros de documentos
     * archivados para el usuario y el cargo.
     */
    /*
     * 2018-04-24 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
     * feature-151.
     */
    @Query(nativeQuery = true, value = ""
            + "SELECT *\n"
            + "FROM trd\n"
            + "JOIN\n"
            + "  (SELECT serie.trd_id,\n"
            + "          count(1)\n"
            + "   FROM trd serie\n"
            + "   JOIN trd subserie ON (subserie.trd_serie = serie.trd_id)\n"
            + "   JOIN documento_dependencia ON (documento_dependencia.trd_id = subserie.trd_id)\n"
            + "   WHERE serie.trd_serie IS NULL\n"
            + "     AND documento_dependencia.activo = 1\n"
            + "     AND documento_dependencia.quien = :usuario\n"
            + "     AND documento_dependencia.cargo_id = :cargo\n"
            + "   GROUP BY serie.trd_id\n"
            + "   HAVING count(1) > 0) series_documentos_archivo ON (series_documentos_archivo.trd_id = trd.trd_id)"
            + "")
    public List<Trd> findAllSeriesWithArchivoByUsuarioAndCargo(@Param("usuario") Integer usuarioID, @Param("cargo") Integer cargoID);

}
