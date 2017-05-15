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

/**
 * @author mcr
 *
 */
@Entity
@Table(name = "DEPARTAMENTO")
@LaamLabel("Definición de departamento")
public class Departamento extends AuditActivoCreateSupport {

	@Id
	@GenericGenerator(name = "DEPARTAMENTO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "DEPARTAMENTO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPARTAMENTO_SEQ")
	@Column(name = "DPT_ID")
	private Integer id;

	@LaamLabel("Nombre")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "DPT_NOMBRE")
	private String nombre;

	@LaamLabel("País")
	@LaamCreate(order = 50)
	@LaamListColumn(order = 60)
	@ManyToOne
	@JoinColumn(name = "PAI_ID")
	@LaamWidget(value = "select", list = "pais")
	private Pais pais;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

}
