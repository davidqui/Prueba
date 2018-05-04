package com.laamware.ejercito.doc.web.ctrl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aspose.words.Document;
import com.laamware.ejercito.doc.web.dto.CargoDTO;
import com.laamware.ejercito.doc.web.dto.FlashAttributeValue;
import com.laamware.ejercito.doc.web.dto.KeysValuesAsposeDocxDTO;
import com.laamware.ejercito.doc.web.dto.UsuarioVistoBuenoDTO;
import com.laamware.ejercito.doc.web.entity.Adjunto;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.Clasificacion;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.DependenciaCopiaMultidestino;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.DocumentoDependenciaDestino;
import com.laamware.ejercito.doc.web.entity.DocumentoEnConsulta;
import com.laamware.ejercito.doc.web.entity.DocumentoObservacion;
import com.laamware.ejercito.doc.web.entity.Estado;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.HProcesoInstancia;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.OFSStage;
import com.laamware.ejercito.doc.web.entity.PDFDocumento;
import com.laamware.ejercito.doc.web.entity.Plantilla;
import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.entity.Radicacion;
import com.laamware.ejercito.doc.web.entity.RestriccionDifusion;
import com.laamware.ejercito.doc.web.entity.Tipologia;
import com.laamware.ejercito.doc.web.entity.Transicion;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.entity.Variable;
import com.laamware.ejercito.doc.web.repo.AdjuntoRepository;
import com.laamware.ejercito.doc.web.repo.AdjuntoRepositoryCustom;
import com.laamware.ejercito.doc.web.repo.CargosRepository;
import com.laamware.ejercito.doc.web.repo.ClasificacionRepository;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoDependenciaAdicionalRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoDependenciaRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoObservacionRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.ExpedienteRepository;
import com.laamware.ejercito.doc.web.repo.FormatoRepository;
import com.laamware.ejercito.doc.web.repo.HProcesoInstanciaRepository;
import com.laamware.ejercito.doc.web.repo.InstanciaRepository;
import com.laamware.ejercito.doc.web.repo.PlantillaRepository;
import com.laamware.ejercito.doc.web.repo.ProcesoRepository;
import com.laamware.ejercito.doc.web.repo.RadicacionRepository;
import com.laamware.ejercito.doc.web.repo.RestriccionDifusionRepository;
import com.laamware.ejercito.doc.web.repo.TipologiaRepository;
import com.laamware.ejercito.doc.web.repo.TransicionRepository;
import com.laamware.ejercito.doc.web.repo.TrdRepository;
import com.laamware.ejercito.doc.web.repo.VariableRepository;
import com.laamware.ejercito.doc.web.serv.ArchivoAutomaticoService;
import com.laamware.ejercito.doc.web.serv.DependenciaCopiaMultidestinoService;
import com.laamware.ejercito.doc.web.serv.DependenciaService;
import com.laamware.ejercito.doc.web.serv.DocumentoEnConsultaService;
import com.laamware.ejercito.doc.web.serv.DriveService;
import com.laamware.ejercito.doc.web.serv.JasperService;
import com.laamware.ejercito.doc.web.serv.OFS;
import com.laamware.ejercito.doc.web.serv.OFSEntry;
import com.laamware.ejercito.doc.web.serv.ProcesoService;
import com.laamware.ejercito.doc.web.serv.RadicadoService;
import com.laamware.ejercito.doc.web.serv.TRDService;
import com.laamware.ejercito.doc.web.serv.UsuarioService;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.BusinessLogicValidation;
import com.laamware.ejercito.doc.web.util.GeneralUtils;
import com.laamware.ejercito.doc.web.util.UsuarioGradoComparator;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.logging.Level;

import net.sourceforge.jbarcodebean.JBarcodeBean;
import net.sourceforge.jbarcodebean.model.Interleaved25;
import org.springframework.jdbc.UncategorizedSQLException;

@Controller(value = "documentoController")
@RequestMapping(DocumentoController.PATH)
public class DocumentoController extends UtilController {

    private static final String TITULO_FLASH_ASIGNADOS_COPIA_MULTIDESTINO = "Asignados Copia Dependencia Multidestino:";

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy hh:mm a", new Locale("es", "CO"));
    /* **************************** REFORMA ********************************* */

    private static final Logger LOG = LoggerFactory.getLogger(DocumentoController.class);

    static final String PATH = "/documento";
    private static final com.aspose.words.License LICENSE = new com.aspose.words.License();
    /*
    2017-10-02 edison.gonzalez@controltechcg.com feature #129 : Organizacion
    de las variables.
     */
    private String idPlantillaSeleccionada;
    private boolean fileSaveRequestGet = false;
    public static final String STRING_JNLP = " <jnlp" + " spec=\"1.0+\""
            + " codebase=\"http://192.168.52.112:8181/java\"" + " href=\"launch.jnlp\">" + " <information>"
            + "<title>SIGDI-Scanner</title>" + "<vendor>ASCONTROLTECH - SIGDI</vendor>" + "<homepage href=\"\"/>"
            + "<description>SIGDI-Scanner</description>" + "<description kind=\"short\">Scanner - SIGDI</description>"
            + "<offline-allowed/>" + "</information>" + " <resources>" + "<j2se version=\"1.7+\"/>"
            + "<jar href='doc-web-scanner.jar' main='true'/>" + "<jar href='lib/uk.co.mmscomputing.device.twain.jar'/>"
            + " </resources>" + " <application-desc main-class='doc.web.scanner.JFrameDocWebScanner'/>" + " </jnlp> ";
    List<Plantilla> listaPlantilla;
    private String imagesRoot;
    private String ofsRoot;

    @Autowired
    private DataSource dataSource;

    @Autowired
    DocumentoDependenciaAdicionalRepository documentoDependenciaAdicionalRepository;

    @Autowired
    DocumentoRepository documentRepository;

    @Autowired
    ExpedienteRepository expedienteRepository;

    @Autowired
    ProcesoService procesoService;

    @Autowired
    JasperService jasperService;

    @Autowired
    DriveService driveService;

    @Autowired
    OFS ofs;

    @Autowired
    PlantillaRepository plantillaRepository;

    @Autowired
    DependenciaRepository dependenciaRepository;

    @Autowired
    DocumentoObservacionRepository dobRepository;

    @Autowired
    DocumentoGeneradorVariables documentoGeneradorVariables;

    @Autowired
    DocumentoDependenciaRepository documentoDependenciaRepository;

    @Autowired
    FormatoRepository formatoRepository;

    @Autowired
    AdjuntoRepositoryCustom ajuntoRcustom;

    // 2017-04-18 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech)
    @Autowired
    ArchivoAutomaticoService archivoAutomaticoService;

    // 2017-04-20 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech)
    @Autowired
    DocumentoEnConsultaService documentoEnConsultaService;

    // 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
    // feature-78
    @Autowired
    UsuarioService usuarioService;

    // 2017-09-29 edison.gonzalez@controltechcg.com Issue #129 (SICDI-Controltech)
    // feature-129
    @Autowired
    RestriccionDifusionRepository restriccionDifusionRepository;

    @Autowired
    ProcesoRepository procesoRepository;

    @Autowired
    InstanciaRepository instanciaRepository;

    @Autowired
    HProcesoInstanciaRepository hprocesoInstanciaRepository;

    @Autowired
    TransicionRepository transicionRepository;

    @Autowired
    VariableRepository variableRepository;

    @Autowired
    ClasificacionRepository clasificacionRepository;

    @Autowired
    TrdRepository trdRepository;

    @Autowired
    AdjuntoRepository adjuntoRepository;

    @Autowired
    TipologiaRepository tipologiaRepository;

    /**
     * Servicio de generacion de numero de radicado.
     */
    @Autowired
    RadicadoService radicadoService;

    @Autowired
    RadicacionRepository radicacionRepository;

    @Autowired
    CargosRepository cargosRepository;

    @Autowired
    TRDService tRDService;

    /**
     * 2018-04-11 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech)
     * feature-156: Servicio de multidestino.
     */
    @Autowired
    private DependenciaCopiaMultidestinoService multidestinoService;

    /*
     * 2018-04-12 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech)
     * feature-156: Servicio de dependencias.
     */
    @Autowired
    private DependenciaService dependenciaService;

