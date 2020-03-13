package br.com.maxgontijo.pmgo.planilhasveiculos.service.impl;

import br.com.maxgontijo.pmgo.planilhasveiculos.dto.MandadoDto;
import br.com.maxgontijo.pmgo.planilhasveiculos.dto.MonitoradoDto;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.ArquivoCsv;
import br.com.maxgontijo.pmgo.planilhasveiculos.service.ProcessarArquivoMandadosMonitoradosService;
import br.com.maxgontijo.pmgo.planilhasveiculos.util.CharsetDetector;
import br.com.maxgontijo.pmgo.planilhasveiculos.util.DadosInvalidosException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("processarArquivoMandadosMonitoradosService")
public class ProcessarArquivoMandadosMonitoradosServiceImpl implements ProcessarArquivoMandadosMonitoradosService {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public ArquivoCsv processarArquivoCsv(InputStream inMonitorados, char separadorMonitorados) {
        try (BufferedReader bf = new BufferedReader(CharsetDetector.createTextInputStreamReader(inMonitorados))) {
            ArquivoCsv monitorados = new ArquivoCsv();

            String line = bf.readLine();
            String[] nomesColunas = line.split(String.valueOf(separadorMonitorados), -1);

            for (String c : nomesColunas) {
                monitorados.getNomesColunas().add(c);
            }

            while ((line = bf.readLine()) != null) {
                String[] tupla = line.split(String.valueOf(separadorMonitorados), -1);
                monitorados.getTuplas().add(tupla);
            }

            return monitorados;
        } catch (IOException e) {
            throw new DadosInvalidosException("Problema ao ler arquivo de monitorados.");
        }
    }

    @Override
    public List<MandadoDto> processarArquivo(InputStream inMonitorados, char separadorMonitorados, InputStream inMandados, char separadorMandados) {
        List<MonitoradoDto> monitorados = lerArquivoMonitorados(inMonitorados, separadorMonitorados);
        List<MandadoDto> mandados = lerArquivoMandados(monitorados, inMandados, separadorMandados);
        return mandados;
    }

    private List<MandadoDto> lerArquivoMandados(List<MonitoradoDto> monitorados, InputStream inMandados, char separadorMandados) {
        Map<String, List<MonitoradoDto>> monitoradosMap = new TreeMap<>();
        for (MonitoradoDto m : monitorados) {
            List<MonitoradoDto> regs = monitoradosMap.get(m.getNome());
            if (regs == null) {
                regs = new ArrayList<>(2);
                monitoradosMap.put(m.getNome(), regs);
            }
            regs.add(m);
        }

        try (BufferedReader bf = new BufferedReader(CharsetDetector.createTextInputStreamReader(inMandados))) {
            String line;
            List<MandadoDto> mandados = new ArrayList<>();
            bf.readLine();
            while ((line = bf.readLine()) != null) {
                String[] campos = line.split(String.valueOf(separadorMandados), -1);

                List<MonitoradoDto> regs = monitoradosMap.get(getString(campos, 1));

                if (regs != null) {
                    for (MonitoradoDto m : regs) {
                        MandadoDto mandado = new MandadoDto();
                        mandado.setNumero(getString(campos, 0));
                        mandado.setDataExpedicao(getDate(campos, 7));
                        mandado.setNomeMae(getString(campos, 3, true));
                        mandado.setMonitorado(m);
                        mandados.add(mandado);
                    }
                }
            }

            mandados.sort((m1, m2) -> {
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

            return mandados;
        } catch (IOException e) {
            throw new DadosInvalidosException("Problema ao ler arquivo de monitorados.");
        }
    }

    private List<MonitoradoDto> lerArquivoMonitorados(InputStream inMonitorados, char separadorMonitorados) {
        try (BufferedReader bf = new BufferedReader(CharsetDetector.createTextInputStreamReader(inMonitorados))) {
            String line;
            List<MonitoradoDto> monitorados = new ArrayList<>();
            bf.readLine();
            while ((line = bf.readLine()) != null) {
                String[] campos = line.split(String.valueOf(separadorMonitorados), -1);

                MonitoradoDto m = new MonitoradoDto();
                m.setLinhaPlanilha(getLong(campos, 0));
                m.setNome(getString(campos, 1, true));
                m.setNomePais(getString(campos, 9));

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

    private Date getDate(String[] linha, int i) {
        try {
            return sdf.parse(linha[i].trim());
        } catch (Exception e) {
            return null;
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
