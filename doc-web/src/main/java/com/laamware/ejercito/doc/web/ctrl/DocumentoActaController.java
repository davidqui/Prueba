package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.dto.DocumentoActaDTO;
import com.laamware.ejercito.doc.web.dto.DocumentoObservacionDTO;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Transicion;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.enums.DocumentoActaEstado;
import com.laamware.ejercito.doc.web.serv.CargoService;
import com.laamware.ejercito.doc.web.serv.ClasificacionService;
import com.laamware.ejercito.doc.web.serv.DocumentoActaService;
import com.laamware.ejercito.doc.web.serv.DocumentoObservacionService;
import com.laamware.ejercito.doc.web.serv.ProcesoService;
import com.laamware.ejercito.doc.web.serv.TransicionService;
import com.laamware.ejercito.doc.web.util.BusinessLogicValidation;
import java.security.Principal;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    private static final String DOCUMENTO_ACTA_GUARDAR_TEMPLATE = "documento-acta";

    private static final String DOCUMENTO_ACTA_CARGAR_TEMPLATE = "documento-acta-carga";

    private static final String SECURITY_DENIED_TEMPLATE = "security-denied";

    private static final String REDIRECT_ACCESO_DENEGADO_URL = "redirect:/documento/acceso-denegado";

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

        return DOCUMENTO_ACTA_GUARDAR_TEMPLATE;
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
            return DOCUMENTO_ACTA_GUARDAR_TEMPLATE;
        }

        try {
            documento = actaService.guardarRegistroDatos(documentoActaDTO, usuarioSesion);
        } catch (ParseException ex) {
            LOG.log(Level.SEVERE, documentoActaDTO.toString(), ex);

            cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

            uiModel.addAttribute("documentoActaDTO", documentoActaDTO);
            uiModel.addAttribute(AppConstants.FLASH_ERROR, "Excepción: Error registrando datos del acta: " + ex.getMessage());
            return DOCUMENTO_ACTA_GUARDAR_TEMPLATE;
        }

        cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

        return DOCUMENTO_ACTA_GUARDAR_TEMPLATE;
    }

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

    @ResponseBody
    @RequestMapping(value = "/observacion", method = RequestMethod.POST)
    public ResponseEntity<?> registrarComentarios(final DocumentoObservacionDTO observacionDTO, Principal principal) {
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

            if (!transicion.getEstadoInicial().getId().equals(procesoInstancia.getEstado().getId())) {
                redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "El acta seleccionada no se encuentra en el estado requisito para la aplicación de la transición.");
                return REDIRECT_MAIN_URL;
            }
        }

        final String documentoID = procesoInstancia.getVariable(Documento.DOC_ID);
        Documento documento = actaService.buscarDocumento(documentoID);

        if (documento.getRadicado() == null || documento.getRadicado().trim().isEmpty()) {
            documento = actaService.asignarNumeroRadicacion(documento, usuarioSesion);

            procesoInstancia.setAsignado(usuarioSesion);
            procesoInstancia.setQuienMod(usuarioSesion.getId());
            procesoInstancia.setCuandoMod(new Date());
            procesoInstancia.forward(procesoTransicionID);

            uiModel.addAttribute(AppConstants.FLASH_SUCCESS, "Ha sido asignado el número de radicación \"" + documento.getRadicado() + "\" al acta \"" + documento.getAsunto() + "\".");
        }

        cargarInformacionBasicaUIModel(uiModel, documento, procesoInstancia, usuarioSesion);

        return DOCUMENTO_ACTA_CARGAR_TEMPLATE;
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
        uiModel.addAttribute("clasificaciones", clasificacionService.findAllActivoOrderByOrden());
        uiModel.addAttribute("subseriesTrdActas", actaService.buscarSubseriesActasPorUsuario(usuarioSesion));
        uiModel.addAttribute("cargosUsuario", cargoService.buildCargosXUsuario(usuarioSesion));
        uiModel.addAttribute("documentoObservaciones", observacionService.findAllByDocumento(documento));
    }

}
