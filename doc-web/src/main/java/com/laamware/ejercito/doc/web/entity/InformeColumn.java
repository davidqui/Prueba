package com.laamware.ejercito.doc.web.entity;

public class InformeColumn extends InformeRenderizableAdapter {

	private String title;
	private String clazz;

	@Override
	public String render() {
		StringBuilder sb = new StringBuilder();
		sb.append(renderElementWithClassAndValue("th", clazz, title));
		return sb.toString();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

}
