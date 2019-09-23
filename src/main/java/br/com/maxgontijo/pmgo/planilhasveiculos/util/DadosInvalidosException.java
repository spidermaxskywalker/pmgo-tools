package br.com.maxgontijo.pmgo.planilhasveiculos.util;

import java.util.ArrayList;
import java.util.List;

public class DadosInvalidosException extends IunesException {
	private static final long serialVersionUID = 1L;

	private final List<String> messages;

	public DadosInvalidosException() {
		super();
		this.messages = new ArrayList<>();
	}

	public DadosInvalidosException(String message, Throwable cause) {
		super(message, cause);
		this.messages = new ArrayList<>();
	}

	public DadosInvalidosException(String message) {
		super(message);
		this.messages = new ArrayList<>();
	}

	public DadosInvalidosException(Throwable cause) {
		super(cause);
		this.messages = new ArrayList<>();
	}

	public List<String> getMessages() {
		return messages;
	}
}
