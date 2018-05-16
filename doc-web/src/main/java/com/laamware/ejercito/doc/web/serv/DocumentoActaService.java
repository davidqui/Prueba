package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.DocumentoActaDTO;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoActa;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.enums.DocumentoActaEstado;
import com.laamware.ejercito.doc.web.enums.DocumentoActaMode;
import com.laamware.ejercito.doc.web.repo.DocumentoActaRepository;
import com.laamware.ejercito.doc.web.util.BusinessLogicValidation;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private DocumentoActaRepository documentoActaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DocumentoService documentoService;

    @Autowired
    private TRDService trdService;

    /**
     * Constructor.
     *
     * @param serieActasID ID de la TRD correspondiente a la serie de actas.
     * Cargado por archivo de propiedades.
     */
    @Autowired
    public DocumentoActaService(@Value("${com.mil.imi.sicdi.trd.serie.actas}") Integer serieActasID) {
        this.serieActasID = serieActasID;
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
     * @return Resumen del proceso de validación.
     */
    public BusinessLogicValidation validarGuardarActa(final DocumentoActaDTO documentoActaDTO) {
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
        final String fechaElaboracion = documentoActaDTO.getFechaElaboracion();
        if(fechaElaboracion == null || fechaElaboracion.trim().isEmpty()){
            validation.addError(documentoActaDTO, "fechaElaboracion", "Debe seleccionar una fecha de elaboración.");
        }
        
        // clasificacion
        final String clasificacion = documentoActaDTO.getClasificacion();
        if(clasificacion == null || clasificacion.trim().isEmpty()){
            validation.addError(documentoActaDTO, "clasificacion", "Debe seleccionar el nivel de clasificación.");
        }
        
        // trd
        final String trd = documentoActaDTO.getTrd();
        if(trd == null || trd.trim().isEmpty()){
            validation.addError(documentoActaDTO, "trd", "Debe seleccionar la subserie TRD.");
        }
        
        // numeroFolios
        final String numeroFolios = documentoActaDTO.getNumeroFolios();
        if(numeroFolios == null || numeroFolios.trim().isEmpty()){
            validation.addError(documentoActaDTO, "numeroFolios", "Debe ingresar el número de folios.");
        }

        return validation;
    }

}
