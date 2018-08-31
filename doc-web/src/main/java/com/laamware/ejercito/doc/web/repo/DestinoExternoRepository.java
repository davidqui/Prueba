package com.laamware.ejercito.doc.web.repo;
import com.laamware.ejercito.doc.web.entity.DestinoExterno;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repositorio de persistencia para {@link DestinoExterno}.
 *
 * @author samuel.delgado@controltechcg.com 
 * @since 1.8
 * @version 31/08/2018 Issue gogs #10 (SICDI-Controltech) feature-gogs-10
 */
public interface DestinoExternoRepository extends JpaRepository<DestinoExterno, Integer>{
    
    /**
     * Lista todas los destinos externos activos
     * @param sort ordenar por
     * @return lista de destinos externos
     */
    public List<DestinoExterno> getByActivoTrue(Sort sort);

    /**
     * Lista todas los destinos externos activos, ordenadas por el nombre.
     *
     * @return lista de destinos externos
     */
    public List<DestinoExterno> findAllByActivoTrueOrderByNombreAsc();

}
