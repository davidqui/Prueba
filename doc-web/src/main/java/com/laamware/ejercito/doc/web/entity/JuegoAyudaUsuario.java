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
@Table(name = "JUEGO_AYUDA_USUARIO")
public class JuegoAyudaUsuario extends AuditActivoCreateSupport {

	@Id
	@GenericGenerator(name = "JUEGO_AYUDA_USUARIO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "JUEGO_AYUDA_USUARIO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JUEGO_AYUDA_USUARIO_SEQ")
	@Column(name = "JAU_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "JNI_ID")
	private JuegoNivel juegoNivel;

	@ManyToOne
	@JoinColumn(name = "USU_ID")
	private Usuario usuario;

	@Column(name = "AYUDA1")
	private Boolean ayuda1;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public JuegoNivel getJuegoNivel() {
		return juegoNivel;
	}

	public void setJuegoNivel(JuegoNivel juegoNivel) {
		this.juegoNivel = juegoNivel;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Boolean getAyuda1() {
		return ayuda1;
	}

	public void setAyuda1(Boolean ayuda1) {
		this.ayuda1 = ayuda1;
	}

}
