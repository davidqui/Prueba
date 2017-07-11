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

	@Query(value = "SELECT d FROM Documento d WHERE d.elabora.id = :id OR d.firma.id = :id OR d.vistoBueno.id = :id OR d.aprueba.id = :id")
	List<Documento> findDocumentoByUsuario(@Param("id") Integer id);

	@Query(value = "SELECT d FROM Documento d WHERE d.cuando >= :finicio AND d.cuando <= :ffin")
	List<Documento> findDocumentoByFechaInicioYfin(@Param("finicio") Date finicio, @Param("ffin") Date ffin);

	@Query(value = "SELECT d FROM Documento d WHERE d.clasificacion.id = :idClasificacion")
	List<Documento> findDocumentoByClasificacion(@Param("idClasificacion") Integer idClasificacion);

	Documento findOneByInstanciaId(String pin);

	Documento findOneByExpediente(Expediente e);

	@Query(nativeQuery = true, value = "select FN_DOCUMENTO_RADICADO(?) from dual")
	String getRadicado(Integer depId);

	List<Documento> findByExpediente(Expediente expediente, Sort sort);

	List<Documento> findByCodigoValidaScannerAndEstadoCodigoValidaScanner(String codigoValidaScanner,
			Integer estadoCodigoValidaScanner);

	List<Documento> findByTrdIdAndExpedienteIsNull(Integer tdrid, Sort sort);

	@Query(nativeQuery = true, value = "SELECT EXTRACT(YEAR FROM DOC_PLAZO) - EXTRACT(YEAR FROM CUANDO) FROM DOCUMENTO")
	Integer getPlazoInYears();

	@Query(nativeQuery = true, value = "SELECT COUNT(*) FROM DOCUMENTO WHERE CODIGO_VALIDA_SCANNER = :codigo AND ESTADO_CODIGO_VALIDA_SCANNER = 1")
	Integer getCantidadCodigoScanerValido(@Param("codigo") String codigo);

	List<Documento> findByPrestado(boolean prestado);

	@Query(nativeQuery = true, value = "SELECT DOC.* FROM DOCUMENTO DOC "
			+ "JOIN EXPEDIENTE EXP ON DOC.EXP_ID=EXP.EXP_ID "
			+ "JOIN EXPEDIENTE_ESTADO EXES ON EXP.EXP_ID= EXES.EXP_ID "
			+ "JOIN ESTADO_EXPEDIENTE ESEX ON EXES.EXES_ID=ESEX.ESEX_ID " + "WHERE ESEX.ESEX_NOMBRE=?")
	List<Documento> findByEstadoExpediente(String estadoExpeidente);

	/*
	 * 2017-02-14 jgarcia@controltechcg.com Issue #108: Se añade el estado
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
	@Query(nativeQuery = true, value = ""
			+ " SELECT                                                                           "
			+ " DOCUMENTO.*                                                                      "
			+ " FROM                                                                             "
			+ " DOCUMENTO                                                                        "
			+ " LEFT JOIN DOCUMENTO_EN_CONSULTA ON (DOCUMENTO_EN_CONSULTA.DOC_ID = DOCUMENTO.DOC_ID) "
			+ " WHERE                                                                            "
			+ " DOCUMENTO.DOC_ID IN (                                                            "
			+ " SELECT DISTINCT                                                                  "
			+ " DOCUMENTO.DOC_ID                                                                 "
			+ " FROM                                                                             "
			+ " DOCUMENTO                                                                        "
			+ " JOIN PROCESO_INSTANCIA          ON (PROCESO_INSTANCIA.PIN_ID = DOCUMENTO.PIN_ID) "
			+ " JOIN USUARIO USUARIO_ASIGNADO   ON (USUARIO_ASIGNADO.USU_ID  = PROCESO_INSTANCIA.USU_ID_ASIGNADO) "
			+ " WHERE                                                                            "
			+ " 1 = 1                                                                            "
			+ " AND DOCUMENTO.DOC_ASUNTO       IS NOT NULL                                       "
			+ " AND PROCESO_INSTANCIA.PES_ID   NOT IN (48,52,83,101,102)                         "
			+ " AND USUARIO_ASIGNADO.USU_LOGIN = :login                                          "
			+ " AND NOT (PROCESO_INSTANCIA.PRO_ID = 41 AND PROCESO_INSTANCIA.PES_ID = 49)        "
			+ " UNION                                                                            "
			+ " SELECT                                                                           "
			+ " DOCUMENTO_DEP_DESTINO.DOC_ID                                                     "
			+ " FROM                                                                             "
			+ " DOCUMENTO_DEP_DESTINO                                                            "
			+ " JOIN DOCUMENTO                          ON (DOCUMENTO.DOC_ID          = DOCUMENTO_DEP_DESTINO.DOC_ID) "
			+ " JOIN PROCESO_INSTANCIA                  ON (PROCESO_INSTANCIA.PIN_ID  = DOCUMENTO.PIN_ID) "
			+ " JOIN DEPENDENCIA                        ON (DEPENDENCIA.DEP_ID        = DOCUMENTO_DEP_DESTINO.DEP_ID) "
			+ " LEFT JOIN USUARIO USUARIO_JEFE_1_DEP    ON (USUARIO_JEFE_1_DEP.USU_ID = DEPENDENCIA.USU_ID_JEFE) "
			+ " LEFT JOIN USUARIO USUARIO_JEFE_2_DEP    ON (USUARIO_JEFE_2_DEP.USU_ID = DEPENDENCIA.USU_ID_JEFE_ENCARGADO) "
			+ " WHERE                                                                            "
			+ " 1 = 1                                                                            "
			+ " AND DOCUMENTO.DOC_ASUNTO IS NOT NULL                                             "
			+ " AND PROCESO_INSTANCIA.PES_ID = 49                                                "
			+ " AND DOCUMENTO_DEP_DESTINO.ACTIVO = 1                                             "
			+ " AND (                                                                            "
			+ " (DEPENDENCIA.FCH_INICIO_JEFE_ENCARGADO <= SYSDATE AND SYSDATE <= DEPENDENCIA.FCH_FIN_JEFE_ENCARGADO AND USUARIO_JEFE_2_DEP.USU_LOGIN = :login) "
			+ " OR (((DEPENDENCIA.FCH_INICIO_JEFE_ENCARGADO IS NULL OR DEPENDENCIA.FCH_FIN_JEFE_ENCARGADO IS NULL) OR (NOT(DEPENDENCIA.FCH_INICIO_JEFE_ENCARGADO <= SYSDATE AND SYSDATE <= DEPENDENCIA.FCH_FIN_JEFE_ENCARGADO))) AND USUARIO_JEFE_1_DEP.USU_LOGIN = :login) "
			+ " )                                                                                "
			+ " )                                                                                "
			+ " AND                                                                              "
			+ " (DOCUMENTO_EN_CONSULTA.DEC_ID IS NULL OR (DOCUMENTO_EN_CONSULTA.ACTIVO = 0))     "
			+ " ORDER BY DOCUMENTO.CUANDO_MOD DESC                                               ")
	List<Documento> findBandejaEntrada(@Param("login") String login);

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

	/*
	 * 2017-03-27 jgarcia@controltechcg.com Issue #22 (SIGDI-Incidencias01):
	 * Modificación de consulta SQL de la bandeja de enviados para que no
	 * presente la información de los documentos anulados.
	 * 
	 * 2017-07-05 jgarcia@controltechcg.com Issue #115 (SICDI-Controltech)
	 * feature-115: Modificación de sentencia de bandeja enviados para filtro
	 * por rango de fechas.
	 */
	@Query(nativeQuery = true, value = ""
			+ " SELECT EST.PES_ID, DOC.*                                                         "
			+ " FROM DOCUMENTO DOC                                                               "
			+ " JOIN S_INSTANCIA_USUARIO HPIN ON DOC.PIN_ID = HPIN.PIN_ID                        "
			+ " JOIN PROCESO_INSTANCIA PIN ON DOC.PIN_ID = PIN.PIN_ID                            "
			+ " JOIN PROCESO_ESTADO EST ON EST.PES_ID = PIN.PES_ID                               "
			+ " JOIN USUARIO USU ON HPIN.USU_ID = USU.USU_ID                                     "
			+ " WHERE USU.USU_LOGIN = ?                                                          "
			+ " AND DOC.DOC_RADICADO IS NOT NULL                                                 "
			+ " AND EST.PES_FINAL = 1                                                            "
			+ " AND EST.PES_ID NOT IN (83, 101)                                                  "
			+ " AND DOC.CUANDO_MOD BETWEEN ? AND ?                                               "
			+ " ORDER BY DOC.CUANDO_MOD DESC                                                     ")
	List<Documento> findBandejaEnviados(String name, Date fechaInicial, Date fechaFinal);

	@Query(nativeQuery = true, value = "SELECT u.USU_LOGIN, vb.CUANDO, u.USU_NOMBRE, u.USU_GRADO FROM DOCUMENTO_USU_VISTOS_BUENOS vb INNER JOIN USUARIO u ON vb.USU_ID_VISTO_BUENO = u.USU_ID WHERE vb.DOC_ID = ? ORDER BY vb.CUANDO ASC")
	List<Object[]> findVistosBuenosDocumentos(String docID);

	@Query(value = "SELECT d FROM PDFDocumento d WHERE d.id = :id")
	PDFDocumento findPDFDocumento(@Param("id") String id);

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
	 * @param usuarioLogin
	 *            Login del usuario.
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
}
