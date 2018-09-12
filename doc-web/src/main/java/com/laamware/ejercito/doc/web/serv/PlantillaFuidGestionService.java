package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.PlantillaFuidGestion;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.PlantillaFuidGestionRepository;
import java.io.IOException;
import java.util.Date;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Servicio para plantilla de FUID.
 *
 * @author edison.gonzalez@controltechcg.com
 * @since Sep 11, 2018
 * @version 1.0.0 (feature-gogs-4).
 */
@Service
public class PlantillaFuidGestionService {

    /**
     * Repositorio de plantillas.
     */
    @Autowired
    private PlantillaFuidGestionRepository plantillaRepository;

    /**
     * Servicio de OFS.
     */
    @Autowired
    private OFS ofs;

    /**
     * Obtiene la plantilla activa en el sistema.
     *
     * @return Plantilla activa.
     */
    public PlantillaFuidGestion findPlantillaActiva() {
        return plantillaRepository.findByActivoTrue();
    }

    /**
     * Registra un archivo como nueva plantilla activa de Fuid. 
     * En caso que exista una plantilla activa anterior, la coloca como
     * inactiva.
     *
     * @param file Archivo multipart cargado por el usuario.
     * @param usuario Usuario.
     * @return Nuevo registro de plantilla de transferencia de archivo.
     * @throws IOException En caso que exista un error durante el almacenamiento
     * en el OFS.
     */
    @Transactional
    public PlantillaFuidGestion registrarNuevaPlantilla(final MultipartFile file,
            final Usuario usuario) throws IOException {
        final PlantillaFuidGestion actualPlantilla = findPlantillaActiva();
        final String codigoOFS = ofs.saveAsIs(file.getBytes(), file.getContentType());

        if (actualPlantilla != null) {
            actualPlantilla.setActivo(false);
            plantillaRepository.saveAndFlush(actualPlantilla);
        }

        final Date ahora = new Date(System.currentTimeMillis());
        final String firmaMD5 = DigestUtils.md5Hex(file.getInputStream());

        final PlantillaFuidGestion nuevaPlantilla = new PlantillaFuidGestion(true, file.getOriginalFilename(), (int) file.getSize(), firmaMD5, codigoOFS, usuario, ahora);
        plantillaRepository.saveAndFlush(nuevaPlantilla);

        return nuevaPlantilla;
    }
}
