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
@Table(name = "BARRIO")
@LaamLabel("Definición de barrio")
public class Barrio extends AuditActivoCreateSupport {
	@Id
	@GenericGenerator(name = "BARRIO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "BARRIO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BARRIO_SEQ")
	@Column(name = "BAR_ID")
	private Integer id;

	@LaamLabel("Nombre")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "BAR_NOMBRE")
	private String nombre;

	@LaamLabel("Zona")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 30)
	@ManyToOne
	@JoinColumn(name = "ZON_ID")
	@LaamWidget(value = "select", list = "Zona")
	private Zona zona;

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

	public Zona getZona() {
		return zona;
	}

	public void setZona(Zona zona) {
		this.zona = zona;
	}

}
