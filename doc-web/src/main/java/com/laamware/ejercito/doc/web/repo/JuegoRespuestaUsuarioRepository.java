package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.laamware.ejercito.doc.web.entity.JuegoPregunta;
import com.laamware.ejercito.doc.web.entity.JuegoRespuestaUsuario;
import com.laamware.ejercito.doc.web.entity.Usuario;

public interface JuegoRespuestaUsuarioRepository extends
		GenJpaRepository<JuegoRespuestaUsuario, Integer> {

	List<JuegoRespuestaUsuario> findByPreguntaAndUsuario(
			JuegoPregunta juegoPregunta, Usuario usuario);

	@Query("select jru from JuegoRespuestaUsuario jru where jru.pregunta.juegoNivel.id = ?1 and jru.usuario.id=?2")
	List<JuegoRespuestaUsuario> findByJuegoNivelAndUsuario(Integer nivelId,
			Integer usuarioId);

	List<JuegoRespuestaUsuario> findByUsuarioId(Integer usuarioId);

	@Query("delete from JuegoRespuestaUsuario jru where jru.usuario.id=?1")
	void deleteAllByUsuario(Integer usuarioId);

}
