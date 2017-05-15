package com.laamware.ejercito.doc.web.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.laamware.ejercito.doc.web.entity.Condicion;
import com.laamware.ejercito.doc.web.entity.Instancia;

/**
 * Métodos utilitarios usados en la gestión de los procesos y sus instancias
 * 
 * @author acpreda
 *
 */
public class ProcesoUtils {

	private static final Logger log = LoggerFactory.getLogger(ProcesoUtils.class);

	/**
	 * Reemplaza las marcas con el formato {...}
	 * 
	 * @param str
	 *            Cadena de caracteres que contiene las variables
	 * @param map
	 *            Mapa con los objetos que se usarán para reemplazar
	 * @return
	 * @throws InvocacionException
	 *             Ocurre cuando no se puede obtener el valor de una propiedad o
	 *             el método get de la propiedad lanza un excepción.
	 * @throws SintaxisException
	 *             Ocurre cuando la sintaxis del nombre de la variable no
	 *             corresponde al formato necesario
	 */
	public static String resolveUrl(String str, Map<String, Object> map) throws SintaxisException, InvocacionException {
		return GeneralUtils.merge(str, map, Locale.US);
	}

	/**
	 * Obtiene el objeto de la clase que ejecuta la condición
	 * 
	 * @param condicion
	 * @return
	 */
	public static ICondicion getConditionObject(Condicion condicion, String tmpDirectory) {
		ICondicionEntry iCondicionEntry = condicionesCache.get(condicion.getId());
		boolean compile = false;
		Date modDate = condicion.getCuandoMod() == null ? condicion.getCuando() : condicion.getCuandoMod();
		if (modDate == null)
			modDate = new Date();
		if (iCondicionEntry == null) {
			compile = true;
		} else {

			Date cacheDate = iCondicionEntry.condicion.getCuandoMod() == null ? iCondicionEntry.condicion.getCuando()
					: iCondicionEntry.condicion.getCuandoMod();
			if (cacheDate.getTime() < modDate.getTime()) {
				compile = true;
			}
		}

		if (compile == true) {
			ICondicion iCondicion = compileCondicion(condicion, tmpDirectory);
			if (iCondicion == null)
				return null;
			iCondicionEntry = new ICondicionEntry();
			iCondicionEntry.condicion = new Condicion();
			iCondicionEntry.condicion.setCuandoMod(modDate);
			iCondicionEntry.condicion.setId(condicion.getId());
			iCondicionEntry.condicion.setNombre(condicion.getNombre());
			iCondicionEntry.condicion.setPrograma(condicion.getPrograma());
			iCondicionEntry.iCondicion = iCondicion;
			condicionesCache.put(condicion.getId(), iCondicionEntry);
		}

		return iCondicionEntry.iCondicion;
	}

	// /////////////////////////////// PRIVADO ///////////////////////////////

	private static Map<Integer, ICondicionEntry> condicionesCache = new HashMap<Integer, ICondicionEntry>();

	static class ICondicionEntry {
		Condicion condicion;
		ICondicion iCondicion;
	}

	/**
	 * Compila el programa de una condición como una clase que implementa la
	 * interfaz ICondicion.
	 * 
	 * @param tmpDirectory
	 * 
	 * @param e
	 */
	@SuppressWarnings("rawtypes")
	private static ICondicion compileCondicion(Condicion condicion, String tmpDirectory) {

		if (StringUtils.isBlank(condicion.getPrograma())) {
			return null;
		}

		StringBuilder b = new StringBuilder();
		String packageName = ProcesoUtils.class.getPackage().getName();
		String className = "Condicion_" + condicion.getId() + "_" + new Date().getTime();
		b.append("package ").append(packageName).append(";");
		b.append("import ").append(ICondicion.class.getName()).append(";");
		b.append("import ").append(Instancia.class.getName()).append(";");
		b.append("import ").append(Map.class.getName()).append(";");
		b.append("public class ").append(className).append(" implements ICondicion { ");
		b.append("public boolean cumple(Map<String, String> vars, Object facade) throws Exception {");
		b.append(condicion.getPrograma());
		b.append("}");
		b.append("}");

		JavaFileObject file = new JavaSourceFromString(packageName + "." + className, b.toString());
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			throw new RuntimeException("El sistema no se está ejecutando con una instalación de JDK sino con un JRE");
		}
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

		URL[] urls = ((URLClassLoader) (ICondicion.class.getClassLoader())).getURLs();

		String[] paths = new String[urls.length];
		for (int i = 0; i < paths.length; i++) {
			paths[i] = urls[i].getPath();
			if (paths[i].contains(":")) {
				paths[i] = paths[i].substring(paths[i].lastIndexOf(":") + 1);
			}
			if (paths[i].contains("!")) {
				paths[i] = paths[i].substring(0, paths[i].indexOf("!"));
			}
		}

		List<String> optionList = new ArrayList<String>();
		String classpath = StringUtils.join(paths, (SystemUtils.IS_OS_WINDOWS) ? ';' : ':');
		optionList.addAll(Arrays.asList("-d", tmpDirectory, "-cp", classpath));
		System.out.println("Usando el classpath: " + classpath);

		Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
		CompilationTask task = compiler.getTask(null, null, diagnostics, optionList, null, compilationUnits);

		boolean success = task.call();
		for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
			log.error(String.format(
					"code=%s, kind=%s, position=%s, start-position=%s, end-position=%s, source=%s, message=%s",
					diagnostic.getCode(), diagnostic.getKind(), diagnostic.getPosition(), diagnostic.getStartPosition(),
					diagnostic.getEndPosition(), diagnostic.getSource(), diagnostic.getMessage(null)));
		}
		log.info("Success: " + success);

		if (success) {
			condicion.setCompilado(true);
			try {
				@SuppressWarnings({ "resource", "deprecation" })
				ClassLoader loader = new URLClassLoader(new URL[] { new File(tmpDirectory).toURL() },
						Thread.currentThread().getContextClassLoader());
				Class clazz = loader.loadClass(packageName + "." + className);
				ICondicion icondicion = (ICondicion) clazz.newInstance();
				return icondicion;
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
		}

		return null;
	}
}
