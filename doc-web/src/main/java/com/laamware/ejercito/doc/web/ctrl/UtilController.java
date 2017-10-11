package com.laamware.ejercito.doc.web.ctrl;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;

public abstract class UtilController {

    @Autowired
    UsuarioRepository usuR;

    private Map<String, Integer> userIds = new HashMap<>();
    private Map<Integer, String> userNombres = new HashMap<>();
    private Map<Integer, String> userLogins = new HashMap<>();

    /**
     * Establece el formato de fecha
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true, 10));
    }

    /**
     * Obtiene el usuario activo en sesión.
     *
     * @param principal Objeto de A&A.
     * @return Usuario activo.
     */
    protected Usuario getUsuario(Principal principal) {
        /*
         * 2017-09-12 jgarcia@controltechcg.com Issue #123 (SICDI-Controltech) 
         * hotfix-123: Corrección en búsqueda de usuarios para que únicamente 
         * presente información de usuarios activos.
         */
        Usuario usuario = usuR.getByLoginAndActivoTrue(principal.getName());
        return usuario;
    }

    /**
     * Obtiene el login del usuario
     *
     * @param id
     * @return
     */
    protected String login(Integer id) {
        String login = userLogins.get(id);
        if (StringUtils.isBlank(login)) {
            Usuario u = usuR.getOne(id);
            indexUsuario(u);
            return userLogins.get(id);
        }
        return login;
    }

    /**
     * Obtiene el nombre del usuario
     *
     * @param id
     * @return
     */
    protected String nombre(Integer id) {
        String x = userNombres.get(id);
        if (StringUtils.isBlank(x)) {
            Usuario u = usuR.getOne(id);
            indexUsuario(u);
            return userNombres.get(id);
        }
        return x;
    }

    /**
     * Obtiene el id del usuario
     *
     * @param principal
     * @return
     */
    protected Integer getUsuarioId(Principal principal) {
        Integer id = userIds.get(principal.getName());
        if (id == null) {
            Usuario u = getUsuario(principal);
            id = u.getId();
            indexUsuario(u);
        }
        return id;
    }

    /**
     * Elimina la información en memoria de un usuario
     *
     * @param principal
     */
    protected synchronized void unindex(Principal principal) {
        String login = principal.getName();
        Integer id = getUsuarioId(principal);
        userIds.remove(login);
        userLogins.remove(id);
        userNombres.remove(id);
    }

    /**
     * Almacena la información del usuario en memoria
     *
     * @param u
     */
    private synchronized void indexUsuario(Usuario u) {

        String login = u.getLogin();
        String nombre = u.getNombre();
        /*
            2017-11-10 edison.gonzalez@controltechcg.com Issue #131 (SICDI-Controltech) 
            feature-131: Cambio en la entidad usuario, se coloca llave foranea el grado.
        */
        String grado = u.getUsuGrado().getNombre();
        if (StringUtils.isNotBlank(grado) && !AppConstants.SIN_GRADO.equals(grado)) {
            nombre = grado + ". " + nombre;
        }

        Integer id = u.getId();

        userIds.put(login, id);
        userNombres.put(id, nombre);
        userLogins.put(id, login);

    }

