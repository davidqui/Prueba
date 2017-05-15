package com.laamware.ejercito.doc.web.ctrl;

import java.security.Principal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.EstadoExpediente;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.PrestamoSolicitud;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.ExpedienteRepository;
import com.laamware.ejercito.doc.web.repo.PrestamoSolicitudRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import com.laamware.ejercito.doc.web.util.FreeTextUtils;

/**
 * @author mcr
 *
 */

@Controller
@RequestMapping(value = PrestamoSolicitudController.PATH)
public class PrestamoSolicitudController extends UtilController {

	public static final String PATH = "/archivo/solicitud";

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	PrestamoSolicitudRepository prestamoSolicitudRepository;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DataSource ds;

	@Autowired
	ExpedienteRepository expedienteRepository;

	@Autowired
	private PrestamoSolicitudValidator prestamoSolicitudValidator;

	@Autowired
	private PrestamoSolicitudDocValidator prestamoSolicitudDocValidator;

	// carga el formulario de solicitud de prestamo de expediente
	@RequestMapping(value = "form", method = RequestMethod.GET)
	public String getSolicitudExpediente(
			@RequestParam("uid") Integer usuarioId, Model model,
			Principal principal) {

		Usuario u = usuarioRepository.getOne(usuarioId);
		PrestamoSolicitud prestamoSolicitud = new PrestamoSolicitud();

		prestamoSolicitud.setUsuarioSolicita(u);
		prestamoSolicitud.setDependencia(u.getDependencia());
		prestamoSolicitud.setFechaSolicitud(new Date());

		model.addAttribute("prestamoSolicitud", prestamoSolicitud);

		return "archivo-prestamo-solicitud";
	}

	// Pone la solicitud de prestamo en la bandeja "Solicitud de prestamos" y
	// guardala solicitud
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public String sendSolicitud(
			@ModelAttribute("prestamoSolicitud") PrestamoSolicitud prestamoSolicitud,
			BindingResult result, final RedirectAttributes redirect,
			Model model, Principal principal) {
		prestamoSolicitudValidator.validate(prestamoSolicitud, result);
		if (result.hasErrors()) {

			model.addAttribute("prestamoSolicitud", prestamoSolicitud);
			prestamoSolicitud.setFechaSolicitud(new Date());

			return "archivo-prestamo-solicitud";

		}
		Usuario u = getUsuario(principal);
		prestamoSolicitud.setDependencia(u.getDependencia());

		prestamoSolicitudRepository.save(prestamoSolicitud);
		redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
				"Los datos han sido enviados correctamente");

