package com.laamware.ejercito.doc.web.ctrl;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DependenciaTrdRepository;
import com.laamware.ejercito.doc.web.repo.TrdRepository;

@Controller
@RequestMapping(value = "/trd")
public class TrdController extends UtilController {

	@Autowired
	TrdRepository trdR;

	@Autowired
	DependenciaTrdRepository dependenciaTrdRepository;

	@RequestMapping(value = "/series.json", method = RequestMethod.GET)
	@ResponseBody
	public List<Trd> series(Principal principal) {

		Usuario usuario = getUsuario(principal);
		Dependencia dep = usuario.getDependencia();

		List<Trd> trds = trdR.findSeriesByDependencia(dep.getId());

		return trds;
	}

	@RequestMapping(value = "/subseries.json", method = RequestMethod.GET)
	@ResponseBody
	public List<Trd> subseries(@RequestParam("serieId") Integer serieId, Principal principal) {

		Usuario usuario = getUsuario(principal);
		
		List<Trd> subseries = trdR.findSubseries(serieId, usuario.getDependencia().getId());

		return subseries;
	}
}
