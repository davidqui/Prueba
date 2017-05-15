package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.Estado;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Transicion;
import com.laamware.ejercito.doc.web.repo.EstadoRepository;
import com.laamware.ejercito.doc.web.repo.TipoTransicionRepository;
import com.laamware.ejercito.doc.web.repo.TransicionRepository;

@Controller
@RequestMapping(AdminTransicionController.PATH)
public class AdminTransicionController extends
		AdminGenController<Transicion, Integer, TransicionRepository> {

	static final String PATH = "/admin/proceso-transicion";

	@Autowired
	TransicionRepository transicionRepository;

	@Autowired
	EstadoRepository procesoEstadoRepository;

	@Autowired
	TipoTransicionRepository tipoTransicionRepository;

	@Override
	TransicionRepository getRepository() {
		return transicionRepository;
	}

	@Override
	String getPath() {
		return AdminTransicionController.PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(Transicion.class);
		d.addAction("Condiciones...", AdminCondicionController.PATH,
				new String[] { "id" }, new String[] { "ptr" });
		return d;
	}

	@Override
	protected List<Transicion> findAll(boolean all) {
		if (!all) {
			return getRepository().findByEstadoInicialAndActivo(
					getEstadoInicial(), true);
		} else {
			return getRepository().findByEstadoInicial(getEstadoInicial());
		}
	}

	private Integer getEstadoInicialId() {
		return new Integer(queryStringMap.get("pes"));
	}

	private Estado getEstadoInicial() {
		Estado x = new Estado();
		x.setId(getEstadoInicialId());
		return x;
	}

	@Override
	protected void processRequest(HttpServletRequest req) {
		String pes = req.getParameter("pes");
		queryStringMap.put("pes", pes);
	}

	@Override
	public String getActivePill() {
		return "proceso";
	}

	@Override
	protected void preSave(Transicion e) {
		e.setEstadoInicial(getEstadoInicial());
		if (e.getId() != null) {
			Transicion old = getRepository().getOne(e.getId());
			e.setCondiciones(old.getCondiciones());
		}
	}

	@Override
	protected void registerLists(Map<String, Object> map) {
		Estado pe = procesoEstadoRepository.findOne(getEstadoInicialId());
		map.put("estados", procesoEstadoRepository.findByProcesoId(pe
				.getProceso().getId()));
		map.put("tipos", tipoTransicionRepository.findAll());
	}

	@Override
	protected String returnUrl(HttpServletRequest req) {
		Estado e = procesoEstadoRepository.getOne(getEstadoInicialId());
		return AdminEstadoController.PATH + "?pid=" + e.getProceso().getId();
	}
}
