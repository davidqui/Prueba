package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.dto.CargoDTO;
import com.laamware.ejercito.doc.web.dto.DocumentoDependenciaArchivoDTO;
import com.laamware.ejercito.doc.web.dto.ExpUsuarioDto;
import com.laamware.ejercito.doc.web.dto.ExpedienteDTO;
import com.laamware.ejercito.doc.web.dto.PaginacionDTO;
import com.laamware.ejercito.doc.web.dto.TrdArchivoDocumentosDTO;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.DependenciaTrd;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.DocumentoObservacionDefecto;
import com.laamware.ejercito.doc.web.entity.ExpDocumento;
import com.laamware.ejercito.doc.web.entity.ExpObservacion;
import com.laamware.ejercito.doc.web.entity.ExpTrd;
import com.laamware.ejercito.doc.web.entity.ExpUsuario;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.ExpedienteTransicion;
import com.laamware.ejercito.doc.web.entity.ParNombreExpediente;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.AdjuntoRepository;
import com.laamware.ejercito.doc.web.repo.CargosRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoDependenciaRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.ExpedienteEstadoRepository;
import com.laamware.ejercito.doc.web.repo.ExpedienteRepository;
import com.laamware.ejercito.doc.web.repo.TrdRepository;
import com.laamware.ejercito.doc.web.serv.CargoService;
import com.laamware.ejercito.doc.web.serv.DocumentoDependenciaService;
import com.laamware.ejercito.doc.web.serv.DocumentoObservacionDefectoService;
import com.laamware.ejercito.doc.web.serv.ExpDocumentoService;
import com.laamware.ejercito.doc.web.serv.ExpObservacionService;
import com.laamware.ejercito.doc.web.serv.ExpedienteService;
import com.laamware.ejercito.doc.web.serv.ExpedienteTransicionService;
import com.laamware.ejercito.doc.web.serv.ExpedienteService;
import com.laamware.ejercito.doc.web.serv.ExpTrdService;
import com.laamware.ejercito.doc.web.serv.ExpUsuarioService;
import com.laamware.ejercito.doc.web.serv.ParNombreExpedienteService;
import com.laamware.ejercito.doc.web.serv.TRDService;
import com.laamware.ejercito.doc.web.serv.UsuarioService;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.DateUtil;
import com.laamware.ejercito.doc.web.util.PaginacionUtil;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author mcr
 *
 */
