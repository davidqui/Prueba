package com.laamware.ejercito.doc.web.ctrl;

import java.security.Principal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
/*
import org.springframework.scheduling.annotation.Scheduled;
*/
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoPrestamo;
import com.laamware.ejercito.doc.web.entity.EstadoExpediente;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.ExpedienteEstado;
import com.laamware.ejercito.doc.web.entity.ExpedientePrestamo;
import com.laamware.ejercito.doc.web.entity.Prestamo;
import com.laamware.ejercito.doc.web.entity.PrestamoSolicitud;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoPrestamoRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.EstadoExpedienteRepository;
import com.laamware.ejercito.doc.web.repo.ExpedienteEstadoRepository;
import com.laamware.ejercito.doc.web.repo.ExpedientePrestamoRepository;
import com.laamware.ejercito.doc.web.repo.ExpedienteRepository;
import com.laamware.ejercito.doc.web.repo.PrestamoRepository;
import com.laamware.ejercito.doc.web.repo.PrestamoSolicitudRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import com.laamware.ejercito.doc.web.util.FreeTextUtils;

/**
 * @author mcr
 *
 */
@Controller
@RequestMapping(value = PrestamoController.PATH)
public class PrestamoController extends UtilController {

	public static final String PATH = "/prestamo";

	@Autowired
	ExpedienteRepository expedienteRepository;

	@Autowired
	ExpedienteEstadoRepository expedienteEstadoRepository;

	@Autowired
	EstadoExpedienteRepository estadoExpedienteRepository;

	@Autowired
	DependenciaRepository dependenciaRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	PrestamoRepository prestamoRepository;

	@Autowired
	ExpedientePrestamoRepository expedientePrestamoRepository;

	@Autowired
	DocumentoRepository documentoRepository;

	@Autowired
	DocumentoPrestamoRepository documentoPrestamoRepository;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DataSource ds;

	@Autowired
	private PrestamoValidator prestamoValidator;

	@Autowired
	PrestamoSolicitudRepository prestamoSolicitudRepository;

	@Autowired
	UsuarioRepository UsuarioRepository;

	// Muestra el formulario que usa el funcionario para registrar el prestamo
	// de un
	// expediente
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String getSolicitudPrestamoExpediente(@RequestParam("eid") Integer eid, @RequestParam("depid") Integer depid,
			@RequestParam("usuid") Integer usuid, Model model, Principal principal) {

		Expediente expediente = expedienteRepository.findOne(eid);

		Prestamo prestamo = new Prestamo();
		Dependencia dep = dependenciaRepository.findOne(depid);
		Usuario u = usuarioRepository.getOne(usuid);
		if (depid != 0 && usuid != 0) {
			prestamo.setDependencia(dep);
			prestamo.setUsuarioSolicita(u);

		}

		prestamo.setFuncionarioPresta(getUsuario(principal));
		prestamo.setFechaPrestamo(new Date());
		model.addAttribute("activePill", "solicitud");
		model.addAttribute("prestamo", prestamo);
		model.addAttribute("eid", eid);
		model.addAttribute("expediente", expediente);
		model.addAttribute("dependencia", null);
		List<Dependencia> dependencias = dependenciaRepository.findByActivo(true);

		model.addAttribute("dependencias", dependencias);

		return "archivo-prestamo";
	}

