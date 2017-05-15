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
 * Entidad que representa un documento asignado a bandeja de consulta.
 * 
 * @author jgarcia@controltechcg.com
 * @since Abril 19, 2017
 *
 */
// 2017-04-18 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech)
@Entity
@Table(name = "DOCUMENTO_EN_CONSULTA")
public class DocumentoEnConsulta extends AuditActivoModifySupport {

	/** ID del registro. */
	@Id
	@Column(name = "DEC_ID")
	@GenericGenerator(name = "DOCUMENTO_EN_CONSULTA_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "DOCUMENTO_EN_CONSULTA_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENTO_EN_CONSULTA_SEQ")
	private Integer id;

	/** Documento asociado */
	@ManyToOne
	@JoinColumn(name = "DOC_ID")
	private Documento documento;

	/**
	 * Constructor.
	 */
	public DocumentoEnConsulta() {
		super();
	}

	/**
	 * Obtiene el ID del registro.
	 * 
	 * @return ID.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Establece el ID del registro.
	 * 
	 * @param id
	 *            ID.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Obtiene el documento.
	 * 
	 * @return Documento.
	 */
	public Documento getDocumento() {
		return documento;
	}

	/**
	 * Establece el documento.
	 * 
	 * @param documento
	 *            Documento.
	 */
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	@Override
	public String toString() {
		return "DocumentoEnConsulta [getId()=" + getId() + ", getDocumento()=" + getDocumento() + ", getActivo()="
				+ getActivo() + ", getQuien()=" + getQuien() + "]";
	}

}
