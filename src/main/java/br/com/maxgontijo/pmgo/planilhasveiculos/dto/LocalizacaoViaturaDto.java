package br.com.maxgontijo.pmgo.planilhasveiculos.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LocalizacaoViaturaDto extends ModelDto {
    private Integer sequencia;
    private String prefixo;
    private String unidade;
    private Date dataHora;
    private Double latitude;
    private Double longitude;
    private String endRua;
    private String endBairro;
    private String endMunicipio;
    private String endEstado;
    private String endCep;
    private String endJson;

    public Integer getSequencia() {
        return sequencia;
    }

    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }

    public String getPrefixo() {
        return prefixo;
    }

    public void setPrefixo(String prefixo) {
        this.prefixo = prefixo;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public String getDataHoraFormatada() {
        return dataHora != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataHora) : null;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getEndRua() {
        return endRua;
    }

    public void setEndRua(String endRua) {
        this.endRua = endRua;
    }

    public String getEndBairro() {
        return endBairro;
    }

    public void setEndBairro(String endBairro) {
        this.endBairro = endBairro;
    }

    public String getEndMunicipio() {
        return endMunicipio;
    }

    public void setEndMunicipio(String endMunicipio) {
        this.endMunicipio = endMunicipio;
    }

    public String getEndEstado() {
        return endEstado;
    }

    public void setEndEstado(String endEstado) {
        this.endEstado = endEstado;
    }

    public String getEndCep() {
        return endCep;
    }

    public void setEndCep(String endCep) {
        this.endCep = endCep;
    }

    public String getEndJson() {
        return endJson;
    }

    public void setEndJson(String endJson) {
        this.endJson = endJson;
    }
}
