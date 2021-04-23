package br.com.maxgontijo.pmgo.planilhasveiculos.service;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Veiculo;

import java.util.List;

public interface VeiculoService {
    List<Veiculo> extrairVeiculosDeListaPlacas(String placas);

    void salvarVeiculos(List<Veiculo> veiculos);
}
