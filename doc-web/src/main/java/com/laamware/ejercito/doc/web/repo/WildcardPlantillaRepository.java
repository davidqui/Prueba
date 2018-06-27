package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.WildcardPlantilla;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de persistencia para {@link WildcardPlantilla}.
 *
 * @author samuel.delgado@controltechcg.com
 * @since 1.8
 * @version 27/06/2018 Issue #176 (SICDI-Controltech) feature-176
 */
public interface WildcardPlantillaRepository extends JpaRepository<WildcardPlantilla, Integer>{

    /***
     * Busca los wildcards por texto
     * @param texto
     * @return lista de wildcards
     */
    public List<WildcardPlantilla> findByTexto(String texto);
    
}
