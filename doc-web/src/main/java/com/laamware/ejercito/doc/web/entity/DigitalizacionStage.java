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
@Table(name = "DIGI_STAGE")
public class DigitalizacionStage {

	public static final Integer TIPO_SCANNER = 0;
	public static final Integer TIPO_FILE = 1;
	
	@Id
	@GenericGenerator(name = "DIGI_STAGE_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "DIGI_STAGE_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DIGI_STAGE_SEQ")
	@Column(name = "DST_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "DOC_ID")
	private Documento documento;

	@ManyToOne
	@JoinColumn(name = "USU_ID")
	private Usuario usuario;

	@Column(name = "DST_ARCHIVOS")
	private String archivos;

	@Column(name = "DST_TIPO")
	private Integer tipo;

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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getArchivos() {
		return archivos;
	}

	public void setArchivos(String archivos) {
		this.archivos = archivos;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

}
