package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Juego;
import com.laamware.ejercito.doc.web.entity.JuegoNivel;
import com.laamware.ejercito.doc.web.repo.JuegoNivelRepository;

@Controller
@RequestMapping(AdminJuegoNivelController.PATH)
public class AdminJuegoNivelController extends
		AdminGenController<JuegoNivel, Integer, JuegoNivelRepository> {

	static final String PATH = "/admin/juego-nivel";

	@Autowired
	JuegoNivelRepository juegoNivelRepository;

	@Override
	JuegoNivelRepository getRepository() {
		return juegoNivelRepository;
	}

	@Override
	String getPath() {
		return AdminJuegoNivelController.PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(JuegoNivel.class);
		d.addAction("Preguntas", AdminJuegoPreguntaController.PATH,
				new String[] { "id" }, new String[] { "jnid" });

		return d;
	}

	@Override
	protected List<JuegoNivel> findAll() {
		return getRepository().findByJuegoIdAndActivoOrderByDificultadAsc(
				getJuegoId(), true);
	}

	private Integer getJuegoId() {
		return new Integer(queryStringMap.get("jid"));
	}

	private Juego getJuego() {
		Juego x = new Juego();
		x.setId(getJuegoId());
		return x;
	}

	@Override
	protected void processRequest(HttpServletRequest req) {
		String jid = req.getParameter("jid");
		queryStringMap.put("jid", jid);
	}

	@Override
	public String getActivePill() {
		return "juego";
	}

	@Override
	protected void preSave(JuegoNivel e) {
		e.setJuego(getJuego());
	}

	@Override
	protected String returnUrl(HttpServletRequest req) {

		return AdminJuegoController.PATH;
	}

}
