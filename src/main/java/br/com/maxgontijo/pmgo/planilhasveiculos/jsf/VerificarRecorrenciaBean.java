package br.com.maxgontijo.pmgo.planilhasveiculos.jsf;

import br.com.maxgontijo.pmgo.planilhasveiculos.dto.RecorrenciaDto;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.enums.Acesso;
import br.com.maxgontijo.pmgo.planilhasveiculos.service.ProcessarArquivoRecorrenciaService;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@ManagedBean
@ViewScoped
@AcessoBean(Acesso.RECORRENCIAS)
public class VerificarRecorrenciaBean extends GenericJsfBean {
    private @Autowired
    ProcessarArquivoRecorrenciaService processarArquivoRecorrenciaService;

    private List<RecorrenciaDto> recorrencias;
    private List<Integer> listas;
    private Integer totalOcorrencias;

    @PostConstruct
    public void init() {
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            recorrencias = processarArquivoRecorrenciaService.processarArquivo(event.getFile().getInputstream());
            listas = new ArrayList<>();

            Map<Integer, Integer> qtdes = new TreeMap<>();
            for (RecorrenciaDto rec : recorrencias) {
                for (Integer l : rec.getListas()) {
                    Integer qtde = qtdes.get(l);
                    if (qtde == null) {
                        qtde = 0;
                    }
                    qtde++;
                    qtdes.put(l, qtde);
                }
            }
            for (Integer v : qtdes.values()) {
                listas.add(v);
            }

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", event.getFile().getFileName() + " foi processado.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", event.getFile().getFileName() + " NÃO foi processado.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void limpar() {
        this.recorrencias = null;
        this.listas = null;
        this.totalOcorrencias = null;
    }

    public List<Integer> getListas() {
        return listas;
    }

    public Integer getTotalOcorrencias() {
        return totalOcorrencias;
    }

    public List<RecorrenciaDto> getRecorrencias() {
        return recorrencias;
    }
}
