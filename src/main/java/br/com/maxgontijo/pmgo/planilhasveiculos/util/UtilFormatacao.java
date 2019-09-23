package br.com.maxgontijo.pmgo.planilhasveiculos.util;

public class UtilFormatacao {
	public static String formatarStrNumber(Long valor, String mask) {
		String maskNumbers = mask.replaceAll("[^\\d]", "");
		String valueStr = String.format("%0" + maskNumbers.length() + "d", valor);

		StringBuilder out = new StringBuilder();

		int index = 0;
		for (int i = 0; i < mask.length(); i++) {
			if (Character.isDigit(mask.charAt(i))) {
				out.append(valueStr.charAt(index++));
			} else {
				out.append(mask.charAt(i));
			}
		}

		return out.toString();
	}

	public static String formatarCPF(Long cpf) {
		return formatarStrNumber(cpf, "999.999.999-99");
	}

	public static String formatarCNPJ(Long cnpj) {
		return formatarStrNumber(cnpj, "99.999.999/9999-99");
	}

	public static String formatarValor(Double valor) {
		return formatarValor(valor, 2);
	}

	public static String formatarValor(Double valor, int casasDecimais) {
		return String.format("%." + casasDecimais + "f", valor).replace('.', ',');
	}

	public static String numeroDecimalParaRomano(Integer numeroDecimal) {
		if (numeroDecimal < 1 || numeroDecimal > 3999) {
			throw new DadosInvalidosException("Somente números inteiros no intervalo de 1 a 3999 podem ser convertidos para números romanos. Valor informado: " + numeroDecimal);
		}

		int[] vaNum = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
		String[] vaRom = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
		StringBuilder numeroRomano = new StringBuilder();

		while (true) {
			if (numeroDecimal == 0) {
				break;
			}
			int i = 0;
			while (numeroDecimal > 0) {
				if (numeroDecimal >= vaNum[i]) {
					numeroRomano.append(vaRom[i]);
					numeroDecimal -= vaNum[i];
				} else {
					i++;
				}
			}
		}

		return numeroRomano.toString();
	}

	public static Integer numeroRomanoParaDecimal(String numeroRomano) {
		int[] vaNum = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
		String[] vaRom = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };

		int numeroDecimal = 0;

		int k = 0;
		for (int i = 0; i < numeroRomano.length(); i++) {
			String c = numeroRomano.substring(i, i + 1);
			while (k < vaRom.length && !c.equals(vaRom[k])) {
				k++;
			}
			if (k == vaRom.length) {
				throw new DadosInvalidosException("Parâmetro informado não se trata de um número romano válido. Valor informado: " + numeroRomano);
			}
			if (i + 1 < numeroRomano.length()) {
				String c2 = numeroRomano.substring(i + 1, i + 2);
				for (int j = 0; j < k; j++) {
					if (c2.equals(vaRom[j])) {
						c = numeroRomano.substring(i, i + 2);
						i++;
						break;
					}
				}
			}
			for (int m = 0; m < vaRom.length; m++) {
				if (vaRom[m].equals(c)) {
					numeroDecimal += vaNum[m];
					break;
				}
			}
		}

		return numeroDecimal;
	}
}
