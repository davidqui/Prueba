package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.JuegoNivel;
import com.laamware.ejercito.doc.web.entity.JuegoPregunta;
import com.laamware.ejercito.doc.web.repo.JuegoNivelRepository;
import com.laamware.ejercito.doc.web.repo.JuegoPreguntaRepository;

@Controller
@RequestMapping(AdminJuegoPreguntaController.PATH)
public class AdminJuegoPreguntaController extends
		AdminGenController<JuegoPregunta, Integer, JuegoPreguntaRepository> {

	static final String PATH = "/admin/juego-pregunta";

	@Autowired
	JuegoPreguntaRepository juegoPreguntaRepository;

	@Autowired
	JuegoNivelRepository juegoNivelRepository;

	@Override
	public String getActivePill() {
		return "pregunta";
	}

	@Override
	JuegoPreguntaRepository getRepository() {
		return juegoPreguntaRepository;
	}

	@Override
	String getPath() {
		return AdminJuegoPreguntaController.PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(JuegoPregunta.class);
		d.addAction("Respuestas", AdminJuegoRespuestaController.PATH,
				new String[] { "id" }, new String[] { "jpid" });
		return d;
	}

	@Override
	protected List<JuegoPregunta> findAll(boolean all) {

		if (!all) {
			return getRepository().findByJuegoNivelIdAndActivo(
					getJuegoNivelId(), true);
		} else {
			return getRepository().findByJuegoNivelId(getJuegoNivelId());
		}

	}

	private Integer getJuegoNivelId() {
		return new Integer(queryStringMap.get("jnid"));
	}

	private JuegoNivel getJuegoNivel() {
		JuegoNivel x = new JuegoNivel();
		x.setId(getJuegoNivelId());
		return x;
	}

	@Override
	protected void processRequest(HttpServletRequest req) {
		String jnid = req.getParameter("jnid");
		queryStringMap.put("jnid", jnid);
	}

	@Override
	protected void preSave(JuegoPregunta e) {
		e.setJuegoNivel(getJuegoNivel());
	}

	@Override
	protected String returnUrl(HttpServletRequest req) {
		JuegoNivel jn = juegoNivelRepository.getOne(getJuegoNivelId());
		return AdminJuegoNivelController.PATH + "?jid=" + jn.getJuego().getId();
	}
}
