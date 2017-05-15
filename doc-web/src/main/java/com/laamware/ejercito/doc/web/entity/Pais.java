package com.laamware.ejercito.doc.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author mcr
 *
 */
@Entity
@Table(name = "PAIS")
@LaamLabel("Definición de país")
public class Pais extends AuditActivoCreateSupport {
	@Id
	@GenericGenerator(name = "PAIS_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "PAIS_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAIS_SEQ")
	@Column(name = "PAI_ID")
	private Integer id;

	@LaamLabel("Nombre")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "PAI_NOMBRE")
	private String nombre;

	@LaamLabel("Sigla")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 30)
	@Column(name = "PAI_SIGLA")
	private String sigla;

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

}
