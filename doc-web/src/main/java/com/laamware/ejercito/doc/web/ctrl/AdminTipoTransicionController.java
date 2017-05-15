package com.laamware.ejercito.doc.web.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.TipoTransicion;
import com.laamware.ejercito.doc.web.repo.TipoTransicionRepository;

@Controller
@RequestMapping(AdminTipoTransicionController.PATH)
public class AdminTipoTransicionController
		extends
		AdminGenController<TipoTransicion, Integer, TipoTransicionRepository> {

	static final String PATH = "/admin/proceso-transicion-tipo";

	@Autowired
	TipoTransicionRepository tipoTransicionRepository;

	@Override
	TipoTransicionRepository getRepository() {
		return tipoTransicionRepository;
	}

	@Override
	String getPath() {
		return AdminTipoTransicionController.PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		return reflectDescriptor(TipoTransicion.class);
	}

	@ModelAttribute("activePill")
	public String getActivePill() {
		return "proceso-transicion-tipo";
	}
}
