package com.laamware.ejercito.doc.web.ctrl;

import com.aspose.words.License;
import com.laamware.ejercito.doc.web.dto.CargoDTO;
import com.laamware.ejercito.doc.web.dto.TransferenciaArchivoValidacionDTO;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.CargosRepository;
import com.laamware.ejercito.doc.web.serv.TransferenciaArchivoService;
import com.laamware.ejercito.doc.web.serv.UsuarioService;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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
     * Tamaño de la lista de presentación en la página de búsqueda de usuarios.
     */
    private static final int BUSQUEDA_PAGE_SIZE = 10;

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
     * Servicio de usuarios.
     */
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Servicio de cargos.
     */
    @Autowired
    CargosRepository cargosRepository;

    /**
     * Presenta el formulario de búsqueda de usuario destino, para el finder
     * correspondiente.
     *
     * @param criteria Criteria de búsqueda.
     * @param pageIndex Índice de la página a presentar.
     * @param model Modelo de UI.
     * @return Nombre del template Freemarker del formulario.
     */
    @RequestMapping(value = "/formulario-buscar-usuario", method = {RequestMethod.GET, RequestMethod.POST})
    public String presentarFormularioBusquedaUsuarioPOST(@RequestParam(value = "criteria", required = false) String criteria,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
            Model model) {
        final Page<Usuario> page = usuarioService.findAllByCriteriaSpecification(criteria, pageIndex, BUSQUEDA_PAGE_SIZE);

        model.addAttribute("criteria", criteria);
        model.addAttribute("usuarios", page.getContent());
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("totalPages", page.getTotalPages());

        return "transferencia-archivo-buscar-usuario";
    }

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

        model.addAttribute("cargoOrigen", origenUsuario.getUsuCargoPrincipalId().getId());

        final List<TransferenciaArchivo> transferenciasRecibidas
                = transferenciaService.findAllRecibidasActivasByDestinoUsuario(origenUsuario.getId());
        model.addAttribute("transferenciasRecibidas", transferenciasRecibidas);

        final Integer numArchivosRegistrosTotal = transferenciaService.countAllArchivoActivoByUsuario(origenUsuario.getId());
        model.addAttribute("numArchivosRegistrosTotal", numArchivosRegistrosTotal);

        if (!transferenciaService.hayPlantillaActiva()) {
            model.addAttribute(AppConstants.FLASH_ERROR,
                    "ATENCIÓN: No hay plantilla activa para la generación "
                    + "del acta de transferencia de archivo.");
        }

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
     * @param cargoOrigen
     * @param cargoDestino
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
            @RequestParam(value = "cargoOrigen") Integer cargoOrigen,
            @RequestParam(value = "cargoDestino", required = false) Integer cargoDestino,
            Principal principal, Model model) {

        final Usuario origenUsuario = getUsuario(principal);
        model.addAttribute("origenUsuario", origenUsuario);
        
        model.addAttribute("cargoOrigen", cargoOrigen);
        model.addAttribute("cargoDestino", cargoDestino);

        final List<TransferenciaArchivo> transferenciasRecibidas
                = transferenciaService.findAllRecibidasActivasByDestinoUsuario(origenUsuario.getId());
        model.addAttribute("transferenciasRecibidas", transferenciasRecibidas);

        model.addAttribute("tipoTransferencia", tipoTransferencia);

        final Integer numArchivosRegistrosTotal = transferenciaService.countAllArchivoActivoByUsuario(origenUsuario.getId());
        model.addAttribute("numArchivosRegistrosTotal", numArchivosRegistrosTotal);

        final TransferenciaArchivo transferenciaAnterior;
        if (transferenciaAnteriorID == null) {
            transferenciaAnterior = null;
        } else {
            transferenciaAnterior = transferenciaService
                    .findOneTransferenciaArchivo(transferenciaAnteriorID);
        }
        model.addAttribute("transferenciaAnterior", transferenciaAnterior);

        if (destinoUsuarioID == null) {
            model.addAttribute(AppConstants.FLASH_ERROR,
                    "Debe seleccionar un usuario destino de la transferencia.");
            return "transferencia-archivo-crear";
        }

        final Usuario destinoUsuario = usuarioService.findOne(destinoUsuarioID);
        model.addAttribute("destinoUsuario", destinoUsuario);
        
        // 2018-03-12 edison.gonzalez@controltechcg.com Issue #151 (SIGDI-Controltech):
        // Se añade la lista de cargos del usuario destino
        List<Object[]> list = cargosRepository.findCargosXusuario(destinoUsuario.getId());
        List<CargoDTO> cargoDTOs = new ArrayList<>();
        for (Object[] os : list) {
            CargoDTO cargoDTO = new CargoDTO(((BigDecimal) os[0]).intValue(), (String) os[1]);
            cargoDTOs.add(cargoDTO);
        }
        model.addAttribute("cargosXusuarioDestino", cargoDTOs);

        final TransferenciaArchivoValidacionDTO validacionDTO
                = transferenciaService.validarTransferencia(origenUsuario,
                        destinoUsuario, tipoTransferencia, transferenciaAnterior);
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
        
        Cargo cOrigen = cargosRepository.findOne(cargoOrigen);
        Cargo cDestino = cargosRepository.findOne(cargoDestino);
        
        model.addAttribute("nombreCargoOrigen", cOrigen.getCarNombre());
        model.addAttribute("nombreCargoDestino", cDestino.getCarNombre());
        
        return "transferencia-archivo-confirmar";
    }

    /*
        2018-03-12 edison.gonzalez@controltechcg.com Issue #151 (SICDI-Controltech) 
        feature-151: Se añaden los campos de los cargos de los usuarios origen y destino.
    */
    /**
     * Procesa la transferencia de archivo.
     *
     * @param origenUsuarioID ID del usuario de origen de la transferencia.
     * @param destinoUsuarioID ID del usuario de destino de la transferencia.
     * @param tipoTransferencia Identificador del tipo de la transferencia
     * realizada.
     * @param transferenciaAnteriorID ID de la transferencia anterior
     * seleccionada. Este valor únicamente es obligatorio cuando el tipo de
     * transferencia es {@link TransferenciaArchivo#PARCIAL_TIPO}.
     * @param cargoOrigen ID del cargo del usuario origen
     * @param cargoDestino ID del cargo del usuario destino
     * @param principal Objeto principal de A&A.
     * @param model Modelo de UI.
     * @return Nombre del template Freemarker redirigido.
     */
    @RequestMapping(value = "/procesar", method = RequestMethod.POST)
    public String procesarTransferenciaArchivoPOST(
            @RequestParam("origenUsuario") Integer origenUsuarioID,
            @RequestParam("destinoUsuario") Integer destinoUsuarioID,
            @RequestParam("tipoTransferencia") String tipoTransferencia,
            @RequestParam(value = "transferenciaAnterior", required = false) Integer transferenciaAnteriorID,
            @RequestParam(value = "cargoOrigen") Integer cargoOrigen,
            @RequestParam(value = "cargoDestino") Integer cargoDestino,
            Principal principal, Model model) {

        final Usuario creadorUsuario = getUsuario(principal);
        final Usuario origenUsuario = usuarioService.findOne(origenUsuarioID);
        final Usuario destinoUsuario = usuarioService.findOne(destinoUsuarioID);
        final TransferenciaArchivo transferenciaAnterior;
        if (transferenciaAnteriorID == null) {
            transferenciaAnterior = null;
        } else {
            transferenciaAnterior = transferenciaService
                    .findOneTransferenciaArchivo(transferenciaAnteriorID);
        }
        model.addAttribute("transferenciaAnterior", transferenciaAnterior);

        final License asposeLicense;
        try {
            asposeLicense = getAsposeLicence();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR,
                    "Excepción obteniendo Licencia Aspose: " + ex.getMessage());
            return presentarFormularioCreacionGET(principal, model);
        }

        final TransferenciaArchivo nuevatransferencia;
        try {
            nuevatransferencia = transferenciaService
                    .crearTransferenciaConActa(creadorUsuario,
                            origenUsuario, destinoUsuario, tipoTransferencia,
                            transferenciaAnterior, asposeLicense,cargoOrigen,cargoDestino);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR,
                    "Excepción durante transferencia: " + ex.getMessage());
            return presentarFormularioCreacionGET(principal, model);
        }

        model.addAttribute("nuevatransferencia", nuevatransferencia);

        model.addAttribute(AppConstants.FLASH_SUCCESS,
                "Se ha realizado la transferencia de archivo exitosamente.");

        return "transferencia-archivo-resultado";
    }

    /**
     * Obtiene la referencia de la licencia de Aspose.
     *
     * @return Licencia de Aspose.
     * @throws Exception
     */
    private License getAsposeLicence() throws Exception {
        final License asposeLicense = new License();
        final Resource resource = new ClassPathResource("Aspose.Words.lic");
        asposeLicense.setLicense(resource.getInputStream());
        return asposeLicense;
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

}
