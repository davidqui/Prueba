package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.TransferenciaArchivoValidacionDTO;
import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.Grados;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivoDetalle;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DocumentoDependenciaRepository;
import com.laamware.ejercito.doc.web.repo.GradosRepository;
import com.laamware.ejercito.doc.web.repo.PlantillaTransferenciaArchivoRepository;
import com.laamware.ejercito.doc.web.repo.TransferenciaArchivoDetalleRepository;
import com.laamware.ejercito.doc.web.repo.TransferenciaArchivoRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio con las reglas de negocio para el proceso de transferencia de
 * archivo.
 *
 * @author jgarcia@controltechcg.com
 * @since Ago 28, 2017
 * @version 1.0.0 (feature-120).
 */
@Service
public class TransferenciaArchivoService {

    /**
     * Repositorio de maestro de transferencia.
     */
    @Autowired
    private TransferenciaArchivoRepository transferenciaRepository;

    /**
     * Repositorio de detalle de transferencia.
     */
    @Autowired
    private TransferenciaArchivoDetalleRepository detalleRepository;

    /**
     * Repositorio de plantilla de transferencia.
     */
    @Autowired
    private PlantillaTransferenciaArchivoRepository plantillaRepository;

    /**
     * Repositorio de registros de archivo.
     */
    @Autowired
    private DocumentoDependenciaRepository documentoDependenciaRepository;

    /**
     * Repositorio de grados.
     */
    @Autowired
    private GradosRepository gradoRepository;

