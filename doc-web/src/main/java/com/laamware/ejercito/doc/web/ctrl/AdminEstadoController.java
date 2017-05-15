package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.Estado;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.repo.EstadoRepository;

@Controller
@RequestMapping(AdminEstadoController.PATH)
public class AdminEstadoController extends AdminGenController<Estado, Integer, EstadoRepository> {

	static final String PATH = "/admin/proceso-estado";

	@Autowired
	EstadoRepository procesoEstadoRepository;

	@Override
	EstadoRepository getRepository() {
		return procesoEstadoRepository;
	}

	@Override
	String getPath() {
		return AdminEstadoController.PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(Estado.class);
		d.addAction("Transiciones...", "/admin/proceso-transicion", new String[] { "id" }, new String[] { "pes" });
		return d;
	}

	@Override
	protected List<Estado> findAll() {
		// 2017-02-09 jgarcia@controltechcg.com Issue #138: Corrección para
		// presentación de módulo de estados.
		Integer procesoId = getProcesoId();
		List<Estado> estados = getRepository().findByProcesoIdAndActivo(procesoId, true);
		return estados;
	}

	@Override
	protected List<Estado> findAll(boolean all) {
		// 2017-02-09 jgarcia@controltechcg.com Issue #138: Corrección para
		// presentación de módulo de estados.
		if (all) {
			return getRepository().findByActivo(true);
		} else {
			return findAll();
		}
	}

	private Integer getProcesoId() {
		return new Integer(queryStringMap.get("pid"));
	}

	private Proceso getProceso() {
		Proceso x = new Proceso();
		x.setId(getProcesoId());
		return x;
	}

	@Override
	protected void processRequest(HttpServletRequest req) {
		String pid = req.getParameter("pid");
		queryStringMap.put("pid", pid);
	}

	@Override
	public String getActivePill() {
		return "proceso";
	}

	@Override
	protected void preSave(Estado e) {
		e.setProceso(getProceso());
		if (e.getId() != null) {
			Estado old = getRepository().getOne(e.getId());
			e.setTransiciones(old.getTransiciones());
		}
	}

	@Override
	protected String returnUrl(HttpServletRequest req) {
		return AdminProcesoController.PATH;
	}
}
