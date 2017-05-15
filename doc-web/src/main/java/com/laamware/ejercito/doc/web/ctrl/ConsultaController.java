package com.laamware.ejercito.doc.web.ctrl;

import java.security.Principal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.laamware.ejercito.doc.web.dto.DocumentoDTO;
import com.laamware.ejercito.doc.web.entity.Clasificacion;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.BandejaRepository;
import com.laamware.ejercito.doc.web.repo.ClasificacionRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.ExpedienteRepository;
import com.laamware.ejercito.doc.web.serv.ProcesoService;
import com.laamware.ejercito.doc.web.util.DateUtil;

@Controller
@RequestMapping(value = ConsultaController.PATH)
public class ConsultaController extends UtilController {

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

	public static final String PATH = "/consulta";

	@Autowired
	DataSource ds;

	@Autowired
	BandejaRepository banR;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	ProcesoService procesoService;

	@Autowired
	DocumentoRepository documentoRepository;

	@Autowired
	ExpedienteRepository expedienteRepository;

	@Autowired
	ClasificacionRepository clasificacionRepository;

	JdbcTemplate jdbcTemplate;

	/**
	 * Ejecuta la búsqueda de documentos cuyo asunto o contenido contenga el
	 * texto ingresado como términos de búsqueda
	 * 
	 * @param term
	 *            Texto a buscar
	 * @param model
	 * @param principal
	 * @return
	 */
	@PreAuthorize("hasRole('BANDEJAS')")
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String consulta(@RequestParam(value = "term") String term, Model model, Principal principal) {
		// System.out.println("ConsultaController.consulta()>>>");
		// System.out.println("term=" + term);

		if (StringUtils.isBlank(term)) {
			return "consulta";
		}

		jdbcTemplate = new JdbcTemplate(ds);

		buscar(model, term, term, term, null, null, term, null, term, term, null, null, principal);

		model.addAttribute("term", term);

		// System.out.println("<<<ConsultaController.consulta()");

		return "consulta";
	}

	// Issue #106
	private static final SimpleDateFormat DATE_FORMAT_YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");

