package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import com.laamware.ejercito.doc.web.entity.JuegoNivel;

public interface JuegoNivelRepository extends
		GenJpaRepository<JuegoNivel, Integer> {

	List<JuegoNivel> findByJuegoId(Integer id, Sort sort);

	List<JuegoNivel> findByJuegoIdAndActivoOrderByDificultadAsc(Integer id,
			boolean b);

	@Query("select jn from JuegoNivel jn where jn.dificultad = ?1")
	JuegoNivel findByDificultad(Integer gradoDificultad);

	@Query("select jn from JuegoNivel jn where jn.dificultad = (select max(jnn.dificultad) from JuegoNivel jnn where jnn.juego.id =?1 ) ")
	JuegoNivel nivelMaxDificultad(Integer jjuId);

	@Query("SELECT jn FROM JuegoNivel jn WHERE jn.dificultad=(SELECT MAX(jn.dificultad) FROM JuegoNivel jn) AND jn.juego.id=?1")
	JuegoNivel findMaxDificultadByJuego(Integer jjuId);

	@Query("SELECT jn FROM JuegoNivel jn WHERE jn.dificultad=(SELECT MIN(jn.dificultad) FROM JuegoNivel jn)")
	JuegoNivel findByMinDificultad();

}
