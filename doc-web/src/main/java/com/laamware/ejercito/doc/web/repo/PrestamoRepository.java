package com.laamware.ejercito.doc.web.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.Prestamo;

public interface PrestamoRepository extends GenJpaRepository<Prestamo, Integer> {

	@Query(nativeQuery = true, value = "select DISTINCT P.* FROM PRESTAMO P JOIN DOCUMENTO_PRESTAMO DP ON DP.PRE_ID=P.PRE_ID JOIN DOCUMENTO D ON DP.DOC_ID=D.DOC_ID AND D.EXP_ID=?")
	Prestamo findByExpediente(Expediente e);

	List<Prestamo> findByFechaDevolucionBefore(Date date);

}
