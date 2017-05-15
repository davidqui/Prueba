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

/**
 * @author mcr
 *
 */
@Entity
@Table(name = "ARCHIVO")
@LaamLabel("Definición de archivo")
public class Archivo extends AuditActivoModifySupport {

	@Id
	@GenericGenerator(name = "ARCHIVO_SEQ", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "ARCHIVO_SEQ"),
			@Parameter(name = "allocationSize", value = "1") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARCHIVO_SEQ")
	@Column(name = "ARC_ID")
	private Integer id;

	@LaamLabel("Edificio")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 20)
	@ManyToOne
	@JoinColumn(name = "EDF_ID")
	@LaamWidget(value = "select", list = "edificios")
	private Edificio edificio;

	@LaamLabel("Piso")
	@LaamCreate(order = 20)
	@LaamListColumn(order = 30)
	@ManyToOne
	@JoinColumn(name = "PISO_ID")
	@LaamWidget(value = "select", list = "pisos")
	private Piso piso;

	@LaamLabel("Área")
	@LaamCreate(order = 10)
	@LaamListColumn(order = 20)
	@ManyToOne
	@JoinColumn(name = "AREA_ID")
	@LaamWidget(value = "select", list = "areas")
	private Area area;

	@LaamLabel("Módulo")
	@LaamCreate(order = 30)
	@LaamListColumn(order = 40)
	@ManyToOne
	@JoinColumn(name = "MOD_ID")
	@LaamWidget(value = "select", list = "modulos")
	private Modulo modulo;

	@LaamLabel("Estante")
	@LaamCreate(order = 40)
	@LaamListColumn(order = 50)
	@ManyToOne
	@JoinColumn(name = "EST_ID")
	@LaamWidget(value = "select", list = "estantes")
	private Estante estante;

	@LaamLabel("Entrepaño")
	@LaamCreate(order = 50)
	@LaamListColumn(order = 60)
	@ManyToOne
	@JoinColumn(name = "ENT_ID")
	@LaamWidget(value = "select", list = "entrepaños")
	private Entrepano Entrepano;

	@LaamLabel("Tipo Caja")
	@LaamCreate(order = 60)
	@LaamListColumn(order = 70)
	@ManyToOne
	@JoinColumn(name = "TCA_ID")
	@LaamWidget(value = "select", list = "cajas")
	private TipoCaja tipocaja;

	@LaamLabel("Número de caja")
	@LaamCreate(order = 70)
	@LaamListColumn(order = 80)
	@Column(name = "ARC_NUM_CAJA")
	private Integer numCaja;

	@LaamLabel("Tipo de almacenaje")
	@LaamCreate(order = 80)
	@LaamListColumn(order = 90)
	@ManyToOne
	@JoinColumn(name = "TAL_ID")
	private TipoAlmacenaje tipoAlmacenaje;

	@LaamLabel("Número de almacenaje")
	@LaamCreate(order = 90)
	@LaamListColumn(order = 100)
	@Column(name = "ARC_NUM_ALMACENAJE")
	private Integer numAlmacenaje;

	@OneToMany(mappedBy = "archivo")
	private List<ArchivoExpediente> expedientes;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Edificio getEdificio() {
		return edificio;
	}

	public void setEdificio(Edificio edificio) {
		this.edificio = edificio;
	}

	public Piso getPiso() {
		return piso;
	}

	public void setPiso(Piso piso) {
		this.piso = piso;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public Estante getEstante() {
		return estante;
	}

	public void setEstante(Estante estante) {
		this.estante = estante;
	}

	public Entrepano getEntrepano() {
		return Entrepano;
	}

	public void setEntrepano(Entrepano entrepano) {
		Entrepano = entrepano;
	}

	public TipoCaja getTipocaja() {
		return tipocaja;
	}

	public void setTipocaja(TipoCaja tipocaja) {
		this.tipocaja = tipocaja;
	}

	public Integer getNumCaja() {
		return numCaja;
	}

	public void setNumCaja(Integer numCaja) {
		this.numCaja = numCaja;
	}

	public TipoAlmacenaje getTipoAlmacenaje() {
		return tipoAlmacenaje;
	}

	public void setTipoAlmacenaje(TipoAlmacenaje tipoAlmacenaje) {
		this.tipoAlmacenaje = tipoAlmacenaje;
	}

	public Integer getNumAlmacenaje() {
		return numAlmacenaje;
	}

	public void setNumAlmacenaje(Integer numAlmacenaje) {
		this.numAlmacenaje = numAlmacenaje;
	}

	public List<ArchivoExpediente> getExpedientes() {
		return expedientes;
	}

	public void setExpedientes(List<ArchivoExpediente> expedientes) {
		this.expedientes = expedientes;
	}

}
