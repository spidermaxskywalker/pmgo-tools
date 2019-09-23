package br.com.maxgontijo.pmgo.planilhasveiculos.repository;

import org.springframework.stereotype.Repository;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Veiculo;

@Repository
public interface VeiculoRepository extends MaxGenericJpaRepository<Veiculo, Integer> {
}
