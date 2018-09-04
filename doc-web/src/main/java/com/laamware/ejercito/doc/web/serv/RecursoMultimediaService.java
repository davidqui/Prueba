package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.RecursoMultimedia;
import com.laamware.ejercito.doc.web.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laamware.ejercito.doc.web.repo.TematicaRepository;
import com.laamware.ejercito.doc.web.repo.RecursoMultimediaRepository;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.ReflectionException;
import com.laamware.ejercito.doc.web.util.ReflectionUtil;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Servicio para las operaciones de los recursos multimedia.
 * @author jcespedeso@imi.mil.co 
 * @author dquijanor@imi.mil.co 
 * @author aherreram@imi.mil.co 
 * @since Septiembre 3, 2018 _feature_9 (SICDI-GETDE)
 */
@Service
public class RecursoMultimediaService {

    private static final Logger LOG = Logger.getLogger(DocumentoObservacionDefectoService.class.getName());

    /**
     * Repositorio de recurso multimedia.
     */
    @Autowired
    private TematicaRepository tematicaRepository;
    private RecursoMultimediaRepository recursoMultimediaRepository;

    /**
     * Lista todos los recursos multimedia disponibles.
     * 
     * @return 
     */
    public List<RecursoMultimedia> findAll() {
        return recursoMultimediaRepository.findAll();
    }

    /**
     * Busca un registro de recurso multimedia por Id.
     *
     * @param id identificador del registro
     * @return
     */
    public RecursoMultimedia findOne(Integer id) {
        return recursoMultimediaRepository.getOne(id);
    }
    
    /**
     * Permite crear un nuevo registro de recurso multimedia e informacion de auditoria.
     * 
     * @param recursoMultimedia
     * @param usuario id del usuario en sesión
     * @throws BusinessLogicException
     * @throws ReflectionException 
     */
    public void crearRecursoMultimedia(RecursoMultimedia recursoMultimedia, Usuario usuario) throws BusinessLogicException, ReflectionException {
        final String nombre = recursoMultimedia.getNombre();
        final String descripcion = recursoMultimedia.getDescripcion();
        final String fuente = recursoMultimedia.getFuente();
        final BigInteger pesoOrden = recursoMultimedia.getPesoOrden();
        
        if (nombre == null || nombre.trim().length() == 0) {
            throw new BusinessLogicException("El texto del nombre es obligatorio.");
        }
        if (descripcion == null || descripcion.trim().length() == 0) {
            throw new BusinessLogicException("El texto de la descripción es obligatorio.");
        }
        if (fuente == null || fuente.trim().length() == 0) {
            throw new BusinessLogicException("El texto de la fuente es obligatorio.");
        }

        final int textoNombreColumnLength = ReflectionUtil.getColumnLength(RecursoMultimedia.class, "nombre");
        if (nombre.trim().length() > textoNombreColumnLength) {
            throw new BusinessLogicException("El texto del nombre permite máximo " + textoNombreColumnLength + " caracteres.");
        }
        final int textoFuenteColumnLength = ReflectionUtil.getColumnLength(RecursoMultimedia.class, "fuente");
        if (fuente.trim().length() > textoFuenteColumnLength) {
            throw new BusinessLogicException("El texto de la fuente permite máximo " + textoFuenteColumnLength + " caracteres.");
        }
        
        RecursoMultimedia nombreRecursoMultimedia = recursoMultimediaRepository.findOneByNombre(nombre);
        if (nombreRecursoMultimedia != null) {
            throw new BusinessLogicException("El nombre del recurso multimedia que desea crear ya existe.");
        }
        
        recursoMultimedia.setNombre(nombre.toUpperCase());
        recursoMultimedia.setDescripcion(descripcion);
        recursoMultimedia.setFuente(fuente);
        recursoMultimedia.setPesoOrden(pesoOrden);
        recursoMultimedia.setPesoOrden(pesoOrden);
        recursoMultimedia.setTipo("validar contenType");
        recursoMultimedia.setUbicacion("validar el directorio donde se guardaria el archivo");
        recursoMultimedia.setActivo(Boolean.TRUE);
        recursoMultimedia.setCuando(new Date());
        recursoMultimedia.setCuandoMod(new Date());
        recursoMultimedia.setQuien(usuario);
        recursoMultimedia.setQuienMod(usuario);
        
        recursoMultimediaRepository.saveAndFlush(recursoMultimedia);
    }
    
