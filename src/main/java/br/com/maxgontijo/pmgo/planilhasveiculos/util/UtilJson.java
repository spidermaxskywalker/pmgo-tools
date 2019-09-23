package br.com.maxgontijo.pmgo.planilhasveiculos.util;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UtilJson {
	public static <T> T loadJsonObject(String json, Class<T> clazz) {
		try {
			T tagRoots = new ObjectMapper().readValue(json, clazz);
			return tagRoots;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> T loadJsonObject(InputStream json, Class<T> clazz) {
		try {
			T tagRoots = new ObjectMapper().readValue(json, clazz);
			return tagRoots;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> T loadJsonObjectFromPath(String path, Class<T> clazz) {
		InputStream in = UtilRecurso.loadResource(path);
		return loadJsonObject(in, clazz);
	}
}
