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
@Table(name = "JUEGO")
@LaamLabel("Configurar juego")
public class Juego extends AuditActivoModifySupport {
	
	@Id
	@GenericGenerator(name = "JUEGO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "JUEGO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JUEGO_SEQ")
	@Column(name = "JJU_ID")
	private Integer id;
	
	@Column(name = "JJU_NOMBRE")
	@LaamListColumn(order = 10)
	@LaamCreate(order = 10)
	@LaamLabel("Tema de la capacitación")
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
	
	
	

}
