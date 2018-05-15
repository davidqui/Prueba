package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.DocumentoActaService;
import com.laamware.ejercito.doc.web.serv.ProcesoService;
import java.security.Principal;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para proceso documental de "Registro de Acta".
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/15/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
@Controller
@RequestMapping(DocumentoActaController.PATH)
public class DocumentoActaController extends UtilController {

    private static final Logger LOG = Logger.getLogger(DocumentoActaController.class.getName());

    /**
     * Ruta principal.
     */
    static final String PATH = "/documento-acta";

    @Autowired
    private DocumentoActaService actaService;

    @Autowired
    private ProcesoService procesoService;

    /**
     * Procesa el documento de acta, según la transición a aplicar.
     *
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @param uiModel Modelo de UI.
     * @param redirectAttributes Atributos de redirección.
     * @param principal Información de sesión.
     * @return Nombre del template a presentar en la UI.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String procesar(@RequestParam("pin") final String procesoInstanciaID, Model uiModel, RedirectAttributes redirectAttributes, Principal principal) {
        LOG.info("com.laamware.ejercito.doc.web.ctrl.DocumentoActaController.procesar()");

        final String template = "documento-acta";

        final Usuario usuarioSesion = getUsuario(principal);
        final boolean tieneAccesoPorAsignacion = actaService.verificaAccesoDocumentoActa(usuarioSesion, procesoInstanciaID);
        if (!tieneAccesoPorAsignacion) {
            return "security-denied";
        }

        Instancia procesoInstancia = procesoService.instancia(procesoInstanciaID);

        final boolean tieneAccesoPorClasificacion = actaService.tieneAccesoPorClasificacion(usuarioSesion, procesoInstancia);
        if (!tieneAccesoPorClasificacion) {
            return "redirect:/documento/acceso-denegado";
        }

        final String documentoID = procesoInstancia.getVariable(Documento.DOC_ID);

        final Documento documentoActa;
        if (documentoID == null) {
            documentoActa = actaService.crearDocumentoActa(procesoInstancia, usuarioSesion);
            procesoInstancia = procesoService.instancia(procesoInstanciaID);
        } else {
            documentoActa = actaService.buscarDocumentoActa(documentoID);
        }

        uiModel.addAttribute("documentoActa", documentoActa);
        uiModel.addAttribute("procesoInstancia", procesoInstancia);
        uiModel.addAttribute("usuarioSesion", usuarioSesion);

        return template;
    }
}
