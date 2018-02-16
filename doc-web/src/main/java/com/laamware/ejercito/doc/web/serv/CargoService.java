package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.repo.CargosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author egonzalezm
 */
@Service
public class CargoService {

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
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "Excepcion-" + e.getMessage();
        }
        return mensaje;
    }
}
