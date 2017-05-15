package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.Edificio;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.repo.EdificioRepository;

@Controller
@RequestMapping(AdminEdificioController.PATH)
public class AdminEdificioController extends
		ArchivoGenController<Edificio, Integer, EdificioRepository> {

	static final String PATH = "/admin/edificio";

	@Autowired
	EdificioRepository edificioRepository;

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "edificio";
	}

	@Override
	EdificioRepository getRepository() {
		return edificioRepository;
	}

	@Override
	protected List<Edificio> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		return reflectDescriptor(Edificio.class);
	}

}
