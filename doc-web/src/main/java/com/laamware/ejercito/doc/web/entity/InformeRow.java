package com.laamware.ejercito.doc.web.entity;

import java.util.ArrayList;
import java.util.List;

public class InformeRow extends InformeRenderizableAdapter {

	private String clazz;

	private List<InformeCell> cells = new ArrayList<InformeCell>();

	public InformeRow(Object[] o) {
		for (Object x : o) {
			InformeCell cell = new InformeCell();
			cell.setValue(x == null ? "" : x.toString());
			cells.add(cell);
		}
	}

	@Override
	public String render() {
		StringBuilder cb = new StringBuilder();
		for (InformeCell c : cells) {
			cb.append(c.render());
		}
		return renderElementWithClassAndValue("tr", clazz, cb.toString());
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

}
