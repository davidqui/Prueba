package com.laamware.ejercito.doc.web.ctrl;

import org.springframework.validation.Errors;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Instancia;

public interface DocumentoValidator {

	public void validate(Documento target, Instancia i, Errors errors);

}
