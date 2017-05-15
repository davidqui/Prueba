package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import com.laamware.ejercito.doc.web.entity.JuegoPregunta;
import com.laamware.ejercito.doc.web.entity.JuegoRespuestaUsuarioHistorial;

public interface JuegoRespuestaUsuarioHistorialRepository extends
GenJpaRepository<JuegoRespuestaUsuarioHistorial, Integer> {

	List<JuegoRespuestaUsuarioHistorial> findByPregunta(
			JuegoPregunta juegoPregunta);
}
