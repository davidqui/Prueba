package com.laamware.ejercito.doc.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "EDIFICIO")
@LaamLabel("Definición de edificio")
public class Edificio extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "EDIFICIO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "EDIFICIO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EDIFICIO_SEQ")
	@Column(name = "EDF_ID")
	private Integer id;

	@LaamLabel("Nombre")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "EDF_NOMBRE")
	private String nombre;

	@LaamLabel("Código")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 30)
	@Column(name = "EDF_CODIGO")
	private String codigo;

	@LaamLabel("Sigla")
	@LaamCreate(order = 30)
	@LaamListColumn(order = 40)
	@Column(name = "EDF_SIGLA")
	private String sigla;

	@LaamLabel("Dirección")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "EDF_DIRECCION")
	private String direccion;

	@LaamLabel("Localización")
	@LaamCreate(order = 110)
	@LaamWidget(value = "localizacion")
	@Transient
	private String localizacion;

	@LaamLabel("País")
	@LaamListColumn(order = 50)
	@ManyToOne
	@JoinColumn(name = "PAI_ID")
	private Pais pais;

	@LaamLabel("Departamento")
	@LaamListColumn(order = 50)
	@ManyToOne
	@JoinColumn(name = "DPT_ID")
	private Departamento departamento;

	@LaamLabel("Municipio")
	@LaamListColumn(order = 50)
	@ManyToOne
	@JoinColumn(name = "MPO_ID")
	private Municipio municipio;

	@LaamLabel("Barrio")
	@LaamListColumn(order = 50)
	@ManyToOne
	@JoinColumn(name = "BAR_ID")
	private Barrio barrio;

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

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public Barrio getBarrio() {
		return barrio;
	}

	public void setBarrio(Barrio barrio) {
		this.barrio = barrio;
	}

}
