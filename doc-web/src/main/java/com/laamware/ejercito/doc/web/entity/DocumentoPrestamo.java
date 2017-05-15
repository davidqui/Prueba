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
@Table(name = "DOCUMENTO_PRESTAMO")
public class DocumentoPrestamo extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "DOCUMENTO_PRESTAMO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "DOCUMENTO_PRESTAMO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENTO_PRESTAMO_SEQ")
	@Column(name = "DOCPRE_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "DOC_ID")
	private Documento documento;

	@ManyToOne
	@JoinColumn(name = "PRE_ID")
	private Prestamo prestamo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public Prestamo getPrestamo() {
		return prestamo;
	}

	public void setPrestamo(Prestamo prestamo) {
		this.prestamo = prestamo;
	}

}
