package com.laamware.ejercito.doc.web.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import com.laamware.ejercito.doc.web.ctrl.DocumentoMode;
import com.laamware.ejercito.doc.web.dto.UsuarioVistoBuenoDTO;
import com.laamware.ejercito.doc.web.util.GeneralUtils;

@Entity
@Table(name = "DOCUMENTO")
public class Documento extends AuditModifySupport {

    public static final String DOC_ID = "doc.id";

    public static final String DOC_STICKER_DATOS_COMPLETOS = "doc.sticker.datos.completos";

    public static final String DOC_STICKER_ID = "doc.sticker.id";

    public static final String DOC_JEFE_ASIGNADO_ID = "doc.jefe.asignado.id";

    public static final String DOC_DEPE_ASIGNADO_ID = "doc.depe.asignado.id";

    public static final String DOC_MODE = "doc.mode";

    public static final String DOC_ADJUNTOS = "doc.adjuntos";

    public static final String DOC_PUEDE_DAR_RESPUESTA = "doc.puede.dar_respuesta";

    public static final String DOC_PUEDE_ARCHIVAR = "doc.puede.archivar";

    public static final String DOC_VISTO_BUENO = "doc.visto.bueno";

    public static final String DOC_FORMATOS = "doc.formatos";

    public static final String DOC_MODE_CONSTRUCCION = "doc.mode.construccion";

    public static final String DOC_MODE_DESTINATARIO = "doc.destinatario.mode";

    public static final String VAL_DOC_MODE_DESTINATARIO_TEXTO = "texto";

    public static final String VAL_DOC_MODE_DESTINATARIO_DEP = "dep";

    public static Documento create() {
        Documento x = new Documento();
        x.id = GeneralUtils.newId();
        return x;
    }

    @Id
    @Column(name = "DOC_ID")
    private String id;

    @Size(max = 256)
    @Column(name = "DOC_ASUNTO")
    private String asunto;

    @ManyToOne
    @JoinColumn(name = "TRD_ID")
    private Trd trd;

    @ManyToOne
    @JoinColumn(name = "CLA_ID")
    private Clasificacion clasificacion;

    @Size(max = 64)
    @Column(name = "DOC_NUM_OFICIO")
    private String numeroOficio;

    @Column(name = "DOC_PLAZO")
    private Date plazo;

    @ManyToOne
    @JoinColumn(name = "DEP_ID_DES")
    private Dependencia dependenciaDestino;

    @ManyToOne
    @JoinColumn(name = "DEP_ID_REM")
    private Dependencia dependenciaRemitente;

    @Column(name = "DOC_FCH_OFICIO")
    private Date fechaOficio;

    @Max(value = 99999, message = "El numero maximo es 99999.")
    @Column(name = "DOC_NUM_FOLIOS")
    private Integer numeroFolios;

    @ManyToOne
    @JoinColumn(name = "PIN_ID")
    private Instancia instancia;

    @Column(name = "DOC_STICKER")
    private String sticker;

    @OneToMany
    @JoinColumn(name = "DOC_ID")
    @OrderBy("cuando ASC")
    private List<Adjunto> adjuntos = new ArrayList<Adjunto>();

    @Column(name = "DOC_CONTENIDO")
    private String contenido;

    @ManyToOne
    @JoinColumn(name = "USU_ID_ELABORA")
    private Usuario elabora;

    @ManyToOne
    @JoinColumn(name = "USU_ID_FIRMA")
    private Usuario firma;

    @ManyToOne
    @JoinColumn(name = "USU_ID_VISTO_BUENO")
    private Usuario vistoBueno;

    @ManyToOne
    @JoinColumn(name = "USU_ID_APRUEBA")
    private Usuario aprueba;

    @ManyToOne
    @JoinColumn(name = "USU_ID_ULTIMA_ACCION")
    private Usuario usuarioUltimaAccion;

    @Column(name = "DOC_RADICADO")
    private String radicado;

    @Column(name = "DOC_RELACIONADO")
    private String relacionado;

    @OneToMany
    @JoinColumn(name = "DOC_ID")
    private List<DocumentoObservacion> observaciones;

    @ManyToOne
    @JoinColumn(name = "EXP_ID")
    private Expediente expediente;

