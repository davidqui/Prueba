package com.laamware.ejercito.doc.web.serv;


import com.laamware.ejercito.doc.web.entity.ExpedienteEstado;
import com.laamware.ejercito.doc.web.repo.ExpedienteEstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Servicio para las operaciones de los estados de los expedientes.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @since Jul 27, 208 Issue #181 (SICDI-Controltech) feature-181
 */
@Service
public class ExpedienteEstadoService {

    /**
     * Repositorio de estados de expedientes.
     */
    @Autowired
    private ExpedienteEstadoRepository expedienteEstadoRepository;
    
    
    public ExpedienteEstado findById(Long id){
        return expedienteEstadoRepository.findOne(id);
    }
}
