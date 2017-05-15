package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.Almacenaje;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.repo.AlmacenajeRepository;
import com.laamware.ejercito.doc.web.repo.CajaRepository;
import com.laamware.ejercito.doc.web.repo.TipoAlmacenajeRepository;

@Controller
@RequestMapping(AdminAlmacenajeController.PATH)
public class AdminAlmacenajeController extends
		ArchivoGenController<Almacenaje, Integer, AlmacenajeRepository> {

	static final String PATH = "/admin/almacenaje";

	@Autowired
	AlmacenajeRepository almacenajeRepository;

	@Autowired
	CajaRepository cajaRepository;

	@Autowired
	TipoAlmacenajeRepository tipoAlmacenajeRepository;

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "almacenaje";
	}

	@Override
	AlmacenajeRepository getRepository() {
		return almacenajeRepository;
	}

	@Override
	protected List<Almacenaje> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		return reflectDescriptor(Almacenaje.class);
	}

	@Override
	protected void registerLists(Map<String, Object> map) {

		map.put("cajas",
				cajaRepository.findAll(new Sort(Direction.ASC, "nombre")));

		map.put("tipoAlmacenajes", tipoAlmacenajeRepository.findAll(new Sort(
				Direction.ASC, "nombre")));

	}

}
