package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.dto.PaginacionDTO;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.DestinoExterno;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.DestinoExternoService;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.PaginacionUtil;
import com.laamware.ejercito.doc.web.util.ReflectionException;
import static java.lang.Math.toIntExact;
import java.security.Principal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
 * Controlador administrado para {@link DestinoExterno}.
 *
 * @author samuel.delgado@controltechcg.com 
 * @since 1.8
 * @version 31/08/2018 Issue gogs #10 (SICDI-Controltech) feature-gogs-10
 */
@Controller
@PreAuthorize(value = "hasRole('ADMIN_DESTINO_EXTERNO')")
@RequestMapping(AdminDestinoExternoController.PATH)
public class AdminDestinoExternoController extends UtilController{
    
    private static final Logger LOG = Logger.getLogger(AdminDestinoExternoController.class.getName());
    
    static final String PATH = "/admin/destino-externo";

    private static final String LIST_TEMPLATE = "destino-externo-list";

    private static final String CREATE_TEMPLATE = "destino-externo-create";

    private static final String EDIT_TEMPLATE = "destino-externo-edit";

    private static final String DESTINO_EXTERNO_FTL = "destinoExterno";

    @Autowired
    private DestinoExternoService destinoExternoService;

    /**
     * Permite listar todos las destino externo para los documentos
     * del sistema
     *
     * @param all
     * @param model
     * @return Pagina de consulta de destino externo
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
        
        Long count;
        
        Pageable pageable = new PageRequest(page-1, pageSize, Sort.Direction.ASC, "nombre");
        
        Page<DestinoExterno> list;
        if (!all) {
            list = destinoExternoService.findActive(pageable);
        } else {
            list = destinoExternoService.findAll(pageable);
        }
        
        count = list.getTotalElements();
        adminPageable(count, model, page, pageSize);
        model.addAttribute("list", list.getContent());
        model.addAttribute("all", all);

        return LIST_TEMPLATE;
    }

    /**
     * Permite visualizar el formulario de creación de un destino externo
     * defecto del sistema
     *
     * @param model
     * @return Pagina que crea un destino externo
     */
    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public String create(Model model) {
        DestinoExterno destinoExterno = new DestinoExterno();
        model.addAttribute(DESTINO_EXTERNO_FTL, destinoExterno);
        return CREATE_TEMPLATE;
    }

    /**
     * Permite visualizar el formulario de edición de un destino externo
     * defecto del sistema
     *
     * @param model
     * @param req
     * @return Pagina que edita un destino externo.
     */
    @RequestMapping(value = {"/edit"}, method = RequestMethod.GET)
    public String edit(Model model, HttpServletRequest req) {
        Integer id = Integer.parseInt(req.getParameter("id"));
        DestinoExterno destinoExterno = destinoExternoService.findOne(id);
        model.addAttribute(DESTINO_EXTERNO_FTL, destinoExterno);
        return EDIT_TEMPLATE;
    }

    /**
     * Permite crear un destino externo del sistema
     *
     * @param detinoExterno
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return
     */
    @RequestMapping(value = {"/crear"}, method = RequestMethod.POST)
    public String crear(DestinoExterno detinoExterno, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
            MultipartFile archivo, Principal principal) {
        model.addAttribute(DESTINO_EXTERNO_FTL, detinoExterno);
        final Usuario usuarioSesion = getUsuario(principal);

        try {
            destinoExternoService.crear(detinoExterno, usuarioSesion);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
            return "redirect:" + PATH + "?" + model.asMap().get("queryString");
        } catch (BusinessLogicException | ReflectionException ex) {
            LOG .log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return CREATE_TEMPLATE;
        }
    }

    /**
     * Permite actualizar un destino externo del sistema
     *
     * @param detinoExterno
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return
     */
    @RequestMapping(value = {"/actualizar"}, method = RequestMethod.POST)
    public String actualizar(DestinoExterno detinoExterno, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
            MultipartFile archivo, Principal principal) {
        final Usuario usuarioSesion = getUsuario(principal);
        model.addAttribute(DESTINO_EXTERNO_FTL, detinoExterno);

        try {
            destinoExternoService.editar(detinoExterno, usuarioSesion);
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
     * Permite eliminar un odestino externo del sistema
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
            DestinoExterno detinoExterno = destinoExternoService.findOne(id);
            destinoExternoService.eliminar(detinoExterno, usuarioSesion);
            model.addAttribute(AppConstants.FLASH_SUCCESS, "Observación por defecto eliminada con éxito");
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, req.getParameter("id"), ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
        }

        return "redirect:" + PATH;
    }

    @ModelAttribute("descriptor")
    GenDescriptor getDescriptor() {
        return GenDescriptor.find(DestinoExterno.class);
    }

    @ModelAttribute("activePill")
    public String getActivePill() {
        return "destinos-externos";
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
    public AdminDestinoExternoController controller() {
        return this;
    }
}
