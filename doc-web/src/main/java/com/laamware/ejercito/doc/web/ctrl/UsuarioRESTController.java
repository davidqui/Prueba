package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.dto.CargoDTO;
import com.laamware.ejercito.doc.web.dto.UsuarioBusquedaDTO;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.CargosRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
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
     * Servicio de cargos.
     */
    @Autowired
    CargosRepository cargosRepository;

    /**
     * Servicio REST que busca un usuario activo por el número de documento.
     *
     * @param documento Número de documento.
     * @return DTO de Usuario activo o {@code null} si no hay concordancia en el
     * sistema.
     */
    @RequestMapping(value = "/buscar/activo/documento/{documento}", method = RequestMethod.POST)
    @ResponseBody
    public UsuarioBusquedaDTO buscarUsuarioActivoByDocumentoJSON(@PathVariable String documento) {
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
     * Servicio REST que busca un usuario activo por el número de documento.
     *
     * @param id ID del usuario.
     * @return DTO de Usuario activo o {@code null} si no hay concordancia en el
     * sistema.
     */
    @RequestMapping(value = "/buscar/activo/id/{id}", method = RequestMethod.POST)
    @ResponseBody
    public UsuarioBusquedaDTO buscarUsuarioActivoJSON(@PathVariable Integer id) {
        final Usuario usuario
                = usuarioRepository.findOne(id);

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
        /*
            2017-11-10 edison.gonzalez@controltechcg.com Issue #131 (SICDI-Controltech) 
            feature-131: Cambio en la entidad usuario, se coloca llave foranea el grado.
        */
        busquedaDTO.setGrado(usuario.getUsuGrado().getId());
        
        if (usuario.getClasificacion() != null) {
            busquedaDTO.setClasificacionId(usuario.getClasificacion().getId());
            busquedaDTO.setClasificacionNombre(usuario.getClasificacion()
                    .getNombre());
        }
        
        // 2018-03-12 edison.gonzalez@controltechcg.com Issue #151 (SIGDI-Controltech):
        // Se añade la lista de cargos del usuario destino
        List<Object[]> list = cargosRepository.findCargosXusuario(usuario.getId());
        List<CargoDTO> cargoDTOs = new ArrayList<>();
        for (Object[] os : list) {
            CargoDTO cargoDTO = new CargoDTO(((BigDecimal) os[0]).intValue(), (String) os[1]);
            cargoDTOs.add(cargoDTO);
        }
        busquedaDTO.setCargosDestino(cargoDTOs);

        return busquedaDTO;
    }
}
