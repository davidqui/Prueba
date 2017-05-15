package com.laamware.ejercito.doc.web.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.laamware.ejercito.doc.web.entity.Adjunto;

public interface AdjuntoRepository extends JpaRepository<Adjunto, String> {

	List<Adjunto> getAdjuntosByDocumentoId(String docId);
		
}
