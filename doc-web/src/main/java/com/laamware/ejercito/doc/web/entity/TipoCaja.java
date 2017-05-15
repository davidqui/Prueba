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
@Table(name = "TIPO_CAJA")
@LaamLabel("Definición de tipos de caja")
public class TipoCaja extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "TIPO_CAJA_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "TIPO_CAJA_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPO_CAJA_SEQ")
	@Column(name = "TC_ID")
	private Integer id;

	@LaamLabel("Nombre")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "TC_NOMBRE")
	private String nombre;

	@LaamLabel("Numero")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 20)
	@Column(name = "TC_NUMERO")
	private String numero;

	@LaamLabel("Código")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 30)
	@Column(name = "TC_CODIGO")
	private String codigo;

	@LaamLabel("Sigla")
	@LaamCreate(order = 30)
	@LaamListColumn(order = 40)
	@Column(name = "TC_SIGLA")
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

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

}
