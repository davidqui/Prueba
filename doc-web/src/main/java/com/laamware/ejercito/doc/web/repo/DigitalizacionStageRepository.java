package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laamware.ejercito.doc.web.entity.DigitalizacionStage;

public interface DigitalizacionStageRepository extends
		JpaRepository<DigitalizacionStage, Integer> {

	List<DigitalizacionStage> findByDocumentoIdAndUsuarioId(String docId,
			Integer usuarioId);

}
