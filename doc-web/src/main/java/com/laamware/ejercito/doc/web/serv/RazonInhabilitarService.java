package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.RazonInhabilitar;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.RazonInhabilitarRepository;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.ReflectionException;
import com.laamware.ejercito.doc.web.util.ReflectionUtil;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Servicio de lógica de negocio para {@link razonInhabilitar}.
 *
 * @author samuel.delgado@controltechcg.com
 * @since 1.8
 * @version 08/16/2018 Issue gogs #7 (SICDI-Controltech) feature-gogs-7
 */
@Service
public class RazonInhabilitarService {
    
    private static final Logger LOG = Logger.getLogger(RazonInhabilitarService.class.getName());
    
    @Autowired
    private RazonInhabilitarRepository razonInhabilitarRepository;
    
    /**
     * *
     * Lista todas las razones
     *
     * @param sort
     * @return
     */
    public List<RazonInhabilitar> findAll(Sort sort) {
        return razonInhabilitarRepository.findAll(sort);
    }

    /**
     * *
     * Lista todas las razones activas
     *
     * @param sort
     * @return
     */
    public List<RazonInhabilitar> findActive(Sort sort) {
        return razonInhabilitarRepository.getByActivoTrue(sort);
    }

    /**
     * *
     * Busca una razón por id
     *
     * @param id identificador de la observacion 
     * @return
     */
    public RazonInhabilitar findOne(Integer id) {
        return razonInhabilitarRepository.findOne(id);
    }

    /**
     * Creación de una razón 
     *
     * @param razonInhabilitar razón  a ser creada
     * @param usuario Usuario que aplico el cambio
     * @throws com.laamware.ejercito.doc.web.util.BusinessLogicException En caso
     * que no se cumplan las validaciones de negocio.
     * @throws com.laamware.ejercito.doc.web.util.ReflectionException En caso
     * que se presenten errores con funciones de relection.
     */
    public void crearRazonInhabilitar(RazonInhabilitar razonInhabilitar, Usuario usuario) throws BusinessLogicException, ReflectionException {
        final String textoRazon = razonInhabilitar.getTextoRazon();
        if (textoRazon == null || textoRazon.trim().length() == 0) {
            throw new BusinessLogicException("El texto de la razón es obligatorio.");
        }

        final int textoRazonColumnLength = ReflectionUtil.getColumnLength(RazonInhabilitar.class, "textoRazon");
        if (textoRazon.trim().length() > textoRazonColumnLength) {
            throw new BusinessLogicException("El texto de la razón permite máximo " + textoRazonColumnLength + " caracteres.");
        }

        razonInhabilitar.setQuien(usuario);
        razonInhabilitar.setCuando(new Date());
        razonInhabilitar.setActivo(Boolean.TRUE);
        razonInhabilitar.setQuienMod(usuario);
        razonInhabilitar.setCuandoMod(new Date());

        razonInhabilitarRepository.saveAndFlush(razonInhabilitar);
    }

    /**
     * Editar una razón 
     *
     * @param razonInhabilitar razón a ser editada
     * @param usuario Usuario que aplico el cambio
     * @throws com.laamware.ejercito.doc.web.util.ReflectionException En caso
     * que se presenten errores con funciones de relection.
     */
    public void editarRazonInhabilitar(RazonInhabilitar razonInhabilitar, Usuario usuario) throws BusinessLogicException, ReflectionException{
        
            final String textoRazon = razonInhabilitar.getTextoRazon();
            if (textoRazon == null || textoRazon.trim().length() == 0) {
                throw new BusinessLogicException("El texto de la razón es obligatorio.");
            }

            final int textoRazonColumnLength = ReflectionUtil.getColumnLength(RazonInhabilitar.class, "textoRazon");
            if (textoRazon.trim().length() > textoRazonColumnLength) {
                throw new BusinessLogicException("El texto de la razón permite máximo " + textoRazonColumnLength + " caracteres.");
            }

            RazonInhabilitar razonInhabilitarAnterior
                    = findOne(razonInhabilitar.getId());

            razonInhabilitar.setQuien(razonInhabilitarAnterior.getQuien());
            razonInhabilitar.setCuando(razonInhabilitarAnterior.getCuando());
            razonInhabilitar.setActivo(razonInhabilitarAnterior.getActivo());

            razonInhabilitar.setQuienMod(usuario);
            razonInhabilitar.setCuandoMod(new Date());

            razonInhabilitarRepository.saveAndFlush(razonInhabilitar);
    }

    /**
     * Eliminar una razón 
     *
     * @param razonInhabilitar razón  a ser eliminada
     * @param usuario Usuario que aplico el cambio
     */
    public void eliminarRazonInhabilitar(RazonInhabilitar razonInhabilitar,
            Usuario usuario) {
        razonInhabilitar.setQuien(usuario);
        razonInhabilitar.setCuando(new Date());
        razonInhabilitar.setActivo(Boolean.FALSE);
        razonInhabilitarRepository.saveAndFlush(razonInhabilitar);
    }

    /**
     * Lista todas las razones  activas, ordenadas por el
     * texto.
     *
     * @return Lista de razones activas ordenadas por texto.
     */
    public List<RazonInhabilitar> listarActivas() {
        return razonInhabilitarRepository.findAllByActivoTrueOrderByTextoRazonAsc();
    }
}
