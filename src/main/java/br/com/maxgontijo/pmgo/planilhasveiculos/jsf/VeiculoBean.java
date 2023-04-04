package br.com.maxgontijo.pmgo.planilhasveiculos.jsf;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Veiculo;
import br.com.maxgontijo.pmgo.planilhasveiculos.model.enums.Acesso;
import br.com.maxgontijo.pmgo.planilhasveiculos.service.VeiculoService;
import br.com.maxgontijo.pmgo.planilhasveiculos.util.DadosInvalidosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.event.ActionEvent;
import java.util.List;

@Component
@Scope("view")
@AcessoBean(Acesso.ADMIN)
public class VeiculoBean extends GenericJsfBean {
    private @Autowired
    VeiculoService veiculoService;

    private List<Veiculo> veiculos;
    private Veiculo veiculo;
    private String placas;

    private String placasExportadas;

    public void selecionarVeiculo(ActionEvent event) {
        try {
            this.veiculo = (Veiculo) event.getComponent().getAttributes().get("veiculo");
        } catch (Exception e) {
            addMessageError(e);
        }
    }

    public void processarPlacas() {
        try {
            veiculos = veiculoService.extrairVeiculosDeListaPlacas(placas);
        } catch (Exception e) {
            veiculos = null;
            addMessageError(e);
        }
    }

    public void clear() {
        placas = null;
        veiculos = null;
    }

    public void salvar() {
        try {
            if (veiculos == null || veiculos.isEmpty()) {
                throw new DadosInvalidosException("Nenhuma placa para salvar.");
            }
            this.veiculoService.salvarVeiculos(veiculos);
            addMessageInfo("Sucesso", "Veículos foram salvos.");
        } catch (Exception e) {
            addMessageError(e);
        }
    }

    public void exportarPlacasTxt() {
        StringBuilder sb = new StringBuilder();
        for (Veiculo v : veiculos) {
            sb.append(v.getPlaca()).append("\n");
        }
        placasExportadas = sb.toString();
    }

    public void clearVeiculo() {
        this.veiculo = null;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public String getPlacas() {
        return placas;
    }

    public void setPlacas(String placas) {
        this.placas = placas;
    }

    public String getPlacasExportadas() {
        return placasExportadas;
    }

    public void setPlacasExportadas(String placasExportadas) {
        this.placasExportadas = placasExportadas;
    }
}
