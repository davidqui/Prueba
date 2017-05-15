package com.laamware.ejercito.doc.web.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "PROCESO_TRANSICION")
@LaamLabel("Transiciones")
public class Transicion extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "PROCESO_TRANSICION_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "PROCESO_TRANSICION_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESO_TRANSICION_SEQ")
	@Column(name = "PTR_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "PTT_ID")
	@LaamCreate(order = 10)
	@LaamLabel("Tipo de transición")
	@LaamWidget(value = "select", list = "tipos")
	@LaamListColumn(order = 20)
	private TipoTransicion tipo;

	@Column(name = "PTR_DEFINICION")
	@LaamCreate(order = 30)
	@LaamLabel("Definición de la transición")
	@LaamWidget("textarea")
	private String definicion;

	@Column(name = "PTR_NOMBRE")
	@LaamCreate(order = 20)
	@LaamLabel("Nombre de la transición")
	@LaamListColumn(order = 10)
	private String nombre;

	@ManyToOne
	@JoinColumn(name = "PES_ID_INICIAL")
	private Estado estadoInicial;

	@ManyToOne
	@JoinColumn(name = "PES_ID_FINAL")
	@LaamCreate(order = 40)
	@LaamLabel("Estado final")
	@LaamListColumn(order = 30)
	@LaamWidget(value = "select", list = "estados")
	private Estado estadoFinal;

	@OneToMany
	@JoinColumn(name = "PTR_ID")
	private List<Condicion> condiciones;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TipoTransicion getTipo() {
		return tipo;
	}

	public void setTipo(TipoTransicion tipo) {
		this.tipo = tipo;
	}

	public String getDefinicion() {
		return definicion;
	}

	public void setDefinicion(String definicion) {
		this.definicion = definicion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Estado getEstadoInicial() {
		return estadoInicial;
	}

	public void setEstadoInicial(Estado estadoInicial) {
		this.estadoInicial = estadoInicial;
	}

	public Estado getEstadoFinal() {
		return estadoFinal;
	}

	public void setEstadoFinal(Estado estadoFinal) {
		this.estadoFinal = estadoFinal;
	}

	@Override
	public String toString() {
		return this.nombre;
	}

	public String replace(Instancia i) {
		return replace(this.definicion, i);
	}

	public String replace(String value, Instancia i) {		
		String x = value;

		Map<String, String> map = new HashMap<String, String>();

		// Reemplaza con propiedades de la transición
		map.put("transicion.id", getId().toString());

		// Reemplaza con propiedades de la instancia
		map.put("instancia.id", i.getId());

		// Reemplaza con las variables de la instancia
		map.putAll(i.getVariablesAsMap());

		return replaceMap(x, map);
	}

	private String replaceMap(String str, Map<String, String> map) {
		if (StringUtils.isBlank(str))
			return "";
		String x = str;
		for (Entry<String, String> entry : map.entrySet()) {
			x = x.replaceAll("\\{" + entry.getKey() + "\\}", entry.getValue());
		}
		return x;
	}

	public List<Condicion> getCondiciones() {
		return condiciones;
	}

	public void setCondiciones(List<Condicion> condiciones) {
		this.condiciones = condiciones;
	}

}
