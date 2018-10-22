package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Capacitacion;
import com.laamware.ejercito.doc.web.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laamware.ejercito.doc.web.repo.CapacitacionRepository;
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
 * Servicio para las operaciones de las capacitaciones.
 * @author jcespedeso@imi.mil.co 
 * @author dquijanor@imi.mil.co 
 * @author aherreram@imi.mil.co 
 * @since Octubre 17 de 2018 _feature_25 (SICDI-GETDE)
 */
@Service
public class CapacitacionService {

    private static final Logger LOG = Logger.getLogger(CapacitacionService.class.getName());

    /**
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * Repositorio de Capacitacion.
     */
    @Autowired
    private CapacitacionRepository capacitacionRepository;

    /**
     * Repositorio del Sevicio de indexación de archivos de SICDI.
     */
    @Autowired
    private OFS ofs;
    
    
   /**
    * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
    * @param id
    * @return 
    */
    public Capacitacion findOne(Integer id) {
        return capacitacionRepository.getOne(id);
    }
    
   /**
    * Método que crea una CApacitación
    * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
    * @param capacitacion
    * @param usuario
    * @param files
    * @throws BusinessLogicException
    * @throws ReflectionException 
    */
     
    public void crearCapacitacion(Capacitacion capacitacion, Usuario usuario, MultipartFile files) throws BusinessLogicException, ReflectionException{
//        final Integer temaCapacitacion = capacitacion.getTemaCapacitacion();
        final Integer notaObtenida = capacitacion.getNotaObtenida();
        final String resultado = capacitacion.getResultado();
        final String ubicacion = capacitacion.getUbicacionCertificado();
        final BigInteger numCertificado = capacitacion.getNumeroCertificado();
        
        if (resultado == null || resultado.trim().length() == 0) {
            throw new BusinessLogicException("El texto del resultado es obligatorio.");
        }
        if (notaObtenida == null || notaObtenida == 0) {
            throw new BusinessLogicException("El texto de la notaObtenida es obligatorio.");
        }
        if (ubicacion == null || ubicacion.trim().length() == 0) {
            throw new BusinessLogicException("El texto de la ubicacion es obligatorio.");
        }

        final int textoResultadoColumnLength = ReflectionUtil.getColumnLength(Capacitacion.class, "resultado");
        if (resultado.trim().length() > textoResultadoColumnLength) {
            throw new BusinessLogicException("El texto del resultado permite máximo " + textoResultadoColumnLength + " caracteres.");
        }
        final int textoNotaObtenidaColumnLength = ReflectionUtil.getColumnLength(Capacitacion.class, "notaObtenida");
        
        
//        Capacitacion nombreCapacitacion = capacitacionRepository.findOneByResultadoAndActivoTrue(resultado);
//        if (nombreCapacitacion != null) {
//            throw new BusinessLogicException("El resultado de la capacitación que desea crear ya existe.");
//        }
        if (!isValidoContentType(files)|| files.getSize()>100000000) {
             throw new BusinessLogicException("Señor usuario solo se permite la carga de archivos en formato PDF, AVI, MP4 y MPEG o de maximo 100MB");
        }
        
//        capacitacion.setTemaCapacitacion(temaCapacitacion);
        capacitacion.setNotaObtenida(notaObtenida);
        capacitacion.setResultado(resultado.toUpperCase());
        capacitacion.setNumeroCertificado(numCertificado);
        capacitacion.setUbicacionCertificado(ubicacion);
        
        try {
            final String ofsFileID = ofs.saveAllFile(files);
            capacitacion.setUbicacionCertificado(ofsFileID);
//            capacitacion.setNombreArchivoFinal(files.getOriginalFilename().replace(files.getOriginalFilename(), ofsFileID));
        } catch (IOException ex) {
            Logger.getLogger(CapacitacionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        capacitacion.setActivo(Boolean.TRUE);
        capacitacion.setCuando(new Date());
//        capacitacion.setCuandoMod(new Date());
//        capacitacion.setQuien(usuario);
//        capacitacion.setQuienMod(usuario);
        
        capacitacionRepository.saveAndFlush(capacitacion);
    }

    /**
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param capacitacion
     * @return
     * @throws BusinessLogicException
     * @throws ReflectionException
     * @throws IOException 
     */
    public OFSEntry viewCapacitacionFile(Capacitacion capacitacion) throws BusinessLogicException, ReflectionException, IOException{
       
            Capacitacion recurso= capacitacionRepository.findOne(capacitacion.getId());
            OFSEntry entry=ofs.read(recurso.getUbicacionCertificado());
            return entry;
    }
    /**
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param ubicacion
     * @return
     * @throws BusinessLogicException
     * @throws ReflectionException
     * @throws IOException 
     */
    public OFSEntry viewCapacitacionFile(String ubicacion) throws BusinessLogicException, ReflectionException, IOException{
            OFSEntry entry=ofs.read(ubicacion);
            return entry;
    }
    
    
    /**
     * Metodo que Edita Una Capacitación
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param capacitacion
     * @param usuario
     * @throws BusinessLogicException
     * @throws ReflectionException 
     */
    public void editarCapacitacion(Capacitacion capacitacion, Usuario usuario) throws BusinessLogicException, ReflectionException {

        final String resultado = capacitacion.getResultado();
        if (resultado == null || resultado.trim().length() == 0) {
            throw new BusinessLogicException("El texto del resultado es obligatorio.");
        }
        final Integer nota = capacitacion.getNotaObtenida();
        if (nota == null || nota == 0) {
            throw new BusinessLogicException("El texto de la nota es obligatorio.");
        }
        final String ubicaion = capacitacion.getUbicacionCertificado();
        if (ubicaion == null || ubicaion.trim().length() == 0) {
            throw new BusinessLogicException("El texto de la fuente es obligatorio.");
        }

        final int textoResultadoColumnLength = ReflectionUtil.getColumnLength(Capacitacion.class, "resultado");
        if (resultado.trim().length() > textoResultadoColumnLength) {
            throw new BusinessLogicException("El texto del resultado permite máximo " + textoResultadoColumnLength + " caracteres.");
        }
        final int textoNotaColumnLength = ReflectionUtil.getColumnLength(Capacitacion.class, "nota");
        if (ubicaion.trim().length() > textoNotaColumnLength) {
            throw new BusinessLogicException("El texto de la nota permite máximo " + textoNotaColumnLength + " caracteres.");
        }
        
//        Capacitacion NombreCapacitacion = capacitacionRepository.findOneByResultadoAndActivoTrue(resultado);
//        if (NombreCapacitacion != null && !NombreCapacitacion.getId().equals(capacitacion.getId())&& NombreCapacitacion != null) {
//            throw new BusinessLogicException("Este resultado ya existe.");
//        }
        capacitacion.setResultado(resultado.toUpperCase());
        capacitacion.setNotaObtenida(nota);
        capacitacion.setUbicacionCertificado(ubicaion);
        Capacitacion nombreCapacitacionAnterior = findOne(capacitacion.getId());
        capacitacion.setTemaCapacitacion(nombreCapacitacionAnterior.getTemaCapacitacion());
        capacitacion.setUbicacionCertificado(nombreCapacitacionAnterior.getUbicacionCertificado());
        capacitacion.setCuando(nombreCapacitacionAnterior.getCuando());
//        capacitacion.setNombreArchivoOriginal(nombreCapacitacionAnterior.getNombreArchivoOriginal());
//        capacitacion.setNombreArchivoFinal(nombreCapacitacionAnterior.getNombreArchivoFinal());
        
        capacitacionRepository.saveAndFlush(capacitacion);
    }
    
    /**
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param capacitacion
     * @param usuario 
     */
    public void eliminarCapacitacion(Capacitacion capacitacion,Usuario usuario) {
        
        capacitacionRepository.saveAndFlush(capacitacion);
    }
    
    /***/
    public void recuperarCapacitacion(Capacitacion capacitacion,Usuario usuario) {
//        capacitacion.setQuienMod(usuario);
//        capacitacion.setCuandoMod(new Date());
//        capacitacion.setActivo(Boolean.TRUE);
        capacitacionRepository.saveAndFlush(capacitacion);
    }
    
    /**
     * 2018-10-17 Issue #25 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @return 
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
     * 2018-10-17 Issue #25 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param file Archivo a evaluar.
     * @return Valor que identifica la extencion del archivo que recibio por
     * parametro.
     */
    private String identificarExtencionArchivo(MultipartFile file) {
        return extencionesPermitidas().get(file.getContentType());
    }
    
    /**
     * Permite validar el formato del archivo que se esta cargando.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param contentType Tipo de contenido.
     * @return {@code true} Si el tipo de contenido es válido (PDF, AVI,
     * JPEG, JPG4); de lo contrario {@code false}.
     */
    private boolean isValidoContentType(MultipartFile file) {
        return extencionesPermitidas().containsKey(file.getContentType());
    }
    
    /**
     * Lista todas las capacitaciones activos
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param sort
     * @return Lista de Vapacitación Activo = 1
     */
//    public List<Capacitacion> findActive(Sort sort) {
//        return capacitacionRepository.getByActivoTrue(sort);
//    }
    
    /**
     * Lista todas las capacitaciones activas y que pertenezcan a un unico tema de capacitacion. 
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param sort Ordenamiento.
     * @param id Id de la Tematica por la cual se filtran los datos de registro multimedia.
     * @return Lista de capacitación Activas y que pertenecen a una unico tema de capacitación. 
     */
//    public List<Capacitacion> findActiveAndTemaCapacitacion(Sort sort, Integer id) {
//        return capacitacionRepository.getByActivoTrueAndTemaCapacitacionId(sort, id);
//    }
    
    /**
     * Lista todos las capacitaciónes
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param sort
     * @return Lista de capacitaciones sin importar su estado. 
     */
    public List<Capacitacion> findAll(Sort sort) {
        return capacitacionRepository.findAll(sort);
    }
    
    /**
     * Lista todas las capacitaciones de un Tema de Capacitación.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param capacitacion Id de la capacitacion por la cual se filtran los datos de una capacitacion.
     * @return Lista de Recursos Multimedia de una unica Tematica.
     */
//    public List<Capacitacion> findAllByTemaCapacitacion(final Capacitacion capacitacion) {
//        return capacitacionRepository.findAllByTemaCapacitacionId(capacitacion.getId());
//    }
    
//    /**
//     * Lista todas las cpacitaciones activas y que pertenezcan a un unico tema de capacitacion permitiendo su paginación.
//     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
//     * @param pageable
//     * @param id
//     * @return 
//     */
//    public Page<Capacitacion> findActiveAndTemaCapacitacionPage(Pageable pageable, Integer id) {
//        return capacitacionRepository.getByActivoTrueAndTemaCapacitacionId(pageable,id);
//    }
//    
//    /**
//     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
//     * @param pageable
//     * @param nombre
//     * @return 
//     */
//    public Page<Capacitacion> findAllActiveTemaCapacitacion(Pageable pageable, String tema) {
//        return capacitacionRepository.findByResultadoIgnoreCaseContaining(pageable, tema);
//    }
//    
//    /**
//     * Lista todos las Capacitaciones
//     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
//     * @param pageable
//     * @param nombre
//     * @return 
//     */
//    public Page<Capacitacion> findAllTemaCapacitacion(Pageable pageable, String tema) {
//        return capacitacionRepository.findByResultadoIgnoreCaseContaining(pageable, tema);
//    }
//    
    /**
     * Lista todos las Capacitación para paginación.
     * 2018-10-17 Issue #25 SICDI-GETDE feature-25 dquijanor@imi.mil.co
     * @param pageable
     * @return Objetos Page con la información de las Capacitación.
     */
//    public Page<Capacitacion> findAllByTemaCapacitacionPage(Pageable pageable, final Capacitacion capacitacion) {
//        return capacitacionRepository.findAllByTemaCapacitacionId(pageable, capacitacion.getId());
//    }
    
   
}