    @Size(max = 255)
    @Column(name = "DOC_REM_NOMBRE")
    private String remitenteNombre;

    @Size(max = 255)
    @Column(name = "DOC_REM_TITULO")
    private String remitenteTitulo;

    @Size(max = 255)
    @Column(name = "DOC_REM_DIRECCION")
    private String remitenteDireccion;

    @Transient
    private DocumentoMode mode;

    @Size(max = 256)
    @Column(name = "DOC_DEST_NOMBRE")
    private String destinatarioNombre;

    // 2017-03-08 jgarcia@controltechcg.com Issue #10 (SIGDI-Incidencias01):
    // Ampliación el campo "DOC_DEST_TITULO" de la tabla "DOCUMENTO" de 32 a 128
    // caracteres.
    @Size(max = 128)
    @Column(name = "DOC_DEST_TITULO")
    private String destinatarioTitulo;

    @Size(max = 255)
    @Column(name = "DOC_DEST_DIRECCION")
    private String destinatarioDireccion;

    @Column(name = "DOC_PDF")
    private String pdf;

    @Column(name = "DOC_PAGINAS")
    private String paginas;

    @Size(max = 256)
    @Column(name = "DOC_DESCRIPCION")
    private String descripcion;

    @Column(name = "DOC_CONTENT_FILE")
    private String contentFile;

    @ManyToOne
    @JoinColumn(name = "PLA_ID")
    private Plantilla plantilla;

    @OneToMany
    @JoinColumn(name = "DOC_ID")
    @OrderBy("cuando ASC")
    private List<Formato> formatos = new ArrayList<Formato>();

    @Size(max = 32)
    @Column(name = "PRO_RAD_ORFEO")
    private String radicadoOrfeo;

    @Size(max = 32)
    @Column(name = "PRO_NUM_BOLSA")
    private String numeroBolsa;

    @Column(name = "DOC_PRESTADO")
    private Boolean prestado;

    @Column(name = "ESTADO_TMP")
    private String estadoTemporal;

    @Column(name = "DOC_DOCX_DOCUMENTO")
    private String docx4jDocumento;

    @OneToMany
    @JoinColumn(name = "DOC_ID")
    private List<DocumentoDependenciaDestino> documentoDependenciaDestinos = new ArrayList<DocumentoDependenciaDestino>();

    @Column(name = "FECHA_GEN_CODIGO_SCANNER")
    private Date fechaGeneracionCodigoScanner;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO_VALIDA_COD_SCANNER")
    private Usuario usuarioValidaCodigoScanner;

    @Column(name = "CODIGO_VALIDA_SCANNER")
    private String codigoValidaScanner;

    @Column(name = "ESTADO_CODIGO_VALIDA_SCANNER")
    private Integer estadoCodigoValidaScanner;

    /*
         * 2017-02-02 edison.gonzalez@controltechcg.com Feature #129: Variable que 
	 * permite almacenar la etiqueta de grado en el documento. Aplica solo para documentos
         * externos.
     */
    @Column(name = "GRADO")
    private String grado;

    /*
         * 2017-02-02 edison.gonzalez@controltechcg.com Feature #129: Variable que 
	 * permite almacenar la etiqueta de gota de agua en el documento. Aplica solo para documentos
         * externos.
     */
    @Column(name = "MARCA_AGUA")
    private String marcaAgua;

    /*
         * 2017-02-02 edison.gonzalez@controltechcg.com Feature #129: Variable que 
	 * permite almacenar la etiqueta para distinguir la restriccion de la difusion en el documento.
         * Aplica para todos los documentos.
     */
    @JoinColumn(name = "RESTRICCION_DIFUSION", referencedColumnName = "RES_ID")
    @ManyToOne
    private RestriccionDifusion restriccionDifusion;

    @Transient
    private List<UsuarioVistoBuenoDTO> vistosBuenos = new ArrayList<UsuarioVistoBuenoDTO>();

    @Transient
    private boolean docx4jDocumentoVacio;

    @Transient
    private boolean docx4jDocumentoFormatoInvalido;

    /*
	 * 2017-02-02 jgarcia@controltechcg.com Issue #129: Variable que se
	 * encuentra en el documento que en tiempo de validación que permite
	 * establecer si tiene errores para manejo en el formulario.
     */
    @Transient
    private boolean withErrors;

