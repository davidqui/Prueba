package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.ExpObservacion;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpObservacionRepository extends JpaRepository<ExpObservacion, Long> {

    public List<ExpObservacion> findByExpIdExpId(Long expId, Sort sort);
}
