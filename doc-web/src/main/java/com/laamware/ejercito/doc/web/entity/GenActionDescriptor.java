package com.laamware.ejercito.doc.web.entity;

import java.util.ArrayList;
import java.util.List;

public class GenActionDescriptor {

	public static class GenActionDescriptorParameter {
		private String name;
		private GenPropDescriptor prop;

		public GenActionDescriptorParameter(String name, GenPropDescriptor prop) {
			this.name = name;
			this.prop = prop;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public GenPropDescriptor getProp() {
			return prop;
		}

		public void setProp(GenPropDescriptor prop) {
			this.prop = prop;
		}

	}

	private String label;
	private String baseUrl;
	private List<GenActionDescriptorParameter> parameters = new ArrayList<GenActionDescriptorParameter>();

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public List<GenActionDescriptorParameter> getParameters() {
		return parameters;
	}

}
