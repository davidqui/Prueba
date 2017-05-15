package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.Destinatario;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.repo.DestinatarioRepository;
import com.laamware.ejercito.doc.web.repo.PlantillaRepository;
import com.laamware.ejercito.doc.web.repo.TipoDestinatarioRepository;

@Controller
@PreAuthorize("hasRole('ELIMINADO_ADMIN_DESTINATARIOS')")
@RequestMapping(AdminDestinatarioController.PATH)
public class AdminDestinatarioController extends
		AdminGenController<Destinatario, Integer, DestinatarioRepository> {

	static final String PATH = "/admin/destinatarios";

	@Autowired
	DestinatarioRepository repo;

	@Autowired
	PlantillaRepository plantillaRepository;

	@Autowired
	TipoDestinatarioRepository tipoDestinatarioRepository;

	@Override
	DestinatarioRepository getRepository() {
		return repo;
	}

	@Override
	protected List<Destinatario> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(Destinatario.class);
		return d;
	}

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "destinatarios";
	}

	@Override
	protected void registerLists(Map<String, Object> map) {
		map.put("tiposDestinatario", tipoDestinatarioRepository.findByActivo(
				true, new Sort(Direction.ASC, "nombre")));
	}
}
