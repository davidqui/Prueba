package com.laamware.ejercito.doc.web.ctrl;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.ExpedienteEstado;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.EstadoExpedienteRepository;
import com.laamware.ejercito.doc.web.repo.ExpedienteEstadoRepository;
import com.laamware.ejercito.doc.web.repo.ExpedienteRepository;
import com.laamware.ejercito.doc.web.repo.TrdRepository;

@Controller
@PreAuthorize("hasRole('ADMIN_EXPEDIENTES')")
@RequestMapping(AdminExpedienteController.PATH)
public class AdminExpedienteController extends AdminGenController<Expediente, Integer, ExpedienteRepository> {

	static final String PATH = "/admin/expediente";

	@Value("${docweb.expediente.serie}")
	Integer expedienteSerie;

	@Autowired
	ExpedienteRepository repo;

	@Autowired
	DependenciaRepository dependenciaRepository;

	@Autowired
	DocumentoRepository documentoRepository;

	@Autowired
	TrdRepository trdRepository;

	@Autowired
	EstadoExpedienteRepository estadoExpedienteRepository;

	@Autowired
	ExpedienteEstadoRepository expedienteEstadoRepository;

	@Override
	ExpedienteRepository getRepository() {
		return repo;
	}

	@Override
	protected List<Expediente> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(Expediente.class);

		d.addAction("Contenido...", "/admin/expediente/contenido", new String[] { "id" }, new String[] { "eid" });

		return d;
	}

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "expediente";
	}

	@Override
	protected void registerLists(Map<String, Object> map) {
		map.put("dependencias", dependenciaRepository.findAll(new Sort(Direction.ASC, "nombre")));

		map.put("tdrs", trdRepository.findBySerie(expedienteSerie, new Sort(Direction.ASC, "nombre")));

	}

	@Override
	protected void preSave(Expediente e) {

		Integer plazo = e.getTrd().getPlazo();

		try {
			if (plazo == null) {
				e.setTiempoRetencion(e.getTrd().getRetArchivoGeneral());

			} else
				e.setTiempoRetencion(plazo);

		} catch (Exception exeption) {

		}
		e.setEstado(estadoExpedienteRepository.getByNombre("Abierto"));

	}

	@Override
	protected void postSave(Expediente e) {

		ExpedienteEstado ee = new ExpedienteEstado();
		ee.setExpediente(e);
		ee.setEstado(estadoExpedienteRepository.getByNombre("Abierto"));
		expedienteEstadoRepository.save(ee);

	}

	@RequestMapping(value = "/contenido", method = RequestMethod.GET)
	public String contenido(@RequestParam("eid") Integer eid, Model model, Principal principal) {
		Expediente expediente = repo.findOne(eid);
		model.addAttribute("expediente", expediente);
		model.addAttribute("activePill", getActivePill());
		List<Documento> documentos = documentoRepository.findByExpediente(expediente,
				new Sort(Direction.ASC, "cuando"));
		for (Documento documento : documentos) {
			documento.getAdjuntos().size();
		}
		model.addAttribute("documentos", documentos);
		return "admin-expediente-contenido";
	}

}
