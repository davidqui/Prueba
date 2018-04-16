package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.DocumentoDependenciaDestino;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Rafael G Blanco
 * @deprecated 2018-04-16 jgarcia@controltechcg.com Issue #156
 * (SICDI-Controltech) feature-156 Se reemplaza por la entidad
 * {@link DependenciaCopiaMultidestino} y su correspondiente repositorio
 * {@link DependenciaCopiaMultidestinoRepository}.
 */
@Deprecated
public interface DocumentoDependenciaAdicionalRepository extends GenJpaRepository<DocumentoDependenciaDestino, String> {

    @Query(nativeQuery = true, value = "SELECT DOC.* FROM DOCUMENTO_DEP_DESTINO DOC WHERE DOC.DOC_ID=?")
    List<DocumentoDependenciaDestino> findByDocumento(String idDocumento);

}
