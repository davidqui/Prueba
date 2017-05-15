package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.laamware.ejercito.doc.web.entity.JuegoPreguntaTemp;

public interface JuegoPreguntaTempRepo extends
		GenJpaRepository<JuegoPreguntaTemp, Integer> {

	@Query("select jpt from JuegoPreguntaTemp jpt where jpt.juegoPregunta.juegoNivel.id=?1 and jpt.activo=?2 ")
	List<JuegoPreguntaTemp> findByPreguntaNivelAndActivo(Integer nivelId,
			boolean b);

	JuegoPreguntaTemp findByJuegoPreguntaIdAndUsuarioId(Integer preguntaId,
			Integer usuarioId);

	@Modifying
	@Transactional
	@Query("delete from JuegoPreguntaTemp jpt where jpt.usuario.id=?1")
	void deleteAllByUsuario(Integer usuarioId);

	Long countByUsuarioId(Integer usuario);

}