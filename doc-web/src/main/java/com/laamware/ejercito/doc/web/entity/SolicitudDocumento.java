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
@Table(name = "SOLICITUD_DOCUMENTO")
public class SolicitudDocumento extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "SOLICITUD_DOCUMENTO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "SOLICITUD_DOCUMENTO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SOLICITUD_DOCUMENTO_SEQ")
	@Column(name = "SOLDOC_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "PRESOL_ID")
	private PrestamoSolicitud prestamoSOlicitud;

	@ManyToOne
	@JoinColumn(name = "DOC_ID")
	private Documento documento;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PrestamoSolicitud getPrestamoSOlicitud() {
		return prestamoSOlicitud;
	}

	public void setPrestamoSOlicitud(PrestamoSolicitud prestamoSOlicitud) {
		this.prestamoSOlicitud = prestamoSOlicitud;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

}
