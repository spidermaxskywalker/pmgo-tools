package br.com.maxgontijo.pmgo.planilhasveiculos.repository;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Radar;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.Veiculo;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.VeiculoRadar;

@Repository
public interface VeiculoRadarRepository extends MaxGenericJpaRepository<VeiculoRadar, Integer> {
	public VeiculoRadar findByVeiculoAndRadarAndDataHora(Veiculo v, Radar r, Date datahora);

	public List<VeiculoRadar> findByVeiculoOrderByDataHoraDesc(Veiculo v);
}
