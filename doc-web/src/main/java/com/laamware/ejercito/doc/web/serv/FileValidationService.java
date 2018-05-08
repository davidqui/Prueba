package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para procesos de validación de archivos externos sobre archivos del
 * OFS del sistema.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 05/08/2018 Issue #160 (SICDI-Controltech) feature-160
 */
@Service
public class FileValidationService {

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private OFS ofs;

    /**
     * Valida si el arreglo de bytes del archivo a validar corresponde al
     * arreglo de bytes del documento firmado y enviado con el UUID indicado.
     *
     * @param archivoValidarBytes Arreglo de bytes del archivo a validar.
     * @param docFirmaEnvioUUID UUID de firma y envío del documento en el OFS a
     * comparar.
     * @return {@code true} si los arreglos de bytes de ambos archivos son los
     * mismos; de lo contrario, {@code false}.
     * @throws BusinessLogicException En caso que el UUID no tenga
     * correspondencia con algún documento del sistema.
     * @throws IOException En caso de error de E/S.
     */
    public boolean isValid(final byte[] archivoValidarBytes, final String docFirmaEnvioUUID) throws BusinessLogicException, IOException {
        final Documento documento = documentoRepository.findOneByFirmaEnvioUUID(docFirmaEnvioUUID);
        if (documento == null) {
            throw new BusinessLogicException("No hay documento con UUID: " + docFirmaEnvioUUID);
        }

        final OFSEntry ofsEntry = ofs.read(documento.getPdf());
        final byte[] documentoOFSBytes = ofsEntry.getContent();

        if (archivoValidarBytes.length != documentoOFSBytes.length) {
            return false;
        }

        for (int index = 0; index < archivoValidarBytes.length; index++) {
            if (archivoValidarBytes[index] != documentoOFSBytes[index]) {
                return false;
            }
        }

        return true;
    }
}
