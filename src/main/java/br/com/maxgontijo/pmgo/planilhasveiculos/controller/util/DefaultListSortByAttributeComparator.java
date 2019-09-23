package br.com.maxgontijo.pmgo.planilhasveiculos.controller.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DefaultListSortByAttributeComparator extends AbstractListSortComparator<Object> {
	private Method[] methods;

	public DefaultListSortByAttributeComparator(String[] args) {
		super(args);
		if (args.length == 0) {
			throw new RuntimeException("É necessário informar um atributo de ordenação");
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(Object o1, Object o2) {
		if (methods == null) {
			try {
				methods = getMethodSequenceByAttributeName(o1.getClass(), args[0]);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeException("Ordenação não foi possível pois houve erro ao tentar processar atributo " + args[0] + " para a classe " + o1.getClass(), e);
			}
		}

		try {
			o1 = parse(o1, methods);
			o2 = parse(o2, methods);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("Ordenação não foi possível pois houve erro ao tentar processar atributo " + args[0] + " para a classe " + o1.getClass(), e);
		}

		if (o1 == null) {
			if (o2 != null) {
				return 1;
			}
		} else if (o2 == null) {
			return -1;
		} else {
			if (o1 instanceof String) {
				return ((String) o1).compareToIgnoreCase((String) o2);
			} else {
				return ((Comparable) o1).compareTo(o2);
			}
		}

		return 0;
	}

	private Object parse(Object object, Method[] methods) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		object = methods[0].invoke(object);
		for (int i = 1; object != null && i < methods.length; i++) {
			object = methods[i].invoke(object);
		}
		return object;
	}

	private Method[] getMethodSequenceByAttributeName(Class<?> clazz, String attribute) throws NoSuchMethodException, SecurityException {
		String[] attributes = attribute.split("\\.");
		Method[] methods = new Method[attributes.length];
		for (int i = 0; i < attributes.length; i++) {
			methods[i] = getMethodByAttributeName(clazz, attributes[i]);
			clazz = methods[i].getReturnType();
		}
		return methods;
	}

	private Method getMethodByAttributeName(Class<?> clazz, String attribute) throws NoSuchMethodException, SecurityException {
		while (clazz != Object.class) {
			Method m = clazz.getDeclaredMethod("get" + attribute.toUpperCase().charAt(0) + attribute.substring(1));

			if (m != null) {
				return m;
			}

			clazz = clazz.getSuperclass();
		}
		return null;
	}
}
