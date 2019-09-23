package br.com.maxgontijo.pmgo.planilhasveiculos.dto;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.convert.converter.Converter;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FiltroConverter implements Converter<String, Filtro[]> {
	private final char CHAR_ERRADO = 65533;

	@Override
	public Filtro[] convert(String filtroJsonBase64) {
		try {
			if (filtroJsonBase64 == null || filtroJsonBase64.trim().isEmpty()) {
				return null;
			}

			try {
				byte[] bytes = Base64.getDecoder().decode(filtroJsonBase64);
				String json = new String(bytes, StandardCharsets.ISO_8859_1);

				if (json != null && json.indexOf(CHAR_ERRADO) >= 0) {
					json = new String(bytes, StandardCharsets.UTF_8);
				}

				ObjectMapper mapper = new ObjectMapper();
				Filtro[] filtro;
				try {
					filtro = mapper.readValue(json, Filtro[].class);
				} catch (JsonMappingException e) {
					Object[][] filtroObj = mapper.readValue(json, Object[][].class);
					filtro = new Filtro[filtroObj.length];
					for (int i = 0; i < filtroObj.length; i++) {
						filtro[i] = new Filtro((String) filtroObj[i][0], (String) filtroObj[i][1], filtroObj[i][2]);
					}
				}
				return filtro;
			} catch (IllegalArgumentException e) {
				String filter = filtroJsonBase64;
				Pattern pattern = Pattern.compile("(.*?)(:|<|>)(.*?),");
				Matcher matcher = pattern.matcher(filter + ",");

				List<Filtro> filtros = new LinkedList<>();
				while (matcher.find()) {
					String op = matcher.group(2);
					if (op.equals(":")) {
						op = "=";
					}
					filtros.add(new Filtro(matcher.group(1), op, matcher.group(3)));
				}
				return filtros.toArray(new Filtro[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
