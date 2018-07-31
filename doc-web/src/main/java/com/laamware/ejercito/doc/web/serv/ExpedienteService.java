package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.repo.ExpedienteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para las operaciones de los expedientes.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @since Jul 27, 208 Issue #181 (SICDI-Controltech) feature-181
 */
@Service
public class ExpedienteService {

    /**
     * Repositorio de expedientes.
     */
    @Autowired
    private ExpedienteRepository expedienteRepository;
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
    
    
    public static final Long ESTADO_INICIAL_EXPEDIENTE = new Long(1100) ;
    public static final Long ESTADO_ENVIADO_APROBAR = new Long(1101) ;

    
    
    public Expediente finById(Long id){
        return expedienteRepository.findOne(id);
    }
    
    public Expediente CrearExpediente(Expediente expediente, Usuario usuario) throws BusinessLogicException{
        
        if (expediente.getExpNombre() == null || expediente.getExpNombre().trim().length() < 1)
            throw new BusinessLogicException("El nombre del expediente es requerido");
        if (expediente.getTrdIdPrincipal() == null)
            throw new BusinessLogicException("Debe elegir una TRD principal");
        if (expediente.getExpDescripcion() == null || expediente.getExpDescripcion().trim().length() < 1)
            throw new BusinessLogicException("La descripción del expediente es requerida");
        
        List<Expediente> expedientesNombre = expedienteRepository.getByExpNombreAndDepId(expediente.getExpNombre(), usuario.getDependencia());
        if (!expedientesNombre.isEmpty()) {
            throw new BusinessLogicException("Ya existe un expediente con este nombre.");
        }

        expediente.setUsuCreacion(usuario);
        expediente.setUsuarioAsignado(Expediente.ASIGNADO_USUARIO);
        expediente.setDepId(usuario.getDependencia());
        expediente.setFecCreacion(new Date());
        expediente.setIndCerrado(false);
        expediente.setIndAprobadoInicial(false);
        expediente.setEstadoCambio(false);
        
        expediente = expedienteRepository.saveAndFlush(expediente);
        expedienteTransicionService.crearTransicion(expediente, 
                expedienteEstadoService.findById(ESTADO_INICIAL_EXPEDIENTE), usuario, null, null);
        return expediente;
    }

    public void enviarAprobar(Expediente expediente, Usuario usuarioSesion) {
        expediente.setUsuarioAsignado(1);
        
        expedienteTransicionService.crearTransicion(expediente, 
                expedienteEstadoService.findById(ESTADO_ENVIADO_APROBAR), usuarioSesion, null, null);
        
        expedienteRepository.saveAndFlush(expediente);
    }

    /**
     * Obtiene el numero de registros de las bandejas de entrada por usuario
     *
     * @param usuId Identificador del usuario
     * @return Número de registros
     */
    public int obtenerCountExpedientesPorUsuario(Integer usuId) {
        return expedienteRepository.findExpedientesPorUsuarioCount(usuId);
    }

    /**
     * Obtiene los registros de las bandejas de entrada por usuario, de acuerdo
     * a la fila inicial y final.
     *
     * @param usuId Identificador del usuario
     * @param inicio Numero de registro inicial
     * @param fin Numero de registro final
     * @return Lista de documentos
     */
    public List<Expediente> obtenerExpedientesPorUsuarioPaginado(Integer usuId, int inicio, int fin) {
        return expedienteRepository.findExpedientesPorUsuarioPaginado(usuId, inicio, fin);
    }
}
