package br.com.maxgontijo.pmgo.planilhasveiculos.jsf;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Veiculo;
import br.com.maxgontijo.pmgo.planilhasveiculos.repository.VeiculoRepository;

@ManagedBean
@ViewScoped
public class VeiculoBean {
	private @Autowired VeiculoRepository veiculoRepository;

	private List<Veiculo> veiculos;

	@PostConstruct
	public void init() {
		this.veiculos = this.veiculoRepository.findAll(Sort.by("placa"));
	}

	public List<Veiculo> getRegistros() {
		return veiculos;
	}
}
