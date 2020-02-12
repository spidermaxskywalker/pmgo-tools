package br.com.maxgontijo.pmgo.planilhasveiculos.jsf;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.component.export.ExcelOptions;
import org.primefaces.component.export.PDFOptions;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.maxgontijo.pmgo.planilhasveiculos.dto.MandatoDto;
import br.com.maxgontijo.pmgo.planilhasveiculos.service.ProcessarArquivoMandatosMonitoradosService;

@ManagedBean
@ViewScoped
public class VerificarMonitoradosMandatosBean extends GenericJsfBean {
	private @Autowired ProcessarArquivoMandatosMonitoradosService processarArquivoMandatosMonitoradosService;

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
			mandatos = processarArquivoMandatosMonitoradosService.processarArquivo(fileMonitorados.getInputstream(), separadorMonitorados.charAt(0), fileMandatos.getInputstream(), separadorMandato.charAt(0));

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
