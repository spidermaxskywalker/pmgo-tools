package br.com.maxgontijo.pmgo.planilhasveiculos.service.impl;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Usuario;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.enums.Acesso;
import br.com.maxgontijo.pmgo.planilhasveiculos.repository.UsuarioRepository;
import br.com.maxgontijo.pmgo.planilhasveiculos.service.UsuarioService;
import br.com.maxgontijo.pmgo.planilhasveiculos.util.AcessoException;
import br.com.maxgontijo.pmgo.planilhasveiculos.util.DadosInvalidosException;
import br.com.maxgontijo.pmgo.planilhasveiculos.util.NaoEncontradoException;
import br.com.maxgontijo.pmgo.planilhasveiculos.util.UtilSessao;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service("usuarioService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class UsuarioServiceImpl implements UsuarioService {
    private @Autowired
    EntityManager entityManager;

    private @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> listar() {
        List<Usuario> list = usuarioRepository.findAll();

        List<Usuario> listRet = new ArrayList<>(list.size());
        for (Usuario u : list) {
//            u.getAcessos().size();
//            entityManager.detach(u);
//            u.setPassword(null);

            Usuario nu = new Usuario();
            nu.setId(u.getId());
            nu.setUsername(u.getUsername());
            nu.setAcessos(new ArrayList<>(u.getAcessos()));
            nu.setAtivo(u.isAtivo());

            listRet.add(nu);
        }

        return listRet;
    }

    @Override
    public List<Acesso> listarAcessosUsuarioLogado() {
        String username = UtilSessao.getUsuarioLogado().getUsername();
        Usuario usuario = usuarioRepository.findByUsername(username);

        if (usuario == null) {
            throw new DadosInvalidosException("Usuário não encontrado");
        }

        return new ArrayList<>(usuario.getAcessos());
    }

    @Override
    public boolean usuarioLogadoTemAcesso(Acesso acesso) {
        return listarAcessosUsuarioLogado().contains(acesso);
    }

    @Override
    public void assertUsuarioLogadoTemAcesso(Acesso acesso) {
        if (!usuarioLogadoTemAcesso(acesso)) {
            throw new AcessoException("Usuário sem acesso ao recurso");
        }
    }

    @Override
    public void criarUsuario(String username, String password) {
        assertUsuarioLogadoTemAcesso(Acesso.ADMIN);

        Usuario usuario = usuarioRepository.findByUsername(username);

        if (usuario != null) {
            throw new DadosInvalidosException("Usuário já existe");
        }

        validarSenha(password);

        usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setAtivo(true);
        usuario.setAcessos(new ArrayList<>());
        usuario.setPassword(passwordToMd5(password));

        usuarioRepository.save(usuario);
    }

    @Override
    public void alterarUsuario(Usuario usuario) {
        assertUsuarioLogadoTemAcesso(Acesso.ADMIN);

        Usuario usuarioBd = usuarioRepository.getOne(usuario.getId());

        if (usuario == null) {
            throw new DadosInvalidosException("Usuário não encontrado");
        }

        usuarioBd.setUsername(usuario.getUsername());
        usuarioBd.getAcessos().clear();
        usuarioBd.getAcessos().addAll(usuario.getAcessos());
        usuarioBd.setAtivo(usuario.isAtivo());

        usuarioRepository.save(usuarioBd);
    }

    @Override
    public void alterarSenha(String username, String oldPassword, String newPassword) {
        if (!UtilSessao.getUsuarioLogado().getUsername().equals(username)) {
            assertUsuarioLogadoTemAcesso(Acesso.ADMIN);
        }

        Usuario usuario = usuarioRepository.findByUsername(username);

        if (usuario == null) {
            throw new AcessoException("Usuário ou senha antiga inválidos");
        }

        String oldMd5 = passwordToMd5(oldPassword);

        if (!oldMd5.equalsIgnoreCase(usuario.getPassword())) {
            throw new AcessoException("Usuário ou senha antiga inválidos");
        }

        if (!usuario.isAtivo()) {
            throw new AcessoException("Usuário inativo");
        }

        validarSenha(newPassword);

        String newMd5 = passwordToMd5(newPassword);

        usuario.setPassword(newMd5);

        usuarioRepository.save(usuario);
    }

    @Override
    public void definirSenha(String username, String password) {
        if (!UtilSessao.getUsuarioLogado().getUsername().equals(username)) {
            assertUsuarioLogadoTemAcesso(Acesso.ADMIN);
        }

        Usuario usuario = usuarioRepository.findByUsername(username);

        if (usuario == null) {
            throw new AcessoException("Usuário não encontrado.");
        }

        validarSenha(password);
        String newMd5 = passwordToMd5(password);

        usuario.setPassword(newMd5);

        usuarioRepository.save(usuario);
    }

    @Override
    public void alterarAtivacao(String username, boolean statusAtivo) {
        assertUsuarioLogadoTemAcesso(Acesso.ADMIN);

        Usuario usuario = usuarioRepository.findByUsername(username);

        if (usuario == null) {
            throw new AcessoException("Usuário não encontrado");
        }

        usuario.setAtivo(statusAtivo);

        usuarioRepository.save(usuario);
    }

    @Override
    public void alterarAcessos(String username, List<Acesso> acessos) {
        assertUsuarioLogadoTemAcesso(Acesso.ADMIN);

        Usuario usuario = usuarioRepository.findByUsername(username);

        if (usuario == null) {
            throw new AcessoException("Usuário não encontrado");
        }

        usuario.setAcessos(acessos);

        usuarioRepository.save(usuario);
    }

    private void validarSenha(String password) {
        if (password == null || password.isEmpty()) {
            throw new DadosInvalidosException("Senha é obrigatória");
        }

        if (password.contains(" ")) {
            throw new DadosInvalidosException("Senha nao pode conter espaços");
        }

        if (password.length() < 4) {
            throw new DadosInvalidosException("Senha precisa ter pelo menos tamanho 4");
        }
    }

    @Override
    public void login(String username, String password) {
        Usuario usuario = usuarioRepository.findByUsername(username);

        if (usuario == null) {
            throw new AcessoException("Usuário ou senha inválidos");
        }

        String md5 = passwordToMd5(password);

        if (!md5.equalsIgnoreCase(usuario.getPassword())) {
            throw new AcessoException("Usuário ou senha inválidos");
        }

        if (!usuario.isAtivo()) {
            throw new AcessoException("Usuário inativo");
        }

        Usuario usuarioSessao = new Usuario();
        usuarioSessao.setId(usuario.getId());
        usuarioSessao.setUsername(usuario.getUsername());
        usuarioSessao.setAtivo(usuario.isAtivo());
        usuarioSessao.setAcessos(new ArrayList<>(usuario.getAcessos()));

        UtilSessao.putUsuarioLogado(usuarioSessao);
    }

    @Override
    public void removerUsuario(String username) {
        assertUsuarioLogadoTemAcesso(Acesso.ADMIN);

        Usuario usuario = usuarioRepository.findByUsername(username);

        if (usuario.getAcessos().contains(Acesso.ADMIN)) {
            throw new AcessoException("Usuários ADMIN não podem ser removidos.");
        }

        if (usuario == null) {
            throw new NaoEncontradoException("Usuário não encontrado");
        }

        usuarioRepository.delete(usuario);
    }

    private String passwordToMd5(String password) {
        return DigestUtils.md5Hex(password).toUpperCase();
    }
}
