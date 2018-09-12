package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.DocumentoDTO;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Estado;
import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.enums.DocumentoActaEstado;
import com.laamware.ejercito.doc.web.entity.Rol;
import com.laamware.ejercito.doc.web.repo.RolRepository;
import com.laamware.ejercito.doc.web.util.DateUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Servicio que permite realizar tareas asociadas al motor de busqueda.
 *
 * @author edison.gonzalez@controltechcg.com
 * @since November 11, 2017
 *
 */
/*
 * 2017-11-09 edison.gonzalez@controltechcg.com Issue #136 (SICDI-Controltech)
 */
@Service
public class ConsultaService {

    private static final Logger LOG = Logger.getLogger(ConsultaService.class.getName());

    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private DependenciaService dependenciaService;
    
    /*
     * 2018-05-08 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech) feature-160.
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public int retornaCountConsultaMotorBusqueda(String asignado, String asunto, String fechaInicio, String fechaFin, String radicado, String destinatario, Integer clasificacion, Integer dependenciaDestino, Integer dependenciaOrigen, boolean sameValue, Integer usuarioID, String firmaUUID, boolean puedeBuscarXDocFirmaEnvioUUID, String tipoProceso) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Operador lógico a utilizar en la sentencia SQL de la consulta general.
     *
     * @author jgarcia@controltechcg.com
     * @since Feb 14, 2017
     */
    // Issue #128 corrección consulta con único valor de filtro general.
    private enum SentenceOperator {
        AND, OR;
    }

    /**
     * Metodo que permite retornar la cantidad de registros de la consulta del
     * motor de busqueda, de acuerdo a los filtros.
     *
     * @param asignado
     * @param asunto
     * @param fechaInicio
     * @param fechaFin
     * @param radicado
     * @param destinatario
     * @param clasificacion
     * @param dependenciaDestino
     * @param dependenciaOrigen
     * @param sameValue
     * @param usuarioID
     * @param firmaUUID
     * @param puedeBuscarXDocFirmaEnvioUUID
     * @param cargosIDs
     * @param tipoProceso
     * @return
     */
    /*
     * 2018-05-08 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech)
     * feature-160: Parámetros firmaUUID y puedeBuscarXDocFirmaEnvioUUID.
     *
     * 2018-06-05 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162: Arreglo de IDs de cargos del usuario en sesión (cargosIDs).
     */
    @Deprecated
    public int retornaCountConsultaMotorBusqueda(final String asignado, final String asunto, final String fechaInicio, final String fechaFin,
            final String radicado, final String destinatario, final Integer clasificacion, final Integer dependenciaDestino, final Integer dependenciaOrigen,
            final boolean sameValue, final Integer usuarioID, final String firmaUUID, final boolean puedeBuscarXDocFirmaEnvioUUID, final Integer[] cargosIDs, 
            final Integer tipoProceso, final boolean permisoAdministradorArchivo) {
        StringBuilder sql = retornaConsultaPrincipal();
        LinkedList<Object> parameters = armaConsulta(sql, asignado, asunto, fechaInicio, fechaFin, radicado, destinatario, clasificacion, dependenciaDestino,
                dependenciaOrigen, sameValue, usuarioID, firmaUUID, puedeBuscarXDocFirmaEnvioUUID, cargosIDs, tipoProceso, permisoAdministradorArchivo);

//        LOG.info("**************************************************");
//        LOG.info("sql = " + sql);
//        LOG.info("parameters = " + parameters);
//        LOG.info("**************************************************");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String count = ""
                + "select count(1)\n"
                + "from(\n"
                + sql.toString()
                + ")\n";

        LOG.log(Level.FINEST, sql.toString());

        int i = 0;
        try {
            i = jdbcTemplate.queryForObject(count, parameters.toArray(), Integer.class);
        } catch (DataAccessException e) {
        }
        LOG.log(Level.FINEST, "retorna count");
        return i;
    }

