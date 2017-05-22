package com.laamware.ejercito.doc.web.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.laamware.ejercito.doc.web.ctrl.GenController.MapWrapper;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.DependenciaTrd;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.repo.DependenciaTrdRepository;
import com.laamware.ejercito.doc.web.repo.TrdRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;

@Controller
@RequestMapping(DependenciaController.PATH)
public class DependenciaController extends UtilController {

	static final String PATH = "/dependencias";

	@Autowired
	DependenciaRepository dependenciaRepository;

	@Autowired
	TrdRepository trdRepository;

	@Autowired
	DependenciaTrdRepository dependenciaTrdRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@PreAuthorize("hasRole('ADMIN_DEPENDENCIAS')")
	@RequestMapping(value = { "" }, method = RequestMethod.GET)
	public String list(@RequestParam(value = "all", required = false, defaultValue = "false") Boolean all,
			Model model) {

		List<Dependencia> list = findAll(all);
		model.addAttribute("list", list);
		model.addAttribute("all", all);
		return "dependencia-list";
	}

	@PreAuthorize("hasRole('ADMIN_DEPENDENCIAS')")
	@RequestMapping(value = { "/create" }, method = RequestMethod.GET)
	public String create(Model model) {
		return "dependencia-create";
	}

	protected List<Dependencia> findAll(boolean all) {
		if (!all) {
			return dependenciaRepository.findByActivo(true);
		} else {
			return dependenciaRepository.findAll();
		}
	}

	@PreAuthorize("hasRole('ADMIN_DEPENDENCIAS')")
	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(Model model, HttpServletRequest req) {
		Integer id = Integer.valueOf(req.getParameter("id"));
		Dependencia dependencia = dependenciaRepository.findOne(id);
		model.addAttribute("entity", dependencia);
		return "dependencia-edit";
	}

	@PreAuthorize("hasRole('ADMIN_DEPENDENCIAS')")
	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String save(@Valid Dependencia e, HttpServletRequest req, BindingResult eResult, Model model,
			RedirectAttributes redirect, MultipartFile archivo) {

		try {
			// preSave(e);

			String idS = req.getParameter("id");
			if (idS != null && idS.trim().length() > 0) {
				e.setId(Integer.parseInt(idS.trim()));
			}

			model.addAttribute("entity", e);

			if (e.getCodigo() == null || e.getCodigo().trim().length() == 0) {
				model.addAttribute(AppConstants.FLASH_ERROR, "El código de la dependencia es requerido");
				if (e.getId() != null) {
					return "dependencia-edit";
				} else {
					return "dependencia-create";
				}
			}

			if (e.getNombre() == null || e.getNombre().trim().length() == 0) {
				model.addAttribute(AppConstants.FLASH_ERROR, "El nombre de la dependencia es requerido");
				if (e.getId() != null) {
					return "dependencia-edit";
				} else {
					return "dependencia-create";
				}
			}

			if (e.getJefeEncargado() != null && e.getJefeEncargado().getId() != null) {

				if (e.getFchInicioJefeEncargado() == null || e.getFchFinJefeEncargado() == null) {
					model.addAttribute(AppConstants.FLASH_ERROR,
							"La fecha de inicio segundo comandante, está vacía. Por favor ingrese una fecha válida");
					return "dependencia-edit";
				}
				if (e.getFchInicioJefeEncargado().compareTo(e.getFchFinJefeEncargado()) >= 0) {
					model.addAttribute(AppConstants.FLASH_ERROR,
							"La fecha de inicio segundo comandante, no puede ser MAYOR a la fecha de fin segundo comandante");
					return "dependencia-edit";
				}

			}

			try {

				dependenciaRepository.save(e);

			} catch (Exception e2) {

				if (e2 instanceof org.springframework.dao.DataIntegrityViolationException) {

					model.addAttribute(AppConstants.FLASH_ERROR,
							"Ya existe una dependencia con el Código LDAP ingresado. Un Código LDAP puede estar asociado a una única dependencia");

					if (e.getId() != null) {
						return "dependencia-edit";
					} else {
						return "dependencia-create";
					}
				}
				throw e2;
			}

			// postSave(e);
			redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
			return "redirect:" + PATH + "?" + model.asMap().get("queryString");
		} catch (Exception ex) {
			ex.printStackTrace();
			redirect.addFlashAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
			if (e.getId() != null) {
				return "dependencia-edit";
			} else {
				return "dependencia-create";
			}
		}
	}

	@PreAuthorize("hasRole('ADMIN_DEPENDENCIAS')")
	@RequestMapping(value = { "/delete" }, method = RequestMethod.GET)
	public String delete(Model model, HttpServletRequest req, RedirectAttributes redirect) {
		Integer id = Integer.valueOf(req.getParameter("id"));
		try {
			Dependencia dep = dependenciaRepository.findOne(id);
			dep.setActivo(false);
			dependenciaRepository.save(dep);
			model.addAttribute(AppConstants.FLASH_SUCCESS, "Dependencia eliminada con éxito");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
		}
		return "redirect:" + PATH;
	}

