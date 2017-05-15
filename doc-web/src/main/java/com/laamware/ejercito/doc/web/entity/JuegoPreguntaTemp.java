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
@Table(name = "JUEGO_PREGUNTA_TEMP")
public class JuegoPreguntaTemp extends AuditActivoModifySupport {
	@Id
	@GenericGenerator(name = "JUEGO_PREGUNTA_TEMP_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "JUEGO_PREGUNTA_TEMP_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JUEGO_PREGUNTA_TEMP_SEQ")
	@Column(name = "JPRT_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "JPR_ID")
	private JuegoPregunta juegoPregunta;

	@ManyToOne
	@JoinColumn(name = "USU_ID")
	private Usuario usuario;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public JuegoPregunta getJuegoPregunta() {
		return juegoPregunta;
	}

	public void setJuegoPregunta(JuegoPregunta juegoPregunta) {
		this.juegoPregunta = juegoPregunta;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
