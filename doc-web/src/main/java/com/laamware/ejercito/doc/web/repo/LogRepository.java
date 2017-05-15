package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.laamware.ejercito.doc.web.entity.Log;

public interface LogRepository extends JpaRepository<Log, Integer> {

	@Query("select l from Log l WHERE l.quien=?1 AND l.detalleAccion != NULL order by l.cuando desc")
	List<Log> findAllLogLogByUserOnly(String loginUsuario);

}
