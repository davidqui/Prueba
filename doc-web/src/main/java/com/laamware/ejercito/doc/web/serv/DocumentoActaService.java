package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoActa;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.enums.DocumentoActaEstado;
import com.laamware.ejercito.doc.web.enums.DocumentoActaMode;
import com.laamware.ejercito.doc.web.repo.DocumentoActaRepository;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para proceso documental de "Registro de Acta".
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/15/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
@Service
public class DocumentoActaService {

    public static final Map<DocumentoActaEstado, DocumentoActaMode> ESTADO_MODE_MAP;
    
    private static final Logger LOG = Logger.getLogger(DocumentoActaService.class.getName());

    static {
        final Map<DocumentoActaEstado, DocumentoActaMode> map = new LinkedHashMap<>();
        ESTADO_MODE_MAP = Collections.unmodifiableMap(map);
    }
    
    @Autowired
    private DocumentoActaRepository documentoActaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DocumentoService documentoService;

    /**
     * Verifica si el usuario tiene acceso al documento acta.
     *
     * @param usuario Usuario.
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @return {@code true} si el usuario tiene acceso al documento acta; de lo
     * contrario, {@code false}.
     */
    public boolean verificaAccesoDocumentoActa(final Usuario usuario, final String procesoInstanciaID) {
        return usuarioService.verificaAccesoDocumentoActa(usuario, procesoInstanciaID);
    }

    /**
     * Crea un nuevo documento referente al acta e instancia del documento acta.
     *
     * @param procesoInstancia Instancia del proceso.
     * @param usuarioCreador Usuario creador del documento.
     * @return Nueva instancia de documento.
     */
    public Documento crearDocumentoAsociadoActa(final Instancia procesoInstancia, final Usuario usuarioCreador) {
        final Documento documentoAsociado = documentoService.crearDocumento(procesoInstancia, usuarioCreador);

        final DocumentoActa documentoActa = new DocumentoActa(documentoAsociado.getId());
        documentoActaRepository.saveAndFlush(documentoActa);

        return documentoAsociado;
    }

    /**
     * Busca un documento.
     *
     * @param documentoID ID del documento acta.
     * @return Documento acta, o {@code null} en caso de no tener
     * correspondencia en el sistema.
     */
    public Documento buscarDocumentoAsociadoActa(final String documentoID) {
        return documentoService.buscarDocumento(documentoID);
    }

    /**
     * Indica si el usuario tiene acceso al documento por nivel de
     * clasificación.
     *
     * @param usuario Usuario.
     * @param procesoInstancia Instancia del proceso del documento.
     * @return {@code true} si el usuario es el siguiente asignado al documento
     * o si el usuario tiene un nivel de clasificación igual o superior al nivel
     * de clasificación asignado al documento; de lo contrario, {@code false}.
     */
    public boolean tieneAccesoPorClasificacion(final Usuario usuario, final Instancia procesoInstancia) {
        return documentoService.tieneAccesoPorClasificacion(usuario, procesoInstancia);
    }

    /**
     * Busca la información del documento acta.
     *
     * @param documentoID ID del documento.
     * @return Documento acta o {@code null} en caso que no exista
     * correspondencia en el sistema.
     */
    public DocumentoActa buscarDocumentoActa(final String documentoID) {
        return documentoActaRepository.findOne(documentoID);
    }

}
