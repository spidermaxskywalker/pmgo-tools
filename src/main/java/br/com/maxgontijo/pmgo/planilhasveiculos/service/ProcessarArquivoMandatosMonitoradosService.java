package br.com.maxgontijo.pmgo.planilhasveiculos.service;

import br.com.maxgontijo.pmgo.planilhasveiculos.dto.MandatoDto;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.ArquivoCsv;

import java.io.InputStream;
import java.util.List;

public interface ProcessarArquivoMandatosMonitoradosService {
    public ArquivoCsv processarArquivoCsv(InputStream inMonitorados, char separadorMonitorados);

    public List<MandatoDto> processarArquivo(InputStream inMonitorados, char separadorMonitorados, InputStream inMandatos, char separadorMandato);
}
