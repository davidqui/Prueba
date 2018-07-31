package com.laamware.ejercito.doc.web.serv;


import com.laamware.ejercito.doc.web.entity.ExpObservacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laamware.ejercito.doc.web.repo.ExpObservacionRepository;
import java.util.List;
import org.springframework.data.domain.Sort;


/**
 * Servicio para las observaciones de los expedientes.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @since Jul 27, 208 Issue #181 (SICDI-Controltech) feature-181
 */
@Service
public class ExpObservacionService {

    /**
     * Repositorio de las observaciones de los expedientes.
     */
    @Autowired
    private ExpObservacionRepository expObservacionRepository;
    
    public List<ExpObservacion> retornarListaTransicionesXexpediente(Long expId){
        return expObservacionRepository.findByExpIdExpId(expId, new Sort(Sort.Direction.DESC, "fecCreacion"));
    } 
    
    public void guardarObservacion(ExpObservacion expObservacion){
        expObservacionRepository.saveAndFlush(expObservacion);
    }
}
