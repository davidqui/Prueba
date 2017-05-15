package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.DependenciaTrd;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.repo.DependenciaTrdRepository;
import com.laamware.ejercito.doc.web.repo.TrdRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;

@RequestMapping(AdminDependenciaController.PATH)
public class AdminDependenciaController extends AdminGenController<Dependencia, Integer, DependenciaRepository> {

	static final String PATH = "/admin/dependencia";

	@Autowired
	DependenciaRepository dependenciaRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	TrdRepository trdRepository;

	@Autowired
	DependenciaTrdRepository dependenciaTrdRepository;

	@Override
	DependenciaRepository getRepository() {
		return dependenciaRepository;
	}

	@Override
	String getPath() {
		return AdminDependenciaController.PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(Dependencia.class);
		d.addAction("Subseries", PATH + "/trds-subseries", new String[] { "id" }, new String[] { "depId" });
		return d;
	}

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "dependencia";
	}

	@Override
	protected void registerLists(Map<String, Object> map) {
		super.registerLists(map);
		map.put("usuarios", usuarioRepository.findAll(new Sort(Direction.ASC, "login")));
		map.put("dependencias", dependenciaRepository.findAll(new Sort(Direction.ASC, "nombre")));
	}

	@RequestMapping(value = "/trds-subseries", method = RequestMethod.GET)
	public String dependenciaTrd(@RequestParam("depId") String depIdS, Model model) {

		Integer depId = Integer.parseInt(depIdS.replaceAll("\\.", "").trim());
		Dependencia dep = dependenciaRepository.findOne(depId);
		List<Trd> trds = trdRepository.findByActivoAndSerieNotNull(true, (new Sort(Direction.ASC, "nombre")));

		model.addAttribute("dependencia", dep);
		model.addAttribute("trds", trds);
		model.addAttribute("controller", this);

		return "admin-dependencia-trds";
	}

	@RequestMapping(value = "/trds-subseries", method = RequestMethod.POST)
	public String roles(@RequestParam("depId") Integer depId, @RequestParam("trd") Integer[] trds, Model model) {

		Dependencia dep = dependenciaRepository.findOne(depId);

		for (DependenciaTrd dtrd : dep.getTrds()) {
			Integer current = dtrd.getTrd().getId();
			boolean found = false;
			for (Integer t : trds) {
				if (t.equals(current)) {
					found = true;
					break;
				}
			}
			if (!found) {
				dependenciaTrdRepository.delete(dtrd);
			}
		}

		for (Integer t : trds) {
			boolean found = false;
			for (DependenciaTrd dtrd : dep.getTrds()) {
				if (t.equals(dtrd.getTrd().getId())) {
					found = true;
					break;
				}
			}
			if (!found) {
				DependenciaTrd newdtrd = new DependenciaTrd();
				newdtrd.setDependencia(dep);
				Trd trd = new Trd();
				trd.setId(t);
				newdtrd.setTrd(trd);
				dependenciaTrdRepository.save(newdtrd);
			}
		}

		return String.format("redirect:%s", PATH);
	}

	public boolean has(Integer trdId, List<DependenciaTrd> trdLinks) {
		for (DependenciaTrd dependenciaTrd : trdLinks) {
			if (dependenciaTrd.getTrd().getId().equals(trdId))
				return true;
		}
		return false;
	}

}
