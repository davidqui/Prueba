package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.repo.ExpedienteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para las operaciones de los expedientes.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @since Jul 27, 208 Issue #181 (SICDI-Controltech) feature-181
 */
@Service
public class ExpedienteService {

    /**
     * Repositorio de expedientes.
     */
    @Autowired
    private ExpedienteRepository expedienteRepository;

    /**
     * Obtiene el numero de registros de las bandejas de entrada por usuario
     *
     * @param usuId Identificador del usuario
     * @return Número de registros
     */
    public int obtenerCountExpedientesPorUsuario(Integer usuId) {
        return expedienteRepository.findExpedientesPorUsuarioCount(usuId);
    }

    /**
     * Obtiene los registros de las bandejas de entrada por usuario, de acuerdo
     * a la fila inicial y final.
     *
     * @param usuId Identificador del usuario
     * @param inicio Numero de registro inicial
     * @param fin Numero de registro final
     * @return Lista de documentos
     */
    public List<Expediente> obtenerExpedientesPorUsuarioPaginado(Integer usuId, int inicio, int fin) {
        return expedienteRepository.findExpedientesPorUsuarioPaginado(usuId, inicio, fin);
    }
}
