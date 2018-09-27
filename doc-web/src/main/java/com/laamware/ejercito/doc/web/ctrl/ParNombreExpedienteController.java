package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.ParNombreExpediente;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.ParNombreExpedienteService;
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
 * Controlador para {@link ParNombreExpediente}.
 *
 * @author dquijanor
 *
 */
@Controller
@PreAuthorize(value = "hasRole('ADMIN_PAR_NOMBRE_EXPEDIENTE')")
@RequestMapping(ParNombreExpedienteController.PATH)
public class ParNombreExpedienteController extends UtilController {

    private static final Logger LOG = Logger.getLogger(ParNombreExpedienteController.class.getName());

    static final String PATH = "/admin/parnombrexpediente";

    private static final String LIST_TEMPLATE = "par-nombre-expediente-list";

    private static final String CREATE_TEMPLATE = "par-nombre-expediente-list-create";

    private static final String EDIT_TEMPLATE = "par-nombre-expediente-list-edit";

    private static final String NOMBRE_DEFECTO_FTL = "nombreExpediente";

    @Autowired
    private ParNombreExpedienteService parNombreExpedienteService;

    /**
     *  Permite listar todos los nombres de expedientes
     * 
     * @param all
     * @param model
     * @return 
     */
    
    /**
    * 2018-09-24 samuel.delgado@controltechcg.com Issue #174 (SICDI-Controltech)
    * feature-174: Adición para la paginación.
    */
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String list(@RequestParam(value = "all", required = false, defaultValue = "false") Boolean all, 
            @RequestParam(value = "pageIndex", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            Model model) {
        
        if (page == null || page < 0)
            page = 1;
        if (pageSize == null || pageSize < 0)
            pageSize = ADMIN_PAGE_SIZE;
        
        Pageable pageable = new PageRequest(page-1, pageSize, Sort.Direction.ASC, "fecCreacion");

        Page<ParNombreExpediente> list = parNombreExpedienteService.findAll(pageable);
        Long count = list.getTotalElements();
        adminPageable(count, model, page, pageSize);
        model.addAttribute("list", list.getContent());
        model.addAttribute("all", all);

        return LIST_TEMPLATE;
    }

    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public String create(Model model) {
        ParNombreExpediente parNombreExpediente = new ParNombreExpediente();
        model.addAttribute(NOMBRE_DEFECTO_FTL, parNombreExpediente);
        return CREATE_TEMPLATE;
    }

    /**
     * Permite visualizar el formulario de edición de un nombre de expediente
     * por
     *
     * @param model
     * @param req
     * @return Pagina que edita un nombre de expediente.
     */
     
 
    @RequestMapping(value = {"/edit"}, method = RequestMethod.GET)
    public String edit(Model model, HttpServletRequest req) {
        Long id = Long.parseLong(req.getParameter("id"));
        ParNombreExpediente parNombreExpediente = parNombreExpedienteService.findOne(id);
        model.addAttribute(NOMBRE_DEFECTO_FTL, parNombreExpediente);
        return EDIT_TEMPLATE;
    }

    /**
     * Permite crear un nombre de expediente
     *
     * @param nombreExpediente
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return Pagina para crear un nombre de expediente.
     */
    @RequestMapping(value = {"/crear"}, method = RequestMethod.POST)
    public String crear(ParNombreExpediente nombreExpediente, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
        MultipartFile archivo, Principal principal) {
        model.addAttribute(NOMBRE_DEFECTO_FTL, nombreExpediente);
        final Usuario usuarioSesion = getUsuario(principal);

        try {
            parNombreExpedienteService.crearParNombreExpediente(nombreExpediente, usuarioSesion);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
            return "redirect:" + PATH + "?" + model.asMap().get("queryString");
        } catch (BusinessLogicException | ReflectionException ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return CREATE_TEMPLATE;
        }
    }

    /**
     * Permite actualizar un nombre de expediente
     *
     * @param nombreExpediente
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return Pagina que edita un nombre de expediente.
     */
    @RequestMapping(value = {"/actualizar"}, method = RequestMethod.POST)
    public String actualizar(ParNombreExpediente nombreExpediente, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
            MultipartFile archivo, Principal principal) {
        final Usuario usuarioSesion = getUsuario(principal);
        model.addAttribute(NOMBRE_DEFECTO_FTL, nombreExpediente);

        try {
            parNombreExpedienteService.editarParNombreExpediente(nombreExpediente, usuarioSesion);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
            return "redirect:" + PATH + "?" + model.asMap().get("queryString");
        } catch (BusinessLogicException | ReflectionException ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return EDIT_TEMPLATE;
        }
    }

    @ModelAttribute("descriptor")
    GenDescriptor getDescriptor() {
        return GenDescriptor.find(ParNombreExpediente.class);
    }

    @ModelAttribute("activePill")
    public String getActivePill() {
        return "parnombrexpediente";
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
    public ParNombreExpedienteController controller() {
        return this;
    }
}
