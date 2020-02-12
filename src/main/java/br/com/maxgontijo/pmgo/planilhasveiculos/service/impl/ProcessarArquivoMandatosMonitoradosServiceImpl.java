package br.com.maxgontijo.pmgo.planilhasveiculos.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.maxgontijo.pmgo.planilhasveiculos.dto.MandatoDto;
import br.com.maxgontijo.pmgo.planilhasveiculos.dto.MonitoradoDto;
import br.com.maxgontijo.pmgo.planilhasveiculos.service.ProcessarArquivoMandatosMonitoradosService;
import br.com.maxgontijo.pmgo.planilhasveiculos.util.DadosInvalidosException;

@Service("processarArquivoMandatosMonitoradosService")
public class ProcessarArquivoMandatosMonitoradosServiceImpl implements ProcessarArquivoMandatosMonitoradosService {
	@Override
	@Transactional
	public List<MandatoDto> processarArquivo(InputStream inMonitorados, char separadorMonitorados, InputStream inMandatos, char separadorMandatos) {
		List<MonitoradoDto> monitorados = lerArquivoMonitorados(inMonitorados, separadorMonitorados);
		List<MandatoDto> mandatos = lerArquivoMandatos(monitorados, inMandatos, separadorMandatos);
		return mandatos;
	}

	private List<MandatoDto> lerArquivoMandatos(List<MonitoradoDto> monitorados, InputStream inMandatos, char separadorMandatos) {
		Map<String, List<MonitoradoDto>> monitoradosMap = new TreeMap<>();
		for (MonitoradoDto m : monitorados) {
			List<MonitoradoDto> regs = monitoradosMap.get(m.getNome());
			if (regs == null) {
				regs = new ArrayList<>(2);
				monitoradosMap.put(m.getNome(), regs);
			}
			regs.add(m);
		}

		try (BufferedReader bf = new BufferedReader(new InputStreamReader(inMandatos))) {
			String line;
			List<MandatoDto> mandatos = new ArrayList<>();
			bf.readLine();
			while ((line = bf.readLine()) != null) {
				String[] campos = line.split(String.valueOf(separadorMandatos), -1);

				List<MonitoradoDto> regs = monitoradosMap.get(getString(campos, 1));

				if (regs != null) {
					for (MonitoradoDto m : regs) {
						MandatoDto mandato = new MandatoDto();
						mandato.setNumero(getString(campos, 0));
						mandato.setMonitorado(m);
						mandatos.add(mandato);
					}
				}
			}

			mandatos.sort((m1, m2) -> {
				int c = m1.getMonitorado().getNome().compareTo(m2.getMonitorado().getNome());
				if (c == 0) {
					c = m1.getNumero().compareTo(m2.getNumero());
					if (c == 0) {
						c = m1.getMonitorado().getIdMonitorado().compareTo(m2.getMonitorado().getIdMonitorado());
						if (c == 0) {
							c = m1.getMonitorado().getLinhaPlanilha().compareTo(m2.getMonitorado().getLinhaPlanilha());
						}
					}
				}
				return c;
			});

			return mandatos;
		} catch (IOException e) {
			throw new DadosInvalidosException("Problema ao ler arquivo de monitorados.");
		}
	}

	private List<MonitoradoDto> lerArquivoMonitorados(InputStream inMonitorados, char separadorMonitorados) {
		try (BufferedReader bf = new BufferedReader(new InputStreamReader(inMonitorados))) {
			String line;
			List<MonitoradoDto> monitorados = new ArrayList<>();
			bf.readLine();
			while ((line = bf.readLine()) != null) {
				String[] campos = line.split(String.valueOf(separadorMonitorados), -1);

				MonitoradoDto m = new MonitoradoDto();
				m.setLinhaPlanilha(getLong(campos, 0));
				m.setNome(getString(campos, 1, true));

				if (m.getNome() == null || m.getNome().isEmpty()) {
					continue;
				}

				m.setIdMonitorado(getString(campos, 2));
				m.setAlcunha(getString(campos, 4, true));
				m.setTelefones(getString(campos, 10));
				m.setEndereco(getMultiplasStrings(campos, ", ", 11, 12, 15));
				m.setCidade(getMultiplasStrings(campos, " - ", 13, 14));
				m.setEstabelecimento(getString(campos, 16));
				m.setTipoPerfil(getString(campos, 17));
				m.setNomePerfil(getString(campos, 18));
				m.setDataInclusao(getString(campos, 19));
				m.setArtigos(getString(campos, 21));

				monitorados.add(m);
			}

			monitorados.sort((m1, m2) -> {
				int c = m1.getNome().compareTo(m2.getNome());
				if (c == 0) {
					c = m1.getIdMonitorado().compareTo(m2.getIdMonitorado());
					if (c == 0) {
						c = m1.getLinhaPlanilha().compareTo(m2.getLinhaPlanilha());
					}
				}
				return c;
			});

			return monitorados;
		} catch (IOException e) {
			throw new DadosInvalidosException("Problema ao ler arquivo de monitorados.");
		}
	}

	private Long getLong(String[] linha, int i) {
		try {
			return Long.parseLong(linha[i].trim());
		} catch (Exception e) {
			return -1L;
		}
	}

	private String getMultiplasStrings(String[] linha, String separador, int... pos) {
		try {
			StringBuilder sb = new StringBuilder();
			for (int i : pos) {
				String s = getString(linha, i);
				if (s != null && !s.isEmpty()) {
					sb.append(separador).append(s);
				}
			}
			return sb.length() > 0 ? sb.substring(separador.length()) : "";
		} catch (Exception e) {
			return "";
		}
	}

	private String getString(String[] linha, int i) {
		return getString(linha, i, false);
	}

	private String getString(String[] linha, int i, boolean limpar) {
		try {
			return limpar ? limparTexto(linha[i]) : linha[i].trim();
		} catch (Exception e) {
			return "";
		}
	}

	private String limparTexto(String nome) {
		nome = Normalizer
				.normalize(nome, Normalizer.Form.NFD)
				.replaceAll("[^\\p{ASCII}]", "");
		nome = nome.replaceAll("[^a-zA-Z0-9 ]", " ").replaceAll("\\s+", " ").trim().toUpperCase();
		return nome;
	}
}
