package br.com.maxgontijo.pmgo.planilhasveiculos.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

public class UtilRecurso {
	public static InputStream loadResource(String resource) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
	}

	public static byte[] loadResourceAsByteArray(String resource) throws IOException {
		InputStream in = loadResource(resource);
		return toByteArray(in);
	}

	public static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		in.close();
		out.close();
	}

	public static byte[] toByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copy(in, out);
		return out.toByteArray();
	}

	public static Properties loadProperties(String resource) {
		try {
			Properties props = new Properties();
			InputStream in = loadResource(resource);
			props.load(in);
			return props;
		} catch (Exception e) {
			return null;
		}
	}

	public static URL getUrlResource(String resource) {
		return Thread.currentThread().getContextClassLoader().getResource(resource);
	}
}
