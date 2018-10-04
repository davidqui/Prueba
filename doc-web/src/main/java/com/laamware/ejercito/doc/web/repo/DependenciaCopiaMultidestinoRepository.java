package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.DependenciaCopiaMultidestino;
import com.laamware.ejercito.doc.web.entity.Documento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para {@link DependenciaCopiaMultidestino}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/10/2018 (SICDI-Controltech Issue #156 feature-156)
 */
@Repository
public interface DependenciaCopiaMultidestinoRepository extends JpaRepository<DependenciaCopiaMultidestino, Integer> {

    /**
     * Busca la lista de registros activos para el documento indicado.
     *
     * @param documentoOriginal Documento original.
     * @return Lista de registros de copia multidestino.
     */
    public List<DependenciaCopiaMultidestino> findAllByDocumentoOriginalAndActivoTrue(Documento documentoOriginal);
    
    /**
     * Busca la lista de registros pendientes de clonación para el documento indicado.
     *
     * @param documentoOriginal Documento original.
     * @return Lista de registros de copia multidestino.
     */
    public List<DependenciaCopiaMultidestino> findAllByDocumentoOriginalAndActivoTrueAndDocumentoResultadoIsNull(Documento documentoOriginal);

    /**
     * Busca si existe un registro activo para el documento y la dependencia
     * indicados.
     *
     * @param documentoOriginal Documento original.
     * @param dependenciaDestino Dependencia destino de copia.
     * @return Instancia del registro activo, {@code null} si no hay
     * correspondencia en el sistema.
     */
    public DependenciaCopiaMultidestino findByDocumentoOriginalAndDependenciaDestinoAndActivoTrue(Documento documentoOriginal, Dependencia dependenciaDestino);

    /**
     * Cuenta el número de registros activos para el documento indicado.
     *
     * @param documentoOriginal Documento original.
     * @return Número de registros de copia multidestino.
     */
    public Long countByDocumentoOriginalAndActivoTrue(Documento documentoOriginal);
    
    /**
     * Cuenta el número de registros activos que se encuentran pendientes de
     * asignar id del documento resultante.
     *
     * @param docIdOriginal Identificado del Documento original.
     * @return Número de registros de copia multidestino que no se han clonado.
     */
    @Query(value = ""
            + "select count(1) "
            + "from DEPENDENCIA_COPIA_MULTIDESTINO "
            + "where doc_id_original = :docIdOriginal "
            + "and doc_id_resultado is null "
            + "and activo = 1", nativeQuery = true)
    public Integer cantidadDocumentosResultadosPendientesXDocumentoOriginal(@Param("docIdOriginal") String docIdOriginal);

}
