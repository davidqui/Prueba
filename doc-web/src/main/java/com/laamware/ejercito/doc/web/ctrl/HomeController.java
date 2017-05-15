package com.laamware.ejercito.doc.web.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController extends UtilController {

	@RequestMapping(value = { "", "/" })
	public String index(RedirectAttributes redirect, Model model) {
		byPassFlassAttributes(redirect, model);
		return String.format("redirect:%s/entrada", BandejaController.PATH);
	}

}
