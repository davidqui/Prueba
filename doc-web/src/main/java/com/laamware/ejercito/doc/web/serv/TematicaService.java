package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Tematica;
import com.laamware.ejercito.doc.web.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laamware.ejercito.doc.web.repo.TematicaRepository;
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
 * Servicio para las operaciones para tematicas.
 * @author jcespedeso@imi.mil.co 
 * @author dquijanor@imi.mil.co 
 * @author aherreram@imi.mil.co 
 * @since Septiembre 3, 2018 _feature_9 (SICDI-GETDE)
 */
@Service
public class TematicaService {

    private static final Logger LOG = Logger.getLogger(DocumentoObservacionDefectoService.class.getName());

    /**
     * Repositorio de tematicas educativas SICDI.
     */
    @Autowired
    private TematicaRepository tematicaRepository;

    /**
     * Lista todos las tematicas educativas disponibles.
     * 
     * @return 
     */
    public List<Tematica> findAllFull() {
        return tematicaRepository.findAll();
    }

    /**
     * Busca un registro de tematica especifico por su Id.
     *
     * @param id identificador del registro.
     * @return
     */
    public Tematica findOne(Integer id) {
        return tematicaRepository.getOne(id);
    }
    
    /**
     * Permite crear un nuevo registo de tematica e informacion de auditoria.
     * 
     * @param tematica
     * @param usuario id del usuario en sesión
     * @throws BusinessLogicException
     * @throws ReflectionException 
     */
    public void crearTematica(Tematica tematica, Usuario usuario) throws BusinessLogicException, ReflectionException {
        final String nombre = tematica.getNombre();
        final String descripcion = tematica.getDescripcion();
        if (nombre == null || nombre.trim().length() == 0) {
            throw new BusinessLogicException("El texto del nombre es obligatorio.");
        }

        final int textoNombreColumnLength = ReflectionUtil.getColumnLength(Tematica.class, "nombre");
        if (nombre.trim().length() > textoNombreColumnLength) {
            throw new BusinessLogicException("El texto del nombre permite máximo " + textoNombreColumnLength + " caracteres.");
        }
        
        Tematica nombreTematica = tematicaRepository.findOneByNombre(nombre);
        if (nombreTematica != null) {
            throw new BusinessLogicException("Este nombre ya existe.");
        }
        
        tematica.setNombre(nombre.toUpperCase());
        tematica.setDescripcion(descripcion.toUpperCase());
        tematica.setActivo(Boolean.TRUE);
        tematica.setCuando(new Date());
        tematica.setCuandoMod(new Date());
        tematica.setQuien(usuario);
        tematica.setQuienMod(usuario);
        
        tematicaRepository.saveAndFlush(tematica);
    }
    
    /**
     * Permite editar un registro de tematica.
     * 
     * @param tematica
     * @param usuario id del usuario en sesión
     * @throws BusinessLogicException
     * @throws ReflectionException 
     */
    public void editarTematica(Tematica tematica, Usuario usuario) throws BusinessLogicException, ReflectionException {

        final String nombre = tematica.getNombre();
        if (nombre == null || nombre.trim().length() == 0) {
            throw new BusinessLogicException("El texto del nombre es obligatorio.");
        }
        final String descripcion = tematica.getDescripcion();
        if (descripcion == null || descripcion.trim().length() == 0) {
            throw new BusinessLogicException("El texto de descripcion es obligatorio.");
        }

        final int textoNombreColumnLength = ReflectionUtil.getColumnLength(Tematica.class, "nombre");
        if (nombre.trim().length() > textoNombreColumnLength) {
            throw new BusinessLogicException("El texto del nombre permite máximo " + textoNombreColumnLength + " caracteres.");
        }
        
        Tematica tematicaNombre = tematicaRepository.findOneByNombre(nombre);
        if (tematicaNombre != null && !tematicaNombre.getId().equals(tematica.getId())&& tematicaNombre != null) {
            throw new BusinessLogicException("Este nombre ya existe.");
        }
        tematica.setNombre(nombre.toUpperCase());
        tematica.setDescripcion(descripcion.toUpperCase());
        Tematica nombreTematicaAnterior = findOne(tematica.getId());
        tematica.setQuien(nombreTematicaAnterior.getQuien());
        tematica.setCuando(nombreTematicaAnterior.getCuando());
        tematica.setQuienMod(usuario);
        tematica.setCuandoMod(new Date());
        tematica.setActivo(nombreTematicaAnterior.getActivo());

        tematicaRepository.saveAndFlush(tematica);
    }

    /**
     * Permite eliminar logicamente un registro de recursos multimedia. 
     * 
     * @param tematica
     * @param usuario id del usuario en sesión
     */
    public void eliminarTematica(Tematica tematica,Usuario usuario) {
        tematica.setQuienMod(usuario);
        tematica.setCuandoMod(new Date());
        tematica.setActivo(Boolean.FALSE);
        tematicaRepository.saveAndFlush(tematica);
    }

    /**
     * Lista todas las Tematicas activos para paginación.
     * 
     * @param pageable
     * @return Objeto Pageable con información de Tematicas activas.
     */
    public Page<Tematica> findActive(Pageable pageable) {
        return tematicaRepository.getByActivoTrue(pageable);
    }
    
    /**
     * Lista todas las Tematicas activos
     * 
     * @param sort
     * @return Listado de Tematicas activas
     */
    public List<Tematica> findActive(Sort sort) {
        return tematicaRepository.getByActivoTrue(sort);
    }
    
    /**
     * Lista todos las tematicas
     * 
     * @param sort
     * @return 
     */
    public List<Tematica> findAll(Sort sort) {
        return tematicaRepository.findAll(sort);
    }
    
    /**
     * Lista todos las tematicas para paginación.
     * 
     * @param pageable
     * @return Objetos Page con la información de las Tematicas.
     */
    public Page<Tematica> findAll(Pageable pageable) {
        return tematicaRepository.findAll(pageable);
    }
}
