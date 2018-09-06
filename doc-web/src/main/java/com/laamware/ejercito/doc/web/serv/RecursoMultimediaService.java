package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.RecursoMultimedia;
import com.laamware.ejercito.doc.web.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laamware.ejercito.doc.web.repo.RecursoMultimediaRepository;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.ReflectionException;
import com.laamware.ejercito.doc.web.util.ReflectionUtil;
import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

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
    private RecursoMultimediaRepository recursoMultimediaRepository;

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
     * @param recursoMultimedia datos de la entidad.
     * @param usuario id usuario en sesion.
     * @param directorioRoot
     * @param subDirectorioTematica
     * @param files informacion del archivo.
     * @throws BusinessLogicException
     * @throws ReflectionException 
     */
    public void crearRecursoMultimedia(RecursoMultimedia recursoMultimedia, Usuario usuario, String directorioRoot, String subDirectorioTematica, MultipartFile files) throws BusinessLogicException, ReflectionException {
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
        
        RecursoMultimedia nombreRecursoMultimedia = recursoMultimediaRepository.findOneByNombreAndActivoTrue(nombre);
        if (nombreRecursoMultimedia != null) {
            throw new BusinessLogicException("El nombre del recurso multimedia que desea crear ya existe.");
        }
        if (!isValidoContentType(files)|| files.getSize()>100000000) {
             throw new BusinessLogicException("Señor usuario solo se permite la carga de archivos en formato PDF, AVI, MP4 y MPEG o de maximo 100MB");
        }
        
        recursoMultimedia.setNombre(nombre.toUpperCase());
        recursoMultimedia.setDescripcion(descripcion.toUpperCase());
        recursoMultimedia.setFuente(fuente.toUpperCase());
        recursoMultimedia.setPesoOrden(pesoOrden);
        recursoMultimedia.setTipo(files.getContentType());
        recursoMultimedia.setUbicacion(directorioRoot + File.separator + subDirectorioTematica);
        recursoMultimedia.setActivo(Boolean.TRUE);
        recursoMultimedia.setCuando(new Date());
        recursoMultimedia.setCuandoMod(new Date());
        recursoMultimedia.setQuien(usuario);
        recursoMultimedia.setQuienMod(usuario);
        
        guardarArchivo(files, directorioRoot, subDirectorioTematica);
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
        
        RecursoMultimedia recursoMultimediaNombre = recursoMultimediaRepository.findOneByNombreAndActivoTrue(nombre);
        if (recursoMultimediaNombre != null && !recursoMultimediaNombre.getId().equals(recursoMultimedia.getId())&& recursoMultimediaNombre != null) {
            throw new BusinessLogicException("Este nombre ya existe.");
        }
        recursoMultimedia.setNombre(nombre.toUpperCase());
        recursoMultimedia.setDescripcion(descripcion.toUpperCase());
        recursoMultimedia.setFuente(fuente.toUpperCase());
        recursoMultimedia.setPesoOrden(pesoOrden);
        RecursoMultimedia nombreRecursoAnterior = findOne(recursoMultimedia.getId());
        recursoMultimedia.setUbicacion(nombreRecursoAnterior.getUbicacion());
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
    
        
    /**
     * Permite agregar un archivo multimedia.
     * 
     * @param file Archivo seleccionado.
     * @param directorioRoot
     * @param subDirectorioTematica 
     */
    public void guardarArchivo(MultipartFile file, String directorioRoot, String subDirectorioTematica){
        /*
        * Numero Aleatorio para renombrar el archivo formato (numeroEvento+numeroAleatorio+extension)
        * El archivo es almacenado en el directorio de destino sin extencion
         */
        int numeroAleatorio = ThreadLocalRandom.current().nextInt(1, 1000);
        String nuevoNombre = subDirectorioTematica + "" + numeroAleatorio;
        byte[] bytes;
        try {
            bytes = file.getBytes();
            Path path = Paths.get(directorioRoot + File.separator + subDirectorioTematica + File.separator
                + file.getOriginalFilename().replace(file.getOriginalFilename(), nuevoNombre+"."+identificarExtencionArchivo(file)));
        Files.write(path, bytes);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(RecursoMultimediaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    /**
     * Suministra una lista de los ContentType permitidos.
     * 
     * @return Map con la informacion de los ContentType que se permiten cargar.
     */
    private Map<String, String> extencionesPermitidas() {
        Map<String, String> listContentType = new HashMap<>();
        listContentType.put("application/pdf", "pdf");
        listContentType.put("video/x-msvideo", "avi");
        listContentType.put("video/mpeg", "mpeg");
        listContentType.put("video/mp4", "mp4");
        
        return listContentType;
    }
    
    /**
     * Metodo que identifica la metadata de la extension de un archivo.
     *
     * @param file Archivo a evaluar.
     * @return Valor que identifica la extencion del archivo que recibio por
     * parametro.
     */
    private String identificarExtencionArchivo(MultipartFile file) {
        return extencionesPermitidas().get(file.getContentType());
    }
    
    /**
     * Permite validar el formato del archivo que se esta cargando.
     *
     * @param contentType Tipo de contenido.
     * @return {@code true} Si el tipo de contenido es válido (PDF, AVI,
     * JPEG, JPG4); de lo contrario {@code false}.
     */
    private boolean isValidoContentType(MultipartFile file) {
        return extencionesPermitidas().containsKey(file.getContentType());
    }
    
    /**
     * Lista todas los recursos activos
     * 
     * @param sort
     * @return 
     */
    public List<RecursoMultimedia> findActive(Sort sort) {
        return recursoMultimediaRepository.getByActivoTrue(sort);
    }
    
    /**
     * Lista todos los recurso
     * 
     * @param sort
     * @return 
     */

    public List<RecursoMultimedia> findAll(Sort sort) {
        return recursoMultimediaRepository.findAll(sort);
    }
}
