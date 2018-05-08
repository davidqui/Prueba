package com.laamware.ejercito.doc.web.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.PDFDocumento;

public interface DocumentoRepository extends JpaRepository<Documento, String> {

    /* 2017-02-14 jgarcia@controltechcg.com Issue #108: Se añade el estado
     * anulado con IDs 101 (Anulado) 2017-02-15 jgarcia@controltechcg.com Issue
     * #108: Se añade el estado anulado con IDs 102 (Archivado)
     *
     * 2017-03-17 jgarcia@controltechcg.com Issue #27 (SIGDI-Incidencias01):
     * Modificación en el orden de la consulta SQL que obtiene los datos de los
     * documentos a presentar en la bandeja de entrada.
     *
     * 2017-03-22 jgarcia@controltechcg.com Issue #29 (SIGDI-Incidencias01):
     * Modificación en la sentencia SQL que obtiene la información de la bandeja
     * de entrada de un usuario, para que también aparezcan los documentos
     * asignados por Copia Dependencia.
     *
     * 2017-04-20 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech):
     * Modificación en la consulta SQL que obtiene la información de los
     * documentos a presentar en la bandeja de entrada del usuario en sesión,
     * para que no presente los documentos asignados que se encuentren en la
     * bandeja de apoyo y consulta. Mejora codificación y formato de sentencias
     * SQL.
     *
     * 2017-05-15 jgarcia@controltechcg.com Issue #81 (SICDI-Controltech):
     * hotfix-81 -> Corrección en la consulta SQL de la bandeja de entrada para
     * que no presente los documentos del proceso externo enviados por el mismo
     * usuario en sesión.
     */
    String CONSULTABANDEJAENTRADA = ""
            + "SELECT documento.*\n"
            + "FROM documento\n"
            + "     LEFT JOIN documento_en_consulta ON (documento_en_consulta.doc_id = documento.doc_id)\n"
            + "WHERE documento.doc_id IN ( SELECT DISTINCT documento.doc_id\n"
            + "                            FROM documento\n"
            + "                                 JOIN proceso_instancia ON (proceso_instancia.pin_id = documento.pin_id)\n"
            + "                                 JOIN usuario usuario_asignado ON (usuario_asignado.usu_id = proceso_instancia.usu_id_asignado)\n"
            + "                            WHERE 1 = 1\n"
            + "                            AND documento.doc_asunto IS NOT NULL\n"
            + "                            AND proceso_instancia.pes_id NOT IN (48,52,83,101,102)\n"
            + "                            AND usuario_asignado.usu_login =:login\n"
            + "                            AND NOT (proceso_instancia.pro_id = 41 AND proceso_instancia.pes_id = 49)\n"
            + "                            UNION\n"
            + "                            SELECT documento_dep_destino.doc_id\n"
            + "                            FROM documento_dep_destino\n"
            + "                                 JOIN documento ON (documento.doc_id = documento_dep_destino.doc_id)\n"
            + "                                 JOIN proceso_instancia ON ( proceso_instancia.pin_id = documento.pin_id)\n"
            + "                                 JOIN dependencia ON (dependencia.dep_id = documento_dep_destino.dep_id)\n"
            + "                                 LEFT JOIN usuario usuario_jefe_1_dep ON (usuario_jefe_1_dep.usu_id = dependencia.usu_id_jefe)\n"
            + "                                 LEFT JOIN usuario usuario_jefe_2_dep ON (usuario_jefe_2_dep.usu_id = dependencia.usu_id_jefe_encargado)\n"
            + "                            WHERE 1 = 1\n"
            + "                            AND documento.doc_asunto IS NOT NULL\n"
            + "                            AND proceso_instancia.pes_id = 49\n"
            + "                            AND documento_dep_destino.activo = 1\n"
            + "                            AND ((dependencia.fch_inicio_jefe_encargado <= SYSDATE AND SYSDATE <= dependencia.fch_fin_jefe_encargado\n"
            + "                                  AND usuario_jefe_2_dep.usu_login =:login\n"
            + "                                  ) OR (((dependencia.fch_inicio_jefe_encargado IS NULL OR dependencia.fch_fin_jefe_encargado IS NULL) \n"
            + "                                         OR ( NOT (dependencia.fch_inicio_jefe_encargado <= SYSDATE AND SYSDATE <= dependencia.fch_fin_jefe_encargado))\n"
            + "                                         )"
            + "                                        AND usuario_jefe_1_dep.usu_login =:login\n"
            + "                                         )))\n"
            + "    AND (documento_en_consulta.dec_id IS NULL OR (documento_en_consulta.activo = 0)) \n";

