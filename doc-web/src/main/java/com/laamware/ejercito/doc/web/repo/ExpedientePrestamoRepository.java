package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.laamware.ejercito.doc.web.entity.ExpedientePrestamo;

public interface ExpedientePrestamoRepository extends GenJpaRepository<ExpedientePrestamo, Integer> {

	ExpedientePrestamo findByPrestamoId(Integer id);

	List<ExpedientePrestamo> findByActivo(Boolean b);

	@Query(nativeQuery = true, value = "SELECT EP.* FROM EXPEDIENTE_PRESTAMO EP "
			+ "JOIN PRESTAMO PRE ON EP.PRE_ID=PRE.PRE_ID " 
			+ "JOIN USUARIO U ON PRE.USU_ID_SOLICITA=U.USU_ID "
			+ "WHERE PRE.USU_ID_SOLICITA=?1 "
			+ "AND EP.ACTIVO=?2 "
			+ "ORDER BY PRE.PRE_FCH_DEVOLUCION DESC")
	List<ExpedientePrestamo> findByUsuarioIdAndActivo(Integer uid, Boolean b);

}
