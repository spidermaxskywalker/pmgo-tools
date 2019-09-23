package br.com.maxgontijo.pmgo.planilhasveiculos.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

public class UtilReflection {
	@SuppressWarnings("unchecked")
	public static <T> T propertyGetValue(String property, Object object) {
		try {
			String[] props = property.split("\\.");
			Method method = null;
			for (int i = 0; object != null && i < props.length; i++) {
				method = object.getClass().getMethod(getMethodNameGetFromProperty(props[i]));
				if (method == null) {
					method = object.getClass().getMethod(getMethodNameIsFromProperty(props[i]));
				}
				object = method.invoke(object);
			}
			return (T) object;
		} catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> void propertySetValue(String property, Object object, T value) {
		propertySetValue(property, object, value, false);
	}

	public static <T> void propertySetValue(String property, Object object, T value, boolean forceCasting) {
		try {
			String[] props = property.split("\\.");
			Method method = null;
			for (int i = 0; object != null && i < props.length - 1; i++) {
				method = object.getClass().getMethod(getMethodNameGetFromProperty(props[i]));
				object = method.invoke(object);
			}

			if (object != null) {
				String propName = getMethodNameSetFromProperty(props[props.length - 1]);
				for (Method m : object.getClass().getMethods()) {
					if (m.getName().equals(propName)) {
						Class<?> propertyType = m.getParameterTypes()[0];
						if (!forceCasting || value == null || propertyType.isAssignableFrom(value.getClass())) {
							m.invoke(object, value);
						} else {
							Object convertedValue;
							if (propertyType == Short.class || propertyType == short.class)
								convertedValue = Short.parseShort(value.toString());
							else if (propertyType == Integer.class || propertyType == int.class)
								convertedValue = Integer.parseInt(value.toString());
							else if (propertyType == Long.class || propertyType == long.class)
								convertedValue = Long.parseLong(value.toString());
							else if (propertyType == Character.class || propertyType == char.class)
								convertedValue = value.toString().isEmpty() ? '\u0000' : value.toString().charAt(0);
							else if (propertyType == Float.class || propertyType == float.class)
								convertedValue = Float.parseFloat(value.toString());
							else if (propertyType == Double.class || propertyType == double.class)
								convertedValue = Double.parseDouble(value.toString());
							else if (propertyType == Boolean.class || propertyType == boolean.class)
								convertedValue = Boolean.parseBoolean(value.toString());
							else if (propertyType == Date.class)
								convertedValue = UtilDate.toDateDefaultPattern(value.toString());
							else if (propertyType.isEnum()) {
								convertedValue = propertyType.getMethod("valueOf", String.class).invoke(null, value.toString());
							} else
								throw new RuntimeException("Tipo de valor não previsto para alteração de propriedade");

							m.invoke(object, convertedValue);
						}
						break;
					}
				}
			}
		} catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private static String getMethodNameGetFromProperty(String property) {
		return "get" + (property.substring(0, 1).toUpperCase()) + property.substring(1);
	}

	private static String getMethodNameIsFromProperty(String property) {
		return "is" + (property.substring(0, 1).toUpperCase()) + property.substring(1);
	}

	private static String getMethodNameSetFromProperty(String property) {
		return "set" + (property.substring(0, 1).toUpperCase()) + property.substring(1);
	}
}
