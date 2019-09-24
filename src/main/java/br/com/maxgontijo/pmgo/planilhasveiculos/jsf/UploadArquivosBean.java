package br.com.maxgontijo.pmgo.planilhasveiculos.jsf;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.maxgontijo.pmgo.planilhasveiculos.service.ProcessarArquivoService;

@ManagedBean
@ViewScoped
public class UploadArquivosBean extends GenericJsfBean {
	private @Autowired ProcessarArquivoService processarArquivoService;

	private UploadedFile file;

	@PostConstruct
	public void init() {
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void handleFileUpload(FileUploadEvent event) {
		try {
			int qtde = processarArquivoService.processarArquivo(event.getFile().getFileName(), event.getFile().getInputstream(), null);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", event.getFile().getFileName() + " foi processado.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Foram inseridos " + qtde + " registros.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", event.getFile().getFileName() + " NÃO foi processado.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
}
