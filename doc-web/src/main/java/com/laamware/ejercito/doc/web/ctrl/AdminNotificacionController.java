package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Clasificacion;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Notificacion;
import com.laamware.ejercito.doc.web.entity.TipoNotificacion;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.entity.WildCardNotificacion;
import com.laamware.ejercito.doc.web.repo.ClasificacionRepository;
import com.laamware.ejercito.doc.web.serv.NotificacionService;
import com.laamware.ejercito.doc.web.serv.TipoNotificacionService;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import java.security.Principal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Controlador para {@link Notificacion}.
 *
 * @author samuel.delgado@controltechcg.com
 * @since 1.8
 * @version 30/05/2018 Issue #169 (SICDI-Controltech) feature-169
 */
@Controller
@PreAuthorize(value = "hasRole('ADMIN_NOTIFICACIONES')")
@RequestMapping(AdminNotificacionController.PATH)
public class AdminNotificacionController extends UtilController {

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
    private ClasificacionRepository clasificacionRepository;

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

        model.addAttribute("list", list);
        model.addAttribute("all", all);

        return LIST_TEMPLATE;
    }

    /**
     * Permite visualizar el formulario de creación de una notificación del
     * sistema
     *
     * @param idTipoNotificacion
     * @param model
     * @return Pagina que crea una observacion por defecto
     */
    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public String create(@RequestParam(value = "tipoNotificacion", required = false, defaultValue = "false") String idTipoNotificacion, Model model, HttpServletRequest req) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        List<TipoNotificacion> tiposNotificaciones = tipoNotificacionService.findActive(sort);
        List<Clasificacion> clasificaciones = clasificacionRepository.findByActivo(true);
        model.addAttribute("tiposNotificaciones", tiposNotificaciones);
        model.addAttribute("clasificaciones", clasificaciones);
        Notificacion notificacion = new Notificacion();

        if (!idTipoNotificacion.equals("false")) {
            String id_clasificacion = req.getParameter("clasificacion");
            notificacion.setTipoNotificacion(getTipoNotificacionById(tiposNotificaciones, idTipoNotificacion));
            if (!id_clasificacion.equals("")) {
                notificacion.setClasificacion(getClasificacionById(clasificaciones, id_clasificacion));
            }
        }
        model.addAttribute("notificacion", notificacion);
        return CREATE_TEMPLATE;
    }

    /**
     * Permite visualizar el formulario de edición de una notificación defecto
     * del sistema
     *
     * @param idTipoNotificacion
     * @param model
     * @param req
     * @return Pagina que edita una observación por defecto.
     */
    @RequestMapping(value = {"/edit"}, method = RequestMethod.GET)
    public String edit(@RequestParam(value = "tipoNotificacion", required = false, defaultValue = "false") String idTipoNotificacion,
            Model model, HttpServletRequest req) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        Integer id = Integer.parseInt(req.getParameter("id"));
        Notificacion notificacion = notificacionService.findOne(id);
        List<TipoNotificacion> tiposNotificaciones = tipoNotificacionService.findActive(sort);
        List<Clasificacion> clasificaciones = clasificacionRepository.findByActivo(true);
        model.addAttribute("notificacion", notificacion);
        model.addAttribute("tiposNotificaciones", tiposNotificaciones);
        model.addAttribute("clasificaciones", clasificaciones);

        if (!idTipoNotificacion.equals("false")) {
            String id_clasificacion = req.getParameter("clasificacion");
            notificacion.setTipoNotificacion(getTipoNotificacionById(tiposNotificaciones, idTipoNotificacion));
            if (!id_clasificacion.equals("")) {
                notificacion.setClasificacion(getClasificacionById(clasificaciones, id_clasificacion));
            }
        }
        return EDIT_TEMPLATE;
    }

    @RequestMapping(value = {"/crear"}, method = RequestMethod.POST)
    public String crear(Notificacion notificacion, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
            MultipartFile archivo, Principal principal) {
        model.addAttribute("notificacion", notificacion);
        Usuario logueado = getUsuario(principal);
        try {
            notificacionService.crearNotificacion(notificacion, logueado);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
            return "redirect:" + PATH + "?" + model.asMap().get("queryString");
        } catch (BusinessLogicException ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return CREATE_TEMPLATE;
        }
    }

    /**
     * Permite actualizar una notificación del sistema
     *
     * @param notificacion
     * @param req
     * @param eResult
     * @param model
     * @param redirect
     * @param archivo
     * @param principal
     * @return
     */
    @RequestMapping(value = {"/actualizar"}, method = RequestMethod.POST)
    public String actualizar(Notificacion notificacion, HttpServletRequest req, BindingResult eResult, Model model, RedirectAttributes redirect,
            MultipartFile archivo, Principal principal) {
        final Usuario usuarioSesion = getUsuario(principal);
        model.addAttribute("notificacion", notificacion);
        try {
            notificacionService.editarNotifiacion(notificacion, usuarioSesion);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
            return "redirect:" + PATH + "?" + model.asMap().get("queryString");
        } catch (BusinessLogicException ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return EDIT_TEMPLATE;
        }
    }

    /**
     * *
     * Permite eliminar una notificación del sistema
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
            Notificacion notificacion = notificacionService.findOne(id);
            notificacionService.eliminarNotifiacion(notificacion, usuarioSesion);
            model.addAttribute(AppConstants.FLASH_SUCCESS, "Notificación eliminada con éxito");
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, req.getParameter("id"), ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
        }
        return "redirect:" + PATH;
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

    @ModelAttribute("tiposNotificaciones")
    public List<TipoNotificacion> findActiveTipoNotificacion() {
        return tipoNotificacionService.findActive(new Sort(new Sort.Order(Sort.Direction.DESC, "cuando")));
    }

    @ModelAttribute("clasificaciones")
    public List<Clasificacion> findActiveClasificacion() {
        return clasificacionRepository.findByActivo(true);
    }

    /**
     * *
     * Retorna un tipo de notificación en una lista dado un id
     *
     * @param tiposNotificaciones lista de tipos de notificación
     * @param idTipoNotificacion id del tipo de notificación
     * @return tipo de notificación en caso de encontrar o null en caso
     * contrario
     */
    public static TipoNotificacion getTipoNotificacionById(List<TipoNotificacion> tiposNotificaciones, String idTipoNotificacion) {
        for (int i = 0; i < tiposNotificaciones.size(); i++) {
            if (tiposNotificaciones.get(i).getId() == Integer.parseInt(idTipoNotificacion)) {
                return tiposNotificaciones.get(i);
            }
        }
        return null;
    }

    /**
     * *
     * Retorna una clasificación en una lista dado un id
     *
     * @param clasificaciones lista de clasificaciones
     * @param id_clasificacion id del la clasificación
     * @return clasificación en caso de encontrar o null en caso contrario
     */
    public static Clasificacion getClasificacionById(List<Clasificacion> clasificaciones, String id_clasificacion) {
        for (int i = 0; i < clasificaciones.size(); i++) {
            if (clasificaciones.get(i).getId() == Integer.parseInt(id_clasificacion)) {
                return clasificaciones.get(i);
            }
        }
        return null;
    }
}
