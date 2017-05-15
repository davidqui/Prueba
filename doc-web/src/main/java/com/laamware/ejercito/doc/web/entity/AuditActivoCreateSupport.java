package com.laamware.ejercito.doc.web.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AuditActivoCreateSupport extends AuditCreateSupport {

	@Column(name = "ACTIVO")
	private Boolean activo = Boolean.TRUE;

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

}
