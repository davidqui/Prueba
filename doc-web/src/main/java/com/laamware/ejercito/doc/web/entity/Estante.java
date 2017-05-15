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
@Table(name = "ESTANTE")
@LaamLabel("Definición de estante")
public class Estante extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "ESTANTE_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "ESTANTE_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESTANTE_SEQ")
	@Column(name = "ESTT_ID")
	private Integer id;

	@LaamLabel("Nombre")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "ESTT_NOMBRE")
	private String nombre;

	@LaamLabel("Código")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 30)
	@Column(name = "ESTT_CODIGO")
	private String codigo;

	@LaamLabel("Módulo")
	@LaamCreate(order = 50)
	@LaamListColumn(order = 60)
	@ManyToOne
	@JoinColumn(name = "MOD_ID")
	@LaamWidget(value = "select", list = "modulos")
	private Modulo modulo;

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

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

}
