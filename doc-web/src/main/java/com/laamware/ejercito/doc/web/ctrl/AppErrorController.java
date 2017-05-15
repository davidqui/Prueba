package com.laamware.ejercito.doc.web.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/*
* 2017-03-28 jgarcia@controltechcg.com Issue #14 (SIGDI-Controltech): Implementación de controlador para presentación de mensajes de error con el estilo de la aplicación.
*/
@Controller
public class AppErrorController implements ErrorController {

	private static final String PATH = "/error";

	@RequestMapping(value = PATH)
	public String error(HttpServletRequest request, Model model) {
		int statusCode = (int) request.getAttribute("javax.servlet.error.status_code");
		model.addAttribute("status", statusCode);

		String statusMessage;
		switch (statusCode) {
		case 401:
			statusMessage = "No tiene autorización para acceder al recurso solicitado. Por favor, notificar al administrador del sistema.";
			break;
		case 404:
			statusMessage = "El recurso solicitado no existe o no se encuentra disponible.";
			break;
		case 500:
			statusMessage = "Se ha presentado un error interno en la aplicación. Por favor, notificar al administrador del sistema.";
			break;
		default:
			statusMessage = null;
			break;
		}
		model.addAttribute("statusMessage", statusMessage);

		Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
		if (exception != null) {
			String exceptionMessage = exception.getMessage();
			model.addAttribute("exceptionMessage", exceptionMessage);
		}

		return "error-app";
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}

}
