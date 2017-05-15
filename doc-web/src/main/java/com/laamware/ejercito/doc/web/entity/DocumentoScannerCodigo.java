package com.laamware.ejercito.doc.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.laamware.ejercito.doc.web.util.GeneralUtils;

/**
 * 
 * @author rafar
 *
 */
@Entity
@Table(name = "DOCUMENTO_SCANNER_CODIGO")
public class DocumentoScannerCodigo extends AuditActivoModifySupport {

	public static DocumentoScannerCodigo create() {
		DocumentoScannerCodigo x = new DocumentoScannerCodigo();
		x.id = GeneralUtils.newId();
		return x;
	}
	
	@Id
	@Column(name = "DOCCOD_ID")
	private String id;

	@Column(name = "DOCCOD_CODIGO")
	private String codigo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}
