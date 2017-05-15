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
 * @author mcr
 *
 */
@Entity
@Table(name = "DESTINATARIO")
@LaamLabel("Destinatarios")
public class Destinatario extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "DESTINATARIO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "DESTINATARIO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DESTINATARIO_SEQ")
	@Column(name = "DES_ID")
	private Integer id;

	@LaamLabel("Número de documento de identificación")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "DES_DOCUMENTO")
	private String documento;

	@LaamLabel("Sigla")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 20)
	@Column(name = "DES_SIGLA")
	private String sigla;

	@LaamLabel("Dirección")
	@LaamCreate(order = 20)
	@Column(name = "DES_DIRECCION")
	private String direccion;

	@LaamLabel("Nombre")
	@LaamCreate(order = 30)
	@LaamListColumn(order = 30)
	@Column(name = "DES_NOMBRE")
	private String nombre;

	@LaamLabel("Tipo")
	@LaamCreate(order = 40)
	@LaamListColumn(order = 40)
	@LaamWidget(value = "select", list = "tiposDestinatario")
	@ManyToOne
	@JoinColumn(name = "TDE_ID")
	private TipoDestinatario tipoDestinatario;

	@Column(name = "REF_ID")
	private Integer refId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getRefId() {
		return refId;
	}

	public void setRefId(Integer refId) {
		this.refId = refId;
	}

	public TipoDestinatario getTipoDestinatario() {
		return tipoDestinatario;
	}

	public void setTipoDestinatario(TipoDestinatario tipoDestinatario) {
		this.tipoDestinatario = tipoDestinatario;
	}

	@Override
	public String toString() {
		return tipoDestinatario.getNombre() + " - " + nombre;
	}
}
