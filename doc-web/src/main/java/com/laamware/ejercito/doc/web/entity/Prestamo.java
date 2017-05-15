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
@Table(name = "PRESTAMO")
public class Prestamo extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "PRESTAMO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "PRESTAMO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRESTAMO_SEQ")
	@Column(name = "PRE_ID")
	private Integer id;

	@Column(name = "PRE_CODIGO")
	private String codigo;

	@OneToMany(mappedBy = "documento")
	private List<DocumentoPrestamo> docs;

	@ManyToOne
	@JoinColumn(name = "USU_ID_PRESTA")
	private Usuario funcionarioPresta;

	@ManyToOne
	@JoinColumn(name = "USU_ID_SOLICITA")
	private Usuario usuarioSolicita;

	@ManyToOne
	@JoinColumn(name = "DEP_ID")
	private Dependencia dependencia;

	@Column(name = "PRE_FCH_PRESTAMO")
	private Date fechaPrestamo;

	@Column(name = "PRE_FCH_DEVOLUCION")
	private Date fechaDevolucion;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public List<DocumentoPrestamo> getDocs() {
		return docs;
	}

	public void setDocs(List<DocumentoPrestamo> docs) {
		this.docs = docs;
	}

	public Usuario getFuncionarioPresta() {
		return funcionarioPresta;
	}

	public void setFuncionarioPresta(Usuario funcionarioPresta) {
		this.funcionarioPresta = funcionarioPresta;
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

	public Date getFechaPrestamo() {
		return fechaPrestamo;
	}

	public void setFechaPrestamo(Date fechaPrestamo) {
		this.fechaPrestamo = fechaPrestamo;
	}

	public Date getFechaDevolucion() {
		return fechaDevolucion;
	}

	public void setFechaDevolucion(Date fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}

}
