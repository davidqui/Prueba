
package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.TransExpedienteDetalle;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.TransExpedienteDetalleRepository;
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
    
    
    public void eliminarExpedientesTransferencia(TransferenciaArchivo transferenciaArchivo){
        List<TransExpedienteDetalle> expedientes = transExpedienteDetalleRepository.findAllByTarIdAndActivoTrue(transferenciaArchivo);
        for (TransExpedienteDetalle expediente : expedientes) { expediente.setActivo(0); }
        transExpedienteDetalleRepository.save(expedientes);
    }
}
