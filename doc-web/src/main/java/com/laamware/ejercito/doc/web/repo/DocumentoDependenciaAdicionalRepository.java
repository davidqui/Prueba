package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.laamware.ejercito.doc.web.entity.DocumentoDependenciaDestino;

/**
 * 
 * @author Rafael G Blanco 
 *
 */
public interface DocumentoDependenciaAdicionalRepository extends GenJpaRepository<DocumentoDependenciaDestino, String> {

	@Query(nativeQuery = true, value = "SELECT DOC.* FROM DOCUMENTO_DEP_DESTINO DOC WHERE DOC.DOC_ID=?")
	List<DocumentoDependenciaDestino> findByDocumento(String idDocumento);
	
}