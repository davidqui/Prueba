package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.DocumentoDTO;
import com.laamware.ejercito.doc.web.entity.Estado;
import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.util.DateUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Servicio que permite realizar el motor de busqueda.
 *
 * @author edison.gonzalez@controltechcg.com
 * @since Octrubre 31, 2017
 *
 */
// 2017-10-31 edison.gonzalez@controltechcg.com Issue #136 (SICDI-Controltech)
@Service
public class ConsultaService {

    @Autowired
    private DataSource dataSource;

    // Issue #106
    private static final SimpleDateFormat DATE_FORMAT_YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");

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

    public int retornaCountConsultaMotorBusqueda(String asignado, String asunto, String fechaInicio,
            String fechaFin, String radicado, String destinatario,
            Integer clasificacion, Integer dependenciaDestino, Integer dependenciaOrigen, boolean sameValue, Integer usuarioID) {

        StringBuilder sql = retornaConsultaPrincipal();
        LinkedList<Object> parameters = armaConsulta(sql, asignado, asunto, fechaInicio, fechaFin, radicado, destinatario, clasificacion, dependenciaDestino, dependenciaOrigen, sameValue, usuarioID);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String count = ""
                + "select count(1)\n"
                + "from(\n"
                + sql.toString()
                + ")\n";

        System.err.println("count= " + count);
        System.err.println("parameters= " + java.util.Arrays.toString(parameters.toArray()));
        int i = 0;
        try {
            i = jdbcTemplate.queryForObject(count, parameters.toArray(), Integer.class);
        } catch (Exception e) {
            System.err.println("e= " + e);
        }
        return i;
    }

