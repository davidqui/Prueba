package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Dominio;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.DominioService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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
 * Controlador para {@link Dominio}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 05/02/2018 Issue #159 (SICDI-Controltech) feature-159
 */
@Controller
@PreAuthorize(value = "hasRole('ADMIN_DOMINIO')")
@RequestMapping(DominioController.PATH)
public class DominioController extends UtilController {

    @Autowired
    private DominioService dominioService;

    static final String PATH = "/admin/dominio";

    /**
     * Permite listar todos los dominios del sistema
     * @param all
     * @param model
     * @return Pagina de consulta de dominio
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
        
        Pageable pageable = new PageRequest(page-1, pageSize, Sort.Direction.DESC, "nombre");

        
        Page<Dominio> list;
        if (!all) {
            list = dominioService.mostrarDominiosActivos(pageable);
        }else{
            list = dominioService.findAll(pageable);
        }
        
        Long count = list.getTotalElements();
        adminPageable(count, model, page, pageSize);
        model.addAttribute("list", list.getContent());
        model.addAttribute("all", all);
        
        return "dominio-list";
    }

    /**
     * Permite visualizar el formulario de creación de un dominio del sistema
     * @param model
     * @return Pagina que crea el dominio
     */
    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public String create(Model model) {
        Dominio dominio = new Dominio();
        model.addAttribute("dominio", dominio);
        return "dominio-create";
    }

    /**
     * Permite visualizar el formulario de edición de un dominio del sistema
     * @param model
     * @param req
     * @return Pagina que edita el dominio
     */
    @RequestMapping(value = {"/edit"}, method = RequestMethod.GET)
    public String edit(Model model, HttpServletRequest req) {
        String codigo = req.getParameter("codigo");
        Dominio dominio = dominioService.findOne(codigo);
        model.addAttribute("dominio", dominio);
        return "dominio-edit";
    }

    /**
     * Permite crear un dominio del sistema
     * @param e
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return 
     */
    @RequestMapping(value = {"/crear"}, method = RequestMethod.POST)
    public String crear(Dominio e, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
            MultipartFile archivo, Principal principal) {
        model.addAttribute("dominio", e);

        Usuario logueado = getUsuario(principal);
        String retorno = dominioService.crearDominio(e, logueado);
        if ("OK".equals(retorno)) {
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
            return "redirect:" + PATH + "?" + model.asMap().get("queryString");
        }

        if (retorno != null && retorno.trim().length() > 0 && retorno.contains("-")) {
            String tipo = retorno.substring(0, retorno.indexOf("-"));
            String mensaje = retorno.substring(retorno.indexOf("-") + 1, retorno.length());

            if ("Excepcion".equals(tipo)) {
                redirect.addFlashAttribute(AppConstants.FLASH_ERROR, mensaje);
            }

            if ("Error".equals(tipo)) {
                model.addAttribute(AppConstants.FLASH_ERROR, mensaje);
            }
        }
        return "dominio-create";
    }

    /**
     * Permite actualizar el dominio del sistema
     * @param e
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return 
     */
    @RequestMapping(value = {"/actualizar"}, method = RequestMethod.POST)
    public String actualizar(Dominio e, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
            MultipartFile archivo, Principal principal) {
        model.addAttribute("dominio", e);
        Usuario logueado = getUsuario(principal);
        String retorno = dominioService.editarDominio(e, logueado);
        if ("OK".equals(retorno)) {
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
            return "redirect:" + PATH + "?" + model.asMap().get("queryString");
        }

        if (retorno != null && retorno.trim().length() > 0 && retorno.contains("-")) {
            String tipo = retorno.substring(0, retorno.indexOf("-"));
            String mensaje = retorno.substring(retorno.indexOf("-") + 1, retorno.length());

            if ("Excepcion".equals(tipo)) {
                redirect.addFlashAttribute(AppConstants.FLASH_ERROR, mensaje);
            }

            if ("Error".equals(tipo)) {
                model.addAttribute(AppConstants.FLASH_ERROR, mensaje);
            }
        }
        return "dominio-edit";
    }
    
    /**
     * Permite eliminar el dominio del sistema
     * @param model
     * @param req
     * @param redirect
     * @param principal
     * @return 
     */
    @RequestMapping(value = {"/delete"}, method = RequestMethod.GET)
    public String delete(Model model, HttpServletRequest req, RedirectAttributes redirect, Principal principal) {
        String codigo = req.getParameter("codigo");
        try {
            Usuario logueado = getUsuario(principal);
            Dominio dominio = dominioService.findOne(codigo);
            dominioService.eliminarDominio(dominio, logueado);
            model.addAttribute(AppConstants.FLASH_SUCCESS, "Dependencia eliminada con éxito");
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
        }
        return "redirect:" + PATH;
    }

    @ModelAttribute("descriptor")
    GenDescriptor getDescriptor() {
        GenDescriptor d = GenDescriptor.find(Dominio.class);
        return d;
    }

    @ModelAttribute("activePill")
    public String getActivePill() {
        return "dominio";
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
    public DominioController controller() {
        return this;
    }
}