    /* 2017-03-27 jgarcia@controltechcg.com Issue #22 (SIGDI-Incidencias01):
     * Modificación de consulta SQL de la bandeja de enviados para que no
     * presente la información de los documentos anulados.
     *
     * 2017-07-05 jgarcia@controltechcg.com Issue #115 (SICDI-Controltech)
     * feature-115: Modificación de sentencia de bandeja enviados para filtro
     * por rango de fechas.
     *
     * 2017-07-25 jgarcia@controltechcg.com Issue #118 (SICDI-Controltech)
     * hotfix-118: Corrección en la sentencia SQL de la bandeja de enviados,
     * para que no presente documentos cuyo usuario asignado actual corresponda
     * al usuario en sesión.
     *
     * 2018-03-22 edison.gonzalez@controltechcg.com Issue #155 (SICDI-Controltech)
     * hotfix-155: Corrección en la sentencia SQL de la bandeja de enviados,
     * para que presente los documentos de procesos externos a pesar de que sea 
     * usuario asignado y sea el usuario en sesión.
     */
    String CONSULTABANDEJAENVIADOS = ""
            + "SELECT doc.*\n"
            + "FROM documento doc\n"
            + "     JOIN s_instancia_usuario hpin ON doc.pin_id = hpin.pin_id\n"
            + "     JOIN proceso_instancia pin ON doc.pin_id = pin.pin_id\n"
            + "     JOIN proceso_estado est ON est.pes_id = pin.pes_id\n"
            + "     JOIN usuario usu ON hpin.usu_id = usu.usu_id\n"
            + "     JOIN usuario usu_asignado ON (usu_asignado.usu_id = pin.usu_id_asignado)\n"
            + "WHERE usu.usu_login = :login\n"
            + "AND ((usu_asignado.usu_login <> :login) OR ((pin.pro_id = 41 AND pin.pes_id = 49 AND usu_asignado.usu_login = :login )))\n"
            + "AND doc.doc_radicado IS NOT NULL\n"
            + "AND est.pes_final = 1\n"
            + "AND est.pes_id NOT IN (83,101)\n"
            + "AND doc.cuando_mod BETWEEN :fechaInicial AND :fechaFinal\n";

    /*
	 * 2017-07-10 jgarcia@controltechcg.com Issue #115 (SICDI-Controltech)
	 * feature-115: Modificación de sentencia de bandeja enviados para filtro
	 * por rango de fechas.
	 * 
	 * 2017-07-11 jgarcia@controltechcg.com Issue #115 (SICDI-Controltech)
	 * feature-115: Modificación en fecha de filtro de dato de creación por dato
	 * de última modificación del documento.
     */
    String CONSULTABANDEJAENTRAMITE = ""
            + "SELECT DOC.*\n"
            + "FROM DOCUMENTO DOC\n"
            + "    JOIN S_INSTANCIA_USUARIO HPIN ON (DOC.PIN_ID = HPIN.PIN_ID)\n"
            + "    JOIN PROCESO_INSTANCIA   PIN  ON (DOC.PIN_ID = PIN.PIN_ID)\n"
            + "    JOIN PROCESO_ESTADO      EST  ON (EST.PES_ID = PIN.PES_ID)\n"
            + "    JOIN USUARIO             USU  ON (HPIN.USU_ID = USU.USU_ID)\n"
            + "WHERE USU.USU_LOGIN = :login\n"
            + "AND EST.PES_FINAL != 1\n"
            + "AND DOC.CUANDO_MOD BETWEEN :fechaInicial AND :fechaFinal";

