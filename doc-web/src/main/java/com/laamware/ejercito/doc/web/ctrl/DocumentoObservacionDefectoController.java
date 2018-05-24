package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.DocumentoObservacionDefecto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador para {@link DocumentoObservacionDefecto}.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/23/2018 Issue #172 (SICDI-Controltech) feature-172
 */
@Controller
@PreAuthorize(value = "hasRole('ADMIN_DOC_OBSERVACION_DEFECTO')")
@RequestMapping(DocumentoObservacionDefectoController.PATH)
public class DocumentoObservacionDefectoController extends UtilController {

    static final String PATH = "/admin/doc-observacion-defecto";

}
