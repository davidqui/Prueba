package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.RecursoMultimedia;
import com.laamware.ejercito.doc.web.entity.Tematica;
import com.laamware.ejercito.doc.web.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laamware.ejercito.doc.web.repo.RecursoMultimediaRepository;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.ReflectionException;
import com.laamware.ejercito.doc.web.util.ReflectionUtil;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * Repositorio del Sevicio de indexación de archivos de SICDI.
     */
    @Autowired
    private OFS ofs;
    
    
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
     * 2018-09-06 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 
     * @param recursoMultimedia datos de la entidad.
     * @param usuario id usuario en sesion.
     * @param files informacion del archivo.
     * @throws BusinessLogicException
     * @throws ReflectionException 
     */
    public void crearRecursoMultimedia(RecursoMultimedia recursoMultimedia, Usuario usuario, MultipartFile files) throws BusinessLogicException, ReflectionException{
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
        recursoMultimedia.setNombreArchivoOriginal(files.getOriginalFilename());
        
        try {
            final String ofsFileID = ofs.saveAllFile(files);
            recursoMultimedia.setUbicacion(ofsFileID);
            recursoMultimedia.setNombreArchivoFinal(files.getOriginalFilename().replace(files.getOriginalFilename(), ofsFileID));
        } catch (IOException ex) {
            Logger.getLogger(RecursoMultimediaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        recursoMultimedia.setActivo(Boolean.TRUE);
        recursoMultimedia.setCuando(new Date());
        recursoMultimedia.setCuandoMod(new Date());
        recursoMultimedia.setQuien(usuario);
        recursoMultimedia.setQuienMod(usuario);
        
        recursoMultimediaRepository.saveAndFlush(recursoMultimedia);
    }

    /**
     * Recupera la data del recurso multimedia guardado en disco para su visualización.
     * 
     * 2018-09-13 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 
     * @param recursoMultimedia datos de la entidad
     * @return
     * @throws BusinessLogicException
     * @throws ReflectionException
     * @throws IOException 
     */
    public OFSEntry viewRecursoMultimediaFile(RecursoMultimedia recursoMultimedia) throws BusinessLogicException, ReflectionException, IOException{
       
            RecursoMultimedia recurso= recursoMultimediaRepository.findOne(recursoMultimedia.getId());
            OFSEntry entry=ofs.read(recurso.getUbicacion());
            return entry;
    }
    
    public OFSEntry viewRecursoMultimediaFile(String ubicacion) throws BusinessLogicException, ReflectionException, IOException{
            OFSEntry entry=ofs.read(ubicacion);
            return entry;
    }
    
    
    /**
     * Permite editar un registro de recursos multimedia.
     * 
     * 2018-09-17 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
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
        RecursoMultimedia nombreRecursoAnterior = findOne(recursoMultimedia.getId());
        recursoMultimedia.setPesoOrden(pesoOrden);
        recursoMultimedia.setTematica(nombreRecursoAnterior.getTematica());
        recursoMultimedia.setTipo(nombreRecursoAnterior.getTipo());
        recursoMultimedia.setUbicacion(nombreRecursoAnterior.getUbicacion());
        recursoMultimedia.setQuien(nombreRecursoAnterior.getQuien());
        recursoMultimedia.setCuando(nombreRecursoAnterior.getCuando());
        recursoMultimedia.setQuienMod(usuario);
        recursoMultimedia.setCuandoMod(new Date());
        recursoMultimedia.setActivo(nombreRecursoAnterior.getActivo());
        recursoMultimedia.setNombreArchivoOriginal(nombreRecursoAnterior.getNombreArchivoOriginal());
        recursoMultimedia.setNombreArchivoFinal(nombreRecursoAnterior.getNombreArchivoFinal());
        
        recursoMultimediaRepository.saveAndFlush(recursoMultimedia);
    }
    
    /**
     * Permite eliminar logicamente un registro de recursos multimedia. 
     * 
     * @param recursoMultimedia Data de un recurso multimedia especifico. 
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
     * 2018-09-17 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 
     * @param recursoMultimedia Data de un recurso multimedia especifico.
     * @param usuario id del usuario en sesión
     */
    public void recuperarRecursoMultimedia(RecursoMultimedia recursoMultimedia,Usuario usuario) {
        recursoMultimedia.setQuienMod(usuario);
        recursoMultimedia.setCuandoMod(new Date());
        recursoMultimedia.setActivo(Boolean.TRUE);
        recursoMultimediaRepository.saveAndFlush(recursoMultimedia);
    }
    
    /**
     * Suministra una lista de los ContentType permitidos.
     * 
     *  2018-09-06 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 
     * @return Map con la informacion de los ContentType que se permiten cargar.
     */
    public Map<String, String> extencionesPermitidas() {
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
     *  2018-09-06 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
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
     *  2018-09-06 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
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
     * @return Lista de recursos multimedia Activo = 1
     */
    public List<RecursoMultimedia> findActive(Sort sort) {
        return recursoMultimediaRepository.getByActivoTrue(sort);
    }
    
    /**
     * Lista todos los recursos multimedia activos y que pertenezcan a una unica tematica. 
     * 
     * 2018-09-17 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 
     * @param sort Ordenamiento.
     * @param id Id de la Tematica por la cual se filtran los datos de registro multimedia.
     * @return Lista de Recursos multimedia Activos y que pertenecen a una unica tematica. 
     */
    public List<RecursoMultimedia> findActiveAndTematica(Sort sort, Integer id) {
        return recursoMultimediaRepository.getByActivoTrueAndTematicaId(sort, id);
    }
    
    /**
     * Lista todos los recurso multimedia
     * 
     * @param sort
     * @return Lista de recursos multimedia sin importar su estado. 
     */
    public List<RecursoMultimedia> findAll(Sort sort) {
        return recursoMultimediaRepository.findAll(sort);
    }
    
    /**
     * Lista todos los Recursos Multimedia de una Tematica.
     * 
     * 2018-09-14 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 
     * @param tematica Id de la tematica por la cual se filtran los datos de recurso multimedia activo.
     * @return Lista de Recursos Multimedia de una unica Tematica.
     */
    public List<RecursoMultimedia> findAllByTematica(final Tematica tematica) {
        return recursoMultimediaRepository.findAllByTematicaId(tematica.getId());
    }
    
    /**
     * Lista todos los recursos multimedia activos y que pertenezcan a una unica tematica permitiendo su paginación. 
     * 
     * 2018-10-01 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 
     * @param pageable
     * @param id Id de la Tematica por la cual se filtran los datos de registro multimedia.
     * @return Lista de Recursos multimedia Activos y que pertenecen a una unica tematica. 
     */
    public Page<RecursoMultimedia> findActiveAndTematicaPage(Pageable pageable, Integer id) {
        return recursoMultimediaRepository.getByActivoTrueAndTematicaId(pageable,id);
    }
    
    /**
     * Busca todos los registros activos de Recurso Multimedia por el nombre, para paginar.
     * 
     * 2018-10-01 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 
     * @param pageable
     * @param nombre
     * @return Objeto Page con los datos del resultado.
     */
    public Page<RecursoMultimedia> findAllActiveName(Pageable pageable, String nombre) {
        return recursoMultimediaRepository.findByNombreIgnoreCaseContainingAndActivoTrue(pageable, nombre);
    }
    
    /**
     * Busca todos los registros de Recurso Multimedia por el nombre, para paginar.
     * 
     * 2018-10-01 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 
     * @param pageable
     * @param nombre
     * @return Objeto Page con los datos del resultado.
     */
    public Page<RecursoMultimedia> findAllName(Pageable pageable, String nombre) {
        return recursoMultimediaRepository.findByNombreIgnoreCaseContaining(pageable, nombre);
    }
    
    /**
     * Lista todos los Recursos Multimedia de una Tematica para su paginación.
     * 
     * 2018-10-01 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 
     * @param pageable
     * @param tematica Id de la tematica por la cual se filtran los datos de recurso multimedia activo.
     * @return Lista de Recursos Multimedia de una unica Tematica.
     */
    public Page<RecursoMultimedia> findAllByTematicaPage(Pageable pageable, final Tematica tematica) {
        return recursoMultimediaRepository.findAllByTematicaId(pageable, tematica.getId());
    }
    
   
}
