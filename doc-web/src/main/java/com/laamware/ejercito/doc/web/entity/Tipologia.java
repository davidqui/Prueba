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
@Table(name = "TIPOLOGIA_DOCUMENTO")
public class Tipologia extends AuditActivoModifySupport {

    public static final Tipologia DOCUMENTO_LLEGADO = new Tipologia();

    static {
        DOCUMENTO_LLEGADO.id = 9999;
    }

    @Id
    @GenericGenerator(name = "TIPOLOGIA_DOCUMENTO_SEQ", strategy = "sequence", parameters = {
        @Parameter(name = "sequence", value = "TIPOLOGIA_DOCUMENTO_SEQ")
        ,
			@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPOLOGIA_DOCUMENTO_SEQ")
    @Column(name = "TDO_ID")
    @LaamListColumn(order = 10)
    @LaamLabel("ID")
    private Integer id;

    @Column(name = "TDO_NOMBRE")
    @LaamListColumn(order = 20)
    @LaamLabel("Nombre la tipología")
    @LaamCreate(order = 10)
    private String nombre;

    @LaamListColumn(order = 30)
    @Column(name = "TDO_CODIGO")
    @LaamLabel("Código")
    @LaamCreate(order = 20)
    private String codigo;

    @Column(name = "TDO_ARCHIVO")
    private String archivo;

    /**
     * Constructor vacío.
     */
    /*
     * 2018-05-21 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public Tipologia() {
    }

    /**
     * Constructor.
     *
     * @param id ID.
     */
    /*
     * 2018-05-21 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public Tipologia(Integer id) {
        this.id = id;
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

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

}
