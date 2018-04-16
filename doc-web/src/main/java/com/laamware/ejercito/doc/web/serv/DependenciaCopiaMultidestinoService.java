package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Adjunto;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.DependenciaCopiaMultidestino;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DependenciaCopiaMultidestinoRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.BusinessLogicValidation;
import com.laamware.ejercito.doc.web.util.GeneralUtils;
import java.sql.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.sql.DataSource;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

/**
 * Servicio de lógica de negocio para {@link DependenciaCopiaMultidestino}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/10/2018 (SICDI-Controltech Issue #156 feature-156)
 */
@Service
public class DependenciaCopiaMultidestinoService {

    private static final Logger LOG = Logger.getLogger(DependenciaCopiaMultidestinoService.class.getName());

    private static final String PROC_COPIA_DOC_MULTIDESTINO = "PROC_COPIA_DOC_MULTIDESTINO";

    private static final String T_ARRAY_UUID = "T_ARRAY_UUID";

    private static final String P_ARRAY_UUID_DOC_ADJUNTO = "P_ARRAY_UUID_DOC_ADJUNTO";

    private static final String P_DOC_DOCX_DOCUMENTO = "P_DOC_DOCX_DOCUMENTO";

    private static final String P_DOC_CONTENT_FILE = "P_DOC_CONTENT_FILE";

    private static final String P_DEP_ID_DES = "P_DEP_ID_DES";

    private static final String P_DOC_ID_NUEVO = "P_DOC_ID_NUEVO";

    private static final String P_PIN_ID_NUEVO = "P_PIN_ID_NUEVO";

    private static final String P_DOC_ID_ORIGEN = "P_DOC_ID_ORIGEN";

    @Autowired
    private DependenciaCopiaMultidestinoRepository multidestinoRepository;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private DependenciaService dependenciaService;

    @Autowired
    private AdjuntoService adjuntoService;

    @Autowired
    private DataSource dataSource;

    /**
     * Indica si el documento es multi-destino.
     *
     * @param documento Documento.
     * @return {@code true} si el documento es multi-destino; de lo contrario,
     * {@code false}.
     */
    public boolean esDocumentoMultidestino(final Documento documento) {
        final Long count = multidestinoRepository.countByDocumentoOriginalAndActivoTrue(documento);
        return (count > 0);
    }

    /**
     * Lista todos los registros de dependencia copia multidestino activos para
     * un documento.
     *
     * @param documentoOriginal Documento original.
     * @return Lista de los registros activos de multidestino.
     */
    public List<DependenciaCopiaMultidestino> listarActivos(final Documento documentoOriginal) {
        return multidestinoRepository.findAllByDocumentoOriginalAndActivoTrue(documentoOriginal);
    }

    /**
     * Busca un registro activo para el documento y dependencia destino
     * indicados.
     *
     * @param documentoOriginal Documento original.
     * @param dependenciaDestino Dependencia destino.
     * @return Instancia del registro activo, o {@code null} en caso que no
     * exista para los parámetros indicados.
     */
    public DependenciaCopiaMultidestino buscarRegistroActivo(final Documento documentoOriginal, final Dependencia dependenciaDestino) {
        return multidestinoRepository.findByDocumentoOriginalAndDependenciaDestinoAndActivoTrue(documentoOriginal, dependenciaDestino);
    }

    /**
     * Crea un registro de copia multidestino.
     *
     * @param documentoOriginalID ID del documento original.
     * @param dependenciaDestinoID ID de la dependencia destino.
     * @param usuarioSesion Usuario en sesión.
     * @return Instancia del registro creado.
     * @throws BusinessLogicException En caso que no se cumpla alguna de las
     * reglas de negocio.
     */
    public DependenciaCopiaMultidestino crear(final String documentoOriginalID, final Integer dependenciaDestinoID, final Usuario usuarioSesion) throws BusinessLogicException {
        final Documento documentoOriginal = documentoRepository.findOne(documentoOriginalID);
        if (documentoOriginal == null) {
            throw new BusinessLogicException("El ID del documento original no es válido en el sistema.");
        }

        final Dependencia dependenciaDestino = dependenciaService.findOne(dependenciaDestinoID);
        if (dependenciaDestino == null) {
            throw new BusinessLogicException("El ID de la dependencia destino no es válido en el sistema.");
        }

        if (documentoOriginal.getDependenciaDestino().getId().equals(dependenciaDestino.getId())) {
            throw new BusinessLogicException("La dependencia seleccionada corresponde a la misma dependencia destino del documento original.");
        }

        final DependenciaCopiaMultidestino registroActual = multidestinoRepository.findByDocumentoOriginalAndDependenciaDestinoAndActivoTrue(documentoOriginal, dependenciaDestino);
        if (registroActual != null) {
            throw new BusinessLogicException("Ya existe un registro activo para el documento original y la dependencia destino seleccionados (" + registroActual.getId() + ").");
        }

        final Usuario jefeActivoDependenciaDestino = dependenciaService.getJefeActivoDependencia(dependenciaDestino);
        if (jefeActivoDependenciaDestino == null) {
            throw new BusinessLogicException("La dependencia no tiene un jefe activo asignado.");
        }

        DependenciaCopiaMultidestino copiaMultidestino = new DependenciaCopiaMultidestino(documentoOriginal, dependenciaDestino, usuarioSesion, new Date());
        return multidestinoRepository.saveAndFlush(copiaMultidestino);
    }

