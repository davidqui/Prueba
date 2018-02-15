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

import com.laamware.ejercito.doc.web.ctrl.UsuarioMode;
import com.laamware.ejercito.doc.web.dto.UsuarioHistorialFirmaDTO;

/**
 * @author rafar
 *
 */
@Entity
@Table(name = "USUARIO")
@LaamLabel("Usuarios")
public class Usuario extends AuditActivoModifySupport {

    @Id
    @GenericGenerator(name = "USUARIO_SEQ", strategy = "sequence", parameters = {
        @Parameter(name = "sequence", value = "USUARIO_SEQ")
        , @Parameter(name = "allocationSize", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIO_SEQ")
    @Column(name = "USU_ID")
    private Integer id;

    @LaamLabel("Login")
    @LaamCreate(order = 10)
    @LaamListColumn(order = 10)
    @Column(name = "USU_LOGIN")
    private String login;

    @Column(name = "USU_PASSWORD")
    private String password = "nopassword";

    @LaamLabel("Documento de identidad")
    @LaamCreate(order = 20)
    @LaamListColumn(order = 20)
    @Column(name = "USU_DOCUMENTO")
    private String documento;

    @LaamLabel("Nombre")
    @LaamCreate(order = 30)
    @LaamListColumn(order = 30)
    @Column(name = "USU_NOMBRE")
    private String nombre;

    @LaamLabel("Teléfono")
    @LaamCreate(order = 40)
    @Column(name = "USU_TELEFONO")
    private String telefono;
    
    /*
        2017-11-10 edison.gonzalez@controltechcg.com Issue #131 (SICDI-Controltech) 
        feature-131: Cambio en la entidad usuario, se coloca llave foranea el grado.
     */
    @LaamLabel("Grado")
    @LaamCreate(order = 50)
    @LaamListColumn(order = 50)
    @JoinColumn(name = "USU_GRADO", referencedColumnName = "GRA_ID")
    @ManyToOne
    private Grados usuGrado;

    @LaamLabel("Perfil")
    @LaamCreate(order = 60)
    @LaamListColumn(order = 60)
    @LaamWidget(list = "grados", value = "select")
    @ManyToOne
    @JoinColumn(name = "PER_ID")
    private Perfil perfil;

    @LaamLabel("Dependencia")
    @LaamCreate(order = 70)
    @LaamListColumn(order = 70)
    @LaamWidget(list = "dependencias", value = "select")
    @ManyToOne
    @JoinColumn(name = "DEP_ID")
    private Dependencia dependencia;

    @LaamLabel("Nivel de acceso")
    @LaamCreate(order = 80)
    @LaamListColumn(order = 80)
    @LaamWidget(list = "clasificaciones", value = "select")
    @ManyToOne
    @JoinColumn(name = "CLA_ID")
    private Clasificacion clasificacion;

    @LaamLabel("Imagen de la firma")
    @LaamCreate(order = 90)
    @LaamWidget(value = "ofsfile")
    @Column(name = "USU_IMAGEN_FIRMA")
    private String imagenFirma;

    @Column(name = "USU_IMAGEN_FIRMA_EXT")
    private String imagenFirmaExtension;

    @Column(name = "UMA_ID")
    private Integer uma;

    @LaamLabel("Email")
    @LaamCreate(order = 100)
    @Column(name = "USU_EMAIL")
    private String email;
    
    @LaamLabel("Cargo principal")
    @LaamCreate(order = 110)
    @LaamListColumn(order = 110)
    @LaamWidget(list = "cargos", value = "select")
    @JoinColumn(name = "USU_CARGO_PRINCIPAL_ID", referencedColumnName = "CAR_ID")
    @ManyToOne
    private Cargo usuCargoPrincipalId;
    
    @LaamLabel("Cargo Alterno #1")
    @LaamCreate(order = 120)
    @LaamListColumn(order = 120)
    @LaamWidget(list = "cargos", value = "select")
    @JoinColumn(name = "USU_CARGO1_ID", referencedColumnName = "CAR_ID")
    @ManyToOne
    private Cargo usuCargo1Id;
    
    @LaamLabel("Cargo Alterno #2")
    @LaamCreate(order = 130)
    @LaamListColumn(order = 130)
    @LaamWidget(list = "cargos", value = "select")
    @JoinColumn(name = "USU_CARGO2_ID", referencedColumnName = "CAR_ID")
    @ManyToOne
    private Cargo usuCargo2Id;
    
    @LaamLabel("Cargo Alterno #3")
    @LaamCreate(order = 140)
    @LaamListColumn(order = 140)
    @LaamWidget(list = "cargos", value = "select")
    @JoinColumn(name = "USU_CARGO3_ID", referencedColumnName = "CAR_ID")
    @ManyToOne
    private Cargo usuCargo3Id;
    
    @LaamLabel("Cargo Alterno #4")
    @LaamCreate(order = 150)
    @LaamListColumn(order = 150)
    @LaamWidget(list = "cargos", value = "select")
    @JoinColumn(name = "USU_CARGO4_ID", referencedColumnName = "CAR_ID")
    @ManyToOne
    private Cargo usuCargo4Id;
    
    @LaamLabel("Cargo Alterno #5")
    @LaamCreate(order = 160)
    @LaamListColumn(order = 160)
    @LaamWidget(list = "cargos", value = "select")
    @JoinColumn(name = "USU_CARGO5_ID", referencedColumnName = "CAR_ID")
    @ManyToOne
    private Cargo usuCargo5Id;
    
    @LaamLabel("Cargo Alterno #6")
    @LaamCreate(order = 170)
    @LaamListColumn(order = 170)
    @LaamWidget(list = "cargos", value = "select")
    @JoinColumn(name = "USU_CARGO6_ID", referencedColumnName = "CAR_ID")
    @ManyToOne
    private Cargo usuCargo6Id;
    
    @LaamLabel("Cargo Alterno #7")
    @LaamCreate(order = 180)
    @LaamListColumn(order = 180)
    @LaamWidget(list = "cargos", value = "select")
    @JoinColumn(name = "USU_CARGO7_ID", referencedColumnName = "CAR_ID")
    @ManyToOne
    private Cargo usuCargo7Id;
    
    @LaamLabel("Cargo Alterno #8")
    @LaamCreate(order = 190)
    @LaamListColumn(order = 190)
    @LaamWidget(list = "cargos", value = "select")
    @JoinColumn(name = "USU_CARGO8_ID", referencedColumnName = "CAR_ID")
    @ManyToOne
    private Cargo usuCargo8Id;
    
    @LaamLabel("Cargo Alterno #9")
    @LaamCreate(order = 200)
    @LaamListColumn(order = 200)
    @LaamWidget(list = "cargos", value = "select")
    @JoinColumn(name = "USU_CARGO9_ID", referencedColumnName = "CAR_ID")
    @ManyToOne
    private Cargo usuCargo9Id;
    
    @LaamLabel("Cargo Alterno #10")
    @LaamCreate(order = 210)
    @LaamListColumn(order = 210)
    @LaamWidget(list = "cargos", value = "select")
    @JoinColumn(name = "USU_CARGO10_ID", referencedColumnName = "CAR_ID")
    @ManyToOne
    private Cargo usuCargo10Id;

    @Transient
    private List<UsuarioHistorialFirmaDTO> historialUsuarios = new ArrayList<UsuarioHistorialFirmaDTO>();

    @Transient
    private String mensajeNivelAcceso;

    @Transient
    private boolean restriccionDocumentoNivelAcceso;

    public Usuario(Integer id) {
        this.id = id;
    }

    public Usuario() {
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        /*
            2017-11-10 edison.gonzalez@controltechcg.com Issue #131 (SICDI-Controltech) 
            feature-131: Cambio en la entidad usuario, se coloca llave foranea el grado.
         */
        if (usuGrado != null && !AppConstants.SIN_GRADO.equals(usuGrado.getId())) {
            b.append(usuGrado.getId()).append(". ");
        }
        b.append(nombre);
        return b.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        if (login != null) {
            this.login = login.trim().toLowerCase();
        } else {
            this.login = null;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public Integer getUma() {
        return uma;
    }

    public void setUma(Integer uma) {
        this.uma = uma;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Clasificacion clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getImagenFirma() {
        return imagenFirma;
    }

    public void setImagenFirma(String imagenFirma) {
        this.imagenFirma = imagenFirma;
    }

    @Transient
    private UsuarioMode mode;

    public UsuarioMode getMode() {
        return mode;
    }

    public void setMode(UsuarioMode mode) {
        this.mode = mode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean usuarioTieneFirmaCargada() {
        return imagenFirma != null && imagenFirma.trim().length() > 0;
    }

    public String getImagenFirmaExtension() {
        return imagenFirmaExtension;
    }

    public void setImagenFirmaExtension(String imagenFirmaExtension) {
        this.imagenFirmaExtension = imagenFirmaExtension;
    }

    public List<UsuarioHistorialFirmaDTO> getHistorialUsuarios() {
        return historialUsuarios;
    }

    public void setHistorialUsuarios(List<UsuarioHistorialFirmaDTO> historialUsuarios) {
        this.historialUsuarios = historialUsuarios;
    }

    public String getMensajeNivelAcceso() {
        return mensajeNivelAcceso;
    }

    public void setMensajeNivelAcceso(String mensajeNivelAcceso) {
        this.mensajeNivelAcceso = mensajeNivelAcceso;
    }

    public boolean isRestriccionDocumentoNivelAcceso() {
        return restriccionDocumentoNivelAcceso;
    }

    public void setRestriccionDocumentoNivelAcceso(boolean restriccionDocumentoNivelAcceso) {
        this.restriccionDocumentoNivelAcceso = restriccionDocumentoNivelAcceso;
    }

    @Transient
    private String idString;

    public String getIdString() {
        if (id != null) {
            idString = id.toString().replaceAll("\\.", "");
        } else {
            idString = null;
        }
        return idString;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public Grados getUsuGrado() {
        return usuGrado;
    }

    public void setUsuGrado(Grados usuGrado) {
        this.usuGrado = usuGrado;
    }

    public Cargo getUsuCargo7Id() {
        return usuCargo7Id;
    }

    public void setUsuCargo7Id(Cargo usuCargo7Id) {
        this.usuCargo7Id = usuCargo7Id;
    }

    public Cargo getUsuCargo10Id() {
        return usuCargo10Id;
    }

    public void setUsuCargo10Id(Cargo usuCargo10Id) {
        this.usuCargo10Id = usuCargo10Id;
    }

    public Cargo getUsuCargo9Id() {
        return usuCargo9Id;
    }

    public void setUsuCargo9Id(Cargo usuCargo9Id) {
        this.usuCargo9Id = usuCargo9Id;
    }

    public Cargo getUsuCargo8Id() {
        return usuCargo8Id;
    }

    public void setUsuCargo8Id(Cargo usuCargo8Id) {
        this.usuCargo8Id = usuCargo8Id;
    }

    public Cargo getUsuCargo6Id() {
        return usuCargo6Id;
    }

    public void setUsuCargo6Id(Cargo usuCargo6Id) {
        this.usuCargo6Id = usuCargo6Id;
    }

    public Cargo getUsuCargo5Id() {
        return usuCargo5Id;
    }

    public void setUsuCargo5Id(Cargo usuCargo5Id) {
        this.usuCargo5Id = usuCargo5Id;
    }

    public Cargo getUsuCargo4Id() {
        return usuCargo4Id;
    }

    public void setUsuCargo4Id(Cargo usuCargo4Id) {
        this.usuCargo4Id = usuCargo4Id;
    }

    public Cargo getUsuCargo3Id() {
        return usuCargo3Id;
    }

    public void setUsuCargo3Id(Cargo usuCargo3Id) {
        this.usuCargo3Id = usuCargo3Id;
    }

    public Cargo getUsuCargo2Id() {
        return usuCargo2Id;
    }

    public void setUsuCargo2Id(Cargo usuCargo2Id) {
        this.usuCargo2Id = usuCargo2Id;
    }

    public Cargo getUsuCargo1Id() {
        return usuCargo1Id;
    }

    public void setUsuCargo1Id(Cargo usuCargo1Id) {
        this.usuCargo1Id = usuCargo1Id;
    }

    public Cargo getUsuCargoPrincipalId() {
        return usuCargoPrincipalId;
    }

    public void setUsuCargoPrincipalId(Cargo usuCargoPrincipalId) {
        this.usuCargoPrincipalId = usuCargoPrincipalId;
    }
    
    

}
