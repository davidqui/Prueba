package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.DocumentoExpDTO;
import com.laamware.ejercito.doc.web.dto.ExpedienteDTO;
import com.laamware.ejercito.doc.web.entity.ExpTrd;
import com.laamware.ejercito.doc.web.entity.ExpUsuario;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.ParNombreExpediente;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.ExpedienteRepository;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para las operaciones de los expedientes.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @since Jul 27, 208 Issue #181 (SICDI-Controltech) feature-181
 */
@Service
public class ExpedienteService {

    /**
     * Repositorio de expedientes.
     */
    @Autowired
    private ExpedienteRepository expedienteRepository;

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
    private ExpUsuarioService expUsuarioService;

    @Autowired
    private ExpTrdService expTrdService;

    @Autowired
    TRDService trdService;

    @Autowired
    DocumentoRepository documentoRepository;

    /*
     * Servicio de notificaciones.
     */
    @Autowired
    private NotificacionService notificacionService;

    public static final Long ESTADO_INICIAL_EXPEDIENTE = new Long(1100);
    public static final Long ESTADO_ENVIADO_APROBAR = new Long(1101);
    public static final Long ESTADO_ADMINISTRADOR_MODIFICADO = new Long(1111);
    public static final Long ESTADO_APROBADO = new Long(1102);
    public static final Long ESTADO_RECHAZADO = new Long(1103);
    public static final Long ESTADO_CAMBIO_TIPO = new Long(1112);
    public static final Long ESTADO_EXPEDIENTE_CERRADO = new Long(1106);
    public static final Long ESTADO_EXPEDIENTE_REABIERTO = new Long(1110);

    public static final int EXPEDIENTE_SIMPLE = 1;
    public static final int EXPEDIENTE_COMPLEJO = 2;

    public final static Integer NOTIFICACION_EXPEDIENTE_POR_APROBAR = 202;
    public final static Integer NOTIFICACION_EXPEDIENTE_RESPUESTA = 203;
    public final static Integer NOTIFICACION_EXPEDIENTE_CERRADO = 205;
    public final static Integer NOTIFICACION_EXPEDIENTE_REABIERTO = 206;

    public Expediente finById(Long id) {
        return expedienteRepository.findOne(id);
    }

    /**
     * *
     * Método para crear un expediente
     *
     * @param expediente expediente a crear
     * @param usuario usuario quien crea el expediente
     * @param numeroExpediente elemento del nombre del expediente
     * @param parNombreExpediente elemento de los parametrizables por el
     * administrador
     * @param opcionalNombre elemnto opcional dentro del nombre
     * @return Expediente creado
     * @throws BusinessLogicException si ocurre algun error con validación de
     * logica del negocio
     */
    public Expediente CrearExpediente(Expediente expediente, Usuario usuario,
            String numeroExpediente, ParNombreExpediente parNombreExpediente, String opcionalNombre) throws BusinessLogicException {

        if (usuario.getDependencia().getJefe() == null) {
            throw new BusinessLogicException("En su dependencia no existe un jefe.");
        }
        if (expediente.getTrdIdPrincipal() == null) {
            throw new BusinessLogicException("Debe elegir una TRD principal");
        }
        if (expediente.getExpDescripcion() == null || expediente.getExpDescripcion().trim().length() < 1) {
            throw new BusinessLogicException("La descripción del expediente es requerida");
        }
        if (numeroExpediente.trim().equals("") || parNombreExpediente == null) {
            throw new BusinessLogicException("La construcción del nombre requiere todos los campos exceptuando el ultimo");
        }

        String nombre = expediente.getTrdIdPrincipal().getCodigo() + "-" + Calendar.getInstance().get(Calendar.YEAR)
                + "-" + String.format("%03d", Integer.parseInt(numeroExpediente)) + "-" + parNombreExpediente.getParNombre();

        if (!opcionalNombre.trim().equals("")) {
            nombre += "-" + opcionalNombre;
        }

        expediente.setExpNombre(nombre.toUpperCase());

        List<Expediente> expedientesNombre = expedienteRepository.getByExpNombreAndDepId(expediente.getExpNombre(), usuario.getDependencia());
        if (!expedientesNombre.isEmpty()) {
            throw new BusinessLogicException("Ya existe un expediente con este nombre.");
        }

        expediente.setUsuCreacion(usuario);
        expediente.setUsuarioAsignado(Expediente.ASIGNADO_USUARIO);
        expediente.setDepId(usuario.getDependencia());
        expediente.setFecCreacion(new Date());
        expediente.setIndCerrado(false);
        expediente.setIndAprobadoInicial(false);
        expediente.setEstadoCambio(false);
        expediente.setIndAprobadoInicial(true);

        try {
            expediente = expedienteRepository.saveAndFlush(expediente);
        } catch (Exception e) {
            throw new BusinessLogicException("Ha ocurrido un error con la BD comuniquese");
        }
        expedienteTransicionService.crearTransicion(expediente,
                expedienteEstadoService.findById(ESTADO_INICIAL_EXPEDIENTE), usuario, null, null);
        return expediente;
    }

