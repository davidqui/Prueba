package com.laamware.ejercito.doc.web.ctrl;

import com.aspose.words.License;
import static com.laamware.ejercito.doc.web.ctrl.ExpedienteController.PATH;
import com.laamware.ejercito.doc.web.dto.CargoDTO;
import com.laamware.ejercito.doc.web.dto.TransferenciaArchivoValidacionDTO;
import com.laamware.ejercito.doc.web.dto.TrdDTO;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.ExpTrd;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivoDetalle;
import com.laamware.ejercito.doc.web.entity.TransferenciaTransicion;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.CargosRepository;
import com.laamware.ejercito.doc.web.repo.TransferenciaArchivoDetalleRepository;
import com.laamware.ejercito.doc.web.serv.DocumentoDependenciaService;
import com.laamware.ejercito.doc.web.serv.DocumentoService;
import com.laamware.ejercito.doc.web.serv.ExpedienteService;
import com.laamware.ejercito.doc.web.serv.TransExpedienteDetalleService;
import com.laamware.ejercito.doc.web.serv.TransferenciaArchivoDetalleService;
import com.laamware.ejercito.doc.web.serv.TransferenciaArchivoService;
import com.laamware.ejercito.doc.web.serv.TransferenciaEstadoService;
import com.laamware.ejercito.doc.web.serv.TransferenciaTransicionService;
import com.laamware.ejercito.doc.web.serv.UsuarioService;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador para el proceso de transferencia de archivo.
 *
 * @author jgarcia@controltechcg.com
 * @since Ago 28, 2017
 * @version 1.0.0 (feature-120).
 */
@Controller
@RequestMapping(TransferenciaArchivoController.PATH)
public class TransferenciaArchivoController extends UtilController {

    /**
     * Logger.
     */
    private static final Logger LOG
            = Logger.getLogger(TransferenciaArchivoController.class.getName());

    public static final Long ESTADO_RECHAZADO = new Long(50);
    
    /**
     * Ruta raíz del controlador.
     */
    public static final String PATH = "/transferencia-archivo";

    /**
     * Servicio de transferencia de archivo.
     */
    @Autowired
    private TransferenciaArchivoService transferenciaService;

    /**
     * Servicio de usuarios.
     */
    @Autowired
    private UsuarioService usuarioService;
    
    /**
     * Servicio de documentos.
     */
    @Autowired    
    private DocumentoService documentoService;
    
    /**
     * Servicio de expediente
     */
    @Autowired
    private ExpedienteService expedienteService;

    /**
     * Servicio de cargos.
     */
    @Autowired
    CargosRepository cargosRepository;
    
    /**
     * Servicio de documentos dependencia
     */
    @Autowired
    private DocumentoDependenciaService documentoDependenciaService;
    
    /**
     *  respositorio de transferencia archivo detalle
     */
    @Autowired
    private TransferenciaArchivoDetalleService transferenciaArchivoDetalleService;
    
    
    public String listarTransferencias(Principal principal, Model model,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize){
        final Usuario origenUsuario = getUsuario(principal);
        
        
////        List<TransferenciaArchivo> expedientes = new ArrayList<>();
////        int count = expedienteService.obtenerCountExpedientesPorUsuario(usuSesion.getId(), filtro, all);
////        int totalPages = 0;
////        String labelInformacion = "";
////
////        if (count > 0) {
////            PaginacionDTO paginacionDTO = PaginacionUtil.retornaParametros(count, pageIndex, pageSize);
////            totalPages = paginacionDTO.getTotalPages();
////            expedientes = expedienteService.obtenerExpedientesDTOPorUsuarioPaginado(usuSesion.getId(), paginacionDTO.getRegistroInicio(), paginacionDTO.getRegistroFin(), filtro, all);
////            labelInformacion = paginacionDTO.getLabelInformacion();
////        }
//
//        model.addAttribute("expedientes", expedientes);
//        model.addAttribute("pageIndex", pageIndex);
//        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("labelInformacion", labelInformacion);
//        model.addAttribute("pageSize", pageSize);
        List<TransferenciaArchivo> transferenciasRealizadas = transferenciaService.findAllByOrigenUsuarioId(origenUsuario.getId());
        model.addAttribute("transferenciasRealizadas", transferenciasRealizadas);
        return "transferencia-archivo-listar";
    }

