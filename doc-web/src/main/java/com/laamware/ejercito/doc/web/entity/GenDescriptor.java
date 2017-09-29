package com.laamware.ejercito.doc.web.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.StringUtils;

public class GenDescriptor {

    public static Map<Class<?>, GenDescriptor> descriptorMap = new HashMap<Class<?>, GenDescriptor>();

    public static <T> GenDescriptor find(Class<T> clazz) {
        GenDescriptor d = descriptorMap.get(clazz);
        if (d == null) {
            d = build(clazz);
        }
        return d;
    }

    private static <T> GenDescriptor build(Class<T> clazz) {
        GenDescriptor d = new GenDescriptor();
        descriptorMap.put(clazz, d);

        d.type = clazz.getSimpleName();
        d.name = d.type;
        d.label = d.type;

        LaamLabel classLabel = clazz.getAnnotation(LaamLabel.class);
        if (classLabel != null) {
            d.label = classLabel.value();
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {

            /*
			 * 2017-02-07 jgarcia@controltechcg.com Issue #108: Corrección para
			 * no solicitar obtención de valores por método get/is a constantes.
             */
            int fieldModifiers = field.getModifiers();
            boolean fieldStatic = Modifier.isStatic(fieldModifiers);
            boolean fieldFinal = Modifier.isFinal(fieldModifiers);
            boolean fieldConstant = (fieldStatic && fieldFinal);

            GenPropDescriptor p = new GenPropDescriptor();
            d.properties.add(p);
            p.setField(field);
            p.setName(field.getName());

            final Class<?> fieldType = field.getType();
            p.setType(fieldType.getSimpleName());

            if (!fieldConstant) {
                try {
                    /*
					 * 2017-02-02 jgarcia@controltechcg.com Issue #129:
					 * Corrección en el manejo de asignación de valores
					 * booleanos del descriptor.
                     */
                    final String fieldNameCapitalized = StringUtils.capitalize(field.getName());
                    String getMethodName;
                    boolean fieldBoolean = fieldType.equals(Boolean.class) || fieldType.equals(boolean.class);
                    if (fieldBoolean) {
                        getMethodName = "is" + fieldNameCapitalized;
                    } else {
                        getMethodName = "get" + fieldNameCapitalized;
                    }

                    Method getter;
                    try {
                        getter = clazz.getMethod(getMethodName, new Class<?>[0]);
                    } catch (NoSuchMethodException ex) {
                        if (!fieldBoolean) {
                            throw ex;
                        }

                        // 2017-02-07 jgarcia@controltechcg.com : Se realiza
                        // este manejo ya que hay puntos en la implementación
                        // donde se usa método get en vez de método is para
                        // campos booleanos.
                        getMethodName = "get" + fieldNameCapitalized;
                        getter = clazz.getMethod(getMethodName, new Class<?>[0]);
                    }

                    p.setGetter(getter);

                    final String setMethodName = "set" + fieldNameCapitalized;
                    Method setter = clazz.getMethod(setMethodName, new Class<?>[]{fieldType});
                    p.setSetter(setter);
                } catch (NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
            }

            // @Id
            Id propId = field.getAnnotation(Id.class);
            if (propId != null) {
                p.setId(true);
            }

            // @LaamLabel
            LaamLabel label = field.getAnnotation(LaamLabel.class);
            if (label != null) {
                p.setLabel(label.value());
            } else {
                p.setLabel(p.getName());
            }

            // @LaamListColumn
            LaamListColumn listColumn = field.getAnnotation(LaamListColumn.class);
            if (listColumn != null) {
                p.setList(true);
                p.setListOrder(listColumn.order());
            }

            // @LaamCreate
            LaamCreate create = field.getAnnotation(LaamCreate.class);
            if (create != null) {
                p.setCreate(true);
                p.setCreateOrder(create.order());
            }

            resolveWidget(field, p, d);
        }

        return d;
    }

    private static void resolveWidget(Field field, GenPropDescriptor p, GenDescriptor d) {
        LaamWidget widget = field.getAnnotation(LaamWidget.class);
        if (widget != null) {
            p.setWidget(widget.value());
            p.setWidgetList(widget.list());
            if (p.getWidget().equals("select")) {
                GenDescriptor widgetDescriptor = null;
                if (widget.type().equals(Object.class) == false) {
                    if (d.getType().equals(widget.type().getSimpleName())) {

                        widgetDescriptor = d;
                    } else {
                        widgetDescriptor = GenDescriptor.find(widget.type());
                    }
                } else {
                    widgetDescriptor = GenDescriptor.find(field.getType());
                }
                GenPropDescriptor widgetValue = widgetDescriptor.idProperties().get(0);
                p.setWidgetValueProp(widgetValue);
            } else if (p.getWidget().equals("ofsfile")) {
                p.setWidget("ofsfile");
            }
            return;
        } else {
            if ("String".equals(p.getType()) || "Integer".equals(p.getType()) || "Long".equals(p.getType())) {
                p.setWidget("text");
                return;
            } else if ("Boolean".equals(p.getType())) {
                p.setWidget("checkbox");
                return;
            } else {
                ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
                if (manyToOne != null) {
                    p.setWidget("select");
                    return;
                }
            }
            p.setWidget("text");
            return;
        }
    }

    public void addAction(String label, String baseUrl, String[] parameters, String[] parameterNames) {

        for (GenActionDescriptor x : actions) {
            if (x.getLabel().equals(label)) {
                return;
            }
        }

        GenActionDescriptor a = new GenActionDescriptor();
        a.setLabel(label);
        a.setBaseUrl(baseUrl);
        if (parameters.length != parameterNames.length) {
            throw new RuntimeException(
                    "El tamaño del arreglo de parámetros no corresponde al tamaño del arreglo de nombres de parámetros: parameters.length != parameterNames.length");
        }
        for (int i = 0; i < parameters.length; i++) {
            boolean found = false;
            for (GenPropDescriptor prop : properties) {
                if (prop.getName().equals(parameters[i])) {
                    a.getParameters()
                            .add(new GenActionDescriptor.GenActionDescriptorParameter(parameterNames[i], prop));
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new RuntimeException("La propiedad " + parameters[i] + " no existe en la clase " + this.type);
            }
        }
        this.actions.add(a);
    }

    private String name;
    private String label;
    private String description;
    private String type;
    private List<GenPropDescriptor> properties = new ArrayList<GenPropDescriptor>();
    private List<GenActionDescriptor> actions = new ArrayList<GenActionDescriptor>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<GenPropDescriptor> getProperties() {
        return properties;
    }

    public List<GenPropDescriptor> listProperties() {
        List<GenPropDescriptor> props = new ArrayList<GenPropDescriptor>();

        for (GenPropDescriptor genPropDescriptor : properties) {
            if (genPropDescriptor.getList() == true) {
                props.add(genPropDescriptor);
            }
        }

        Collections.sort(props, new Comparator<GenPropDescriptor>() {
            @Override
            public int compare(GenPropDescriptor o1, GenPropDescriptor o2) {
                return Integer.compare(o1.getListOrder(), o2.getListOrder());
            }
        });

        return props;
    }

    public List<GenPropDescriptor> createProperties() {
        List<GenPropDescriptor> props = new ArrayList<GenPropDescriptor>();

        for (GenPropDescriptor genPropDescriptor : properties) {
            if (genPropDescriptor.getCreate() == true) {
                props.add(genPropDescriptor);
            }
        }

        Collections.sort(props, new Comparator<GenPropDescriptor>() {
            @Override
            public int compare(GenPropDescriptor o1, GenPropDescriptor o2) {
                return Integer.compare(o1.getCreateOrder(), o2.getCreateOrder());
            }
        });

        return props;
    }

    public List<GenPropDescriptor> idProperties() {
        List<GenPropDescriptor> props = new ArrayList<GenPropDescriptor>();

        for (GenPropDescriptor genPropDescriptor : properties) {
            if (genPropDescriptor.getId() == true) {
                props.add(genPropDescriptor);
            }
        }

        return props;
    }

    public List<GenActionDescriptor> getActions() {
        return actions;
    }

}
