package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Dominio;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.serv.DominioService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String list(Model model) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "nombre"));
        List<Dominio> list = dominioService.mostrarDominiosActivos(sort);
        model.addAttribute("list", list);
        return "dominio-list";
    }

    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public String create(Model model) {
        Dominio dominio = new Dominio();
        dominio.setCodigo("hjdhjsdhjdsj");
        model.addAttribute("dominio", dominio);
        return "dominio-create";
    }

    @RequestMapping(value = {"/edit"}, method = RequestMethod.GET)
    public String edit(Model model, HttpServletRequest req) {
        String codigo = req.getParameter("codigo");
        Dominio dominio = dominioService.findOne(codigo);
        model.addAttribute("dominio", dominio);
        return "dominio-edit";
    }

    @RequestMapping(value = {"/crear"}, method = RequestMethod.POST)
    public String crear(@Valid Dominio e, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
            MultipartFile archivo) {
        System.err.println("dominio= " + e);
        model.addAttribute("dominio", e);
        System.err.println("dominio despues= " + e);
        String retorno = dominioService.crearDominio(e);
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

    @RequestMapping(value = {"/save"}, method = RequestMethod.POST)
    public String save(@Valid Dominio e, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
            MultipartFile archivo) {
        System.err.println("dominio save= " + e);        
        model.addAttribute("dominio", e);

        String retorno = dominioService.editarDominio(e);
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
