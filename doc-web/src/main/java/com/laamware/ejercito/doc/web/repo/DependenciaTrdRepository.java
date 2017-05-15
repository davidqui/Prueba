package com.laamware.ejercito.doc.web.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.laamware.ejercito.doc.web.entity.DependenciaTrd;

public interface DependenciaTrdRepository extends
		GenJpaRepository<DependenciaTrd, Integer> {

	@Query(nativeQuery = true, value = "SELECT DISTINCT T.TRD_SERIE FROM TRD T JOIN DEPENDENCIA_TRD DT ON DT.TRD_ID=T.TRD_ID WHERE DT.DEP_ID=? AND DT.TRD_ID=T.TRD_ID")
	List<BigDecimal> findSeries(Integer dependenciaId);

}
