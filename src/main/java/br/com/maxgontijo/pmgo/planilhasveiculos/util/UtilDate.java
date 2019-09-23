package br.com.maxgontijo.pmgo.planilhasveiculos.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilDate {
	public static final SimpleDateFormat DEFAULT_DATE_DISPLAY_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
	public static final SimpleDateFormat DEFAULT_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat DEFAULT_YMD = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat DEFAULT_TIMESTAMP_FORMATTER = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	public static Date toDateDefaultPattern(String date) {
		if (date == null) {
			return null;
		}
		try {
			return DEFAULT_DATE_FORMATTER.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static Date toDateDefaultPatternEx(String date) throws ParseException {
		if (date == null) {
			return null;
		}
		return DEFAULT_DATE_FORMATTER.parse(date);
	}

	public static String toStringDefaultPattern(Date date) {
		return DEFAULT_DATE_FORMATTER.format(date);
	}

	public static String toStringYMD(Date date) {
		return DEFAULT_YMD.format(date);
	}

	public static Date toDateDefaultTimestampPattern(String date) {
		if (date == null) {
			return null;
		}
		try {
			return DEFAULT_TIMESTAMP_FORMATTER.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static Date toDateDefaultTimestampPatternEx(String date) throws ParseException {
		if (date == null) {
			return null;
		}
		return DEFAULT_TIMESTAMP_FORMATTER.parse(date);
	}

	public static String toStringDefaultTimestampPattern(Date date) {
		return DEFAULT_TIMESTAMP_FORMATTER.format(date);
	}

	public static Date toDateDefaultDisplayPattern(String date) throws ParseException {
		if (date == null) {
			return null;
		}
		return DEFAULT_DATE_DISPLAY_FORMATTER.parse(date);
	}

	public static String toStringDefaultDisplayPattern(Date date) throws ParseException {
		if (date == null) {
			return null;
		}
		return DEFAULT_DATE_DISPLAY_FORMATTER.format(date);
	}
}
