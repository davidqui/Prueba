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
@Table(name = "DOCUMENTO_DEPENDENCIA")
public class DocumentoDependencia extends AuditActivoCreateSupport {

	@Id
	@GenericGenerator(name = "DOCUMENTO_DEPENDENCIA_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "DOCUMENTO_DEPENDENCIA_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENTO_DEPENDENCIA_SEQ")
	@Column(name = "DCDP_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "DOC_ID")
	private Documento documento;

	@ManyToOne
	@JoinColumn(name = "DEP_ID")
	private Dependencia dependencia;

	@ManyToOne
	@JoinColumn(name = "TRD_ID")
	private Trd trd;

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

	public Dependencia getDependencia() {
		return dependencia;
	}

	public void setDependencia(Dependencia dependencia) {
		this.dependencia = dependencia;
	}

	public Trd getTrd() {
		return trd;
	}

	public void setTrd(Trd trd) {
		this.trd = trd;
	}

}
