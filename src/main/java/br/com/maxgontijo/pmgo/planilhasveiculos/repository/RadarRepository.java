package br.com.maxgontijo.pmgo.planilhasveiculos.repository;

import org.springframework.stereotype.Repository;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Radar;

@Repository
public interface RadarRepository extends MaxGenericJpaRepository<Radar, Integer> {
	public Radar findByCodigo(String codigo);
}
