package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Dominio;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DominioRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Servicio de negocio para {@link Dominio}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 05/02/2018 Issue #159 (SICDI-Controltech) feature-159
 */
@Service
public class DominioService {

    @Autowired
    private DominioRepository dominioRepository;

    public List<Dominio> mostrarDominiosActivos(Sort sort) {
        return dominioRepository.getByActivoTrue(sort);
    }

    public List<Dominio> findAll(Sort sort) {
        return dominioRepository.findAll(sort);
    }

    public Dominio findOne(String id) {
        return dominioRepository.findOne(id);
    }

    /**
     * Creación del dominio
     * @param dominio dominio a ser creado
     * @param usuario Usuario que aplico el cambio
     * @return 
     */
    public String crearDominio(Dominio dominio, Usuario usuario) {
        String mensaje = "OK";
        try {
            System.err.println("dominioservice= " + dominio);
            if (dominio.getCodigo() == null || dominio.getCodigo().trim().length() == 0) {
                mensaje = "Error-El código del dominio es obligatorio.";
                return mensaje;
            }

            if (dominio.getCodigo() != null && dominio.getCodigo().length() > 8) {
                mensaje = "Error-El código del dominio debe ser de máximo 8 caracteres.";
                return mensaje;
            }

            if (dominio.getNombre() == null || dominio.getNombre().trim().length() == 0) {
                mensaje = "Error-El nombre del dominio es obligatorio.";
                return mensaje;
            }

            int numRegistros = dominioRepository.findregistrosCodigoRepetido(dominio.getCodigo());
            if (numRegistros > 0) {
                mensaje = "Error-El código del dominio ya se encuentra registrado en el sistema.";
                return mensaje;
            }

            dominio.setQuien(usuario);
            dominio.setCuando(new Date());
            dominio.setActivo(Boolean.TRUE);
            dominio.setQuienMod(usuario);
            dominio.setCuandoMod(new Date());

            if (dominio.getVisualizaLinkOWA() == null) {
                dominio.setVisualizaLinkOWA(Boolean.FALSE);
            }

            dominioRepository.saveAndFlush(dominio);
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "Excepcion-" + e.getMessage();
        }
        return mensaje;
    }

    /**
     * Editar del dominio
     * @param dominio dominio a ser editado
     * @param usuario Usuario que aplico el cambio
     * @return 
     */
    public String editarDominio(Dominio dominio, Usuario usuario) {
        String mensaje = "OK";
        try {
            if (dominio.getCodigo() == null || dominio.getCodigo().trim().length() == 0) {
                mensaje = "Error-El código del dominio es obligatorio.";
                return mensaje;
            }

            if (dominio.getNombre() == null || dominio.getNombre().trim().length() == 0) {
                mensaje = "Error-El nombre del dominio es obligatorio.";
                return mensaje;
            }
            Dominio dominioAnterior = findOne(dominio.getCodigo());

            dominio.setActivo(dominioAnterior.getActivo());
            dominio.setCuando(dominioAnterior.getCuando());
            dominio.setQuien(dominioAnterior.getQuien());
            
            dominio.setQuienMod(usuario);
            dominio.setCuandoMod(new Date());

            if (dominio.getVisualizaLinkOWA() == null) {
                dominio.setVisualizaLinkOWA(Boolean.FALSE);
            }

            dominioRepository.saveAndFlush(dominio);
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "Excepcion-" + e.getMessage();
        }
        return mensaje;
    }
    
    /**
     * Eliminar del dominio
     * @param dominio dominio a ser editado
     * @param usuario Usuario que aplico el cambio
     * @return 
     */
    public String eliminarDominio(Dominio dominio, Usuario usuario) {
        String mensaje = "OK";
        try {
            dominio.setQuienMod(usuario);
            dominio.setCuandoMod(new Date());

            if (dominio.getVisualizaLinkOWA() == null) {
                dominio.setVisualizaLinkOWA(Boolean.FALSE);
            }
            
            dominio.setActivo(Boolean.FALSE);

            dominioRepository.saveAndFlush(dominio);
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "Excepcion-" + e.getMessage();
        }
        return mensaje;
    }
}
