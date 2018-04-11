package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.DependenciaCopiaMultidestino;
import com.laamware.ejercito.doc.web.entity.Documento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
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
     * Busca si existe un registro activo para el documento y la dependencia
     * indicados.
     *
     * @param documentoOriginal Documento original.
     * @param dependenciaDestino Dependencia destino de copia.
     * @return Instancia del registro activo, {@code null} si no hay
     * correspondencia en el sistema.
     */
    public DependenciaCopiaMultidestino findByDocumentoOriginalAndDependenciaDestinoAndActivoTrue(Documento documentoOriginal, Dependencia dependenciaDestino);

}
