package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Tematica;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.TematicaService;
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
 * Controlador para {@link Tematica}.
 *
 * @author jcespedeso@imi.mil.co 
 * @author dquijanor@imi.mil.co 
 * @author aherreram@imi.mil.co
 * @since Septiembre 3, 2018 _feature_9 (SICDI-GETDE)
 */

@Controller
@PreAuthorize(value = "hasRole('ADMIN_TEMATICA')")
@RequestMapping(TematicaController.PATH)
public class TematicaController extends UtilController {

    private static final Logger LOG = Logger.getLogger(TematicaController.class.getName());

    static final String PATH = "/admin/tematica";

    private static final String LIST_TEMPLATE = "tematica-list";

    private static final String CREATE_TEMPLATE = "tematica-list-create";

    private static final String EDIT_TEMPLATE = "tematica-list-edit";

    private static final String NOMBRE_DEFECTO_FTL = "tematica";

    @Autowired
    private TematicaService tematicaService;

    /**
     *  Permite listar todos los temas disponibles del modulo tematica  
     * 
     * @param all
     * @param model
     * @return 
     */
     
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String list(@RequestParam(value = "all", required = false, defaultValue = "false") Boolean all, Model model) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        
        List<Tematica> list = tematicaService.findAll();
        model.addAttribute("list", list);
        model.addAttribute("all", all);
        return LIST_TEMPLATE;
    }

    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public String create(Model model) {
        Tematica tematica = new Tematica();
        model.addAttribute(NOMBRE_DEFECTO_FTL, tematica);
        return CREATE_TEMPLATE;
    }

    /**
     * Permite visualizar el formulario de edición de una tematica en especifico
     
     * @param model
     * @param req  conjunto de data recibida por intermedio del request a traves del formulario 
     * @return Pagina que edita un nombre de tematica.
     */
    
    @RequestMapping(value = {"/edit"}, method = RequestMethod.GET)
    public String edit(Model model, HttpServletRequest req) {
        Integer id = Integer.parseInt(req.getParameter("id"));
        Tematica tematica = tematicaService.findOne(id);
        model.addAttribute(NOMBRE_DEFECTO_FTL, tematica);
        return EDIT_TEMPLATE;
    }

    /**
     * Permite crear un nombre de tematica
     *
     * @param tematica
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return Pagina para crear un nombre de tematica.
     */
    @RequestMapping(value = {"/crear"}, method = RequestMethod.POST)
    public String crear(Tematica tematica, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
        MultipartFile archivo, Principal principal) {
        model.addAttribute(NOMBRE_DEFECTO_FTL, tematica);
        final Usuario usuarioSesion = getUsuario(principal);

        try {
            tematicaService.crearTematica(tematica, usuarioSesion);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
            return "redirect:" + PATH + "?" + model.asMap().get("queryString");
        } catch (BusinessLogicException | ReflectionException ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return CREATE_TEMPLATE;
        }
    }

    /**
     * Permite actualizar una tematica
     *
     * @param tematica
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return Pagina que edita un nombre de tematica.
     */
    @RequestMapping(value = {"/actualizar"}, method = RequestMethod.POST)
    public String actualizar(Tematica tematica, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
            MultipartFile archivo, Principal principal) {
        final Usuario usuarioSesion = getUsuario(principal);
        model.addAttribute(NOMBRE_DEFECTO_FTL, tematica);

        try {
            tematicaService.editarTematica(tematica, usuarioSesion);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro modificado con éxito");
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
            Tematica tematica = tematicaService.findOne(id);
            tematicaService.eliminarTematica(tematica, usuarioSesion);
            model.addAttribute(AppConstants.FLASH_SUCCESS, "Tematica eliminada con éxito");
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, req.getParameter("id"), ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
        }

        return "redirect:" + PATH;
    }
    
    @ModelAttribute("descriptor")
    GenDescriptor getDescriptor() {
        return GenDescriptor.find(Tematica.class);
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
    public TematicaController controller() {
        return this;
    }
}
