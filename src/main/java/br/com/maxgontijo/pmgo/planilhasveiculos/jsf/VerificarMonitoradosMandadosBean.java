package br.com.maxgontijo.pmgo.planilhasveiculos.jsf;

import br.com.maxgontijo.pmgo.planilhasveiculos.dto.MandadoDto;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.ArquivoCsv;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.enums.Acesso;
import br.com.maxgontijo.pmgo.planilhasveiculos.service.ProcessarArquivoMandadosMonitoradosService;
import br.com.maxgontijo.pmgo.planilhasveiculos.util.UtilZip;
import org.primefaces.component.export.ExcelOptions;
import org.primefaces.component.export.PDFOptions;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@ManagedBean
@ViewScoped
@AcessoBean(Acesso.MANDADOS_MONITORADOS)
public class VerificarMonitoradosMandadosBean extends GenericJsfBean {
    private @Autowired
    ProcessarArquivoMandadosMonitoradosService processarArquivoMandadosMonitoradosService;

    private ArquivoCsv arquivoMonitorados;
    private ArquivoCsv arquivoMandados;

    private List<MandadoDto> mandados;

    private UploadedFile fileMonitorados;
    private UploadedFile fileMandados;

    private String separadorMonitorados = ";";
    private String separadorMandado = ",";

    private ExcelOptions excelOpt;
    private PDFOptions pdfOpt;

    @PostConstruct
    public void init() {
        excelOpt = new ExcelOptions();
        excelOpt.setFacetBgColor("#cccccc");
        excelOpt.setFacetFontSize("11");
        excelOpt.setFacetFontColor("#000000");
        excelOpt.setFacetFontStyle("BOLD");
        excelOpt.setCellFontColor("#000000");
        excelOpt.setCellFontSize("10");

        pdfOpt = new PDFOptions();
        pdfOpt.setFacetBgColor("#cccccc");
        pdfOpt.setFacetFontSize("11");
        pdfOpt.setFacetFontColor("#000000");
        pdfOpt.setFacetFontStyle("BOLD");
        pdfOpt.setCellFontColor("#000000");
        pdfOpt.setCellFontSize("10");
    }

    public void handleFileUploadMonitorados(FileUploadEvent event) {
        try {
            this.fileMonitorados = event.getFile();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", event.getFile().getFileName() + " foi enviado.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", event.getFile().getFileName() + " NÃO foi enviado.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Detalhe do Erro", e.getMessage() != null ? e.getMessage() : e.toString());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void handleFileUploadMandados(FileUploadEvent event) {
        try {
            this.fileMandados = event.getFile();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", event.getFile().getFileName() + " foi enviado.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", event.getFile().getFileName() + " NÃO foi enviado.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Detalhe do Erro", e.getMessage() != null ? e.getMessage() : e.toString());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void processar() {
        try {
            InputStream inMonitorados = extractInputStream(fileMonitorados);
            InputStream inMandados = extractInputStream(fileMandados);

            mandados = processarArquivoMandadosMonitoradosService.processarArquivo(inMonitorados, separadorMonitorados.charAt(0), inMandados, separadorMandado.charAt(0));

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Arquivos foram processados.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao processar arquivos.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Detalhe do Erro", e.getMessage() != null ? e.getMessage() : e.toString());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void limpar() {
        this.mandados = null;
        this.fileMonitorados = null;
        this.fileMandados = null;
        this.arquivoMonitorados = null;
        this.arquivoMandados = null;
    }

    public void mostrarArquivoCsvMonitorados() {
        if (arquivoMonitorados == null && fileMonitorados != null && separadorMonitorados != null) {
            arquivoMonitorados = processarArquivoCsv(fileMonitorados, separadorMonitorados);
        }
    }

    public void mostrarArquivoCsvMandados() {
        if (arquivoMandados == null && fileMandados != null && separadorMandado != null) {
            arquivoMandados = processarArquivoCsv(fileMandados, separadorMandado);
        }
    }

    private ArquivoCsv processarArquivoCsv(UploadedFile uf, String separador) {
        try {
            InputStream inMandados = extractInputStream(uf);
            ArquivoCsv arq = processarArquivoMandadosMonitoradosService.processarArquivoCsv(inMandados, separador.charAt(0));
            while (true) {
                String[] tupla = arq.getTuplas().get(arq.getTuplas().size() - 1);
                if (tupla.length == 1) {
                    arq.getTuplas().remove(arq.getTuplas().size() - 1);
                } else {
                    boolean achouUma;
                    achouUma = false;
                    for (int i = 1; i < tupla.length; i++) {
                        if (!tupla[i].trim().isEmpty()) {
                            achouUma = true;
                            break;
                        }
                    }
                    if (!achouUma) {
                        arq.getTuplas().remove(arq.getTuplas().size() - 1);
                    } else {
                        break;
                    }
                }
            }
            return arq;
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao processar arquivo.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Detalhe do Erro", e.getMessage() != null ? e.getMessage() : e.toString());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
    }

    private InputStream extractInputStream(UploadedFile uf) throws IOException {
        InputStream in = uf.getInputstream();
        if (uf.getFileName() != null && uf.getFileName().trim().toLowerCase().endsWith(".zip")) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            UtilZip.unzipOneSingleFile(in, out);
            in = new ByteArrayInputStream(out.toByteArray());
        }
        return in;
    }

    public UploadedFile getFileMonitorados() {
        return fileMonitorados;
    }

    public UploadedFile getFileMandados() {
        return fileMandados;
    }

    public String getSeparadorMonitorados() {
        return separadorMonitorados;
    }

    public void setSeparadorMonitorados(String separadorMonitorados) {
        this.separadorMonitorados = separadorMonitorados;
    }

    public String getSeparadorMandado() {
        return separadorMandado;
    }

    public void setSeparadorMandado(String separadorMandado) {
        this.separadorMandado = separadorMandado;
    }

    public ArquivoCsv getArquivoMonitorados() {
        return arquivoMonitorados;
    }

    public ArquivoCsv getArquivoMandados() {
        return arquivoMandados;
    }

    public List<MandadoDto> getMandados() {
        return mandados;
    }

    public PDFOptions getPdfOpt() {
        return pdfOpt;
    }

    public ExcelOptions getExcelOpt() {
        return excelOpt;
    }
}
