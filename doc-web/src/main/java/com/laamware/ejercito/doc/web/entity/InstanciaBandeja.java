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
@Table(name = "INSTANCIA_BANDEJA")
public class InstanciaBandeja extends AuditActivoModifySupport {

	public static final String ENTRADA = "ENTRADA";

	@Id
	@GenericGenerator(name = "INSTANCIA_BANDEJA_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "INSTANCIA_BANDEJA_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INSTANCIA_BANDEJA_SEQ")
	@Column(name = "ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "PIN_ID")
	private Instancia instancia;

	@Column(name = "BANDEJA")
	private String bandeja;

	@ManyToOne
	@JoinColumn(name = "USU_ID")
	private Usuario usuario;

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

	public String getBandeja() {
		return bandeja;
	}

	public void setBandeja(String bandeja) {
		this.bandeja = bandeja;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
