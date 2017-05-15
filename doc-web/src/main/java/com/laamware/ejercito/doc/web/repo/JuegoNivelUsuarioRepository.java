package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.laamware.ejercito.doc.web.entity.JuegoNivel;
import com.laamware.ejercito.doc.web.entity.JuegoNivelUsuario;

public interface JuegoNivelUsuarioRepository extends
		GenJpaRepository<JuegoNivelUsuario, Integer> {

	@Query("select jnu from JuegoNivelUsuario jnu where jnu.usuario.id = ?1")
	JuegoNivelUsuario findByUser(Integer userId);

	JuegoNivelUsuario findByJuegoNivelId(JuegoNivel nivel);

	@Query("select max(jnu.juegoNivel.dificultad) from JuegoNivelUsuario jnu where jnu.usuario.id = ?1")
	Integer findMaxDificultadByUser(Integer userId);

	JuegoNivelUsuario findByJuegoNivelIdAndUsuarioId(Integer nivelId,
			Integer userId);

	Long countByUsuarioId(Integer id);

	List<JuegoNivelUsuario> findAllByUsuarioId(Integer uid);

}
