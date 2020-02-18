package br.com.maxgontijo.pmgo.planilhasveiculos.jsf;

import br.com.maxgontijo.pmgo.planilhasveiculos.dto.MandatoDto;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.ArquivoCsv;
import br.com.maxgontijo.pmgo.planilhasveiculos.service.ProcessarArquivoMandatosMonitoradosService;
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
public class VerificarMonitoradosMandatosBean extends GenericJsfBean {
    private @Autowired
    ProcessarArquivoMandatosMonitoradosService processarArquivoMandatosMonitoradosService;

    private ArquivoCsv arquivoMonitorados;
    private ArquivoCsv arquivoMandatos;

    private List<MandatoDto> mandatos;

    private UploadedFile fileMonitorados;
    private UploadedFile fileMandatos;

    private String separadorMonitorados = ";";
    private String separadorMandato = ",";

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

    public void handleFileUploadMandatos(FileUploadEvent event) {
        try {
            this.fileMandatos = event.getFile();
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
            InputStream inMandatos = extractInputStream(fileMandatos);

            mandatos = processarArquivoMandatosMonitoradosService.processarArquivo(inMonitorados, separadorMonitorados.charAt(0), inMandatos, separadorMandato.charAt(0));

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
        this.mandatos = null;
        this.fileMonitorados = null;
        this.fileMandatos = null;
        this.arquivoMonitorados = null;
        this.arquivoMandatos = null;
    }

    public void mostrarArquivoCsvMonitorados() {
        if (arquivoMonitorados == null && fileMonitorados != null && separadorMonitorados != null) {
            arquivoMonitorados = processarArquivoCsv(fileMonitorados, separadorMonitorados);
        }
    }

    public void mostrarArquivoCsvMandatos() {
        if (arquivoMandatos == null && fileMandatos != null && separadorMandato != null) {
            arquivoMandatos = processarArquivoCsv(fileMandatos, separadorMandato);
        }
    }

    private ArquivoCsv processarArquivoCsv(UploadedFile uf, String separador) {
        try {
            InputStream inMandatos = extractInputStream(uf);
            ArquivoCsv arq = processarArquivoMandatosMonitoradosService.processarArquivoCsv(inMandatos, separador.charAt(0));
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

    public UploadedFile getFileMandatos() {
        return fileMandatos;
    }

    public String getSeparadorMonitorados() {
        return separadorMonitorados;
    }

    public void setSeparadorMonitorados(String separadorMonitorados) {
        this.separadorMonitorados = separadorMonitorados;
    }

    public String getSeparadorMandato() {
        return separadorMandato;
    }

    public void setSeparadorMandato(String separadorMandato) {
        this.separadorMandato = separadorMandato;
    }

    public ArquivoCsv getArquivoMonitorados() {
        return arquivoMonitorados;
    }

    public ArquivoCsv getArquivoMandatos() {
        return arquivoMandatos;
    }

    public List<MandatoDto> getMandatos() {
        return mandatos;
    }

    public PDFOptions getPdfOpt() {
        return pdfOpt;
    }

    public ExcelOptions getExcelOpt() {
        return excelOpt;
    }
}
