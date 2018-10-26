package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Pregunta;
import com.laamware.ejercito.doc.web.entity.Respuesta;
import com.laamware.ejercito.doc.web.entity.TemaCapacitacion;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.PreguntaService;
import com.laamware.ejercito.doc.web.serv.RespuestaService;
import com.laamware.ejercito.doc.web.serv.TemaCapacitacionService;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.ReflectionException;
import java.security.Principal;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para {@link Respuesta}.
 *
 * @author jcespedeso@imi.mil.co 
 * @author dquijanor@imi.mil.co 
 * @author aherreram@imi.mil.co
 * @since Septiembre 3, 2018 _feature_9 (SICDI-GETDE)
 */

@Controller
@PreAuthorize(value = "hasRole('ADMIN_TEMA_CAPACITACION')")
@RequestMapping(RespuestaController.PATH)
public class RespuestaController extends UtilController {

    private static final Logger LOG = Logger.getLogger(RespuestaController.class.getName());

    static final String PATH = "/admin/respuesta";

    private static final String LIST_TEMPLATE = "respuesta-list";

    private static final String CREATE_TEMPLATE = "respuesta-list-create";

    private static final String EDIT_TEMPLATE = "respuesta-list-edit";

    private static final String NOMBRE_DEFECTO_FTL = "respuesta";

    @Autowired
    private RespuestaService respuestaService;
    
    @Autowired
    private PreguntaService preguntaService;
    
    @Autowired
    private TemaCapacitacionService temaCapacitacionService;

    /**
     *  Permite listar todos los temas disponibles del modulo Capacitacion  
     * 2018-10-18 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param all
     * @param page
     * @param pageSize
     * @param model
     * @return Lista de todos las Respuestas segun corresponda activas o activas y eliminadas.
     */
    @RequestMapping(value = {"/list/{key}"}, method = RequestMethod.GET)
    public String list(@PathVariable(value = "key") Integer key, 
            @RequestParam(value = "all", required = false, defaultValue = "false") Boolean all,
                       @RequestParam(value = "pageIndex", required = false) Integer page,
                       @RequestParam(value = "pageSize", required = false) Integer pageSize, Model model) {
        
        Pregunta seleccionPregunta = preguntaService.findOne(key);
        model.addAttribute("respuestaView", seleccionPregunta);
        
        TemaCapacitacion seleccionTema = temaCapacitacionService.findOne(seleccionPregunta.getTemaCapacitacion().getId());
        model.addAttribute("temaView", seleccionTema);
        
        if (page == null || page < 0)
            page = 1;
        
        if (pageSize == null || pageSize < 0)
            pageSize = ADMIN_PAGE_SIZE;
        
        Long count;
        
        Pageable pageable = new PageRequest(page-1, pageSize, Sort.Direction.DESC, "cuando");
        
        Page<Respuesta> list;
        if (!all) {
            list = respuestaService.findActive(pageable);
        } else {
            list = respuestaService.findAll(pageable);
        }
        
        count = list.getTotalElements();
        adminPageable(count, model, page, pageSize);
        model.addAttribute("list", list.getContent());
        model.addAttribute("all", all);
        return LIST_TEMPLATE;
    }

    /**
     * Crea una nueva instancia de Tema de Capacitacion para la creación de un nuevo registro.
     * 2018-10-18 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param model
     * @return Vista de creación de una nueva tematica.
     */
    
    @RequestMapping(value = {"/create/{key}"}, method = RequestMethod.GET)
    public String create(@PathVariable(value = "key") Integer key, Model model) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        
        Respuesta respuesta = new Respuesta();
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<pasando por Metodo create>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> = " + respuesta);

