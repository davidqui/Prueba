package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.Estante;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.repo.EstanteRepository;
import com.laamware.ejercito.doc.web.repo.ModuloRepository;

@Controller
@RequestMapping(AdminEstanteController.PATH)
public class AdminEstanteController extends
		ArchivoGenController<Estante, Integer, EstanteRepository> {

	static final String PATH = "/admin/estante";

	@Autowired
	EstanteRepository estanteRepository;

	@Autowired
	ModuloRepository moduloRepository;

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "estante";
	}

	@Override
	EstanteRepository getRepository() {
		return estanteRepository;
	}

	@Override
	protected List<Estante> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		return reflectDescriptor(Estante.class);
	}

	@Override
	protected void registerLists(Map<String, Object> map) {

		map.put("modulos",
				moduloRepository.findAll(new Sort(Direction.ASC, "nombre")));

	}

}
