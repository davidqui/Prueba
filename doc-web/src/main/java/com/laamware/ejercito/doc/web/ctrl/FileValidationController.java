package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.serv.FileValidationService;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("/file-validation")
public class FileValidationController {

    private static final Logger LOG = Logger.getLogger(FileValidationController.class.getName());

    @Autowired
    private FileValidationService validationService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String viewForm() {
        return "file-validator";
    }

    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public String validate(@RequestParam("archivo_validar") final MultipartFile archivoValidar, @RequestParam("doc_firma_envio_uuid") final String docFirmaEnvioUUID,
            Model model) {
        try {
            final byte[] archivoValidarBytes = archivoValidar.getBytes();
            boolean valid = validationService.isValid(archivoValidarBytes, docFirmaEnvioUUID);
            model.addAttribute("valid", valid);
        } catch (IOException | BusinessLogicException | RuntimeException ex) {
             model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            // TODO: Enviar datos de error en el Flash Attributes.
            LOG.log(Level.SEVERE, null, ex);
        }

        return "file-validator";
    }

}
