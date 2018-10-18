package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Respuesta;
import com.laamware.ejercito.doc.web.entity.ResultadoCapacitacion;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ResultadoCapacitacionRepository extends PagingAndSortingRepository<ResultadoCapacitacion, Integer>, JpaRepository<ResultadoCapacitacion, Integer> {
    
    
    
    /**
     * * Busca todos los registros de los Resultados de la Capaciracion por el Id.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param Id id del Resultado de la capacitacion a buscar
     * @return 
     */
    public List<Respuesta> findAllByResultadoCapacitacionId(Integer Id);
    
    /**
     * * Busca todos los registros de los Resultados de la Capaciracion por el Id, para paginar.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param pageable  Clase de springframework para la realizacion de paginacion
     * @param Id id de la Respuesta a buscar
     * @return 
     */
    public Page<Respuesta> findAllByResultadoCapacitacionId(Pageable pageable,Integer Id);
    
}
