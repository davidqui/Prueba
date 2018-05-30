package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.entity.UsuarioXDocumentoActa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de persistencia para {@link UsuarioXDocumentoActa}.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/23/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
@Repository
public interface UsuarioXDocumentoActaRepository extends JpaRepository<UsuarioXDocumentoActa, Integer> {

    /**
     * Busca todos los registros activos de usuarios asignados al documento
     * acta.
     *
     * @param documento Documento acta.
     * @return Lista de todos los registros activos de usuarios asignados al
     * documento acta.
     */
    public List<UsuarioXDocumentoActa> findAllByActivoTrueAndDocumento(Documento documento);

    /**
     * Busca un registro activo para el usuario y el documento.
     *
     * @param usuario Usuario.
     * @param documento Documento acta.
     * @return Instancia del registro de asociación entre el usuario y el
     * documento acta, o {@code null} en caso que no exista correspondencia
     * activa.
     */
    public UsuarioXDocumentoActa findByUsuarioAndDocumentoAndActivoTrue(Usuario usuario, Documento documento);

}
