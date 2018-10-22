package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.TemaCapacitacion;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TemaCapacitacionRepository extends PagingAndSortingRepository<TemaCapacitacion, Integer>, JpaRepository<TemaCapacitacion, Integer> {
    
    TemaCapacitacion findOneByTema(String tema);

    public List<TemaCapacitacion> getByActivoTrue(Sort sort);
    public Page<TemaCapacitacion> getByActivoTrue(Pageable pageable);

//    TemaCapacitacion findOneByTextoTemaCapacitacionAndActivoTrue(String tema);
//    public Page<TemaCapacitacion> findByTemaIgnoreCaseContaining(Pageable pageable, String tema);
//    public Page<TemaCapacitacion> findByTemaIgnoreCaseContainingAndActivoTrue(Pageable pageable, String tema);

//    public List<TemaCapacitacion> getByActivoTrueAndTemaCapacitacionId(Sort sort, Integer Id);
//    public Page<TemaCapacitacion> getByActivoTrueAndTemaCapacitacionId(Pageable pageable,Integer Id);
    
    /**
     * * Busca todos los Temas de Capacitacion por el Id.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param Id
     * @return 
     */
//    public List<TemaCapacitacion> findAllByTemaCapacitacionId(Integer Id);
    
    /**
     **      * * Busca todos los Temas de Capacitacion por el Id, para paginar.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param pageable  Clase de springframework para la realizacion de paginacion
     * @param Id id de la TemaCapacitacion a buscar
     * @return 
     */
//    public Page<TemaCapacitacion> findAllByTemaCapacitacionId(Pageable pageable,Integer Id);
    
}
