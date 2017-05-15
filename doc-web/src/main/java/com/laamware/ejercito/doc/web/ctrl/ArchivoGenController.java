package com.laamware.ejercito.doc.web.ctrl;

import java.io.Serializable;

import org.springframework.web.bind.annotation.ModelAttribute;

import com.laamware.ejercito.doc.web.repo.GenJpaRepository;

public abstract class ArchivoGenController<ET, IDT extends Serializable, RT extends GenJpaRepository<ET, IDT>>
		extends GenController<ET, IDT, RT> {

	@Override
	@ModelAttribute("templatePrefix")
	protected String getTemplatePrefix() {
		return "archivo-menu";
	}

	@ModelAttribute("activePill")
	protected String activePill() {
		return getActivePill();
	}

	public abstract String getActivePill();

}
