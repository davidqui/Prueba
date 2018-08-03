package com.laamware.ejercito.doc.web.serv;


import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.ExpUsuario;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laamware.ejercito.doc.web.repo.ExpUsuarioRepository;
import java.util.Date;
import java.util.List;
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
    
    public static final Long ESTADO_USUARIO_ASIGNADO = new Long(1107);
    public static final Long ESTADO_USUARIO_MODIFICADO = new Long(1101) ;
    public static final Long ESTADO_USUARIO_ELIMINADO = new Long(1101) ;


    
    public final static Integer PER_LECTURA = 1;
    public final static Integer PER_INDEXACION = 1;
    
    /**
     * Lista los usuarios de un expediente
     * @param expediente
     * @return 
     */
    public List<ExpUsuario> findByExpediente(Expediente expediente){
        return expUsuarioRepository.getByExpIdAndActivoTrue(expediente, new Sort(Sort.Direction.DESC, "usuId.usuGrado.pesoOrden"));
    }
    
    /**
     * Lista los usuarios expediente de un expediente por un usuario
     * @param expediente
     * @param usuario
     * @return 
     */
    public List<ExpUsuario> findByExpedienteAndUsuario(Expediente expediente, Usuario usuario){
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
        
        if (expediente.getUsuCreacion().getDependencia().getJefe().getId().equals(usuCreador.getId()))
            expUsuario.setIndAprobado(true);
        expUsuarioRepository.saveAndFlush(expUsuario);
        
        expedienteTransicionService.crearTransicion(expediente, 
            expedienteEstadoService.findById(ESTADO_USUARIO_ASIGNADO), usuCreador, null, usuario);
        
    }
    
    
    public void eliminarUsuarioExpediente(Long EpxUsuarioId, Usuario Usuario, Expediente expediente){
        ExpUsuario expUsuario = expUsuarioRepository.getOne(EpxUsuarioId);
        expUsuario.setActivo(false);
        expUsuario.setFecModificacion(new Date());
        expUsuario.setUsuModificacion(Usuario);
        expUsuarioRepository.saveAndFlush(expUsuario);
        expedienteTransicionService.crearTransicion(expediente, 
            expedienteEstadoService.findById(ESTADO_USUARIO_ELIMINADO), Usuario, null, expUsuario.getUsuId());
    }

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
    
    
}