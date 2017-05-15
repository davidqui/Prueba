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
@Table(name = "MUNICIPIO")
@LaamLabel("Definición de municipio")
public class Municipio extends AuditActivoCreateSupport {

	@Id
	@GenericGenerator(name = "MUNICIPIO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "MUNICIPIO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MUNICIPIO_SEQ")
	@Column(name = "MPO_ID")
	private Integer id;

	@LaamLabel("Nombre")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "MPO_NOMBRE")
	private String nombre;

	@LaamLabel("Departamento")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 30)
	@ManyToOne
	@JoinColumn(name = "DPT_ID")
	@LaamWidget(value = "select", list = "departamento")
	private Departamento departamento;

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

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

}
