package com.laamware.ejercito.doc.web.serv;


import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.ExpDocumento;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laamware.ejercito.doc.web.repo.ExpDocumentoRepository;
import java.util.Date;


/**
 * Servicio para los documentos de los expedientes.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @since Jul 27, 208 Issue #181 (SICDI-Controltech) feature-181
 */
@Service
public class ExpDocumentoService {

    /**
     * Repositorio de los documentos de los expedientes.
     */
    @Autowired
    private ExpDocumentoRepository expDocumentoRepository;
    
    /**
     * Repositorio de estados del expediente.
     */
    @Autowired
    private ExpedienteEstadoService expedienteEstadoService;
    
    /**
     * Repositorio de transiciones del espediente.
     */
    @Autowired
    private ExpedienteTransicionService expedienteTransicionService;
    
    public static final Long ESTADO_DOCUMENTO_AGREGADO = new Long(1104) ;
    
    public void agregarDocumentoExpediente(final Documento documento, final Expediente expediente, Usuario usuarioSesion){
        ExpDocumento expDocumento = new ExpDocumento();
        expDocumento.setDocId(documento);
        expDocumento.setExpId(expediente);
        expDocumento.setActivo(true);
        expDocumento.setFecCreacion(new Date());
//        expDocumento.setUsuCreacion(usuarioSesion);
        
        expDocumentoRepository.saveAndFlush(expDocumento);
        expedienteTransicionService.crearTransicion(expediente, 
            expedienteEstadoService.findById(ESTADO_DOCUMENTO_AGREGADO), usuarioSesion, documento, null);
    }
}