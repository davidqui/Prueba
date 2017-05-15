package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.laamware.ejercito.doc.web.entity.Bandeja;

public interface BandejaRepository extends GenJpaRepository<Bandeja, Integer> {

	/**
	 * Obtiene las bandejas activas ordenadas según se define en el parámetro
	 * 
	 * @param activo
	 * @param sort
	 * @return
	 */
	List<Bandeja> findByActivo(Boolean activo, Sort sort);

}
