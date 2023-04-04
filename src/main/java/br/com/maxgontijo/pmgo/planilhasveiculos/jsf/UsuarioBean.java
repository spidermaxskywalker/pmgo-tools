package br.com.maxgontijo.pmgo.planilhasveiculos.jsf;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Usuario;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.enums.Acesso;
import br.com.maxgontijo.pmgo.planilhasveiculos.service.UsuarioService;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("view")
@AcessoBean(Acesso.ADMIN)
public class UsuarioBean extends GenericJsfBean {
    private @Autowired
    UsuarioService usuarioService;

    private List<Usuario> usuarios;
    private Usuario usuario;

    private List<AcessoUsuario> acessosUsuario;

    @PostConstruct
    public void init() {
        atualizarLista();
    }

    public void atualizarLista() {
        try {
            usuarios = usuarioService.listar();
        } catch (Exception e) {
            usuarios = new ArrayList<>();
            addMessageError(e);
        }
    }

    public void novoUsuario() {
        usuario = new Usuario();
        usuario.setAcessos(new ArrayList<>());

        Acesso[] acessos = Acesso.values();
        acessosUsuario = new ArrayList<>(acessos.length);
        for (Acesso a : acessos) {
            acessosUsuario.add(new AcessoUsuario(false, a));
        }
    }

    public void selecionarUsuario(ActionEvent event) {
        try {
            this.usuario = (Usuario) event.getComponent().getAttributes().get("usuario");

            Acesso[] acessos = Acesso.values();
            acessosUsuario = new ArrayList<>(acessos.length);
            for (Acesso a : acessos) {
                acessosUsuario.add(new AcessoUsuario(this.usuario.getAcessos().contains(a), a));
            }
        } catch (Exception e) {
            addMessageError(e);
        }
    }

    public void removerUsuario(ActionEvent event) {
        try {
            this.usuario = (Usuario) event.getComponent().getAttributes().get("usuario");
            this.usuarioService.removerUsuario(usuario.getUsername());
        } catch (Exception e) {
            addMessageError(e);
        } finally {
            atualizarLista();
        }
    }

    public void salvar() {
        try {
            usuario.getAcessos().clear();
            for (AcessoUsuario au : acessosUsuario) {
                if (au.selecionado) {
                    usuario.getAcessos().add(au.acesso);
                }
            }

            if (usuario.getId() == null) {
                usuarioService.criarUsuario(usuario.getUsername(), usuario.getPassword());
            } else {
                usuarioService.alterarUsuario(usuario);
            }
            clearUsuario();
            PrimeFaces.current().ajax().addCallbackParam("sucesso", true);
        } catch (Exception e) {
            addMessageError(e);
            PrimeFaces.current().ajax().addCallbackParam("sucesso", false);
        } finally {
            atualizarLista();
        }
    }

    public void clearUsuario() {
        this.usuario = null;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<AcessoUsuario> getAcessosUsuario() {
        return acessosUsuario;
    }

    public static class AcessoUsuario {
        private boolean selecionado;
        private Acesso acesso;

        public AcessoUsuario(boolean selecionado, Acesso acesso) {
            this.selecionado = selecionado;
            this.acesso = acesso;
        }

        public boolean isSelecionado() {
            return selecionado;
        }

        public void setSelecionado(boolean selecionado) {
            this.selecionado = selecionado;
        }

        public Acesso getAcesso() {
            return acesso;
        }
    }
}
