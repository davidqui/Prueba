package com.laamware.ejercito.doc.web.serv;

import com.aspose.words.Cell;
import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.Row;
import com.aspose.words.Run;
import com.aspose.words.Table;
import com.laamware.ejercito.doc.web.dto.KeysValuesAsposeDocxDTO;
import com.laamware.ejercito.doc.web.dto.TransferenciaArchivoValidacionDTO;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Clasificacion;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.Grados;
import com.laamware.ejercito.doc.web.entity.PlantillaTransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivoDetalle;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.ClasificacionRepository;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoDependenciaRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.GradosRepository;
import com.laamware.ejercito.doc.web.repo.PlantillaTransferenciaArchivoRepository;
import com.laamware.ejercito.doc.web.repo.TransferenciaArchivoDetalleRepository;
import com.laamware.ejercito.doc.web.repo.TransferenciaArchivoRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import com.laamware.ejercito.doc.web.util.NumeroVersionComparator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sql.DataSource;
import net.sourceforge.jbarcodebean.JBarcodeBean;
import net.sourceforge.jbarcodebean.model.Interleaved25;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio con las reglas de negocio para el proceso de transferencia de
 * archivo.
 *
 * @author jgarcia@controltechcg.com
 * @since Ago 28, 2017
 * @version 1.0.0 (feature-120).
 */
@Service
public class TransferenciaArchivoService {

    /**
     * Logger.
     */
    private static final Logger LOG
            = Logger.getLogger(TransferenciaArchivoService.class.getName());

    /**
     * Locale para Colombia.
     */
    private static final Locale LOCALE_ES_CO = new Locale("es", "CO");

    /**
     * Formato de fecha completa.
     */
    private static final String FULL_DATE_FORMAT_PATTERN = "dd 'de' MMMM 'de' yyyy hh:mm a";

    /**
     * Formato de fecha para presentar en el acta.
     */
    private static final String DOCUMENT_DATE_FORMAT_PATTERN = "dd 'de' MMMM 'de' yyyy";

    /**
     * Formato de fecha de firma de documento transferido.
     */
    private static final String DETALLE_DOCUMENTO_FIRMA_DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    /**
     * Prefijo de línea de mando.
     */
    private static final String LINEA_MANDO_PREFIX = "MDN-CGFM-COEJC-SECEJ";

    /**
     * Separador de línea de mando.
     */
    private static final String LINEA_MANDO_SEPARATOR = "-";

    /**
     * Datasource del sistema.
     */
    @Autowired
    private DataSource dataSource;

    /**
     * Servicio de OFS.
     */
    @Autowired
    private OFS ofs;

    /**
     * Repositorio de maestro de transferencia.
     */
    @Autowired
    private TransferenciaArchivoRepository transferenciaRepository;

    /**
     * Repositorio de detalle de transferencia.
     */
    @Autowired
    private TransferenciaArchivoDetalleRepository detalleRepository;

    /**
     * Repositorio de plantilla de transferencia.
     */
    @Autowired
    private PlantillaTransferenciaArchivoRepository plantillaRepository;

    /**
     * Repositorio de registros de archivo.
     */
    @Autowired
    private DocumentoDependenciaRepository documentoDependenciaRepository;

    /**
     * Repositorio de grados.
     */
    @Autowired
    private GradosRepository gradoRepository;

    /**
     * Repositorio de usuario.
     */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Repositorio de clasificaciones.
     */
    @Autowired
    private ClasificacionRepository clasificacionRepository;

    /**
     * Repositorio de dependencias.
     */
    @Autowired
    private DependenciaRepository dependenciaRepository;

    /**
     * Repositorio de documentos.
     */
    @Autowired
    private DocumentoRepository documentoRepository;

    /**
     * Servicio de generacion de numero de radicado.
     */
    @Autowired
    RadicadoService radicadoService;

    /**
     * Busca un registro de transferencia de archivo.
     *
     * @param id ID del registro.
     * @return Transferencia.
     */
    public TransferenciaArchivo findOneTransferenciaArchivo(Integer id) {
        return transferenciaRepository.findOne(id);
    }

    /**
     * Obtiene la lista de todos los registros de archivo activos para un
     * usuario.
     *
     * @param usuarioID ID del usuario.
     * @return Lista de todos los registros de archivo activos.
     */
    public List<DocumentoDependencia> findAllArchivoActivoByUsuario(Integer usuarioID) {
        return documentoDependenciaRepository.findAllActivoByUsuario(usuarioID);
    }

