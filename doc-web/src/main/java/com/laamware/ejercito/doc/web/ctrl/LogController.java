package com.laamware.ejercito.doc.web.ctrl;

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
	
	@PreAuthorize("hasRole('ADMIN_LOG')")
	@RequestMapping(value = "/usuario", method = RequestMethod.GET)
	public String listarLogPorUsuario(@RequestParam("usuario") String usuario, Model model) {
		
		if( usuario == null || usuario.trim().length() == 0 ){
			model.addAttribute("list", Collections.EMPTY_LIST);
		}else{
			model.addAttribute("list", logRepository.findAllLogLogByUserOnly( usuario.trim() ));
		}
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
