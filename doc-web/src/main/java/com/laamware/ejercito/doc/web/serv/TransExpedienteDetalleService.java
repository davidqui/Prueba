
package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.TransExpedienteDetalle;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.TransExpedienteDetalleRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para el detalle de los expedientes de las transferencias.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @version 08/22/2018 Issue #4 (SICDI-Controltech) feature-4
 */
@Service
public class TransExpedienteDetalleService {
    
    
    @Autowired
    private TransExpedienteDetalleRepository transExpedienteDetalleRepository;
    
    @Autowired
    private ExpedienteService expedienteService;
    
    /***
     * Guarda un expediente en una transferencia
     * @param transferenciaArchivo transferencia en la que se va a guardar
     * @param expediente expediente que se va a transferir
     * @param usuario usuario que realiza el proceso
     */
    public void guardarExpedienteTransferencia(TransferenciaArchivo transferenciaArchivo, 
            Expediente expediente, Usuario usuario){
        TransExpedienteDetalle transExpedienteDetalle = new TransExpedienteDetalle();
        transExpedienteDetalle.setTarId(transferenciaArchivo);
        transExpedienteDetalle.setExpId(expediente);
        transExpedienteDetalle.setAnteriorDepId(transferenciaArchivo.getOrigenDependencia());
        transExpedienteDetalle.setAnteriorQuien(transferenciaArchivo.getOrigenUsuario());
        transExpedienteDetalle.setNuevoDepId(transferenciaArchivo.getDestinoDependencia());
        transExpedienteDetalle.setNuevoQuien(transferenciaArchivo.getDestinoUsuario());
        transExpedienteDetalle.setIndRealizado(0);
        transExpedienteDetalle.setActivo(1);
        transExpedienteDetalle.setFecTransferencia(new Date());
        transExpedienteDetalle.setTraExpId(expediente.getExpId());
        
        transExpedienteDetalleRepository.save(transExpedienteDetalle);
    }
    
    /***
     * Elimina todas los expedientes asociados a una transferencia
     * @param transferenciaArchivo  transferencia a elminar
     */
    public void eliminarExpedientesTransferencia(TransferenciaArchivo transferenciaArchivo){
        List<TransExpedienteDetalle> expedientes = transExpedienteDetalleRepository.findAllByTarIdAndActivo(transferenciaArchivo, 1);
        for (TransExpedienteDetalle expediente : expedientes) { 
            expediente.setActivo(0);
            transExpedienteDetalleRepository.save(expediente);
        }
    }
    
    /***
     * Busca las transferencia de expedientes dado una transfernecia de archivo
     * @param transferenciaArchivo
     * @return lista de transferencia de expediente
     */
    public List<TransExpedienteDetalle> buscarXTransferenciaArchivo(TransferenciaArchivo transferenciaArchivo){
        return transExpedienteDetalleRepository.findAllByTarIdAndActivo(transferenciaArchivo, 1);
    }
    
    /***
     * Busca las transferencia de expedientes no concluidos en otras transferencias
     * @param usuario usuario a consultar los expedientes
     * @param transferenciaArchivo transferencia que no debe consultar
     * @return lista de transferencia de expediente
     */
    public List<TransExpedienteDetalle> buscarOtrosExpedientesEnTranseferencia(Usuario usuario, TransferenciaArchivo transferenciaArchivo){
        return transExpedienteDetalleRepository.expedienteXUsuarioxNotTransferencia(usuario.getId(), transferenciaArchivo.getId());
    }
    
    /***
     * Método para transferir los expediente de una transfenrencia
     * @param transferenciaArchivo transferencia de archivo
     * @param usuario usuario
     */
    public void transferirExpedientes(TransferenciaArchivo transferenciaArchivo, Usuario usuario){
        List<TransExpedienteDetalle> buscarXTransferenciaArchivo = buscarXTransferenciaArchivo(transferenciaArchivo);
        for (TransExpedienteDetalle transExpedienteDetalle : buscarXTransferenciaArchivo) {
            transExpedienteDetalle.setIndRealizado(1);
            expedienteService.cambiarUsuarioCreador(transExpedienteDetalle.getExpId(), transExpedienteDetalle.getNuevoQuien(), transExpedienteDetalle.getAnteriorQuien());
            transExpedienteDetalleRepository.save(transExpedienteDetalle);
        }
    }
    
    /***
     * Lista los expedientes que no encuentran en custodia de un usuario.
     * @param transferenciaArchivo transferencia de archivo
     * @param usuario usuario a evaluar si tiene los expedientes
     * @return lista de los expedientes que no pertenecen al usuario.
     */
    public List<TransExpedienteDetalle> expedientesNoPosesionTransferencia(TransferenciaArchivo transferenciaArchivo, Usuario usuario){
        List<TransExpedienteDetalle> buscarXTransferenciaArchivo = buscarXTransferenciaArchivo(transferenciaArchivo);
        List<TransExpedienteDetalle> answ = new ArrayList<>();
        for (TransExpedienteDetalle transExpedienteDetalle : buscarXTransferenciaArchivo) {
            if (!transExpedienteDetalle.getExpId().getUsuCreacion().getId().equals(usuario.getId())) {
                answ.add(transExpedienteDetalle);
            }
        }
        return answ;
    }
}
