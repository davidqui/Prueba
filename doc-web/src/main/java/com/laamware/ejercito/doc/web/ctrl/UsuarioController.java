package com.laamware.ejercito.doc.web.ctrl;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.laamware.ejercito.doc.web.dto.UsuarioHistorialFirmaDTO;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Clasificacion;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Grados;
import com.laamware.ejercito.doc.web.entity.Perfil;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.ClasificacionRepository;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.repo.GradosRepository;
import com.laamware.ejercito.doc.web.repo.PerfilRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import com.laamware.ejercito.doc.web.serv.DependenciaService;
import com.laamware.ejercito.doc.web.serv.LdapService;
import com.laamware.ejercito.doc.web.serv.OFS;

@Controller
@PreAuthorize("hasRole('ADMIN_USUARIOS')")
@RequestMapping(UsuarioController.PATH)
public class UsuarioController extends UtilController {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	ClasificacionRepository clasificacionRepository;

	@Autowired
	PerfilRepository perfilRepository;

	@Autowired
	DependenciaRepository dependenciaRepository;

	@Autowired
	GradosRepository gradosRepository;

	@Autowired
	OFS ofs;

	@Autowired
	LdapService ldapService;

	/**
	 * Servicio de dependencias.
	 */
	// 2017-06-01 jgarcia@controltechcg.com Issue #99 (SICDI-Controltech)
	// hotfix-99
	@Autowired
	private DependenciaService dependenciaService;

	@Value("${docweb.authMode}")
	String authMode;

	static final String PATH = "/usuarios";

