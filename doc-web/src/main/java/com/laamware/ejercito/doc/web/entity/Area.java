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
@Table(name = "AREA")
@LaamLabel("Definición de área")
public class Area extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "AREA_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "AREA_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AREA_SEQ")
	@Column(name = "AREA_ID")
	private Integer id;

	@LaamLabel("Nombre")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "AREA_NOMBRE")
	private String nombre;

	@LaamLabel("Sigla")
	@LaamCreate(order = 30)
	@LaamListColumn(order = 40)
	@Column(name = "AREA_SIGLA")
	private String sigla;

	@LaamLabel("Código")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 30)
	@Column(name = "AREA_CODIGO")
	private String codigo;

	@LaamLabel("Piso")
	@LaamCreate(order = 30)
	@LaamListColumn(order = 40)
	@ManyToOne
	@JoinColumn(name = "PISO_ID")
	@LaamWidget(value = "select", list = "pisos")
	private Piso piso;

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

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Piso getPiso() {
		return piso;
	}

	public void setPiso(Piso piso) {
		this.piso = piso;
	}

}
