package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.laamware.ejercito.doc.web.entity.Perfil;

public interface PerfilRepository extends GenJpaRepository<Perfil, Integer> {

	List<Perfil> findByActivo(boolean activo, Sort sort);

}
