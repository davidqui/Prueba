package com.laamware.ejercito.doc.web.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author mcr
 *
 */
@Entity
@Table(name = "PRESTAMO_SOLICITUD")
public class PrestamoSolicitud extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "PRESTAMO_SOLICITUD_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "PRESTAMO_SOLICITUD_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRESTAMO_SOLICITUD_SEQ")
	@Column(name = "PRESOL_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "USU_ID_SOLICITA")
	private Usuario usuarioSolicita;

	@ManyToOne
	@JoinColumn(name = "DEP_ID")
	private Dependencia dependencia;

	@OneToMany(mappedBy = "documento")
	private List<DocumentoPrestamo> docs;

	@Column(name = "PRESOL_FCH")
	private Date fechaSolicitud;

	@Column(name = "PRESOL_FCH_DEVOLUCION")
	private Date fechaDevolucion;

	@ManyToOne
	@JoinColumn(name = "EXP_ID")
	private Expediente expediente;

	@ManyToOne
	@JoinColumn(name = "DOC_ID")
	private Documento documento;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Usuario getUsuarioSolicita() {
		return usuarioSolicita;
	}

	public void setUsuarioSolicita(Usuario usuarioSolicita) {
		this.usuarioSolicita = usuarioSolicita;
	}

	public Dependencia getDependencia() {
		return dependencia;
	}

	public void setDependencia(Dependencia dependencia) {
		this.dependencia = dependencia;
	}

	public List<DocumentoPrestamo> getDocs() {
		return docs;
	}

	public void setDocs(List<DocumentoPrestamo> docs) {
		this.docs = docs;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public Date getFechaDevolucion() {
		return fechaDevolucion;
	}

	public void setFechaDevolucion(Date fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

}
