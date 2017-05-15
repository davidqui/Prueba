package com.laamware.ejercito.doc.web.entity;

import java.util.Date;

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
@Table(name = "EXPEDIENTE_ESTADO")
public class ExpedienteEstado extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "EXPEDIENTE_ESTADO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "EXPEDIENTE_ESTADO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXPEDIENTE_ESTADO_SEQ")
	@Column(name = "EXES_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "ESEX_ID")
	private EstadoExpediente estado;

	@ManyToOne
	@JoinColumn(name = "EXP_ID")
	private Expediente expediente;

	@ManyToOne
	@JoinColumn(name = "USU_ID_TRANSFERENCIA")
	private Usuario usuarioTransferencia;

	@Column(name = "EXES_FCH_TRANSFERENCIA")
	private Date fechaTransferencia;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public EstadoExpediente getEstado() {
		return estado;
	}

	public void setEstado(EstadoExpediente estado) {
		this.estado = estado;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public Usuario getUsuarioTransferencia() {
		return usuarioTransferencia;
	}

	public void setUsuarioTransferencia(Usuario usuarioTransferencia) {
		this.usuarioTransferencia = usuarioTransferencia;
	}

	public Date getFechaTransferencia() {
		return fechaTransferencia;
	}

	public void setFechaTransferencia(Date fechaTransferencia) {
		this.fechaTransferencia = fechaTransferencia;
	}

}
