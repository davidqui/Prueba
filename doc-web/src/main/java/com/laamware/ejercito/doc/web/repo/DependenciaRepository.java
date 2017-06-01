package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.laamware.ejercito.doc.web.entity.Dependencia;

public interface DependenciaRepository extends GenJpaRepository<Dependencia, Integer> {

	List<Dependencia> findByActivo(boolean activo, Sort sort);

	List<Dependencia> findByActivoAndPadre(boolean activo, Integer padreId, Sort sort);

	List<Dependencia> findByActivoAndPadreIsNull(boolean activo, Sort sort);

	Dependencia findByActivoAndDepCodigoLdap(boolean activo, String depCodLdap);

	/**
	 * Obtiene la lista de dependencias activas en las cuales el usuario es jefe
	 * principal o jefe encargado.
	 * 
	 * @param usuarioId
	 *            ID del usuario.
	 * @return Lista de dependencias activas.
	 */
	// 2017-06-01 jgarcia@controltechcg.com Issue #99 (SICDI-Controltech)
	// hotfix-99
	@Query(nativeQuery = true, value = ""
			+ " SELECT                                                                           "
			+ " DEPENDENCIA.*                                                                    "
			+ " FROM                                                                             "
			+ " DEPENDENCIA                                                                      "
			+ " WHERE                                                                            "
			+ " DEPENDENCIA.ACTIVO = 1                                                           "
			+ " AND (DEPENDENCIA.USU_ID_JEFE = :usuarioId OR DEPENDENCIA.USU_ID_JEFE = :usuarioId ) ")
	List<Dependencia> findActivoByJefeAsignado(@Param("usuarioId") Integer usuarioId);
}
