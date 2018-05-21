package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.TrdArchivoDocumentosDTO;
import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.Trd;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.TrdRepository;
import com.laamware.ejercito.doc.web.util.DateUtil;
import com.laamware.ejercito.doc.web.util.NumeroVersionIdentificable;
import com.laamware.ejercito.doc.web.util.NumeroVersionIdentificableComparator;
import java.util.ArrayList;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Servicio para las operaciones de las TRD.
 *
 * @author jgarcia@controltechcg.com
 * @since May 15, 2017
 */
// 2017-05-15 jgarcia@controltechcg.com Issue #80 (SICDI-Controltech) feature-80
@Service
public class TRDService {

    @Autowired
    private TrdRepository trdRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Busca una TRD por su ID.
     *
     * @param id ID.
     * @return Instancia de la TRD, o {@code null} en caso que no haya
     * correspondencia en el sistema.
     */
    /*
     * 2018-04-25 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
     * feature-151.
     */
    public Trd findOne(final Integer id) {
        return trdRepository.findOne(id);
    }

    /**
     * Ordena la lista de TRD por el código, en ordenamiento tipo número de
     * versión.
     *
     * @param identificables Lista de TRD.
     */
    /*
     * 2018-04-25 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
     * feature-151: Cambio de parámetro para que acepte listas de cualquier
     * objeto que se identifica con número de versión.
     */
    public void ordenarPorCodigo(List<? extends NumeroVersionIdentificable> identificables) {
        Collections.sort(identificables, new NumeroVersionIdentificableComparator());
    }

    /**
     * Obtiene la lista de series TRD que contienen documentos archivados para
     * el usuario, según el cargo indicado, en las TRD disponibles para la
     * dependencia del usuario.
     *
     * @param usuario Usuario. Obligatorio.
     * @param cargo Cargo. Opcional.
     * @param anyo Año. Opcional.
     * @return Lista de series TRD que para el usuario (y el cargo, en caso de
     * ser indicado) tienen registros de documentos archivados.
     */
    /*
     * 2018-04-24 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
     * feature-151: Definición de los métodos para presentación de la pantalla
     * de consulta de archivo a través del servicio de TRD.
     */
    public List<TrdArchivoDocumentosDTO> findAllSeriesWithArchivoByUsuarioAndCargoAndAnyo(final Usuario usuario, final Cargo cargo, final Integer anyo) {
        String sql = ""
                + "SELECT trd.trd_id AS \"id\",\n"
                + "       trd.trd_codigo AS \"codigo\",\n"
                + "       trd.trd_nombre AS \"nombre\",\n"
                + "       serie_documento_archivo.serie_count AS \"numeroDocumentosArchivados\"\n"
                + "FROM trd\n"
                + "JOIN\n"
                + "  (SELECT serie.trd_id,\n"
                + "          count(1) AS serie_count\n"
                + "   FROM trd serie\n"
                + "   JOIN trd subserie ON (subserie.trd_serie = serie.trd_id)\n"
                + "   JOIN dependencia_trd ON (dependencia_trd.trd_id = subserie.trd_id)\n"
                + "   JOIN documento_dependencia ON (documento_dependencia.trd_id = subserie.trd_id)\n"
                + "   WHERE serie.activo = 1\n"
                + "     AND serie.trd_serie IS NULL\n"
                + "     AND subserie.activo = 1\n"
                + "     AND dependencia_trd.activo = 1\n"
                + "     AND dependencia_trd.dep_id = ?\n"
                + "     AND documento_dependencia.activo = 1\n"
                + "     AND documento_dependencia.quien = ?\n"
                + (cargo == null ? "" : "     AND documento_dependencia .cargo_id = ?\n");
        /*
         * 2018-05-04 jgarcia@controltechcg.com Issue #157 (SICDI-Controltech)
         * feature-157: Adición del filtro de año.
         */
        if (anyo != null && anyo != 0) {
            sql += " AND documento_dependencia.cuando BETWEEN ? AND ? \n";
        }

        sql += "   GROUP BY serie.trd_id\n"
                + "   HAVING count(1) > 0) serie_documento_archivo ON (serie_documento_archivo.trd_id = trd.trd_id)"
                + "";

        final List<Object> params = new ArrayList<>();
        params.add(usuario.getDependencia().getId());
        params.add(usuario.getId());

        if (cargo != null) {
            params.add(cargo.getId());
        }

        /*
         * 2018-05-04 jgarcia@controltechcg.com Issue #157 (SICDI-Controltech)
         * feature-157: Adición del filtro de año.
         */
        if (anyo != null && anyo != 0) {
            params.add(DateUtil.getMinDateOfMonth(Calendar.JANUARY, anyo));
            params.add(DateUtil.getMaxDateOfMonth(Calendar.DECEMBER, anyo));
        }

        final RowMapper<TrdArchivoDocumentosDTO> rowMapper = new BeanPropertyRowMapper<>(TrdArchivoDocumentosDTO.class);
        return jdbcTemplate.query(sql, params.toArray(), rowMapper);
    }

