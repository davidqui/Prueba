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
@Table(name = "JUEGO_NIVEL")
@LaamLabel("Nivel de dificultad")
public class JuegoNivel extends AuditActivoModifySupport {
	
	@Id
	@GenericGenerator(name = "JUEGO_NIVEL_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "JUEGO_NIVEL_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JUEGO_NIVEL_SEQ")
	@Column(name = "JNI_ID")
	private Integer id;
	
	@LaamLabel("Nombre del nivel")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "JNI_NOMBRE")
	private String nombre;
	
	@ManyToOne
	@JoinColumn(name = "JJU_ID")
	private Juego juego;
	
	@LaamLabel("Número de preguntas")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "JNI_NUM_PREGUNTAS")
	private Integer numeroPregutas;
	
	@LaamLabel("Premio")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "JNI_PREMIO")
	private String premio;
	
	@LaamLabel("Grado de dificultad")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "JNI_DIFICULTAD")
	private Integer dificultad;

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

	public Juego getJuego() {
		return juego;
	}

	public void setJuego(Juego juego) {
		this.juego = juego;
	}

	public Integer getNumeroPregutas() {
		return numeroPregutas;
	}

	public void setNumeroPregutas(Integer numeroPregutas) {
		this.numeroPregutas = numeroPregutas;
	}

	public String getPremio() {
		return premio;
	}

	public void setPremio(String premio) {
		this.premio = premio;
	}

	public Integer getDificultad() {
		return dificultad;
	}

	public void setDificultad(Integer dificultad) {
		this.dificultad = dificultad;
	}
		
	
}
