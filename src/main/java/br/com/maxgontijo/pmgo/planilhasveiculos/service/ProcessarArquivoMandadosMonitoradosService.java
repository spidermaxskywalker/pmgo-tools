package br.com.maxgontijo.pmgo.planilhasveiculos.service;

import br.com.maxgontijo.pmgo.planilhasveiculos.dto.MandadoDto;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.ArquivoCsv;

import java.io.InputStream;
import java.util.List;

public interface ProcessarArquivoMandadosMonitoradosService {
    public ArquivoCsv processarArquivoCsv(InputStream inMonitorados, char separadorMonitorados);

    public List<MandadoDto> processarArquivo(InputStream inMonitorados, char separadorMonitorados, InputStream inMandados, char separadorMandado);
}
