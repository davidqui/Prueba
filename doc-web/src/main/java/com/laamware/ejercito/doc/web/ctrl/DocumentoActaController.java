package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.dto.DocumentoActaDTO;
import com.laamware.ejercito.doc.web.dto.DocumentoObservacionDTO;
import com.laamware.ejercito.doc.web.entity.Adjunto;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Tipologia;
import com.laamware.ejercito.doc.web.entity.Transicion;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.entity.UsuarioXDocumentoActa;
import com.laamware.ejercito.doc.web.enums.DocumentoActaEstado;
import com.laamware.ejercito.doc.web.serv.AdjuntoService;
import com.laamware.ejercito.doc.web.serv.CargoService;
import com.laamware.ejercito.doc.web.serv.ClasificacionService;
import com.laamware.ejercito.doc.web.serv.DocumentoActaService;
import com.laamware.ejercito.doc.web.serv.DocumentoObservacionDefectoService;
import com.laamware.ejercito.doc.web.serv.DocumentoObservacionService;
import com.laamware.ejercito.doc.web.serv.ProcesoService;
import com.laamware.ejercito.doc.web.serv.TipologiaService;
import com.laamware.ejercito.doc.web.serv.TransicionService;
import com.laamware.ejercito.doc.web.serv.UsuarioService;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.BusinessLogicValidation;
import com.laamware.ejercito.doc.web.util.Global;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para proceso documental de "Registro de Acta".
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/15/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
@Controller
@RequestMapping(DocumentoActaController.PATH)
public class DocumentoActaController extends UtilController {

    private static final Logger LOG = Logger.getLogger(DocumentoActaController.class.getName());

    /**
     * Ruta principal.
     */
    static final String PATH = "/documento-acta";

    private static final String REDIRECT_MAIN_URL = "redirect:/";

    private static final String DOCUMENTO_ACTA_REGISTRAR_TEMPLATE = "documento-acta-registro";

    private static final String DOCUMENTO_ACTA_CARGAR_TEMPLATE = "documento-acta-carga";

    private static final String DOCUMENTO_ACTA_CONSULTAR_TEMPLATE = "documento-acta-consultar";

    private static final String DOCUMENTO_ACTA_USUARIOS_TEMPLATE = "documento-acta-usuarios";

    private static final String DOCUMENTO_ACTA_ENVIO_REGISTRO_TEMPLATE = "documento-acta-envioRegistro";
    
    private static final String DOCUMENTO_ACTA_VALIDAR_TEMPLATE = "documento-acta-validar";

    private static final String SECURITY_DENIED_TEMPLATE = "security-denied";

    private static final String REDIRECT_ACCESO_DENEGADO_URL = "redirect:/documento/acceso-denegado";

    private static final String REDIRECT_PROCESO_INSTANCIA_URL_FORMAT = "redirect:" + ProcesoController.PATH + "/instancia?pin=%s";
    
    public static final String VARIABLE_STICKER = "doc.sticker";

    @Autowired
    private DocumentoActaService actaService;

    @Autowired
    private ProcesoService procesoService;

    @Autowired
    private ClasificacionService clasificacionService;

    @Autowired
    private CargoService cargoService;

    @Autowired
    private DocumentoObservacionService observacionService;

    @Autowired
    private TransicionService transicionService;

    @Autowired
    private TipologiaService tipologiaService;

    @Autowired
    private AdjuntoService adjuntoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DocumentoObservacionDefectoService observacionDefectoService;

    @Override
    public String nombre(Integer idUsuario) {
        return super.nombre(idUsuario);
    }

    /**
     * Procesa el documento de acta, según la transición a aplicar.
     *
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @param uiModel Modelo de UI.
     * @param principal Información de sesión.
     * @param redirectAttributes Atributos de redirección.
     * @return Nombre del template a presentar en la UI.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String procesar(@RequestParam("pin") final String procesoInstanciaID, Model uiModel, Principal principal, RedirectAttributes redirectAttributes) {
        final Usuario usuarioSesion = getUsuario(principal);
        final boolean tieneAccesoPorAsignacion = actaService.verificaAccesoDocumentoActa(usuarioSesion, procesoInstanciaID);
        if (!tieneAccesoPorAsignacion) {
            return SECURITY_DENIED_TEMPLATE;
        }

        Instancia procesoInstancia = procesoService.instancia(procesoInstanciaID);
        if (procesoInstancia.getEstado().getId().equals(DocumentoActaEstado.ANULADO.getId())) {
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "El acta seleccionada se encuentra anulada y no puede ser consultada.");
            return REDIRECT_MAIN_URL;
        }

        final boolean tieneAccesoPorClasificacion = actaService.tieneAccesoPorClasificacion(usuarioSesion, procesoInstancia);
        if (!tieneAccesoPorClasificacion) {
            return REDIRECT_ACCESO_DENEGADO_URL;
        }

        String documentoID = procesoInstancia.getVariable(Documento.DOC_ID);

        final Documento documento;
        if (documentoID == null) {
            documento = actaService.crearDocumento(procesoInstancia, usuarioSesion);
            procesoInstancia = procesoService.instancia(procesoInstanciaID);
        } else {
            documento = actaService.buscarDocumento(documentoID);
        }

        cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

        if (procesoInstancia.getEstado().getId().equals(DocumentoActaEstado.ACTA_DIGITALIZADA.getId())) {
            return DOCUMENTO_ACTA_CONSULTAR_TEMPLATE;
        }

        return DOCUMENTO_ACTA_REGISTRAR_TEMPLATE;
    }

    /**
     * Procesa la información proveniente del formulario y guarda la misma en
     * los registros del documento, la instancia del proceso y el acta.
     *
     * @param documentoActaDTO DTO del documento acta.
     * @param uiModel Modelo de UI.
     * @param principal Información de sesión.
     * @return Nombre del template a presentar en la UI.
     */
    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public String guardar(DocumentoActaDTO documentoActaDTO, Model uiModel, Principal principal) {
        final Usuario usuarioSesion = getUsuario(principal);

        final String docId = documentoActaDTO.getDocId();
        Documento documento = actaService.buscarDocumento(docId);
        Instancia procesoInstancia = procesoService.instancia(documento.getInstancia().getId());

        final BusinessLogicValidation logicValidation = actaService.validarGuardarActa(documentoActaDTO, usuarioSesion);
        if (!logicValidation.isAllOK()) {
            cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

            uiModel.addAttribute("documentoActaDTO", documentoActaDTO);
            uiModel.addAttribute("logicValidation", logicValidation);
            uiModel.addAttribute(AppConstants.FLASH_ERROR, "Existen errores en el formulario.");
            return DOCUMENTO_ACTA_REGISTRAR_TEMPLATE;
        }

        try {
            documento = actaService.guardarRegistroDatos(documentoActaDTO, usuarioSesion);
        } catch (ParseException ex) {
            LOG.log(Level.SEVERE, documentoActaDTO.toString(), ex);

            cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

            uiModel.addAttribute("documentoActaDTO", documentoActaDTO);
            uiModel.addAttribute(AppConstants.FLASH_ERROR, "Excepción: Error registrando datos del acta: " + ex.getMessage());
            return DOCUMENTO_ACTA_REGISTRAR_TEMPLATE;
        }

        cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

        return DOCUMENTO_ACTA_REGISTRAR_TEMPLATE;
    }