    /*
	 * 2017-02-06 jgarcia@controltechcg.com Issue #118 Presentación de jefes de
	 * dependencias adicionales a un documento.
     */
    @Transient
    private String textoAsignado;

    /**
     * Indica si el documento corresponde a un documento respuesta.
     *
     * @return {@code true} si el documento tiene otro documento relacionado,
     * además de ser la respuesta de este; de lo contrario, {@code false}.
     */
    // 2017-05-18 jgarcia@controltechcg.com Issue #87 (SIGDI-Controltech)
    // feature-87
    public boolean esDocumentoRespuesta() {
        if (getRelacionado() == null || getRelacionado().trim().isEmpty()) {
            return false;
        }

        if (getAsunto() == null || getAsunto().trim().isEmpty() || !getAsunto().toUpperCase().startsWith("RE:")) {
            return false;
        }

        return true;
    }

    /**
     * Indica si el documento corresponde a un documento respuesta en estado de
     * construcción.
     *
     * @return {@code true} Si el documento corresponde a un documento respuesta
     * y se encuentra en estado de construcción; de lo contrario, {@code false}
     */
    // 2017-04-07 jgarcia@controltechcg.com Issue #37 (SIGDI-Controltech)
    public boolean documentoRespuestaEnConstruccion() {
        // 2017-05-18 jgarcia@controltechcg.com Issue #87 (SIGDI-Controltech)
        // feature-87
        if (!esDocumentoRespuesta()) {
            return false;
        }

        return (getInstancia().getEstado().getId().equals(Estado.EN_CONSTRUCCION));
    }

    /**
     * Indica si el documento corresponde a un documento respuesta en estado de
     * revisión por jefe de dependencia.
     *
     * @return {@code true} Si el documento corresponde a un documento respuesta
     * y se encuentra en estado de revisión por jefe de dependencia; de lo
     * contrario, {@code false}
     */
    // 2017-05-18 jgarcia@controltechcg.com Issue #87 (SIGDI-Controltech)
    // feature-87
    public boolean documentoRespuestaEnRevisionJefeDependencia() {
        if (!esDocumentoRespuesta()) {
            return false;
        }

        return (getInstancia().getEstado().getId().equals(Estado.EN_REVISION_JEFE_DEPENDENCIA));
    }

    public boolean isWithErrors() {
        return withErrors;
    }

    public void setWithErrors(boolean withErrors) {
        this.withErrors = withErrors;
    }

    public String getTextoAsignado() {
        return textoAsignado;
    }

