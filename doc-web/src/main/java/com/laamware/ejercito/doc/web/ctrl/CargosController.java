package com.laamware.ejercito.doc.web.ctrl;

import static com.laamware.ejercito.doc.web.ctrl.UtilController.ADMIN_PAGE_SIZE;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.repo.CargosRepository;
import com.laamware.ejercito.doc.web.serv.CargoService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
 *
 * @author egonzalezm
 */
@Controller
@PreAuthorize(value = "hasRole('ADMIN_CARGOS')")
@RequestMapping(CargosController.PATH)
public class CargosController extends UtilController {

    @Autowired
    CargosRepository cargosRepository;

    @Autowired
    CargoService cargoService;

    static final String PATH = "/admin/cargos";

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String list(@RequestParam(value = "pageIndex", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize, Model model) {
        
        if (page == null || page < 0)
            page = 1;
        if (pageSize == null || pageSize < 0)
            pageSize = ADMIN_PAGE_SIZE;
        
        Pageable pageable = new PageRequest(page-1, pageSize, Sort.Direction.DESC, "carNombre");
        
        Page<Cargo> list = cargosRepository.findAll(pageable);
        Long count = list.getTotalElements();
        adminPageable(count, model, page, pageSize);
        model.addAttribute("list", list.getContent());      
        
        return "cargos-list";
    }

    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public String create(Model model) {
        Cargo cargo = new Cargo();
        cargo.setCarIndLdap(false);
        model.addAttribute("cargo", cargo);
        return "cargos-create";
    }

    @RequestMapping(value = {"/edit"}, method = RequestMethod.GET)
    public String edit(Model model, HttpServletRequest req) {
        Integer id = 0;
        if (req.getParameter("id") != null) {
            try {
                id = Integer.parseInt(req.getParameter("id"));
            } catch (NumberFormatException e) {
            }
        }
        Cargo cargo = cargosRepository.findOne(id);
        model.addAttribute("cargo", cargo);
        return "cargos-create";
    }

    @RequestMapping(value = {"/save"}, method = RequestMethod.POST)
    public String save(@Valid Cargo e, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
            MultipartFile archivo) {
        model.addAttribute("cargo", e);

        String idS = req.getParameter("id");
        if (idS != null && idS.trim().length() > 0) {
            e.setId(Integer.parseInt(idS.trim()));
        }

        String retorno = cargoService.guardarCargo(e);
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
        return "cargos-create";
    }

    @ModelAttribute("descriptor")
    GenDescriptor getDescriptor() {
        GenDescriptor d = GenDescriptor.find(Cargo.class);
        return d;
    }

    @ModelAttribute("activePill")
    public String getActivePill() {
        return "cargos";
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
    public CargosController controller() {
        return this;
    }
}
