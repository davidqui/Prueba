package com.laamware.ejercito.doc.web.entity;

public class InformeCell extends InformeRenderizableAdapter {

	private String value;
	private String clazz;

	@Override
	public String render() {
		return renderElementWithClassAndValue("td", clazz, value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

}
