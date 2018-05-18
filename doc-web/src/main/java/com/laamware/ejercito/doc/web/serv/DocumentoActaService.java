package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.DocumentoActaDTO;
import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.Clasificacion;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.entity.Radicacion;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.enums.DocumentoActaEstado;
import com.laamware.ejercito.doc.web.enums.DocumentoActaMode;
import com.laamware.ejercito.doc.web.util.BusinessLogicValidation;
import com.laamware.ejercito.doc.web.util.DateUtil;
import com.laamware.ejercito.doc.web.util.Global;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Servicio para proceso documental de "Registro de Acta".
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/15/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
@Service
public class DocumentoActaService {

    public static final Map<DocumentoActaEstado, DocumentoActaMode> ESTADO_MODE_MAP;

    public static final Map<String, String> ESTADO_MODE_MAP_FOR_UI;

    private static final Logger LOG = Logger.getLogger(DocumentoActaService.class.getName());

    static {
        final Map<DocumentoActaEstado, DocumentoActaMode> baseMap = new LinkedHashMap<>();
        baseMap.put(DocumentoActaEstado.ACTA_DIGITALIZADA, DocumentoActaMode.SOLO_CONSULTA);
        baseMap.put(DocumentoActaEstado.ANULADO, DocumentoActaMode.SOLO_CONSULTA);
        baseMap.put(DocumentoActaEstado.NUMERO_DE_RADICACION_GENERADO, DocumentoActaMode.CARGA_ACTA_DIGITAL);
        baseMap.put(DocumentoActaEstado.REGISTRO_DE_DATOS_DEL_ACTA, DocumentoActaMode.EDICION_INFORMACION);

        ESTADO_MODE_MAP = Collections.unmodifiableMap(baseMap);

        final Map<String, String> forUIMap = new LinkedHashMap<>();
        for (Map.Entry<DocumentoActaEstado, DocumentoActaMode> entry : baseMap.entrySet()) {
            forUIMap.put(entry.getKey().getId().toString(), entry.getValue().name());
        }

        ESTADO_MODE_MAP_FOR_UI = Collections.unmodifiableMap(forUIMap);
    }

    private final Integer serieActasID;

    private final Integer diasLimiteFechaElaboracion;

    private final Integer diasLimiteFechaPlazo;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DocumentoService documentoService;

    @Autowired
    private TRDService trdService;

    @Autowired
    private ClasificacionService clasificacionService;

    @Autowired
    private RadicadoService radicadoService;

    @Autowired
    private DependenciaService dependenciaService;

    @Autowired
    private OFS ofs;

    /**
     * Constructor.
     *
     * @param serieActasID ID de la TRD correspondiente a la serie de actas.
     * Cargado por archivo de propiedades.
     * @param diasLimiteFechaElaboracion Número de días para el límite de la
     * fecha de elaboración. Cargado por archivo de propiedades.
     * @param diasLimiteFechaPlazo Número de días para el establecimiento de la
     * fecha límite de plazo de construcción del acta en el sistema. Cargado por
     * archivo de propiedades.
     */
    @Autowired
    public DocumentoActaService(
            @Value("${com.mil.imi.sicdi.trd.serie.actas}") Integer serieActasID,
            @Value("${com.mil.imi.sicdi.documento.acta.limite.fecha-elaboracion.dias}") Integer diasLimiteFechaElaboracion,
            @Value("${com.mil.imi.sicdi.documento.acta.limite.fecha-plazo.dias}") Integer diasLimiteFechaPlazo
    ) {
        this.serieActasID = serieActasID;
        this.diasLimiteFechaElaboracion = diasLimiteFechaElaboracion;
        this.diasLimiteFechaPlazo = diasLimiteFechaPlazo;
    }

    /**
     * Crea un nuevo documento.
     *
     * @param procesoInstancia Instancia del proceso a asociar con el documento.
     * @param usuarioCreador Usuario creador del documento.
     * @return Nuevo documento con estado {@link Documento#ESTADO_TEMPORAL}.
     * @see
     * DocumentoService#crearDocumento(com.laamware.ejercito.doc.web.entity.Instancia,
     * com.laamware.ejercito.doc.web.entity.Usuario)
     */
    public Documento crearDocumento(final Instancia procesoInstancia, final Usuario usuarioCreador) {
        return documentoService.crearDocumento(procesoInstancia, usuarioCreador);
    }

