package com.laamware.ejercito.doc.web.ctrl;

import static com.laamware.ejercito.doc.web.ctrl.RazonInhabilitarController.PATH;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.RazonInhabilitar;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.RazonInhabilitarService;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.ReflectionException;
import java.security.Principal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para {@link RazonInhabilitar}.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/23/2018 Issue #172 (SICDI-Controltech) feature-172
 */
@Controller
@PreAuthorize(value = "hasRole('ADMIN_RAZON_INHABILITAR')")
@RequestMapping(RazonInhabilitarController.PATH)
public class RazonInhabilitarController extends UtilController{
    private static final Logger LOG = Logger.getLogger(RazonInhabilitarService.class.getName());
    
    static final String PATH = "/admin/razon-inhabilitar";

    private static final String LIST_TEMPLATE = "razon-inhabilitar-list";

    private static final String CREATE_TEMPLATE = "razon-inhabilitar-create";

    private static final String EDIT_TEMPLATE = "razon-inhabilitar-edit";

    private static final String RAZON_INHABILITAR_FTL = "razonInhabilitar";
    
    @Autowired
    private RazonInhabilitarService razonInhabilitarService;
    
    /**
     * Permite listar todos las razones inhabilitar usuario para los documentos
     * del sistema
     *
     * @param all
     * @param model
     * @return Pagina de consulta de razones inhabilitar usuario
     */
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String list(@RequestParam(value = "all", required = false, defaultValue = "false") Boolean all, Model model) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        List<RazonInhabilitar> list;
        if (!all) {
            list = razonInhabilitarService.findActive(sort);
        } else {
            list = razonInhabilitarService.findAll(sort);
        }

        model.addAttribute("list", list);
        model.addAttribute("all", all);

        return LIST_TEMPLATE;
    }

    /**
     * Permite visualizar el formulario de creación de una razón inhabilitar usuario
     * del sistema
     *
     * @param model
     * @return Pagina que crea una observacion por defecto
     */
    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public String create(Model model) {
        RazonInhabilitar razonInhabilitar = new RazonInhabilitar();
        model.addAttribute(RAZON_INHABILITAR_FTL, razonInhabilitar);
        return CREATE_TEMPLATE;
    }

    /**
     * Permite visualizar el formulario de edición de una razón inhabilitar usuario
     * del sistema
     *
     * @param model
     * @param req
     * @return Pagina que edita una razón inhabilitar usuario defecto.
     */
    @RequestMapping(value = {"/edit"}, method = RequestMethod.GET)
    public String edit(Model model, HttpServletRequest req) {
        Integer id = Integer.parseInt(req.getParameter("id"));
        RazonInhabilitar razonInhabilitar = razonInhabilitarService.findOne(id);
        model.addAttribute(RAZON_INHABILITAR_FTL, razonInhabilitar);
        return EDIT_TEMPLATE;
    }

    /**
     * Permite crear una razón inhabilitar usuario del sistema
     *
     * @param razonInhabilitar
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return
     */
    @RequestMapping(value = {"/crear"}, method = RequestMethod.POST)
    public String crear(RazonInhabilitar razonInhabilitar, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect, Principal principal) {
        model.addAttribute(RAZON_INHABILITAR_FTL, razonInhabilitar);
        final Usuario usuarioSesion = getUsuario(principal);

        try {
            razonInhabilitarService.crearRazonInhabilitar(razonInhabilitar, usuarioSesion);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
            return "redirect:" + PATH + "?" + model.asMap().get("queryString");
        } catch (BusinessLogicException | ReflectionException ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return CREATE_TEMPLATE;
        }
    }

    /**
     * Permite actualizar una razón inhabilitar usuario del sistema
     *
     * @param razonInhabilitar
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return
     */
    @RequestMapping(value = {"/actualizar"}, method = RequestMethod.POST)
    public String actualizar(RazonInhabilitar razonInhabilitar, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
            MultipartFile archivo, Principal principal) {
        final Usuario usuarioSesion = getUsuario(principal);
        model.addAttribute(RAZON_INHABILITAR_FTL, razonInhabilitar);

        try {
            razonInhabilitarService.editarRazonInhabilitar(razonInhabilitar, usuarioSesion);
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
     * Permite eliminar una razón inhabilitar usuario del sistema
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
            final Integer id = Integer.parseInt(req.getParameter("id"));
            final Usuario usuarioSesion = getUsuario(principal);
            RazonInhabilitar razonInhabilitar = razonInhabilitarService.findOne(id);
            razonInhabilitarService.eliminarRazonInhabilitar(razonInhabilitar, usuarioSesion);
            model.addAttribute(AppConstants.FLASH_SUCCESS, "Observación por defecto eliminada con éxito");
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, req.getParameter("id"), ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
        }

        return "redirect:" + PATH;
    }

    @ModelAttribute("descriptor")
    GenDescriptor getDescriptor() {
        return GenDescriptor.find(RazonInhabilitar.class);
    }

    @ModelAttribute("activePill")
    public String getActivePill() {
        return "doc-observacion-defecto";
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
    public RazonInhabilitarController controller() {
        return this;
    }
}
