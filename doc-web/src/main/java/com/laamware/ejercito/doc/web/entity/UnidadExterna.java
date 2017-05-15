package com.laamware.ejercito.doc.web.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "UNIDAD_EXTERNA")
public class UnidadExterna {

	@Id
	@GenericGenerator(name = "UNIDAD_EXTERNA_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "UNIDAD_EXTERNA_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UNIDAD_EXTERNA_SEQ")
	@Column(name = "UEX_ID")
	private Integer id;

	@Column(name = "UEX_DOCUMENTO")
	private String uexDocumento;

	@Column(name = "UEX_SIGLA")
	private String uexSigla;

	@Column(name = "UEX_NOMBRE")
	private String uexNombre;

	@Column(name = "UEX_TELEFONO")
	private String uexTelefono;

	@Column(name = "UEX_DIRECCION")
	private String uexDireccion;

	@Column(name = "UEX_NOMBRE_DESTINATARIO")
	private String uexNombreDestinatario;

	@CreatedBy
	@Column(name = "QUIEN")
	private Integer quien;

	@CreatedDate
	@Column(name = "CUANDO")
	private Date cuando;

	@CreatedBy
	@Column(name = "QUIEN_MOD")
	private Integer quienModifica;

	@CreatedDate
	@Column(name = "CUANDO_MOD")
	private Date cuandoModifico;

	@Column(name = "ACTIVO")
	private Integer activo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUexDocumento() {
		return uexDocumento;
	}

	public void setUexDocumento(String uexDocumento) {
		this.uexDocumento = uexDocumento;
	}

	public String getUexSigla() {
		return uexSigla;
	}

	public void setUexSigla(String uexSigla) {
		this.uexSigla = uexSigla;
	}

	public String getUexNombre() {
		return uexNombre;
	}

	public void setUexNombre(String uexNombre) {
		this.uexNombre = uexNombre;
	}

	public String getUexTelefono() {
		return uexTelefono;
	}

	public void setUexTelefono(String uexTelefono) {
		this.uexTelefono = uexTelefono;
	}

	public String getUexDireccion() {
		return uexDireccion;
	}

	public void setUexDireccion(String uexDireccion) {
		this.uexDireccion = uexDireccion;
	}

	public String getUexNombreDestinatario() {
		return uexNombreDestinatario;
	}

	public void setUexNombreDestinatario(String uexNombreDestinatario) {
		this.uexNombreDestinatario = uexNombreDestinatario;
	}

	public Integer getQuien() {
		return quien;
	}

	public void setQuien(Integer quien) {
		this.quien = quien;
	}

	public Date getCuando() {
		return cuando;
	}

	public void setCuando(Date cuando) {
		this.cuando = cuando;
	}

	public Integer getQuienModifica() {
		return quienModifica;
	}

	public void setQuienModifica(Integer quienModifica) {
		this.quienModifica = quienModifica;
	}

	public Date getCuandoModifico() {
		return cuandoModifico;
	}

	public void setCuandoModifico(Date cuandoModifico) {
		this.cuandoModifico = cuandoModifico;
	}

	public Integer getActivo() {
		return activo;
	}

	public void setActivo(Integer activo) {
		this.activo = activo;
	}

}
