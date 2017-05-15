package com.laamware.ejercito.doc.web.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DOCUMENTO_ADJUNTO")
public class Adjunto extends AuditActivoCreateSupport {

	public static Adjunto create() {
		Adjunto x = new Adjunto();
		x.id = UUID.randomUUID().toString().replaceAll("-", "");
		return x;
	}

	@Id
	@Column(name = "DAD_ID")
	private String id;

	@ManyToOne
	@JoinColumn(name = "TDO_ID")
	private Tipologia tipologia;

	@ManyToOne
	@JoinColumn(name = "DOC_ID")
	private Documento documento;

	@Column(name = "DAD_CONTENT")
	private String contenido;

	@Column(name = "DAD_ORIGINAL")
	private String original;

	@LaamLabel("Descripción")
	@LaamCreate(order = 50)
	@LaamWidget("textarea")
	@Column(name = "DAD_DESCRIPCION")
	private String descripcion;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Tipologia getTipologia() {
		return tipologia;
	}

	public void setTipologia(Tipologia tipologia) {
		this.tipologia = tipologia;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
