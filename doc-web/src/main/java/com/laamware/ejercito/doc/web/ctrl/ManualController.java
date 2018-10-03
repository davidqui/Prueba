package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Notificacion;
import com.laamware.ejercito.doc.web.entity.RecursoMultimedia;
import com.laamware.ejercito.doc.web.entity.Tematica;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.RecursoMultimediaService;
import com.laamware.ejercito.doc.web.serv.TematicaService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = ManualController.PATH)
public class ManualController extends UtilController {

    static final String PATH = "/manual";
    private static final String LIST_TEMPLATE = "recursoMultimedia-busqueda";

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    TematicaService tematicaService;

    @Autowired
    RecursoMultimediaService recursoMultimediaService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                new SimpleDateFormat("yyyy-MM-dd"), true, 10));
    }

    @ModelAttribute("activePill")
    public String getActivePill() {
        return "none";
    }
    
    @ModelAttribute("descriptor")
    GenDescriptor getDescriptor() {
        return GenDescriptor.find(RecursoMultimedia.class);
    }
    
    /**
     * Modela los datos necesarios para la vista de introducción del modulo de manual del usuario final.
     * 
     * 2018-09-17 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 
     * @param model Parametro requerido por clase de spring.
     * @param principal Id del usuario activo en sesión.
     * @return 
     */
    @RequestMapping(value = "/intro", method = RequestMethod.GET)
    public String manualIntroView(Model model, Principal principal) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        Usuario usuario = getUsuario(principal);

        model.addAttribute("usuario", usuario.getNombre());
        model.addAttribute("activePill", "none");
        model.addAttribute("tematicas", tematicaService.findActive(sort));
        model.addAttribute("recursos", recursoMultimediaService.findActive(sort));

        return "manual-intro";
    }
    
    /**
     * Modela los datos necesarios para la vista de introducción del modulo de manual del usuario final.
     * 
     * 2018-09-17 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 
     * @param model Parametro requerido por clase de spring.
     * @param principal Id del usuario activo en sesión.
     * @param key Id del Recurso Multimedia.
     * @return Retorna el Recurso Multimedia segun Id suministrado.
     */
    @RequestMapping(value = "/multimedia/{key}", method = RequestMethod.GET)
    public String recursoMultimediaView(Model model, Principal principal, @PathVariable(value = "key") Integer key) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        Usuario usuario = getUsuario(principal);
        List recursosMultimedia = recursoMultimediaService.findActiveAndTematica(sort, key);
        model.addAttribute("tema", tematicaService.findOne(key));
        model.addAttribute("tematicas", tematicaService.findActive(sort));
        model.addAttribute("recursos", recursosMultimedia);

        return "manual-descripcion";
    }
    
    /**
     * Permite buscar los Recursos Multimedia coincidentes con el valor suministrado.
     * 
     * 2018-10-02 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 
     * @param findTokens Criterio de busqueda
     * @param all
     * @param page 
     * @param pageSize
     * @param model Parametro requerido por clase de spring. 
     * @return Retorna la lista de Recursos Multimedia segun criterio de busqueda. 
     */
    @RequestMapping(value = "/busqueda", method = RequestMethod.GET)
    public String listByTematica(@RequestParam(value = "findTokens") String findTokens, 
                                 @RequestParam(value = "all", required = false, defaultValue = "false") Boolean all, 
                                 @RequestParam(value = "pageIndex", required = false) Integer page,
                                 @RequestParam(value = "pageSize", required = false) Integer pageSize, Model model) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        model.addAttribute("tematicas", tematicaService.findActive(sort));
        model.addAttribute("findData", findTokens);

        if (page == null || page < 0)
            page = 1;
        
        if (pageSize == null || pageSize < 0)
            pageSize = ADMIN_PAGE_SIZE;
        
        Long count;
        
        Pageable pageable = new PageRequest(page-1, pageSize, Sort.Direction.ASC, "cuando");

        Page <RecursoMultimedia> list;
        if (!all) {
            list = recursoMultimediaService.findAllActiveName(pageable, findTokens);
        } else {
            list = recursoMultimediaService.findAllName(pageable, findTokens);
        }
        
        count = list.getTotalElements();
        adminPageable(count, model, page, pageSize);
        model.addAttribute("list", list.getContent());
        model.addAttribute("all", all);
        
        return "recursoMultimedia-busqueda";
    }

}
