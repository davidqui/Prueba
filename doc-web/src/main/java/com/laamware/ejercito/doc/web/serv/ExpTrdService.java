package com.laamware.ejercito.doc.web.serv;


import com.laamware.ejercito.doc.web.entity.ExpTrd;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laamware.ejercito.doc.web.repo.ExpTrdRepository;
import java.util.Date;
import java.util.List;


/**
 * Servicio para las trds de los expedientes.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @since Jul 27, 208 Issue #181 (SICDI-Controltech) feature-181
 */
@Service
public class ExpTrdService {

    /**
     * Repositorio de las trds de los expedientes.
     */
    @Autowired
    private ExpTrdRepository expTrdRepository;
    
    /***
     * Lista las trd dado un expediente.
     * @param expediente
     * @return 
     */
    public List<ExpTrd> findTrdsByExpediente(Expediente expediente){
        return expTrdRepository.findByExpIdAndActivoTrue(expediente);
    }
    
    /***
     * Lista las trds por un expediente.
     * @param expediente
     * @return lista de trd en un expediente
     */
    public List<ExpTrd> findTrdsByExpedienteAll(Expediente expediente){
        return expTrdRepository.findByExpId(expediente);
    }
    
    /**
     * Guarda una trd en un expediente 
     * @param expediente expediente a agregar
     * @param trd trd por agregar 
     * @param usuario usuario que agrega
     */
    public void guardarTrdExpediente(final Expediente expediente, final Trd trd,
            final Usuario usuario){
        
        ExpTrd expTrd = new ExpTrd();
        expTrd.setTrdId(trd);
        expTrd.setUsuCreacion(usuario);
        expTrd.setFecCreacion(new Date());
        expTrd.setActivo(true);
        expTrd.setExpId(expediente);
        expTrd.setIndAprobado(true);
        
        expTrdRepository.saveAndFlush(expTrd);
    }
    
    /***
     * Cambia el estado de una trd ya existente
     * @param expTrd trd existente dentro de expediente
     * @param usuario usuario que realiza la acción de modificar
     * @param activo parametro a actualizar
     */
    public void guardarTrdExpediente(ExpTrd expTrd, final Usuario usuario, boolean activo){
        expTrd.setUsuModificacion(usuario);
        expTrd.setFecModificacion(new Date());
        expTrd.setIndAprobado(true);
        expTrd.setActivo(activo);
        expTrdRepository.saveAndFlush(expTrd);
    }
    
    /***
     * Valida en un expediente una trd
     * @param expediente exopediente a validar
     * @param trd trd por validar
     * @return true si si la contiene, false de lo contrario
     */
    public boolean validateTrdByExpediente(Expediente expediente, Trd trd){
        List<ExpTrd> trds = expTrdRepository.findByExpIdAndActivoTrue(expediente);
        for (ExpTrd tr : trds) {
            if (tr.getTrdId().getId().equals(trd.getId())) {
                return true;
            }
        }
        return false;
    }
}