    /**
     * servicio de transiciones de transferencia
     */
    @Autowired
    private TransferenciaTransicionService transferenciaTransicionService;
    
    /***
     * servicio de estado de una transferencia
     */
    @Autowired
    private TransferenciaEstadoService transferenciaEstadoService;
    
    /**
     * servicio de expediente de una transferencia
     */
    @Autowired
    private TransExpedienteDetalleService transExpedienteDetalleService;
    
    /**
     * Presenta el formulario de creación para el proceso de transferencia de
     * archivo.
     *
     * @param principal Objeto principal de A&A.
     * @param model Modelo de UI.
     * @return Nombre del template Freemarker del formulario.
     */
    @RequestMapping(value = "/crear", method = RequestMethod.GET)
    public String presentarFormularioCreacionGET(Principal principal, Model model) {
        final Usuario origenUsuario = getUsuario(principal);
        
        final List<TransferenciaArchivo> transferenciasRecibidas = transferenciaService.findAllRecibidasActivasByDestinoUsuario(origenUsuario.getId());
        model.addAttribute("transferenciasRecibidas", transferenciasRecibidas);

        final Integer numArchivosRegistrosTotal = transferenciaService.countAllArchivoActivoByUsuario(origenUsuario.getId());
        model.addAttribute("numArchivosRegistrosTotal", numArchivosRegistrosTotal);

        if (!transferenciaService.hayPlantillaActiva()) {
            model.addAttribute(AppConstants.FLASH_ERROR,
                    "ATENCIÓN: No hay plantilla activa para la generación "
                    + "del acta de transferencia de archivo.");
        }

        return "transferencia-archivo-crear";
    }

    /**
     * Valida el formulario de creación de la transferencia. En caso exitoso,
     * redirecciona al formulario de confirmación; de lo contrario, redirecciona
     * al formulario de creación, indicando los errores en la operación.
     *
     * @param origenUsuarioID ID del usuario de origen de la transferencia.
     * @param destinoUsuarioID ID del usuario de destino de la transferencia.
     * @param tipoTransferencia Identificador del tipo de la transferencia
     * realizada.
     * @param transferenciaAnteriorID ID de la transferencia anterior
     * seleccionada. Este valor únicamente es obligatorio cuando el tipo de
     * transferencia es {@link TransferenciaArchivo#PARCIAL_TIPO}.
     * @param cargoOrigen
     * @param cargoDestino
     * @param principal Objeto principal de A&A.
     * @param model Modelo de UI.
     * @return Nombre del template Freemarker redirigido.
     */
    @RequestMapping(value = "/crear", method = RequestMethod.POST)
    public String validarFormularioCreacionPOST(
            @RequestParam("origenUsuario") Integer origenUsuarioID,
            @RequestParam("destinoUsuario") Integer destinoUsuarioID,
            @RequestParam("tipoTransferencia") String tipoTransferencia,
            @RequestParam(value = "transferenciaAnterior", required = false) Integer transferenciaAnteriorID,
            @RequestParam(value = "cargoOrigen") Integer cargoOrigen,
            @RequestParam(value = "cargoDestino", required = false) Integer cargoDestino,
            Principal principal, Model model) {

        final Usuario origenUsuario = getUsuario(principal);
        model.addAttribute("origenUsuario", origenUsuario);

        model.addAttribute("cargoOrigen", cargoOrigen);
        model.addAttribute("cargoDestino", cargoDestino);

        final List<TransferenciaArchivo> transferenciasRecibidas
                = transferenciaService.findAllRecibidasActivasByDestinoUsuario(origenUsuario.getId());
        model.addAttribute("transferenciasRecibidas", transferenciasRecibidas);

        model.addAttribute("tipoTransferencia", tipoTransferencia);

        final Integer numArchivosRegistrosTotal = transferenciaService.countAllArchivoActivoByUsuario(origenUsuario.getId());
        model.addAttribute("numArchivosRegistrosTotal", numArchivosRegistrosTotal);

        final TransferenciaArchivo transferenciaAnterior;
        if (transferenciaAnteriorID == null) {
            transferenciaAnterior = null;
        } else {
            transferenciaAnterior = transferenciaService
                    .findOneTransferenciaArchivo(transferenciaAnteriorID);
        }
        model.addAttribute("transferenciaAnterior", transferenciaAnterior);

        if (destinoUsuarioID == null) {
            model.addAttribute(AppConstants.FLASH_ERROR,
                    "Debe seleccionar un usuario destino de la transferencia.");
            return "transferencia-archivo-crear";
        }

        final Usuario destinoUsuario = usuarioService.findOne(destinoUsuarioID);
        model.addAttribute("destinoUsuario", destinoUsuario);

        // 2018-03-12 edison.gonzalez@controltechcg.com Issue #151 (SIGDI-Controltech):
        // Se añade la lista de cargos del usuario destino
        List<Object[]> list = cargosRepository.findCargosXusuario(destinoUsuario.getId());
        List<CargoDTO> cargoDTOs = new ArrayList<>();
        for (Object[] os : list) {
            CargoDTO cargoDTO = new CargoDTO(((BigDecimal) os[0]).intValue(), (String) os[1]);
            cargoDTOs.add(cargoDTO);
        }
        model.addAttribute("cargosXusuarioDestino", cargoDTOs);

        final TransferenciaArchivoValidacionDTO validacionDTO
                = transferenciaService.validarTransferencia(origenUsuario,
                        destinoUsuario, tipoTransferencia, transferenciaAnterior);
        if (!validacionDTO.isOK()) {
            model.addAttribute(AppConstants.FLASH_ERROR,
                    buildFlashErrorMessage(validacionDTO));
            return "transferencia-archivo-crear";
        }

        final List<DocumentoDependencia> registrosArchivo
                = transferenciaService.findRegistrosArchivo(tipoTransferencia,
                        transferenciaAnterior, origenUsuario);
        model.addAttribute("registrosArchivo", registrosArchivo);

        if (registrosArchivo.isEmpty()) {
            model.addAttribute(AppConstants.FLASH_ERROR,
                    "El usuario origen no tiene registros de archivo para "
                    + "transferir.");
            return "transferencia-archivo-crear";
        }

        Cargo cOrigen = cargosRepository.findOne(cargoOrigen);
        Cargo cDestino = cargosRepository.findOne(cargoDestino);

        model.addAttribute("nombreCargoOrigen", cOrigen.getCarNombre());
        model.addAttribute("nombreCargoDestino", cDestino.getCarNombre());

        return "transferencia-archivo-confirmar";
    }

