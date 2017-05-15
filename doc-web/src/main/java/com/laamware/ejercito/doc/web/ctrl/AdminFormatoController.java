package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.laamware.ejercito.doc.web.entity.Formato;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.repo.FormatoRepository;
import com.laamware.ejercito.doc.web.repo.TrdRepository;
import com.laamware.ejercito.doc.web.serv.OFS;

@Controller
@PreAuthorize("hasRole('ADMIN_FORMATOS')")
@RequestMapping(AdminFormatoController.PATH)
public class AdminFormatoController extends
		AdminGenController<Formato, Integer, FormatoRepository> {

	static final String PATH = "/admin/formatos";

	@Autowired
	FormatoRepository formatoRepository;

	@Autowired
	TrdRepository trdRepository;

	@Override
	protected List<Formato> findAll() {
		return getRepository().findAll();
	}

	@Override
	public String getActivePill() {
		return "formato";
	}

	@Override
	FormatoRepository getRepository() {
		return formatoRepository;
	}

	@Autowired
	OFS ofs;

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		return reflectDescriptor(Formato.class);
	}

	@Override
	protected void preSaveWithFiles(Formato f, MultipartFile archivo) {

		if (!archivo.isEmpty()) {

			try {
				f.setOriginal(archivo.getOriginalFilename());
				String id = ofs.saveAsIs(archivo.getBytes(),
						archivo.getContentType());
				f.setContenido(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void registerLists(Map<String, Object> map) {

		map.put("series", trdRepository.findByActivoAndSerieNotNull(true,
				new Sort(Direction.ASC, "codigo")));

	}
}
