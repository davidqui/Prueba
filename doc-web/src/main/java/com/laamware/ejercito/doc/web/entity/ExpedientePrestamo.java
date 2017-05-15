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
@Table(name = "EXPEDIENTE_PRESTAMO")
@LaamLabel("Prestamos")
public class ExpedientePrestamo extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "EXPEDIENTE_PRESTAMO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "EXPEDIENTE_PRESTAMO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXPEDIENTE_PRESTAMO_SEQ")
	@Column(name = "EXPPRE_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "EXP_ID")
	private Expediente expediente;

	@ManyToOne
	@JoinColumn(name = "PRE_ID")
	private Prestamo prestamo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public Prestamo getPrestamo() {
		return prestamo;
	}

	public void setPrestamo(Prestamo prestamo) {
		this.prestamo = prestamo;
	}

}