    /**
     * Metodo que permite retornar la lista de registros de la consulta del
     * motor de busqueda, de acuerdo a los filtros.
     *
     * @param asignado
     * @param asunto
     * @param fechaInicio
     * @param fechaFin
     * @param radicado
     * @param destinatario
     * @param clasificacion
     * @param dependenciaDestino
     * @param dependenciaOrigen
     * @param sameValue
     * @param usuarioID
     * @param firmaUUID
     * @param puedeBuscarXDocFirmaEnvioUUID
     * @param cargosIDs
     * @param inicio
     * @param fin
     * @param tipoProceso
     * @return
     */
    /*
     * 2018-05-08 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech)
     * feature-160: Parámetros firmaUUID y puedeBuscarXDocFirmaEnvioUUID.
     *
     * 2018-06-05 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162: Arreglo de IDs de cargos del usuario en sesión (cargosIDs).
     */
    /*
     * 2018-07-09 samuel.delgado@controltechcg.com Issue #177 (SICDI-Controltech)
     * feature-160: Parámetro tipoProceso
     */
    @Deprecated
    public List<DocumentoDTO> retornaConsultaMotorBusqueda(final String asignado, final String asunto, final String fechaInicio, final String fechaFin,
            final String radicado, final String destinatario, final Integer clasificacion, final Integer dependenciaDestino, final Integer dependenciaOrigen,
            final boolean sameValue, final Integer usuarioID, final String firmaUUID, final boolean puedeBuscarXDocFirmaEnvioUUID, final Integer[] cargosIDs,
            final int inicio, final int fin, final Integer tipoProceso, final boolean permisoAdministradorArchivo) {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        StringBuilder sql = retornaConsultaPrincipal();
        LinkedList<Object> parameters = armaConsulta(sql, asignado, asunto, fechaInicio, fechaFin, radicado, destinatario, clasificacion, dependenciaDestino, dependenciaOrigen,
                sameValue, usuarioID, firmaUUID, puedeBuscarXDocFirmaEnvioUUID, cargosIDs, tipoProceso, permisoAdministradorArchivo);

        LOG.info("##################################################");
        LOG.info("sql = " + sql);
        LOG.info("parameters = " + parameters);
        LOG.info("##################################################");
        String consulta = ""
                + "select *\n"
                + "from(\n"
                + "     select a.*, rownum num_lineas\n"
                + "     from(\n"
                + sql.toString()
                + "     ORDER BY DOC.CUANDO_MOD DESC\n"
                + "     ) a\n"
                + ") \n"
                + "where num_lineas >= ? and num_lineas <= ?\n";

        parameters.add(inicio);
        parameters.add(fin);

        return jdbcTemplate.query(consulta, parameters.toArray(), new RowMapper<DocumentoDTO>() {
            @Override
            public DocumentoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                DocumentoDTO c = new DocumentoDTO(rs.getString("id"), rs.getString("idInstancia"), rs.getString("asunto"), rs.getDate("cuandoMod"), rs.getString("nombreProceso"),
                        rs.getString("nombreEstado"), rs.getString("nombreUsuarioAsignado"), rs.getString("nombreUsuarioEnviado"), rs.getString("nombreUsuarioElabora"),
                        rs.getString("nombreUsuarioReviso"), rs.getString("nombreUsuarioVbueno"), rs.getString("nombreUsuarioFirma"), rs.getString("nombreClasificacion"),
                        rs.getString("numeroRadicado"), rs.getString("unidadOrigen"), rs.getString("unidadDestino"), false);
                return c;
            }
        });
    }

    /**
     * Metodo que arma los filtros de la consulta del motor de busqueda de
     * acuerdo a los filtros de la pantalla.
     *
     * @param sql
     * @param asignado
     * @param asunto
     * @param fechaInicio
     * @param fechaFin
     * @param radicado
     * @param destinatario
     * @param clasificacion
     * @param sameValue
     * @param dependenciaDestino
     * @param dependenciaOrigen
     * @param usuarioID
     * @param firmaUUID
     * @param puedeBuscarXDocFirmaEnvioUUID
     * @param cargosIDs
     * @param tipoProceso
     * @return
     */
    /*
     * 2018-05-08 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech)
     * feature-160: Parámetros firmaUUID y puedeBuscarXDocFirmaEnvioUUID.
     * 
     * 2018-06-05 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162: Arreglo de IDs de cargos del usuario en sesión (cargosIDs).
     */
    @Deprecated
    public LinkedList<Object> armaConsulta(final StringBuilder sql, final String asignado, final String asunto, final String fechaInicio,
            final String fechaFin, final String radicado, final String destinatario, final Integer clasificacion, final Integer dependenciaDestino,
            final Integer dependenciaOrigen, final boolean sameValue, final Integer usuarioID, final String firmaUUID, final boolean puedeBuscarXDocFirmaEnvioUUID,
            final Integer[] cargosIDs, final Integer tipoProceso, final boolean permisoAdministradorArchivo) {

        // Issue #128
        SentenceOperator operator = (sameValue) ? SentenceOperator.OR : SentenceOperator.AND;
        LinkedList<Object> parameters = new LinkedList<>();

        /*
        * 2018-07-05 samuel.delgado@controltechcg.com Issue #177 (SICDI-Controltech) feature-177.
        * La nueva consulta requiere de estos parametros.
        */
//        parameters.add(Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS);
//        parameters.add(Proceso.ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA);
//        parameters.add(Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS);
//        parameters.add(usuarioID);
//        parameters.add(usuarioID);
//        parameters.add(usuarioID);
//        parameters.add(Proceso.ID_TIPO_PROCESO_REGISTRO_ACTAS);
//        parameters.add(usuarioID);
//        parameters.add(DocumentoActaEstado.ACTA_DIGITALIZADA.getId());
//        parameters.add(usuarioID);
//        parameters.add(DocumentoActaEstado.ACTA_DIGITALIZADA.getId());
//        parameters.add(usuarioID);
//        parameters.add(DocumentoActaEstado.ACTA_DIGITALIZADA.getId());

        
        /*
         * 2017-05-23 jgarcia@controltechcg.com Issue #91 (SICDI-Controltech)
         * hotfix-91: Lista de estados para no tener en cuenta en la consulta de
         * documentos a través de Búsqueda Avanzada.
         *
         * 2018-06-05 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
         * feature-162: Adición de estados de anulación correspondientes al
         * proceso de Registro de Actas.
         */
        final Integer[] estadosNoAplican = {Estado.ANULADO, Estado.ANULADO_NEW, DocumentoActaEstado.ANULADO.getId()};
        sql.append("AND INSTANCIA.PES_ID NOT IN (");
        for (int index = 0; index < estadosNoAplican.length; index++) {
            final Integer estadoID = estadosNoAplican[index];
            sql.append("?").append((index < estadosNoAplican.length - 1) ? ", " : "");
            parameters.add(estadoID);
        }
        sql.append(")\n");
        
        /*
        * 2018-07-05 samuel.delgado@controltechcg.com Issue #177 (SICDI-Controltech) feature-177.
        * se agrega validación de rol del usuario si posee el rol de administrador de archivo no 
        * entra a verificar si estuvo involucrado en el documento.
        *
        * 2018-08-06 samuel.delgado@controtechcg.com Issue #181 (SICDI-Controltech) feature-181.
        *   se cambia la validación del método se sube de nivel de llamado.
        */
        if (!permisoAdministradorArchivo) {
            /*
             * 2017-10-31 edison.gonzalez@controltechcg.com Issue #136: Ajuste para
             * filtrar si el usuario dio vistos bueno.
             *
             * 2018-06-05 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
             * feature-162: Filtro de usuario asociado a una acta y validación del
             * cargo. En caso que el usuario sea un asociado, este únicamente podrá
             * consultar el acta cuando esta haya sido digitalizada. Modificación
             * para visualización de actas tras transferencia de archivo.
             */
            sql.append(" AND ((PROCESO.PRO_ID IN (?, ?, ?) AND (DOC.USU_ID_ELABORA = ? OR DOC.USU_ID_FIRMA = ? OR USU.USU_ID = ?)) \n");
            sql.append(" OR ((PROCESO.PRO_ID IN (?) AND ((USU.USU_ID = ? AND INSTANCIA.PES_ID <> ?) OR (DOCUMENTO_DEPENDENCIA.QUIEN = ? AND INSTANCIA.PES_ID = ?) OR (USUARIO_X_DOCUMENTO_ACTA.USU_ID = ? AND INSTANCIA.PES_ID = ? \n");
            parameters.add(Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS);
            parameters.add(Proceso.ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA);
            parameters.add(Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS);
            parameters.add(usuarioID);
            parameters.add(usuarioID);
            parameters.add(usuarioID);
            parameters.add(Proceso.ID_TIPO_PROCESO_REGISTRO_ACTAS);
            parameters.add(usuarioID);
            parameters.add(DocumentoActaEstado.ACTA_DIGITALIZADA.getId());
            parameters.add(usuarioID);
            parameters.add(DocumentoActaEstado.ACTA_DIGITALIZADA.getId());
            parameters.add(usuarioID);
            parameters.add(DocumentoActaEstado.ACTA_DIGITALIZADA.getId());

            if (cargosIDs != null && cargosIDs.length > 0) {
                sql.append(" AND USUARIO_X_DOCUMENTO_ACTA.CAR_ID IN (");
                for (int index = 0; index < cargosIDs.length; index++) {
                    final Integer cargoID = cargosIDs[index];
                    sql.append("?").append((index < cargosIDs.length - 1) ? ", " : "");
                    parameters.add(cargoID);
                }
                sql.append(")");
            }
            sql.append("))))) \n");
        }
        // Issue #128
        sql.append("AND ( \n");

        // Issue #128
        boolean hasConditions = false;

        if (StringUtils.isNotBlank(asignado)) {
            // Issue #128
            sql.append("( \n");
            sql.append("		LOWER(USU_ULT_ACCION.USU_LOGIN) 	= 		LOWER( ? ) \n");
            sql.append("	OR 	LOWER(USU_ULT_ACCION.USU_DOCUMENTO)	= 		LOWER( ? ) \n");
            sql.append("	OR 	LOWER(USU_ULT_ACCION.USU_NOMBRE) 	LIKE	LOWER( ? ) \n");
            sql.append("	OR 	LOWER(USU_ULT_ACCION.USU_TELEFONO) 	= 		LOWER( ? ) \n");
            sql.append(") \n");

            parameters.add(asignado.trim());
            parameters.add(asignado.trim());
            parameters.add("%" + asignado.trim() + "%");
            parameters.add(asignado.trim());
            hasConditions = true;
        }

        if (StringUtils.isNotBlank(asunto)) {
            // Issue #128
            sql.append(hasConditions ? operator.name() : "").append(" LOWER(DOC.DOC_ASUNTO) LIKE LOWER( ? ) \n");
            parameters.add("%" + asunto.trim() + "%");
            hasConditions = true;
        }

        /*
         * 2017-05-30 jgarcia@controltechcg.com Issue #96 (SICDI-Controltech)
         * hotfix-96: Corrección para construir la sentencia SQL de la búsqueda
         * avanzada, según la existencia o no de los filtros de fecha,
         * estableciendo la condición de concatenación de las condiciones.
         */
        Date fInicio = null;
        if (StringUtils.isNotBlank(fechaInicio)) {
            fInicio = parseFilterDate(fechaInicio, DateUtil.SetTimeType.START_TIME);
        }

        Date fFin = null;
        if (StringUtils.isNotBlank(fechaFin)) {
            fFin = parseFilterDate(fechaFin, DateUtil.SetTimeType.END_TIME);
        }

        /*
         * 2017-02-15 jgarcia@controltechcg.com Issue #142: Se separa la
         * validación realizada sobre los campos de fecha para que la búsqueda
         * sea independiente y no se necesite siempre de los dos campos para
         * filtrar. Se utilizan los campos de fecha indicados en el issue, según
         * el proceso asociado al documento.
         *
         * 2017-05-30 jgarcia@controltechcg.com Issue #96 (SICDI-Controltech)
         * hotfix-96: Corrección para construir la sentencia SQL de la búsqueda
         * avanzada, según la existencia o no de los filtros de fecha,
         * estableciendo la condición de concatenación de las condiciones.
         */
        if (fInicio != null) {
            sql.append(hasConditions ? operator.name() : "").append(" ((CASE WHEN INSTANCIA.PRO_ID = ").append(Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS).append(" THEN DOC.CUANDO ELSE DOCFIRMA.CUANDO END) >= ?) \n");

            parameters.add(fInicio);

            hasConditions = true;
        }

        /*
         * 2017-02-15 jgarcia@controltechcg.com Issue #142: Se separa la
         * validación realizada sobre los campos de fecha para que la búsqueda
         * sea independiente y no se necesite siempre de los dos campos para
         * filtrar. Se utilizan los campos de fecha indicados en el issue, según
         * el proceso asociado al documento.
         *
         * 2017-05-30 jgarcia@controltechcg.com Issue #96 (SICDI-Controltech)
         * hotfix-96: Corrección para construir la sentencia SQL de la búsqueda
         * avanzada, según la existencia o no de los filtros de fecha,
         * estableciendo la condición de concatenación de las condiciones.
         */
        if (fFin != null) {
            if (fInicio == null) {
                sql.append((hasConditions ? operator.name() : ""));
            } else {
                sql.append(SentenceOperator.AND.name());
            }

            sql.append(" ((CASE WHEN INSTANCIA.PRO_ID = ").append(Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS).append(" THEN DOC.CUANDO ELSE DOCFIRMA.CUANDO END) <= ?) \n");
            parameters.add(fFin);
            hasConditions = true;
        }

        if (StringUtils.isNotBlank(radicado)) {
            // Issue #128
            sql.append(hasConditions ? operator.name() : "").append(" LOWER(DOC.DOC_RADICADO) LIKE LOWER( ? ) \n");

            parameters.add("%" + radicado.trim() + "%");

            hasConditions = true;
        }

        if (StringUtils.isNotBlank(destinatario)) {
            // Issue #128
            sql.append(hasConditions ? operator.name() : "").append(" ( \n");
            sql.append("		LOWER(DEP.DEP_NOMBRE) 				LIKE LOWER( ? ) \n");
            sql.append("	OR 	LOWER(DEP.DEP_SIGLA) 				= 	 LOWER( ? ) \n");
            sql.append("	OR 	LOWER(USU_DEP_JEFE.USU_DOCUMENTO)	=    LOWER( ? ) \n");
            sql.append("	OR 	LOWER(USU_DEP_JEFE.USU_NOMBRE) 		LIKE LOWER( ? ) \n");
            sql.append("	OR 	LOWER(USU_DEP_JEFE.USU_TELEFONO) 	=    LOWER( ? ) \n");
            sql.append(") \n");

            parameters.add("%" + destinatario.trim() + "%");
            parameters.add(destinatario.trim());
            parameters.add(destinatario.trim());
            parameters.add("%" + destinatario.trim() + "%");
            parameters.add(destinatario.trim());

            hasConditions = true;
        }

        if (clasificacion != null) {
            // Issue #128
            sql.append(hasConditions ? operator.name() : "").append(" DOC.CLA_ID = ? \n");
            parameters.add(clasificacion);
            hasConditions = true;
        }
        
        // Issue #177
        if (tipoProceso != null) {
            sql.append(hasConditions ? operator.name() : "").append(" INSTANCIA.PRO_ID = ? \n");
            parameters.add(tipoProceso);
            hasConditions = true;
        }

        // Issue #77
        if (dependenciaDestino != null) {
            // Issue #128
            sql.append(hasConditions ? operator.name() : "").append(" DOC.DEP_ID_DES = ? \n");
            parameters.add(dependenciaDestino);
            hasConditions = true;
        }

        // Issue #136
        if (dependenciaOrigen != null) {
            // Issue #136
            sql.append(hasConditions ? operator.name() : "").append(" DEP_ORIGEN.DEP_ID = ? \n");
            parameters.add(dependenciaOrigen);
            hasConditions = true;
        }

        /*
         * 2018-05-08 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech)
         * feature-160: Únicamente puede buscar por el UUID de firma/envío si el
         * usuario tiene privilegio para realizar búsqueda con este filtro.
         */
        if (puedeBuscarXDocFirmaEnvioUUID && firmaUUID != null && !firmaUUID.trim().isEmpty()) {
            sql.append(hasConditions ? operator.name() : "").append(" DOC.DOC_FIRMA_ENVIO_UUID = ? \n");
            parameters.add(firmaUUID);
            hasConditions = true;
        }

        // Issue #128
        sql.append(" ) \n");

        return parameters;
    }

    /**
     * Parsea el dato de la fecha enviado desde la interfaz, a un valor de
     * fecha, con la hora correspondiente según sí es fecha inicial o fecha
     * final.
     *
     * @param dateValue Valor recibido desde la interfaz.
     * @param setTimeType Tipo de fecha.
     * @return Valor de la fecha, o {@code null} en caso que el valor recibido
     * sea {@code null} o no se pueda parsear.
     * @see #DATE_FORMAT_YYYYMMDD
     */
    /*
     * 2017-05-30 jgarcia@controltechcg.com Issue #96 (SICDI-Controltech)
     * hotfix-96: Corrección para construir la sentencia SQL de la búsqueda
     * avanzada, según la existencia o no de los filtros de fecha, estableciendo
     * la condición de concatenación de las condiciones.
     */
    private Date parseFilterDate(String dateValue, DateUtil.SetTimeType setTimeType) {
        if (dateValue == null) {
            return null;
        }

        try {
            return DateUtil.setTime(new SimpleDateFormat(DATE_FORMAT).parse(dateValue.trim()), setTimeType);
        } catch (ParseException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Metodo que permite retornar la consulta principal del motor de busqueda.
     */
    private StringBuilder retornaConsultaPrincipal() {
        /*
         * 2017-02-15 jgarcia@controltechcg.com Issue #142: Nuevas asociaciones
         * para obtener los campos de fechas solicitados.
         * 
         * 2017-02-17 jgarcia@controltechcg.com Issue #128: Se corrige sentencia
         * SQL para evitar repetición de la información.
         *
         * 2017-10-31 edison.gonzalez@controltechcg.com Issue #136: Se agregan
         * nuevos campos, para realizar una sola consulta.
         *
         * 2018-06-05 jgarcia@controltechcg.com Issue #162: Nuevas asociaciones
         * correspondientes al proceso de Registro de Actas
         * (USUARIO_X_DOCUMENTO_ACTA, DOCUMENTO_DEPENDENCIA).
         * 2017-02-15 jgarcia@controltechcg.com Issue #142: Nuevas asociaciones
         * para obtener los campos de fechas solicitados.
         *
         * 2018-07-05 samuel.delgado@controltechcg.com Issue #177: se realiza una anotación
         * en una nueva columna validando si el usuario elaboro o firmo o modifico si es así
         * la columna toma el valor de 1 de lo contrario valdra 0. 
         */
        return new StringBuilder("\n"
                + " SELECT DISTINCT "
                + "      INSTANCIA.PIN_ID                                                                                        \"idInstancia\", \n"
                + "      DOC.DOC_ID                                                                                              \"id\", \n"
                + "      DOC.DOC_ASUNTO                                                                                          \"asunto\", \n"
                + "      DOC.CUANDO_MOD                                                                                          \"cuandoMod\", \n"
                + "      PROCESO.PRO_NOMBRE                                                                                      \"nombreProceso\", \n"
                + "      EST.PES_NOMBRE                                                                                          \"nombreEstado\", \n"
                + "      DECODE(USU_ASIGNADO.USU_NOMBRE, NULL, NULL,USU_ASIGNADO.USU_GRADO||'. '||USU_ASIGNADO.USU_NOMBRE)       \"nombreUsuarioAsignado\", \n"
                + "      DECODE(USU_ULT_ACCION.USU_NOMBRE, NULL, NULL,USU_ULT_ACCION.USU_GRADO||'. '||USU_ULT_ACCION.USU_NOMBRE) \"nombreUsuarioEnviado\", \n"
                + "      DECODE(USU_ELABORA.USU_NOMBRE, NULL, NULL,USU_ELABORA.USU_GRADO||'. '||USU_ELABORA.USU_NOMBRE)          \"nombreUsuarioElabora\", \n"
                + "      DECODE(USU_REVISO.USU_NOMBRE, NULL, NULL,USU_REVISO.USU_GRADO||'. '||USU_REVISO.USU_NOMBRE)             \"nombreUsuarioReviso\", \n"
                + "      DECODE(USU_VBUENO.USU_NOMBRE, NULL, NULL,USU_VBUENO.USU_GRADO||'. '||USU_VBUENO.USU_NOMBRE)             \"nombreUsuarioVbueno\", \n"
                + "      DECODE(USU_FIRMA.USU_NOMBRE, NULL, NULL,USU_FIRMA.USU_GRADO||'. '||USU_FIRMA.USU_NOMBRE)                \"nombreUsuarioFirma\", \n"
                + "      CLASIFICACION.CLA_NOMBRE                                                                                \"nombreClasificacion\", \n"
                + "      DOC.DOC_RADICADO                                                                                        \"numeroRadicado\", \n"
                + "      DEP_ORIGEN.DEP_ORI_NOMBRE                                                                               \"unidadOrigen\", \n"
                + "      case when INSTANCIA.PRO_ID = ? THEN DES_EXTERNO.SIGLA else DEP_DESTINO.DEP_DES_NOMBRE end               \"unidadDestino\", \n"
                + "       nvl((select 1\n" 
                + "            from dual\n" 
                + "            where (INSTANCIA.PRO_ID IN (?, ?, ?) AND (DOC.USU_ID_ELABORA = ? OR DOC.USU_ID_FIRMA = ? OR USU.USU_ID = ?)"
                + "                    OR ((INSTANCIA.PRO_ID IN (?) AND ((USU.USU_ID = ? AND INSTANCIA.PES_ID <> ?) OR (DOCUMENTO_DEPENDENCIA.QUIEN = ? AND INSTANCIA.PES_ID = ?)"
                + "                    OR (USUARIO_X_DOCUMENTO_ACTA.USU_ID = ? AND INSTANCIA.PES_ID = ?)))))),0)                 \"indPertenece\" \n"
                + " FROM DOCUMENTO DOC \n"
                + " LEFT JOIN USUARIO USU_ULT_ACCION         ON (DOC.USU_ID_ULTIMA_ACCION	= USU_ULT_ACCION.USU_ID) \n"
                + " LEFT JOIN DEPENDENCIA DEP                ON (DOC.DEP_ID_DES 		= DEP.DEP_ID) \n"
                + " LEFT JOIN USUARIO USU_DEP_JEFE           ON (DEP.USU_ID_JEFE 		= USU_DEP_JEFE.USU_ID) \n"
                + " LEFT JOIN PROCESO_INSTANCIA INSTANCIA    ON (DOC.PIN_ID 			= INSTANCIA.PIN_ID) \n"
                + " LEFT JOIN DOCUMENTO_USU_FIRMA DOCFIRMA   ON (DOC.DOC_ID 			= DOCFIRMA.DOC_ID ) \n"
                + " LEFT JOIN S_INSTANCIA_USUARIO HPIN       ON (DOC.PIN_ID                  = HPIN.PIN_ID AND HPIN.USU_ID = ?) \n"
                + " LEFT JOIN USUARIO USU                    ON (HPIN.USU_ID                 = USU.USU_ID) \n"
                + " LEFT JOIN PROCESO_ESTADO EST             ON (EST.PES_ID                  = INSTANCIA.PES_ID) \n"
                + " LEFT JOIN PROCESO PROCESO                ON (INSTANCIA.PRO_ID            = PROCESO.PRO_ID) \n"
                + " LEFT JOIN USUARIO USU_ASIGNADO           ON (INSTANCIA.USU_ID_ASIGNADO   = USU_ASIGNADO.USU_ID) \n"
                + " LEFT JOIN USUARIO USU_ELABORA            ON (DOC.USU_ID_ELABORA          = USU_ELABORA.USU_ID) \n"
                + " LEFT JOIN USUARIO USU_REVISO             ON (DOC.USU_ID_APRUEBA          = USU_REVISO.USU_ID) \n"
                + " LEFT JOIN USUARIO USU_VBUENO             ON (DOC.USU_ID_VISTO_BUENO      = USU_VBUENO.USU_ID) \n"
                + " LEFT JOIN USUARIO USU_FIRMA              ON (DOC.USU_ID_FIRMA            = USU_FIRMA.USU_ID) \n"
                + " LEFT JOIN DESTINO_EXTERNO DES_EXTERNO    ON (DOC.ADE_ID            = DES_EXTERNO.ADE_ID) \n"
                + " LEFT JOIN CLASIFICACION                  ON (DOC.CLA_ID                  = CLASIFICACION.CLA_ID) \n"
                + " LEFT JOIN (SELECT DEP_ORI_ID, DEP_ORI_NOMBRE, DEP_ID FROM (SELECT FIRST_VALUE(DEP_ORI_ID) OVER (PARTITION BY DEP_ID ORDER BY ROW_NUM ASC) DEP_ORI_ID, FIRST_VALUE(DEP_ORI_NOMBRE) OVER (PARTITION BY DEP_ID ORDER BY ROW_NUM ASC) DEP_ORI_NOMBRE, DEP_ID FROM(SELECT LEVEL ROW_NUM, CONNECT_BY_ROOT DEP_ID AS DEP_ORI_ID, CONNECT_BY_ROOT DEP_SIGLA AS DEP_ORI_NOMBRE, DEP_ID FROM DEPENDENCIA WHERE (CONNECT_BY_ROOT DEP_IND_ENVIO_DOCUMENTOS = 1 OR CONNECT_BY_ROOT DEP_PADRE IS NULL) CONNECT BY DEP_PADRE = PRIOR DEP_ID)) GROUP BY DEP_ORI_ID, DEP_ORI_NOMBRE, DEP_ID) DEP_ORIGEN ON (DEP_ORIGEN.DEP_ID = USU_ELABORA.DEP_ID)\n"
                + " LEFT JOIN (SELECT DEP_ORI_ID, DEP_DES_NOMBRE, DEP_ID FROM (SELECT FIRST_VALUE(DEP_ORI_ID) OVER (PARTITION BY DEP_ID ORDER BY ROW_NUM ASC) DEP_ORI_ID, FIRST_VALUE(DEP_DES_NOMBRE) OVER (PARTITION BY DEP_ID ORDER BY ROW_NUM ASC) DEP_DES_NOMBRE, DEP_ID FROM(SELECT LEVEL ROW_NUM, CONNECT_BY_ROOT DEP_ID AS DEP_ORI_ID, CONNECT_BY_ROOT DEP_SIGLA AS DEP_DES_NOMBRE, DEP_ID FROM DEPENDENCIA WHERE (CONNECT_BY_ROOT DEP_IND_ENVIO_DOCUMENTOS = 1 OR CONNECT_BY_ROOT DEP_PADRE IS NULL) CONNECT BY DEP_PADRE = PRIOR DEP_ID)) GROUP BY DEP_ORI_ID, DEP_DES_NOMBRE, DEP_ID) DEP_DESTINO ON (DEP_DESTINO.DEP_ID = DOC.DEP_ID_DES)\n"
                + " LEFT JOIN USUARIO_X_DOCUMENTO_ACTA       ON (USUARIO_X_DOCUMENTO_ACTA.DOC_ID  = DOC.DOC_ID AND USUARIO_X_DOCUMENTO_ACTA.ACTIVO = 1 AND USUARIO_X_DOCUMENTO_ACTA.USU_ID = ?)\n"
                + " LEFT JOIN DOCUMENTO_DEPENDENCIA          ON (DOCUMENTO_DEPENDENCIA.DOC_ID  = DOC.DOC_ID AND DOCUMENTO_DEPENDENCIA.ACTIVO = 1)\n"
                + " WHERE 1 = 1 \n"
                + "");
    }
    
    
    
    /**
     * Nueva consulta simplificada.
     * @return StringBuilder
     */
    private StringBuilder retornarNuevaConsulta(){
        return new StringBuilder(" "+
        "SELECT DISTINCT       \n" +
        "      DOC.DOC_ID       ,\n" +
        "      DOC.CUANDO_MOD  \n"+        
        " FROM DOCUMENTO DOC \n" +
        " LEFT JOIN DEPENDENCIA DEP                ON (DOC.DEP_ID_DES 		      = DEP.DEP_ID) \n" +
        " LEFT JOIN PROCESO_INSTANCIA INSTANCIA    ON (DOC.PIN_ID 			      = INSTANCIA.PIN_ID) \n" +
        " LEFT JOIN S_INSTANCIA_USUARIO HPIN       ON (DOC.PIN_ID                  = HPIN.PIN_ID) \n" +
        " LEFT JOIN USUARIO_X_DOCUMENTO_ACTA       ON (USUARIO_X_DOCUMENTO_ACTA.DOC_ID  = DOC.DOC_ID AND USUARIO_X_DOCUMENTO_ACTA.ACTIVO = 1)\n" +
        " LEFT JOIN DOCUMENTO_DEPENDENCIA          ON (DOCUMENTO_DEPENDENCIA.DOC_ID  = DOC.DOC_ID AND DOCUMENTO_DEPENDENCIA.ACTIVO = 1)\n" +
        "");
    }
    
    
    /***
     * Metodo que reduce los campos a buscar.
     * @param sql
     * @param asunto
     * @param fechaInicio
     * @param fechaFin
     * @param radicado
     * @param dependenciaDestino
     * @param dependenciaOrigen
     * @param usuarioID
     * @param cargosIDs
     * @param sameValue
     * @param permisoAdministradorArchivo
     * @param tipoBusqueda
     * @param destinoExterno
     * @return 
     */
    public LinkedList<Object> armaConsultaNueva(final StringBuilder sql, final String asunto, final String fechaInicio,
            final String fechaFin, final String radicado, final Integer dependenciaDestino,
            final Integer dependenciaOrigen, final Integer usuarioID, final Integer[] cargosIDs,
            final boolean sameValue, final boolean permisoAdministradorArchivo, final Integer tipoBusqueda,
            final Integer destinoExterno){
         
        if (dependenciaOrigen != null) {
            sql.append(" LEFT JOIN USUARIO USU_ELABORA ON (DOC.USU_ID_ELABORA = USU_ELABORA.USU_ID) \n");
            sql.append(" LEFT JOIN DEPENDENCIA DEP_ORIGEN ON (DEP_ORIGEN.DEP_ID = USU_ELABORA.DEP_ID) \n");
        }
        
        sql.append(" WHERE 1 = 1 \n");
         
        SentenceOperator operator = (sameValue) ? SentenceOperator.OR : SentenceOperator.AND;
        LinkedList<Object> parameters = new LinkedList<>();
        boolean hasConditions = false;
        
        final Integer[] estadosNoAplican = {Estado.ANULADO, Estado.ANULADO_NEW, DocumentoActaEstado.ANULADO.getId()};
        sql.append("AND INSTANCIA.PES_ID NOT IN (");
        for (int index = 0; index < estadosNoAplican.length; index++) {
            final Integer estadoID = estadosNoAplican[index];
            sql.append("?").append((index < estadosNoAplican.length - 1) ? ", " : "");
            parameters.add(estadoID);
        }
        sql.append(") AND DOC.DOC_ASUNTO IS NOT NULL \n");
        
        if (!permisoAdministradorArchivo) {
            sql.append(" AND ((INSTANCIA.PRO_ID IN (?, ?, ?) AND (DOC.USU_ID_ELABORA = ? OR DOC.USU_ID_FIRMA = ? OR HPIN.USU_ID = ?)) \n");
            sql.append(" OR ((INSTANCIA.PRO_ID IN (?) AND ((HPIN.USU_ID = ? AND INSTANCIA.PES_ID <> ?) OR (DOCUMENTO_DEPENDENCIA.QUIEN = ? AND INSTANCIA.PES_ID = ?) OR (USUARIO_X_DOCUMENTO_ACTA.USU_ID = ? AND INSTANCIA.PES_ID = ? \n");
            parameters.add(Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS);
            parameters.add(Proceso.ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA);
            parameters.add(Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS);
            parameters.add(usuarioID);
            parameters.add(usuarioID);
            parameters.add(usuarioID);
            parameters.add(Proceso.ID_TIPO_PROCESO_REGISTRO_ACTAS);
            parameters.add(usuarioID);
            parameters.add(DocumentoActaEstado.ACTA_DIGITALIZADA.getId());
            parameters.add(usuarioID);
            parameters.add(DocumentoActaEstado.ACTA_DIGITALIZADA.getId());
            parameters.add(usuarioID);
            parameters.add(DocumentoActaEstado.ACTA_DIGITALIZADA.getId());
            
            if (cargosIDs != null && cargosIDs.length > 0) {
                sql.append(" AND USUARIO_X_DOCUMENTO_ACTA.CAR_ID IN (");
                for (int index = 0; index < cargosIDs.length; index++) {
                    final Integer cargoID = cargosIDs[index];
                    sql.append("?").append((index < cargosIDs.length - 1) ? ", " : "");
                    parameters.add(cargoID);
                }
                sql.append(")");
            }
            sql.append("))))) \n");
        }
        sql.append(" AND (");
        if (StringUtils.isNotBlank(radicado)) {
            sql.append(hasConditions ? operator.name() : "").append(" LOWER(DOC.DOC_RADICADO) LIKE LOWER( ? ) \n");
            parameters.add("%" + radicado.trim() + "%");
            hasConditions = true;
        }
        
        if (StringUtils.isNotBlank(asunto)) {
            sql.append(hasConditions ? operator.name() : "").append(" LOWER(DOC.DOC_ASUNTO) LIKE LOWER( ? ) \n");
            parameters.add("%" + asunto.trim() + "%");
            hasConditions = true;
        }
        
        Date fInicio = null;
        if (StringUtils.isNotBlank(fechaInicio)) {
            fInicio = parseFilterDate(fechaInicio, DateUtil.SetTimeType.START_TIME);
        }

        Date fFin = null;
        if (StringUtils.isNotBlank(fechaFin)) {
            fFin = parseFilterDate(fechaFin, DateUtil.SetTimeType.END_TIME);
        }
        
        if (fInicio != null) {
            sql.append(hasConditions ? operator.name() : "").append(" DOC.CUANDO >= ? \n");
            parameters.add(fInicio);
            hasConditions = true;
        }
        
        if (fFin != null) {
            if (fInicio == null) {
                sql.append((hasConditions ? operator.name() : ""));
            } else {
                sql.append(SentenceOperator.AND.name());
            }
            sql.append(" DOC.CUANDO <= ? \n");
            parameters.add(fFin);
            hasConditions = true;
        }
        
        if (dependenciaDestino != null) {
            sql.append(hasConditions ? operator.name() : "").append(" DOC.DEP_ID_DES = ? \n");
            parameters.add(dependenciaDestino);
            hasConditions = true;
        }

        if (dependenciaOrigen != null) {
            List<Integer> dependencias = subDepsList(dependenciaOrigen);
            String strDependencias = dependencias.toString();
            System.out.println("DEPENDENCIAS IDS "+strDependencias);
            sql.append(hasConditions ? operator.name() : "").append(" DEP_ORIGEN.DEP_ID IN ("+strDependencias.substring(1, strDependencias.length()-1)+") \n");
            hasConditions = true;
        }
        
        
         if (tipoBusqueda != null ) {
             if (tipoBusqueda.equals(0)) {
                sql.append(hasConditions ? operator.name() : "").append(" INSTANCIA.PRO_ID IN (?, ?, ?) \n");
                parameters.add(Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS);
                parameters.add(Proceso.ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA);
                parameters.add(Proceso.ID_TIPO_PROCESO_REGISTRO_ACTAS);
             }
             if(tipoBusqueda.equals(1)){
                sql.append(hasConditions ? operator.name() : "").append(" INSTANCIA.PRO_ID = ? \n");
                parameters.add(Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS);
                 if (destinoExterno != null) {
                     sql.append(hasConditions ? operator.name() : "").append(" DOC.ADE_ID = ? \n");
                     parameters.add(destinoExterno);
                 }
             }
         }
         
        sql.append(" ) \n");
        
        return parameters;
     }
     
     
     /***
      * Motor nuevo que simplifica las consultas
      * @param asunto
      * @param fechaInicio
      * @param fechaFin
      * @param radicado
      * @param dependenciaDestino
      * @param dependenciaOrigen
      * @param usuarioID
      * @param cargosIDs
      * @param permisoAdministradorArchivo
      * @param sameValue
      * @param inicio
      * @param fin
      * @param tipoBusqueda
      * @param destinoExterno
      * @return 
      */
     public Object[] retornaConsultaMotorBusquedaNuevo(final String asunto, final String fechaInicio, 
             final String fechaFin,  final String radicado, final Integer dependenciaDestino, 
             final Integer dependenciaOrigen, final Integer usuarioID, final Integer[] cargosIDs, final boolean permisoAdministradorArchivo, final boolean sameValue,
             final int inicio, final int fin, final Integer tipoBusqueda, final Integer destinoExterno){
         
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        StringBuilder sql = retornarNuevaConsulta();
        LinkedList<Object> parameters = armaConsultaNueva(sql, asunto, fechaInicio, fechaFin, radicado, dependenciaDestino, dependenciaOrigen, usuarioID, cargosIDs, sameValue, permisoAdministradorArchivo, tipoBusqueda, destinoExterno);
        LOG.info("##################################################");
        LOG.info("sql = " + sql);
        LOG.info("parameters = " + parameters);
        LOG.info("##################################################");
        String consulta = ""
                + "select DOC_ID, RESULT_COUNT \n"
                + "from(\n"
                + "     select a.DOC_ID, ROWNUM RNUM, COUNT(*) OVER () RESULT_COUNT\n"
                + "     from(\n"
                + sql.toString()
                + "     ORDER BY DOC.CUANDO_MOD DESC\n"
                + "     ) a\n"
                + ") \n"
                + "where RNUM between ? and ? \n";
        
        parameters.add(inicio);
        parameters.add(fin);
        int counter = 0;
        List<DocumentoDTO> ids = jdbcTemplate.query(consulta, parameters.toArray(), new RowMapper<DocumentoDTO>() {
            @Override
            public DocumentoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                DocumentoDTO c = new DocumentoDTO();
                c.setId(rs.getString("DOC_ID"));
                c.setNumDocumentos(Integer.parseInt(rs.getString("RESULT_COUNT")));
                return c;
            }
        });
        Object[] asw = new Object[2];
        asw[0] = 0;
         if (!ids.isEmpty()) {
            String consulta2 = ""
            + retornaConsultaPrincipal()
            + " AND DOC.DOC_ID IN (\n";
            String parametrosConsulta2 = "";
            for (int i = 0; i < ids.size()-1; i++) {
               counter = ids.get(i).getNumDocumentos();
               parametrosConsulta2 += "'"+ids.get(i).getId()+"',";
            }
            parametrosConsulta2 += "'"+ids.get(ids.size()-1).getId()+"'";
            counter = ids.get(ids.size()-1).getNumDocumentos();
            consulta2 += parametrosConsulta2+" ) \n";
            System.out.println("AQUI MIRAR ");
            System.out.println(consulta2);
             
            LinkedList<Object> parameters3 = new LinkedList<>();
            parameters3.add(Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS);
            parameters3.add(Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS);
            parameters3.add(Proceso.ID_TIPO_PROCESO_GENERAR_Y_ENVIAR_DOCUMENTO_PARA_UNIDADES_DE_INTELIGENCIA_Y_CONTRAINTELIGENCIA);
            parameters3.add(Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS);
            parameters3.add(usuarioID);
            parameters3.add(usuarioID);
            parameters3.add(usuarioID);
            parameters3.add(Proceso.ID_TIPO_PROCESO_REGISTRO_ACTAS);
            parameters3.add(usuarioID);
            parameters3.add(DocumentoActaEstado.ACTA_DIGITALIZADA.getId());
            parameters3.add(usuarioID);
            parameters3.add(DocumentoActaEstado.ACTA_DIGITALIZADA.getId());
            parameters3.add(usuarioID);
            parameters3.add(DocumentoActaEstado.ACTA_DIGITALIZADA.getId());
            parameters3.add(usuarioID);
            parameters3.add(usuarioID);
            System.out.println(parameters3);
            asw[0] = counter;
            List<DocumentoDTO> documentos;
            documentos = jdbcTemplate.query(consulta2, parameters3.toArray(), new RowMapper<DocumentoDTO>() {
            @Override
            public DocumentoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                    DocumentoDTO c = new DocumentoDTO(rs.getString("id"), rs.getString("idInstancia"), rs.getString("asunto"), rs.getDate("cuandoMod"), rs.getString("nombreProceso"),
                            rs.getString("nombreEstado"), rs.getString("nombreUsuarioAsignado"), rs.getString("nombreUsuarioEnviado"), rs.getString("nombreUsuarioElabora"),
                            rs.getString("nombreUsuarioReviso"), rs.getString("nombreUsuarioVbueno"), rs.getString("nombreUsuarioFirma"), rs.getString("nombreClasificacion"),
                            rs.getString("numeroRadicado"), rs.getString("unidadOrigen"), rs.getString("unidadDestino"), rs.getBoolean("indPertenece"));
                    return c;
                }
            });
             
            asw[1] = documentos;
         }
         return asw;
     }
    
    /***
     * Método que retorna si un usuario posee un rol
     * @param roles roles del usuario.
     * @param rol rol a consultar
     * @return true si posee el permiso flase si no lo posee
     */
    public boolean hasPermision(List<Rol> roles, String rolPermiso){
        for (Rol rol : roles) {
            if (rol.getId().equals(rolPermiso)) {
                return true;
            }
        }
        return false;
    }
    
    /***
     * Da la lista de identificadores de las subdependencia de un identificador de una dependencia
     * @param depId identificador de la dependencia
     * @return  lista de identificadores sub dependencias 
     */
    public List<Integer> subDepsList(Integer depId){
        List<Integer> list =  new ArrayList<>();
        List<Dependencia> listaDependencias = dependenciaService.depsHierarchy();
        for (Dependencia dependencia : listaDependencias) {
            if (dependencia.getId().equals(depId)) {
                subDepsIds(dependencia, list);
            }else{
                subDepsListRecur(dependencia, depId, list);
            }
        }
        return list;
    }
    
    /**
     * Recorsión para buscar en el arbol las sub dependencias
     * @param d dependencia a consultar sus subdependencias
     * @param depId identificador de la subdependencia
     * @param listIds lista de identificadores de la dependencia
     * @return 
     */
    public List<Integer> subDepsListRecur(Dependencia d, Integer depId, List<Integer> listIds){
        for (Dependencia dependencia : d.getSubs()) {
            if (dependencia.getId().equals(depId)) {
                subDepsIds(dependencia, listIds);
            }else{
                subDepsListRecur(dependencia, depId, listIds);
            }
        }
        return listIds;
    }
    
    /**
     * sub busca en el arbol de dependenica  
     * @param d dependencia a buscar
     * @param listIds lista de identificadores de la dependencia
     * @return 
     */
    public List<Integer> subDepsIds(Dependencia d, List<Integer> listIds){
        listIds.add(d.getId());
        if (d.getSubs() == null || d.getSubs().isEmpty()) 
            return listIds;
        for (Dependencia dep : d.getSubs()) {
            subDepsIds(dep, listIds);
        }
        return listIds;
    }
    
     
}
