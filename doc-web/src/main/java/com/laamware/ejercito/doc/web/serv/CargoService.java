package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.CargoDTO;
import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.CargosRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author egonzalezm
 */
@Service
public class CargoService {

    private static final Logger LOG = Logger.getLogger(CargoService.class.getName());

    @Autowired
    CargosRepository cargosRepository;

    public String guardarCargo(Cargo cargo) {
        String mensaje = "OK";
        try {
            if (cargo.getCarNombre() == null || cargo.getCarNombre().trim().length() == 0) {
                mensaje = "Error-El nombre del cargo es obligatorio.";
                return mensaje;
            }

            if (cargo.getId() != null) {
                int numRegistros = cargosRepository.findregistrosNombreRepetido(cargo.getCarNombre(), cargo.getId());
                if (numRegistros > 0) {
                    mensaje = "Error-El nombre del cargo ya se encuentra registrado en el sistema.";
                    return mensaje;
                }
            }
            cargo.setCarIndLdap(Boolean.FALSE);
            cargosRepository.save(cargo);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            mensaje = "Excepcion-" + ex.getMessage();
        }
        return mensaje;
    }

    /**
     * Construye una lista de DTO de cargos correspondientes a un usuario, según
     * su orden de prioridad, para utilizar en las interfaces gráficas.
     *
     * @param usuario Usuario.
     * @return Lista de DTO de cargos correspondientes al usuario.
     */
    /*
     * 2018-05-17 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162: Utilización de la lógica de construcción de los DTO para
     * utilizar en diferentes UI.
     */
    public List<CargoDTO> buildCargosXUsuario(final Usuario usuario) {
        final List<Object[]> list = cargosRepository.findCargosXusuario(usuario.getId());
        final List<CargoDTO> cargos = new ArrayList<>();
        for (Object[] data : list) {
            cargos.add(new CargoDTO(((BigDecimal) data[0]).intValue(), (String) data[1]));
        }
        return cargos;
    }

    /**
     * Obtiene una lista de IDs y nombres de los cargos asociados a un usuario.
     *
     * @param usuID ID del usuario.
     * @return Lista de IDs y nombres de los cargos asociados al usuario.
     */
    /*
     * 2018-05-29 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public List<Object[]> findCargosXusuario(Integer usuID) {
        return cargosRepository.findCargosXusuario(usuID);
    }

}
