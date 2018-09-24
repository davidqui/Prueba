package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Dominio;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de persistencia para {@link Dominio}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 05/02/2018 Issue #159 (SICDI-Controltech) feature-159
 */
@Repository
public interface DominioRepository extends JpaRepository<Dominio, String> {

    List<Dominio> getByActivoTrue(Sort sort);
    
    Page<Dominio> getByActivoTrue(Pageable pageable);

    @Query(nativeQuery = true, value = ""
            + "SELECT count(1) "
            + "FROM DOMINIO "
            + "WHERE UPPER(DOM_CODIGO) = UPPER(:codigo) "
            + "AND ACTIVO = 1")
    public Integer findregistrosCodigoRepetido(@Param("codigo") String codigo);

    /**
     * Obtiene la lista de dominios activos, colocando como primero un dominio
     * por defecto y el resto ordenados por nombre.
     *
     * @param codigoDefault Código del dominio activo que se desea colocar como
     * primero de la lista por defecto.
     * @return Lista de dominios activos, con el dominio por defecto como
     * primero, y el resto ordenados por nombre.
     */
    /*
     * 2018-05-16 jgarcia@controltechcg.com Issue #164 (SICDI-Controltech)
     * hotfix-164.
     */
    @Query(nativeQuery = true, value = ""
            + "SELECT\n"
            + "  *\n"
            + "FROM DOMINIO\n"
            + "WHERE ACTIVO = 1\n"
            + "ORDER BY CASE\n"
            + "  WHEN DOM_CODIGO = :dom_codigo_default THEN 1\n"
            + "  ELSE 2\n"
            + "END ASC, NOMBRE ASC"
            + "")
    public List<Dominio> findAllActivoOrderByDominioDefaultAndNombreAsc(@Param("dom_codigo_default") String codigoDefault);
}
