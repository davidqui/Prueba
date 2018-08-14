package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.ExpUsuarioDto;
import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.ExpUsuario;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laamware.ejercito.doc.web.repo.ExpUsuarioRepository;
import com.laamware.ejercito.doc.web.util.DateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.data.domain.Sort;

/**
 * Servicio para los usuarios de los expedientes.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @since Jul 27, 208 Issue #181 (SICDI-Controltech) feature-181
 */
@Service
public class ExpUsuarioService {

    /**
     * Repositorio de los usuarios de los expedientes.
     */
    @Autowired
    private ExpUsuarioRepository expUsuarioRepository;

    /**
     * Repositorio de estados del expediente.
     */
    @Autowired
    private ExpedienteEstadoService expedienteEstadoService;

    /**
     * Repositorio de transiciones del espediente.
     */
    @Autowired
    private ExpedienteTransicionService expedienteTransicionService;
        
    /*
     * Servicio de notificaciones.
     */
    @Autowired
    private NotificacionService notificacionService;

    // constantes de estado
    public static final Long ESTADO_USUARIO_ASIGNADO = new Long(1107);
    public static final Long ESTADO_USUARIO_MODIFICADO = new Long(1101);
    public static final Long ESTADO_USUARIO_ELIMINADO = new Long(1101);
    
    // constantes de proceso 
    public final static Integer PER_LECTURA = 1;
    public final static Integer PER_INDEXACION = 2;

    // constantes de notificación
    public final static Integer NOTIFICACION_EXPEDIENTE_USUARIO_AGREGADO = 200;
    public final static Integer NOTIFICACION_EXPEDIENTE_USUARIO_ELIMINADO = 201;
    
    /**
     * Lista los usuarios de un expediente
     *
     * @param expediente
     * @return
     */
    public List<ExpUsuario> findByExpediente(Expediente expediente) {
        return expUsuarioRepository.getByExpIdAndActivoTrue(expediente, new Sort(Sort.Direction.DESC, "usuId.usuGrado.pesoOrden"));
    }

    /**
     * Lista los usuarios expediente de un expediente por un usuario
     *
     * @param expediente
     * @param usuario
     * @return
     */
    public List<ExpUsuario> findByExpedienteAndUsuario(Expediente expediente, Usuario usuario) {
        return expUsuarioRepository.getByExpIdAndUsuIdAndActivoTrue(expediente, usuario);
    }
    
    /**
     * Lista los usuarios expediente de un expediente por un usuario
     * @param expediente
     * @param usuario
     * @return 
     */
    public List<ExpUsuario> findByExpedienteAndUsuarioAndPermisoTrue(Expediente expediente, Usuario usuario){
        return expUsuarioRepository.getByExpIdAndUsuIdAndActivoTrueIndAprobadoTrue(expediente, usuario);
    }
    
    /**
     * Lista los usuarios expediente de un expediente por un usuario si tiene permiso de indexación
     * @param expediente
     * @param usuario
     * @return 
     */
    public List<ExpUsuario> findByExpedienteAndUsuarioAndPermisoTruePermisoIndexacion(Expediente expediente, Usuario usuario){
        return expUsuarioRepository.getByExpIdAndUsuIdAndPermisoAndActivoTrueIndAprobadoTrue(expediente, usuario, 2);
    }
    