    /**
     * Verifica si el usuario tiene acceso al documento acta.
     *
     * @param usuario Usuario.
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @return {@code true} si el usuario tiene acceso al documento acta; de lo
     * contrario, {@code false}.
     */
    public boolean verificaAccesoDocumentoActa(final Usuario usuario, final String procesoInstanciaID) {
        return usuarioService.verificaAccesoDocumentoActa(usuario, procesoInstanciaID);
    }

    /**
     * Busca un documento.
     *
     * @param documentoID ID del documento.
     * @return Documento, o {@code null} en caso de no tener correspondencia en
     * el sistema.
     * @see DocumentoService#buscarDocumento(java.lang.String)
     */
    public Documento buscarDocumento(final String documentoID) {
        return documentoService.buscarDocumento(documentoID);
    }

    /**
     * Indica si el usuario tiene acceso al documento por nivel de
     * clasificación.
     *
     * @param usuario Usuario.
     * @param procesoInstancia Instancia del proceso del documento.
     * @return {@code true} si el usuario es el siguiente asignado al documento
     * o si el usuario tiene un nivel de clasificación igual o superior al nivel
     * de clasificación asignado al documento; de lo contrario, {@code false}.
     */
    public boolean tieneAccesoPorClasificacion(final Usuario usuario, final Instancia procesoInstancia) {
        return documentoService.tieneAccesoPorClasificacion(usuario, procesoInstancia);
    }

    /**
     * Busca la lista de todas las subseries TRD correspondientes a la serie TRD
     * de actas, asignadas a la dependencia del usuario.
     *
     * @param usuario Usuario.
     * @return Lista de subseries TRD de actas asignadas a la dependencia del
     * usuario.
     */
    public List<Trd> buscarSubseriesActasPorUsuario(final Usuario usuario) {
        final Trd serieActas = new Trd();
        serieActas.setId(serieActasID);

        List<Trd> subseriesActas = trdService.findSubseriesbySerieAndUsuario(serieActas, usuario);
        trdService.ordenarPorCodigo(subseriesActas);
        return subseriesActas;
    }

