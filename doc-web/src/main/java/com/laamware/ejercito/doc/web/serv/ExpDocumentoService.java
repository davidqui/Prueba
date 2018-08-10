package com.laamware.ejercito.doc.web.serv;


import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.ExpDocumento;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laamware.ejercito.doc.web.repo.ExpDocumentoRepository;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


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
    
    @Autowired
    private DocumentoService documentoService;
    /*
     * Servicio de notificaciones.
     */
    @Autowired
    private NotificacionService notificacionService;
    
    
    public final static Integer NOTIFICACION_EXPEDIENTE_DOCUMENTO_INDEXADO = 204;
    
    public static final Long ESTADO_DOCUMENTO_AGREGADO = new Long(1104) ;
    public static final Long ESTADO_DOCUMENTO_DESVINCULADO = new Long(1105) ;
    
    public void agregarDocumentoExpediente(final Documento documento, final Expediente expediente, Usuario usuarioSesion){
        ExpDocumento expDocumento = new ExpDocumento();
        expDocumento.setDocId(documento);
        expDocumento.setExpId(expediente);
        expDocumento.setActivo(true);
        expDocumento.setFecCreacion(new Date());
        expDocumento.setUsuCreacion(usuarioSesion);
        expDocumentoRepository.saveAndFlush(expDocumento);
        expedienteTransicionService.crearTransicion(expediente, 
            expedienteEstadoService.findById(ESTADO_DOCUMENTO_AGREGADO), usuarioSesion, documento, null);
        
        Map<String, Object> model = new HashMap();
        model.put("documento", documento);
        model.put("usuario", usuarioSesion);
        model.put("expediente", expediente);
        
        try {
            notificacionService.enviarNotificacion(model, NOTIFICACION_EXPEDIENTE_DOCUMENTO_INDEXADO, expediente.getDepId().getJefe());
        } catch (Exception ex) {
            Logger.getLogger(ExpUsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Busca un expediente documento por un documento.
     * @param documento
     * @return 
     */
    public ExpDocumento findByDocumento(Documento documento){
        return expDocumentoRepository.findByActivoTrueAndDocId(documento);
    }
    
    public void eliminaDocumentoExpediente(final Documento documento, final Expediente expediente, Usuario usuarioSesion){
        ExpDocumento expDocumento = expDocumentoRepository.findByActivoTrueAndDocId(documento);
        if(expDocumento != null){
            if(documento.getExpediente() != null){
                documento.setExpediente(null);
                documentoService.actualizar(documento);
            }
            
            expDocumento.setActivo(false);
            expDocumento.setFecModificacion(new Date());
            expDocumento.setUsuModificacion(usuarioSesion);
            expDocumentoRepository.saveAndFlush(expDocumento);
            expedienteTransicionService.crearTransicion(expediente, 
                expedienteEstadoService.findById(ESTADO_DOCUMENTO_DESVINCULADO), usuarioSesion, documento, null);
            
        }
    }
}
