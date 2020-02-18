package br.com.maxgontijo.pmgo.planilhasveiculos.model;

import java.util.ArrayList;
import java.util.List;

public class ArquivoCsv {
    private final List<String> nomesColunas;
    private final List<String[]> tuplas;

    public ArquivoCsv() {
        this.nomesColunas = new ArrayList<>();
        this.tuplas = new ArrayList<>();
    }

    public List<String> getNomesColunas() {
        return nomesColunas;
    }

    public List<String[]> getTuplas() {
        return tuplas;
    }
}
