package com.laamware.ejercito.doc.web.ctrl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;

@Service
public class DocumentoGeneradorVariables {

	@Autowired
	DocumentoRepository documentoRepository;

	/**
	 * Genera las variables de instancia que apliquen dependiendo del estado de
	 * los datos.
	 * 
	 * @param i
	 */
	public void generar(Instancia i) {

		String docId = i.getVariable(Documento.DOC_ID);

		// Si el documento no está definido prácticamente no se puede hacer
		// nada, por eso se retorna de inmediato
		if (StringUtils.isBlank(docId)) {
			return;
		}

		// Obtiene el documento
		Documento doc = documentoRepository.getOne(docId);

		// Genera la variable que indica si ya se puede generar el sticker.
		// Básicamente verifica que los datos necesarios estén presentes en el
		// documento.
		DocumentoMode.RegistroValidator val = new DocumentoMode.RegistroValidator();
		Errors errors = new BeanPropertyBindingResult(doc, "doc");
		val.validate(doc, i, errors);
		if (errors.hasErrors() == false) {
			i.setVariable(Documento.DOC_STICKER_DATOS_COMPLETOS, "true");
		}

		// Marca la instancia para que se pueda archivar o dar respuesta
		if (doc.getTrd() != null && doc.getTrd().getId() != null) {
			i.setVariable(Documento.DOC_PUEDE_ARCHIVAR, "true");
			i.setVariable(Documento.DOC_PUEDE_DAR_RESPUESTA, "true");
		}

		// Marca la instancia con visto bueno
		if (doc.getVistoBueno() != null && doc.getVistoBueno().getId() != null) {
			i.setVariable(Documento.DOC_VISTO_BUENO, "true");
		} else {
			i.setVariable(Documento.DOC_VISTO_BUENO, "false");
		}
	}
}
