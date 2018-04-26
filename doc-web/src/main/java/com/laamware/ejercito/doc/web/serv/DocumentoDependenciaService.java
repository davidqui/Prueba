package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.DocumentoDependenciaArchivoDTO;
import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.entity.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

/**
 * Servicio de lógica de negocio para {@link DocumentoDependencia}. NOTA: Las
 * operaciones de archivo automático se encuentran en el servicio
 * {@link ArchivoAutomaticoService}. Las operaciones de transferencia de archivo
 * se encuentran en el servicio {@link TransferenciaArchivoService}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/25/2018 Issue #151 (SICDI-Controltech) feature-151
 */
@Service
public class DocumentoDependenciaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Obtiene la lista de registros de archivo (DTOs) para la pantalla de
     * archivo, según la subserie, el usuario (y su dependencia), y el cargo (si
     * este es indicado).
     *
     * @param subserie Subserie TRD. Obligatorio.
     * @param usuario Usuario. Obligatorio.
     * @param cargo Cargo.
     * @return Lista de registros de archivo para los parámetros indicados.
     */
    public List<DocumentoDependenciaArchivoDTO> findAllBySubserieAndUsuarioAndCargo(final Trd subserie, final Usuario usuario, final Cargo cargo) {
        final String sql = ""
                + "SELECT documento.doc_radicado AS \"numeroRadicado\",\n"
                + "       documento.pin_id AS \"procesoInstanciaID\",\n"
                + "       documento.doc_asunto AS \"documentoAsunto\",\n"
                + "       documento.cuando AS \"fechaCreacionDocumento\",\n"
                + "       documento_dependencia.cuando AS \"fechaCreacionArchivo\",\n"
                + "       cargo.car_nombre AS \"cargoNombre\"\n"
                + "FROM documento_dependencia\n"
                + "JOIN dependencia_trd ON (dependencia_trd.dep_id = documento_dependencia.dep_id\n"
                + "                         AND dependencia_trd.trd_id = documento_dependencia.trd_id)\n"
                + "JOIN documento ON (documento.doc_id = documento_dependencia.doc_id)\n"
                + "LEFT JOIN cargo ON (cargo.car_id = documento_dependencia.cargo_id)\n"
                + "WHERE documento_dependencia.activo = 1\n"
                + "  AND documento_dependencia.trd_id = ?\n"
                + "  AND documento_dependencia.quien = ?\n"
                + (cargo == null ? "" : "     AND documento_dependencia .cargo_id = ?\n")
                + "  AND dependencia_trd.activo = 1\n"
                + "  AND dependencia_trd.dep_id = ?\n"
                + "ORDER BY documento_dependencia.cuando DESC"
                + "";

        final List<Object> params = new ArrayList<>();
        params.add(subserie.getId());
        params.add(usuario.getId());
        if (cargo != null) {
            params.add(cargo.getId());
        }
        params.add(usuario.getDependencia().getId());

        final RowMapper<DocumentoDependenciaArchivoDTO> rowMapper = new BeanPropertyRowMapper<>(DocumentoDependenciaArchivoDTO.class);
        return jdbcTemplate.query(sql, params.toArray(), rowMapper);
    }
}