    /* ---------------------- públicos ------------------------------- */
    /**
     * Muestra la página de acceso denegado
     *
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping(value = "/acceso-denegado")
    public String accesoDenegado(Model model, Principal principal) {
        return "documento-acceso-denegado";
    }

    @RequestMapping(value = "/documento_html_seleccionado", method = RequestMethod.GET)
    public @ResponseBody
    String documentoHtmlSeleccionado(@RequestParam("id") String id) {

        if (id == null || id.trim().length() == 0) {
            return "";
        }
        Integer idPlantilla = Integer.parseInt(id);
        for (Plantilla plantilla : listaPlantilla) {
            if (plantilla.getId().equals(idPlantilla)) {
                return plantilla.getTextoDefultPlantilla();
            }
        }
        return "";
    }

    /**
     *
     * @param pin
     * @param file
     * @param doc
     * @param docBind
     * @param redirect
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping(value = "/savepin", method = RequestMethod.POST)
    public String documentopin(@RequestParam("pin") String pin,
            @RequestParam(value = "file", required = false) MultipartFile file, @Valid Documento doc,
            BindingResult docBind, final RedirectAttributes redirect, Model model, Principal principal) {

        fileSaveRequestGet = true;
        return documento(pin, file, doc, docBind, redirect, model, principal);

    }

    /**
     * s
     *
     * @param documento
     * @param root
     * @return
     * @throws java.lang.Exception
     */
    public static KeysValuesAsposeDocxDTO getKeysValuesWord(PDFDocumento documento, String root) throws Exception {

        KeysValuesAsposeDocxDTO keysValuesAsposeDocxDTO = new KeysValuesAsposeDocxDTO();
        if (documento == null) {
            return keysValuesAsposeDocxDTO;
        }

        byte[] imageInByteBarCode = null;

        JBarcodeBean barcode = new JBarcodeBean();

        // nuestro tipo de codigo de barra
        barcode.setCodeType(new Interleaved25());
        // barcode.setCodeType(new Code39());

        if (documento.getPdfTexto4() != null && documento.getPdfTexto4().trim().length() > 0) {
            // nuestro valor a codificar y algunas configuraciones mas
            String chars = documento.getPdfTexto4().trim();
            String codigo = "";
            for (int indice = 0; indice < chars.length(); indice++) {
                try {
                    codigo += String.valueOf(Integer.parseInt(chars.charAt(indice) + ""));
                } catch (Exception e) {
                    // no es un numero
                }
            }
            barcode.setCode(codigo);
        } else {
            barcode.setCode("000000000");
        }
        barcode.setCheckDigit(true);

        BufferedImage bufferedImage = barcode.draw(new BufferedImage(200, 20, BufferedImage.TYPE_INT_RGB));

        File fTempIMGCodBarra = null;
        try {
            fTempIMGCodBarra = File.createTempFile("_sigdi_img_tmp_", ".png");
            ImageIO.write(bufferedImage, "png", fTempIMGCodBarra);

            imageInByteBarCode = FileUtils.readFileToByteArray(fTempIMGCodBarra);

            fTempIMGCodBarra.delete();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (fTempIMGCodBarra != null) {
                    fTempIMGCodBarra.deleteOnExit();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        keysValuesAsposeDocxDTO.put("N_CLASIFICACION", documento.getPdfTexto1());
        keysValuesAsposeDocxDTO.put("n_radicado", documento.getPdfTexto3() != null ? documento.getPdfTexto3() : "");
        keysValuesAsposeDocxDTO.put("fecha_doc", documento.getPdfTexto5() != null ? documento.getPdfTexto5() : "");
        keysValuesAsposeDocxDTO.put("ELABORA_DEP_SPADRE_S",
                documento.getPdfTexto6() != null ? documento.getPdfTexto6() : "");
        keysValuesAsposeDocxDTO.put("S_DEP_DESTINO",
                documento.getPdfTexto6_2() != null ? documento.getPdfTexto6_2() : "");
        keysValuesAsposeDocxDTO.put("cod_barra", imageInByteBarCode);
        keysValuesAsposeDocxDTO.put("FIRMA_GRADO", documento.getPdfTexto17() != null ? documento.getPdfTexto17() : "");
        // keysValuesAsposeDocxDTO.put("FIRMA_GRADO_CORTO",
        // documento.getPdfTexto57() != null ? documento.getPdfTexto57() : "");
        keysValuesAsposeDocxDTO.put("FIRMA_NOM", documento.getPdfTexto18() != null ? documento.getPdfTexto18() : "");
        keysValuesAsposeDocxDTO.put("FIRMA_CARGO", documento.getPdfTexto19() != null ? documento.getPdfTexto19() : "");
        keysValuesAsposeDocxDTO.put("anexos", documento.getPdfTexto25() != null ? documento.getPdfTexto25() : "");
        keysValuesAsposeDocxDTO.put("ELABORA_SGRADO",
                documento.getPdfTexto13() != null ? documento.getPdfTexto13() : "");
        keysValuesAsposeDocxDTO.put("Elabora_Nom", documento.getPdfTexto14() != null ? documento.getPdfTexto14() : "");
        keysValuesAsposeDocxDTO.put("Elabora_cargo",
                documento.getPdfTexto15() != null ? documento.getPdfTexto15() : "");
        keysValuesAsposeDocxDTO.put("REVISO_SGRADO",
                documento.getPdfTexto21() != null ? documento.getPdfTexto21() : "");
        keysValuesAsposeDocxDTO.put("Reviso_Nom", documento.getPdfTexto22() != null ? documento.getPdfTexto22() : "");
        keysValuesAsposeDocxDTO.put("Reviso_cargo", documento.getPdfTexto23() != null ? documento.getPdfTexto23() : "");
        keysValuesAsposeDocxDTO.put("VB_SGRADO", documento.getPdfTexto32() != null ? documento.getPdfTexto32() : "");
        keysValuesAsposeDocxDTO.put("Vb_Nom", documento.getPdfTexto33() != null ? documento.getPdfTexto33() : "");
        keysValuesAsposeDocxDTO.put("Vb_cargo", documento.getPdfTexto34() != null ? documento.getPdfTexto34() : "");
        keysValuesAsposeDocxDTO.put("Elabora_dep_dir",
                documento.getPdfTexto44() != null ? documento.getPdfTexto44() : "");
        keysValuesAsposeDocxDTO.put("linea_mando", documento.getPdfTexto35() != null ? documento.getPdfTexto35() : "");
        keysValuesAsposeDocxDTO.put("cod_trd", documento.getPdfTexto36() != null ? documento.getPdfTexto36() : "");
        keysValuesAsposeDocxDTO.put("FIRMA_DEP", documento.getPdfTexto37() != null ? documento.getPdfTexto37() : "");
        keysValuesAsposeDocxDTO.put("elabora_dep_spadre",
                documento.getPdfTexto2() != null ? documento.getPdfTexto2() : "");
        keysValuesAsposeDocxDTO.put("elabora_dep_spadre_s",
                documento.getPdfTexto38() != null ? documento.getPdfTexto38() : "");
        keysValuesAsposeDocxDTO.put("elabora_dep_sigla",
                documento.getPdfTexto40() != null ? documento.getPdfTexto40() : "");
        keysValuesAsposeDocxDTO.put("s_dep_destino",
                documento.getPdfTexto6_2() != null ? documento.getPdfTexto6_2() : "");
        keysValuesAsposeDocxDTO.put("nom_trd", documento.getPdfTexto42() != null ? documento.getPdfTexto42() : "");
        keysValuesAsposeDocxDTO.put("elabora_dep_spadre_s",
                documento.getPdfTexto43() != null ? documento.getPdfTexto43() : "");
        keysValuesAsposeDocxDTO.put("elabora_dep_spadre_dir",
                documento.getPdfTexto45() != null ? documento.getPdfTexto45() : "");
        keysValuesAsposeDocxDTO.put("depdestino_jefe_grado",
                documento.getPdfTexto46() != null ? documento.getPdfTexto46() : "");
        keysValuesAsposeDocxDTO.put("depdestino_jefe_sgrado",
                documento.getPdfTexto47() != null ? documento.getPdfTexto47() : "");
        keysValuesAsposeDocxDTO.put("dep_destino_jefe_nom",
                documento.getPdfTexto48() != null ? documento.getPdfTexto48() : "");
        keysValuesAsposeDocxDTO.put("dep_destino_dir",
                documento.getPdfTexto49() != null ? documento.getPdfTexto49() : "");
        keysValuesAsposeDocxDTO.put("dep_destino_jefe_cargo",
                documento.getPdfTexto56() != null ? documento.getPdfTexto56() : "");
        keysValuesAsposeDocxDTO.put("asunto", documento.getPdfTexto50() != null ? documento.getPdfTexto50() : "");

        keysValuesAsposeDocxDTO.put("destinatario_cargo",
                documento.getPdfTexto51() != null ? documento.getPdfTexto51() : "");
        keysValuesAsposeDocxDTO.put("destinatario_nom",
                documento.getPdfTexto52() != null ? documento.getPdfTexto52() : "");
        keysValuesAsposeDocxDTO.put("destinatario_dir",
                documento.getPdfTexto53() != null ? documento.getPdfTexto53() : "");
        keysValuesAsposeDocxDTO.put("hora_doc", documento.getPdfTexto54() != null ? documento.getPdfTexto54() : "");
        keysValuesAsposeDocxDTO.put("elabora_tel", documento.getPdfTexto55() != null ? documento.getPdfTexto55() : "");

        keysValuesAsposeDocxDTO.put("elabora_dep", documento.getPdfTexto58() != null ? documento.getPdfTexto58() : "");
        keysValuesAsposeDocxDTO.put("linea_mando2", documento.getPdfTexto57() != null ? documento.getPdfTexto57() : "");
        keysValuesAsposeDocxDTO.put("S_DEP_DESTINO_ADD",
                documento.getPdfTexto59() != null ? documento.getPdfTexto59() : "");
        keysValuesAsposeDocxDTO.put("firma_telefono",
                documento.getPdfTexto60() != null ? documento.getPdfTexto60() : "");
        keysValuesAsposeDocxDTO.put("firma_email", documento.getPdfTexto61() != null ? documento.getPdfTexto61() : "");

        keysValuesAsposeDocxDTO.put("VB_SGRADO_1", documento.getPdfTexto64() != null ? documento.getPdfTexto64() : "");
        keysValuesAsposeDocxDTO.put("Vb_Nom_1", documento.getPdfTexto65() != null ? documento.getPdfTexto65() : "");
        keysValuesAsposeDocxDTO.put("Vb_Cargo_1", documento.getPdfTexto66() != null ? documento.getPdfTexto66() : "");

        keysValuesAsposeDocxDTO.put("VB_SGRADO_2", documento.getPdfTexto67() != null ? documento.getPdfTexto67() : "");
        keysValuesAsposeDocxDTO.put("Vb_Nom_2", documento.getPdfTexto68() != null ? documento.getPdfTexto68() : "");
        keysValuesAsposeDocxDTO.put("Vb_Cargo_2", documento.getPdfTexto69() != null ? documento.getPdfTexto69() : "");

        keysValuesAsposeDocxDTO.put("VB_SGRADO_3", documento.getPdfTexto70() != null ? documento.getPdfTexto70() : "");
        keysValuesAsposeDocxDTO.put("Vb_Nom_3", documento.getPdfTexto71() != null ? documento.getPdfTexto71() : "");
        keysValuesAsposeDocxDTO.put("Vb_Cargo_3", documento.getPdfTexto72() != null ? documento.getPdfTexto72() : "");

        keysValuesAsposeDocxDTO.put("VB_SGRADO_4", documento.getPdfTexto73() != null ? documento.getPdfTexto73() : "");
        keysValuesAsposeDocxDTO.put("Vb_Nom_4", documento.getPdfTexto74() != null ? documento.getPdfTexto74() : "");
        keysValuesAsposeDocxDTO.put("Vb_Cargo_4", documento.getPdfTexto75() != null ? documento.getPdfTexto75() : "");

        keysValuesAsposeDocxDTO.put("VB_SGRADO_5", documento.getPdfTexto76() != null ? documento.getPdfTexto76() : "");
        keysValuesAsposeDocxDTO.put("Vb_Nom_5", documento.getPdfTexto77() != null ? documento.getPdfTexto77() : "");
        keysValuesAsposeDocxDTO.put("Vb_Cargo_5", documento.getPdfTexto78() != null ? documento.getPdfTexto78() : "");

        keysValuesAsposeDocxDTO.put("VB_SGRADO_6", documento.getPdfTexto79() != null ? documento.getPdfTexto79() : "");
        keysValuesAsposeDocxDTO.put("Vb_Nom_6", documento.getPdfTexto80() != null ? documento.getPdfTexto80() : "");
        keysValuesAsposeDocxDTO.put("Vb_Cargo_6", documento.getPdfTexto81() != null ? documento.getPdfTexto81() : "");

        /*
		 * 2017-02-06 jgarcia@controltechcg.com Issue #123: Nuevo campos
		 * PDF_TEXTO para WILDCARDS de generación de documento
		 * (DEPENDENCIA.CIUDAD).
         */
        keysValuesAsposeDocxDTO.put("DEPENDENCIA_CIUDAD_ELABORA",
                documento.getPdfTexto82() != null ? documento.getPdfTexto82() : "");
        keysValuesAsposeDocxDTO.put("DEPENDENCIA_CIUDAD_DESTINO",
                documento.getPdfTexto83() != null ? documento.getPdfTexto83() : "");

        if (documento.getPdfTexto30() != null && documento.getPdfTexto30().trim().length() > 0) {
            try {
                File file = new File(root + "/" + documento.getPdfTexto30().trim());
                if (file.exists() && file.isFile()) {
                    keysValuesAsposeDocxDTO.put("img_firma", FileUtils.readFileToByteArray(file));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
		 * 2017-09-29 edison.gonzalez@controltechcg.com Issue #129: Nuevo campos
		 * PDF_TEXTO para WILDCARDS de generación de documento
		 * (grado y restriccion de la difusion).
         */
        keysValuesAsposeDocxDTO.put("DESTINATARIO_GRADO",
                documento.getPdfTexto84() != null ? documento.getPdfTexto84() : "");

        keysValuesAsposeDocxDTO.put("EXTERNO_MARCA_AGUA", documento.getPdfTexto85() != null ? documento.getPdfTexto85().toUpperCase() : "");

        keysValuesAsposeDocxDTO.put("RESTRICCION_DIFUSION",
                documento.getPdfTexto86() != null ? documento.getPdfTexto86() : "");

        org.springframework.core.io.Resource resource = new ClassPathResource("Aspose.Words.lic");

        LICENSE.setLicense(resource.getInputStream());

        if (LICENSE.getIsLicensed()) {
            System.out.println("Aspose.Words.Java.lic is Set OK!");
        } else {
            System.out.println("ERRORR...Aspose.Words.Java.lic is FAILED!");
        }

        return keysValuesAsposeDocxDTO;
    }

    /**
     * Muestra la pantalla con los datos del documento. Si la instancia no tiene
     * definido un documento asociado entonces lo crea y lo enlaza a la
     * instancia. Igualmente enlaza la instancia al documento para poder sacar
     * rápidamente las bandejas.
     *
     * @param pin Identificador de la instancia
     * @param archivoHeader
     * @param model
     * @param principal
     * @param redirect
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String documento(@RequestParam("pin") String pin,
            @RequestParam(value = "archivoHeader", required = false) String archivoHeader, Model model,
            Principal principal, final RedirectAttributes redirect) {

        // Si el request trae información en headerView, se envía a la vista
        // para que se muestre otro header al que está por defecto
        model.addAttribute("archivoHeader", archivoHeader);

        Usuario usuarioLogueado = getUsuario(principal);
        model.addAttribute("usuariologueado", usuarioLogueado);

        // 2018-01-31 edison.gonzalez@controltechcg.com Issue #147 (SICDI-Controltech)
        List<Dependencia> listaDependencias = depsHierarchy();
        model.addAttribute("dependencias", listaDependencias);
        
        // 2018-05-04 edison.gonzalez@controltechcg.com Issue #157 (SICDI-Controltech)
        List<Trd> listaTrds = trdsHierarchy(usuarioLogueado);
        model.addAttribute("trds", listaTrds);
        /*
         * 2017-11-17 edison.gonzalez@controltechcg.com Issue #139: Verifica permisos
         * de acceso al documento.
         */
        //Verifica permisos de acceso al documento
        Boolean acceso = usuarioService.verificaAccesoDocumento(usuarioLogueado.getId(), pin);

        if (!acceso) {
            return "security-denied";
        }

        // Obtiene la instancia de proceso
        Instancia i = procesoService.instancia(pin);

        // Obtiene el identificador de documento
        String docId = i.getVariable(Documento.DOC_ID);

        // Si no se tiene el identificador de documento entonces crea el
        // documento y lo enlaza bidireccionalmente a la instancia. De lo
        // contrario carga el documento desde la base de datos.
        Documento doc = null;
        if (StringUtils.isBlank(docId)) {
            doc = Documento.create();
            doc.setInstancia(i);
            doc.setElabora(getUsuario(principal));
            doc.setEstadoTemporal("TEMPORAL");
            i.setVariable(Documento.DOC_ID, doc.getId());
            instanciaRepository.save(i);

            /*
			 * 2017-02-08 jgarcia@controltechcg.com Issue #94: Se corrige en los
			 * puntos donde se hacen persistentes los documentos, para que
			 * siempre se registre el usuario de la última accíón.
             */
            doc.setUsuarioUltimaAccion(usuarioLogueado);
            documentRepository.saveAndFlush(doc);
        } else {
            doc = documentRepository.getOne(docId);
            doc.setEstadoTemporal(null);
        }

        //2018-02-27 edison.gonzalez@controltechcg.com Issue #151 (SICDI-Controltech)
        model.addAttribute("cambiarIdCargoElabora", false);

        if (Objects.equals(i.getProceso().getId(), Proceso.ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA)
                || Objects.equals(i.getProceso().getId(), Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS)) {
            if (i.getVariables().size() <= 5 && Objects.equals(usuarioLogueado.getId(), doc.getElabora().getId())) {
                model.addAttribute("cambiarIdCargoElabora", true);
            }

            if (DocumentoMode.NAME_EN_CONSTRUCCION.equals(i.getVariable(Documento.DOC_MODE)) && Objects.equals(getUsuario(principal).getId(), doc.getElabora().getId())) {
                model.addAttribute("cambiarIdCargoElabora", true);
            }
        }

        if (Objects.equals(i.getProceso().getId(), Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS)) {
            if (i.getVariables().size() <= 4 && Objects.equals(usuarioLogueado.getId(), doc.getElabora().getId())) {
                model.addAttribute("cambiarIdCargoElabora", true);
            }
            if (DocumentoMode.NAME_REGISTRO.equals(i.getVariable(Documento.DOC_MODE)) && Objects.equals(getUsuario(principal).getId(), doc.getElabora().getId())) {
                model.addAttribute("cambiarIdCargoElabora", true);
            }
        }

        model.addAttribute("cambiarIdCargoFirma", false);
        if (i.getEstado() != null && Objects.equals(Estado.PENDIENTE, i.getEstado().getId())) {
            if (i.getAsignado() != null && Objects.equals(i.getAsignado().getId(), usuarioLogueado.getId())) {
                model.addAttribute("cambiarIdCargoFirma", true);
            }
        }

        // Cargamos los vistos buenos del documentos
        List<Object[]> vistosBuenosValores = documentRepository.findVistosBuenosDocumentos(doc.getId());
        for (Object[] obVistoBueno : vistosBuenosValores) {
            doc.getVistosBuenos().add(new UsuarioVistoBuenoDTO((Date) obVistoBueno[1], (String) obVistoBueno[0],
                    (String) obVistoBueno[3], (String) obVistoBueno[2]));
        }

        // Si el usuario no es el que se encuentra asignado a la instancia
        // aplica el nivel de acceso
        if (usuarioLogueado.getId() != i.getAsignado().getId()) {
            // Si el usuario no tiene el nivel de acceso necesario para ver el
            // documento entonces lo redirecciona a la página de
            // /acceso-denegado
            Integer nivelDocumento = (doc.getClasificacion() == null ? 0 : doc.getClasificacion().getOrden());
            Integer nivelUsuario = (usuarioLogueado.getClasificacion() == null ? 0
                    : usuarioLogueado.getClasificacion().getOrden());
            if (nivelDocumento > nivelUsuario) {
                return "redirect:/documento/acceso-denegado";
            }
        }

        // Variable que muestra o no un <textarea> utilizado para mostrar como
        // ejemplo el texto de las plantillas parametrizadas
        if (doc.getInstancia().getProceso().getId()
                .equals(Proceso.ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA)
                || doc.getInstancia().getProceso().getId()
                        .equals(Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS)) {
            model.addAttribute("mostrar_text_html", true);
        }

        // Determina el modo de visualización y edición del documento
        DocumentoMode mode = DocumentoMode.getByName(i.getVariable(Documento.DOC_MODE));
        doc.setMode(mode);

        // Pone los atributos en el modelo necesarios para armar la vista
        model.addAttribute("documento", doc);

        /*
		 * 2017-02-22 jgarcia@controltechcg.com Issue #141: En caso que el
		 * documento ya haya sido firmado, se obtiene la fecha de la firma y se
		 * sube al modelo para que el dato pueda ser consumido por la pantalla.
		 * 2017-02-22 jgarcia@controltechcg.com Issue #141: Corrección en la
		 * sentencia SQL que obtiene la fecha de la firma.
         */
        if (doc.getFirma() != null) {
            Date cuandoFirma = documentRepository.findCuandoFirma(doc.getInstancia().getId());
            model.addAttribute("doc_cuando_firma", cuandoFirma);
        } else {
            model.addAttribute("doc_cuando_firma", null);
        }

        if (StringUtils.isNotBlank(doc.getRelacionado())) {
            model.addAttribute("relacionado", documentRepository.getOne(doc.getRelacionado()));
        }

        expedientes(model, principal);
        tipologias(doc.getTrd(), model);
        plantillas(model);

        // 2017-04-26 jgarcia@controltechcg.com Issue #58 (SICDI-Controltech)
        addConsultaTemplateModelAttributes(model, usuarioLogueado, doc);
        return "documento";
    }

    /**
     * Adiciona las variables necesarias para el cálculo de las acciones
     * asociadas a la bandeja de consulta de los documentos en sesión.
     *
     * @param model Modelo de presentación.
     * @param usuarioLogueado Usuario en sesión.
     * @param documento Documento.
     */
    /*
	 * 2017-04-26 jgarcia@controltechcg.com Issue #58 (SICDI-Controltech):
	 * Corrección separando la asignación de atributos asociados a los procesos
	 * de bandeja de consulta en una funcionalidad individual, que pueda ser
	 * invocada en los diferentes procesos del documento.
     */
    private void addConsultaTemplateModelAttributes(Model model, Usuario usuarioLogueado, Documento documento) {
        /*
		 * 2017-04-20 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech):
		 * Se busca registro del documento en consulta para ingresarlo como
		 * referencia al modelo de presentación. Establecimiento de variables de
		 * modelo para indicar si el usuario puede enviar el documento a la
		 * bandeja de entrada o extraerlo de la misma.
         */
        DocumentoEnConsulta documentoEnConsulta = documentoEnConsultaService.buscarActivo(documento);
        model.addAttribute("documentoEnConsulta", documentoEnConsulta != null);

        boolean puedeEnviarABandejaConsulta = documentoEnConsultaService.validarEnviarABandejaConsulta(usuarioLogueado,
                documento);
        model.addAttribute("puedeEnviarABandejaConsulta", puedeEnviarABandejaConsulta);

        boolean puedeExtraerDeBandejaConsulta = documentoEnConsultaService
                .validarExtraerDeBandejaConsulta(usuarioLogueado, documento);
        model.addAttribute("puedeExtraerDeBandejaConsulta", puedeExtraerDeBandejaConsulta);
    }

    /**
     *
     * @param idDocumentoDependenciaAdicional
     * @param principal
     */
    @RequestMapping(value = "/eliminarDependenciAdicionalDocumento/{idDocumentoDependenciaAdicional}", method = RequestMethod.GET)
    public void eliminarDependenciaAdiconal(
            @PathVariable("idDocumentoDependenciaAdicional") String idDocumentoDependenciaAdicional,
            Principal principal) {

        documentoDependenciaAdicionalRepository.delete(idDocumentoDependenciaAdicional);

    }

    /**
     *
     * @param idDependencia
     * @param idDocumento
     * @param principal
     * @deprecated 2018-04-16 jgarcia@controltechcg.com Issue #156
     * (SICDI-Controltech) feature-156 Se reemplaza por la entidad
     * {@link DependenciaCopiaMultidestino} y el método
     * {@link DependenciaCopiaMultidestinoController#crearRegistroMultidestino(java.lang.String, java.lang.Integer, java.security.Principal)}.
     */
    @RequestMapping(value = "/dependenciAdicionalDocumento/{idDependencia}/{idDocumento}", method = RequestMethod.GET)
    @Deprecated
    public void cargarNuevaDependenciaAdiconal(@PathVariable("idDependencia") Integer idDependencia,
            @PathVariable("idDocumento") String idDocumento, Principal principal) {

        // CONSULTAMOS EL DOCUMENTO ACTUAL
        Documento documentoActual = documentRepository.findOne(idDocumento);

        // PRIMERO VALIDAMOS QUE LA DEPENDENCIA A ADICIONAR NO EXISTA
        if (documentoActual.getDependenciaDestino() != null && documentoActual.getDependenciaDestino().getId() != null
                && !documentoActual.getDependenciaDestino().getId().equals(idDependencia)) {

            // TAMBIEN SE VERIFICA QUE NO EXISTA EN LA LISTA DE DEPENDENCIAS
            // ADICIONALES
            List<DocumentoDependenciaDestino> lista = documentoDependenciaAdicionalRepository
                    .findByDocumento(idDocumento);
            boolean encontrado = false;

            for (DocumentoDependenciaDestino documentoDependenciaDestino : lista) {
                if (documentoDependenciaDestino.getDependencia().getId().equals(idDependencia)) {
                    encontrado = true;
                    break;
                }
            }

            // SI NO EXISTE
            if (!encontrado) {

                // LO ADICIONAMOS
                DocumentoDependenciaDestino entity = DocumentoDependenciaDestino.create();
                entity.setCuandoInserta(new Date());
                Dependencia dependencia = new Dependencia();
                dependencia.setId(idDependencia);
                entity.setDependencia(dependencia);
                entity.setElabora(new Usuario(getUsuarioId(principal)));
                Documento documento = new Documento();
                documento.setId(idDocumento);
                entity.setDocumento(documento);

                documentoDependenciaAdicionalRepository.save(entity);
            }

        }

    }

    /**
     *
     * @param pin
     * @param tid
     * @param model
     * @param req
     * @param principal
     * @param resp
     */
    @RequestMapping(value = "/digitalizar-descargar-jnlp", method = RequestMethod.GET)
    public void descargarJnlp(@RequestParam("pin") String pin, @RequestParam("tid") Integer tid, Model model,
            HttpServletRequest req, Principal principal, HttpServletResponse resp) {

        ServletOutputStream os = null;
        ByteArrayInputStream is = null;
        try {

            resp.setContentLength((int) STRING_JNLP.getBytes("UTF-8").length);
            resp.setContentType("UTF-8");

            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", "SIGDI-Scanner.jnlp");
            resp.setHeader(headerKey, headerValue);

            // Write response
            os = resp.getOutputStream();
            is = new ByteArrayInputStream(STRING_JNLP.getBytes("UTF-8"));
            IOUtils.copy(is, os);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param id
     * @param resp
     */
    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public void download(@PathVariable("id") String id, HttpServletResponse resp) {

        ServletOutputStream os = null;
        ByteArrayInputStream is = null;
        try {
            OFSEntry entry = ofs.read(id);
            byte[] content = entry.getContent();
            resp.setContentLength((int) content.length);
            resp.setContentType(entry.getContentType());

            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", "Formato.docx");
            resp.setHeader(headerKey, headerValue);

            // Write response
            os = resp.getOutputStream();
            is = new ByteArrayInputStream(content);
            IOUtils.copy(is, os);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda las modificaciones a un documento dependiendo del modo de edición
     *
     * @param pin Identificador de la instancia de proceso
     * @param file
     * @param doc La información del documento que proviene del formulario
     * @param docBind Enlace del framework con la entidad
     * @param model Modelo para la vista
     * @param principal Usuario
     * @param redirect Información para la redirección
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String documento(@RequestParam("pin") String pin,
            @RequestParam(value = "archivoContenido", required = false) MultipartFile file, @Valid Documento doc,
            BindingResult docBind, final RedirectAttributes redirect, Model model, Principal principal) {

        // 2018-01-31 edison.gonzalez@controltechcg.com Issue #147 (SICDI-Controltech)
        List<Dependencia> listaDependencias = depsHierarchy();
        model.addAttribute("dependencias", listaDependencias);
        
        // Obtiene la instancia de proceso
        Instancia i = procesoService.instancia(pin);

        // Obtiene el identificador del documento de la instancia
        String docId = i.getVariable(Documento.DOC_ID);

        // Establece el modo de edición según la información registrada en la
        // instancia
        String modeName = i.getVariable(Documento.DOC_MODE);

        DocumentoMode mode = DocumentoMode.getByName(modeName);

        //2018-02-27 edison.gonzalez@controltechcg.com Issue #151 (SICDI-Controltech)
        Usuario logueado = getUsuario(principal);
        
        // 2018-05-04 edison.gonzalez@controltechcg.com Issue #157 (SICDI-Controltech)
        List<Trd> listaTrds = trdsHierarchy(logueado);
        model.addAttribute("trds", listaTrds);
        
        Integer elaboraDocumento = null;
        if (doc.getElabora() != null) {
            elaboraDocumento = doc.getElabora().getId();
        }

        model.addAttribute("cambiarIdCargoElabora", false);

        if (Objects.equals(i.getProceso().getId(), Proceso.ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA)
                || Objects.equals(i.getProceso().getId(), Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS)) {
            if (i.getVariables().size() <= 5) {
                model.addAttribute("cambiarIdCargoElabora", true);
            }

            if (DocumentoMode.NAME_EN_CONSTRUCCION.equals(modeName) && Objects.equals(logueado.getId(), elaboraDocumento)) {
                model.addAttribute("cambiarIdCargoElabora", true);
            }
        }

        if (Objects.equals(i.getProceso().getId(), Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS)) {
            if (i.getVariables().size() <= 4 && Objects.equals(logueado.getId(), elaboraDocumento)) {
                model.addAttribute("cambiarIdCargoElabora", true);
            }

            if (DocumentoMode.NAME_REGISTRO.equals(i.getVariable(Documento.DOC_MODE)) && Objects.equals(logueado.getId(), elaboraDocumento)) {
                model.addAttribute("cambiarIdCargoElabora", true);
            }
        }

        model.addAttribute("cambiarIdCargoFirma", false);
        if (i.getEstado() != null && Objects.equals(Estado.PENDIENTE, i.getEstado().getId())) {
            Integer firmaDocumento = null;
            if (i.getAsignado() != null) {
                firmaDocumento = i.getAsignado().getId();
            }
            if (Objects.equals(firmaDocumento, logueado.getId())) {
                model.addAttribute("cambiarIdCargoFirma", true);
            }
        }

        // Obtiene el documento previamente almacenado en la base de datos o
        // crea uno nuevo (sin almacenarlo) para que sirva como objeto anterior
        Documento old = null;
        if (StringUtils.isBlank(docId)) {
            old = new Documento();
        } else {
            old = documentRepository.getOne(docId);
        }

        if (fileSaveRequestGet) {

            if ((file == null || file.getOriginalFilename().isEmpty())) {
                if (old == null || old.getDocx4jDocumento().trim().length() == 0) {
                    doc.setDocx4jDocumentoVacio(true);
                }
            }

            if (file != null && (old == null || old.getDocx4jDocumento().trim().length() == 0)) {
                if (file.getContentType() == null || !file.getContentType()
                        .equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                    doc.setDocx4jDocumentoFormatoInvalido(true);
                }
            }
        }

        Usuario usuarioLogueado = getUsuario(principal);
        model.addAttribute("usuariologueado", usuarioLogueado);

        // Verifica la información que proviene del
        // formulario, basado en el modo de edición
        mode.transferirNoEditables(old, doc);
        mode.defaults(doc, usuarioLogueado, dependenciaRepository, trdRepository);

        /**
         * 2018-04-11 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech)
         * feature-156: En caso que antes de realizar el proceso de validación,
         * el documento no cuente con su lista de dependencias copia
         * multidestino, se asigna la información correspondiente al subconjunto
         * activo, con el fin de poder pasar esta información a las
         * funcionalidades de validación.
         */
        if (doc.getDependenciaCopiaMultidestinos() == null || doc.getDependenciaCopiaMultidestinos().isEmpty()) {
            doc.setDependenciaCopiaMultidestinos(multidestinoService.listarActivos(doc));
        }

        mode.validate(doc, i, docBind);

        /*
		 * 2017-02-02 jgarcia@controltechcg.com Issue #129: Variable que se
		 * encuentra en el documento que en tiempo de validación que permite
		 * establecer si tiene errores para manejo en el formulario.
         */
        doc.setWithErrors(docBind.hasErrors());

        // 2017-04-26 jgarcia@controltechcg.com Issue #58 (SICDI-Controltech)
        addConsultaTemplateModelAttributes(model, usuarioLogueado, doc);

        // Si se encuentran errores en el formulario entonces se debe construir
        // la vista con los datos del formulario, rellenando los datos que no
        // son editables con la información del documento almacenado
        if (docBind.hasErrors()) {

            System.out.println(docBind);
            doc.setMode(mode);
            model.addAttribute("documento", doc);
            if (StringUtils.isNotBlank(doc.getRelacionado())) {
                model.addAttribute("relacionado", documentRepository.getOne(doc.getRelacionado()));
            }

            plantillas(model);
            expedientes(model, principal);
            tipologias(doc.getTrd(), model);

            model.addAttribute(AppConstants.FLASH_ERROR, "Existen errores en el formulario");

            return "documento";
        }

        String fileId = null;
        if (fileSaveRequestGet) {
            try {
                if (file != null) {
                    // 2017-02-17 jgarcia@controltechcg.com Issue #140: Se
                    // extienden las validaciones en el punto de almacenamiento
                    // para evitar que en casos (en tiempo de edición) donde no
                    // se ingrese una nueva plantilla, se sobreescriba la
                    // anterior relacionada al documento.
                    if (file.getSize() > 0 && !file.getContentType().equals("application/octet-stream")) {
                        fileId = ofs.saveAsIs(file.getBytes(), file.getContentType());
                        model.addAttribute(AppConstants.FLASH_SUCCESS, "El archivo a word fue guardado con éxito");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                redirect.addFlashAttribute(AppConstants.FLASH_ERROR, "Ocurrió un error inesperado: " + e.getMessage());
                model.addAttribute(AppConstants.FLASH_ERROR, "Ocurrió un error inesperado: " + e.getMessage());
            }
        }

        // Si no se encuentran errores en el formulario entonces se transfieren
        // las propiedades editables al objeto que se extrajo de la base de
        // datos y luego se guarda para que se realicen los cambios.
        try {

            mode.transferirEditables(doc, old);

            if (fileSaveRequestGet && fileId != null) {
                doc.setDocx4jDocumento(fileId);
                old.setDocx4jDocumento(fileId);
            }

            // Guarda el archivo de contenido si se envía uno nuevo
            if (file != null && file.isEmpty() == false) {
                fileId = ofs.saveAsIs(file.getBytes(), file.getContentType());
                old.setContentFile(fileId);
            }

            /*
			 * 2017-02-08 jgarcia@controltechcg.com Issue #94: Se corrige en los
			 * puntos donde se hacen persistentes los documentos, para que
			 * siempre se registre el usuario de la última accíón.
             */
            old.setUsuarioUltimaAccion(usuarioLogueado);
            documentRepository.saveAndFlush(old);

            // Aplica el generador de variables
            documentoGeneradorVariables.generar(i);

        } catch (Exception e) {
            LOG.error("Guardando el documento", e);
            mode.transferirNoEditables(old, doc);
            doc.setMode(mode);
            model.addAttribute("documento", doc);
            if (StringUtils.isNotBlank(doc.getRelacionado())) {
                model.addAttribute("relacionado", documentRepository.getOne(doc.getRelacionado()));
            }

            plantillas(model);
            expedientes(model, principal);
            tipologias(doc.getTrd(), model);
            model.addAttribute(AppConstants.FLASH_ERROR, "Ocurrió un error inesperado: " + e.getMessage());
            return "documento";
        }

        redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Los datos han sido guardados correctamente");

        return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);

    }

    /**
     * Genera el sticker del documento
     *
     * @param pin
     * @param model
     * @param principal
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/sticker", method = RequestMethod.GET)
    public String sticker(@RequestParam("pin") String pin, Model model, Principal principal,
            RedirectAttributes redirect) {

        // Obtiene la instancia
        Instancia i = procesoService.instancia(pin);

        // Obtiene el documento registrado en las variables de la instancia
        String docId = i.getVariable(Documento.DOC_ID);
        Documento doc = documentRepository.getOne(docId);

        // Si el documento aún no tiene número de radicado entonces le asigna
        // uno nuevo
        if (StringUtils.isBlank(doc.getRadicado())) {
            /*
                * 2017-11-14 edison.gonzalez@controltechcg.com Issue #138: Se llama
                * al servicio encargado de retornar el numero de radicado, segun el tipo
                * de proceso.
             */

            Radicacion radicacion = radicacionRepository.findByProceso(doc.getInstancia().getProceso());
            doc.setRadicado(radicadoService.retornaNumeroRadicado(getSuperDependencia(doc.getDependenciaDestino()).getId(), radicacion.getRadId()));
        }

        try {

            // Si el documento aún no cuenta con sticker entonces genera uno
            // nuevo y le asigna el ID de este al documento
            if (StringUtils.isBlank(doc.getSticker())) {
                Map<String, Object> params = new HashMap<String, Object>();
                // params.put("P_DOCUMENTO", doc);
                System.err.println("Radicado numero= " + doc.getRadicado());
                params.put("radicado", doc.getRadicado() == null ? "" : doc.getRadicado());
                params.put("asunto", doc.getAsunto() == null ? "" : doc.getAsunto());
                params.put("cuando", sdf.format(doc.getCuando()));
                params.put("elabora", doc.getElabora().getNombre());
                params.put("destinatario", doc.getDependenciaDestino() != null ? doc.getDependenciaDestino().getSigla()
                        : doc.getDestinatario());
                params.put("remitente", doc.getRemitente() == null ? "" : doc.getRemitente());
                params.put("imagesRoot", imagesRoot);
                List<String> listaUnElemento = new ArrayList<>(1);
                listaUnElemento.add("");
                String stickerId = jasperService.savePdf("sticker", params, listaUnElemento, null);
                doc.setSticker(stickerId);
            }

            /*
			 * 2017-02-08 jgarcia@controltechcg.com Issue #94: Se corrige en los
			 * puntos donde se hacen persistentes los documentos, para que
			 * siempre se registre el usuario de la última accíón.
             */
            Usuario uActualLogin = getUsuario(principal);
            doc.setUsuarioUltimaAccion(uActualLogin);

            // Guarda los cambios realizados al documento
            documentRepository.saveAndFlush(doc);

            // Cambia el modo de edición del documento
            i.setVariable(Documento.DOC_MODE, "con_sticker");

            // Aplica el generador de variables
            documentoGeneradorVariables.generar(i);

            // Intenta realizar la siguiente transición que aplique
            i.forward();

            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "El sticker se ha generado correctamente");

        } catch (Exception e) {
            redirect.addFlashAttribute(AppConstants.FLASH_ERROR, e.getMessage());
            e.printStackTrace();
        }

        return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);

    }

    /**
     *
     * @param pin
     * @return
     */
    @RequestMapping(value = "/validarcarguedigitacion", method = RequestMethod.GET)
    @ResponseBody
    public String validarCargarDigitacion(@RequestParam("pin") String pin) {

        // Obtiene la instancia de proceso
        Instancia i = procesoService.instancia(pin);

        if (i.getEstado().getId().equals(Estado.DIGITALIZACION)) {
            return "DIGITALIZACION";
        }

        return "NODIGITALIZACION";
    }

    /**
     * Muestra las diferentes opciones que tiene el usuario para digitalizar el
     * documento
     *
     * @param pin Indentificador de la instancia
     * @param tid
     * @param model
     * @param req
     * @param principal
     * @return
     */
    @RequestMapping(value = "/digitalizar-principal", method = RequestMethod.GET)
    public String digitalizarPrincipal(@RequestParam("pin") String pin, @RequestParam("tid") Integer tid, Model model,
            HttpServletRequest req, Principal principal) {

        // Obtiene la instancia de proceso
        Instancia i = procesoService.instancia(pin);

        if (i.getEstado().getId().equals(Estado.DIGITALIZACION)) {
            return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
        }

        // Obtiene el documento asociado a la instancia
        String docId = i.getVariable(Documento.DOC_ID);
        Documento doc = documentRepository.getOne(docId);

        // VALIDAMOS SI ESTE DOCUMENTO TIENE UN CODIGO GENERADO VALIDO
        if (doc.getCodigoValidaScanner() == null || doc.getCodigoValidaScanner().trim().length() == 0
                || doc.getEstadoCodigoValidaScanner() == null || doc.getEstadoCodigoValidaScanner() == 0) {

            // REALIZAMOS EL PROCESO MAXIMO 10 VECES, HASTA QUE SE GENERE UN
            // CODIGO VALIDO
            int veces = 10;
            while (veces >= 1) {

                veces--;

                // GENERAMOS UN NUEVO CODIGO
                Random random = new Random();
                StringBuilder sb = new StringBuilder();
                for (int indice = 1; indice <= 6; indice++) {
                    sb.append(random.nextInt(9));
                }

                // VERIFICAMOS QUE EL CODIGO NO EXISTE ACTIVO
                Integer cantidad = documentRepository.getCantidadCodigoScanerValido(sb.toString());
                if (cantidad != null && cantidad > 0) {
                    System.out.println("SE GENERÓ UN CODIGO INVÁLIDO PARA SCANNER, REINTENTENADO GENERAR NUEVO CODIGO");
                    continue;
                }

                doc.setEstadoCodigoValidaScanner(1);
                doc.setCodigoValidaScanner(sb.toString());
                doc.setFechaGeneracionCodigoScanner(new Date());
                doc.setUsuarioValidaCodigoScanner(getUsuario(principal));

                // guardamos
                documentRepository.save(doc);

                // salimos del while
                break;
            }

            // ACTUALIZAMOS LA INFORMACION DEL DOCUMENTO CON LO ULTIMO EN BASE
            // DE DATOS
            doc = documentRepository.getOne(doc.getId());
        }

        // Pone el documento en el modelo
        model.addAttribute("documento", doc);

        // Detecta si se trata de un windows para evitar el uso de scanner en
        // caso que no se trate de un sistema operativo Windows. Por el momento
        // el sistema sólo es compatible con TWAIN_32 y no con SANE.
        String agent = req.getHeader("User-Agent");
        if (agent.toLowerCase().contains("windows")) {
            model.addAttribute("windowsOS", true);
        }

        model.addAttribute("tid", tid);

        return "documento-digitalizar-principal";
    }

    /**
     * Muestra la pantalla que contiene el Applet para obtener imágenes desde
     * TWAIN_32. Cada imagen obtenida se envía al servidor de inmediato y queda
     * en stage de OFS
     *
     * @param pin Indentificador de la instancia de proceso
     * @param tid
     * @param model
     * @param req
     * @param principal
     * @return
     */
    @RequestMapping(value = "/digitalizar-principal-scanner", method = RequestMethod.GET)
    public String digitalizarPrincipalScanner(@RequestParam("pin") String pin, @RequestParam("tid") Integer tid,
            Model model, HttpServletRequest req, Principal principal) {

        // Obtiene la instancia de proceso
        Instancia i = procesoService.instancia(pin);

        // Obtiene el documento asociado a la instancia de proceso
        String docId = i.getVariable(Documento.DOC_ID);
        Documento doc = documentRepository.getOne(docId);

        // Obtiene el stage o crea uno nuevo
        OFSStage stage = ofs.stage(doc.getId(), getUsuarioId(principal));

        // Obtiene el encabezado Cookie para pasarlo al Applet
        String cookieHeader = req.getHeader("Cookie");

        // Construye el modelo necesario para la vista
        model.addAttribute("documento", doc);
        model.addAttribute("baseUrl",
                String.format("%s://%s:%s", req.getScheme(), req.getServerName(), req.getServerPort()));
        model.addAttribute("stage", stage);
        model.addAttribute("cookie", cookieHeader);

        // Detecta si se trata de un windows
        String agent = req.getHeader("User-Agent");
        if (agent.toLowerCase().contains("windows")) {
            model.addAttribute("windowsOS", true);
        }
        model.addAttribute("tid", tid);

        return "documento-digitalizar-principal-scanner";
    }

    /**
     * Termina el proceso de digitalización por stage de OFS. Debe aplicar el
     * proceso de OCR a cada una de las imágenes, guardar el texto resultante en
     * la base de datos en un campo de búsqueda free text, luego juntar todas
     * las imágenes en un sólo PDF y guardarlos en el OFS, borrar todo lo
     * relacionado al stage. El archivo PDF debe quedar adjunto al documento.
     *
     * @param pin Identificador de la instancia de proceso
     * @param tid
     * @param model
     * @param principal
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/attach", method = RequestMethod.GET)
    public String attachStage(@RequestParam("pin") String pin, @RequestParam("tid") Integer tid, Model model,
            Principal principal, RedirectAttributes redirect) {

        // Obtiene la instancia de proceso
        Instancia i = procesoService.instancia(pin);

        // Obtiene el documento
        String docId = i.getVariable(Documento.DOC_ID);
        Documento doc = documentRepository.getOne(docId);

        // Genera el PDF que unifica todas las imágenes del stage y adjunta el
        // archivo resultante como un adjunto al documento.
        // TODO: Pasar las imágenes por OCR y guardar el texto
        String pdfId = null;
        try {
            pdfId = ofs.pdf(doc.getId(), getUsuarioId(principal));

            Adjunto adjunto = new Adjunto();
            adjunto.setContenido(pdfId);
            adjunto.setDocumento(doc);
            adjunto.setId(GeneralUtils.newId());
            adjunto.setOriginal(pdfId);
            adjunto.setTipologia(Tipologia.DOCUMENTO_LLEGADO);
            adjuntoRepository.save(adjunto);

            // Aplica el generador de variables
            documentoGeneradorVariables.generar(i);

            // Cambia el modo de visualización
            i.setVariable(Documento.DOC_MODE, DocumentoMode.NAME_DIGITALIZANDO);

            // Intenta avanzar en el proceso
            i.forward();

        } catch (IOException e) {
            e.printStackTrace();
            redirect.addFlashAttribute(AppConstants.FLASH_ERROR, "Ocurrió un error inesperado: " + e.getMessage());
        }

        model.addAttribute("tid", tid);
        return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
    }

    /**
     * Realiza la digitalización mediente la unidad compartida de red
     *
     * @param pin Identificador de la instancia de proceso
     * @param tid
     * @param file El archivo seleccionado
     * @param model
     * @param req
     * @param principal
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/digitalizar-principal-net", method = RequestMethod.GET)
    public String digitalizarPrincipalNet(@RequestParam("pin") String pin, @RequestParam("tid") Integer tid,
            @RequestParam(value = "file", required = false) String file, Model model, HttpServletRequest req,
            Principal principal, RedirectAttributes redirect) {

        // Obtiene la instancia de proceso
        Instancia i = procesoService.instancia(pin);

        // Obtiene el documento asociado a la instancia de proceso
        String docId = i.getVariable(Documento.DOC_ID);
        Documento doc = documentRepository.getOne(docId);

        // Deja el documento en el modelo para crear la vista
        model.addAttribute("documento", doc);

        // Si no se ha seleccionado un archivo de los existentes en la unidad
        // entonces se prepara para crear la vista listando los archivos
        // existentes. De lo contrario guarda el archivo en le OFS y lo pone
        // como adjunto al documento.
        if (StringUtils.isBlank(file)) {

            // Detecta si se trata de un windows
            String agent = req.getHeader("User-Agent");
            if (agent.toLowerCase().contains("windows")) {
                model.addAttribute("windowsOS", true);
            }

            // Detecta si la unidad virtual está montada en el mismo computador
            // del que proviene el request
            String ip = req.getRemoteAddr();
            if (driveService.checkConnection(ip, principal.getName())) {
                model.addAttribute("files", driveService.files(principal.getName()));
            } else {
                model.addAttribute("nodrive", true);
            }

            return "documento-digitalizar-principal-net";

        } else {

            String fileId = null;
            try {
                byte[] bytes = driveService.read(file, principal.getName());
                fileId = ofs.save(bytes, "application/pdf");
                driveService.delete(file, principal.getName());

                Adjunto adjunto = new Adjunto();
                adjunto.setContenido(fileId);
                adjunto.setDocumento(doc);
                adjunto.setId(GeneralUtils.newId());
                adjunto.setOriginal(file);
                adjunto.setTipologia(Tipologia.DOCUMENTO_LLEGADO);
                adjuntoRepository.save(adjunto);

                // Aplica el generador de variables
                documentoGeneradorVariables.generar(i);

                // Cambia el modo de visualización
                i.setVariable(Documento.DOC_MODE, DocumentoMode.NAME_DIGITALIZANDO);

                // Intenta avanzar en el proceso
                i.forward();

            } catch (IOException e) {
                e.printStackTrace();
                redirect.addFlashAttribute(AppConstants.FLASH_ERROR, "Ocurrió un error inesperado: " + e.getMessage());
            }

            model.addAttribute("tid", tid);

            return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);

        }
    }

    /**
     * Muestra la pantalla de digitalización mediante la subida de archivos por
     * HTTP
     *
     * @param pin Identificador de la instancia de proceso
     * @param tid
     * @param model
     * @param req
     * @param principal
     * @return
     */
    @RequestMapping(value = "/digitalizar-principal-file", method = RequestMethod.GET)
    public String digitalizarPrincipalFile(@RequestParam("pin") String pin, @RequestParam("tid") Integer tid,
            Model model, HttpServletRequest req, Principal principal) {

        // Obtiene la instancia de proceso
        Instancia i = procesoService.instancia(pin);

        // Obtiene el documento asociado a la instancia de proceso
        String docId = i.getVariable(Documento.DOC_ID);
        Documento doc = documentRepository.getOne(docId);

        // Deja el documento en el modelo para crear la vista
        model.addAttribute("documento", doc);
        model.addAttribute("tid", tid);

        return "documento-digitalizar-principal-file";
    }

    /**
     * Realiza la digitalización mediante la recepción de archivos por HTTP
     *
     * @param docId Identificador del documento
     * @param tid
     * @param archivo Archivo recibido
     * @param model
     * @param req
     * @param principal
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/digitalizar-principal-file", method = RequestMethod.POST)
    public String digitalizarPrincipalFile(@RequestParam("doc") String docId, @RequestParam("tid") Integer tid,
            @RequestParam("archivo") MultipartFile archivo, Model model, HttpServletRequest req, Principal principal,
            RedirectAttributes redirect) {
        // Obtiene el documento asociado a la instancia de proceso
        Documento doc = documentRepository.getOne(docId);

        /*
		 * 2017-02-15 jgarcia@controltechcg.com Issue #143: Se realiza
		 * validación sobre el archivo seleccionado por el usuario en el proceso
		 * de carga. Se valida que el archivo no esté vacío, y que el content
		 * type corresponda a "application/pdf".
         */
        String errorMessage = null;

        if (archivo.getSize() == 0) {
            errorMessage = "Debe seleccionar un archivo para subir. El formato del archivo debe ser PDF.";
        } else if (!archivo.getContentType().equals("application/pdf")) {
            errorMessage = "Únicamente se permite subir archivos con formato PDF.";
        }

        if (errorMessage != null) {
            model.addAttribute("documento", doc);
            model.addAttribute(AppConstants.FLASH_ERROR, errorMessage);
            model.addAttribute("tid", tid);
            return "documento-digitalizar-principal-file";
        }

        Instancia i = procesoService.instancia(doc.getInstancia().getId());

        String fileId = null;
        try {
            fileId = ofs.save(archivo.getBytes(), archivo.getContentType());

            Adjunto adjunto = new Adjunto();
            adjunto.setContenido(fileId);
            adjunto.setDocumento(doc);
            adjunto.setId(GeneralUtils.newId());
            adjunto.setOriginal(archivo.getOriginalFilename());
            adjunto.setTipologia(Tipologia.DOCUMENTO_LLEGADO);
            adjuntoRepository.save(adjunto);

            // Aplica el generador de variables
            documentoGeneradorVariables.generar(i);

            // Cambia el modo de visualización
            i.setVariable(Documento.DOC_MODE, DocumentoMode.NAME_DIGITALIZANDO);

            // Intenta avanzar en el proceso
            i.forward(tid);

        } catch (IOException e) {
            e.printStackTrace();
            redirect.addFlashAttribute(AppConstants.FLASH_ERROR, "Ocurrió un error inesperado: " + e.getMessage());
        }

        return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, doc.getInstancia().getId());

    }

    /**
     * Descarga un archivo
     *
     * @param docId
     * @param dad
     * @param model
     * @param resp
     */
    @RequestMapping(value = "/adjunto", method = RequestMethod.GET)
    public void adjunto(@RequestParam("doc") String docId, @RequestParam("dad") String dad, Model model,
            HttpServletResponse resp) {

        try {
            Adjunto a = adjuntoRepository.getOne(dad);
            OFSEntry entry = ofs.read(a.getContenido());
            byte[] bytes = entry.getContent();

            resp.setContentLength((int) bytes.length);
            resp.setContentType(entry.getContentType());

            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", a.getOriginal());
            resp.setHeader(headerKey, headerValue);

            // Write response
            ServletOutputStream outStream = resp.getOutputStream();
            IOUtils.write(bytes, outStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda un adjunto
     *
     * @param docId
     * @param tipologia
     * @param archivo
     * @param model
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/adjunto", method = RequestMethod.POST)
    public String adjunto(@RequestParam("doc") String docId, @RequestParam("tipologia") Tipologia tipologia,
            @RequestParam("archivo") MultipartFile archivo, Model model, RedirectAttributes redirect) {

        Documento doc = documentoModel(model, docId);
        /*
		 * 2017-04-11 jgarcia@controltechcg.com Issue #46 (SIGDI-Controltech):
		 * Corrección en la presentación de mensajes de error y validaciones de
		 * tipo de archivo para el proceso de carga de archivos adjuntos
		 * asociados a un documento.
         */
        if (archivo.isEmpty()) {
            redirect.addFlashAttribute(AppConstants.FLASH_ERROR, "ERROR: Debe seleccionar un archivo adjunto.");
        } else {
            try {
                if (tipologia == null) {
                    redirect.addFlashAttribute(AppConstants.FLASH_ERROR,
                            "ERROR: Debe seleccionar una tipología para el archivo adjunto.");
                } else {
                    String contentType = archivo.getContentType();

                    boolean validAttachmentContentType = isValidAttachmentContentType(contentType);
                    if (!validAttachmentContentType) {
                        redirect.addFlashAttribute(AppConstants.FLASH_ERROR,
                                "ERROR: Únicamente se permiten cargar archivos adjuntos tipo PDF, JPG o PNG.");
                    } else {
                        Adjunto adjunto = Adjunto.create();
                        adjunto.setDocumento(doc);
                        adjunto.setTipologia(tipologia);
                        adjunto.setOriginal(archivo.getOriginalFilename());

                        byte[] bytes = archivo.getBytes();

                        String fileId = ofs.save(bytes, contentType);
                        adjunto.setContenido(fileId);

                        adjuntoRepository.save(adjunto);
                        redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                                "Archivo adjuntado: " + adjunto.getOriginal());
                    }
                }
            } catch (Exception e) {
                redirect.addFlashAttribute(AppConstants.FLASH_ERROR,
                        "No se adjuntó el archivo: Ocurrio un error inesperado.");
                e.printStackTrace();
            }
        }

        return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, doc.getInstancia().getId());
    }

    /**
     * Indica si el tipo de contenido del archivo adjunto es válido para el
     * sistema.
     *
     * @param contentType Tipo de contenido.
     * @return {@code true} si el tipo de contenido es válido (PDF, JPG, JPEG,
     * PNG); de lo contrario, {@code false}.
     */
    // 2017-04-11 jgarcia@controltechcg.com Issue #46 (SIGDI-Controltech)
    private boolean isValidAttachmentContentType(String contentType) {
        String[] validContentTypes = {"application/pdf", "image/jpeg", "image/jpg", "image/png"};

        for (String validContentType : validContentTypes) {
            if (contentType.equals(validContentType)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Actualiza el nombre del archivo adjunto
     *
     * @param dad (identificador del adjunto)
     * @param nombreAdjunto
     * @param desc
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/adjunto/update", method = RequestMethod.GET)
    public String adjuntoUpdate(@RequestParam("dad") String dad, @RequestParam("nAd") String nombreAdjunto,
            @RequestParam("desc") String desc, RedirectAttributes redirect) {

        Adjunto a = adjuntoRepository.getOne(dad);
        Tipologia t = a.getTipologia();
        t.setNombre(nombreAdjunto);
        a.setTipologia(t);
        a.setDescripcion(desc);
        try {
            adjuntoRepository.save(a);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Archivo actualizado");
        } catch (Exception e) {
            redirect.addFlashAttribute(AppConstants.FLASH_ERROR, "Archivo no actualizado: " + e.getMessage());
        }
        return "redirect:/expediente/list";
    }

    /**
     * Elimina un archivo adjunto mediante la marca de activo del mismo
     *
     * @param dad
     * @param pin
     * @param redirect
     * @param principal
     * @return
     */
    @RequestMapping(value = "/adjunto/{dad}/eliminar", method = RequestMethod.GET)
    public String adjuntoEliminar(@PathVariable("dad") String dad, @RequestParam("pin") String pin,
            RedirectAttributes redirect, Principal principal) {

        // Obtiene la instancia de proceso
        Instancia i = procesoService.instancia(pin);

        // Obtiene el documento asociado a la instancia de proceso
        String docId = i.getVariable(Documento.DOC_ID);
        Documento doc = documentRepository.getOne(docId);

        // Determina el modo de visualización y edición del documento
        DocumentoMode mode = DocumentoMode.getByName(i.getVariable(Documento.DOC_MODE));
        doc.setMode(mode);
        boolean deleted = false;
        if (mode.get("adjuntos_edit")) {
            List<Adjunto> adjuntos = doc.getAdjuntos();
            for (Adjunto adjunto : adjuntos) {
                if (adjunto.getId().equals(dad)) {
                    adjunto.setActivo(false);
                    adjuntoRepository.save(adjunto);
                    deleted = true;
                    break;
                }
            }
        }

        if (!deleted) {
            redirect.addFlashAttribute(AppConstants.FLASH_ERROR, "El adjunto no se encuentra o no se puede borrar");
        } else {
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "El adjunto se ha eliminado");
        }

        return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
    }

    /**
     * Crea una observación al documento
     *
     * @param docId
     * @param observacion
     * @param model
     * @return
     */
    @RequestMapping(value = {"/observacion"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> observacion(@RequestParam("doc") String docId,
            @RequestParam("observacion") String observacion, Model model) {
        Documento doc = documentRepository.findOne(docId);
        DocumentoObservacion obs = new DocumentoObservacion();
        obs.setDocumento(doc);
        String escaped = observacion.replace("&", "&amp;");
        escaped = escaped.replace("<", "&lt;");
        escaped = escaped.replace(">", "&gt;");
        escaped = escaped.replace("\n", "<br/>");
        obs.setTexto(escaped);
        dobRepository.save(obs);
        Map<String, String> map = new HashMap<String, String>();
        map.put("texto", obs.getTexto());
        map.put("cuando", obs.getCuando().toString());
        map.put("quien", usuR.getOne(obs.getQuien()).toString());
        return map;
    }

    @RequestMapping(value = "/asignar", method = {RequestMethod.GET, RequestMethod.POST})
    public String asignar(@RequestParam(value = "pin", required = true) final String pin,
            @RequestParam(value = "tid", required = true) final String tidS,
            @RequestParam(value = "mode", required = true) final String mode,
            @RequestParam(value = "did", required = false) final String didS,
            @RequestParam(value = "uid", required = false) final String uidS, Model model, final Principal principal,
            RedirectAttributes redirect) {

        Integer idUsuario = null;
        if (uidS != null) {
            idUsuario = Integer.parseInt(uidS.replaceAll("\\.", ""));
        }

        Integer idDependencia = null;
        if (didS != null) {
            idDependencia = Integer.parseInt(didS.replaceAll("\\.", ""));
        }

        Integer tid = Integer.parseInt(tidS.replaceAll("\\.", ""));
        Integer did = idDependencia;
        final Integer uid = idUsuario;

        model.addAttribute("mode", mode);
        model.addAttribute("pin", pin);
        model.addAttribute("tid", tid);
        model.addAttribute("did", did);

        if ("usuario".equals(mode)) {
            if (uid != null) {

                Instancia i = asignar(pin, tid, new EstrategiaSeleccionUsuario() {
                    @Override
                    public Usuario select(Instancia i, Documento d) {
                        return usuR.getOne(uid);
                    }
                });

                String docId = i.getVariable(Documento.DOC_ID);
                Documento doc = documentRepository.getOne(docId);
                doc.setUsuarioUltimaAccion(getUsuario(principal));
                documentRepository.saveAndFlush(doc);

                /*
                 * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
                 * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
                 */
                redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                        buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, i, "Asignado a "));
                return redirectToInstancia(i);
            } else {
                if (did != null) {
                    Instancia i = procesoService.instancia(pin);

                    // 2017-10-05 edison.gonzalez@controltechcg.com Issue #131
                    List<Usuario> usuarios = usuR.findByDependenciaAndActivoTrueOrderByGradoDesc(did);
                    String docId = i.getVariable(Documento.DOC_ID);
                    Documento doc = documentRepository.getOne(docId);

                    model.addAttribute("documento", doc);

                    int pesoClasificacionDocumento = doc.getClasificacion().getId();

                    for (Usuario usuarioSeleccionado : usuarios) {

                        if (usuarioSeleccionado.getClasificacion() == null
                                || usuarioSeleccionado.getClasificacion().getId() == null) {
                            usuarioSeleccionado.setMensajeNivelAcceso(" ****SIN CLSIFICACIÓN****");
                            usuarioSeleccionado.setRestriccionDocumentoNivelAcceso(true);
                        } else {
                            int pesoClasificacionUsuario = usuarioSeleccionado.getClasificacion().getId();
                            usuarioSeleccionado
                                    .setMensajeNivelAcceso(" -" + usuarioSeleccionado.getClasificacion().getNombre());
                            usuarioSeleccionado.setRestriccionDocumentoNivelAcceso(
                                    pesoClasificacionUsuario < pesoClasificacionDocumento);
                        }
                    }

                    model.addAttribute("usuarios", usuarios);
                }
                model.addAttribute("dependencias", depsHierarchy());
            }
        } else if ("dependencia".equals(mode)) {

        } else if ("jefatura".equals(mode)) {

        } else if ("otra-jefatura".equals(mode)) {

        } else if ("mijefe".equals(mode)) {
            Instancia i = asignar(pin, tid, new EstrategiaSeleccionUsuario() {

                @Override
                public Usuario select(Instancia i, Documento d) {

                    Date hoy = new Date();
                    if (d.getDependenciaDestino() != null && d.getDependenciaDestino().getJefeEncargado() != null
                            && (d.getDependenciaDestino().getFchInicioJefeEncargado() != null
                            && d.getDependenciaDestino().getFchInicioJefeEncargado().compareTo(hoy) <= 0
                            && d.getDependenciaDestino().getFchFinJefeEncargado() != null
                            && hoy.compareTo(d.getDependenciaDestino().getFchFinJefeEncargado()) <= 0)) {
                        return d.getDependenciaDestino().getJefeEncargado();
                    } else {
                        Usuario yo = usuR.findByLoginAndActivo(principal.getName(), true);
                        return yo.getDependencia().getJefe();
                    }

                }

            });

            String docId = i.getVariable(Documento.DOC_ID);
            Documento doc = documentRepository.getOne(docId);
            doc.setUsuarioUltimaAccion(getUsuario(principal));
            documentRepository.saveAndFlush(doc);

            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Documento asignado a " + i.getAsignado());

            return redirectToInstancia(i);
        } else {
            model.addAttribute("mode", "nomode");
        }

        return "documento-asignar";
    }

    /**
     * Asigna al jefe máximo y avanza en el proceso
     *
     * @param pin Identificador de la instancia de proceso
     * @param tid
     * @param model
     * @param redirect
     * @param principal
     * @return
     */
    @RequestMapping(value = "/asignar-jefe-maximo", method = RequestMethod.GET)
    public String asignarJefeMaximo(@RequestParam("pin") String pin,
            @RequestParam(value = "tid", required = false) Integer tid, Model model, RedirectAttributes redirect,
            Principal principal) {

        /*
		 * 2017-02-09 jgarcia@controltechcg.com Issue #11 (SIGDI-Incidencias01):
		 * En caso que el usuario en sesión corresponda como Jefe Segundo de la
		 * Dependencia Destino, se asigna el documento al Jefe principal de la
		 * Dependencia.
         */
        final Usuario yo = getUsuario(principal);

        // Asigna al jefe máximo

        /*
		 * 2017-03-16 jgarcia@controltechcg.com Issue #24 (SIGDI-Incidencias01):
		 * Se separa la estrategia de selección de usuario, en la asignación de
		 * Jefe Máximo, para realizar una validación previa si hay o no jefe en
		 * la Super Dependencia. En caso que no haya Jefe Asignado se presenta
		 * un mensaje de error.
         */
        EstrategiaSeleccionUsuario estrategiaSeleccion = new EstrategiaSeleccionUsuario() {
            @Override
            public Usuario select(Instancia i, Documento d) {
                /*
				 * 2017-02-06 jgarcia@controltechcg.com Issue# 150: Para
				 * asignación de Jefe Máximo, se busca la Super Dependencia
				 * Destino. Sobre esta se consulta si tiene un Jefe Segundo
				 * (Encargado). Si lo tiene se compara el rango de fechas
				 * asignado en la Super Dependencia. En caso que la fecha actual
				 * corresponda al rango, se selecciona el Jefe Segundo
				 * (Encargado). De lo contrario, a las reglas anteriormente
				 * descritas, se selecciona el Jefe de la Super Dependencia.
                 */
                Dependencia dependenciaDestino = d.getDependenciaDestino();
                Dependencia superDependencia = getSuperDependencia(dependenciaDestino);

                Usuario jefeActivo = dependenciaService.getJefeActivoDependencia(superDependencia);
                /*
				 * 2017-02-09 jgarcia@controltechcg.com Issue #11
				 * (SIGDI-Incidencias01): En caso que el usuario en sesión
				 * corresponda como Jefe Segundo de la Dependencia Destino, se
				 * asigna el documento al Jefe principal de la Dependencia.
                 */
                if (jefeActivo != null && yo.getId().equals(jefeActivo.getId())) {
                    return superDependencia.getJefe();
                }

                return jefeActivo;
            }
        };

        /*
		 * 2017-03-16 jgarcia@controltechcg.com Issue #24 (SIGDI-Incidencias01):
		 * En caso que no haya Jefe Asignado se presenta un mensaje de error.
         */
        Instancia actualInstancia = procesoService.instancia(pin);
        String docID = actualInstancia.getVariable(Documento.DOC_ID);
        Documento documento = documentRepository.getOne(docID);

        Usuario usuarioSeleccionado = estrategiaSeleccion.select(actualInstancia, documento);
        if (usuarioSeleccionado == null) {
            redirect.addFlashAttribute(AppConstants.FLASH_ERROR, "Dependencia destino no tiene Jefe Activo asignado.");
            return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
        }

        Instancia i = asignar(pin, tid, estrategiaSeleccion);

        i.setVariable(Documento.DOC_MODE, DocumentoMode.NAME_ENTREGADO);

        /*
         * 2018-03-06 edison.gonzalez@controltechcg.com Issue #151
         * (SICDI-Controltech): Se ejecuta el proceso de archivo automático,
         * tras aplicar la transición de entregar documentos.
         */
        archivoAutomaticoService.archivarAutomaticamente(documento, yo);

        /*
         * 2017-05-30 jgarcia@controltechcg.com Issue #98 (SICDI-Controltech)
         * hotfix-98: Corrección en texto de mensaje de asignación de usuario a
         * siguiente transición del documento.
         */
 /*
         * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
         * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
         */
        redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, i, "Asignado a "));

        if (i.transiciones().size() > 0) {
            return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
        } else {
            return "redirect:/";
        }
    }

    /**
     * Pantalla de asignación a dependencia
     *
     * @param pin Identificador de la instancia de proceso
     * @param tid
     * @param did
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping(value = "/asignar-jefe-dependencia", method = RequestMethod.GET)
    public String asignarJefeDependencia(@RequestParam("pin") String pin,
            @RequestParam(value = "tid", required = false) Integer tid,
            @RequestParam(value = "did", required = false) Integer did, Model model, Principal principal) {
        model.addAttribute("pin", pin);
        model.addAttribute("tid", tid);

        // Carga las dependencias según el parámetro did que es el número de
        // identificación de la dependencia padre. En caso que no esté presente
        // el parámetro entonces se asume que se trata de dependencias jefatura.
        /*
		 * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS:
		 * Ordenamiento por peso. Modificación: variable y orden en que se
		 * presentan las dependencias.
         */
        if (did != null) {
            List<Dependencia> deps = dependenciaRepository.findByActivoAndPadre(true, did,
                    new Sort(Direction.ASC, "pesoOrden", "nombre"));
            model.addAttribute("dependencias", deps);
            Dependencia dep = dependenciaRepository.getOne(did);
            model.addAttribute("did", did);
            if (dep.getPadre() != null) {
                model.addAttribute("pid", dep.getPadre());
            }
        } else {
            /*
			 * 2017-02-08 jgarcia@controltechcg.com Issue #4
			 * (SIGDI-Incidencias01): Se realiza la búsqueda inicial por las
			 * dependencias hijas, de la dependencia destino del documento. Si
			 * el conjunto obtenido es vacío, entonces se presentan todas las
			 * dependencias padres para la selección por parte del usuario.
             */
            Instancia instancia = instanciaRepository.findOne(pin);
            Documento documento = documentRepository.findOne(instancia.getVariable(Documento.DOC_ID));
            /*
			 * 2017-02-08 jgarcia@controltechcg.com Issue #31
			 * (SIGDI-Incidencias01): Modificación en el proceso de asignación
			 * de jefe de dependencia para que aparezcan las dependencias hijas
			 * del usuario en sesión en caso que no corresponda a la misma
			 * dependencia destino del documento.
             */
            Dependencia dependenciaDestino = documento.getDependenciaDestino();
            Usuario jefeActivoDependenciaDestino = dependenciaService.getJefeActivoDependencia(dependenciaDestino);
            Usuario usuarioSesion = getUsuario(principal);
            if (jefeActivoDependenciaDestino == null
                    || !jefeActivoDependenciaDestino.getId().equals(usuarioSesion.getId())) {
                dependenciaDestino = usuarioSesion.getDependencia();
            }
            /*
			 * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS:
			 * Ordenamiento por peso. Modificación: variable y orden en que se
			 * presentan las dependencias.
             */
            List<Dependencia> dependencias = dependenciaRepository.findByActivoAndPadre(Boolean.TRUE,
                    dependenciaDestino.getId(), new Sort(Direction.ASC, "pesoOrden", "nombre"));
            if (dependencias == null || dependencias.isEmpty()) {
                dependencias = dependenciaRepository.findByActivoAndPadreIsNull(true,
                        new Sort(Direction.ASC, "pesoOrden", "nombre"));
            } else {
                /*
				 * 2017-02-09 jgarcia@controltechcg.com Issue #11
				 * (SIGDI-Incidencias01): En caso que el usuario en sesión
				 * corresponda como Jefe Segundo de la Dependencia Destino, se
				 * adiciona la Dependencia Destino en la lista de asignación
				 * para que pueda ser asignado el documento al Jefe principal de
				 * la Dependencia.
                 */
                Usuario jefeActivo = dependenciaService.getJefeActivoDependencia(dependenciaDestino);
                if (jefeActivo != null && usuarioSesion.getId().equals(jefeActivo.getId())) {
                    if (!dependencias.contains(dependenciaDestino)) {
                        dependencias.add(0, dependenciaDestino);
                    }
                }
            }

            model.addAttribute("dependencias", dependencias);
        }

        return "documento-asignar-dependencia";
    }

    /**
     * Asigna al jefe de dependencia
     *
     * @param pin Identificador de la instancia de proceso
     * @param tid
     * @param depId
     * @param model
     * @param redirect
     * @param principal
     * @return
     */
    @RequestMapping(value = "/asignar-jefe-dependencia", method = RequestMethod.POST)
    public String asignarJefeDependencia(@RequestParam("pin") String pin,
            @RequestParam(value = "tid", required = false) Integer tid, @RequestParam("depId") final Integer depId,
            Model model, RedirectAttributes redirect, Principal principal) {

        final Usuario usuarioSesion = getUsuario(principal);

        /*
		 * 2017-03-16 jgarcia@controltechcg.com Issue #24 (SIGDI-Incidencias01):
		 * Se separa la estrategia de selección de usuario, en la asignación de
		 * Jefe de Dependencia, para realizar una validación previa si hay o no
		 * jefe en la Super Dependencia. En caso que no haya Jefe Asignado se
		 * presenta un mensaje de error.
         */
        EstrategiaSeleccionUsuario estrategiaSeleccion = new EstrategiaSeleccionUsuario() {
            @Override
            public Usuario select(Instancia i, Documento d) {
                /*
				 * 2017-02-09 jgarcia@controltechcg.com Issue #11
				 * (SIGDI-Incidencias01): En caso que el usuario en sesión
				 * corresponda como Jefe Segundo de la Dependencia Destino, se
				 * asigna el documento al Jefe principal de la Dependencia.
                 */
                Dependencia dependenciaDestino = dependenciaRepository.getOne(depId);

                /*
				 * 2017-02-09 jgarcia@controltechcg.com Issue #11
				 * (SIGDI-Incidencias01): En caso que el usuario en sesión
				 * corresponda como Jefe Segundo de la Dependencia Destino, se
				 * asigna el documento al Jefe principal de la Dependencia.
                 */
                Usuario jefeActivo = dependenciaService.getJefeActivoDependencia(dependenciaDestino);
                if (jefeActivo != null && usuarioSesion.getId().equals(jefeActivo.getId())) {
                    return dependenciaDestino.getJefe();
                }

                return jefeActivo;
            }
        };

        /*
		 * 2017-03-16 jgarcia@controltechcg.com Issue #24 (SIGDI-Incidencias01):
		 * En caso que no haya Jefe Asignado se presenta un mensaje de error.
         */
        Instancia actualInstancia = procesoService.instancia(pin);
        String docID = actualInstancia.getVariable(Documento.DOC_ID);
        Documento documento = documentRepository.getOne(docID);

        Usuario usuarioSeleccionado = estrategiaSeleccion.select(actualInstancia, documento);
        if (usuarioSeleccionado == null) {
            redirect.addFlashAttribute(AppConstants.FLASH_ERROR, "Dependencia destino no tiene Jefe Activo asignado.");
            return "redirect:/";
        }

        // Asigna al jefe de la dependencia
        Instancia i = asignar(pin, tid, estrategiaSeleccion);

        i.setVariable(Documento.DOC_MODE, DocumentoMode.NAME_ENTREGADO);

        /*
         * 2017-03-13 jgarcia@controltechcg.com Issue #12 (SIGDI-Incidencias01):
         * Registro del usuario en sesión como usuario de la última acción
         * (Asignar Jefe Dependencia), para que este aparezca como el usuario
         * que envío el documento.
         */
        documento.setUsuarioUltimaAccion(usuarioSesion);
        documentRepository.saveAndFlush(documento);

        /*
         * 2017-05-30 jgarcia@controltechcg.com Issue #98 (SICDI-Controltech)
         * hotfix-98: Corrección en texto de mensaje de asignación de usuario a
         * siguiente transición del documento.
         * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
         * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
         */
        redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, i, "Asignado a "));

        if (i.transiciones().size() > 0) {
            return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
        } else {
            return "redirect:/";
        }
    }

    /**
     * Pantalla de asignación a cualquiera
     *
     * @param pin Identificador de la instancia de proceso
     * @param tid
     * @param depId
     * @param usuId
     * @param model
     * @param principal
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/asignar-cualquiera-dependencia", method = RequestMethod.GET)
    public String asignarCualquieraDependencia(@RequestParam("pin") String pin,
            @RequestParam(value = "tid", required = false) Integer tid,
            @RequestParam(value = "depId", required = false) Integer depId,
            @RequestParam(value = "usuId", required = false) final Integer usuId, Model model, Principal principal,
            RedirectAttributes redirect) {

        model.addAttribute("pin", pin);
        model.addAttribute("tid", tid);
        /*
		 * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS:
		 * Ordenamiento por peso. Modificación: variable y orden en que se
		 * presentan las dependencias.
         */
        if (usuId == null) {
            if (depId == null) {
                List<Dependencia> deps = dependenciaRepository.findByActivoAndPadreIsNull(true,
                        new Sort(Direction.ASC, "pesoOrden", "nombre"));
                model.addAttribute("dependencias", deps);
            } else {
                List<Dependencia> deps = dependenciaRepository.findByActivoAndPadre(true, depId,
                        new Sort(Direction.ASC, "pesoOrden", "nombre"));
                model.addAttribute("dependencias", deps);
                // 2017-10-05 edison.gonzalez@controltechcg.com Issue #131
                model.addAttribute("usuarios",
                        usuR.findByDependenciaAndActivoTrueOrderByGradoDesc(depId));
            }
        } else {
            // Asigna al jefe de la dependencia
            Instancia i = asignar(pin, tid, new EstrategiaSeleccionUsuario() {
                @Override
                public Usuario select(Instancia i, Documento d) {
                    return usuR.getOne(usuId);
                }
            });

            /*
             * 2017-05-30 jgarcia@controltechcg.com Issue #98
             * (SICDI-Controltech) hotfix-98: Corrección en texto de mensaje de
             * asignación de usuario a siguiente transición del documento.
             * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
             * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
             */
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                    buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, i, "Asignado a "));

