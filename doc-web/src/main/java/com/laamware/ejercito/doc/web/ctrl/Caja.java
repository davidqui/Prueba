package com.laamware.ejercito.doc.web.ctrl;

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

import com.laamware.ejercito.doc.web.entity.AuditActivoCreateSupport;
import com.laamware.ejercito.doc.web.entity.Entrepano;
import com.laamware.ejercito.doc.web.entity.LaamCreate;
import com.laamware.ejercito.doc.web.entity.LaamLabel;
import com.laamware.ejercito.doc.web.entity.LaamListColumn;
import com.laamware.ejercito.doc.web.entity.LaamWidget;
import com.laamware.ejercito.doc.web.entity.TipoCaja;

/**
 * @author mcr
 *
 */
@Entity
@Table(name = "CAJA")
@LaamLabel("Definición de caja")
public class Caja extends AuditActivoCreateSupport {

	@Id
	@GenericGenerator(name = "CAJA_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "CAJA_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAJA_SEQ")
	@Column(name = "CJ_ID")
	private Integer id;

	@LaamLabel("Nombre")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 10)
	@Column(name = "CJ_NOMBRE")
	private String nombre;

	@LaamLabel("Código")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 30)
	@Column(name = "CJ_CODIGO")
	private String codigo;

	@LaamLabel("Tipo de caja")
	@LaamCreate(order = 40)
	@LaamListColumn(order = 50)
	@ManyToOne
	@JoinColumn(name = "TC_ID")
	@LaamWidget(value = "select", list = "tipoCajas")
	private TipoCaja tipoCaja;

	@LaamLabel("Entrepaño")
	@LaamCreate(order = 50)
	@LaamListColumn(order = 60)
	@ManyToOne
	@JoinColumn(name = "ENT_ID")
	@LaamWidget(value = "select", list = "entrepanos")
	private Entrepano entrepano;

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

	public TipoCaja getTipoCaja() {
		return tipoCaja;
	}

	public void setTipoCaja(TipoCaja tipoCaja) {
		this.tipoCaja = tipoCaja;
	}

	public Entrepano getEntrepano() {
		return entrepano;
	}

	public void setEntrepano(Entrepano entrepano) {
		this.entrepano = entrepano;
	}

}