    public boolean isAuthorized(String roles) {

        String[] split = roles.split(",");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> granted = authentication.getAuthorities();
        boolean is = true;
        for (String rol : split) {
            boolean found = false;
            for (GrantedAuthority grantedAuthority : granted) {
                if (grantedAuthority.getAuthority().equals(rol)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                is &= false;
            }
        }
        return is;
    }

    /**
     *
     * @return True if Principal has any admin role, False instead
     */
    public boolean hasAdminRole() {
        /*
		 * 2017-06-22 jgarcia@controltechcg.com Issue #111 (SICDI-Controltech)
		 * hotfix-111: Corrección en implementación de función utilitaria que
		 * indica si el usuario tiene rol administrador.
         */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> granted = authentication.getAuthorities();

        for (GrantedAuthority grantedAuthority : granted) {
            if (grantedAuthority.getAuthority().startsWith(AppConstants.PREFIX_ROLE_ADMIN)) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @return True if Principal has any archivo role, False instead
     */
    public boolean hasArchivoRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> granted = authentication.getAuthorities();
        boolean isArchivo = false;
        for (GrantedAuthority grantedAuthority : granted) {
            if (grantedAuthority.getAuthority().startsWith(AppConstants.PREFIX_ROLE_ARCHIVO)) {
                isArchivo = true;
                break;
            }
        }

        return isArchivo;
    }

    /**
     *
     * @return The first admin role found for the principal
     */
    public String getFirstAdminRolePath() {
        Boolean pathFoud = Boolean.FALSE;
        String rolePath = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> granted = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : granted) {
            if (grantedAuthority.getAuthority().startsWith(AppConstants.PREFIX_ROLE_ADMIN)) {
                rolePath = getPathForRole(grantedAuthority.getAuthority());
                if (rolePath != null) {
                    pathFoud = Boolean.TRUE;
                    break;
                }

            }
        }
        if (pathFoud) {
            return rolePath;
        } else {
            return "redirect:/access-denied";
        }

    }

    /**
     *
     * @return The first admin role found for the principal
     */
    public String getFirstArchivoRolePath() {
        Boolean pathFoud = Boolean.FALSE;
        String rolePath = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> granted = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : granted) {
            if (grantedAuthority.getAuthority().startsWith(AppConstants.PREFIX_ROLE_ARCHIVO)) {
                rolePath = getPathForRole(grantedAuthority.getAuthority());
                if (rolePath != null) {
                    pathFoud = Boolean.TRUE;
                    break;
                }

            }
        }
        if (pathFoud) {
            return rolePath;
        } else {
            return "redirect:/access-denied";
        }

    }

    private String getPathForRole(String role) {
        switch (role) {
            case AppConstants.ADMIN_PERFILES:
                return String.format("redirect:%s", AppConstants.PATH_ADMIN_PERFILES);
            case AppConstants.ADMIN_DEPENDENCIAS:
                return String.format("redirect:%s", AppConstants.PATH_ADMIN_DEPENDENCIAS);
            case AppConstants.ADMIN_USUARIOS:
                return String.format("redirect:%s", AppConstants.PATH_ADMIN_USUARIOS);
            case AppConstants.ADMIN_TIPOS_DESTINATARIO:
                return String.format("redirect:%s", AppConstants.PATH_ADMIN_TIPOS_DESTINATARIO);
            case AppConstants.ADMIN_DESTINATARIOS:
                return String.format("redirect:%s", AppConstants.PATH_ADMIN_DESTINATARIOS);
            case AppConstants.ADMIN_TRD:
                return String.format("redirect:%s", AppConstants.PATH_ADMIN_TRD);
            case AppConstants.ADMIN_EXPEDIENTES:
                return String.format("redirect:%s", AppConstants.PATH_ADMIN_EXPEDIENTES);
            case AppConstants.ADMIN_AUDITORIA:
                return String.format("redirect:%s", AppConstants.PATH_ADMIN_AUDITORIA);
            case AppConstants.ADMIN_CLASIFICACIONES:
                return String.format("redirect:%s", AppConstants.PATH_ADMIN_CLASIFICACIONES);
            case AppConstants.ADMIN_PLANTILLAS:
                return String.format("redirect:%s", AppConstants.PATH_ADMIN_PLANTILLAS);
            case AppConstants.ADMIN_PROCESOS:
                return String.format("redirect:%s", AppConstants.PATH_ADMIN_PROCESOS);
            case AppConstants.ADMIN_TIPOLOGIA:
                return String.format("redirect:%s", AppConstants.PATH_ADMIN_TIPOLOGIA);
            case AppConstants.ADMIN_TIPO_DOCUMENTO:
                return String.format("redirect:%s", AppConstants.PATH_ADMIN_TIPO_DOCUMENTO);
            case AppConstants.ADMIN_BANDEJAS:
                return String.format("redirect:%s", AppConstants.PATH_ADMIN_BANDEJAS);
            case AppConstants.ADMIN_CAPACITACION:
                return String.format("redirect:%s", AppConstants.PATH_ADMIN_CAPACITACION);
            case AppConstants.ADMIN_GRADOS:
                return String.format("redirect:%s", AppConstants.PATH_ADMIN_GRADOS);
            case AppConstants.ADMIN_INFORMES:
                return String.format("redirect:%s", AppConstants.PATH_ADMIN_INFORMES);
            case AppConstants.ADMIN_FORMATOS:
                return String.format("redirect:%s", AppConstants.PATH_ADMIN_FORMATOS);
            case AppConstants.ARCHIVO_CENTRAL:
                return String.format("redirect:%s", AppConstants.PATH_ARCHIVO_CENTRAL);
            default:
                return null;
        }
    }

