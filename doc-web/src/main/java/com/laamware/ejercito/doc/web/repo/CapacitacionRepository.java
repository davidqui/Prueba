package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Capacitacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapacitacionRepository extends  JpaRepository<Capacitacion, Integer> {
    
    Capacitacion findOneByResultado(String resultado);

//    public List<Capacitacion> getByActivoTrue(Sort sort);
//    
//    public Page<Capacitacion> getByActivoTrue(Pageable pageable);
//
//    Capacitacion findOneByResultadoAndActivoTrue(String resultado);
    
   
    public Page<Capacitacion> findByResultadoIgnoreCaseContaining(Pageable pageable, String resultado);
    
//    public Page<Capacitacion> findByResultadoIgnoreCaseContainingAndActivoTrue(Pageable pageable, String resultado);

//    public List<Capacitacion> getByActivoTrueAndTemaCapacitacionId(Sort sort, Integer Id);
    
//    public Page<Capacitacion> getByActivoTrueAndCapacitacionId(Pageable pageable,Integer Id);
    
    /**
     * Busca todos los registros de las  Capacitaciones por el Id.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param Id id de la capaciracion a buscar
     * @return 
     */
//    public List<Capacitacion> findAllByCapacitacionId(Integer Id);
    
    /**
     * Busca todos los registros de las  Capacitaciones por el Id.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param Id id de la capacitacion a buscar
     * @param pageable  Clase de springframework para la realizacion de paginacion
     * @return 
     */
     
//    public Page<Capacitacion> findAllByCapacitacionId(Pageable pageable,Integer Id);
    
}
