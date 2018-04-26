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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import com.laamware.ejercito.doc.web.util.NumeroVersionIdentificable;

@Entity
@Table(name = "TRD")
@LaamLabel("Tabla de retención documental")
public class Trd extends AuditActivoModifySupport implements NumeroVersionIdentificable {

    @Id
    @GenericGenerator(name = "TRD_SEQ", strategy = "sequence", parameters = {
        @Parameter(name = "sequence", value = "TRD_SEQ")
        ,
			@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRD_SEQ")
    @Column(name = "TRD_ID")
    private Integer id;

    @LaamLabel("Nombre")
    @LaamCreate(order = 10)
    @LaamListColumn(order = 10)
    @Column(name = "TRD_NOMBRE")
    private String nombre;

    @LaamLabel("Código")
    @LaamCreate(order = 20)
    @LaamListColumn(order = 20)
    @Column(name = "TRD_CODIGO")
    private String codigo;

    @LaamLabel("Serie")
    @LaamCreate(order = 30)
    @LaamListColumn(order = 30)
    @LaamWidget(value = "select", list = "series", type = Trd.class)
    @Column(name = "TRD_SERIE")
    private Integer serie;

    @LaamLabel("RAG (Años)")
    @LaamCreate(order = 40)
    @LaamListColumn(order = 40)
    @Column(name = "TRD_RET_ARCHIVO_GENERAL")
    private Integer retArchivoGeneral;

    @LaamLabel("RAC (Años)")
    @LaamCreate(order = 50)
    @LaamListColumn(order = 50)
    @Column(name = "TRD_RET_ARCHIVO_CENTRAL")
    private Integer retArchivoCentral;

    @LaamLabel("DCT")
    @LaamCreate(order = 60)
    @LaamListColumn(order = 60)
    @Column(name = "TRD_DIS_CT")
    private Boolean disposicionDT;

    @LaamLabel("DD")
    @LaamCreate(order = 70)
    @LaamListColumn(order = 70)
    @Column(name = "TRD_DIS_D")
    private Boolean disposicionD;

    @LaamLabel("DM")
    @LaamCreate(order = 80)
    @LaamListColumn(order = 80)
    @Column(name = "TRD_DIS_M")
    private Boolean disposicionM;

    @LaamLabel("DS")
    @LaamCreate(order = 90)
    @LaamListColumn(order = 90)
    @Column(name = "TRD_DIS_S")
    private Boolean disposicionS;

    @LaamLabel("DE")
    @LaamCreate(order = 100)
    @LaamListColumn(order = 100)
    @Column(name = "TRD_DIS_E")
    private Boolean disposicionE;

    @LaamLabel("Procedimiento")
    @LaamCreate(order = 120)
    @LaamWidget("editor")
    @Column(name = "TRD_PROCEDIMIENTO")
    private String procedimiento;

    @LaamLabel("Plantilla")
    @LaamCreate(order = 110)
    @LaamWidget(value = "select", list = "plantillas")
    @ManyToOne
    @JoinColumn(name = "PLA_ID")
    private Plantilla plantilla;

    @LaamLabel("Tipo de documento")
    @LaamCreate(order = 120)
    @LaamWidget(value = "select", list = "tiposDocumento")
    @ManyToOne
    @JoinColumn(name = "TDC_ID")
    private TipoDocumento tipoDocumento;

    @LaamLabel("Plazo en días de los documento")
    @Column(name = "TRD_PLAZO")
    @LaamCreate(order = 130)
    private Integer plazo;

    @Transient
    private List<Documento> documentos = new ArrayList<Documento>();

    /*
     * 2018-04-25 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
     * feature-151: Identificación del campo tipo número de versión.
     */
    @Override
    public String numeroVersion() {
        return getCodigo();
    }

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

    public Integer getSerie() {
        return serie;
    }

    public void setSerie(Integer serie) {
        this.serie = serie;
    }

    public Integer getRetArchivoGeneral() {
        return retArchivoGeneral;
    }

    public void setRetArchivoGeneral(Integer retArchivoGeneral) {
        this.retArchivoGeneral = retArchivoGeneral;
    }

    public Integer getRetArchivoCentral() {
        return retArchivoCentral;
    }

    public void setRetArchivoCentral(Integer retArchivoCentral) {
        this.retArchivoCentral = retArchivoCentral;
    }

    public Boolean getDisposicionDT() {
        return disposicionDT;
    }

    public void setDisposicionDT(Boolean disposicionDT) {
        this.disposicionDT = disposicionDT;
    }

    public Boolean getDisposicionD() {
        return disposicionD;
    }

    public void setDisposicionD(Boolean disposicionD) {
        this.disposicionD = disposicionD;
    }

    public Boolean getDisposicionM() {
        return disposicionM;
    }

    public void setDisposicionM(Boolean disposicionM) {
        this.disposicionM = disposicionM;
    }

    public Boolean getDisposicionS() {
        return disposicionS;
    }

    public void setDisposicionS(Boolean disposicionS) {
        this.disposicionS = disposicionS;
    }

    public Boolean getDisposicionE() {
        return disposicionE;
    }

    public void setDisposicionE(Boolean disposicionE) {
        this.disposicionE = disposicionE;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public Plantilla getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(Plantilla plantilla) {
        this.plantilla = plantilla;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Integer getPlazo() {
        return plazo;
    }

    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }

    public List<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<Documento> documentos) {
        this.documentos = documentos;
    }

}
