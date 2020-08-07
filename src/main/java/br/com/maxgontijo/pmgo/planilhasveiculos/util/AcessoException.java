package br.com.maxgontijo.pmgo.planilhasveiculos.util;

public class AcessoException extends PmException {
    private static final long serialVersionUID = 1L;

    public AcessoException() {
        super();
    }

    public AcessoException(String message, Throwable cause) {
        super(message, cause);
    }

    public AcessoException(String message) {
        super(message);
    }

    public AcessoException(Throwable cause) {
        super(cause);
    }
}
