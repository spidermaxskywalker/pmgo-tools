package br.com.maxgontijo.pmgo.planilhasveiculos.dto;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import br.com.maxgontijo.pmgo.planilhasveiculos.util.UtilDate;

public class DateRestConverter implements Converter<String, Date> {
	@Override
	public Date convert(String date) {
		try {
			return UtilDate.toDateDefaultPattern(date);
		} catch (Exception e) {
			return null;
		}
	}
}
