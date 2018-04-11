package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.DependenciaCopiaMultidestino;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DependenciaCopiaMultidestinoRepository;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio de lógica de negocio para {@link DependenciaCopiaMultidestino}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/10/2018 (SICDI-Controltech Issue #156 feature-156)
 */
@Service
public class DependenciaCopiaMultidestinoService {

    @Autowired
    private DependenciaCopiaMultidestinoRepository multidestinoRepository;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private DependenciaRepository dependenciaRepository;

    /**
     * Lista todos los registros de dependencia copia multidestino activos para
     * un documento.
     *
     * @param documentoOriginal Documento original.
     * @return Lista de los registros activos de multidestino.
     */
    public List<DependenciaCopiaMultidestino> listarActivos(final Documento documentoOriginal) {
        return multidestinoRepository.findAllByDocumentoOriginalAndActivoTrue(documentoOriginal);
    }

    /**
     * Busca un registro activo para el documento y dependencia destino
     * indicados.
     *
     * @param documentoOriginal Documento original.
     * @param dependenciaDestino Dependencia destino.
     * @return Instancia del registro activo, o {@code null} en caso que no
     * exista para los parámetros indicados.
     */
    public DependenciaCopiaMultidestino buscarRegistroActivo(final Documento documentoOriginal, final Dependencia dependenciaDestino) {
        return multidestinoRepository.findByDocumentoOriginalAndDependenciaDestinoAndActivoTrue(documentoOriginal, dependenciaDestino);
    }

    /**
     * Crea un registro de copia multidestino.
     *
     * @param documentoOriginalID ID del documento original.
     * @param dependenciaDestinoID ID de la dependencia destino.
     * @param usuarioSesion Usuario en sesión.
     * @return Instancia del registro creado.
     * @throws BusinessLogicException En caso que no se cumpla alguna de las
     * reglas de negocio.
     */
    public DependenciaCopiaMultidestino crear(final String documentoOriginalID, final Integer dependenciaDestinoID, final Usuario usuarioSesion) throws BusinessLogicException {
        final Documento documentoOriginal = documentoRepository.findOne(documentoOriginalID);
        if (documentoOriginal == null) {
            throw new BusinessLogicException("El ID del documento original no es válido en el sistema.");
        }

        final Dependencia dependenciaDestino = dependenciaRepository.findOne(dependenciaDestinoID);
        if (dependenciaDestino == null) {
            throw new BusinessLogicException("El ID de la dependencia destino no es válido en el sistema.");
        }

        if (documentoOriginal.getDependenciaDestino().getId().equals(dependenciaDestino.getId())) {
            throw new BusinessLogicException("La dependencia seleccionada corresponde a la misma dependencia destino del documento original.");
        }

        final DependenciaCopiaMultidestino registroActual = multidestinoRepository.findByDocumentoOriginalAndDependenciaDestinoAndActivoTrue(documentoOriginal, dependenciaDestino);
        if (registroActual != null) {
            throw new BusinessLogicException("Ya existe un registro activo para el documento original y la dependencia destino seleccionados (" + registroActual.getId() + ").");
        }

        DependenciaCopiaMultidestino copiaMultidestino = new DependenciaCopiaMultidestino(documentoOriginal, dependenciaDestino, usuarioSesion, new Date());
        return multidestinoRepository.saveAndFlush(copiaMultidestino);
    }

    /**
     * Elimina - de forma lógica - el registro.
     *
     * @param id ID del registro a eliminar.
     * @param usuarioSesion Usuario en sesión.
     * @throws BusinessLogicException En caso que no se cumpla alguna de las
     * reglas de negocio.
     */
    public void eliminar(final Integer id, final Usuario usuarioSesion) throws BusinessLogicException {
        DependenciaCopiaMultidestino copiaMultidestino = multidestinoRepository.findOne(id);
        if (copiaMultidestino == null) {
            throw new BusinessLogicException("El ID del registro no es válido en el sistema.");
        }

        if (!copiaMultidestino.getActivo()) {
            throw new BusinessLogicException("El registro ya se encuentra eliminado en el sistema.");
        }

        copiaMultidestino.setActivo(Boolean.FALSE);
        copiaMultidestino.setQuienMod(usuarioSesion);
        copiaMultidestino.setCuandoMod(new Date());

        multidestinoRepository.saveAndFlush(copiaMultidestino);
    }
}
