package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Pregunta;
import com.laamware.ejercito.doc.web.entity.TemaCapacitacion;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.PreguntaService;
import com.laamware.ejercito.doc.web.serv.TemaCapacitacionService;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.ReflectionException;
import java.security.Principal;
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
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para {@link Pregunta}.
 *
 * @author jcespedeso@imi.mil.co
 * @author dquijanor@imi.mil.co
 * @author dquijanor@imi.mil.co
 * @since Septiembre 3, 2018 _feature_9 (SICDI-GETDE)
 */
@Controller
@PreAuthorize(value = "hasRole('ADMIN_TEMA_CAPACITACION')")
@RequestMapping(PreguntaController.PATH)
public class PreguntaController extends UtilController {
    
    private static final Logger LOG = Logger.getLogger(PreguntaController.class.getName());

    static final String PATH = "/admin/pregunta";

    private static final String LIST_TEMPLATE = "pregunta-list";

    private static final String CREATE_TEMPLATE = "pregunta-list-create";

    private static final String EDIT_TEMPLATE = "pregunta-list-edit";

    private static final String NOMBRE_DEFECTO_FTL = "pregunta";

    @Autowired
    private PreguntaService preguntaService;
    
    @Autowired
    private TemaCapacitacionService temaCapacitacionService;
    
    /**
     * Lista los Recursos Multimedia Activos por Pregunta
     * 
     * 2018-10-24 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * 
     * @param key Id de una Pregunta.
     * @param all 
     * @param page 
     * @param pageSize 
     * @param model Parametro requerido por clase de spring
     * @param redirect Parametro requerido por clase de spring
     * @return Según corresponda genera un Lista de Recursos Multimedia Activos y por Pregunta 
     * o los Recursos Multimedia de una Pregunta. 
     */
    @RequestMapping(value = {"/list/{key}"}, method = RequestMethod.GET)
    public String listByPregunta(@PathVariable(value = "key") Integer key, 
                                 @RequestParam(value = "all", required = false, defaultValue = "false") Boolean all, 
                                 @RequestParam(value = "pageIndex", required = false) Integer page,
                                 @RequestParam(value = "pageSize", required = false) Integer pageSize, Model model, RedirectAttributes redirect) {
        
        TemaCapacitacion seleccionPregunta = temaCapacitacionService.findOne(key);
        model.addAttribute("preguntaView", seleccionPregunta);

        if (page == null || page < 0)
            page = 1;
        
        if (pageSize == null || pageSize < 0)
            pageSize = ADMIN_PAGE_SIZE;
        
        Long count;
        
        Pageable pageable = new PageRequest(page-1, pageSize, Sort.Direction.DESC, "cuando");

        Page <Pregunta> list;
        if (!all) {
            list = preguntaService.findActiveAndTemaCapacitacionPage(pageable, key);
        } else {
            list = preguntaService.findAllByTemaCapacitacionPage(pageable, seleccionPregunta);
        }
        
        count = list.getTotalElements();
        adminPageable(count, model, page, pageSize);
        model.addAttribute("list", list.getContent());
        model.addAttribute("all", all);
        
        return LIST_TEMPLATE;
    }
    
