package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.Capacitacion;
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
import static java.util.Arrays.sort;
import java.util.List;
import java.util.Random;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = CapacitacionController.PATH)
public class CapacitacionController extends UtilController {

    static final String PATH = "/capacitacion";
    private static final String LIST_TEMPLATE = "capacitacion-busqueda";

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

    @RequestMapping(value = "/pregunta", method = RequestMethod.GET)
    public String pregunta(Pregunta pregunta, Model model, Principal principal) {

        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));

        List<Pregunta> preguntas = preguntaService.findActiveAndTemaCapacitacion(sort, pregunta.getId());
        // Selecciona un indice al azar de la lista de preguntas
        int idx = new Random().nextInt(preguntas.size());
        Pregunta laPregunta = preguntas.get(idx);
        preguntaService.buscarPreguntaActiva(laPregunta.getPregunta());

        model.addAttribute("laPregunta", laPregunta);

        return "capacitacion";

    }

    @RequestMapping(value = "/juego", method = RequestMethod.GET)
    public String validateGameState(TemaCapacitacion temaCapacitacion,
            Model model, Principal principal) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        Usuario usuario = getUsuario(principal);

        List<TemaCapacitacion> temaCapacitacions = temaCapacitacionService.findAllList(sort);

        

        model.addAttribute("usuario", usuario.getNombre());
        model.addAttribute("activePill", temaCapacitacions);
        model.addAttribute("temas", temaCapacitacionService.findActive(sort));
        model.addAttribute("preguntas", preguntaService.findActive(sort));
        model.addAttribute("respuestas", respuestaService.findActive(sort));

        return "capacitacion";
    }
}
