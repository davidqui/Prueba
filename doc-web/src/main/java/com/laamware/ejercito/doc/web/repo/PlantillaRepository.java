package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.laamware.ejercito.doc.web.entity.Plantilla;

public interface PlantillaRepository extends GenJpaRepository<Plantilla, Integer> {

	List<Plantilla> findByActivo(boolean b, Sort sort);
        
        List<Plantilla> findByBookmarkNameAndBookmarkValue(String bookmarkName, String bookmarkValue);
}
