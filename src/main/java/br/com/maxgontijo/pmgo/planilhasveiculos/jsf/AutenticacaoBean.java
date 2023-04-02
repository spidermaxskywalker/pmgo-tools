package br.com.maxgontijo.pmgo.planilhasveiculos.jsf;

import br.com.maxgontijo.pmgo.planilhasveiculos.service.UsuarioService;
import br.com.maxgontijo.pmgo.planilhasveiculos.util.UtilSessao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class AutenticacaoBean extends GenericJsfBean {
    private @Autowired
    UsuarioService usuarioService;

    private String username;
    private String password;

    public AutenticacaoBean() {
        System.out.println("Teste");
    }

    public String logout() {
        UtilSessao.invalidateSession();
        return "index?faces-redirect=true";
    }

    public String login() {
        try {
            usuarioService.login(username, password);

            username = password = null;

            return "index?faces-redirect=true";
        } catch (Exception e) {
            addMessageError(e);
            return "";
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