    /**
     * Obtiene la cuenta de todos los documentos archivados activos para un
     * usuario.
     *
     * @param usuarioID ID del usuario.
     * @return Cuenta de documentos activos archivados.
     */
    public Integer countAllArchivoActivoByUsuario(Integer usuarioID) {
        return documentoDependenciaRepository.countAllActivoByUsuario(usuarioID);
    }

    /**
     * Obtiene todas las transferencias de archivo activas en estado
     * {@link TransferenciaArchivo#APROBADA_ESTADO} para un usuario destino,
     * ordenadas por la fecha de aprobación.
     *
     * @param destinoUsuario ID del usuario destino.
     * @return Lista de transferencias.
     */
    public List<TransferenciaArchivo> findAllRecibidasActivasByDestinoUsuario(
            final Integer destinoUsuario) {
        return transferenciaRepository
                .findAllRecibidasActivasByDestinoUsuario(destinoUsuario);
    }

    /**
     * Indica si hay plantilla activa para el acta de la transferencia.
     *
     * @return {@code true} si hay plantilla activa; de lo contrario,
     * {@code false}.
     */
    public Boolean hayPlantillaActiva() {
        return plantillaRepository.findByActivoTrue() != null;
    }

    /**
     * Valida los parámetros de entrada el proceso de transferencia.
     *
     * @param origenUsuario Usuario origen de la transferencia.
     * @param destinoUsuario Usuario destino de la transferencia.
     * @param tipoTransferencia Tipo de transferencia.
     * @param transferenciaAnterior Registro maestro de transferencia anterior a
     * transferir de nuevo.
     * @return DTO con los resultados de la validación.
     */
    public TransferenciaArchivoValidacionDTO validarTransferencia(
            final Usuario origenUsuario, final Usuario destinoUsuario,
            final String tipoTransferencia,
            final TransferenciaArchivo transferenciaAnterior) {
        final TransferenciaArchivoValidacionDTO validacionDTO
                = new TransferenciaArchivoValidacionDTO();

        final PlantillaTransferenciaArchivo plantilla
                = plantillaRepository.findByActivoTrue();
        if (plantilla == null) {
            validacionDTO.addError(
                    "ATENCIÓN: No hay plantilla activa para la generación "
                    + "del acta de transferencia de archivo.");
        }

        if (tipoTransferencia.equals(TransferenciaArchivo.PARCIAL_TIPO)
                && transferenciaAnterior == null) {
            validacionDTO.addError(
                    "Debe seleccionar una transferencia previa cuando se elige "
                    + "realizar transferencia parcial.");
        }

        if (origenUsuario == null) {
            validacionDTO.addError("Debe seleccionar un usuario origen de la "
                    + "transferencia.");
        } else {
            if (origenUsuario.getClasificacion() == null) {
                validacionDTO.addError("El usuario origen "
                        + getUsuarioDescripcion(origenUsuario, false) + " no "
                        + "tiene una clasificación configurada en el sistema.");
            } else if (!origenUsuario.getClasificacion().getActivo()) {
                validacionDTO.addError("El usuario origen "
                        + getUsuarioDescripcion(origenUsuario, false) + " no "
                        + "tiene una clasificación activa en el sistema ["
                        + origenUsuario.getClasificacion().getNombre()
                        + "].");
            }
        }

        if (destinoUsuario == null) {
            validacionDTO.addError("Debe seleccionar un usuario destino de la "
                    + "transferencia.");
        } else if (!destinoUsuario.getActivo()) {
            validacionDTO.addError("El usuario destino "
                    + getUsuarioDescripcion(destinoUsuario, false) + " no "
                    + "se encuentra activo en el sistema.");
        } else {
            if (destinoUsuario.getClasificacion() == null) {
                validacionDTO.addError("El usuario destino "
                        + getUsuarioDescripcion(destinoUsuario, false) + " no "
                        + "tiene una clasificación configurada en el sistema.");
            } else if (!destinoUsuario.getClasificacion().getActivo()) {
                validacionDTO.addError("El usuario destino "
                        + getUsuarioDescripcion(destinoUsuario, false) + " no "
                        + "tiene una clasificación activa en el sistema ["
                        + destinoUsuario.getClasificacion().getNombre()
                        + "].");
            }
        }

        if (origenUsuario != null && destinoUsuario != null) {
            if (destinoUsuario.getId().equals(origenUsuario.getId())) {
                validacionDTO.addError("Debe seleccionar un usuario destino "
                        + "diferente al usuario origen.");
            } else {
                if (tipoTransferencia.equals(TransferenciaArchivo.TOTAL_TIPO)
                        && origenUsuario.getClasificacion() != null
                        && destinoUsuario.getClasificacion() != null
                        && destinoUsuario.getClasificacion().getOrden()
                                .compareTo(origenUsuario.getClasificacion()
                                        .getOrden()) < 0) {
                    validacionDTO.addError(
                            "El usuario destino "
                            + getUsuarioDescripcion(destinoUsuario, true) + " tiene "
                            + "una clasificación menor que la clasificación del "
                            + "usuario origen "
                            + getUsuarioDescripcion(origenUsuario, true) + ".");
                } else if (tipoTransferencia.equals(TransferenciaArchivo.PARCIAL_TIPO)
                        && transferenciaAnterior != null
                        && destinoUsuario.getClasificacion().getOrden()
                                .compareTo(transferenciaAnterior.getOrigenClasificacion()
                                        .getOrden()) < 0) {
                    validacionDTO.addError(
                            "El usuario destino "
                            + getUsuarioDescripcion(destinoUsuario, true) + " tiene "
                            + "una clasificación menor que la clasificación de "
                            + "la transferencia original ["
                            + transferenciaAnterior.getOrigenClasificacion()
                            + "].");
                }
            }
        }

        return validacionDTO;
    }

