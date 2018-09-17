package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.RecursoMultimedia;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecursoMultimediaRepository extends JpaRepository<RecursoMultimedia, Integer> {

    RecursoMultimedia findOneByNombreAndActivoTrue(String nombre);

    public List<RecursoMultimedia> getByActivoTrueAndTematicaId(Sort sort, Integer Id);
    public List<RecursoMultimedia> getByActivoTrue(Sort sort);
    
    /**
     * Busca todos los registros de Recurso Multimedia en Tematica por el Id.
     * 
     * 2018-09-03 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 
     * @param Id Id de la tematica a buscar.
     * @return Listado de Recursos Multimedia de la Tematica a la que corresponda el Id.
     */
    public List<RecursoMultimedia> findAllByTematicaId(Integer Id);
}
