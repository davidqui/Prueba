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
@Table(name = "PROCESO_VAR")
public class VariableProceso extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "PROCESO_VAR_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "PROCESO_VAR_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESO_VAR_SEQ")
	@Column(name = "PVA_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "PRO_ID")
	private Proceso proceso;

	@LaamCreate(order = 10)
	@LaamLabel("Nombre de la variable")
	@LaamListColumn(order = 10)
	@Column(name = "PVA_KEY")
	private String key;

	@LaamCreate(order = 20)
	@LaamLabel("Valor de la variable")
	@LaamListColumn(order = 20)
	@Column(name = "PVA_VALUE")
	private String value;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Proceso getProceso() {
		return proceso;
	}

	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
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