    /*
	 * 2017-04-18 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech)
	 * 
	 * 2017-06-29 jgarcia@controltechcg.com Issue #113 (SICDI-Controltech)
	 * feature-113: Modificación en sentencia SQL que obtiene los documentos en
	 * bandeja de apoyo y consulta del usuario en sesión, para presentar la
	 * información ordenada por la fecha de creación del documento en orden
	 * descendente.
	 * 
	 * 2017-07-10 jgarcia@controltechcg.com Issue #115 (SICDI-Controltech)
	 * feature-115: Modificación de sentencia de bandeja enviados para filtro
	 * por rango de fechas.
     */
    String CONSULTABANDEJACONSULTA = ""
            + "SELECT DOCUMENTO.*\n"
            + "FROM DOCUMENTO\n"
            + "    JOIN DOCUMENTO_EN_CONSULTA ON (DOCUMENTO_EN_CONSULTA.DOC_ID = DOCUMENTO.DOC_ID)\n"
            + "    JOIN USUARIO USUARIO_QUIEN ON (USUARIO_QUIEN.USU_ID = DOCUMENTO_EN_CONSULTA.QUIEN)\n"
            + "WHERE DOCUMENTO_EN_CONSULTA.ACTIVO = 1\n"
            + "AND USUARIO_QUIEN.USU_LOGIN = :login\n"
            + "AND USUARIO_QUIEN.ACTIVO = 1\n"
            + "AND DOCUMENTO.CUANDO BETWEEN :fechaInicial AND :fechaFinal";

    Documento findOneByInstanciaId(String pin);

    Documento findOneByExpediente(Expediente e);

    @Query(value = "SELECT d FROM Documento d WHERE d.elabora.id = :id OR d.firma.id = :id OR d.vistoBueno.id = :id OR d.aprueba.id = :id")
    List<Documento> findDocumentoByUsuario(@Param("id") Integer id);

    @Query(value = "SELECT d FROM Documento d WHERE d.cuando >= :finicio AND d.cuando <= :ffin")
    List<Documento> findDocumentoByFechaInicioYfin(@Param("finicio") Date finicio, @Param("ffin") Date ffin);

    @Query(value = "SELECT d FROM Documento d WHERE d.clasificacion.id = :idClasificacion")
    List<Documento> findDocumentoByClasificacion(@Param("idClasificacion") Integer idClasificacion);

    @Query(value = "SELECT d FROM PDFDocumento d WHERE d.id = :id")
    PDFDocumento findPDFDocumento(@Param("id") String id);

    @Query(nativeQuery = true, value = "select FN_DOCUMENTO_RADICADO(?) from dual")
    String getRadicado(Integer depId);

    List<Documento> findByPrestado(boolean prestado);

    List<Documento> findByExpediente(Expediente expediente, Sort sort);

    List<Documento> findByTrdIdAndExpedienteIsNull(Integer tdrid, Sort sort);

    List<Documento> findByCodigoValidaScannerAndEstadoCodigoValidaScanner(String codigoValidaScanner, Integer estadoCodigoValidaScanner);

    @Query(nativeQuery = true, value = "SELECT EXTRACT(YEAR FROM DOC_PLAZO) - EXTRACT(YEAR FROM CUANDO) FROM DOCUMENTO")
    Integer getPlazoInYears();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM DOCUMENTO WHERE CODIGO_VALIDA_SCANNER = :codigo AND ESTADO_CODIGO_VALIDA_SCANNER = 1")
    Integer getCantidadCodigoScanerValido(@Param("codigo") String codigo);

    @Query(nativeQuery = true, value = "SELECT u.USU_LOGIN, vb.CUANDO, u.USU_NOMBRE, u.USU_GRADO FROM DOCUMENTO_USU_VISTOS_BUENOS vb INNER JOIN USUARIO u ON vb.USU_ID_VISTO_BUENO = u.USU_ID WHERE vb.DOC_ID = ? ORDER BY vb.CUANDO ASC")
    List<Object[]> findVistosBuenosDocumentos(String docID);

    @Query(nativeQuery = true, value = "SELECT DOC.* FROM DOCUMENTO DOC "
            + "JOIN EXPEDIENTE EXP ON DOC.EXP_ID=EXP.EXP_ID "
            + "JOIN EXPEDIENTE_ESTADO EXES ON EXP.EXP_ID= EXES.EXP_ID "
            + "JOIN ESTADO_EXPEDIENTE ESEX ON EXES.EXES_ID=ESEX.ESEX_ID " + "WHERE ESEX.ESEX_NOMBRE=?")
    List<Documento> findByEstadoExpediente(String estadoExpeidente);