    /**
     * Aplica la anulación del acta.
     *
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @param procesoTransicionID ID de la transición del proceso a aplicar.
     * @param principal Información de sesión.
     * @param redirectAttributes Atributos de redirección.
     * @return URL de redirección tras aplicada la transición.
     */
    @RequestMapping(value = "/anular", method = RequestMethod.GET)
    public String anular(@RequestParam("pin") String procesoInstanciaID, @RequestParam("tid") Integer procesoTransicionID, Principal principal, RedirectAttributes redirectAttributes) {
        final Usuario usuarioSesion = getUsuario(principal);

        Instancia procesoInstancia = procesoService.instancia(procesoInstanciaID);
        final String documentoID = procesoInstancia.getVariable(Documento.DOC_ID);
        final Documento documento = actaService.buscarDocumento(documentoID);

        procesoInstancia.setAsignado(usuarioSesion);
        procesoInstancia.setQuienMod(usuarioSesion.getId());
        procesoInstancia.setCuandoMod(new Date());
        procesoInstancia.forward(procesoTransicionID);

        redirectAttributes.addFlashAttribute(AppConstants.FLASH_SUCCESS, "El acta \"" + documento.getAsunto() + "\" ha sido anulado exitosamente.");

        return REDIRECT_MAIN_URL;
    }

    /**
     * Registra una nueva observación al acta.
     *
     * @param observacionDTO DTO con la información de la observación.
     * @param principal Información de sesión.
     * @return Entidad de respuesta con el código HTTP correspondiente.
     */
    @ResponseBody
    @RequestMapping(value = "/observacion", method = RequestMethod.POST)
    public ResponseEntity<?> registrarObservacion(final DocumentoObservacionDTO observacionDTO, Principal principal) {
        final Usuario usuarioSesion = getUsuario(principal);

        final String documentoID = observacionDTO.getDocumentoID();
        if (documentoID == null || documentoID.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Debe indicar el ID del documento.");
        }

        final String observacion = observacionDTO.getObservacionTexto();
        if (observacion == null || observacion.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Debe enviar la observación a registrar.");
        }

        final Documento documento = actaService.buscarDocumento(documentoID);
        if (documento == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ID de documento no válido.");
        }

        observacionService.crearObservacion(documento, observacion, usuarioSesion);

        return ResponseEntity.ok("Comentario registrado exitosamente.");
    }

    /**
     * Genera el número de radicado para el acta y carga la información
     * necesaria en el modelo de UI para la visualización del resultado y
     * presentación del formulario de carga.
     *
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @param procesoTransicionID ID de la transición del proceso a aplicar.
     * @param uiModel Modelo de UI.
     * @param principal Información de sesión.
     * @param redirectAttributes Atributos de redirección.
     * @return Nombre del template a presentar en caso de éxito o, URL de
     * redirección en caso de error.
     */
    @RequestMapping(value = "/generar-numero-radicado", method = RequestMethod.GET)
    public String generarNumeroRadicado(@RequestParam("pin") String procesoInstanciaID, @RequestParam(value = "tid", required = false) Integer procesoTransicionID, Model uiModel, Principal principal, RedirectAttributes redirectAttributes) {
        final Usuario usuarioSesion = getUsuario(principal);
        final boolean tieneAccesoPorAsignacion = actaService.verificaAccesoDocumentoActa(usuarioSesion, procesoInstanciaID);
        if (!tieneAccesoPorAsignacion) {
            return SECURITY_DENIED_TEMPLATE;
        }

        Instancia procesoInstancia = procesoService.instancia(procesoInstanciaID);
        if (procesoInstancia.getEstado().getId().equals(DocumentoActaEstado.ANULADO.getId())) {
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "El acta seleccionada se encuentra anulada y no puede ser consultada.");
            return REDIRECT_MAIN_URL;
        }

        final boolean tieneAccesoPorClasificacion = actaService.tieneAccesoPorClasificacion(usuarioSesion, procesoInstancia);
        if (!tieneAccesoPorClasificacion) {
            return REDIRECT_ACCESO_DENEGADO_URL;
        }

