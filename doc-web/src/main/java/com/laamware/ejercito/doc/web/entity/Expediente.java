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
@Table(name = "EXPEDIENTE")
@LaamLabel("Expedientes")
public class Expediente extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "EXPEDIENTE_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "EXPEDIENTE_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXPEDIENTE_SEQ")
	@Column(name = "EXP_ID")
	private Integer id;

	@Column(name = "EXP_CODIGO")
	@LaamLabel("Código")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	private String codigo;

	@LaamLabel("Nombre")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 20)
	@Column(name = "EXP_NOMBRE")
	private String nombre;

	@LaamLabel("Dependencia")
	@LaamCreate(order = 30)
	@LaamListColumn(order = 30)
	@LaamWidget(value = "select", list = "dependencias")
	@ManyToOne
	@JoinColumn(name = "DEP_ID")
	private Dependencia dependencia;

	@LaamLabel("Tiempo de retencion")
	@LaamListColumn(order = 40)
	@Column(name = "EXP_TIEMPO_RETENCION")
	private Integer tiempoRetencion;

	@Column(name = "TDF_ID")
	private Integer tdf;

	//Issue #2
	//En el módulo de Administración – Expedientes – Crear nuevo expediente: cambiar el campo donde dice: Procedimiento por Descripción.
	@LaamLabel("Descripción")
	@LaamCreate(order = 50)
	@LaamWidget("textarea")
	@Column(name = "EXP_PROCEDIMIENTO")
	private String procedimiento;

	@LaamLabel("TRD")
	@LaamCreate(order = 120)
	@LaamWidget(value = "select", list = "tdrs")
	@ManyToOne
	@JoinColumn(name = "TRD_ID")
	@LaamListColumn(order = 60)
	private Trd trd;

	@Column(name = "PLA_ID")
	private Integer plaId;

	@ManyToOne
	@JoinColumn(name = "ESEX_ID")
	EstadoExpediente estado;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Dependencia getDependencia() {
		return dependencia;
	}

	public void setDependencia(Dependencia dependencia) {
		this.dependencia = dependencia;
	}

	public Integer getTiempoRetencion() {
		return tiempoRetencion;
	}

	public void setTiempoRetencion(Integer tiempoRetencion) {

		this.tiempoRetencion = tiempoRetencion;

	}

	public Integer getTdf() {
		return tdf;
	}

	public void setTdf(Integer tdf) {
		this.tdf = tdf;
	}

	public String getProcedimiento() {
		return procedimiento;
	}

	public void setProcedimiento(String procedimiento) {
		this.procedimiento = procedimiento;
	}

	public Integer getPlaId() {
		return plaId;
	}

	public void setPlaId(Integer plaId) {
		this.plaId = plaId;
	}

	public Trd getTrd() {
		return trd;
	}

	public void setTrd(Trd trd) {
		this.trd = trd;
	}

	public EstadoExpediente getEstado() {
		return estado;
	}

	public void setEstado(EstadoExpediente estado) {
		this.estado = estado;
	}

}
