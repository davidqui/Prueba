package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Clasificacion;
import com.laamware.ejercito.doc.web.entity.Pregunta;
import com.laamware.ejercito.doc.web.entity.TemaCapacitacion;
import com.laamware.ejercito.doc.web.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laamware.ejercito.doc.web.repo.TemaCapacitacionRepository;
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
 * Servicio para las operaciones para Tema de Capacitación.
 *
 * @author jcespedeso@imi.mil.co
 * @author dquijanor@imi.mil.co
 * @author aherreram@imi.mil.co
 * @since Septiembre 3, 2018 _feature_9 (SICDI-GETDE)
 */
@Service
public class TemaCapacitacionService {

    private static final Logger LOG = Logger.getLogger(TemaCapacitacionService.class.getName());

    /**
     * Repositorio de Tema de Capacitación educativas SICDI. 2018-10-17 Issue
     * #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     */
    @Autowired
    private TemaCapacitacionRepository temaCapacitacionRepository;
    /**
     * Repositorio de Pregunta de Capacitación educativas SICDI. 2018-10-17 Issue
     * #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     */
    @Autowired
    private PreguntaService preguntaService;

    /**
     * Lista todos las Tema de Capacitación educativas disponibles. 2018-10-17
     * Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     *
     * @return
     */
    public List<TemaCapacitacion> findAllFull() {
        return temaCapacitacionRepository.findAll();
    }

    /**
     * Busca un registro de Tema de Capacitación especifico por su Id.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     *
     * @param id identificador del registro.
     * @return
     */
    public TemaCapacitacion findOne(Integer id) {
        return temaCapacitacionRepository.getOne(id);
    }

    /**
     * Metodo para crear un Tema de Capacitación 2018-10-17 Issue #25
     * SICDI-GETDE feature-25 dquijanor@imi.mil.co
     *
     * @param temaCapacitacion
     * @param usuario
     * @throws BusinessLogicException
     * @throws ReflectionException
     */
    public void crearTemaCapacitacion(TemaCapacitacion temaCapacitacion, Usuario usuario) throws BusinessLogicException, ReflectionException {
        final String tema = temaCapacitacion.getTema();
        final Clasificacion clasificacion = temaCapacitacion.getClasificacion();
        
        if (tema == null || tema.trim().length() == 0) {
            throw new BusinessLogicException("El texto del tema es obligatorio.");
            
        }

        final int textoTemaColumnLength = ReflectionUtil.getColumnLength(TemaCapacitacion.class, "tema");
        if (tema.trim().length() > textoTemaColumnLength) {
            throw new BusinessLogicException("El texto del tema permite máximo " + textoTemaColumnLength + " caracteres.");
        }

        List<TemaCapacitacion> nombreTema = temaCapacitacionRepository.findAllByTemaAndActivoTrue(tema);
        
        for (int i = 0; i < nombreTema.size() ; i++) {
           if (nombreTema != null &&nombreTema.get(i).getClasificacion().equals(clasificacion)) {
            throw new BusinessLogicException("Este Tema de Capacitacion ya existe.");
        } 
        }

        temaCapacitacion.setTema(tema.toUpperCase());
        temaCapacitacion.setClasificacion(clasificacion);
        temaCapacitacion.setActivo(Boolean.TRUE);
        temaCapacitacion.setCuando(new Date());
        temaCapacitacion.setCuandoMod(new Date());
        temaCapacitacion.setQuien(usuario);
        temaCapacitacion.setQuienMod(usuario);

        temaCapacitacionRepository.saveAndFlush(temaCapacitacion);
    }

