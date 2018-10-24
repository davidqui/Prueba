package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Pregunta;
import com.laamware.ejercito.doc.web.entity.TemaCapacitacion;
import com.laamware.ejercito.doc.web.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laamware.ejercito.doc.web.repo.PreguntaRepository;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.ReflectionException;
import com.laamware.ejercito.doc.web.util.ReflectionUtil;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

/**
 * Servicio para las operaciones para Pregunta.
 * @author jcespedeso@imi.mil.co 
 * @author dquijanor@imi.mil.co 
 * @author dquijanor@imi.mil.co 
 * @since Septiembre 3, 2018 _feature_9 (SICDI-GETDE)
 */
@Service
public class PreguntaService {

    private static final Logger LOG = Logger.getLogger(PreguntaService.class.getName());

    /**
     * Repositorio de Pregunta del Módulo de capacitación de SICDI.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     */
    @Autowired
    private PreguntaRepository preguntaRepository;

    /**
     * Lista todos las Pregunta  disponibles.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @return 
     */
    public List<Pregunta> findAllFull() {
        return preguntaRepository.findAll();
    }

    /**
     * Busca un registro de Pregunta especifico por su Id.
     *2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param id identificador del registro.
     * @return
     */
    public Pregunta findOne(Integer id) {
        return preguntaRepository.getOne(id);
    }
    
    /**
     * Metodo para crear una Pregunta
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param pregunta
     * @param usuario
     * @throws BusinessLogicException
     * @throws ReflectionException 
     */
    public void crearPregunta(Pregunta pregunta, Usuario usuario) throws BusinessLogicException, ReflectionException {
        final String textoPregunta = pregunta.getPregunta();
        final boolean activo = pregunta.getActivo();
        if (textoPregunta == null || textoPregunta.trim().length() == 0) {
            throw new BusinessLogicException("El texto de la pregunta es obligatorio.");
        }

        final int textoPreguntaColumnLength = ReflectionUtil.getColumnLength(Pregunta.class, "pregunta");
        if (textoPregunta.trim().length() > textoPreguntaColumnLength) {
            throw new BusinessLogicException("El texto de la pregunta permite máximo " + textoPreguntaColumnLength + " caracteres.");
        }
        
        Pregunta nombrePregunta = preguntaRepository.findOneByPregunta(textoPregunta.toUpperCase());
        if (nombrePregunta != null) {
            throw new BusinessLogicException("Esta Pregunta ya existe.");
        }
        
        pregunta.setPregunta(textoPregunta.toUpperCase());
//        pregunta.setTemaCapacitacion(pregunta.getTemaCapacitacion());
        pregunta.setActivo(Boolean.TRUE);
        pregunta.setCuando(new Date());
        pregunta.setCuandoMod(new Date());
        pregunta.setQuien(usuario);
        pregunta.setQuienMod(usuario);
        
        preguntaRepository.saveAndFlush(pregunta);
    }
    
    /**
     * Metodo para Editar una Pregunta de un Tema de Capacitacion.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param pregunta variable del objeto Tema de capacitacion
     * @param usuario variable del objeto Usuario en sesión
     * @throws BusinessLogicException
     * @throws ReflectionException 
     */
    public void editarPregunta(Pregunta pregunta, Usuario usuario) throws BusinessLogicException, ReflectionException {

        final String textoPregunta = pregunta.getPregunta();
        if (textoPregunta == null || textoPregunta.trim().length() == 0) {
            throw new BusinessLogicException("El texto de la pregunta es obligatorio.");
        }

        final int textoPreguntaColumnLength = ReflectionUtil.getColumnLength(Pregunta.class, "pregunta");
        if (textoPregunta.trim().length() > textoPreguntaColumnLength) {
            throw new BusinessLogicException("El texto de la pregunta permite máximo " + textoPreguntaColumnLength + " caracteres.");
        }
        
        Pregunta nombrePregunta = preguntaRepository.findOneByPregunta(textoPregunta);
        if (nombrePregunta != null && !nombrePregunta.getId().equals(pregunta.getId())&& nombrePregunta != null) {
            throw new BusinessLogicException("Esta pregunta ya existe.");
        }
        pregunta.setPregunta(textoPregunta.toUpperCase());
        Pregunta nombrePreguntaAnterior = findOne(pregunta.getId());
        pregunta.setActivo(nombrePreguntaAnterior.getActivo());
        pregunta.setCuando(nombrePreguntaAnterior.getCuando());
        pregunta.setCuandoMod(new Date());
        pregunta.setQuien(nombrePreguntaAnterior.getQuien());
        pregunta.setQuienMod(usuario);
        

        preguntaRepository.saveAndFlush(pregunta);
    }

