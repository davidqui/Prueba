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
@Table(name = "INFORME")
public class Informe extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "INFORME_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "INFORME_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INFORME_SEQ")
	@Column(name = "INF_ID")
	@LaamLabel("ID")
	private Integer id;

	@Column(name = "INF_NOMBRE")
	@LaamListColumn(order = 20)
	@LaamLabel("Título del informe")
	@LaamCreate(order = 20)
	private String nombre;

	@Column(name = "INF_DESCRIPCION")
	@LaamListColumn(order = 25)
	@LaamLabel("Descripción")
	@LaamCreate(order = 25)
	@LaamWidget(value = "textarea")
	private String descripcion;

	@Column(name = "INF_SQL")
	@LaamLabel("SQL")
	@LaamCreate(order = 30)
	@LaamWidget(value = "textarea")
	private String sql;

	@Column(name = "INF_ROL")
	@LaamLabel("Rol")
	@LaamCreate(order = 40)
	private String rol;

	@Column(name = "INF_PARAM")
	@LaamLabel("Definición de parámetros")
	@LaamCreate(order = 50)
	@LaamWidget(value = "textarea")
	private String parametros;

	@Override
	public String toString() {
		return nombre;
	}

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

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getParametros() {
		return parametros;
	}

	public void setParametros(String parametros) {
		this.parametros = parametros;
	}

}
