package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.dto.UsuarioBusquedaDTO;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para servicios de usuarios.
 *
 * @author jgarcia@controltechcg.com
 * @since Ago 31, 2017
 * @version 1.0.0 (feature-120).
 */
@RestController
@RequestMapping("/rest/usuario")
public class UsuarioRESTController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Servicio REST que busca un usuario activo por el número de documento.
     *
     * @param documento Número de documento.
     * @return DTO de Usuario activo o {@code null} si no hay concordancia en el
     * sistema.
     */
    @RequestMapping(value = "/buscar/activo/{documento}", method = RequestMethod.POST)
    @ResponseBody
    public UsuarioBusquedaDTO buscarUsuarioActivoJSON(@PathVariable String documento) {
        final Usuario usuario
                = usuarioRepository.findByActivoTrueAndDocumento(documento);

        if (usuario == null) {
            final UsuarioBusquedaDTO busquedaDTO = new UsuarioBusquedaDTO();
            busquedaDTO.setOk(Boolean.FALSE);
            busquedaDTO.setMensajeBusqueda("Usuario no encontrado en el sistema.");
            return busquedaDTO;
        }

        return convertirABusquedaDTO(usuario);
    }

    /**
     * Convierte un usuario a un DTO de búsqueda.
     *
     * @param usuario Usuario.
     * @return DTO.
     */
    private UsuarioBusquedaDTO convertirABusquedaDTO(final Usuario usuario) {
        final UsuarioBusquedaDTO busquedaDTO = new UsuarioBusquedaDTO();
        busquedaDTO.setOk(Boolean.TRUE);
        busquedaDTO.setId(usuario.getId());
        busquedaDTO.setNombre(usuario.getNombre());
        busquedaDTO.setGrado(usuario.getGrado());

        if (usuario.getClasificacion() != null) {
            busquedaDTO.setClasificacionId(usuario.getClasificacion().getId());
            busquedaDTO.setClasificacionNombre(usuario.getClasificacion()
                    .getNombre());
        }

        return busquedaDTO;
    }
}