		return String.format("redirect:%s/list",
				ExpedienteController.PATH);
	}

	// carga el formulario de solicitud de prestamo de documento
	@RequestMapping(value = "/prestamo-solicitud-doc", method = RequestMethod.GET)
	public String getSolicitudDocumento(@RequestParam("uid") Integer usuarioId,
			Model model, Principal principal) {

		Usuario u = usuarioRepository.getOne(usuarioId);
		PrestamoSolicitud prestamoSolicitud = new PrestamoSolicitud();

		prestamoSolicitud.setUsuarioSolicita(u);
		prestamoSolicitud.setDependencia(u.getDependencia());
		prestamoSolicitud.setFechaSolicitud(new Date());

		model.addAttribute("prestamoSolicitud", prestamoSolicitud);

		return "archivo-prestamo-solicitud-doc";
	}

	// Pone la solicitud de prestamo en la bandeja "Solicitud de prestamos" y
	// guardala solicitud
	@RequestMapping(value = "/send-doc", method = RequestMethod.POST)
	public String sendSolicitudDoc(
			@ModelAttribute("prestamoSolicitud") PrestamoSolicitud prestamoSolicitud,
			BindingResult result, final RedirectAttributes redirect,
			Model model, Principal principal) {

		prestamoSolicitudDocValidator.validate(prestamoSolicitud, result);
		if (result.hasErrors()) {

			model.addAttribute("prestamoSolicitud", prestamoSolicitud);
			prestamoSolicitud.setFechaSolicitud(new Date());

			return "archivo-prestamo-solicitud-doc";

		}
		Usuario u = getUsuario(principal);
		prestamoSolicitud.setDependencia(u.getDependencia());

		prestamoSolicitudRepository.save(prestamoSolicitud);
		redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
				"Los datos han sido enviados correctamente");

		return String.format("redirect:%s/list", ExpedienteController.PATH);
	}

	// realiza la búsqueda del expediente por nombre según el término ingresado
	@RequestMapping(value = "/searchByName", method = RequestMethod.GET)
	@ResponseBody
	public List<Expediente> searchExpedienteByName(
			@RequestParam("term") String term) {

		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		StringBuilder sbQuery = new StringBuilder();
		sbQuery.append("SELECT EXP.* FROM EXPEDIENTE EXP "
				+ " JOIN ESTADO_EXPEDIENTE EE ON EXP.ESEX_ID=EE.ESEX_ID "
				+ " WHERE EE.ESEX_NOMBRE='Archivado' AND ");
		sbQuery.append(FreeTextUtils.buildFreeTextORSQLCondition(
				"EXP.EXP_NOMBRE", term, 1));

		List<Expediente> listExpediente = jdbcTemplate.query(
				sbQuery.toString(), new RowMapper<Expediente>() {
					@Override
					public Expediente mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Expediente exp = new Expediente();
						exp.setId(rs.getInt("EXP_ID"));
						exp.setCodigo(rs.getString("EXP_CODIGO"));
						exp.setNombre(rs.getString("EXP_NOMBRE"));
						return exp;
					}
				});

		return listExpediente;

	}

	// realiza la búsqueda del expediente por código segun el término ingresado
	@RequestMapping(value = "/searchByCode", method = RequestMethod.GET)
	@ResponseBody
	public List<Expediente> searchExpedienteByCode(
			@RequestParam("term") String term) {

		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		StringBuilder sbQuery = new StringBuilder();
		sbQuery.append("SELECT EXP.* ,EE.ESEX_NOMBRE FROM EXPEDIENTE EXP "
				+ " JOIN ESTADO_EXPEDIENTE EE ON EXP.ESEX_ID=EE.ESEX_ID "
				+ " WHERE EE.ESEX_NOMBRE='Archivado' "
				+ " AND CONTAINS(EXP.EXP_CODIGO, " + "' ");
		sbQuery.append(term);
		sbQuery.append("%',1)>0 ");

		List<Expediente> listExpediente = jdbcTemplate.query(
				sbQuery.toString(), new RowMapper<Expediente>() {
					@Override
					public Expediente mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Expediente exp = new Expediente();
						exp.setId(rs.getInt("EXP_ID"));
						exp.setCodigo(rs.getString("EXP_CODIGO"));
						exp.setNombre(rs.getString("EXP_NOMBRE"));
						EstadoExpediente estado = new EstadoExpediente();
						exp.setEstado(estado);
						estado.setNombre(rs.getString("ESEX_NOMBRE"));
						return exp;
					}
				});

		return listExpediente;

	}

	// realiza la búsqueda del expediente por fecha
	/*
	 * @RequestMapping(value = "/searchByDate", method = RequestMethod.GET)
	 * 
	 * @ResponseBody public List<Expediente> searchExpedienteByDate(
	 * 
	 * @RequestParam("firstDate") String firstDate,
	 * 
	 * @RequestParam("secondDate") String secondDate) throws ParseException {
	 * 
	 * DateFormat f = new SimpleDateFormat("yyyy-MM-dd"); DateFormat f2 = new
	 * SimpleDateFormat("yyyy-MM-dd");
	 * 
	 * Date d1 = f.parse(firstDate); Date d2 = f2.parse(secondDate);
	 * 
	 * List<Expediente> listExpediente = expedienteRepository
	 * .findByCuandoBetween(d1, d2);
	 * 
	 * return listExpediente;
	 * 
	 * }
	 */

	// realiza la búsqueda del expediente por fecha
	@RequestMapping(value = "/searchByDate", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> searchExpedienteByDate(
			@RequestParam("firstDate") String firstDate,
			@RequestParam("secondDate") String secondDate)
			throws ParseException {

		DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat f2 = new SimpleDateFormat("yyyy-MM-dd");

		Date d1 = f.parse(firstDate);
		Date d2 = f2.parse(secondDate);

		List<Expediente> listExpediente = expedienteRepository
				.findByCuandoBetween(d1, d2);

		HashMap<String, Object> map = new HashMap<String, Object>();

		for (Expediente e : listExpediente) {

			if (e.getEstado().getNombre() != "Prestado") {

				map.put("id", e.getId());
				map.put("nombre", e.getNombre());
				map.put("codigo", e.getCodigo());
				map.put("fechaCreacion", e.getCuando());
			}

		}

		return map;

	}

	// realiza la búsqueda del documento por nombre según el término ingresado
	@RequestMapping(value = "/searchByAsunto", method = RequestMethod.GET)
	@ResponseBody
	public List<Documento> searchDocumentoByAsunto(
			@RequestParam("term") String term) {

		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		StringBuilder sbQuery = new StringBuilder();
		sbQuery.append("SELECT DOC.*, EXP.EXP_ID, EXP.EXP_NOMBRE FROM DOCUMENTO DOC "
				+ "JOIN EXPEDIENTE EXP ON DOC.EXP_ID=EXP.EXP_ID WHERE  ");
		sbQuery.append(FreeTextUtils.buildFreeTextORSQLCondition(
				"DOC.DOC_ASUNTO", term, 1));

		List<Documento> listDocumento = jdbcTemplate.query(sbQuery.toString(),
				new RowMapper<Documento>() {
					@Override
					public Documento mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Documento doc = new Documento();
						doc.setId(rs.getString("DOC_ID"));
						doc.setAsunto(rs.getString("DOC_ASUNTO"));
						Expediente expediente = new Expediente();
						doc.setExpediente(expediente);
						expediente.setId(rs.getInt("EXP_ID"));
						expediente.setNombre(rs.getString("EXP_NOMBRE"));
						return doc;
					}
				});

		return listDocumento;

	}

	// realiza la búsqueda del documento por nombre del expediente según el
	// término ingresado
	@RequestMapping(value = "/searchByExpediente", method = RequestMethod.GET)
	@ResponseBody
	public List<Documento> searchDocumentoByExpediente(
			@RequestParam("term") String term) {

		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		StringBuilder sbQuery = new StringBuilder();
		sbQuery.append("SELECT DOC.*, EXP.EXP_ID, EXP.EXP_NOMBRE FROM DOCUMENTO DOC "
				+ " JOIN EXPEDIENTE EXP ON DOC.EXP_ID=EXP.EXP_ID "
				+ " JOIN ESTADO_EXPEDIENTE EE ON EXP.ESEX_ID=EE.ESEX_ID "
				+ " WHERE EE.ESEX_NOMBRE='Archivado' "
				+ " AND CONTAINS(EXP.EXP_NOMBRE, " + "' ");
		sbQuery.append(term);
		sbQuery.append("%',1)>0 ");

		List<Documento> listDocumento = jdbcTemplate.query(sbQuery.toString(),
				new RowMapper<Documento>() {
					@Override
					public Documento mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Documento doc = new Documento();
						doc.setId(rs.getString("DOC_ID"));
						doc.setAsunto(rs.getString("DOC_ASUNTO"));
						Expediente expediente = new Expediente();
						doc.setExpediente(expediente);
						expediente.setId(rs.getInt("EXP_ID"));
						expediente.setNombre(rs.getString("EXP_NOMBRE"));
						return doc;
					}
				});

		return listDocumento;

	}

	// Muestra el listado de solicitudes de prestamos
	@RequestMapping(value = "/list-solicitud", method = RequestMethod.GET)
	public String list(Model model, Principal principal) {

		List<PrestamoSolicitud> solicitudes = prestamoSolicitudRepository
				.findByActivo(true, new Sort(Direction.ASC, "fechaSolicitud"));

		if (solicitudes.size() > 0)
			model.addAttribute("solicitudes", solicitudes);

		model.addAttribute("activePill", "solicitud");

		return "archivo-prestamo-solicitud-list";

	}

}
