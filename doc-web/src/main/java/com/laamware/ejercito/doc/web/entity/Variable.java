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
@Table(name = "PROCESO_INSTANCIA_VAR")
public class Variable extends AuditActivoModifySupport {

	/**
	 * Variable que controla la página en la que se visualiza la instancia de
	 * proceso
	 */
	public static final String VAR_HOME = "var_home";

	public static final String VAR_DOC_FORM_TEMPLATE = "var_doc_form_template";

	public static final String PROC_CURRENT_LOCATION = "proc.current.location";

	@Id
	@GenericGenerator(name = "PROCESO_INSTANCIA_VAR_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "PROCESO_INSTANCIA_VAR_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESO_INSTANCIA_VAR_SEQ")
	@Column(name = "PIV_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "PIN_ID")
	private Instancia instancia;

	@Column(name = "PIV_KEY")
	private String key;

	@Column(name = "PIV_VALUE")
	private String value;

	public Variable(String key, String value, Instancia instancia) {
		this.key = key;
		this.value = value;
		this.instancia = instancia;
	}

	public Variable() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Instancia getInstancia() {
		return instancia;
	}

	public void setInstancia(Instancia instancia) {
		this.instancia = instancia;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