@Controller
@RequestMapping(value = "/expediente")
public class ExpedienteController extends UtilController {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentoController.class);

    public static final String PATH = "/expediente";

    /*
     * 2018-05-03 jgarcia@controltechcg.com Issue #157 (SICDI-Controltech)
     * feature-157: Año mínimo para los selectores de filtro por año.
     */
    @Value("${com.mil.imi.sicdi.minFilterSelectorYear}")
    private Integer minFilterSelectorYear;

    @Autowired
    ExpedienteRepository repo;

    @Autowired
    DocumentoRepository documentoRepository;

    @Autowired
    TrdRepository trdRepository;

    @Autowired
    AdjuntoRepository adjuntoRepository;

    @Autowired
    DocumentoDependenciaRepository documentoDependenciaRepository;

    @Autowired
    ExpedienteEstadoRepository expedienteEstadoRepository;

    @Autowired
    CargosRepository cargosRepository;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    CargoService cargoService;

    // 2017-05-15 jgarcia@controltechcg.com Issue #80 (SICDI-Controltech)
    // feature-80
    @Autowired
    TRDService trdService;

    
    @Autowired
    ExpedienteService expedienteService;

    @Autowired
    ExpTrdService expTrdService;

    @Autowired
    ExpUsuarioService expUsuarioService;

    
    @Autowired
    ExpDocumentoService expDocumentoService;
     
    @Autowired
    private ExpedienteTransicionService expedienteTransicionService;

    @Autowired
    private ExpObservacionService expObservacionService;

    /*
     * 2018-04-26 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
     * feature-151.
     */
    @Autowired
    private DocumentoDependenciaService documentoDependenciaService;

    @Autowired
    private DocumentoObservacionDefectoService observacionDefectoService;
    
    @Autowired
    private ParNombreExpedienteService parNombreExpedienteService;

    @Autowired
    DataSource ds;

    @RequestMapping(value = "/listarExpedientes", method = RequestMethod.GET)
    public String listarExpediente(Model model, Principal principal,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Usuario usuSesion = getUsuario(principal);

        List<ExpedienteDTO> expedientes = new ArrayList<>();
        int count = expedienteService.obtenerCountExpedientesPorUsuario(usuSesion.getId());
        int totalPages = 0;
        String labelInformacion = "";

        if (count > 0) {
            PaginacionDTO paginacionDTO = PaginacionUtil.retornaParametros(count, pageIndex, pageSize);
            totalPages = paginacionDTO.getTotalPages();
            expedientes = expedienteService.obtenerExpedientesDTOPorUsuarioPaginado(usuSesion.getId(), paginacionDTO.getRegistroInicio(), paginacionDTO.getRegistroFin());
            labelInformacion = paginacionDTO.getLabelInformacion();
        }

        for (ExpedienteDTO e : expedientes) {
            System.err.println("" + e.toString());
        }

        model.addAttribute("expedientes", expedientes);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("labelInformacion", labelInformacion);
        model.addAttribute("pageSize", pageSize);

        return "expediente-listar";
    }

    @RequestMapping(value = "/administrarExpediente", method = RequestMethod.GET)
    public String administrar(Model model, Principal principal, @RequestParam(value = "expId", required = true) Long expId) {
        System.err.println("dto= " + expId.toString());
        Usuario usuSesion = getUsuario(principal);
        ExpedienteDTO dTO = expedienteService.obtieneExpedienteDTOPorUsuarioPorExpediente(usuSesion.getId(), expId);
        if(dTO == null){
            return "security-denied";
        }

        List<ExpedienteTransicion> expedienteTransicions = expedienteTransicionService.retornarListaTransicionesXexpediente(expId);
        List<ExpObservacion> expObservacions = expObservacionService.retornarListaTransicionesXexpediente(expId);

        model.addAttribute("expediente",dTO);
        model.addAttribute("expTransicion",expedienteTransicions);
        model.addAttribute("observaciones",expObservacions);

        
        return "expediente-administrar";
    }
    
    
    
    /**
     * Crea una observación al documento
     *
     * @param expId
     * @param observacion
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping(value = {"/observacion"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> observacion(@RequestParam("expId") String expId, @RequestParam("observacion") String observacion, Model model, Principal principal) {
        Usuario usuSesion = getUsuario(principal);
        Long expIdValue = Long.parseLong(expId);
        Expediente expediente = expedienteService.findOne(expIdValue);

        ExpObservacion expObservacion = new ExpObservacion();
        expObservacion.setExpId(expediente);
        String escaped = observacion.replace("&", "&amp;");
        escaped = escaped.replace("<", "&lt;");
        escaped = escaped.replace(">", "&gt;");
        escaped = escaped.replace("\n", "<br/>");
        expObservacion.setExpObservacion(escaped);
        expObservacion.setFecCreacion(new Date());
        expObservacion.setUsuId(usuSesion);

        expObservacionService.guardarObservacion(expObservacion);
        Map<String, String> map = new HashMap<>();
        map.put("texto", expObservacion.getExpObservacion());
        map.put("cuando", DateUtil.dateFormatObservacion.format(expObservacion.getFecCreacion()));
        map.put("quien", expObservacion.getUsuId().toString());
        return map;
    }
    
    @ResponseBody
    @RequestMapping(value = "/cambios-pendientes/{exp}", method = RequestMethod.GET)
    public ResponseEntity<?> retornaCambiosPendientes(@PathVariable("exp") Long expId, Model model, Principal principal) {
//        System.out.println("retornaCambiosPendientes");
        final Expediente expediente = expedienteService.finById(expId);
        final Usuario usuario = getUsuario(principal);
        
        if(!expedienteService.permisoAdministrador(usuario, expediente))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        
        
        List<ExpUsuarioDto> usuariosPorAprobar = expUsuarioService.retornaUsuariosPendientesPorAprobar(expediente);
        return ResponseEntity.ok(usuariosPorAprobar);
    }
    
    @RequestMapping(value = "/aprobar-rechazar/{exp}/{tipo}/{observacion}", method = RequestMethod.POST)
    @Transactional
    public String aprobarOrechazar(@PathVariable("exp") Long expId, @PathVariable("tipo") int tipo, @PathVariable("observacion") String observacion, Model model, Principal principal){
        final Expediente expediente = expedienteService.finById(expId);
        final Usuario usuarioSesion = getUsuario(principal);

        if(!expedienteService.permisoAdministrador(usuarioSesion, expediente))
            return "security-denied";

        if(tipo == 1){
            expedienteService.aprobarExpediente(expediente, usuarioSesion);
            if(expediente.getIndAprobadoInicial()){
                model.addAttribute(AppConstants.FLASH_SUCCESS, "Los cambios en el expediente han sido aprobados.");
            }else{
                model.addAttribute(AppConstants.FLASH_SUCCESS, "El expediente ha sido aprobado.");
            }
        }else{
            expedienteService.rechazarExpediente(expediente, usuarioSesion);
            if(expediente.getIndAprobadoInicial()){
                model.addAttribute(AppConstants.FLASH_SUCCESS, "Los cambios en el expediente han sido rechazados.");
            }else{
                model.addAttribute(AppConstants.FLASH_SUCCESS, "El expediente ha sido rechazado.");
            }
        }
        return String.format("redirect:%s/administrarExpediente?expId=%s", PATH, expediente.getExpId());
    }
    
    @RequestMapping(value = "/modifica-tipo-expediente", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> modificarTipoExpediente(@RequestParam(value = "expId", required = true) Long expId, Model model, Principal principal){
        final Expediente expediente = expedienteService.finById(expId);
        final Usuario usuarioSesion = getUsuario(principal);

        if (!expedienteService.permisoAdministrador(usuarioSesion, expediente))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        
        try {
            expedienteService.modificarTipoExpediente(expediente, usuarioSesion);
            model.addAttribute(AppConstants.FLASH_SUCCESS, "Se ha cambiado el tipo de expediente.");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BusinessLogicException ex) {
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            List<Trd> trdExpedienteDocumentos = trdService.getTrdExpedienteDocumentos(expediente);
            return new ResponseEntity<>(trdExpedienteDocumentos, HttpStatus.BAD_REQUEST);
        }
    }

    
    @RequestMapping(value = "/enviarAprobar/{exp}", method = RequestMethod.GET)
    @Transactional
    public String enviarAprobar(@PathVariable("exp") Long expId, Model model, Principal principal){
        final Expediente expediente = expedienteService.finById(expId);
        final Usuario usuarioSesion = getUsuario(principal);

        if(!expedienteService.permisoAdministrador(usuarioSesion, expediente))
            return "security-denied";

        expedienteService.enviarAprobar(expediente, usuarioSesion);
        model.addAttribute(AppConstants.FLASH_SUCCESS, "Se a enviado una notificación al jefe de la dependencia para que apruebe el expediente.");
        return "redirect:"+PATH;
    }
    
    @RequestMapping(value = "/crear", method = RequestMethod.GET)
    @Transactional
    public String crearExpediente(Model model, Principal principal) {
        final Usuario usuarioSesion = getUsuario(principal);
        List<ParNombreExpediente> nombreExpediente = parNombreExpedienteService.findAll();
        Expediente expediente = new Expediente();
        model.addAttribute("expediente", expediente);
        List<Trd> trds = trdService.buildTrdsHierarchy(usuarioSesion);
        model.addAttribute("trds", trds);
        model.addAttribute("nombreExpediente", nombreExpediente);
        model.addAttribute("year", Calendar.getInstance().get(Calendar.YEAR));
        return "expediente-crear";
    }

    @RequestMapping(value = "/crear", method = RequestMethod.POST)
    @Transactional
    public String guardarExpediente(Expediente expediente, Model model, Principal principal, HttpServletRequest req) {
        final Usuario usuarioSesion = getUsuario(principal);
        
        String numeroExpediente = req.getParameter("numberExpediente");
        
        String parNombreExpediente = req.getParameter("parNombreExpediente");
        
        String opcionalNombre = req.getParameter("opcionalNombre");
        
        try {
            ParNombreExpediente pNombreExpediente = parNombreExpedienteService.findOne(Long.parseLong(parNombreExpediente));
            expediente = expedienteService.CrearExpediente(expediente, usuarioSesion, numeroExpediente,
                    pNombreExpediente, opcionalNombre);
        } catch (BusinessLogicException ex) {
            model.addAttribute("expediente", expediente);
            List<Trd> trds = trdService.buildTrdsHierarchy(usuarioSesion);
            List<ParNombreExpediente> nombreExpediente = parNombreExpedienteService.findAll();
            model.addAttribute("trds", trds);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            model.addAttribute("nombreExpediente", nombreExpediente);
            model.addAttribute("year", Calendar.getInstance().get(Calendar.YEAR));
            return "expediente-crear";
        }

        if (expediente.getExpTipo() == 1)    
            return "redirect:"+PATH+"/asignar-usuario-expediente/"+expediente.getExpId();

        return "redirect:"+PATH+"/trds-expediente/"+expediente.getExpId();
    }

    
    
    @RequestMapping(value = "/asignar-usuario-expediente/{exp}", method = RequestMethod.GET)
    public String listUsuarioExpediente(@PathVariable("exp") Long expId, Model model, Principal principal, HttpServletRequest req){

        final Expediente expediente = expedienteService.finById(expId);
        final Usuario usuarioSesion = getUsuario(principal);

        if(!expedienteService.permisoAdministrador(usuarioSesion, expediente))
            return "security-denied";
        
       Usuario usuCreador = expediente.getUsuCreacion();
       Usuario jefeDependencia = expediente.getDepId().getJefe();
       List<ExpUsuario> usuarios = expUsuarioService.findByExpediente(expediente);
       model.addAttribute("usuCreador", usuCreador);
       model.addAttribute("jefeDependencia", jefeDependencia);
       model.addAttribute("usuarios", usuarios);
       model.addAttribute("expediente", expediente);
       model.addAttribute("esJefeDependencia", usuarioSesion.getId().equals(jefeDependencia.getId()));
       
        return "expediente-seleccionar-usuarios";
    }

    @ResponseBody
    @RequestMapping(value = "/asignar-usuario-expediente/{exp}/{permiso}/{usuarioID}/{cargoID}", method = RequestMethod.POST)
    public ResponseEntity<?> asignarUsuarioExpediente(@PathVariable("exp") Long expId, @PathVariable("permiso") Integer permiso, @PathVariable("usuarioID") Integer usuarioID,
            @PathVariable("cargoID") Integer cargoID, Principal principal) {
        final Usuario usuarioSesion = getUsuario(principal);
        final Usuario usuario = usuarioService.findOne(usuarioID);
        final Cargo cargoUsu = cargoService.findOne(cargoID);

        final Expediente expediente = expedienteService.finById(expId);

        if(!expedienteService.permisoAdministrador(usuarioSesion, expediente))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        List<ExpUsuario> usuarioEnEspediente = expUsuarioService.findByExpedienteAndUsuario(expediente, usuario);
        if (!usuarioEnEspediente.isEmpty() || expediente.getUsuCreacion().getId().equals(usuarioID) || expediente.getDepId().getJefe().getId().equals(usuarioID))
            return new ResponseEntity<>("Ya existe este usuario en el expediente.", HttpStatus.BAD_REQUEST);

        expUsuarioService.agregarUsuarioExpediente(expediente, usuarioSesion, usuario, cargoUsu, permiso);

        return ResponseEntity.ok(usuarioID);
    }
    
    @ResponseBody
    @RequestMapping(value = "/agregar-documento-expediente/{exp}/{docId}", method = RequestMethod.POST)
    public ResponseEntity<?> asignarDocumentoExpediente(@PathVariable("exp") Long expId, @PathVariable("docId") String docId,
            Principal principal) {
        final Usuario usuarioSesion = getUsuario(principal);
        final Documento documento = documentoRepository.findOne(docId);
        final Expediente expediente = expedienteService.finById(expId);
        
        Boolean acceso = usuarioService.verificaAccesoDocumento(usuarioSesion.getId(), documento.getInstancia().getId());
        if((!expedienteService.permisoAdministrador(usuarioSesion, expediente) && !expedienteService.permisoIndexacion(usuarioSesion, expediente)) || !acceso)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (!expediente.getIndAprobadoInicial())
            return new ResponseEntity<>("El expediente no ha sido aprobado por el jefe de la dependencia.", HttpStatus.UNAUTHORIZED);
        if (!((documento.esDocumentoRevisionRadicado() || documento.esDocumentoEnviadoInterno())&& (usuarioSesion.getId() == documento.getInstancia().getAsignado().getId())))
            return new ResponseEntity<>("El documento no se encuentra en la etapa necesaria para agregarlo al expediente", HttpStatus.BAD_REQUEST);
        if (!expTrdService.validateTrdByExpediente(expediente, documento.getTrd()))
            return new ResponseEntity<>("El expediente no admite esta trd comuníquese con el administrador de este para que la agregue. </br></br> "+documento.getTrd().getNombre(),HttpStatus.UNAUTHORIZED);
        ExpDocumento expDocumento = expDocumentoService.findByDocumento(documento);
        if(expDocumento != null)
            return new ResponseEntity<>("Este documento ya esta asociado a un expediente.",HttpStatus.UNAUTHORIZED);
        
        
        expDocumentoService.agregarDocumentoExpediente(documento, expediente, usuarioSesion);

        return ResponseEntity.ok("Agregado Correctamente");
    }

    @ResponseBody
    @RequestMapping(value = "/editar-usuario-expediente/{exp}/{permiso}/{usuarioID}/{cargoID}", method = RequestMethod.POST)
    public ResponseEntity<?> editarUsuarioExpediente(@PathVariable("exp") Long expId, @PathVariable("permiso") Integer permiso, @PathVariable("usuarioID") Long usuarioID,
            @PathVariable("cargoID") Integer cargoID, Principal principal){

        final Usuario usuarioSesion = getUsuario(principal);
        final Cargo cargoUsu = cargoService.findOne(cargoID);
        final Expediente expediente = expedienteService.finById(expId);

        if(!expedienteService.permisoAdministrador(usuarioSesion, expediente))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        expUsuarioService.editarUsuarioExpediente(expediente, usuarioSesion, usuarioID, cargoUsu, permiso);

        
        return ResponseEntity.ok(usuarioID);
    }

    @ResponseBody
    @RequestMapping(value = "/eliminar-usuario-expediente/{exp}/{usuarioID}", method = RequestMethod.POST)
    public ResponseEntity<?> eliminarUsuarioExpediente(@PathVariable("exp") Long expId, @PathVariable("usuarioID") Long usuarioID, Principal principal){
        final Usuario usuarioSesion = getUsuario(principal);
        final Expediente expediente = expedienteService.finById(expId);

        if(!expedienteService.permisoAdministrador(usuarioSesion, expediente))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        expUsuarioService.eliminarUsuarioExpediente(usuarioID, usuarioSesion, expediente);

        return ResponseEntity.ok(usuarioID);
    }
    
    @ResponseBody
    @RequestMapping(value = "/cambiar-creador/{exp}/{usuarioID}", method = RequestMethod.POST)
    public ResponseEntity<?> cambiarUsuarioCreadorExpediente(@PathVariable("exp") Long expId, @PathVariable("usuarioID") Integer usuarioID, Principal principal){
        final Usuario usuarioSesion = getUsuario(principal);
        final Usuario usuarioAsignar = usuarioService.findOne(usuarioID);
        final Expediente expediente = expedienteService.finById(expId);
        
        if (!expediente.getDepId().getJefe().getId().equals(usuarioSesion.getId()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        
       if (usuarioAsignar.getDependencia().getJefe() == null)
           return new ResponseEntity<>("No existe un jefe en esta dependencia.", HttpStatus.BAD_REQUEST);
       
        List<ExpTrd> expTrds = expTrdService.findTrdsByExpediente(expediente);
       if (!esDependenciaCompatible(expTrds, usuarioAsignar)){
           String message = "<ul>";
           for (ExpTrd expTrd : expTrds) { message += "<li>"+expTrd.getTrdId().getNombre()+"</li>"; }
           return new ResponseEntity<>("La dependencia no cumple con las trd minimas para realizar el traspaso."+message+"</ul>", HttpStatus.BAD_REQUEST);
       }
       expedienteService.cambiarUsuarioCreador(expediente, usuarioAsignar, usuarioSesion);
       
        return ResponseEntity.ok(usuarioID);
    }

    @ResponseBody
    @RequestMapping(value = "/cargos-usuario/{usuarioID}", method = RequestMethod.POST)
    public ResponseEntity<?> cargosUsuario(@PathVariable("usuarioID") Integer usuarioID, Principal principal) {
        List<Object[]> cargos = cargoService.findCargosXusuario(usuarioID);
        List<CargoDTO> cargoDTOs = new ArrayList<>();
        for (Object[] os : cargos) {
            CargoDTO cargoDTO = new CargoDTO(((BigDecimal) os[0]).intValue(), (String) os[1]);
            cargoDTOs.add(cargoDTO);
        }
        return ResponseEntity.ok(cargoDTOs);
    }

    @RequestMapping(value = "/trds-expediente/{expId}", method = RequestMethod.GET)
    public String seleccionarTrdExpediente(@PathVariable("expId") Long expId, Model model, Principal principal, HttpServletRequest req){
        final Usuario usuarioSesion = getUsuario(principal);
        final Expediente expediente = expedienteService.finById(expId);

        if (!expedienteService.permisoAdministrador(usuarioSesion, expediente) || expediente.getExpTipo() == 1)
            return "security-denied";

        List<Trd> trds = trdService.buildTrdsHierarchy(usuarioSesion);
        List<ExpTrd> trdsPreseleccionadas = expTrdService.findTrdsByExpediente(expediente);
        List<Trd> trdDocumentos = trdService.getTrdExpedienteDocumentos(expediente);
        model.addAttribute("trds", trds);
        model.addAttribute("trdsPreseleccionadas", trdsPreseleccionadas);
        model.addAttribute("expediente", expediente);
        model.addAttribute("trdDocumentos", trdDocumentos);

        return "expediente-seleccionar-trds";
    }

    @RequestMapping(value = "/trds-expediente/{expId}", method = RequestMethod.POST)
    public String asignarTrdsExpediente(@PathVariable("expId") Long expId,  @RequestParam(value="trd", required=false) Integer[] trds, Principal principal,
            HttpServletRequest req, RedirectAttributes redirect){
        final Usuario usuarioSesion = getUsuario(principal);
        final Expediente expediente = expedienteService.finById(expId);

        if(!expedienteService.permisoAdministrador(usuarioSesion, expediente))
            return "security-denied";

        List<ExpTrd> trdsPreseleccionadas = expTrdService.findTrdsByExpedienteAll(expediente);
        List<Trd> trdDocumentos = trdService.getTrdExpedienteDocumentos(expediente);
        if (trds != null) {
            for (Integer trd : trds) {
                boolean hasElement = false;
                for (ExpTrd trdsPreseleccionada : trdsPreseleccionadas) {
                    if (trdsPreseleccionada.getTrdId().getId().equals(trd)) {
                        hasElement = true;
                        expTrdService.guardarTrdExpediente(trdsPreseleccionada, usuarioSesion, true);
                    }
                }
                if (!hasElement) {
                    Trd pTrd = trdService.findOne(trd);
                    expTrdService.guardarTrdExpediente(expediente, pTrd, usuarioSesion);
                }
            }
        }
        
        desvincularTrds(trdsPreseleccionadas, trds, usuarioSesion, trdDocumentos);
        if (!expediente.getIndAprobadoInicial())
            return "redirect:"+PATH+"/asignar-usuario-expediente/"+expediente.getExpId();
        return "redirect:"+PATH+"/administrarExpediente?expId="+expediente.getExpId();
    }
    
    public void desvincularTrds(List<ExpTrd> trdExpediente, Integer[] trds, Usuario usuarioSesion, List<Trd> trdDocumentos){
        for (ExpTrd trd : trdExpediente) {
            boolean hasElment = false;
            if (trds != null) {
                for (int i = 0; i < trds.length; i++) {
                    if (trd.getTrdId().getId().equals(trds[i])) {
                        hasElment = true;
                        break;
                    }
                }
            }
            for (int i = 0; i < trdDocumentos.size(); i++) {
                if (trd.getTrdId().getId().equals(trdDocumentos.get(i).getId())) {
                    hasElment = true;
                    break;
                }
            }
            if (!hasElment) {
                expTrdService.guardarTrdExpediente(trd, usuarioSesion, false);
            }
        }
    }


    /**
     * @param model
     * @param principal
     * @return exediente-list: La lista de expedientes que contienen documentos
     * en estado final.
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, Principal principal) {

        Dependencia dependencia = getUsuario(principal).getDependencia();

        try {
            // Si es jefe de una dependencia padre muestra todos los expedientes
            if (dependencia != null && dependencia.getPadre() == null) {
                if (dependencia.getJefe().getId() == getUsuario(principal).getId()) {

                    JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
                    StringBuilder sbQuery = new StringBuilder();
                    sbQuery.append("SELECT EXP.EXP_ID, EXP.EXP_NOMBRE, EXP.CUANDO,EXP.DEP_ID, EXP.TRD_ID, EXP.ESEX_ID, "
                            + " count(doc.doc_id), sum(PES.PES_FINAL) " + " FROM EXPEDIENTE EXP "
                            + " JOIN ESTADO_EXPEDIENTE EE ON EE.ESEX_ID = EXP.ESEX_ID "
                            + " JOIN DEPENDENCIA DEP ON EXP.DEP_ID=DEP.DEP_ID "
                            + " JOIN DOCUMENTO DOC ON EXP.EXP_ID = DOC.EXP_ID "
                            + " JOIN PROCESO_INSTANCIA PIN ON DOC.PIN_ID = PIN.PIN_ID "
                            + " JOIN PROCESO_ESTADO PES ON PIN.PES_ID = PES.PES_ID " + " WHERE EXP.ACTIVO=1 ");
                    sbQuery.append(" AND (EE.ESEX_NOMBRE='Abierto' OR EE.ESEX_NOMBRE='Cerrado') ");
                    sbQuery.append(
                            " GROUP BY EXP.EXP_ID, EXP.EXP_NOMBRE, EXP.CUANDO,EXP.DEP_ID, EXP.TRD_ID, EXP.ESEX_ID ");
                    sbQuery.append("having count(doc.doc_id) = sum(PES.PES_FINAL) ");
                    sbQuery.append(" ORDER BY EXP.EXP_NOMBRE desc");

                    model.addAttribute("uid", getUsuario(principal).getId());

                    return "expediente-list";

                }
            }

        } catch (Exception e) {

        }
        // Si no es jefe de una dependencia padre solo ve los expedientes de su
        // dependencia

        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("SELECT EXP.EXP_ID, EXP.EXP_NOMBRE, EXP.CUANDO,EXP.DEP_ID, EXP.TRD_ID, EXP.ESEX_ID, "
                + " count(doc.doc_id), sum(PES.PES_FINAL) " + " FROM EXPEDIENTE EXP "
                + " JOIN ESTADO_EXPEDIENTE EE ON EE.ESEX_ID = EXP.ESEX_ID "
                + " JOIN DEPENDENCIA DEP ON EXP.DEP_ID=DEP.DEP_ID " + " JOIN DOCUMENTO DOC ON EXP.EXP_ID = DOC.EXP_ID "
                + " JOIN PROCESO_INSTANCIA PIN ON DOC.PIN_ID = PIN.PIN_ID "
                + " JOIN PROCESO_ESTADO PES ON PIN.PES_ID = PES.PES_ID " + " WHERE DEP.DEP_ID=");
        sbQuery.append(dependencia.getId());
        sbQuery.append(" AND  EXP.ACTIVO=1 ");
        sbQuery.append(" AND (EE.ESEX_NOMBRE='Abierto' OR EE.ESEX_NOMBRE='Cerrado') ");
        sbQuery.append(" GROUP BY EXP.EXP_ID, EXP.EXP_NOMBRE, EXP.CUANDO,EXP.DEP_ID, EXP.TRD_ID, EXP.ESEX_ID ");
        sbQuery.append("having count(doc.doc_id) = sum(PES.PES_FINAL) ");
        sbQuery.append(" ORDER BY EXP.EXP_NOMBRE desc");

        model.addAttribute("uid", getUsuario(principal).getId());

        return "expediente-list";

    }

    /**
     * Presenta la información del archivo del usuario, según los criterios
     * indicados.
     *
     * @param serieID ID de la serie documental. No obligatorio.
     * @param subserieID ID de la subserie documental. No obligatorio.
     * @param cargoID ID del cargo seleccionado como filtro de búsqueda. No
     * obligatorio.
     * @param anyo Año de filtro. No obligatorio. En caso de ser {@code null} o
     * cero, se presenta la información de todos los años.
     * @param model Modelo de información entre vista y controlador.
     * @param principal Objeto de información de usuario en sesión.
     * @return URL de la pantalla de búsqueda.
     */
    /*
     * 2018-04-24 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
     * feature-151: Modificación de nombre de parámetros en el método para mayor
     * entendimiento.
     *
     * 2018-05-03 jgarcia@controltechcg.com Issue #157 (SICDI-Controltech)
     * feature-157: Adición de parámetros del filtro de año seleccionado.
     */
    @RequestMapping(value = "/carpeta", method = {RequestMethod.GET, RequestMethod.POST})
    public String carpeta(@RequestParam(value = "ser", required = false) Integer serieID, @RequestParam(value = "sub", required = false) Integer subserieID,
            @RequestParam(value = "cargoFiltro", required = false) Integer cargoID, @RequestParam(value = "anyo", required = false) Integer anyo,
            Model model, Principal principal) {
        model.addAttribute("ser", serieID);
        model.addAttribute("sub", subserieID);
        model.addAttribute("cargoFiltro", cargoID == null ? Integer.valueOf(0) : cargoID);
        /*
         * 2018-05-03 jgarcia@controltechcg.com Issue #157 (SICDI-Controltech)
         * feature-157: Establecimiento del atributo de modelo correspondiente
         * al año seleccionado por el usuario y la lista de años desde el mínimo
         * configurado en las propiedades del sistema hasta el año actual de
         * forma descendente.
         */
        model.addAttribute("anyo", anyo == null ? Integer.valueOf(0) : anyo);
        model.addAttribute("filterYears", DateUtil.createListOfYears(minFilterSelectorYear, GregorianCalendar.getInstance().get(Calendar.YEAR), Boolean.FALSE));

        final Usuario usuarioSesion = getUsuario(principal);

        if (subserieID != null) {
            /*
             * 2017-05-05 jgarcia@controltechcg.com Issue #63
             * (SICDI-Controltech)
             *
             * 2017-05-11 jgarcia@controltechcg.com Issue #79
             * (SICDI-Controltech): Limitar la presentación de documentos
             * archivados por usuario en sesión y TRD.
             *
             * 2018-04-26 jgarcia@controltechcg.com Issue #151
             * (SICDI-Controltech) feature-151: Obtención de la información de
             * documentos archivados, a través de DTO. Retiro del mapa
             * "registrosArchivoMapa", ya que no es necesario por el uso de los
             * DTO.
             *
             * 2018-05-03 jgarcia@controltechcg.com Issue #157
             * (SICDI-Controltech) feature-157: Adición de filtro por año.
             */
            final Trd subserie = trdService.findOne(subserieID);
            final Cargo cargo = (cargoID == null) ? null : cargosRepository.findOne(cargoID);
            final List<DocumentoDependenciaArchivoDTO> documentos = documentoDependenciaService.findAllBySubserieAndUsuarioAndCargoAndAnyo(subserie, usuarioSesion, cargo, anyo);

            model.addAttribute("docs", documentos);

            model.addAttribute("subserie", subserie);
            model.addAttribute("retornaSerie", subserie.getSerie());
        } else if (serieID != null && serieID > 0) {
            /*
             * 2017-05-17 jgarcia@controltechcg.com Issue #86
             * (SICDI-Controltech) hotfix-86: Corrección para presentar
             * únicamente las subseries asociadas a la dependencia del usuario
             * en sesión en las pantallas de Archivo.
             *
             * 2018-04-25 jgarcia@controltechcg.com Issue #151
             * (SICDI-Controltech) feature-151: Uso de DTO de TRD y reducción de
             * proceso de búsquedas para mejora del rendimiento del proceso de
             * consulta.
             *
             * 2018-05-03 jgarcia@controltechcg.com Issue #157
             * (SICDI-Controltech) feature-157: Adición de filtro por año.
             */
            final Trd serie = trdService.findOne(serieID);
            final Cargo cargo = (cargoID == null) ? null : cargosRepository.findOne(cargoID);
            final List<TrdArchivoDocumentosDTO> subseries = trdService.findAllSubseriesWithArchivoBySerieAndUsuarioAndCargoAndAnyo(serie, usuarioSesion, cargo, anyo);


            /*
             * 2017-05-15 jgarcia@controltechcg.com Issue #80
             * (SICDI-Controltech) feature-80: Ordenamiento tipo número de
             * versión de las TRD por código.
             */
            trdService.ordenarPorCodigo(subseries);
            model.addAttribute("subseries", subseries);
            model.addAttribute("serie", serie);
            model.addAttribute("retornaSerie", 0);
        } else {
            /*
             * 2018-04-24 jgarcia@controltechcg.com Issue #151
             * (SICDI-Controltech) feature-151: Cambio en la presentación de la
             * pantalla de archivo para que únicamente presente las series TRD
             * en las que el usuario tiene documentos archivados.
             *
             * 2018-05-03 jgarcia@controltechcg.com Issue #157
             * (SICDI-Controltech) feature-157: Adición de filtro por año.
             */
            final Cargo cargo = (cargoID == null) ? null : cargosRepository.findOne(cargoID);
            final List<TrdArchivoDocumentosDTO> series = trdService.findAllSeriesWithArchivoByUsuarioAndCargoAndAnyo(usuarioSesion, cargo, anyo);

            /*
             * 2017-05-15 jgarcia@controltechcg.com Issue #80
             * (SICDI-Controltech) feature-80: Ordenamiento tipo número de
             * versión de las TRD por código.
             */
            trdService.ordenarPorCodigo(series);

            model.addAttribute("series", series);
        }

        return "expediente-carpeta";
    }

    /**
     * Construye un mapara de los registros de archivo, utilizando como llave el
     * ID del documento asociado.
     *
     * @param registros Lista de registros de archivo.
     * @return Mapa de registros de archivo.
     */
    // 2017-05-15 jgarcia@controltechcg.com Issue #82 (SICDI-Controltech)
    // feature-82
    private Map<String, DocumentoDependencia> buildMapaRegistrosArchivo(List<DocumentoDependencia> registros) {
        Map<String, DocumentoDependencia> map = new LinkedHashMap<>();

        for (DocumentoDependencia registro : registros) {
            map.put(registro.getDocumento().getId(), registro);
        }

        return map;
    }

    public Trd getTrd(String cod) {
        return trdRepository.findByCodigo(cod);
    }

    @Override
    public String nombre(Integer id) {
        return super.nombre(id);
    }

    @RequestMapping(value = "/expediente-vacio", method = RequestMethod.GET)
    public String expedienteVacio(Model model, Principal principal) {

        return "expediente-vacio";
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
        cargoDTOs.add(new CargoDTO(0, "TODOS"));
        for (Object[] os : list) {
            CargoDTO cargoDTO = new CargoDTO(((BigDecimal) os[0]).intValue(), (String) os[1]);
            cargoDTOs.add(cargoDTO);
        }
        return cargoDTOs;
    }

    @ModelAttribute("observacionesDefecto")
    public List<DocumentoObservacionDefecto> listarObservacionDefectoActivas() {
        return observacionDefectoService.listarActivas();
    }
    
    public boolean esDependenciaCompatible(List<ExpTrd> expTrds, Usuario usuario){
        List<Trd> padres = trdService.findSeriesByUsuario(usuario);
        List<Trd> trds = new ArrayList<>();
        for (Trd padre : padres) {
           List<Trd> subTrds = trdService.findSubseriesbySerieAndUsuario(padre, usuario);
            for (Trd subTrd : subTrds) {
                trds.add(subTrd);
            }
        }
        int counter = 0;
        for (ExpTrd expTrd : expTrds) {
            for (Trd trd : trds) {
                if (expTrd.getTrdId().getId().equals(trd.getId())) {
                    counter++;
                    break;
                }
            }
        }
        return counter == expTrds.size();
    }

    
    public boolean has(Integer trdId, List<ExpTrd> PreSeleccionadas) {
        for (ExpTrd trd : PreSeleccionadas) {
            if (trd.getTrdId().getId().equals(trdId)) {
                return true;
            }
        }
        return false;
    }
    
    
    public boolean hasInDocument(Integer trdId, List<Trd> trdsDocumento){
        for (Trd trd : trdsDocumento) {
            if (trd.getId().equals(trdId)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasAllSubseriesSelected(final List<ExpTrd> PreSeleccionadas, final List<Trd> subseries) {
        if (subseries.size() != PreSeleccionadas.size()) {
            return false;
        }

        for (Trd subserie : subseries) {
            if (!has(subserie.getId(), PreSeleccionadas)) {
                return false;
            }
        }

        return true;
    }

    public boolean hasAllSubseriesSelectedByPadre(final List<ExpTrd> PreSeleccionadas, final List<Trd> subseries, Integer id){
        for (Trd subserie : subseries) {
            if (subserie.getId().equals(id)) {
                for (Trd sub : subserie.getSubs()) {
                    if (!has(sub.getId(), PreSeleccionadas)) {
                        return false;
                    }
                }
                break;
            }
        }
        return true;
    }

    /**
     * Agrega el controlador
     *
     * @return controlador
     */
    @ModelAttribute("controller")
    public ExpedienteController controller() {
        return this;
    }
    
    @ModelAttribute("pageSizes")
    public List<Integer> pageSizes(Model model) {
        List<Integer> list = Arrays.asList(10, 30, 50);
        model.addAttribute("pageSizes", list);
        return list;
    }
}
