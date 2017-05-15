package com.laamware.ejercito.doc.web.ctrl;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.laamware.ejercito.doc.web.entity.Juego;
import com.laamware.ejercito.doc.web.entity.JuegoAyudaSegundaUsuario;
import com.laamware.ejercito.doc.web.entity.JuegoAyudaUsuario;
import com.laamware.ejercito.doc.web.entity.JuegoNivel;
import com.laamware.ejercito.doc.web.entity.JuegoNivelUsuario;
import com.laamware.ejercito.doc.web.entity.JuegoPregunta;
import com.laamware.ejercito.doc.web.entity.JuegoPreguntaTemp;
import com.laamware.ejercito.doc.web.entity.JuegoRespuesta;
import com.laamware.ejercito.doc.web.entity.JuegoRespuestaUsuario;
import com.laamware.ejercito.doc.web.entity.JuegoRespuestaUsuarioHistorial;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.JuegoAyudaSegundaUsuarioRepository;
import com.laamware.ejercito.doc.web.repo.JuegoAyudaUsuarioRepository;
import com.laamware.ejercito.doc.web.repo.JuegoNivelRepository;
import com.laamware.ejercito.doc.web.repo.JuegoNivelUsuarioRepository;
import com.laamware.ejercito.doc.web.repo.JuegoPreguntaRepository;
import com.laamware.ejercito.doc.web.repo.JuegoPreguntaTempRepo;
import com.laamware.ejercito.doc.web.repo.JuegoRepository;
import com.laamware.ejercito.doc.web.repo.JuegoRespuestaRepository;
import com.laamware.ejercito.doc.web.repo.JuegoRespuestaUsuarioHistorialRepository;
import com.laamware.ejercito.doc.web.repo.JuegoRespuestaUsuarioRepository;

@Controller
@RequestMapping(value = JuegoController.PATH)
public class JuegoController extends UtilController {

	static final String PATH = "/capacitacion-juego";

	@Autowired
	JuegoRepository juegoRepository;

	@Autowired
	JuegoNivelRepository juegoNivelRepository;

	@Autowired
	JuegoNivelUsuarioRepository juegoNivelUsuarioRepository;

	@Autowired
	JuegoPreguntaRepository juegoPreguntaRepository;

	@Autowired
	JuegoPreguntaTempRepo juegoPreguntaTempRepo;

	@Autowired
	JuegoRespuestaRepository juegoRespuestaRepository;

	@Autowired
	JuegoRespuestaUsuarioRepository juegoRespuestaUsuarioRepository;
	@Autowired
	JuegoRespuestaUsuarioHistorialRepository juegoRespuestaUsuarioHistorialRepository;
	@Autowired
	JuegoAyudaUsuarioRepository juegoAyudaUsuarioRepository;

	@Autowired
	JuegoAyudaSegundaUsuarioRepository juegoAyudaSegundaUsuarioRepository;

