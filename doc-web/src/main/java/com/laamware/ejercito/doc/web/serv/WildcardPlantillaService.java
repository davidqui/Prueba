package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.WildcardPlantilla;
import com.laamware.ejercito.doc.web.repo.WildcardPlantillaRepository;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio de negocio para {@link WildcardPlantilla}.
 *
 * @author samuel.delgado@controltechcg.com
 * @since 1.8
 * @version 27/06/2018 Issue #176 (SICDI-Controltech) feature-176
 */
@Service
public class WildcardPlantillaService {
    private static final Logger LOG = Logger.getLogger(DominioService.class.getName());
    
    @Autowired
    private WildcardPlantillaRepository wildcardPlantillaRepository;
    
    /***
     * Busca los wildcards por texto
     * @param text
     * @return lista de wildcards
     */
    public List<WildcardPlantilla> findByText(String text){
        return wildcardPlantillaRepository.findByTexto(text);
    }
    
    public WildcardPlantilla crearWildcardPlantilla(WildcardPlantilla wildcard){
        return wildcardPlantillaRepository.saveAndFlush(wildcard);
    }
}
