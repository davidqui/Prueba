package com.laamware.ejercito.doc.web.serv;


import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.ExpedienteTransicion;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.ExpedienteEstado;
import com.laamware.ejercito.doc.web.entity.ExpedienteTransicion;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.ExpedienteTransicionRepository;
import java.util.List;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


/**
 * Servicio para las operaciones de las transiciones de los expedientes.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @since Jul 27, 208 Issue #181 (SICDI-Controltech) feature-181
 */
@Service
public class ExpedienteTransicionService {

    /**
     * Repositorio de transiciones de expedientes.
     */
    @Autowired
    private ExpedienteTransicionRepository expedienteTransicionRepository;
    
    public List<ExpedienteTransicion> retornarListaTransicionesXexpediente(Long expId){
        return expedienteTransicionRepository.findByExpIdExpId(expId, new Sort(Sort.Direction.DESC, "fecCreacion"));
    }     
    
    public void crearTransicion(Expediente expediente, ExpedienteEstado estadoTransicion, Usuario usuario, Documento documento, Usuario usuModificado){
        
        ExpedienteTransicion expedienteTransicion = new ExpedienteTransicion();
        expedienteTransicion.setDocId(documento);
        expedienteTransicion.setExpEstId(estadoTransicion);
        expedienteTransicion.setExpId(expediente);
        expedienteTransicion.setFecCreacion(new Date());
        expedienteTransicion.setUsuCreacion(usuario);
        expedienteTransicion.setUsuModificado(usuModificado);
        expedienteTransicionRepository.saveAndFlush(expedienteTransicion);
    }
    
    
    public List<ExpedienteTransicion> listaTrasicionesXdocumento(Documento documento){
        return expedienteTransicionRepository.findByDocId(documento, new Sort(Sort.Direction.DESC, "fecCreacion"));
    }
}
