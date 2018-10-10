package com.laamware.ejercito.doc.web.ctrl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.Principal;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.laamware.ejercito.doc.web.dto.KeysValuesAsposeDocxDTO;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.serv.OFS;
import com.laamware.ejercito.doc.web.serv.OFSEntry;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

@Controller
@RequestMapping(value = "/ofs")
public class OFSController extends UtilController {

    private static final Logger LOG = LoggerFactory.getLogger(OFSController.class);

    @Autowired
    OFS ofs;

    @Autowired
    private DataSource dataSource;

    @Autowired
    DocumentoRepository documentRepository;
    
    /**
    * feature-gogs-15 samuel.delgado@controltechcg.com (SIGDI-CONTROLTECH): parametro que 
    * especifica el inicio de la linea de mando
    */
    @Value("${com.mil.imi.sicdi.linea.mando.inicio}")
    private String inicioLineaMando;

    private final String F = "zzzz1234567890abcdefghijklmnopqrstuvwxyz";

    @RequestMapping(value = "/downloaddocxtopdf/{id}/{iddoc}", method = RequestMethod.GET)
    public void downloaddocxtopdf(
            @PathVariable("id") String id,
            @PathVariable("iddoc") String iddoc,
            HttpServletResponse resp) {

        ServletOutputStream os = null;
        ByteArrayInputStream is = null;
        try {

            OFSEntry entry = null;

            File file = new File(ofs.getPath(id));
            if (!file.exists()) {
                entry = ofs.read(F);
            } else {
                //LLAMAMOS LA FUNCION QUE ACTUALIZA LOS DATOS				
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                jdbcTemplate.setResultsMapCaseInsensitive(true);

                SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withFunctionName("FN_PDF_DOC_PREVIEW");

                /**
                 * feature-gogs-15 samuel.delgado@controltechcg.com (SIGDI-CONTROLTECH): se 
                 * agrega parametro para laparte inicial de la linea de mando
                 */
                Map<String, Object> mapParams = new HashMap<>();
                mapParams.put("P_DOC_ID", iddoc);
                mapParams.put("P_LINEA_MANDO_INICIO", inicioLineaMando);
                SqlParameterSource in = new MapSqlParameterSource().addValues(mapParams);
                simpleJdbcCall.executeFunction(String.class, in);

                KeysValuesAsposeDocxDTO asposeDocxDTO = DocumentoController.getKeysValuesWord(documentRepository.findPDFDocumento(iddoc), ofs.getRoot());

                com.aspose.words.Document documentAspose = new com.aspose.words.Document(ofs.getPath(id));

                documentAspose.getMailMerge().execute(asposeDocxDTO.getNombres(), asposeDocxDTO.getValues());

                for (int indice = 0; indice < asposeDocxDTO.getNombres().length; indice++) {

                    String valor = asposeDocxDTO.getNombres()[indice];

                    if ("S_DEP_DESTINO".equalsIgnoreCase(valor) && asposeDocxDTO.getValues()[indice].toString().trim().length() > 0) {
                        ofs.insertWatermarkText(documentAspose, asposeDocxDTO.getValues()[indice].toString().trim());//APLICAMOS LA MARCA DE AGUA, EN CASO LA TENGA
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

                File fTempSalidaPDF = File.createTempFile("_sigdi_aspose_", ".pdf");
                documentAspose.save(fTempSalidaPDF.getPath());

                entry = ofs.readPDFAspose(fTempSalidaPDF);

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
            }

            byte[] content = entry.getContent();
            resp.setContentLength((int) content.length);
            resp.setContentType(entry.getContentType());
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s.pdf\"", id);
            resp.setHeader(headerKey, headerValue);

            // Write response
            os = resp.getOutputStream();
            is = new ByteArrayInputStream(content);
            IOUtils.copy(is, os);

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Error metodo downloaddocxtopdf: ", e);
        }
    }

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
            String headerValue = String.format("attachment; filename=\"%s.pdf\"", id);
            resp.setHeader(headerKey, headerValue);

            // Write response
            os = resp.getOutputStream();
            is = new ByteArrayInputStream(content);
            IOUtils.copy(is, os);

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Error metodo download: ", e);
        }
    }

    @RequestMapping(value = "/download-as-is/{id}", method = RequestMethod.GET)
    public void downloadAsIs(@PathVariable("id") String id, HttpServletResponse resp) {

        ServletOutputStream os = null;
        ByteArrayInputStream is = null;
        try {
            OFSEntry entry = ofs.read(id);
            byte[] content = entry.getContent();
            resp.setContentLength((int) content.length);
            resp.setContentType(entry.getContentType());
            String headerKey = "Content-Disposition";
            String headerValue = String.format(
                    "attachment; filename=\"%s\"", id);
            resp.setHeader(headerKey, headerValue);

            // Write response
            os = resp.getOutputStream();
            is = new ByteArrayInputStream(content);
            IOUtils.copy(is, os);

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Error metodo downloadAsIs: ", e);
        }
    }
       
    @RequestMapping(value = "/download/tmb/{id}", method = RequestMethod.GET)
    public void downloadTmb(@PathVariable("id") String id, HttpServletResponse resp) {

        ServletOutputStream os = null;
        ByteArrayInputStream is = null;
        try {
            byte[] content = ofs.readThumbnail(id);
            resp.setContentLength((int) content.length);
            resp.setContentType("image/gif");
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s.gif\"", id);
            resp.setHeader(headerKey, headerValue);

            // Write response
            os = resp.getOutputStream();
            is = new ByteArrayInputStream(content);
            IOUtils.copy(is, os);

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Error metodo downloadTmb: ", e);
        }
    }
    
    /**
     * Permite visualizar la vista previa de un archivo pdf
     * 
     * 2018-10-10 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 
     * @param id Codigo de ubicación del Archivo
     * @param resp 
     */
    @RequestMapping(value = "/download/tmb-static/{id}", method = RequestMethod.GET)
    public void downloadTmbStatic(@PathVariable("id") String id, HttpServletResponse resp) {

        ServletOutputStream os = null;
        ByteArrayInputStream is = null;
        try {
            byte[] content = ofs.readThumbnail(id,ofs.getMultimediaRoute());
            resp.setContentLength((int) content.length);
            resp.setContentType("image/gif");
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s.gif\"", id);
            resp.setHeader(headerKey, headerValue);

            // Write response
            os = resp.getOutputStream();
            is = new ByteArrayInputStream(content);
            IOUtils.copy(is, os);

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Error metodo downloadTmb: ", e);
        }
    }

    @RequestMapping(value = "/viewer", method = RequestMethod.GET)
    public String viewer() {
        return "pdf-viewer";
    }

    @RequestMapping(value = "/viewerODF", method = RequestMethod.GET)
    public String webodfViewer() {
        return "webodf-viewer";
    }

    @RequestMapping(value = "/upload-stage", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> uploadStage(
            @RequestParam(value = "stageId", required = false) String stageId,
            @RequestParam(value = "ref", required = false) String refId,
            @RequestParam("binario") MultipartFile archivo, Model model,
            Principal principal, HttpServletRequest req) {

        if (!archivo.isEmpty()) {
            try {
                Map<String, Object> response = ofs.save(archivo.getBytes(),
                        archivo.getContentType(), stageId, refId,
                        getUsuario(principal));
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("Error metodo uploadStage: ", e);
            }
        }
        return null;
    }

}
