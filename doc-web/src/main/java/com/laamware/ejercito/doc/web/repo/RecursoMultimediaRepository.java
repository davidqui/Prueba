package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.RecursoMultimedia;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecursoMultimediaRepository extends JpaRepository<RecursoMultimedia, Integer> {

    RecursoMultimedia findOneByNombreAndActivoTrue(String nombre);

    public List<RecursoMultimedia> getByActivoTrue(Sort sort);
    
    /**
     * Busca todos los Recursos Multimedia activos para una Tematica.
     *
     * @param docId ID del documento.
     * @return Lista de documentos adjuntos activos.
     */
    /*
     * 2018-04-13 jgarcia@contrltechcg.com Issue #156 (SICDI-Controltech)
     * feature-156.
     */
//    public List<Adjunto> findAllByDocumentoIdAndActivoTrue(String docId);
}