	// Guarda el prestamo de un expediente
	@RequestMapping(value = "/save-prestamo", method = RequestMethod.POST)
	public String savePrestamoExpediente(@RequestParam("eid") Integer eid,
			@ModelAttribute("prestamo") Prestamo prestamo, BindingResult result, Model model, Principal principal) {

		model.addAttribute("activePill", "solicitud");
		prestamoValidator.validate(prestamo, result);
		if (result.hasErrors()) {

			model.addAttribute("activePill", "solicitud");
			model.addAttribute("prestamo", prestamo);
			model.addAttribute("eid", eid);
			model.addAttribute("expediente", expedienteRepository.findOne(eid));
			model.addAttribute("dependencia", null);

			List<Dependencia> dependencias = dependenciaRepository.findByActivo(true);
			model.addAttribute("dependencias", dependencias);
			return "archivo-prestamo";

		}
		// Se guarda el prestamo
		prestamoRepository.save(prestamo);
		Expediente e = expedienteRepository.findOne(eid);
		// Se cambia el estado del expediente a "Prestado"
		EstadoExpediente estado = estadoExpedienteRepository.getByNombre("Prestado");
		e.setEstado(estado);
		// Obtenemos el expediente de la tabla de control de los estados
		// del
		// expediente
		ExpedienteEstado ee = expedienteEstadoRepository.findByExpediente(e);
		// Se guarda el record del cambio de estado del expediente en la tabla
		// de control ExpedienteEstado
		ee.setUsuarioTransferencia(prestamo.getFuncionarioPresta());
		ee.setEstado(estado);
		ee.setFechaTransferencia(new Date());
		expedienteEstadoRepository.save(ee);

		// Se guarda el record del expediente prestado en la tabla de
		// control ExpedientePrestamo

		ExpedientePrestamo ep = expedientePrestamoRepository.findByPrestamoId(prestamo.getId());
		try {
			if (ep != null) {
				ep.setPrestamo(prestamo);
				ep.setExpediente(e);
				ep.setActivo(true);
				expedientePrestamoRepository.save(ep);

			} else {
				ExpedientePrestamo newep = new ExpedientePrestamo();
				newep.setPrestamo(prestamo);
				newep.setExpediente(e);
				newep.setActivo(true);
				expedientePrestamoRepository.save(newep);

			}

		} catch (Exception exception) {

		}

		// Se guardan records de los documentos prestados en la tabla de control
		// DocumentoPrestamo
		List<Documento> docs = documentoRepository.findByExpediente(e, new Sort(Direction.ASC, "asunto"));

		for (Documento d : docs) {

			DocumentoPrestamo dp = documentoPrestamoRepository.findByDocumentoId(d.getId());
			try {
				if (dp != null) {

					dp.setPrestamo(prestamo);
					dp.setDocumento(d);
					dp.setActivo(true);
					documentoPrestamoRepository.save(dp);
					d.setPrestado(true);

				} else {
					DocumentoPrestamo newdp = new DocumentoPrestamo();
					newdp.setPrestamo(prestamo);
					newdp.setDocumento(d);
					newdp.setActivo(true);
					documentoPrestamoRepository.save(newdp);
					d.setPrestado(true);

				}
			} catch (Exception exception) {

			}
		}

		// Si el prestamo se realiza directamente por solicitud, se inactiva la
		// solicitud.

		PrestamoSolicitud prestamoSolicitud = prestamoSolicitudRepository.findByExpedienteId(eid);

		try {

			if (prestamoSolicitud != null) {

				prestamoSolicitud.setActivo(false);
			}

		} catch (Exception exce) {

		}

		return String.format("redirect:%s/list-solicitud", PrestamoSolicitudController.PATH);
	}

	// Actualiza el prestamo de un expediente
	@RequestMapping(value = "/update-prestamo", method = RequestMethod.GET)
	public String updatePrestamoExpediente(@RequestParam("eid") Integer eid, Model model, Principal principal) {

		Expediente expediente = expedienteRepository.findOne(eid);

		Prestamo prestamo = prestamoRepository.findByExpediente(expediente);

		model.addAttribute("activePill", "solicitud");
		model.addAttribute("prestamo", prestamo);
		model.addAttribute("eid", eid);
		model.addAttribute("expediente", expediente);
		model.addAttribute("dependencia", prestamo.getDependencia());
		List<Dependencia> dependencias = dependenciaRepository.findByActivo(true);

		model.addAttribute("dependencias", dependencias);

		return "archivo-prestamo";
	}

	// Muestra el formulario que usa el usuario para realizar el prestamo de un
	// documento
	@RequestMapping(value = "/prestamo-doc", method = RequestMethod.GET)
	public String getPrestamoDoc(@RequestParam("docid") String docid, @RequestParam("depid") Integer depid,
			@RequestParam("usuid") Integer usuid, Model model, Principal principal) {

		// Se obtiene el documento
		Documento doc = documentoRepository.findOne(docid);

		Prestamo prestamoDoc = new Prestamo();
		// Se obtiene la dependencia del usuario que solicita el prestamo
		Dependencia dep = dependenciaRepository.findOne(depid);
		Usuario u = usuarioRepository.getOne(usuid);
		// Si el id de dependencia es diferente de cero se determina que es una
		// solicitud de prestamo
		// y asignamos al nuevo prestamo la dependencia y el usuario solicitante
		if (depid != 0 && usuid != 0) {
			prestamoDoc.setDependencia(dep);
			prestamoDoc.setUsuarioSolicita(u);

		}

		prestamoDoc.setFuncionarioPresta(getUsuario(principal));
		prestamoDoc.setFechaPrestamo(new Date());
		model.addAttribute("activePill", "solicitud");
		model.addAttribute("prestamoDoc", prestamoDoc);
		model.addAttribute("docid", docid);
		model.addAttribute("doc", doc);
		// Por qué se pone null en dependencia?
		model.addAttribute("dependencia", null);
		List<Dependencia> dependencias = dependenciaRepository.findByActivo(true);

		model.addAttribute("dependencias", dependencias);

		return "archivo-prestamo-doc";
	}

