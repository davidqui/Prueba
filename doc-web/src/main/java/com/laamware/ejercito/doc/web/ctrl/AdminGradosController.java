package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Grados;
import com.laamware.ejercito.doc.web.repo.GradosRepository;


@RequestMapping(AdminGradosController.PATH)
public class AdminGradosController extends
AdminGenController<Grados, String, GradosRepository> {

	static final String PATH = "/admin/grados";

	@Autowired
	GradosRepository repo;

	@Override
	GradosRepository getRepository() {
		return repo;
	}

	@Override
	protected List<Grados> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(Grados.class);
		return d;
	}

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "grados";
	}
	
	
	@Override
	protected void preSave(Grados e) {
		super.preSave(e);
	}

}
