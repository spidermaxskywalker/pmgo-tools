package br.com.maxgontijo.pmgo.planilhasveiculos.dto;

import java.util.Date;

public class MandatoDto extends ModelDto {
    private String numero;
    private Date dataExpedicao;
    private String nomeMae;
    private MonitoradoDto monitorado;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDataExpedicao() {
        return dataExpedicao;
    }

    public void setDataExpedicao(Date dataExpedicao) {
        this.dataExpedicao = dataExpedicao;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public MonitoradoDto getMonitorado() {
        return monitorado;
    }

    public void setMonitorado(MonitoradoDto monitorado) {
        this.monitorado = monitorado;
    }

}
