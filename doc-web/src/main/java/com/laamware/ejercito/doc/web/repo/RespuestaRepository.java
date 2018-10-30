package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Respuesta;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RespuestaRepository extends PagingAndSortingRepository<Respuesta, Integer>, JpaRepository<Respuesta, Integer> {
    
    Respuesta findOneByTextoRespuesta(String textoRespuesta);

    public List<Respuesta> getByActivoTrue(Sort sort);
    public Page<Respuesta> getByActivoTrue(Pageable pageable);

    Respuesta findOneByTextoRespuestaAndActivoTrue(String textoRespuesta);
    
    public Page<Respuesta> findByTextoRespuestaIgnoreCaseContaining(Pageable pageable, String textoRespuesta);
    
    public Page<Respuesta> findByTextoRespuestaIgnoreCaseContainingAndActivoTrue(Pageable pageable, String textoRespuesta);

    public List<Respuesta> getByActivoTrueAndPreguntaId(Sort sort, Integer Id);
    public Page<Respuesta> getByActivoTrueAndPreguntaId(Pageable pageable,Integer Id);
    
    /**
     * Busca todos los registros de las  Respuestas por el Id.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param Id id de la Respuesta a buscar
     * @return 
     */
     
    public List<Respuesta> findAllByPreguntaId(Integer Id);
    
    /**
     * * Busca todos los registros de las preguntas en el Tema de Respuesta por el Id, para paginar.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param pageable  Clase de springframework para la realizacion de paginacion
     * @param Id id de la Respuesta a buscar
     * @return 
     */
    public Page<Respuesta> findAllByPreguntaId(Pageable pageable,Integer Id);
    
    
    
}