            if (i.transiciones().size() > 0) {
                return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
            } else {
                return "redirect:/";
            }
        }

        return "documento-asignar-cualquiera";
    }

    /**
     * Pantalla de asignación a cualquiera en la misma dependencia
     *
     * @param pin Identificador de la instancia de proceso
     * @param tid
     * @param usuId
     * @param did
     * @param model
     * @param principal
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/asignar-cualquiera-mi-dependencia", method = RequestMethod.GET)
    public String asignarCualquieraMiDependencia(@RequestParam("pin") String pin,
            @RequestParam(value = "tid", required = false) Integer tid,
            @RequestParam(value = "usuId", required = false) final Integer usuId,
            @RequestParam(value = "did", required = false) Integer did, Model model, Principal principal,
            RedirectAttributes redirect) {

        model.addAttribute("pin", pin);
        model.addAttribute("tid", tid);

        Usuario usuarioSesion = getUsuario(principal);

        if (usuId == null) {

            Instancia instancia = procesoService.instancia(pin);

            model.addAttribute("usuarios", "USUARIOS");

            List<Dependencia> listaDependencias = new ArrayList<>(1);
            model.addAttribute("dependencias_arbol", listaDependencias);

            String docID = instancia.getVariable(Documento.DOC_ID);
            Documento documento = documentRepository.getOne(docID);

            /*
			 * 2017-03-16 jgarcia@controltechcg.com Issue #16
			 * (SIGDI-Incidencias01): Corrección para que en caso que el proceso
			 * corresponda a la generación de documentos externos, la asignación
			 * se realize sobre la dependencia del usuario en sesión.
             */
            Integer procesoID = instancia.getProceso().getId();
            Dependencia dependenciaDestino;
            if (procesoID.equals(Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS)) {
                dependenciaDestino = usuarioSesion.getDependencia();
            } else {
                /*
				 * 2017-03-24 jgarcia@controltechcg.com Issue #29
				 * (SIGDI-Incidencias01): Cambio para que la dependencia base
				 * corresponda a la dependencia del usuario en sesión.
                 */
                // dependenciaDestino = documento.getDependenciaDestino();
                dependenciaDestino = usuarioSesion.getDependencia();
            }

            /*
			 * 2017-03-16 jgarcia@controltechcg.com Issue #16
			 * (SIGDI-Incidencias01): Se modifica el nodo raíz del arbol de
			 * dependencias para presentar desde la super dependencia (unidad)
			 * correspondiente a la dependencia destino del documento.
             */
            Dependencia superDependencia = getSuperDependencia(dependenciaDestino);
            depsHierarchy(superDependencia);
            listaDependencias.add(superDependencia);

            if (did != null) {
                // 2017-10-05 edison.gonzalez@controltechcg.com Issue #131
                List<Usuario> usuarios = usuR.findByDependenciaAndActivoTrueOrderByGradoDesc(did);

                model.addAttribute("documento", documento);

                int pesoClasificacionDocumento = documento.getClasificacion().getId();

                for (Usuario usuarioSeleccionado : usuarios) {

                    if (usuarioSeleccionado.getClasificacion() == null
                            || usuarioSeleccionado.getClasificacion().getId() == null) {
                        usuarioSeleccionado.setMensajeNivelAcceso(" ****SIN CLSIFICACIÓN****");
                        usuarioSeleccionado.setRestriccionDocumentoNivelAcceso(true);
                    } else {
                        int pesoClasificacionUsuario = usuarioSeleccionado.getClasificacion().getId();
                        usuarioSeleccionado
                                .setMensajeNivelAcceso(" -" + usuarioSeleccionado.getClasificacion().getNombre());
                        usuarioSeleccionado.setRestriccionDocumentoNivelAcceso(
                                pesoClasificacionUsuario < pesoClasificacionDocumento);
                    }
                }

                model.addAttribute("lista_usuarios", usuarios);
            }

        } else {
            Instancia i = asignar(pin, tid, new EstrategiaSeleccionUsuario() {
                @Override
                public Usuario select(Instancia i, Documento d) {
                    return usuR.getOne(usuId);
                }
            });

            /*
             * 2017-03-14 jgarcia@controltechcg.com Issue #12
             * (SIGDI-Incidencias01): En el paso de asignar a cualquier usuario
             * de mi dependencia, tras la selección del usuario, se modifica la
             * funcionalidad para que se registre el usuario en sesión como el
             * último usuario que realizó acción sobre el documento, para que
             * este pueda ser visualizado en los datos del documento, en el
             * apartado "Proceso > Enviado por:".
             */
            Documento documento = documentRepository.findOne(i.getVariable(Documento.DOC_ID));
            documento.setUsuarioUltimaAccion(usuarioSesion);
            documentRepository.saveAndFlush(documento);

            /*
             * 2017-05-30 jgarcia@controltechcg.com Issue #98
             * (SICDI-Controltech) hotfix-98: Corrección en texto de mensaje de
             * asignación de usuario a siguiente transición del documento.
             * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
             * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
             */
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                    buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, i, "Asignado a "));

            if (i.transiciones().size() > 0) {
                return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
            } else {
                return "redirect:/";
            }
        }

        return "documento-asignar-cualquiera";
    }

    /**
     * Asigna al mismo usuario
     *
     * @param pin Identificador de la instancia de proceso
     * @param tid
     * @param model
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/asignar-mismo", method = RequestMethod.GET)
    public String asignarMismo(@RequestParam("pin") String pin,
            @RequestParam(value = "tid", required = false) Integer tid, Model model, RedirectAttributes redirect) {

        Instancia i = asignar(pin, tid, new EstrategiaSeleccionUsuario() {
            @Override
            public Usuario select(Instancia i, Documento d) {
                return i.getAsignado();
            }
        });

        /*
         * 2017-05-30 jgarcia@controltechcg.com Issue #98 (SICDI-Controltech)
         * hotfix-98: Corrección en texto de mensaje de asignación de usuario a
         * siguiente transición del documento.
         * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
         * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
         */
        redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, i, "Asignado a "));

        if (i.transiciones().size() > 0) {
            return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
        } else {
            return "redirect:/";
        }
    }

    /**
     * Realiza la transición asignando el usuario anterior
     *
     * @param pin
     * @param bvb
     * @param tid
     * @param model
     * @param redirect
     * @param principal
     * @return
     */
    @RequestMapping(value = "/devolver", method = RequestMethod.GET)
    public String devolver(@RequestParam("pin") final String pin,
            @RequestParam(value = "bvb", required = false) final Integer bvb, @RequestParam("tid") Integer tid,
            Model model, RedirectAttributes redirect, Principal principal) {

        // Asigna al jefe del usuario
        Instancia i = asignar(pin, tid, new EstrategiaSeleccionUsuario() {
            @Override
            public Usuario select(Instancia i, Documento d) {
                // Elimina el visto bueno
                if (bvb != null) {
                    d.setVistoBueno(null);
                }

                Usuario yo = i.getAsignado();

                List<HProcesoInstancia> hinstancias = hprocesoInstanciaRepository.findById(pin,
                        new Sort(Direction.DESC, "cuandoMod"));

                for (HProcesoInstancia h : hinstancias) {
                    if (h.getAsignado().getId() != yo.getId()) {
                        return h.getAsignado();
                    }
                }

                return yo;
            }
        });

        // Cambia a en construcción
        String construccionMode = i.getVariable(Documento.DOC_MODE_CONSTRUCCION);
        if (StringUtils.isBlank(construccionMode)) {
            construccionMode = DocumentoMode.NAME_EN_CONSTRUCCION;
        }

        String docId = i.getVariable(Documento.DOC_ID);
        Documento doc = documentRepository.getOne(docId);
        doc.setUsuarioUltimaAccion(getUsuario(principal));
        documentRepository.saveAndFlush(doc);

        i.setVariable(Documento.DOC_MODE, construccionMode);

        /*
		 * 2017-03-14 jgarcia@controltechcg.com Issue #20 (SIGDI-Incidencias01):
		 * Se establece el estado "EN_CONSTRUCCION" (61) sobre la instancia del
		 * proceso al aplicar la transición "No dar visto bueno" (Devolver
		 * documento).
         */
        Integer procesoID = i.getProceso().getId();
        if (procesoID
                .equals(Proceso.ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA)
                || procesoID.equals(Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS)) {
            Estado estadoEnConstruccion = new Estado();
            estadoEnConstruccion.setId(Estado.EN_CONSTRUCCION);
            i.setEstado(estadoEnConstruccion);

            /*
             * 2017-03-22 jgarcia@controltechcg.com Issue #29
             * (SIGDI-Incidencias01): Se modifica la función devolver, para que
             * en la actualización de los datos de la instancia del proceso,
             * reasigne al usuario creador del documento.
             *
             * 2017-04-20 jgarcia@controltechcg.com Issue #52
             * (SICDI-Controltech): Corrección en la asignación del usuario
             * creador del documento, ya que en tiempo de generación del
             * documento respuesta, el usuario que firma y envía queda
             * registrado como el usuario que genera el documento.
             */
            Usuario usuarioCreador = doc.getElabora();
            i.setAsignado(usuarioCreador);

            instanciaRepository.saveAndFlush(i);

            /*
             * 2017-06-05 jgarcia@controltechcg.com Issue #98
             * (SICDI-Controltech) hotfix-98: Corrección en mensaje de
             * asignación para transición "No dar visto bueno" y "Devolver para
             * correcciones".
             * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
             * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
             */
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                    buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, i, "Asignado a "));
            return "redirect:/";
        }

        /*
         * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
         * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
         */
        redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                buildAsignadosText(documentoDependenciaAdicionalRepository, usuarioService, dependenciaService, i, "Asignado a ", true));

        if (i.transiciones().size() > 0) {
            return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
        } else {
            return "redirect:/";
        }
    }

    /**
     * Realiza la transición asignando el usuario elaboro
     *
     * @param pin
     * @param tid
     * @param bvb
     * @param model
     * @param principal
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/devolver-elaboro", method = RequestMethod.GET)
    public String devolverElaboro(@RequestParam("pin") final String pin, @RequestParam("tid") Integer tid,
            @RequestParam(value = "bvb", required = false) final Integer bvb, Model model, final Principal principal,
            RedirectAttributes redirect) {

        // Asigna al usuario que elaboró
        Instancia i = asignar(pin, tid, new EstrategiaSeleccionUsuario() {
            @Override
            public Usuario select(Instancia i, Documento d) {
                // Elimina el visto bueno
                if (bvb != null) {
                    d.setVistoBueno(null);
                }
                if (d.getElabora() != null) {
                    return d.getElabora();
                }
                return getUsuario(principal);
            }
        });

        // Cambia a en construcción
        String construccionMode = i.getVariable(Documento.DOC_MODE_CONSTRUCCION);
        if (StringUtils.isBlank(construccionMode)) {
            construccionMode = DocumentoMode.NAME_EN_CONSTRUCCION;
        }
        i.setVariable(Documento.DOC_MODE, construccionMode);

        String docId = i.getVariable(Documento.DOC_ID);
        Documento doc = documentRepository.getOne(docId);
        doc.setUsuarioUltimaAccion(getUsuario(principal));
        documentRepository.saveAndFlush(doc);

        /*
         * Issue #118
         *
         * 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
         * feature-78
         *
         * 2017-05-24 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech)
         * feature-73
         *
         * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
         * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
         */
        redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, i, "Asignado a "));

        if (i.transiciones().size() > 0) {
            return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
        } else {
            return "redirect:/";
        }
    }

    /**
     * Marca la aprobación y avanza
     *
     * @param pin
     * @param tid
     * @param model
     * @param principal
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/aprobar", method = RequestMethod.GET)
    public String aprobar(@RequestParam("pin") String pin, @RequestParam("tid") Integer tid, Model model,
            Principal principal, RedirectAttributes redirect) {

        // Asigna al jefe del usuario
        Instancia i = asignar(pin, tid, new EstrategiaSeleccionUsuario() {
            @Override
            public Usuario select(Instancia i, Documento d) {
                // Usuario yo = i.getAsignado();
                Usuario crea = d.getElabora();
                return crea;
            }
        });

        Usuario uActualLogin = getUsuario(principal);

        String docId = i.getVariable(Documento.DOC_ID);
        Documento doc = documentRepository.getOne(docId);
        doc.setUsuarioUltimaAccion(uActualLogin);
        doc.setAprueba(uActualLogin);
        documentRepository.saveAndFlush(doc);

        // Cambia a solo lectura
        i.setVariable(Documento.DOC_MODE, DocumentoMode.NAME_SOLO_LECTURA);

        /*
         * Issue #118
         *
         * 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
         * feature-78
         *
         * 2017-05-24 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech)
         * feature-73
         *
         * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
         * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
         */
        redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, i, "Asignado a "));

        if (i.transiciones().size() > 0) {
            return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/seleccionar-expediente", method = RequestMethod.GET)
    public String seleccionarExpediente(@RequestParam("returnUrl") String returnUrl,
            @RequestParam("cancelUrl") String cancelUrl, Model model, Principal principal,
            RedirectAttributes redirect) {
        expedientes(model, principal);
        try {
            model.addAttribute("returnUrl", URLDecoder.decode(returnUrl, "UTF-8"));
            model.addAttribute("cancelUrl", URLDecoder.decode(cancelUrl, "UTF-8"));
        } catch (Exception e) {
            String msg = String.format("No se pueden decodificar las direcciones: return=%s, cancel=%s", returnUrl,
                    cancelUrl);
            LOG.error(msg, e);
            redirect.addFlashAttribute(AppConstants.FLASH_ERROR, msg);
            return "redirect:/";
        }
        return "documento-seleccionar-expediente";
    }

    /**
     * Aplica sobre el documento, el proceso de firma y envío. En caso que el
     * documento corresponda al proceso de generación interna y además tenga
     * dependencias copia multidestino, ejecuta el proceso correspondiente al
     * envío de copias del documento original a los múltiples destinos
     * seleccionados.
     *
     * @param pinID ID de la instancia del proceso del documento.
     * @param transicionID ID de la transición a aplicar.
     * @param expedienteID ID del expediente a asignar al documento. Opcional.
     * @param cargoIdFirma ID del cargo con el que el usuario en sesión firmará
     * el documento.
     * @param model Modelo de interfaz de usuario.
     * @param principal Instancia de información de usuario en sesión.
     * @param redirect Modelo de atributos de redirecciones.
     * @return Nombre del template o URL de redirección a presentar posterior a
     * la ejecución de la funcionalidad.
     */
    /*
     * 2018-04-11 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech)
     * feature-156: Se realiza separación entre el método que recibe la
     * información para el proceso de firma y entre los métodos que realizan
     * el proceso de firma y envío del documento.
     */
    // TODO: La URL es temporal mientras se realiza el desarrollo.
    @RequestMapping(value = "/firmar", method = RequestMethod.GET)
    public String firmarDocumento(@RequestParam("pin") String pinID, @RequestParam("tid") Integer transicionID, @RequestParam(value = "expId", required = false) Integer expedienteID,
            @RequestParam(value = "cargoIdFirma", required = false) Integer cargoIdFirma, Model model, Principal principal, RedirectAttributes redirect) {

        final String redirectURL = String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pinID);

        final Usuario usuarioSesion = getUsuario(principal);

        final Instancia instancia = instanciaRepository.findOne(pinID);

        final String documentoID = instancia.getVariable(Documento.DOC_ID);
        final Documento documento = documentRepository.findOne(documentoID);

        if (instancia.getProceso().getId().equals(Proceso.ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA)) {
            if (!multidestinoService.esDocumentoMultidestino(documento)) {
                try {
                    // TODO: Debe ejecutar el proceso de firma normal.
                    firmarYEnviarDocumento_NEW(documento, pinID, transicionID, expedienteID, cargoIdFirma, usuarioSesion);
                } catch (BusinessLogicException | RuntimeException ex) {
                    java.util.logging.Logger.getLogger(DocumentoController.class.getName()).log(Level.SEVERE, null, ex);
                    redirect.addFlashAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
                    return redirectURL;
                }

                /*
                 * Issue #118
                 *
                 * 2017-05-15 jgarcia@controltechcg.com Issue #78
                 * (SICDI-Controltech) feature-78
                 *
                 * 2017-05-24 jgarcia@controltechcg.com Issue #73
                 * (SICDI-Controltech) feature-73
                 *
                 * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
                 * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
                 */
                redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                        buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, instancia, "Asignado a "));
                return redirectURL;
            }

            final List<Dependencia> todasDependenciasMultidestino = multidestinoService.listarTodasDependenciasMultidestino(documento);
            final BusinessLogicValidation todasDependenciasDestinoValidacion = multidestinoService.validarTodasDependenciasMultidestino(todasDependenciasMultidestino);
            if (!todasDependenciasDestinoValidacion.isAllOK()) {
                // TODO: Enviar mensaje de error a la pantalla de firma y envío,
                // y evitar el procesamiento del documento.
                final String mensajeError = construirMensajeError(todasDependenciasDestinoValidacion);
                redirect.addFlashAttribute(AppConstants.FLASH_ERROR, mensajeError);
                return redirectURL;
            }

            // Verifica si se necesita realizar el proceso de clonación de los
            // documentos adicionales.
            Integer numPendientes = multidestinoService.cantidadDocumentosResultadosPendientesXDocumentoOriginal(documento.getId());

            if (numPendientes != 0) {
                try {
                    multidestinoService.clonarDocumentoMultidestino(documento);
                } catch (UncategorizedSQLException ex) {
                    java.util.logging.Logger.getLogger(DocumentoController.class.getName()).log(Level.SEVERE, null, ex);
                    redirect.addFlashAttribute(AppConstants.FLASH_ERROR, ex.getSQLException().getMessage());
                    return redirectURL;
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(DocumentoController.class.getName()).log(Level.SEVERE, null, ex);
                    redirect.addFlashAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
                    return redirectURL;
                }
            }

            try {
                // TODO: Colocar los parámetros necesarios para el proceso de
                // firmar y enviar del proceso original.
                firmarYEnviarDocumento_NEW(documento, pinID, transicionID, expedienteID, cargoIdFirma, usuarioSesion);
            } catch (BusinessLogicException | RuntimeException ex) {
                // TODO: Implementar la funcionalidad para realizar rollback en caso que se genere un error.
                java.util.logging.Logger.getLogger(DocumentoController.class.getName()).log(Level.SEVERE, null, ex);
                redirect.addFlashAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
                return redirectURL;
            }

            final List<Documento> documentosMultidestino = multidestinoService.listarTodosDocumentosMultidestino(documento);

            for (final Documento documentoMultidestino : documentosMultidestino) {
                try {
                    // TODO: Colocar los parámetros necesarios para el proceso de
                    // firmar y enviar.
                    final String multidestinoPinID = documentoMultidestino.getInstancia().getId();
                    firmarYEnviarDocumento_NEW(documentoMultidestino, multidestinoPinID, transicionID, expedienteID, cargoIdFirma, usuarioSesion);
                } catch (BusinessLogicException | RuntimeException ex) {
                    // TODO: Implementar la funcionalidad para realizar rollback en caso que se genere un error.
                    java.util.logging.Logger.getLogger(DocumentoController.class.getName()).log(Level.SEVERE, null, ex);
                    redirect.addFlashAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
                    return redirectURL;
                }
            }

            // TODO: Pendiente crear el método específico para la firma de 
            // documentos internos que permita la aplicación de multidestino
            // en caso de aplicar.
            /*
             * Issue #118
             *
             * 2017-05-15 jgarcia@controltechcg.com Issue #78
             * (SICDI-Controltech) feature-78
             *
             * 2017-05-24 jgarcia@controltechcg.com Issue #73
             * (SICDI-Controltech) feature-73
             *
             * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
             * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
             */
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, instancia, "Asignado a "));
            return redirectURL;
        }

        /*
         * Ejecuta el proceso de forma normal para documentos de otros procesos
         * (Generación externa).
         */
        try {
            firmarYEnviarDocumento_NEW(documento, pinID, transicionID, expedienteID, cargoIdFirma, usuarioSesion);
        } catch (BusinessLogicException | RuntimeException ex) {
            java.util.logging.Logger.getLogger(DocumentoController.class.getName()).log(Level.SEVERE, null, ex);
            redirect.addFlashAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            return redirectURL;
        }

        /**
         * Issue #118
         *
         * 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
         * feature-78
         *
         * 2017-05-24 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech)
         * feature-73
         *
         * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
         * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
         */
        redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, instancia, "Asignado a "));
        return redirectURL;
    }

    
    /**
     * Aplica la lógica de firma y envío para un documento.
     *
     * @param documento Documento.
     */
    // TODO: Cambiar el nombre y firma (parámetros) del método.
    private void firmarYEnviarDocumento_NEW(Documento documento, final String pinID, final Integer transicionID, final Integer expedienteID,
            final Integer cargoIdFirma, final Usuario usuarioSesion) throws BusinessLogicException {
        // TODO: Aplicar el proceso de firma y envío.
        System.out.println("Firmar y enviar: " + documento.getId() + "-----pinID= " + pinID);

        /*
         * 2018-04-11 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech)
         * feature-156: Retirar la opción dentro de la función
         * DocumentoController#firmar() que pregunta la selección del expediente
         * cuando este aún no ha sido asignado.
         */
        //<editor-fold defaultstate="collapsed" desc="feature-156">
        //        if (expId == null) {
        //            try {
        //                /*
        //                 * 2017-02-07 jgarcia@controltechcg.com Issue #47: Se modifica
        //                 * el redirect del formulario para que en el momento de dar clic
        //                 * al botón "Firmar y enviar" no haya invocación al formulario
        //                 * documento-seleccionar-expediente.ftl, sino que vaya
        //                 * directamente a la pantalla de firma.
        //                 */
        //                Instancia instancia = instanciaRepository.getOne(pin);
        //                Documento documento = documentRepository.findOneByInstanciaId(instancia.getId());
        //                Expediente expediente = documento.getExpediente();
        //
        //                if (expediente != null) {
        //                    expId = expediente.getId();
        //                }
        //
        //                if (expId == null) {
        //                    return String.format("redirect:/documento/seleccionar-expediente?returnUrl=%s&cancelUrl=%s",
        //                            URLEncoder.encode(String.format("/documento/firmar?pin=%s&tid=%d&cargoIdFirma=%d", pin, tid, cargoIdFirma), "UTF-8"),
        //                            URLEncoder.encode(String.format("/proceso/instancia?pin=%s", pin), "UTF-8"));
        //                }
        //            } catch (Exception e) {
        //                redirect.addFlashAttribute(AppConstants.FLASH_ERROR,
        //                        "Error estableciendo el mecanismo para selección de expediente");
        //                LOG.error("Error estableciendo el mecanismo para selección de expediente", e);
        //                return String.format("redirect:/proceso/instancia?pin=%s", pin);
        //            }
        //        }
        //</editor-fold>
        Instancia instanciaAntesDeEjecutarProceso = instanciaRepository.getOne(pinID);
        final Estado estadoIntanciaTMP = instanciaAntesDeEjecutarProceso.getEstado();
        final Usuario usuarioAsignadoTMP = instanciaAntesDeEjecutarProceso.getAsignado();

        /*
         * 2017-06-01 jgarcia@controltechcg.com Issue #99 (SICDI-Controltech)
         * hotfix-99: Validación del proceso de selección de usuario para
         * determinar si el usuario es null, para presentar el mensaje de error
         * correspondiente.
         */
        EstrategiaSeleccionUsuario selector = new EstrategiaSeleccionUsuario() {
            @Override
            public Usuario select(Instancia instanciaSeleccion, Documento documentoSeleccion) {

                /*
                 * 2017-02-06 jgarcia@controltechcg.com Issue# 150: Para
                 * asignación de Jefe, se busca la Dependencia Destino. Sobre
                 * esta se consulta si tiene un Jefe Segundo (Encargado). Si lo
                 * tiene se compara el rango de fechas asignado en la
                 * Dependencia. En caso que la fecha actual corresponda al
                 * rango, se selecciona el Jefe Segundo (Encargado). De lo
                 * contrario, a las reglas anteriormente descritas, se
                 * selecciona el Jefe de la Dependencia.
                 */
                Dependencia dependenciaDestino = documentoSeleccion.getDependenciaDestino();

                if (dependenciaDestino != null) {
                    /*
                     * 2017-02-09 jgarcia@controltechcg.com Issue #11
                     * (SIGDI-Incidencias01): En caso que el usuario en sesión
                     * corresponda como Jefe Segundo de la Dependencia Destino,
                     * se asigna el documento al Jefe principal de la
                     * Dependencia.
                     */
                    Usuario jefeActivo = dependenciaService.getJefeActivoDependencia(dependenciaDestino);
                    if (jefeActivo != null && usuarioSesion.getId().equals(jefeActivo.getId())) {
                        return dependenciaDestino.getJefe();
                    }
                    return jefeActivo;
                } else {
                    return usuarioSesion;
                }
            }
        };

        Instancia instanciaTMP = procesoService.instancia(pinID);
        String docIDTMP = instanciaTMP.getVariable(Documento.DOC_ID);
        Documento documentoTMP = documentRepository.getOne(docIDTMP);

        if (selector.select(instanciaTMP, documentoTMP) == null) {
            throw new BusinessLogicException("ERROR: No hay Jefe Asignado para la Dependencia destino.");
        }

        Instancia instancia = asignar(pinID, transicionID, selector);

        // Cambia a solo lectura
        instancia.setVariable(Documento.DOC_MODE, DocumentoMode.NAME_SOLO_LECTURA);

        // Obtiene el documento asociado a la instancia de proceso
        String docID = instancia.getVariable(Documento.DOC_ID);
        documento = documentRepository.getOne(docID);

        /*
         * Genera el PDF.
         *
         * 2018-04-11 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech)
         * feature-156: Retirar la opción dentro de la función
         * DocumentoController#firmar() que pregunta la selección del expediente
         * cuando este aún no ha sido asignado.
         */
        if (expedienteID != null && expedienteID > 0) {
            Expediente expediente = new Expediente();
            expediente.setId(expedienteID);
            documento.setExpediente(expediente);
        }

        //2017-03-01 edison.gonzalez@controltech.com Issue #151
        if (cargoIdFirma != null) {
            Cargo cargoFirma = new Cargo(cargoIdFirma);
            documento.setCargoIdFirma(cargoFirma);
        }

        documento.setFirma(usuarioSesion);

        /*
         * 2017-11-14 edison.gonzalez@controltechcg.com Issue #138: Se llama al
         * servicio encargado de retornar el numero de radicado, segun el tipo
         * de proceso.
         */
        Radicacion radicacion = radicacionRepository.findByProceso(documento.getInstancia().getProceso());
        documento.setRadicado(radicadoService.retornaNumeroRadicado(getSuperDependencia(usuarioSesion.getDependencia()).getId(), radicacion.getRadId()));

        /*
         * 2017-02-08 jgarcia@controltechcg.com Issue #94: Se corrige en los
         * puntos donde se hacen persistentes los documentos, para que siempre
         * se registre el usuario de la última accíón.
         */
        documento.setUsuarioUltimaAccion(usuarioSesion);

        documentRepository.saveAndFlush(documento);

        // EJECUTAMOS LA FUNCION PARA GENERAR LOS DATOS A CARGAR EN EL PDF
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withFunctionName("FN_PDF_RADIOGRAMA_MAIN");

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("P_DOC_ID", documento.getId())
                .addValue("P_USU_ID_FIRMA", usuarioSesion.getId())
                .addValue("P_DOC_RADICADO", documento.getRadicado());

        String plSqlResultado = simpleJdbcCall.executeFunction(String.class, in);

        if (!"OK".equals(plSqlResultado)) {
            // DEJAMOS EL DOC, COMO SU ESTADO ANTERIOR
            documento.setFirma(null);
            documento.setCargoIdFirma(null);
            documento.setRadicado(null);
            //2018-02-28 edison.gonzalez@controltechcg.com Issue #151.
            documento.setCargoIdFirma(null);
            instancia.setEstado(estadoIntanciaTMP);
            instancia.setAsignado(usuarioAsignadoTMP);

            documentRepository.save(documento);
            instanciaRepository.save(instancia);

            throw new BusinessLogicException("ERROR: Generando los datos a cargar al PDF: " + plSqlResultado);
        }

        try {
            KeysValuesAsposeDocxDTO asposeDocxDTO = DocumentoController.getKeysValuesWord(documentRepository.findPDFDocumento(documento.getId()), ofs.getRoot());

            Document documentAspose = new Document(ofs.getPath(documento.getDocx4jDocumento()));
            documentAspose.getMailMerge().execute(asposeDocxDTO.getNombres(), asposeDocxDTO.getValues());

            for (int indice = 0; indice < asposeDocxDTO.getNombres().length; indice++) {
                // TODO: Revisar el tema de la marca de agua...
                String valor = asposeDocxDTO.getNombres()[indice];

                if ("S_DEP_DESTINO".equalsIgnoreCase(valor) && asposeDocxDTO.getValues()[indice].toString().trim().length() > 0) {
                    // APLICAMOS LA MARCA DE AGUA, EN CASO LA TENGA
                    ofs.insertWatermarkText(documentAspose, asposeDocxDTO.getValues()[indice].toString().trim());
                    break;
                }

                /**
                 * 2017-09-29 edison.gonzalez@controltechcg.com Issue #129 : Se
                 * adiciona la marca de agua para documentos externos.
                 */
                if ("EXTERNO_MARCA_AGUA".equalsIgnoreCase(valor) && asposeDocxDTO.getValues()[indice].toString().trim().length() > 0) {
                    //APLICAMOS LA MARCA DE AGUA, EN CASO LA TENGA
                    ofs.insertWatermarkText(documentAspose, asposeDocxDTO.getValues()[indice].toString().trim());
                    break;
                }
            }

            File fTempSalidaPDF = File.createTempFile("_sigdi_temp_", ".pdf");
            documentAspose.save(fTempSalidaPDF.getPath());

            OFSEntry ofsEntry = ofs.readPDFAspose(fTempSalidaPDF);
            String pdfID = ofs.save(ofsEntry.getContent(), AppConstants.MIME_TYPE_PDF);

            try {
                fTempSalidaPDF.delete();
            } catch (Exception ex1) {
                LOG.error(pinID + "\t" + ex1.getMessage(), ex1);
                try {
                    fTempSalidaPDF.deleteOnExit();
                } catch (Exception ex2) {
                    LOG.error(pinID + "\t" + ex2.getMessage(), ex2);
                }
            }

            // SE ASIGNA EL USUARIO QUE FIRMA COMO EL ULTIMO
            documento.setUsuarioUltimaAccion(usuarioSesion);

            documento.setPdf(pdfID);
            documentRepository.save(documento);

        } catch (Exception ex) {
            LOG.error(pinID + "\t" + ex.getMessage(), ex);

            try {
                documento.setFirma(null);
                documento.setCargoIdFirma(null);
                documento.setRadicado(null);
                //2018-02-28 edison.gonzalez@controltechcg.com Issue #151.
                documento.setCargoIdFirma(null);
                instancia.setEstado(estadoIntanciaTMP);
                instancia.setAsignado(usuarioAsignadoTMP);

                documentRepository.save(documento);
                instanciaRepository.save(instancia);

                throw new BusinessLogicException("ERROR: Se generó un problema al firmar el documento: " + ex.getMessage(), ex);
            } catch (BusinessLogicException blex) {
                throw blex;
            } catch (Exception ex1) {
                throw new BusinessLogicException("ERROR: Se generó un problema al firmar el documento: " + ex1.getMessage(), ex1);
            }
        }

        /*
         * 2017-04-18 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech):
         * Se ejecuta el proceso de archivo automático, tras aplicar la
         * transición de firmar y enviar.
         */
        archivoAutomaticoService.archivarAutomaticamente(documento, null);
    }

    /**
     * Construye el mensaje completo de error a presentar en pantalla a partir
     * de los errores registrados en la validación.
     *
     * @param validation Validación realizada.
     * @return Mensaje completo de error.
     */
    private String construirMensajeError(final BusinessLogicValidation validation) {
        final String separator = " - ";

        final StringBuilder builder = new StringBuilder();

        for (int index = 0; index < validation.getNumberOfErrors(); index++) {
            final String message = validation.getError(index).getMessage();
            builder.append(message);

            if (index < (validation.getNumberOfErrors() - 1)) {
                builder.append(separator);
            }
        }

        return builder.toString();
    }

    /**
     * Marca la firma y avanza. Se remplaza por el metodo
     * {@link #firmarDocumento}
     *
     * @param pin
     * @param tid
     * @param expId
     * @param cargoIdFirma
     * @param model
     * @param principal
     * @param redirect
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/firmar-depreciado", method = RequestMethod.PATCH)
    public String firmar(@RequestParam("pin") String pin, @RequestParam("tid") Integer tid,
            @RequestParam(value = "expId", required = false) Integer expId, @RequestParam(value = "cargoIdFirma", required = false) Integer cargoIdFirma,
            Model model, Principal principal, RedirectAttributes redirect) {

        /*
         * 2018-04-11 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech)
         * feature-156: Retirar la opción dentro de la función
         * DocumentoController#firmar() que pregunta la selección del expediente
         * cuando este aún no ha sido asignado.
         */
        //<editor-fold defaultstate="collapsed" desc="feature-156">
        //        if (expId == null) {
        //            try {
        //                /*
        //                 * 2017-02-07 jgarcia@controltechcg.com Issue #47: Se modifica
        //                 * el redirect del formulario para que en el momento de dar clic
        //                 * al botón "Firmar y enviar" no haya invocación al formulario
        //                 * documento-seleccionar-expediente.ftl, sino que vaya
        //                 * directamente a la pantalla de firma.
        //                 */
        //                Instancia instancia = instanciaRepository.getOne(pin);
        //                Documento documento = documentRepository.findOneByInstanciaId(instancia.getId());
        //                Expediente expediente = documento.getExpediente();
        //
        //                if (expediente != null) {
        //                    expId = expediente.getId();
        //                }
        //
        //                if (expId == null) {
        //                    return String.format("redirect:/documento/seleccionar-expediente?returnUrl=%s&cancelUrl=%s",
        //                            URLEncoder.encode(String.format("/documento/firmar?pin=%s&tid=%d&cargoIdFirma=%d", pin, tid, cargoIdFirma), "UTF-8"),
        //                            URLEncoder.encode(String.format("/proceso/instancia?pin=%s", pin), "UTF-8"));
        //                }
        //            } catch (Exception e) {
        //                redirect.addFlashAttribute(AppConstants.FLASH_ERROR,
        //                        "Error estableciendo el mecanismo para selección de expediente");
        //                LOG.error("Error estableciendo el mecanismo para selección de expediente", e);
        //                return String.format("redirect:/proceso/instancia?pin=%s", pin);
        //            }
        //        }
        //</editor-fold>
        final Usuario yo = getUsuario(principal);

        Instancia instanciaAntesDeEjecutarProceso = instanciaRepository.getOne(pin);
        final Estado estadoIntanciaTMP = instanciaAntesDeEjecutarProceso.getEstado();
        final Usuario usuarioAsiganoTMP = instanciaAntesDeEjecutarProceso.getAsignado();

        /*
         * 2017-06-01 jgarcia@controltechcg.com Issue #99 (SICDI-Controltech)
         * hotfix-99: Validación del proceso de selección de usuario para
         * determinar si el usuario es null, para presentar el mensaje de error
         * correspondiente.
         */
        EstrategiaSeleccionUsuario selector = new EstrategiaSeleccionUsuario() {
            @Override
            public Usuario select(Instancia i, Documento d) {

                /*
                 * 2017-02-06 jgarcia@controltechcg.com Issue# 150: Para
                 * asignación de Jefe, se busca la Dependencia Destino. Sobre
                 * esta se consulta si tiene un Jefe Segundo (Encargado). Si lo
                 * tiene se compara el rango de fechas asignado en la
                 * Dependencia. En caso que la fecha actual corresponda al
                 * rango, se selecciona el Jefe Segundo (Encargado). De lo
                 * contrario, a las reglas anteriormente descritas, se
                 * selecciona el Jefe de la Dependencia.
                 */
                Dependencia dependenciaDestino = d.getDependenciaDestino();

                if (dependenciaDestino != null) {
                    /*
                     * 2017-02-09 jgarcia@controltechcg.com Issue #11
                     * (SIGDI-Incidencias01): En caso que el usuario en sesión
                     * corresponda como Jefe Segundo de la Dependencia Destino,
                     * se asigna el documento al Jefe principal de la
                     * Dependencia.
                     */
                    Usuario jefeActivo = dependenciaService.getJefeActivoDependencia(dependenciaDestino);
                    if (jefeActivo != null && yo.getId().equals(jefeActivo.getId())) {
                        return dependenciaDestino.getJefe();
                    }

                    return jefeActivo;
                } else {
                    return yo;
                }
            }
        };

        Instancia instanciaTmp = procesoService.instancia(pin);
        String docTmpId = instanciaTmp.getVariable(Documento.DOC_ID);
        Documento documentoTmp = documentRepository.getOne(docTmpId);

        if (selector.select(instanciaTmp, documentoTmp) == null) {
            redirect.addFlashAttribute(AppConstants.FLASH_ERROR,
                    "ERROR: No hay Jefe Asignado para la Dependencia destino.");
            return String.format("redirect:/proceso/instancia?pin=%s", pin);
        }

        Instancia i = asignar(pin, tid, selector);

        // Cambia a solo lectura
        i.setVariable(Documento.DOC_MODE, DocumentoMode.NAME_SOLO_LECTURA);

        // Obtiene el documento asociado a la instancia de proceso
        String docId = i.getVariable(Documento.DOC_ID);
        Documento doc = documentRepository.getOne(docId);

        /*
         * Genera el PDF.
         *
         * 2018-04-11 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech)
         * feature-156: Retirar la opción dentro de la función
         * DocumentoController#firmar() que pregunta la selección del expediente
         * cuando este aún no ha sido asignado.
         */
        if (expId != null && expId > 0) {
            Expediente exp = new Expediente();
            exp.setId(expId);
            doc.setExpediente(exp);
        }

        //2017-03-01 edison.gonzalez@controltech.com Issue #151
        if (cargoIdFirma != null) {
            Cargo cargoFirma = new Cargo(cargoIdFirma);
            doc.setCargoIdFirma(cargoFirma);
        }

        doc.setFirma(yo);

        /*
         * 2017-11-14 edison.gonzalez@controltechcg.com Issue #138: Se llama al
         * servicio encargado de retornar el numero de radicado, segun el tipo
         * de proceso.
         */
        Radicacion radicacion = radicacionRepository.findByProceso(doc.getInstancia().getProceso());
        doc.setRadicado(radicadoService.retornaNumeroRadicado(getSuperDependencia(yo.getDependencia()).getId(), radicacion.getRadId()));

        /*
         * 2017-02-08 jgarcia@controltechcg.com Issue #94: Se corrige en los
         * puntos donde se hacen persistentes los documentos, para que siempre
         * se registre el usuario de la última accíón.
         */
        Usuario uActualLogin = getUsuario(principal);
        doc.setUsuarioUltimaAccion(uActualLogin);

        documentRepository.saveAndFlush(doc);

        // EJECUTAMOS LA FUNCION PARA GENERAR LOS DATOS A CARGAR EN EL PDF
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withFunctionName("FN_PDF_RADIOGRAMA_MAIN");

        SqlParameterSource in = new MapSqlParameterSource().addValue("P_DOC_ID", doc.getId())
                .addValue("P_USU_ID_FIRMA", yo.getId()).addValue("P_DOC_RADICADO", doc.getRadicado());

        String resultado = simpleJdbcCall.executeFunction(String.class, in);

        if (!"OK".equals(resultado)) {

            // DEJAMOS EL DOC, COMO SU ESTADO ANTERIOR
            doc.setFirma(null);
            doc.setCargoIdFirma(null);
            doc.setRadicado(null);
            //2018-02-28 edison.gonzalez@controltechcg.com Issue #151.
            doc.setCargoIdFirma(null);
            i.setEstado(estadoIntanciaTMP);
            i.setAsignado(usuarioAsiganoTMP);

            documentRepository.save(doc);
            instanciaRepository.save(i);

            try {
                redirect.addFlashAttribute(AppConstants.FLASH_ERROR, resultado);
                LOG.error("Error al generar los datos a cargar al pdf.", new Exception(resultado));
                return String.format("redirect:/documento/seleccionar-expediente?returnUrl=%s&cancelUrl=%s",
                        URLEncoder.encode(String.format("/documento/firmar?pin=%s&tid=%d", pin, tid), "UTF-8"),
                        URLEncoder.encode(String.format("/proceso/instancia?pin=%s", pin), "UTF-8"));
            } catch (Exception e) {
                redirect.addFlashAttribute(AppConstants.FLASH_ERROR,
                        "Error estableciendo el mecanismo para selección de expediente");
                LOG.error("Error estableciendo el mecanismo para selección de expediente", e);
                return String.format("redirect:/proceso/instancia?pin=%s", pin);
            }
        }

        try {

            KeysValuesAsposeDocxDTO asposeDocxDTO = DocumentoController
                    .getKeysValuesWord(documentRepository.findPDFDocumento(doc.getId()), ofs.getRoot());

            Document documentAspose = new Document(ofs.getPath(doc.getDocx4jDocumento()));

            documentAspose.getMailMerge().execute(asposeDocxDTO.getNombres(), asposeDocxDTO.getValues());

            for (int indice = 0; indice < asposeDocxDTO.getNombres().length; indice++) {

                // TODO: Revisar el tema de la marca de agua...
                String valor = asposeDocxDTO.getNombres()[indice];

                if ("S_DEP_DESTINO".equalsIgnoreCase(valor)
                        && asposeDocxDTO.getValues()[indice].toString().trim().length() > 0) {
                    // APLICAMOS LA MARCA DE AGUA, EN CASO LA TENGA
                    ofs.insertWatermarkText(documentAspose, asposeDocxDTO.getValues()[indice].toString().trim());
                    break;
                }

                /*
                 * 2017-09-29 edison.gonzalez@controltechcg.com issue #129 : Se adiciona la marca de agua
                 * para documentos externos.
                 */
                if ("EXTERNO_MARCA_AGUA".equalsIgnoreCase(valor) && asposeDocxDTO.getValues()[indice].toString().trim().length() > 0) {
                    ofs.insertWatermarkText(documentAspose, asposeDocxDTO.getValues()[indice].toString().trim());//APLICAMOS LA MARCA DE AGUA, EN CASO LA TENGA
                    break;
                }
            }

            File fTempSalidaPDF = File.createTempFile("_sigdi_temp_", ".pdf");
            documentAspose.save(fTempSalidaPDF.getPath());

            OFSEntry entry = ofs.readPDFAspose(fTempSalidaPDF);

            String pdfId = ofs.save(entry.getContent(), AppConstants.MIME_TYPE_PDF);

            try {
                fTempSalidaPDF.delete();
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    fTempSalidaPDF.deleteOnExit();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

            // SE ASIGNA EL USUARIO QUE FIRMA COMO EL ULTIMO
            doc.setUsuarioUltimaAccion(getUsuario(principal));

            doc.setPdf(pdfId);
            documentRepository.save(doc);

        } catch (Exception e) {

            LOG.error(e.getMessage(), e);

            try {

                doc.setFirma(null);
                doc.setCargoIdFirma(null);
                doc.setRadicado(null);
                //2018-02-28 edison.gonzalez@controltechcg.com Issue #151.
                doc.setCargoIdFirma(null);
                i.setEstado(estadoIntanciaTMP);
                i.setAsignado(usuarioAsiganoTMP);

                documentRepository.save(doc);
                instanciaRepository.save(i);

                redirect.addFlashAttribute(AppConstants.FLASH_ERROR, "Error al firmar el documento, posible error.");

                return String.format("redirect:/documento/seleccionar-expediente?returnUrl=%s&cancelUrl=%s",
                        URLEncoder.encode(String.format("/documento/firmar?pin=%s&tid=%d", pin, tid), "UTF-8"),
                        URLEncoder.encode(String.format("/proceso/instancia?pin=%s", pin), "UTF-8"));
            } catch (Exception e1) {
                redirect.addFlashAttribute(AppConstants.FLASH_ERROR,
                        "Error estableciendo el mecanismo para selección de expediente");
                LOG.error("Error estableciendo el mecanismo para selección de expediente", e1);
                return String.format("redirect:/proceso/instancia?pin=%s", pin);
            }
        }

        /*
         * 2017-04-18 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech):
         * Se ejecuta el proceso de archivo automático, tras aplicar la
         * transición de firmar y enviar.
         */
        archivoAutomaticoService.archivarAutomaticamente(doc, null);

        /*
         * Issue #118
         *
         * 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
         * feature-78
         *
         * 2017-05-24 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech)
         * feature-73
         *
         * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
         * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
         */
        redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, i, "Asignado a "));

        return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
    }

    /**
     * Da el visto bueno
     *
     * @param pin
     * @param tid
     * @param model
     * @param principal
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/vistobueno", method = RequestMethod.GET)
    public String vistobueno(@RequestParam("pin") final String pin, @RequestParam("tid") Integer tid, Model model,
            Principal principal, RedirectAttributes redirect) {

        // Devuelve al usuario y marca el visto bueno
        Instancia i = asignar(pin, tid, new EstrategiaSeleccionUsuario() {
            @Override
            public Usuario select(Instancia i, Documento d) {
                /*
				 * Usuario yo = i.getAsignado(); d.setVistoBueno(yo);
				 * List<HProcesoInstancia> hinstancias =
				 * hprocesoInstanciaRepository.findById(pin, new
				 * Sort(Direction.DESC, "cuandoMod")); for (HProcesoInstancia h
				 * : hinstancias) { if (h.getAsignado().getId() != yo.getId())
				 * return h.getAsignado(); } return yo;
                 */
                // Usuario yo = i.getAsignado();
                Usuario crea = d.getElabora();
                d.setVistoBueno(crea);
                return crea;
            }
        });

        Usuario uActualLogin = getUsuario(principal);

        String docId = i.getVariable(Documento.DOC_ID);
        Documento doc = documentRepository.getOne(docId);
        doc.setUsuarioUltimaAccion(uActualLogin);
        doc.setVistoBueno(uActualLogin);
        documentRepository.saveAndFlush(doc);
        ajuntoRcustom.insertIntoDocumentoLogVistosBueno(doc.getId(), uActualLogin.getId(), new Date());
        // Cambia a solo lectura
        i.setVariable(Documento.DOC_MODE, DocumentoMode.NAME_SOLO_LECTURA);

        /*
         * Issue #118
         *
         * 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
         * feature-78
         *
         * 2017-05-24 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech)
         * feature-73
         *
         * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
         * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
         */
        redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, i, "Asignado a "));

        if (i.transiciones().size() > 0) {
            return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
        } else {
            return "redirect:/";
        }
    }

    /**
     * Archiva un documento
     *
     * @param pin
     * @param tid
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping(value = "/archivar", method = RequestMethod.GET)
    public String archivar(@RequestParam("pin") String pin, @RequestParam("tid") Integer tid, Model model,
            Principal principal) {

        Instancia i = procesoService.instancia(pin);

        Documento doc = documentRepository.findOneByInstanciaId(pin);
        Usuario usuario = getUsuario(principal);

        DocumentoDependencia docDep = new DocumentoDependencia();
        Trd trd = trdRepository.getOne(doc.getTrd().getId());

        docDep.setDependencia(usuario.getDependencia());
        docDep.setDocumento(doc);
        docDep.setTrd(trd);

        documentoDependenciaRepository.save(docDep);

        // Aplica el generador de variables
        documentoGeneradorVariables.generar(i);

        // Pone el documento en modo sólo lectura
        i.setVariable(Documento.DOC_MODE, DocumentoMode.NAME_SOLO_LECTURA);

        // Intenta avanzar en el proceso
        i.forward(tid);

        if (i.transiciones().size() > 0) {
            return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
        } else {
            return "redirect:/";
        }
    }

    /**
     * Anula un documento
     *
     * @param pin
     * @param model
     * @param tid
     * @param principal
     * @return
     */
    @RequestMapping(value = "/anular", method = RequestMethod.GET)
    public String anular(@RequestParam("pin") String pin, @RequestParam("tid") Integer tid, Model model,
            Principal principal) {
        Instancia i = procesoService.instancia(pin);

        // Aplica el generador de variables
        documentoGeneradorVariables.generar(i);

        // Pone el documento en modo sólo lectura
        i.setVariable(Documento.DOC_MODE, DocumentoMode.NAME_SOLO_LECTURA);

        // Intenta avanzar en el proceso
        i.forward(tid);

        if (i.transiciones().size() > 0) {
            return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
        } else {
            return "redirect:/";
        }
    }

    /**
     * Muestra el listado de funcionarios de la dependencia
     *
     * @param pin
     * @param tid
     * @param usuId
     * @param did
     * @param model
     * @param principal
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/dar-respuesta", method = RequestMethod.GET)
    public String darRespuesta(@RequestParam("pin") String pin,
            @RequestParam(value = "tid", required = false) Integer tid,
            @RequestParam(value = "usuId", required = false) final Integer usuId,
            @RequestParam(value = "did", required = false) Integer did, Model model, Principal principal,
            RedirectAttributes redirect) {
        /*
		 * 2017-03-14 jgarcia@controltechcg.com Issue #16 (SIGDI-Incidencias01):
		 * Se obtiene la información de la unidad a la que pertenece el usuario
		 * en sesión. Se cambia toda la funcionalidad actual de selección de
		 * usuario, por un árbol de selección de dependencias a partir de la
		 * unidad del usuario en sesión, reemplazando el template
		 * documento-dar-respuesta.ftl para que sea compatible con esta nueva
		 * funcionalidad.
         */

        model.addAttribute("pin", pin);
        model.addAttribute("tid", tid);

        if (usuId == null) {
            Instancia instancia = procesoService.instancia(pin);
            String docId = instancia.getVariable(Documento.DOC_ID);
            Documento documento = documentRepository.getOne(docId);

            model.addAttribute("usuarios", "USUARIOS");

            List<Dependencia> listaDependencias = new ArrayList<>(1);
            model.addAttribute("dependencias_arbol", listaDependencias);

            Usuario usuarioSesion = getUsuario(principal);
            Dependencia dependencia = usuarioSesion.getDependencia();
            Dependencia unidadDependencia = getSuperDependencia(dependencia);
            depsHierarchy(unidadDependencia);
            listaDependencias.add(unidadDependencia);

            if (did != null) {
                // 2017-10-05 edison.gonzalez@controltechcg.com Issue #131
                List<Usuario> usuarios = usuR.findByDependenciaAndActivoTrueOrderByGradoDesc(did);

                model.addAttribute("documento", documento);

                int pesoClasificacionDocumento = documento.getClasificacion().getId();

                for (Usuario usuarioSeleccionado : usuarios) {

                    if (usuarioSeleccionado.getClasificacion() == null
                            || usuarioSeleccionado.getClasificacion().getId() == null) {
                        usuarioSeleccionado.setMensajeNivelAcceso(" ****SIN CLSIFICACIÓN****");
                        usuarioSeleccionado.setRestriccionDocumentoNivelAcceso(true);
                    } else {
                        int pesoClasificacionUsuario = usuarioSeleccionado.getClasificacion().getId();
                        usuarioSeleccionado
                                .setMensajeNivelAcceso(" -" + usuarioSeleccionado.getClasificacion().getNombre());
                        usuarioSeleccionado.setRestriccionDocumentoNivelAcceso(
                                pesoClasificacionUsuario < pesoClasificacionDocumento);
                    }
                }

                model.addAttribute("lista_usuarios", usuarios);
            }

        } else {
            Instancia i = asignar(pin, tid, new EstrategiaSeleccionUsuario() {
                @Override
                public Usuario select(Instancia i, Documento d) {
                    return usuR.getOne(usuId);
                }
            });

            /*
             * 2017-03-14 jgarcia@controltechcg.com Issue #12
             * (SIGDI-Incidencias01): En el paso de asignar a cualquier usuario
             * de mi dependencia, tras la selección del usuario, se modifica la
             * funcionalidad para que se registre el usuario en sesión como el
             * último usuario que realizó acción sobre el documento, para que
             * este pueda ser visualizado en los datos del documento, en el
             * apartado "Proceso > Enviado por:".
             */
            Documento documento = documentRepository.findOne(i.getVariable(Documento.DOC_ID));
            Usuario usuarioSesion = getUsuario(principal);
            documento.setUsuarioUltimaAccion(usuarioSesion);
            documentRepository.saveAndFlush(documento);

            /*
             * 2017-05-30 jgarcia@controltechcg.com Issue #98
             * (SICDI-Controltech) hotfix-98: Corrección en texto de mensaje de
             * asignación de usuario a siguiente transición del documento.
             *
             * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
             * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
             */
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                    buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, i, "Asignado a "));

            if (i.transiciones().size() > 0) {
                return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
            } else {
                return "redirect:/";
            }
        }

        return "documento-dar-respuesta";
    }

    /**
     * Asigna al funcionario de la dependencia y luego hace la transición
     *
     * @param pin
     * @param tid
     * @param usuId
     * @param model
     * @param principal
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/dar-respuesta", method = RequestMethod.POST)
    public String darRespuesta(@RequestParam("pin") String pin, @RequestParam("tid") Integer tid,
            @RequestParam("usuId") Integer usuId, Model model, Principal principal, RedirectAttributes redirect) {

        Instancia instanciaOriginal = procesoService.instancia(pin);
        try {
            instanciaOriginal.forward(tid);

            String docID = instanciaOriginal.getVariable(Documento.DOC_ID);
            Documento documentoOriginal = documentRepository.getOne(docID);

            Usuario usuarioSesion = getUsuario(principal);
            Usuario usuarioAsignar = usuR.findOne(usuId);
            /*
			 * 2017-03-16 jgarcia@controltechcg.com Issue #16
			 * (SIGDI-Incidencias01): Se retira restriccion del proceso de
			 * creación del documento de respuesta, que no permitía asignar un
			 * usuario de una dependencia diferente a la del usuario en sesión.
             */

            instanciaOriginal.setAsignado(usuarioAsignar);

            Documento documentoNuevo = Documento.create();
            String asuntoNuevo = "RE: " + documentoOriginal.getAsunto();
            documentoNuevo.setAsunto(asuntoNuevo);
            documentoNuevo.setElabora(usuarioAsignar);
            documentoNuevo.setClasificacion(documentoOriginal.getClasificacion());

            if (StringUtils.isNotBlank(documentoOriginal.getRemitenteNombre())) {
                documentoNuevo.setDestinatarioNombre(documentoOriginal.getRemitenteNombre());
                documentoNuevo.setDestinatarioTitulo(documentoOriginal.getRemitenteTitulo());
                documentoNuevo.setDestinatarioDireccion(documentoOriginal.getRemitenteDireccion());
                documentoNuevo.setRemitenteNombre(documentoOriginal.getDestinatarioNombre());
                documentoNuevo.setRemitenteTitulo(documentoOriginal.getDestinatarioTitulo());
                documentoNuevo.setRemitenteDireccion(documentoOriginal.getDestinatarioDireccion());
            }

            if (documentoOriginal.getDependenciaRemitente() != null) {
                documentoNuevo.setDependenciaDestino(documentoOriginal.getDependenciaRemitente());
            }
            documentoNuevo.setFechaOficio(new Date());

            // Crea una nueva instancia del proceso de documentos internos con
            // los datos básico
            Integer procesoRespuestaIDOriginal = instanciaOriginal.getProceso().getRespuesta();
            if (procesoRespuestaIDOriginal == null) {
                throw new Exception(String.format("El proceso '%s' no tiene asociado un proceso de respuesta",
                        instanciaOriginal.getProceso()));
            }

            String instanciaNuevaID = procesoService.instancia(procesoRespuestaIDOriginal, usuarioAsignar);
            Instancia instanciaNueva = new Instancia();
            instanciaNueva.setId(instanciaNuevaID);

            Variable variableID = new Variable();
            variableID.setKey(Documento.DOC_ID);
            variableID.setValue(documentoNuevo.getId());

            Instancia instanciaTemporal = new Instancia();
            instanciaTemporal.setId(instanciaNuevaID);
            variableID.setInstancia(instanciaTemporal);
            variableRepository.save(variableID);

            documentoNuevo.setInstancia(instanciaNueva);
            documentoNuevo.setRelacionado(documentoOriginal.getId());
            documentoOriginal.setRelacionado(documentoNuevo.getId());

            documentRepository.save(documentoNuevo);

            // Aplica el generador de variables
            documentoGeneradorVariables.generar(instanciaOriginal);

            // Pone el documento en modo sólo lectura
            instanciaOriginal.setVariable(Documento.DOC_MODE, DocumentoMode.NAME_SOLO_LECTURA);

            // Intenta avanzar en el proceso
            instanciaOriginal.forward(tid);

            /*
             * 2017-02-10 jgarcia@controltechcg.com Issue #96: En caso que se
             * coloque el estado "En proceso de respuesta", el documento se
             * archiva.
             *
             * 2017-02-20 jgarcia@controltechcg.com Issue #138: Corrección para
             * que el archivado automático únicamente se realice para el proceso
             * interno.
             *
             * 2017-06-13 jgarcia@controltechcg.com Issue #102
             * (SICDI-Controltech) feature-102: Retiro de la lógica
             * correspondiente al archivo automático de documento original para
             * el usuario en sesión cuando este selecciona la transición "Dar
             * Respuesta" para el proceso de Generación de Documentos Internos,
             * solicitado en Febrero/2017.
             *
             * 2017-05-30 jgarcia@controltechcg.com Issue #98
             * (SICDI-Controltech) hotfix-98: Corrección en texto de mensaje de
             * asignación de usuario a siguiente transición del documento.
             *
             * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
             * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
             */
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                    buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, instanciaOriginal, "Asignado a "));

        } catch (Exception e) {
            e.printStackTrace();
            redirect.addFlashAttribute(AppConstants.FLASH_ERROR, "Ocurrió un error inesperado: " + e.getMessage());
        }

        if (instanciaOriginal.transiciones().size() > 0) {
            return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
        } else {
            return "redirect:/";
        }
    }

    /**
     * Muestra la pantalla de reasignación con las personas a las que se puede
     * reasignar la instancia de proceso
     *
     * @param pin
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping(value = "/reasignar", method = RequestMethod.GET)
    public String reasignar(@RequestParam("pin") String pin, Model model, Principal principal) {
        model.addAttribute("pin", pin);

        /*
		 * 2017-03-14 jgarcia@controltechcg.com Issue #17 (SIGDI-Incidencias01):
		 * Se valida si el usuario es jefe principal o jefe encargado de la
		 * dependencia destino del documento. En caso de ser así, se presentan
		 * las dependencias hermanas de dicha dependencia. El cambio aplica para
		 * todas las reasignaciones de la aplicación.
         */
        Usuario usuarioSesion = getUsuario(principal);
        Instancia instancia = procesoService.instancia(pin);
        Documento documento = documentRepository.findOne(instancia.getVariable(Documento.DOC_ID));
        Dependencia dependenciaDestino = documento.getDependenciaDestino();
        /*
		 * 2017-03-14 jgarcia@controltechcg.com Issue #20 (SIGDI-Incidencias01):
		 * Corrección para Proceso Externo, en la accíón de Reasignación, ya que
		 * los documentos de este proceso no tienen dependencia destino. En este
		 * caso, se determina como dependencia la correspondiente al usuario en
		 * sesión.
         */
        if (dependenciaDestino == null) {
            dependenciaDestino = usuarioSesion.getDependencia();
        }

        Usuario jefeActivoDependenciaDestino = dependenciaService.getJefeActivoDependencia(dependenciaDestino);
        /*
		 * 2017-03-14 jgarcia@controltechcg.com Issue #33 (SIGDI-Controltech):
		 * Corrección para el proceso de radicación de documentos, en caso que
		 * la dependencia destino no tenga un jefe activo se presente la
		 * información, en el proceso Reasignar, a partir de la super
		 * dependencia del usuario en sesión, o a su propia dependencia.
         */
        if (jefeActivoDependenciaDestino != null
                && jefeActivoDependenciaDestino.getId().equals(usuarioSesion.getId())) {
            List<Dependencia> dependenciasHermanas;

            Integer dependenciaPadreID = dependenciaDestino.getPadre();
            /*
			 * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS:
			 * Ordenamiento por peso. Modificación: variable y orden en que se
			 * presentan las dependencias.
             */
            if (dependenciaPadreID == null || dependenciaPadreID == 0) {
                dependenciasHermanas = dependenciaRepository.findByActivoAndPadreIsNull(true,
                        new Sort(Direction.ASC, "pesoOrden", "nombre"));
            } else {
                dependenciasHermanas = dependenciaRepository.findByActivoAndPadre(true, dependenciaPadreID,
                        new Sort(Direction.ASC, "pesoOrden", "nombre"));
            }

            model.addAttribute("dependenciasHermanas", dependenciasHermanas);
        } else {

            Dependencia dependenciaUsuario = usuarioSesion.getDependencia();
            Dependencia superDependenciaUsuario = getSuperDependencia(dependenciaUsuario);
            if (superDependenciaUsuario.getJefe().getId() == usuarioSesion.getId()) {
                // Determina si el usuario es un jefe de superdependencia.
                // Obtiene el listado de las dependencias que se encuentran
                // bajo
                // su
                // mando y el listado de otras superdependencias
                /*
				 * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS:
				 * Ordenamiento por peso. Modificación: variable y orden en que
				 * se presentan las dependencias.
                 */
                model.addAttribute("dependenciasHermanas", dependenciaRepository.findByActivoAndPadreIsNull(true,
                        new Sort(Direction.ASC, "pesoOrden", "nombre")));
            } else if (dependenciaUsuario.getJefe().getId() == usuarioSesion.getId()) {
                // Determina si el usuario es el jefe de su dependencia
                // Obtiene el listado de usuarios de su dependencia y las
                // dependencias hermanas
                /*
				 * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS:
				 * Ordenamiento por peso. Modificación: variable y orden en que
				 * se presentan las dependencias.
                 */
                model.addAttribute("dependenciasHermanas", dependenciaRepository.findByActivoAndPadre(true,
                        dependenciaUsuario.getPadre(), new Sort(Direction.ASC, "pesoOrden", "nombre")));
            } else {
                // El usuario no es jefe y por tanto sólo puede reasignar al
                // jefe de
                // su dependencia.
                model.addAttribute("dependencias", Collections.singletonList(dependenciaUsuario));
            }
        }
        return "documento-reasignar";
    }

    /**
     * Realiza la reasignación
     *
     * @param pin
     * @param depId
     * @param usuId
     * @param observacion
     * @param principal
     * @param model
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/reasignar", method = RequestMethod.POST)
    public String reasignar(@RequestParam("pin") String pin,
            @RequestParam(value = "depId", required = false) Integer depId,
            @RequestParam(value = "usuId", required = false) Integer usuId,
            @RequestParam(value = "observacion", required = false) String[] observacion, Principal principal,
            Model model, RedirectAttributes redirect) {
        Instancia instancia = null;
        if (usuId != null) {
            final Usuario u = usuR.getOne(usuId);
            instancia = asignarNoForward(pin, null, new EstrategiaSeleccionUsuario() {
                @Override
                public Usuario select(Instancia i, Documento d) {
                    return u;
                }
            });
        } else if (depId != null) {
            final Dependencia dep = dependenciaRepository.getOne(depId);
            final Usuario usuarioSesion = getUsuario(principal);

            /*
			 * 2017-03-30 jgarcia@controltechcg.com Issue #33
			 * (SIGDI-Controltech): Modificación en el proceso de estrategia de
			 * selección del usuario a reasignar para realizar una validación
			 * previa si la dependencia seleccionada tiene jefe activo o no, con
			 * el fin que en caso negativo la aplicación presente un mensaje al
			 * usuario sobre la situación.
             */
            EstrategiaSeleccionUsuario estrategiaSeleccion = new EstrategiaSeleccionUsuario() {

                @Override
                public Usuario select(Instancia i, Documento d) {
                    /*
					 * 2017-02-09 jgarcia@controltechcg.com Issue #11
					 * (SIGDI-Incidencias01): En caso que el usuario en sesión
					 * corresponda como Jefe Segundo de la Dependencia Destino,
					 * se asigna el documento al Jefe principal de la
					 * Dependencia.
                     */
                    Usuario jefeActivo = dependenciaService.getJefeActivoDependencia(dep);
                    if (jefeActivo != null && usuarioSesion.getId().equals(jefeActivo.getId())) {
                        return dep.getJefe();
                    }

                    return jefeActivo;
                }
            };

            // Se pasan los parámetros en null, ya que no son utilizados en este
            // punto.
            Usuario usuarioSeleccion = estrategiaSeleccion.select(null, null);
            if (usuarioSeleccion == null) {
                redirect.addFlashAttribute(AppConstants.FLASH_ERROR,
                        "Dependencia destino no tiene Jefe Activo asignado.");
                return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
            }

            instancia = asignarNoForward(pin, null, estrategiaSeleccion);
        }

        Documento doc = documentRepository.getOne(instancia.getVariable(Documento.DOC_ID));

        String obsTexto = null;
        for (String obsx : observacion) {
            if (StringUtils.isNotBlank(obsx)) {
                obsTexto = obsx;
                break;
            }
        }

        if (obsTexto != null && obsTexto.trim().length() > 0) {
            DocumentoObservacion obs = new DocumentoObservacion();
            obs.setTexto(obsTexto);
            obs.setDocumento(doc);
            dobRepository.save(obs);
        }

        // 2017-02-09 jgarcia@controltechcg.com Issue #11 (SIGDI-Incidencias01)
        redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Reasignado a: " + instancia.getAsignado());

        if (instancia.transiciones().size() > 0) {
            return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
        } else {
            return "redirect:/";
        }
    }

    /**
     * Obtiene el nombre y la descripción del documento
     *
     * @param docId Es el identificador del documento
     *
     * @return
     */
    @RequestMapping(value = "/get-info", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> getInfoDoc(@RequestParam("docId") String docId) {

        Documento doc = documentRepository.getOne(docId);

        Map<String, String> docInfo = new TreeMap<>();
        docInfo.put(doc.getAsunto(), doc.getDescripcion());

        return docInfo;

    }

    /**
     * Actualiza el nombre y la descripción del documento
     *
     * @param docId Es el identificador del documento
     * @param asunto Es el asunto del documento
     * @param desc Es la descripción del documento
     *
     *
     * @return
     */
    @RequestMapping(value = "/update-info", method = RequestMethod.POST)
    public String updateInfoDoc(@RequestParam("docId") String docId, @RequestParam("asunto") String asunto,
            @RequestParam("desc") String desc) {

        Documento doc = documentRepository.getOne(docId);
        doc.setAsunto(asunto);
        doc.setDescripcion(desc);
        documentRepository.save(doc);

        return String.format("redirect:%s/contenido?eid=%s", ExpedienteController.PATH, doc.getExpediente().getId());

    }

    /**
     * Mueve el documento a otro expediente
     *
     * @param docid Es el identificador del documento
     * @param expid El id del expediente al que se va a mover el documento
     *
     *
     * @return
     */
    @RequestMapping(value = "/mover", method = RequestMethod.GET)
    public String moveDoc(@RequestParam("docid") String docid, @RequestParam("expid") Integer expid) {

        Documento doc = documentRepository.getOne(docid);
        Expediente exp = expedienteRepository.getOne(expid);
        doc.setExpediente(exp);
        documentRepository.save(doc);

        return String.format("redirect:%s/contenido?eid=%s", ExpedienteController.PATH, doc.getExpediente().getId());

    }

    /**
     * ************ DEFINITIVO ***********************
     */
    /**
     * Realiza la transición asignando el jefe de dependencia
     *
     * @param pin
     * @param tid
     * @param model
     * @return
     */
    @RequestMapping(value = "/solicitud-revision", method = RequestMethod.GET)
    public String solicitudRevision(@RequestParam("pin") String pin, @RequestParam("tid") Integer tid, Model model) {
        Instancia instancia = instanciaModel(pin, model);
        Dependencia dependencia = instancia.getAsignado().getDependencia();
        /*
		 * 2017-02-09 jgarcia@controltechcg.com Issue #11 (SIGDI-Incidencias01):
		 * En caso que el usuario en sesión corresponda como Jefe Segundo de la
		 * Dependencia Destino, se asigna el documento al Jefe principal de la
		 * Dependencia.
         */
        Usuario jefeActivo = dependenciaService.getJefeActivoDependencia(dependencia);
        instancia.setAsignado(jefeActivo);
        transicion(instancia, tid, null);
        return "redirect:" + PATH + "?pin=" + pin;
    }

    /**
     * Obtiene la solicitud de asignación cíclica de documento y presenta el
     * formulario correspondiente al usuario.
     *
     * @param instanciaID ID de la instancia del proceso. Obligatorio.
     * @param transicionID ID de la transición aplicada. Obligatorio.
     * @param usuarioID ID del usuario seleccionado para asignar la continuación
     * del proceso.
     * @param dependenciaID ID de la dependencia seleccionada para la búsquda de
     * usuarios a asignar.
     * @param model Modelo de atributos.
     * @param principal Información de usuario en sesión.
     * @param redirectAttributes Atributos de redirección.
     * @return Nombre del template del formulario.
     */
    // 2017-05-19 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech)
    // feature-73.
    @RequestMapping(value = "/asignar-documento-ciclico", method = RequestMethod.GET)
    public String asignarDocumentoCiclico(@RequestParam(value = "pin") final String instanciaID,
            @RequestParam(value = "tid") final Integer transicionID,
            @RequestParam(value = "usuId", required = false) final Integer usuarioID,
            @RequestParam(value = "did", required = false) final Integer dependenciaID, Model model,
            Principal principal, RedirectAttributes redirectAttributes) {

        model.addAttribute("pin", instanciaID);
        model.addAttribute("tid", transicionID);

        Transicion transicion = transicionRepository.findOne(transicionID);
        model.addAttribute("transicion", transicion);

        final Usuario usuarioSesion = getUsuario(principal);

        if (usuarioID == null) {
            model.addAttribute("usuarios", "USUARIOS");

            Instancia instancia = procesoService.instancia(instanciaID);

            final String documentoID = instancia.getVariable(Documento.DOC_ID);
            Documento documento = documentRepository.getOne(documentoID);
            model.addAttribute("documento", documento);

            List<Dependencia> listaDependencias = new ArrayList<>(1);
            Dependencia dependenciaDestino = usuarioSesion.getDependencia();
            Dependencia superDependencia = getSuperDependencia(dependenciaDestino);
            depsHierarchy(superDependencia);
            listaDependencias.add(superDependencia);
            model.addAttribute("dependencias_arbol", listaDependencias);

            if (dependenciaID != null) {
                // 2017-10-05 edison.gonzalez@controltechcg.com Issue #131
                List<Usuario> usuarios = usuR.findByDependenciaAndActivoTrueOrderByGradoDesc(dependenciaID);

                int pesoClasificacionDocumento = documento.getClasificacion().getId();

                for (Usuario usuarioSeleccionado : usuarios) {

                    if (usuarioSeleccionado.getClasificacion() == null
                            || usuarioSeleccionado.getClasificacion().getId() == null) {
                        usuarioSeleccionado.setMensajeNivelAcceso(" ****SIN CLSIFICACIÓN****");
                        usuarioSeleccionado.setRestriccionDocumentoNivelAcceso(true);
                    } else {
                        int pesoClasificacionUsuario = usuarioSeleccionado.getClasificacion().getId();
                        usuarioSeleccionado
                                .setMensajeNivelAcceso(" -" + usuarioSeleccionado.getClasificacion().getNombre());
                        usuarioSeleccionado.setRestriccionDocumentoNivelAcceso(
                                pesoClasificacionUsuario < pesoClasificacionDocumento);
                    }
                }

                model.addAttribute("lista_usuarios", usuarios);
            }

        } else {
            Instancia instancia = asignar(instanciaID, transicionID, new EstrategiaSeleccionUsuario() {
                @Override
                public Usuario select(Instancia i, Documento d) {
                    return usuR.getOne(usuarioID);
                }
            });

            Documento documento = documentRepository.findOne(instancia.getVariable(Documento.DOC_ID));
            documento.setUsuarioUltimaAccion(usuarioSesion);
            documentRepository.saveAndFlush(documento);

            /*
             * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
             * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
             */
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                    buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, instancia, "Asignado a "));

            if (instancia.transiciones().size() > 0) {
                return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, instanciaID);
            } else {
                return "redirect:/";
            }
        }

        return "asignar-documento-ciclico";
    }

    /**
     * Obtiene la solicitud de dar respuesta para el proceso cíclico de un
     * documento y presenta el formulario correspondiente al usuario.
     *
     * @param instanciaID ID de la instancia del proceso. Obligatorio.
     * @param transicionID ID de la transición aplicada. Obligatorio.
     * @param procesoRespuestaID ID del proceso seleccionado para la
     * construcción del documento respuesta.
     * @param model Modelo de atributos.
     * @param principal Información de usuario en sesión.
     * @param redirectAttributes Atributos de redirección.
     * @return Nombre del template del formulario.
     */
    // 2017-05-19 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech)
    // feature-73.
    @RequestMapping(value = "/dar-respuesta-ciclico", method = RequestMethod.GET)
    public String darRespuestaCiclico(@RequestParam("pin") String instanciaID,
            @RequestParam("tid") Integer transicionID,
            @RequestParam(value = "proResp", required = false) Integer procesoRespuestaID, Model model,
            Principal principal, RedirectAttributes redirectAttributes) {

        final Instancia instanciaOriginal = procesoService.instancia(instanciaID);
        final Proceso procesoOriginal = instanciaOriginal.getProceso();

        if (procesoOriginal.getId().equals(Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS)
                && procesoRespuestaID == null) {
            return String.format("redirect:%s/seleccionar-proceso-respuesta-ciclico?pin=%s&tid=%d", PATH, instanciaID,
                    transicionID);
        }

        if (procesoOriginal.getId().equals(
                Proceso.ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA)) {
            procesoRespuestaID = Proceso.ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA;
        }

        try {
            instanciaOriginal.forward(transicionID);

            final String documentoID = instanciaOriginal.getVariable(Documento.DOC_ID);
            final Documento documentoOriginal = documentRepository.getOne(documentoID);

            /*
			 * 2017-06-07 jgarcia@controltechcg.com Issue #105
			 * (SICDI-Controltech) feature-73: Validación en generación de nuevo
			 * documento respuesta para evitar que se creen 2 ó más documentos
			 * asociados.
             */
            String asuntoNuevo = "RE: " + documentoOriginal.getAsunto();
            final String relacionadoId = documentoOriginal.getRelacionado();
            if (relacionadoId != null && !documentoOriginal.getRelacionado().trim().isEmpty()) {
                Documento documentoRelacionado = documentRepository.getOne(relacionadoId);
                if (documentoRelacionado != null) {
                    final String asuntoRelacionado = documentoRelacionado.getAsunto();
                    if (asuntoRelacionado != null && asuntoRelacionado.equals(asuntoNuevo)) {
                        redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR,
                                "ERROR: El documento ya cuenta con una respuesta asociada.");
                        return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, instanciaID);
                    }
                }
            }

            final Usuario usuarioSesion = getUsuario(principal);
            instanciaOriginal.setAsignado(usuarioSesion);

            Documento documentoNuevo = Documento.create();

            documentoNuevo.setAsunto(asuntoNuevo);
            documentoNuevo.setElabora(usuarioSesion);
            documentoNuevo.setClasificacion(documentoOriginal.getClasificacion());

            if (procesoOriginal.getId().equals(Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS)
                    && procesoRespuestaID.equals(Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS)) {
                if (StringUtils.isNotBlank(documentoOriginal.getRemitenteNombre())) {
                    documentoNuevo.setDestinatarioNombre(documentoOriginal.getRemitenteNombre());
                }

                if (StringUtils.isNotBlank(documentoOriginal.getRemitenteTitulo())) {
                    documentoNuevo.setDestinatarioTitulo(documentoOriginal.getRemitenteTitulo());
                }

                if (StringUtils.isNotBlank(documentoOriginal.getRemitenteDireccion())) {
                    documentoNuevo.setDestinatarioDireccion(documentoOriginal.getRemitenteDireccion());
                }

                if (StringUtils.isNotBlank(documentoOriginal.getDestinatarioNombre())) {
                    documentoNuevo.setRemitenteNombre(documentoOriginal.getDestinatarioNombre());
                }

                if (StringUtils.isNotBlank(documentoOriginal.getDestinatarioTitulo())) {
                    documentoNuevo.setRemitenteTitulo(documentoOriginal.getDestinatarioTitulo());
                }

                if (StringUtils.isNotBlank(documentoOriginal.getDestinatarioDireccion())) {
                    documentoNuevo.setRemitenteDireccion(documentoOriginal.getDestinatarioDireccion());
                }
            }

            if (documentoOriginal.getDependenciaRemitente() != null) {
                documentoNuevo.setDependenciaDestino(documentoOriginal.getDependenciaRemitente());
            }

            documentoNuevo.setFechaOficio(new Date());

            final String instanciaNuevaID = procesoService.instancia(procesoRespuestaID, usuarioSesion);
            Instancia instanciaNueva = new Instancia();
            instanciaNueva.setId(instanciaNuevaID);

            Variable variableID = new Variable();
            variableID.setKey(Documento.DOC_ID);
            variableID.setValue(documentoNuevo.getId());

            Instancia instanciaTemporal = new Instancia();
            instanciaTemporal.setId(instanciaNuevaID);
            variableID.setInstancia(instanciaTemporal);
            variableRepository.save(variableID);

            documentoNuevo.setInstancia(instanciaNueva);
            documentoNuevo.setRelacionado(documentoOriginal.getId());
            documentoOriginal.setRelacionado(documentoNuevo.getId());

            documentRepository.save(documentoNuevo);

            documentoGeneradorVariables.generar(instanciaOriginal);

            // Pone el documento en modo sólo lectura
            instanciaOriginal.setVariable(Documento.DOC_MODE, DocumentoMode.NAME_SOLO_LECTURA);

            instanciaOriginal.forward(transicionID);

            /*
             * 2017-02-10 jgarcia@controltechcg.com Issue #96: En caso que se
             * coloque el estado "En proceso de respuesta", el documento se
             * archiva.
             *
             * 2017-02-20 jgarcia@controltechcg.com Issue #138: Corrección para
             * que el archivado automático únicamente se realice para el proceso
             * interno.
             *
             * 2017-06-13 jgarcia@controltechcg.com Issue #102
             * (SICDI-Controltech) feature-102: Retiro de la lógica
             * correspondiente al archivo automático de documento original para
             * el usuario en sesión cuando este selecciona la transición "Dar
             * Respuesta" para el proceso de Generación de Documentos Internos,
             * solicitado en Febrero/2017.
             *
             * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
             * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
             */
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                    buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, instanciaOriginal,
                            "Documento respuesta \"" + asuntoNuevo + "\" creado. Asignado a: "));

        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "Excepción: " + ex.getMessage());
        }

        if (instanciaOriginal.transiciones().size() > 0) {
            return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, instanciaID);
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = {"/seleccionarDependencia"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public Integer seleccionarDependencia(Model model, Principal principal, HttpServletRequest req) {
        Usuario usuarioSesion = getUsuario(principal);
        Dependencia dependenciaDestino = usuarioSesion.getDependencia();
        return dependenciaDestino.getId();
    }

    /**
     * Obtiene la solicitud para la presentación de la pantalla de selección de
     * proceso para construir la respuesta de un proceso cíclico.
     *
     * @param instanciaID ID de la instancia del proceso. Obligatorio.
     * @param transicionID ID de la transición aplicada. Obligatorio.
     * @param model Modelo de atributos.
     * @param principal Información de usuario en sesión.
     * @return Nombre del template del formulario.
     */
    // 2017-05-22 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech)
    // feature-73.
    @RequestMapping(value = "/seleccionar-proceso-respuesta-ciclico", method = RequestMethod.GET)
    public String seleccionarProcesoRespuestaCiclico(@RequestParam("pin") String instanciaID,
            @RequestParam("tid") Integer transicionID, Model model, Principal principal) {
        final Instancia instancia = procesoService.instancia(instanciaID);
        final String documentoID = instancia.getVariable(Documento.DOC_ID);
        final Documento documento = documentRepository.findOne(documentoID);

        model.addAttribute("pin", instanciaID);
        model.addAttribute("tid", transicionID);
        model.addAttribute("documento", documento);

        List<Proceso> list = procesoRepository.findAll();
        List<Proceso> procesos = new ArrayList<>();
        for (Proceso proceso : list) {
            if (proceso.getActivo()
                    && !proceso.getId().equals(Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS)) {
                procesos.add(proceso);
            }
        }

        model.addAttribute("procesos", procesos);

        return "seleccionar-proceso-respuesta-ciclico";
    }

    /**
     * Obtiene la solicitud de reasignación cíclica de un documento y presenta
     * el formulario correspondiente al usuario.
     *
     * @param instanciaID ID de la instancia del proceso.
     * @param model Modelo de atributos.
     * @param principal Información de usuario en sesión.
     * @return Nombre del template del formulario.
     */
    // 2017-05-19 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech)
    // feature-73.
    @RequestMapping(value = "/reasignar-ciclico", method = RequestMethod.GET)
    public String reasignarCiclico(@RequestParam("pin") String instanciaID, Model model, Principal principal) {

        model.addAttribute("pin", instanciaID);

        final Instancia instancia = procesoService.instancia(instanciaID);
        final String documentoID = instancia.getVariable(Documento.DOC_ID);
        final Documento documento = documentRepository.findOne(documentoID);
        model.addAttribute("documento", documento);

        /*
	 * 2018-02-02 edison.gonzalez@controltechcg.com Issue #147: Validacion para que tenga en cuenta el
	 * campo Indicador de envio documentos.
         */
        List<Dependencia> unidades = depsHierarchyPadre();

        model.addAttribute("unidades", unidades);

        return "reasignar-ciclico";
    }

    /**
     * Obtiene la selección del usuario sobre la unidad a la cual será
     * reasignado el documento, realiza las validaciones correspondientes y
     * ejecuta el proceso de reasignación o presenta los mensajes de error,
     * según el éxito de la operación.
     *
     * @param instanciaID ID de la instancia del proceso.
     * @param dependenciaID ID de la dependencia seleccionada.
     * @param observaciones Observaciones.
     * @param principal Información de usuario en sesión.
     * @param model Modelo de atributos.
     * @param redirectAttributes Atributos de redirección.
     * @return Instrucciones de redirección del flujo, según las reglas de
     * negocio y validaciones aplicadas.
     */
    // 2017-05-22 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech)
    // feature-73.
    @RequestMapping(value = "/reasignar-ciclico", method = RequestMethod.POST)
    public String reasignarCiclico(@RequestParam("pin") String instanciaID,
            @RequestParam(value = "depId", required = false) Integer dependenciaID,
            @RequestParam(value = "observacion", required = false) String[] observaciones, Principal principal,
            Model model, RedirectAttributes redirectAttributes) {
        Instancia instancia = null;

        if (dependenciaID == null) {
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR, "Debe seleccionar una Unidad.");
            return String.format("redirect:%s/reasignar-ciclico?pin=%s", PATH, instanciaID);
        }

        final Dependencia dependenciaUnidad = dependenciaRepository.getOne(dependenciaID);
        final Usuario usuarioSesion = getUsuario(principal);

        EstrategiaSeleccionUsuario estrategiaSeleccion = new EstrategiaSeleccionUsuario() {

            @Override
            public Usuario select(Instancia instancia, Documento documento) {
                Usuario jefeActivo = dependenciaService.getJefeActivoDependencia(dependenciaUnidad);
                if (jefeActivo != null && usuarioSesion.getId().equals(jefeActivo.getId())) {
                    return dependenciaUnidad.getJefe();
                }

                return jefeActivo;
            }
        };

        // Se pasan los parámetros en null, ya que no son utilizados en este
        // punto.
        Usuario usuarioSeleccion = estrategiaSeleccion.select(null, null);
        if (usuarioSeleccion == null) {
            redirectAttributes.addFlashAttribute(AppConstants.FLASH_ERROR,
                    "Unidad destino " + dependenciaUnidad.getNombre() + "no tiene Jefe Activo asignado.");
            return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, instanciaID);
        }

        instancia = asignarNoForward(instanciaID, null, estrategiaSeleccion);

        Documento documento = documentRepository.getOne(instancia.getVariable(Documento.DOC_ID));

        String observacionesTexto = null;
        for (String observacion : observaciones) {
            if (StringUtils.isNotBlank(observacion)) {
                observacionesTexto = observacion;
                break;
            }
        }

        if (observacionesTexto != null && observacionesTexto.trim().length() > 0) {
            DocumentoObservacion observacion = new DocumentoObservacion();
            observacion.setTexto(observacionesTexto);
            observacion.setDocumento(documento);
            dobRepository.save(observacion);
        }

        /*
         * 2018-04-25 edison.gonzalez@controltechcg.com Issue #156
         * (SICDI-Controltech) feature-156 Reemplazo metodo depreciado
         */
        redirectAttributes.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                buildAsignadosTextMultidestino(multidestinoService, usuarioService, dependenciaService, instancia, "Reasignado a: "));

        if (instancia.transiciones().size() > 0) {
            return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, instanciaID);
        } else {
            return "redirect:/";
        }
    }

    /**
     * Aplica el proceso de anulación para un documento respuesta en estado de
     * construcción, regresando el documento original al estado correspondiente
     * al manejo de transiciones cíclicas.
     *
     * @param instanciaID ID de la instancia del proceso.
     * @param transicionID ID de la transición aplicada.
     * @param model Modelo de atributos.
     * @param principal Información de usuario en sesión.
     * @param redirectAttributes Atributos de redirección.
     * @return Instrucciones de redirección del flujo, según las reglas de
     * negocio y validaciones aplicadas.
     */
    // 2017-05-22 jgarcia@controltechcg.com Issue #93 (SICDI-Controltech)
    // feature-93.
    @RequestMapping(value = "/anular-respuesta-documento-ciclico", method = RequestMethod.GET)
    public String anularRespuestaDocumentoCiclico(@RequestParam("pin") String instanciaID,
            @RequestParam("tid") Integer transicionID, Model model, Principal principal,
            RedirectAttributes redirectAttributes) {
        final Instancia instanciaRespuesta = procesoService.instancia(instanciaID);
        final String documentoRespuestaID = instanciaRespuesta.getVariable(Documento.DOC_ID);
        final Documento documentoRespuesta = documentRepository.findOne(documentoRespuestaID);
        final String asuntoRespuesta = documentoRespuesta.getAsunto();

        final String documentoOriginalID = documentoRespuesta.getRelacionado();
        final Documento documentoOriginal = documentRepository.getOne(documentoOriginalID);
        final Instancia instanciaOriginal = documentoOriginal.getInstancia();
        final Proceso procesoOriginal = instanciaOriginal.getProceso();

        if (procesoOriginal.getId().equals(Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS)) {
            Estado estado = new Estado();
            estado.setId(Estado.REVISIÓN_JEFE_JEFATURA);
            instanciaOriginal.setEstado(estado);
        } else if (procesoOriginal.getId().equals(
                Proceso.ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA)) {
            Estado estado = new Estado();
            estado.setId(Estado.ENVIADO);
            instanciaOriginal.setEstado(estado);
        }

        documentoOriginal.setRelacionado(null);

        documentRepository.saveAndFlush(documentoOriginal);
        instanciaRepository.saveAndFlush(instanciaOriginal);
        instanciaRespuesta.forward(transicionID);

        redirectAttributes.addFlashAttribute(AppConstants.FLASH_SUCCESS,
                "El documento respuesta \"" + asuntoRespuesta + "\" ha sido anulado exitosamente.");

        return "redirect:/";
    }

    /*
	 * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS:
	 * Ordenamiento por peso. Modificación: variable y orden en que se presentan
	 * las dependencias.
     */
    private List<Dependencia> depsHierarchy() {
        List<Dependencia> root = dependenciaRepository.findByActivoAndPadreIsNull(true,
                new Sort(Direction.ASC, "pesoOrden", "nombre"));
        for (Dependencia d : root) {
            depsHierarchy(d);
        }
        return root;
    }

    /*
	 * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS:
	 * Ordenamiento por peso. Modificación: variable y orden en que se presentan
	 * las dependencias.
     */
    private void depsHierarchy(Dependencia d) {
        List<Dependencia> subs = dependenciaRepository.findByActivoAndPadre(true, d.getId(),
                new Sort(Direction.ASC, "pesoOrden", "nombre"));
        d.setSubs(subs);
        for (Dependencia x : subs) {
            depsHierarchy(x);
        }
    }

    private List<Dependencia> depsHierarchyPadre() {
        List<Dependencia> root = this.dependenciaRepository.findByActivoAndPadreIsNull(true, new Sort(Sort.Direction.ASC, new String[]{"pesoOrden", "nombre"}));
        for (Dependencia d : root) {
            depsHierarchyPadre(d);
        }
        return root;
    }

    private void depsHierarchyPadre(Dependencia d) {
        List<Dependencia> subs = this.dependenciaRepository.findByActivoAndPadreAndDepIndEnvioDocumentos(true, d.getId(), true, new Sort(Sort.Direction.ASC, new String[]{"pesoOrden", "nombre"}));

        d.setSubs(subs);
        for (Dependencia x : subs) {
            depsHierarchyPadre(x);
        }
    }

    private String redirectToInstancia(Instancia i) {
        if (i.transiciones().size() > 0) {
            return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, i.getId());
        } else {
            return "redirect:/";
        }
    }

    private Instancia asignar(String pin, Integer tid, EstrategiaSeleccionUsuario selector) {
        Instancia i = procesoService.instancia(pin);
        String docId = i.getVariable(Documento.DOC_ID);
        Documento doc = documentRepository.getOne(docId);

        // Aplica el generador de variables
        documentoGeneradorVariables.generar(i);

        // Intenta avanzar en el proceso
        boolean haAvanzado = (tid == null ? i.forward() : i.forward(tid));
        if (haAvanzado) {
            Usuario u = selector.select(i, doc);
            i.asignar(selector.select(i, doc));
        }

        return i;
    }

    private Instancia asignarNoForward(String pin, Integer tid, EstrategiaSeleccionUsuario selector) {

        Instancia i = procesoService.instancia(pin);
        String docId = i.getVariable(Documento.DOC_ID);
        Documento doc = documentRepository.getOne(docId);

        // Aplica el generador de variables
        documentoGeneradorVariables.generar(i);

        i.asignar(selector.select(i, doc));

        return i;
    }

    protected Dependencia getSuperDependencia(Dependencia dep) {
        /*
	 * 2018-01-30 edison.gonzalez@controltechcg.com Issue #147: Validacion para que tenga en cuenta el
	 * campo Indicador de envio documentos.
         */
        if (dep.getPadre() == null || (dep.getDepIndEnvioDocumentos() != null && dep.getDepIndEnvioDocumentos())) {
            return dep;
        }

        Dependencia jefatura = dep;
        Integer jefaturaId = dep.getPadre();
        while (jefaturaId != null) {
            jefatura = dependenciaRepository.getOne(jefaturaId);

            if (jefatura.getDepIndEnvioDocumentos() != null && jefatura.getDepIndEnvioDocumentos()) {
                return jefatura;
            }

            jefaturaId = jefatura.getPadre();
        }
        return jefatura;
    }

    /* --------------------------- privados -------------------------- */
    /**
     * Carga el listado de destinatarios al modelo
     *
     * @param model
     * @return
     */
    public List<Plantilla> plantillas(Model model) {
        // 2017-02-27 jgarcia@controltechcg.com Orden de plantillas por nombre.
        listaPlantilla = plantillaRepository.findByActivo(true, new Sort(Direction.ASC, "nombre"));
        model.addAttribute("plantillas", listaPlantilla);
        return listaPlantilla;
    }

    /**
     * Carga el listado de dependencias al modelo
     *
     * @param model
     * @return
     */
    /*
	 * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS:
	 * Ordenamiento por peso. Modificación: variable y orden en que se presentan
	 * las dependencias.
     */
    public List<Dependencia> dependencias(Model model) {
        List<Dependencia> list = dependenciaRepository.findByActivo(true,
                new Sort(Direction.ASC, "pesoOrden", "nombre"));
        model.addAttribute("dependencias", list);
        return list;
    }

    /**
     * Carga el listado de expedientes al modelo
     *
     * @param model
     * @param principal
     * @return
     */
    public List<Expediente> expedientes(Model model, Principal principal) {

        Dependencia dependencia = getUsuario(principal).getDependencia();

        if (dependencia == null) {
            return new ArrayList<Expediente>();
        }

        Integer dependenciaId = dependencia.getId();
        List<Expediente> list = expedienteRepository.findByActivoAndDependenciaId(true, dependenciaId,
                new Sort(Direction.ASC, "nombre"));
        model.addAttribute("expedientes", list);
        return list;
    }

    /* **************************** FIN REFORMA***************************** */
    @Value("${docweb.images.root}")
    public void setImagesRoot(String imagesRoot) {
        File file = new File(imagesRoot);
        if (file.exists() == false) {
            file.mkdir();
        }
        this.imagesRoot = imagesRoot;
    }

    @Value("${docweb.ofs.root}")
    public void setOfsRoot(String ofsRoot) {
        File file = new File(ofsRoot);
        if (file.exists() == false) {
            file.mkdir();
        }
        this.ofsRoot = ofsRoot;
    }

    /**
     * ******************** MODEL ATTRIBUTES ****************
     */
    /**
     * Carga el listado de tipologías
     *
     * @param trd
     * @param model
     */
    public void tipologias(Trd trd, Model model) {
        List<Tipologia> t = tipologiaRepository.findByActivo(true, new Sort(Direction.ASC, "nombre"));
        model.addAttribute("tipologias", t);
    }

    /**
     * Carga el listado de clasificaciones al modelo
     *
     * @return
     */
    @ModelAttribute("clasificaciones")
    public List<Clasificacion> clasificaciones() {
        return clasificacionRepository.findByActivo(true, new Sort(Direction.ASC, "orden"));
    }

    /**
     * 2017-09-29 edison.gonzalez@controltechcg.com Issue #129
     * (SICDI-Controltech) feature-129: Carga el listado de restricciones de
     * difusión
     *
     * @return
     */
    @ModelAttribute("restriccionesDifusion")
    public List<RestriccionDifusion> restriccionesDifusion() {
        return restriccionDifusionRepository.findByActivoTrue(new Sort(Direction.ASC, "resDescripcion"));
    }

    /**
     * Carga el listado de TRDs
     *
     * @return
     */
    @ModelAttribute("trds")
    public List<Trd> trds() {
        return trdRepository.findByActivoAndSerieNotNull(true, new Sort(Direction.ASC, "nombre"));
    }

    /**
     * Carga el listado de cargos del usuario en sesión.
     *
     * @param principal
     * @return
     */
    @ModelAttribute("cargosXusuario")
    public List<CargoDTO> cargosXusuario(Principal principal) {
        Usuario usuarioSesion = getUsuario(principal);
        List<Object[]> list = cargosRepository.findCargosXusuario(usuarioSesion.getId());
        List<CargoDTO> cargoDTOs = new ArrayList<>();
        for (Object[] os : list) {
            CargoDTO cargoDTO = new CargoDTO(((BigDecimal) os[0]).intValue(), (String) os[1]);
            cargoDTOs.add(cargoDTO);
        }
        return cargoDTOs;
    }

    /**
     * Pone el controlador
     *
     * @return
     */
    @ModelAttribute("controller")
    public DocumentoController controller() {
        return this;
    }

    /**
     * 2017-10-02 edison.gonzalez@controltechcg.com feature #129 : retorna el Id
     * del proceso externo
     *
     * @return
     */
    @ModelAttribute("procesoExternoId")
    public int retornaIdProcesoExterno() {
        return Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS;
    }

    /**
     * 2017-10-11 edison.gonzalez@controltechcg.com feature #129 : retorna el Id
     * del proceso de registro de documentos
     *
     * @return
     */
    @ModelAttribute("procesoRegistroDocumentos")
    public int retornaIdProcesoRegistroDocumentos() {
        return Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS;
    }

    /**
     * ********************* PRIVADOS *****************
     */
    private Instancia instanciaModel(String pin, Model model) {
        Instancia instancia = procesoService.instancia(pin);
        model.addAttribute("instancia", instancia);
        return instancia;
    }

    private Documento documentoModel(Model model, String docId) {
        Documento documento;
        if (StringUtils.isNotBlank(docId)) {
            documento = documentRepository.findOne(docId);
        } else {
            documento = new Documento();
        }
        model.addAttribute("documento", documento);
        return documento;
    }

    private void transicion(Instancia instancia, Integer tid, Map<String, String> params) {
        Transicion transicion = transicionRepository.findOne(tid);

        Estado estadoFinal = transicion.getEstadoFinal();
        instancia.setEstado(estadoFinal);

        if (params != null) {
            for (Entry<String, String> param : params.entrySet()) {
                String key = param.getKey();
                String value = param.getValue();
                Variable v = instancia.findVariable(key);
                if (v == null) {
                    v = new Variable();
                    v.setInstancia(instancia);
                    v.setKey(key);
                    instancia.getVariables().add(v);
                    variableRepository.save(v);
                }
                v.setValue(value);
            }
        }

        instanciaRepository.save(instancia);
    }

    @Override
    public String login(Integer id) {
        return super.login(id);
    }

    @Override
    public String nombre(Integer id) {
        return super.nombre(id);
    }

    public String mergePlantilla(String plantilla, Documento doc, Instancia i) {
        Map<String, Object> map = new HashMap<>();
        map.put("documento", doc);
        map.put("instancia", i);
        Locale locale = LocaleContextHolder.getLocale();
        return GeneralUtils.merge(plantilla, map, locale);
    }

    public String getIdPlantillaSeleccionada() {
        return idPlantillaSeleccionada;
    }

    public void setIdPlantillaSeleccionada(String idPlantillaSeleccionada) {
        this.idPlantillaSeleccionada = idPlantillaSeleccionada;
    }

    private static interface EstrategiaSeleccionUsuario {

        Usuario select(Instancia i, Documento d);
    }

    /**
     * Construye un texto de asignación del documento que presenta el nombre del
     * asignado primario y de los jefes de dependencia destino asociados.
     *
     * @param dependenciaAdicionalRepository Repositorio de persistencia para
     * las dependencias adicionales.
     * @param usuarioService Servicio para usuarios.
     * @param dependenciaService Servicio para dependencias.
     * @param instancia Instancia del proceso.
     * @param textoInicial Texto que se colocaría al inicio del resultado.
     * @param manejarMultiplesDestinos Indica si el texto debe manejar la
     * presentación de usuarios asignados según múltiples destinos.
     *
     * @return Texto que comienza con el valor de texto inicial (Si se ha
     * colocado alguno) seguido del nombre del asignado primario. En caso que el
     * documento de la instancia del proceso tenga asociados dependencias
     * destino, presentará en el mismo texto (separado por comas) los jefes de
     * cada dependencia.
     *
     * @deprecated 2018-04-16 jgarcia@controltechcg.com Issue #156
     * (SICDI-Controltech) feature-156 Se reemplaza por la entidad
     * {@link DependenciaCopiaMultidestino} y el método
     * {@link #buildAsignadosTextMultidestino(com.laamware.ejercito.doc.web.serv.DependenciaCopiaMultidestinoService, com.laamware.ejercito.doc.web.serv.UsuarioService, com.laamware.ejercito.doc.web.serv.DependenciaService, com.laamware.ejercito.doc.web.entity.Instancia, java.lang.String) }.
     */
    /*
     * 2017-02-06 jgarcia@controltechcg.com Issue #118 Presentación de jefes de
     * dependencias adicionales a un documento.
     *
     * 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
     * feature-78
     *
     * 2017-05-24 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech)
     * feature-73
     *
     * 2018-04-12 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech)
     * feature-156: Se agrega como parámetro la instancia de DependenciaService
     * para poder llamar el método getJefeActivoDependencia().
     */
    @Deprecated
    public static String buildAsignadosText(final DocumentoDependenciaAdicionalRepository dependenciaAdicionalRepository, final UsuarioService usuarioService, final DependenciaService dependenciaService,
            Instancia instancia, String textoInicial, boolean manejarMultiplesDestinos) {
        final String documentoID = instancia.getVariable(Documento.DOC_ID);

        String text = (textoInicial == null || textoInicial.trim().isEmpty()) ? "" : textoInicial;
        /*
         * 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
         * feature-78: Presentar información básica de los usuarios asignadores
         * y asignados en las bandejas del sistema.
         */
        text += usuarioService.mostrarInformacionBasica(instancia.getAsignado());

        /*
         * 2017-02-08 jgarcia@controltechcg.com Issue #118 Modificación para que
         * los jefes de dependencia destino únicamente se presenten cuando el
         * estado corresponde a Enviado.
         *
         * 2017-05-24 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech)
         * feature-73: Opción para indicar si la construcción del texto de
         * asignados debe manejar múltiples destinos o no.
         */
        if (!manejarMultiplesDestinos || instancia.getEstado().getId() != Estado.ENVIADO) {
            return text;
        }

        List<DocumentoDependenciaDestino> destinos = dependenciaAdicionalRepository.findByDocumento(documentoID);

        if (destinos.isEmpty()) {
            return text;
        }

        for (int i = 0; i < destinos.size(); i++) {
            DocumentoDependenciaDestino destino = destinos.get(i);
            // 2017-02-08 jgarcia@controltechcg.com Issue #118 Corrección en
            // caso que hayan dependencias sin jefe.
            Dependencia dependencia = destino.getDependencia();
            if (dependencia != null) {
                /*
                 * 2017-03-23 jgarcia@controltechcg.com Issue #29
                 * (SIGDI-Incidencias01): Corrección en la presentación del
                 * mensaje de asignación, para mostrar el jefe encargado cuando
                 * este esté activo.
                 */
                Usuario jefe = dependenciaService.getJefeActivoDependencia(dependencia);
                if (jefe != null) {
                    /*
                     * 2017-03-08 jgarcia@controltechcg.com Issue #6
                     * (SIGDI-Incidencias01): Corrección en presentación de
                     * información de destinatarios, para que salga el rango del
                     * jefe de la dependencia.
                     *
                     * 2017-05-15 jgarcia@controltechcg.com Issue #78
                     * (SICDI-Controltech) feature-78: Presentar información
                     * básica de los usuarios asignadores y asignados en las
                     * bandejas del sistema.
                     */
                    text += ", " + usuarioService.mostrarInformacionBasica(jefe);
                }
            }
        }

        return text;
    }

    /**
     * Construye el mensaje que indica la lista de usuarios asignados para la
     * aplicación de la próxima transición para la instancia según el proceso
     * asociado, utilizando la información de registros multidestino.
     *
     * @param copiaMultidestinoService Servicio de dependencias copia
     * multidestino.
     * @param usuarioService Servicio de usuarios.
     * @param dependenciaService Servicio de dependencias.
     * @param instancia Instancia del proceso.
     * @param textoInicial Texto inicial del mensaje de asignación.
     *
     * @return Objeto que almacena el valor de texto inicial (Si se ha colocado
     * alguno) seguido del nombre del asignado primario. En caso que el
     * documento de la instancia del proceso tenga asociados dependencias copias
     * multidestino, adicionara a una lista la información de los jefes de cada
     * dependencia.
     */
    /*
     * 2018-04-16 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech)
     * feature-156: Se crea como reemplazo del metodo buildAsignadosText() para
     * utilizar la información de los registros multidestino.
     *
     * 2018-04-25 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech)
     * feature-156: Se crea como reemplazo del metodo buildAsignadosText() para
     * utilizar la información de los registros multidestino.
     * Se cambia el objeto de retorno, para implementar los mensajes cuando se
     * realiza la firma de un documento con multidestino.
     */
    public static FlashAttributeValue buildAsignadosTextMultidestino(final DependenciaCopiaMultidestinoService copiaMultidestinoService, final UsuarioService usuarioService,
            final DependenciaService dependenciaService, final Instancia instancia, final String textoInicial) {
        /*
         * 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
         * feature-78: Presentar información básica de los usuarios asignadores
         * y asignados en las bandejas del sistema.
         */
        final String asignadoPrincipal = usuarioService.mostrarInformacionBasica(instancia.getAsignado());
        final String textoPrincipal = textoInicial + asignadoPrincipal;
        final FlashAttributeValue flashAttributeValue = new FlashAttributeValue(textoPrincipal, TITULO_FLASH_ASIGNADOS_COPIA_MULTIDESTINO);

        /*
         * 2017-02-08 jgarcia@controltechcg.com Issue #118 Modificación para que
         * los jefes de dependencia destino únicamente se presenten cuando el
         * estado corresponde a Enviado.
         *
         * 2017-05-24 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech)
         * feature-73: Opción para indicar si la construcción del texto de
         * asignados debe manejar múltiples destinos o no.
         */
        final Estado estado = instancia.getEstado();
        if (!estado.getId().equals(Estado.ENVIADO)) {
            return flashAttributeValue;
        }

        final String documentoID = instancia.getVariable(Documento.DOC_ID);
        final Documento documento = new Documento();
        documento.setId(documentoID);

        final List<DependenciaCopiaMultidestino> copiaMultidestinos = copiaMultidestinoService.listarActivos(documento);

        if (copiaMultidestinos.isEmpty()) {
            return flashAttributeValue;
        }

        final Map<Integer, Usuario> jefesMap = new LinkedHashMap<>();

        for (int i = 0; i < copiaMultidestinos.size(); i++) {
            final DependenciaCopiaMultidestino copiaMultidestino = copiaMultidestinos.get(i);

            // 2017-02-08 jgarcia@controltechcg.com Issue #118 Corrección en
            // caso que hayan dependencias sin jefe.
            final Dependencia dependencia = copiaMultidestino.getDependenciaDestino();
            if (dependencia != null) {
                /*
                 * 2017-03-23 jgarcia@controltechcg.com Issue #29
                 * (SIGDI-Incidencias01): Corrección en la presentación del
                 * mensaje de asignación, para mostrar el jefe encargado cuando
                 * este esté activo.
                 */
                final Usuario jefe = dependenciaService.getJefeActivoDependencia(dependencia);
                if (jefe != null) {
                    /*
                     * 2017-03-08 jgarcia@controltechcg.com Issue #6
                     * (SIGDI-Incidencias01): Corrección en presentación de
                     * información de destinatarios, para que salga el rango del
                     * jefe de la dependencia.
                     *
                     * 2017-05-15 jgarcia@controltechcg.com Issue #78
                     * (SICDI-Controltech) feature-78: Presentar información
                     * básica de los usuarios asignadores y asignados en las
                     * bandejas del sistema.
                     */
                    jefesMap.put(jefe.getId(), jefe);
                }
            }
        }

        final UsuarioGradoComparator usuarioGradoComparator = new UsuarioGradoComparator();
        final List<Usuario> jefes = new ArrayList<>(jefesMap.values());
        Collections.sort(jefes, usuarioGradoComparator);

        for (final Usuario jefe : jefes) {
            flashAttributeValue.addAltMessage(usuarioService.mostrarInformacionBasica(jefe));
        }

        return flashAttributeValue;
    }

    private List<Trd> trdsHierarchy(Usuario usuario) {
        List<Trd> trds = tRDService.findSeriesByUsuario(usuario);

        for (Trd trd : trds) {
            trdsHierarchy(trd,usuario);
        }
        return trds;
    }

    private void trdsHierarchy(Trd d, Usuario usuario) {
        List<Trd> subs = tRDService.findSubseriesbySerieAndUsuario(d, usuario);
        d.setSubs(subs);
        for (Trd x : subs) {
            trdsHierarchy(x, usuario);
        }
    }
}
