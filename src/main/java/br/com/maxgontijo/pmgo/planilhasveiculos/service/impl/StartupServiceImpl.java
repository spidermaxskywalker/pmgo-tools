package br.com.maxgontijo.pmgo.planilhasveiculos.service.impl;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Usuario;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.enums.Acesso;
import br.com.maxgontijo.pmgo.planilhasveiculos.repository.UsuarioRepository;
import br.com.maxgontijo.pmgo.planilhasveiculos.service.StartupService;
import br.com.maxgontijo.pmgo.planilhasveiculos.util.UtilRecurso;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

@Service("startupService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class StartupServiceImpl implements StartupService {
    private @Autowired
    EntityManager entityManager;

    private @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void verificarAdminUser() {
        Properties props = UtilRecurso.loadProperties("application.properties");

        String adminUser = props.getProperty("pmgo.adminuser.username");
        String adminPass = props.getProperty("pmgo.adminuser.password");

        Usuario user = this.usuarioRepository.findByUsername(adminUser);

        if (user == null) {
            user = new Usuario();
            user.setUsername(adminUser);
            user.setPassword(DigestUtils.md5Hex(adminPass).toUpperCase());
        }

        user.setAtivo(true);
        user.setAcessos(new ArrayList<>(Arrays.asList(Acesso.values())));
        this.usuarioRepository.save(user);
    }
}
