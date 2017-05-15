package com.laamware.ejercito.doc.web.ctrl;

import java.security.Principal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.EstadoExpediente;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.ExpedienteEstado;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.AdjuntoRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoDependenciaRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.EstadoExpedienteRepository;
import com.laamware.ejercito.doc.web.repo.ExpedienteEstadoRepository;
import com.laamware.ejercito.doc.web.repo.ExpedienteRepository;
import com.laamware.ejercito.doc.web.repo.TrdRepository;

/**
 * @author mcr
 *
 */
@Controller
@RequestMapping(value = "/expediente")
public class ExpedienteController extends UtilController {

	private static final Logger LOG = LoggerFactory.getLogger(DocumentoController.class);

	public static final String PATH = "/expediente";

	@Autowired
	ExpedienteRepository repo;

	@Autowired
	DocumentoRepository documentoRepository;

	@Autowired
	TrdRepository trdRepository;

	@Autowired
	AdjuntoRepository adjuntoRepository;

	@Autowired
	DocumentoDependenciaRepository documentoDependenciaRepository;

	@Autowired
	ExpedienteEstadoRepository expedienteEstadoRepository;

	@Autowired
	EstadoExpedienteRepository estadoExpedienteRepository;

	@Autowired
	DataSource ds;

	@RequestMapping(value = "/contenido", method = RequestMethod.GET)
	@Transactional
	public String contenido(@RequestParam("eid") Integer eid, Model model, Principal principal) {

		Expediente expediente = repo.findOne(eid);
		model.addAttribute("eid", eid);
		model.addAttribute("expediente", expediente);
		List<Documento> documentos = documentoRepository.findByExpediente(expediente,
				new Sort(Direction.ASC, "cuando"));
		for (Documento documento : documentos) {
			documento.getAdjuntos().size();
		}
		model.addAttribute("documentos", documentos);
		return "expediente-contenido";
	}

