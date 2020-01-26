package br.com.maxgontijo.pmgo.planilhasveiculos.dto;

import java.util.List;

public class RecorrenciaDto extends ModelDto {
	private String nome;
	private List<Integer> listas;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Integer> getListas() {
		return listas;
	}

	public void setListas(List<Integer> listas) {
		this.listas = listas;
	}
}
