package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.dto.CargoDTO;
import com.laamware.ejercito.doc.web.dto.DocumentoDTO;
import com.laamware.ejercito.doc.web.dto.PaginacionDTO;
import com.laamware.ejercito.doc.web.dto.TransferenciaArchivoDTO;
import com.laamware.ejercito.doc.web.dto.TransferenciaArchivoValidacionDTO;
import com.laamware.ejercito.doc.web.dto.TrdDTO;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.TransJustificacionDefecto;
import com.laamware.ejercito.doc.web.entity.TransExpedienteDetalle;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivoDetalle;
import com.laamware.ejercito.doc.web.entity.TransferenciaObservacion;
import com.laamware.ejercito.doc.web.entity.TransferenciaTransicion;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.CargosRepository;
import com.laamware.ejercito.doc.web.serv.DocumentoDependenciaService;
import com.laamware.ejercito.doc.web.serv.ExpedienteService;
import com.laamware.ejercito.doc.web.serv.TransExpedienteDetalleService;
import com.laamware.ejercito.doc.web.serv.TransJustificacionDefectoService;
import com.laamware.ejercito.doc.web.serv.TransferenciaArchivoDetalleService;
import com.laamware.ejercito.doc.web.serv.TransferenciaArchivoService;
import com.laamware.ejercito.doc.web.serv.TransferenciaEstadoService;
import com.laamware.ejercito.doc.web.serv.TransferenciaObservacionService;
import com.laamware.ejercito.doc.web.serv.TransferenciaTransicionService;
import com.laamware.ejercito.doc.web.serv.UsuarioService;
import com.laamware.ejercito.doc.web.util.PaginacionUtil;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private static final Logger LOG = Logger.getLogger(TransferenciaArchivoController.class.getName());

    public static final Long ESTADO_RECHAZADO = new Long(50);
    private static final String TRANSFERENCIASPROCESO = "PROCESO";
    private static final String TRANSFERENCIASREALIZADAS = "ORIGEN";
    private static final String TRANSFERENCIASRECIBIDAS = "DESTINO";
        
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
     *  servicio de transferencia archivo detalle
     */
    @Autowired
    private TransferenciaArchivoDetalleService transferenciaArchivoDetalleService;

    /**
     * servicio de transiciones de transferencia
     */
    @Autowired
    private TransferenciaTransicionService transferenciaTransicionService;

    /**
     * *
     * servicio de estado de una transferencia
     */
    @Autowired
    private TransferenciaEstadoService transferenciaEstadoService;

    /**
     * servicio de expediente de una transferencia
     */
    @Autowired
    private TransExpedienteDetalleService transExpedienteDetalleService;
    
    @Autowired
    private TransJustificacionDefectoService transJustificacionDefectoService;

    
    /**
     * servicio de las observaciones de transferencia.
     */
    @Autowired
    private TransferenciaObservacionService transferenciaObservacionService;
        
    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public String listarTransferencias(Principal principal, Model model,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "tipoTransferencia", required = true, defaultValue = TRANSFERENCIASPROCESO) String tipoTransferencia) {
        final Usuario origenUsuario = getUsuario(principal);

        List<TransferenciaArchivoDTO> transferencias = new ArrayList<>();
        int count;
        int totalPages = 0;
        String labelInformacion = "";

        if (tipoTransferencia.equals(TRANSFERENCIASPROCESO)) {
            count = transferenciaService.findCountProcesoByUsuarioId(origenUsuario.getId());
            if (count > 0) {
                PaginacionDTO paginacionDTO = PaginacionUtil.retornaParametros(count, pageIndex, pageSize);
                totalPages = paginacionDTO.getTotalPages();
                transferencias = transferenciaService.findAllProcesoByUsuarioId(origenUsuario.getId(), paginacionDTO.getRegistroInicio(), paginacionDTO.getRegistroFin());
                labelInformacion = paginacionDTO.getLabelInformacion();
            }
        }
        
        if (tipoTransferencia.equals(TRANSFERENCIASREALIZADAS)) {
            count = transferenciaService.findCountRealizadasByUsuarioId(origenUsuario.getId());
            if (count > 0) {
                PaginacionDTO paginacionDTO = PaginacionUtil.retornaParametros(count, pageIndex, pageSize);
                totalPages = paginacionDTO.getTotalPages();
                transferencias = transferenciaService.findAllRealizadasByUsuarioId(origenUsuario.getId(), paginacionDTO.getRegistroInicio(), paginacionDTO.getRegistroFin());
                labelInformacion = paginacionDTO.getLabelInformacion();
            }
        }
        
        if (tipoTransferencia.equals(TRANSFERENCIASRECIBIDAS)) {
            count = transferenciaService.findCountRecibidasByUsuarioId(origenUsuario.getId());
            if (count > 0) {
                PaginacionDTO paginacionDTO = PaginacionUtil.retornaParametros(count, pageIndex, pageSize);
                totalPages = paginacionDTO.getTotalPages();
                transferencias = transferenciaService.findAllRecibidasByUsuarioId(origenUsuario.getId(), paginacionDTO.getRegistroInicio(), paginacionDTO.getRegistroFin());
                labelInformacion = paginacionDTO.getLabelInformacion();
            }
        }

        model.addAttribute("transferencias", transferencias);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("labelInformacion", labelInformacion);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("tipoTransferencia", tipoTransferencia);
        
        return "transferencia-archivo-listar";
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
     * Método que retorna la pagina para seleccionar los documentos que van a
     * estar en la transferencia de archivo
     *
     * @param transId Identificador de la transferencia
     * @param principal usuario en sesión
     * @param model modelo del template
     * @return pagína para seleccionar los documentos.
     */
    @RequestMapping(value = "/seleccionar-documentos/{trans}", method = RequestMethod.GET)
    public String seleccionarDocumentos(@PathVariable("trans") Integer transId, Principal principal, Model model){
        
        Usuario usuarioSesion = getUsuario(principal);
        TransferenciaArchivo transferenciaArchivo = transferenciaService.findOneTransferenciaArchivo(transId);
        if (!transferenciaService.permisoEditarTransferencia(transferenciaArchivo, usuarioSesion)) {
            return "security-denied";
        }
        
        List<TrdDTO> documentoXtrdDadoUsuario = documentoDependenciaService.documentoXtrdDadoUsuario(usuarioSesion, transferenciaArchivo.getUsuOrigenCargo().getId());
        List<DocumentoDependencia> documentosEnTransferencia = documentoDependenciaService.listarDocumentosOtrasTransferencias(usuarioSesion, transferenciaArchivo);
        List<TransferenciaArchivoDetalle> documentosXTransferenciaArchivo = transferenciaArchivoDetalleService.buscarDocumentosTransferencia(transferenciaArchivo);

        model.addAttribute("transId", transId);
        model.addAttribute("trds", documentoXtrdDadoUsuario);
        model.addAttribute("documentosXTransferenciaArchivo", documentosXTransferenciaArchivo);
        model.addAttribute("documentosEnTransferencia", documentosEnTransferencia);
        return "transferencia-seleccionar-documentos";
    }

    /**
     * *
     * Método que recive los documentos seleccionados y los registra en el
     * sistema.
     *
     * @param transId Identificador de la transferencia
     * @param documentos Identificadores de los documentos a asignar
     * @param principal usuario en sesión
     * @param model modelo del template
     * @return pagína de continuación del proceso.
     */
    @RequestMapping(value = "/seleccionar-documentos/{trans}", method = RequestMethod.POST)
    public String asignarDocumentos(@PathVariable("trans") Integer transId, @RequestParam(value = "documentos", required = false) Integer[] documentos,
            Principal principal, Model model) {

        Usuario usuarioSesion = getUsuario(principal);
        TransferenciaArchivo transferenciaArchivo = transferenciaService.findOneTransferenciaArchivo(transId);
        List<DocumentoDependencia> documentosEnTransferencia = documentoDependenciaService.listarDocumentosOtrasTransferencias(usuarioSesion, transferenciaArchivo);

        if(! transferenciaService.permisoEditarTransferencia(transferenciaArchivo, usuarioSesion)){
            return "security-denied";
        }

        transferenciaArchivoDetalleService.eliminarDocumentosTransferencia(transferenciaArchivo);
        if (documentos != null) {
            for (Integer documento : documentos) {
                boolean find = false;
                for (DocumentoDependencia documentoDependencia : documentosEnTransferencia) {
                    if (documentoDependencia.getId().equals(documento)) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    DocumentoDependencia documentoDependencia = documentoDependenciaService.buscarPorId(documento);
                    transferenciaArchivoDetalleService.guardarDocumentoTransferencia(transferenciaArchivo, documentoDependencia, usuarioSesion);
                }
            }
        }
        
        List<Expediente>  expedientes = expedienteService.getExpedientesXusuarioCreador(usuarioSesion);
        List<TransferenciaTransicion> findTransferenciaTransicionRechazado = transferenciaTransicionService.findTransferenciaTransicionRechazado(transferenciaEstadoService.getById(ESTADO_RECHAZADO), transferenciaArchivo);
        
        if (!findTransferenciaTransicionRechazado.isEmpty() || expedientes.isEmpty()) {
            return "redirect:" + PATH + "/resumen/"+ transId;
        }
        return "redirect:" + PATH + "/seleccionar-expediente/" + transId;
    }

    /**
     * *
     * Método que retorna la página para seleccionar los expedientes.
     *
     * @param transId
     * @param principal usuario en sesión
     * @param model modelo del template
     * @return pagína para seleccionar los expedientes.
     */
    @RequestMapping(value = "/seleccionar-expediente/{trans}", method = RequestMethod.GET)
    public String seleccionarExpedientes(@PathVariable("trans") Integer transId, Principal principal, Model model) {
        Usuario usuarioSesion = getUsuario(principal);
        TransferenciaArchivo transferenciaArchivo = transferenciaService.findOneTransferenciaArchivo(transId);
        if (!transferenciaService.permisoEditarTransferencia(transferenciaArchivo, usuarioSesion)) {
            return "security-denied";
        }
        
        List<Expediente>  expedientes = expedienteService.getExpedientesXusuarioCreador(usuarioSesion);
        List<TransExpedienteDetalle> expedientesSeleccionados = transExpedienteDetalleService.buscarXTransferenciaArchivo(transferenciaArchivo);
        List<TransExpedienteDetalle> expedientesEnOtrasTransferencias = transExpedienteDetalleService.buscarOtrosExpedientesEnTranseferencia(usuarioSesion, transferenciaArchivo);
        model.addAttribute("transId", transId);
        model.addAttribute("expedientes", expedientes);
        model.addAttribute("expedientesSeleccionados", expedientesSeleccionados);
        model.addAttribute("expedientesEnOtrasTransferencias", expedientesEnOtrasTransferencias);
        
        return "transferencia-seleccionar-expedientes";
    }

    
    /***
     * Método para seleccionar los expedientes
     * @param transId Identificador de la transicion
     * @param principal usuario en sesión
     * @param model modelo del template 
     * @param expedientes expedientes para agregar
     * @return  pagína resumen.
     */
    @RequestMapping(value = "/seleccionar-expediente/{trans}", method = RequestMethod.POST)
    public String asignarExpedientes(@PathVariable("trans") Integer transId, Principal principal, Model model,
            @RequestParam(value = "expedientes", required = false) Long[] expedientes) {
        Usuario usuarioSesion = getUsuario(principal);
        TransferenciaArchivo transferenciaArchivo = transferenciaService.findOneTransferenciaArchivo(transId);
        if (!transferenciaService.permisoEditarTransferencia(transferenciaArchivo, usuarioSesion)) {
            return "security-denied";
        }
        transExpedienteDetalleService.eliminarExpedientesTransferencia(transferenciaArchivo);
        if (expedientes != null) {
            for (Long expediente : expedientes) {
                Expediente pExpediente = expedienteService.finById(expediente);
                transExpedienteDetalleService.guardarExpedienteTransferencia(transferenciaArchivo,
                        pExpediente, usuarioSesion);
            }
        }
        return "redirect:" + PATH + "/resumen/" + transId;
    }

    /**
     * *
     * Método que retorna la página para ver el resumen de la transferencia.
     *
     * @param transId
     * @param principal usuario en sesión
     * @param model modelo del template
     * @return pagína para ver el resumen del expediente.
     */
    @RequestMapping(value = "/resumen/{trans}", method = RequestMethod.GET)
    public String resumenExpediente(@PathVariable("trans") Integer transId, Principal principal, Model model){
        Usuario usuarioSesion = getUsuario(principal);
        TransferenciaArchivo transferenciaArchivo = transferenciaService.findOneTransferenciaArchivo(transId);
        if(! transferenciaService.permisoVerTransferencia(transferenciaArchivo, usuarioSesion))
            return "security-denied";
        
        List<TransExpedienteDetalle> expedientesSeleccionados = transExpedienteDetalleService.buscarXTransferenciaArchivo(transferenciaArchivo);
        List<TransferenciaArchivoDetalle> documentosXTransferenciaArchivo = transferenciaArchivoDetalleService.buscarDocumentosTransferencia(transferenciaArchivo);
        List<TransferenciaTransicion> transiciones = transferenciaTransicionService.findTransferenciaTransiciones(transferenciaArchivo);
        List<TransferenciaObservacion> observaciones = transferenciaObservacionService.observacionesPorTranferencia(transferenciaArchivo);
        
        model.addAttribute("transferenciaArchivo", transferenciaArchivo);
        model.addAttribute("transiciones", transiciones);
        model.addAttribute("usuario", usuarioSesion);
        model.addAttribute("expedientesSeleccionados", expedientesSeleccionados);
        model.addAttribute("documentosXTransferenciaArchivo", documentosXTransferenciaArchivo);
        model.addAttribute("observaciones", observaciones);
        
        if (transferenciaArchivo.getDestinoUsuario().getId().equals(usuarioSesion.getId()) &&
                transferenciaArchivo.getIndAprobado() == 1) {
            List<TransferenciaArchivoDetalle> documentosNoPosesionTransferencia = transferenciaArchivoDetalleService.documentosNoPosesionTransferencia(transferenciaArchivo, usuarioSesion);
            List<TransExpedienteDetalle> expedientesNoPosesionTransferencia = transExpedienteDetalleService.expedientesNoPosesionTransferencia(transferenciaArchivo, usuarioSesion);
            model.addAttribute("documentosNoPosesionTransferencia", documentosNoPosesionTransferencia);
            model.addAttribute("expedientesNoPosesionTransferencia", expedientesNoPosesionTransferencia);
        }
        
        return "transferencia-resumen";
    }
    
    /**
     * Método para recibir la tranferencia por el usuario destino.
     * @param transId Identificador de la transferencia
     * @param cargo Identificador del cargo seleccionado
     * @param principal usuario en sesión 
     * @return Codigo de respuesta exitoso, en caso contrario 400 ó 401 
     */
    @RequestMapping(value = "/recibir-destinatario/{trans}/{cargo}", method = RequestMethod.POST)
    public ResponseEntity<?> recibirTransferenciaDestinatario(@PathVariable("trans") Integer transId, 
            @PathVariable("cargo") Integer cargo, Principal principal){
        Usuario usuarioSesion = getUsuario(principal);
        
        Cargo pCargo = cargosRepository.findOne(cargo);
        TransferenciaArchivo transferenciaArchivo = transferenciaService.findOneTransferenciaArchivo(transId);
        
        if(! transferenciaService.permisoAprobarDestinatario(transferenciaArchivo, usuarioSesion))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        
        transferenciaService.aprobarDestinatario(transferenciaArchivo, pCargo, usuarioSesion);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /***
     * Método para rechazar la transferencia ya sea por el usuario destinatario ó 
     * jefe dependencia origen.
     * @param transId Identificador de la transferencia
     * @param observacion observación realizada
     * @param principal usuario en sesión
     * @param req request
     * @return Codigo de respuesta exitoso, en caso contrario 400 ó 401 
     */
    @RequestMapping(value = "/rechazar/{trans}", method = RequestMethod.POST)
    public ResponseEntity<?> rechazarTransferencia(@PathVariable("trans") Integer transId, 
            @RequestParam(value = "observacion", required = true) String observacion, Principal principal, HttpServletRequest req){
        Usuario usuarioSesion = getUsuario(principal);
        TransferenciaArchivo transferenciaArchivo = transferenciaService.findOneTransferenciaArchivo(transId);

        if(! transferenciaService.permisoRechazar(transferenciaArchivo, usuarioSesion))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        
        transferenciaService.rechazarTransferencia(transferenciaArchivo, observacion, usuarioSesion);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /***
     * Método para anular una transferencia
     * @param transId Identificador de una transferencia
     * @param principal usuario en sesión
     * @return pagína listar transferencias si todo sale correcto
     */
    @RequestMapping(value = "/anular/{trans}", method = RequestMethod.GET)
    public String anularTransferencia(@PathVariable("trans") Integer transId,  Principal principal){
        Usuario usuarioSesion = getUsuario(principal);
        TransferenciaArchivo transferenciaArchivo = transferenciaService.findOneTransferenciaArchivo(transId);
        
        if(! transferenciaService.permisoEditarTransferencia(transferenciaArchivo, usuarioSesion))
            return "security-denied";

        transferenciaService.anularTransferencia(transferenciaArchivo, usuarioSesion);
        
        return "redirect:" + PATH + "/listar/";
    }
    
    /***
     * Método para aprobar la trasferencia
     * @param transId Identificador de la transferencia
     * @param principal usuario en sesión 
     * @return pagína resumen de la transferencia 
     */
    @RequestMapping(value = "/aprobar/{trans}", method = RequestMethod.GET)
    public String aprobarTransferencia(@PathVariable("trans") Integer transId,  Principal principal){
        Usuario usuarioSesion = getUsuario(principal);
        TransferenciaArchivo transferenciaArchivo = transferenciaService.findOneTransferenciaArchivo(transId);
        
        if(! transferenciaService.permisoAprobarJefe(transferenciaArchivo, usuarioSesion))
            return "security-denied";
        
        
        
        transferenciaService.aprobarTransferencia(transferenciaArchivo, usuarioSesion);
        
        return "redirect:" + PATH + "/resumen/"+transId;
    }
    
    /***
     * Método que permite enviar la transferencia al usuaqrio destino
     * @param transId identificador de la transferencia 
     * @param principal usuario en sesión 
     * @return página de resumen de transferencia
     */
    @RequestMapping(value = "/enviar/{trans}", method = RequestMethod.GET)
    public String enviarTransferencia(@PathVariable("trans") Integer transId,  Principal principal){
        Usuario usuarioSesion = getUsuario(principal);
        TransferenciaArchivo transferenciaArchivo = transferenciaService.findOneTransferenciaArchivo(transId);
        
        if(! transferenciaService.permisoEditarTransferencia(transferenciaArchivo, usuarioSesion))
            return "security-denied";
        
        transferenciaService.enviarTransferencia(transferenciaArchivo, usuarioSesion);
        return "redirect:" + PATH + "/resumen/"+transId;
    }

    /**
     * Retorna los valores para la paginación
     *
     * @param model
     * @return
     */
    @ModelAttribute("pageSizes")
    public List<Integer> pageSizes(Model model) {
        List<Integer> list = Arrays.asList(10, 30, 50);
        model.addAttribute("pageSizes", list);
        return list;
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
    
    /**
     * Verifica si un documento esta dentro de una lista
     * @param id identificador del documento dependencia 
     * @param preseleccion lista de documentos en transferencia
     * @return true si está false de lo contrario
     */
    public boolean hasDocumento(Integer id, List<TransferenciaArchivoDetalle> preseleccion){
        for (TransferenciaArchivoDetalle pr : preseleccion) {
            if (pr.getDocumentoDependencia().getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Verifica si un documento en trasnferencia esta dentro de una lista
     * @param id identificador del documento transferencia 
     * @param preseleccion lista de documentos en transferencia 
     * @return true si está false de lo contrario
     */
    public boolean hasDocumentoEnTransferencia(Integer id, List<DocumentoDependencia> preseleccion){
        for (DocumentoDependencia pr : preseleccion) {
            if (pr.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
    
    /***
     * Verifica si un expediente esta dentro de una lista
     * @param id identificador del expediente 
     * @param preseleccion lista de expedientes
     * @return true si está false de lo contrario
     */
    public boolean hasExpediente(Long id, List<TransExpedienteDetalle> preseleccion){
        for (TransExpedienteDetalle pr : preseleccion) {
            if(pr.getExpId().getExpId().equals(id)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Verifica si un documento no se encuentra en la lista de los documentos que no posee custodia.
     * @param id iidentificador del documento
     * @param documentos lista de documentos sin juristicacción
     * @return true si no se encuentra en la lista, false de lo contrio
     */
    public boolean hasDocumentoTransferido(Integer id, List<TransferenciaArchivoDetalle> documentos){
        for (TransferenciaArchivoDetalle documento : documentos) {
            if (documento.getId().equals(id)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Verifica si un expediente no se encuentra en la lista de los expedientes que no posee custodia.
     * @param id iidentificador del documento
     * @param documentos lista de documentos sin juristicacción
     * @return true si no se encuentra en la lista, false de lo contrio
     */
    public boolean hasExpedienteTransferido(Long id, List<TransExpedienteDetalle> expedientes){
        for (TransExpedienteDetalle expediente : expedientes) {
            if (expediente.getTraExpId().equals(id)) {
                return false;
            }
        }
        return true;
    }

    
    /**
     * Lista las observaciones por defecto activas, ordenadas por texto.
     *
     * @return Lista de observaciones por defecto activas.
     */
    /*
     * 2018-05-24 jgarcia@controltechcg.com Issue #172 (SICDI-Controltech)
     * feature-172: Lista de observaciones por defecto para el modelo de UI.
     */
    @ModelAttribute("justificacionesDefecto")
    public List<TransJustificacionDefecto> listarJustificacionesPorDefectoActivas() {
        return transJustificacionDefectoService.listarActivas();
    }
    
    /**
     * Presenta el formulario de creación para el proceso de transferencia de
     * archivo en gestion.
     *
     * @param principal Objeto principal de A&A.
     * @param model Modelo de UI.
     * @return Nombre del template Freemarker del formulario.
     */
    @RequestMapping(value = "/crearTransferenciaGestion", method = RequestMethod.GET)
    public String crearFormularioTransferenciaGestionGET(Principal principal, Model model) {
        final Usuario origenUsuario = getUsuario(principal);
        
        model.addAttribute("origenUsuario", origenUsuario);
        model.addAttribute("dependencia", origenUsuario.getDependencia());

        if(origenUsuario.getUsuCargoPrincipalId()== null){
            model.addAttribute(AppConstants.FLASH_ERROR,"ATENCIÓN: El usuario en sesión no tiene cargo principal en el sistema.");
        }
        
        if (!transferenciaService.hayPlantillaActiva()) {
            model.addAttribute(AppConstants.FLASH_ERROR,"ATENCIÓN: No hay plantilla activa para la generación del acta de transferencia de archivo.");
        }
        
        return "transferencia-gestion-crear";
    }

    /**
     * Valida el formulario de creación de la transferencia. En caso exitoso,
     * redirecciona al formulario de confirmación; de lo contrario, redirecciona
     * al formulario de creación, indicando los errores en la operación.
     *
     * @param origenUsuarioID ID del usuario de origen de la transferencia.
     * @param destinoUsuarioID ID del usuario de destino de la transferencia.
     * @param justificacion
     * @param cargoOrigen
     * @param principal Objeto principal de A&A.
     * @param model Modelo de UI.
     * @return Nombre del template Freemarker redirigido.
     */
    @RequestMapping(value = "/crearTransferenciaGestion", method = RequestMethod.POST)
    public String crearFormularioTransferenciaGestionPOST(
            @RequestParam("origenUsuario") Integer origenUsuarioID,
            @RequestParam("destinoUsuario") Integer destinoUsuarioID,
            @RequestParam(value = "cargoOrigen") Integer cargoOrigen,
            @RequestParam(value = "justificacion") String justificacion,
            Principal principal, Model model) {

        try {
            final Usuario origenUsuario = getUsuario(principal);
            model.addAttribute("origenUsuario", origenUsuario);
            model.addAttribute("cargoOrigen", cargoOrigen);
            model.addAttribute("dependencia", origenUsuario.getDependencia());
            model.addAttribute("justificacion", justificacion);
            
            if (destinoUsuarioID == null) {
                model.addAttribute(AppConstants.FLASH_ERROR,"Debe seleccionar un usuario destino de la transferencia.");
                return "transferencia-gestion-crear";
            }
            
            final Usuario destinoUsuario = usuarioService.findOne(destinoUsuarioID);
            model.addAttribute("destinoUsuario", destinoUsuario);
            
            final TransferenciaArchivoValidacionDTO validacionDTO = transferenciaService.validarTransferenciaGestion(origenUsuario,cargoOrigen,destinoUsuario, justificacion);
            if (!validacionDTO.isOK()) {
                model.addAttribute(AppConstants.FLASH_ERROR,buildFlashErrorMessage(validacionDTO));
                return "transferencia-gestion-crear";
            }
            
            TransferenciaArchivo ta = transferenciaService.crearEncabezadoTransferenciaGestion(origenUsuario, cargoOrigen, destinoUsuario, justificacion);
            model.addAttribute(AppConstants.FLASH_SUCCESS,"El encabezado de la transferencia del archivo ha sido creada satisfactoriamente.");
            System.err.println("tranns= "+ta);
            return String.format("redirect:%s/seleccionar-documentos/%s", PATH, ta.getId());
        } catch (Exception ex) {
            model.addAttribute(AppConstants.FLASH_ERROR,"Error creando la transferencia. Por favor comuniquese con el administrador del sistema.");
            return "transferencia-gestion-crear";
        }
    }
    
    @RequestMapping(value = "/devolver/{trans}/{usuId}", method = RequestMethod.POST)
    public ResponseEntity<?> devolverTransferencia(@PathVariable("trans") Integer transId,
            @PathVariable("usuId") Integer usuId, Principal principal, Model model){
        final Usuario origenUsuario = getUsuario(principal);
        final TransferenciaArchivo transferenciaArchivo = transferenciaService.findOneTransferenciaArchivo(transId);
        if (!transferenciaService.permisoAprobarDestinatario(transferenciaArchivo, origenUsuario))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        
        return null;
    }
//    
//    @RequestMapping(value = "/verificar-transferencia/{trans}", method = RequestMethod.POST)
//    public ResponseEntity<?> verificarDevolucionTransferencia(@PathVariable("trans") Integer transId, Principal principal){
//        final Usuario origenUsuario = getUsuario(principal);
//        final TransferenciaArchivo transferenciaArchivo = transferenciaService.findOneTransferenciaArchivo(transId);
//        if (!transferenciaService.permisoAprobarDestinatario(transferenciaArchivo, origenUsuario))
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        List<TransferenciaArchivoDetalle> documentosNoPosesionTransferencia = transferenciaArchivoDetalleService.documentosNoPosesionTransferencia(transferenciaArchivo, origenUsuario);
//        List<DocumentoDTO> documentos = new ArrayList<>();
//        for (TransferenciaArchivoDetalle dt : documentosNoPosesionTransferencia) {
//            DocumentoDTO doc = new DocumentoDTO();
//            doc.setAsunto(dt.getDocumentoDependencia().getDocumento().getAsunto());
//            doc.setCuandoMod(dt.getDocumentoDependencia().getDocumento().getCuandoMod());
//            doc.setNombreClasificacion(dt.getDocumentoDependencia().getDocumento().getClasificacion().getNombre());
//        }
//        return ResponseEntity.ok(documentos);
//    }
//    
    
}
