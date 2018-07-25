package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.dto.NotificacionxUsuarioDTO;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.NotificacionxUsuarioService;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controlador de notificaciones x Usuario.
 *
 * @author edison.gonzalez@controltechcg.com
 * @since 1.8
 * @version 25/07/2018 Issue #182 (SICDI-Controltech) feature-182.
 */
@Controller
@RequestMapping(value = NotificacionxUsuarioController.PATH)
public class NotificacionxUsuarioController extends UtilController {

    static final String PATH = "/notificaciousuario";

    @Autowired
    NotificacionxUsuarioService notificacionxUsuarioService;

    /**
     * Muestra la pantalla de inicio del juego
     *
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping(value = "/retorna-notificaciones", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<NotificacionxUsuarioDTO> retornaNotificaciones(Model model, Principal principal) {
        System.out.println("entra al metodo");
        Usuario usuario = getUsuario(principal);

        List<NotificacionxUsuarioDTO> dTOs = notificacionxUsuarioService.retornaNotificacionesXUsuario(usuario.getId());
        return dTOs;
    }

    @RequestMapping(value = "/cambiaEstado/{tnfId}", method = RequestMethod.POST)
    @ResponseBody
    public Integer cambiarEstado(Model model, Principal principal, @PathVariable("tnfId") Integer tnfId){
        System.err.println("llega a cambiarEstado");
        Usuario usuario = getUsuario(principal);
        return notificacionxUsuarioService.cambiarEstado(usuario.getId(), tnfId);
    }

}
