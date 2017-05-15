package com.laamware.ejercito.doc.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "PERFIL_ROL")
public class PerfilRol extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "PERFIL_ROL_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "PERFIL_ROL_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERFIL_ROL_SEQ")
	@Column(name = "PER_ROL_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "ROL_ID")
	private Rol rol;

	@ManyToOne
	@JoinColumn(name = "PER_ID")
	private Perfil perfil;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

}
