package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivoDetalle;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.TransferenciaArchivoDetalleRepository;
import java.util.ArrayList;
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
            transferenciaArchivoDetalle.setIndRealizado(1);
            documentoDependenciaService.completarTransferencia(transferenciaArchivoDetalle.getDocumentoDependencia(), 
                    transferenciaArchivoDetalle.getNuevoUsuario(), transferenciaArchivoDetalle.getNuevoDependencia(),
                    transferenciaArchivo.getUsuDestinoCargo());
        }
    }
    
    /***
     * Lista los documentos de la transferencia que no se encuentra en la custodia del usuario.
     * @param transferenciaArchivo transferencia de archivo
     * @param usuario usuario a evaluar si tiene los documentos
     * @return lista de los documentos que no pertenecen al usuario.
     */
    public List<TransferenciaArchivoDetalle> documentosNoPosesionTransferencia(TransferenciaArchivo transferenciaArchivo, Usuario usuario){
        List<TransferenciaArchivoDetalle> answ = new ArrayList<>();
        List<TransferenciaArchivoDetalle> findAllByTransferenciaArchivoAndActivo = transferenciaArchivoDetalleRepository.findAllByTransferenciaArchivoAndActivo(transferenciaArchivo, 1);
        for (TransferenciaArchivoDetalle transferenciaArchivoDetalle : findAllByTransferenciaArchivoAndActivo) {
            if (!transferenciaArchivoDetalle.getDocumentoDependencia().getQuien().equals(usuario.getId())) {
                answ.add(transferenciaArchivoDetalle);
            }
        }
        return answ;
    }
    
    /**
     * Genera los nuevos apuntadores a los documentos para la nueva transferencia
     * @param anterior anterior transferencia. 
     * @param nueva nueva transferencia.
     */
    public void reeviarDocumentosTransferencia(TransferenciaArchivo anterior, TransferenciaArchivo nueva){
        List<TransferenciaArchivoDetalle> documentos = transferenciaArchivoDetalleRepository.findAllByTransferenciaArchivoAndActivo(anterior, 1);
        for (TransferenciaArchivoDetalle documento : documentos) {
            if (documento.getDocumentoDependencia().getQuien().equals(anterior.getDestinoUsuario().getId())) {
                guardarDocumentoTransferencia(nueva, documento.getDocumentoDependencia(), nueva.getOrigenUsuario());
            }
        }
    }

}
