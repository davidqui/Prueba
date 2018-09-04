package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.RecursoMultimedia;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.RecursoMultimediaService;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.ReflectionException;
import java.security.Principal;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para {@link RecursoMultimedia}.
 *
 * @author jcespedeso@imi.mil.co 
 * @author dquijanor@imi.mil.co 
 * @author aherreram@imi.mil.co
 * @since Septiembre 3, 2018 _feature_9 (SICDI-GETDE)
 */

@Controller
@PreAuthorize(value = "hasRole('ADMIN_RECURSO_MULTIMEDIA')")
@RequestMapping(RecursoMultimediaController.PATH)
public class RecursoMultimediaController extends UtilController {

    private static final Logger LOG = Logger.getLogger(RecursoMultimediaController.class.getName());

    static final String PATH = "/admin/recursoMultimedia";

    private static final String LIST_TEMPLATE = "recursoMultimedia-list";

    private static final String CREATE_TEMPLATE = "recursoMultimedia-list-create";

    private static final String EDIT_TEMPLATE = "recursoMultimedia-list-edit";

    private static final String NOMBRE_DEFECTO_FTL = "recursoMultimedia";

    @Autowired
    private RecursoMultimediaService recursoMultimediaService;

    /**
     *  Permite listar todos los recursos multimedia.
     * 
     * @param all
     * @param model
     * @return 
     */
     
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String list(@RequestParam(value = "all", required = false, defaultValue = "false") Boolean all, Model model) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        
        List<RecursoMultimedia> list = recursoMultimediaService.findAll();
        model.addAttribute("list", list);
        model.addAttribute("all", all);
        return LIST_TEMPLATE;
    }

    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public String create(Model model) {
        RecursoMultimedia recursoMultimedia = new RecursoMultimedia();
        model.addAttribute(NOMBRE_DEFECTO_FTL, recursoMultimedia);
        return CREATE_TEMPLATE;
    }

    /**
     * Permite visualizar el formulario de edición de un recurso multimedia.
     *
     * @param model
     * @param req  conjunto de data recibida por intermedio del request a traves del formulario. 
     * @return Pagina que edita un nombre de expediente.
     */
    
    @RequestMapping(value = {"/edit"}, method = RequestMethod.GET)
    public String edit(Model model, HttpServletRequest req) {
        Integer id = Integer.parseInt(req.getParameter("id"));
        RecursoMultimedia recursoMultimedia = recursoMultimediaService.findOne(id);
        model.addAttribute(NOMBRE_DEFECTO_FTL, recursoMultimedia);
        return EDIT_TEMPLATE;
    }

    /**
     * Permite crear el recurso multimedia.
     *
     * @param recursoMultimedia
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return Pagina para crear un nombre de expediente.
     */
    @RequestMapping(value = {"/crear"}, method = RequestMethod.POST)
    public String crear(RecursoMultimedia recursoMultimedia, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
        MultipartFile archivo, Principal principal) {
        model.addAttribute(NOMBRE_DEFECTO_FTL, recursoMultimedia);
        final Usuario usuarioSesion = getUsuario(principal);

        try {
            recursoMultimediaService.crearRecursoMultimedia(recursoMultimedia, usuarioSesion);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
            return "redirect:" + PATH + "?" + model.asMap().get("queryString");
        } catch (BusinessLogicException | ReflectionException ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return CREATE_TEMPLATE;
        }
    }

    /**
     * Permite actualizar un recurso multimedia.
     *
     * @param recursoMultimedia
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return Pagina que edita un nombre de expediente.
     */
    @RequestMapping(value = {"/actualizar"}, method = RequestMethod.POST)
    public String actualizar(RecursoMultimedia recursoMultimedia, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
            MultipartFile archivo, Principal principal) {
        final Usuario usuarioSesion = getUsuario(principal);
        model.addAttribute(NOMBRE_DEFECTO_FTL, recursoMultimedia);

        try {
            recursoMultimediaService.editarRecursoMultimedia(recursoMultimedia, usuarioSesion);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
            return "redirect:" + PATH + "?" + model.asMap().get("queryString");
        } catch (BusinessLogicException | ReflectionException ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return EDIT_TEMPLATE;
        }
    }

    @RequestMapping(value = {"/delete"}, method = RequestMethod.GET)
    public String delete(Model model, HttpServletRequest req, RedirectAttributes redirect, Principal principal) {
        try {
            final Integer id = Integer.parseInt(req.getParameter("id"));
            final Usuario usuarioSesion = getUsuario(principal);
            RecursoMultimedia recursoMultimedia = recursoMultimediaService.findOne(id);
            recursoMultimediaService.eliminarRecursoMultimedia(recursoMultimedia, usuarioSesion);
            model.addAttribute(AppConstants.FLASH_SUCCESS, "Recurso eliminado con éxito");
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, req.getParameter("id"), ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
        }

        return "redirect:" + PATH;
    }
    
    @ModelAttribute("descriptor")
    GenDescriptor getDescriptor() {
        return GenDescriptor.find(RecursoMultimedia.class);
    }

    @ModelAttribute("activePill")
    public String getActivePill() {
        return "recursoMultimedia";
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
    public RecursoMultimediaController controller() {
        return this;
    }
}
