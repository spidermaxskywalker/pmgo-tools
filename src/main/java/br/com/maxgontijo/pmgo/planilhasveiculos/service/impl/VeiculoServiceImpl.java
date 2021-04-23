package br.com.maxgontijo.pmgo.planilhasveiculos.service.impl;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Veiculo;
import br.com.maxgontijo.pmgo.planilhasveiculos.repository.VeiculoRepository;
import br.com.maxgontijo.pmgo.planilhasveiculos.service.VeiculoService;
import br.com.maxgontijo.pmgo.planilhasveiculos.util.DadosInvalidosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("veiculoService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class VeiculoServiceImpl implements VeiculoService {
    @Autowired
    private VeiculoRepository veiculoRepository;

    @Override
    public List<Veiculo> extrairVeiculosDeListaPlacas(String placasStr) {
        if (placasStr == null) {
            throw new DadosInvalidosException("É necessário informar ao menos uma placa.");
        }

        String[] placas = placasStr.trim()
                .replaceAll("\r", "")
                .replaceAll("\n", " ")
                .replaceAll("\t", " ")
                .replaceAll("-", "")
                .replaceAll("\\s{2,}", " ")
                .toUpperCase().split(" ");


        if (placas.length == 0 || placas[0].length() == 0) {
            throw new DadosInvalidosException("É necessário informar ao menos uma placa.");
        }

        Set<String> placasSet = new TreeSet<>(Arrays.asList(placas));

        List<Veiculo> veiculos = new ArrayList<>();

        for (String placa : placasSet) {
            Veiculo v = veiculoRepository.findByPlaca(placa);
            if (v == null) {
                v = new Veiculo();
                v.setPlaca(placa);
            }
            veiculos.add(v);
        }

        return veiculos;
    }

    @Override
    public void salvarVeiculos(List<Veiculo> veiculos) {
        for (Veiculo v : veiculos) {
            veiculoRepository.save(v);
        }
    }
}
