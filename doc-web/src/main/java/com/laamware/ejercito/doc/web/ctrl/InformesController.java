package com.laamware.ejercito.doc.web.ctrl;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.laamware.ejercito.doc.web.entity.Informe;
import com.laamware.ejercito.doc.web.entity.InformeParametros;
import com.laamware.ejercito.doc.web.entity.InformeParametros.InformeParametro;
import com.laamware.ejercito.doc.web.entity.InformeTable;
import com.laamware.ejercito.doc.web.repo.InformeRepository;

@Controller
@RequestMapping(value = "/informes")
public class InformesController extends UtilController {

	@Autowired
	InformeRepository informeRepository;

	@Autowired
	SessionFactory sessionFactory;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model, Principal principal) {

		List<Informe> informes = informeRepository.findByActivo(true, new Sort(
				Direction.ASC, "cuando"));
		model.addAttribute("informes", informes);

		return "informes";
	}

	@RequestMapping(value = "informe", method = RequestMethod.GET)
	public String informe(@RequestParam("id") Integer id, Model model,
			Principal principal) {
		Informe informe = informeRepository.getOne(id);
		InformeParametros params = new InformeParametros(informe);
		model.addAttribute("informe", informe);
		model.addAttribute("params", params);
		return "informes-informe";
	}

	@RequestMapping(value = "informe", method = RequestMethod.POST)
	public String informe(@RequestParam("id") Integer id, Model model,
			HttpServletRequest request, Principal principal) {

		Map<String, Object> parametros = findParametros(request);

		Informe inf = informeRepository.findOne(id);

		Session session = sessionFactory.openSession();
		SQLQuery x = session.createSQLQuery(inf.getSql());
		InformeParametros params = new InformeParametros(inf);
		for (InformeParametro p : params.getParametros()) {
			if (p.getType().equals("text") || p.getType().equals("date")) {
				x.setString(p.getId(), parametros.get(p.getId()).toString());
			} else if (p.getType().equals("number")) {
				x.setInteger(p.getId(), new Integer(parametros.get(p.getId())
						.toString()));
			}
		}
		@SuppressWarnings("unchecked")
		List<Object[]> results = x.list();
		session.close();
		InformeTable tabla = new InformeTable();
		for (Object[] o : results) {
			tabla.addRow(o);
		}
		model.addAttribute("tabla", tabla);
		model.addAttribute("params", params);

		return "informes-informe";
	}

	private Map<String, Object> findParametros(HttpServletRequest request) {
		Map<String, String[]> map = request.getParameterMap();
		Map<String, Object> ret = new HashMap<String, Object>();
		for (Entry<String, String[]> x : map.entrySet()) {
			if (StringUtils
					.startsWith(x.getKey(), InformeParametro.NAME_PREFIX)) {
				String paramName = x.getKey().substring(
						InformeParametro.NAME_PREFIX.length(),
						x.getKey().length());
				String value = StringUtils.EMPTY;
				if (x.getValue().length > 0) {
					value = x.getValue()[0];
				}
				ret.put(paramName, value);
			}
		}
		return ret;
	}
}
