package com.laamware.ejercito.doc.web.repo;

import org.springframework.data.jpa.repository.Query;

import com.laamware.ejercito.doc.web.entity.Juego;

public interface JuegoRepository extends GenJpaRepository<Juego, Integer> {

	@Query(nativeQuery = true, value="select * from Juego where rownum = 1")
	Juego findJuego();

}