    /**
     * Elimina - de forma lógica - el registro.
     *
     * @param id ID del registro a eliminar.
     * @param usuarioSesion Usuario en sesión.
     * @throws BusinessLogicException En caso que no se cumpla alguna de las
     * reglas de negocio.
     */
    public void eliminar(final Integer id, final Usuario usuarioSesion) throws BusinessLogicException {
        DependenciaCopiaMultidestino copiaMultidestino = multidestinoRepository.findOne(id);
        if (copiaMultidestino == null) {
            throw new BusinessLogicException("El ID del registro no es válido en el sistema.");
        }

        if (!copiaMultidestino.getActivo()) {
            throw new BusinessLogicException("El registro ya se encuentra eliminado en el sistema.");
        }

        copiaMultidestino.setActivo(Boolean.FALSE);
        copiaMultidestino.setQuienMod(usuarioSesion);
        copiaMultidestino.setCuandoMod(new Date());

        multidestinoRepository.saveAndFlush(copiaMultidestino);
    }

    /**
     * Lista todos las dependencias destino de un documento multi-destino.
     *
     * @param documentoOriginal Documento original.
     * @return Lista con todas las dependencias (Unión de la dependencia destino
     * del documento con las dependencias destino de los registros de copia
     * multidestino).
     */
    public List<Dependencia> listarTodasDependenciasMultidestino(final Documento documentoOriginal) {
        final List<DependenciaCopiaMultidestino> copiaMultidestinos = listarActivos(documentoOriginal);

        final List<Dependencia> dependencias = new LinkedList<>();
        dependencias.add(documentoOriginal.getDependenciaDestino());

        for (final DependenciaCopiaMultidestino copiaMultidestino : copiaMultidestinos) {
            dependencias.add(copiaMultidestino.getDependenciaDestino());
        }

        return dependencias;
    }

    /**
     * Valida que todas las dependencias de un documento multi-destino (Original
     * y copias) cumplan las reglas de negocio precondiciones al procesamiento
     * de clonación de la información del documento original.
     *
     * @param dependenciasMultidestino Lista de todas las dependencias de un
     * documento multidestino.
     * @return Resultado de la validación realizada.
     */
    public BusinessLogicValidation validarTodasDependenciasMultidestino(final List<Dependencia> dependenciasMultidestino) {
        final BusinessLogicValidation validation = new BusinessLogicValidation();

        for (final Dependencia dependencia : dependenciasMultidestino) {
            final Usuario jefeActivoDependencia = dependenciaService.getJefeActivoDependencia(dependencia);
            if (jefeActivoDependencia == null) {
                validation.addError(dependencia, "La dependencia \"" + dependencia.getNombre() + "\" no tiene Jefe Activo.");
            }
        }

        return validation;
    }

    /**
     * Clona la información del documento original en nuevos documentos a partir
     * de sus registros de dependencias copia multidestino.
     *
     * @param documentoOriginal Documento original.
     * @throws java.sql.SQLException
     */
    public void clonarDocumentoMultidestino(final Documento documentoOriginal) throws SQLException {
        final List<DependenciaCopiaMultidestino> copiaMultidestinos = listarActivos(documentoOriginal);

        for (final DependenciaCopiaMultidestino copiaMultidestino : copiaMultidestinos) {
            clonarDocumentoMultidestino(documentoOriginal, copiaMultidestino);
        }
    }

