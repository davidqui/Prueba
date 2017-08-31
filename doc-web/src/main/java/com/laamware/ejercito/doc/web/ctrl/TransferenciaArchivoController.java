package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.dto.TransferenciaArchivoValidacionDTO;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import com.laamware.ejercito.doc.web.serv.TransferenciaArchivoService;
import java.security.Principal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador para el proceso de transferencia de archivo.
 *
 * @author jgarcia@controltechcg.com
 * @since Ago 28, 2017
 * @version 1.0.0 (feature-120).
 */
@Controller
@RequestMapping(TransferenciaArchivoController.PATH)
public class TransferenciaArchivoController extends UtilController {

    /**
     * Logger.
     */
    private static final Logger LOG
            = Logger.getLogger(TransferenciaArchivoController.class.getName());

    /**
     * Ruta raíz del controlador.
     */
    public static final String PATH = "/transferencia-archivo";

    /**
     * Servicio de transferencia de archivo.
     */
    @Autowired
    private TransferenciaArchivoService transferenciaService;

    /**
     * Repositorio de usuarios.
     */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Presenta el formulario de creación para el proceso de transferencia de
     * archivo.
     *
     * @param principal Objeto principal de A&A.
     * @param model Modelo de UI.
     * @return Nombre del template Freemarker del formulario.
     */
    @RequestMapping(value = "/crear", method = RequestMethod.GET)
    public String presentarFormularioCreacionGET(Principal principal, Model model) {
        final Usuario origenUsuario = getUsuario(principal);
        model.addAttribute("origenUsuario", origenUsuario);

        final List<TransferenciaArchivo> transferenciasRecibidas
                = transferenciaService.findAllRecibidasActivasByDestinoUsuario(origenUsuario.getId());
        model.addAttribute("transferenciasRecibidas", transferenciasRecibidas);

        return "transferencia-archivo-crear";
    }

    /**
     * Valida el formulario de creación de la transferencia. En caso exitoso,
     * redirecciona al formulario de confirmación; de lo contrario, redirecciona
     * al formulario de creación, indicando los errores en la operación.
     *
     * @param origenUsuarioID ID del usuario de origen de la transferencia.
     * @param destinoUsuarioID ID del usuario de destino de la transferencia.
     * @param tipoTransferencia Identificador del tipo de la transferencia
     * realizada.
     * @param transferenciaAnteriorID ID de la transferencia anterior
     * seleccionada. Este valor únicamente es obligatorio cuando el tipo de
     * transferencia es {@link TransferenciaArchivo#PARCIAL_TIPO}.
     * @param principal Objeto principal de A&A.
     * @param model Modelo de UI.
     * @return Nombre del template Freemarker redirigido.
     */
    @RequestMapping(value = "/crear", method = RequestMethod.POST)
    public String validarFormularioCreacionPOST(
            @RequestParam("origenUsuario") Integer origenUsuarioID,
            @RequestParam("destinoUsuario") Integer destinoUsuarioID,
            @RequestParam("tipoTransferencia") String tipoTransferencia,
            @RequestParam(value = "transferenciaAnterior", required = false) Integer transferenciaAnteriorID,
            Principal principal, Model model) {
        LOG.log(Level.INFO, "transferenciaAnterior = {0}", transferenciaAnteriorID);
        LOG.log(Level.INFO, "tipoTransferencia = {0}", tipoTransferencia);
        LOG.log(Level.INFO, "destinoUsuario = {0}", destinoUsuarioID);
        LOG.log(Level.INFO, "origenUsuario = {0}", origenUsuarioID);

        final Usuario origenUsuario = getUsuario(principal);
        model.addAttribute("origenUsuario", origenUsuario);

        final List<TransferenciaArchivo> transferenciasRecibidas
                = transferenciaService.findAllRecibidasActivasByDestinoUsuario(origenUsuario.getId());
        model.addAttribute("transferenciasRecibidas", transferenciasRecibidas);

        model.addAttribute("tipoTransferencia", tipoTransferencia);

        if (destinoUsuarioID == null) {
            model.addAttribute(AppConstants.FLASH_ERROR,
                    "Debe seleccionar un usuario destino de la transferencia.");
            return "transferencia-archivo-crear";
        }

        final Usuario destinoUsuario = usuarioRepository.findOne(destinoUsuarioID);
        model.addAttribute("destinoUsuario", destinoUsuario);

        final TransferenciaArchivo transferenciaAnterior;
        if (transferenciaAnteriorID == null) {
            transferenciaAnterior = null;
        } else {
            transferenciaAnterior = transferenciaService
                    .findOneTransferenciaArchivo(transferenciaAnteriorID);
        }
        model.addAttribute("transferenciaAnterior", transferenciaAnterior);

        final TransferenciaArchivoValidacionDTO validacionDTO
                = transferenciaService.validarTransferencia(origenUsuario,
                        destinoUsuario, tipoTransferencia, transferenciaAnteriorID);
        if (!validacionDTO.isOK()) {
            model.addAttribute(AppConstants.FLASH_ERROR,
                    buildFlashErrorMessage(validacionDTO));
            return "transferencia-archivo-crear";
        }

        final List<DocumentoDependencia> registrosArchivo
                = transferenciaService.findRegistrosArchivo(tipoTransferencia,
                        transferenciaAnterior, origenUsuario);
        model.addAttribute("registrosArchivo", registrosArchivo);

        if (registrosArchivo.isEmpty()) {
            model.addAttribute(AppConstants.FLASH_ERROR,
                    "El usuario origen no tiene registros de archivo para "
                    + "transferir.");
            return "transferencia-archivo-crear";
        }

        return "transferencia-archivo-confirmar";
    }