    public String getPathArchivo() {

        HashMap<String, String> grantsMap = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> granted = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : granted) {
            if (grantedAuthority.getAuthority().startsWith(AppConstants.PREFIX_ROLE_ARCHIVO)) {
                grantsMap.put(grantedAuthority.getAuthority(), grantedAuthority.getAuthority());
            }
        }
        if (grantsMap.containsKey(AppConstants.ARCHIVO_CENTRAL)) {
            return String.format("redirect:%s", AppConstants.PATH_ARCHIVO_CENTRAL);
        } else {
            return "redirect:/access-denied";
        }
    }

    public String getPathAdmin() {

        HashMap<String, String> grantsMap = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> granted = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : granted) {
            if (grantedAuthority.getAuthority().startsWith(AppConstants.PREFIX_ROLE_ADMIN)) {
                grantsMap.put(grantedAuthority.getAuthority(), grantedAuthority.getAuthority());
            }
        }

        if (grantsMap.containsKey(AppConstants.ADMIN_PERFILES)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_PERFILES);
        }
        if (grantsMap.containsKey(AppConstants.ADMIN_DEPENDENCIAS)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_DEPENDENCIAS);
        }
        if (grantsMap.containsKey(AppConstants.ADMIN_USUARIOS)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_USUARIOS);
        }
        if (grantsMap.containsKey(AppConstants.ADMIN_TIPOS_DESTINATARIO)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_TIPOS_DESTINATARIO);
        }
        if (grantsMap.containsKey(AppConstants.ADMIN_DESTINATARIOS)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_DESTINATARIOS);
        }
        if (grantsMap.containsKey(AppConstants.ADMIN_TRD)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_TRD);
        }
        if (grantsMap.containsKey(AppConstants.ADMIN_EXPEDIENTES)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_EXPEDIENTES);
        }
        if (grantsMap.containsKey(AppConstants.ADMIN_AUDITORIA)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_AUDITORIA);
        }
        if (grantsMap.containsKey(AppConstants.ADMIN_CLASIFICACIONES)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_CLASIFICACIONES);
        }
        if (grantsMap.containsKey(AppConstants.ADMIN_PLANTILLAS)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_PLANTILLAS);
        }
        if (grantsMap.containsKey(AppConstants.ADMIN_PROCESOS)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_PROCESOS);
        }
        if (grantsMap.containsKey(AppConstants.ADMIN_TIPOLOGIA)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_TIPOLOGIA);
        }
        if (grantsMap.containsKey(AppConstants.ADMIN_TIPO_DOCUMENTO)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_TIPO_DOCUMENTO);
        }
        if (grantsMap.containsKey(AppConstants.ADMIN_BANDEJAS)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_BANDEJAS);
        }
        if (grantsMap.containsKey(AppConstants.ADMIN_CAPACITACION)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_CAPACITACION);
        }
        if (grantsMap.containsKey(AppConstants.ADMIN_GRADOS)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_GRADOS);
        }
        if (grantsMap.containsKey(AppConstants.ADMIN_INFORMES)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_INFORMES);
        }
        if (grantsMap.containsKey(AppConstants.ADMIN_FORMATOS)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_FORMATOS);
        }
        if (grantsMap.containsKey(AppConstants.ADMIN_LOG)) {
            return String.format("redirect:%s", AppConstants.PATH_ADMIN_LOG);
        } else {
            return "redirect:/access-denied";
        }
    }

    /**
     * Agrega el controlador al modelo
     *
     * @return
     */
    @ModelAttribute("utilController")
    protected UtilController getUtilController() {
        return this;
    }

    /**
     * Reescribe los mensajes flash para que se renueven para el siguiente
     * request
     *
     * @param redirect
     * @param model
     */
    public void byPassFlassAttributes(RedirectAttributes redirect, Model model) {
        Map<String, Object> map = model.asMap();
        for (String k : map.keySet()) {
            if (k.startsWith("FLASH")) {
                redirect.addFlashAttribute(k, map.get(k));
            }
        }
    }

}