        if (procesoTransicionID != null) {
            final Transicion transicion = transicionService.find(procesoTransicionID);
            if (transicion == null || !transicion.getActivo()) {
                redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "Transición no válida.");
                return REDIRECT_MAIN_URL;
            }
        }

        final String documentoID = procesoInstancia.getVariable(Documento.DOC_ID);
        Documento documento = actaService.buscarDocumento(documentoID);

        final BusinessLogicValidation validation = actaService.validarRegistroUsuarios(documento);
        if (!validation.isAllOK()) {
            uiModel.addAttribute(AppConstants.FLASH_ERROR, validation.errorsToString());

            cargarInformacionSeleccionUsuariosUIModel(uiModel, documento);
            cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

            return DOCUMENTO_ACTA_USUARIOS_TEMPLATE;
        }

        if (documento.getRadicado() == null || documento.getRadicado().trim().isEmpty()) {
            documento = actaService.asignarNumeroRadicacion(documento, usuarioSesion);

            procesoInstancia.setAsignado(usuarioSesion);
            procesoInstancia.setQuienMod(usuarioSesion.getId());
            procesoInstancia.setCuandoMod(new Date());
            procesoInstancia.forward(procesoTransicionID);

            uiModel.addAttribute(AppConstants.FLASH_SUCCESS, "Ha sido asignado el número de radicación \"" + documento.getRadicado() + "\" al acta \"" + documento.getAsunto() + "\".");
        }

        cargarInformacionSeleccionUsuariosUIModel(uiModel, documento);
        cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

        return DOCUMENTO_ACTA_ENVIO_REGISTRO_TEMPLATE;
    }

    /**
     * Carga el archivo digital correspondiente al acta.
     *
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @param multipartFile Archivo multiparte correspondiente al acta digital.
     * @param uiModel Modelo de UI.
     * @param principal Información de sesión.
     * @param redirectAttributes Atributos de redirección.
     * @return Nombre del template a presentar en caso de éxito o, URL de
     * redirección en caso de error.
     */
    @RequestMapping(value = "/cargar-acta-digital", method = RequestMethod.POST)
    public String cargarActaDigital(@RequestParam("pin") String procesoInstanciaID, @RequestParam("archivo") MultipartFile multipartFile, Model uiModel, Principal principal, RedirectAttributes redirectAttributes) {
        System.err.println("/cargar-acta-digital");
        final Usuario usuarioSesion = getUsuario(principal);
        final boolean tieneAccesoPorAsignacion = actaService.verificaAccesoDocumentoActa(usuarioSesion, procesoInstanciaID);
        if (!tieneAccesoPorAsignacion) {
            return SECURITY_DENIED_TEMPLATE;
        }

        Instancia procesoInstancia = procesoService.instancia(procesoInstanciaID);
        if (procesoInstancia.getEstado().getId().equals(DocumentoActaEstado.ANULADO.getId())) {
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "El acta seleccionada se encuentra anulada y no puede ser consultada.");
            return REDIRECT_MAIN_URL;
        }

        final boolean tieneAccesoPorClasificacion = actaService.tieneAccesoPorClasificacion(usuarioSesion, procesoInstancia);
        if (!tieneAccesoPorClasificacion) {
            return REDIRECT_ACCESO_DENEGADO_URL;
        }

        final String documentoID = procesoInstancia.getVariable(Documento.DOC_ID);
        Documento documento = actaService.buscarDocumento(documentoID);

        if (multipartFile.getSize() == 0) {
            cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

            uiModel.addAttribute(AppConstants.FLASH_ERROR, "Debe cargar un archivo.");
            return DOCUMENTO_ACTA_CARGAR_TEMPLATE;
        }

        if (!multipartFile.getContentType().equals(Global.PDF_FILE_CONTENT_TYPE)) {
            cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

            uiModel.addAttribute(AppConstants.FLASH_ERROR, "Únicamente se permiten cargar archivos en formato PDF como actas digitalizadas.");
            return DOCUMENTO_ACTA_CARGAR_TEMPLATE;
        }

        try {
            documento = actaService.cargarActaDigitalizada(documento, multipartFile, usuarioSesion);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, procesoInstanciaID + "\t" + multipartFile.getName(), ex);

            cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

            uiModel.addAttribute(AppConstants.FLASH_ERROR, "ERROR: Se presentó la siguiente excepción al cargar el archivo: " + ex.getMessage());
            return DOCUMENTO_ACTA_CARGAR_TEMPLATE;
        }

        cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

        return DOCUMENTO_ACTA_CARGAR_TEMPLATE;
    }

    /**
     * Carga y guarda un archivo adjunto, asociado al acta.
     *
     * @param documentoID ID del documento.
     * @param tipologiaID ID de al tipología del adjunto.
     * @param archivo Archivo multiparte correspondiente al adjunto.
     * @param uiModel Modelo de UI.
     * @param principal Información de sesión.
     * @param redirectAttributes Atributos de redirección.
     * @return Nombre del template a presentar en caso de éxito o, URL de
     * redirección en caso de error.
     */
    @RequestMapping(value = "/adjunto/{docid}/guardar", method = RequestMethod.POST)
    public String cargarGuardarAdjunto(@PathVariable("docid") String documentoID, @RequestParam("tipologia") Integer tipologiaID, @RequestParam("archivo") MultipartFile archivo, Model uiModel, Principal principal, RedirectAttributes redirectAttributes) {
        final Usuario usuarioSesion = getUsuario(principal);

        Documento documento = actaService.buscarDocumento(documentoID);
        Instancia procesoInstancia = procesoService.instancia(documento.getInstancia().getId());

        cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

        if (archivo.isEmpty()) {
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "ERROR: Debe seleccionar un archivo adjunto.");
            return redirectProcesoInstanciaURL(documento);
        }

        if (tipologiaID == null || tipologiaID <= 0) {
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "ERROR: Debe seleccionar una tipología para el archivo adjunto.");
            return redirectProcesoInstanciaURL(documento);
        }

        final Tipologia tipologia = tipologiaService.find(tipologiaID);
        if (tipologia == null || !tipologia.getActivo()) {
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "ERROR: Debe seleccionar una tipología activa para el sistema.");
            return redirectProcesoInstanciaURL(documento);
        }

        final String contentType = archivo.getContentType();
        boolean validAttachmentContentType = adjuntoService.isValidAttachmentContentType(contentType);
        if (!validAttachmentContentType) {
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "ERROR: Únicamente se permiten cargar archivos adjuntos tipo PDF, JPG o PNG.");
            return redirectProcesoInstanciaURL(documento);
        }

        try {
            final Adjunto adjunto = adjuntoService.guardarAdjunto(documento, tipologia, usuarioSesion, archivo);

            redirectAttributes.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Archivo adjuntado: " + adjunto.getOriginal());
            return redirectProcesoInstanciaURL(documento);
        } catch (IOException | RuntimeException ex) {
            LOG.log(Level.SEVERE, documentoID, ex);

            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "No se adjuntó el archivo: Ocurrio un error inesperado.");
            return redirectProcesoInstanciaURL(documento);
        }
    }

    /**
     * Elimina un archivo adjunto, asociado a un documento acta de la instancia
     * del proceso.
     *
     * @param documentoAdjuntoID ID del adjunto.
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @param uiModel Modelo de UI.
     * @param principal Información de sesión.
     * @param redirectAttributes Atributos de redirección.
     * @return Nombre del template a presentar en caso de éxito o, URL de
     * redirección en caso de error.
     */
    @RequestMapping(value = "/adjunto/{dad}/{pin}/eliminar", method = RequestMethod.DELETE)
    public String adjuntoEliminar(@PathVariable("dad") String documentoAdjuntoID, @PathVariable("pin") String procesoInstanciaID, Model uiModel, RedirectAttributes redirectAttributes, Principal principal) {
        final Usuario usuarioSesion = getUsuario(principal);

        Instancia procesoInstancia = procesoService.instancia(procesoInstanciaID);

        final String documentoID = procesoInstancia.getVariable(Documento.DOC_ID);
        Documento documento = actaService.buscarDocumento(documentoID);

        cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

        final Adjunto adjunto = adjuntoService.findByIDActivoAndDocumento(documentoAdjuntoID, documento);
        if (adjunto == null) {
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "ERROR: No hay correspondencia en el sistema para el adjunto seleccionado.");
            return redirectProcesoInstanciaURL(documento);
        }

        try {
            adjuntoService.eliminarAdjunto(adjunto, usuarioSesion);

            redirectAttributes.addFlashAttribute(AppConstants.FLASH_SUCCESS, "El adjunto se ha eliminado.");
            return redirectProcesoInstanciaURL(documento);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, documentoAdjuntoID, ex);

            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "El adjunto no se encuentra o no se puede borrar.");
            return redirectProcesoInstanciaURL(documento);
        }
    }

    /**
     * Carga el acta digitalizada al sistema de forma definitiva, archivando el
     * acta al usuario creador del documento.
     *
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @param procesoTransicionID ID de la transición del proceso a aplicar.
     * @param uiModel Modelo de UI.
     * @param principal Información de sesión.
     * @param redirectAttributes Atributos de redirección.
     * @return Nombre del template a presentar en caso de éxito o, URL de
     * redirección en caso de error.
     */
    @RequestMapping(value = "/cargar-acta-digitalizada", method = RequestMethod.GET)
    public String cargarActaDigitalizada(@RequestParam("pin") String procesoInstanciaID, @RequestParam(value = "tid", required = false) Integer procesoTransicionID, Model uiModel, Principal principal, RedirectAttributes redirectAttributes) {
        final Usuario usuarioSesion = getUsuario(principal);

        Instancia procesoInstancia = procesoService.instancia(procesoInstanciaID);
        final String documentoID = procesoInstancia.getVariable(Documento.DOC_ID);
        Documento documento = actaService.buscarDocumento(documentoID);

        final boolean tieneAccesoPorAsignacion = actaService.verificaAccesoDocumentoActa(usuarioSesion, procesoInstanciaID);
        if (!tieneAccesoPorAsignacion) {
            final boolean puedeConsultarPorAsociacionOArchivo = puedeConsultarPorAsociacionOArchivo(usuarioSesion, documento);
            if (!puedeConsultarPorAsociacionOArchivo) {
                return SECURITY_DENIED_TEMPLATE;
            }
        }

        if (procesoInstancia.getEstado().getId().equals(DocumentoActaEstado.ANULADO.getId())) {
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "El acta seleccionada se encuentra anulada y no puede ser consultada.");
            return REDIRECT_MAIN_URL;
        }

        final boolean tieneAccesoPorClasificacion = actaService.tieneAccesoPorClasificacion(usuarioSesion, procesoInstancia);
        if (!tieneAccesoPorClasificacion) {
            return REDIRECT_ACCESO_DENEGADO_URL;
        }

        if (procesoInstancia.getEstado().getId().equals(DocumentoActaEstado.ACTA_DIGITALIZADA.getId())) {
            cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);
            return DOCUMENTO_ACTA_CONSULTAR_TEMPLATE;
        }

        procesoInstancia = actaService.digitalizarYArchivarActa(documento, procesoInstancia, usuarioSesion, procesoTransicionID);

        cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

        uiModel.addAttribute(AppConstants.FLASH_SUCCESS, "El acta ha sido digitalizada y archivada.");

        return DOCUMENTO_ACTA_CONSULTAR_TEMPLATE;
    }

    /**
     * Procesa el registro de información del acta y presenta el formulario de
     * selección de usuarios, según la TRD y nivel de clasificación asignadas al
     * acta.
     *
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @param procesoTransicionID ID de la transición del proceso a aplicar.
     * @param uiModel Modelo de UI.
     * @param principal Información de sesión.
     * @param redirectAttributes Atributos de redirección.
     * @return Nombre del template a presentar en caso de éxito o, URL de
     * redirección en caso de error.
     */
    @RequestMapping(value = "/seleccionar-usuarios", method = RequestMethod.GET)
    public String seleccionarUsuarios(@RequestParam("pin") String procesoInstanciaID, @RequestParam(value = "tid", required = false) Integer procesoTransicionID, Model uiModel,
            Principal principal, RedirectAttributes redirectAttributes) {
        final Usuario usuarioSesion = getUsuario(principal);
        final boolean tieneAccesoPorAsignacion = actaService.verificaAccesoDocumentoActa(usuarioSesion, procesoInstanciaID);
        if (!tieneAccesoPorAsignacion) {
            return SECURITY_DENIED_TEMPLATE;
        }

        Instancia procesoInstancia = procesoService.instancia(procesoInstanciaID);
        if (procesoInstancia.getEstado().getId().equals(DocumentoActaEstado.ANULADO.getId())) {
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "El acta seleccionada se encuentra anulada y no puede ser consultada.");
            return REDIRECT_MAIN_URL;
        }

        final boolean tieneAccesoPorClasificacion = actaService.tieneAccesoPorClasificacion(usuarioSesion, procesoInstancia);
        if (!tieneAccesoPorClasificacion) {
            return REDIRECT_ACCESO_DENEGADO_URL;
        }

        if (procesoTransicionID != null) {
            final Transicion transicion = transicionService.find(procesoTransicionID);
            if (transicion == null || !transicion.getActivo()) {
                redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "Transición no válida.");
                return REDIRECT_MAIN_URL;
            }
        }

        final String documentoID = procesoInstancia.getVariable(Documento.DOC_ID);
        Documento documento = actaService.buscarDocumento(documentoID);

        if (!procesoInstancia.getEstado().getId().equals(DocumentoActaEstado.REGISTRO_DE_USUARIOS_DEL_ACTA.getId())) {
            procesoInstancia.setAsignado(usuarioSesion);
            procesoInstancia.setQuienMod(usuarioSesion.getId());
            procesoInstancia.setCuandoMod(new Date());
            procesoInstancia.forward(procesoTransicionID);

            procesoInstancia = procesoService.instancia(procesoInstanciaID);

            uiModel.addAttribute(AppConstants.FLASH_SUCCESS, "La información básica del acta \"" + documento.getAsunto() + "\" ha sido registrada.");
        }

        cargarInformacionSeleccionUsuariosUIModel(uiModel, documento);
        cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

        return DOCUMENTO_ACTA_USUARIOS_TEMPLATE;
    }

    /**
     * Registra la lista de usuarios asociados al acta.
     *
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @param uiModel Modelo de UI.
     * @param principal Información de sesión.
     * @param redirectAttributes Atributos de redirección.
     * @return Nombre del template a presentar en caso de éxito o, URL de
     * redirección en caso de error.
     */
    @RequestMapping(value = "/registrar-usuarios", method = RequestMethod.POST)
    public String registrarUsuarios(@RequestParam("pin") String procesoInstanciaID, Model uiModel, Principal principal, RedirectAttributes redirectAttributes) {
        final Instancia procesoInstancia = procesoService.instancia(procesoInstanciaID);
        final String documentoID = procesoInstancia.getVariable(Documento.DOC_ID);
        final Documento documento = actaService.buscarDocumento(documentoID);

        final BusinessLogicValidation validation = actaService.validarRegistroUsuarios(documento);
        if (validation.isAllOK()) {
            uiModel.addAttribute(AppConstants.FLASH_SUCCESS, "Se han registrado los usuarios seleccionados al acta.");
        } else {
            uiModel.addAttribute(AppConstants.FLASH_ERROR, validation.errorsToString());
        }

        final Usuario usuarioSesion = getUsuario(principal);

        cargarInformacionSeleccionUsuariosUIModel(uiModel, documento);
        cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

        return DOCUMENTO_ACTA_USUARIOS_TEMPLATE;
    }

    /**
     * Asigna un usuario, con un cargo correspondiente, al documento acta.
     *
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @param usuarioID ID del usuario a asignar.
     * @param cargoID ID del cargo seleccionado para el usuario a asignar.
     * @param principal Información de sesión.
     * @return ID del nuevo registro.
     */
    @ResponseBody
    @RequestMapping(value = "/asignar-usuario-acta/{pin}/{usuarioID}/{cargoID}", method = RequestMethod.POST)
    public ResponseEntity<?> asignarUsuarioActa(@PathVariable("pin") String procesoInstanciaID, @PathVariable("usuarioID") Integer usuarioID,
            @PathVariable("cargoID") Integer cargoID, Principal principal) {
        final Instancia procesoInstancia = procesoService.instancia(procesoInstanciaID);
        final String documentoID = procesoInstancia.getVariable(Documento.DOC_ID);
        final Documento documento = actaService.buscarDocumento(documentoID);

        final Usuario usuario = usuarioService.findOne(usuarioID);
        final Cargo cargo = cargoService.findOne(cargoID);
        final Usuario usuarioSesion = getUsuario(principal);

        final UsuarioXDocumentoActa usuarioActa = actaService.asignarUsuarioActa(documento, usuario, cargo, usuarioSesion);

        return ResponseEntity.ok(usuarioActa.getId());
    }

    /**
     * Elimina la asignación de un usuario a un documento acta.
     *
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @param registroID ID del registro de asociación a eliminar.
     * @param principal Información de sesión.
     * @return ID del registro eliminado.
     */
    @ResponseBody
    @RequestMapping(value = "/eliminar-usuario-acta/{pin}/{registroID}", method = RequestMethod.DELETE)
    public ResponseEntity<?> eliminarUsuarioActa(@PathVariable("pin") String procesoInstanciaID, @PathVariable("registroID") Integer registroID, Principal principal) {
        final Instancia procesoInstancia = procesoService.instancia(procesoInstanciaID);
        final String documentoID = procesoInstancia.getVariable(Documento.DOC_ID);
        final Documento documento = actaService.buscarDocumento(documentoID);

        final Usuario usuarioSesion = getUsuario(principal);

        try {
            final UsuarioXDocumentoActa usuarioXDocumentoActa = actaService.eliminarUsuarioActa(documento, registroID, usuarioSesion);

            return ResponseEntity.ok(usuarioXDocumentoActa.getId());
        } catch (BusinessLogicException ex) {
            LOG.log(Level.SEVERE, procesoInstanciaID + "\t" + registroID, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /**
     * Indica si el usuario puede consultar el documento acta por asociación en
     * el registro de acta o porque tiene el acta en su archivo.
     *
     * @param usuario Usuario.
     * @param documento Documento acta.
     * @return {@code true} si el usuario puede consultar el documento; de lo
     * contrario, {@code false}.
     */
    private boolean puedeConsultarPorAsociacionOArchivo(final Usuario usuario, final Documento documento) {
        final boolean esUsuarioAsociadoParaConsulta = actaService.esUsuarioAsociadoParaConsulta(usuario, documento);
        if (esUsuarioAsociadoParaConsulta) {
            return esUsuarioAsociadoParaConsulta;
        }

        final DocumentoDependencia registroArchivoActivo = actaService.buscarRegistroArchivoActivo(documento, usuario);
        return registroArchivoActivo != null;
    }

    /**
     * Carga la información básica y necesaria para la construcción de las
     * interfaces gráficas.
     *
     * @param uiModel Modelo de UI.
     * @param documento Documento.
     * @param procesoInstancia Instancia del proceso.
     * @param usuarioSesion Usuario en sesión.
     */
    private void cargarInformacionBasicaUIModel(final Model uiModel, final Documento documento, final Instancia procesoInstancia, final Usuario usuarioSesion) {
        uiModel.addAttribute("documento", documento);
        uiModel.addAttribute("procesoInstancia", procesoInstancia);
        uiModel.addAttribute("usuarioSesion", usuarioSesion);
        uiModel.addAttribute("estadoModeMap", DocumentoActaService.ESTADO_MODE_MAP_FOR_UI);
        uiModel.addAttribute("clasificaciones", clasificacionService.listarClasificacionesActivasYAutorizadas(usuarioSesion));
        uiModel.addAttribute("subseriesTrdActas", actaService.buscarSubseriesActasPorUsuario(usuarioSesion));
        uiModel.addAttribute("cargosUsuario", cargoService.buildCargosXUsuario(usuarioSesion));
        uiModel.addAttribute("documentoObservaciones", observacionService.findAllByDocumento(documento));
        uiModel.addAttribute("tipologias", tipologiaService.listarActivas());
        uiModel.addAttribute("usuariosAsignados", actaService.listarRegistrosUsuariosAsignados(documento));
        uiModel.addAttribute("observacionesDefecto", observacionDefectoService.listarActivas());
        uiModel.addAttribute("usuariosAsignadosConsulta", actaService.listarRegistrosUsuariosAsignadosConsulta(documento));
        uiModel.addAttribute("usuarioRegistro",actaService.retornaUltimoUsuarioRegistroAsignado(documento));
        uiModel.addAttribute("sticker",procesoInstancia.findVariable(DocumentoActaController.VARIABLE_STICKER));
    }

    /**
     * Construye la URL de redirección a la pantalla asignada para la instancia
     * del proceso del documento.
     *
     * @param documento Documento.
     * @return URL de la redirección a la pantalla asignada.
     */
    private String redirectProcesoInstanciaURL(Documento documento) {
        return String.format(REDIRECT_PROCESO_INSTANCIA_URL_FORMAT, documento.getInstancia().getId());
    }

    /**
     * Carga la información de selección de usuarios necesaria para la
     * construcción de las interfaces gráficas asociadas.
     *
     * @param uiModel Modelo de UI.
     * @param documento Documento.
     */
    private void cargarInformacionSeleccionUsuariosUIModel(final Model uiModel, final Documento documento) {
        uiModel.addAttribute("debeSeleccionarUsuarios", actaService.debeSeleccionarUsuarios(documento));
        uiModel.addAttribute("seleccionUsuarioSubserieActa", actaService.obtenerSeleccionUsuarioSubserieActa(documento).name());
    }

    /**
     * Envia al usuario de registro el acta.
     *
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @param procesoTransicionID ID de la transición del proceso a aplicar.
     * @param uiModel Modelo de UI.
     * @param principal Información de sesión.
     * @param redirectAttributes Atributos de redirección.
     * @return Nombre del template a presentar en caso de éxito o, URL de
     * redirección en caso de error.
     */
    @RequestMapping(value = "/usuario-registro", method = RequestMethod.GET)
    public String usuarioRegistro(@RequestParam("pin") String procesoInstanciaID, @RequestParam(value = "tid", required = false) Integer procesoTransicionID, Model uiModel, Principal principal, RedirectAttributes redirectAttributes) {
        final Usuario usuarioSesion = getUsuario(principal);
        final boolean tieneAccesoPorAsignacion = actaService.verificaAccesoDocumentoActa(usuarioSesion, procesoInstanciaID);
        if (!tieneAccesoPorAsignacion) {
            return SECURITY_DENIED_TEMPLATE;
        }

        Instancia procesoInstancia = procesoService.instancia(procesoInstanciaID);
        if (procesoInstancia.getEstado().getId().equals(DocumentoActaEstado.ANULADO.getId())) {
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "El acta seleccionada se encuentra anulada y no puede ser consultada.");
            return REDIRECT_MAIN_URL;
        }

        final boolean tieneAccesoPorClasificacion = actaService.tieneAccesoPorClasificacion(usuarioSesion, procesoInstancia);
        if (!tieneAccesoPorClasificacion) {
            return REDIRECT_ACCESO_DENEGADO_URL;
        }

        final String documentoID = procesoInstancia.getVariable(Documento.DOC_ID);
        Documento documento = actaService.buscarDocumento(documentoID);

        cargarInformacionSeleccionUsuariosUIModel(uiModel, documento);
        cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

        return DOCUMENTO_ACTA_ENVIO_REGISTRO_TEMPLATE;
    }

    /**
     * Envia al usuario de registro el acta.
     *
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @param procesoTransicionID ID de la transición del proceso a aplicar.
     * @param uiModel Modelo de UI.
     * @param principal Información de sesión.
     * @param redirectAttributes Atributos de redirección.
     * @return Nombre del template a presentar en caso de éxito o, URL de
     * redirección en caso de error.
     */
    @RequestMapping(value = "/enviar-registro", method = RequestMethod.GET)
    public String enviarUsuarioRegistro(@RequestParam("pin") String procesoInstanciaID, @RequestParam(value = "tid", required = false) Integer procesoTransicionID, Model uiModel, Principal principal, RedirectAttributes redirectAttributes) {
        final Usuario usuarioSesion = getUsuario(principal);
        final boolean tieneAccesoPorAsignacion = actaService.verificaAccesoDocumentoActa(usuarioSesion, procesoInstanciaID);
        if (!tieneAccesoPorAsignacion) {
            return SECURITY_DENIED_TEMPLATE;
        }

        Instancia procesoInstancia = procesoService.instancia(procesoInstanciaID);
        if (procesoInstancia.getEstado().getId().equals(DocumentoActaEstado.ANULADO.getId())) {
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "El acta seleccionada se encuentra anulada y no puede ser consultada.");
            return REDIRECT_MAIN_URL;
        }

        final boolean tieneAccesoPorClasificacion = actaService.tieneAccesoPorClasificacion(usuarioSesion, procesoInstancia);
        if (!tieneAccesoPorClasificacion) {
            return REDIRECT_ACCESO_DENEGADO_URL;
        }

        final String documentoID = procesoInstancia.getVariable(Documento.DOC_ID);
        Documento documento = actaService.buscarDocumento(documentoID);

        //Colocar el metodo que llama a la seleccion del usuario.
        Usuario usuarioRegistro = usuarioService.retornaUsuarioRegistro(usuarioSesion);
        if (usuarioRegistro == null) {
            uiModel.addAttribute(AppConstants.FLASH_ERROR, "No se encontro el usuario de registro. Por favor comuniquese con el administrador del sistema.");

            cargarInformacionSeleccionUsuariosUIModel(uiModel, documento);
            cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

            return DOCUMENTO_ACTA_ENVIO_REGISTRO_TEMPLATE;
        }

        procesoInstancia.setQuienMod(usuarioSesion.getId());
        procesoInstancia.setCuandoMod(new Date());
        procesoInstancia.forward(procesoTransicionID);
        procesoInstancia.asignar(usuarioRegistro);
        
        uiModel.addAttribute(AppConstants.FLASH_SUCCESS, "Ha sido asignado al usuario \"" + usuarioRegistro.toString() + "\" al acta \"" + documento.getAsunto() + "\".");

        cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

        return DOCUMENTO_ACTA_CARGAR_TEMPLATE;
    }
    
    /**
     * Envia al usuario de registro el acta.
     *
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @param procesoTransicionID ID de la transición del proceso a aplicar.
     * @param uiModel Modelo de UI.
     * @param principal Información de sesión.
     * @param redirectAttributes Atributos de redirección.
     * @return Nombre del template a presentar en caso de éxito o, URL de
     * redirección en caso de error.
     */
    @RequestMapping(value = "/cargar-acta-digital", method = RequestMethod.GET)
    public String cargarActaDigital(@RequestParam("pin") String procesoInstanciaID, @RequestParam(value = "tid", required = false) Integer procesoTransicionID, Model uiModel, Principal principal, RedirectAttributes redirectAttributes) {
        final Usuario usuarioSesion = getUsuario(principal);
        final boolean tieneAccesoPorAsignacion = actaService.verificaAccesoDocumentoActa(usuarioSesion, procesoInstanciaID);
        if (!tieneAccesoPorAsignacion) {
            return SECURITY_DENIED_TEMPLATE;
        }

        Instancia procesoInstancia = procesoService.instancia(procesoInstanciaID);
        if (procesoInstancia.getEstado().getId().equals(DocumentoActaEstado.ANULADO.getId())) {
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "El acta seleccionada se encuentra anulada y no puede ser consultada.");
            return REDIRECT_MAIN_URL;
        }

        final boolean tieneAccesoPorClasificacion = actaService.tieneAccesoPorClasificacion(usuarioSesion, procesoInstancia);
        if (!tieneAccesoPorClasificacion) {
            return REDIRECT_ACCESO_DENEGADO_URL;
        }

        final String documentoID = procesoInstancia.getVariable(Documento.DOC_ID);
        Documento documento = actaService.buscarDocumento(documentoID);

        cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

        return DOCUMENTO_ACTA_CARGAR_TEMPLATE;
    }
    
    /**
     * Envia al usuario quie creo el acta, para que valide el usuario quien creo 
     * el acta.
     *
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @param procesoTransicionID ID de la transición del proceso a aplicar.
     * @param uiModel Modelo de UI.
     * @param principal Información de sesión.
     * @param redirectAttributes Atributos de redirección.
     * @return Nombre del template a presentar en caso de éxito o, URL de
     * redirección en caso de error.
     */
    @RequestMapping(value = "/validar-acta", method = RequestMethod.GET)
    public String validarActa(@RequestParam("pin") String procesoInstanciaID, @RequestParam(value = "tid", required = false) Integer procesoTransicionID, Model uiModel, Principal principal, RedirectAttributes redirectAttributes) {
        final Usuario usuarioSesion = getUsuario(principal);
        final boolean tieneAccesoPorAsignacion = actaService.verificaAccesoDocumentoActa(usuarioSesion, procesoInstanciaID);
        if (!tieneAccesoPorAsignacion) {
            return SECURITY_DENIED_TEMPLATE;
        }

        Instancia procesoInstancia = procesoService.instancia(procesoInstanciaID);
        if (procesoInstancia.getEstado().getId().equals(DocumentoActaEstado.ANULADO.getId())) {
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "El acta seleccionada se encuentra anulada y no puede ser consultada.");
            return REDIRECT_MAIN_URL;
        }

        final boolean tieneAccesoPorClasificacion = actaService.tieneAccesoPorClasificacion(usuarioSesion, procesoInstancia);
        if (!tieneAccesoPorClasificacion) {
            return REDIRECT_ACCESO_DENEGADO_URL;
        }

        final String documentoID = procesoInstancia.getVariable(Documento.DOC_ID);
        Documento documento = actaService.buscarDocumento(documentoID);

        //Colocar el metodo que llama a la seleccion del usuario.
        Usuario usuarioRegistro = usuarioService.retornaUsuarioRegistro(usuarioSesion);
        if (usuarioRegistro == null) {
            uiModel.addAttribute(AppConstants.FLASH_ERROR, "No se encontro el usuario de registro. Por favor comuniquese con el administrador del sistema.");

            cargarInformacionSeleccionUsuariosUIModel(uiModel, documento);
            cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

            return DOCUMENTO_ACTA_ENVIO_REGISTRO_TEMPLATE;
        }
        
        procesoInstancia.setQuienMod(usuarioSesion.getId());
        procesoInstancia.setCuandoMod(new Date());
        procesoInstancia.forward(procesoTransicionID);
        procesoInstancia.asignar(documento.getElabora());

        uiModel.addAttribute(AppConstants.FLASH_SUCCESS, "Ha sido asignado al usuario \"" + documento.getElabora().toString() + "\" al acta \"" + documento.getAsunto() + "\".");

        cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);
        if(Objects.equals(usuarioSesion.getId(), documento.getElabora().getId())){
            return DOCUMENTO_ACTA_VALIDAR_TEMPLATE;
        }else{
            return DOCUMENTO_ACTA_CONSULTAR_TEMPLATE;
        }
    }
    
    /**
     * Envia al usuario quie creo el acta, para que valide el usuario quien creo 
     * el acta.
     *
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @param procesoTransicionID ID de la transición del proceso a aplicar.
     * @param uiModel Modelo de UI.
     * @param principal Información de sesión.
     * @param redirectAttributes Atributos de redirección.
     * @return Nombre del template a presentar en caso de éxito o, URL de
     * redirección en caso de error.
     */
    @RequestMapping(value = "/validar-acta-usuario-sesion", method = RequestMethod.GET)
    public String validarActaUsuarioSesion(@RequestParam("pin") String procesoInstanciaID, @RequestParam(value = "tid", required = false) Integer procesoTransicionID, Model uiModel, Principal principal, RedirectAttributes redirectAttributes) {
        final Usuario usuarioSesion = getUsuario(principal);
        final boolean tieneAccesoPorAsignacion = actaService.verificaAccesoDocumentoActa(usuarioSesion, procesoInstanciaID);
        if (!tieneAccesoPorAsignacion) {
            return SECURITY_DENIED_TEMPLATE;
        }

        Instancia procesoInstancia = procesoService.instancia(procesoInstanciaID);
        if (procesoInstancia.getEstado().getId().equals(DocumentoActaEstado.ANULADO.getId())) {
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "El acta seleccionada se encuentra anulada y no puede ser consultada.");
            return REDIRECT_MAIN_URL;
        }

        final boolean tieneAccesoPorClasificacion = actaService.tieneAccesoPorClasificacion(usuarioSesion, procesoInstancia);
        if (!tieneAccesoPorClasificacion) {
            return REDIRECT_ACCESO_DENEGADO_URL;
        }

        final String documentoID = procesoInstancia.getVariable(Documento.DOC_ID);
        Documento documento = actaService.buscarDocumento(documentoID);

        cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

        return DOCUMENTO_ACTA_VALIDAR_TEMPLATE;
    }
    
    /**
     * Envia al usuario quie creo el acta, para que valide el usuario quien creo 
     * el acta.
     *
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @param procesoTransicionID ID de la transición del proceso a aplicar.
     * @param uiModel Modelo de UI.
     * @param principal Información de sesión.
     * @param redirectAttributes Atributos de redirección.
     * @return Nombre del template a presentar en caso de éxito o, URL de
     * redirección en caso de error.
     */
    @RequestMapping(value = "/generar-sticker", method = RequestMethod.GET)
    public String generarSticker(@RequestParam("pin") String procesoInstanciaID, @RequestParam(value = "tid", required = false) Integer procesoTransicionID, Model uiModel, Principal principal, RedirectAttributes redirectAttributes) {
        final Usuario usuarioSesion = getUsuario(principal);
        final boolean tieneAccesoPorAsignacion = actaService.verificaAccesoDocumentoActa(usuarioSesion, procesoInstanciaID);
        if (!tieneAccesoPorAsignacion) {
            return SECURITY_DENIED_TEMPLATE;
        }

        Instancia procesoInstancia = procesoService.instancia(procesoInstanciaID);
        if (procesoInstancia.getEstado().getId().equals(DocumentoActaEstado.ANULADO.getId())) {
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "El acta seleccionada se encuentra anulada y no puede ser consultada.");
            return REDIRECT_MAIN_URL;
        }

        final boolean tieneAccesoPorClasificacion = actaService.tieneAccesoPorClasificacion(usuarioSesion, procesoInstancia);
        if (!tieneAccesoPorClasificacion) {
            return REDIRECT_ACCESO_DENEGADO_URL;
        }

        final String documentoID = procesoInstancia.getVariable(Documento.DOC_ID);
        Documento documento = actaService.buscarDocumento(documentoID);
        
        actaService.generaVariableSticker(procesoInstancia);

        cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

        return DOCUMENTO_ACTA_CARGAR_TEMPLATE;
    }
}
