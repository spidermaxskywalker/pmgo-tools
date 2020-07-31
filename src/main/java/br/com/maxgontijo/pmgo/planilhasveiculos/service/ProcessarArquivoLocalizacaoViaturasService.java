package br.com.maxgontijo.pmgo.planilhasveiculos.service;

import java.io.InputStream;
import java.util.List;

import br.com.maxgontijo.pmgo.planilhasveiculos.dto.RecorrenciaDto;

public interface ProcessarArquivoRecorrenciaService {
	public List<RecorrenciaDto> processarArquivo(InputStream input);
}
