package br.com.maxgontijo.pmgo.planilhasveiculos.service;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Usuario;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.enums.Acesso;

import java.util.List;

public interface UsuarioService {
    List<Usuario> listar();

    List<Acesso> listarAcessosUsuarioLogado();

    boolean usuarioLogadoTemAcesso(Acesso acesso);

    void assertUsuarioLogadoTemAcesso(Acesso acesso);

    void criarUsuario(String username, String password);

    void alterarUsuario(Usuario usuario);

    void alterarSenha(String username, String oldPassword, String newPassword);

    void definirSenha(String username, String password);

    void alterarAtivacao(String username, boolean statusAtivo);

    void alterarAcessos(String username, List<Acesso> acessos);

    void login(String username, String password);

    void removerUsuario(String username);
}
