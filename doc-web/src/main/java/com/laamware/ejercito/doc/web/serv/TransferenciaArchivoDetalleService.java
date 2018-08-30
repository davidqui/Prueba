package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivoDetalle;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.TransferenciaArchivoDetalleRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio con las reglas de negocio para el proceso de transferencia de
 * archivo detalle.
 *
 * @author samuel.delgado@controltechcg.com
 * @since Ago 27, 2018
 * @version 1.0.0 (feature-120).
 */
@Service
public class TransferenciaArchivoDetalleService {
    
    
    /**
     * Repositorio de detalle de transferencia.
     */
    @Autowired
    private TransferenciaArchivoDetalleRepository transferenciaArchivoDetalleRepository;
    
    /**
     * Servicio para documentos dependencia.
     */
    @Autowired
    private DocumentoDependenciaService documentoDependenciaService;
    
    
    public void guardarDocumentoTransferencia(TransferenciaArchivo transferenciaArchivo, 
        DocumentoDependencia documentoDependencia, Usuario usuario){
        TransferenciaArchivoDetalle transferenciaArchivoDetalle = new TransferenciaArchivoDetalle();
        transferenciaArchivoDetalle.setDocumentoDependencia(documentoDependencia);
        transferenciaArchivoDetalle.setDocumentoID(documentoDependencia.getDocumento().getId());
        transferenciaArchivoDetalle.setActivo(1);
        transferenciaArchivoDetalle.setIndRealizado(0);
        transferenciaArchivoDetalle.setAnteriorDependencia(transferenciaArchivo.getOrigenDependencia());
        transferenciaArchivoDetalle.setAnteriorUsuario(usuario);
        transferenciaArchivoDetalle.setAnteriorFechaAsignacion(documentoDependencia.getCuando());
        transferenciaArchivoDetalle.setNuevoDependencia(transferenciaArchivo.getDestinoDependencia());
        transferenciaArchivoDetalle.setNuevoUsuario(transferenciaArchivo.getDestinoUsuario());
        transferenciaArchivoDetalle.setNuevoFechaAsignacion(new Date());
        transferenciaArchivoDetalle.setTransferenciaArchivo(transferenciaArchivo);
        transferenciaArchivoDetalleRepository.save(transferenciaArchivoDetalle);
    }
    
    public void eliminarDocumentosTransferencia(TransferenciaArchivo transferenciaArchivo){
        List<TransferenciaArchivoDetalle> documentos = transferenciaArchivoDetalleRepository.findAllByTransferenciaArchivoAndActivo(transferenciaArchivo, 1);
        for (TransferenciaArchivoDetalle documento : documentos) { 
            documento.setActivo(0);
            transferenciaArchivoDetalleRepository.save(documento);
        }
    }
    
    
    public List<TransferenciaArchivoDetalle> buscarDocumentosTransferencia(TransferenciaArchivo transferenciaArchivo){
        return transferenciaArchivoDetalleRepository.findAllByTransferenciaArchivoAndActivo(transferenciaArchivo, 1);
    }
    
    public void transferirDocumentos(TransferenciaArchivo transferenciaArchivo){
        List<TransferenciaArchivoDetalle> buscarDocumentosTransferencia = buscarDocumentosTransferencia(transferenciaArchivo);
        for (TransferenciaArchivoDetalle transferenciaArchivoDetalle : buscarDocumentosTransferencia) {
            transferenciaArchivoDetalle.setIndRealizado(0);
            documentoDependenciaService.completarTransferencia(transferenciaArchivoDetalle.getDocumentoDependencia(), 
                    transferenciaArchivoDetalle.getNuevoUsuario(), transferenciaArchivoDetalle.getNuevoDependencia(),
                    transferenciaArchivo.getUsuDestinoCargo());
        }
    }

}
