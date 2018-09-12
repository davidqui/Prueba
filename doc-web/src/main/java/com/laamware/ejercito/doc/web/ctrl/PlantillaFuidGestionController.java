package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.PlantillaFuidGestion;
import com.laamware.ejercito.doc.web.entity.PlantillaTransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.PlantillaFuidGestionService;
import com.laamware.ejercito.doc.web.serv.PlantillaTransferenciaArchivoService;
import java.io.IOException;
import java.security.Principal;
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
 * Controlador para la plantilla de transferencia de archivo.
 *
 * @author jgarcia@controltechcg.com
 * @since Ago 29, 2017
 * @version 1.0.0 (feature-120).
 */
@Controller
@PreAuthorize("hasRole('ADMIN_FUID_GESTION')")
@RequestMapping(PlantillaFuidGestionController.PATH)
public class PlantillaFuidGestionController extends UtilController {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(PlantillaFuidGestionController.class.getName());

    /**
     * Ruta raíz del controlador.
     */
    public static final String PATH = "/admin/plantilla-fuid";

    /**
     * Content type válido para las plantillas de carga.
     */
    private static final String VALID_CONTENT_TYPE
            = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    /**
     * Servicio de plantillas.
     */
    @Autowired
    private PlantillaFuidGestionService plantillaService;

    /**
     * Obtiene el descriptor de la entidad.
     *
     * @return Descriptor.
     */
    @ModelAttribute("descriptor")
    GenDescriptor getDescriptor() {
        final GenDescriptor descriptor = GenDescriptor.find(PlantillaFuidGestion.class);

        return descriptor;
    }

    /**
     * Obtiene la píldora activa.
     *
     * @return Píldora.
     */
    @ModelAttribute("activePill")
    public String getActivePill() {
        return "plantilla-fuid";
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
     * Presenta el formulario de registro de la plantilla.
     *
     * @param principal Objeto principal de A&A.
     * @param model Modelo de UI.
     * @return Nombre del template Freemarker del formulario.
     */
    @PreAuthorize("hasRole('ADMIN_FUID_GESTION')")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String presentarFormularioRegistroGET(Principal principal, Model model) {
        final PlantillaFuidGestion plantilla = plantillaService.findPlantillaActiva();
        model.addAttribute("plantilla", plantilla);

        return "plantilla-fuid";
    }

    /**
     * Valida y procesa el formulario de registro de la plantilla.
     *
     * @param file Archivo cargado.
     * @param principal Objeto principal de A&A.
     * @param model Modelo de UI.
     * @return Nombre del template Freemarker del formulario.
     */
    @PreAuthorize("hasRole('ADMIN_FUID_GESTION')")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String procesarFormularioRegistroPOST(@RequestParam(value = "file") MultipartFile file,
            Principal principal, Model model) {

        if (file.getSize() == 0) {
            model.addAttribute(AppConstants.FLASH_ERROR,"Debe seleccionar un archivo de plantilla (*.docx).");
            return presentarFormularioRegistroGET(principal, model);
        }

        if (!file.getContentType().equals(VALID_CONTENT_TYPE)) {
            model.addAttribute(AppConstants.FLASH_ERROR,"Únicamente se permiten archivo Word (*.docx) como plantilla.");
            return presentarFormularioRegistroGET(principal, model);
        }

        final Usuario usuario = getUsuario(principal);
        
        final PlantillaFuidGestion nuevaPlantilla;
        try {
            nuevaPlantilla = plantillaService.registrarNuevaPlantilla(file, usuario);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, "Excepción al almacenar el archivo en el OFS: " + ex.getMessage());
            return presentarFormularioRegistroGET(principal, model);
        }

        model.addAttribute("plantilla", nuevaPlantilla);
        model.addAttribute(AppConstants.FLASH_SUCCESS,"Se ha actualizado la plantilla de FUID a: "+ nuevaPlantilla.getNombreArchivo() + ".");

        return "plantilla-fuid";
    }
}