	/***
	 * 
	 * @param model
	 * @param asignado
	 * @param asunto
	 * @param contenido
	 * @param fechaInicio
	 * @param fechaFin
	 * @param radicado
	 * @param expediente
	 * @param destinatario
	 * @param remitente
	 * @param clasificacion
	 * @param principal
	 * @return
	 */
	/*
	 * 2017-02-10 jgarcia@controltechcg.com Issue #105: Se modifica todo el
	 * cuerpo del método buscar() para construir una sola sentencia SQL a partir
	 * de los valores ingresados desde el formulario. 2017-02-13
	 * jgarcia@controltechcf.com Issue #77: Se amplían los valores de búsqueda,
	 * para obtener el campo de la dependencia.
	 */
	@RequestMapping(value = "/parametros", method = RequestMethod.GET)
	public String buscar(Model model, @RequestParam(value = "asignado", required = false) String asignado,
			@RequestParam(value = "asunto", required = false) String asunto,
			@RequestParam(value = "contenido", required = false) String contenido,
			@RequestParam(value = "fechaInicio", required = false) String fechaInicio,
			@RequestParam(value = "fechaFin", required = false) String fechaFin,
			@RequestParam(value = "radicado", required = false) String radicado,
			@RequestParam(value = "expediente", required = false) Integer expediente,
			@RequestParam(value = "destinatario", required = false) String destinatario,
			@RequestParam(value = "remitente", required = false) String remitente,
			@RequestParam(value = "clasificacion", required = false) Integer clasificacion,
			@RequestParam(value = "dependenciaDestino", required = false) Integer dependenciaDestino,
			Principal principal) {
		// System.out.println("ConsultaController.buscar()>>>");
		// System.out.println("model=[" + model + "]");
		// System.out.println("asignado=[" + asignado + "]");
		// System.out.println("asunto=[" + asunto + "]");
		// System.out.println("contenido=[" + contenido + "]");
		// System.out.println("fechaInicio=[" + fechaInicio + "]");
		// System.out.println("fechaFin=[" + fechaFin + "]");
		// System.out.println("radicado=[" + radicado + "]");
		// System.out.println("expediente=[" + expediente + "]");
		// System.out.println("destinatario=[" + destinatario + "]");
		// System.out.println("remitente=[" + remitente + "]");
		// System.out.println("clasificacion=[" + clasificacion + "]");
		// System.out.println("dependenciaDestino=[" + dependenciaDestino +
		// "]");
		// System.out.println("principal=[" + principal + "]");

		// Issue #105, Issue #128
		Object[] args = { asignado, asunto, contenido, fechaInicio, fechaFin, radicado, expediente, destinatario,
				remitente, clasificacion, dependenciaDestino };

		// 2017-02-14 jgarcia@controltechcg.com Issue #128: Corrección consulta
		// con único valor de filtro general.

		ArrayList<Object> stringArgs = new ArrayList<>();
		for (int i = 0; i < args.length; i++) {
			if (args[i] != null && args[i].getClass().equals(String.class)) {
				// 2017-02-15 jgarcia@controltechcg.com Issue #142: Corrección
				// para que no salga error cuando no digitan ni seleccionan
				// ningún valor en el formulario.
				if (((String) args[i]).trim().isEmpty()) {
					args[i] = null;
				} else {
					stringArgs.add(args[i]);
				}
			}
		}

		boolean hasNotNullArgs = false;
		for (Object arg : args) {
			if (arg != null) {
				hasNotNullArgs = true;
				break;
			}
		}

		if (!hasNotNullArgs) {
			return "consulta-parametros";
		}

		boolean sameValue = true;
		Object lastValue = null;
		for (int i = 0; i < stringArgs.size(); i++) {
			Object arg = stringArgs.get(i);
			if (i == 0) {
				lastValue = arg;
			} else if (!Objects.equals(lastValue, arg)) {
				sameValue = false;
				break;
			}
		}

		// Issue #128
		SentenceOperator operator = (sameValue) ? SentenceOperator.OR : SentenceOperator.AND;

		Set<DocumentoDTO> documentosTemporal = new HashSet<>();

		/*
		 * 2017-02-06 jgarcia@controltechcg.com Issue #128: Modificación en los
		 * procesos de búsqueda, para que únicamente presente la información de
		 * los documentos asociados al usuario en los pasos de creación y firma.
		 */
		Usuario usuario = getUsuario(principal);
		Integer usuarioID = usuario.getId();
		// System.out.println("usuario=" + usuario);

		expedientes(model, principal);

		jdbcTemplate = new JdbcTemplate(ds);

		LinkedList<Object> parameters = new LinkedList<>();

		StringBuilder sql = new StringBuilder();
		// 2017-02-17 jgarcia@controltechcg.com Issue #128: Se corrige sentencia
		// SQL para evitar repetición de la información.
		sql.append("SELECT DISTINCT \n");
		sql.append("DOC.DOC_ID \n");
		sql.append("FROM DOCUMENTO DOC \n");
		sql.append(
				"LEFT JOIN USUARIO USU_ULT_ACCION		ON (DOC.USU_ID_ULTIMA_ACCION	= USU_ULT_ACCION.USU_ID) \n");
		sql.append("LEFT JOIN DEPENDENCIA DEP 				ON (DOC.DEP_ID_DES 				= DEP.DEP_ID) \n");
		sql.append("LEFT JOIN USUARIO USU_DEP_JEFE 			ON (DEP.USU_ID_JEFE 			= USU_DEP_JEFE.USU_ID) \n");

		// 2017-02-15 jgarcia@controltechcg.com Issue #142: Nuevas asociaciones
		// para obtener los campos de fechas solicitados.
		sql.append("LEFT JOIN PROCESO_INSTANCIA INSTANCIA	ON (DOC.PIN_ID 					= INSTANCIA.PIN_ID) \n");
		sql.append("LEFT JOIN DOCUMENTO_USU_FIRMA DOCFIRMA 	ON (DOC.DOC_ID 					= DOCFIRMA.DOC_ID ) \n");

		sql.append("WHERE 1 = 1 \n");

		sql.append("AND (DOC.USU_ID_ELABORA = ? OR DOC.USU_ID_FIRMA = ?) \n");
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
			sql.append((hasConditions ? operator.name() : "") + " LOWER(DOC.DOC_ASUNTO) LIKE LOWER( ? ) \n");

			parameters.add("%" + asunto.trim() + "%");

			hasConditions = true;
		}

