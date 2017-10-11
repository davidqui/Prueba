package com.laamware.ejercito.doc.web.ctrl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.repo.TrdRepository;

public class DocumentoMode extends HashMap<String, Boolean> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /*
		 * 2017-10-02 edison.gonzalez@controltechcg.com feature #129 : Se adiciona a la lista
		 * NAMES la clave gradoExterno, marca de agua externo Y restriccion de sdifusion
                 * para que el componente aparezca,según lo indicado en documento.ftl.
     */
    private static final List<String> NAMES = Collections
            .unmodifiableList(Arrays.asList("sticker", "trd", "destinatario", "asunto", "remitente", "numeroOficio",
                    "fechaOficio", "numeroFolios", "plazo", "clasificacion", "expediente", "adjuntos", "observaciones",
                    "contenido", "radicado", "formatos", "plantilla", "radicadoOrfeo", "numeroBolsa", "guardar", "gradoExterno", "marcaAguaExterno", "restriccionDifusion"));

    public static final String NAME_REGISTRO = "registro";

    public static final String NAME_CON_STICKER = "con_sticker";

    public static final String NAME_DIGITALIZANDO = "digitalizando";

    public static final String NAME_ENTREGADO = "entregado";

    public static final String NAME_EN_CONSTRUCCION = "en_construccion";

    public static final String NAME_SOLO_LECTURA = "solo_lectura";

    public static final String NAME_EN_CONSTRUCCION_INTERNO = "en_construccion_interno";

    public static final String NAME_EN_CONSTRUCCION_EXTERNO = "en_construccion_externo";

    public static final String NAME_SOLO_LECTURA_INTERNO = "solo_lectura_interno";

    private static final DocumentoMode REGISTRO = new DocumentoMode();

    private static final DocumentoMode CON_STICKER = new DocumentoMode();

    private static final DocumentoMode DIGITALIZANDO = new DocumentoMode();

    private static final DocumentoMode ENTREGADO = new DocumentoMode();

    private static final DocumentoMode EN_CONSTRUCCION = new DocumentoMode();

    private static final DocumentoMode SOLO_LECTURA = new DocumentoMode();

    private static final DocumentoMode EN_CONSTRUCCION_INTERNO = new DocumentoMode();

    private static final DocumentoMode EN_CONSTRUCCION_EXTERNO = new DocumentoMode();

    private static final DocumentoMode SOLO_LECTURA_INTERNO = new DocumentoMode();

    private static final Map<String, DocumentoMode> modes = new HashMap<String, DocumentoMode>();

    private DocumentoValidator validator;

    public DocumentoMode() {
        noAll();
    }

    static {
        /*
		 * 2017-09-28 edison.gonzalez@controltechcg.com feature #129 : Se adiciona al mapa
		 * de construcción la clave gradoExterno y marca de agua externo para que el componente aparezca,
		 * según lo indicado en documento.ftl.
         */
        REGISTRO.edit("destinatario").editAndView("asunto").editAndView("remitente").editAndView("numeroOficio")
                .editAndView("fechaOficio").editAndView("numeroFolios").editAndView("clasificacion")
                .editAndView("radicadoOrfeo").editAndView("numeroBolsa").editAndView("trd").editAndView("observaciones")
                .editAndView("guardar").editAndView("restriccionDifusion").editAndView("gradoExterno");
        REGISTRO.validator = new RegistroValidator();
        modes.put(NAME_REGISTRO, REGISTRO);

        CON_STICKER.view("sticker").view("destinatario").view("asunto").view("remitente").view("numeroOficio")
                .view("fechaOficio").view("numeroFolios").view("clasificacion").view("radicado").view("radicadoOrfeo")
                .view("numeroBolsa").editAndView("trd").editAndView("observaciones").editAndView("guardar").view("restriccionDifusion")
                .view("gradoExterno");
        CON_STICKER.validator = new ConStickerValidator();
        modes.put(NAME_CON_STICKER, CON_STICKER);

        DIGITALIZANDO.view("sticker").view("destinatario").view("asunto").view("remitente").view("numeroOficio")
                .view("fechaOficio").view("numeroFolios").view("clasificacion").editAndView("adjuntos")
                .editAndView("observaciones").view("radicado").view("radicadoOrfeo").view("numeroBolsa")
                .editAndView("trd").editAndView("guardar").view("restriccionDifusion").view("gradoExterno");
        DIGITALIZANDO.validator = new DigitalizandoValidator();
        modes.put(NAME_DIGITALIZANDO, DIGITALIZANDO);

        ENTREGADO.editAndView("trd").view("destinatario").view("asunto").view("remitente").view("numeroOficio")
                .view("fechaOficio").view("numeroFolios").view("clasificacion").editAndView("expediente")
                .view("adjuntos").editAndView("observaciones").view("radicado").view("radicadoOrfeo")
                .view("numeroBolsa").editAndView("trd").editAndView("guardar").view("restriccionDifusion")
                .view("gradoExterno");
        ENTREGADO.validator = new EntregadoValidator();
        modes.put(NAME_ENTREGADO, ENTREGADO);

        /*
		 * 2017-02-07 jgarcia@controltechcg.com Issue #47 : Se adiciona al mapa
		 * de construcción la clave expediente para que el componente aparezca,
		 * según lo indicado en documento.ftl.
         */
        EN_CONSTRUCCION.editAndView("trd").editAndView("destinatario").editAndView("asunto")
                .editAndView("clasificacion").editAndView("adjuntos").editAndView("observaciones")
                .editAndView("contenido").editAndView("formatos").editAndView("plantilla").editAndView("plazo")// .editAndView("docx4jDocumento")
                .editAndView("guardar").editAndView("expediente").editAndView("restriccionDifusion")
                .editAndView("marcaAguaExterno").editAndView("gradoExterno");
        EN_CONSTRUCCION.validator = new EnConstruccionValidator();
        modes.put(NAME_EN_CONSTRUCCION, EN_CONSTRUCCION);

        /*
		 * 2017-02-07 jgarcia@controltechcg.com Issue #47 : Se adiciona al mapa
		 * de construcción la clave expediente para que el componente aparezca,
		 * según lo indicado en documento.ftl.
         */
        EN_CONSTRUCCION_INTERNO.editAndView("trd").editAndView("destinatario").editAndView("asunto")
                .editAndView("clasificacion").editAndView("adjuntos").editAndView("observaciones")
                .editAndView("contenido").editAndView("formatos").editAndView("plantilla").editAndView("guardar")
                .editAndView("expediente").editAndView("restriccionDifusion");
        EN_CONSTRUCCION_INTERNO.validator = new EnConstruccionValidator();
        modes.put(NAME_EN_CONSTRUCCION_INTERNO, EN_CONSTRUCCION_INTERNO);

        /*
		 * 2017-02-07 jgarcia@controltechcg.com Issue #47 : Se adiciona al mapa
		 * de construcción la clave expediente para que el componente aparezca,
		 * según lo indicado en documento.ftl.
         */
        EN_CONSTRUCCION_EXTERNO.editAndView("trd").editAndView("destinatario").editAndView("asunto")
                .editAndView("plazo").editAndView("clasificacion").editAndView("adjuntos").editAndView("observaciones")
                .editAndView("contenido").editAndView("formatos").editAndView("plantilla").editAndView("guardar")
                .editAndView("expediente").editAndView("gradoExterno").editAndView("marcaAguaExterno")
                .editAndView("restriccionDifusion");
        EN_CONSTRUCCION_EXTERNO.validator = new EnConstruccionExternoValidator();
        modes.put(NAME_EN_CONSTRUCCION_EXTERNO, EN_CONSTRUCCION_EXTERNO);

        SOLO_LECTURA.view("trd").view("destinatario").view("asunto").view("remitente").view("numeroOficio")
                .view("fechaOficio").view("numeroFolios").view("plazo").view("clasificacion").view("expediente")
                .view("adjuntos").editAndView("observaciones").view("radicado").view("contenido").view("plantilla")
                .view("radicadoOrfeo").view("numeroBolsa").view("restriccionDifusion")
                .view("gradoExterno").view("marcaAguaExterno");
        modes.put(NAME_SOLO_LECTURA, SOLO_LECTURA);

        SOLO_LECTURA_INTERNO.view("trd").view("destinatario").view("remitente").view("asunto").view("remitente")
                .view("numeroOficio").view("fechaOficio").view("numeroFolios").view("plazo").view("clasificacion")
                .view("expediente").view("adjuntos").editAndView("observaciones").view("radicado").view("contenido")
                .view("plantilla").view("restriccionDifusion").view("gradoExterno");
        modes.put(NAME_SOLO_LECTURA_INTERNO, SOLO_LECTURA_INTERNO);
    }

    public DocumentoMode noAll() {
        for (String name : DocumentoMode.NAMES) {
            noView(name);
            noEdit(name);
        }
        return this;
    }

    public DocumentoMode noEdit(String name) {
        this.put(name + "_edit", false);
        return this;
    }

    public DocumentoMode edit(String name) {
        this.put(name + "_edit", true);
        return this;
    }

    public DocumentoMode editAndView(String name) {
        this.edit(name);
        this.view(name);
        return this;
    }

    public DocumentoMode view(String name) {
        this.put(name + "_view", true);
        return this;
    }

    public DocumentoMode noView(String name) {
        this.put(name + "_view", false);
        return this;
    }

    /**
     * Obtiene el modo mediante el nombre
     *
     * @param name
     * @return
     */
    public static DocumentoMode getByName(String name) {
        DocumentoMode mode = modes.get(name);
        if (mode == null) {
            return SOLO_LECTURA;
        } else {
            return mode;
        }
    }

    /**
     * Valida un documento
     *
     * @param documento
     * @param i
     * @param bind
     */
    public void validate(Documento documento, Instancia i, BindingResult bind) {
        if (validator != null) {
            validator.validate(documento, i, bind);
        }
    }
    
    /**
     * Transfiere las propiedades que son editables de source a target
     *
     * @param source
     * @param target
     */
    public void transferirEditables(Documento source, Documento target) {

        if (get("trd_edit")) {
            target.setTrd(source.getTrd());
        }

        if (get("destinatario_edit")) {
            target.setDependenciaDestino(source.getDependenciaDestino());
            target.setDestinatarioNombre(source.getDestinatarioNombre());
            target.setDestinatarioTitulo(source.getDestinatarioTitulo());
            target.setDestinatarioDireccion(source.getDestinatarioDireccion());
        }

        if (get("asunto_edit")) {
            target.setAsunto(source.getAsunto());
        }

        if (get("remitente_edit")) {
            target.setDependenciaRemitente(source.getDependenciaRemitente());
            target.setRemitenteNombre(source.getRemitenteNombre());
            target.setRemitenteTitulo(source.getRemitenteTitulo());
            target.setRemitenteDireccion(source.getRemitenteDireccion());
        }

        if (get("radicadoOrfeo_edit")) {
            target.setRadicadoOrfeo(source.getRadicadoOrfeo());
        }

        if (get("numeroOficio_edit")) {
            target.setNumeroOficio(source.getNumeroOficio());
        }

        if (get("numeroBolsa_edit")) {
            target.setNumeroBolsa(source.getNumeroBolsa());
        }

        if (get("fechaOficio_edit")) {
            target.setFechaOficio(source.getFechaOficio());
        }

        if (get("numeroFolios_edit")) {
            target.setNumeroFolios(source.getNumeroFolios());
        }

        if (get("plazo_edit")) {
            target.setPlazo(source.getPlazo());
        }

        if (get("clasificacion_edit")) {
            target.setClasificacion(source.getClasificacion());
        }

        if (get("expediente_edit")) {
            target.setExpediente(source.getExpediente());
        }

        if (get("contenido_edit")) {
            target.setContenido(source.getContenido());
        }

        if (get("plantilla_edit")) {
            target.setPlantilla(source.getPlantilla());
        }

        /*
		 * 2017-09-28 edison.gonzalez@controltechcg.com issue #129 : Se adiciona al mapa
		 * de construcción la clave gradoExterno y marca de agua externo para que el componente aparezca,
		 * según lo indicado en documento.ftl.
         */
        if (get("gradoExterno_edit")) {
            target.setGradoExterno(source.getGradoExterno());
        }

        if (get("marcaAguaExterno_edit")) {
            target.setMarcaAguaExterno(source.getMarcaAguaExterno());
        }

        if (get("restriccionDifusion_edit")) {
            target.setRestriccionDifusion(source.getRestriccionDifusion());
        }
    }
    
    /**
     * Transfiere las propiedades que no son editables de source a target
     *
     * @param source
     * @param target
     */
    public void transferirNoEditables(Documento source, Documento target) {

        if (get("trd_edit") == false) {
            target.setTrd(source.getTrd());
        }

        if (get("destinatario_edit") == false) {
            target.setDependenciaDestino(source.getDependenciaDestino());
            target.setDestinatarioNombre(source.getDestinatarioNombre());
            target.setDestinatarioTitulo(source.getDestinatarioTitulo());
            target.setDestinatarioDireccion(source.getDestinatarioNombre());
        }

        if (get("asunto_edit") == false) {
            target.setAsunto(source.getAsunto());
        }

        if (get("remitente_edit") == false) {
            target.setDependenciaRemitente(source.getDependenciaRemitente());
            target.setRemitenteNombre(source.getRemitenteNombre());
            target.setRemitenteTitulo(source.getRemitenteTitulo());
            target.setRemitenteDireccion(source.getRemitenteNombre());
        }

        if (get("radicadoOrfeo_edit") == false) {
            target.setRadicadoOrfeo(source.getRadicadoOrfeo());
        }

        if (get("numeroOficio_edit") == false) {
            target.setNumeroOficio(source.getNumeroOficio());
        }

        if (get("numeroBolsa_edit") == false) {
            target.setNumeroBolsa(source.getNumeroBolsa());
        }

        if (get("fechaOficio_edit") == false) {
            target.setFechaOficio(source.getFechaOficio());
        }

        if (get("numeroFolios_edit") == false) {
            target.setNumeroFolios(source.getNumeroFolios());
        }

        if (get("plazo_edit") == false) {
            target.setPlazo(source.getPlazo());
        }

        if (get("clasificacion_edit") == false) {
            target.setClasificacion(source.getClasificacion());
        }

        if (get("expediente_edit") == false) {
            target.setExpediente(source.getExpediente());
        }

        if (get("contenido_edit") == false) {
            target.setContenido(source.getContenido());
        }

        if (get("plantilla_edit") == false) {
            target.setPlantilla(source.getPlantilla());
        }

        /*
            2017-09-28 edison.gonzalez@controltechcg.com issue #129 : Se adiciona al mapa
            de construcción la clave gradoExterno, marca de agua externo y restriccion
            de difusion para que el componente aparezca, según lo indicado en documento.ftl.
         */
        if (get("gradoExterno_edit") == false) {
            target.setGradoExterno(source.getGradoExterno());
        }

        if (get("marcaAguaExterno_edit") == false) {
            target.setMarcaAguaExterno(source.getMarcaAguaExterno());
        }

        if (get("restriccionDifusion_edit") == false) {
            target.setRestriccionDifusion(source.getRestriccionDifusion());
        }

        // Información no editable por naturaleza
        target.setAprueba(source.getAprueba());
        target.setCuando(source.getCuando());
        target.setCuandoMod(source.getCuandoMod());
        target.setElabora(source.getElabora());
        target.setFirma(source.getFirma());
        target.setInstancia(source.getInstancia());
        target.setQuien(source.getQuien());
        target.setQuienMod(source.getQuienMod());
        target.setRadicado(source.getRadicado());
        target.setRelacionado(source.getRelacionado());
        target.setSticker(source.getSticker());
        target.setVistoBueno(source.getVistoBueno());
        target.setPdf(source.getPdf());

    }

    public void defaults(Documento target, Usuario user, DependenciaRepository dependenciaRepository,
            TrdRepository trdRepository) {

        // Si la TRD tiene plazo entonces debe fijarse la fecha de plazo a
        // partir de este valor y la fecha de creación del documento
        if (target.getTrd() != null) {
            Trd trd = trdRepository.getOne(target.getTrd().getId());
            if (trd.getPlazo() != null) {
                Date nuevoPlazo = DateUtils.addDays(target.getCuando(), trd.getPlazo());
                target.setPlazo(nuevoPlazo);
            }
        }

        // Si el modo de edición es REGISTRO entonces la dependencia destino
        // debe ser la super dependencia del usuario
        if (this == DocumentoMode.REGISTRO) {
            if (user.getDependencia() == null) {
                throw new RuntimeException("Usuario no tiene dependencia asignada");
            }
            target.setDependenciaDestino(user.getDependencia().obtenerJefatura(dependenciaRepository));
        }

    }

    /**
     * Validador para el modo NAME_REGISTRO
     *
     * @author acpreda
     *
     */
    public static class RegistroValidator implements DocumentoValidator {

        @Override
        public void validate(Documento target, Instancia i, Errors errors) {
            Documento doc = (Documento) target;
            if (doc.getDependenciaDestino() == null || doc.getDependenciaDestino().getId() == null) {
                errors.rejectValue("dependenciaDestino", "documento.dependenciaDestino.empty");
            }
            if (StringUtils.isBlank(doc.getAsunto())) {
                errors.rejectValue("asunto", "documento.asunto.empty");
            }
            if (StringUtils.isBlank(doc.getRemitenteNombre())) {
                errors.rejectValue("remitenteNombre", "documento.remitenteNombre.empty");
            }
            if (StringUtils.isBlank(doc.getRemitenteTitulo())) {
                errors.rejectValue("remitenteTitulo", "documento.remitenteTitulo.empty");
            }
            
            //2017-09-29 edison.gonzalez@controltechcg.com Issue #129: Se adiciona el mensaje
            //que controla que la direccion del remitente sea obligatoria.
            if(StringUtils.isBlank(doc.getRemitenteDireccion())) {
                errors.rejectValue("remitenteDireccion", "documento.remitenteDireccion.empty");
            }
            
            if (StringUtils.isBlank(doc.getNumeroOficio())) {
                errors.rejectValue("numeroOficio", "documento.numeroOficio.empty");
            }
            if (StringUtils.isNumeric(doc.getNumeroOficio()) == false
                    && StringUtils.isBlank(doc.getNumeroOficio()) == false) {
                errors.rejectValue("numeroOficio", "documento.numeroOficio.typeMismatch");
            }
            if (doc.getFechaOficio() == null) {
                errors.rejectValue("fechaOficio", "documento.fechaOficio.empty");
            }
            if (StringUtils.isNumeric(doc.getNumeroBolsa()) == false
                    && StringUtils.isBlank(doc.getNumeroBolsa()) == false) {
                errors.rejectValue("numeroBolsa", "documento.numeroBolsa.typeMismatch");
            }
            if (doc.getNumeroFolios() == null || doc.getNumeroFolios() <= 0) {
                errors.rejectValue("numeroFolios", "documento.numeroFolios.empty", "me mensahe personal");
            }
            if (doc.getClasificacion() == null || doc.getClasificacion().getId() == null) {
                errors.rejectValue("clasificacion", "documento.clasificacion.empty");
            }

            /*
			 * 2017-02-07 jgarcia@controltechcg.com Issue #93: Se coloca
			 * validacíón de selección de TRD para el mapa de validación del
			 * modo "registro".
             */
            if (doc.getTrd() == null || doc.getTrd().getId() <= 0) {
                errors.rejectValue("trd", "documento.trd.empty");
            }
        }
    }

    /**
     * Validador para el modo NAME_ENTREGADO
     *
     * @author acpreda
     *
     */
    public static class EntregadoValidator implements DocumentoValidator {

        RegistroValidator registroValidator = new RegistroValidator();

        @Override
        public void validate(Documento target, Instancia i, Errors errors) {
            registroValidator.validate(target, i, errors);
        }

    }

    /**
     * Validador para el modo NAME_EN_CONSTRUCCION
     *
     * @author acpreda
     *
     */
    public static class EnConstruccionValidator implements DocumentoValidator {

        @Override
        public void validate(Documento target, Instancia i, Errors errors) {

            Documento doc = (Documento) target;

            // Dependiendo del modo de destinatario realiza la validación
            String tipoDestinatario = i.getVariable(Documento.DOC_MODE_DESTINATARIO);
            if (Documento.VAL_DOC_MODE_DESTINATARIO_DEP.equals(tipoDestinatario)) {
                if (doc.getDependenciaDestino() == null || doc.getDependenciaDestino().getId() == null) {
                    errors.rejectValue("dependenciaDestino", "documento.dependenciaDestino.empty");
                } else if (doc.getDependenciaDestino().getJefe() == null
                        || doc.getDependenciaDestino().getJefe().getId() == null) {
                    errors.rejectValue("dependenciaDestino", "documento.dependenciaDestino.jefe.empty");
                }
            } else if (Documento.VAL_DOC_MODE_DESTINATARIO_TEXTO.equals(tipoDestinatario)) {
                if (StringUtils.isBlank(doc.getDestinatarioNombre())) {
                    errors.rejectValue("dependenciaDestino", "documento.dependenciaDestino.empty");
                }
            }

            if (StringUtils.isBlank(doc.getAsunto())) {
                errors.rejectValue("asunto", "documento.asunto.empty");
            }
            if (doc.getClasificacion() == null || doc.getClasificacion().getId() == null) {
                errors.rejectValue("clasificacion", "documento.clasificacion.empty");
            }

            if (doc.getTrd() == null || doc.getTrd().getId() <= 0) {
                errors.rejectValue("trd", "documento.trd.empty");
            }
            if (doc.getPlazo() != null) {
                Calendar cHoy = Calendar.getInstance();
                cHoy.set(Calendar.HOUR, 0);
                cHoy.set(Calendar.HOUR_OF_DAY, 0);
                cHoy.set(Calendar.MINUTE, 0);
                cHoy.set(Calendar.SECOND, 0);
                cHoy.set(Calendar.MILLISECOND, 0);

                Calendar cPlazo = Calendar.getInstance();
                cPlazo.setTime(doc.getPlazo());
                cPlazo.set(Calendar.HOUR, 0);
                cPlazo.set(Calendar.HOUR_OF_DAY, 0);
                cPlazo.set(Calendar.MINUTE, 0);
                cPlazo.set(Calendar.SECOND, 0);
                cPlazo.set(Calendar.MILLISECOND, 0);

                if (cPlazo.compareTo(cHoy) < 0) {
                    errors.rejectValue("plazo", "documento.plazo.invalid");
                }
            }
            if (doc.isDocx4jDocumentoVacio()) {
                errors.rejectValue("docx4jDocumento", "documento.docx4jDocumento.empty");
            } else if (doc.isDocx4jDocumentoFormatoInvalido()) {
                errors.rejectValue("docx4jDocumento", "documento.docx4jDocumento.invalidFormat");
            }
            /*
			 * if(doc.getPlantilla() == null) { errors.rejectValue("plantilla",
			 * "documento.plantilla.empty"); }
             */
            if (i.getProceso().getId().equals(Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS)
                    && StringUtils.isBlank(doc.getDestinatarioNombre())) {
                errors.rejectValue("destinatarioNombre", "documento.destinatarioNombre.empty");
            }
            if (i.getProceso().getId().equals(Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS)
                    && StringUtils.isBlank(doc.getDestinatarioTitulo())) {
                errors.rejectValue("destinatarioTitulo", "documento.destinatarioTitulo.empty");
            }

            /*
			 * 2017-09-29 edison.gonzalez@controltechcg.com Issue #129: Se coloca
			 * validacíón del campo marca de agua como obligatorio.
             */
            if (i.getProceso().getId().equals(Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS)
                    && StringUtils.isBlank(doc.getMarcaAguaExterno())) {
                errors.rejectValue("marcaAguaExterno", "documento.marcaAguaExterno.empty");
            }
            
            if (i.getProceso().getId().equals(Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS)
                    && StringUtils.isBlank(doc.getDestinatarioDireccion())) {
                errors.rejectValue("destinatarioDireccion", "documento.destinatarioDireccion.empty");
            }

        }

    }

    /**
     * Validador para el modo NAME_EN_CONSTRUCCION
     *
     * @author acpreda
     *
     */
    public static class EnConstruccionExternoValidator implements DocumentoValidator {

        @Override
        public void validate(Documento target, Instancia i, Errors errors) {

            Documento doc = (Documento) target;
            Date hoy = new Date();
            if (StringUtils.isBlank(doc.getAsunto())) {
                errors.rejectValue("asunto", "documento.asunto.empty");
            }
            if (StringUtils.isBlank(doc.getDestinatarioNombre())) {
                errors.rejectValue("destinatarioNombre", "documento.destinatarioNombre.empty");
            }
            if (doc.getClasificacion() == null || doc.getClasificacion().getId() == null) {
                errors.rejectValue("clasificacion", "documento.clasificacion.empty");
            }

            if (doc.getTrd() == null || doc.getTrd().getId() <= 0) {
                errors.rejectValue("trd", "documento.trd.empty");
            }
        }

    }

    /**
     * Validador para el modo NAME_DIGITALIZANDO
     *
     * @author acpreda
     *
     */
    public static class DigitalizandoValidator implements DocumentoValidator {

        RegistroValidator registroValidator = new RegistroValidator();

        @Override
        public void validate(Documento target, Instancia i, Errors errors) {
            registroValidator.validate(target, i, errors);
        }

    }

    /**
     * Validador para el modo NAME_CON_STICKER
     *
     * @author acpreda
     *
     */
    public static class ConStickerValidator implements DocumentoValidator {

        RegistroValidator registroValidator = new RegistroValidator();

        @Override
        public void validate(Documento target, Instancia i, Errors errors) {
            registroValidator.validate(target, i, errors);
        }

    }
}