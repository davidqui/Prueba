package com.laamware.ejercito.doc.web.entity;

import java.util.regex.Pattern;

/**
 * Clase que define algunas constantes que se usan dentro de la aplicación web
 *
 * @author acpreda
 *
 */
public class AppConstants {

    /**
     * Es el nombre que se le da a los mensajes flash cuando se trata de
     * informar al usuario un resultado exitoso.
     */
    public static final String FLASH_SUCCESS = "FLASH_SUCCESS";

    /**
     * Es el nombre que se le da a los mensajes flash cuando se trata de
     * informar al usuario algo para que tenga en cuenta.
     */
    public static final String FLASH_INFO = "FLASH_INFO";

    /**
     * Es el nombre que se le da a los mensajes flash cuando se trata de
     * informar al usuario una advertencia.
     */
    public static final String FLASH_WARN = "FLASH_WARN";

    /**
     * Es el nombre que se le da a los mensajes flash cuando se trata de
     * informar al usuario un error.
     */
    public static final String FLASH_ERROR = "FLASH_ERROR";

    /**
     * Expresión regular para validar claves alfanuméricas como los nombres de
     * rol, nombres de usuario, claves de menú, etc.
     */
    public static final Pattern REGEX_PATTERN_ALPHANUMERIC_KEY = Pattern.compile("[a-zA-Z0-9_]+");

    /**
     * Formato de fecha predeterminado
     */
    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd";

    /**
     * Formato de fecha predeterminado
     */
    // 2017-02-22 jgarcia@controltechcg.com Issue #141 (También en todos los
    // templates *.ftl donde se encuentra el mismo formato.
    public static final String DATE_TIME_FORMAT_DEFAULT = "yyyy-MM-dd hh:mm a";

    /**
     * constante de rol anonymous
     */
    public static final String ROL_ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String MIME_TYPE_PDF = "application/pdf";

    /**
     * Grado por defecto que se asigna cuando no hay grado asociado al usuario
     */
    public static final String SIN_GRADO = " ";

    /**
     * Prefijo de los roles de administrador
     */
    public static final String PREFIX_ROLE_ADMIN = "ADMIN_";

    /**
     * Prefijo de los roles de archivo
     */
    public static final String PREFIX_ROLE_ARCHIVO = "ARCHIVO_";

    /**
     * Rol de Archivo central
     */
    public static final String ARCHIVO_CENTRAL = "ARCHIVO_CENTRAL";
    public static final String PATH_ARCHIVO_CENTRAL = "/archivo";

    /**
     * Rol de administración de perfiles
     */
    public static final String ADMIN_PERFILES = "ADMIN_PERFILES";
    public static final String PATH_ADMIN_PERFILES = "/admin/profiles";

    /**
     * Rol de administración de auditoria
     */
    public static final String ADMIN_AUDITORIA = "ADMIN_AUDITORIA";
    public static final String PATH_ADMIN_AUDITORIA = "/admin/audit-security";

    /**
     * Rol de administración de juego
     */
    public static final String ADMIN_CAPACITACION = "ADMIN_CAPACITACION";
    public static final String PATH_ADMIN_CAPACITACION = "/admin/juego";

    /**
     * Rol de administración de Depedencias
     */
    public static final String ADMIN_DEPENDENCIAS = "ADMIN_DEPENDENCIAS";
    public static final String PATH_ADMIN_DEPENDENCIAS = "/dependencias";

    /**
     * Rol de administración de Usuarios
     */
    public static final String ADMIN_USUARIOS = "ADMIN_USUARIOS";
    public static final String PATH_ADMIN_USUARIOS = "/usuarios";

    /**
     * Rol de administración de Tipos Destinatario
     */
    public static final String ADMIN_TIPOS_DESTINATARIO = "ADMIN_TIPOS_DESTINATARIO";
    public static final String PATH_ADMIN_TIPOS_DESTINATARIO = "/admin/tipo-destinatario";

    /**
     * Rol de administración de Destinatarios
     */
    public static final String ADMIN_DESTINATARIOS = "ADMIN_DESTINATARIOS";
    public static final String PATH_ADMIN_DESTINATARIOS = "/admin/destinatarios";