    /**
     * Permite editar un registro de recursos multimedia
     * 
     * @param recursoMultimedia
     * @param usuario id del usuario en sesión
     * @throws BusinessLogicException
     * @throws ReflectionException 
     */
    public void editarRecursoMultimedia(RecursoMultimedia recursoMultimedia, Usuario usuario) throws BusinessLogicException, ReflectionException {

        final BigInteger pesoOrden = recursoMultimedia.getPesoOrden();
        final String nombre = recursoMultimedia.getNombre();
        if (nombre == null || nombre.trim().length() == 0) {
            throw new BusinessLogicException("El texto del nombre es obligatorio.");
        }
        final String descripcion = recursoMultimedia.getDescripcion();
        if (descripcion == null || descripcion.trim().length() == 0) {
            throw new BusinessLogicException("El texto de la descripción es obligatorio.");
        }
        final String fuente = recursoMultimedia.getFuente();
        if (fuente == null || fuente.trim().length() == 0) {
            throw new BusinessLogicException("El texto de la fuente es obligatorio.");
        }

        final int textoNombreColumnLength = ReflectionUtil.getColumnLength(RecursoMultimedia.class, "nombre");
        if (nombre.trim().length() > textoNombreColumnLength) {
            throw new BusinessLogicException("El texto del nombre permite máximo " + textoNombreColumnLength + " caracteres.");
        }
        final int textoFuenteColumnLength = ReflectionUtil.getColumnLength(RecursoMultimedia.class, "fuente");
        if (fuente.trim().length() > textoFuenteColumnLength) {
            throw new BusinessLogicException("El texto de la fuente permite máximo " + textoFuenteColumnLength + " caracteres.");
        }
        
        RecursoMultimedia recursoMultimediaNombre = recursoMultimediaRepository.findOneByNombre(nombre);
        if (recursoMultimediaNombre != null && !recursoMultimediaNombre.getId().equals(recursoMultimedia.getId())&& recursoMultimediaNombre != null) {
            throw new BusinessLogicException("Este nombre ya existe.");
        }
        recursoMultimedia.setNombre(nombre.toUpperCase());
        recursoMultimedia.setDescripcion(descripcion.toUpperCase());
        recursoMultimedia.setFuente(fuente.toUpperCase());
        recursoMultimedia.setPesoOrden(pesoOrden);
        RecursoMultimedia nombreRecursoAnterior = findOne(recursoMultimedia.getId());
        recursoMultimedia.setQuien(nombreRecursoAnterior.getQuien());
        recursoMultimedia.setCuando(nombreRecursoAnterior.getCuando());
        recursoMultimedia.setQuienMod(usuario);
        recursoMultimedia.setCuandoMod(new Date());
        recursoMultimedia.setActivo(nombreRecursoAnterior.getActivo());

        recursoMultimediaRepository.saveAndFlush(recursoMultimedia);
    }
    
    /**
     * Permite eliminar logicamente un registro de recursos multimedia. 
     * 
     * @param recursoMultimedia
     * @param usuario id del usuario en sesión
     */
    public void eliminarRecursoMultimedia(RecursoMultimedia recursoMultimedia,Usuario usuario) {
        recursoMultimedia.setQuienMod(usuario);
        recursoMultimedia.setCuandoMod(new Date());
        recursoMultimedia.setActivo(Boolean.FALSE);
        recursoMultimediaRepository.saveAndFlush(recursoMultimedia);
    }
    
    /**
     * Permite reactivar un registro de recurso multimedia.
     * 
     * @param recursoMultimedia
     * @param usuario id del usuario en sesión
     */
    public void recuperarRecursoMultimedia(RecursoMultimedia recursoMultimedia,Usuario usuario) {
        recursoMultimedia.setQuienMod(usuario);
        recursoMultimedia.setCuandoMod(new Date());
        recursoMultimedia.setActivo(Boolean.TRUE);
        recursoMultimediaRepository.saveAndFlush(recursoMultimedia);
    }

}