    /**
     * Obtiene la descripción del usuario.
     *
     * @param usuario Usuario.
     * @param conClasificacion {@code true} indica que se debe presentar la
     * clasificación del usuario.
     * @return Descripción.
     */
    public String getUsuarioDescripcion(final Usuario usuario,
            final boolean conClasificacion) {
        /*
            2017-11-10 edison.gonzalez@controltechcg.com Issue #131 (SICDI-Controltech) 
            feature-131: Cambio en la entidad usuario, se coloca llave foranea el grado.
         */
        String descripcion = usuario.getUsuGrado().getId() + " "
                + usuario.getNombre();

        if (!conClasificacion) {
            return descripcion;
        }

        descripcion += " [" + usuario.getClasificacion().getNombre() + "]";
        return descripcion;
    }

    /**
     * Procesa, aplica y crea una transferencia de archivo, generando el acta
     * correspondiente.
     *
     * @param creadorUsuario Usuario creador de la transferencia.
     * @param origenUsuario Usuario origen de la transferencia.
     * @param destinoUsuario Usuario destino de la transferencia.
     * @param tipoTransferencia Tipo de transferencia.
     * @param transferenciaAnterior Registro maestro de transferencia anterior a
     * transferir de nuevo. En caso que este parámetro sea {@code null}, se
     * realizará transferencia de todo el archivo del usuario origen, al usuario
     * destino.
     * @param asposeLicense Licencia de Aspose.
     * @return Registro maestro de la transferencia creada.
     * @throws java.lang.Exception Si ocurre algún error durante la creación del
     * documento Aspose.
     * @see TransferenciaArchivo#TOTAL_TIPO
     * @see TransferenciaArchivo#PARCIAL_TIPO
     */
    public TransferenciaArchivo crearTransferenciaConActa(final Usuario creadorUsuario,
            final Usuario origenUsuario, final Usuario destinoUsuario,
            final String tipoTransferencia,
            final TransferenciaArchivo transferenciaAnterior, final License asposeLicense) throws Exception {

        final TransferenciaArchivo transferenciaArchivo
                = crearTransferencia(creadorUsuario, origenUsuario, destinoUsuario,
                        tipoTransferencia, transferenciaAnterior);

        final PlantillaTransferenciaArchivo plantilla
                = plantillaRepository.findByActivoTrue();

        crearActa(transferenciaArchivo, plantilla, asposeLicense);

        return transferenciaArchivo;
    }

