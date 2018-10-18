package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Capacitacion;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CapacitacionRepository extends PagingAndSortingRepository<Capacitacion, Integer>, JpaRepository<Capacitacion, Integer> {
    
    Capacitacion findOneByCapacitacion(String tema);

    public List<Capacitacion> getByActivoTrue(Sort sort);
    public Page<Capacitacion> getByActivoTrue(Pageable pageable);

    Capacitacion findOneByCapacitacionAndActivoTrue(String tema);
    
    public Page<Capacitacion> findByCapacitacionIgnoreCaseContaining(Pageable pageable, String tema);
    
    public Page<Capacitacion> findByCapacitacionIgnoreCaseContainingAndActivoTrue(Pageable pageable, String tema);

    public List<Capacitacion> getByActivoTrueAndCapacitacionId(Sort sort, Integer Id);
    public Page<Capacitacion> getByActivoTrueAndCapacitacionId(Pageable pageable,Integer Id);
    
    /**
     * Busca todos los registros de las  Capacitaciones por el Id.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param Id id de la capaciracion a buscar
     * @return 
     */
    public List<Capacitacion> findAllByCapacitacionId(Integer Id);
    
    /**
     * Busca todos los registros de las  Capacitaciones por el Id.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param Id id de la capacitacion a buscar
     * @param pageable  Clase de springframework para la realizacion de paginacion
     * @return 
     */
     
    public Page<Capacitacion> findAllByCapacitacionId(Pageable pageable,Integer Id);
    
}
