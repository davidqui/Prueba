package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Notificacion;
import com.laamware.ejercito.doc.web.serv.NotificacionService;
import com.laamware.ejercito.doc.web.serv.TipoNotificacionService;
import com.laamware.ejercito.doc.web.serv.WildCardNotificacionService;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador para {@link Notificacion}.
 *
 * @author samuel.delgado@controltechcg.com 
 * @since 1.8
 * @version 30/05/2018 Issue #169 (SICDI-Controltech) feature-169
 */
@Controller
@PreAuthorize(value = "hasRole('ADMIN_NOTIFICACIONES')")
@RequestMapping(AdminNotificacionController.PATH)
public class AdminNotificacionController {
    
    private static final Logger LOG = Logger.getLogger(AdminNotificacionController.class.getName());

    static final String PATH = "/admin/notificacion";
    
    private static final String LIST_TEMPLATE = "admin-notificaciones-list";

    private static final String CREATE_TEMPLATE = "admin-notificaciones-create";

    private static final String EDIT_TEMPLATE = "admin-notificaciones-edit";
    
    @Autowired
    private NotificacionService notificacionService;
    
    @Autowired
    private TipoNotificacionService tipoNotificacionService;
    
    @Autowired
    private WildCardNotificacionService wildCardNotificacionService;
    
    
    /**
     * Permite listar todos las notificaciones del sistema
     *
     * @param all
     * @param model
     * @return Pagina de consulta de observaciones por defecto
     */
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String list(@RequestParam(value = "all", required = false, defaultValue = "false") Boolean all, Model model) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        List<Notificacion> list;
        if (!all) {
            list = notificacionService.findActive(sort);
        } else {
            list = notificacionService.findAll(sort);
        }
        System.out.println("QUE SUCEDE? "+ list.get(0).toString());
        model.addAttribute("list", list);
        model.addAttribute("all", all);

        return LIST_TEMPLATE;
    }
    
     /**
     * Permite visualizar el formulario de creación de una notificación del sistema
     *
     * @param model
     * @return Pagina que crea una observacion por defecto
     */
    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public String create(Model model) {
        Notificacion notificacion = new Notificacion();
        model.addAttribute("notificacion", notificacion);
        return CREATE_TEMPLATE;
    }

    /**
     * Permite visualizar el formulario de edición de una notificación defecto del sistema
     *
     * @param model
     * @param req
     * @return Pagina que edita una observación por defecto.
     */
    @RequestMapping(value = {"/edit"}, method = RequestMethod.GET)
    public String edit(Model model, HttpServletRequest req) {
        Integer id = Integer.parseInt(req.getParameter("id"));
        Notificacion notificacion = notificacionService.findOne(id);
        model.addAttribute("notificacion", notificacion);
        return EDIT_TEMPLATE;
    }
    
    @ModelAttribute("descriptor")
    GenDescriptor getDescriptor() {
        return GenDescriptor.find(Notificacion.class);
    }

    @ModelAttribute("activePill")
    public String getActivePill() {
        return "notificaciones";
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
    public AdminNotificacionController controller() {
        return this;
    }
}