    /**
     * Lista todos los documentos destino de un documento multi-destino.
     *
     * @param documentoOriginal Documento original.
     * @return Lista con todos los documentos (Unión del documento original con
     * los documentos generados por los registros de copia multidestino).
     */
    public List<Documento> listarTodosDocumentosMultidestino(final Documento documentoOriginal) {
        final List<DependenciaCopiaMultidestino> copiaMultidestinos = listarActivos(documentoOriginal);

        final List<Documento> documentos = new LinkedList<>();
        documentos.add(documentoOriginal);

        for (final DependenciaCopiaMultidestino copiaMultidestino : copiaMultidestinos) {
            documentos.add(copiaMultidestino.getDocumentoResultado());
        }

        return documentos;
    }

    /**
     * Clona la información del documento original en un nuevo documento basado
     * en el registro de dependencia copia multidestino.
     *
     * @param documentoOriginal Documento original.
     * @param copiaMultidestino Registro de dependencia copia multidestino.
     */
    private void clonarDocumentoMultidestino(final Documento documentoOriginal, final DependenciaCopiaMultidestino copiaMultidestino) throws SQLException {
        LOG.info("com.laamware.ejercito.doc.web.serv.DependenciaCopiaMultidestinoService.clonarDocumentoMultidestino()");

        final String p_doc_id_origen = documentoOriginal.getId();
        final String p_pin_id_nuevo = GeneralUtils.newId();
        final String p_doc_id_nuevo = GeneralUtils.newId();
        final Integer p_dep_id_des = copiaMultidestino.getDependenciaDestino().getId();
        /*
         * TODO: Verificar estos valores contra lo que debe estar en el OFS. 
         */
        final String p_doc_content_file = GeneralUtils.newId();
        final String p_doc_docx_documento = GeneralUtils.newId();

        final List<Adjunto> adjuntos = adjuntoService.findAllActivos(documentoOriginal);
        final String[] adjuntosUUIDs = GeneralUtils.generateUUIDs(adjuntos.size());

        // TODO: Enviar la información a la función PL/SQL del proceso de clonación.
        LOG.info("p_doc_id_origen=" + p_doc_id_origen + "\tp_pin_id_nuevo=" + p_pin_id_nuevo + "\tp_doc_id_nuevo=" + p_doc_id_nuevo
                + "\tp_dep_id_des=" + p_dep_id_des + "\tadjuntosUUIDs=" + Arrays.toString(adjuntosUUIDs));

        final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        LOG.info("jdbcTemplate = " + jdbcTemplate);

        final SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(PROC_COPIA_DOC_MULTIDESTINO);
        LOG.info("simpleJdbcCall = " + simpleJdbcCall);

        final ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor(T_ARRAY_UUID,
                simpleJdbcCall.getJdbcTemplate().getDataSource().getConnection());
        LOG.info("arrayDescriptor = " + arrayDescriptor);

        final Array p_array_uuid_doc_adjunto = new ARRAY(arrayDescriptor, simpleJdbcCall.getJdbcTemplate().getDataSource().getConnection(), adjuntosUUIDs);
        LOG.info("p_array_uuid_doc_adjunto = " + p_array_uuid_doc_adjunto);

        final SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue(P_DOC_ID_ORIGEN, p_doc_id_origen)
                .addValue(P_PIN_ID_NUEVO, p_pin_id_nuevo)
                .addValue(P_DOC_ID_NUEVO, p_doc_id_nuevo)
                .addValue(P_DEP_ID_DES, p_dep_id_des)
                .addValue(P_DOC_CONTENT_FILE, p_doc_content_file)
                .addValue(P_DOC_DOCX_DOCUMENTO, p_doc_docx_documento)
                .addValue(P_ARRAY_UUID_DOC_ADJUNTO, p_array_uuid_doc_adjunto);
        LOG.info("sqlParameterSource = " + sqlParameterSource);

        simpleJdbcCall.execute(sqlParameterSource);
        LOG.info("EJECUTADO PROCEDIMIENTO!");

        //
        // TODO: Aplicar los cambios necesarios sobre el OFS.
        //
        final Documento documentoResultado = new Documento();
        documentoResultado.setId(p_doc_id_nuevo);
        copiaMultidestino.setDocumentoResultado(documentoResultado);

        copiaMultidestino.setFechaHoraCreacionDocumentoResultado(new Date());

        multidestinoRepository.saveAndFlush(copiaMultidestino);
    }
}
