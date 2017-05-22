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
import com.laamware.ejercito.doc.web.serv.TRDService;

@Controller
@RequestMapping(value = "/trd")
public class TrdController extends UtilController {

	// 2017-05-22 jgarcia@controltechcg.com Issue #80 (SICDI-Controltech)
	// feature-80
	@Autowired
	TRDService trdService;

	@Autowired
	TrdRepository trdR;

	@Autowired
	DependenciaTrdRepository dependenciaTrdRepository;

	@RequestMapping(value = "/series.json", method = RequestMethod.GET)
	@ResponseBody
	public List<Trd> series(Principal principal) {

		Usuario usuario = getUsuario(principal);
		Dependencia dependencia = usuario.getDependencia();

		List<Trd> trds = trdR.findSeriesByDependencia(dependencia.getId());

		/*
		 * 2017-05-22 jgarcia@controltechcg.com Issue #80 (SICDI-Controltech)
		 * feature-80: Ordenamiento tipo número de versión de las TRD por código
		 * para servicios REST que proveen la información para los diálogos
		 * modales en la creación de documento interno.
		 */
		trdService.ordenarPorCodigo(trds);

		return trds;
	}

	@RequestMapping(value = "/subseries.json", method = RequestMethod.GET)
	@ResponseBody
	public List<Trd> subseries(@RequestParam("serieId") Integer serieId, Principal principal) {

		Usuario usuario = getUsuario(principal);

		List<Trd> subseries = trdR.findSubseries(serieId, usuario.getDependencia().getId());

		/*
		 * 2017-05-22 jgarcia@controltechcg.com Issue #80 (SICDI-Controltech)
		 * feature-80: Ordenamiento tipo número de versión de las TRD por código
		 * para servicios REST que proveen la información para los diálogos
		 * modales en la creación de documento interno.
		 */
		trdService.ordenarPorCodigo(subseries);

		return subseries;
	}
}
