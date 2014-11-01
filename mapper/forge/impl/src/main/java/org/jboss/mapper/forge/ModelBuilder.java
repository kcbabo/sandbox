package org.jboss.mapper.forge;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public class ModelBuilder {
	
	public static Model fromJavaClass(Class<?> javaClass) {
		Model model = new Model(javaClass.getSimpleName(), javaClass.getName());
		addFieldsToModel(javaClass.getDeclaredFields(), model);
		return model;
	}
	
	public static Class<?> getFieldType(Field field) {
		Class<?> type;
		
		if (field.getType().isArray()) {
			return field.getType().getComponentType();
		} else if (Collection.class.isAssignableFrom(field.getType())) {
			Type t = field.getGenericType();
			if (t instanceof ParameterizedType) {
				type = (Class)((ParameterizedType)t).getActualTypeArguments()[0];
			} else {
				type = Object.class;
			}
		} else {
			type = field.getType();
		}
		
		return type;
	}
	
	public static String getListName(Class<?> listType) { 
		return "[" + listType.getName() + "]";
	}
	
	public static String getListType(String listName) { 
		return listName.split("\\[")[1].split("\\]")[0];
	}
	
	private static void addFieldsToModel(Field[] fields, Model model) {
		for (Field field : fields) {
			String fieldType;
			Field[] childFields = null;
			boolean isCollection = false;
			
			if (field.getType().isArray()) {
				isCollection = true;
				fieldType = getListName(field.getType().getComponentType());
				childFields = field.getType().getComponentType().getDeclaredFields();
			} else if (Collection.class.isAssignableFrom(field.getType())) {
				isCollection = true;
				Type t = field.getGenericType();
				if (t instanceof ParameterizedType) {
					Class<?> tClass = (Class)((ParameterizedType)t).getActualTypeArguments()[0];
					fieldType = getListName(tClass);
					childFields = tClass.getDeclaredFields();
				} else {
					fieldType = getListName(Object.class);
				}
			} else {
				fieldType = field.getType().getName();
				if (!field.getType().isPrimitive() 
						&& !field.getType().getName().equals(String.class.getName())
						&& !field.getType().getName().startsWith("java.lang")) {
					
					childFields = field.getType().getDeclaredFields();
				}
			}

			Model child = model.addChild(field.getName(), fieldType);
			child.setIsCollection(isCollection);
			if (childFields != null) {
				addFieldsToModel(childFields, child);
			}
		}
	}
}
