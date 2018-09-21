package com.laamware.ejercito.doc.web.serv;

import com.itextpdf.text.pdf.PdfReader;
import com.laamware.ejercito.doc.web.entity.Adjunto;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.InstanciaRepository;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio de lógica de negocio para las operaciones de Documentos.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/15/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
@Service
public class DocumentoService {

    private static final Logger LOG = Logger.getLogger(DocumentoService.class.getName());

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private InstanciaRepository instanciaRepository;

    @Autowired
    private OFS ofs;

    /**
     * Crea un nuevo documento.
     *
     * @param procesoInstancia Instancia del proceso a asociar con el documento.
     * @param usuarioCreador Usuario creador del documento.
     * @return Nuevo documento con estado {@link Documento#ESTADO_TEMPORAL}.
     */
    public Documento crearDocumento(Instancia procesoInstancia, final Usuario usuarioCreador) {
        System.err.println("Creando documento");
        final Documento documento = Documento.create();
        System.err.println("Creando documento id= " + documento.getId());
        documento.setInstancia(procesoInstancia);
        documento.setEstadoTemporal(Documento.ESTADO_TEMPORAL);

        documento.setElabora(usuarioCreador);
        documento.setUsuarioUltimaAccion(usuarioCreador);
        procesoInstancia.setVariable(Documento.DOC_ID, documento.getId());
        instanciaRepository.save(procesoInstancia);

        return documentoRepository.saveAndFlush(documento);
    }

    /**
     * Busca un documento.
     *
     * @param documentoID ID del documento.
     * @return Documento, o {@code null} en caso de no tener correspondencia en
     * el sistema.
     */
    public Documento buscarDocumento(final String documentoID) {
        return documentoRepository.findOne(documentoID);
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
        if (usuario.getId().equals(procesoInstancia.getAsignado().getId())) {
            return true;
        }

        final Documento documento = documentoRepository.findOneByInstanciaId(procesoInstancia.getId());

        final Integer clasificacionDocumento = (documento.getClasificacion() == null ? 0 : documento.getClasificacion().getOrden());
        final Integer clasificacionUsuario = (usuario.getClasificacion() == null ? 0 : usuario.getClasificacion().getOrden());

        return (clasificacionUsuario >= clasificacionDocumento);
    }

    /**
     * Actualiza el documento.
     *
     * @param documento
     * @return Documento actualizado.
     */
    Documento actualizar(Documento documento) {
        return documentoRepository.saveAndFlush(documento);
    }

    /**
     * Metodo que permite identificar el número de folios de un documento.
     *
     * @param documento
     * @return Número de folios
     */
    public Integer obtenerNumeroFolios(Documento documento) {
        int numFolios = 0;
        try {
            if ((documento.getInstancia().getProceso().getId().equals(Proceso.ID_TIPO_PROCESO_REGISTRO_ACTAS)) || (documento.getInstancia().getProceso().getId().equals(Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS))) {
                numFolios = documento.getNumeroFolios();
            } else {
                if (documento.getPdf() != null) {
                    final OFSEntry ofsEntry = ofs.read(documento.getPdf());
                    //Obtiene el número de folios del documento
                    PdfReader pdfReader = new PdfReader(ofsEntry.getContent());
                    numFolios = pdfReader.getNumberOfPages();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Adjunto> adjuntos = documento.getAdjuntos();

        //Obtiene el número de folios de los adjuntos
        if (!adjuntos.isEmpty()) {
            for (Adjunto adjunto : adjuntos) {
                if (adjunto.getOriginal() != null && adjunto.getOriginal().length() > 0 && adjunto.getActivo()) {
                    final int i = adjunto.getOriginal().lastIndexOf('.');
                    String extension = adjunto.getOriginal().substring(i + 1);
                    if (extension.toLowerCase().equals("pdf")) {
                        try {
                            final OFSEntry ofsEntryAdjunto = ofs.read(adjunto.getContenido());
                            PdfReader pdfReader = new PdfReader(ofsEntryAdjunto.getContent());
                            numFolios = numFolios + pdfReader.getNumberOfPages();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        numFolios = numFolios + 1;
                    }
                }
            }
        }
        return numFolios;
    }

    /**
     * Metodo que retorna la linea de mando de acuerdo a la dependencia del
     * usuario
     *
     * @param usuario
     * @return Linea de mando
     */
    public String retornaLineaMando(Usuario usuario) {
        List<Object[]> result = documentoRepository.retornaSiglasDependientes(usuario.getDependencia().getId());
        String lineaMando = "";
        if (!result.isEmpty()) {
            for (Object object : result) {
                lineaMando = lineaMando + "-" + (String) object;
            }
        }
        return lineaMando;
    }
}
