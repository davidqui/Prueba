package com.laamware.ejercito.doc.web.ctrl;

import java.security.Principal;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.ExpedienteEstado;
import com.laamware.ejercito.doc.web.repo.ArchivoRepository;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoPrestamoRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.EstadoExpedienteRepository;
import com.laamware.ejercito.doc.web.repo.ExpedienteEstadoRepository;
import com.laamware.ejercito.doc.web.repo.ExpedientePrestamoRepository;
import com.laamware.ejercito.doc.web.repo.ExpedienteRepository;
import com.laamware.ejercito.doc.web.repo.PrestamoRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;

@Controller
@RequestMapping(value = ArchivoController.PATH)
public class ArchivoController extends UtilController {

	public static final String PATH = "/archivo";

	@Autowired
	ArchivoRepository archivoRepository;

	@Autowired
	PrestamoRepository prestamoRepository;

	@Autowired
	ExpedientePrestamoRepository expedientePrestamoRepository;

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
	DocumentoRepository documentoRepository;

	@Autowired
	DocumentoPrestamoRepository documentoPrestamoRepository;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DataSource ds;

	@PreAuthorize("hasRole('ARCHIVO_CENTRAL')")
	@RequestMapping(value = "/menu", method = RequestMethod.GET)
	public String contenido(Model model, Principal principal) {

		return "redirect:/admin/edificio";
	}

	// Muestra el listado de expedientes archivados
	@PreAuthorize("hasRole('ARCHIVO_CENTRAL')")
	@RequestMapping(value = "/list-archivados-exp", method = RequestMethod.GET)
	public String listExpedientes(Model model, Principal principal) {

		model.addAttribute("activeNav", "archivados");

		Dependencia dependencia = getUsuario(principal).getDependencia();
		try {

			if (dependencia != null) {

				List<ExpedienteEstado> expArchivados = expedienteEstadoRepository
						.findByActivoAndEstado(true, "Archivado");
				if (expArchivados.size() > 0)
					model.addAttribute("expArchivados", expArchivados);

				return "archivo-expediente-list";

			}

		} catch (Exception e) {

		}
		return "archivo-expediente-list";

	}

	// Muestra el listado de documentos archivados
	@RequestMapping(value = "/list-archivados-doc", method = RequestMethod.GET)
	public String listPrestamosDoc(Model model, Principal principal) {

		model.addAttribute("activeNav", "archivados");

		List<Documento> docArchivados = documentoRepository
				.findByEstadoExpediente("Archivado");

		if (docArchivados.size() > 0) {
			model.addAttribute("docArchivados", docArchivados);
		}

		return "archivo-docs-list";

	}

	@ModelAttribute("activeNav")
	public String getActivePill() {
		return "no-active-pill";
	}

}
