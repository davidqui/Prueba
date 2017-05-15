package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.laamware.ejercito.doc.web.entity.DocumentoPrestamo;

public interface DocumentoPrestamoRepository extends GenJpaRepository<DocumentoPrestamo, Integer> {

	DocumentoPrestamo findByPrestamoId(Integer id);

	DocumentoPrestamo findByDocumentoId(String id);

	@Query(nativeQuery = true, value = "SELECT DP.* FROM DOCUMENTO_PRESTAMO DP "
			+ "JOIN PRESTAMO PRE ON DP.PRE_ID=PRE.PRE_ID " 
			+ "JOIN USUARIO U ON PRE.USU_ID_SOLICITA=U.USU_ID "
			+ "WHERE PRE.USU_ID_SOLICITA=?1 " 
			+ "AND DP.ACTIVO=?2 " 
			+ "ORDER BY PRE_FCH_DEVOLUCION DESC")
	List<DocumentoPrestamo> findByUsuarioIdAndActivo(Integer uid, Boolean b);

}
