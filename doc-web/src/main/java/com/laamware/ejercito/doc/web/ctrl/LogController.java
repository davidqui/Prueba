package com.laamware.ejercito.doc.web.ctrl;

import static com.laamware.ejercito.doc.web.ctrl.UtilController.ADMIN_PAGE_SIZE;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Log;
import com.laamware.ejercito.doc.web.repo.LogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Controller
@PreAuthorize("hasRole('ADMIN_LOG')")
@RequestMapping(LogController.PATH)
public class LogController extends UtilController{

	@Autowired
	LogRepository logRepository;

	static final String PATH = "/admin/logs";

	@PreAuthorize("hasRole('ADMIN_LOG')")
	@RequestMapping(value = { "" }, method = RequestMethod.GET)
	public String listarUsuarios(Model model) {
		model.addAttribute("list", Collections.EMPTY_LIST);
		return "log-list";
	}
	
        /**
        * 2018-09-24 samuel.delgado@controltechcg.com Issue #174 (SICDI-Controltech)
        * feature-174: Adición para la paginación.
        */
	@PreAuthorize("hasRole('ADMIN_LOG')")
	@RequestMapping(value = "/usuario", method = RequestMethod.GET)
	public String listarLogPorUsuario(@RequestParam("usuario") String usuario,
                @RequestParam(value = "filtro", required = false, defaultValue = "") String filtro,
                @RequestParam(value = "pageIndex", required = false) Integer page,
                @RequestParam(value = "pageSize", required = false) Integer pageSize,
                Model model) {
            
                if (page == null || page < 0)
                    page = 1;
                if (pageSize == null || pageSize < 0)
                    pageSize = ADMIN_PAGE_SIZE;
		
                Pageable pageable = new PageRequest(page-1, pageSize, Sort.Direction.ASC, "cuando");

                Page<Log> list = null;
                model.addAttribute("totalPages", 0);
		if( usuario == null || usuario.trim().length() == 0 ){
                    model.addAttribute("list", Collections.EMPTY_LIST);
		}else{
                    list = logRepository.findAllLogLogByUserOnly( usuario.trim(), filtro.toLowerCase(), pageable);
                    Long count = list.getTotalElements();
                    adminPageable(count, model, page, pageSize);
                    model.addAttribute("list", list.getContent());
		}
                model.addAttribute("usuario", usuario);
                model.addAttribute("filtro", filtro);
		return "log-list";
	}	

	@ModelAttribute("descriptor")
	GenDescriptor getDescriptor() {
		
		GenDescriptor d = GenDescriptor.find(Log.class);
		
		return d;
	}

	@ModelAttribute("activePill")
	public String getActivePill() {
		return "log";
	}

	@ModelAttribute("templatePrefix")
	protected String getTemplatePrefix() {
		return "admin";
	}

}
