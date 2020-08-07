package br.com.maxgontijo.pmgo.planilhasveiculos.util;

public class NaoEncontradoException extends PmException {
    private static final long serialVersionUID = 1L;

    public NaoEncontradoException() {
        super();
    }

    public NaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }

    public NaoEncontradoException(String message) {
        super(message);
    }

    public NaoEncontradoException(Throwable cause) {
        super(cause);
    }
}
