package com.laamware.ejercito.doc.web.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.laamware.ejercito.doc.web.entity.Estado;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.entity.Transicion;
import com.laamware.ejercito.doc.web.repo.ProcesoRepository;
import com.laamware.ejercito.doc.web.repo.TransicionRepository;

@Controller
//@PreAuthorize("hasRole('ADMIN_PROCESOS')")
@RequestMapping(AdminProcesoController.PATH)
public class AdminProcesoController 
//       extends AdminGenController<Proceso, Integer, ProcesoRepository> 
        {

	static final String PATH = "/admin/proceso";

	@Autowired
	ProcesoRepository procesoRepository;

	@Autowired
	private TransicionRepository transicionRepository;

//	@Override
//	ProcesoRepository getRepository() {
//		return procesoRepository;
//	}
//
//	@Override
//	String getPath() {
//		return AdminProcesoController.PATH;
//	}
//
//	@Override
//	GenDescriptor getDescriptor() {
//		GenDescriptor d = reflectDescriptor(Proceso.class);
//
//		d.addAction("Estados...", AdminEstadoController.PATH, new String[] { "id" }, new String[] { "pid" });
//
//		d.addAction("Variables...", AdminVariableController.PATH, new String[] { "id" }, new String[] { "pid" });
//
//		d.addAction("Diagrama...", AdminProcesoController.PATH + "/diagrama", new String[] { "id" },
//				new String[] { "pid" });
//
//		return d;
//	}
//
//	@Override
//	@ModelAttribute("activePill")
//	public String getActivePill() {
//		return "proceso";
//	}
//
////	@RequestMapping(value = "/diagrama", method = RequestMethod.GET)
//	public String diagrama(@RequestParam("pid") Integer pid, Model model) {
//		model.addAttribute("pid", pid);
//		return "admin-proceso-diagrama";
//	}
//
//	@Override
//	protected void registerLists(Map<String, Object> map) {
//		super.registerLists(map);
//		List<Proceso> procesos = procesoRepository.findByActivoAndAliasIsNull(true, new Sort(Direction.ASC, "nombre"));
//		map.put("procesos", procesos);
//		List<Proceso> procesosActivos = procesoRepository.findByActivo(true, new Sort(Direction.ASC, "nombre"));
//		map.put("procesosActivos", procesosActivos);
//	}

//	@RequestMapping(value = "/proceso-json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> procesoJson(@RequestParam("proId") Integer proId) {

		Proceso proceso = procesoRepository.findOne(proId);

		HashMap<String, Object> map = new HashMap<String, Object>();

		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		map.put("nodes", nodes);
		for (Estado e : proceso.getEstados()) {

			if (e.getActivo() == false)
				continue;

			HashMap<String, Object> node = new HashMap<String, Object>();
			node.put("id", e.getId());

			String label = e.getNombre();
			if (label.indexOf(' ') < 0) {
				label = label.substring(0, 4);
			} else {
				label = getSigla(label);
			}
			node.put("label", label);
			node.put("title", e.getNombre());
			node.put("shape", "circle");
			node.put("shadow", "true");
			node.put("mass", 1.0 * e.getId().toString().length() * 2);

			if (e.getEsInicial() == true) {
				node.put("color", "green");
			} else if (e.getEsFinal() == true) {
				node.put("color", "red");
			}
			nodes.add(node);
		}

		List<Transicion> pTransiciones = transicionRepository.findByEstadoInicialProceso(proceso);
		List<Map<String, Object>> edges = new ArrayList<Map<String, Object>>();
		map.put("edges", edges);
		for (Transicion t : pTransiciones) {
			if (t.getActivo() == false)
				continue;
			HashMap<String, Object> edge = new HashMap<String, Object>();
			edge.put("from", t.getEstadoInicial().getId());
			edge.put("to", t.getEstadoFinal().getId());
			edge.put("label", t.getNombre());
			edge.put("length", 50);
			edges.add(edge);
		}

		HashMap<String, Object> options = new HashMap<String, Object>();
		map.put("options", options);
		options.put("height", "100%");
		options.put("width", "100%");

		HashMap<String, Object> edgeOptions = new HashMap<String, Object>();
		options.put("edges", edgeOptions);
		edgeOptions.put("arrows", "to");

		HashMap<String, Object> layoutOptions = new HashMap<String, Object>();
		options.put("layout", layoutOptions);
		layoutOptions.put("randomSeed", 0);

		HashMap<String, Object> manipOptions = new HashMap<String, Object>();
		options.put("manipulation", manipOptions);
		manipOptions.put("enabled", true);

		return map;
	}

	private String getSigla(String str) {
		String x = StringUtils.normalizeSpace(str);
		String[] words = StringUtils.splitByWholeSeparator(x, null);
		StringBuilder b = new StringBuilder();
		for (String w : words) {
			b.append(primeraLetra(w));
		}
		return b.toString().toUpperCase();
	}

	private Character primeraLetra(String w) {
		if (StringUtils.isBlank(w)) {
			return null;
		}
		final int sz = w.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isLetter(w.charAt(i)) == true) {
				return w.charAt(i);
			}
		}
		return null;
	}

}
