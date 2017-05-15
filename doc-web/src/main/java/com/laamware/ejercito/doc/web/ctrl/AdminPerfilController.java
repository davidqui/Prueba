package com.laamware.ejercito.doc.web.ctrl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Perfil;
import com.laamware.ejercito.doc.web.entity.PerfilRol;
import com.laamware.ejercito.doc.web.entity.Rol;
import com.laamware.ejercito.doc.web.repo.PerfilRepository;
import com.laamware.ejercito.doc.web.repo.PerfilRolRepository;
import com.laamware.ejercito.doc.web.repo.RolRepository;

@Controller
@PreAuthorize("hasRole('ADMIN_PERFILES')")
@RequestMapping(AdminPerfilController.PATH)
public class AdminPerfilController extends
		AdminGenController<Perfil, Integer, PerfilRepository> {

	static final String PATH = "/admin/profiles";

	@Autowired
	PerfilRepository repo;

	@Autowired
	PerfilRolRepository perfilRolRepository;

	@Autowired
	RolRepository rolRepository;
	
	@Override
	PerfilRepository getRepository() {
		return repo;
	}

	@Override
	protected List<Perfil> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(Perfil.class);

		d.addAction("Roles...", PATH + "/roles", new String[] { "id" },
				new String[] { "pid" });

		return d;
	}

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "profiles";
	}

	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public String roles(@RequestParam("pid") Integer pid, Model model) {

		Perfil p = repo.findOne(pid);
		List<Rol> roles = rolRepository.findAll(new Sort(Direction.ASC, "id"));
		
		//ELIMINAMOS LOS NO ACTIVOS
		Iterator<Rol> iteraRoles = roles.iterator();
		while( iteraRoles.hasNext() ){
			if( !iteraRoles.next().getActivo() ){
				iteraRoles.remove();
			}
		}
		
		model.addAttribute("perfil", p);
		model.addAttribute("roles", roles);
		model.addAttribute("controller", this);

		return "admin-profile-roles";
	}

	@RequestMapping(value = "/roles", method = RequestMethod.POST)
	public String roles(@RequestParam("pid") Integer pid,
			@RequestParam("role") String[] roles, Model model) {

		Perfil p = repo.findOne(pid);

		for (PerfilRol pr : p.getRoles()) {
			String current = pr.getRol().getId();
			boolean found = false;
			for (String r : roles) {
				if (r.equals(current)) {
					found = true;
					break;
				}
			}
			if (!found) {
				perfilRolRepository.delete(pr);
			}
		}

		for (String r : roles) {
			boolean found = false;
			for (PerfilRol pr : p.getRoles()) {
				if (r.equals(pr.getRol().getId())) {
					found = true;
					break;
				}
			}
			if (!found) {
				PerfilRol newPr = new PerfilRol();
				newPr.setPerfil(p);
				Rol rol = new Rol();
				rol.setId(r);
				newPr.setRol(rol);
				perfilRolRepository.save(newPr);
			}
		}

		return String.format("redirect:%s", PATH);
	}

	public boolean has(String rolId, List<PerfilRol> rolLinks) {
		for (PerfilRol perfilRol : rolLinks) {
			if(perfilRol.getRol().getId().equals(rolId))
				return true;
		}
		return false;
	}
	
}
