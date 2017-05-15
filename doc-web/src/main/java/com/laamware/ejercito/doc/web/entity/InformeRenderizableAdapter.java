package com.laamware.ejercito.doc.web.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

public abstract class InformeRenderizableAdapter implements InformeRenderizable {

	protected Map<String, String> attributes = new HashMap<String, String>();

	protected void addAttribute(String name, String value) {
		this.attributes.put(name, value);
	}

	protected Map<String, String> createAttributesMap() {
		return new HashMap<String, String>();
	}

	protected String renderSubmitButton(String text) {
		return String.format("<button class=\"btn btn-primary\">%s</button>",
				text);
	}

	protected String renderClassAttribute(String clazz) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(clazz)) {
			sb.append(" class=\"").append(clazz).append("\"");
		}
		return sb.toString();
	}

	protected String renderElementWithClassAndValue(String element,
			String clazz, String value) {
		Map<String, String> attr = Collections.singletonMap("class", clazz);
		StringBuilder sb = new StringBuilder();
		sb.append(renderElementWithAttributesAndContent(element, attr, value));
		return sb.toString();
	}

	protected String renderElementWithAttributesAndContent(String element,
			Map<String, String> attributes, String content) {

		StringBuilder ab = new StringBuilder();
		if (attributes != null) {
			for (Entry<String, String> e : attributes.entrySet()) {
				ab.append(" ")
						.append(renderAttribute(e.getKey(), e.getValue()));
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<").append(element).append(" ").append(ab.toString());
		if (StringUtils.isBlank(content)) {
			sb.append("/>");
		} else {
			sb.append(">");
			sb.append(content);
			sb.append("</").append(element).append(">");
		}
		return sb.toString();

	}

	private String renderAttribute(String key, String value) {
		return String.format("%s=\"%s\"", key, value);
	}

}
