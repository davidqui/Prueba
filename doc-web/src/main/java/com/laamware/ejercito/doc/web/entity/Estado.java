package com.laamware.ejercito.doc.web.entity;

import java.util.ArrayList;
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
@Table(name = "PROCESO_ESTADO")
public class Estado extends AuditActivoModifySupport {

	public static final Integer ANULADO = 83;
	// 2017-02-09 jgarcia@controltechcg.com Issue #96: Establecimiento de nuevos
	// estados configurados.
	public static final Integer ANULADO_NEW = 101;
	public static final Integer APROBADO = 55;
	public static final Integer ARCHIVADO = 48;
	// 2017-02-09 jgarcia@controltechcg.com Issue #96: Establecimiento de nuevos
	// estados configurados.
	public static final Integer ARCHIVADO_NEW = 102;
	public static final Integer CON_VISTO_BUENO = 57;
	public static final Integer DIGITALIZACION = 44;
	public static final Integer EN_CONSTRUCCION = 61;
	public static final Integer EN_PROCESO_DE_RESPUESTA = 52;
	public static final Integer EN_REVISION_JEFE_DEPENDENCIA = 54;
	public static final Integer ENTREGA_DE_DOCUMENTOS = 45;
	public static final Integer ENVIADO = 49;
	public static final Integer PENDIENTE = 58;
	public static final Integer PENDIENTE_DE_ARCHIVAR = 81;
	public static final Integer PENDIENTE_DE_RESPUESTA = 82;
	public static final Integer PENDIENTE_VISTO_BUENO = 56;
	public static final Integer RECEPCION_DOCUMENTO = 42;
	public static final Integer REVISION_JEFE_DEPENDENCIA = 47;
	public static final Integer REVISIÓN_JEFE_JEFATURA = 46;
	public static final Integer STICKER = 43;

	@Id
	@GenericGenerator(name = "PROCESO_ESTADO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "PROCESO_ESTADO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESO_ESTADO_SEQ")
	@Column(name = "PES_ID")
	private Integer id;

	@LaamLabel("Nombre del estado")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "PES_NOMBRE")
	private String nombre;

	@LaamLabel("Descripción")
	@LaamCreate(order = 20)
	@Column(name = "PES_DESCRIPCION")
	private String descripcion;

	@ManyToOne
	@JoinColumn(name = "PRO_ID")
	private Proceso proceso;

	@LaamLabel("¿Es estado inicial?")
	@LaamCreate(order = 30)
	@LaamListColumn(order = 20)
	@Column(name = "PES_INICIAL")
	private Boolean esInicial = Boolean.FALSE;

	@LaamLabel("¿Es estado final?")
	@LaamCreate(order = 40)
	@LaamListColumn(order = 30)
	@Column(name = "PES_FINAL")
	private Boolean esFinal = Boolean.FALSE;

	@OneToMany
	@JoinColumn(name = "PES_ID_INICIAL")
	private List<Transicion> transiciones;

	@LaamLabel("URL del estado")
	@LaamCreate(order = 50)
	@Column(name = "PES_LOCATION")
	private String location;

	@LaamLabel("Tipo de reasignación")
	@LaamCreate(order = 60)
	@Column(name = "PES_REASIGNACION")
	private Integer reasignacion;

	@LaamLabel("¿Es estado detenido?")
	@LaamCreate(order = 60)
	@Column(name = "PES_TRUNCATED")
	private Boolean esTruncado;

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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Proceso getProceso() {
		return proceso;
	}

	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}

	@Override
	public String toString() {
		return nombre;
	}

	public Boolean getEsInicial() {
		return esInicial;
	}

	public void setEsInicial(Boolean esInicial) {
		this.esInicial = esInicial;
	}

	public Boolean getEsFinal() {
		return esFinal;
	}

	public void setEsFinal(Boolean esFinal) {
		this.esFinal = esFinal;
	}

	public List<Transicion> getTransiciones() {
		return transiciones;
	}

	public void setTransiciones(List<Transicion> transiciones) {
		this.transiciones = transiciones;
	}

	public List<Transicion> getTransicionesActivas() {
		/*
		 * 2017-03-17 jgarcia@controltechcg.com Issue #26 (SIGDI-Incidencias01):
		 * Manejo para corregir NullPointerException en la obtención de la lista
		 * de transiciones activas por Estado.
		 */
		List<Transicion> activas = new ArrayList<Transicion>();
		if (getTransiciones() == null) {
			return activas;
		}

		for (Transicion transicion : getTransiciones()) {
			if (transicion.getActivo()) {
				activas.add(transicion);
			}
		}

		return activas;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getReasignacion() {
		return reasignacion;
	}

	public void setReasignacion(Integer reasignacion) {
		this.reasignacion = reasignacion;
	}

	public Boolean getEsTruncado() {
		return esTruncado;
	}

	public void setEsTruncado(Boolean esTruncado) {
		this.esTruncado = esTruncado;
	}

}
