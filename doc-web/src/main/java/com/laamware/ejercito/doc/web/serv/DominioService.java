package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Dominio;
import com.laamware.ejercito.doc.web.repo.DominioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    public List<Dominio> mostrarDominiosActivos() {
        return dominioRepository.getByActivoTrue();
    }
}