    /***
     * Agrega un usuario a un expediente
     * @param expediente expediente a agergar 
     * @param usuCreador usuario quien agrega
     * @param usuario usuario a agregar
     * @param cargo cargo con el que se agregara
     * @param permiso permiso con el que se le agregara
     */
    public void agregarUsuarioExpediente(Expediente expediente, Usuario usuCreador, Usuario usuario, Cargo cargo, Integer permiso){
        ExpUsuario expUsuario = new ExpUsuario();
        expUsuario.setUsuCreacion(usuario);
        expUsuario.setUsuId(usuario);
        expUsuario.setExpId(expediente);
        expUsuario.setCarId(cargo);
        expUsuario.setUsuCreacion(usuCreador);
        expUsuario.setFecCreacion(new Date());
        expUsuario.setIndAprobado(false);
        expUsuario.setPermiso(permiso);
        expUsuario.setActivo(true);

        if (expediente.getUsuCreacion().getDependencia().getJefe().getId().equals(usuCreador.getId())) {
            expUsuario.setIndAprobado(true);
        }
        expUsuarioRepository.saveAndFlush(expUsuario);

        expedienteTransicionService.crearTransicion(expediente,
                expedienteEstadoService.findById(ESTADO_USUARIO_ASIGNADO), usuCreador, null, usuario);
        
        Map<String, Object> model = new HashMap();
        model.put("expUsuario", expUsuario);
        model.put("usuario", usuCreador);
        try {
            notificacionService.enviarNotificacion(model, NOTIFICACION_EXPEDIENTE_USUARIO_AGREGADO, usuario);
        } catch (Exception ex) {
            Logger.getLogger(ExpUsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Elimina un usuario de un expediente 
     * @param EpxUsuarioId Identificador usuario a eliminar 
     * @param usuarioSesion usuario quien elimina
     * @param expediente expediente del que se le elimina
     */
    public void eliminarUsuarioExpediente(Long EpxUsuarioId, Usuario usuarioSesion, Expediente expediente) {
        ExpUsuario expUsuario = expUsuarioRepository.getOne(EpxUsuarioId);
        expUsuario.setActivo(false);
        expUsuario.setFecModificacion(new Date());
        expUsuario.setUsuModificacion(usuarioSesion);
        expUsuarioRepository.saveAndFlush(expUsuario);
        expedienteTransicionService.crearTransicion(expediente,
                expedienteEstadoService.findById(ESTADO_USUARIO_ELIMINADO), usuarioSesion, null, expUsuario.getUsuId());
        
        Map<String, Object> model = new HashMap();
        model.put("expUsuario", expUsuario);
        model.put("usuario", usuarioSesion);
        
        try {
            notificacionService.enviarNotificacion(model, NOTIFICACION_EXPEDIENTE_USUARIO_ELIMINADO, expUsuario.getUsuId());
        } catch (Exception ex) {
            Logger.getLogger(ExpUsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /***
     * Edita un ususario de un expediente
     * @param expediente expediente al que se le editara
     * @param usuarioSesion usuario que edita 
     * @param usuario usuario que se edita 
     * @param cargoUsu cargo de usuario editado 
     * @param permiso permiso del usuario editado
     */
    public void editarUsuarioExpediente(Expediente expediente, Usuario usuarioSesion, Long usuario, Cargo cargoUsu, Integer permiso) {
        ExpUsuario expUsuario = expUsuarioRepository.getOne(usuario);
        expUsuario.setPermiso(permiso);
        expUsuario.setCarId(cargoUsu);
        expUsuario.setFecModificacion(new Date());
        expUsuario.setUsuModificacion(usuarioSesion);
        expUsuarioRepository.saveAndFlush(expUsuario);
        expedienteTransicionService.crearTransicion(expediente,
                expedienteEstadoService.findById(ESTADO_USUARIO_MODIFICADO), usuarioSesion, null, expUsuario.getUsuId());
    }
    
    /***
     * Lista los usuarios que estan pendientes por aprobar dentro de un expediente
     * @param expediente
     * @return lista de usuarios 
     */
    public List<ExpUsuarioDto> retornaUsuariosPendientesPorAprobar(Expediente expediente) {
        List<ExpUsuario> usuarios = expUsuarioRepository.getByExpIdAndActivoTrueAndIndAprobadoFalse(expediente, new Sort(Sort.Direction.DESC, "usuId.usuGrado.pesoOrden"));
        List<ExpUsuarioDto> expUsuarioDtos = new ArrayList<>();
        if(usuarios.size()> 0){
            for (ExpUsuario eu : usuarios) {
               ExpUsuarioDto usuarioDto = new ExpUsuarioDto(eu.getExpId().getExpId(), eu.getExpUsuId(), (eu.getPermiso() == 1)? "Lectura":"Indexación", eu.getUsuId().toString(), eu.getCarId().getCarNombre(), eu.getFecCreacion() != null? DateUtil.dateFormatObservacion.format(eu.getFecCreacion()):null, eu.getFecModificacion()!= null? DateUtil.dateFormatObservacion.format(eu.getFecModificacion()):null, eu.getUsuId().getClasificacion().getNombre());
               expUsuarioDtos.add(usuarioDto);
            }   
        }
        return expUsuarioDtos;
    }
    
    /***
     * Aprueba los usuarios de un expediente
     * @param expediente expediente a aprobar usuarios
     * @param usuarioSesion usuario que aprueba 
     */
    public void aprobarUsuariosPorExpediente(Expediente expediente,Usuario usuarioSesion){
        List<ExpUsuario> expUsuarios = expUsuarioRepository.getByExpIdAndActivoTrueAndIndAprobadoFalse(expediente, new Sort(Sort.Direction.DESC, "usuId.usuGrado.pesoOrden"));
        if(expUsuarios.size() > 0){
            for(ExpUsuario expUsuario :expUsuarios){
                expUsuario.setIndAprobado(true);
                expUsuario.setUsuModificacion(usuarioSesion);
                expUsuario.setFecModificacion(new Date());
                expUsuarioRepository.saveAndFlush(expUsuario);
            }
        }
    }
    
    /***
     * Rechaza un expediente
     * @param expediente Expediente a rechazar usuarios
     * @param usuarioSesion usuario que rechaza
     */
    public void rechazarUsuariosPorExpediente(Expediente expediente,Usuario usuarioSesion){
        List<ExpUsuario> expUsuarios = expUsuarioRepository.getByExpIdAndActivoTrueAndIndAprobadoFalse(expediente, new Sort(Sort.Direction.DESC, "usuId.usuGrado.pesoOrden"));
        if(expUsuarios.size() > 0){
            for(ExpUsuario expUsuario :expUsuarios){
                expUsuario.setIndAprobado(false);
                expUsuario.setActivo(false);
                expUsuario.setUsuModificacion(usuarioSesion);
                expUsuario.setFecModificacion(new Date());
                expUsuarioRepository.saveAndFlush(expUsuario);
            }
        }
    }
    
    /***
     * Verifica si una lista de usuraios tiene algun cambio por aprobar
     * @param usuarios
     * @return 
     */
    public boolean tieneCambios(List<ExpUsuario> usuarios) {
        for (ExpUsuario usuario : usuarios) {
            if (!usuario.getIndAprobado())
                return true;
        }
        return false;
    }
}
