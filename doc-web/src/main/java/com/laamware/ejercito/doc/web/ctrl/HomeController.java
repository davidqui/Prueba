package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.UsuarioService;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController extends UtilController {
    
        @Autowired
        private UsuarioService usuarioService;

	@RequestMapping(value = { "", "/" })
	public String index(RedirectAttributes redirect, Model model) {
		byPassFlassAttributes(redirect, model);
		return String.format("redirect:%s/entrada", BandejaController.PATH);
	}
        
        /*
        * 2018-08-15 samuel.delgado@controltechcg.com Issue #7 (SICDI-Controltech)
        * feature-gogs-7: metodos para activar (habilitarUsuario) o desactivar usuario (inhabilitarUsuario)
        */
        /***
         * Método para habilitar un usuario.
         * @param principal Sesión de usuario
         * @param req request
         * @return Codigo de respuesta exitoso, en caso contrario Bad Request
         */
        @RequestMapping(value = "/habilitar-usuario", method = { RequestMethod.POST })
        public ResponseEntity<?> habilitarUsuario(Principal principal, HttpServletRequest req){
            final Usuario usuarioSesion = getUsuario(principal);
            usuarioService.habilitarUsuario(usuarioSesion);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        
        /***
         * Método para inhabilitar un usuario.
         * @param message mensaje por el cual se deshabilita
         * @param principal Sesión de usuario
         * @param req request
         * @return Codigo de respuesta exitoso, en caso contrario Bad Request
         */
        @RequestMapping(value = "/inhabilitar-usuario", method = { RequestMethod.POST })
        public ResponseEntity<?> inhabilitarUsuario(@RequestParam(value = "descr", required = true) String message, Principal principal, HttpServletRequest req){
            final Usuario usuarioSesion = getUsuario(principal);
            usuarioService.inhabilitarUsuario(usuarioSesion, message);
            return new ResponseEntity<>(HttpStatus.OK);
        }
}