    public List<DocumentoDTO> retornaConsultaMotorBusqueda(String asignado, String asunto, String fechaInicio,
            String fechaFin, String radicado, String destinatario,
            Integer clasificacion, Integer dependenciaDestino, Integer dependenciaOrigen, boolean sameValue, Integer usuarioID, int inicio, int fin) {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        StringBuilder sql = retornaConsultaPrincipal();
        LinkedList<Object> parameters = armaConsulta(sql, asignado, asunto, fechaInicio, fechaFin, radicado, destinatario, clasificacion, dependenciaDestino, dependenciaOrigen, sameValue, usuarioID);

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

        System.err.println("CONSULTA PRINCIPAL= " + consulta);
        System.err.println("parameters= " + java.util.Arrays.toString(parameters.toArray()));
        return jdbcTemplate.query(consulta, parameters.toArray(), new RowMapper<DocumentoDTO>() {
            @Override
            public DocumentoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                DocumentoDTO c = new DocumentoDTO(rs.getString("id"), rs.getString("idInstancia"), rs.getString("asunto"), rs.getDate("cuandoMod"), rs.getString("nombreProceso"),
                        rs.getString("nombreEstado"), rs.getString("nombreUsuarioAsignado"), rs.getString("nombreUsuarioEnviado"), rs.getString("nombreUsuarioElabora"),
                        rs.getString("nombreUsuarioReviso"), rs.getString("nombreUsuarioVbueno"), rs.getString("nombreUsuarioFirma"), rs.getString("nombreClasificacion"),
                        rs.getString("numeroRadicado"));
                return c;
            }
        });
    }

    /**
     * Verifica si los parametros del motor de busqueda se encuentran vacios
     *
     * @param sql
     * @param asignado
     * @param asunto
     * @param fechaInicio
     * @param fechaFin
     * @param radicado
     * @param destinatario
     * @param remitente
     * @param clasificacion
     * @param sameValue
     * @param dependenciaDestino
     * @param dependenciaOrigen
     * @param usuarioID
     * @return
     */
    public LinkedList<Object> armaConsulta(StringBuilder sql, String asignado, String asunto, String fechaInicio,
            String fechaFin, String radicado, String destinatario,
            Integer clasificacion, Integer dependenciaDestino, Integer dependenciaOrigen, boolean sameValue, Integer usuarioID) {

        // Issue #128
        SentenceOperator operator = (sameValue) ? SentenceOperator.OR : SentenceOperator.AND;
        LinkedList<Object> parameters = new LinkedList<>();

        /*
		 * 2017-05-23 jgarcia@controltechcg.com Issue #91 (SICDI-Controltech)
		 * hotfix-91: Lista de estados para no tener en cuenta en la consulta de
		 * documentos a través de Búsqueda Avanzada.
         */
        final Integer[] estadosNoAplican = {Estado.ANULADO, Estado.ANULADO_NEW};
        sql.append("AND INSTANCIA.PES_ID NOT IN (");
        for (int index = 0; index < estadosNoAplican.length; index++) {
            final Integer estadoID = estadosNoAplican[index];
            sql.append("?").append((index < estadosNoAplican.length - 1) ? ", " : "");
            parameters.add(estadoID);
        }
        sql.append(")\n");

        // 2017-10-31 edison.gonzalez@controltechcg.com Issue #136: Ajuste
        // para filtrar si el usuario dio vistos bueno.
        sql.append("AND (DOC.USU_ID_ELABORA = ? OR DOC.USU_ID_FIRMA = ? OR USU.USU_ID = ?) \n");
        parameters.add(usuarioID);
        parameters.add(usuarioID);
        parameters.add(usuarioID);

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
            sql.append(hasConditions ? operator.name() : "").append(" DEP_ORIGEN.DEP_ORI_ID = ? \n");
            parameters.add(dependenciaOrigen);
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
            return DateUtil.setTime(DATE_FORMAT_YYYYMMDD.parse(dateValue.trim()), setTimeType);
        } catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private StringBuilder retornaConsultaPrincipal() {
        // 2017-02-17 jgarcia@controltechcg.com Issue #128: Se corrige sentencia
        // SQL para evitar repetición de la información.
        // 2017-10-31 edison.gonzalez@controltechcg.com Issue #136: Se agregan 
        // nuevos campos, para realizar una sola consulta.
        // 2017-02-15 jgarcia@controltechcg.com Issue #142: Nuevas asociaciones
        // para obtener los campos de fechas solicitados.
        return new StringBuilder(""
                + "SELECT DISTINCT INSTANCIA.PIN_ID    \"idInstancia\", \n"
                + "       DOC.DOC_ID                   \"id\", \n"
                + "       DOC.DOC_ASUNTO               \"asunto\", \n"
                + "       DOC.CUANDO_MOD               \"cuandoMod\", \n"
                + "       PROCESO.PRO_NOMBRE           \"nombreProceso\", \n"
                + "       EST.PES_NOMBRE               \"nombreEstado\", \n"
                + "       DECODE(USU_ASIGNADO.USU_NOMBRE, NULL, NULL,USU_ASIGNADO.USU_GRADO||'. '||USU_ASIGNADO.USU_NOMBRE)         \"nombreUsuarioAsignado\", \n"
                + "       DECODE(USU_ULT_ACCION.USU_NOMBRE, NULL, NULL,USU_ULT_ACCION.USU_GRADO||'. '||USU_ULT_ACCION.USU_NOMBRE)   \"nombreUsuarioEnviado\", \n"
                + "       DECODE(USU_ELABORA.USU_NOMBRE, NULL, NULL,USU_ELABORA.USU_GRADO||'. '||USU_ELABORA.USU_NOMBRE)            \"nombreUsuarioElabora\", \n"
                + "       DECODE(USU_REVISO.USU_NOMBRE, NULL, NULL,USU_REVISO.USU_GRADO||'. '||USU_REVISO.USU_NOMBRE)               \"nombreUsuarioReviso\", \n"
                + "       DECODE(USU_VBUENO.USU_NOMBRE, NULL, NULL,USU_VBUENO.USU_GRADO||'. '||USU_VBUENO.USU_NOMBRE)               \"nombreUsuarioVbueno\", \n"
                + "       DECODE(USU_FIRMA.USU_NOMBRE, NULL, NULL,USU_FIRMA.USU_GRADO||'. '||USU_FIRMA.USU_NOMBRE)                  \"nombreUsuarioFirma\", \n"
                + "       CLASIFICACION.CLA_NOMBRE     \"nombreClasificacion\", \n"
                + "       DOC.DOC_RADICADO             \"numeroRadicado\" \n"
                + "FROM DOCUMENTO DOC \n"
                + "LEFT JOIN USUARIO USU_ULT_ACCION		ON (DOC.USU_ID_ULTIMA_ACCION	= USU_ULT_ACCION.USU_ID) \n"
                + "LEFT JOIN DEPENDENCIA DEP 		ON (DOC.DEP_ID_DES 		= DEP.DEP_ID) \n"
                + "LEFT JOIN USUARIO USU_DEP_JEFE 		ON (DEP.USU_ID_JEFE 		= USU_DEP_JEFE.USU_ID) \n"
                + "LEFT JOIN PROCESO_INSTANCIA INSTANCIA	ON (DOC.PIN_ID 			= INSTANCIA.PIN_ID) \n"
                + "LEFT JOIN DOCUMENTO_USU_FIRMA DOCFIRMA 	ON (DOC.DOC_ID 			= DOCFIRMA.DOC_ID ) \n"
                + "LEFT JOIN S_INSTANCIA_USUARIO HPIN       ON (DOC.PIN_ID                  = HPIN.PIN_ID) \n"
                + "LEFT JOIN USUARIO USU                    ON (HPIN.USU_ID                 = USU.USU_ID) \n"
                + "LEFT JOIN PROCESO_ESTADO EST             ON (EST.PES_ID                  = INSTANCIA.PES_ID) \n"
                + "LEFT JOIN PROCESO PROCESO                ON (INSTANCIA.PRO_ID            = PROCESO.PRO_ID) \n"
                + "LEFT JOIN USUARIO USU_ASIGNADO           ON (INSTANCIA.USU_ID_ASIGNADO   = USU_ASIGNADO.USU_ID) \n"
                + "LEFT JOIN USUARIO USU_ELABORA            ON (DOC.USU_ID_ELABORA          = USU_ELABORA.USU_ID) \n"
                + "LEFT JOIN USUARIO USU_REVISO             ON (DOC.USU_ID_APRUEBA          = USU_REVISO.USU_ID) \n"
                + "LEFT JOIN USUARIO USU_VBUENO             ON (DOC.USU_ID_VISTO_BUENO      = USU_VBUENO.USU_ID) \n"
                + "LEFT JOIN USUARIO USU_FIRMA              ON (DOC.USU_ID_FIRMA            = USU_FIRMA.USU_ID) \n"
                + "LEFT JOIN CLASIFICACION                  ON (DOC.CLA_ID                  = CLASIFICACION.CLA_ID) \n"
                + "LEFT JOIN (SELECT CONNECT_BY_ROOT DEP_ID AS DEP_ORI_ID, DEP_ID FROM DEPENDENCIA START WITH DEP_PADRE IS NULL CONNECT BY DEP_PADRE = PRIOR DEP_ID ) DEP_ORIGEN ON (DEP_ORIGEN.DEP_ID = USU_ULT_ACCION.DEP_ID )\n"
                + "WHERE 1 = 1 \n");
    }
}


