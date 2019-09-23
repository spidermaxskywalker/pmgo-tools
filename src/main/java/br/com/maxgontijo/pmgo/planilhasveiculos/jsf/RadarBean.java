package br.com.maxgontijo.pmgo.planilhasveiculos.jsf;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Radar;
import br.com.maxgontijo.pmgo.planilhasveiculos.repository.RadarRepository;

@ManagedBean
@ViewScoped
public class RadarBean {
	private @Autowired RadarRepository radarRepository;

	private List<Radar> radares;

	@PostConstruct
	public void init() {
		this.radares = this.radarRepository.findAll(Sort.by("placa"));
	}

	public List<Radar> getRegistros() {
		return radares;
	}
}
