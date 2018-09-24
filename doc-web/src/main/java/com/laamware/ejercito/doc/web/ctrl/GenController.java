package com.laamware.ejercito.doc.web.ctrl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.AuditActivoCreateSupport;
import com.laamware.ejercito.doc.web.entity.AuditActivoModifySupport;
import com.laamware.ejercito.doc.web.entity.Clasificacion;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.GenPropDescriptor;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.repo.GenJpaRepository;
import com.laamware.ejercito.doc.web.serv.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public abstract class GenController<ET, IDT extends Serializable, RT extends GenJpaRepository<ET, IDT>>
		extends UtilController {

	public static class MapWrapper<K, E> {
		private Map<K, E> map;

		public MapWrapper(Map<K, E> map) {
			this.map = map;
		}

		public E get(K k) {
			return map.get(k);
		}
	}
        
        //issue-179 constante llave del cache clasificacion
        public final static String CLAS_CACHE_KEY = "clasificacion";

        //issue-179 constante llave del cache trd
        public final static String TRD_CACHE_KEY = "trd";
    
        /*
         * 2018-07-11 samuel.delgado@controltechcg.com Issue #179 (SICDI-Controltech)
         * feature-179: Servicio de cache.
         */
        @Autowired
        private CacheService cacheService;
    

	Map<String, String> queryStringMap = new HashMap<String, String>();

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true, 10));
	}

	abstract RT getRepository();

	@ModelAttribute("path")
	abstract String getPath();

	@ModelAttribute("descriptor")
	abstract GenDescriptor getDescriptor();

	@ModelAttribute("queryString")
	protected String getQueryString(HttpServletRequest req) {
		processRequest(req);
		boolean first = true;
		StringBuilder b = new StringBuilder();
		for (Entry<String, String> x : queryStringMap.entrySet()) {
			if (!first)
				b.append("&");
			b.append(x.getKey()).append("=").append(x.getValue());
			first = false;
		}
		if (b.length() > 0)
			return b.toString();
		else
			return null;
	}

	@ModelAttribute("lists")
	protected MapWrapper<String, Object> getLists() {
		HashMap<String, Object> m = new HashMap<String, Object>();
		registerLists(m);
		MapWrapper<String, Object> w = new MapWrapper<String, Object>(m);
		return w;
	}

	@ModelAttribute("returnUrl")
	protected String getReturnUrl(HttpServletRequest req) {
		return returnUrl(req);
	}

	protected String returnUrl(HttpServletRequest req) {
		return null;
	}

	protected void registerLists(Map<String, Object> map) {

	}

	protected void processRequest(HttpServletRequest req) {

	}

	protected void preSave(ET e) {

	}

	protected void preSaveWithFiles(ET e, MultipartFile archivo) {

	}

	protected void postSave(ET e) {

	}

	@SuppressWarnings("unchecked")
	protected IDT getId(ET e) {
		GenDescriptor d = getDescriptor();
		if (d.idProperties().size() != 1) {
			throw new RuntimeException("La clase tiene ninguno o más de un campo marcado con @Id");
		}
		GenPropDescriptor p = d.idProperties().get(0);
		return (IDT) p.value(e);
	}

	@SuppressWarnings("unchecked")
	protected IDT getId(String value) {
		GenDescriptor d = getDescriptor();
		if (d.idProperties().size() != 1) {
			throw new RuntimeException("La clase tiene ninguno o más de un campo marcado con @Id");
		}
		GenPropDescriptor p = d.idProperties().get(0);
		String type = p.getType();
		if ("Integer".equals(type)) {
			return (IDT) new Integer(value);
		} else if ("Long".equals(type)) {
			return (IDT) new Long(value);
		} else if ("String".equals(type)) {
			return (IDT) value;
		} else {
			throw new RuntimeException(
					"La clave primaria no se puede convertir desde String. Sobre escriba el método getId(String value)");
		}
	}

	@ModelAttribute("templatePrefix")
	protected String getTemplatePrefix() {
		return null;
	}

	protected List<ET> findAll() {
		return getRepository().findAll();
	}

	protected List<ET> findAll(boolean all) {
		if (!all) {
			return getRepository().findByActivo(true);
		} else {
			return findAll();
		}
	}
        
        /**
        * 2018-09-24 samuel.delgado@controltechcg.com Issue #174 (SICDI-Controltech)
        * feature-174: Adición para la paginación.
        */
	protected Page<ET> findAll(boolean all, Pageable pageable) {
		if (!all) {
			return getRepository().findByActivo(true, pageable);
		} else {
			return getRepository().findAll(pageable);
		}
	}
        /**
        * 2018-09-24 samuel.delgado@controltechcg.com Issue #174 (SICDI-Controltech)
        * feature-174: Adición para la paginación.
        */
	@RequestMapping(value = { "" }, method = RequestMethod.GET)
	public String list(@RequestParam(value = "all", required = false, defaultValue = "false") Boolean all,
                        @RequestParam(value = "pageIndex", required = false) Integer page,
                        @RequestParam(value = "pageSize", required = false) Integer pageSize,
			Model model) {
            
                if (page == null || page < 0)
                    page = 1;
                if (pageSize == null || pageSize < 0)
                    pageSize = ADMIN_PAGE_SIZE;
                
                Pageable pageable = new PageRequest(page-1, pageSize);

		Page<ET> list = findAll(all, pageable);
                adminPageable(list.getTotalElements(), model, page, pageSize);
		model.addAttribute("list", list.getContent());
		model.addAttribute("all", all);
                
		// 2017-02-13 jgarcia@controltechcg.com Issue #49.
		if (useNoCreateList()) {
			return "gen-list-no-create";
		}

		return "gen-list";
	}

	/**
	 * Indica si el controlador presenta su listado utilizando un template que
	 * no presenta la opción de creación.
	 * 
	 * @return {@code true} si ha de utilizar el template de lista sin creación;
	 *         de lo contrario, {@code false}.
	 */
	// 2017-02-13 jgarcia@controltechcg.com Issue #49.
	protected boolean useNoCreateList() {
		return false;
	}

	@RequestMapping(value = { "/{id}" }, method = RequestMethod.GET)
	public String read(IDT id, Model model) {
		ET e = getRepository().getOne(id);
		model.addAttribute("entity", e);
		return "gen-read";
	}

	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(Model model, HttpServletRequest req) {
		GenDescriptor descriptor = getDescriptor();
		if (descriptor.idProperties().size() != 1) {
			throw new RuntimeException(
					"La clase tiene ninguno o más de un campo marcado con @Id. Por lo tanto no se puede usar el método de edición genérico");
		}
		GenPropDescriptor p = descriptor.idProperties().get(0);
		String idStr = req.getParameter(p.getName());
		IDT id = getId(idStr);
		ET e = getRepository().getOne(id);
		model.addAttribute("entity", e);
		return "gen-edit";
	}

	@RequestMapping(value = { "/create" }, method = RequestMethod.GET)
	public String create(Model model) {
		return "gen-create";
	}

	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String save(@Valid ET e, BindingResult eResult, Model model, RedirectAttributes redirect,
			MultipartFile archivo) {
		model.addAttribute("entity", e);
		try {
			preSave(e);

			preSaveWithFiles(e, archivo);

			getRepository().save(e);
			postSave(e);
                        /*
                        * 2018-07-11 samuel.delgado@controltechcg.com Issue #179 (SICDI-Controltech)
                        * feature-179: se elimina el cache de dependencias.
                        */
                        if (e instanceof Clasificacion) {
                            cacheService.deleteKeyCache(CLAS_CACHE_KEY);
                        }
                        if (e instanceof Trd) {
                            System.out.println("MODI Trd");
                            cacheService.deleteKeyCache(TRD_CACHE_KEY);
                        }
			redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
			return "redirect:" + getPath() + "?" + model.asMap().get("queryString");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
			if (getId(e) != null) {
				return "gen-edit";
			} else {
				return "gen-create";
			}
		}
	}

	@RequestMapping(value = { "/delete" }, method = RequestMethod.GET)
	public String delete(Model model, HttpServletRequest req, RedirectAttributes redirect) {
		GenDescriptor descriptor = getDescriptor();
		if (descriptor.idProperties().size() != 1) {
			throw new RuntimeException(
					"La clase tiene ninguno o más de un campo marcado con @Id. Por lo tanto no se puede usar el método de eliminación genérico");
		}
		GenPropDescriptor p = descriptor.idProperties().get(0);
		String idStr = req.getParameter(p.getName());
		IDT id = getId(idStr);
		try {
			ET e = getRepository().getOne(id);
			if (AuditActivoModifySupport.class.isAssignableFrom(e.getClass())) {
				AuditActivoModifySupport au = (AuditActivoModifySupport) e;
				au.setActivo(false);
				getRepository().save(e);
			} else if (AuditActivoCreateSupport.class.isAssignableFrom(e.getClass())) {
				AuditActivoCreateSupport au = (AuditActivoCreateSupport) e;
				au.setActivo(false);
				getRepository().save(e);
			} else {
				getRepository().delete(e);
			}
                        /*
                        * 2018-07-11 samuel.delgado@controltechcg.com Issue #179 (SICDI-Controltech)
                        * feature-179: se elimina el cache de dependencias.
                        */
                        if (e instanceof Clasificacion) {
                            cacheService.deleteKeyCache(CLAS_CACHE_KEY);
                        }
                        if (e instanceof Trd) {
                            System.out.println("MODI Trd");
                            cacheService.deleteKeyCache(TRD_CACHE_KEY);
                        }
                        
			redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro eliminado con éxito");
		} catch (Exception ex) {
			ex.printStackTrace();
			redirect.addFlashAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
		}
		return "redirect:" + getPath() + "?" + model.asMap().get("queryString");
	}

	protected <T> GenDescriptor reflectDescriptor(Class<T> clazz) {
		GenDescriptor d = GenDescriptor.find(clazz);
		return d;
	}

}