    /**
     * Metodo para eliminar un Pregunta
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param pregunta
     * @param usuario 
     */
    public void eliminarPregunta(Pregunta  pregunta,Usuario usuario) {
        pregunta.setQuienMod(usuario);
        pregunta.setCuandoMod(new Date());
        pregunta.setActivo(Boolean.FALSE);
        preguntaRepository.saveAndFlush(pregunta);
    }
    /**
     * Permite reactivar un registro de recurso multimedia.
     * 
     * 2018-10-24 Issue 25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * 
     * @param pregunta Data de un recurso multimedia especifico.
     * @param usuario id del usuario en sesión
     */
    public void recuperarPregunta(Pregunta pregunta,Usuario usuario) {
        pregunta.setQuienMod(usuario);
        pregunta.setCuandoMod(new Date());
        pregunta.setActivo(Boolean.TRUE);
        preguntaRepository.saveAndFlush(pregunta);
    }

    /**
     * Lista todas las Preguntas activos para paginación.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param pageable
     * @return Objeto Pageable con información de Preguntas activas.
     */
    public Page<Pregunta> findActive(Pageable pageable) {
        return preguntaRepository.getByActivoTrue(pageable);
    }
    
    /**
     * Lista todas las Preguntas activas
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param sort
     * @return Listado de Preguntas activas
     */
    public List<Pregunta> findActive(Sort sort) {
        return preguntaRepository.getByActivoTrue(sort);
    }
    
    /**
     * Lista todos las preguntas
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param sort
     * @return 
     */
    public List<Pregunta> findAll(Sort sort) {
        return preguntaRepository.findAll(sort);
    }
    
    /**
     * Lista todos las Preguntas para paginación.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param pageable
     * @return Objetos Page con la información de las Preguntas.
     */
    public Page<Pregunta> findAll(Pageable pageable) {
        return preguntaRepository.findAll(pageable);
    }
    /**
     * Lista todos los recursos multimedia activos y que pertenezcan a una unica tematica permitiendo su paginación. 
     * 
     * 2018-10-01 Issue 25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * 
     * @param pageable
     * @param id Id de la Tematica por la cual se filtran los datos de registro multimedia.
     * @return Lista de Recursos multimedia Activos y que pertenecen a una unica tematica. 
     */
    public Page<Pregunta> findActiveAndTemaCapacitacionPage(Pageable pageable, Integer id) {
        return preguntaRepository.getByActivoTrueAndTemaCapacitacionId(pageable,id);
    }
    
    /**
     * Lista todos los Recursos Multimedia de una Tematica para su paginación.
     * 
     * 2018-10-01 Issue 25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * 
     * @param pageable
     * @param tematica Id de la tematica por la cual se filtran los datos de recurso multimedia activo.
     * @return Lista de Recursos Multimedia de una unica Tematica.
     */
    public Page<Pregunta> findAllByTemaCapacitacionPage(Pageable pageable, final TemaCapacitacion temaCapacitacion) {
        return preguntaRepository.findAllByTemaCapacitacionId(pageable, temaCapacitacion.getId());
    }
    
    
    
}
