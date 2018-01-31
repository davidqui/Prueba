package com.laamware.ejercito.doc.web.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.laamware.ejercito.doc.web.repo.DependenciaRepository;

@Entity
@Table(name = "DEPENDENCIA")
@LaamLabel("Dependencias")
public class Dependencia extends AuditActivoModifySupport {

    @Id
    @GenericGenerator(name = "DEPENDENCIA_SEQ", strategy = "sequence", parameters = {
        @Parameter(name = "sequence", value = "DEPENDENCIA_SEQ")
        ,
			@Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPENDENCIA_SEQ")
    @Column(name = "DEP_ID")
    private Integer id;

    @LaamLabel("Código de la dependencia")
    @LaamCreate(order = 10)
    @LaamListColumn(order = 20)
    @Column(name = "DEP_CODIGO")
    private String codigo;

    @LaamLabel("Nombre de la dependencia")
    @LaamCreate(order = 10)
    @LaamListColumn(order = 20)
    @Column(name = "DEP_NOMBRE")
    private String nombre;

    @LaamLabel("Sigla")
    @LaamCreate(order = 15)
    @LaamListColumn(order = 25)
    @Column(name = "DEP_SIGLA")
    private String sigla;

    @LaamLabel("Orgánico de")
    @LaamCreate(order = 20)
    @LaamListColumn(order = 30)
    @Column(name = "DEP_PADRE")
    private Integer padre;

    @LaamLabel("Jefe Dependencia")
    @LaamCreate(order = 30)
    @LaamListColumn(order = 40)
    @ManyToOne
    @JoinColumn(name = "USU_ID_JEFE")
    @LaamWidget(value = "select", list = "usuarios")
    private Usuario jefe;

    @LaamLabel("Firma")
    @LaamCreate(order = 40)
    @ManyToOne
    @JoinColumn(name = "USU_ID_FIRMA_PRINCIPAL")
    @LaamWidget(value = "select", list = "usuarios")
    private Usuario firmaPrincipal;

    @LaamLabel("Resolución jefe")
    @LaamCreate(order = 50)
    @LaamWidget(value = "editor")
    @Column(name = "DEP_RESOLUCION_JEFE")
    private String depResolucionJefe;

    @LaamLabel("Segundo Comandante")
    @LaamCreate(order = 60)
    @ManyToOne
    @JoinColumn(name = "USU_ID_JEFE_ENCARGADO")
    @LaamWidget(value = "select", list = "usuarios")
    private Usuario jefeEncargado;

    @LaamLabel("Fecha de inicio segundo comandante")
    @LaamCreate(order = 70)
    @Column(name = "FCH_INICIO_JEFE_ENCARGADO")
    @LaamWidget(value = "calendar")
    private Date fchInicioJefeEncargado;

    @LaamLabel("Fecha de fin segundo comandante")
    @LaamCreate(order = 80)
    @Column(name = "FCH_FIN_JEFE_ENCARGADO")
    @LaamWidget(value = "calendar")
    private Date fchFinJefeEncargado;

    @OneToMany(mappedBy = "dependencia")
    private List<DependenciaTrd> trds;

    @LaamLabel("Código LDAP")
    @LaamListColumn(order = 50)
    @LaamCreate(order = 90)
    @Column(name = "DEP_CODIGO_LDAP")
    private String depCodigoLdap;

    @LaamLabel("Código Orfeo")
    @LaamListColumn(order = 60)
    @LaamCreate(order = 100)
    @Column(name = "DEP_CODIGO_ORFEO")
    private String depCodigoOrfeo;

    @LaamLabel("Dirección")
    @LaamCreate(order = 70)
    @LaamListColumn(order = 120)
    @Column(name = "DIRECCION")
    private String direccion;

    /*
	 * 2017-02-06 jgarcia@controltechcg.com Issue #123: Nuevo campo CIUDAD en la
	 * tabla de DEPENDENCIA.
     */
    @LaamLabel("Ciudad")
    @LaamCreate(order = 80)
    @LaamListColumn(order = 150)
    @Column(name = "CIUDAD")
    private String ciudad;

    /*
	 * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS: Ordenamiento por peso.
     */
    @LaamLabel("Peso Orden")
    @LaamCreate(order = 10)
    @LaamListColumn(order = 50)
    @Column(name = "DEP_PESO_ORDEN")
    private Integer pesoOrden;

    @LaamLabel("Indicador Envió documentos")
    @LaamCreate(order = 60)
    @LaamListColumn(order = 50)
    @LaamWidget(value = "checkbox")
    @Column(name = "DEP_IND_ENVIO_DOCUMENTOS")
    private Boolean depIndEnvioDocumentos;

    @Transient
    private List<Dependencia> subs;

    @Transient
    private String siglaNombre;

    @Transient
    private String idString;

    @Transient
    private String idPadreString;

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

    public Integer getPadre() {
        return padre;
    }

    public void setPadre(Integer padre) {
        this.padre = padre;
    }

    public Usuario getJefe() {
        return jefe;
    }

    public void setJefe(Usuario jefe) {
        this.jefe = jefe;
    }

    public Usuario getFirmaPrincipal() {
        return firmaPrincipal;
    }

    public void setFirmaPrincipal(Usuario firmaPrincipal) {
        this.firmaPrincipal = firmaPrincipal;
    }

    public String getDepResolucionJefe() {
        return depResolucionJefe;
    }

    public void setDepResolucionJefe(String depResolucionJefe) {
        this.depResolucionJefe = depResolucionJefe;
    }

    public Usuario getJefeEncargado() {
        return jefeEncargado;
    }

    public void setJefeEncargado(Usuario jefeEncargado) {
        this.jefeEncargado = jefeEncargado;
    }

    public Date getFchInicioJefeEncargado() {
        return fchInicioJefeEncargado;
    }

    public void setFchInicioJefeEncargado(Date fchInicioJefeEncargado) {
        this.fchInicioJefeEncargado = fchInicioJefeEncargado;
    }

    public Date getFchFinJefeEncargado() {
        return fchFinJefeEncargado;
    }

    public void setFchFinJefeEncargado(Date fchFinJefeEncargado) {
        this.fchFinJefeEncargado = fchFinJefeEncargado;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public List<DependenciaTrd> getTrds() {
        return trds;
    }

    public void setTrds(List<DependenciaTrd> trds) {
        this.trds = trds;
    }

    public String getDepCodigoLdap() {
        return depCodigoLdap;
    }

    public void setDepCodigoLdap(String depCodigoLdap) {
        this.depCodigoLdap = depCodigoLdap;
    }

    public String getDepCodigoOrfeo() {
        return depCodigoOrfeo;
    }

    public void setDepCodigoOrfeo(String depCodigoOrfeo) {
        this.depCodigoOrfeo = depCodigoOrfeo;
    }

    public Dependencia jefaturaCompartida(Dependencia dep, DependenciaRepository rep) {
        // Se obtienen todas las dependencias padre en una lista ordenada para
        // cada una de las dependencias
        Dependencia padre = this;
        List<Dependencia> thisPadres = new ArrayList<>();
        while (padre.getPadre() != null && (padre = rep.getOne(padre.getPadre())) != null) {
            thisPadres.add(padre);
        }
        padre = dep;
        List<Dependencia> depPadres = new ArrayList<>();
        while (padre.getPadre() != null && (padre = rep.getOne(padre.getPadre())) != null) {
            depPadres.add(padre);
        }

        // En la intersección deben estar todas las jefaturas comunes en orden
        // jerárquico. Por lo tanto en la primera posición se encuentra la
        // jefatura compartida.
        @SuppressWarnings("unchecked")
        Collection<Dependencia> intersection = CollectionUtils.intersection(thisPadres, depPadres);
        if (intersection.isEmpty()) {
            return null;
        }
        return intersection.iterator().next();
    }

    public Dependencia obtenerJefatura(DependenciaRepository repository) {
        Dependencia padre = this;
        /*
	 * 2018-01-30 edison.gonzalez@controltechcg.com Issue #147: Validacion para que tenga en cuenta el
	 * campo Indicador de envio documentos.
        */
        while (padre.getPadre() != null && (padre = repository.getOne(padre.getPadre())) != null && (padre.getDepIndEnvioDocumentos() == null || !padre.getDepIndEnvioDocumentos())) {
        }
        return padre;
    }

    public List<Dependencia> getSubs() {
        return subs;
    }

    public void setSubs(List<Dependencia> subs) {
        this.subs = subs;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getSiglaNombre() {
        if (nombre != null && sigla != null) {
            return sigla + " - " + nombre;
        } else {
            siglaNombre = nombre;
        }
        return siglaNombre;
    }

    public String getIdString() {
        if (id != null) {
            idString = id.toString().replaceAll("\\.", "");
        } else {
            idString = null;
        }
        return idString;
    }

    public String getIdPadreString() {
        if (padre != null) {
            idPadreString = padre.toString().replaceAll("\\.", "");
        } else {
            idPadreString = null;
        }
        return idPadreString;
    }

    public void setSiglaNombre(String siglaNombre) {
        this.siglaNombre = siglaNombre;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public void setIdPadreString(String idPadreString) {
        this.idPadreString = idPadreString;
    }

    /**
     * @return the ciudad
     */
    // Issue #123
    public String getCiudad() {
        return ciudad;
    }

    /**
     * @param ciudad the ciudad to set
     */
    // Issue #123
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * @return the Peso Orden
     */
    // Issue #45
    public Integer getPesoOrden() {
        return pesoOrden;
    }

    /**
     * @param pesoOrden
     */
    // Issue #45
    public void setPesoOrden(Integer pesoOrden) {
        this.pesoOrden = pesoOrden;
    }

    /**
     * @return the Indicador de envio documentos
     */
    // Issue #147
    public Boolean getDepIndEnvioDocumentos() {
        return depIndEnvioDocumentos;
    }

    /**
     * @param depIndEnvioDocumentos
     */
    // Issue #147
    public void setDepIndEnvioDocumentos(Boolean depIndEnvioDocumentos) {
        this.depIndEnvioDocumentos = depIndEnvioDocumentos;
    }
}
