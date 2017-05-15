package com.laamware.ejercito.doc.web.ctrl;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laamware.ejercito.doc.web.entity.Bandeja;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.repo.BandejaRepository;

@Controller
@PreAuthorize("hasRole('ELIMINADO_ADMIN_BANDEJAS')")
@RequestMapping(AdminBandejaController.PATH)
public class AdminBandejaController extends
		AdminGenController<Bandeja, Integer, BandejaRepository> {

	static final String PATH = "/admin/bandeja";

	@Autowired
	BandejaRepository repo;

	@Autowired
	DataSource ds;

	@Override
	BandejaRepository getRepository() {
		return repo;
	}

	@Override
	protected List<Bandeja> findAll() {
		return getRepository().findAll(new Sort(Direction.ASC, "nombre"));
	}

	@Override
	String getPath() {
		return PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(Bandeja.class);
		return d;
	}

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "bandeja";
	}

	@Override
	protected void postSave(Bandeja e) {

		// Intenta eliminar y crear la vista de la bandeja
		StringBuilder stmtbuiBuilder = new StringBuilder(
				"CREATE OR REPLACE VIEW ");
		stmtbuiBuilder.append("VB_").append(e.getCodigo());
		stmtbuiBuilder.append(" AS ");
		stmtbuiBuilder.append(e.getViewSql());

		try {
			CallableStatement call = ds.getConnection().prepareCall(
					stmtbuiBuilder.toString());
			call.execute();
			e.setCompila(true);
		} catch (SQLException e1) {
			e1.printStackTrace();
			e.setCompila(Boolean.FALSE);
		}

		repo.save(e);

	}
}
