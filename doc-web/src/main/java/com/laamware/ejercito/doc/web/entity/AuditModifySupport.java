package com.laamware.ejercito.doc.web.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
public class AuditModifySupport extends AuditCreateSupport {

	@LastModifiedDate
	@Column(name = "CUANDO_MOD")
	private Date cuandoMod;

	@LastModifiedBy
	@Column(name = "QUIEN_MOD")
	private Integer quienMod;

	public Date getCuandoMod() {
		return cuandoMod;
	}

	public void setCuandoMod(Date cuandoMod) {
		this.cuandoMod = cuandoMod;
	}

	public Integer getQuienMod() {
		return quienMod;
	}

	public void setQuienMod(Integer quienMod) {
		this.quienMod = quienMod;
	}

}