	// Guarda el prestamo de un documento
	@RequestMapping(value = "/save-prestamo-doc", method = RequestMethod.POST)
	public String savePrestamoDoc(@RequestParam("docid") String docid,
			@ModelAttribute("prestamoDoc") Prestamo prestamoDoc, BindingResult result,
			final RedirectAttributes redirect, Model model, Principal principal) {

		Documento doc = documentoRepository.findOne(docid);

		model.addAttribute("activePill", "solicitud");
		prestamoValidator.validate(prestamoDoc, result);
		if (result.hasErrors()) {

			model.addAttribute("activePill", "solicitud");
			model.addAttribute("prestamoDoc", prestamoDoc);
			model.addAttribute("docid", docid);
			model.addAttribute("doc", documentoRepository.findOne(docid));
			model.addAttribute("dependencia", null);

			List<Dependencia> dependencias = dependenciaRepository.findByActivo(true);
			model.addAttribute("dependencias", dependencias);
			return "archivo-prestamo-doc";

		}
		prestamoRepository.save(prestamoDoc);
		doc.setPrestado(true);
		// Si el prestamo es nuevo, ponemos el documento en "Prestamo"
		try {
			if (prestamoDoc.getId() == null) {

				DocumentoPrestamo dp = new DocumentoPrestamo();
				dp.setPrestamo(prestamoDoc);
				dp.setDocumento(doc);
				documentoPrestamoRepository.save(dp);
				redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Los datos han sido guardados correctamente");

			}

		} catch (Exception e) {

			model.addAttribute("activePill", "solicitud");
			model.addAttribute("prestamoDoc", prestamoDoc);
			model.addAttribute("docid", docid);
			model.addAttribute("doc", documentoRepository.findOne(docid));
			model.addAttribute("dependencia", null);

			List<Dependencia> dependencias = dependenciaRepository.findByActivo(true);
			model.addAttribute("dependencias", dependencias);
			model.addAttribute(AppConstants.FLASH_ERROR, "Ocurrió un error inesperado: " + e.getMessage());
			return "archivo-prestamo-doc";

		}
		// Si el prestamo se realiza directamente por solicitud, se inactiva la
		// solicitud.
		/*
		 * Implementar para las solicitudes de prestamo de documentos
		 * 
		 * PrestamoSolicitud prestamoSolicitud = prestamoSolicitudRepository
		 * .findByDocumentoId(eid);
		 */

		return String.format("redirect:%s/list-solicitud", PrestamoSolicitudController.PATH);
	}

	// Metodo que soporta el autocomplete del formulario
	// "archivo-prestamo y archivo-prestamo-doc"
	@RequestMapping(value = "/usuarios", method = RequestMethod.GET)
	@ResponseBody
	public List<Usuario> getusuariosList(@RequestParam("depId") Integer depId, @RequestParam("term") String term) {

		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		StringBuilder sbQuery = new StringBuilder();
		sbQuery.append("SELECT USU.* FROM USUARIO USU WHERE USU.DEP_ID=");
		sbQuery.append(depId + "AND ");
		sbQuery.append(FreeTextUtils.buildFreeTextORSQLCondition("USU.USU_NOMBRE", term, 1));

		List<Usuario> listUsuarios = jdbcTemplate.query(sbQuery.toString(), new RowMapper<Usuario>() {
			@Override
			public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
				Usuario usu = new Usuario();
				usu.setId(rs.getInt("USU_ID"));
				usu.setNombre(rs.getString("USU_NOMBRE"));
				usu.setDocumento(rs.getString("USU_DOCUMENTO"));
				return usu;
			}
		});