    /**
     * Rol de administración de Trd
     */
    public static final String ADMIN_TRD = "ADMIN_TRD";
    public static final String PATH_ADMIN_TRD = "/admin/trd";

    /**
     * Rol de administración de Clasificaciones
     */
    public static final String ADMIN_CLASIFICACIONES = "ADMIN_CLASIFICACIONES";
    public static final String PATH_ADMIN_CLASIFICACIONES = "/admin/clasificacion";

    /**
     * Rol de administración de plantilla
     */
    public static final String ADMIN_PLANTILLAS = "ADMIN_PLANTILLAS";
    public static final String PATH_ADMIN_PLANTILLAS = "/admin/plantilla";

    /**
     * Rol de administración de proceso
     */
    public static final String ADMIN_PROCESOS = "ADMIN_PROCESOS";
    public static final String PATH_ADMIN_PROCESOS = "/admin/proceso";

    /**
     * Rol de administración de tipologia
     */
    public static final String ADMIN_TIPOLOGIA = "ADMIN_TIPOLOGIA";
    public static final String PATH_ADMIN_TIPOLOGIA = "/admin/tipologia";

    /**
     * Rol de administración de tipo-documento
     */
    public static final String ADMIN_TIPO_DOCUMENTO = "ADMIN_TIPO_DOCUMENTO";
    public static final String PATH_ADMIN_TIPO_DOCUMENTO = "/admin/tipo-documento";

    /**
     * Rol de administración de bandeja
     */
    public static final String ADMIN_BANDEJAS = "ADMIN_BANDEJAS";
    public static final String PATH_ADMIN_BANDEJAS = "/admin/bandeja";

    /**
     * Rol de administración de grados
     */
    public static final String ADMIN_GRADOS = "ADMIN_GRADOS";
    public static final String PATH_ADMIN_GRADOS = "/grados";

    /**
     * Rol de administración de /admin/informes
     */
    public static final String ADMIN_INFORMES = "ADMIN_INFORMES";
    public static final String PATH_ADMIN_INFORMES = "/admin/informes";

    /**
     * Rol de administración de formatos
     */
    public static final String ADMIN_FORMATOS = "ADMIN_FORMATOS";
    public static final String PATH_ADMIN_FORMATOS = "/admin/formatos";

    public static final String ADMIN_LOG = "ADMIN_LOG";
    public static final String PATH_ADMIN_LOG = "/admin/logs";

    /**
     * Rol de administración de cargos
     */
    public static final String ADMIN_CARGOS = "ADMIN_CARGOS";
    public static final String PATH_ADMIN_CARGOS = "/admin/cargos";

    /**
     * Rol de administración de dominio
     */
    public static final String ADMIN_DOMINIO = "ADMIN_DOMINIO";
    public static final String PATH_ADMIN_DOMINIO = "/admin/dominio";
    /**
     * Rol de administración de nombre de expediente
     */
    public static final String ADMIN_PAR_NOMBRE_EXPEDIENTE = "ADMIN_PAR_NOMBRE_EXPEDIENTE";
    public static final String PATH_ADMIN_PAR_NOMBRE_EXPEDIENTE = "/admin/parnombrexpediente";

    /**
     * Rol de administración de tematica
     */
    public static final String ADMIN_TEMATICA = "ADMIN_TEMATICA";
    public static final String PATH_ADMIN_TEMATICA = "/admin/tematica";
    
    /**
     * Rol de administración de Recursos Multimedia
     */
    public static final String ADMIN_RECURSO_MULTIMEDIA = "ADMIN_RECURSO_MULTIMEDIA";
    public static final String PATH_ADMIN_RECURSO_MULTIMEDIA = "/admin/recursoMultimedia";

    /**
     * Rol para búsqueda de documentos por firma/envío UUID.
     */
    /*
     * 2018-05-08 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech)
     * feature-160.
     */
    public static final String BUSCAR_X_DOC_FIRMA_ENVIO_UUID = "BUSCAR_X_DOC_FIRMA_ENVIO_UUID";
}
