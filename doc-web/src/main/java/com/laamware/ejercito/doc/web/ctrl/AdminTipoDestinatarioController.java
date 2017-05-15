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
import com.laamware.ejercito.doc.web.entity.TipoDestinatario;
import com.laamware.ejercito.doc.web.repo.TipoDestinatarioRepository;

@Controller
@PreAuthorize("hasRole('ELIMINADO_ADMIN_TIPOS_DESTINATARIO')")
@RequestMapping(AdminTipoDestinatarioController.PATH)
public class AdminTipoDestinatarioController
		extends
		AdminGenController<TipoDestinatario, Integer, TipoDestinatarioRepository> {

	static final String PATH = "/admin/tipo-destinatario";

	@Autowired
	TipoDestinatarioRepository repo;

	@Override
	TipoDestinatarioRepository getRepository() {
		return repo;
	}

	@Override
	protected List<TipoDestinatario> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(TipoDestinatario.class);
		return d;
	}

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "tipo-destinatario";
	}

}