    // 2017-02-22 jgarcia@controltechcg.com Issue #141 Búsqueda de la fecha de
    // firma del documento.
    // 2017-02-24 jgarcia@controltechcg.com Issue #141: Corrección de la
    // sentencia SQL a partir de la información presentada en el histórico del
    // proceso.
    @Query(nativeQuery = true, value = "SELECT MIN(CUANDO_MOD) FROM H_PROCESO_INSTANCIA WHERE PIN_ID = :pinID AND PES_ID = 49")
    Date findCuandoFirma(@Param("pinID") String pinID);

    /**
     * Obtiene la lista de documentos a presentar en la bandeja de consulta para
     * un usuario.
     *
     * @param usuarioLogin Login del usuario.
     * @return Lista de documentos en bandeja de consulta.
     */
    /*
	 * 2017-04-18 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech)
	 * 
	 * 2017-06-29 jgarcia@controltechcg.com Issue #113 (SICDI-Controltech)
	 * feature-113: Modificación en sentencia SQL que obtiene los documentos en
	 * bandeja de apoyo y consulta del usuario en sesión, para presentar la
	 * información ordenada por la fecha de creación del documento en orden
	 * descendente.
	 * 
	 * 2017-07-10 jgarcia@controltechcg.com Issue #115 (SICDI-Controltech)
	 * feature-115: Modificación de sentencia de bandeja enviados para filtro
	 * por rango de fechas.
     */
    @Query(nativeQuery = true, value = ""
            + " SELECT                                                                             "
            + " DOCUMENTO.*                                                                        "
            + " FROM DOCUMENTO                                                                     "
            + " JOIN DOCUMENTO_EN_CONSULTA ON (DOCUMENTO_EN_CONSULTA.DOC_ID = DOCUMENTO.DOC_ID)    "
            + " JOIN USUARIO USUARIO_QUIEN ON (USUARIO_QUIEN.USU_ID = DOCUMENTO_EN_CONSULTA.QUIEN) "
            + " WHERE                                                                              "
            + " DOCUMENTO_EN_CONSULTA.ACTIVO = 1                                                   "
            + " AND USUARIO_QUIEN.USU_LOGIN = :login                                               "
            + " AND USUARIO_QUIEN.ACTIVO = 1                                                       "
            + " AND DOCUMENTO.CUANDO BETWEEN :fechaInicial AND :fechaFinal                         "
            + " ORDER BY DOCUMENTO.CUANDO DESC                                                     ")
    List<Documento> findBandejaConsulta(@Param("login") String usuarioLogin, @Param("fechaInicial") Date fechaInicial,
            @Param("fechaFinal") Date fechaFinal);

    /**
     * Obtiene el numero de registros de las bandejas de entrada por usuario.
     *
     * @param login
     * @return Numero de registros.
     */
    @Query(value = "select count(1)\n"
            + "from(\n"
            + CONSULTABANDEJAENTRADA
            + ") documento\n", nativeQuery = true)
    int findBandejaEntradaCount(@Param("login") String login);

    /**
     * Obtiene los registros de las bandejas de entrada por usuario, de acuerdo
     * a la fila inicial y final.
     *
     * @param login
     * @param inicio
     * @param fin
     * @return Lista de bandejas de entrada.
     */
    @Query(value = ""
            + "select doc.*\n"
            + "from(\n"
            + "     select doc.*, rownum num_lineas\n"
            + "     from(\n"
            + CONSULTABANDEJAENTRADA
            + "         ORDER BY documento.cuando_mod DESC\n"
            + "     )doc\n"
            + ") doc\n"
            + "where doc.num_lineas >= :inicio and doc.num_lineas <= :fin\n", nativeQuery = true)
    List<Documento> findBandejaEntradaPaginado(@Param("login") String login, @Param("inicio") int inicio, @Param("fin") int fin);

    /**
     * Obtiene el numero de registros de las bandejas de enviados por usuario y
     * fechas.
     *
     * @param login
     * @param fechaInicial
     * @param fechaFinal
     * @return Numero de registros.
     */
    @Query(value = ""
            + "select count(1)\n"
            + "from(\n"
            + CONSULTABANDEJAENVIADOS
            + ")", nativeQuery = true)
    int findBandejaEnviadosCount(@Param("login") String login, @Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal);

