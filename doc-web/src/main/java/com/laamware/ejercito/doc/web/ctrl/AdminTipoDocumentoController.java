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

import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.TipoDocumento;
import com.laamware.ejercito.doc.web.repo.PlantillaRepository;
import com.laamware.ejercito.doc.web.repo.TipoDocumentoRepository;

@Controller
@PreAuthorize("hasRole('ADMIN_TIPO_DOCUMENTO')")
@RequestMapping(AdminTipoDocumentoController.PATH)
public class AdminTipoDocumentoController extends
		AdminGenController<TipoDocumento, Integer, TipoDocumentoRepository> {

	static final String PATH = "/admin/tipo-documento";

	@Autowired
	TipoDocumentoRepository repo;

	@Autowired
	PlantillaRepository plantillaRepository;

	@Override
	TipoDocumentoRepository getRepository() {
		return repo;
	}

	@Override
	protected List<TipoDocumento> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(TipoDocumento.class);
		return d;
	}

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "tipo-documento";
	}

	@Override
	protected void registerLists(Map<String, Object> map) {
		// 2017-02-27 jgarcia@controltechcg.com Orden de plantillas por nombre.
		map.put("plantillas", plantillaRepository.findByActivo(true, new Sort(Direction.ASC, "nombre")));
	}
}
