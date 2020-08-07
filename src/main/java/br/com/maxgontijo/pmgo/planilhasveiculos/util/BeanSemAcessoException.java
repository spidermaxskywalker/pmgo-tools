package br.com.maxgontijo.pmgo.planilhasveiculos.util;

public class BeanSemAcessoException extends PmException {
    private static final long serialVersionUID = 1L;

    public BeanSemAcessoException() {
        super();
    }

    public BeanSemAcessoException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanSemAcessoException(String message) {
        super(message);
    }

    public BeanSemAcessoException(Throwable cause) {
        super(cause);
    }
}
