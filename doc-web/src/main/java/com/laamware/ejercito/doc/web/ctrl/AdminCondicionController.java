package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.Condicion;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Transicion;
import com.laamware.ejercito.doc.web.repo.CondicionRepository;
import com.laamware.ejercito.doc.web.repo.TransicionRepository;
import com.laamware.ejercito.doc.web.serv.ProcesoService;

@Controller
@RequestMapping(AdminCondicionController.PATH)
public class AdminCondicionController extends
		AdminGenController<Condicion, Integer, CondicionRepository> {

	static final String PATH = "/admin/proceso-condicion";

	@Autowired
	CondicionRepository condicionRepository;

	@Autowired
	TransicionRepository transicionRepository;

	@Autowired
	ProcesoService procesoService;

	@Override
	CondicionRepository getRepository() {
		return condicionRepository;
	}

	@Override
	String getPath() {
		return AdminCondicionController.PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(Condicion.class);
		return d;
	}

	@Override
	protected List<Condicion> findAll(boolean all) {
		if (all) {
			return getRepository().findByTransicionAndActivo(getTransicion(),
					true);
		} else {
			return getRepository().findByTransicion(getTransicion());

		}
	}

	private Integer getTransicionId() {
		return new Integer(queryStringMap.get("ptr"));
	}

	private Transicion getTransicion() {
		Transicion x = new Transicion();
		x.setId(getTransicionId());
		return x;
	}

	@Override
	protected void processRequest(HttpServletRequest req) {
		String ptr = req.getParameter("ptr");
		queryStringMap.put("ptr", ptr);
	}

	@Override
	public String getActivePill() {
		return "proceso";
	}

	@Override
	protected void preSave(Condicion e) {
		e.setTransicion(getTransicion());
		procesoService.getConditionObject(e);
	}

	@Override
	protected String returnUrl(HttpServletRequest req) {
		Transicion e = transicionRepository.getOne(getTransicionId());
		return AdminTransicionController.PATH + "?pes="
				+ e.getEstadoInicial().getId();
	}
}
