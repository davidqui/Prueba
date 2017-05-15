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
@Table(name = "BANDEJA")
@LaamLabel("Definición de bandejas")
public class Bandeja extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "BANDEJA_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "BANDEJA_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BANDEJA_SEQ")
	@Column(name = "BAN_ID")
	private Integer id;

	@Column(name = "BAN_CODIGO")
	@LaamLabel("Código único")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	private String codigo;

	@Column(name = "BAN_NOMBRE")
	@LaamLabel("Nombre de la bandeja")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 20)
	private String nombre;

	@Column(name = "BAN_VIEW_SQL")
	@LaamLabel("SQL de la vista")
	@LaamCreate(order = 30)
	@LaamWidget(value = "textarea")
	private String viewSql;

	@Column(name = "BAN_COMPILA")
	@LaamLabel("¿Compila?")
	@LaamListColumn(order = 40)
	private Boolean compila = Boolean.FALSE;

	@Column(name = "BAN_ORDEN")
	@LaamLabel("Orden")
	@LaamCreate(order = 35)
	@LaamListColumn(order = 35)
	private Integer orden;

	/**
	 * Construye la consulta parametrizada para obtener los elementos de la
	 * bandeja
	 * 
	 * @return
	 */
	public String getQuery() {
		StringBuilder b = new StringBuilder();
		b.append("SELECT * FROM VB_").append(this.codigo)
				.append(" WHERE USU_ID = ? ORDER BY fecha desc");
		return b.toString();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getViewSql() {
		return viewSql;
	}

	public void setViewSql(String viewSql) {
		this.viewSql = viewSql;
	}

	public Boolean getCompila() {
		return compila;
	}

	public void setCompila(Boolean compila) {
		this.compila = compila;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

}
