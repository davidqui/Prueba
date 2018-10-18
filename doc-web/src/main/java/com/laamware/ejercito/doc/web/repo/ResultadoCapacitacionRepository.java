package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.ResultadoCapacitacion;
import com.laamware.ejercito.doc.web.entity.ResultadoCapacitacion;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ResultadoCapacitacionRepository extends PagingAndSortingRepository<ResultadoCapacitacion, Integer>, JpaRepository<ResultadoCapacitacion, Integer> {
    

    public List<ResultadoCapacitacion> getByActivoTrue(Sort sort);
    public Page<ResultadoCapacitacion> getByActivoTrue(Pageable pageable);

    public Page<ResultadoCapacitacion> findByTextoResultadoCapacitacionIgnoreCaseContaining(Pageable pageable, String nombre);
    public Page<ResultadoCapacitacion> findByTextoResultadoCapacitacionIgnoreCaseContainingAndActivoTrue(Pageable pageable, String nombre);

    public List<ResultadoCapacitacion> getByActivoTrueAndTemaCapacitacionId(Sort sort, Integer Id);
    public Page<ResultadoCapacitacion> getByActivoTrueAndTemaCapacitacionId(Pageable pageable,Integer Id);
    
    /**
     * * Busca todos los registros de los Resultados de la Capaciracion por el Id.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param Id id del Resultado de la capacitacion a buscar
     * @return 
     */
    public List<ResultadoCapacitacion> findAllByResultadoCapacitacionId(Integer Id);
    
    /**
     * * Busca todos los registros de los Resultados de la Capaciracion por el Id, para paginar.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param pageable  Clase de springframework para la realizacion de paginacion
     * @param Id id de la ResultadoCapacitacion a buscar
     * @return 
     */
    public Page<ResultadoCapacitacion> findAllByResultadoCapacitacionId(Pageable pageable,Integer Id);
    
}
