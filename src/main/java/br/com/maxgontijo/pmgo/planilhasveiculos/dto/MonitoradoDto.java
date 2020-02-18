package br.com.maxgontijo.pmgo.planilhasveiculos.dto;

public class MonitoradoDto extends ModelDto {
	private Long linhaPlanilha;
	private String nome;
	private String nomePais;
	private String idMonitorado;
	private String alcunha;
	private String telefones;
	private String endereco;
	private String cidade;
	private String estabelecimento;
	private String tipoPerfil;
	private String nomePerfil;
	private String dataInclusao;
	private String artigos;

	public Long getLinhaPlanilha() {
		return linhaPlanilha;
	}

	public void setLinhaPlanilha(Long linhaPlanilha) {
		this.linhaPlanilha = linhaPlanilha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomePais() {
		return nomePais;
	}

	public void setNomePais(String nomePais) {
		this.nomePais = nomePais;
	}

	public String getIdMonitorado() {
		return idMonitorado;
	}

	public void setIdMonitorado(String idMonitorado) {
		this.idMonitorado = idMonitorado;
	}

	public String getAlcunha() {
		return alcunha;
	}

	public void setAlcunha(String alcunha) {
		this.alcunha = alcunha;
	}

	public String getTelefones() {
		return telefones;
	}

	public void setTelefones(String telefones) {
		this.telefones = telefones;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstabelecimento() {
		return estabelecimento;
	}

	public void setEstabelecimento(String estabelecimento) {
		this.estabelecimento = estabelecimento;
	}

	public String getTipoPerfil() {
		return tipoPerfil;
	}

	public void setTipoPerfil(String tipoPerfil) {
		this.tipoPerfil = tipoPerfil;
	}

	public String getNomePerfil() {
		return nomePerfil;
	}

	public void setNomePerfil(String nomePerfil) {
		this.nomePerfil = nomePerfil;
	}

	public String getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(String dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public String getArtigos() {
		return artigos;
	}

	public void setArtigos(String artigos) {
		this.artigos = artigos;
	}
}
