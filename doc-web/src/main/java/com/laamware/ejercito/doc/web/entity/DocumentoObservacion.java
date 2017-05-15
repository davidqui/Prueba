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
@Table(name = "DOCUMENTO_OBS")
public class DocumentoObservacion extends AuditCreateSupport {

	@Id
	@GenericGenerator(name = "DOCUMENTO_OBS_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "DOCUMENTO_OBS_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENTO_OBS_SEQ")
	@Column(name = "DOB_ID")
	private Integer id;

	@Column(name = "DOB_OBS")
	private String texto;

	@ManyToOne
	@JoinColumn(name = "DOC_ID")
	private Documento documento;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

}
