package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.TransJustificacionDefecto;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.TransJustificacionDefectoService;
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
 * Controlador para {@link TransJustificacionDefectoController}.
 *
 * @author dquijanor
 *
 */
@Controller
@PreAuthorize(value = "hasRole('ADMIN_TRANS_JUSTIF_DEFECTO')")
@RequestMapping(TransJustificacionDefectoController.PATH)
public class TransJustificacionDefectoController extends UtilController {

    private static final Logger LOG = Logger.getLogger(TransJustificacionDefectoController.class.getName());

    static final String PATH = "/admin/transjustificaciondefecto";

    private static final String LIST_TEMPLATE = "trans-justificacion-defecto-list";

    private static final String CREATE_TEMPLATE = "trans-justificacion-defecto-create";

    private static final String EDIT_TEMPLATE = "trans-justificacion-defecto-edit";

    private static final String NOMBRE_DEFECTO_FTL = "justificacionDefecto";

    @Autowired
    private TransJustificacionDefectoService justificacionDefectoService;

    /**
     *  Permite listar todos los justificacion tranferencia por defecto
     * 
     * @param all
     * @param model
     * @return 
     */
     
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String list(@RequestParam(value = "all", required = false, defaultValue = "false") Boolean all, Model model) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        
        List<TransJustificacionDefecto> list;
        if (!all) {
            list = justificacionDefectoService.findActive(sort);
        } else {
            list = justificacionDefectoService.findAll(sort);
        }
        
        model.addAttribute("list", list);
        model.addAttribute("all", all);

        return LIST_TEMPLATE;
    }

    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public String create(Model model) {
        TransJustificacionDefecto transJustificacionDefecto = new TransJustificacionDefecto();
        model.addAttribute(NOMBRE_DEFECTO_FTL, transJustificacionDefecto);
        return CREATE_TEMPLATE;
    }

    /**
     * Permite visualizar el formulario de edición de un nombre de la observacion transferencia por defecto
     *
     * @param model
     * @param req
     * @return Pagina que edita un nombre de la observacion transferencia por defecto
     */
     
 
    @RequestMapping(value = {"/edit"}, method = RequestMethod.GET)
    public String edit(Model model, HttpServletRequest req) {
        Long id = Long.parseLong(req.getParameter("id"));
        TransJustificacionDefecto transJustificacionDefecto = justificacionDefectoService.findOne(id);
        model.addAttribute(NOMBRE_DEFECTO_FTL, transJustificacionDefecto);
        return EDIT_TEMPLATE;
    }

    /**
     * Permite crear un nombre de la observacion transferencia por defecto
     *
     * @param justificacionDefecto
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return Pagina para crear un nombre de expediente.
     */
    @RequestMapping(value = {"/crear"}, method = RequestMethod.POST)
    public String crear(TransJustificacionDefecto justificacionDefecto, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
        MultipartFile archivo, Principal principal) {
        model.addAttribute(NOMBRE_DEFECTO_FTL, justificacionDefecto);
        final Usuario usuarioSesion = getUsuario(principal);

        try {
            justificacionDefectoService.crearTransJustificacionDefecto(justificacionDefecto, usuarioSesion);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
            return "redirect:" + PATH + "?" + model.asMap().get("queryString");
        } catch (BusinessLogicException | ReflectionException ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return CREATE_TEMPLATE;
        }
    }

    /**
     * Permite actualizar un nombre de de la observacion transferencia por defecto
     *
     * @param justificacionDefecto
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return Pagina que edita un nombre de la observacion transferencia por defecto
     */
    @RequestMapping(value = {"/actualizar"}, method = RequestMethod.POST)
    public String actualizar(TransJustificacionDefecto justificacionDefecto, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
            MultipartFile archivo, Principal principal) {
        final Usuario usuarioSesion = getUsuario(principal);
        model.addAttribute(NOMBRE_DEFECTO_FTL, justificacionDefecto);

        try {
            justificacionDefectoService.editarTransJustificacionDefecto(justificacionDefecto, usuarioSesion);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
            return "redirect:" + PATH + "?" + model.asMap().get("queryString");
        } catch (BusinessLogicException | ReflectionException ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return EDIT_TEMPLATE;
        }
    }
    
    /**
     * *
     * Permite eliminar una observación por defecto del sistema
     *
     * @param model
     * @param req
     * @param redirect
     * @param principal
     * @return
     */
    @RequestMapping(value = {"/delete"}, method = RequestMethod.GET)
    public String delete(Model model, HttpServletRequest req, RedirectAttributes redirect, Principal principal) {
        try {
            final Long id = Long.parseLong(req.getParameter("id"));
            final Usuario usuarioSesion = getUsuario(principal);
            TransJustificacionDefecto justificacionDefecto = justificacionDefectoService.findOne(id);
            justificacionDefectoService.eliminarTransJustificacionDefecto(justificacionDefecto, usuarioSesion);
            model.addAttribute(AppConstants.FLASH_SUCCESS, "Texto Observación por defecto eliminada con éxito");
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, req.getParameter("id"), ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
        }

        return "redirect:" + PATH;
    }

    @ModelAttribute("descriptor")
    GenDescriptor getDescriptor() {
        return GenDescriptor.find(TransJustificacionDefecto.class);
    }

    @ModelAttribute("activePill")
    public String getActivePill() {
        return "transjustificaciondefecto";
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
    public TransJustificacionDefectoController controller() {
        return this;
    }
}
