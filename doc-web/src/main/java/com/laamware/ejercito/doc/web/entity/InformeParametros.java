package com.laamware.ejercito.doc.web.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class InformeParametros extends InformeRenderizableAdapter {

	public static class InformeParametro extends InformeRenderizableAdapter {

		public static final String NAME_PREFIX = "INFP_";
		private String id;
		private String title;
		private String type;
		private String source;

		@Override
		public String render() {

			Map<String, String> labelAttr = createAttributesMap();
			labelAttr.put("for", getName());
			String label = renderElementWithAttributesAndContent("label",
					labelAttr, title);

			Map<String, String> inputAttr = createAttributesMap();
			inputAttr.put("type", type);
			inputAttr.put("class", "form-control");
			inputAttr.put("id", getName());
			inputAttr.put("name", getName());
			String input = renderElementWithAttributesAndContent("input",
					inputAttr, null);

			return renderElementWithClassAndValue("fieldset", "form-group",
					label + input);
		}

		private String getName() {
			return NAME_PREFIX + id;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

	}

	private ArrayList<InformeParametro> parametros;

	private Informe informe;

	public InformeParametros(Informe informe) {
		this.informe = informe;
		parseParametros(informe.getParametros());
	}

	private void parseParametros(String parametros) {
		if (StringUtils.isNotBlank(parametros)) {
			String[] defs = parametros.split(";");
			this.parametros = new ArrayList<InformeParametro>();
			for (String def : defs) {
				this.parametros.add(parseParametro(def));
			}
		}
	}

	private InformeParametro parseParametro(String def) {
		String[] tokens = StringUtils.splitPreserveAllTokens(def, ",");
		InformeParametro x = new InformeParametro();
		x.id = tokens[0];
		x.title = tokens[1];
		x.type = tokens[2];
		x.source = tokens[3];
		return x;
	}

	@Override
	public String render() {
		Map<String, String> formAttr = new HashMap<String, String>();
		formAttr.put("action", "/informes/informe?id="
				+ informe.getId().toString());
		formAttr.put("method", "POST");

		StringBuilder content = new StringBuilder();

		if (parametros != null) {
			for (InformeParametro p : parametros) {
				content.append(p.render());
			}
		}

		content.append(renderSubmitButton("Ejecutar"));

		return renderElementWithAttributesAndContent("form", formAttr,
				content.toString());
	}

	public ArrayList<InformeParametro> getParametros() {
		return parametros;
	}

	public void setParametros(ArrayList<InformeParametro> parametros) {
		this.parametros = parametros;
	}

}