	/**
	 * @param model
	 * @param principal
	 * @return exediente-list: La lista de expedientes que contienen documentos
	 *         en estado final.
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model, Principal principal) {

		Dependencia dependencia = getUsuario(principal).getDependencia();

		try {
			// Si es jefe de una dependencia padre muestra todos los expedientes
			if (dependencia != null && dependencia.getPadre() == null) {
				if (dependencia.getJefe().getId() == getUsuario(principal).getId()) {

					JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
					StringBuilder sbQuery = new StringBuilder();
					sbQuery.append("SELECT EXP.EXP_ID, EXP.EXP_NOMBRE, EXP.CUANDO,EXP.DEP_ID, EXP.TRD_ID, EXP.ESEX_ID, "
							+ " count(doc.doc_id), sum(PES.PES_FINAL) " + " FROM EXPEDIENTE EXP "
							+ " JOIN ESTADO_EXPEDIENTE EE ON EE.ESEX_ID = EXP.ESEX_ID "
							+ " JOIN DEPENDENCIA DEP ON EXP.DEP_ID=DEP.DEP_ID "
							+ " JOIN DOCUMENTO DOC ON EXP.EXP_ID = DOC.EXP_ID "
							+ " JOIN PROCESO_INSTANCIA PIN ON DOC.PIN_ID = PIN.PIN_ID "
							+ " JOIN PROCESO_ESTADO PES ON PIN.PES_ID = PES.PES_ID " + " WHERE EXP.ACTIVO=1 ");
					sbQuery.append(" AND (EE.ESEX_NOMBRE='Abierto' OR EE.ESEX_NOMBRE='Cerrado') ");
					sbQuery.append(
							" GROUP BY EXP.EXP_ID, EXP.EXP_NOMBRE, EXP.CUANDO,EXP.DEP_ID, EXP.TRD_ID, EXP.ESEX_ID ");
					sbQuery.append("having count(doc.doc_id) = sum(PES.PES_FINAL) ");
					sbQuery.append(" ORDER BY EXP.EXP_NOMBRE desc");

					List<Expediente> expedientes = jdbcTemplate.query(sbQuery.toString(), new RowMapper<Expediente>() {
						@Override
						public Expediente mapRow(ResultSet rs, int rowNum) throws SQLException {
							Expediente exp = new Expediente();
							exp.setId(rs.getInt("EXP_ID"));
							exp.setNombre(rs.getString("EXP_NOMBRE"));
							exp.setCuando(rs.getDate("CUANDO"));
							Dependencia dependencia = new Dependencia();
							exp.setDependencia(dependencia);
							dependencia.setId(rs.getInt("DEP_ID"));
							EstadoExpediente estado = new EstadoExpediente();
							exp.setEstado(estado);
							estado.setId(rs.getInt("ESEX_ID"));
							return exp;
						}
					});
					List<Expediente> list = new ArrayList<Expediente>();
					for (Expediente e : expedientes) {
						Expediente ex = new Expediente();
						ex = repo.findOne(e.getId());
						list.add(ex);
					}

					model.addAttribute("expedientes", list);
					model.addAttribute("uid", getUsuario(principal).getId());

					return "expediente-list";

				}
			}

		} catch (Exception e) {

		}
		// Si no es jefe de una dependencia padre solo ve los expedientes de su
		// dependencia

		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		StringBuilder sbQuery = new StringBuilder();
		sbQuery.append("SELECT EXP.EXP_ID, EXP.EXP_NOMBRE, EXP.CUANDO,EXP.DEP_ID, EXP.TRD_ID, EXP.ESEX_ID, "
				+ " count(doc.doc_id), sum(PES.PES_FINAL) " + " FROM EXPEDIENTE EXP "
				+ " JOIN ESTADO_EXPEDIENTE EE ON EE.ESEX_ID = EXP.ESEX_ID "
				+ " JOIN DEPENDENCIA DEP ON EXP.DEP_ID=DEP.DEP_ID " + " JOIN DOCUMENTO DOC ON EXP.EXP_ID = DOC.EXP_ID "
				+ " JOIN PROCESO_INSTANCIA PIN ON DOC.PIN_ID = PIN.PIN_ID "
				+ " JOIN PROCESO_ESTADO PES ON PIN.PES_ID = PES.PES_ID " + " WHERE DEP.DEP_ID=");
		sbQuery.append(dependencia.getId());
		sbQuery.append(" AND  EXP.ACTIVO=1 ");
		sbQuery.append(" AND (EE.ESEX_NOMBRE='Abierto' OR EE.ESEX_NOMBRE='Cerrado') ");
		sbQuery.append(" GROUP BY EXP.EXP_ID, EXP.EXP_NOMBRE, EXP.CUANDO,EXP.DEP_ID, EXP.TRD_ID, EXP.ESEX_ID ");
		sbQuery.append("having count(doc.doc_id) = sum(PES.PES_FINAL) ");
		sbQuery.append(" ORDER BY EXP.EXP_NOMBRE desc");

		List<Expediente> expedientes = jdbcTemplate.query(sbQuery.toString(), new RowMapper<Expediente>() {
			@Override
			public Expediente mapRow(ResultSet rs, int rowNum) throws SQLException {
				Expediente exp = new Expediente();
				exp.setId(rs.getInt("EXP_ID"));
				exp.setNombre(rs.getString("EXP_NOMBRE"));
				exp.setCuando(rs.getDate("CUANDO"));
				Dependencia dependencia = new Dependencia();
				exp.setDependencia(dependencia);
				dependencia.setId(rs.getInt("DEP_ID"));
				EstadoExpediente estado = new EstadoExpediente();
				exp.setEstado(estado);
				estado.setId(rs.getInt("ESEX_ID"));
				return exp;
			}
		});
		List<Expediente> list = new ArrayList<Expediente>();
		for (Expediente e : expedientes) {
			Expediente ex = new Expediente();
			ex = repo.findOne(e.getId());
			list.add(ex);
			System.out.println("expediente id=" + ex.getId());
			System.out.println("expediente trd=" + ex.getTrd().getNombre());
		}

		model.addAttribute("expedientes", list);
		model.addAttribute("uid", getUsuario(principal).getId());

		return "expediente-list";

	}

	@RequestMapping(value = "/carpeta", method = RequestMethod.GET)
	public String carpeta(@RequestParam(value = "ser", required = false) Integer ser,
			@RequestParam(value = "sub", required = false) Integer sub, Model model, Principal principal) {
		Usuario usuario = getUsuario(principal);
		Dependencia dependencia = usuario.getDependencia();

		if (sub != null) {
			/*
			 * 2017-05-05 jgarcia@controltechcg.com Issue #63
			 * (SICDI-Controltech)
			 * 
			 * 2017-05-11 jgarcia@controltechcg.com Issue #79
			 * (SICDI-Controltech): Limitar la presentación de documentos
			 * archivados por usuario en sesión y TRD.
			 */
			List<DocumentoDependencia> docDep = documentoDependenciaRepository
					.findByQuienAndTrdIdOrderByCuandoDesc(usuario.getId(), sub);

