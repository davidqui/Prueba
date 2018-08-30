
package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.TransferenciaObservacion;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.TransferenciaObservacionRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para las observaciones de las transferencias.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @version 08/22/2018 Issue #4 (SICDI-Controltech) feature-4
 */
@Service
public class TransferenciaObservacionService {
    
    /**
     * Respositorio para las observaciones de las transferencias
     */
    @Autowired
    private TransferenciaObservacionRepository transferenciaObservacionRepository;
    
    public void crearObservacon(TransferenciaArchivo transferenciaArchivo, String observacion, Usuario usuario){
        TransferenciaObservacion transferenciaObservacion = new TransferenciaObservacion();
        transferenciaObservacion.setTarId(transferenciaArchivo);
        transferenciaObservacion.setTraObservacion(observacion);
        transferenciaObservacion.setFecCreacion(new Date());
        transferenciaObservacion.setUsuId(usuario);
        transferenciaObservacionRepository.save(transferenciaObservacion);
    }
    
    /**
     * Lista las observaciones dada una transferencia
     * @param transferencia transferencia
     * @return lista de observaciones
     */
    public List<TransferenciaObservacion> observacionesPorTranferencia(TransferenciaArchivo transferencia){
        return transferenciaObservacionRepository.findByTarIdOrderByFecCreacionDesc(transferencia);
    }
    
    
}
