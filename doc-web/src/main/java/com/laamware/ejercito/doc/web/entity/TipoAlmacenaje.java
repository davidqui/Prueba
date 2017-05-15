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
@Table(name = "TIPO_ALMACENAJE")
@LaamLabel("Definición de tipos de almacenaje")
public class TipoAlmacenaje extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "TIPO_ALMACENAJE_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "TIPO_ALMACENAJE_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPO_ALMACENAJE_SEQ")
	@Column(name = "TAL_ID")
	private Integer id;

	@LaamLabel("Nombre")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 20)
	@Column(name = "TAL_NOMBRE")
	private String nombre;

	@LaamLabel("Código")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 30)
	@Column(name = "TAL_CODIGO")
	private String codigo;

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

}