			List<Documento> documentos = new ArrayList<Documento>();
			for (DocumentoDependencia d : docDep) {

				Documento doc = new Documento();
				doc = documentoRepository.findOne(d.getDocumento().getId());
				documentos.add(doc);

			}

			model.addAttribute("docs", documentos);

			/*
			 * 2017-05-15 jgarcia@controltechcg.com Issue #82
			 * (SICDI-Controltech) feature-82: Implementación de mapa de
			 * registros de archivos para asociación en pantalla de presentación
			 * del archivo.
			 */
			Map<String, DocumentoDependencia> registrosArchivoMapa = buildMapaRegistrosArchivo(docDep);
			model.addAttribute("registrosArchivoMapa", registrosArchivoMapa);

			Trd subserie = trdRepository.findOne(sub);
			model.addAttribute("subserie", subserie);
		} else if (ser != null) {
			List<Trd> subseries = trdRepository.findBySerie(ser, new Sort(Direction.ASC, "codigo"));
			for (Trd trd : subseries) {
				/*
				 * 2017-05-05 jgarcia@controltechcg.com Issue #63
				 * (SICDI-Controltech)
				 * 
				 * 2017-05-11 jgarcia@controltechcg.com Issue #79
				 * (SICDI-Controltech): Limitar la presentación de documentos
				 * archivados por usuario en sesión y TRD.
				 */
				List<DocumentoDependencia> docDep = documentoDependenciaRepository
						.findByQuienAndTrdIdOrderByCuandoDesc(usuario.getId(), trd.getId());

				List<Documento> documentos = new ArrayList<Documento>();
				for (DocumentoDependencia d : docDep) {

					Documento doc = new Documento();
					doc = documentoRepository.findOne(d.getDocumento().getId());
					documentos.add(doc);

				}

				trd.setDocumentos(documentos);
			}

			model.addAttribute("subseries", subseries);
			Trd serie = trdRepository.findById(ser);
			model.addAttribute("serie", serie);
		} else {
			List<Trd> trds = trdRepository.findSeriesByDependencia(dependencia.getId());

			model.addAttribute("series", trds);
		}

		return "expediente-carpeta";
	}

	/**
	 * Construye un mapara de los registros de archivo, utilizando como llave el
	 * ID del documento asociado.
	 * 
	 * @param registros
	 *            Lista de registros de archivo.
	 * @return Mapa de registros de archivo.
	 */
	// 2017-05-15 jgarcia@controltechcg.com Issue #82 (SICDI-Controltech)
	// feature-82
	private Map<String, DocumentoDependencia> buildMapaRegistrosArchivo(List<DocumentoDependencia> registros) {
		Map<String, DocumentoDependencia> map = new LinkedHashMap<>();

		for (DocumentoDependencia registro : registros) {
			map.put(registro.getDocumento().getId(), registro);
		}

		return map;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@Transactional
	public String updateExpediente(@RequestParam("eid") Integer eid, Model model, Principal principal) {

		Expediente expediente = repo.findOne(eid);
		repo.save(expediente);

		return "redirect:/list";
	}

	public Trd getTrd(String cod) {
		return trdRepository.findByCodigo(cod);
	}

	@Override
	public String nombre(Integer id) {
		return super.nombre(id);
	}

	@RequestMapping(value = "/expedientes-dependencia", method = RequestMethod.GET)
	public String expedientesDependencia(@RequestParam("did") String did, @RequestParam("eid") Integer eid, Model model,
			Principal principal) {

		Dependencia dependencia = getUsuario(principal).getDependencia();

		Documento doc = documentoRepository.getOne(did);

		model.addAttribute("docId", doc.getId());
		model.addAttribute("eid", eid);

		model.addAttribute("expDoc", doc.getExpediente().getId());

		List<Expediente> expedientes = repo.findAllOthers(dependencia.getId(), doc.getExpediente().getId());

		if (expedientes.size() < 1)
			model.addAttribute("expMessage",
					"No existen más expedientes en su dependencia. Para crear un expediente, use el módulo de administración");
		else {

			model.addAttribute("expedientes", expedientes);

		}
		return "expediente-list-dependencias";

	}

	// Deja el expediente en estado cerrado
	@RequestMapping(value = "/cerrar", method = RequestMethod.POST)
	public String closeExpediente(@RequestParam("eid") Integer eid, Model model, Principal principal) {

		Expediente expediente = repo.findOne(eid);

		EstadoExpediente estado = estadoExpedienteRepository.getByNombre("Cerrado");

		ExpedienteEstado ee = expedienteEstadoRepository.findByExpediente(expediente);
		// Se guarda el registro del expediente con su nuevo estado en la tabla
		// de control
		// ExpedienteEstado

		try {
			if (ee != null) {
				ee.setExpediente(expediente);
				ee.setEstado(estado);
				ee.setUsuarioTransferencia(getUsuario(principal));
				ee.setFechaTransferencia(new Date());
				expedienteEstadoRepository.save(ee);
			} else {
				ExpedienteEstado expedienteEstado = new ExpedienteEstado();
				expedienteEstado.setExpediente(expediente);
				expedienteEstado.setEstado(estado);
				expedienteEstado.setUsuarioTransferencia(getUsuario(principal));
				expedienteEstado.setFechaTransferencia(new Date());
				expedienteEstadoRepository.save(expedienteEstado);

			}

		} catch (Exception e) {

			LOG.error("Cerrando el expediente", e);
			model.addAttribute(AppConstants.FLASH_ERROR, "Ocurrió un error inesperado: " + e.getMessage());

			return "expediente-list";

		}

		expediente.setEstado(estado);
		repo.save(expediente);

		return String.format("redirect:%s/list", ExpedienteController.PATH);

	}

	// Archiva el expediente en el archivo central
	@RequestMapping(value = "/archivar", method = RequestMethod.POST)
	public String archivarExpediente(@RequestParam("eid") Integer eid, Model model, Principal principal) {

		Expediente expediente = repo.findOne(eid);

		EstadoExpediente estado = estadoExpedienteRepository.getByNombre("Archivado");

		ExpedienteEstado ee = expedienteEstadoRepository.findByExpediente(expediente);

		try {
			if (estado != null && ee != null) {

				ee.setExpediente(expediente);
				ee.setEstado(estado);
				ee.setUsuarioTransferencia(getUsuario(principal));
				ee.setFechaTransferencia(new Date());
				expedienteEstadoRepository.save(ee);
				expediente.setEstado(estado);
				repo.save(expediente);

			}

		} catch (Exception e) {

			LOG.error("Archivando el expediente", e);
			model.addAttribute(AppConstants.FLASH_ERROR, "Ocurrió un error inesperado: " + e.getMessage());

			return "expediente-list";

		}

		return String.format("redirect:%s/list", ExpedienteController.PATH);
	}

	@RequestMapping(value = "/expediente-vacio", method = RequestMethod.GET)
	public String expedienteVacio(Model model, Principal principal) {

		return "expediente-vacio";
	}

}
