package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import com.laamware.ejercito.doc.web.entity.JuegoAyudaSegundaUsuario;
import com.laamware.ejercito.doc.web.entity.JuegoNivel;
import com.laamware.ejercito.doc.web.entity.Usuario;

public interface JuegoAyudaSegundaUsuarioRepository extends
GenJpaRepository<JuegoAyudaSegundaUsuario, Integer> {

List<JuegoAyudaSegundaUsuario> findByJuegoNivelAndUsuario(JuegoNivel juegoNivel,
	Usuario usuario);

}
