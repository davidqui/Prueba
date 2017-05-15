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
import com.laamware.ejercito.doc.web.entity.Piso;
import com.laamware.ejercito.doc.web.repo.EdificioRepository;
import com.laamware.ejercito.doc.web.repo.PisoRepository;

@Controller
@RequestMapping(AdminPisoController.PATH)
public class AdminPisoController extends
		ArchivoGenController<Piso, Integer, PisoRepository> {

	static final String PATH = "/admin/piso";

	@Autowired
	PisoRepository pisoRepository;

	@Autowired
	EdificioRepository edificioRepository;

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "piso";
	}

	@Override
	PisoRepository getRepository() {
		return pisoRepository;
	}

	@Override
	protected List<Piso> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		return reflectDescriptor(Piso.class);
	}

	@Override
	protected void registerLists(Map<String, Object> map) {

		map.put("edificios",
				edificioRepository.findAll(new Sort(Direction.ASC, "nombre")));

	}

}
