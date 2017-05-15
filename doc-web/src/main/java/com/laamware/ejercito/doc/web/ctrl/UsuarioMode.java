package com.laamware.ejercito.doc.web.ctrl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioMode extends HashMap<String, Boolean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final List<String> NAMES = Collections.unmodifiableList(Arrays.asList("login", "documento", "nombre",
			"telefono", "grado", "dependencia", "perfil", "clasificacion", "imagen", "accion"));

	private static final Map<String, UsuarioMode> modes = new HashMap<String, UsuarioMode>();

	public static final String REGISTRO_NAME = "REGISTRO";
	public static final String EDICION_NAME = "EDICION";
	
	private static final UsuarioMode REGISTRO = new UsuarioMode().putName(REGISTRO_NAME);
	private static final UsuarioMode EDICION = new UsuarioMode().putName(EDICION_NAME);
	
	
	private String name;

	
	public UsuarioMode() {
		noAll();
	}
	
	public UsuarioMode putName(String name) {
		this.name = name;
		return this;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	static {

		REGISTRO.editAndView("login").editAndView("documento").editAndView("nombre").editAndView("telefono")
				.editAndView("grado").editAndView("dependencia").editAndView("perfil").editAndView("clasificacion").editAndView("imagen").view("accion");
		modes.put(REGISTRO_NAME, REGISTRO);

		EDICION.view("login").editAndView("documento").editAndView("nombre").editAndView("telefono")
		.editAndView("grado").editAndView("dependencia").editAndView("perfil").editAndView("clasificacion").editAndView("imagen").editAndView("accion");
		modes.put(EDICION_NAME, EDICION);
	}
	
	public UsuarioMode noAll() {
		for (String name : UsuarioMode.NAMES) {
			noView(name);
			noEdit(name);
		}
		return this;
	}

	public UsuarioMode noEdit(String name) {
		this.put(name + "_edit", false);
		return this;
	}

	public UsuarioMode edit(String name) {
		this.put(name + "_edit", true);
		return this;
	}

	public UsuarioMode editAndView(String name) {
		this.edit(name);
		this.view(name);
		return this;
	}

	public UsuarioMode view(String name) {
		this.put(name + "_view", true);
		return this;
	}

	public UsuarioMode noView(String name) {
		this.put(name + "_view", false);
		return this;
	}

	public static UsuarioMode getByName(String name) {
		UsuarioMode mode = modes.get(name);
		if (mode == null) {
			return REGISTRO;
		} else {
			return mode;
		}
	}

}
