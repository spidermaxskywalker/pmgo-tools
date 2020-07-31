package br.com.maxgontijo.pmgo.planilhasveiculos.jsf;

import br.com.maxgontijo.pmgo.planilhasveiculos.dto.LocalizacaoViaturaDto;
import br.com.maxgontijo.pmgo.planilhasveiculos.service.ProcessarArquivoLocalizacaoViaturasService;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@ManagedBean
//@ViewScoped
@SessionScoped
public class ObterEnderecosDeCoordenadasBean extends GenericJsfBean {
    private @Autowired
    ProcessarArquivoLocalizacaoViaturasService processarArquivoLocalizacaoViaturasService;

    private String fileName;
    private byte[] fileBytes;
    private StreamedContent file;
    private LocalizacaoViaturaDto registro;

    private List<LocalizacaoViaturaDto> viaturas;

    @PostConstruct
    public void init() {
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            viaturas = processarArquivoLocalizacaoViaturasService.processarArquivo(event.getFile().getInputstream(), output);
            fileBytes = output.toByteArray();

            fileName = event.getFile().getFileName();
            if (fileName == null) {
                fileName = "planilha.xlsx";
            }
            if (fileName.lastIndexOf('.') >= 0) {
                fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            }
            fileName = fileName + "-edited.xlsx";

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", event.getFile().getFileName() + " foi carregado.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
            viaturas = null;
            e.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, event.getFile().getFileName() + " NÃO foi carregado.", e.getMessage() != null ? e.getMessage() : e.getClass().getName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void prepararDownload() {
        DefaultStreamedContent file = new DefaultStreamedContent();
        file.setName(fileName);
        file.setStream(new ByteArrayInputStream(fileBytes));
        this.file = file;
    }

    public void showJson(ActionEvent event) {
        this.registro = (LocalizacaoViaturaDto) event.getComponent().getAttributes().get("registro");
    }

    public void limpar() {
        this.viaturas = null;
        this.file = null;
        this.fileName = null;
        this.fileBytes = null;
        this.registro = null;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    public LocalizacaoViaturaDto getRegistro() {
        return registro;
    }

    public void setRegistro(LocalizacaoViaturaDto registro) {
        this.registro = registro;
    }

    public List<LocalizacaoViaturaDto> getViaturas() {
        return viaturas;
    }
}
