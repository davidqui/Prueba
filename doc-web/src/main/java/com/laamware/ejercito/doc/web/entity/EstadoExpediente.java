package com.laamware.ejercito.doc.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "ESTADO_EXPEDIENTE")
@LaamLabel("Estados de un expediente")
public class EstadoExpediente extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "ESTADO_EXPEDIENTE_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "ESTADO_EXPEDIENTE_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESTADO_EXPEDIENTE_SEQ")
	@Column(name = "ESEX_ID")
	private Integer id;

	@LaamLabel("Nombre")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "ESEX_NOMBRE")
	private String nombre;

	@Column(name = "ESEX_DESCRIPCION")
	@LaamLabel("Descripción del estado")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 20)
	private String descripcion;

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

	@Override
	public String toString() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
