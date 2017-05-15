package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.TipoCaja;
import com.laamware.ejercito.doc.web.repo.TipoCajaRepository;

@Controller
@RequestMapping(AdminTipoCajaController.PATH)
public class AdminTipoCajaController extends
		ArchivoGenController<TipoCaja, Integer, TipoCajaRepository> {

	static final String PATH = "/admin/tipo-caja";

	@Autowired
	TipoCajaRepository tipoCajaRepository;

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "tipo-caja";
	}

	@Override
	TipoCajaRepository getRepository() {
		return tipoCajaRepository;
	}

	@Override
	protected List<TipoCaja> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		return reflectDescriptor(TipoCaja.class);
	}

}
