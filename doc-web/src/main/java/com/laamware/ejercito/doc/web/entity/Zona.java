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
@Table(name = "Zona")
@LaamLabel("Definición de zona")
public class Zona extends AuditActivoCreateSupport {
	@Id
	@GenericGenerator(name = "ZONA_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "ZONA_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ZONA_SEQ")
	@Column(name = "ZON_ID")
	private Integer id;

	@LaamLabel("Nombre")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "ZON_NOMBRE")
	private String nombre;

	@LaamLabel("Municipio")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 30)
	@ManyToOne
	@JoinColumn(name = "MPO_ID")
	@LaamWidget(value = "select", list = "Municipio")
	private Municipio municipio;

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

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

}
