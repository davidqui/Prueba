package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.PlantillaTransferenciaArchivo;
import com.laamware.ejercito.doc.web.repo.PlantillaTransferenciaArchivoRepository;
import java.security.Principal;
import java.util.Date;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controlador para la plantilla de transferencia de archivo.
 *
 * @author jgarcia@controltechcg.com
 * @since Ago 29, 2017
 * @version 1.0.0 (feature-120).
 */
@Controller
@PreAuthorize("hasRole('ADMIN_PLANTILLA_TRANSF_ARCHIVO')")
@RequestMapping(PlantillaTransferenciaArchivoController.PATH)
public class PlantillaTransferenciaArchivoController extends UtilController {

    /**
     * Logger.
     */
    private static final Logger LOG
            = Logger.getLogger(PlantillaTransferenciaArchivoController.class.getName());

    /**
     * Ruta raíz del controlador.
     */
    public static final String PATH = "/admin/plantilla-transf-archivo";

    /**
     * Repositorio de plantillas.
     */
    @Autowired
    private PlantillaTransferenciaArchivoRepository plantillaRepository;

    /**
     * Obtiene el descriptor de la entidad.
     *
     * @return Descriptor.
     */
    @ModelAttribute("descriptor")
    GenDescriptor getDescriptor() {
        final GenDescriptor descriptor
                = GenDescriptor.find(PlantillaTransferenciaArchivo.class);

        return descriptor;
    }

    /**
     * Obtiene la píldora activa.
     *
     * @return Píldora.
     */
    @ModelAttribute("activePill")
    public String getActivePill() {
        return "plantilla-transf-archivo";
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
    @PreAuthorize("hasRole('ADMIN_PLANTILLA_TRANSF_ARCHIVO')")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String presentarFormularioRegistroGET(Principal principal, Model model) {
//        final PlantillaTransferenciaArchivo plantilla
//                = plantillaRepository.findByActivoTrue();

        PlantillaTransferenciaArchivo plantilla = new PlantillaTransferenciaArchivo(Boolean.TRUE, "Archivo.docx", 479234782, "MD%9872498745", "asjkdhajksdhaksj", getUsuario(principal), new Date());
        model.addAttribute("plantilla", plantilla);

        return "plantilla-transf-archivo";
    }
}