		/*
		 * 2017-02-15 jgarcia@controltechcg.com Issue #142: Se separa la
		 * validación realizada sobre los campos de fecha para que la búsqueda
		 * sea independiente y no se necesite siempre de los dos campos para
		 * filtrar. Se utilizan los campos de fecha indicados en el issue, según
		 * el proceso asociado al documento.
		 */
		if (StringUtils.isNotBlank(fechaInicio)) {
			Date fInicio;
			try {
				fInicio = DateUtil.setTime(DATE_FORMAT_YYYYMMDD.parse(fechaInicio.trim()),
						DateUtil.SetTimeType.START_TIME);

				sql.append((hasConditions ? operator.name() : "") + " ((CASE WHEN INSTANCIA.PRO_ID = "
						+ Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS
						+ " THEN DOC.CUANDO ELSE DOCFIRMA.CUANDO END) >= ?) \n");

				parameters.add(fInicio);

				hasConditions = true;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		/*
		 * 2017-02-15 jgarcia@controltechcg.com Issue #142: Se separa la
		 * validación realizada sobre los campos de fecha para que la búsqueda
		 * sea independiente y no se necesite siempre de los dos campos para
		 * filtrar. Se utilizan los campos de fecha indicados en el issue, según
		 * el proceso asociado al documento.
		 */
		if (StringUtils.isNotBlank(fechaFin)) {
			try {

				Date fFin = DateUtil.setTime(DATE_FORMAT_YYYYMMDD.parse(fechaFin.trim()),
						DateUtil.SetTimeType.END_TIME);

				sql.append((hasConditions ? operator.name() : "") + " ((CASE WHEN INSTANCIA.PRO_ID = "
						+ Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS
						+ " THEN DOC.CUANDO ELSE DOCFIRMA.CUANDO END) <= ?) \n");

				parameters.add(fFin);

				hasConditions = true;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (StringUtils.isNotBlank(radicado)) {
			// Issue #128
			sql.append((hasConditions ? operator.name() : "") + " LOWER(DOC.DOC_RADICADO) LIKE LOWER( ? ) \n");

			parameters.add("%" + radicado.trim() + "%");

			hasConditions = true;
		}

		if (StringUtils.isNotBlank(destinatario)) {
			// Issue #128
			sql.append((hasConditions ? operator.name() : "") + " ( \n");
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
			sql.append((hasConditions ? operator.name() : "") + " DOC.CLA_ID = ? \n");

			parameters.add(clasificacion);

			hasConditions = true;
		}

		// Issue #77
		if (dependenciaDestino != null) {
			// Issue #128
			sql.append((hasConditions ? operator.name() : "") + " DOC.DEP_ID_DES = ? \n");

			parameters.add(dependenciaDestino);

			hasConditions = true;
		}

		// Issue #128
		sql.append(" ) \n");

		// System.out.println("sql.toString()»»»" + sql.toString());
		// System.out.println("parameters.toArray()»»»" +
		// java.util.Arrays.toString(parameters.toArray()));

		documentosTemporal
				.addAll(jdbcTemplate.query(sql.toString(), parameters.toArray(), new RowMapper<DocumentoDTO>() {
					@Override
					public DocumentoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
						DocumentoDTO documentoDTO = new DocumentoDTO();
						documentoDTO.setId(rs.getString("DOC_ID"));
						return documentoDTO;
					}
				}));

		List<Documento> documentos = new ArrayList<Documento>();
		for (DocumentoDTO documentoDTO : documentosTemporal) {

			Documento documento = documentoRepository.findOne(documentoDTO.getId());
			documentos.add(documento);
			Instancia i = procesoService.instancia(documento.getInstancia().getId());
			documento.setInstancia(i);
		}

		// System.out.println("documentos.size()=" + documentos.size());

		model.addAttribute("totalResultados", documentos.size());
		model.addAttribute("documentos", documentos);

		if (documentos.isEmpty()) {
			return "consulta-parametros";
		}

		return "consulta";
	}

	/**
	 * Carga el listado de expedientes al modelo
	 * 
	 * @return
	 */
	public List<Expediente> expedientesAdicional(Model model, Principal principal) {

		Dependencia dependencia = getUsuario(principal).getDependencia();

		if (dependencia == null)
			return new ArrayList<Expediente>();

		Integer dependenciaId = dependencia.getId();
		List<Expediente> list = expedienteRepository.findByActivoAndDependenciaId(true, dependenciaId,
				new Sort(Direction.ASC, "dato"));
		model.addAttribute("id", list);
		return list;
	}

	/**
	 * Carga el listado de expedientes al modelo
	 * 
	 * @return
	 */
	public List<Expediente> expedientes(Model model, Principal principal) {

		Dependencia dependencia = getUsuario(principal).getDependencia();

		if (dependencia == null)
			return new ArrayList<Expediente>();

		Integer dependenciaId = dependencia.getId();
		List<Expediente> list = expedienteRepository.findByActivoAndDependenciaId(true, dependenciaId,
				new Sort(Direction.ASC, "nombre"));
		model.addAttribute("expedientes", list);
		return list;
	}

	/**
	 * Carga el listado de clasificaciones al modelo
	 * 
	 * @return
	 */
	@ModelAttribute("clasificaciones")
	public List<Clasificacion> clasificaciones() {
		return clasificacionRepository.findByActivo(true, new Sort(Direction.ASC, "orden"));
	}
}
