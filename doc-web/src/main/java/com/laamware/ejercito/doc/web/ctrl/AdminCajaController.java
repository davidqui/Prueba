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
import com.laamware.ejercito.doc.web.repo.CajaRepository;
import com.laamware.ejercito.doc.web.repo.EntrepanoRepository;
import com.laamware.ejercito.doc.web.repo.TipoCajaRepository;

@Controller
@RequestMapping(AdminCajaController.PATH)
public class AdminCajaController extends
		ArchivoGenController<Caja, Integer, CajaRepository> {

	static final String PATH = "/admin/caja";

	@Autowired
	CajaRepository cajaRepository;

	@Autowired
	EntrepanoRepository entrepanoRepository;

	@Autowired
	TipoCajaRepository tipoCajaRepository;

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "caja";
	}

	@Override
	CajaRepository getRepository() {
		return cajaRepository;
	}

	@Override
	protected List<Caja> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		return reflectDescriptor(Caja.class);
	}

	@Override
	protected void registerLists(Map<String, Object> map) {

		map.put("tipoCajas",
				tipoCajaRepository.findAll(new Sort(Direction.ASC, "nombre")));

		map.put("entrepanos",
				entrepanoRepository.findAll(new Sort(Direction.ASC, "nombre")));

	}

}
