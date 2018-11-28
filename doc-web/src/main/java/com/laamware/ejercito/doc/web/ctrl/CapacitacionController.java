package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Pregunta;
import com.laamware.ejercito.doc.web.entity.TemaCapacitacion;
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
import com.laamware.ejercito.doc.web.serv.CapacitacionService;
import com.laamware.ejercito.doc.web.serv.PreguntaService;
import com.laamware.ejercito.doc.web.serv.RespuestaService;
import com.laamware.ejercito.doc.web.serv.TemaCapacitacionService;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.ReflectionException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = CapacitacionController.PATH)
public class CapacitacionController extends UtilController {

    private static final Logger LOG = Logger.getLogger(CapacitacionController.class.getName());

    static final String PATH = "/capacitacion";

    private static final String LIST_TEMPLATE = "capacitacion-juego";

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    TemaCapacitacionService temaCapacitacionService;

    @Autowired
    PreguntaService preguntaService;

    @Autowired
    RespuestaService respuestaService;

    @Autowired
    CapacitacionService capacitacionService;

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
        return GenDescriptor.find(TemaCapacitacion.class);
    }

    /**
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping(value = "/intro", method = RequestMethod.GET)
    public String capacitacionIntroView(Model model, Principal principal) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        Usuario usuario = getUsuario(principal);

        List<TemaCapacitacion> temas = temaCapacitacionService.findAllList(sort);

        model.addAttribute("usuario", usuario.getNombre());
        model.addAttribute("activePill", "none");
        model.addAttribute("temas", temas);
//        model.addAttribute("preguntas", preguntaService.findActive(sort));
//        model.addAttribute("respuestas", respuestaService.findActive(sort));

        return "juego-intro";
    }

    @RequestMapping(value = {"/list/{key}"}, method = RequestMethod.GET)
    public String listByPregunta(@PathVariable(value = "key") Integer key,
            @RequestParam(value = "all", required = false, defaultValue = "false") Boolean all,
            @RequestParam(value = "pageIndex", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize, Model model, RedirectAttributes redirect) {

        TemaCapacitacion seleccionPregunta = temaCapacitacionService.findOne(key);
        model.addAttribute("preguntaView", seleccionPregunta);

        if (page == null || page < 0) {
            page = 1;
        }

        if (pageSize == null || pageSize < 0) {
            pageSize = ADMIN_PAGE_SIZE;
        }

        Long count;

        Pageable pageable = new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "cuando");

        Page<Pregunta> list;
        if (!all) {
            list = preguntaService.findActiveAndTemaCapacitacionPage(pageable, key);
        } else {
            list = preguntaService.findAllByTemaCapacitacionPage(pageable, seleccionPregunta);
        }

        count = list.getTotalElements();
        adminPageable(count, model, page, pageSize);
        model.addAttribute("list", list.getContent());
        model.addAttribute("all", all);

        return LIST_TEMPLATE;
    }

    @RequestMapping(value = "/pregunta", method = RequestMethod.GET)
    public String pregunta(Pregunta pregunta, Model model, Principal principal, RedirectAttributes redirect) {

        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        System.out.println("Entra al metodo pregunta<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + pregunta);

        model.addAttribute("pregunta", pregunta);
        final Usuario usuarioSesion = getUsuario(principal);
        TemaCapacitacion temaCapacitacion = temaCapacitacionService.findOne(pregunta.getId());
        try {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<entrando al controlador Pregunta>>>>>>>>>>>>>>>>>>>>>" + temaCapacitacion + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + pregunta);
//            preguntaService.crearPregunta(pregunta.getPregunta(), temaCapacitacion, usuarioSesion);
            capacitacionService.generarPregunta(pregunta, temaCapacitacion, usuarioSesion);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");

            return "capacitacion-juego";
//            return "redirect:" + PATH + "/list/" + pregunta.getId();
        } catch (BusinessLogicException | ReflectionException ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            Pregunta pregunta1 = new Pregunta();
            model.addAttribute("capacitacion", pregunta1);
            model.addAttribute("capacitacion", temaCapacitacion);
        }

        return LIST_TEMPLATE;

    }

    @RequestMapping(value = "/juego", method = RequestMethod.GET)
    public String GameState(TemaCapacitacion temaCapacitacion, Pregunta pregunta,
            Model model, Principal principal) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        Usuario usuario = getUsuario(principal);
        final Usuario usuarioSesion = getUsuario(principal);
        
//        Pageable pageable;
//        pageable = new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "cuando");
//
//        TemaCapacitacion temaCapacitacions = temaCapacitacionService.findOne(temaCapacitacion.getId());
//
//        List<Pregunta>  preguntas = preguntaService.findActiveAndTemaCapacitacion(sort, pregunta.getId());

        /*System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<entrando al controlador Metodo Jego >>>>>>>>>>>>>>>>>>>>>"
               + temaCapacitacions + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");*/



        try {
            capacitacionService.generarPregunta(pregunta, temaCapacitacion, usuarioSesion);

            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<temaCapacitacions<<<<<<<<<<<<<<<<<<<<<<<<< = " + temaCapacitacion
                    + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<usuarioSesion<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<= "+usuarioSesion+
                    "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< preguntas>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> = "+ pregunta);
            System.out.println("generarPregunta");
            return LIST_TEMPLATE;

        } catch (BusinessLogicException | ReflectionException ex) {

            model.addAttribute("usuario", usuario.getNombre());
            model.addAttribute("activePill", temaCapacitacion);
            model.addAttribute("temas", temaCapacitacionService.findActive(sort));
            model.addAttribute("preguntas", preguntaService.findActive(sort));
            model.addAttribute("respuestas", respuestaService.findActive(sort));
        }

        return LIST_TEMPLATE;
    }
}
