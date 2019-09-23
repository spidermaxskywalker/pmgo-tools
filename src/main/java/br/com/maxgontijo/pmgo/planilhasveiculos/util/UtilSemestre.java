package br.com.maxgontijo.pmgo.planilhasveiculos.util;

import java.util.Calendar;
import java.util.Date;

public class UtilSemestre {
	public static int semestreAtual() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int mes = cal.get(Calendar.MONTH) + 1;
		int ano = cal.get(Calendar.YEAR);
		return anoMesParaSemestre(ano, mes);
	}

	public static int anoMesParaSemestre(int ano, int mes) {
		if (mes <= 6) {
			return ano * 10 + 1;
		} else {
			return ano * 10 + 2;
		}
	}

	public static int anoSemestreParaInt(int ano, int semestre) {
		return ano * 10 + semestre;
	}

	public static int adicionarSemestre(int anoSemestre, int quantidade) {
		return adicionarSemestre(anoSemestre / 10, anoSemestre % 10, quantidade);
	}

	public static int adicionarSemestre(int ano, int semestre, int quantidade) {
		int v = ano * 10 + semestre;
		v += (quantidade / Math.abs(quantidade)) * ((Math.abs(quantidade) / 2) * 10);
		v += (quantidade / Math.abs(quantidade)) * (Math.abs(quantidade) % 2);
		if (v % 10 == 3) {
			v = v + 10 - 2;
		} else if (v % 10 == 0) {
			v = v - 10 + 2;
		}
		return v;
	}
}
