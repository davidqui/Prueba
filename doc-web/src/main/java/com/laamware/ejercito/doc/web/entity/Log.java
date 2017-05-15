package com.laamware.ejercito.doc.web.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "LOG")
@LaamLabel("Log por usuario")
public class Log {

	@Id
	@GenericGenerator(name = "LOG_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "LOG_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_SEQ")
	@Column(name = "LOG_ID")
	private Integer id;

	@LaamLabel("Quien")
	@LaamCreate(order = 1)
	@LaamListColumn(order = 2)
	@Column(name = "QUIEN")
	private String quien;

	@LaamLabel("Cuando")
	@LaamCreate(order = 3)
	@LaamListColumn(order = 4)
	@Column(name = "CUANDO")
	private Date cuando;

	//@LaamLabel("Contenido de la acción")
	//@LaamCreate(order = 5)
	//@LaamListColumn(order = 6)
	@Column(name = "LOG_CONTENIDO")
	private String contenido;
	
	@LaamLabel("Detalle de la acción")
	@LaamCreate(order = 7)
	@LaamListColumn(order = 8)
	@Column(name = "detalle_accion")
	private String detalleAccion;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQuien() {
		return quien;
	}

	public void setQuien(String quien) {
		this.quien = quien;
	}

	public Date getCuando() {
		return cuando;
	}

	public void setCuando(Date cuando) {
		this.cuando = cuando;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public String getDetalleAccion() {
		return detalleAccion;
	}

	public void setDetalleAccion(String detalleAccion) {
		this.detalleAccion = detalleAccion;
	}

}
