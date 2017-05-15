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
@Table(name = "ARCHIVO_EXPEDIENTE")
public class ArchivoExpediente extends AuditActivoCreateSupport {

	@Id
	@GenericGenerator(name = "ARCHIVO_EXPEDIENTE_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "ARCHIVO_EXPEDIENTE_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARCHIVO_EXPEDIENTE_SEQ")
	@Column(name = "AEX_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "ARC_ID")
	private Archivo archivo;

	@ManyToOne
	@JoinColumn(name = "EXP_ID")
	private Expediente expediente;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Archivo getArchivo() {
		return archivo;
	}

	public void setArchivo(Archivo archivo) {
		this.archivo = archivo;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

}
