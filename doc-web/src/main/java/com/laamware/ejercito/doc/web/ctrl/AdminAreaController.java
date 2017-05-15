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
import com.laamware.ejercito.doc.web.entity.Area;
import com.laamware.ejercito.doc.web.repo.AreaRepository;
import com.laamware.ejercito.doc.web.repo.PisoRepository;

@Controller
@RequestMapping(AdminAreaController.PATH)
public class AdminAreaController extends
		ArchivoGenController<Area, Integer, AreaRepository> {

	static final String PATH = "/admin/area";

	@Autowired
	PisoRepository pisoRepository;

	@Autowired
	AreaRepository areaRepository;

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "area";
	}

	@Override
	AreaRepository getRepository() {
		return areaRepository;
	}

	@Override
	protected List<Area> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		return reflectDescriptor(Area.class);
	}

	@Override
	protected void registerLists(Map<String, Object> map) {

		map.put("pisos",
				pisoRepository.findAll(new Sort(Direction.ASC, "nombre")));

	}

}
