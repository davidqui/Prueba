package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.laamware.ejercito.doc.web.entity.Destinatario;

public interface DestinatarioRepository extends
GenJpaRepository<Destinatario, Integer> {

	Destinatario getByTipoDestinatarioIdAndRefId(int i, Integer id);

	List<Destinatario> findByActivo(boolean activo, Sort sort);
	
	List<Destinatario> findByNombreContainingAndActivo(String t, boolean activo);
	
	

	List<Destinatario> findByNombre(String destinatario);

}
