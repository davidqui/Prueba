package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.laamware.ejercito.doc.web.entity.Proceso;

public interface ProcesoRepository extends GenJpaRepository<Proceso, Integer> {

	List<Proceso> findByActivo(boolean b);

	List<Proceso> findByActivo(boolean b, Sort sort);

	/**
	 * Obtiene la lista de procesos que no son alias de ninguno
	 * 
	 * @param b
	 *            indica si los procesos están o no activos
	 * @param sort
	 *            orden
	 * @return lista de procesos con las características descritas
	 */
	List<Proceso> findByActivoAndAliasIsNull(boolean b, Sort sort);

}
