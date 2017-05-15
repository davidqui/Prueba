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
@Table(name = "PROCESO_TRANSICION_COND")
@LaamLabel("Condiciones")
public class Condicion extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "PROCESO_TRANSICION_COND_SEQ", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "PROCESO_TRANSICION_COND_SEQ") })
	@GeneratedValue(generator = "PROCESO_TRANSICION_COND_SEQ", strategy = GenerationType.SEQUENCE)
	@Column(name = "PTC_ID")
	private Integer id;

	@LaamLabel("Nombre")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "PTC_NOMBRE")
	private String nombre;

	@Column(name = "PTC_PROGRAMA")
	@LaamLabel("Programa")
	@LaamCreate(order = 20)
	@LaamWidget(value = "textarea")
	private String programa;

	@ManyToOne
	@JoinColumn(name = "PTR_ID")
	private Transicion transicion;

	@Column(name = "PTC_COMPILADO")
	@LaamLabel("Compilado")
	@LaamListColumn(order = 20)
	private Boolean compilado = Boolean.FALSE;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPrograma() {
		return programa;
	}

	public void setPrograma(String programa) {
		this.programa = programa;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Transicion getTransicion() {
		return transicion;
	}

	public void setTransicion(Transicion transicion) {
		this.transicion = transicion;
	}

	public Boolean getCompilado() {
		return compilado;
	}

	public void setCompilado(Boolean compilado) {
		this.compilado = compilado;
	}

}
