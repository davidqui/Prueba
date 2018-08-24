
package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.TransferenciaEstado;
import com.laamware.ejercito.doc.web.entity.TransferenciaTransicion;
import com.laamware.ejercito.doc.web.repo.TransferenciaTransicionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para las transiciones de las transferencias.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @version 08/22/2018 Issue #4 (SICDI-Controltech) feature-4
 */
@Service
public class TransferenciaTransicionService {
    
    @Autowired
    private TransferenciaTransicionRepository transferenciaTransicionRepository;
    
    /***
     * Busca una transición dentro de una transferencia de archivo 
     * @param transferenciaEstado 
     * @return 
     */
    public TransferenciaTransicion findTransferenciaTransicionRechazado(TransferenciaEstado transferenciaEstado, TransferenciaArchivo transferenciaArchivo){
        return transferenciaTransicionRepository.findOneByTraEstIdAndTarId(transferenciaEstado, transferenciaArchivo);
    }
}
