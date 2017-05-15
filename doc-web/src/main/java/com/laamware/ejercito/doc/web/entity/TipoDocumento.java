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
@Table(name = "TIPO_DOCUMENTO")
public class TipoDocumento extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "TIPO_DOCUMENTO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "TIPO_DOCUMENTO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPO_DOCUMENTO_SEQ")
	@Column(name = "TDC_ID")
	@LaamListColumn(order = 10)
	@LaamLabel("ID")
	private Integer id;

	@Column(name = "TDC_NOMBRE")
	@LaamListColumn(order = 20)
	@LaamLabel("Nombre la tipología")
	@LaamCreate(order = 10)
	private String nombre;

	@ManyToOne
	@JoinColumn(name = "PLA_ID")
	@LaamListColumn(order = 30)
	@LaamLabel("Plantilla")
	@LaamCreate(order = 20)
	@LaamWidget(value = "select", list = "plantillas")
	private Plantilla plantilla;

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

	public Plantilla getPlantilla() {
		return plantilla;
	}

	public void setPlantilla(Plantilla plantilla) {
		this.plantilla = plantilla;
	}

	@Override
	public String toString() {
		return nombre;
	}
}
