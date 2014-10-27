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
	
	private static void addFieldsToModel(Field[] fields, Model model) {
		for (Field field : fields) {
			Model child = model.addChild(field.getName(), field.getType().getName());
			
			if (field.getType().isPrimitive() || field.getType().getName().equals(String.class.getName())) {
				// nothing more to do for this child model
				continue;
			}
			
			if (field.getType().isArray()) {
				child.setIsCollection(true);
				addFieldsToModel(field.getType().getComponentType().getDeclaredFields(), child);
			} else if (Collection.class.isAssignableFrom(field.getType())) {
				child.setIsCollection(true);
				Type t = field.getGenericType();
				if (t instanceof ParameterizedType) {
					Class<?> tClass = (Class)((ParameterizedType)t).getActualTypeArguments()[0];
					addFieldsToModel(tClass.getDeclaredFields(), child);
				}
			} else {
				addFieldsToModel(field.getType().getDeclaredFields(), child);
			}
		}
	}
}
