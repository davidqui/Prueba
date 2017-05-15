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
@Table(name = "PISO")
@LaamLabel("Definición de piso")
public class Piso extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "PISO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "PISO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PISO_SEQ")
	@Column(name = "PISO_ID")
	private Integer id;

	@LaamLabel("Nombre")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 20)
	@Column(name = "PISO_NOMBRE")
	private String nombre;

	@LaamLabel("Sigla")
	@LaamCreate(order = 30)
	@LaamListColumn(order = 40)
	@Column(name = "PISO_SIGLA")
	private String sigla;

	@LaamLabel("Código")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 30)
	@Column(name = "PISO_CODIGO")
	private String codigo;

	@LaamLabel("Edificio")
	@LaamCreate(order = 30)
	@LaamListColumn(order = 40)
	@ManyToOne
	@JoinColumn(name = "EDF_ID")
	@LaamWidget(value = "select", list = "edificios")
	private Edificio edificio;

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

	public Edificio getEdificio() {
		return edificio;
	}

	public void setEdificio(Edificio edificio) {
		this.edificio = edificio;
	}

}
