package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.FileValidationDTO;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

/**
 * Servicio para procesos de validación de archivos externos sobre archivos del
 * OFS del sistema.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 05/08/2018 Issue #160 (SICDI-Controltech) feature-160
 */
@Service
public class FileValidationService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private OFS ofs;

    /**
     * Valida si el arreglo de bytes del archivo a validar corresponde al
     * arreglo de bytes del documento firmado y enviado con el UUID indicado.
     *
     * @param archivoValidarBytes Arreglo de bytes del archivo a validar.
     * @param docFirmaEnvioUUID UUID de firma y envío del documento en el OFS a
     * comparar.
     * @return DTO con los resultados del proceso de validación.
     * @throws IOException En caso de error de E/S.
     */
    public FileValidationDTO isValid(final byte[] archivoValidarBytes, final String docFirmaEnvioUUID) throws IOException {
        final Documento documento = documentoRepository.findOneByFirmaEnvioUUID(docFirmaEnvioUUID);
        if (documento == null) {
            return new FileValidationDTO(false, "No hay documento con UUID: " + docFirmaEnvioUUID);
        }

        final OFSEntry ofsEntry = ofs.read(documento.getPdf());
        final byte[] documentoOFSBytes = ofsEntry.getContent();

        final String noMatchMessage = "El archivo cargado no corresponde al archivo generado en SICDI con UUID: " + docFirmaEnvioUUID;

        if (archivoValidarBytes.length != documentoOFSBytes.length) {
            return new FileValidationDTO(false, noMatchMessage);
        }

        for (int index = 0; index < archivoValidarBytes.length; index++) {
            if (archivoValidarBytes[index] != documentoOFSBytes[index]) {
                return new FileValidationDTO(false, noMatchMessage);
            }
        }

        final List<Object> params = new LinkedList<>();
        params.add(docFirmaEnvioUUID);

        final String sql = "SELECT *\n"
                + "FROM\n"
                + "  (SELECT DISTINCT instancia.pin_id \"procesoInstanciaID\",\n"
                + "                   doc.doc_id \"documentoID\",\n"
                + "                   doc.doc_asunto \"asuntoDocumento\",\n"
                + "                   doc.cuando \"fechaCreacion\",\n"
                + "                   proceso.pro_nombre \"nombreProceso\",\n"
                + "                   est.pes_nombre \"nombreEstado\",\n"
                + "                   DECODE( usu_elabora.usu_nombre, NULL, NULL, usu_elabora.usu_grado || '. ' || usu_elabora.usu_nombre ) \"nombreUsuarioCreador\",\n"
                + "                   DECODE( usu_firma.usu_nombre, NULL, NULL, usu_firma.usu_grado || '. ' || usu_firma.usu_nombre ) \"nombreUsuarioFirmaYEnvio\",\n"
                + "                   clasificacion.cla_nombre \"nombreClasificacion\",\n"
                + "                   doc.doc_radicado \"numeroRadicado\",\n"
                + "                   dep_origen.dep_ori_nombre \"unidadOrigen\",\n"
                + "                   dep_destino.dep_des_nombre \"unidadDestino\",\n"
                + "                   docfirma.cuando \"fechaFirmaEnvio\"\n"
                + "   FROM documento doc\n"
                + "   LEFT JOIN usuario usu_ult_accion ON ( doc.usu_id_ultima_accion = usu_ult_accion.usu_id )\n"
                + "   LEFT JOIN dependencia dep ON ( doc.dep_id_des = dep.dep_id )\n"
                + "   LEFT JOIN usuario usu_dep_jefe ON ( dep.usu_id_jefe = usu_dep_jefe.usu_id )\n"
                + "   LEFT JOIN proceso_instancia instancia ON ( doc.pin_id = instancia.pin_id )\n"
                + "   LEFT JOIN documento_usu_firma docfirma ON ( doc.doc_id = docfirma.doc_id )\n"
                + "   LEFT JOIN s_instancia_usuario hpin ON ( doc.pin_id = hpin.pin_id )\n"
                + "   LEFT JOIN usuario usu ON ( hpin.usu_id = usu.usu_id )\n"
                + "   LEFT JOIN proceso_estado est ON ( est.pes_id = instancia.pes_id )\n"
                + "   LEFT JOIN proceso proceso ON ( instancia.pro_id = proceso.pro_id )\n"
                + "   LEFT JOIN usuario usu_asignado ON ( instancia.usu_id_asignado = usu_asignado.usu_id )\n"
                + "   LEFT JOIN usuario usu_elabora ON ( doc.usu_id_elabora = usu_elabora.usu_id )\n"
                + "   LEFT JOIN usuario usu_reviso ON ( doc.usu_id_aprueba = usu_reviso.usu_id )\n"
                + "   LEFT JOIN usuario usu_vbueno ON ( doc.usu_id_visto_bueno = usu_vbueno.usu_id )\n"
                + "   LEFT JOIN usuario usu_firma ON ( doc.usu_id_firma = usu_firma.usu_id )\n"
                + "   LEFT JOIN clasificacion ON ( doc.cla_id = clasificacion.cla_id )\n"
                + "   LEFT JOIN\n"
                + "     ( SELECT dep_ori_id,\n"
                + "              dep_ori_nombre,\n"
                + "              dep_id\n"
                + "      FROM\n"
                + "        ( SELECT FIRST_VALUE( dep_ori_id ) OVER(PARTITION BY dep_id\n"
                + "                                                ORDER BY row_num ASC ) dep_ori_id,\n"
                + "                                           FIRST_VALUE( dep_ori_nombre ) OVER(PARTITION BY dep_id\n"
                + "                                                                              ORDER BY row_num ASC ) dep_ori_nombre,\n"
                + "                                                                         dep_id\n"
                + "         FROM\n"
                + "           ( SELECT LEVEL row_num,\n"
                + "                          CONNECT_BY_ROOT dep_id AS dep_ori_id,\n"
                + "                          CONNECT_BY_ROOT dep_sigla AS dep_ori_nombre,\n"
                + "                          dep_id\n"
                + "            FROM dependencia\n"
                + "            WHERE ( CONNECT_BY_ROOT dep_ind_envio_documentos = 1\n"
                + "                   OR CONNECT_BY_ROOT dep_padre IS NULL ) CONNECT BY dep_padre =\n"
                + "              PRIOR dep_id ) )\n"
                + "      GROUP BY dep_ori_id,\n"
                + "               dep_ori_nombre,\n"
                + "               dep_id ) dep_origen ON ( dep_origen.dep_id = usu_elabora.dep_id )\n"
                + "   LEFT JOIN\n"
                + "     ( SELECT dep_ori_id,\n"
                + "              dep_des_nombre,\n"
                + "              dep_id\n"
                + "      FROM\n"
                + "        ( SELECT FIRST_VALUE( dep_ori_id ) OVER(PARTITION BY dep_id\n"
                + "                                                ORDER BY row_num ASC ) dep_ori_id,\n"
                + "                                           FIRST_VALUE( dep_des_nombre ) OVER(PARTITION BY dep_id\n"
                + "                                                                              ORDER BY row_num ASC ) dep_des_nombre,\n"
                + "                                                                         dep_id\n"
                + "         FROM\n"
                + "           ( SELECT LEVEL row_num,\n"
                + "                          CONNECT_BY_ROOT dep_id AS dep_ori_id,\n"
                + "                          CONNECT_BY_ROOT dep_sigla AS dep_des_nombre,\n"
                + "                          dep_id\n"
                + "            FROM dependencia\n"
                + "            WHERE ( CONNECT_BY_ROOT dep_ind_envio_documentos = 1\n"
                + "                   OR CONNECT_BY_ROOT dep_padre IS NULL ) CONNECT BY dep_padre =\n"
                + "              PRIOR dep_id ) )\n"
                + "      GROUP BY dep_ori_id,\n"
                + "               dep_des_nombre,\n"
                + "               dep_id ) dep_destino ON ( dep_destino.dep_id = doc.dep_id_des )\n"
                + "   WHERE doc.doc_firma_envio_uuid = ? \n"
                + "     AND docfirma.usu_id_firma = usu_firma.usu_id\n"
                + "   ORDER BY docfirma.cuando DESC)\n"
                + "WHERE rownum = 1";

        final RowMapper<FileValidationDTO> rowMapper = new BeanPropertyRowMapper<>(FileValidationDTO.class);
        final List<FileValidationDTO> list = jdbcTemplate.query(sql, params.toArray(), rowMapper);

        final String message = "El archivo cargado corresponde al archivo generado en SICDI con UUID: " + docFirmaEnvioUUID;

        final FileValidationDTO fileValidation = list.get(0);
        fileValidation.setValid(true);
        fileValidation.setMessage(message);
        return fileValidation;
    }

}
