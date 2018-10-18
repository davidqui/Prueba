package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Pregunta;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PreguntaRepository extends PagingAndSortingRepository<Pregunta, Integer>, JpaRepository<Pregunta, Integer> {
    
    Pregunta findOneByPregunta(String nombre);

    public List<Pregunta> getByActivoTrue(Sort sort);
    public Page<Pregunta> getByActivoTrue(Pageable pageable);

    Pregunta findOneByPreguntaAndActivoTrue(String nombre);
    public Page<Pregunta> findByPreguntaIgnoreCaseContaining(Pageable pageable, String nombre);
    public Page<Pregunta> findByPreguntaIgnoreCaseContainingAndActivoTrue(Pageable pageable, String nombre);

    public List<Pregunta> getByActivoTrueAndTemaCapacitacionId(Sort sort, Integer Id);
    public Page<Pregunta> getByActivoTrueAndTemaCapacitacionId(Pageable pageable,Integer Id);
    
    /**Busca todos los registros de las preguntas en el Tema de Capacitacion por el Id.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param Id id de la pregunta a buscar
     * @return 
     */
    
    public List<Pregunta> findAllByPreguntaId(Integer Id);
    
    /**
     * Busca todos los registros de las preguntas en el Tema de Capacitacion por el Id, para paginar.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param pageable  Clase de springframework para la realizacion de paginacion
     * @param Id id de la pregunta a buscar
     * @return 
     */
     
    public Page<Pregunta> findAllByPreguntaId(Pageable pageable,Integer Id);
    
}
