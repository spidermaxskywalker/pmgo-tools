package br.com.maxgontijo.pmgo.planilhasveiculos.util;

public class IunesException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public IunesException() {
		super();
	}

	public IunesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IunesException(String message, Throwable cause) {
		super(message, cause);
	}

	public IunesException(String message) {
		super(message);
	}

	public IunesException(Throwable cause) {
		super(cause);
	}
}
