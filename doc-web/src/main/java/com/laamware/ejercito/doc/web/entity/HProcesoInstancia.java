package com.laamware.ejercito.doc.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "H_PROCESO_INSTANCIA")
public class HProcesoInstancia extends AuditModifySupport {

	@Id
	@Column(name = "HPIN_ID")
	private String hid;

	@Column(name = "PIN_ID")
	private String id;

	@ManyToOne
	@JoinColumn(name = "PRO_ID")
	private Proceso proceso;

	@ManyToOne
	@JoinColumn(name = "PES_ID")
	private Estado estado;

	@ManyToOne
	@JoinColumn(name = "USU_ID_ASIGNADO")
	private Usuario asignado;

	public String getHid() {
		return hid;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Proceso getProceso() {
		return proceso;
	}

	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Usuario getAsignado() {
		return asignado;
	}

	public void setAsignado(Usuario asignado) {
		this.asignado = asignado;
	}

}
