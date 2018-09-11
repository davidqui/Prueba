package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.DestinoExterno;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DestinoExternoRepository;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.ReflectionException;
import com.laamware.ejercito.doc.web.util.ReflectionUtil;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * servicio para {@link DestinoExterno}.
 *
 * @author samuel.delgado@controltechcg.com 
 * @since 1.8
 * @version 31/08/2018 Issue gogs #10 (SICDI-Controltech) feature-gogs-10
 */
@Service
public class DestinoExternoService {
    
    /**
     * Repositorio de persistencia para {@link DestinoExterno}.
     */
    @Autowired
    private DestinoExternoRepository destinoExternoRepository;
    
    /**
     * *
     * Lista todas las observaciones
     *
     * @param sort
     * @return
     */
    public List<DestinoExterno> findAll(Sort sort) {
        return destinoExternoRepository.findAll(sort);
    }

    /**
     * *
     * Lista todas las observaciones activas
     *
     * @param sort
     * @return
     */
    public List<DestinoExterno> findActive(Sort sort) {
        return destinoExternoRepository.getByActivoTrue(sort);
    }

    /**
     * *
     * Busca una observacion por defecto por id
     *
     * @param id identificador de la observacion por defecto
     * @return
     */
    public DestinoExterno findOne(Integer id) {
        return destinoExternoRepository.findOne(id);
    }

    /**
     * Creación de una obsevación por defecto
     *
     * @param destinoExterno observación por defecto a ser creada
     * @param usuario Usuario que aplico el cambio
     * @throws com.laamware.ejercito.doc.web.util.BusinessLogicException En caso
     * que no se cumplan las validaciones de negocio.
     * @throws com.laamware.ejercito.doc.web.util.ReflectionException En caso
     * que se presenten errores con funciones de relection.
     */
    public void crear(DestinoExterno destinoExterno, Usuario usuario) throws BusinessLogicException, ReflectionException {
        final String textoDestinoExterno = destinoExterno.getNombre().toUpperCase();
        final String siglaDestinoExterno = destinoExterno.getSigla().toUpperCase();
        
        if (textoDestinoExterno == null || textoDestinoExterno.trim().length() == 0) {
            throw new BusinessLogicException("El nombre del destino externo es obligatorio.");
        }

        final int textoDestinoExternoColumnLength = ReflectionUtil.getColumnLength(DestinoExterno.class, "nombre");
        if (textoDestinoExterno.trim().length() > textoDestinoExternoColumnLength) {
            throw new BusinessLogicException("El nombre del destino externo permite máximo " + textoDestinoExternoColumnLength + " caracteres.");
        }
        
        if (siglaDestinoExterno == null || siglaDestinoExterno.trim().length() == 0) {
            throw new BusinessLogicException("la sigla del destino externo es obligatorio.");
        }

        final int textoSiglaColumnLength = ReflectionUtil.getColumnLength(DestinoExterno.class, "sigla");
        if (siglaDestinoExterno.trim().length() > textoSiglaColumnLength) {
            throw new BusinessLogicException("la sigla del destino externo permite máximo " + textoDestinoExternoColumnLength + " caracteres.");
        }
        
        DestinoExterno destinoSigla = destinoExternoRepository.findOneBySiglaAndActivoTrue(siglaDestinoExterno);
        if (destinoSigla != null ) {
            throw new BusinessLogicException("ya existe un destino externo con esta sigla.");
        }
        
        
        destinoExterno.setNombre(textoDestinoExterno);
        destinoExterno.setSigla(siglaDestinoExterno);
        
        
        destinoExterno.setQuien(usuario.getId());
        destinoExterno.setCuando(new Date());
        destinoExterno.setActivo(Boolean.TRUE);
        destinoExterno.setQuienMod(usuario.getId());
        destinoExterno.setCuandoMod(new Date());

        destinoExternoRepository.saveAndFlush(destinoExterno);
    }

    /**
     * Editar una obsevación por defecto
     *
     * @param destinoExterno obsevación por defecto a ser editada
     * @param usuario Usuario que aplico el cambio
     * @throws com.laamware.ejercito.doc.web.util.ReflectionException En caso
     * que se presenten errores con funciones de relection.
     */
    public void editar(DestinoExterno destinoExterno, Usuario usuario) throws BusinessLogicException, ReflectionException{
        
            final String textoDestinoExterno = destinoExterno.getNombre();
            final String siglaDestinoExterno = destinoExterno.getSigla();

            if (textoDestinoExterno == null || textoDestinoExterno.trim().length() == 0) {
                throw new BusinessLogicException("El nombre del destino externo es obligatorio.");
            }

            final int textoDestinoExternoColumnLength = ReflectionUtil.getColumnLength(DestinoExterno.class, "nombre");
            if (textoDestinoExterno.trim().length() > textoDestinoExternoColumnLength) {
                throw new BusinessLogicException("El nombre del destino externo permite máximo " + textoDestinoExternoColumnLength + " caracteres.");
            }

            if (siglaDestinoExterno == null || siglaDestinoExterno.trim().length() == 0) {
                throw new BusinessLogicException("la sigla del destino externo es obligatorio.");
            }

            final int textoSiglaColumnLength = ReflectionUtil.getColumnLength(DestinoExterno.class, "sigla");
            if (siglaDestinoExterno.trim().length() > textoSiglaColumnLength) {
                throw new BusinessLogicException("la sigla del destino externo permite máximo " + textoDestinoExternoColumnLength + " caracteres.");
            }
            
            
            DestinoExterno destinoSigla = destinoExternoRepository.findOneBySiglaAndActivoTrue(siglaDestinoExterno);
            if (destinoSigla != null && !destinoExterno.getId().equals(destinoSigla.getId())) {
                throw new BusinessLogicException("ya existe un destino externo con esta sigla.");
            }
        
            destinoExterno.setNombre(textoDestinoExterno);
            destinoExterno.setSigla(siglaDestinoExterno);

            DestinoExterno documentoDestinoExternoAnterior
                    = findOne(destinoExterno.getId());

            destinoExterno.setQuien(documentoDestinoExternoAnterior.getQuien());
            destinoExterno.setCuando(documentoDestinoExternoAnterior.getCuando());
            destinoExterno.setActivo(documentoDestinoExternoAnterior.getActivo());

            destinoExterno.setQuienMod(usuario.getId());
            destinoExterno.setCuandoMod(new Date());

            destinoExternoRepository.saveAndFlush(destinoExterno);
    }

    /**
     * Eliminar una observación por defecto
     *
     * @param destinoExterno obsevación por defecto a ser eliminada
     * @param usuario Usuario que aplico el cambio
     */
    public void eliminar(DestinoExterno destinoExterno,
            Usuario usuario) {
        destinoExterno.setQuien(usuario.getId());
        destinoExterno.setCuando(new Date());
        destinoExterno.setActivo(Boolean.FALSE);
        destinoExternoRepository.saveAndFlush(destinoExterno);
    }

    /**
     * Lista todas las destino externo activas, ordenadas por el
     * texto.
     *
     * @return Lista de destinos externos activos ordenadas por nombre.
     */
    public List<DestinoExterno> listarActivas() {
        return destinoExternoRepository.findAllByActivoTrueOrderByNombreAsc();
    }
}
