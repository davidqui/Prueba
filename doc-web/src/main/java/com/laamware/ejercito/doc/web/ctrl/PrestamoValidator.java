package com.laamware.ejercito.doc.web.ctrl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.laamware.ejercito.doc.web.entity.Prestamo;

@Component
public class PrestamoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Prestamo.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		Prestamo prestamo = (Prestamo) target;

		if (StringUtils.isBlank(prestamo.getCodigo())) {
			errors.rejectValue("codigo", "prestamo.codigo.empty");
		}
		if (prestamo.getFechaDevolucion() == null) {
			errors.rejectValue("fechaDevolucion",
					"prestamo.fechaDevolucion.empty");
		}
		if (prestamo.getDependencia() == null
				|| prestamo.getDependencia().getId() == null) {
			errors.rejectValue("dependencia", "prestamo.dependencia.empty");
		}

		if (prestamo.getUsuarioSolicita() == null
				|| prestamo.getUsuarioSolicita().getId() == null) {
			errors.rejectValue("usuarioSolicita",
					"prestamo.usuarioSolicita.empty");
		}

	}
}