    /**
     * Obtiene la lista de subseries TRD que contienen documentos archivados
     * para el usuario, según la serie y el cargo indicado, en las TRD
     * disponibles para la dependencia del usuario.
     *
     * @param serie TRD serie. Obligatorio.
     * @param usuario Usuario. Obligatorio.
     * @param cargo Cargo. Opcional.
     * @param anyo Año. Opcional.
     * @return Lista de subseries TRD que para el usuario, la serie, (y el
     * cargo, en caso de ser indicado) tienen registros de documentos
     * archivados.
     */
    /*
     * 2018-04-25 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
     * feature-151: Definición de los métodos para presentación de la pantalla
     * de consulta de archivo a través del servicio de TRD.
     */
    public List<TrdArchivoDocumentosDTO> findAllSubseriesWithArchivoBySerieAndUsuarioAndCargoAndAnyo(final Trd serie, final Usuario usuario, final Cargo cargo, final Integer anyo) {
        String sql = ""
                + "SELECT trd.trd_id AS \"id\",\n"
                + "       trd.trd_codigo AS \"codigo\",\n"
                + "       trd.trd_nombre AS \"nombre\",\n"
                + "       subserie_documento_archivo.subserie_count AS \"numeroDocumentosArchivados\"\n"
                + "FROM trd\n"
                + "JOIN\n"
                + "  (SELECT subserie.trd_id,\n"
                + "          count(1) AS subserie_count\n"
                + "   FROM trd subserie\n"
                + "   JOIN dependencia_trd ON (dependencia_trd.trd_id = subserie.trd_id)\n"
                + "   JOIN documento_dependencia ON (documento_dependencia.trd_id = subserie.trd_id)\n"
                + "   WHERE subserie.activo = 1\n"
                + "     AND subserie.trd_serie = ?\n"
                + "     AND dependencia_trd.activo = 1\n"
                + "     AND dependencia_trd.dep_id = ?\n"
                + "     AND documento_dependencia.activo = 1\n"
                + "     AND documento_dependencia.quien = ?\n"
                + (cargo == null ? "" : "     AND documento_dependencia .cargo_id = ?\n");
        /*
         * 2018-05-04 jgarcia@controltechcg.com Issue #157 (SICDI-Controltech)
         * feature-157: Adición del filtro de año.
         */
        if (anyo != null && anyo != 0) {
            sql += " AND documento_dependencia.cuando BETWEEN ? AND ? \n";
        }

        sql += "   GROUP BY subserie.trd_id\n"
                + "   HAVING count(1) > 0) subserie_documento_archivo ON (subserie_documento_archivo.trd_id = trd.trd_id)"
                + "";

        final List<Object> params = new ArrayList<>();
        params.add(serie.getId());
        params.add(usuario.getDependencia().getId());
        params.add(usuario.getId());

        if (cargo != null) {
            params.add(cargo.getId());
        }

        /*
         * 2018-05-04 jgarcia@controltechcg.com Issue #157 (SICDI-Controltech)
         * feature-157: Adición del filtro de año.
         */
        if (anyo != null && anyo != 0) {
            params.add(DateUtil.getMinDateOfMonth(Calendar.JANUARY, anyo));
            params.add(DateUtil.getMaxDateOfMonth(Calendar.DECEMBER, anyo));
        }

        final RowMapper<TrdArchivoDocumentosDTO> rowMapper = new BeanPropertyRowMapper<>(TrdArchivoDocumentosDTO.class);
        return jdbcTemplate.query(sql, params.toArray(), rowMapper);
    }

    public List<Trd> findSeriesByUsuario(Usuario usuario) {
        return trdRepository.findSeriesByDependencia(usuario.getDependencia().getId());
    }

    public List<Trd> findSubseriesbySerieAndUsuario(Trd serie, Usuario usuario) {
        return trdRepository.findSubseries(serie.getId(), usuario.getDependencia().getId());
    }

    /**
     * Lista todas las subseries TRD activas.
     *
     * @return Lista de las subseries TRD activas, ordenadas por el código de la
     * TRD.
     */
    /*
     * 2018-05-21 jgarcia@controltechcg.com Issue #170 (SICDI-Controltech)
     * feature-170.
     */
    public List<Trd> findAllSubseriesActivas() {
        final List<Trd> subseries = trdRepository.findAllByActivoTrueAndSerieNotNull();
        ordenarPorCodigo(subseries);
        return subseries;
    }

    /**
     * Lista todas las subseries TRD activas.
     *
     * @return Lista de las subseries TRD activas, ordenadas por el nombre de la
     * TRD.
     */
    /*
     * 2018-05-21 jgarcia@controltechcg.com Issue #170 (SICDI-Controltech)
     * feature-170.
     */
    public List<Trd> findAllSubseriesActivasOrdenPorNombre() {
        return trdRepository.findByActivoAndSerieNotNull(Boolean.TRUE, (new Sort(Direction.ASC, "nombre")));
    }

}
