package com.laamware.ejercito.doc.web.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

public class GeneralUtils {

    public final static String newId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * Genera un arreglo de UUIDs.
     *
     * @param quantity Cantidad de UUIDs a generar.
     * @return Arreglo de UUIDs.
     */
    /*
     * 2018-04-13 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech)
     * feature-156: Método de apoyo.
     */
    public final static String[] generateUUIDs(final int quantity) {
        final String[] array = new String[quantity];

        for (int i = 0; i < array.length; i++) {
            array[i] = newId();
        }

        return array;
    }

    public static String mapToString(Map<String, String[]> map) {
        StringBuilder sb = new StringBuilder();
        Iterator<Entry<String, String[]>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, String[]> entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=').append('"');
            String[] values = entry.getValue();
            for (String string : values) {
                sb.append(string).append(",");
            }
            sb.append('"');
            if (iter.hasNext()) {
                sb.append(',').append(' ');
            }
        }
        return sb.toString();

    }

    /**
     * Mapa que mantiene las referencias a los métodos get que se vayan usando
     * para evitar sobrecarga por usar reflección de clases.
     */
    private static Map<Class<?>, Map<String, Method>> propertiesCache = new HashMap<Class<?>, Map<String, Method>>();

    /**
     * Reemplaza las marcas con el formato {...}
     *
     * @param str Cadena de caracteres que contiene las variables
     * @param map Mapa con los objetos que se usarán para reemplazar
     * @return
     * @throws InvocacionException Ocurre cuando no se puede obtener el valor de
     * una propiedad o el método get de la propiedad lanza un excepción.
     * @throws SintaxisException Ocurre cuando la sintaxis del nombre de la
     * variable no corresponde al formato necesario
     */
    public static String merge(String str, Map<String, Object> map, Locale locale)
            throws SintaxisException, InvocacionException {
        final int l = str.length();
        StringBuilder b = new StringBuilder(l);
        StringBuilder vb = null;
        boolean escaped = false;
        boolean varname = false;
        for (int i = 0; i < l; i++) {
            Character c = str.charAt(i);
            if (c == '\\') {
                escaped = true;
            } else if (escaped) {
                b.append(c);
                escaped = false;
            } else if (c == '{') {
                vb = new StringBuilder();
                varname = true;
            } else if (c == '}') {
                if (varname == false) {
                    b.append(c);
                } else {
                    String[] path = StringUtils.splitByWholeSeparatorPreserveAllTokens(vb.toString(), ".");
                    Object value = null;
                    try {
                        value = getValue(map, path, 0, locale);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (value != null) {
                        b.append(value.toString());
                    }
                }
                varname = false;
            } else {
                if (varname == true) {
                    vb.append(c);
                } else {
                    b.append(c);
                }
            }
        }
        return b.toString();
    }

    /**
     * Obtiene el valor de una posición del camino en adelante, aplicado sobre
     * el objeto que se pasa como parámetro.
     *
     * @param obj El objeto raiz sobre el que se inicia la evaluación del camino
     * @param path El camino representado en un arreglo de cadenas,cada una
     * representando una propiedad
     * @param index Índice del camino desde el que se inicia la evaluación
     * @return
     * @throws SintaxisException
     * @throws InvocacionException
     */
    @SuppressWarnings("rawtypes")
    private static Object getValue(Object obj, String[] path, Integer index, Locale locale)
            throws SintaxisException, InvocacionException {
        String name = path[index];
        if (obj == null) {
            return "{" + StringUtils.join(path, '.') + "}";
        }
        Object value = null;
        if (Map.class.isAssignableFrom(obj.getClass())) {
            value = ((Map) obj).get(name);
        } else {
            value = getPropertyValue(obj, name);
        }
        if (index == (path.length - 1)) {
            if (value instanceof Date) {
                SimpleDateFormat sdf = new SimpleDateFormat("d 'de' MMMMM 'del' yyyy", locale);
                return sdf.format(value);
            } else {
                return value;
            }
        } else {
            return getValue(value, path, index + 1, locale);
        }
    }

    /**
     * Obtiene el valor de una propiedad del objeto
     *
     * @param obj Objeto que contiene la propiedad
     * @param name Nombre de la propiedad
     * @return
     * @throws SintaxisException
     * @throws InvocacionException
     */
    private static Object getPropertyValue(Object obj, String name) throws SintaxisException, InvocacionException {
        Method getter = getGetter(obj.getClass(), name);
        try {
            return getter.invoke(obj, new Object[0]);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new InvocacionException("Invocando " + getter.toString() + " sobre el objeto " + obj.toString(), e);
        }
    }

    /**
     * Obtiene el método get que corresponde a la propiedad
     *
     * @param class1
     * @param name
     * @return
     * @throws SintaxisException
     */
    private static Method getGetter(Class<? extends Object> class1, String name) throws SintaxisException {
        Map<String, Method> props = propertiesCache.get(class1);
        if (props == null) {
            props = new HashMap<String, Method>();
            propertiesCache.put(class1, props);
        }
        Method getter = props.get(name);
        if (getter == null) {
            try {
                getter = class1.getMethod("get" + StringUtils.capitalize(name), new Class<?>[0]);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new SintaxisException(
                        "No se encuentra la propiedad " + name + " para la clase " + class1.getName());
            }
            props.put(name, getter);
        }
        return getter;
    }

}
