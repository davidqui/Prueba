package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.DocumentoObservacionDefecto;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.DocumentoObservacionDefectoService;
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
 * Controlador para {@link DocumentoObservacionDefecto}.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/23/2018 Issue #172 (SICDI-Controltech) feature-172
 */
@Controller
@PreAuthorize(value = "hasRole('ADMIN_DOC_OBSERVACION_DEFECTO')")
@RequestMapping(DocumentoObservacionDefectoController.PATH)
public class DocumentoObservacionDefectoController extends UtilController {

    private static final Logger LOG = Logger.getLogger(DocumentoObservacionDefectoController.class.getName());

    static final String PATH = "/admin/doc-observacion-defecto";

    private static final String LIST_TEMPLATE = "documento-observaciones-defecto-list";

    private static final String CREATE_TEMPLATE = "documento-observaciones-defecto-list-create";

    private static final String EDIT_TEMPLATE = "documento-observaciones-defecto-list-edit";

    private static final String OBSERVACION_DEFECTO_FTL = "observacionDefecto";

    @Autowired
    private DocumentoObservacionDefectoService documentoObservacionDefectoService;

    /**
     * Permite listar todos las observaciones por defecto para los documentos
     * del sistema
     *
     * @param all
     * @param model
     * @return Pagina de consulta de observaciones por defecto
     */
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String list(@RequestParam(value = "all", required = false, defaultValue = "false") Boolean all, Model model) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        List<DocumentoObservacionDefecto> list;
        if (!all) {
            list = documentoObservacionDefectoService.findActive(sort);
        } else {
            list = documentoObservacionDefectoService.findAll(sort);
        }

        model.addAttribute("list", list);
        model.addAttribute("all", all);

        return LIST_TEMPLATE;
    }

    /**
     * Permite visualizar el formulario de creación de una observación por
     * defecto del sistema
     *
     * @param model
     * @return Pagina que crea una observacion por defecto
     */
    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public String create(Model model) {
        DocumentoObservacionDefecto documentoObservacionDefecto = new DocumentoObservacionDefecto();
        model.addAttribute(OBSERVACION_DEFECTO_FTL, documentoObservacionDefecto);
        return CREATE_TEMPLATE;
    }

    /**
     * Permite visualizar el formulario de edición de una observación por
     * defecto del sistema
     *
     * @param model
     * @param req
     * @return Pagina que edita una observación por defecto.
     */
    @RequestMapping(value = {"/edit"}, method = RequestMethod.GET)
    public String edit(Model model, HttpServletRequest req) {
        Integer id = Integer.parseInt(req.getParameter("id"));
        DocumentoObservacionDefecto documentoObservacionDefecto = documentoObservacionDefectoService.findOne(id);
        model.addAttribute(OBSERVACION_DEFECTO_FTL, documentoObservacionDefecto);
        return EDIT_TEMPLATE;
    }

    /**
     * Permite crear una observación por defecto del sistema
     *
     * @param observacionDefecto
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return
     */
    @RequestMapping(value = {"/crear"}, method = RequestMethod.POST)
    public String crear(DocumentoObservacionDefecto observacionDefecto, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
            MultipartFile archivo, Principal principal) {
        model.addAttribute(OBSERVACION_DEFECTO_FTL, observacionDefecto);
        final Usuario usuarioSesion = getUsuario(principal);

        try {
            documentoObservacionDefectoService.crearObservacionDefecto(observacionDefecto, usuarioSesion);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
            return "redirect:" + PATH + "?" + model.asMap().get("queryString");
        } catch (BusinessLogicException | ReflectionException ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return CREATE_TEMPLATE;
        }
    }

    /**
     * Permite actualizar una observación por defecto del sistema
     *
     * @param observacionDefecto
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return
     */
    @RequestMapping(value = {"/actualizar"}, method = RequestMethod.POST)
    public String actualizar(DocumentoObservacionDefecto observacionDefecto, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
            MultipartFile archivo, Principal principal) {
        final Usuario usuarioSesion = getUsuario(principal);
        model.addAttribute(OBSERVACION_DEFECTO_FTL, observacionDefecto);

        try {
            documentoObservacionDefectoService.editarObservacionDefecto(observacionDefecto, usuarioSesion);
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
            final Integer id = Integer.parseInt(req.getParameter("id"));
            final Usuario usuarioSesion = getUsuario(principal);
            DocumentoObservacionDefecto observacionDefecto = documentoObservacionDefectoService.findOne(id);
            documentoObservacionDefectoService.eliminarObservacionDefecto(observacionDefecto, usuarioSesion);
            model.addAttribute(AppConstants.FLASH_SUCCESS, "Observación por defecto eliminada con éxito");
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, req.getParameter("id"), ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
        }

        return "redirect:" + PATH;
    }

    @ModelAttribute("descriptor")
    GenDescriptor getDescriptor() {
        return GenDescriptor.find(DocumentoObservacionDefecto.class);
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
    public DocumentoObservacionDefectoController controller() {
        return this;
    }
}
