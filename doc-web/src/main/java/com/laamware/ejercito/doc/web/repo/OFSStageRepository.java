package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laamware.ejercito.doc.web.entity.OFSStage;

public interface OFSStageRepository extends JpaRepository<OFSStage, String> {

	List<OFSStage> findByRefAndUsuarioId(String ref, Integer usuarioId);

}
