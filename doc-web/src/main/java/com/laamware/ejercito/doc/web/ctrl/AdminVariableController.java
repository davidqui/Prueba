package com.laamware.ejercito.doc.web.ctrl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.entity.VariableProceso;
import com.laamware.ejercito.doc.web.repo.VariableProcesoRepository;

@Controller
@RequestMapping(AdminVariableController.PATH)
public class AdminVariableController extends
		AdminGenController<VariableProceso, Integer, VariableProcesoRepository> {

	static final String PATH = "/admin/proceso-variable";

	@Autowired
	VariableProcesoRepository variableRepository;

	@Override
	VariableProcesoRepository getRepository() {
		return variableRepository;
	}

	@Override
	String getPath() {
		return AdminVariableController.PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(VariableProceso.class);
		return d;
	}

	@Override
	protected List<VariableProceso> findAll(boolean all) {
		return getRepository().findByProcesoId(getProcesoId());
	}

	private Integer getProcesoId() {
		return new Integer(queryStringMap.get("pid"));
	}

	private Proceso getProceso() {
		Proceso x = new Proceso();
		x.setId(getProcesoId());
		return x;
	}

	@Override
	protected void processRequest(HttpServletRequest req) {
		String pid = req.getParameter("pid");
		queryStringMap.put("pid", pid);
	}

	@Override
	public String getActivePill() {
		return "proceso";
	}

	@Override
	protected void preSave(VariableProceso e) {
		e.setProceso(getProceso());
	}

	@Override
	protected String returnUrl(HttpServletRequest req) {
		return AdminProcesoController.PATH;
	}
}
