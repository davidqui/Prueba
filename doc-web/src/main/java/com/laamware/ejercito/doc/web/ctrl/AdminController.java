package com.laamware.ejercito.doc.web.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController extends UtilController {

	@RequestMapping("")
	public String rootAdmin(Model model) {
		String pathAdmin = getPathAdmin();
		return pathAdmin;

	}
	
	@RequestMapping("/external-entities")
	public String externalEntities(Model model) {
		return "admin-external-entities";
	}

	@RequestMapping("/receiver-types")
	public String receiverTypes(Model model) {
		return "admin-receiver-types";
	}

	@RequestMapping("/receivers")
	public String receivers(Model model) {
		return "admin-receivers";
	}

	@RequestMapping("/keywords")
	public String keywords(Model model) {
		return "admin-keywords";
	}

	@RequestMapping("/reports")
	public String reports(Model model) {
		return "admin-reports";
	}

	@RequestMapping("/signatures")
	public String signatures(Model model) {
		return "admin-signatures";
	}

	@RequestMapping("/document-types")
	public String documentTypes(Model model) {
		return "admin-document-types";
	}

	@RequestMapping("/central-archive")
	public String centralArchive(Model model) {
		return "admin-central-archive";
	}

	@RequestMapping("/terms")
	public String terms(Model model) {
		return "admin-terms";
	}

	@RequestMapping("/audit-security")
	public String auditSecurity(Model model) {
		return "admin-audit-security";
	}

}
