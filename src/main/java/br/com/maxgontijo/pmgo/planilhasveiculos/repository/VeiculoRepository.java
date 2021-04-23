package br.com.maxgontijo.pmgo.planilhasveiculos.repository;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Veiculo;
import org.springframework.stereotype.Repository;

@Repository
public interface VeiculoRepository extends MaxGenericJpaRepository<Veiculo, Integer> {
    Veiculo findByPlaca(String placa);
}