    /**
     * Repositorio de usuario.
     */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Obtiene todas las transferencias de archivo activas en estado
     * {@link TransferenciaArchivo#APROBADA_ESTADO} para un usuario destino,
     * ordenadas por la fecha de aprobación.
     *
     * @param destinoUsuario ID del usuario destino.
     * @return Lista de transferencias.
     */
    public List<TransferenciaArchivo> findAllRecibidasActivasByDestinoUsuario(
            final Integer destinoUsuario) {
        return transferenciaRepository
                .findAllRecibidasActivasByDestinoUsuario(destinoUsuario);
    }

    /**
     * Valida los parámetros de entrada el proceso de transferencia.
     *
     * @param origenUsuario Usuario origen de la transferencia.
     * @param destinoUsuario Usuario destino de la transferencia.
     * @param tipoTransferencia Tipo de transferencia.
     * @return DTO con los resultados de la validación.
     */
    public TransferenciaArchivoValidacionDTO validarTransferencia(
            final Usuario origenUsuario, final Usuario destinoUsuario,
            final String tipoTransferencia) {
        final TransferenciaArchivoValidacionDTO validacionDTO
                = new TransferenciaArchivoValidacionDTO();

        if (origenUsuario == null) {
            validacionDTO.addError("Debe seleccionar un usuario origen de la "
                    + "transferencia.");
        } else {
            if (origenUsuario.getClasificacion() == null) {
                validacionDTO.addError("El usuario origen "
                        + getUsuarioDescripcion(origenUsuario, false) + " no "
                        + "tiene una clasificación configurada en el sistema.");
            } else if (!origenUsuario.getClasificacion().getActivo()) {
                validacionDTO.addError("El usuario origen "
                        + getUsuarioDescripcion(origenUsuario, false) + " no "
                        + "tiene una clasificación activa en el sistema ["
                        + origenUsuario.getClasificacion().getNombre()
                        + "].");
            }
        }

        if (destinoUsuario == null) {
            validacionDTO.addError("Debe seleccionar un usuario destino de la "
                    + "transferencia.");
        } else if (!destinoUsuario.getActivo()) {
            validacionDTO.addError("El usuario destino "
                    + getUsuarioDescripcion(destinoUsuario, false) + " no "
                    + "se encuentra activo en el sistema.");
        } else {
            if (destinoUsuario.getClasificacion() == null) {
                validacionDTO.addError("El usuario destino "
                        + getUsuarioDescripcion(destinoUsuario, false) + " no "
                        + "tiene una clasificación configurada en el sistema.");
            } else if (!destinoUsuario.getClasificacion().getActivo()) {
                validacionDTO.addError("El usuario destino "
                        + getUsuarioDescripcion(destinoUsuario, false) + " no "
                        + "tiene una clasificación activa en el sistema ["
                        + destinoUsuario.getClasificacion().getNombre()
                        + "].");
            }
        }

        if (origenUsuario != null && destinoUsuario != null) {
            if (destinoUsuario.getId().equals(origenUsuario.getId())) {
                validacionDTO.addError("Debe seleccionar un usuario destino "
                        + "diferente al usuario origen.");
            } else if (origenUsuario.getClasificacion() != null
                    && destinoUsuario.getClasificacion() != null
                    && destinoUsuario.getClasificacion().getOrden()
                            .compareTo(origenUsuario.getClasificacion()
                                    .getOrden()) < 0) {
                validacionDTO.addError(
                        "El usuario destino "
                        + getUsuarioDescripcion(destinoUsuario, true) + " tiene "
                        + "una clasificación menor que la clasificación del "
                        + "usuario origen "
                        + getUsuarioDescripcion(origenUsuario, true) + ".");
            }
        }

        return validacionDTO;
    }

    /**
     * Obtiene la descripción del usuario.
     *
     * @param usuario Usuario.
     * @param conClasificacion {@code true} indica que se debe presentar la
     * clasificación del usuario.
     * @return Descripción.
     */
    public String getUsuarioDescripcion(final Usuario usuario,
            final boolean conClasificacion) {
        String descripcion = usuario.getGrado() + " "
                + usuario.getNombre();

        if (!conClasificacion) {
            return descripcion;
        }

        descripcion += " [" + usuario.getClasificacion().getNombre() + "]";
        return descripcion;
    }

    /**
     * Procesa, aplica y crea una transferencia de archivo.
     *
     * @param creadorUsuario Usuario creador de la transferencia.
     * @param origenUsuario Usuario origen de la transferencia.
     * @param destinoUsuario Usuario destino de la transferencia.
     * @param tipoTransferencia Tipo de transferencia.
     * @param transferenciaAnterior Registro maestro de transferencia anterior a
     * transferir de nuevo. En caso que este parámetro sea {@code null}, se
     * realizará transferencia de todo el archivo del usuario origen, al usuario
     * destino.
     * @return Registro maestro de la transferencia creada.
     * @see TransferenciaArchivo#TOTAL_TIPO
     * @see TransferenciaArchivo#PARCIAL_TIPO
     */
    @Transactional
    public TransferenciaArchivo crearTransferencia(final Usuario creadorUsuario,
            final Usuario origenUsuario, final Usuario destinoUsuario,
            final String tipoTransferencia,
            final TransferenciaArchivo transferenciaAnterior) {

        final Date ahora = new Date(System.currentTimeMillis());

        final Grados creadorGrado
                = gradoRepository.findOne(creadorUsuario.getGrado());
        final Grados origenGrado
                = gradoRepository.findOne(origenUsuario.getGrado());
        final Grados destinoGrado
                = gradoRepository.findOne(destinoUsuario.getGrado());

        final TransferenciaArchivo transferencia = new TransferenciaArchivo(
                tipoTransferencia,
                creadorUsuario, creadorUsuario.getDependencia(),
                creadorGrado, creadorUsuario.getCargo(), ahora, origenUsuario,
                origenUsuario.getDependencia(), origenUsuario.getClasificacion(),
                origenGrado, origenUsuario.getCargo(), destinoUsuario,
                destinoUsuario.getDependencia(), destinoUsuario.getClasificacion(),
                destinoGrado, destinoUsuario.getCargo(),
                transferenciaAnterior == null ? null : transferenciaAnterior.getId());

        transferenciaRepository.save(transferencia);

        final List<DocumentoDependencia> registrosArchivo;
        if (transferenciaAnterior == null) {
            registrosArchivo = documentoDependenciaRepository
                    .findAllActivoByUsuario(origenUsuario.getId());
        } else {
            registrosArchivo = documentoDependenciaRepository
                    .findAllByUsuarioAndTransferenciaArchivo(origenUsuario.getId(),
                            transferenciaAnterior.getId());
        }

        for (DocumentoDependencia documentoDependencia : registrosArchivo) {
            procesarRegistroArchivo(transferencia, documentoDependencia, ahora);
        }

        transferencia.setNumeroDocumentos(registrosArchivo.size());
        transferencia.setEstado(TransferenciaArchivo.APROBADA_ESTADO);
        transferencia.setFechaAprobacion(ahora);

        transferenciaRepository.saveAndFlush(transferencia);

        if (transferenciaAnterior != null) {
            transferenciaAnterior.setActivo(Boolean.FALSE);
            transferenciaRepository.saveAndFlush(transferenciaAnterior);
        }

        return transferencia;
    }

    /**
     * Procesa un registro de archivo para su transferencia.
     *
     * @param transferencia Transferencia de archivo maestro.
     * @param documentoDependencia Registro de archivo.
     * @param fechaAsignacion Fecha de asignación.
     */
    @Transactional
    private void procesarRegistroArchivo(final TransferenciaArchivo transferencia,
            final DocumentoDependencia documentoDependencia, final Date fechaAsignacion) {

        final Usuario anteriorUsuario
                = usuarioRepository.findOne(documentoDependencia.getQuien());

        final TransferenciaArchivoDetalle detalle
                = new TransferenciaArchivoDetalle(transferencia,
                        documentoDependencia,
                        documentoDependencia.getDocumento().getId(),
                        documentoDependencia.getDependencia(), anteriorUsuario,
                        documentoDependencia.getCuando(),
                        transferencia.getDestinoDependencia(),
                        transferencia.getDestinoUsuario(), fechaAsignacion);

        detalleRepository.saveAndFlush(detalle);
    }

}
