package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import com.laamware.ejercito.doc.web.entity.JuegoRespuesta;

public interface JuegoRespuestaRepository extends
		GenJpaRepository<JuegoRespuesta, Integer> {

	List<JuegoRespuesta> findByJuegoPreguntaId(Integer id);

	List<JuegoRespuesta> findByJuegoPreguntaIdAndActivo(
			Integer juegoPreguntaId, boolean b);

}
