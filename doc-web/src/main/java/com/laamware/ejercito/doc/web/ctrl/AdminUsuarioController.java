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
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.ClasificacionRepository;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.repo.PerfilRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;

@Controller
@RequestMapping(AdminUsuarioController.PATH)
public class AdminUsuarioController extends
		AdminGenController<Usuario, Integer, UsuarioRepository> {

	static final String PATH = "/admin/users";

	@Autowired
	UsuarioRepository repo;

	@Autowired
	DependenciaRepository dependenciaRepository;

	@Autowired
	ClasificacionRepository clasificacionRepository;

	@Autowired
	PerfilRepository perfilRepository;

	@Override
	UsuarioRepository getRepository() {
		return repo;
	}

	@Override
	protected List<Usuario> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "login"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(Usuario.class);
		return d;
	}

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "users";
	}

	@Override
	protected void registerLists(Map<String, Object> map) {
		map.put("perfiles", perfilRepository.findByActivo(true, new Sort(
				Direction.ASC, "nombre")));
		map.put("dependencias", dependenciaRepository.findByActivo(true,
				new Sort(Direction.ASC, "nombre")));
		map.put("clasificaciones", clasificacionRepository.findByActivo(true,
				new Sort(Direction.ASC, "orden")));
	}
}