    /**
     * Procesa, aplica y crea una transferencia de archivo.
     *
     * @param creadorUsuario Usuario creador de la transferencia.
     * @param origenUsuario Usuario origen de la transferencia.
     * @param destinoUsuario Usuario destino de la transferencia.
     * @param tipoTransferencia Tipo de transferencia.
     * @param transferenciaAnterior Registro maestro de transferencia anterior a
     * transferir de nuevo. En caso que este parámetro sea {@code null}, se
     * realizará transferencia de todo el archivo del usuario origen, al usuario
     * destino.
     * @return Registro maestro de la transferencia creada.
     * @see TransferenciaArchivo#TOTAL_TIPO
     * @see TransferenciaArchivo#PARCIAL_TIPO
     */
    @Transactional
    private TransferenciaArchivo crearTransferencia(final Usuario creadorUsuario,
            final Usuario origenUsuario, final Usuario destinoUsuario,
            final String tipoTransferencia, final TransferenciaArchivo transferenciaAnterior) {

        final Date ahora = new Date(System.currentTimeMillis());

        /*
            2017-11-10 edison.gonzalez@controltechcg.com Issue #131 (SICDI-Controltech) 
            feature-131: Cambio en la entidad usuario, se coloca llave foranea el grado.
         */
        final Grados creadorGrado
                = gradoRepository.findOne(creadorUsuario.getUsuGrado().getId());
        final Grados origenGrado
                = gradoRepository.findOne(origenUsuario.getUsuGrado().getId());
        final Grados destinoGrado
                = gradoRepository.findOne(destinoUsuario.getUsuGrado().getId());

        final TransferenciaArchivo transferencia = new TransferenciaArchivo(
                tipoTransferencia,
                creadorUsuario, creadorUsuario.getDependencia(),
                creadorGrado, creadorUsuario.getCargo(), ahora, origenUsuario,
                origenUsuario.getDependencia(), origenUsuario.getClasificacion(),
                origenGrado, origenUsuario.getCargo(), destinoUsuario,
                destinoUsuario.getDependencia(), destinoUsuario.getClasificacion(),
                destinoGrado, destinoUsuario.getCargo(),
                transferenciaAnterior == null ? null
                        : transferenciaAnterior.getId(),
                transferenciaAnterior == null ? null
                        : transferenciaAnterior.getOrigenClasificacion());

        transferenciaRepository.save(transferencia);

        final List<DocumentoDependencia> registrosArchivo
                = findRegistrosArchivo(tipoTransferencia, transferenciaAnterior,
                        origenUsuario);

        for (DocumentoDependencia registroArchivo : registrosArchivo) {
            procesarRegistroArchivo(transferencia, registroArchivo, ahora);
        }

        transferencia.setNumeroDocumentos(registrosArchivo.size());
        transferencia.setEstado(TransferenciaArchivo.APROBADA_ESTADO);
        transferencia.setFechaAprobacion(ahora);

        final Integer unidadDestinoID = dependenciaRepository.findUnidadID(destinoUsuario.getDependencia().getId());
        /*
            * 2017-11-14 edison.gonzalez@controltechcg.com Issue #138: Se llama
            * al servicio encargado de retornar el numero de radicado, segun el tipo
            * de proceso, para el caso de transferencia de archivos se toma el proceso
            * de registro de documentos internos.
         */
        final String radicado = radicadoService.retornaNumeroRadicado(unidadDestinoID, RadicadoService.EnumRadicacion.TRANSFERENCIA_ARCHIVO.getValue());
        transferencia.setNumeroRadicado(radicado);

        transferenciaRepository.saveAndFlush(transferencia);

        if (transferenciaAnterior == null) {
            final List<TransferenciaArchivo> transferenciasRecibidas
                    = transferenciaRepository.findAllRecibidasActivasByDestinoUsuario(origenUsuario.getId());
            for (TransferenciaArchivo transferenciasRecibida : transferenciasRecibidas) {
                transferenciasRecibida.setActivo(Boolean.FALSE);
                transferenciaRepository.saveAndFlush(transferenciasRecibida);
            }
        } else {
            transferenciaAnterior.setActivo(Boolean.FALSE);
            transferenciaRepository.saveAndFlush(transferenciaAnterior);
        }

        return transferencia;
    }

    /**
     * Busca la lista de registros de archivo a transferir.
     *
     * @param tipoTransferencia Tipo de transferencia.
     * @param transferenciaAnterior Transferencia anterior seleccionado, en caso
     * que el tipo de transferencia sea
     * {@link TransferenciaArchivo#PARCIAL_TIPO}.
     * @param origenUsuario Usuario origen de la transferencia.
     * @return Lista de registros de archivo a transferir.
     */
    public List<DocumentoDependencia> findRegistrosArchivo(final String tipoTransferencia,
            final TransferenciaArchivo transferenciaAnterior,
            final Usuario origenUsuario) {
        if (tipoTransferencia.equals(TransferenciaArchivo.TOTAL_TIPO)) {
            return documentoDependenciaRepository
                    .findAllActivoByUsuario(origenUsuario.getId());
        }

        return documentoDependenciaRepository
                .findAllByUsuarioAndTransferenciaArchivo(origenUsuario.getId(),
                        transferenciaAnterior.getId());

    }

