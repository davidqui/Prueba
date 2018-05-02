package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.dto.DependenciaCopiaMultidestinoDTO;
import com.laamware.ejercito.doc.web.entity.DependenciaCopiaMultidestino;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.DependenciaCopiaMultidestinoService;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controlador para {@link DependenciaCopiaMultidestino}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/10/2018 (SICDI-Controltech Issue #156 feature-156)
 */
@Controller
@RequestMapping(DependenciaCopiaMultidestinoController.PATH)
public class DependenciaCopiaMultidestinoController extends UtilController {

    private static final Logger LOG = Logger.getLogger(DependenciaCopiaMultidestinoController.class.getName());

    private static final String DATETIME_FORMAT = "yyyy-MM-dd hh:mm aa";

    private static final String CREATE_MESSAGE = "Registro creado.";

    private static final String DELETE_MESSAGE = "Registro eliminado.";

    public static final String PATH = "/dependencia-copia-multidestino";

    @Autowired
    private DependenciaCopiaMultidestinoService multidestinoService;

    /**
     * Crea un registro de copia multidestino.
     *
     * @param documentoID ID del documento original.
     * @param dependenciaID ID de la dependencia destino de la copia.
     * @param principal Información de sesión autenticada.
     * @return DTO con la información del proceso.
     */
    @ResponseBody
    @RequestMapping(value = "/{documentoID}/{dependenciaID}", method = RequestMethod.POST)
    public ResponseEntity<DependenciaCopiaMultidestinoDTO> crearRegistroMultidestino(@PathVariable("documentoID") String documentoID, @PathVariable("dependenciaID") Integer dependenciaID, Principal principal) {
        final String logMsg = documentoID + "\t" + dependenciaID;

        try {
            final Usuario usuarioSesion = getUsuario(principal);

            final DependenciaCopiaMultidestino copiaMultidestino;
            try {
                copiaMultidestino = multidestinoService.crear(documentoID, dependenciaID, usuarioSesion);
            } catch (BusinessLogicException ex) {
                LOG.log(Level.SEVERE, logMsg, ex);
                return new ResponseEntity<>(new DependenciaCopiaMultidestinoDTO(false, ex.getMessage()), HttpStatus.BAD_REQUEST);
            }

            final int id = copiaMultidestino.getId();
            final String nombreDependencia = copiaMultidestino.getDependenciaDestino().getNombre();
            final String fechaCreacion = new SimpleDateFormat(DATETIME_FORMAT).format(copiaMultidestino.getCuando()).toUpperCase();
            final String nombreUsuarioCreador = (usuarioSesion.getUsuGrado().getId() + " " + usuarioSesion.getNombre()).trim().toUpperCase();

            return new ResponseEntity<>(new DependenciaCopiaMultidestinoDTO(true, CREATE_MESSAGE, id, nombreDependencia, fechaCreacion, nombreUsuarioCreador), HttpStatus.OK);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, logMsg, ex);
            return new ResponseEntity<>(new DependenciaCopiaMultidestinoDTO(false, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Elimina un registro de copia multidestino.
     *
     * @param id ID del registro a eliminar.
     * @param principal Información de sesión autenticada.
     * @return DTO con la información del proceso.
     */
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<DependenciaCopiaMultidestinoDTO> borrarRegistroMultidestino(@PathVariable("id") Integer id, Principal principal) {
        final String logMsg = "" + id;
        try {
            final Usuario usuarioSesion = getUsuario(principal);

            try {
                multidestinoService.eliminar(id, usuarioSesion);
            } catch (BusinessLogicException ex) {
                LOG.log(Level.SEVERE, logMsg, ex);
                return new ResponseEntity<>(new DependenciaCopiaMultidestinoDTO(false, ex.getMessage()), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity(new DependenciaCopiaMultidestinoDTO(true, DELETE_MESSAGE), HttpStatus.OK);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, logMsg, ex);
            return new ResponseEntity<>(new DependenciaCopiaMultidestinoDTO(false, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
