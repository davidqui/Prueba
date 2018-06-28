package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.DocumentoActaDTO;
import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.Clasificacion;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.HProcesoInstancia;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.entity.Radicacion;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.entity.UsuarioXDocumentoActa;
import com.laamware.ejercito.doc.web.enums.DocumentoActaEstado;
import com.laamware.ejercito.doc.web.enums.DocumentoActaMode;
import com.laamware.ejercito.doc.web.enums.DocumentoActaUsuarioSeleccion;
import com.laamware.ejercito.doc.web.repo.InstanciaRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioXDocumentoActaRepository;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.BusinessLogicValidation;
import com.laamware.ejercito.doc.web.util.DateUtil;
import com.laamware.ejercito.doc.web.util.Global;
import com.laamware.ejercito.doc.web.util.UsuarioXDocumentoActaComparator;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
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

    private String imagesRoot;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy hh:mm a", new Locale("es", "CO"));

    static {
        final Map<DocumentoActaEstado, DocumentoActaMode> baseMap = new LinkedHashMap<>();
        baseMap.put(DocumentoActaEstado.ACTA_DIGITALIZADA, DocumentoActaMode.SOLO_CONSULTA);
        baseMap.put(DocumentoActaEstado.ANULADO, DocumentoActaMode.SOLO_CONSULTA);
        baseMap.put(DocumentoActaEstado.NUMERO_DE_RADICACION_GENERADO, DocumentoActaMode.CARGA_ACTA_DIGITAL);
        baseMap.put(DocumentoActaEstado.REGISTRO_DE_DATOS_DEL_ACTA, DocumentoActaMode.EDICION_INFORMACION);
        baseMap.put(DocumentoActaEstado.REGISTRO_DE_USUARIOS_DEL_ACTA, DocumentoActaMode.SELECCION_USUARIOS);
        baseMap.put(DocumentoActaEstado.ENVIO_REGISTRO, DocumentoActaMode.SOLO_CONSULTA);
        baseMap.put(DocumentoActaEstado.CARGA_ACTA, DocumentoActaMode.CARGA_ACTA_DIGITAL);
        baseMap.put(DocumentoActaEstado.VALIDAR_ACTA, DocumentoActaMode.SOLO_CONSULTA);

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

    private final Integer[] subseriesActasUsuario_1_1;

    private final Integer[] subseriesActasUsuario_0_0;

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

    @Autowired
    private ArchivoAutomaticoService archivoAutomaticoService;

    @Autowired
    private UsuarioXDocumentoActaRepository usuarioXDocumentoActaRepository;

    @Autowired
    private DocumentoDependenciaService documentoDependenciaService;

    @Autowired
    private InstanciaRepository instanciaRepository;

    @Autowired
    private ProcesoService procesoService;

    @Autowired
    JasperService jasperService;

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
     * @param subseriesActasUsuario_1_1 Arreglo de ID de subseries TRD de actas
     * con selección de mínimo y máximo 1 usuario.
     * @param subseriesActasUsuario_0_0 Arreglo de ID de subseries TRD de actas
     * con selección de mínimo y máximo 0 usuarios.
     */
    @Autowired
    public DocumentoActaService(
            @Value("${com.mil.imi.sicdi.trd.serie.actas}") Integer serieActasID,
            @Value("${com.mil.imi.sicdi.documento.acta.limite.fecha-elaboracion.dias}") Integer diasLimiteFechaElaboracion,
            @Value("${com.mil.imi.sicdi.documento.acta.limite.fecha-plazo.dias}") Integer diasLimiteFechaPlazo,
            @Value("${com.mil.imi.sicdi.trd.subseries.actas.usuario-1-1}") Integer[] subseriesActasUsuario_1_1,
            @Value("${com.mil.imi.sicdi.trd.subseries.actas.usuario-0-0}") Integer[] subseriesActasUsuario_0_0
    ) {
        this.serieActasID = serieActasID;
        this.diasLimiteFechaElaboracion = diasLimiteFechaElaboracion;
        this.diasLimiteFechaPlazo = diasLimiteFechaPlazo;
        this.subseriesActasUsuario_1_1 = subseriesActasUsuario_1_1;
        this.subseriesActasUsuario_0_0 = subseriesActasUsuario_0_0;
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
                } else {
                    final Date fechaSistema = DateUtil.setTime(new Date(), DateUtil.SetTimeType.END_TIME);
                    if (actaFechaElaboracion.after(fechaSistema)) {
                        validation.addError(documentoActaDTO, campo, "La fecha de elaboración es mayor a la fecha del sistema.");
                    }
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

        // descripcion
        campo = "actaDescripcion";
        final String actaDescripcion = documentoActaDTO.getActaDescripcion();
        if (actaDescripcion == null || actaDescripcion.trim().isEmpty()) {
            validation.addError(documentoActaDTO, campo, "Debe ingresar una descripción del acta.");
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
        documento.setActaDescripcion(documentoActaDTO.getActaDescripcion());

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
        documento.setSticker(generaSticker(documento));

        documento.setQuienMod(usuarioSesion.getId());
        documento.setCuandoMod(new Date());

        return documentoService.actualizar(documento);
    }

    /**
     * Genera el sticker en el OFS.
     *
     * @param doc Documento
     * @return el identificador del ofs del sticker del acta
     */
    private String generaSticker(Documento doc) {
        try {
            System.err.println("Genera sticker");
            Map<String, Object> params = new HashMap<String, Object>();
            // params.put("P_DOCUMENTO", doc);
            params.put("radicado", doc.getRadicado() == null ? "" : doc.getRadicado());
            params.put("asunto", doc.getAsunto() == null ? "" : doc.getAsunto());
            params.put("cuando", sdf.format(doc.getActaFechaElaboracion()));
            params.put("elabora", doc.getElabora().getNombre());
            params.put("imagesRoot", imagesRoot);
            List<String> listaUnElemento = new ArrayList<>(1);
            listaUnElemento.add("");
            return jasperService.savePdf("sticker_acta", params, listaUnElemento, null);
        } catch (Exception ex) {
            Logger.getLogger(DocumentoActaService.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
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
     * Digitaliza definitivamente el acta y archiva el documento correspondiente
     * al usuario creador del registro.
     *
     * @param documento Documento.
     * @param procesoInstancia Instancia del proceso.
     * @param usuarioSesion Usuario en sesión.
     * @param procesoTransicionID
     * @return Instancia del proceso modificada por el proceso de
     * digitalización.
     */
    public Instancia digitalizarYArchivarActa(Documento documento, Instancia procesoInstancia, final Usuario usuarioSesion, Integer procesoTransicionID) {
        procesoInstancia.setAsignado(usuarioSesion);
        procesoInstancia.setQuienMod(usuarioSesion.getId());
        procesoInstancia.setCuandoMod(new Date());
        procesoInstancia.forward(procesoTransicionID);

        archivoAutomaticoService.archivarAutomaticamente(documento, usuarioSesion);

        return procesoInstancia;
    }

    /**
     * Busca todos los registros activos de usuarios asignados al documento
     * acta.
     *
     * @param documento Documento acta.
     * @return Lista de todos los registros activos de usuarios asignados al
     * documento acta.
     */
    public List<UsuarioXDocumentoActa> listarRegistrosUsuariosAsignados(final Documento documento) {
        List<UsuarioXDocumentoActa> list = usuarioXDocumentoActaRepository.findAllByActivoTrueAndDocumento(documento);
        Collections.sort(list, new UsuarioXDocumentoActaComparator());
        return list;
    }

    /**
     * Busca todos los registros activos de usuarios asignados al documento
     * acta, incluyendo al usuario que elabora el acta, con fines de consulta.
     *
     * @param documento Documento acta.
     * @return Lista de todos los registros activos de usuarios asignados al
     * documento acta.
     */
    public List<UsuarioXDocumentoActa> listarRegistrosUsuariosAsignadosConsulta(final Documento documento) {
        List<UsuarioXDocumentoActa> list = usuarioXDocumentoActaRepository.findAllByActivoTrueAndDocumento(documento);

        UsuarioXDocumentoActa usuarioElaboraXDocumentoActa = new UsuarioXDocumentoActa();
        usuarioElaboraXDocumentoActa.setUsuario(documento.getElabora());
        usuarioElaboraXDocumentoActa.setCargo(documento.getCargoIdElabora());
        usuarioElaboraXDocumentoActa.setDocumento(documento);
        list.add(usuarioElaboraXDocumentoActa);

        Collections.sort(list, new UsuarioXDocumentoActaComparator());
        return list;
    }

    /**
     * Obtiene el tipo de selección de usuario según la subserie TRD.
     *
     * @param subserieTrdID ID de la subserie TRD.
     * @return Tipo de selección de usuario según la subserie.
     */
    public DocumentoActaUsuarioSeleccion obtenerSeleccionUsuarioSubserieActa(final Integer subserieTrdID) {
        if (esSubserieActaUsuario0_0(subserieTrdID)) {
            return DocumentoActaUsuarioSeleccion.SELECCION_0_0;
        }

        if (esSubserieActaUsuario1_1(subserieTrdID)) {
            return DocumentoActaUsuarioSeleccion.SELECCION_1_1;
        }

        return DocumentoActaUsuarioSeleccion.SELECCION_1_N;
    }

    /**
     * Obtiene el tipo de selección de usuario según la subserie TRD del
     * documento.
     *
     * @param documento Documento.
     * @return Tipo de selección de usuario según la subserie.
     */
    public DocumentoActaUsuarioSeleccion obtenerSeleccionUsuarioSubserieActa(final Documento documento) {
        return obtenerSeleccionUsuarioSubserieActa(documento.getTrd().getId());
    }

    /**
     * Indica si el documento acta debe seleccionar usuarios, según la subserie
     * TRD asociada.
     *
     * @param documento Documento acta.
     * @return {@code true} en caso que la subserie TRD del documento
     * corresponde a una selección de usuarios; de lo contrario, {@code false}.
     */
    public boolean debeSeleccionarUsuarios(final Documento documento) {
        return !obtenerSeleccionUsuarioSubserieActa(documento.getTrd().getId()).equals(DocumentoActaUsuarioSeleccion.SELECCION_0_0);
    }

    /**
     * Asigna un usuario y uno de sus cargos al documento acta.
     *
     * @param documento Documento acta.
     * @param usuario Usuario a asignar.
     * @param cargo Cargo a asignar.
     * @param usuarioSesion Usuario en sesión.
     * @return Instancia del registro.
     */
    public UsuarioXDocumentoActa asignarUsuarioActa(final Documento documento, final Usuario usuario, final Cargo cargo, final Usuario usuarioSesion) {
        final UsuarioXDocumentoActa registroActual = usuarioXDocumentoActaRepository.findByUsuarioAndDocumentoAndActivoTrue(usuario, documento);
        if (registroActual != null) {
            registroActual.setCargo(cargo);
            registroActual.setCuando(new Date());
            registroActual.setQuien(usuarioSesion);

            return usuarioXDocumentoActaRepository.saveAndFlush(registroActual);
        }

        UsuarioXDocumentoActa nuevoRegistro = new UsuarioXDocumentoActa();
        nuevoRegistro.setActivo(Boolean.TRUE);
        nuevoRegistro.setCargo(cargo);
        nuevoRegistro.setCuando(new Date());
        nuevoRegistro.setDocumento(documento);
        nuevoRegistro.setQuien(usuarioSesion);
        nuevoRegistro.setUsuario(usuario);

        return usuarioXDocumentoActaRepository.saveAndFlush(nuevoRegistro);
    }

    /**
     * Elimina, de forma lógica, un registro de usuario para documento acta.
     *
     * @param documento Documento asociado.
     * @param id ID del registro.
     * @param usuarioSesion Usuario en sesión.
     * @return Instancia del registro eliminado.
     * @throws BusinessLogicException En caso que el registro correspondiente al
     * ID no esté asociada al documento.
     */
    public UsuarioXDocumentoActa eliminarUsuarioActa(final Documento documento, final Integer id, final Usuario usuarioSesion) throws BusinessLogicException {
        UsuarioXDocumentoActa usuarioXDocumentoActa = usuarioXDocumentoActaRepository.findOne(id);
        if (!usuarioXDocumentoActa.getDocumento().getId().equals(documento.getId())) {
            throw new BusinessLogicException("Registro de usuario por documento acta no corresponde al documento: " + id + "-" + documento.getId());
        }

        usuarioXDocumentoActa.setActivo(Boolean.FALSE);
        usuarioXDocumentoActa.setCuando(new Date());
        usuarioXDocumentoActa.setQuien(usuarioSesion);

        return usuarioXDocumentoActaRepository.saveAndFlush(usuarioXDocumentoActa);
    }

    /**
     * Valida los usuarios registrados sobre el tipo de selección asociada a la
     * subserie TRD del documento.
     *
     * @param documento Documento.
     * @return Resumen del proceso de validación.
     */
    public BusinessLogicValidation validarRegistroUsuarios(final Documento documento) {
        final List<UsuarioXDocumentoActa> usuariosActa = listarRegistrosUsuariosAsignados(documento);
        final DocumentoActaUsuarioSeleccion usuarioSeleccion = obtenerSeleccionUsuarioSubserieActa(documento);

        final BusinessLogicValidation validation = new BusinessLogicValidation();

        switch (usuarioSeleccion) {
            case SELECCION_0_0: {
                if (!usuariosActa.isEmpty()) {
                    validation.addError(null, null, "La subserie TRD seleccionada no debe tener ningún usuario seleccionado.");
                }
                break;
            }

            case SELECCION_1_1: {
                if (usuariosActa.size() != 1) {
                    validation.addError(null, null, "La subserie TRD seleccionada debe tener únicamente un usuario seleccionado.");
                }
                break;
            }

            case SELECCION_1_N: {
                if (usuariosActa.isEmpty()) {
                    validation.addError(null, null, "La subserie TRD seleccionada debe tener al menos un usuario seleccionado.");
                }
                break;
            }
        }

        return validation;
    }

    /**
     * Indica si el usuario se encuentra asociado al documento acta.
     *
     * @param usuario Usuario.
     * @param documento Documento acta.
     * @return {@code true} si el usuario se encuentra asociado (con registro
     * activo) al documento acta; de lo contrario, {@code false}.
     */
    public boolean esUsuarioAsociadoParaConsulta(final Usuario usuario, final Documento documento) {
        return usuarioXDocumentoActaRepository.findByUsuarioAndDocumentoAndActivoTrue(usuario, documento) != null;
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

    /**
     * Indica si la subserie corresponde a una subserie de acta con seleccion
     * de, máx y mín, 1 usuario.
     *
     * @param subserieTrdID ID de la subserie.
     * @return {@code true} si la subserie corresponde a la lista de subseries
     * con el tipo de selección de usuario; de lo contrario, {@code false}.
     */
    private boolean esSubserieActaUsuario1_1(final Integer subserieTrdID) {
        for (final Integer subserie : subseriesActasUsuario_1_1) {
            if (subserie.equals(subserieTrdID)) {
                return true;
            }
        }

        return false;
    }

    @Value("${docweb.images.root}")
    public void setImagesRoot(String imagesRoot) {
        File file = new File(imagesRoot);
        if (file.exists() == false) {
            file.mkdir();
        }
        this.imagesRoot = imagesRoot;
    }

    /**
     * Indica si la subserie corresponde a una subserie de acta con seleccion
     * de, máx y mín, 0 usuarios.
     *
     * @param subserieTrdID ID de la subserie.
     * @return {@code true} si la subserie corresponde a la lista de subseries
     * con el tipo de selección de usuario; de lo contrario, {@code false}.
     */
    private boolean esSubserieActaUsuario0_0(final Integer subserieTrdID) {
        for (final Integer subserie : subseriesActasUsuario_0_0) {
            if (subserie.equals(subserieTrdID)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Busca un registro de archivo activo para un documento y un usuario
     * asociado.
     *
     * @param documento Documento.
     * @param usuario Usuario.
     * @return Instancia del registro activo de archivo para el documento y el
     * usuario. En caso de no existir correspondencia en el sistema, se retorna
     * {@code null}.
     * @see
     * DocumentoDependenciaService#buscarRegistroActivo(com.laamware.ejercito.doc.web.entity.Documento,
     * com.laamware.ejercito.doc.web.entity.Usuario)
     */
    /*
     * 2018-06-05 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public DocumentoDependencia buscarRegistroArchivoActivo(Documento documento, Usuario usuario) {
        return documentoDependenciaService.buscarRegistroActivo(documento, usuario);
    }

    /**
     * Retorna el ultimo usuario registrado
     *
     * @param documento Utiliza el documento
     * @return usuario de registro
     */
    public Usuario retornaUltimoUsuarioRegistroAsignado(Documento documento) {
        Instancia instancia = instanciaRepository.findOneByIdAndEstadoId(documento.getInstancia().getId(), DocumentoActaEstado.CARGA_ACTA.getId());
        if (instancia == null) {
            List<HProcesoInstancia> hProcesoInstancias = procesoService.getHistoria(documento.getInstancia().getId());
            for (HProcesoInstancia hProcesoInstancia : hProcesoInstancias) {
                if (hProcesoInstancia.getEstado().getId().equals(DocumentoActaEstado.CARGA_ACTA.getId())) {
                    return hProcesoInstancia.getAsignado();
                }
            }
        } else {
            return instancia.getAsignado();
        }
        return null;
    }

}
