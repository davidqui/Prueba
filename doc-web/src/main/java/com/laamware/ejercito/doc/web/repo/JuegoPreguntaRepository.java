package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.laamware.ejercito.doc.web.entity.JuegoPregunta;

public interface JuegoPreguntaRepository extends
		GenJpaRepository<JuegoPregunta, Integer> {

	List<JuegoPregunta> findByJuegoNivelId(Integer id);

	List<JuegoPregunta> findByJuegoNivelIdAndActivo(Integer juegoNivelId,
			boolean b);

	@Query(nativeQuery = true, value = "select FN_JUEGO_PREGUNTA(?, ?) from dual")
	Integer findRandomId(Integer jjuId, Integer usuarioId);

	List<JuegoPregunta> findByIdAndActivo(Integer preguntaId, boolean b);

}
