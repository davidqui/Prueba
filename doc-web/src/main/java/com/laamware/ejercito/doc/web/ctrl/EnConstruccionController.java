package com.laamware.ejercito.doc.web.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(EnConstruccionController.PATH)
public class EnConstruccionController extends UtilController {

	public static final String PATH = "/enconstruccion";

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index() {
		return "enconstruccion-index";
	}
	
}
