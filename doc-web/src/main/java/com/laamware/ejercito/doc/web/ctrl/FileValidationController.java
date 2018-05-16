package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.dto.FileValidationDTO;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.serv.FileValidationService;
import com.laamware.ejercito.doc.web.util.Global;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controlador para procesos de validación de archivos externos sobre archivos
 * del OFS del sistema.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 05/08/2018 Issue #160 (SICDI-Controltech) feature-160
 */
@Controller
@PreAuthorize("hasRole('VALIDAR_PDF_DOCFIRMAENVIO_UUID')")
@RequestMapping("/admin/file-validation")
public class FileValidationController extends UtilController {

    private static final Logger LOG = Logger.getLogger(FileValidationController.class.getName());

    private static final String TEMPLATE_NAME = "file-validation";

    @Autowired
    private FileValidationService validationService;

    /**
     * Obtiene la píldora activa.
     *
     * @return Píldora.
     */
    @ModelAttribute("activePill")
    public String getActivePill() {
        return TEMPLATE_NAME;
    }

    /**
     * Obtiene el prefijo del administrable.
     *
     * @return Prefijo.
     */
    @ModelAttribute("templatePrefix")
    protected String getTemplatePrefix() {
        return "admin";
    }

    /**
     * Presenta el template.
     *
     * @return Nombre del template.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String viewForm() {
        return TEMPLATE_NAME;
    }

    /**
     * Valida si el archivo cargado corresponde al archivo en el OFS, según el
     * UUID de firma y envío.
     *
     * @param archivoValidar Archivo cargado a validar.
     * @param docFirmaEnvioUUID UUID de firma y envío del documento a comparar.
     * @param model Modelo UI.
     * @return Nombre del template a presentar.
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String validate(@RequestParam("archivo_validar") final MultipartFile archivoValidar,
            @RequestParam("doc_firma_envio_uuid") final String docFirmaEnvioUUID, Model model) {
        try {
            model.addAttribute("doc_firma_envio_uuid", docFirmaEnvioUUID);

            final long size = archivoValidar.getSize();
            if (size == 0) {
                model.addAttribute(AppConstants.FLASH_ERROR, "Debe cargar un archivo PDF.");
                return TEMPLATE_NAME;
            }

            final String contentType = archivoValidar.getContentType();
            if (contentType.equals(Global.BINARY_FILE_CONTENT_TYPE)) {
                model.addAttribute(AppConstants.FLASH_ERROR, "ATENCIÖN: No se permiten cargar archivos binarios.");
                return TEMPLATE_NAME;
            }

            if (!contentType.equals(Global.PDF_FILE_CONTENT_TYPE)) {
                model.addAttribute(AppConstants.FLASH_ERROR, "Únicamente se permite cargar archivos PDF.");
                return TEMPLATE_NAME;
            }

            if (docFirmaEnvioUUID == null || docFirmaEnvioUUID.trim().isEmpty()) {
                model.addAttribute(AppConstants.FLASH_ERROR, "Debe ingresar el UUID de Firma y Envío del documento originado en SICDI.");
                return TEMPLATE_NAME;
            }

            final byte[] archivoValidarBytes = archivoValidar.getBytes();

            final FileValidationDTO fileValidation = validationService.isValid(archivoValidarBytes, docFirmaEnvioUUID);
            model.addAttribute("fileValidation", fileValidation);
            model.addAttribute(fileValidation.isValid() ? AppConstants.FLASH_SUCCESS : AppConstants.FLASH_ERROR, fileValidation.getMessage());
        } catch (IOException | RuntimeException ex) {
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            LOG.log(Level.SEVERE, docFirmaEnvioUUID, ex);
        }

        return TEMPLATE_NAME;
    }

}