	@RequestMapping(value = { "" }, method = RequestMethod.GET)
	public String listarUsuarios(@RequestParam(value = "all", required = false, defaultValue = "false") Boolean all,
			Model model) {
		List<Usuario> list = findAll(all);
		model.addAttribute("list", list);
		model.addAttribute("all", all);
		return "usuario-list";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String ver(Model model, Principal principal) {
		Usuario usuario = new Usuario();
		usuario.setMode(UsuarioMode.getByName(UsuarioMode.REGISTRO_NAME));
		model.addAttribute("usuario", usuario);
		return "usuario";
	}

	@RequestMapping(value = "/nuevo", method = RequestMethod.GET)
	public String nuevo(Model model, Principal principal) {
		Usuario usuario = new Usuario();
		usuario.setMode(UsuarioMode.getByName(UsuarioMode.REGISTRO_NAME));
		model.addAttribute("usuario", usuario);
		return "usuario";
	}

	@RequestMapping(value = "/editar", method = RequestMethod.GET)
	public String editarUsuario(Model model, Principal principal, HttpServletRequest req) {

		Integer idUsuario = Integer.valueOf(req.getParameter("id").replaceAll("\\.", ""));
		Usuario usuario = usuarioRepository.findOne(idUsuario);
		usuario.setMode(UsuarioMode.getByName(UsuarioMode.EDICION_NAME));
		model.addAttribute("usuario", usuario);

		cargarDatosHistorialFirmasCargadas(usuario);

		return "usuario";
	}

	@RequestMapping(value = "/guardar", method = RequestMethod.POST)
	public String guardarUsuario(@RequestParam("file") MultipartFile file, Usuario usuario, BindingResult docBind,
			Model model, HttpServletRequest req, Principal principal, RedirectAttributes redirect) {
		try {

			String idS = req.getParameter("id");
			if (idS != null && idS.trim().length() > 0) {
				usuario.setId(Integer.parseInt(idS.trim()));
			}
			// Validar que no exista el usuario registrado si es nuevo
			if (docBind.hasErrors()) {
				System.out.println(docBind);
				usuario.setMode(UsuarioMode.getByName(UsuarioMode.REGISTRO_NAME));
				model.addAttribute("usuario", usuario);
				return "usuario";
			}

			/*
			 * if (usuario.getDocumento() == null ||
			 * usuario.getDocumento().trim().length() == 0) {
			 * model.addAttribute(AppConstants.FLASH_ERROR,
			 * "El documento del usuario es requerido" );
			 * usuario.setMode(UsuarioMode.getByName(UsuarioMode.REGISTRO_NAME))
			 * ; model.addAttribute("usuario", usuario); return "usuario"; }
			 */

			if (usuario.getPerfil() == null || usuario.getPerfil().getId() == null) {
				model.addAttribute(AppConstants.FLASH_ERROR, "El perfil del usuario es requerido");
				usuario.setMode(UsuarioMode.getByName(UsuarioMode.REGISTRO_NAME));
				model.addAttribute("usuario", usuario);
				return "usuario";
			}

			if (usuario.getClasificacion() == null || usuario.getClasificacion().getId() == null) {
				model.addAttribute(AppConstants.FLASH_ERROR, "El nivel de ácceso del usuario es requerido");
				usuario.setMode(UsuarioMode.getByName(UsuarioMode.REGISTRO_NAME));
				model.addAttribute("usuario", usuario);
				return "usuario";
			}

			String fileId = null;
			if ("ad".equals(authMode)) {
				Usuario usuarioLdap = ldapService.getUsuarioFromLdapByAccountName(usuario.getLogin());
				if (usuarioLdap == null) {
					model.addAttribute(AppConstants.FLASH_ERROR,
							String.format(
									"El usuario con el login %s no se encuentra en el Directorio Activo y no puede ser registrado",
									usuario.getLogin()));
					usuario.setMode(UsuarioMode.getByName(UsuarioMode.REGISTRO_NAME));
					model.addAttribute("usuario", usuario);
					return "usuario";
				} else if (usuarioLdap.getDependencia() == null) {
					model.addAttribute(AppConstants.FLASH_ERROR,
							String.format(
									"No se encontró dependencia en el Directorio Activo para el usuario con el login %s, o la dependencia asociada en el Directorio Activo no se encuentra configurada en el sistema y por lo tanto el usuario no puede ser registrado",
									usuario.getLogin()));
					usuario.setMode(UsuarioMode.getByName(UsuarioMode.REGISTRO_NAME));
					model.addAttribute("usuario", usuario);
					return "usuario";
				}
			}

			if (usuario.getId() == null) {
				List<Usuario> usuarioExiste = usuarioRepository.findByLogin(usuario.getLogin());
				if (usuarioExiste != null && !usuarioExiste.isEmpty() && usuarioExiste.get(0) != null
						&& usuarioExiste.get(0).getActivo()) {
					model.addAttribute(AppConstants.FLASH_ERROR,
							String.format("El usuario con el login %s ya se encuentra registrado", usuario.getLogin()));
					usuario.setMode(UsuarioMode.getByName(UsuarioMode.REGISTRO_NAME));
					model.addAttribute("usuario", usuario);
					return "usuario";
				} else if (usuarioExiste != null && !usuarioExiste.isEmpty() && usuarioExiste.get(0) != null
						&& !usuarioExiste.get(0).getActivo()) {
					Usuario usu = usuarioExiste.get(0);
					usu.setNombre(usuario.getNombre());
					usu.setTelefono(usuario.getTelefono());
					usu.setEmail(usuario.getEmail());
					usu.setGrado(usuario.getGrado());
					usu.setClasificacion(usuario.getClasificacion());
					usu.setDependencia(usuario.getDependencia());
					usu.setPerfil(usuario.getPerfil());
					usu.setDocumento(usuario.getDocumento());
					usu.setActivo(Boolean.TRUE);
					usu.setQuienMod(getUsuario(principal).getId());
					usu.setCuandoMod(new Date());
					usuarioRepository.save(usu);
					redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
							"La información del usuario fue almacenada correctamente.");
					model.addAttribute(AppConstants.FLASH_INFO,
							"La información del usuario fue almacenada correctamente.");
					// return "redirect:" + PATH;
					return "usuario";
				}
			}
			try {

				if (file != null) {
					fileId = ofs.saveAsIs(file.getBytes(), file.getContentType());

					if (file.getContentType() != null && file.getContentType().toLowerCase().startsWith("image/")) {
						String extesion = file.getContentType().split("/")[1];
						usuario.setImagenFirmaExtension(fileId + "." + extesion);
						usuario.setImagenFirma(fileId);
					} else {
						// dejamos la misma imagen
						if (usuario.getId() != null) {
							Usuario usuarioImagen = usuarioRepository.findOne(usuario.getId());
							if (usuarioImagen != null) {
								usuario.setImagenFirmaExtension(usuarioImagen.getImagenFirmaExtension());
								usuario.setImagenFirma(usuarioImagen.getImagenFirma());
							}
						}

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				redirect.addFlashAttribute(AppConstants.FLASH_ERROR, "Ocurrió un error inesperado: " + e.getMessage());
				model.addAttribute(AppConstants.FLASH_ERROR, "Ocurrió un error inesperado: " + e.getMessage());
				usuario.setMode(UsuarioMode.getByName(UsuarioMode.REGISTRO_NAME));
				model.addAttribute("usuario", usuario);
				return "usuario";
			}

			usuario.setQuienMod(getUsuario(principal).getId());
			usuario.setCuandoMod(new Date());
			usuario.setMode(UsuarioMode.getByName(UsuarioMode.REGISTRO_NAME));
			usuarioRepository.save(usuario);

			cargarDatosHistorialFirmasCargadas(usuario);

			redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS,
					"La información del usuario fue almacenada correctamente.");
			model.addAttribute(AppConstants.FLASH_INFO, "La información del usuario fue almacenada correctamente.");
			return "redirect:" + PATH;
			// return "usuario";
			// return "redirect:" + PATH;
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute(AppConstants.FLASH_ERROR, "Ocurrió un error inesperado: " + e.getMessage());
			usuario.setMode(UsuarioMode.getByName(UsuarioMode.REGISTRO_NAME));
			model.addAttribute("usuario", usuario);
			return "usuario";
		}
	}

	private void cargarDatosHistorialFirmasCargadas(Usuario usuario) {

		List<Object[]> historialusuario = usuarioRepository.findHistorialFirmaUsuario(usuario.getId());
		for (Object[] historial : historialusuario) {
			usuario.getHistorialUsuarios().add(new UsuarioHistorialFirmaDTO((Date) historial[1], (String) historial[0],
					(String) historial[3], (String) historial[2]));
		}

	}

	/**
	 * Consulta los datos del usuario en el DA a partir del login ingresado
	 * 
	 * @param model
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "/consultar-ldap", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> usuarioConsultar(@RequestParam("login") String login, Model model, Principal principal,
			HttpServletRequest req) {

		if ("ad".equals(authMode)) {
			try {
				Usuario usuarioLdap = ldapService.getUsuarioFromLdapByAccountName(login.trim().toLowerCase());
				if (usuarioLdap != null) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("login", usuarioLdap.getLogin());
					map.put("documento", usuarioLdap.getDocumento());
					map.put("nombre", usuarioLdap.getNombre());
					map.put("telefono", usuarioLdap.getTelefono());
					map.put("grado", usuarioLdap.getGrado());
					map.put("dependencia", usuarioLdap.getDependencia() != null
							? usuarioLdap.getDependencia().getId().toString() : null);
					map.put("email", usuarioLdap.getEmail());
					map.put("cargo", usuarioLdap.getCargo());
					return map;
				} else {
					return null;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	@RequestMapping(value = { "/eliminar" }, method = RequestMethod.GET)
	public String eliminar(Model model, HttpServletRequest req, RedirectAttributes redirect) {
		Integer idUsuario = Integer.valueOf(req.getParameter("id"));
		try {
			Usuario usuario = usuarioRepository.findOne(idUsuario);
			// usuario.setActivo(false);
			usuarioRepository.save(usuario);

			/*
			 * 2017-06-01 jgarcia@controltechcg.com Issue #99
			 * (SICDI-Controltech) hotfix-99: Corrección en el proceso de
			 * eliminar usuario, para que se realice la búsqueda de las
			 * dependencias en las cuales se encuentra como jefe asignado (jefe
			 * principal o jefe encargado) y el consecuente retiro de esta
			 * asignación.
			 */
			List<Dependencia> dependencias = dependenciaService.retirarUsuarioComoJefeAsignado(usuario);

			/*
			 * 2017-06-01 jgarcia@controltechcg.com Issue #99
			 * (SICDI-Controltech) hotfix-99: Creación de mensaje de eliminación
			 * de usuario, según lista de dependencias sobre las cuales fue
			 * retirado como jefe asignado.
			 */
			String mensaje = buildMensajeUsuarioEliminado("Usuario eliminado con éxito.", dependencias);

			redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, mensaje);
		} catch (Exception ex) {
			ex.printStackTrace();
			redirect.addFlashAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
		}
		return "redirect:" + PATH;
	}

	/**
	 * Construye el mensaje de éxito correspondiente a la operación de eliminar
	 * un usuario. En caso que el usuario haya sido retirado de dependencias
	 * como jefe asignado, se presentará la información de dichas dependencias.
	 * 
	 * @param mensajeInicial
	 *            Mensaje inicial a presentar.
	 * @param dependencias
	 *            Lista de dependencias activas sobre las cuales el usuario fue
	 *            retirado como jefe asignado.
	 * @return Mensaje de éxito.
	 */
	// 2017-06-01 jgarcia@controltechcg.com Issue #99 (SICDI-Controltech)
	// hotfix-99
	private String buildMensajeUsuarioEliminado(String mensajeInicial, List<Dependencia> dependencias) {
		if (dependencias == null || dependencias.isEmpty()) {
			return mensajeInicial;
		}

		StringBuilder builder = new StringBuilder(mensajeInicial);
		builder.append(
				" El usuario ha sido retirado como Jefe Asignado (Principal o Encargado) de las siguientes dependencias: ");

		for (int i = 0; i < dependencias.size(); i++) {
			Dependencia dependencia = dependencias.get(i);
			builder.append(dependencia.getNombre());

			final String sigla = dependencia.getSigla();
			if (sigla != null && !dependencia.getSigla().trim().isEmpty()) {
				builder.append(" (").append(dependencia.getSigla()).append(")");
			}

			if (i < (dependencias.size() - 1)) {
				builder.append(", ");
			}
		}

		return builder.toString();
	}

	@ModelAttribute("activePill")
	public String getActivePill() {
		return "users";
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

	/**
	 * Carga el listado de perfiles al modelo
	 * 
	 * @return
	 */
	@ModelAttribute("perfiles")
	public List<Perfil> perfiles() {
		return perfilRepository.findByActivo(true);
	}

	/**
	 * Carga el listado de dependencias al modelo
	 * 
	 * @return
	 */
	@ModelAttribute("dependencias")
	public List<Dependencia> dependencias(Model model) {
		List<Dependencia> list = dependenciaRepository.findByActivo(true, new Sort(Direction.ASC, "nombre"));
		model.addAttribute("dependencias", list);
		return list;
	}

	/**
	 * Carga el listado de Grados al modelo
	 * 
	 * @return
	 */
	@ModelAttribute("grados")
	public List<Grados> grados(Model model) {
		List<Grados> list = gradosRepository.findByActivo(true, new Sort(Direction.ASC, "id"));
		model.addAttribute("grados", list);
		return list;
	}

	@ModelAttribute("descriptor")
	GenDescriptor getDescriptor() {
		GenDescriptor d = GenDescriptor.find(Usuario.class);
		return d;
	}

	protected List<Usuario> findAll(boolean all) {
		if (!all) {
			return usuarioRepository.findByActivo(true);
		} else {
			return usuarioRepository.findAll();
		}
	}
}