    /**
     * Valida la información del acta que se envía desde el formulario, previo
     * al proceso de guardado inicial del documento.
     *
     * @param documentoActaDTO DTO de documento acta.
     * @param usuario Usuario.
     * @return Resumen del proceso de validación.
     */
    public BusinessLogicValidation validarGuardarActa(final DocumentoActaDTO documentoActaDTO, final Usuario usuario) {
        final BusinessLogicValidation validation = new BusinessLogicValidation();

        String campo;

        // asunto
        campo = "asunto";
        final String asunto = documentoActaDTO.getAsunto();
        if (asunto == null || asunto.trim().isEmpty()) {
            validation.addError(documentoActaDTO, campo, "Debe ingresar un asunto.");
        }

        // actaLugar
        campo = "actaLugar";
        final String actaLugar = documentoActaDTO.getActaLugar();
        if (actaLugar == null || actaLugar.trim().isEmpty()) {
            validation.addError(documentoActaDTO, campo, "Debe ingresar un lugar.");
        }

        // actaFechaElaboracion
        campo = "actaFechaElaboracion";
        final String _actaFechaElaboracion = documentoActaDTO.getActaFechaElaboracion();
        if (_actaFechaElaboracion == null || _actaFechaElaboracion.trim().isEmpty()) {
            validation.addError(documentoActaDTO, campo, "Debe seleccionar una fecha de elaboración.");
        } else {
            try {
                final Date actaFechaElaboracion = buildFechaElaboracion(_actaFechaElaboracion);
                final Date actaFechaElaboracionLimite = DateUtil.setTime(DateUtil.add(new Date(), Calendar.DATE, -diasLimiteFechaElaboracion), DateUtil.SetTimeType.START_TIME);
                if (actaFechaElaboracion.before(actaFechaElaboracionLimite)) {
                    validation.addError(documentoActaDTO, campo, "La fecha de elaboración es menor que la fecha límite permitida: "
                            + new SimpleDateFormat(Global.DATE_FORMAT).format(actaFechaElaboracionLimite));
                }
            } catch (ParseException ex) {
                LOG.log(Level.SEVERE, _actaFechaElaboracion, ex);
                validation.addError(documentoActaDTO, campo, "Debe enviar una fecha de elaboración válida.");
            }
        }

        // clasificacion
        campo = "clasificacion";
        final String _clasificacionID = documentoActaDTO.getClasificacion();
        if (_clasificacionID == null || _clasificacionID.trim().isEmpty()) {
            validation.addError(documentoActaDTO, campo, "Debe seleccionar el nivel de clasificación.");
        } else {
            try {
                final Integer clasificacionID = Integer.parseInt(_clasificacionID);
                final Clasificacion clasificacion = clasificacionService.findActivo(clasificacionID);
                if (clasificacion == null) {
                    validation.addError(documentoActaDTO, campo, "Debe seleccionar un nivel de clasificación activo.");
                }

                // TODO: Verificar si hay más reglas asociadas a la clasificación seleccionada.
            } catch (NumberFormatException ex) {
                LOG.log(Level.SEVERE, _clasificacionID, ex);
                validation.addError(documentoActaDTO, campo, "Debe enviar una clasificación válida.");
            }
        }

        // trd
        campo = "trd";
        final String _trdID = documentoActaDTO.getTrd();
        if (_trdID == null || _trdID.trim().isEmpty()) {
            validation.addError(documentoActaDTO, campo, "Debe seleccionar la subserie TRD.");
        } else {
            try {
                final Integer trdID = Integer.parseInt(_trdID);
                boolean trdValida = trdService.validateSubserieTrdForUser(new Trd(trdID), new Trd(serieActasID), usuario);
                if (!trdValida) {
                    validation.addError(documentoActaDTO, campo, "Debe seleccionar la subserie TRD válida y/o asignada.");
                }
            } catch (NumberFormatException ex) {
                LOG.log(Level.SEVERE, _trdID, ex);
                validation.addError(documentoActaDTO, campo, "Debe enviar una subserie TRD válida.");
            }
        }

        // numeroFolios
        campo = "numeroFolios";
        final String _numeroFolios = documentoActaDTO.getNumeroFolios();
        if (_numeroFolios == null || _numeroFolios.trim().isEmpty()) {
            validation.addError(documentoActaDTO, campo, "Debe ingresar el número de folios.");
        } else {
            try {
                final Integer numeroFolios = Integer.parseInt(_numeroFolios);
                if (numeroFolios <= 0) {
                    validation.addError(documentoActaDTO, campo, "Debe ingresar un número de folios mayor o igual a 1.");
                }
            } catch (NumberFormatException ex) {
                LOG.log(Level.SEVERE, _numeroFolios, ex);
                validation.addError(documentoActaDTO, campo, "Debe ingresar un número de folios válido.");
            }
        }

        // cargoElabora
        campo = "cargoElabora";
        final String cargoElabora = documentoActaDTO.getCargoElabora();
        if (cargoElabora == null || cargoElabora.trim().isEmpty()) {
            validation.addError(documentoActaDTO, campo, "Debe ingresar el cargo con el cual elabora el acta.");
        }

        return validation;
    }

