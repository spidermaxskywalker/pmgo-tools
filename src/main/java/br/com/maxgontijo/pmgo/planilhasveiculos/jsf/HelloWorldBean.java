package br.com.maxgontijo.pmgo.planilhasveiculos.jsf;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Usuario;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.enums.Acesso;
import br.com.maxgontijo.pmgo.planilhasveiculos.service.UsuarioService;
import br.com.maxgontijo.pmgo.planilhasveiculos.util.DadosInvalidosException;
import br.com.maxgontijo.pmgo.planilhasveiculos.util.UtilSessao;
import br.com.maxgontijo.pmgo.planilhasveiculos.util.UtilValidacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("view")
public class HelloWorldBean extends GenericJsfBean {
    @Autowired
    private UsuarioService usuarioService;

    public String getHello() {
        return "Ferramentas de Análise de Dados";
    }

    private String senhaAtual;
    private String senhaNova1;
    private String senhaNova2;

    public void prepararAlterarSenha() {
        senhaAtual = "";
        senhaNova1 = "";
        senhaNova2 = "";
    }

    public void alterarMinhaSenha() {
        try {
            if (UtilValidacao.isVazio(senhaAtual)) {
                throw new DadosInvalidosException("Senha atual deve ser informada");
            }
            if (UtilValidacao.isVazio(senhaNova1) || UtilValidacao.isVazio(senhaNova2)) {
                throw new DadosInvalidosException("Senha nova deve ser informada nos dois campos de forma repetida");
            }
            if (!senhaNova1.equals(senhaNova2)) {
                throw new DadosInvalidosException("Senha nova deve ser informada nos dois campos de forma repetida");
            }
            usuarioService.alterarSenha(getUsuarioLogado().getUsername(), senhaAtual, senhaNova1);
            addMessageInfo("Sucesso", "Senha alterada");
        } catch (Exception e) {
            addMessageError(e);
        }
    }

    public Usuario getUsuarioLogado() {
        return UtilSessao.getUsuarioLogado();
    }

    public boolean isTemAcesso(Acesso acesso) {
        return getUsuarioLogado().getAcessos().contains(acesso);
    }

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }

    public String getSenhaNova1() {
        return senhaNova1;
    }

    public void setSenhaNova1(String senhaNova1) {
        this.senhaNova1 = senhaNova1;
    }

    public String getSenhaNova2() {
        return senhaNova2;
    }

    public void setSenhaNova2(String senhaNova2) {
        this.senhaNova2 = senhaNova2;
    }
}
