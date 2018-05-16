package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.dto.LinkOWARegistroUsoDTO;
import com.laamware.ejercito.doc.web.entity.LinkOWARegistroUso;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.LinkOWARegistroUsoService;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controlador para {@link LinkOWARegistroUso}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 05/02/2018 Issue #159 (SICDI-Controltech) feature-159
 */
@Controller
@RequestMapping("/link-owa-registro-uso")
public class LinkOWARegistroUsoController extends UtilController {

    private static final Logger LOG = Logger.getLogger(LinkOWARegistroUsoController.class.getName());

    @Autowired
    private LinkOWARegistroUsoService service;

    /**
     * Registra un acceso al enlace hacia OWA a través del sistema.
     *
     * @param dto DTO con la información del acceso.
     * @param principal Información de la sesión de usuario.
     * @return Respuesta con el ID del registro realizado.
     */
    @ResponseBody
    @RequestMapping(value = "/registrar", method = RequestMethod.POST)
    public ResponseEntity<?> registrar(@RequestBody(required = true) LinkOWARegistroUsoDTO dto, Principal principal) {
        LOG.log(Level.INFO, "url = {0}", dto);

        final Usuario usuarioSesion = getUsuario(principal);
        final String url = dto.getUrl();
        final LinkOWARegistroUso registro = service.registrar(usuarioSesion, url);
        return ResponseEntity.ok(registro.getId());
    }
}
