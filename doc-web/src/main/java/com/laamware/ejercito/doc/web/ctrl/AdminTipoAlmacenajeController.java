package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.TipoAlmacenaje;
import com.laamware.ejercito.doc.web.repo.TipoAlmacenajeRepository;

@Controller
@RequestMapping(AdminTipoAlmacenajeController.PATH)
public class AdminTipoAlmacenajeController extends
		ArchivoGenController<TipoAlmacenaje, Integer, TipoAlmacenajeRepository> {

	static final String PATH = "/admin/tipo-almacenaje";

	@Autowired
	TipoAlmacenajeRepository tipoAlmacenajeRepository;

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "tipo-almacenaje";
	}

	@Override
	TipoAlmacenajeRepository getRepository() {
		return tipoAlmacenajeRepository;
	}

	@Override
	protected List<TipoAlmacenaje> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		return reflectDescriptor(TipoAlmacenaje.class);
	}

}
