package com.laamware.ejercito.doc.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ROL")
@LaamLabel("Permisos")
public class Rol extends AuditActivoModifySupport {

	@Id
	@Column(name = "ROL_ID")
	@LaamLabel("Id")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	private String id;

	@Column(name="ROL_NOMBRE")
	@LaamLabel("Nombre")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 20)
	private String nombre;
	
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return this.nombre;
	}

}
