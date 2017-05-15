package com.laamware.ejercito.doc.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "TIPO_DESTINATARIO")
@LaamLabel("Tipos de destinatario")
public class TipoDestinatario extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "TIPO_DESTINATARIO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "TIPO_DESTINATARIO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPO_DESTINATARIO_SEQ")
	@Column(name = "TDE_ID")
	private Integer id;

	@Column(name = "TDE_NOMBRE")
	@LaamLabel("Nombre")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	private String nombre;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return nombre;
	}
}