    /**
     * Procesa un registro de archivo para su transferencia.
     *
     * @param transferencia Transferencia de archivo maestro.
     * @param registroArchivo Registro de archivo.
     * @param fechaAsignacion Fecha de asignación.
     */
    @Transactional
    private void procesarRegistroArchivo(final TransferenciaArchivo transferencia,
            final DocumentoDependencia registroArchivo,
            final Date fechaAsignacion) {

        final Usuario anteriorUsuario
                = usuarioRepository.findOne(registroArchivo.getQuien());

        final TransferenciaArchivoDetalle detalle
                = new TransferenciaArchivoDetalle(transferencia,
                        registroArchivo,
                        registroArchivo.getDocumento().getId(),
                        registroArchivo.getDependencia(), anteriorUsuario,
                        registroArchivo.getCuando(),
                        transferencia.getDestinoDependencia(),
                        transferencia.getDestinoUsuario(), fechaAsignacion);

        detalleRepository.saveAndFlush(detalle);

        registroArchivo.setDependencia(transferencia.getDestinoDependencia());
        registroArchivo.setCuando(fechaAsignacion);
        registroArchivo.setQuien(transferencia.getDestinoUsuario().getId());
        documentoDependenciaRepository.save(registroArchivo);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = ""
                + "UPDATE \n"
                + " DOCUMENTO_DEPENDENCIA \n"
                + "SET \n"
                + " DOCUMENTO_DEPENDENCIA.QUIEN = ? \n"
                + "WHERE \n"
                + " DOCUMENTO_DEPENDENCIA.DCDP_ID = ? \n"
                + "";
        Object[] params = {
            transferencia.getDestinoUsuario().getId(),
            registroArchivo.getId()
        };
        jdbcTemplate.update(sql, params);
    }

    /**
     * Crea el acta para la transferencia de archivo.
     *
     * @param transferenciaArchivo Transferencia de archivo.
     * @param plantilla Plantilla de transferencia.
     * @param asposeLicense Licencia de Aspose.
     * @throws Exception Si ocurre algún error durante la creación del documento
     * Aspose.
     */
    private void crearActa(final TransferenciaArchivo transferenciaArchivo,
            final PlantillaTransferenciaArchivo plantilla, final License asposeLicense) throws Exception {

        if (!asposeLicense.getIsLicensed()) {
            LOG.severe("ASPOSE no Licenciado!");
        }

        final KeysValuesAsposeDocxDTO asposeMap = crearMapaAspose(transferenciaArchivo);
        final String plantillaPath = ofs.getPath(plantilla.getCodigoOFS());
        final Document asposeDocument = new Document(plantillaPath);

        asposeDocument.getMailMerge().execute(asposeMap.getNombres(), asposeMap.getValues());

        final List<TransferenciaArchivoDetalle> detalles
                = detalleRepository.findAllByTransferenciaArchivo(transferenciaArchivo);
        ordenarDetalles(detalles);

        // TODO: Formato de tabla (Negrita para títulos, etc...)
        // Ver forma de mantener las celdas iniciales cuando cambie de página.
        final Table table = (Table) asposeDocument.getChild(NodeType.TABLE, 0, true);
        if (table != null) {
            table.removeAllChildren();

            Row headersRow = new Row(asposeDocument);
            fillTableRow(asposeDocument, headersRow, "RADICADO", "CÓDIGO", "ASUNTO", "FECHA FINAL", "ELABORÓ", "UNIDAD DEST.");
            table.appendChild(headersRow);

            for (TransferenciaArchivoDetalle detalle : detalles) {
                final String[] cellValues = buildCellValues(detalle);

                Row row = new Row(asposeDocument);
                fillTableRow(asposeDocument, row, cellValues);
                table.appendChild(row);
            }
        }

        // TODO: Preguntar que hacemos si la dependencia destino no tiene sigla.
        final String siglaDependenciaDestino = Objects.toString(transferenciaArchivo.getDestinoDependencia().getSigla(), "");
        ofs.insertWatermarkText(asposeDocument, siglaDependenciaDestino);

        final File tmpFile = File.createTempFile("_sigdi_temp_", ".pdf");
        asposeDocument.save(tmpFile.getPath());

        final OFSEntry ofsEntry = ofs.readPDFAspose(tmpFile);
        final String codigoActaOFS = ofs.save(ofsEntry.getContent(),
                AppConstants.MIME_TYPE_PDF);

        transferenciaArchivo.setActaOFS(codigoActaOFS);

        transferenciaRepository.saveAndFlush(transferenciaArchivo);

        try {
            tmpFile.delete();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            try {
                tmpFile.deleteOnExit();
            } catch (Exception ex1) {
                LOG.log(Level.SEVERE, null, ex1);
            }
        }
    }

