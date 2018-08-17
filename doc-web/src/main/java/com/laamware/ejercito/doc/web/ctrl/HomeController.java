package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.RazonInhabilitar;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.DependenciaService;
import com.laamware.ejercito.doc.web.serv.RazonInhabilitarService;
import com.laamware.ejercito.doc.web.serv.UsuarioService;
import java.security.Principal;
import java.util.List;
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
        
        @Autowired
        private DependenciaService dependenciaService;
        
        /*
        * 2018-08-16 samuel.delgado@controltechcg.com Issue #7 (SICDI-Controltech)
        * feature-gogs-7: servicio para razones de inhabilitación
        */
        @Autowired 
        private RazonInhabilitarService razonInhabilitarService;

	@RequestMapping(value = { "", "/" })
	public String index(RedirectAttributes redirect, Model model) {
		byPassFlassAttributes(redirect, model);
		return String.format("redirect:%s/entrada", BandejaController.PATH);
	}
        
        /*
        * 2018-08-15 samuel.delgado@controltechcg.com Issue #7 (SICDI-Controltech)
        * feature-gogs-7: metodos para activar (habilitarUsuario) o desactivar 
        * usuario (inhabilitarUsuario) y el método para listar las razones de inhabilitación (razonesInhabilitacion)
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
        public ResponseEntity<?> inhabilitarUsuario(@RequestParam(value = "razon", required = true) Integer razon, Principal principal, HttpServletRequest req){
            final Usuario usuarioSesion = getUsuario(principal);
            
            if (usuarioSesion.getDependencia().getJefe() != null &&
                    usuarioSesion.getDependencia().getJefe().getId().equals(usuarioSesion.getId()))
                return new ResponseEntity<>("Usted es jefe de dependencia mientras lo sea no puede desactivarse.", HttpStatus.BAD_REQUEST);
            if(!dependenciaService.dependenciasUsuarioRegistro(usuarioSesion).isEmpty())
                return new ResponseEntity<>("Usted es usuario registro de dependencia mientras lo sea no puede desactivarse.", HttpStatus.BAD_REQUEST);
                                
            
            usuarioService.inhabilitarUsuario(usuarioSesion, razon);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        
        /***
         * Método para inhabilitar un usuario.
         * @param message mensaje por el cual se deshabilita
         * @param principal Sesión de usuario
         * @param req request
         * @return Codigo de respuesta exitoso, en caso contrario Bad Request
         */
        @RequestMapping(value = "/razon-inhabilitar", method = { RequestMethod.GET })
        public ResponseEntity<?> razonesInhabilitacion(Principal principal, HttpServletRequest req){
            final Usuario usuarioSesion = getUsuario(principal);
            if(usuarioSesion == null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            
            List<RazonInhabilitar> razonesInhabilitar = razonInhabilitarService.listarActivas();
            String asw = "[";
            for (RazonInhabilitar razonInhabilitar : razonesInhabilitar) {
                asw += "{\"id\": "+razonInhabilitar.getId()+",\"texto\":\""+razonInhabilitar.getTextoRazon()+"\"},";
            }
            if (razonesInhabilitar.size() > 0)
                asw = asw.substring(0, asw.length()-1);
            asw += "]";
            return ResponseEntity.ok(asw);
        }
}
