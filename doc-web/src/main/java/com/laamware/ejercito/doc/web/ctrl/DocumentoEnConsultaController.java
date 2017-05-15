package com.laamware.ejercito.doc.web.ctrl;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import com.laamware.ejercito.doc.web.serv.DocumentoEnConsultaService;
import com.laamware.ejercito.doc.web.serv.ProcesoService;

/**
 * Controlador para las operaciones de bandeja de apoyo y consulta.
 * 
 * @author jgarcia@controltechcg.com
 * @since Abril 20, 2017
 *
 */
// 2017-04-20 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech)
@Controller()
@RequestMapping(DocumentoEnConsultaController.PATH)
public class DocumentoEnConsultaController {

	/** Ruta raíz del controlador. */
	public static final String PATH = "/documento-consulta";

	/** Servicio de documentos en consulta. */
	@Autowired
	private DocumentoEnConsultaService documentoEnConsultaService;

	/** Servicio de instancias del proceso. */
	@Autowired
	private ProcesoService procesoService;

	/** Repositorio de documentos. */
	@Autowired
	private DocumentoRepository documentoRepository;

	/** Repositorio de usuarios. */
	@Autowired
	private UsuarioRepository usuarioRepository;

	/**
	 * Procesa la solicitud de envío del documento a la bandeja de apoyo y
	 * consulta.
	 * 
	 * @param pin
	 *            ID de la instancia del proceso del documento.
	 * @param principal
	 *            Información de usuario en sesión.
	 * @param redirect
	 *            Atributos de redirección.
	 * @return URL de la redirección resultado del procesamiento.
	 */
	@RequestMapping(value = "/enviar-apoyo-consulta", method = RequestMethod.GET)
	public String enviarApoyoConsulta(@RequestParam(value = "pin") String pin, Principal principal,
			RedirectAttributes redirect) {
		final Instancia instancia = procesoService.instancia(pin);
		final Documento documento = documentoRepository.findOneByInstanciaId(instancia.getId());

		final Usuario usuarioSesion = getUsuarioEnSesion(principal);

		final boolean puedeEnviarABandejaConsulta = documentoEnConsultaService
				.validarEnviarABandejaConsulta(usuarioSesion, documento);
		if (!puedeEnviarABandejaConsulta) {
			redirect.addFlashAttribute(AppConstants.FLASH_ERROR,
					"Usuario en sesión no está autorizado para enviar el documento a Bandeja de Consulta.");
			return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
		}

		documentoEnConsultaService.enviarABandejaConsulta(usuarioSesion, documento);

		redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Documento enviado a Bandeja de Consulta.");

		return "redirect:/";
	}

	/**
	 * Proceso la solicitud de extracción del documento de la bandeja de apoyo y
	 * consulta.
	 * 
	 * @param pin
	 *            ID de la instancia del proceso del documento.
	 * @param principal
	 *            Información de usuario en sesión.
	 * @param redirect
	 *            Atributos de redirección.
	 * @return URL de la redirección resultado del procesamiento.
	 */
	@RequestMapping(value = "/extraer-apoyo-consulta", method = RequestMethod.GET)
	public String extraerApoyoConsulta(@RequestParam(value = "pin") String pin, Principal principal,
			RedirectAttributes redirect) {
		final Instancia instancia = procesoService.instancia(pin);
		final Documento documento = documentoRepository.findOneByInstanciaId(instancia.getId());

		final Usuario usuarioSesion = getUsuarioEnSesion(principal);

		final boolean puedeExtraerDeBandejaConsulta = documentoEnConsultaService
				.validarExtraerDeBandejaConsulta(usuarioSesion, documento);
		if (!puedeExtraerDeBandejaConsulta) {
			redirect.addFlashAttribute(AppConstants.FLASH_ERROR,
					"Usuario en sesión no está autorizado para enviar el documento a Bandeja de Entrada.");
			return String.format("redirect:%s/instancia?pin=%s", ProcesoController.PATH, pin);
		}

		documentoEnConsultaService.extraerDeBandejaConsulta(usuarioSesion, documento);

		redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Documento enviado a Bandeja de Entrada.");

		return "redirect:/bandeja/apoyo-consulta";
	}

	/**
	 * Obtiene el usuario en sesión.
	 * 
	 * @param principal
	 *            Información de usuario en sesión.
	 * @return Usuario en sesión.
	 */
	private Usuario getUsuarioEnSesion(final Principal principal) {
		final String login = principal.getName();
		return usuarioRepository.getByLogin(login);
	}
}