    /*
        2018-03-12 edison.gonzalez@controltechcg.com Issue #151 (SICDI-Controltech) 
        feature-151: Se añaden los campos de los cargos de los usuarios origen y destino.
     */
    /**
     * Procesa la transferencia de archivo.
     *
     * @param origenUsuarioID ID del usuario de origen de la transferencia.
     * @param destinoUsuarioID ID del usuario de destino de la transferencia.
     * @param tipoTransferencia Identificador del tipo de la transferencia
     * realizada.
     * @param transferenciaAnteriorID ID de la transferencia anterior
     * seleccionada. Este valor únicamente es obligatorio cuando el tipo de
     * transferencia es {@link TransferenciaArchivo#PARCIAL_TIPO}.
     * @param cargoOrigen ID del cargo del usuario origen
     * @param cargoDestino ID del cargo del usuario destino
     * @param principal Objeto principal de A&A.
     * @param model Modelo de UI.
     * @return Nombre del template Freemarker redirigido.
     */
    @RequestMapping(value = "/procesar", method = RequestMethod.POST)
    public String procesarTransferenciaArchivoPOST(
            @RequestParam("origenUsuario") Integer origenUsuarioID,
            @RequestParam("destinoUsuario") Integer destinoUsuarioID,
            @RequestParam("tipoTransferencia") String tipoTransferencia,
            @RequestParam(value = "transferenciaAnterior", required = false) Integer transferenciaAnteriorID,
            @RequestParam(value = "cargoOrigen") Integer cargoOrigen,
            @RequestParam(value = "cargoDestino") Integer cargoDestino,
            Principal principal, Model model) {

        final Usuario creadorUsuario = getUsuario(principal);
        final Usuario origenUsuario = usuarioService.findOne(origenUsuarioID);
        final Usuario destinoUsuario = usuarioService.findOne(destinoUsuarioID);
        final TransferenciaArchivo transferenciaAnterior;
        if (transferenciaAnteriorID == null) {
            transferenciaAnterior = null;
        } else {
            transferenciaAnterior = transferenciaService
                    .findOneTransferenciaArchivo(transferenciaAnteriorID);
        }
        model.addAttribute("transferenciaAnterior", transferenciaAnterior);

        final License asposeLicense;
        try {
            asposeLicense = getAsposeLicence();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR,
                    "Excepción obteniendo Licencia Aspose: " + ex.getMessage());
            return presentarFormularioCreacionGET(principal, model);
        }

