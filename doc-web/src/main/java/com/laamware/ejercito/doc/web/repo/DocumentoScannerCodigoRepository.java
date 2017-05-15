package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laamware.ejercito.doc.web.entity.Adjunto;

public interface DocumentoScannerCodigoRepository extends JpaRepository<Adjunto, String> {

	List<Adjunto> getAdjuntosByDocumentoId(String docId);

}
