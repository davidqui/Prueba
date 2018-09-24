package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Grados;
import com.laamware.ejercito.doc.web.repo.GradosRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Controller
@PreAuthorize("hasRole('ADMIN_GRADOS')")
@RequestMapping(GradosController.PATH)
public class GradosController extends UtilController {

	static final String PATH = "/grados";

	@Autowired
	GradosRepository gradosRepository;

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
            
                Pageable pageable = new PageRequest(page-1, pageSize, Sort.Direction.DESC, "pesoOrden");

                Page<Grados> list = findAll(all, pageable);
                
                Long count = list.getTotalElements();
                adminPageable(count, model, page, pageSize);
                model.addAttribute("list", list.getContent());
                model.addAttribute("all", all);
                
		return "grados-list";
	}

	@RequestMapping(value = { "/create" }, method = RequestMethod.GET)
	public String create(Model model) {
		Grados grado = new Grados();
		model.addAttribute("grado", grado);
		return "grados-create";
	}

	protected Page<Grados> findAll(boolean all, Pageable pageable) {
		if (!all) {
			return gradosRepository.findByActivo(true, pageable);
		} else {
			return gradosRepository.findAll(pageable);
		}
	}

	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(Model model, HttpServletRequest req) {
		String id = req.getParameter("id");
		Grados grado = gradosRepository.findOne(id);
		model.addAttribute("grado", grado);
		return "grados-create";
	}

	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String save(@Valid Grados e, BindingResult eResult, Model model, RedirectAttributes redirect,
			MultipartFile archivo) {
		model.addAttribute("grado", e);
		try {
			// preSave(e);

			// preSaveWithFiles(e, archivo);

			gradosRepository.save(e);
			// postSave(e);
			redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
			return "redirect:" + PATH + "?" + model.asMap().get("queryString");
		} catch (Exception ex) {
			ex.printStackTrace();
			redirect.addFlashAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
			if (e.getId() != null) {
				return "grados-create";
			} else {
				return "grados-create";
			}
		}
	}

	@RequestMapping(value = { "/delete" }, method = RequestMethod.GET)
	public String delete(Model model, HttpServletRequest req, RedirectAttributes redirect) {
		String id = req.getParameter("id");
		try {
			Grados grados = gradosRepository.findOne(id);
			grados.setActivo(false);
			gradosRepository.save(grados);
			model.addAttribute(AppConstants.FLASH_SUCCESS, "Dependencia eliminada con éxito");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
		}
		return "redirect:" + PATH;
	}



	@ModelAttribute("descriptor")
	GenDescriptor getDescriptor() {
		GenDescriptor d = GenDescriptor.find(Grados.class);
		return d;
	}

	@ModelAttribute("activePill")
	public String getActivePill() {
		return "grados";
	}

	@ModelAttribute("templatePrefix")
	protected String getTemplatePrefix() {
		return "admin";
	}



	/**
	 * Agrega el controlador
	 */
	@ModelAttribute("controller")
	public GradosController controller() {
		return this;
	}


}
