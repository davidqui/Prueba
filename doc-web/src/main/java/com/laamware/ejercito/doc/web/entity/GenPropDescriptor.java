package com.laamware.ejercito.doc.web.entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author acpreda
 *
 */
public class GenPropDescriptor {

	private String name;
	private String label;
	private String description;
	private Boolean id = Boolean.FALSE;
	private Boolean assign = Boolean.FALSE;
	private Boolean trans = Boolean.FALSE;
	private Boolean oneToMany = Boolean.FALSE;
	private Boolean manyToOne = Boolean.FALSE;
	private Boolean manyToMany = Boolean.FALSE;
	private Boolean list = Boolean.FALSE;
	private Boolean create = Boolean.FALSE;
	private int listOrder;
	private int createOrder;
	private String type;
	private Field field;
	private Method getter;
	private Method setter;
	private String widget;
	private String widgetList;
	private GenPropDescriptor widgetValueProp;

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

	public Boolean getId() {
		return id;
	}

	public void setId(Boolean id) {
		this.id = id;
	}

	public Boolean getAssign() {
		return assign;
	}

	public void setAssign(Boolean assign) {
		this.assign = assign;
	}

	public Boolean getTrans() {
		return trans;
	}

	public void setTrans(Boolean trans) {
		this.trans = trans;
	}

	public Boolean getOneToMany() {
		return oneToMany;
	}

	public void setOneToMany(Boolean oneToMany) {
		this.oneToMany = oneToMany;
	}

	public Boolean getManyToOne() {
		return manyToOne;
	}

	public void setManyToOne(Boolean manyToOne) {
		this.manyToOne = manyToOne;
	}

	public Boolean getManyToMany() {
		return manyToMany;
	}

	public void setManyToMany(Boolean manyToMany) {
		this.manyToMany = manyToMany;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Method getGetter() {
		return getter;
	}

	public void setGetter(Method getter) {
		this.getter = getter;
	}

	public Method getSetter() {
		return setter;
	}

	public void setSetter(Method setter) {
		this.setter = setter;
	}

	public Boolean getList() {
		return list;
	}

	public void setList(Boolean list) {
		this.list = list;
	}

	public int getListOrder() {
		return listOrder;
	}

	public void setListOrder(int listOrder) {
		this.listOrder = listOrder;
	}

	public Boolean getCreate() {
		return create;
	}

	public void setCreate(Boolean create) {
		this.create = create;
	}

	public int getCreateOrder() {
		return createOrder;
	}

	public void setCreateOrder(int createOrder) {
		this.createOrder = createOrder;
	}

	public Object value(Object o) {
		try {
			if (getField().isAccessible()) {
				return getField().get(o);
			} else {
				if (getGetter() != null) {
					return getGetter().invoke(o, new Object[] {});
				} else {
					return "No accessor";
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException
				| InvocationTargetException e) {
			e.printStackTrace();
			return "Exception";
		}
	}
	
	public Object valueQuote(Object o) {
		Object valor;
		try {
			if (getField().isAccessible()) {
				valor = getField().get(o);
			} else {
				if (getGetter() != null) {
					valor = getGetter().invoke(o, new Object[] {});					
				} else {
					valor = "No accessor";
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			valor = "Exception";
		}
		
		if( valor == null ){
			return null;
		}
		if( valor instanceof String ){
			return valor.toString().replaceAll("\"", "&quot;");
		}
		return valor;
	}
	
	/**
	 * 
	 * @param o
	 * @return
	 */
	public Object valueSelect(Object o) {
		Object valor;
		try {
			if (getField().isAccessible()) {
				valor = getField().get(o);
			} else {
				if (getGetter() != null) {
					valor = getGetter().invoke(o, new Object[] {});					
				} else {
					valor = "No accessor";
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException
				| InvocationTargetException e) {
			e.printStackTrace();
			valor = "Exception";
		}
		
		if( valor == null ){
			return null;
		}
		if( valor instanceof Integer && getter != null && getter.getName().equalsIgnoreCase("getid") ){
			return valor.toString().replaceAll("\\.", "");
		}
		return valor;
	}

	public Object id(Object o) {
		Object val = value(o);
		if(val == null)
			return null;
		if(getField().getType().equals(Integer.class))
			return val;
		GenDescriptor d = GenDescriptor.find(getField().getType());
		return d.idProperties().get(0).value(val);
	}

	public String getWidget() {
		return widget;
	}

	public void setWidget(String widget) {
		this.widget = widget;
	}

	public String getWidgetList() {
		return widgetList;
	}

	public void setWidgetList(String widgetList) {
		this.widgetList = widgetList;
	}

	public GenPropDescriptor getWidgetValueProp() {
		return widgetValueProp;
	}

	public void setWidgetValueProp(GenPropDescriptor widgetValueProp) {
		this.widgetValueProp = widgetValueProp;
	}

}
