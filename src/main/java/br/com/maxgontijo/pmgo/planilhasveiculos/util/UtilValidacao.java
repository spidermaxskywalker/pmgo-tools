package br.com.maxgontijo.pmgo.planilhasveiculos.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class UtilValidacao {
	public static boolean isVazio(String value) {
		return value == null || value.trim().isEmpty();
	}

	public static boolean isHtmlBodyVazio(String html) {
		if (isVazio(html)) {
			return true;
		}

		final String BODY_INI = "<body>";
		int indexIni = html.indexOf(BODY_INI);
		if (indexIni >= 0) {
			final String BODY_FIM = "</body>";
			int indexFim = html.indexOf(BODY_FIM);
			if (indexFim > 0) {
				String str = html.substring(indexIni + BODY_INI.length(), indexFim);
				str = str.replace('\n', ' ').replace('\t', ' ').replace('\r', ' ').trim();
				return str.isEmpty();
			}
		}
		return false;
	}

	public static boolean isCpfValido(Long cpf) {
		if (cpf == null) {
			return false;
		}

		String strCpf = String.format("%011d", cpf);

		if (strCpf.length() != 11) {
			return false;
		}

		boolean todosIguais = true;
		for (int i = 1; i < 11; i++) {
			if (strCpf.charAt(i - 1) != strCpf.charAt(i)) {
				todosIguais = false;
				break;
			}
		}

		if (todosIguais) {
			return false;
		}

		int acum = 0;
		for (int i = 0; i < 9; i++) {
			acum += (strCpf.charAt(i) - '0') * (10 - i);
		}

		if (((acum * 10) % 11) % 10 != strCpf.charAt(9) - '0') {
			return false;
		}

		acum = 0;
		for (int i = 0; i < 9; i++) {
			acum += (strCpf.charAt(i) - '0') * (11 - i);
		}
		acum += (strCpf.charAt(9) - '0') * (2);

		if (((acum * 10) % 11) % 10 != strCpf.charAt(10) - '0') {
			return false;
		}

		return true;
	}

	public static <S> void assertNotEmpty(Object object, String... propertiesAndFieldNames) {
		assertNotEmpty(object, true, propertiesAndFieldNames);
	}

	public static <S> void assertNotEmptyAndNotZero(Object object, String... propertiesAndFieldNames) {
		assertNotEmpty(object, false, propertiesAndFieldNames);
	}

	private static <S> void assertNotEmpty(Object object, boolean canBeZero, String... propertiesAndFieldNames) {
		List<String> messages = new LinkedList<>();

		for (int i = 0; i < propertiesAndFieldNames.length; i = i + 2) {
			try {
				String[] props = propertiesAndFieldNames[i].split("\\.");

				Object value = object;

				for (int j = 0; value != null && j < props.length; j++) {
					props[j] = props[j].toUpperCase().charAt(0) + props[j].substring(1);

					Method method;

					try {
						method = value.getClass().getMethod("get" + props[j]);
					} catch (NoSuchMethodException e1) {
						try {
							method = value.getClass().getDeclaredMethod("is" + props[j]);
						} catch (NoSuchMethodException e2) {
							messages.add("O atributo " + propertiesAndFieldNames[i + 1] + " nao existe.");
							DadosInvalidosException ex = new DadosInvalidosException("Dados inválidos.");
							ex.getMessages().addAll(messages);
							throw ex;
						}
					}

					value = method.invoke(value);

					if (value != null) {
						if (value instanceof String) {
							if (((String) value).trim().isEmpty()) {
								value = null;
							}
						} else if (value instanceof Number && !canBeZero) {
							if (value instanceof Double || value instanceof Float) {
								if (((Number) value).doubleValue() == 0D) {
									value = null;
								}
							} else {
								if (((Number) value).longValue() == 0L) {
									value = null;
								}
							}
						}
					}
				}

				if (value == null) {
					messages.add("O atributo " + propertiesAndFieldNames[i + 1] + " é obrigatório.");
				}
			} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new IunesException(e);
			}
		}

		if (!messages.isEmpty()) {
			DadosInvalidosException ex = new DadosInvalidosException("Dados inválidos.");
			ex.getMessages().addAll(messages);
			throw ex;
		}
	}
}
