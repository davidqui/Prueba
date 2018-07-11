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
import java.math.BigInteger;
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

    /*
     * 2018-07-11 samuel.delgado@controltechcg.com Issue #179 (SICDI-Controltech)
     * feature-179: Servicio de cache.
     */
    @Autowired
    private CacheService cacheService;
    //issue-179 constante llave del cache
    public final static String TRD_CACHE_KEY = "trd";
    
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
        /*
         * 2018-07-11 samuel.delgado@controltechcg.com Issue #179 (SICDI-Controltech)
         * feature-179: se agrega cache a ta respuesta.
         */
        List<Trd> listaTrds = (List<Trd>) cacheService.getKeyCache(TRD_CACHE_KEY+"_did-"+usuario.getDependencia().getId());
        System.out.println("TRD CACHE -- PID "+ listaTrds);
        if (listaTrds == null) {
            listaTrds = trdRepository.findSeriesByDependencia(usuario.getDependencia().getId());
            cacheService.setKeyCache(TRD_CACHE_KEY+"_did-"+usuario.getDependencia().getId(), listaTrds);
            System.out.println("TRD NOCACHE -- PID "+ listaTrds.toString());
        }
        return listaTrds;
    }

    public List<Trd> findSubseriesbySerieAndUsuario(Trd serie, Usuario usuario) {
        /*
         * 2018-07-11 samuel.delgado@controltechcg.com Issue #179 (SICDI-Controltech)
         * feature-179: se agrega cache a ta respuesta.
         */
        List<Trd> listaTrds = (List<Trd>) cacheService.getKeyCache(TRD_CACHE_KEY+"_did-"+usuario.getDependencia().getId()+"_sid-"+serie.getId());
        System.out.println("TRD CACHE -- PID "+ listaTrds);
        if (listaTrds == null) {
            listaTrds = trdRepository.findSubseries(serie.getId(), usuario.getDependencia().getId());
            cacheService.setKeyCache(TRD_CACHE_KEY+"_did-"+usuario.getDependencia().getId()+"_sid-"+serie.getId(), listaTrds);
            System.out.println("TRD NOCACHE -- PID "+ listaTrds.toString());
        }
        return listaTrds;
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
        /*
         * 2018-07-11 samuel.delgado@controltechcg.com Issue #179 (SICDI-Controltech)
         * feature-179: se agrega cache a ta respuesta.
         */
        List<Trd> subseries = (List<Trd>) cacheService.getKeyCache(TRD_CACHE_KEY);       
        System.out.println("TRD CACHE -- PID "+ subseries);
        if (subseries == null) {
            subseries = trdRepository.findAllByActivoTrueAndSerieNotNull();
            cacheService.setKeyCache(TRD_CACHE_KEY, subseries);
            System.out.println("TRD NOCACHE -- PID "+ subseries.toString());
        }
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

    /**
     * Valida si el usuario tiene asignada la subserie TRD dentro de una serie
     * TRD específica.
     *
     * @param subserieTrd Subserie TRD.
     * @param serieTrd Serie TRD.
     * @param usuario Usuario.
     * @return {@code true} en caso que el usuario tenga asignada la subserie
     * dentro de la serie especificada de forma activa; de lo contrario,
     * {@code false}.
     */
    /*
     * 2018-05-16 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public boolean validateSubserieTrdForUser(Trd subserieTrd, Trd serieTrd, Usuario usuario) {
        final BigInteger result = trdRepository.validateSubserieTrdForUser(subserieTrd.getId(), serieTrd.getId(), usuario.getId());
        return result.equals(BigInteger.ONE);
    }

}
