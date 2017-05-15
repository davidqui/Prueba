package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import com.laamware.ejercito.doc.web.entity.JuegoAyudaUsuario;
import com.laamware.ejercito.doc.web.entity.JuegoNivel;
import com.laamware.ejercito.doc.web.entity.Usuario;

public interface JuegoAyudaUsuarioRepository extends
GenJpaRepository<JuegoAyudaUsuario, Integer> {

	List<JuegoAyudaUsuario> findByJuegoNivelAndUsuario(JuegoNivel juegoNivel,
			Usuario usuario);

}
