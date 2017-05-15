package com.laamware.ejercito.doc.web.ctrl;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.laamware.ejercito.doc.web.entity.PrestamoSolicitud;

@Component
public class PrestamoSolicitudValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return PrestamoSolicitud.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		PrestamoSolicitud prestamoSolicitud = (PrestamoSolicitud) target;

		if (prestamoSolicitud.getUsuarioSolicita() == null
				|| prestamoSolicitud.getUsuarioSolicita().getId() == null) {
			errors.rejectValue("usuarioSolicita",
					"prestamoSolicitud.usuarioSolicita.empty");
		}
		if (prestamoSolicitud.getFechaSolicitud() == null) {
			errors.rejectValue("fechaSolicitud",
					"prestamo.fechaSolicitud.empty");
		}
		if (prestamoSolicitud.getUsuarioSolicita() == null
				|| prestamoSolicitud.getUsuarioSolicita().getId() == null) {
			errors.rejectValue("usuarioSolicita",
					"prestamoSolicitud.usuarioSolicita.empty");
		}
		if (prestamoSolicitud.getFechaDevolucion() == null) {
			errors.rejectValue("fechaDevolucion",
					"prestamo.fechaDevolucion.empty");
		}

		if (prestamoSolicitud.getExpediente() == null
				|| prestamoSolicitud.getExpediente().getId() == null) {
			errors.rejectValue("expediente",
					"prestamoSolicitud.expediente.empty");

		}

	}
}
