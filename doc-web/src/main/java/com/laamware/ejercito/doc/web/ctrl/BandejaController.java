package com.laamware.ejercito.doc.web.ctrl;

import java.security.Principal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.InstanciaBandeja;
import com.laamware.ejercito.doc.web.repo.DocumentoDependenciaAdicionalRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.InstanciaBandejaRepository;
import com.laamware.ejercito.doc.web.serv.ProcesoService;
import com.laamware.ejercito.doc.web.serv.UsuarioService;

@Controller
@RequestMapping(value = BandejaController.PATH)
public class BandejaController extends UtilController {

	public static final String PATH = "/bandeja";

	@Autowired
	ProcesoService procesoService;

	@Autowired
	DocumentoRepository docR;

	@Autowired
	InstanciaBandejaRepository instanciaBandejaR;

	// 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
	// feature-78
	@Autowired
	UsuarioService usuarioService;

	// 2017-02-06 jgarcia@controltechcg.com Issue #118 Presentación de jefes de
	// dependencias adicionales a un documento.
	@Autowired
	DocumentoDependenciaAdicionalRepository documentoDependenciaAdicionalRepository;

	@PreAuthorize("hasRole('BANDEJAS')")
	@RequestMapping(value = "/entrada", method = RequestMethod.GET)
	public String entrada(Model model, Principal principal,
			@RequestParam(required = false, value = "action") String action,
			@RequestParam(required = false, value = "pin") String pin) {

		if (StringUtils.isNotBlank(action)) {
			if ("quitar".equals(action)) {
				if (StringUtils.isNotBlank(pin)) {
					InstanciaBandeja ib = instanciaBandejaR.findByInstanciaIdAndUsuarioLoginAndBandejaAndActivo(pin,
							principal.getName(), "ENTRADA", true);
					ib.setActivo(false);
					instanciaBandejaR.save(ib);
				}
			}
		}

		List<Documento> docs = docR.findBandejaEntrada(principal.getName());
		for (Documento d : docs) {
			d.getInstancia().getCuando();
			d.getInstancia().setService(procesoService);
		}
		model.addAttribute("documentos", docs);

		/*
		 * 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
		 * feature-78: Presentar información básica de los usuarios asignadores
		 * y asignados en las bandejas del sistema.
		 */
		model.addAttribute("usuarioService", usuarioService);

		return "bandeja-entrada";
	}

	@PreAuthorize("hasRole('BANDEJAS')")
	@RequestMapping(value = "/enviados", method = RequestMethod.GET)
	public String enviados(Model model, Principal principal) {

		List<Documento> docs = docR.findBandejaEnviados(principal.getName());
		for (Documento d : docs) {
			d.getInstancia().getCuando();
			d.getInstancia().setService(procesoService);

			/*
			 * 2017-02-06 jgarcia@controltechcg.com Issue #118 Presentación de
			 * jefes de dependencias adicionales a un documento.
			 * 
			 * 2017-05-15 jgarcia@controltechcg.com Issue #78
			 * (SICDI-Controltech) feature-78: Presentar información básica de
			 * los usuarios asignadores y asignados en las bandejas del sistema.
			 */
			String asignadosText = DocumentoController.buildAsignadosText(documentoDependenciaAdicionalRepository,
					usuarioService, d.getInstancia(), null);
			d.setTextoAsignado(asignadosText);

		}
		model.addAttribute("documentos", docs);

		/*
		 * 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
		 * feature-78: Presentar información básica de los usuarios asignadores
		 * y asignados en las bandejas del sistema.
		 */
		model.addAttribute("usuarioService", usuarioService);

		return "bandeja-enviados";
	}

	@PreAuthorize("hasRole('BANDEJAS')")
	@RequestMapping(value = "/entramite", method = RequestMethod.GET)
	public String entramite(Model model, Principal principal) {

		List<Documento> docs = docR.findBandejaTramite(principal.getName());
		for (Documento d : docs) {
			d.getInstancia().getCuando();
			d.getInstancia().setService(procesoService);
		}
		model.addAttribute("documentos", docs);

		/*
		 * 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
		 * feature-78: Presentar información básica de los usuarios asignadores
		 * y asignados en las bandejas del sistema.
		 */
		model.addAttribute("usuarioService", usuarioService);

		return "bandeja-tramite";
	}

	@PreAuthorize("hasRole('BANDEJAS')")
	@RequestMapping(value = "/consulta", method = RequestMethod.GET)
	public String consulta(Model model) {

		return "bandeja-consulta";

	}

	/**
	 * Obtiene la información a presentar para la bandeja de apoyo y consulta
	 * del usuario, y carga la pantalla correspondiente.
	 * 
	 * @param model
	 *            Modelo.
	 * @param principal
	 *            Principal.
	 * @return Identificador del template de la bandeja.
	 */
	@PreAuthorize("hasRole('BANDEJAS')")
	@RequestMapping(value = "/apoyo-consulta", method = RequestMethod.GET)
	// 2017-04-18 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech)
	public String apoyoConsulta(Model model, Principal principal) {
		final String login = principal.getName();
		final List<Documento> documentos = docR.findBandejaConsulta(login);

		for (Documento documento : documentos) {
			documento.getInstancia().getCuando();
			documento.getInstancia().setService(procesoService);
		}

		model.addAttribute("documentos", documentos);

		/*
		 * 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
		 * feature-78: Presentar información básica de los usuarios asignadores
		 * y asignados en las bandejas del sistema.
		 */
		model.addAttribute("usuarioService", usuarioService);

		return "bandeja-apoyo-consulta";

	}

}