    /**
     * Metodo para Editar un Tema de Capacitacion. 2018-10-17 Issue #25
     * SICDI-GETDE feature-25 dquijanor@imi.mil.co
     *
     * @param temaCapacitacion variable del objeto Tema de capacitacion
     * @param usuario variable del objeto Usuario en sesión
     * @throws BusinessLogicException
     * @throws ReflectionException
     */
    public void editarTemaCapacitacion(TemaCapacitacion temaCapacitacion, Usuario usuario) throws BusinessLogicException, ReflectionException {

        final String tema = temaCapacitacion.getTema();
        final Clasificacion clasificacion = temaCapacitacion.getClasificacion();
        if (tema == null || tema.trim().length() == 0) {
            throw new BusinessLogicException("El texto del tema es obligatorio.");
        }

        final int textoTemaColumnLength = ReflectionUtil.getColumnLength(TemaCapacitacion.class, "tema");
        if (tema.trim().length() > textoTemaColumnLength) {
            throw new BusinessLogicException("El texto del tema permite máximo " + textoTemaColumnLength + " caracteres.");
        }

//        TemaCapacitacion nombreTema = temaCapacitacionRepository.findOneByTema(tema);
//        if (nombreTema != null && nombreTema.getClasificacion().getId().equals(nombreTema.getClasificacion().getId())) {
//            throw new BusinessLogicException("Este tema de Capacitacion ya existe.");
//        }
        temaCapacitacion.setTema(tema.toUpperCase());
        temaCapacitacion.setClasificacion(clasificacion);
        TemaCapacitacion nombreTemaCapacitacionAnterior = findOne(temaCapacitacion.getId());

        temaCapacitacion.setQuien(nombreTemaCapacitacionAnterior.getQuien());
        temaCapacitacion.setCuando(nombreTemaCapacitacionAnterior.getCuando());
        temaCapacitacion.setQuienMod(usuario);
        temaCapacitacion.setCuandoMod(new Date());
        temaCapacitacion.setActivo(nombreTemaCapacitacionAnterior.getActivo());

        temaCapacitacionRepository.saveAndFlush(temaCapacitacion);
    }

    /**
     * Metodo para eliminar un Tema de Capacitación 2018-10-17 Issue #25
     * SICDI-GETDE feature-25 dquijanor@imi.mil.co
     *
     * @param temaCapacitacion
     * @param usuario
     */
    public void eliminarTemaCapacitacion(TemaCapacitacion temaCapacitacion, Usuario usuario) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cuando"));
        temaCapacitacion.setQuienMod(usuario);
        temaCapacitacion.setCuandoMod(new Date());
        temaCapacitacion.setActivo(Boolean.FALSE);
        temaCapacitacionRepository.saveAndFlush(temaCapacitacion);
        List<Pregunta> preguntas = preguntaService.findActiveAndTemaCapacitacion(sort, temaCapacitacion.getId());
        for (int i = 0; i < preguntas.size(); i++) {
            preguntaService.eliminarPregunta(preguntas.get(i), usuario);
        }
    }


/**
 * Metodo para recuperar un Tema de Capacitación el cual fue eliminado.
 * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
 *
 * @param temaCapacitacion
 * @param usuario
 */
public void recuperarPregunta(TemaCapacitacion temaCapacitacion,Usuario usuario) {
        temaCapacitacion.setQuienMod(usuario);
        temaCapacitacion.setCuandoMod(new Date());
        temaCapacitacion.setActivo(Boolean.TRUE);
        temaCapacitacionRepository.saveAndFlush(temaCapacitacion);
    }

    /**
     * Lista todas las TemaCapacitacions activos para paginación.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param pageable
     * @return Objeto Pageable con información de TemaCapacitacions activas.
     */
    public Page<TemaCapacitacion> findActive(Pageable pageable) {
        return temaCapacitacionRepository.getByActivoTrue(pageable);
    }
    
    /**
     * Lista todas las TemaCapacitacions activos
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param sort
     * @return Listado de TemaCapacitacions activas
     */
    public List<TemaCapacitacion> findActive(Sort sort) {
        return temaCapacitacionRepository.getByActivoTrue(sort);
    }
    
    /**
     * Lista todos los temaCapacitacions
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param sort
     * @return 
     */
    public List<TemaCapacitacion> findAll(Sort sort) {
        return temaCapacitacionRepository.findAll(sort);
    }
    
    /**
     * Lista todos las temaCapacitacions para paginación.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param pageable
     * @return Objetos Page con la información de las TemaCapacitacions.
     */
    public Page<TemaCapacitacion> findAll(Pageable pageable) {
        return temaCapacitacionRepository.findAll(pageable);
    }
}