    /**
     * Construye los valores para las celdas de una fila de la tabla de
     * documentos transferidos.
     *
     * @param detalle Registro de detalle de transferencia.
     * @return Arreglo de valores del detalle de transferencia.
     */
    private String[] buildCellValues(TransferenciaArchivoDetalle detalle) {
        final SimpleDateFormat fechaFirmaDateFormatter = new SimpleDateFormat(DETALLE_DOCUMENTO_FIRMA_DATE_FORMAT_PATTERN, LOCALE_ES_CO);

        final DocumentoDependencia documentoDependencia = detalle.getDocumentoDependencia();
        final Documento documento = documentoDependencia.getDocumento();
        final String radicado = documento.getRadicado();
        final String asunto = documento.getAsunto();

        final Trd trd = documentoDependencia.getTrd();
        final String trdCodigo = trd.getCodigo();

        final Integer quien = documento.getQuien();
        final Usuario documentoQuien = usuarioRepository.findOne(quien);
        /*
            2017-11-10 edison.gonzalez@controltechcg.com Issue #131 (SICDI-Controltech) 
            feature-131: Cambio en la entidad usuario, se coloca llave foranea el grado.
         */
        final String documentoElaboro = (Objects.toString(documentoQuien.getUsuGrado().getId(), "") + " " + documentoQuien.getNombre()).trim();

        /**
         * 2017-09-27 jgarcia@controltechcg.net Issue #128 (SICDI-Controltech)
         * hotfix-128: Corrección para no tener en cuenta la dependencia
         * destino, cuando los documentos son de proceso externo.
         */
        final Dependencia dependenciaDestino = documento.getDependenciaDestino();
        final String siglaDependenciaDestino;
        if (dependenciaDestino == null) {
            siglaDependenciaDestino = "";
        } else {
            siglaDependenciaDestino = Objects.toString(dependenciaDestino.getSigla(), "");
        }

        final String fechaFirma;
        if (documento.getFirma() == null) {
            fechaFirma = "";
        } else {
            final Date cuandoFirma = documentoRepository.findCuandoFirma(documento.getInstancia().getId());
            fechaFirma = fechaFirmaDateFormatter.format(cuandoFirma);
        }

        return new String[]{radicado, trdCodigo, asunto, fechaFirma, documentoElaboro, siglaDependenciaDestino};
    }

