package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.Entrepano;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.repo.EntrepanoRepository;
import com.laamware.ejercito.doc.web.repo.EstanteRepository;

@Controller
@RequestMapping(AdminEntrepanoController.PATH)
public class AdminEntrepanoController extends
		ArchivoGenController<Entrepano, Integer, EntrepanoRepository> {

	static final String PATH = "/admin/entrepano";

	@Autowired
	EstanteRepository estanteRepository;

	@Autowired
	EntrepanoRepository entrepanoRepository;

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "entrepano";
	}

	@Override
	EntrepanoRepository getRepository() {
		return entrepanoRepository;
	}

	@Override
	protected List<Entrepano> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		return reflectDescriptor(Entrepano.class);
	}

	@Override
	protected void registerLists(Map<String, Object> map) {

		map.put("estantes",
				estanteRepository.findAll(new Sort(Direction.ASC, "nombre")));

	}

}
