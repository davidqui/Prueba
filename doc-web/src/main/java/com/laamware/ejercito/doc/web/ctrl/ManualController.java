package com.laamware.ejercito.doc.web.ctrl;

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
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping(value = ManualController.PATH)
public class ManualController extends UtilController {

    static final String PATH = "/manual";

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

   
    @RequestMapping(value = "/intro", method = RequestMethod.GET)
    public String juegoIntroView(Model model, Principal principal) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        Usuario usuario = getUsuario(principal);

        model.addAttribute("usuario", usuario.getNombre());
        model.addAttribute("activePill", "none");
        model.addAttribute("tematicas", tematicaService.findActive(sort));
        model.addAttribute("recursos", recursoMultimediaService.findActive(sort));

        return "manual-intro";
    }

   
    @RequestMapping(value = "/multimedia/{key}", method = RequestMethod.GET)
    public String recursoMultimediaView(Model model, Principal principal, @PathVariable(value = "key") Integer key) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        Usuario usuario = getUsuario(principal);
        List recursosMultimedia= recursoMultimediaService.findActiveAndTematica(sort, key);
        model.addAttribute("tema", tematicaService.findOne(key));
        model.addAttribute("tematicas", tematicaService.findActive(sort));
        model.addAttribute("recursos", recursosMultimedia);

        return "manual-descripcion";
    }

}