    /**
     * *
     * Enviá a aprobar un expediente
     *
     * @param expediente expediente a aprobar
     * @param usuarioSesion usuario quien aprueba
     */
    public void enviarAprobar(Expediente expediente, Usuario usuarioSesion) {
        expediente.setUsuarioAsignado(1);

        expedienteRepository.saveAndFlush(expediente);

        expedienteTransicionService.crearTransicion(expediente,
                expedienteEstadoService.findById(ESTADO_ENVIADO_APROBAR), usuarioSesion, null, null);

        Map<String, Object> model = new HashMap();
        model.put("usuario", usuarioSesion);
        model.put("expediente", expediente);

        try {
            notificacionService.enviarNotificacion(model, NOTIFICACION_EXPEDIENTE_POR_APROBAR, expediente.getDepId().getJefe());
        } catch (Exception ex) {
            Logger.getLogger(ExpUsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Obtiene el numero de expedientes por usuario y filtro
     *
     * @param usuId Identificador del usuario
     * @param filtro filtro de busqueda
     * @return Número de registros
     */
    public int obtenerCountExpedientesPorUsuario(Integer usuId, String filtro) {
        if (filtro != null && filtro.trim().length() > 0) {
            return expedienteRepository.findExpedientesDTOPorUsuarioYfiltroCount(usuId, filtro);
        }
        return expedienteRepository.findExpedientesDTOPorUsuarioCount(usuId);
    }

    /**
     * Obtiene los registros de los expedientes por usuario y filtro, de acuerdo
     * a la fila inicial y final.
     *
     * @param usuId Identificador del usuario
     * @param inicio Numero de registro inicial
     * @param fin Numero de registro final
     * @param filtro Filtro de busqueda
     * @return Lista de documentos
     */
    public List<ExpedienteDTO> obtenerExpedientesDTOPorUsuarioPaginado(Integer usuId, int inicio, int fin, String filtro) {
        List<ExpedienteDTO> expedientes = new ArrayList<>();
        List<Object[]> result;
        if (filtro != null && filtro.trim().length() > 0) {
            System.err.println("filtroservice= " + filtro);
            result = expedienteRepository.findExpedientesPorUsuarioYfiltroPaginado(usuId, inicio, fin, filtro);
        } else {
            result = expedienteRepository.findExpedientesPorUsuarioPaginado(usuId, inicio, fin);
        }

        if (result != null && !result.isEmpty()) {
            for (Object[] object : result) {
                ExpedienteDTO dTO = retornaExpedienteDTO(object);
                expedientes.add(dTO);
            }
        }
        return expedientes;
    }

    /**
     * *
     * Obtiene un expediente dado un usuario y un expediente
     *
     * @param usuId identificador del usuario
     * @param expId identificador del expediente
     * @return
     */
    public ExpedienteDTO obtieneExpedienteDTOPorUsuarioPorExpediente(Integer usuId, Long expId) {
        ExpedienteDTO expedienteDTO = null;
        System.err.println("Buscando expediente");
        List<Object[]> result = expedienteRepository.findExpedienteDtoPorUsuarioPorExpId(usuId, expId);

        if (result != null) {
            System.err.println("Retornando expediente1= " + result.size());
            for (Object[] object : result) {
                expedienteDTO = retornaExpedienteDTO(object);
            }
        }
        return expedienteDTO;
    }

    /**
     * **
     * Cambia el usuario creador de un expediente
     *
     * @param expediente expediente a cambiar
     * @param usuario usuario quíen va a ser el creador
     * @param usuarioSesion usuario quien realiza la aación
     */
    public void cambiarUsuarioCreador(Expediente expediente, Usuario usuario, Usuario usuarioSesion) {
        expediente.setUsuCreacion(usuario);
        expediente.setDepId(usuario.getDependencia());
        expediente.setUsuModificacion(usuarioSesion);
        expediente.setFecModificacion(new Date());
        expedienteTransicionService.crearTransicion(expediente,
                expedienteEstadoService.findById(ESTADO_ADMINISTRADOR_MODIFICADO), usuarioSesion, null, null);
        expedienteRepository.saveAndFlush(expediente);
    }

    /**
     * Metodo que permite retorna el Expediente de acuerdo a su identificador.
     *
     * @param expId Identificador del expediente
     * @return Expediente
     */
    public Expediente findOne(Long expId) {
        return expedienteRepository.findOne(expId);
    }

    /**
     * Metodo que permite aprobar los cambios de un expediente
     *
     * @param expediente Objeto expediente
     * @param usuarioSesion Usuario jefe de dependencia
     */
    public void aprobarExpediente(Expediente expediente, Usuario usuarioSesion) {
        expediente.setUsuarioAsignado(0);
        expediente.setEstadoCambio(true);
        expediente.setIndAprobadoInicial(true);
        expediente.setUsuModificacion(usuarioSesion);
        expediente.setFecModificacion(new Date());

        expUsuarioService.aprobarUsuariosPorExpediente(expediente, usuarioSesion);

        expedienteTransicionService.crearTransicion(expediente,
                expedienteEstadoService.findById(ESTADO_APROBADO), usuarioSesion, null, null);

        expedienteRepository.saveAndFlush(expediente);

        Map<String, Object> model = new HashMap();
        model.put("usuario", usuarioSesion);
        model.put("expediente", expediente);
        model.put("estado", "APROBADO");

        try {
            notificacionService.enviarNotificacion(model, NOTIFICACION_EXPEDIENTE_RESPUESTA, expediente.getUsuCreacion());
        } catch (Exception ex) {
            Logger.getLogger(ExpUsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodo que permite rechazar los cambios de un expediente
     *
     * @param expediente Objeto expediente
     * @param usuarioSesion Usuario jefe de dependencia
     */
    public void rechazarExpediente(Expediente expediente, Usuario usuarioSesion) {
        expediente.setUsuarioAsignado(0);
        expediente.setEstadoCambio(false);
        expediente.setUsuModificacion(usuarioSesion);
        expediente.setFecModificacion(new Date());

        expUsuarioService.rechazarUsuariosPorExpediente(expediente, usuarioSesion);

        expedienteTransicionService.crearTransicion(expediente,
                expedienteEstadoService.findById(ESTADO_RECHAZADO), usuarioSesion, null, null);

        expedienteRepository.saveAndFlush(expediente);

        Map<String, Object> model = new HashMap();
        model.put("usuario", usuarioSesion);
        model.put("expediente", expediente);
        model.put("estado", "RECHAZADO");

        try {
            notificacionService.enviarNotificacion(model, NOTIFICACION_EXPEDIENTE_RESPUESTA, expediente.getUsuCreacion());
        } catch (Exception ex) {
            Logger.getLogger(ExpUsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodo que permite modificar el tipo de un expediente a simple o
     * complejo.
     *
     * @param expediente Objeto expediente
     * @param usuarioSesion Usuario jefe de dependencia
     * @throws com.laamware.ejercito.doc.web.util.BusinessLogicException
     */
    public void modificarTipoExpediente(Expediente expediente, Usuario usuarioSesion) throws BusinessLogicException {
        //Modificar tipo de expediente simple
        if (expediente.getExpTipo() == EXPEDIENTE_SIMPLE) {
            expediente.setExpTipo(EXPEDIENTE_COMPLEJO);

            expedienteTransicionService.crearTransicion(expediente,
                    expedienteEstadoService.findById(ESTADO_CAMBIO_TIPO), usuarioSesion, null, null);

            expedienteRepository.saveAndFlush(expediente);
        } else {
            List<Trd> trdExpedienteDocumentos = trdService.getTrdExpedienteDocumentos(expediente);
            if (trdExpedienteDocumentos.isEmpty()) {
                List<ExpTrd> expTrds = expTrdService.findTrdsByExpediente(expediente);
                for (ExpTrd expTrd : expTrds) {
                    expTrdService.guardarTrdExpediente(expTrd, usuarioSesion, false);
                }

                expediente.setExpTipo(EXPEDIENTE_SIMPLE);

                expedienteTransicionService.crearTransicion(expediente,
                        expedienteEstadoService.findById(ESTADO_CAMBIO_TIPO), usuarioSesion, null, null);

                expedienteRepository.saveAndFlush(expediente);
            } else {
                throw new BusinessLogicException("Las TRDS del expediente ya se encuentran asociadas con documentos. Para realizar esta acción el expediente solo debe tener documentos de una sola TRD.");
            }
        }
    }

    /**
     * Retorna el objeto con los datos parámetricos del expediente
     *
     * @param object Objeto de la consulta
     * @return Datos del expediente
     */
    private ExpedienteDTO retornaExpedienteDTO(Object[] object) {
        ExpedienteDTO dTO = new ExpedienteDTO();
        dTO.setExpId(object[0] != null ? ((BigDecimal) object[0]).longValue() : null);
        dTO.setExpNombre(object[1] != null ? (String) object[1] : "");
        dTO.setFecCreacion(object[2] != null ? (Date) object[2] : null);
        dTO.setDepId(object[3] != null ? ((BigDecimal) object[3]).intValue() : null);
        dTO.setDepNombre(object[4] != null ? (String) object[4] : "");
        dTO.setTrdIdPrincipal(object[5] != null ? ((BigDecimal) object[5]).intValue() : null);
        dTO.setTrdNomIdPrincipal(object[6] != null ? (String) object[6] : "");
        dTO.setIndJefeDependencia(object[7] != null ? ((BigDecimal) object[7]).equals(BigDecimal.ONE) : false);
        dTO.setIndUsuCreador(object[8] != null ? ((BigDecimal) object[8]).equals(BigDecimal.ONE) : false);
        dTO.setIndAprobadoInicial(object[9] != null ? ((BigDecimal) object[9]).equals(BigDecimal.ONE) : false);
        dTO.setJefeDependencia(object[10] != null ? (String) object[10] : "");
        dTO.setUsuarioCreador(object[11] != null ? (String) object[11] : "");
        dTO.setExpTipoId(object[12] != null ? ((BigDecimal) object[12]).intValue() : null);
        dTO.setExpTipo(object[13] != null ? (String) object[13] : "");
        dTO.setExpDescripcion(object[14] != null ? (String) object[14] : "");
        dTO.setIndUsuarioAsignado(object[15] != null ? ((BigDecimal) object[15]).intValue() : null);
        dTO.setIndCerrado(object[16] != null ? ((BigDecimal) object[16]).equals(BigDecimal.ONE) : false);
        dTO.setNumTrdComplejo(object[17] != null ? ((BigDecimal) object[17]).intValue() : null);
        dTO.setNumUsuarios(object[18] != null ? ((BigDecimal) object[18]).intValue() : null);
        dTO.setNumDocumentos(object[19] != null ? ((BigDecimal) object[19]).intValue() : null);
        dTO.setIndIndexacion(object[20] != null ? ((BigDecimal) object[20]).equals(BigDecimal.ONE) : false);
        dTO.setFecMinDocumento(encuentrafechaMinimaExpediente(dTO.getExpId()));
        dTO.setFecMaxDocumento(encuentrafechaMaximaExpediente(dTO.getExpId()));
        return dTO;
    }

    /**
     * *
     * Obtienen los expedientes dado una trd y un usuario
     *
     * @param usuarioSesion Identificador del usuario
     * @param trd identificador de la trd
     * @return lista de expedientes compatibles
     */
    public List<Expediente> obtenerExpedientesIndexacionPorUsuarioPorTrd(Usuario usuarioSesion, Trd trd) {
        return expedienteRepository.findExpedientesIndexacionPorUsuarioPorTrd(usuarioSesion.getId(), trd.getId());
    }

    /**
     * *
     * verifica si el usuario tiene permiso de indexación sobre el documento
     *
     * @param usuario usuario acción
     * @param expediente expediente a consultar
     * @return true si tiene permisos de indexación false si no
     */
    public boolean permisoIndexacion(Usuario usuario, Expediente expediente) {
        List<ExpUsuario> expUsuarios = expUsuarioService.findByExpedienteAndUsuarioAndPermisoTruePermisoIndexacion(expediente, usuario);
        return !expUsuarios.isEmpty();
    }

    /**
     * *
     * Verifica si el usuario tiene permiso de administrador sobre el
     * expediente.
     *
     * @param usuario usuario a consultar
     * @param expediente expediente a consultar
     * @return true si tiene permiso false de lo contrario
     */
    public boolean permisoAdministrador(Usuario usuario, Expediente expediente) {
        if (usuario == null || expediente == null) {
            return false;
        }
        final Usuario jefeDep = expediente.getDepId().getJefe();
        if (!(jefeDep != null && jefeDep.getId().equals(usuario.getId()))
                && !expediente.getUsuCreacion().getId().equals(usuario.getId())) {
            return false;
        }
        return true;
    }

    /**
     * *
     * Verifica si el usuario es jefe de dependencia sobre el expediente.
     *
     * @param usuario usuario a consultar
     * @param expediente expediente a consultar
     * @return true si tiene permiso false de lo contrario
     */
    public boolean permisoJefeDependencia(Usuario usuario, Expediente expediente) {
        if (usuario == null || expediente == null) {
            return false;
        }
        final Usuario jefeDep = expediente.getDepId().getJefe();
        return jefeDep != null && jefeDep.getId().equals(usuario.getId());
    }

    /**
     * *
     * Verifica si el usuario tiene permiso de visualizar el expediente.
     *
     * @param usuario usuario a consultar
     * @param expediente expediente a consultar
     * @return true si tiene permiso false de lo contrario
     */
    public boolean permisoUsuarioLeer(Usuario usuario, Expediente expediente) {
        if (usuario == null || expediente == null) {
            return false;
        }

        List<ExpUsuario> expUsuarios = expUsuarioService.findByExpedienteAndUsuarioAndPermisoTrue(expediente, usuario);

        return !expUsuarios.isEmpty();
    }

    /**
     * *
     * Cierra un expediente
     *
     * @param expediente expediente a cerrar
     * @param usuarioSesion usuario quien realiza el cierre
     */
    public void cerrarExpediente(Expediente expediente, Usuario usuarioSesion) {
        expediente.setIndCerrado(true);
        expedienteRepository.saveAndFlush(expediente);
        expedienteTransicionService.crearTransicion(expediente,
                expedienteEstadoService.findById(ESTADO_EXPEDIENTE_CERRADO), usuarioSesion, null, null);

        Map<String, Object> model = new HashMap();
        model.put("usuario", usuarioSesion);
        model.put("expediente", expediente);

        try {
            notificacionService.enviarNotificacion(model, NOTIFICACION_EXPEDIENTE_CERRADO, expediente.getDepId().getJefe());
        } catch (Exception ex) {
            Logger.getLogger(ExpUsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * *
     * Abre el expediente
     *
     * @param expediente expediente a abrir
     * @param usuarioSesion usuario quien abre
     */
    public void abrirExpediente(Expediente expediente, Usuario usuarioSesion) {
        expediente.setIndCerrado(false);
        expedienteRepository.saveAndFlush(expediente);

        expedienteTransicionService.crearTransicion(expediente,
                expedienteEstadoService.findById(ESTADO_EXPEDIENTE_REABIERTO), usuarioSesion, null, null);

        Map<String, Object> model = new HashMap();
        model.put("usuario", usuarioSesion);
        model.put("expediente", expediente);

        try {
            notificacionService.enviarNotificacion(model, NOTIFICACION_EXPEDIENTE_REABIERTO, expediente.getDepId().getJefe());
        } catch (Exception ex) {
            Logger.getLogger(ExpUsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Obtiene el numero de registros de los documentos por expediente y
     * usuario.
     *
     * @param usuId Identificador del usuario
     * @param expId Identificador del expediente
     * @return Numero de registros.
     */
    public int findDocumentosByUsuIdAndExpIdCount(Integer usuId, Long expId) {
        return documentoRepository.findDocumentosByUsuIdAndExpIdCount(usuId, expId);
    }

    /**
     * Obtiene los registros de los documentos por usuario y expediente, de
     * acuerdo a la fila inicial y final.
     *
     * @param usuId Identificador del usuario
     * @param expId Identificador del expediente
     * @param inicio Registro inicial
     * @param fin Registro final
     * @return Lista de documentos.
     */
    public List<DocumentoExpDTO> findDocumentosByUsuIdAndExpIdPaginado(Integer usuId, Long expId, int inicio, int fin) {
        List<DocumentoExpDTO> expedientes = new ArrayList<>();
        List<Object[]> result = documentoRepository.findDocumentosByUsuIdAndExpIdPaginado(usuId, expId, inicio, fin);
        if (result != null && !result.isEmpty()) {
            for (Object[] object : result) {
                DocumentoExpDTO dTO = retornaDocumentoExpDTO(object);
                expedientes.add(dTO);
            }
        }
        return expedientes;
    }

    /**
     * Obtiene los registros de los documentos por usuario, expediente y
     * subserie.
     *
     * @param usuId Identificador del usuario
     * @param expId Identificador del expediente
     * @param trdId Identificador de la subserie
     * @return Lista documentos.
     */
    public List<DocumentoExpDTO> findDocumentosByUsuIdAndExpIdAndTrdId(Integer usuId, Long expId, int trdId) {
        List<DocumentoExpDTO> expedientes = new ArrayList<>();
        List<Object[]> result = documentoRepository.findDocumentosByUsuIdAndExpIdAndTrdId(usuId, expId, trdId);
        if (result != null && !result.isEmpty()) {
            for (Object[] object : result) {
                DocumentoExpDTO dTO = retornaDocumentoExpDTO(object);
                expedientes.add(dTO);
            }
        }
        return expedientes;
    }

    /**
     * Objeto que contiene información de los documentos del expediente
     *
     * @param object
     * @return DocumentoExpDTO
     */
    private DocumentoExpDTO retornaDocumentoExpDTO(Object[] object) {
        DocumentoExpDTO dTO = new DocumentoExpDTO();
        dTO.setPinId(object[0] != null ? (String) object[0] : "");
        dTO.setAsunto(object[1] != null ? (String) object[1] : "");
        dTO.setRadicado(object[2] != null ? (String) object[2] : "");
        dTO.setClasificacion(object[3] != null ? (String) object[3] : "");
        dTO.setIndVisualizacion(object[4] != null ? ((BigDecimal) object[4]).equals(BigDecimal.ONE) : false);
        dTO.setIndJefeDependencia(object[5] != null ? ((BigDecimal) object[5]).equals(BigDecimal.ONE) : false);
        dTO.setDocId(object[6] != null ? (String) object[6] : "");
        return dTO;
    }

    /**
     * Metodo que permite identificar las fecha minima de los documentos de un
     * expediente.
     *
     * @param expId Identificador del expediente
     * @return Fecha minima del expediente
     */
    public Date encuentrafechaMinimaExpediente(Long expId) {
        Object fecMin = documentoRepository.encuentrafechaMinimaExpediente(expId);
        if (fecMin != null) {
            return (Date) fecMin;
        }
        return null;
    }

    /**
     * Metodo que permite identificar la fecha maxima de los documentos de un
     * expediente.
     *
     * @param expId Identificador del expediente
     * @return Fecha máxima del expediente
     */
    public Date encuentrafechaMaximaExpediente(Long expId) {
        Object fecMax = documentoRepository.encuentrafechaMaximaExpediente(expId);
        if (fecMax != null) {
            return (Date) fecMax;
        }
        return null;
    }
}
