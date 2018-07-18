package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Adjunto;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Tipologia;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.AdjuntoRepository;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Servicio de lógica de negocio para {@link Adjunto}
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/13/2018 (SICDI-Controltech Issue #156 feature-156)
 */
@Service
public class AdjuntoService {

    @Autowired
    private AdjuntoRepository adjuntoRepository;

    @Autowired
    private OFS ofs;

    /**
     * Lista todos los adjuntos activos de un documento.
     *
     * @param documento Documento.
     * @return Lista de todos los adjuntos activos del documento.
     */
    public List<Adjunto> findAllActivos(final Documento documento) {
        return adjuntoRepository.findAllByDocumentoIdAndActivoTrue(documento.getId());
    }

    /**
     * Indica si el tipo de contenido del archivo adjunto es válido para el
     * sistema.
     *
     * @param contentType Tipo de contenido.
     * @return {@code true} si el tipo de contenido es válido (PDF, JPG, JPEG,
     * PNG); de lo contrario, {@code false}.
     */
    /*
     * 2017-04-11 jgarcia@controltechcg.com Issue #46 (SIGDI-Controltech)
     * 2018-05-21 jgarcia@controltechcg.com Issue #162 (SIGDI-Controltech)
     * feature-162: Cambia de DocumentoController a AdjuntoService para
     * reutilización de la funcionalidad.
     */
    public boolean isValidAttachmentContentType(final String contentType) {
        String[] validContentTypes = {"application/pdf", "image/jpeg", "image/jpg", "image/png"};

        for (String validContentType : validContentTypes) {
            if (contentType.equals(validContentType)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Guarda un archivo en el OFS y lo registra como adjunto de un documento a
     * partir de los datos del archivo multiparte cargado por el usuario.
     *
     * @param documento Documento.
     * @param tipologia Tipología del adjunto.
     * @param usuario Usuario que realiza la carga del adjunto.
     * @param multipartFile Archivo multiparte.
     * @return Instancia del registro del archivo adjunto.
     * @throws IOException En caso de presentarse un error con el proceso de E/S
     * asociado a la lectura y almacenamiento del archivo.
     */
    /*
     * 2018-05-21 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public Adjunto guardarAdjunto(final Documento documento, final Tipologia tipologia, final Usuario usuario, final MultipartFile multipartFile) throws IOException {
        return guardarAdjunto(documento, tipologia, usuario, multipartFile.getOriginalFilename(), multipartFile.getBytes(), multipartFile.getContentType());
    }

    /**
     * Guarda un archivo en el OFS y lo registra como adjunto de un documento.
     *
     * @param documento Documento.
     * @param tipologia Tipología del adjunto.
     * @param usuario Usuario que realiza la carga del adjunto.
     * @param archivoNombreOriginal Nombre original del archivo.
     * @param archivoBytes Arreglo de bytes del arcchivo.
     * @param contentType Tipo de contenido del archivo.
     * @return Instancia del registro del archivo adjunto.
     * @throws IOException En caso de presentarse un error con el proceso de E/S
     * asociado a la lectura y almacenamiento del archivo.
     */
    /*
     * 2018-05-21 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public Adjunto guardarAdjunto(final Documento documento, final Tipologia tipologia, final Usuario usuario, final String archivoNombreOriginal, final byte[] archivoBytes, final String contentType) throws IOException {
        Adjunto adjunto = Adjunto.create();
        adjunto.setDocumento(documento);
        adjunto.setTipologia(tipologia);
        adjunto.setOriginal(archivoNombreOriginal);
        adjunto.setQuien(usuario.getId());
        adjunto.setCuando(new Date());

        final String ofsFileID = ofs.save(archivoBytes, contentType);
        adjunto.setContenido(ofsFileID);

        adjunto = adjuntoRepository.saveAndFlush(adjunto);
        return adjunto;
    }

    /**
     * Busca un adjunto por su ID y ID de documento, siempre y cuando el
     * registro se encuentre activo.
     *
     * @param id ID del adjunto.
     * @param documento Documento.
     * @return Instancia del adjunto activo correspondiente al ID y al ID del
     * documento, o {@code null} en caso que no exista correspondencia en el
     * sistema o en caso que el registro no esté activo.
     */
    /*
     * 2018-05-21 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162: Proceso de eliminación de adjuntos para registro de actas.
     */
    public Adjunto findByIDActivoAndDocumento(final String id, final Documento documento) {
        return adjuntoRepository.findByIdAndDocumentoAndActivoTrue(id, documento);
    }

    /**
     * Elimina un adjunto (de forma lógica).
     *
     * @param adjunto Adjunto a eliminar.
     * @param usuario Usuario que realiza el proceso de eliminación.
     * @return Instancia del archivo adjunto eliminado.
     */
    public Adjunto eliminarAdjunto(Adjunto adjunto, final Usuario usuario) {
        adjunto.setActivo(Boolean.FALSE);
        adjunto.setQuien(usuario.getId());
        adjunto.setCuando(new Date());

        adjunto = adjuntoRepository.saveAndFlush(adjunto);
        return adjunto;
    }
}
