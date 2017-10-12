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

    @LaamLabel("Cargo")
    @LaamCreate(order = 55)
    @Column(name = "USU_CARGO")
    private String cargo;

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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
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

}
