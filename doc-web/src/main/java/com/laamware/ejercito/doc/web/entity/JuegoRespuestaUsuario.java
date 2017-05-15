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
@Table(name = "JUEGO_RESPUESTA_USUARIO")
public class JuegoRespuestaUsuario extends AuditActivoCreateSupport {

	@Id
	@GenericGenerator(name = "JUEGO_RESPUESTA_USUARIO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "JUEGO_RESPUESTA_USUARIO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JUEGO_RESPUESTA_USUARIO_SEQ")
	@Column(name = "JRU_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "JPR_ID")
	private JuegoPregunta pregunta;

	@ManyToOne
	@JoinColumn(name = "JRE_ID")
	private JuegoRespuesta juegoRespuesta;

	@ManyToOne
	@JoinColumn(name = "USU_ID")
	private Usuario usuario;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public JuegoPregunta getPregunta() {
		return pregunta;
	}

	public void setPregunta(JuegoPregunta pregunta) {
		this.pregunta = pregunta;
	}

	public JuegoRespuesta getJuegoRespuesta() {
		return juegoRespuesta;
	}

	public void setJuegoRespuesta(JuegoRespuesta juegoRespuesta) {
		this.juegoRespuesta = juegoRespuesta;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