    /**
     * Carga los datos necesarios al formulario para crear un recurso Pregunta.
     * 
     * @param key 
     * @param model Parametro requerido por clase de spring
     * @return Template de creacion.
     */
    @RequestMapping(value = {"/create/{key}"}, method = RequestMethod.GET)
    public String create(@PathVariable(value = "key") Integer key, Model model) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        
        Pregunta pregunta = new Pregunta();
        model.addAttribute(NOMBRE_DEFECTO_FTL, pregunta);
        model.addAttribute("temaCapacitacion", temaCapacitacionService.findActive(sort));
        TemaCapacitacion listPregunta = temaCapacitacionService.findOne(key);
        model.addAttribute("temaCapacitacionCrear", listPregunta);
        return CREATE_TEMPLATE;
    }

    /**
     * Permite visualizar el formulario de edición de un recurso Pregunta.
     *
     * 2018-10-24 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * 
     * @param model Parametro requerido por clase de spring
     * @param req Conjunto de data recibida por intermedio del request a traves
     * del formulario.
     * @return Pagina que permite editar un recurso Pregunta.
     */
    @RequestMapping(value = {"/edit"}, method = RequestMethod.GET)
    public String edit(Model model, HttpServletRequest req) {
        Integer id = Integer.parseInt(req.getParameter("id"));
        Pregunta laPregunta = preguntaService.findOne(id);
        model.addAttribute(NOMBRE_DEFECTO_FTL, laPregunta);
        TemaCapacitacion TemaCap = temaCapacitacionService.findOne(laPregunta.getTemaCapacitacion().getId());
        model.addAttribute("temaCapacitacionEditar", TemaCap);
        return EDIT_TEMPLATE;
    }

    
    /**
     * Modela en la vista los datos necesarios para crear un recurso Pregunta.
     * 
     * 2018-10-24 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * 
     * @param pregunta Datos del Objeto Recursos Multimedia tomados desde el formulario de creación.
     * @param req Conjunto de data recibida por intermedio del request a traves del formulario.
     * @param model Parametro requerido por clase de spring
     * @param redirect Parametro requerido por clase de spring
     * @param principal Id del Usuario en sesión.
     * @param temaCapacitacionId
     * @return Según corresponda pagina con el listado de los recursos Pregunta Activos incluyendo el recien creado 
     * o a la misma vista de creacion en el caso de que se presente una Exception.
     
     */

    @RequestMapping(value = {"/crear"}, method = RequestMethod.POST)
    public String crear(@RequestParam(value = "temaCapacitacion", required = true) Integer temaCapacitacionId, 
            @RequestParam(value = "pregunta", required = true) String pregunta, HttpServletRequest req, Model model,
            RedirectAttributes redirect,
         Principal principal) {
        model.addAttribute(NOMBRE_DEFECTO_FTL, pregunta);
        final Usuario usuarioSesion = getUsuario(principal);
        TemaCapacitacion temaCapacitacion = temaCapacitacionService.findOne(temaCapacitacionId);
        try {
            
            preguntaService.crearPregunta(pregunta, temaCapacitacion,  usuarioSesion);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
            
            return "redirect:" + PATH + "/list/"+temaCapacitacionId;
        } catch (BusinessLogicException | ReflectionException ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
        }
            return CREATE_TEMPLATE;
    }
        
    /**
     * Permite actualizar un recurso Pregunta.
     *
     * 2018-10-24 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * 
     * @param pregunta
     * @param req Conjunto de data recibida por intermedio del request a traves del formulario.
     * @param model Parametro requerido por clase de spring
     * @param redirect Parametro requerido por clase de spring 
     * @param principal Usuarion en la sesión activa. 
     * @return Segun corresponda redirige al listado de Recursos Multimedia Activos incluyendo el recien modificado
     * o a la misma vista de edición en el caso de que se presente una Exception.
     */
    @RequestMapping(value = {"/actualizar/{id}"}, method = RequestMethod.POST)
    public String actualizar(@RequestParam(value = "id", required = true) Integer id, 
            @RequestParam(value = "pregunta", required = true) String pregunta, HttpServletRequest req,  Model model, RedirectAttributes redirect,
            Principal principal) {
        final Usuario usuarioSesion = getUsuario(principal);
        model.addAttribute(NOMBRE_DEFECTO_FTL, pregunta);
        Pregunta pregunta1 = preguntaService.findOne(id);

        try {
            preguntaService.editarPregunta(pregunta1, usuarioSesion);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
            return "redirect:" +PATH+"/list/"+id;
        } catch (BusinessLogicException | ReflectionException ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            TemaCapacitacion listPregunta = temaCapacitacionService.findOne(id);
            model.addAttribute("temasEditar", listPregunta);
            return EDIT_TEMPLATE;
        }
    }
    
    /**
     * Permite eliminar logicamente un registro de recurso Pregunta.
     * 
     * 2018-10-24 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * 
     * @param model Parametro requerido por clase de spring
     * @param req Conjunto de data recibida por intermedio del request a traves del formulario.
     * @param redirect Parametro requerido por clase de spring
     * @param principal Id del usuario en la Sesión activa.
     * @return Según corresponda redirige al listado de Recursos Multimedia Activos y sin el registro eliminado o
     * presente una Exception.
     */
    @RequestMapping(value = {"/delete"}, method = RequestMethod.GET)
    public String delete(Model model, HttpServletRequest req, RedirectAttributes redirect, Principal principal) {
        try {
            final Integer id = Integer.parseInt(req.getParameter("id"));
            final Usuario usuarioSesion = getUsuario(principal);
            Pregunta pregunta = preguntaService.findOne(id);
            preguntaService.eliminarPregunta(pregunta, usuarioSesion);
            model.addAttribute(AppConstants.FLASH_SUCCESS, "Recurso eliminado con éxito");
            return "redirect:" + PATH+"/list/"+pregunta.getTemaCapacitacion().getId();
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, req.getParameter("id"), ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return "redirect:" + PATH;
        }

    }
    
    /**
     * Cambia el estado de un Recurso Multimedia de eliminado a Activo.
     * 
     * 2018-10-24 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * 
     * @param model Parametro requerido por clase de spring.
     * @param req Conjunto de data recibida por intermedio del request a traves del formulario.
     * @param redirect Parametro requerido por clase de spring.
     * @param principal Id del usuario activo en sesión.
     * @return Según corresponda redirige al listado de Recursos Multimedia Activos o 
     * presente una Exception.
     */
    @RequestMapping(value = {"/recuperar"}, method = RequestMethod.GET)
    public String recuperar(Model model, HttpServletRequest req, RedirectAttributes redirect, Principal principal) {
        try {
            final Integer id = Integer.parseInt(req.getParameter("id"));
            final Usuario usuarioSesion = getUsuario(principal);
            Pregunta pregunta = preguntaService.findOne(id);
            preguntaService.recuperarPregunta(pregunta, usuarioSesion);
            model.addAttribute(AppConstants.FLASH_SUCCESS, "Recurso recuperado con éxito");
            return "redirect:" + PATH+"/list/"+pregunta.getTemaCapacitacion().getId();
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, req.getParameter("id"), ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return "redirect:" + PATH;
        }
    }
    
    
    @ModelAttribute("descriptor")
    GenDescriptor getDescriptor() {
        return GenDescriptor.find(Pregunta.class);
    }

    @ModelAttribute("activePill")
    public String getActivePill() {
        return "pregunta";
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
    public PreguntaController controller() {
        return this;
    }
    
}