        model.addAttribute(NOMBRE_DEFECTO_FTL, respuesta);
        model.addAttribute("preguntas", preguntaService.findActive(sort));
        Pregunta listRespuestas = preguntaService.findOne(key);
        model.addAttribute("preguntasCrear", listRespuestas);
        return CREATE_TEMPLATE;
    }

    /**
     * Permite visualizar el formulario de edición de un tema de capacitación en especifico
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param model
     * @param req  conjunto de data recibida por intermedio del request a traves del formulario 
     * @return Pagina que edita un nombre de tematica.
     */
    
    @RequestMapping(value = {"/edit"}, method = RequestMethod.GET)
    public String edit(Model model, HttpServletRequest req) {
        Integer id = Integer.parseInt(req.getParameter("id"));
        Respuesta laRespuesta = respuestaService.findOne(id);
        model.addAttribute(NOMBRE_DEFECTO_FTL, laRespuesta);
        
        Pregunta pregunta = preguntaService.findOne(laRespuesta.getPregunta().getId());
        model.addAttribute("preguntasEditar", pregunta);
        return EDIT_TEMPLATE;
    }

    /**
     * Permite crear un nombre de tema de Capacitación
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param respuesta
     * @param req
     * @param model
     * @param redirect
     * @param principal
     * @return Pagina para crear un nombre de tematica.
     */
    @RequestMapping(value = {"/crear"}, method = RequestMethod.POST)
    public String crear(Respuesta respuesta, HttpServletRequest req,  Model model, RedirectAttributes redirect, Principal principal) {
        model.addAttribute(NOMBRE_DEFECTO_FTL, respuesta);
        final Usuario usuarioSesion = getUsuario(principal);

        try {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<pasando por Metodo Crear>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> = " + respuesta);

            respuestaService.crearRespuesta(respuesta, usuarioSesion);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
            return "redirect:" + PATH + "?" + model.asMap().get("queryString");
        } catch (BusinessLogicException | ReflectionException ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return CREATE_TEMPLATE;
        }
    }

    /**
     * Permite actualizar una tema de capacitación.
     *2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param respuesta
     * @param req
     * @param model
     * @param redirect
     * @param principal
     * @return Pagina que edita un Tema de Capacitación.
     */
    @RequestMapping(value = {"/actualizar"}, method = RequestMethod.POST)
    public String actualizar(Respuesta respuesta, HttpServletRequest req,  Model model, RedirectAttributes redirect,
             Principal principal) {
        final Usuario usuarioSesion = getUsuario(principal);
        model.addAttribute(NOMBRE_DEFECTO_FTL, respuesta);

        try {
            respuestaService.editarRespuesta(respuesta, usuarioSesion);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro modificado con éxito");
            return "redirect:" + PATH + "?" + model.asMap().get("queryString");
        } catch (BusinessLogicException | ReflectionException ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return EDIT_TEMPLATE;
        }
    }
    
    /**
     * Permite eliminar logicamente una Respuesta especifica.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param model
     * @param req
     * @param redirect
     * @param principal
     * @return Mensaje de confirmación de la eliminación
     */
    @RequestMapping(value = {"/delete"}, method = RequestMethod.GET)
    public String delete(Model model, HttpServletRequest req, RedirectAttributes redirect, Principal principal) {
        try {
            final Integer id = Integer.parseInt(req.getParameter("id"));
            final Usuario usuarioSesion = getUsuario(principal);
            Respuesta respuesta = respuestaService.findOne(id);
            respuestaService.eliminarRespuesta(respuesta, usuarioSesion);
            model.addAttribute(AppConstants.FLASH_SUCCESS, "Tema de Capacitacion eliminada con éxito");
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, req.getParameter("id"), ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
        }

        return "redirect:" + PATH;
    }
    
    @RequestMapping(value = {"/recuperar"}, method = RequestMethod.GET)
    public String recuperar(Model model, HttpServletRequest req, RedirectAttributes redirect, Principal principal) {
        try {
            final Integer id = Integer.parseInt(req.getParameter("id"));
            final Usuario usuarioSesion = getUsuario(principal);
            Respuesta respuesta = respuestaService.findOne(id);
            respuestaService.recuperarRespuesta(respuesta, usuarioSesion);
            model.addAttribute(AppConstants.FLASH_SUCCESS, "Recurso recuperado con éxito");
            return "redirect:" + PATH;
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, req.getParameter("id"), ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return "redirect:" + PATH;
        }
    }
    
    @ModelAttribute("descriptor")
    GenDescriptor getDescriptor() {
        return GenDescriptor.find(Respuesta.class);
    }

    @ModelAttribute("activePill")
    public String getActivePill() {
        return "respuesta";
    }

    @ModelAttribute("templatePrefix")
    protected String getTemplatePrefix() {
        return "admin";
    }

    /**
     * Agrega el controlador
     *
     * @return controlador
     */
    @ModelAttribute("controller")
    public RespuestaController controller() {
        return this;
    }
}
