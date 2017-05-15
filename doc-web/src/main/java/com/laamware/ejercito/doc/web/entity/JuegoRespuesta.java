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
@Table(name = "JUEGO_RESPUESTA")
@LaamLabel("Respuesta")
public class JuegoRespuesta extends AuditActivoModifySupport {
	@Id
	@GenericGenerator(name = "JUEGO_RESPUESTA_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "JUEGO_RESPUESTA_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JUEGO_RESPUESTA_SEQ")
	@Column(name = "JRE_ID")
	private Integer id;

	@LaamLabel("Texto de la respuesta")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "JRE_TEXTO")
	private String textoRespuesta;

	@ManyToOne
	@JoinColumn(name = "JPR_ID")
	private JuegoPregunta juegoPregunta;

	@LaamLabel("¿Es es la respusta correcta?")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "JRE_CORRECTA")
	private Boolean esCorrecta;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTextoRespuesta() {
		return textoRespuesta;
	}

	public void setTextoRespuesta(String textoRespuesta) {
		this.textoRespuesta = textoRespuesta;
	}

	public JuegoPregunta getJuegoPregunta() {
		return juegoPregunta;
	}

	public void setJuegoPregunta(JuegoPregunta juegoPregunta) {
		this.juegoPregunta = juegoPregunta;
	}

	public Boolean getEsCorrecta() {
		return esCorrecta;
	}

	public void setEsCorrecta(Boolean esCorrecta) {
		this.esCorrecta = esCorrecta;
	}

}
