package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Tipologia;
import com.laamware.ejercito.doc.web.repo.TipologiaRepository;

@Controller
@PreAuthorize("hasRole('ADMIN_TIPOLOGIA')")
@RequestMapping(AdminTipologiaController.PATH)
public class AdminTipologiaController extends
		AdminGenController<Tipologia, Integer, TipologiaRepository> {

	static final String PATH = "/admin/tipologia";

	@Autowired
	TipologiaRepository tipologiaRepository;

	@Override
	TipologiaRepository getRepository() {
		return tipologiaRepository;
	}

	@Override
	protected List<Tipologia> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(Tipologia.class);
		return d;
	}

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "tipologia";
	}

}
