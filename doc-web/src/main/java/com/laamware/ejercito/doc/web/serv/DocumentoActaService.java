package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.DocumentoActaDTO;
import com.laamware.ejercito.doc.web.entity.Clasificacion;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoActa;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.enums.DocumentoActaEstado;
import com.laamware.ejercito.doc.web.enums.DocumentoActaMode;
import com.laamware.ejercito.doc.web.repo.DocumentoActaRepository;
import com.laamware.ejercito.doc.web.util.BusinessLogicValidation;
import com.laamware.ejercito.doc.web.util.DateUtil;
import com.laamware.ejercito.doc.web.util.Global;
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

    @Autowired
    private DocumentoActaRepository documentoActaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DocumentoService documentoService;

    @Autowired
    private TRDService trdService;

    @Autowired
    private ClasificacionService clasificacionService;

    /**
     * Constructor.
     *
     * @param serieActasID ID de la TRD correspondiente a la serie de actas.
     * Cargado por archivo de propiedades.
     * @param diasLimiteFechaElaboracion Número de días para el límite de la
     * fecha de elaboración. Cargado por archivo de propiedades.
     */
    @Autowired
    public DocumentoActaService(
            @Value("${com.mil.imi.sicdi.trd.serie.actas}") Integer serieActasID,
            @Value("${com.mil.imi.sicdi.documento.acta.limite.fecha-elaboracion.dias}") Integer diasLimiteFechaElaboracion) {
        this.serieActasID = serieActasID;
        this.diasLimiteFechaElaboracion = diasLimiteFechaElaboracion;
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
     * Crea un nuevo documento referente al acta e instancia del documento acta.
     *
     * @param procesoInstancia Instancia del proceso.
     * @param usuarioCreador Usuario creador del documento.
     * @return Nueva instancia de documento.
     */
    public Documento crearDocumentoAsociadoActa(final Instancia procesoInstancia, final Usuario usuarioCreador) {
        final Documento documentoAsociado = documentoService.crearDocumento(procesoInstancia, usuarioCreador);

        final DocumentoActa documentoActa = new DocumentoActa(documentoAsociado.getId());
        documentoActaRepository.saveAndFlush(documentoActa);

        return documentoAsociado;
    }

    /**
     * Busca un documento.
     *
     * @param documentoID ID del documento acta.
     * @return Documento acta, o {@code null} en caso de no tener
     * correspondencia en el sistema.
     */
    public Documento buscarDocumentoAsociadoActa(final String documentoID) {
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
     * Busca la información del documento acta.
     *
     * @param documentoID ID del documento.
     * @return Documento acta o {@code null} en caso que no exista
     * correspondencia en el sistema.
     */
    public DocumentoActa buscarDocumentoActa(final String documentoID) {
        return documentoActaRepository.findOne(documentoID);
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

        // asunto
        final String asunto = documentoActaDTO.getAsunto();
        if (asunto == null || asunto.trim().isEmpty()) {
            validation.addError(documentoActaDTO, "asunto", "Debe ingresar un asunto.");
        }

        // lugar
        final String lugar = documentoActaDTO.getLugar();
        if (lugar == null || lugar.trim().isEmpty()) {
            validation.addError(documentoActaDTO, "lugar", "Debe ingresar un lugar.");
        }

        // fechaElaboracion
        final String _fechaElaboracion = documentoActaDTO.getFechaElaboracion();
        if (_fechaElaboracion == null || _fechaElaboracion.trim().isEmpty()) {
            validation.addError(documentoActaDTO, "fechaElaboracion", "Debe seleccionar una fecha de elaboración.");
        }

        try {
            final Date fechaElaboracion = DateUtil.setTime(new SimpleDateFormat(Global.DATE_FORMAT).parse(_fechaElaboracion), DateUtil.SetTimeType.START_TIME);
            final Date fechaElaboracionLimite = DateUtil.setTime(DateUtil.add(new Date(), Calendar.DATE, -diasLimiteFechaElaboracion), DateUtil.SetTimeType.START_TIME);
            if (fechaElaboracion.before(fechaElaboracionLimite)) {
                validation.addError(documentoActaDTO, "fechaElaboracion", "La fecha de elaboración es menor que la fecha límite permitida: "
                        + new SimpleDateFormat(Global.DATE_FORMAT).format(fechaElaboracionLimite));
            }
        } catch (ParseException ex) {
            LOG.log(Level.SEVERE, _fechaElaboracion, ex);
            validation.addError(documentoActaDTO, "fechaElaboracion", "Debe enviar una fecha de elaboración válida.");
        }

        // clasificacion
        final String _clasificacionID = documentoActaDTO.getClasificacion();
        if (_clasificacionID == null || _clasificacionID.trim().isEmpty()) {
            validation.addError(documentoActaDTO, "clasificacion", "Debe seleccionar el nivel de clasificación.");
        }

        try {
            final Integer clasificacionID = Integer.parseInt(_clasificacionID);
            final Clasificacion clasificacion = clasificacionService.findActivo(clasificacionID);
            if (clasificacion == null) {
                validation.addError(documentoActaDTO, "clasificacion", "Debe seleccionar un nivel de clasificación activo.");
            }

            // TODO: Verificar si hay más reglas asociadas a la clasificación seleccionada.
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, _clasificacionID, ex);
            validation.addError(documentoActaDTO, "clasificacion", "Debe enviar una clasificación válida.");
        }

        // trd
        final String _trdID = documentoActaDTO.getTrd();
        if (_trdID == null || _trdID.trim().isEmpty()) {
            validation.addError(documentoActaDTO, "trd", "Debe seleccionar la subserie TRD.");
        }

        try {
            final Integer trdID = Integer.parseInt(_trdID);
            boolean trdValida = trdService.validateSubserieTrdForUser(new Trd(trdID), new Trd(serieActasID), usuario);
            if (!trdValida) {
                validation.addError(documentoActaDTO, "trd", "Debe seleccionar la subserie TRD válida y/o asignada.");
            }
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, _trdID, ex);
            validation.addError(documentoActaDTO, "trd", "Debe enviar una subserie TRD válida.");
        }

        // numeroFolios
        final String _numeroFolios = documentoActaDTO.getNumeroFolios();
        if (_numeroFolios == null || _numeroFolios.trim().isEmpty()) {
            validation.addError(documentoActaDTO, "numeroFolios", "Debe ingresar el número de folios.");
        }

        try {
            final Integer numeroFolios = Integer.parseInt(_numeroFolios);
            if (numeroFolios <= 0) {
                validation.addError(documentoActaDTO, "numeroFolios", "Debe ingresar un número de folios mayor o igual a 1.");
            }
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, _numeroFolios, ex);
            validation.addError(documentoActaDTO, "numeroFolios", "Debe ingresar un número de folios válido.");
        }

        return validation;
    }

}
