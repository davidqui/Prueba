package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.ParNombreExpediente;
import com.laamware.ejercito.doc.web.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laamware.ejercito.doc.web.repo.ParNombreExpedienteRepository;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.ReflectionException;
import com.laamware.ejercito.doc.web.util.ReflectionUtil;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Servicio para las operaciones de los nombres de los expedientes.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @since Jul 27, 208 Issue #181 (SICDI-Controltech) feature-181
 */
@Service
public class ParNombreExpedienteService {

    private static final Logger LOG = Logger.getLogger(DocumentoObservacionDefectoService.class.getName());

    /**
     * Repositorio de nombre de expedientes.
     */
    @Autowired
    private ParNombreExpedienteRepository parNombreExpedienteRepository;

    /**
     * *
     * Lista todos los Nombres
     *
     * @param sort
     * @return
     */
    public List<ParNombreExpediente> findAll() {
        return parNombreExpedienteRepository.findAll();
    }

    /**
     * *
     * Busca una observacion por defecto por id
     *
     * @param id identificador de la observacion por defecto
     * @return
     */
    public ParNombreExpediente findOne(Long id) {
        return parNombreExpedienteRepository.getOne(id);
    }
    
    public void crearParNombreExpediente(ParNombreExpediente parNombreExpediente, Usuario usuario) throws BusinessLogicException, ReflectionException {
        final String parNombre = parNombreExpediente.getParNombre();
        if (parNombre == null || parNombre.trim().length() == 0) {
            throw new BusinessLogicException("El texto del nombre es obligatorio.");
        }

        final int textoNombreColumnLength = ReflectionUtil.getColumnLength(ParNombreExpediente.class, "parNombre");
        if (parNombre.trim().length() > textoNombreColumnLength) {
            throw new BusinessLogicException("El texto de la observación permite máximo " + textoNombreColumnLength + " caracteres.");
        }
        
        ParNombreExpediente nombreExpediente = parNombreExpedienteRepository.findOneByParNombre(parNombre);
        if (nombreExpediente != null) {
            throw new BusinessLogicException("Este nombre ya existe.");
        }
        
        parNombreExpediente.setParNombre(parNombre.toUpperCase());
        parNombreExpediente.setUsuCreacion(usuario);
        parNombreExpediente.setFecCreacion(new Date());
        parNombreExpediente.setUsuModificacion(usuario);
        parNombreExpediente.setFecModificacion(new Date());

        parNombreExpedienteRepository.saveAndFlush(parNombreExpediente);
    }
    
    public void editarParNombreExpediente(ParNombreExpediente parNombreExpediente, Usuario usuario) throws BusinessLogicException, ReflectionException {

        final String parNombre = parNombreExpediente.getParNombre();
        if (parNombre == null || parNombre.trim().length() == 0) {
            throw new BusinessLogicException("El texto del nombre es obligatorio.");
        }

        final int textoNombreColumnLength = ReflectionUtil.getColumnLength(ParNombreExpediente.class, "parNombre");
        if (parNombre.trim().length() > textoNombreColumnLength) {
            throw new BusinessLogicException("El texto del nombre permite máximo " + textoNombreColumnLength + " caracteres.");
        }
        
        ParNombreExpediente nombreExpediente = parNombreExpedienteRepository.findOneByParNombre(parNombre);
        if (!nombreExpediente.getParId().equals(parNombreExpediente.getParId()) && nombreExpediente != null) {
            throw new BusinessLogicException("Este nombre ya existe.");
        }
        
        parNombreExpediente.setParNombre(parNombre.toUpperCase());
        
        ParNombreExpediente nombreExpedienteAnterior
                = findOne(parNombreExpediente.getParId());
        
        parNombreExpediente.setUsuCreacion(nombreExpedienteAnterior.getUsuCreacion());
        parNombreExpediente.setFecCreacion(nombreExpedienteAnterior.getFecCreacion());
        
        parNombreExpediente.setUsuModificacion(usuario);
        parNombreExpediente.setFecModificacion(new Date());

        parNombreExpedienteRepository.saveAndFlush(parNombreExpediente);
    }

 

}
