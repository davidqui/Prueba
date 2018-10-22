package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Clasificacion;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.TemaCapacitacion;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.ClasificacionService;
import com.laamware.ejercito.doc.web.serv.TemaCapacitacionService;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.ReflectionException;
import java.security.Principal;
import java.util.List;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para {@link TemaCapacitacion}.
 *
 * @author jcespedeso@imi.mil.co 
 * @author dquijanor@imi.mil.co 
 * @author aherreram@imi.mil.co
 * @since Septiembre 3, 2018 _feature_9 (SICDI-GETDE)
 */

@Controller
@PreAuthorize(value = "hasRole('ADMIN_TEMA_CAPACITACION')")
@RequestMapping(TemaCapacitacionController.PATH)
public class TemaCapacitacionController extends UtilController {

    private static final Logger LOG = Logger.getLogger(TemaCapacitacionController.class.getName());

    static final String PATH = "/admin/temaCapacitacion";

    private static final String LIST_TEMPLATE = "temaCapacitacion-list";

    private static final String CREATE_TEMPLATE = "temaCapacitacion-list-create";

    private static final String EDIT_TEMPLATE = "temaCapacitacion-list-edit";

    private static final String NOMBRE_DEFECTO_FTL = "temaCapacitacion";

    @Autowired
    private TemaCapacitacionService temaCapacitacionService;
    
    @Autowired
    private ClasificacionService clasificacionService;

    /**
     *  Permite listar todos los temas disponibles del modulo Capacitacion  
     * 2018-10-18 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param all
     * @param page
     * @param pageSize
     * @param model
     * @return Lista de todos las TemaCapacitacions segun corresponda activas o activas y eliminadas.
     */
    @RequestMapping(value = {" "}, method = RequestMethod.GET)
    public String list(@RequestParam(value = "all", required = false, defaultValue = "false") Boolean all,
                       @RequestParam(value = "pageIndex", required = false) Integer page,
                       @RequestParam(value = "pageSize", required = false) Integer pageSize, Model model) {
        
        if (page == null || page < 0)
            page = 1;
        
        if (pageSize == null || pageSize < 0)
            pageSize = ADMIN_PAGE_SIZE;
        
        Long count;
        
        Pageable pageable = new PageRequest(page-1, pageSize, Sort.Direction.DESC, "cuando");
        
        Page<TemaCapacitacion> list;
        if (!all) {
            list = temaCapacitacionService.findActive(pageable);
        } else {
            list = temaCapacitacionService.findAll(pageable);
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
    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public String create(Model model) {
        TemaCapacitacion temaCapacitacion = new TemaCapacitacion();
        
        List<Clasificacion> clasificacions = clasificacionService.findAllActivoOrderByOrden();
        System.out.println("clasificacions = " + clasificacions+"<<<<<<<<<<<<<<<<<<<<<<<<<<<< >>>>>>>>>>>>>>>>>>>>>>>>>>>");
        model.addAttribute("clasificacions", clasificacions);
        
        model.addAttribute(NOMBRE_DEFECTO_FTL, temaCapacitacion);
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
        TemaCapacitacion temaCapacitacion = temaCapacitacionService.findOne(id);
        model.addAttribute(NOMBRE_DEFECTO_FTL, temaCapacitacion);
        return EDIT_TEMPLATE;
    }

    /**
     * Permite crear un nombre de tema de Capacitación
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param temaCapacitacion
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return Pagina para crear un nombre de tematica.
     */
    @RequestMapping(value = {"/crear"}, method = RequestMethod.POST)
    public String crear(TemaCapacitacion temaCapacitacion, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
        MultipartFile archivo, Principal principal) {
        model.addAttribute(NOMBRE_DEFECTO_FTL, temaCapacitacion);
        final Usuario usuarioSesion = getUsuario(principal);

        try {
            temaCapacitacionService.crearTemaCapacitacion(temaCapacitacion, usuarioSesion);
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
     * @param temaCapacitacion
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return Pagina que edita un Tema de Capacitación.
     */
    @RequestMapping(value = {"/actualizar"}, method = RequestMethod.POST)
    public String actualizar(TemaCapacitacion temaCapacitacion, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
            MultipartFile archivo, Principal principal) {
        final Usuario usuarioSesion = getUsuario(principal);
        model.addAttribute(NOMBRE_DEFECTO_FTL, temaCapacitacion);

        try {
            temaCapacitacionService.editarTemaCapacitacion(temaCapacitacion, usuarioSesion);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro modificado con éxito");
            return "redirect:" + PATH + "?" + model.asMap().get("queryString");
        } catch (BusinessLogicException | ReflectionException ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return EDIT_TEMPLATE;
        }
    }
    
    /**
     * Permite eliminar logicamente una TemaCapacitacion especifica.
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
            TemaCapacitacion temaCapacitacion = temaCapacitacionService.findOne(id);
            temaCapacitacionService.eliminarTemaCapacitacion(temaCapacitacion, usuarioSesion);
            model.addAttribute(AppConstants.FLASH_SUCCESS, "Tema de Capacitacion eliminada con éxito");
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, req.getParameter("id"), ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
        }

        return "redirect:" + PATH;
    }
    
    @ModelAttribute("descriptor")
    GenDescriptor getDescriptor() {
        return GenDescriptor.find(TemaCapacitacion.class);
    }

    @ModelAttribute("activePill")
    public String getActivePill() {
        return "tematica";
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
    public TemaCapacitacionController controller() {
        return this;
    }
}