    public Documento guardarRegistroDatos(final DocumentoActaDTO documentoActaDTO, final Usuario usuario) throws ParseException {
        final Date fechaHoraActual = new Date();

        Documento documento = documentoService.buscarDocumento(documentoActaDTO.getDocId());
        documento.setAsunto(documentoActaDTO.getAsunto());
        documento.setClasificacion(new Clasificacion(Integer.parseInt(documentoActaDTO.getClasificacion())));
        documento.setTrd(new Trd(Integer.parseInt(documentoActaDTO.getTrd())));
        documento.setNumeroFolios(Integer.parseInt(documentoActaDTO.getNumeroFolios()));
        documento.setQuienMod(usuario.getId());
        documento.setCuandoMod(fechaHoraActual);
        documento.setPlazo(buildFechaPlazo(fechaHoraActual));
        documento.setElabora(usuario);
        documento.setDependenciaDestino(usuario.getDependencia());
        documento.setCargoIdElabora(new Cargo(Integer.parseInt(documentoActaDTO.getCargoElabora())));
        documento.setActaLugar(documentoActaDTO.getActaLugar());
        documento.setActaFechaElaboracion(buildFechaElaboracion(documentoActaDTO.getActaFechaElaboracion()));
        documento.setEstadoTemporal(null);

        documento = documentoService.actualizar(documento);
        final Documento buscarDocumento = buscarDocumento(documento.getId());
        System.out.println("buscarDocumento = " + buscarDocumento.getCargoIdElabora().getCarNombre());
        return buscarDocumento;
    }

    /**
     * Genera y asigna el número de radicación para el documento, según el
     * proceso al que pertenece y la súper dependencia de la dependencia
     * destino.
     *
     * @param documento Documento.
     * @param usuarioSesion Usuario en sesión.
     * @return Documento actualizado con el número de radicación asignado.
     */
    public Documento asignarNumeroRadicacion(Documento documento, final Usuario usuarioSesion) {
        final Dependencia dependenciaDestino = documento.getDependenciaDestino();
        final Dependencia superDependencia = dependenciaService.getSuperDependencia(dependenciaDestino);

        final Proceso proceso = documento.getInstancia().getProceso();
        final Radicacion radicacion = radicadoService.findByProceso(proceso);

        documento.setRadicado(radicadoService.retornaNumeroRadicado(superDependencia.getId(), radicacion.getRadId()));

        documento.setQuienMod(usuarioSesion.getId());
        documento.setCuandoMod(new Date());

        return documentoService.actualizar(documento);
    }

    /**
     * Carga el acta digitalizada al documento, almacenándolo en el OFS.
     *
     * @param documento Documento.
     * @param multipartFile Archivo cargado desde la UI.
     * @param usuarioSesion Usuario en sesión.
     * @return Documento actualizado con el OFS ID del archivo cargado.
     * @throws IOException En caso que se presente una situación de error
     * durante el proceso de almacenamiento del archivo en el OFS.
     */
    public Documento cargarActaDigitalizada(Documento documento, final MultipartFile multipartFile, final Usuario usuarioSesion) throws IOException {
        final String ofsFileID = ofs.save(multipartFile.getBytes(), multipartFile.getContentType());
        documento.setPdf(ofsFileID);

        documento.setQuienMod(usuarioSesion.getId());
        documento.setCuandoMod(new Date());

        return documentoService.actualizar(documento);
    }

    /**
     * Construye la fecha de elaboración a partir del dato enviado en el
     * formulario, estableciendo la hora como 00:00:00.
     *
     * @param fechaElaboracion Valor de la fecha de elaboración en formato
     * {@link Global#DATE_FORMAT}.
     * @return Instancia de fecha correspondiente al valor de la fecha de
     * elaboración.
     * @throws ParseException En caso que el valor recibido por formulario no
     * corresponda al formato esperado.
     */
    private Date buildFechaElaboracion(final String fechaElaboracion) throws ParseException {
        return DateUtil.setTime(new SimpleDateFormat(Global.DATE_FORMAT).parse(fechaElaboracion), DateUtil.SetTimeType.START_TIME);
    }

    /**
     * Construye la fecha límite de plazo de construcción del documento acta a
     * partir de la fecha de registro de datos en el sistema, estableciendo la
     * hora de la fecha resultante como 00:00:00.
     *
     * @param fechaHora Fecha y hora de registro de datos.
     * @return Instancia de fecha de límite de plazo.
     */
    private Date buildFechaPlazo(final Date fechaHora) {
        return DateUtil.setTime(DateUtil.add(fechaHora, Calendar.DATE, +diasLimiteFechaPlazo), DateUtil.SetTimeType.START_TIME);
    }

}
