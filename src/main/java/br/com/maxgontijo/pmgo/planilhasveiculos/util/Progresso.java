package br.com.maxgontijo.pmgo.planilhasveiculos.util;

public class Progresso {
    private int total;
    private int completo;

    public void incrementarProgresso() {
        if (completo < total) {
            completo++;
        }
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCompleto() {
        return completo;
    }

    public void setCompleto(int completo) {
        this.completo = completo;
    }

    public double getProgresso() {
        return total > 0 ? completo / (double) total : 1;
    }
}
