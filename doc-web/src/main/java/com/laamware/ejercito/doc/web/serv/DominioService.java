package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Dominio;
import com.laamware.ejercito.doc.web.repo.DominioRepository;
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

    public Dominio findOne(String id) {
        return dominioRepository.findOne(id);
    }
    
    public String crearDominio(Dominio dominio) {
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

            int numRegistros = dominioRepository.findregistrosCodigoRepetido(dominio.getCodigo());
            if (numRegistros > 0) {
                mensaje = "Error-El código del dominio ya se encuentra registrado en el sistema.";
                return mensaje;
            }
            dominioRepository.save(dominio);
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "Excepcion-" + e.getMessage();
        }
        return mensaje;
    }
    
    public String editarDominio(Dominio dominio) {
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

            dominioRepository.save(dominio);
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "Excepcion-" + e.getMessage();
        }
        return mensaje;
    }
}
