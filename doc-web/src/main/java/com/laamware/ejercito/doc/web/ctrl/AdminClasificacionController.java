package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.Clasificacion;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.repo.ClasificacionRepository;

@Controller
@PreAuthorize("hasRole('ADMIN_CLASIFICACIONES')")
@RequestMapping(AdminClasificacionController.PATH)
public class AdminClasificacionController extends
		AdminGenController<Clasificacion, Integer, ClasificacionRepository> {

	static final String PATH = "/admin/clasificacion";

	@Autowired
	ClasificacionRepository clasificacionRepository;

	@Override
	ClasificacionRepository getRepository() {
		return clasificacionRepository;
	}

	@Override
	protected List<Clasificacion> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(Clasificacion.class);
		return d;
	}

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "clasificacion";
	}

}