    /**
     * Obtiene la lista de registros de las bandejas de enviados por usuario y
     * fechas paginado.
     *
     * @param login
     * @param fechaInicial
     * @param fechaFinal
     * @param inicio
     * @param fin
     * @return Numero de registros.
     */
    @Query(value = ""
            + "select doc.*\n"
            + "from(\n"
            + "     select doc.*, rownum num_lineas\n"
            + "     from(\n"
            + CONSULTABANDEJAENVIADOS
            + "         ORDER BY doc.cuando_mod DESC"
            + "     )doc\n"
            + ") doc\n"
            + "where doc.num_lineas >= :inicio and doc.num_lineas <= :fin\n", nativeQuery = true)
    List<Documento> findBandejaEnviadosPaginado(@Param("login") String login, @Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal, @Param("inicio") int inicio, @Param("fin") int fin);

    /**
     * Obtiene el numero de registros de las bandejas de tramites por usuario y
     * fechas.
     *
     * @param login
     * @param fechaInicial
     * @param fechaFinal
     * @return Numero de registros.
     */
    @Query(value = ""
            + "select count(1)\n"
            + "from(\n"
            + CONSULTABANDEJAENTRAMITE
            + ")", nativeQuery = true)
    int findBandejaTramiteCount(@Param("login") String login, @Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal);

    /**
     * Obtiene la lista de registros de las bandejas de tramite por usuario y
     * fechas paginado.
     *
     * @param login
     * @param fechaInicial
     * @param fechaFinal
     * @param inicio
     * @param fin
     * @return Numero de registros.
     */
    @Query(value = ""
            + "select doc.*"
            + "from("
            + "     select doc.*, rownum num_lineas\n"
            + "     from(\n"
            + CONSULTABANDEJAENTRAMITE
            + "         ORDER BY doc.CUANDO DESC"
            + "     )doc\n"
            + ") doc\n"
            + "where doc.num_lineas >= :inicio and doc.num_lineas <= :fin\n", nativeQuery = true)
    List<Documento> findBandejaTramitePaginado(@Param("login") String login, @Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal, @Param("inicio") int inicio, @Param("fin") int fin);

    /**
     * Obtiene el numero de registros de las bandejas de consulta por usuario y
     * fechas.
     *
     * @param login
     * @param fechaInicial
     * @param fechaFinal
     * @return Numero de registros.
     */
    @Query(value = ""
            + "select count(1)\n"
            + "from(\n"
            + CONSULTABANDEJACONSULTA
            + ")", nativeQuery = true)
    int findBandejaConsultaCount(@Param("login") String login, @Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal);

    /**
     * Obtiene la lista de registros de las bandejas de consulta por usuario y
     * fechas paginado.
     *
     * @param login
     * @param fechaInicial
     * @param fechaFinal
     * @param inicio
     * @param fin
     * @return Numero de registros.
     */
    @Query(value = ""
            + "select doc.*"
            + "from("
            + "     select doc.*, rownum num_lineas\n"
            + "     from(\n"
            + CONSULTABANDEJACONSULTA
            + "         ORDER BY DOCUMENTO.CUANDO DESC"
            + "     )doc\n"
            + ") doc\n"
            + "where doc.num_lineas >= :inicio and doc.num_lineas <= :fin\n", nativeQuery = true)
    List<Documento> findBandejaConsultaPaginado(@Param("login") String login, @Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal, @Param("inicio") int inicio, @Param("fin") int fin);

