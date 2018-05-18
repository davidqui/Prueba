package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Clasificacion;
import com.laamware.ejercito.doc.web.repo.ClasificacionRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Servicio de lógica de negocio para {@link Clasificacion}.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/15/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
@Service
public class ClasificacionService {

    @Autowired
    private ClasificacionRepository clasificacionRepository;

    /**
     * Obtiene toda la lista de clasificaciones activas ordenadas por el código
     * de orden de forma ascendente.
     *
     * @return Lista de clasificaciones activas y ordenadas.
     */
    public List<Clasificacion> findAllActivoOrderByOrden() {
        return clasificacionRepository.findByActivo(true, new Sort(Sort.Direction.ASC, "orden"));
    }

    /**
     * Busca una clasificación activa por su ID.
     *
     * @param id ID de la clasificación.
     * @return Instancia de la clasificación correspondiente al ID, si y solo
     * si, esta se encuentra activa; de lo contrario, {@code null}.
     */
    /*
     * 2018-05-16 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public Clasificacion findActivo(Integer id) {
        return clasificacionRepository.findOneByIdAndActivoTrue(id);
    }

}