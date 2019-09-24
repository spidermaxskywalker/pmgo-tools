package br.com.maxgontijo.pmgo.planilhasveiculos.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Radar;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.Veiculo;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.VeiculoRadar;
import br.com.maxgontijo.pmgo.planilhasveiculos.repository.RadarRepository;
import br.com.maxgontijo.pmgo.planilhasveiculos.repository.VeiculoRadarRepository;
import br.com.maxgontijo.pmgo.planilhasveiculos.repository.VeiculoRepository;
import br.com.maxgontijo.pmgo.planilhasveiculos.service.ProcessarArquivoService;

@Service("processarArquivoService")
public class ProcessarArquivoServiceImpl implements ProcessarArquivoService {
	private SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
	private SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd'/'MM'/'yyyy'-'HH':'mm':'ss");

	private @Autowired VeiculoRepository veiculoRepository;
	private @Autowired RadarRepository radarRepository;
	private @Autowired VeiculoRadarRepository veiculoRadarRepository;

	@Override
	@Transactional
	public int processarArquivo(String nomeArquivo, InputStream input, String identificacaoRadar) {
		XSSFWorkbook workbook;

		try {
			workbook = new XSSFWorkbook(input);
		} catch (Exception e) {
			throw new RuntimeException("Arquivo inválido.", e);
		}

		int contador = 0;

		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			XSSFSheet sheet = workbook.getSheetAt(i);

			if (identificacaoRadar == null) {
				identificacaoRadar = sheet.getSheetName().split("_")[0];
			}
			if (identificacaoRadar == null) {
				identificacaoRadar = "SEM_IDENTIFICACAO";
			}
			identificacaoRadar = identificacaoRadar.trim().toUpperCase();

			Radar radar = radarRepository.findByCodigo(identificacaoRadar);
			if (radar == null) {
				radar = new Radar();
				radar.setCodigo(identificacaoRadar);
				radarRepository.save(radar);
			}

			for (int j = 0; j <= sheet.getLastRowNum(); j++) {
				XSSFRow row = sheet.getRow(j);

				if (row != null) {
					Date dataHora = getCellDataTime(row.getCell(0), row.getCell(1));
					String placa;
					String sentido;
					try {
						placa = row.getCell(2).getStringCellValue().trim().toUpperCase();
					} catch (Exception e) {
						placa = null;
					}
					try {
						sentido = row.getCell(3).getStringCellValue().trim();
					} catch (Exception e) {
						sentido = null;
					}

					if (dataHora != null && placa != null) {
						Veiculo veiculo = veiculoRepository.findByPlaca(placa);
						if (veiculo == null) {
							veiculo = new Veiculo();
							veiculo.setPlaca(placa);
							veiculoRepository.save(veiculo);
						}

						VeiculoRadar vr = veiculoRadarRepository.findByVeiculoAndRadarAndDataHora(veiculo, radar, dataHora);
						if (vr == null) {
							vr = new VeiculoRadar();
							vr.setVeiculo(veiculo);
							vr.setRadar(radar);
							vr.setDataHora(dataHora);
							vr.setSentido(sentido);
							veiculoRadarRepository.save(vr);
							contador++;
						}
					}
				}
			}
		}

		try {
			workbook.close();
		} catch (IOException e) {
			throw new RuntimeException("Erro ao fechar a arquivo.", e);
		}

		return contador;
	}

	private Date getCellDate(XSSFCell cell) {
		try {
			try {
				return cell.getDateCellValue();
			} catch (Exception e) {
				String value = cell.getStringCellValue();
				return sdfDate.parse(value);
			}
		} catch (Exception e) {
			return null;
		}
	}

	private Date getCellTime(XSSFCell cell) {
		try {
			try {
				return cell.getDateCellValue();
			} catch (Exception e) {
				String value = cell.getStringCellValue();
				return sdfTime.parse(value);
			}
		} catch (Exception e) {
			return null;
		}
	}

	private Date getCellDataTime(XSSFCell cellDate, XSSFCell cellTime) {
		try {
			Date data = getCellDate(cellDate);
			Date time = getCellTime(cellTime);

			if (time == null) {
				return data;
			} else {
				return sdfDateTime.parse(sdfDate.format(data) + "-" + sdfTime.format(time));
			}
		} catch (Exception e) {
			return null;
		}
	}
}
