package com.laamware.ejercito.doc.web.repo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenJpaRepository<T, TID extends Serializable> extends
		JpaRepository<T, TID> {

	List<T> findByActivo(boolean activo);

}
