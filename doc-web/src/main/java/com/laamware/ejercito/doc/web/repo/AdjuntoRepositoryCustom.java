package com.laamware.ejercito.doc.web.repo;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class AdjuntoRepositoryCustom {

	@PersistenceContext
    private EntityManager em;

	public int insertIntoDocumentoAdjunto(String newId, Integer documentoLlegado, Date date, Integer quien, String docId,String fileId, String string, String string2, Integer activo){
		
		return em.createNativeQuery("INSERT INTO DOCUMENTO_ADJUNTO (DAD_ID,TDO_ID,CUANDO,QUIEN,DOC_ID,DAD_CONTENT,DAD_ORIGINAL,DAD_DESCRIPCION,ACTIVO) VALUES (?,?,?,?,?,?,?,?,?)")
	        .setParameter(1, newId)
	        .setParameter(2, documentoLlegado)
	        .setParameter(3, date)
	        .setParameter(4, quien)
	        .setParameter(5, docId)
	        .setParameter(6, fileId)
	        .setParameter(7, string)
	        .setParameter(8, string2)
	        .setParameter(9, activo)
	        .executeUpdate();
		
	}
	
	public int insertIntoDocumentoLogVistosBueno(String docId, Integer idUsuarioVistoBueno, Date cuando){
		
		return em.createNativeQuery("INSERT INTO DOCUMENTO_USU_VISTOS_BUENOS (DOC_ID, USU_ID_VISTO_BUENO, CUANDO) VALUES ( ?, ?, SYSDATE)")
	        .setParameter(1, docId)
	        .setParameter(2, idUsuarioVistoBueno)
	        .executeUpdate();
		
	}

}
