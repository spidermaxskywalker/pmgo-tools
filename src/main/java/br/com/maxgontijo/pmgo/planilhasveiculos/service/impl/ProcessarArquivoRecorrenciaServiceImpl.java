package br.com.maxgontijo.pmgo.planilhasveiculos.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.maxgontijo.pmgo.planilhasveiculos.dto.RecorrenciaDto;
import br.com.maxgontijo.pmgo.planilhasveiculos.service.ProcessarArquivoRecorrenciaService;

@Service("processarArquivoRecorrenciaService")
public class ProcessarArquivoRecorrenciaServiceImpl implements ProcessarArquivoRecorrenciaService {
	@Override
	@Transactional
	public List<RecorrenciaDto> processarArquivo(InputStream input) {
		XSSFWorkbook workbook;

		try {
			workbook = new XSSFWorkbook(input);
		} catch (Exception e) {
			throw new RuntimeException("Arquivo inválido.", e);
		}

		Map<String, List<Integer>> ocorrencias = new TreeMap<>();

		int qtdeAnterior = 0;
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			XSSFSheet sheet = workbook.getSheetAt(i);

			int qtde = contarListas(sheet);

			for (int k = 0; k < qtde; k++) {
				int colunaNome = k * 4;

				for (int j = 4; j <= sheet.getLastRowNum(); j++) {
					XSSFRow row = sheet.getRow(j);

					if (row == null) {
						break;
					}
					if (row.getCell(colunaNome) == null) {
						break;
					}
					String nome = getCellString(row.getCell(colunaNome));

					if (nome == null) {
						break;
					}

					nome = limparNome(nome);

					List<Integer> locais = ocorrencias.get(nome);
					if (locais == null) {
						locais = new ArrayList<Integer>(Math.min(10, qtde));
						ocorrencias.put(nome, locais);
					}

					locais.add(qtdeAnterior + k);
				}
			}
		}

		try {
			workbook.close();
		} catch (IOException e) {
			throw new RuntimeException("Erro ao fechar a arquivo.", e);
		}

		List<RecorrenciaDto> recorrencias = new ArrayList<>(ocorrencias.size());

		for (Entry<String, List<Integer>> e : ocorrencias.entrySet()) {
			RecorrenciaDto rec = new RecorrenciaDto();
			rec.setNome(e.getKey());
			rec.setListas(e.getValue());
			recorrencias.add(rec);
		}

		recorrencias.sort((r1, r2) -> {
			int c = r2.getListas().size() - r1.getListas().size();
			if (c == 0) {
				c = r1.getNome().compareTo(r2.getNome());
			}
			return c;
		});

		return recorrencias;
	}

	private int contarListas(XSSFSheet sheet) {
		XSSFRow row = sheet.getRow(0);
		int count = 0;
		if (row != null) {
			while (true) {
				String latitudeLabel = getCellString(row.getCell(count * 4));
				if (latitudeLabel == null || !latitudeLabel.trim().toUpperCase().equals("LATITUDE")) {
					break;
				} else {
					count++;
				}
			}
		}
		return count;
	}

	private String limparNome(String nome) {
		nome = Normalizer
				.normalize(nome, Normalizer.Form.NFD)
				.replaceAll("[^\\p{ASCII}]", "");
		nome = nome.replaceAll("[^a-zA-Z0-9 ]", " ").replaceAll("\\s+", " ").trim().toUpperCase();
		return nome;
	}

	private String getCellString(XSSFCell cell) {
		try {
			return cell.getStringCellValue();
		} catch (Exception e) {
			return null;
		}
	}
}
