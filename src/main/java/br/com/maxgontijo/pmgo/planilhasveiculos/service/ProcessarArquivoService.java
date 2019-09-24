package br.com.maxgontijo.pmgo.planilhasveiculos.service;

import java.io.InputStream;

public interface ProcessarArquivoService {
	public int processarArquivo(String nomeArquivo, InputStream input, String identificacaoRadar);
}
