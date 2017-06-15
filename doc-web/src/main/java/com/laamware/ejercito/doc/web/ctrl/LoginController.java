package com.laamware.ejercito.doc.web.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
	String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, HttpServletRequest req, Model model) {
		/*
		 * 2017-06-15 jgarcia@controltechcg.com Issue #23 (SICDI-Controltech)
		 * hotfix-23: Banderas en proceso de login para soporte y depuración.
		 */
		// System.out.println("LoginController.login()");
		// System.out.println("error=" + error);
		// System.out.println("logout=" + logout);

		if (error != null) {
			model.addAttribute("error", "1");
		}
		if (logout != null) {
			model.addAttribute("logout", "1");
		}

		checkCompatibleBrowser(req, model);

		return "login";
	}

	@RequestMapping(value = "/access-denied", method = RequestMethod.GET)
	String accessDenied() {
		return "security-denied";
	}

	/*
	 * @RequestMapping(value = {"","/" ,"/invitacion"}, method =
	 * RequestMethod.GET) String invitacion() {
	 * 
	 * Authentication aut =
	 * SecurityContextHolder.getContext().getAuthentication(); if( aut != null
	 * && aut.isAuthenticated() && aut.getPrincipal() != null &&
	 * !aut.getPrincipal().toString().equals("anonymousUser") ){ return
	 * "bandeja-entrada"; } return "invitacion"; }
	 */

	void checkCompatibleBrowser(HttpServletRequest req, Model model) {
		String userAgent = req.getHeader("User-Agent");
		if (StringUtils.isBlank(userAgent)) {
			model.addAttribute("compatibleBrowser", false);
		} else {
			model.addAttribute("compatibleBrowser", userAgent.contains("Firefox"));
		}
	}
}
