package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Modulo;
import com.laamware.ejercito.doc.web.repo.AreaRepository;
import com.laamware.ejercito.doc.web.repo.ModuloRepository;

@Controller
@RequestMapping(AdminModuloController.PATH)
public class AdminModuloController extends
		ArchivoGenController<Modulo, Integer, ModuloRepository> {

	static final String PATH = "/admin/modulo";

	@Autowired
	ModuloRepository moduloRepository;

	@Autowired
	AreaRepository areaRepository;

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "modulo";
	}

	@Override
	ModuloRepository getRepository() {
		return moduloRepository;
	}

	@Override
	protected List<Modulo> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		return reflectDescriptor(Modulo.class);
	}

	@Override
	protected void registerLists(Map<String, Object> map) {

		map.put("areas",
				areaRepository.findAll(new Sort(Direction.ASC, "nombre")));

	}

}
