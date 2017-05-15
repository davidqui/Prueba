package com.laamware.ejercito.doc.web.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/security")
public class SecurityController {

	@RequestMapping(value = "/nouser", method = RequestMethod.GET)
	public String nouser(Model model) {

		return "security-nouser";
	}
	
	@RequestMapping(value = "/nodependencia", method = RequestMethod.GET)
	public String nodependencia(Model model) {

		return "security-ldapconf";
	}

}
