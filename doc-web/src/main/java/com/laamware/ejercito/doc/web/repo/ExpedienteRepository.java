package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Expediente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpedienteRepository extends JpaRepository<Expediente, Long> {
    
    /**
     * Trae una lista de expedientes dado un nombre y una dependecia.
     * @param nombre
     * @param dependencia
     * @return 
     */
    List<Expediente> getByExpNombreAndDepId(String nombre, Dependencia dependencia);
}
