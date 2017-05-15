package com.laamware.ejercito.doc.web.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "USUARIO_ROL")
public class UsuarioRol {

	@Id
	@GenericGenerator(name = "USUARIO_ROL_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "USUARIO_ROL_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIO_ROL_SEQ")
	@Column(name = "USU_ROL_ID")
	private Integer id;

	@ManyToOne
	private Rol rol;

	@ManyToOne
	private Usuario usuario;

	@CreatedBy
	@Column(name = "QUIEN")
	private Integer quien;

	@CreatedDate
	@Column(name = "CUANDO")
	private Date cuando;

	@CreatedBy
	@Column(name = "QUIEN_MOD")
	private Integer quienModifica;

	@CreatedDate
	@Column(name = "CUANDO_MOD")
	private Date cuandoModifico;

	@Column(name = "ACTIVO")
	private Boolean activo;

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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Integer getQuien() {
		return quien;
	}

	public void setQuien(Integer quien) {
		this.quien = quien;
	}

	public Date getCuando() {
		return cuando;
	}

	public void setCuando(Date cuando) {
		this.cuando = cuando;
	}

	public Integer getQuienModifica() {
		return quienModifica;
	}

	public void setQuienModifica(Integer quienModifica) {
		this.quienModifica = quienModifica;
	}

	public Date getCuandoModifico() {
		return cuandoModifico;
	}

	public void setCuandoModifico(Date cuandoModifico) {
		this.cuandoModifico = cuandoModifico;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

}
