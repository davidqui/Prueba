package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.laamware.ejercito.doc.web.entity.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface LogRepository extends JpaRepository<Log, Integer> {

	@Query("select l from Log l WHERE l.quien=:logUsuario AND l.detalleAccion != NULL "
                + "AND LOWER(l.detalleAccion) LIKE %:filter%"
                + " order by l.cuando desc")
        Page<Log> findAllLogLogByUserOnly(@Param("logUsuario") String logUsuario, @Param("filter") String filter, Pageable pageable);

}
