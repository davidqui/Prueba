package com.laamware.ejercito.doc.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "OFS_STAGE")
public class OFSStage {

	public static final Integer TIPO_SCANNER = 0;
	public static final Integer TIPO_FILE = 1;

	@Id
	@Column(name = "OST_ID")
	private String id;

	@Column(name = "OST_REF")
	private String ref;

	@ManyToOne
	@JoinColumn(name = "USU_ID")
	private Usuario usuario;

	@Column(name = "OST_PARTES")
	private String partes;

	@Column(name = "OST_TIPO")
	private Integer tipo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getPartes() {
		return partes;
	}

	public void setPartes(String partes) {
		this.partes = partes;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

}
