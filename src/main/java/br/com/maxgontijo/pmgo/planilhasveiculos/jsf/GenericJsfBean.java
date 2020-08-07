package br.com.maxgontijo.pmgo.planilhasveiculos.jsf;

import br.com.maxgontijo.pmgo.planilhasveiculos.config.IunesSpringApplicationContext;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.enums.Acesso;
import br.com.maxgontijo.pmgo.planilhasveiculos.util.BeanSemAcessoException;
import br.com.maxgontijo.pmgo.planilhasveiculos.util.UtilSessao;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

abstract class GenericJsfBean {
    public GenericJsfBean() {
        IunesSpringApplicationContext.getContext().getAutowireCapableBeanFactory().autowireBean(this);

        AcessoBean ann = getClass().getAnnotation(AcessoBean.class);
        if (ann != null) {
            Acesso[] acessos = ann.value();
            if (acessos == null || acessos.length == 0) {
                throw new BeanSemAcessoException();
            }
            boolean achou = false;
            for (Acesso a : acessos) {
                if (UtilSessao.getUsuarioLogado().getAcessos().contains(a)) {
                    achou = true;
                    break;
                }
            }
            if (!achou) {
                throw new BeanSemAcessoException();
            }
        }
    }

    public void addMessageInfo(String summary, String detail) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void addMessageError(Throwable e) {
        addMessageError("Erro", (e.getMessage() == null ? e.getClass().toString() : e.getMessage()), null);
        e.printStackTrace();
    }

    public void addMessageError(String summary, String detail) {
        addMessageError(summary, detail, null);
    }

    public void addMessageError(String summary, String detail, Throwable e) {
        if (e != null) {
            detail = detail + (e.getMessage() == null ? e.getClass() : e.getMessage());
            e.printStackTrace();
        }

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
