package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.serv.DocumentService;

@Controller
@RequestMapping("/inbox")
public class InboxController {

	@Autowired
	DocumentService documentService;

	@Autowired
	public InboxController(DocumentService documentService) {
		this.documentService = documentService;
	}

	@RequestMapping("/in")
	String in(Model model) {

		List<Map<String, Object>> list = documentService.search();

		model.addAttribute("list", list);

		return "inbox-in";
	}

	@RequestMapping("/sent")
	String sent(Model model) {

		List<Map<String, Object>> list = documentService.search();

		model.addAttribute("list", list);

		return "inbox-sent";
	}

	@RequestMapping("/pending-send")
	String pendingSend(Model model) {

		List<Map<String, Object>> list = documentService.search();

		model.addAttribute("list", list);

		return "inbox-pending-send";
	}

	@RequestMapping("/pending-input")
	String pendingInput(Model model) {

		List<Map<String, Object>> list = documentService.search();

		model.addAttribute("list", list);

		return "inbox-pending-input";
	}

	@RequestMapping("/history")
	String history(Model model) {

		List<Map<String, Object>> list = documentService.search();

		model.addAttribute("list", list);

		return "inbox-history";
	}

	@RequestMapping("/pending-signature")
	String pendingSignature(Model model) {

		List<Map<String, Object>> list = documentService.search();

		model.addAttribute("list", list);

		return "inbox-pending-signature";
	}

}
