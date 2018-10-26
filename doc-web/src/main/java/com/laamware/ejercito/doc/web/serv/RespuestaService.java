package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Pregunta;
import com.laamware.ejercito.doc.web.entity.Respuesta;
import com.laamware.ejercito.doc.web.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laamware.ejercito.doc.web.repo.RespuestaRepository;
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
 * Servicio para las operaciones para Respuesta.
 * @author jcespedeso@imi.mil.co 
 * @author dquijanor@imi.mil.co 
 * @author aherreram@imi.mil.co 
 * @since Septiembre 3, 2018 _feature_9 (SICDI-GETDE)
 */
@Service
public class RespuestaService {

    private static final Logger LOG = Logger.getLogger(RespuestaService.class.getName());

    /**
     * Repositorio de Respuesta del Módulo de capacitación de SICDI.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     */
    @Autowired
    private RespuestaRepository respuestaRepository;

    /**
     * Lista todos las Respuestas  disponibles.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @return 
     */
    public List<Respuesta> findAllFull() {
        return respuestaRepository.findAll();
    }

    /**
     * Busca un registro de Respuesta especifico por su Id.
     *2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param id identificador del registro.
     * @return
     */
    public Respuesta findOne(Integer id) {
        return respuestaRepository.getOne(id);
    }
    
    /**
     * Metodo para crear una Respuesta
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param respuesta
     * @param usuario
     * @throws BusinessLogicException
     * @throws ReflectionException 
     */
    public void crearRespuesta(Respuesta respuesta, Usuario usuario) throws BusinessLogicException, ReflectionException {
        final String textoRespuesta = respuesta.getTextoRespuesta();
        final boolean activo = respuesta.getActivo();
        final boolean correcta = respuesta.getCorrecta();
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<pasando por el Metodo del Servicio Crear>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> = " + correcta);
        if (textoRespuesta == null || textoRespuesta.trim().length() == 0) {
            throw new BusinessLogicException("El texto de la respuesta es obligatorio.");
        }

        final int textoRespuestaColumnLength = ReflectionUtil.getColumnLength(Respuesta.class, "respuesta");
        if (textoRespuesta.trim().length() > textoRespuestaColumnLength) {
            throw new BusinessLogicException("El texto de la respuesta permite máximo " + textoRespuestaColumnLength + " caracteres.");
        }
        
        Respuesta nombreRespuesta = respuestaRepository.findOneByTextoRespuesta(textoRespuesta);
        if (nombreRespuesta != null) {
            throw new BusinessLogicException("Esta Respuesta ya existe.");
        }
        
//        respuesta.setPregunta(pregunta);
        respuesta.setTextoRespuesta(textoRespuesta.toUpperCase());
        respuesta.setCorrecta(Boolean.FALSE);
        respuesta.setActivo(Boolean.TRUE);
        
        respuesta.setCuando(new Date());
        respuesta.setCuandoMod(new Date());
        respuesta.setQuien(usuario);
        respuesta.setQuienMod(usuario);
        
        respuestaRepository.saveAndFlush(respuesta);
    }
    
    /**
     * Metodo para Editar una Respuesta de un Tema de Capacitacion.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param respuesta variable del objeto Tema de capacitacion
     * @param usuario variable del objeto Usuario en sesión
     * @throws BusinessLogicException
     * @throws ReflectionException 
     */
    public void editarRespuesta(Respuesta respuesta, Usuario usuario) throws BusinessLogicException, ReflectionException {

        final String textoRespuesta = respuesta.getTextoRespuesta();
        final Pregunta pregunta = respuesta.getPregunta();
        if (textoRespuesta == null || textoRespuesta.trim().length() == 0) {
            throw new BusinessLogicException("El texto de la respuesta es obligatorio.");
        }

        final int textoRespuestaColumnLength = ReflectionUtil.getColumnLength(Respuesta.class, "respuesta");
        if (textoRespuesta.trim().length() > textoRespuestaColumnLength) {
            throw new BusinessLogicException("El texto de la respuesta permite máximo " + textoRespuestaColumnLength + " caracteres.");
        }
        
        Respuesta nombreRespuesta = respuestaRepository.findOneByTextoRespuesta(textoRespuesta);
        if (nombreRespuesta != null && !nombreRespuesta.getId().equals(respuesta.getId())&& nombreRespuesta != null) {
            throw new BusinessLogicException("Esta pregunta ya existe.");
        }
        respuesta.setPregunta(pregunta);
        respuesta.setTextoRespuesta(textoRespuesta.toUpperCase());
        Respuesta nombreRespuestaAnterior = findOne(respuesta.getId());
        respuesta.setCorrecta(nombreRespuestaAnterior.getCorrecta());
        respuesta.setActivo(nombreRespuestaAnterior.getActivo());
        respuesta.setCuando(nombreRespuestaAnterior.getCuando());
        respuesta.setCuandoMod(new Date());
        respuesta.setQuienMod(usuario);
        respuesta.setQuien(nombreRespuestaAnterior.getQuien());
        
        respuestaRepository.saveAndFlush(respuesta);
    }

    /**
     * Metodo para eliminar un Respuesta
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param respuesta
     * @param usuario 
     */
    public void eliminarRespuesta(Respuesta  respuesta,Usuario usuario) {
        respuesta.setQuienMod(usuario);
        respuesta.setCuandoMod(new Date());
        respuesta.setActivo(Boolean.FALSE);
        respuestaRepository.saveAndFlush(respuesta);
    }
    
    
    /**
     * Permite reactivar un registro de recurso multimedia.
     * 
     * 2018-10-24 Issue 25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * 
     * @param pregunta Data de un recurso multimedia especifico.
     * @param usuario id del usuario en sesión
     */
    public void recuperarRespuesta(Respuesta respuesta,Usuario usuario) {
        respuesta.setQuienMod(usuario);
        respuesta.setCuandoMod(new Date());
        respuesta.setActivo(Boolean.TRUE);
        respuestaRepository.saveAndFlush(respuesta);
    }

    /**
     * Lista todas las Respuestas activos para paginación.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param pageable
     * @return Objeto Pageable con información de Respuestas activas.
     */
    public Page<Respuesta> findActive(Pageable pageable) {
        return respuestaRepository.getByActivoTrue(pageable);
    }
    
    /**
     * Lista todas las Respuestas activas
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param sort
     * @return Listado de Respuestas activas
     */
    public List<Respuesta> findActive(Sort sort) {
        return respuestaRepository.getByActivoTrue(sort);
    }
    
    /**
     * Lista todos las preguntas
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param sort
     * @return 
     */
    public List<Respuesta> findAll(Sort sort) {
        return respuestaRepository.findAll(sort);
    }
    
    /**
     * Lista todos las Respuestas para paginación.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param pageable
     * @return Objetos Page con la información de las Respuestas.
     */
    public Page<Respuesta> findAll(Pageable pageable) {
        return respuestaRepository.findAll(pageable);
    }
}
