package br.com.maxgontijo.pmgo.planilhasveiculos.jsf;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Veiculo;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.VeiculoRadar;
import br.com.maxgontijo.pmgo.planilhasveiculos.repository.VeiculoRadarRepository;
import br.com.maxgontijo.pmgo.planilhasveiculos.repository.VeiculoRepository;

@ManagedBean
@ViewScoped
public class VeiculoBean extends GenericJsfBean {
	private @Autowired VeiculoRepository veiculoRepository;
	private @Autowired VeiculoRadarRepository veiculoRadarRepository;

	private List<Veiculo> veiculos;

	private Veiculo veiculo;

	private List<VeiculoRadar> registrosVeiculo;

	@PostConstruct
	public void init() {
		this.veiculos = this.veiculoRepository.findAll(Sort.by("placa"));
	}

	public void showRegistrosVeiculo(ActionEvent event) {
		this.veiculo = (Veiculo) event.getComponent().getAttributes().get("registro");
		this.registrosVeiculo = this.veiculoRadarRepository.findByVeiculoOrderByDataHoraDesc(veiculo);
	}

	public void hideRegistrosVeiculo() {
		this.veiculo = null;
		this.registrosVeiculo = null;
	}

	public Veiculo getRegistro() {
		return veiculo;
	}

	public void setRegistro(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public List<Veiculo> getRegistros() {
		return veiculos;
	}

	public List<VeiculoRadar> getRegistrosVeiculo() {
		return registrosVeiculo;
	}
}
