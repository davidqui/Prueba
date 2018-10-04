package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.RecursoMultimedia;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecursoMultimediaRepository extends PagingAndSortingRepository<RecursoMultimedia, Integer>, JpaRepository<RecursoMultimedia, Integer> {

    RecursoMultimedia findOneByNombreAndActivoTrue(String nombre);
    public Page<RecursoMultimedia> findByNombreIgnoreCaseContaining(Pageable pageable, String nombre);
    public Page<RecursoMultimedia> findByNombreIgnoreCaseContainingAndActivoTrue(Pageable pageable, String nombre);

    public List<RecursoMultimedia> getByActivoTrueAndTematicaId(Sort sort, Integer Id);
    public Page<RecursoMultimedia> getByActivoTrueAndTematicaId(Pageable pageable,Integer Id);
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
    
    /**
     * Busca todos los registros de Recurso Multimedia en Tematica por el Id, para paginar.
     * 
     * 2018-10-01 Issue #9 SICDI-GETDE feature-9 aherreram@imi.mil.co
     * 
     * @param Id Id de la tematica a buscar.
     * @return Listado de Recursos Multimedia de la Tematica a la que corresponda el Id.
     */
    public Page<RecursoMultimedia> findAllByTematicaId(Pageable pageable,Integer Id);
    
}