    /*
	 * 2017-03-27 jgarcia@controltechcg.com Issue #22 (SIGDI-Incidencias01):
	 * Modificación de consulta SQL de la bandeja de enviados para que no
	 * presente la información de los documentos anulados.
	 * 
	 * 2017-07-05 jgarcia@controltechcg.com Issue #115 (SICDI-Controltech)
	 * feature-115: Modificación de sentencia de bandeja enviados para filtro
	 * por rango de fechas.
	 * 
	 * 2017-07-25 jgarcia@controltechcg.com Issue #118 (SICDI-Controltech)
	 * hotfix-118: Corrección en la sentencia SQL de la bandeja de enviados,
	 * para que no presente documentos cuyo usuario asignado actual corresponda
	 * al usuario en sesión.
     */
    @Query(nativeQuery = true, value = ""
            + " SELECT EST.PES_ID,                                                               "
            + " DOC.*                                                                            "
            + " FROM DOCUMENTO DOC                                                               "
            + " JOIN S_INSTANCIA_USUARIO HPIN                                                    "
            + " ON DOC.PIN_ID = HPIN.PIN_ID                                                      "
            + " JOIN PROCESO_INSTANCIA PIN                                                       "
            + " ON DOC.PIN_ID = PIN.PIN_ID                                                       "
            + " JOIN PROCESO_ESTADO EST                                                          "
            + " ON EST.PES_ID = PIN.PES_ID                                                       "
            + " JOIN USUARIO USU                                                                 "
            + " ON HPIN.USU_ID = USU.USU_ID                                                      "
            + " JOIN USUARIO USU_ASIGNADO                                                        "
            + " ON (USU_ASIGNADO.USU_ID     = PIN.USU_ID_ASIGNADO)                               "
            + " WHERE USU.USU_LOGIN         = ?                                                  "
            + " AND USU_ASIGNADO.USU_LOGIN <> ?                                                  "
            + " AND DOC.DOC_RADICADO       IS NOT NULL                                           "
            + " AND EST.PES_FINAL           = 1                                                  "
            + " AND EST.PES_ID NOT         IN (83, 101)                                          "
            + " AND DOC.CUANDO_MOD BETWEEN ? AND ?                                               "
            + " ORDER BY DOC.CUANDO_MOD DESC                                                     ")
    List<Documento> findBandejaEnviados(String login, String loginAsignado, Date fechaInicial, Date fechaFinal);

    /*
	 * 2017-07-10 jgarcia@controltechcg.com Issue #115 (SICDI-Controltech)
	 * feature-115: Modificación de sentencia de bandeja enviados para filtro
	 * por rango de fechas.
	 * 
	 * 2017-07-11 jgarcia@controltechcg.com Issue #115 (SICDI-Controltech)
	 * feature-115: Modificación en fecha de filtro de dato de creación por dato
	 * de última modificación del documento.
     */
    @Query(nativeQuery = true, value = ""
            + " SELECT                                                                           "
            + " DOC.*                                                                            "
            + " FROM DOCUMENTO DOC                                                               "
            + " JOIN S_INSTANCIA_USUARIO HPIN ON (DOC.PIN_ID = HPIN.PIN_ID)                      "
            + " JOIN PROCESO_INSTANCIA   PIN  ON (DOC.PIN_ID = PIN.PIN_ID)                       "
            + " JOIN PROCESO_ESTADO      EST  ON (EST.PES_ID = PIN.PES_ID)                       "
            + " JOIN USUARIO             USU  ON (HPIN.USU_ID = USU.USU_ID)                      "
            + " WHERE                                                                            "
            + " USU.USU_LOGIN = ?                                                                "
            + " AND EST.PES_FINAL != 1                                                           "
            + " AND DOC.CUANDO_MOD BETWEEN ? AND ?                                               "
            + " ORDER BY                                                                         "
            + " DOC.CUANDO DESC                                                                  ")
    List<Documento> findBandejaTramite(String name, Date fechaInicial, Date fechaFinal);

    @Query(value = ""
            + "select count(1) "
            + "from( "
            + "    select usu_id "
            + "    from S_INSTANCIA_USUARIO "
            + "    where usu_id = :usuId "
            + "     and pin_id = :pinId "
            + "    union "
            + "    select c.usu_id "
            + "    from DOCUMENTO_DEPENDENCIA a, "
            + "         DOCUMENTO b, "
            + "         usuario c "
            + "    where b.doc_id = a.doc_id "
            + "    and b.pin_id = :pinId "
            + "    and c.usu_id = a.quien"
            + "    and c.usu_id = :usuId "
            + ")", nativeQuery = true)
    Integer verificaAccesoDocumento(@Param("usuId") Integer usuId, @Param("pinId") String pinId);

    /*
	 * 2017-11-14 edison.gonzalez@controltechcg.com Issue #138 (SICDI-Controltech)
	 * feature-138: Creacion de la nueva funcion para generar el numero de radicado,
         * segun la dependencia y el id del proceso.
     */
    @Query(nativeQuery = true, value = "select FN_GENERA_NUM_RADICADO(?,?) from dual")
    String getNumeroRadicado(Integer depId, Integer radId);

    /**
     * Busca un documento por el UUID de firma y envío.
     *
     * @param firmaEnvioUUID UUID de firma y envío.
     * @return Documento correspondiente al UUID, o {@code null} en caso que no
     * exista correspondencia en el sistema.
     */
    public Documento findOneByFirmaEnvioUUID(String firmaEnvioUUID);
}
