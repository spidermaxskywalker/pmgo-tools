package br.com.maxgontijo.pmgo.planilhasveiculos.service;

import br.com.maxgontijo.pmgo.planilhasveiculos.dto.LocalizacaoViaturaDto;
import br.com.maxgontijo.pmgo.planilhasveiculos.util.Progresso;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface ProcessarArquivoLocalizacaoViaturasService {
    List<LocalizacaoViaturaDto> processarArquivo(InputStream input, OutputStream output, Progresso progresso);
}