		return listUsuarios;

	}

	// Muestra el contendido del expediente, en una vita para el Archivo Central
	@RequestMapping(value = "/contenido", method = RequestMethod.GET)
	@Transactional
	public String contenido(@RequestParam("eid") Integer eid, Model model, Principal principal) {

		Expediente expediente = expedienteRepository.findOne(eid);
		model.addAttribute("eid", eid);
		model.addAttribute("expediente", expediente);
		List<Documento> documentos = documentoRepository.findByExpediente(expediente,
				new Sort(Direction.ASC, "cuando"));
		for (Documento documento : documentos) {
			documento.getAdjuntos().size();
		}
		model.addAttribute("documentos", documentos);
		return "archivo-expediente-contenido";
	}

	// Muestra el listado de prestamo de expedientes en el módulo "Archivo"
	@RequestMapping(value = "/list-prestamos-exp", method = RequestMethod.GET)
	public String listPrestamosExp(Model model, Principal principal) {

		List<ExpedientePrestamo> exPrestados = expedientePrestamoRepository.findByActivo(true);
		model.addAttribute("activeNav", "prestados");
		if (exPrestados.size() > 0) {
			model.addAttribute("exPrestados", exPrestados);
		}

		return "archivo-prestamo-exp-list";

	}

	// Muestra el listado de prestamo de documentos en el módulo "Archivo"
	@RequestMapping(value = "/list-prestamos-doc", method = RequestMethod.GET)
	public String listPrestamosDoc(Model model, Principal principal) {

		List<DocumentoPrestamo> docPrestados = documentoPrestamoRepository.findByActivo(true);
		model.addAttribute("activeNav", "prestados");
		if (docPrestados.size() > 0) {
			model.addAttribute("docPrestados", docPrestados);
		}

		return "archivo-prestamo-doc-list";

	}

	// Muestra el listado de expedientes en calidad de prestamo en el módulo de
	// expedientes
	@RequestMapping(value = "/list-exp-prestados", method = RequestMethod.GET)
	public String listPrestamosExpByUser(Model model, Principal principal) {

		Usuario u = getUsuario(principal);
		List<ExpedientePrestamo> exPrestados = expedientePrestamoRepository.findByUsuarioIdAndActivo(u.getId(), true);
		model.addAttribute("activeNav", "expedientes");

		model.addAttribute("uid", u.getId());
		if (exPrestados.size() > 0) {
			model.addAttribute("exPrestados", exPrestados);
		}

		return "expediente-prestados-list";

	}

	// Muestra el listado de prestamo de documentos en el módulo "Archivo"
	@RequestMapping(value = "/list-doc-prestados", method = RequestMethod.GET)
	public String listPrestamosDocByUser(Model model, Principal principal) {

		Usuario u = getUsuario(principal);
		List<DocumentoPrestamo> docPrestados = documentoPrestamoRepository.findByUsuarioIdAndActivo(u.getId(), true);
		model.addAttribute("activeNav", "documentos");
		
		model.addAttribute("uid", u.getId());
		if (docPrestados.size() > 0) {
			model.addAttribute("docPrestados", docPrestados);
		}

		return "documento-prestados-list";

	}

	/*
	 * @Value("${docweb.expediente.serie}")
	 * 
	 * //Realiza la liberación de prestamos cuando el plazo se ha vencido.
	 * 
	 * @Scheduled(cron = "0 0 9-10 * * MON-FRI") public void liberarPrestamos()
	 * {
	 * 
	 * List<Prestamo> prestamosVencidos = prestamoRepository
	 * .findByFechaDevolucionBefore(new Date());
	 * 
	 * try { if (prestamosVencidos.size() > 0) {
	 * 
	 * DocumentoPrestamo dp = new DocumentoPrestamo(); ExpedientePrestamo ep =
	 * new ExpedientePrestamo();
	 * 
	 * for (Prestamo p : prestamosVencidos) {
	 * 
	 * p.setActivo(false); dp = documentoPrestamoRepository
	 * .findByPrestamoId(p.getId()); ep =
	 * expedientePrestamoRepository.findByPrestamoId(p .getId());
	 * dp.setActivo(false); ep.setActivo(false); prestamoRepository.save(p);
	 * documentoPrestamoRepository.save(dp); } }
	 * 
	 * } catch (Exception ex) {
	 * 
	 * }
	 * 
	 * }
	 */
}
