package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.TrdArchivoDocumentosDTO;
import com.laamware.ejercito.doc.web.entity.Cargo;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.util.NumeroVersionIdentificable;
import com.laamware.ejercito.doc.web.util.NumeroVersionIdentificableComparator;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
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
    private JdbcTemplate jdbcTemplate;

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
     * @return Lista de series TRD que para el usuario (y el cargo, en caso de
     * ser indicado) tienen registros de documentos archivados.
     */
    /*
     * 2018-04-24 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
     * feature-151: Definición de los métodos para presentación de la pantalla
     * de consulta de archivo a través del servicio de TRD.
     */
    public List<TrdArchivoDocumentosDTO> findAllSeriesWithArchivoByUsuarioAndCargo(final Usuario usuario, final Cargo cargo) {
        final String sql = ""
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
                + (cargo == null ? "" : "     AND documento_dependencia .cargo_id = ?\n")
                + "   GROUP BY serie.trd_id\n"
                + "   HAVING count(1) > 0) serie_documento_archivo ON (serie_documento_archivo.trd_id = trd.trd_id)"
                + "";

        final List<Object> params = new ArrayList<>();
        params.add(usuario.getDependencia().getId());
        params.add(usuario.getId());
        if (cargo != null) {
            params.add(cargo.getId());
        }

        final RowMapper<TrdArchivoDocumentosDTO> rowMapper = new BeanPropertyRowMapper<>(TrdArchivoDocumentosDTO.class);
        return jdbcTemplate.query(sql, params.toArray(), rowMapper);
    }
}
