package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.ExpedienteDTO;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.repo.ExpedienteRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
        return expedienteRepository.findExpedientesDTOPorUsuarioCount(usuId);
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
    public List<ExpedienteDTO> obtenerExpedientesDTOPorUsuarioPaginado(Integer usuId, int inicio, int fin) {
        List<ExpedienteDTO> expedientes = new ArrayList<>();
        List<Object[]> result = expedienteRepository.findExpedientesPorUsuarioPaginado(usuId, inicio, fin);
        if (result != null && !result.isEmpty()) {
            for (Object[] object : result) {
                ExpedienteDTO dTO = retornaExpedienteDTO(object);
                expedientes.add(dTO);
            }
        }
        return expedientes;
    }

    public ExpedienteDTO obtieneExpedienteDTOPorUsuarioPorExpediente(Integer usuId, Long expId) {
        ExpedienteDTO expedienteDTO = null;
        System.err.println("Buscando expediente");
        List<Object[]> result = expedienteRepository.findExpedienteDtoPorUsuarioPorExpId(usuId, expId);
        
        if (result != null) {
            System.err.println("Retornando expediente1= "+result.size());
            for (Object[] object : result) {
                expedienteDTO = retornaExpedienteDTO(object);   
            }
        }
        return expedienteDTO;
    }
    
    public Expediente findOne(Long expId){
        return expedienteRepository.findOne(expId);
    }

    private ExpedienteDTO retornaExpedienteDTO(Object[] object) {
        ExpedienteDTO dTO = new ExpedienteDTO();
        dTO.setExpId(object[0] != null ? ((BigDecimal) object[0]).longValue() : null);
        dTO.setExpNombre(object[1] != null ? (String) object[1] : "");
        dTO.setFecCreacion(object[2] != null ? (Date) object[2] : null);
        dTO.setDepId(object[3] != null ? ((BigDecimal) object[3]).intValue() : null);
        dTO.setDepNombre(object[4] != null ? (String) object[4] : "");
        dTO.setTrdIdPrincipal(object[5] != null ? ((BigDecimal) object[5]).intValue() : null);
        dTO.setTrdNomIdPrincipal(object[6] != null ? (String) object[6] : "");
        dTO.setIndJefeDependencia(object[7] != null ? ((BigDecimal) object[7]).equals(BigDecimal.ONE) : false);
        dTO.setIndUsuCreador(object[8] != null ? ((BigDecimal) object[8]).equals(BigDecimal.ONE) : false);
        dTO.setIndAprobadoInicial(object[9] != null ? ((BigDecimal) object[9]).equals(BigDecimal.ONE) : false);
        dTO.setJefeDependencia(object[10] != null ? (String) object[10] : "");
        dTO.setUsuarioCreador(object[11] != null ? (String) object[11] : "");
        dTO.setExpTipo(object[12] != null ? (String) object[12] : "");
        dTO.setExpDescripcion(object[13] != null ? (String) object[13] : "");
        dTO.setIndUsuarioAsignado(object[14] != null ? ((BigDecimal) object[14]).intValue() : null);
        dTO.setIndCerrado(object[15] != null ? ((BigDecimal) object[15]).equals(BigDecimal.ONE) : false);
        dTO.setNumTrdComplejo(object[16] != null ? ((BigDecimal) object[16]).intValue() : null);
        dTO.setNumUsuarios(object[17] != null ? ((BigDecimal) object[17]).intValue() : null);
        dTO.setNumDocumentos(object[18] != null ? ((BigDecimal) object[18]).intValue() : null);
        dTO.setIndIndexacion(object[19] != null ? ((BigDecimal) object[19]).equals(BigDecimal.ONE) : false);
        return dTO;
    }
}
