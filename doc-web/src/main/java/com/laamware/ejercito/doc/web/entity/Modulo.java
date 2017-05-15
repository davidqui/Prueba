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

@Entity
@Table(name = "MODULO")
@LaamLabel("Definición de modulo")
public class Modulo extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "MODULO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "MODULO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MODULO_SEQ")
	@Column(name = "MOD_ID")
	private Integer id;

	@LaamLabel("Nombre")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "MOD_NOMBRE")
	private String nombre;

	@LaamLabel("Código")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 30)
	@Column(name = "MOD_CODIGO")
	private String codigo;

	@LaamLabel("Área")
	@LaamCreate(order = 40)
	@LaamListColumn(order = 50)
	@ManyToOne
	@JoinColumn(name = "AREA_ID")
	@LaamWidget(value = "select", list = "areas")
	private Area area;

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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

}
