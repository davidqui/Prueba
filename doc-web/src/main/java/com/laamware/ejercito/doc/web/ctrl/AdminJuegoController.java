package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Juego;
import com.laamware.ejercito.doc.web.repo.JuegoRepository;

@Controller
@PreAuthorize("hasRole('ADMIN_CAPACITACION')")
@RequestMapping(value = AdminJuegoController.PATH)
public class AdminJuegoController extends AdminGenController<Juego, Integer, JuegoRepository> {

	static final String PATH = "/admin/juego";

	@Autowired
	JuegoRepository juegoRepository;

	@Override
	JuegoRepository getRepository() {
		return juegoRepository;
	}

	@Override
	String getPath() {
		return AdminJuegoController.PATH;
	}

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "juego";
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(Juego.class);

		d.addAction("Niveles de dificultad", AdminJuegoNivelController.PATH, new String[] { "id" },
				new String[] { "jid" });

		return d;
	}

	// 2017-02-13 jgarcia@controltechcg.com Issue #49.
	@Override
	protected boolean useNoCreateList() {
		return true;
	}
}
