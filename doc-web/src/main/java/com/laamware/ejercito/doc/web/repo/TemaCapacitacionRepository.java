package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.TemaCapacitacion;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TemaCapacitacionRepository extends PagingAndSortingRepository<TemaCapacitacion, Integer>, JpaRepository<TemaCapacitacion, Integer> {
    
    TemaCapacitacion findOneByTema(String tema);
    
    public List<TemaCapacitacion> findAllByTemaAndActivoTrue(String tema);

    public List<TemaCapacitacion> getByActivoTrue(Sort sort);
    public Page<TemaCapacitacion> getByActivoTrue(Pageable pageable);

    
}