    /**
     * Crea el mapa de campos/valor para el reemplazo de wildcards sobre la
     * plantilla para la generación del mapa a través de la API de Aspose.
     *
     * @param transferenciaArchivo Transferencia de archivo.
     * @return Mapa de campos/valor.
     */
    private KeysValuesAsposeDocxDTO crearMapaAspose(final TransferenciaArchivo transferenciaArchivo) {
        final SimpleDateFormat fullDateFormatter = new SimpleDateFormat(FULL_DATE_FORMAT_PATTERN, LOCALE_ES_CO);
        final SimpleDateFormat documentDateFormatter = new SimpleDateFormat(DOCUMENT_DATE_FORMAT_PATTERN, LOCALE_ES_CO);

        final KeysValuesAsposeDocxDTO map = new KeysValuesAsposeDocxDTO();

        map.put("TIPO_TRANSFERENCIA", "Transferencia " + (transferenciaArchivo.getTipo()
                .equals(TransferenciaArchivo.PARCIAL_TIPO) ? "Parcial" : "Total"));

        map.put("CREADOR_NOMBRE", transferenciaArchivo.getCreadorUsuario().getNombre());
        map.put("CREADOR_DEPENDENCIA_NOMBRE", transferenciaArchivo.getCreadorDependencia().getNombre());
        map.put("CREADOR_GRADO_ID", transferenciaArchivo.getCreadorGrado().getId());
        map.put("CREADOR_GRADO_NOMBRE", transferenciaArchivo.getCreadorGrado().getNombre());
        map.put("CREADOR_CARGO", transferenciaArchivo.getCreadorCargo());

        map.put("FECHA_CREACION", fullDateFormatter.format(transferenciaArchivo.getFechaCreacion()));

        map.put("ORIGEN_NOMBRE", transferenciaArchivo.getOrigenUsuario().getNombre());
        map.put("ORIGEN_DEPENDENCIA_NOMBRE", transferenciaArchivo.getOrigenDependencia().getNombre());
        map.put("ORIGEN_GRADO_ID", transferenciaArchivo.getOrigenGrado().getId());
        map.put("ORIGEN_GRADO_NOMBRE", transferenciaArchivo.getOrigenGrado().getNombre());
        map.put("ORIGEN_CARGO", transferenciaArchivo.getOrigenCargo());
        map.put("ORIGEN_CLASIFICACION", transferenciaArchivo.getOrigenClasificacion().getNombre());

        map.put("DESTINO_NOMBRE", transferenciaArchivo.getDestinoUsuario().getNombre());
        map.put("DESTINO_DEPENDENCIA_NOMBRE", transferenciaArchivo.getDestinoDependencia().getNombre());
        map.put("DESTINO_GRADO_ID", transferenciaArchivo.getDestinoGrado().getId());
        map.put("DESTINO_GRADO_NOMBRE", transferenciaArchivo.getDestinoGrado().getNombre());
        map.put("DESTINO_CARGO", transferenciaArchivo.getDestinoCargo());
        map.put("DESTINO_CLASIFICACION", transferenciaArchivo.getDestinoClasificacion().getNombre());

        map.put("NUMERO_DOCUMENTOS", transferenciaArchivo.getNumeroDocumentos());

        map.put("FECHA_APROBACION", fullDateFormatter.format(transferenciaArchivo.getFechaAprobacion()));

        final File creadorFirma = getImagenFirma(transferenciaArchivo.getCreadorUsuario());
        if (creadorFirma != null) {
            try {
                map.put("CREADOR_FIRMA_IMG", FileUtils.readFileToByteArray(creadorFirma));
                map.put("IMG_FIRMA", FileUtils.readFileToByteArray(creadorFirma));
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "CREADOR_FIRMA_IMG", ex);
            }
        }

