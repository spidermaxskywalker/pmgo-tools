package br.com.maxgontijo.pmgo.planilhasveiculos.repository;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends MaxGenericJpaRepository<Usuario, Integer> {
    Usuario findByUsername(String username);
}
