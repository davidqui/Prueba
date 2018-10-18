package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Pregunta;
import com.laamware.ejercito.doc.web.entity.ResultadoCapacitacion;
import com.laamware.ejercito.doc.web.entity.ResultadoCapacitacion;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.CapacitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laamware.ejercito.doc.web.repo.ResultadoCapacitacionRepository;
import com.laamware.ejercito.doc.web.repo.ResultadoCapacitacionRepository;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.ReflectionException;
import com.laamware.ejercito.doc.web.util.ReflectionUtil;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Servicio para las operaciones para ResultadoCapacitacion.
 * @author jcespedeso@imi.mil.co 
 * @author dquijanor@imi.mil.co 
 * @author aherreram@imi.mil.co 
 * @since Septiembre 3, 2018 _feature_9 (SICDI-GETDE)
 */
@Service
public class ResultadoCapacitacionService {

    private static final Logger LOG = Logger.getLogger(ResultadoCapacitacionService.class.getName());

    /**
     * Repositorio de ResultadoCapacitacion del Módulo de capacitación de SICDI.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     */
    @Autowired
    private ResultadoCapacitacionRepository resultadoCapacitacionRepository;

    /**
     * Lista todos las ResultadoCapacitacions  disponibles.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @return 
     */
    public List<ResultadoCapacitacion> findAllFull() {
        return resultadoCapacitacionRepository.findAll();
    }

    /**
     * Busca un registro de ResultadoCapacitacion especifico por su Id.
     *2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param id identificador del registro.
     * @return
     */
    public ResultadoCapacitacion findOne(Integer id) {
        return resultadoCapacitacionRepository.getOne(id);
    }
    
    /**
     * Metodo para crear una ResultadoCapacitacion
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param resultadoCapacitacion
     * @param usuario
     * @throws BusinessLogicException
     * @throws ReflectionException 
     */
    public void crearResultadoCapacitacion(ResultadoCapacitacion resultadoCapacitacion, Usuario usuario) throws BusinessLogicException, ReflectionException {
        final int capacitacion = resultadoCapacitacion.getCapacitacion();
        final Integer pregunta = resultadoCapacitacion.getPregunta();
        final Boolean correcta = resultadoCapacitacion.getCorrecta();
        
        
        resultadoCapacitacion.setCapacitacion(capacitacion);
        resultadoCapacitacion.setPregunta(pregunta);
        resultadoCapacitacion.setCorrecta(Boolean.TRUE);
        
        resultadoCapacitacionRepository.saveAndFlush(resultadoCapacitacion);
    }
    
    /**
     * Metodo para Editar una ResultadoCapacitacion de un Tema de Capacitacion.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param resultadoCapacitacion
     * @param usuario
     * @throws BusinessLogicException
     * @throws ReflectionException 
     */
    public void editarResultadoCapacitacion(ResultadoCapacitacion resultadoCapacitacion, Usuario usuario) throws BusinessLogicException, ReflectionException {

        final int capacitacion = resultadoCapacitacion.getCapacitacion();
        final Integer pregunta = resultadoCapacitacion.getPregunta();
        final Boolean correcta = resultadoCapacitacion.getCorrecta();
        
        ResultadoCapacitacion nombreResultadoCapacitacionAnterior = findOne(resultadoCapacitacion.getId());
        resultadoCapacitacion.setCapacitacion(nombreResultadoCapacitacionAnterior.getCapacitacion());
        resultadoCapacitacion.setPregunta(nombreResultadoCapacitacionAnterior.getPregunta());
        resultadoCapacitacion.setCorrecta(nombreResultadoCapacitacionAnterior.getCorrecta());
        
        resultadoCapacitacionRepository.saveAndFlush(resultadoCapacitacion);
    }

    /**
     * Metodo para eliminar un ResultadoCapacitacion
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param resultadoCapacitacion
     * @param usuario 
     */
    public void eliminarResultadoCapacitacion(ResultadoCapacitacion  resultadoCapacitacion,Usuario usuario) {
        resultadoCapacitacionRepository.saveAndFlush(resultadoCapacitacion);
    }

    /**
     * Lista todas las ResultadoCapacitacions activos para paginación.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param pageable
     * @return Objeto Pageable con información de ResultadoCapacitacions activas.
     */
    public Page<ResultadoCapacitacion> findActive(Pageable pageable) {
        return resultadoCapacitacionRepository.getByActivoTrue(pageable);
    }
    
    /**
     * Lista todas las ResultadoCapacitacions activas
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param sort
     * @return Listado de ResultadoCapacitacions activas
     */
    public List<ResultadoCapacitacion> findActive(Sort sort) {
        return resultadoCapacitacionRepository.getByActivoTrue(sort);
    }
    
    /**
     * Lista todos las preguntas
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param sort
     * @return 
     */
    public List<ResultadoCapacitacion> findAll(Sort sort) {
        return resultadoCapacitacionRepository.findAll(sort);
    }
    
    /**
     * Lista todos las ResultadoCapacitacions para paginación.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param pageable
     * @return Objetos Page con la información de las ResultadoCapacitacions.
     */
    public Page<ResultadoCapacitacion> findAll(Pageable pageable) {
        return resultadoCapacitacionRepository.findAll(pageable);
    }
}
