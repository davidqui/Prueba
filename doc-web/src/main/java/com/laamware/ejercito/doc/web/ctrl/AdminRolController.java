package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Rol;
import com.laamware.ejercito.doc.web.repo.RolRepository;

@Controller
@RequestMapping(AdminRolController.PATH)
public class AdminRolController extends
		AdminGenController<Rol, String, RolRepository> {

	static final String PATH = "/admin/rol";

	@Autowired
	RolRepository repo;

	@Override
	RolRepository getRepository() {
		return repo;
	}

	@Override
	protected List<Rol> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "id"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(Rol.class);
		return d;
	}

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "rol";
	}

}
