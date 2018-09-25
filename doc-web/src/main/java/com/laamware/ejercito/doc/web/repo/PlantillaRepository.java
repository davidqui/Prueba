package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.laamware.ejercito.doc.web.entity.Plantilla;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlantillaRepository extends GenJpaRepository<Plantilla, Integer> {

	List<Plantilla> findByActivo(boolean b, Sort sort);
        
        Page<Plantilla> findByActivo(boolean b, Pageable pageable);
        
        List<Plantilla> findByBookmarkNameAndBookmarkValue(String bookmarkName, String bookmarkValue);
        
        List<Plantilla> findByBookmarkName(String bookmarkName);
}