    public void setTextoAsignado(String textoAsignado) {
        this.textoAsignado = textoAsignado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public Trd getTrd() {
        return trd;
    }

    public void setTrd(Trd trd) {
        this.trd = trd;
    }

    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Clasificacion clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getNumeroOficio() {
        return numeroOficio;
    }

    public void setNumeroOficio(String numeroOficio) {
        this.numeroOficio = numeroOficio;
    }

    public Date getPlazo() {
        return plazo;
    }

    public void setPlazo(Date plazo) {
        this.plazo = plazo;
    }

    public Date getFechaOficio() {
        return fechaOficio;
    }

    public void setFechaOficio(Date fechaOficio) {
        this.fechaOficio = fechaOficio;
    }

    public Integer getNumeroFolios() {
        return numeroFolios;
    }

    public void setNumeroFolios(Integer numeroFolios) {
        this.numeroFolios = numeroFolios;
    }

    public Instancia getInstancia() {
        return instancia;
    }

    public void setInstancia(Instancia instancia) {
        this.instancia = instancia;
    }

    public String getSticker() {
        return sticker;
    }

    public void setSticker(String sticker) {
        this.sticker = sticker;
    }

    public List<Adjunto> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<Adjunto> adjuntos) {
        this.adjuntos = adjuntos;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Usuario getElabora() {
        return elabora;
    }

    public void setElabora(Usuario elabora) {
        this.elabora = elabora;
    }

    public Usuario getFirma() {
        return firma;
    }

    public void setFirma(Usuario firma) {
        this.firma = firma;
    }

    public Usuario getVistoBueno() {
        return vistoBueno;
    }

    public void setVistoBueno(Usuario vistoBueno) {
        this.vistoBueno = vistoBueno;
    }

    public Usuario getAprueba() {
        return aprueba;
    }

    public void setAprueba(Usuario aprueba) {
        this.aprueba = aprueba;
    }

    public String getRadicado() {
        return radicado;
    }

    public void setRadicado(String radicado) {
        this.radicado = radicado;
    }

    public List<DocumentoObservacion> getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(List<DocumentoObservacion> observaciones) {
        this.observaciones = observaciones;
    }

    public String getRelacionado() {
        return relacionado;
    }

    public void setRelacionado(String relacionado) {
        this.relacionado = relacionado;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public DocumentoMode getMode() {
        return mode;
    }

    public void setMode(DocumentoMode mode) {
        this.mode = mode;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getDestinatarioNombre() {
        return destinatarioNombre;
    }

    public void setDestinatarioNombre(String destinatarioNombre) {
        this.destinatarioNombre = destinatarioNombre;
    }

    public String getSemaforo() {
        if (plazo != null) {
            long diff = plazo.getTime() - new Date().getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (diffDays < 1) {
                return "danger";
            }
            if (diffDays < 2) {
                return "warning";
            }
            return "success";
        }
        return "default";
    }

    public String getPaginas() {
        return paginas;
    }

    public void setPaginas(String paginas) {
        this.paginas = paginas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Formato> getFormatos() {
        return formatos;
    }

    public void setFormatos(List<Formato> formatos) {
        this.formatos = formatos;
    }

    public Dependencia getDependenciaDestino() {
        return dependenciaDestino;
    }

    public void setDependenciaDestino(Dependencia dependenciaDestino) {
        this.dependenciaDestino = dependenciaDestino;
    }

    public Plantilla getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(Plantilla plantilla) {
        this.plantilla = plantilla;
    }

    public String getContentFile() {
        return contentFile;
    }

    public void setContentFile(String contentFile) {
        this.contentFile = contentFile;
    }

    public String getDestinatarioTitulo() {
        return destinatarioTitulo;
    }

    public void setDestinatarioTitulo(String destinatarioTitulo) {
        this.destinatarioTitulo = destinatarioTitulo;
    }

    public String getDestinatarioDireccion() {
        return destinatarioDireccion;
    }

    public void setDestinatarioDireccion(String destinatarioDireccion) {
        this.destinatarioDireccion = destinatarioDireccion;
    }

    public Boolean getPrestado() {
        return prestado;
    }

    public void setPrestado(Boolean prestado) {
        this.prestado = prestado;
    }

    public String getRemitenteNombre() {
        return remitenteNombre;
    }

    public void setRemitenteNombre(String remitenteNombre) {
        this.remitenteNombre = remitenteNombre;
    }

    public String getRemitenteTitulo() {
        return remitenteTitulo;
    }

    public void setRemitenteTitulo(String remitenteTitulo) {
        this.remitenteTitulo = remitenteTitulo;
    }

    public String getRemitenteDireccion() {
        return remitenteDireccion;
    }

    public void setRemitenteDireccion(String remitenteDireccion) {
        this.remitenteDireccion = remitenteDireccion;
    }

    public Dependencia getDependenciaRemitente() {
        return dependenciaRemitente;
    }

    public void setDependenciaRemitente(Dependencia dependenciaRemitente) {
        this.dependenciaRemitente = dependenciaRemitente;
    }

    public String getDestinatario() {
        if (StringUtils.isNotBlank(destinatarioNombre)) {
            StringBuilder b = new StringBuilder();
            if (StringUtils.isNotBlank(destinatarioTitulo)) {
                b.append(destinatarioTitulo).append(" ");
            }
            b.append(destinatarioNombre);
            return b.toString();
        } else {
            if (dependenciaDestino != null) {
                return dependenciaDestino.toString();
            } else {
                return "";
            }
        }
    }

    public String getRemitente() {
        if (StringUtils.isNotBlank(remitenteNombre)) {
            StringBuilder b = new StringBuilder();
            if (StringUtils.isNotBlank(remitenteTitulo)) {
                b.append(remitenteTitulo).append(" ");
            }
            b.append(remitenteNombre);
            return b.toString();
        } else {
            if (dependenciaRemitente != null) {
                return dependenciaRemitente.toString();
            } else {
                return "";
            }
        }
    }

    public String getRadicadoOrfeo() {
        return radicadoOrfeo;
    }

    public void setRadicadoOrfeo(String radicadoOrfeo) {
        this.radicadoOrfeo = radicadoOrfeo;
    }

    public String getNumeroBolsa() {
        return numeroBolsa;
    }

    public void setNumeroBolsa(String numeroBolsa) {
        this.numeroBolsa = numeroBolsa;
    }

    public String getEstadoTemporal() {
        return estadoTemporal;
    }

    public void setEstadoTemporal(String estadoTemporal) {
        this.estadoTemporal = estadoTemporal;
    }

    @Deprecated
    public String consultaEstadoAplicado() {
        // TODOS los documentos tiene aplica el campo Asunto
        if (asunto == null || asunto.trim().length() == 0) {
            return ".";
        }
        return "";
    }

    /**
     * Permite identificar si el documento puede presentar sus transiciones
     * activas correspondientes al estado y proceso.
     *
     * @return {@code true} si el documento puede presentar las transiciones; de
     * lo contrario, {@code false}.
     */
    /*
	 * 2017-04-26 jgarcia@controltechcg.com Issue #57 (SICDI-Controltech):
	 * Reemplazo del método que determina si las transiciones se presentan en el
	 * formulario de documento.
     */
    public boolean presentarTransiciones() {
        Proceso proceso = getInstancia().getProceso();
        if (proceso.getId().equals(Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS)) {
            return presentarTransicionesRegistroDocumentos();
        }

        if (proceso.getId().equals(
                Proceso.ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA)) {
            return presentarTransicionesGeneracionDocumentosInternos();
        }

        return presentarTransicionesGeneracionDocumentosExternos();
    }

    /**
     * Permite identificar si el documento puede presentar sus transiciones
     * activas correspondientes al estado para el proceso de registro de
     * documentos.
     *
     * @return {@code true} si el documento puede presentar las transiciones; de
     * lo contrario, {@code false}.
     */
    // 2017-04-26 jgarcia@controltechcg.com Issue #57 (SICDI-Controltech)
    private boolean presentarTransicionesRegistroDocumentos() {
        if (getTrd() == null) {
            return false;
        }

        if (getAsunto() == null || getAsunto().trim().isEmpty()) {
            return false;
        }

        if (getRemitenteNombre() == null || getRemitenteNombre().trim().isEmpty()) {
            return false;
        }

        if (getRemitenteTitulo() == null || getRemitenteTitulo().trim().isEmpty()) {
            return false;
        }

        if (getNumeroOficio() == null || getNumeroOficio().trim().isEmpty()) {
            return false;
        }

        if (getFechaOficio() == null) {
            return false;
        }

        if (getNumeroFolios() == null) {
            return false;
        }

        if (getClasificacion() == null) {
            return false;
        }

        return true;
    }

    /**
     * Permite identificar si el documento puede presentar sus transiciones
     * activas correspondientes al estado para los proceso de generación de
     * documentos internos.
     *
     * @return {@code true} si el documento puede presentar las transiciones; de
     * lo contrario, {@code false}.
     */
    // 2017-04-26 jgarcia@controltechcg.com Issue #57 (SICDI-Controltech)
    private boolean presentarTransicionesGeneracionDocumentosInternos() {
        if (getTrd() == null) {
            return false;
        }

        if (getDependenciaDestino() == null) {
            return false;
        }

        if (getAsunto() == null || getAsunto().trim().isEmpty()) {
            return false;
        }

        if (getClasificacion() == null) {
            return false;
        }

        if (getDocx4jDocumento() == null || getDocx4jDocumento().trim().isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * Permite identificar si el documento puede presentar sus transiciones
     * activas correspondientes al estado para los proceso de generación de
     * documentos externos.
     *
     * @return {@code true} si el documento puede presentar las transiciones; de
     * lo contrario, {@code false}.
     */
    // 2017-05-02 jgarcia@controltechcg.com Issue #57 (SICDI-Controltech)
    // Corrección para presentación de transiciones para documentos externos.
    private boolean presentarTransicionesGeneracionDocumentosExternos() {
        /*
		 * 2017-05-15 jgarcia@controltechcg.com Issue #81 (SICDI-Controltech):
		 * hotfix-81 -> Corrección para que los documentos del proceso externo
		 * no tengan acciones ni transiciones después de enviados.
         */
        final Integer estado = getInstancia().getEstado().getId();
        if (estado.equals(Estado.ENVIADO)) {
            return false;
        }

        if (getTrd() == null) {
            return false;
        }

        if (getDestinatario() == null || getDestinatario().trim().isEmpty()) {
            return false;
        }

        if (getDestinatarioTitulo() == null || getDestinatarioTitulo().trim().isEmpty()) {
            return false;
        }

        if (getAsunto() == null || getAsunto().trim().isEmpty()) {
            return false;
        }

        if (getClasificacion() == null) {
            return false;
        }

        if (getDocx4jDocumento() == null || getDocx4jDocumento().trim().isEmpty()) {
            return false;
        }

        return true;
    }

    public String mostrarCopiaDependecia() {
        // TODOS los documentos tiene aplica el campo Asunto
        if (asunto == null || asunto.trim().length() == 0) {
            return "N";
        }
        if (instancia == null || instancia.getProceso() == null) {
            return "N";
        }

        return Proceso.ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA
                .equals(instancia.getProceso().getId()) ? "S" : "N";

    }

    public String mostrarCargueDocumentoWord() {
        if (plantilla == null || plantilla.getId() == null || !plantilla.getId().equals(121)) {
            return "N";
        }
        return "Y";
    }

    @Deprecated
    public String mostrarBtnReasignacion() {
        return activarAccionReasignacion() ? "Y" : "N";
    }

    /**
     * Indica si el documento en pantalla debe presentar la acción de
     * reasignación.
     *
     * @return {@code true} si el documento debe presentar la acción de
     * reasignación; de lo contrario, {@code false}.
     */
    public boolean activarAccionReasignacion() {
        /*
		 * 2017-03-13 jgarcia@controltechcg.com Issue #4 (SIGDI-Incidencias01):
		 * Verificación de transición de "Reasignar", dependendiendo la
		 * configuracíón de la DB.
		 * 
		 * 2017-05-15 jgarcia@controltechcg.com Issue #81 (SICDI-Controltech):
		 * hotfix-81 -> Corrección para que los documentos del proceso externo
		 * no tengan acciones ni transiciones después de enviados.
         */
        final Integer procesoID = getInstancia().getProceso().getId();
        final Estado estado = getInstancia().getEstado();
        if (procesoID.equals(Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS)
                && estado.getId().equals(Estado.ENVIADO)) {
            return false;
        }

        return (estado != null && estado.getReasignacion() != null && estado.getReasignacion() == 1);
    }

    /**
     * Indica si el documento se encuentra en estado enviado para el proceso de
     * generación de documentos internos.
     *
     * @return {@code true} si el documento se encuentra en estado enviado para
     * el proceso de generación de documentos internos; de lo contrario,
     * {@code false}.
     * @see
     * Proceso#ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA
     * @see Estado#ENVIADO
     */
    // 2017-03-13 jgarcia@controltechcg.com Issue #48 (SIGDI-Controltech)
    public boolean esDocumentoEnviadoInterno() {
        final Integer procesoID = getInstancia().getProceso().getId();
        if (procesoID != Proceso.ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA) {
            return false;
        }

        final Integer estadoID = getInstancia().getEstado().getId();
        return (estadoID == Estado.ENVIADO);
    }

    /**
     * Indica si el documento se encuentra en estado de revisión para el proceso
     * de radicación de documentos.
     *
     * @return {@code true} si el documento se encuentra en estado de revisión
     * para el proceso de radicación de documentos; de lo contrario,
     * {@code false}.
     * @see Proceso#ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS
     * @see Estado#REVISIÓN_JEFE_JEFATURA
     */
    // 2017-06-01 jgarcia@controltechcg.com Issue #100 (SIGDI-Controltech)
    // feature-73
    public boolean esDocumentoRevisionRadicado() {
        final Integer procesoID = getInstancia().getProceso().getId();
        if (procesoID != Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS) {
            return false;
        }

        final Integer estadoID = getInstancia().getEstado().getId();
        return (estadoID == Estado.REVISIÓN_JEFE_JEFATURA);
    }

    public String mostrarPreview() {
        if (instancia == null || instancia.getEstado() == null || sticker != null) {
            return "N";
        }

        if (instancia.getProceso() != null
                && (Proceso.ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA
                        .equals(instancia.getProceso().getId())
                || Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS
                        .equals(instancia.getProceso().getId()))
                && (getPdf() == null || getPdf().trim().length() == 0)) {
            return "Y";
        }
        return "N";
    }

    public List<UsuarioVistoBuenoDTO> getVistosBuenos() {
        return vistosBuenos;
    }

    public void setVistosBuenos(List<UsuarioVistoBuenoDTO> vistosBuenos) {
        this.vistosBuenos = vistosBuenos;
    }

    public String getDocx4jDocumento() {
        if (docx4jDocumento == null) {
            return "";
        }
        return docx4jDocumento;
    }

    public void setDocx4jDocumento(String docx4jDocumento) {
        this.docx4jDocumento = docx4jDocumento;
    }

    public Usuario getUsuarioUltimaAccion() {
        return usuarioUltimaAccion;
    }

    public void setUsuarioUltimaAccion(Usuario usuarioUltimaAccion) {
        this.usuarioUltimaAccion = usuarioUltimaAccion;
    }

    public String consultarDependenciasDestinosAdicionales() {

        if (documentoDependenciaDestinos.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        for (int index = 0; index < documentoDependenciaDestinos.size(); index++) {

            sb.append(documentoDependenciaDestinos.get(index).getDependencia());
            if (index != (documentoDependenciaDestinos.size() - 1)) {
                sb.append(" - ");
            }
        }

        return sb.toString();
    }

    public List<DocumentoDependenciaDestino> getDocumentoDependenciaDestinos() {
        return documentoDependenciaDestinos;
    }

    public void setDocumentoDependenciaDestinos(List<DocumentoDependenciaDestino> documentoDependenciaDestinos) {
        this.documentoDependenciaDestinos = documentoDependenciaDestinos;
    }

    public boolean isDocx4jDocumentoVacio() {
        return docx4jDocumentoVacio;
    }

    public void setDocx4jDocumentoVacio(boolean docx4jDocumentoVacio) {
        this.docx4jDocumentoVacio = docx4jDocumentoVacio;
    }

    public boolean isDocx4jDocumentoFormatoInvalido() {
        return docx4jDocumentoFormatoInvalido;
    }

    public void setDocx4jDocumentoFormatoInvalido(boolean docx4jDocumentoFormatoInvalido) {
        this.docx4jDocumentoFormatoInvalido = docx4jDocumentoFormatoInvalido;
    }

    public Date getFechaGeneracionCodigoScanner() {
        return fechaGeneracionCodigoScanner;
    }

    public void setFechaGeneracionCodigoScanner(Date fechaGeneracionCodigoScanner) {
        this.fechaGeneracionCodigoScanner = fechaGeneracionCodigoScanner;
    }

    public Usuario getUsuarioValidaCodigoScanner() {
        return usuarioValidaCodigoScanner;
    }

    public void setUsuarioValidaCodigoScanner(Usuario usuarioValidaCodigoScanner) {
        this.usuarioValidaCodigoScanner = usuarioValidaCodigoScanner;
    }

    public String getCodigoValidaScanner() {
        return codigoValidaScanner;
    }

    public void setCodigoValidaScanner(String codigoValidaScanner) {
        this.codigoValidaScanner = codigoValidaScanner;
    }

    public Integer getEstadoCodigoValidaScanner() {
        return estadoCodigoValidaScanner;
    }

    public void setEstadoCodigoValidaScanner(Integer estadoCodigoValidaScanner) {
        this.estadoCodigoValidaScanner = estadoCodigoValidaScanner;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getMarcaAgua() {
        return marcaAgua;
    }

    public void setMarcaAgua(String marcaAgua) {
        this.marcaAgua = marcaAgua;
    }

    public RestriccionDifusion getRestriccionDifusion() {
        return restriccionDifusion;
    }

    public void setRestriccionDifusion(RestriccionDifusion restriccionDifusion) {
        this.restriccionDifusion = restriccionDifusion;
    }
}