        final TransferenciaArchivo nuevatransferencia;
        try {
            nuevatransferencia = transferenciaService
                    .crearTransferenciaConActa(creadorUsuario,
                            origenUsuario, destinoUsuario, tipoTransferencia,
                            transferenciaAnterior, asposeLicense, cargoOrigen, cargoDestino);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR,
                    "Excepción durante transferencia: " + ex.getMessage());
            return presentarFormularioCreacionGET(principal, model);
        }

        model.addAttribute("nuevatransferencia", nuevatransferencia);

        model.addAttribute(AppConstants.FLASH_SUCCESS,
                "Se ha realizado la transferencia de archivo exitosamente.");

        return "transferencia-archivo-resultado";
    }

    /**
     * Obtiene la referencia de la licencia de Aspose.
     *
     * @return Licencia de Aspose.
     * @throws Exception
     */
    private License getAsposeLicence() throws Exception {
        final License asposeLicense = new License();
        final Resource resource = new ClassPathResource("Aspose.Words.lic");
        asposeLicense.setLicense(resource.getInputStream());
        return asposeLicense;
    }

    /**
     * Construye el mensaje de error a partir del DTO de validación.
     *
     * @param validacionDTO DTO de validación.
     * @return Mensaje de error.
     */
    private String buildFlashErrorMessage(final TransferenciaArchivoValidacionDTO validacionDTO) {
        final StringBuilder builder = new StringBuilder();

        for (String error : validacionDTO) {
            builder.append(error).append(" ");
        }

        return builder.toString().trim();
    }

    /**
     * Carga el listado de cargos del usuario en sesión.
     *
     * @param principal
     * @return
     */
    @ModelAttribute("cargosXusuario")
    public List<CargoDTO> cargosXusuario(Principal principal) {
        Usuario usuarioSesion = getUsuario(principal);
        List<Object[]> list = cargosRepository.findCargosXusuario(usuarioSesion.getId());
        List<CargoDTO> cargoDTOs = new ArrayList<>();
        for (Object[] os : list) {
            CargoDTO cargoDTO = new CargoDTO(((BigDecimal) os[0]).intValue(), (String) os[1]);
            cargoDTOs.add(cargoDTO);
        }
        return cargoDTOs;
    }
    
    /**
     * Método que retorna la pagina para seleccionar los documentos que van a estar en la transferencia de archivo
     * @param transId Identificador de la transferencia
     * @param principal usuario en sesión
     * @param model modelo del template
     * @return pagína para seleccionar los documentos.
     */
    @RequestMapping(value = "/seleccionar-documentos/{trans}", method = RequestMethod.GET)
    public String seleccionarDocumentos(@PathVariable("trans") Integer transId, Principal principal, Model model){
        Usuario usuarioSesion = getUsuario(principal);
        TransferenciaArchivo transferenciaArchivo = transferenciaService.findOneTransferenciaArchivo(transId);
        if(! transferenciaService.permisoEditarTransferencia(transferenciaArchivo, usuarioSesion))
            return "security-denied";
        List<TrdDTO> documentoXtrdDadoUsuario = documentoDependenciaService.documentoXtrdDadoUsuario(usuarioSesion);
        List<TransferenciaArchivoDetalle> documentosXTransferenciaArchivo = transferenciaArchivoDetalleService.buscarDocumentosTransferencia(transferenciaArchivo);
        model.addAttribute("trds", documentoXtrdDadoUsuario);
        model.addAttribute("documentosXTransferenciaArchivo", documentosXTransferenciaArchivo);
        return "transferencia-seleccionar-documentos";
    }
    
    /***
     * Método que recive los documentos seleccionados y los registra en el sistema.
     * @param transId Identificador de la transferencia
     * @param documentos Identificadores de los documentos a asignar
     * @param principal usuario en sesión
     * @param model modelo del template
     * @return pagína de continuación del proceso.
     */
    @RequestMapping(value = "/seleccionar-documentos/{trans}", method = RequestMethod.POST)
    public String asignarDocumentos(@PathVariable("trans") Integer transId, @RequestParam(value = "documentos", required = false) Integer[] documentos,
            Principal principal, Model model){
        
        Usuario usuarioSesion = getUsuario(principal);
        TransferenciaArchivo transferenciaArchivo = transferenciaService.findOneTransferenciaArchivo(transId);
        if(! transferenciaService.permisoEditarTransferencia(transferenciaArchivo, usuarioSesion))
            return "security-denied";
        
        transferenciaArchivoDetalleService.eliminarDocumentosTransferencia(transferenciaArchivo);
        if (documentos != null) {
            for (Integer documento : documentos) {
                DocumentoDependencia documentoDependencia = documentoDependenciaService.buscarPorId(documento);
                transferenciaArchivoDetalleService.guardarDocumentoTransferencia(transferenciaArchivo, documentoDependencia, usuarioSesion);
            }
        }
        
        List<Expediente>  expedientes = expedienteService.getExpedientesXusuarioCreador(usuarioSesion);
        TransferenciaTransicion findTransferenciaTransicionRechazado = transferenciaTransicionService.findTransferenciaTransicionRechazado(transferenciaEstadoService.getById(ESTADO_RECHAZADO), transferenciaArchivo);
        
        if (findTransferenciaTransicionRechazado != null || expedientes.isEmpty()) {
            return "redirect:" + PATH + "/resumen/"+ transId;
        }
        return "redirect:" + PATH + "/seleccionar-expediente/" + transId;
    }
    
    /***
     * Método que retorna la página para seleccionar los expedientes.
     * @param principal usuario en sesión
     * @param model modelo del template
     * @return pagína para seleccionar los expedientes. 
     */
    @RequestMapping(value = "/seleccionar-expediente/{trans}", method = RequestMethod.GET)
    public String seleccionarExpedientes(@PathVariable("trans") Integer transId, Principal principal, Model model){
        Usuario usuarioSesion = getUsuario(principal);
        TransferenciaArchivo transferenciaArchivo = transferenciaService.findOneTransferenciaArchivo(transId);
        if(! transferenciaService.permisoEditarTransferencia(transferenciaArchivo, usuarioSesion))
            return "security-denied";
        List<Expediente>  expedientes = expedienteService.getExpedientesXusuarioCreador(usuarioSesion);
        model.addAttribute("expedientes", expedientes);
        return "transferencia-seleccionar-expedientes";
    }
    
    
    @RequestMapping(value = "/seleccionar-expediente/{trans}", method = RequestMethod.POST)
    public String asignarExpedientes(@PathVariable("trans") Integer transId, Principal principal, Model model,
            @RequestParam(value = "expedientes", required = false) Long[] expedientes){
        Usuario usuarioSesion = getUsuario(principal);
        TransferenciaArchivo transferenciaArchivo = transferenciaService.findOneTransferenciaArchivo(transId);
        if(! transferenciaService.permisoEditarTransferencia(transferenciaArchivo, usuarioSesion))
            return "security-denied";
        transExpedienteDetalleService.eliminarExpedientesTransferencia(transferenciaArchivo);
        if (expedientes != null) {
            for (Long expediente : expedientes) {
                Expediente pExpediente = expedienteService.finById(expediente);
                transExpedienteDetalleService.guardarExpedienteTransferencia(transferenciaArchivo,
                        pExpediente, usuarioSesion);
            }
        }
        return "transferencia-resumen";
    }
    
    /***
     * Método que retorna la página para ver el resumen de la transferencia.
     * @param principal usuario en sesión
     * @param model modelo del template
     * @return pagína para ver el resumen del expediente.
     */
    @RequestMapping(value = "/resumen", method = RequestMethod.GET)
    public String resumenExpediente(Principal principal, Model model){
        Usuario usuarioSesion = getUsuario(principal);
        List<Expediente>  expedientes = expedienteService.getExpedientesXusuarioCreador(usuarioSesion);
        model.addAttribute("expedientes", expedientes);
        return "transferencia-resumen";
    }
    
    
    /**
     * Agrega el controlador
     *
     * @return controlador
     */
    @ModelAttribute("controller")
    public TransferenciaArchivoController controller() {
        return this;
    }
    
    
    public boolean hasDocumento(Integer id, List<TransferenciaArchivoDetalle> preseleccion){
        for (TransferenciaArchivoDetalle pr : preseleccion) {
            if(pr.getDocumentoDependencia().getId().equals(id)){
                return true;
            }
        }
        return false;
    }
    
}
