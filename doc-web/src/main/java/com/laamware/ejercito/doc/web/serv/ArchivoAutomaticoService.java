package com.laamware.ejercito.doc.web.serv;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DocumentoDependenciaRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Servicio que permite aplicar el archivo automático.
 *
 * @author jgarcia@controltechcg.com
 * @since Abril 18, 2017
 *
 */
// 2017-04-18 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech)
@Service
public class ArchivoAutomaticoService {

    private static final Logger LOG = LoggerFactory.getLogger(ArchivoAutomaticoService.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DocumentoDependenciaRepository documentoDependenciaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Permite aplicar el archivo automático para los procesos de generación de
     * documento interno y externo, tras la transicipon de firma y envío del
     * documento.
     *
     * @param documento Documento.
     */
    public void archivarAutomaticamente(Documento documento) {

        final Usuario usuarioArchivador = getUsuarioArchivadorAutomatico(documento);

        /*
        2017-12-14 edison.gonzalez@controltechcg.com Issue #144 (SICDI-Controltech) 
        hotfix-144: Duplicidad de registros.
         */
        List<DocumentoDependencia> docDependencias = documentoDependenciaRepository.findAllActivoByUsuarioAndDocumento(usuarioArchivador.getId(), documento.getId());

        if (docDependencias.size() > 0) {
            for (DocumentoDependencia documentoDependencia : docDependencias) {
                LOG.info("Warning: Ya se encontro dato en la tabla DOCUMENTO_DEPENDENCIA con los siguientes atributos: DCDP_ID [" + documentoDependencia.getId() + "], TRD_ID [" + documentoDependencia.getTrd().getId() + "], DEP_ID [" + documentoDependencia.getDependencia().getId() + "]" + "], DOC_ID [" + documentoDependencia.getDocumento().getId() + "], QUIEN [" + documentoDependencia.getQuien() + "], CUANDO [" + documentoDependencia.getCuando() + "]");
            }
            return;
        }

        DocumentoDependencia documentoDependenciaArchivar = new DocumentoDependencia();
        documentoDependenciaArchivar.setDependencia(usuarioArchivador.getDependencia());
        documentoDependenciaArchivar.setDocumento(documento);
        documentoDependenciaArchivar.setTrd(documento.getTrd());
        documentoDependenciaRepository.save(documentoDependenciaArchivar);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "UPDATE DOCUMENTO_DEPENDENCIA SET DOCUMENTO_DEPENDENCIA.QUIEN = ? WHERE DOCUMENTO_DEPENDENCIA.DCDP_ID = ?";
        Object[] params = {usuarioArchivador.getId(), documentoDependenciaArchivar.getId()};
        jdbcTemplate.update(sql, params);
    }

    /**
     * Obtiene el usuario que quedará asociado al documento archivado de forma
     * automática, según el proceso del documento.
     *
     * @param documento Documento.
     * @return Usuario archivador.
     */
    private Usuario getUsuarioArchivadorAutomatico(Documento documento) {
        final Instancia instancia = documento.getInstancia();
        final Proceso proceso = instancia.getProceso();

        if (proceso.getId() == Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS) {
            Usuario usuario = documento.getAprueba();
            return usuario;
        }

        /*
		 * Para proceso de generación de documentos internos y externos.
         */
        final Integer usuarioCreadorID = documento.getQuien();
        Usuario usuario = usuarioRepository.findOne(usuarioCreadorID);
        return usuario;
    }

}
