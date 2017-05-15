package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.laamware.ejercito.doc.web.entity.TipoDestinatario;

public interface TipoDestinatarioRepository extends
GenJpaRepository<TipoDestinatario, Integer> {

	List<TipoDestinatario> findByActivo(boolean activo, Sort sort);

}
