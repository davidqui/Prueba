package com.laamware.ejercito.doc.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "H_PROCESO_INSTANCIA_VAR")
public class HProcesoInstanciaVariable extends AuditActivoModifySupport {

	@Id
	@Column(name = "HPIV_ID")
	private Integer hid;

	@Column(name = "PIV_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "PIN_ID")
	private Instancia instancia;

	@Column(name = "PIV_KEY")
	private String key;

	@Column(name = "PIV_VALUE")
	private String value;

	public Integer getHid() {
		return hid;
	}

	public void setHid(Integer hid) {
		this.hid = hid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Instancia getInstancia() {
		return instancia;
	}

	public void setInstancia(Instancia instancia) {
		this.instancia = instancia;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
