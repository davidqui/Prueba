package com.laamware.ejercito.doc.web.entity;

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

@Entity
@Table(name = "JUEGO_PREGUNTA")
@LaamLabel("Pregunta")
public class JuegoPregunta extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "JUEGO_PREGUNTA_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "JUEGO_PREGUNTA_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JUEGO_PREGUNTA_SEQ")
	@Column(name = "JPR_ID")
	private Integer id;

	@LaamLabel("Texto de la pregunta")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "JPR_TEXTO")
	private String textoPregunta;

	@ManyToOne
	@JoinColumn(name = "JNI_ID")
	private JuegoNivel juegoNivel;

	@OneToMany
	@JoinColumn(name = "JPR_ID")
	private List<JuegoRespuesta> respuestas;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTextoPregunta() {
		return textoPregunta;
	}

	public void setTextoPregunta(String textoPregunta) {
		this.textoPregunta = textoPregunta;
	}

	public JuegoNivel getJuegoNivel() {
		return juegoNivel;
	}

	public void setJuegoNivel(JuegoNivel juegoNivel) {
		this.juegoNivel = juegoNivel;
	}

	public List<JuegoRespuesta> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(List<JuegoRespuesta> respuestas) {
		this.respuestas = respuestas;
	}

}
