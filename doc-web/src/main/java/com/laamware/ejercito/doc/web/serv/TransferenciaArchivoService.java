package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.TransferenciaArchivoDTO;
import com.laamware.ejercito.doc.web.dto.TransferenciaArchivoValidacionDTO;
import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.PlantillaTransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.CargosRepository;
import com.laamware.ejercito.doc.web.repo.PlantillaTransferenciaArchivoRepository;
import com.laamware.ejercito.doc.web.repo.TransferenciaArchivoRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(TransferenciaArchivoService.class.getName());

    /**
     * Estados de la transferencia
     */
    private static final Long TRANSFERENCIA_ESTADO_EN_CONSTRUCCION = new Long(10);
    private static final Long TRANSFERENCIA_ESTADO_PEDIENTE_ACEPTACION = new Long(20);
    private static final Long TRANSFERENCIA_ESTADO_PEDIENTE_AUTORIZACION = new Long(30);
    private static final Long TRANSFERENCIA_ESTADO_APROBADO = new Long(40);
    private static final Long TRANSFERENCIA_ESTADO_RECHAZADO = new Long(50);
    private static final Long TRANSFERENCIA_ESTADO_TRANSFERIDO = new Long(60);
    private static final Long TRANSFERENCIA_ESTADO_ANULADO = new Long(70);
    
    
    /**
     * Notificaciones de la transferencia
     */
    private static final Integer NOTIFICACION_TRANSFERENCIA_USUARIO_DESTINO_ACEPTADO = 400;
    private static final Integer NOTIFICACION_TRANSFERENCIA_RECHAZADA = 401;
    private static final Integer NOTIFICACION_TRANSFERENCIA_POR_AUTORIZAR = 402;
    private static final Integer NOTIFICACION_TRANSFERENCIA_AUTORIZADA = 403;

    /**
     * Repositorio de maestro de transferencia.
     */
    @Autowired
    private TransferenciaArchivoRepository transferenciaRepository;
    
    /**
     * Repositorio de plantilla de transferencia.
     */
    @Autowired
    private PlantillaTransferenciaArchivoRepository plantillaRepository;

    /**
     * Servicio de generacion de numero de radicado.
     */
    @Autowired
    RadicadoService radicadoService;

    /**
     * Repositorio de cargos.
     */
    @Autowired
    CargosRepository cargosRepository;
    
    @Autowired
    private DocumentoDependenciaService documentoDependenciaService;
    
    @Autowired
    private ExpedienteService expedienteService;


    /**
     * Servicio para transiciones de transferencia de archivo
     */
    @Autowired
    private TransferenciaTransicionService transferenciaTransicionService;
    
    /**
     * Servicio de observaciones de transferencia de archivo
     */
    @Autowired
    private TransferenciaObservacionService transferenciaObservacionService;
    
    /**
     * servicio de expediente de una transferencia
     */
    @Autowired
    private TransExpedienteDetalleService transExpedienteDetalleService;
    
    /**
     *  servicio de transferencia archivo detalle
     */
    @Autowired
    private TransferenciaArchivoDetalleService transferenciaArchivoDetalleService;
    
    
    /**
     *  Servicio de notificaciones
     */
    @Autowired
    private NotificacionService notificacionService; 
    
    @Autowired
    private DocumentoActaService documentoActaService;
    
    /**
     * Busca un registro de transferencia de archivo.
     *
     * @param id ID del registro.
     * @return Transferencia.
     */
    public TransferenciaArchivo findOneTransferenciaArchivo(Integer id) {
        return transferenciaRepository.findOne(id);
    }
    
    /**
     * Indica si hay plantilla activa para el acta de la transferencia.
     *
     * @return {@code true} si hay plantilla activa; de lo contrario,
     * {@code false}.
     */
    public Boolean hayPlantillaActiva() {
        return plantillaRepository.findByActivoTrue() != null;
    }
      
    /**
     * Permiso para editar una transferecia segun un usuario
     * @param transferenciaArchivo transferencia 
     * @param usuario usuario para validar el permiso
     * @return true si tiene permiso false de lo contrario
     */
    public boolean permisoEditarTransferencia( final TransferenciaArchivo transferenciaArchivo, final Usuario usuario){
        return transferenciaArchivo != null && usuario != null && 
                transferenciaArchivo.getUsuarioAsignado() == 0 && 
                transferenciaArchivo.getIndAprobado() == 0 && 
                transferenciaArchivo.getCreadorUsuario().getId().equals(usuario.getId());
    } 
    
    /**
     * Permiso para ver una transferecia segun un usuario
     * @param transferenciaArchivo transferencia 
     * @param usuario usuario para validar el permiso
     * @return true si tiene permiso false de lo contrario
     */
    public boolean permisoVerTransferencia(final TransferenciaArchivo transferenciaArchivo, final Usuario usuario){
        return transferenciaArchivo != null && usuario != null &&
                ((transferenciaArchivo.getDestinoUsuario().getId().equals(usuario.getId()) && transferenciaArchivo.getUsuarioAsignado() >= 1) ||
                transferenciaArchivo.getOrigenDependencia().getJefe().getId().equals(usuario.getId()) || 
                  transferenciaArchivo.getOrigenUsuario().getId().equals(usuario.getId()));
    }
    
    /**
     * Permiso para aprobar una transgferencia destinatario segun un usuario
     * @param transferenciaArchivo transferencia 
     * @param usuario usuario para validar el permiso
     * @return true si tiene permiso false de lo contrario
     */
    public boolean permisoAprobarDestinatario(final TransferenciaArchivo transferenciaArchivo, final Usuario usuario){
        return transferenciaArchivo != null && usuario != null &&
                transferenciaArchivo.getUsuarioAsignado() == 1 &&
                transferenciaArchivo.getIndAprobado() == 0 && 
                transferenciaArchivo.getDestinoUsuario().getId().equals(usuario.getId());
    }
    
    /**
     * Permiso para rechazar una transferecia segun un usuario
     * @param transferenciaArchivo transferencia 
     * @param usuario usuario para validar el permiso
     * @return true si tiene permiso false de lo contrario
     */
    public boolean permisoRechazar(final TransferenciaArchivo transferenciaArchivo, final Usuario usuario){
        return transferenciaArchivo != null && usuario != null && 
                ((transferenciaArchivo.getUsuarioAsignado() == 1 && transferenciaArchivo.getDestinoUsuario().getId().equals(usuario.getId())) || 
                (transferenciaArchivo.getUsuarioAsignado() == 2 && transferenciaArchivo.getOrigenDependencia().getJefe().getId().equals(usuario.getId())) ) && 
                transferenciaArchivo.getIndAprobado() == 0;
    }
    
    /**
     * Permiso para aprobar una transferencia
     * @param transferenciaArchivo transferencia 
     * @param usuario usuario para validar el permiso
     * @return true si tiene permiso false de lo contrario
     */
    public boolean permisoAprobarJefe(final TransferenciaArchivo transferenciaArchivo, final Usuario usuario){
        return transferenciaArchivo != null && usuario != null &&
                transferenciaArchivo.getUsuarioAsignado() == 2 && transferenciaArchivo.getOrigenDependencia().getJefe().getId().equals(usuario.getId()) &&
                transferenciaArchivo.getIndAprobado() == 0;
    }
    
    /**
     * Aprueba una transferencia archivo, actualiza las transiciones.
     * @param transferenciaArchivo transferencia
     * @param cargo cargo con el que se aprueba
     * @param usuario usuario que aprueba
     */
    public void aprobarDestinatario(TransferenciaArchivo transferenciaArchivo, Cargo cargo, Usuario usuario){
        transferenciaArchivo.setUsuarioAsignado(2);
        transferenciaArchivo.setUsuDestinoCargo(cargo);
        transferenciaRepository.save(transferenciaArchivo);
        transferenciaTransicionService.crearTransicion(transferenciaArchivo, usuario, TRANSFERENCIA_ESTADO_APROBADO);
        transferenciaTransicionService.crearTransicion(transferenciaArchivo, usuario, TRANSFERENCIA_ESTADO_PEDIENTE_AUTORIZACION);
        
        Map<String, Object> model = new HashMap();
        model.put("usuOrigen", transferenciaArchivo.getOrigenUsuario());
        model.put("usuDestino", transferenciaArchivo.getDestinoUsuario());
        model.put("jefeOrigen", transferenciaArchivo.getOrigenDependencia().getJefe());
        model.put("transferencia", transferenciaArchivo);

        try {
            notificacionService.enviarNotificacion(model, NOTIFICACION_TRANSFERENCIA_USUARIO_DESTINO_ACEPTADO, transferenciaArchivo.getOrigenUsuario());
            notificacionService.enviarNotificacion(model, NOTIFICACION_TRANSFERENCIA_POR_AUTORIZAR, transferenciaArchivo.getOrigenDependencia().getJefe());
        } catch (Exception ex) {
            Logger.getLogger(ExpUsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /***
     * Método para rechazar una transferencia 
     * @param transferenciaArchivo transferencia a rechazar
     * @param Observacion observación realizada
     * @param usuario usuario que realiza la acción
     */
    public void rechazarTransferencia(TransferenciaArchivo transferenciaArchivo, String Observacion, Usuario usuario){
        transferenciaArchivo.setUsuarioAsignado(0);
        transferenciaArchivo.setUsuDestinoCargo(null);
        transferenciaRepository.save(transferenciaArchivo);
        if (Observacion != null && Observacion.trim().length() > 0) {
            transferenciaObservacionService.crearObservacon(transferenciaArchivo, Observacion, usuario);
        }
        transferenciaTransicionService.crearTransicion(transferenciaArchivo, usuario, TRANSFERENCIA_ESTADO_RECHAZADO);
    
        Map<String, Object> model = new HashMap();
        model.put("usuOrigen", transferenciaArchivo.getOrigenUsuario());
        model.put("usuDestino", transferenciaArchivo.getDestinoUsuario());
        model.put("jefeOrigen", transferenciaArchivo.getOrigenDependencia().getJefe());
        model.put("transferencia", transferenciaArchivo);

        try {
            notificacionService.enviarNotificacion(model, NOTIFICACION_TRANSFERENCIA_RECHAZADA, transferenciaArchivo.getOrigenUsuario());
            if (!transferenciaArchivo.getDestinoUsuario().getId().equals(usuario.getId()))
                notificacionService.enviarNotificacion(model, NOTIFICACION_TRANSFERENCIA_RECHAZADA, transferenciaArchivo.getOrigenUsuario());
        } catch (Exception ex) {
            Logger.getLogger(ExpUsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /***
     * Método para enviar una transferencia 
     * @param transferenciaArchivo transferencia a rechazar
     * @param usuario usuario que realiza la acción
     */
    public void enviarTransferencia(TransferenciaArchivo transferenciaArchivo, Usuario usuario){
        transferenciaArchivo.setUsuarioAsignado(1);
        transferenciaRepository.save(transferenciaArchivo);
        transferenciaTransicionService.crearTransicion(transferenciaArchivo, usuario, TRANSFERENCIA_ESTADO_PEDIENTE_ACEPTACION);
    }
    
    /***
     * Método para anular una transferencia 
     * @param transferenciaArchivo transferencia a rechazar
     * @param usuario usuario que realiza la acción
     */
    public void anularTransferencia(TransferenciaArchivo transferenciaArchivo, Usuario usuario){
        transferenciaArchivo.setActivo(false);
        transferenciaArchivo.setEstado(TransferenciaArchivo.ANULADA_ESTADO);
        transExpedienteDetalleService.eliminarExpedientesTransferencia(transferenciaArchivo);
        transferenciaArchivoDetalleService.eliminarDocumentosTransferencia(transferenciaArchivo);
        transferenciaRepository.save(transferenciaArchivo);
        transferenciaTransicionService.crearTransicion(transferenciaArchivo, usuario, TRANSFERENCIA_ESTADO_ANULADO);
    }
    
    /***
     * Método para aprobar una transferencia 
     * @param transferenciaArchivo transferencia a rechazar
     * @param usuario usuario que realiza la acción
     */
    public void aprobarTransferencia(TransferenciaArchivo transferenciaArchivo, Usuario usuario){
        transferenciaTransicionService.crearTransicion(transferenciaArchivo, usuario, TRANSFERENCIA_ESTADO_APROBADO);
        transferenciaArchivoDetalleService.transferirDocumentos(transferenciaArchivo);
        transExpedienteDetalleService.transferirExpedientes(transferenciaArchivo, usuario);
        transferenciaArchivo.setIndAprobado(1);
        transferenciaRepository.save(transferenciaArchivo);
        transferenciaTransicionService.crearTransicion(transferenciaArchivo, usuario, TRANSFERENCIA_ESTADO_TRANSFERIDO);
        
        documentoActaService.crearActaDeTransferencia(transferenciaArchivo);
        
        Map<String, Object> model = new HashMap();
        model.put("usuOrigen", transferenciaArchivo.getOrigenUsuario());
        model.put("usuDestino", transferenciaArchivo.getDestinoUsuario());
        model.put("jefeOrigen", transferenciaArchivo.getOrigenDependencia().getJefe());
        model.put("transferencia", transferenciaArchivo);

        try {
            if (!transferenciaArchivo.getOrigenUsuario().getId().equals(usuario.getId()))
                notificacionService.enviarNotificacion(model, NOTIFICACION_TRANSFERENCIA_USUARIO_DESTINO_ACEPTADO, transferenciaArchivo.getOrigenUsuario());
            notificacionService.enviarNotificacion(model, NOTIFICACION_TRANSFERENCIA_USUARIO_DESTINO_ACEPTADO, transferenciaArchivo.getDestinoUsuario());
        } catch (Exception ex) {
            Logger.getLogger(ExpUsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Valida los parámetros de entrada el proceso de transferencia.
     *
     * @param origenUsuario Usuario origen de la transferencia.
     * @param cargoOrigen
     * @param destinoUsuario Usuario destino de la transferencia.
     * @param justificacion Justificacion de la transferencia
     * @return DTO con los resultados de la validación.
     */
    public TransferenciaArchivoValidacionDTO validarTransferenciaGestion(
            final Usuario origenUsuario, final Integer cargoOrigen, final Usuario destinoUsuario, final String justificacion) {
        final TransferenciaArchivoValidacionDTO validacionDTO = new TransferenciaArchivoValidacionDTO();

        final PlantillaTransferenciaArchivo plantilla = plantillaRepository.findByActivoTrue();
        if (plantilla == null) {
            validacionDTO.addError("ATENCIÓN: No hay plantilla activa para la generación del acta de transferencia de archivo.");
        }

        if (origenUsuario == null) {
            validacionDTO.addError("Debe seleccionar un usuario origen de la transferencia.");
        } else {
            if (origenUsuario.getClasificacion() == null) {
                validacionDTO.addError("El usuario origen "+ getUsuarioDescripcion(origenUsuario, false) + " no tiene una clasificación configurada en el sistema.");
            } else if (!origenUsuario.getClasificacion().getActivo()) {
                validacionDTO.addError("El usuario origen "+ getUsuarioDescripcion(origenUsuario, false) + " no tiene una clasificación activa en el sistema ["+ origenUsuario.getClasificacion().getNombre()+ "].");
            }
        }

        if (destinoUsuario == null) {
            validacionDTO.addError("Debe seleccionar un usuario destino de la transferencia.");
        } else if (!destinoUsuario.getActivo()) {
            validacionDTO.addError("El usuario destino "+ getUsuarioDescripcion(destinoUsuario, false) + " no se encuentra activo en el sistema.");
        } else {
            if (destinoUsuario.getClasificacion() == null) {
                validacionDTO.addError("El usuario destino "+ getUsuarioDescripcion(destinoUsuario, false) + " no tiene una clasificación configurada en el sistema.");
            } else if (!destinoUsuario.getClasificacion().getActivo()) {
                validacionDTO.addError("El usuario destino "+ getUsuarioDescripcion(destinoUsuario, false) + " no tiene una clasificación activa en el sistema ["+ destinoUsuario.getClasificacion().getNombre()+ "].");
            }
        }

        if (origenUsuario != null && destinoUsuario != null) {
            if (destinoUsuario.getId().equals(origenUsuario.getId())) {
                validacionDTO.addError("Debe seleccionar un usuario destino diferente al usuario origen.");
            } else {
                if (origenUsuario.getClasificacion() != null && destinoUsuario.getClasificacion() != null && destinoUsuario.getClasificacion().getOrden().compareTo(origenUsuario.getClasificacion().getOrden()) < 0) {
                    validacionDTO.addError("El usuario destino "+ getUsuarioDescripcion(destinoUsuario, true) + " tiene una clasificación menor que la clasificación del usuario origen "+ getUsuarioDescripcion(origenUsuario, true) + ".");
                }
            }
        }
        
        if(justificacion == null || justificacion.trim().length() == 0){
            validacionDTO.addError("La justificación es obligatoria.");
        }
        
        if(cargoOrigen == null){
            validacionDTO.addError("El cargo del usuario emisor es obligatorio.");
        }
        
        final int numDocumentosPosibles = documentoDependenciaService.cantidadDocumentosPosibleTransferenciaXusuIdAndCargoId(origenUsuario.getId(), cargoOrigen);
        final int numExpedientesPosibles = expedienteService.cantidadExpedientesPosibleTransferenciaXusuId(cargoOrigen);
        
        if( numDocumentosPosibles == 0 && numExpedientesPosibles == 0){
            validacionDTO.addError("El usuario emisor no tiene documentos ni expedientes para realizar una transferencia.");
        }

        return validacionDTO;
    }
    
    public TransferenciaArchivo crearEncabezadoTransferenciaGestion(final Usuario usuarioOrigen, final Integer cargoOrigen, final Usuario destinoUsuario, final String justificacion) throws  Exception{
        TransferenciaArchivo ta = new TransferenciaArchivo();
        ta.setActivo(Boolean.TRUE);
        ta.setEstado(TransferenciaArchivo.CREADA_ESTADO);
        ta.setCreadorUsuario(usuarioOrigen);
        ta.setCreadorDependencia(usuarioOrigen.getDependencia());
        ta.setCreadorGrado(usuarioOrigen.getUsuGrado());
        ta.setUsuCreadorCargo(cargosRepository.getOne(cargoOrigen));
        ta.setFechaCreacion(new Date());
        ta.setOrigenUsuario(usuarioOrigen);
        ta.setOrigenDependencia(usuarioOrigen.getDependencia());
        ta.setOrigenClasificacion(usuarioOrigen.getClasificacion());
        ta.setOrigenGrado(usuarioOrigen.getUsuGrado());
        ta.setUsuOrigenCargo(cargosRepository.getOne(cargoOrigen));
        ta.setOrigenUsuario(usuarioOrigen);
        ta.setOrigenDependencia(usuarioOrigen.getDependencia());
        ta.setOrigenClasificacion(usuarioOrigen.getClasificacion());
        ta.setOrigenGrado(usuarioOrigen.getUsuGrado());
        ta.setUsuOrigenCargo(cargosRepository.getOne(cargoOrigen));
        ta.setDestinoUsuario(destinoUsuario);
        ta.setDestinoDependencia(destinoUsuario.getDependencia());
        ta.setDestinoClasificacion(destinoUsuario.getClasificacion());
        ta.setDestinoGrado(destinoUsuario.getUsuGrado());
        ta.setJustificacion(justificacion);
        ta.setUsuarioAsignado(0);
        ta.setIndAprobado(0);
        
        TransferenciaArchivo taNew = transferenciaRepository.saveAndFlush(ta);
        transferenciaTransicionService.crearTransicion(taNew, usuarioOrigen, TRANSFERENCIA_ESTADO_EN_CONSTRUCCION);
        
        return taNew;
    }
    
    /**
     * Obtiene el numero de transferencias en proceso por usuario
     *
     * @param usuId Identificador del usuario
     * @return Número de registros
     */
    public int findCountProcesoByUsuarioId(Integer usuId) {
        return transferenciaRepository.findCountProcesoByUsuarioId(usuId);
    }
    
    
    /**
     * Obtiene los registros de de transferencias en proceso por usuario, de acuerdo
     * a la fila inicial y final.
     *
     * @param usuId Identificador del usuario
     * @param inicio Numero de registro inicial
     * @param fin Numero de registro final
     * @return Lista de documentos
     */
    public List<TransferenciaArchivoDTO> findAllProcesoByUsuarioId(Integer usuId, int inicio, int fin) {
        List<TransferenciaArchivoDTO> transferenciaArchivos = new ArrayList<>();
        List<Object[]> result = transferenciaRepository.findAllProcesoByUsuarioId(usuId, inicio, fin);        
        
        if (result != null && !result.isEmpty()) {
            for (Object[] object : result) {
                TransferenciaArchivoDTO dTO = retornaTransferenciaArchivoDTO(object);
                transferenciaArchivos.add(dTO);
            }
        }
        return transferenciaArchivos;
    }
    
    /**
     * Obtiene el numero de transferencias realizadas por usuario
     *
     * @param usuId Identificador del usuario
     * @return Número de registros
     */
    public int findCountRealizadasByUsuarioId(Integer usuId) {
        return transferenciaRepository.findCountRealizadasByUsuarioId(usuId);
    }
    
    /**
     * Obtiene los registros de de transferencias realizadas por usuario, de acuerdo
     * a la fila inicial y final.
     *
     * @param usuId Identificador del usuario
     * @param inicio Numero de registro inicial
     * @param fin Numero de registro final
     * @return Lista de documentos
     */
    public List<TransferenciaArchivoDTO> findAllRealizadasByUsuarioId(Integer usuId, int inicio, int fin) {
        List<TransferenciaArchivoDTO> transferenciaArchivos = new ArrayList<>();
        List<Object[]> result = transferenciaRepository.findAllRealizadasByUsuarioId(usuId, inicio, fin);  
        
        if (result != null && !result.isEmpty()) {
            for (Object[] object : result) {
                TransferenciaArchivoDTO dTO = retornaTransferenciaArchivoDTO(object);
                transferenciaArchivos.add(dTO);
            }
        }
        return transferenciaArchivos;
    }
    
    /**
     * Obtiene el numero de transferencias recibidas por usuario
     *
     * @param usuId Identificador del usuario
     * @return Número de registros
     */
    public int findCountRecibidasByUsuarioId(Integer usuId) {
        return transferenciaRepository.findCountRecibidasByUsuarioId(usuId);
    }
    
    /**
     * Obtiene los registros de de transferencias recibidas por usuario, de acuerdo
     * a la fila inicial y final.
     *
     * @param usuId Identificador del usuario
     * @param inicio Numero de registro inicial
     * @param fin Numero de registro final
     * @return Lista de documentos
     */
    public List<TransferenciaArchivoDTO> findAllRecibidasByUsuarioId(Integer usuId, int inicio, int fin) {
        List<TransferenciaArchivoDTO> transferenciaArchivos = new ArrayList<>();
        List<Object[]> result = transferenciaRepository.findAllRecibidasByUsuarioId(usuId, inicio, fin);  
        
        if (result != null && !result.isEmpty()) {
            for (Object[] object : result) {
                TransferenciaArchivoDTO dTO = retornaTransferenciaArchivoDTO(object);
                transferenciaArchivos.add(dTO);
            }
        }
        return transferenciaArchivos;
    }
    
    
    /**
     * Retorna el objeto con los datos parámetricos de la transferencia
     *
     * @param object Objeto de la consulta
     * @return Datos de la transferencia
     */
    private TransferenciaArchivoDTO retornaTransferenciaArchivoDTO(Object[] object) {
        TransferenciaArchivoDTO dTO = new TransferenciaArchivoDTO();
        dTO.setTarId(object[0] != null ? ((BigDecimal) object[0]).intValue() : null);
        dTO.setActivo(object[1] != null ? ((BigDecimal) object[1]).equals(BigDecimal.ONE) : false);
        dTO.setEstado(object[2] != null ? ((String) object[2]) : null);
        dTO.setFechaCreacion(object[3] != null ? (Date) object[3] : null);
        dTO.setUsuIdOrigen(object[4] != null ? ((BigDecimal) object[4]).intValue() : null);
        dTO.setUsuNomOrigen(object[5] != null ? ((String) object[5]) : "");
        dTO.setCarIdOrigen(object[6] != null ? ((BigDecimal) object[6]).intValue() : null);
        dTO.setCarNomOrigen(object[7] != null ? ((String) object[7]) : "");
        dTO.setUsuIdDestino(object[8] != null ? ((BigDecimal) object[8]).intValue() : null);
        dTO.setUsuNomDestino(object[9] != null ? ((String) object[9]) : "");
        dTO.setCarIdDestino(object[10] != null ? ((BigDecimal) object[10]).intValue() : null);
        dTO.setCarNomDestino(object[11] != null ? (String) object[11] : "");
        dTO.setDepId(object[12] != null ? ((BigDecimal) object[12]).intValue() : null);
        dTO.setUsuIdJefe(object[13] != null ? ((BigDecimal) object[13]).intValue() : null);
        dTO.setJustificacion(object[14] != null ? (String) object[14] : "");
        dTO.setUsuarioAsignado(object[15] != null ? ((BigDecimal) object[15]).intValue() : null);
        dTO.setIndAprobado(object[16] != null ? ((BigDecimal) object[16]).equals(BigDecimal.ONE) : false);
        dTO.setNumDocumentos(object[17] != null ? ((BigDecimal) object[17]).intValue() : null);
        dTO.setNumExpedientes(object[18] != null ? ((BigDecimal) object[18]).intValue() : null);
        dTO.setUltEstado(object[19] != null ? ((String) object[19]) : "");
        dTO.setEsUsuarioOrigen(object[20] != null ? ((BigDecimal) object[20]).equals(BigDecimal.ONE) : false);
        dTO.setEsJefe(object[21] != null ? ((BigDecimal) object[21]).equals(BigDecimal.ONE) : false);
        dTO.setEsUsuarioDestino(object[22] != null ? ((BigDecimal) object[22]).equals(BigDecimal.ONE) : false);
        return dTO;
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
        /*
            2017-11-10 edison.gonzalez@controltechcg.com Issue #131 (SICDI-Controltech) 
            feature-131: Cambio en la entidad usuario, se coloca llave foranea el grado.
         */
        String descripcion = usuario.getUsuGrado().getId() + " "
                + usuario.getNombre();

        if (!conClasificacion) {
            return descripcion;
        }

        descripcion += " [" + usuario.getClasificacion().getNombre() + "]";
        return descripcion;
    }
}
