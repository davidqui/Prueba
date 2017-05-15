package com.laamware.ejercito.doc.web.entity;

import java.util.ArrayList;
import java.util.List;

public class InformeTable extends InformeRenderizableAdapter {

	private String clazz = "table";

	private List<InformeColumn> columns = new ArrayList<InformeColumn>();

	private List<InformeRow> rows = new ArrayList<InformeRow>();

	@Override
	public String render() {
		StringBuilder cb = new StringBuilder();
		for (InformeColumn c : columns) {
			cb.append(c.render());
		}
		String thead = renderElementWithClassAndValue("thead", null,
				cb.toString());

		StringBuilder rb = new StringBuilder();
		for (InformeRow r : rows) {
			rb.append(r.render());
		}
		String tbody = renderElementWithClassAndValue("tbody", null,
				rb.toString());

		StringBuilder tb = new StringBuilder();
		tb.append(thead).append(tbody);

		return renderElementWithClassAndValue("table", clazz, tb.toString());
	}

	public void addRow(Object[] o) {
		InformeRow row = new InformeRow(o);
		rows.add(row);
	}

}
