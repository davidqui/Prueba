package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.JuegoPregunta;
import com.laamware.ejercito.doc.web.entity.JuegoRespuesta;
import com.laamware.ejercito.doc.web.repo.JuegoPreguntaRepository;
import com.laamware.ejercito.doc.web.repo.JuegoRespuestaRepository;

@Controller
@RequestMapping(AdminJuegoRespuestaController.PATH)
public class AdminJuegoRespuestaController extends
		AdminGenController<JuegoRespuesta, Integer, JuegoRespuestaRepository> {

	static final String PATH = "/admin/juego-respuesta";

	@Autowired
	JuegoRespuestaRepository juegoRespuestaRepository;

	@Autowired
	JuegoPreguntaRepository juegoPreguntaRepository;

	@Override
	JuegoRespuestaRepository getRepository() {
		return juegoRespuestaRepository;
	}

	@Override
	String getPath() {
		return AdminJuegoRespuestaController.PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(JuegoRespuesta.class);
		return d;
	}

	@Override
	protected List<JuegoRespuesta> findAll(boolean all) {

		if (!all) {
			return getRepository().findByJuegoPreguntaIdAndActivo(
					getJuegoPreguntaId(), true);
		} else {
			return getRepository().findByJuegoPreguntaId(getJuegoPreguntaId());
		}
	}

	private Integer getJuegoPreguntaId() {
		return new Integer(queryStringMap.get("jpid"));
	}

	private JuegoPregunta getJuegoPregunta() {
		JuegoPregunta x = new JuegoPregunta();
		x.setId(getJuegoPreguntaId());
		return x;
	}

	@Override
	protected void processRequest(HttpServletRequest req) {
		String jpid = req.getParameter("jpid");
		queryStringMap.put("jpid", jpid);
	}

	@Override
	protected void preSave(JuegoRespuesta e) {
		e.setJuegoPregunta(getJuegoPregunta());

		try {

			if (e.getEsCorrecta() == null) {

				e.setEsCorrecta(false);

			}

		} catch (Exception ex) {

		}

	}

	@Override
	protected String returnUrl(HttpServletRequest req) {
		JuegoPregunta jp = juegoPreguntaRepository.getOne(getJuegoPreguntaId());
		return AdminJuegoPreguntaController.PATH + "?jnid="
				+ jp.getJuegoNivel().getId();
	}

	@Override
	public String getActivePill() {
		return "respuesta";
	}

}
