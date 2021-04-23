package br.com.maxgontijo.pmgo.planilhasveiculos.model.enums;

public enum Acesso {
    ADMIN("Administrador"),
    RECORRENCIAS("Recorrências"),
    MANDADOS_MONITORADOS("Mandados Monitorados"),
    LOCALIZACAO_VIATURAS("Localização de Viaturas"),
    VEICULOS("Veículos");

    private final String descricao;

    Acesso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
