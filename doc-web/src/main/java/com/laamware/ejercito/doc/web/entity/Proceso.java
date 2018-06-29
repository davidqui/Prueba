package com.laamware.ejercito.doc.web.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "PROCESO")
@LaamLabel("Procesos documentales")
public class Proceso extends AuditActivoModifySupport {

    public static final Integer ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS = 9;
    public static final Integer ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA = 8;
    public static final Integer ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS = 41;

    /**
     * Identificador del proceso de registro de actas.
     */
    /*
     * 2018-05-11 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public static final Integer ID_TIPO_PROCESO_REGISTRO_ACTAS = 100;

    @Id
    @GenericGenerator(name = "PROCESO_SEQ", strategy = "sequence", parameters = {
        @Parameter(name = "sequence", value = "PROCESO_SEQ")
        , @Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESO_SEQ")
    @Column(name = "PRO_ID")
    private Integer id;

    @Column(name = "PRO_NOMBRE")
    @LaamListColumn(order = 10)
    @LaamCreate(order = 10)
    @LaamLabel("Nombre del proceso")
    private String nombre;

    @Column(name = "PRO_DESCRIPCION")
    @LaamCreate(order = 20)
    @LaamLabel("Descripción")
    @LaamWidget("textarea")
    private String descripcion;

    @OneToMany
    @JoinColumn(name = "PRO_ID", updatable = false)
    private List<Estado> estados;

    @OneToMany
    @JoinColumn(name = "PRO_ID", updatable = false)
    private List<VariableProceso> variables;

    @Column(name = "PRO_FACADE")
    private String facade;

    @Column(name = "PRO_IMAGEN")
    @LaamCreate(order = 50)
    @LaamLabel("Imagen")
    private String imagen;

    @Column(name = "PRO_ALIAS")
    @LaamCreate(order = 30)
    @LaamLabel("Es un alias del proceso")
    @LaamWidget(value = "select", list = "procesos", type = Proceso.class)
    private Integer alias;

    @Column(name = "PRO_ID_RESPUESTA")
    @LaamCreate(order = 40)
    @LaamLabel("El proceso de respuesta es")
    @LaamWidget(value = "select", list = "procesosActivos", type = Proceso.class)
    private Integer respuesta;

    public Integer getAlias() {
        return alias;
    }

    public void setAlias(Integer alias) {
        this.alias = alias;
    }

    public Proceso() {
    }

    public Proceso(Integer id) {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Estado> getEstados() {
        return estados;
    }

    public void setEstados(List<Estado> estados) {
        this.estados = estados;
    }

    public List<VariableProceso> getVariables() {
        return variables;
    }

    public void setVariables(List<VariableProceso> variables) {
        this.variables = variables;
    }

    public String getFacade() {
        return facade;
    }

    public void setFacade(String facade) {
        this.facade = facade;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String toString() {
        return nombre;
    }

    public Integer getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Integer respuesta) {
        this.respuesta = respuesta;
    }

}