	@Autowired
	SessionFactory sessionFactory;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				new SimpleDateFormat("yyyy-MM-dd"), true, 10));
	}

	@ModelAttribute("activePill")
	public String getActivePill() {
		return "none";
	}

	public List<Juego> capacitacionOpciones() {

		List<Juego> juegos = juegoRepository.findByActivo(true);

		return juegos;
	}

	/**
	 * Muestra la pantalla de inicio del juego
	 * 
	 * @param model
	 * @param principal
	 * @return
	 */

	@RequestMapping(value = "/intro", method = RequestMethod.GET)
	public String juegoIntroView(Model model, Principal principal) {

		Usuario usuario = getUsuario(principal);

		model.addAttribute("usuario", usuario.getNombre());
		model.addAttribute("activePill", "none");
		model.addAttribute("juegos", capacitacionOpciones());

		return "juego-intro";
	}

	/**
	 * Realiaza las verificaciones del estado del juego según en tema de
	 * capacitación y el usuario. -El tema de capacitación que va a jugar -EL
	 * nivel en el que se encuentra según el tema seleccionado.
	 * 
	 * @param model
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "/juego", method = RequestMethod.GET)
	public String validateGameState(@RequestParam("jid") Integer jid,
			Model model, Principal principal) {

		Usuario usuario = getUsuario(principal);

		Juego juego = juegoRepository.findOne(jid);
		model.addAttribute("juegos", capacitacionOpciones());
		try {

			// 1)Si el juego no existe se envía a una página de error
			if (juego == null) {
				model.addAttribute("activePill", "none");
				System.out.println("EL id del juego no existe");

				return "juego-crear-error";

			}
		} catch (Exception e) {
		}

		// Se valida si el usuario tiene niveles
		List<JuegoNivelUsuario> jnu = juegoNivelUsuarioRepository
				.findAllByUsuarioId(usuario.getId());

		if (jnu.size() <= 0) {

			// 4) Se eliminan las respuestas temporales en caso
			// de
			// que el usuario haya
			// salido del juego antes de terminar su primer nivel
			actualizarBase(usuario.getId());

		} else {
			actualizarBase(usuario.getId());

		}

		// Se obtiene el grado máximo de dificultad al que el usuario ha llegado
		// en el juego.
		Integer maxDificultadUsu = juegoNivelUsuarioRepository
				.findMaxDificultadByUser(usuario.getId());

		// Se obtiene el máximo nivel del dificultad del juego
		JuegoNivel maxNivelGame = juegoNivelRepository
				.findMaxDificultadByJuego(jid);

		try {
			// 2) Se valida si el juego tiene niveles. En caso de que no
			// tenga, se envía a una página de error.
			if (maxNivelGame == null) {
				model.addAttribute("activePill", "none");
				System.out.println("El juego no tiene niveles");
				return "juego-crear-error";

			} else {

				// 3) Se valida si el usuario ya terminó este juego
				if (maxDificultadUsu != null) {
					// Se compara el grado de dificultad del usuario con
					// el del nivel del juego

					if (maxDificultadUsu == maxNivelGame.getDificultad()) {
						// Si son iguales, se envía al usuario a la
						// vista que le informa que ya ha completado el
						// juego.
						model.addAttribute("username", usuario.getNombre());
						model.addAttribute("juegoNivel", maxNivelGame);
						model.addAttribute("activePill", "none");

						return "juego-fin";

					}

				}

			}

		} catch (Exception e) {

		}
		List<JuegoNivel> juegoNiveles = juegoNivelRepository.findByJuegoId(
				juego.getId(), new Sort(Direction.ASC, "dificultad"));
		model.addAttribute("numPregunta", 0);
		model.addAttribute("juegoNiveles", juegoNiveles);
		model.addAttribute("juego", juego);
		model.addAttribute("activePill", juego.getId());

		return "juego-descripcion";
	}

	/**
	 * Muestra la pantalla de puntuación del juego
	 * 
	 * @param model
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "/puntuacion/{jjuId}/{numPregunta}", method = RequestMethod.GET)
	public String puntuacionView(@PathVariable(value = "jjuId") Integer jid,
			@PathVariable(value = "numPregunta") Integer numPregunta,
			RedirectAttributes redirect, Model model, Principal principal) {

		Usuario usuario = getUsuario(principal);

		// VALIDA SI EL TIEMPO PARA RESPONDER TERMINÓ
		if (numPregunta == -1) {

			// Se valida si el usuario tiene niveles
			List<JuegoNivelUsuario> jnu = juegoNivelUsuarioRepository
					.findAllByUsuarioId(usuario.getId());
			// 4) Se eliminan las respuestas temporales en caso
			// de
			// que el usuario haya
			// salido del juego antes de terminar

			if (jnu.size() <= 0) {

				actualizarBase(usuario.getId());

			} else {
				actualizarBase(usuario.getId());

			}

			numPregunta = 0;
			model.addAttribute("flashMsg",
					"El tiempo para responder la pregunta terminó. El nivel se ha reiniciado.");

		}
		// Se pone en el modelo el id del juego y se pone el número de la
		// pregunta en 1°
		model.addAttribute("jjuId", jid);
		model.addAttribute("numPregunta", numPregunta++);

		// Obtiene los los niveles del juego
		List<JuegoNivel> niveles = juegoNivelRepository.findByJuegoId(jid,
				new Sort(Direction.ASC, "dificultad"));
		model.addAttribute("niveles", niveles);

		try {

			// Cuenta el número de niveles que lleva el usuario
			Long hasNivel = juegoNivelUsuarioRepository
					.countByUsuarioId(usuario.getId());
			// Se obtiene el máximo nivel del juego
			JuegoNivel maxNivel = juegoNivelRepository
					.findMaxDificultadByJuego(jid);

			// Si el usuario tiene almenos un nivel completado:
			if (hasNivel != 0) {

				// Se obtiene el grado más alto de dificultad completado por el
				// usuario
				Integer dificultadActualUsu = juegoNivelUsuarioRepository
						.findMaxDificultadByUser(usuario.getId());
				// A partir del máximo grado obtenemos el siguiente nivel a
				// jugar
				JuegoNivel nivel = juegoNivelRepository
						.findByDificultad(dificultadActualUsu + 1);

				// Si la tabla temporal no tiene cargadas las preguntas las
				// carga.
				// La tabla temporal es una tabla que permite eliminar cada
				// pregunta acertada, con el fin de que no se repita durante el
				// nivel.

				// Se valida que la tabla no tenga elementos para el usuario.
				// Sino tiene, carga
				// las preguntas.
				Long hasItems = juegoPreguntaTempRepo.countByUsuarioId(usuario
						.getId());
				if (hasItems == 0) {

					// carga las preguntas en la tabla temporal
					List<JuegoPregunta> preguntas = juegoPreguntaRepository
							.findByJuegoNivelIdAndActivo(nivel.getId(), true);

					List<JuegoPreguntaTemp> preguntasTemp = new ArrayList<JuegoPreguntaTemp>();
					for (JuegoPregunta jp : preguntas) {
						JuegoPreguntaTemp tjp = new JuegoPreguntaTemp();
						tjp.setJuegoPregunta(jp);
						tjp.setUsuario(usuario);
						juegoPreguntaTempRepo.save(tjp);
						preguntasTemp.add(tjp);

					}

				}

				model.addAttribute("nivel", nivel);
				// Se obtiene el número de preguntas del nivel
				Integer np = nivel.getNumeroPregutas();
				// Cuando el usuario termina las preguntas del nivel, se guarda
				// el nivel completado.
				if (numPregunta > np) {

					// En este punto el usuario ha terminado el nivel
					// Se guarda el nuevo nivel completado por el usuario
					JuegoNivelUsuario jnu = new JuegoNivelUsuario();
					jnu.setJuegoNivel(nivel);
					jnu.setUsuario(usuario);
					juegoNivelUsuarioRepository.save(jnu);
					// Se eliminan las todas las preguntas temporales
					Session session = sessionFactory.openSession();
					String deleteJPT = String
							.format("delete from JuegoPreguntaTemp jpt where jpt.usuario.id=:usuarioId");
					Query q = session.createQuery(deleteJPT);
					q.setInteger("usuarioId", usuario.getId());
					q.executeUpdate();

					// Se eliminan las todas las respuestas del usuario
					// correspondientes
					// al nivel actual.

					List<JuegoRespuestaUsuario> delNivelActual = juegoRespuestaUsuarioRepository
							.findByJuegoNivelAndUsuario(nivel.getId(),
									getUsuarioId(principal));

					for (JuegoRespuestaUsuario r : delNivelActual) {

						juegoRespuestaUsuarioRepository.delete(r);

					}

					// Verificamos si el nivel que se está jugando es menor al
					// máximo del juego.
					// Mientras sea menor, se carga el siguiente nivel de
					// dificultad.

					if (nivel.getId() < maxNivel.getId()) {

						// Obtiene el siguiente nivel ya que terminó el actual,
						// para poder cargar las preguntas del siguiente.
						JuegoNivel sigNivel = juegoNivelRepository
								.findByDificultad(nivel.getDificultad() + 1);

						// carga las preguntas en la tabla temporal
						List<JuegoPregunta> preguntas = juegoPreguntaRepository
								.findByJuegoNivelIdAndActivo(sigNivel.getId(),
										true);

						List<JuegoPreguntaTemp> preguntasTemp = new ArrayList<JuegoPreguntaTemp>();
						for (JuegoPregunta jp : preguntas) {
							JuegoPreguntaTemp tjp = new JuegoPreguntaTemp();
							tjp.setJuegoPregunta(jp);
							tjp.setUsuario(usuario);
							juegoPreguntaTempRepo.save(tjp);
							preguntasTemp.add(tjp);

						}

					}

					return String.format("redirect:%s/juego-premio/%d/%d/%d",
							PATH, jid, nivel.getId(), maxNivel.getId());

				}
				List<Integer> puntuacion = new ArrayList<Integer>();
				for (int i = 1; i <= np; i++) {
					puntuacion.add(i);
				}

				model.addAttribute("puntuacion", puntuacion);
				model.addAttribute("numPregunta", numPregunta);

			} else {// FIn de la validacion

				// Cuando el usuario no ha completado ningún nivel, el juego
				// arranca con el nivel mínimo de dificultad
				JuegoNivel nivel = juegoNivelRepository.findByMinDificultad();

				Long hasItems = juegoPreguntaTempRepo.countByUsuarioId(usuario
						.getId());
				if (hasItems == 0) {

					// carga las preguntas en la tabla temporal
					List<JuegoPregunta> preguntas = juegoPreguntaRepository
							.findByJuegoNivelIdAndActivo(nivel.getId(), true);

					List<JuegoPreguntaTemp> preguntasTemp = new ArrayList<JuegoPreguntaTemp>();
					for (JuegoPregunta jp : preguntas) {
						JuegoPreguntaTemp tjp = new JuegoPreguntaTemp();
						tjp.setJuegoPregunta(jp);
						tjp.setUsuario(usuario);
						juegoPreguntaTempRepo.save(tjp);
						preguntasTemp.add(tjp);

					}

				}
				model.addAttribute("nivel", nivel);
				Integer np = nivel.getNumeroPregutas();

				// Cuando el usuario termina las preguntas del nivel, se guarda
				// el nivel completado.
				if (numPregunta > np) {

					JuegoNivelUsuario jnu = new JuegoNivelUsuario();
					jnu.setJuegoNivel(nivel);
					jnu.setUsuario(usuario);
					juegoNivelUsuarioRepository.save(jnu);

					// Verificamos si el nivel que se está jugando es menor al
					// máximo del juego.
					// Mientras sea menor, se carga el siguiente nivel de
					// dificultad.

					if (nivel.getId() < maxNivel.getId()) {

						// Obtiene el siguiente nivel ya que terminó el actual,
						// para poder cargar las preguntas del siguiente.
						JuegoNivel sigNivel = juegoNivelRepository
								.findByDificultad(nivel.getDificultad() + 1);

						// carga las preguntas en la tabla temporal
						List<JuegoPregunta> preguntas = juegoPreguntaRepository
								.findByJuegoNivelIdAndActivo(sigNivel.getId(),
										true);

						List<JuegoPreguntaTemp> preguntasTemp = new ArrayList<JuegoPreguntaTemp>();
						for (JuegoPregunta jp : preguntas) {
							JuegoPreguntaTemp tjp = new JuegoPreguntaTemp();
							tjp.setJuegoPregunta(jp);
							tjp.setUsuario(usuario);
							juegoPreguntaTempRepo.save(tjp);
							preguntasTemp.add(tjp);

						}
					}

					return String.format("redirect:%s/juego-premio/%d/%d/%d",
							PATH, jid, nivel.getId(), maxNivel.getId());

				}
				List<Integer> puntuacion = new ArrayList<Integer>();
				for (int i = 1; i <= np; i++) {
					puntuacion.add(i);
				}
				model.addAttribute("puntuacion", puntuacion);
				model.addAttribute("numPregunta", numPregunta);

			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		return "juego-puntuacion";
	}

	@RequestMapping(value = "/pregunta", method = RequestMethod.GET)
	public String pregunta(@RequestParam("jjuId") Integer jjuId,
			@RequestParam("numPregunta") Integer numPregunta,
			@RequestParam("jNivelId") Integer jNivelId, Model model,
			Principal principal) {

		// Carga la lista de las preguntas temporales del nivel que se está
		// jugando
		List<JuegoPreguntaTemp> preguntasTemp = juegoPreguntaTempRepo
				.findByPreguntaNivelAndActivo(jNivelId, true);
		// Selecciona un indice al azar de la lista de preguntas
		int idx = new Random().nextInt(preguntasTemp.size());
		// Se obtiene la pregunta temporal
		JuegoPreguntaTemp preguntaTmp = preguntasTemp.get(idx);
		// Se carga la pregunta con la info de la pregunta temporal
		JuegoPregunta pregunta = juegoPreguntaRepository.findOne(preguntaTmp
				.getJuegoPregunta().getId());
		// Se obtiene el nivel basado en la pregunta
		JuegoNivel jn = juegoNivelRepository.findOne(pregunta.getJuegoNivel()
				.getId());

		/*
		 * List<JuegoRespuestaUsuario> anteriores =
		 * juegoRespuestaUsuarioRepository
		 * .findByJuegoNivelAndUsuario(jn.getId(), getUsuarioId(principal));
		 * 
		 * 
		 * 
		 * Integer jprId = juegoPreguntaRepository.findRandomId(jjuId,
		 * getUsuarioId(principal));
		 * 
		 * JuegoPregunta pregunta = juegoPreguntaRepository.findOne(jprId);
		 * JuegoNivel jn = juegoNivelRepository.findOne(pregunta.getJuegoNivel()
		 * .getId()); List<JuegoRespuestaUsuario> anteriores =
		 * juegoRespuestaUsuarioRepository
		 * .findByJuegoNivelAndUsuario(jn.getId(), getUsuarioId(principal));
		 * 
		 * // Si la pregunta ya fue seleccionada antes selecciona una nueva //
		 * TODO: La bolsa de preguntas debe ser el doble de las preguntas a //
		 * realizar for (JuegoRespuestaUsuario jru : anteriores) {
		 * 
		 * if (jru.getPregunta().getId() == pregunta.getId()) {
		 * 
		 * do { jprId = juegoPreguntaRepository.findRandomId(jjuId,
		 * getUsuarioId(principal)); } while (jru.getPregunta().getId() ==
		 * jprId);
		 * 
		 * pregunta = juegoPreguntaRepository.findOne(jprId); break;
		 * 
		 * }
		 * 
		 * }
		 */

		// Envía información sobre el estado de las ayudas

		try {
			List<JuegoAyudaUsuario> jau = juegoAyudaUsuarioRepository
					.findByJuegoNivelAndUsuario(jn, getUsuario(principal));

			List<JuegoAyudaSegundaUsuario> jasu = juegoAyudaSegundaUsuarioRepository
					.findByJuegoNivelAndUsuario(jn, getUsuario(principal));

			if (!jau.isEmpty())
				for (JuegoAyudaUsuario ayudas : jau) {

					if (ayudas.getAyuda1() == true) {

						model.addAttribute("primeraAyuda", 1);
					} else {

						model.addAttribute("primeraAyuda", 0);
					}

				}
			else {

				model.addAttribute("primeraAyuda", 0);

			}
			if (!jasu.isEmpty())
				for (JuegoAyudaSegundaUsuario ayudas : jasu) {

					if (ayudas.getAyuda2() == true) {

						model.addAttribute("segundaAyuda", 1);
					} else {

						model.addAttribute("segundaAyuda", 0);
					}

				}
			else {

				model.addAttribute("segundaAyuda", 0);

			}

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		// Baraja las respuestas
		Collections.shuffle(pregunta.getRespuestas());
		model.addAttribute("jjuId", jjuId);
		model.addAttribute("pregunta", pregunta);
		model.addAttribute("preguntaId", pregunta.getId());
		model.addAttribute("numPregunta", numPregunta);
		model.addAttribute("jniId", jn.getId());

		return "juego-preguntas";
	}

	@RequestMapping(value = "/respuesta", method = RequestMethod.GET)
	public String respuesta(@RequestParam("jjuId") Integer jjuId,
			@RequestParam("jrpId") Integer jrpId,
			@RequestParam("numPregunta") Integer numPregunta,
			@RequestParam("preguntaId") Integer preguntaId, Model model,
			RedirectAttributes redirect, Principal principal) {

		model.addAttribute("jjuId", jjuId);
		// Obtenemos la respuesta seleccionada por el usuario y la pregunta
		JuegoRespuesta respuesta = juegoRespuestaRepository.findOne(jrpId);
		JuegoPregunta pregunta = respuesta.getJuegoPregunta();
		Usuario usuario = getUsuario(principal);
		// Si la pregunta ya fue mostrada antes a este usuario, se redirige a
		// una página de error.
		List<JuegoRespuestaUsuario> anteriores = juegoRespuestaUsuarioRepository
				.findByPreguntaAndUsuario(pregunta, usuario);
		if (anteriores.size() > 0) {
			for (JuegoRespuestaUsuario jru : anteriores) {

				System.out.println("Respuesta id:"
						+ jru.getJuegoRespuesta().getId() + "/n");
				System.out.println("Respuesta txt:"
						+ jru.getJuegoRespuesta().getTextoRespuesta() + "/n");
			}
			return String.format("redirect:%s/juego-cheating-error/%d", PATH,
					pregunta.getId());
		}
		// Se guarda la respuesta que el usuario seleccionó.
		JuegoRespuestaUsuario jru = new JuegoRespuestaUsuario();
		jru.setJuegoRespuesta(respuesta);
		jru.setPregunta(pregunta);
		jru.setUsuario(usuario);
		juegoRespuestaUsuarioRepository.save(jru);
		// Se guarda la respuesta que el usuario seleccionó en el historial.
		JuegoRespuestaUsuarioHistorial jruh = new JuegoRespuestaUsuarioHistorial();
		jruh.setJuegoRespuesta(respuesta);
		jruh.setPregunta(pregunta);
		jruh.setUsuario(usuario);
		juegoRespuestaUsuarioHistorialRepository.save(jruh);

		if (respuesta.getEsCorrecta()) {
			// Se verifica si aumenta de nivel y se realizan los cambios
			// necesarios en la
			// base de datos

			// Se eliminan las respuestas temporales

			// Se elimina la pregunta de la respuesta seleccionada de la tabla
			// temporal

			JuegoPreguntaTemp jp = juegoPreguntaTempRepo
					.findByJuegoPreguntaIdAndUsuarioId(preguntaId,
							usuario.getId());

			try {

				if (jp != null)
					juegoPreguntaTempRepo.delete(jp);

			} catch (Exception e) {

			}

			return String.format("redirect:%s/puntuacion/%d/%d", PATH, jjuId,
					numPregunta);
		} else {

			// TODO: Reiniciar el nivel ya que pierde todo lo acumulado del
			// nivel actual

			// Se eliminan las todas las preguntas temporales
			Session session = sessionFactory.openSession();
			String deleteJPT = String
					.format("delete from JuegoPreguntaTemp jpt where jpt.usuario.id=:usuarioId");
			Query q = session.createQuery(deleteJPT);
			q.setInteger("usuarioId", usuario.getId());
			q.executeUpdate();

			// Se eliminan las todas las respuestas del usuario correspondientes
			// al nivel actual.

			List<JuegoRespuestaUsuario> delNivelActual = juegoRespuestaUsuarioRepository
					.findByJuegoNivelAndUsuario(pregunta.getJuegoNivel()
							.getId(), getUsuarioId(principal));

			for (JuegoRespuestaUsuario r : delNivelActual) {

				juegoRespuestaUsuarioRepository.delete(r);

			}

			// Reinicia las ayudas
			List<JuegoAyudaUsuario> jau = juegoAyudaUsuarioRepository
					.findByJuegoNivelAndUsuario(pregunta.getJuegoNivel(),
							getUsuario(principal));

			for (JuegoAyudaUsuario ayudas : jau) {

				juegoAyudaUsuarioRepository.delete(ayudas);

			}
			List<JuegoAyudaSegundaUsuario> jasu = juegoAyudaSegundaUsuarioRepository
					.findByJuegoNivelAndUsuario(pregunta.getJuegoNivel(),
							getUsuario(principal));

			for (JuegoAyudaSegundaUsuario ayudas : jasu) {

				juegoAyudaSegundaUsuarioRepository.delete(ayudas);

			}

			redirect.addFlashAttribute("flashMsg",
					"Ha fallado la respuesta, debe reiniciar el nivel");

			return String.format("redirect:%s/juego-error-respuesta/%d", PATH,
					jjuId);
		}
	}

	@RequestMapping(value = "/juego-error-respuesta/{jjuId}", method = RequestMethod.GET)
	public String juegoErrorRespuestaView(
			@PathVariable(value = "jjuId") Integer jjuId, Principal principal,
			Model model) {

		model.addAttribute("jjuId", jjuId);

		return "juego-error-respuesta";
	}

	public void actualizarBase(Integer uid) {

		// Se eliminan las todas las preguntas temporales
		Session session = sessionFactory.openSession();
		String deleteJPT = String
				.format("delete from JuegoPreguntaTemp jpt where jpt.usuario.id=:usuarioId");
		Query q = session.createQuery(deleteJPT);
		q.setInteger("usuarioId", uid);
		q.executeUpdate();

		// Se eliminan las todas las respuestas del usuario correspondientes
		// al nivel actual.
		String deleteJRU = String
				.format("delete from JuegoRespuestaUsuario jru where jru.usuario.id=:usuarioId");
		Query q2 = session.createQuery(deleteJRU);
		q2.setInteger("usuarioId", uid);
		q2.executeUpdate();
		/*
		 * List<JuegoRespuestaUsuario> delNivelActual =
		 * juegoRespuestaUsuarioRepository .findByJuegoNivelAndUsuario(nid,
		 * uid);
		 * 
		 * for (JuegoRespuestaUsuario r : delNivelActual) {
		 * 
		 * juegoRespuestaUsuarioRepository.delete(r);
		 * 
		 * }
		 */

	}

	@RequestMapping(value = "/juego-cheating-error/{jprId}", method = RequestMethod.GET)
	public String juegoCheatingErrorView(
			@PathVariable(value = "jprId") Integer jprId, Principal principal) {

		Usuario usuario = getUsuario(principal);

		// Reinicia el nivel ya que pierde todo lo acumulado del
		// nivel actual

		// Se eliminan las preguntas temporales para reiniciar el nivel
		Session session = sessionFactory.openSession();
		String deleteJPT = String
				.format("delete from JuegoPreguntaTemp jpt where jpt.usuario.id=:usuarioId");
		Query q = session.createQuery(deleteJPT);
		q.setInteger("usuarioId", usuario.getId());
		q.executeUpdate();

		JuegoPregunta pregunta = juegoPreguntaRepository.findOne(jprId);

		List<JuegoRespuestaUsuario> anterioresDelNivel = juegoRespuestaUsuarioRepository
				.findByJuegoNivelAndUsuario(pregunta.getJuegoNivel().getId(),
						getUsuarioId(principal));

		for (JuegoRespuestaUsuario r : anterioresDelNivel) {

			juegoRespuestaUsuarioRepository.delete(r);

		}

		// Reinicia las ayudas
		List<JuegoAyudaUsuario> jau = juegoAyudaUsuarioRepository
				.findByJuegoNivelAndUsuario(pregunta.getJuegoNivel(),
						getUsuario(principal));

		for (JuegoAyudaUsuario ayudas : jau) {

			juegoAyudaUsuarioRepository.delete(ayudas);

		}
		List<JuegoAyudaSegundaUsuario> jasu = juegoAyudaSegundaUsuarioRepository
				.findByJuegoNivelAndUsuario(pregunta.getJuegoNivel(),
						getUsuario(principal));

		for (JuegoAyudaSegundaUsuario ayudas : jasu) {

			juegoAyudaSegundaUsuarioRepository.delete(ayudas);

		}

		return "juego-cheating-error";
	}

	@RequestMapping(value = "/juego-premio/{jjuId}/{jniId}/{maxNivelId}", method = RequestMethod.GET)
	public String juegoPremioView(@PathVariable(value = "jjuId") Integer jjuId,
			@PathVariable(value = "jniId") Integer jniId,
			@PathVariable(value = "maxNivelId") Integer maxNivelId,
			Model model, Principal principal) {

		Usuario usuario = getUsuario(principal);

		JuegoNivel jn = new JuegoNivel();
		jn = juegoNivelRepository.getOne(jniId);

		JuegoNivel maxNivel = juegoNivelRepository.getOne(maxNivelId);

		if (jn.getDificultad() == maxNivel.getDificultad()) {

			// En este momento el juego a terminado

			juegoRespuestaUsuarioHistorialRepository.deleteAll();
			Session session = sessionFactory.openSession();
			String deleteJPT = String
					.format("delete from JuegoPreguntaTemp jpt where jpt.usuario.id=:usuarioId");
			Query q = session.createQuery(deleteJPT);
			q.setInteger("usuarioId", usuario.getId());
			q.executeUpdate();
			String deleteJRU = String
					.format("delete from JuegoRespuestaUsuario jru where jru.usuario.id=:usuarioId");
			Query q2 = session.createQuery(deleteJRU);
			q2.setInteger("usuarioId", usuario.getId());
			q2.executeUpdate();

			return String.format("redirect:%s/juego-fin/%d", PATH, jn.getId());

		}
		model.addAttribute("juegoNivel", jn);
		model.addAttribute("maxNivel", maxNivel);

		return "juego-premio";
	}

	@RequestMapping(value = "/juego-fin/{jniId}", method = RequestMethod.GET)
	public String juegoFin(@PathVariable(value = "jniId") Integer jniId,
			Model model, Principal principal) {

		JuegoNivel jn = new JuegoNivel();
		jn = juegoNivelRepository.getOne(jniId);
		model.addAttribute("juegoNivel", jn);

		return "juego-fin";
	}

	@RequestMapping(value = "/juego-time-out", method = RequestMethod.GET)
	public String timeOut(@RequestParam("jjuId") Integer jjuId,
			@RequestParam("numPregunta") Integer numPregunta,
			Principal principal) {

		return String.format("redirect:%s/puntuacion/%d/%d", PATH, jjuId, -1);
	}

	@RequestMapping(value = "/juego-ayuda-cincuenta-cincuenta", method = RequestMethod.GET)
	public @ResponseBody String ayudaCincuentaCincuenta(
			@RequestParam("jniId") Integer jniId, Principal principal) {

		JuegoAyudaUsuario jau = new JuegoAyudaUsuario();
		jau.setAyuda1(true);
		jau.setJuegoNivel(juegoNivelRepository.findOne(jniId));
		jau.setUsuario(getUsuario(principal));
		juegoAyudaUsuarioRepository.save(jau);

		return "";
	}

	@RequestMapping(value = "/juego-ayuda-publico", method = RequestMethod.GET)
	public @ResponseBody Map<Integer, Integer> ayudaDelPublico(
			@RequestParam("jprId") Integer jprId,
			@RequestParam("jniId") Integer jniId, Principal principal) {

		JuegoPregunta pregunta = juegoPreguntaRepository.findOne(jprId);
		List<JuegoRespuesta> respuestas = pregunta.getRespuestas();
		List<JuegoRespuestaUsuarioHistorial> respuestasUsuarios = juegoRespuestaUsuarioHistorialRepository
				.findByPregunta(pregunta);
		Integer totalUsuarios = respuestasUsuarios.size();
		Map<Integer, Integer> porcentajes = new TreeMap<Integer, Integer>();
		if (totalUsuarios >= 1) {
			Integer i = 0;
			Integer totalR1 = 0;
			Integer totalR2 = 0;
			Integer totalR3 = 0;
			Integer totalR4 = 0;

			for (JuegoRespuesta jr : respuestas) {

				i++;
				for (JuegoRespuestaUsuarioHistorial jruh : respuestasUsuarios) {

					if (i == 1) {
						if (jr.getId() == jruh.getJuegoRespuesta().getId()) {

							porcentajes.put(jruh.getJuegoRespuesta().getId(),
									totalR1 = totalR1 + 1);

						}

					}
					if (i == 2) {
						if (jr.getId() == jruh.getJuegoRespuesta().getId()) {
							porcentajes.put(jruh.getJuegoRespuesta().getId(),
									totalR2 = totalR2 + 1);

						}

					}
					if (i == 3) {
						if (jr.getId() == jruh.getJuegoRespuesta().getId()) {
							porcentajes.put(jruh.getJuegoRespuesta().getId(),
									totalR3 = totalR3 + 1);

						}

					}
					if (i == 4) {
						if (jr.getId() == jruh.getJuegoRespuesta().getId()) {
							porcentajes.put(jruh.getJuegoRespuesta().getId(),
									totalR4 = totalR4 + 1);

						}

					}
				}

			}
			for (Map.Entry<Integer, Integer> entry : porcentajes.entrySet()) {

				entry.setValue((100 * entry.getValue()) / totalUsuarios);
			}

		}
		JuegoAyudaSegundaUsuario jasu = new JuegoAyudaSegundaUsuario();
		jasu.setAyuda2(true);

		jasu.setJuegoNivel(juegoNivelRepository.findOne(jniId));
		jasu.setUsuario(getUsuario(principal));
		juegoAyudaSegundaUsuarioRepository.save(jasu);

		return porcentajes;
	}

}
