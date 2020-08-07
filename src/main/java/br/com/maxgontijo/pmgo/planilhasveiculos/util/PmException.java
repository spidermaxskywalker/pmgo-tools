package br.com.maxgontijo.pmgo.planilhasveiculos.util;

public class PmException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PmException() {
		super();
	}

	public PmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PmException(String message, Throwable cause) {
		super(message, cause);
	}

	public PmException(String message) {
		super(message);
	}

	public PmException(Throwable cause) {
		super(cause);
	}
}