        final File origenFirma = getImagenFirma(transferenciaArchivo.getOrigenUsuario());
        if (origenFirma != null) {
            try {
                map.put("ORIGEN_FIRMA_IMG", FileUtils.readFileToByteArray(origenFirma));
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "ORIGEN_FIRMA_IMG", ex);
            }
        }

        final File destinoFirma = getImagenFirma(transferenciaArchivo.getDestinoUsuario());
        if (destinoFirma != null) {
            try {
                map.put("DESTINO_FIRMA_IMG", FileUtils.readFileToByteArray(destinoFirma));
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "DESTINO_FIRMA_IMG", ex);
            }
        }

        final Clasificacion minClasificacion = clasificacionRepository.findMinOrderActivo();
        map.put("N_CLASIFICACION", minClasificacion.getNombre());

        final Integer unidadID = dependenciaRepository.findUnidadID(transferenciaArchivo.getOrigenDependencia().getId());
        final Dependencia origenUnidadDependencia = dependenciaRepository.findOne(unidadID);
        map.put("ELABORA_DEP_SPADRE", origenUnidadDependencia.getNombre());

        final String lineaMando = buildLineaMando(transferenciaArchivo);
        map.put("LINEA_MANDO2", lineaMando);

        final String elaboraCiudad = transferenciaArchivo.getOrigenDependencia().getCiudad();
        map.put("DEPENDENCIA_CIUDAD_ELABORA", elaboraCiudad == null ? "" : elaboraCiudad);

        final String fechaDocumento = documentDateFormatter.format(transferenciaArchivo.getFechaAprobacion());
        map.put("FECHA_DOC", fechaDocumento);

        final byte[] barcodeBytes = buildBarcodeBytes(transferenciaArchivo.getNumeroRadicado().replaceAll("-", ""));
        map.put("COD_BARRA", barcodeBytes);

        map.put("N_RADICADO", transferenciaArchivo.getNumeroRadicado());

        map.put("FIRMA_NOM", transferenciaArchivo.getCreadorUsuario().getNombre());
        map.put("FIRMA_GRADO", transferenciaArchivo.getCreadorGrado().getId());
        map.put("FIRMA_CARGO", transferenciaArchivo.getCreadorCargo());

        return map;
    }

    /**
     * Construye la línea de mando de la dependencia origen de la transferencia.
     *
     * @param transferenciaArchivo Transferencia de archivo.
     * @return Línea de mando.
     */
    private String buildLineaMando(final TransferenciaArchivo transferenciaArchivo) {
        String lineaMando = LINEA_MANDO_PREFIX;

        final Dependencia origenDependencia = transferenciaArchivo.getOrigenDependencia();
        final Integer unidadID = dependenciaRepository.findUnidadID(origenDependencia.getId());
        final Dependencia origenUnidadDependencia = dependenciaRepository.findOne(unidadID);
        if (origenUnidadDependencia.getSigla() != null) {
            lineaMando += LINEA_MANDO_SEPARATOR + origenUnidadDependencia.getSigla();
        }

        if (origenDependencia.getSigla() != null) {
            lineaMando += LINEA_MANDO_SEPARATOR + origenDependencia.getSigla();
        }

        return lineaMando;
    }

    /**
     * Obtiene el archivo de la firma del usuario.
     *
     * @param usuario Usuario.
     * @return Archivo.
     */
    private File getImagenFirma(final Usuario usuario) {
        final String imagenFirma = usuario.getImagenFirma();
        if (imagenFirma == null) {
            return null;
        }

        return new File(ofs.getPath(usuario.getImagenFirmaExtension()));
    }

    /**
     * Llena la fila de table.
     *
     * @param asposeDocument Documento ASPOSE.
     * @param row Fila.
     * @param cellValues Valores para las celdas de la fila.
     */
    private void fillTableRow(final Document asposeDocument, final Row row, final String... cellValues) {
        for (String cellValue : cellValues) {
            Paragraph paragraph = new Paragraph(asposeDocument);
            paragraph.appendChild(new Run(asposeDocument, cellValue));

            Cell cell = new Cell(asposeDocument);
            cell.appendChild(paragraph);
            row.appendChild(cell);
        }
    }

    /**
     * Construye un arreglo de bytes correspondiente a la imagen del código de
     * barras del valor indicado.
     *
     * @param value Valor a presentar como código de barras. Todos sus
     * caracteres deben ser números.
     * @return Arreglo de bytes.
     */
    private byte[] buildBarcodeBytes(final String value) {

        byte[] barcodeBytes = null;

        JBarcodeBean barcode = new JBarcodeBean();

        barcode.setCodeType(new Interleaved25());

        String codigo;
        if (value == null || value.trim().isEmpty()) {
            codigo = "000000000";
        } else {
            String chars = value.trim();
            codigo = "";
            for (int indice = 0; indice < chars.length(); indice++) {
                codigo += String.valueOf(Integer.parseInt(chars.charAt(indice) + ""));
            }
        }

        barcode.setCode(codigo);
        barcode.setCheckDigit(true);

        BufferedImage bufferedImage = barcode.draw(new BufferedImage(200, 20, BufferedImage.TYPE_INT_RGB));

        File barcodeTmpFile = null;
        try {
            barcodeTmpFile = File.createTempFile("_sigdi_img_tmp_", ".png");
            ImageIO.write(bufferedImage, "png", barcodeTmpFile);

            barcodeBytes = FileUtils.readFileToByteArray(barcodeTmpFile);

            if (!barcodeTmpFile.delete()) {
                LOG.log(Level.WARNING, "No puedo borrar archivo {0}", barcodeTmpFile.getCanonicalPath());
            }

        } catch (IOException ex) {
            LOG.log(Level.SEVERE, codigo, ex);
            try {
                if (barcodeTmpFile != null) {
                    barcodeTmpFile.deleteOnExit();
                }
            } catch (Exception ex2) {
                LOG.log(Level.SEVERE, codigo, ex2);
            }
        }

        return barcodeBytes;

    }

    /**
     * Ordena la lista de detalles según el código de la TRD asociada.
     *
     * @param detalles Lista de detalles de transferencia.
     */
    private void ordenarDetalles(List<TransferenciaArchivoDetalle> detalles) {
        Collections.sort(detalles, new Comparator<TransferenciaArchivoDetalle>() {
            final NumeroVersionComparator versionComparator = new NumeroVersionComparator();

            @Override
            public int compare(TransferenciaArchivoDetalle detalle1, TransferenciaArchivoDetalle detalle2) {
                final String codigo1 = detalle1.getDocumentoDependencia().getDocumento().getTrd().getCodigo();
                final String codigo2 = detalle2.getDocumentoDependencia().getDocumento().getTrd().getCodigo();
                return versionComparator.compare(codigo1, codigo2);
            }
        });
    }

}
