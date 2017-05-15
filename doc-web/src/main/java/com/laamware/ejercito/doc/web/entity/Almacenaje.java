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

import com.laamware.ejercito.doc.web.ctrl.Caja;

/**
 * @author mcr
 *
 */
@Entity
@Table(name = "ALMACENAJE")
@LaamLabel("Definición de almacenaje")
public class Almacenaje extends AuditActivoCreateSupport {

	@Id
	@GenericGenerator(name = "ALMACENAJE_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "ALMACENAJE_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ALMACENAJE_SEQ")
	@Column(name = "ALM_ID")
	private Integer id;

	@LaamLabel("Nombre")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "ALM_NOMBRE")
	private String nombre;

	@LaamLabel("Código")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 30)
	@Column(name = "ALM_CODIGO")
	private String codigo;

	@LaamLabel("Tipo de almacenaje")
	@LaamCreate(order = 40)
	@LaamListColumn(order = 50)
	@ManyToOne
	@JoinColumn(name = "TAL_ID")
	@LaamWidget(value = "select", list = "tipoAlmacenajes")
	private TipoCaja tipoAlmacenaje;

	@LaamLabel("Caja")
	@LaamCreate(order = 50)
	@LaamListColumn(order = 60)
	@ManyToOne
	@JoinColumn(name = "CJ_ID")
	@LaamWidget(value = "select", list = "cajas")
	private Caja caja;

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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public TipoCaja getTipoAlmacenaje() {
		return tipoAlmacenaje;
	}

	public void setTipoAlmacenaje(TipoCaja tipoAlmacenaje) {
		this.tipoAlmacenaje = tipoAlmacenaje;
	}

	public Caja getCaja() {
		return caja;
	}

	public void setCaja(Caja caja) {
		this.caja = caja;
	}

}
