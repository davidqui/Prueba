package com.laamware.ejercito.doc.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PROCESO_TRANSICION_TIPO")
@LaamLabel("Tipos de transición de proceso")
public class TipoTransicion extends AuditActivoModifySupport {

	@Id
	@Column(name = "PTT_ID")
	@LaamCreate(order = 10)
	@LaamLabel("Identificador único")
	@LaamListColumn(order = 10)
	private Integer id;

	@Column(name = "PTT_NOMBRE")
	@LaamCreate(order = 20)
	@LaamLabel("Nombre del tipo de transición")
	@LaamListColumn(order = 20)
	private String nombre;

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
		return this.nombre;
	}
}
