package br.com.maxgontijo.pmgo.planilhasveiculos.service;

import java.io.InputStream;
import java.util.List;

import br.com.maxgontijo.pmgo.planilhasveiculos.dto.MandatoDto;

public interface ProcessarArquivoMandatosMonitoradosService {
	public List<MandatoDto> processarArquivo(InputStream inMonitorados, char separadorMonitorados, InputStream inMandatos, char separadorMandato);
}