    /**
     * Procesa la transferencia de archivo.
     *
     * @param origenUsuarioID ID del usuario de origen de la transferencia.
     * @param destinoUsuarioID ID del usuario de destino de la transferencia.
     * @param tipoTransferencia Identificador del tipo de la transferencia
     * realizada.
     * @param principal Objeto principal de A&A.
     * @param model Modelo de UI.
     * @return Nombre del template Freemarker redirigido.
     */
    @RequestMapping(value = "/procesar", method = RequestMethod.POST)
    public String procesarTransferenciaArchivoPOST(
            @RequestParam("origenUsuario") Integer origenUsuarioID,
            @RequestParam("destinoUsuario") Integer destinoUsuarioID,
            @RequestParam("tipoTransferencia") String tipoTransferencia,
            Principal principal, Model model) {

        LOG.log(Level.INFO, "tipoTransferencia = {0}", tipoTransferencia);
        LOG.log(Level.INFO, "destinoUsuario = {0}", destinoUsuarioID);
        LOG.log(Level.INFO, "origenUsuario = {0}", origenUsuarioID);

        final Usuario creadorUsuario = getUsuario(principal);
        final Usuario origenUsuario = usuarioRepository.findOne(origenUsuarioID);
        final Usuario destinoUsuario = usuarioRepository.findOne(destinoUsuarioID);

        final TransferenciaArchivo nuevatransferencia
                = transferenciaService.crearTransferencia(creadorUsuario,
                        origenUsuario, destinoUsuario, tipoTransferencia,
                        null);
        model.addAttribute("nuevatransferencia", nuevatransferencia);

        return "transferencia-archivo-resultado";
    }

    /**
     * Construye el mensaje de error a partir del DTO de validación.
     *
     * @param validacionDTO DTO de validación.
     * @return Mensaje de error.
     */
    private String buildFlashErrorMessage(final TransferenciaArchivoValidacionDTO validacionDTO) {
        final StringBuilder builder = new StringBuilder();

        for (String error : validacionDTO) {
            builder.append(error).append(" ");
        }

        return builder.toString().trim();
    }

}