	protected void registerLists(Map<String, Object> map) {
		map.put("usuarios", usuarioRepository.findAll(new Sort(Direction.ASC, "grado", "nombre")));
	}

	
	/**
	 * Carga el listado de dependencias al modelo
	 * 
	 * @return
	 */
	/*
	 * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS: Ordenamiento por peso.
	 * Modificación: variable y orden en que se presentan las dependencias.
	 */
	@ModelAttribute("dependencias")
	public List<Dependencia> dependencias(Model model) {
		List<Dependencia> list = dependenciaRepository.findByActivo(true, new Sort(Direction.ASC, "pesoOrden", "nombre"));
		model.addAttribute("dependencias", list);
		return list;
	}

	@ModelAttribute("lists")
	protected MapWrapper<String, Object> getLists() {
		HashMap<String, Object> m = new HashMap<String, Object>();
		registerLists(m);
		MapWrapper<String, Object> w = new MapWrapper<String, Object>(m);
		return w;
	}

	@ModelAttribute("descriptor")
	GenDescriptor getDescriptor() {
		GenDescriptor d = GenDescriptor.find(Dependencia.class);
		d.addAction("Subseries", PATH + "/trds-subseries", new String[] { "id" }, new String[] { "depId" });
		return d;
	}

	@ModelAttribute("activePill")
	public String getActivePill() {
		return "dependencia";
	}

	@ModelAttribute("templatePrefix")
	protected String getTemplatePrefix() {
		return "admin";
	}

	@PreAuthorize("hasRole('ADMIN_DEPENDENCIAS')")
	@RequestMapping(value = "/trds-subseries", method = RequestMethod.GET)
	public String dependenciaTrd(@RequestParam("depId") Integer depId, Model model) {

		Dependencia dep = dependenciaRepository.findOne(depId);
		List<Trd> trds = trdRepository.findByActivoAndSerieNotNull(true, (new Sort(Direction.ASC, "nombre")));

		model.addAttribute("dependencia", dep);
		model.addAttribute("trds", trds);
		model.addAttribute("controller", this);

		return "admin-dependencia-trds";
	}

	@PreAuthorize("hasRole('ADMIN_DEPENDENCIAS')")
	@RequestMapping(value = "/trds-subseries", method = RequestMethod.POST)
	public String roles(@RequestParam("depId") Integer depId, @RequestParam("trd") Integer[] trds, Model model) {

		Dependencia dep = dependenciaRepository.findOne(depId);

		for (DependenciaTrd dtrd : dep.getTrds()) {
			Integer current = dtrd.getTrd().getId();
			boolean found = false;
			for (Integer t : trds) {
				if (t.equals(current)) {
					found = true;
					break;
				}
			}
			if (!found) {
				dependenciaTrdRepository.delete(dtrd);
			}
		}

		for (Integer t : trds) {
			boolean found = false;
			for (DependenciaTrd dtrd : dep.getTrds()) {
				if (t.equals(dtrd.getTrd().getId())) {
					found = true;
					break;
				}
			}
			if (!found) {
				DependenciaTrd newdtrd = new DependenciaTrd();
				newdtrd.setDependencia(dep);
				Trd trd = new Trd();
				trd.setId(t);
				newdtrd.setTrd(trd);
				dependenciaTrdRepository.save(newdtrd);
			}
		}

		return String.format("redirect:%s", PATH);
	}

	public boolean has(Integer trdId, List<DependenciaTrd> trdLinks) {
		for (DependenciaTrd dependenciaTrd : trdLinks) {
			if (dependenciaTrd.getTrd().getId().equals(trdId))
				return true;
		}
		return false;
	}

	/**
	 * Agrega el controlador
	 */
	@ModelAttribute("controller")
	public DependenciaController controller() {
		return this;
	}

	/**
	 * Retorna el nombre de la dependencia Padre
	 * 
	 * @param idPadre
	 * @return
	 */
	public String nombreDependenciaPadre(String idPadre) {
		try {
			if (idPadre != null && idPadre.trim().length() > 0) {

				Dependencia depPadre = dependenciaRepository.findOne(Integer.valueOf(idPadre.replaceAll("\\.", "")));
				if (depPadre != null)
					return depPadre.getNombre();
				else
					return "";
			}
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return idPadre.toString();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/dependencias.json", method = RequestMethod.GET)
	public List<Map<String, String>> dependencias(@RequestParam(value = "id", required = false) Integer id) {
		List<Map<String, String>> mapList = new ArrayList<>();
		List<Dependencia> dependencias;

		if (id != null) {
			/*
			 * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS: Ordenamiento por peso.
			 * Modificación: variable y orden en que se presentan las dependencias.
			 */
			dependencias = dependenciaRepository.findByActivoAndPadre(true, id, new Sort(Direction.ASC, "pesoOrden", "nombre"));
		} else {
			/*
			 * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS: Ordenamiento por peso.
			 * Modificación: variable y orden en que se presentan las dependencias.
			 */
			dependencias = dependenciaRepository.findByActivoAndPadreIsNull(true, new Sort(Direction.ASC, "pesoOrden", "nombre"));
		}

		for (Dependencia dependencia : dependencias) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", dependencia.getId().toString());
			map.put("nombre", dependencia.getSiglaNombre());
			mapList.add(map);
		}

		return mapList;
	}
}