package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.repo.PlantillaRepository;
import com.laamware.ejercito.doc.web.repo.TipoDocumentoRepository;
import com.laamware.ejercito.doc.web.repo.TrdRepository;

@Controller
@PreAuthorize("hasRole('ADMIN_TRD')")
@RequestMapping(AdminTrdController.PATH)
public class AdminTrdController extends
		AdminGenController<Trd, Integer, TrdRepository> {

	static final String PATH = "/admin/trd";

	@Autowired
	TrdRepository trdRepository;

	@Autowired
	PlantillaRepository plantillaRepository;

	@Autowired
	TipoDocumentoRepository tipoDocumentoRepository;

	@Override
	protected List<Trd> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "codigo"));
	}

	@Override
	public String getActivePill() {
		return "trd";
	}

	@Override
	TrdRepository getRepository() {
		return trdRepository;
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		return reflectDescriptor(Trd.class);
	}

	@Override
	protected void registerLists(Map<String, Object> map) {
		// 2017-02-27 jgarcia@controltechcg.com Orden de plantillas por nombre.
		map.put("plantillas", plantillaRepository.findAll(new Sort(Direction.ASC, "nombre")));
		
		map.put("series", trdRepository.findBySerie(null, new Sort(Direction.ASC, "codigo")));
		
		map.put("tiposDocumento", tipoDocumentoRepository.findAll(new Sort(Direction.ASC, "nombre")));
	}

}
