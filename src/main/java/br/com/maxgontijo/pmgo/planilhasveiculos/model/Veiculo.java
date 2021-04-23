package br.com.maxgontijo.pmgo.planilhasveiculos.model;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.enums.TipoVeiculo;

import javax.persistence.*;

@Entity
@Table(name = "veiculo", schema = Model.SCHEMA_CADASTRO)
public class Veiculo extends Model<Integer> {
    private static final long serialVersionUID = 1L;

    private String placa;
    private TipoVeiculo tipo;
    private String observacoes;

    @Override
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(sequenceName = "veiculo_seq", name = "veiculo_seq", schema = Model.SCHEMA_CADASTRO, allocationSize = 1)
    public Integer getId() {
        return this.id;
    }

    @Column(name = "placa", nullable = false, length = 10, unique = true)
    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tipo")
    public TipoVeiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVeiculo tipo) {
        this.tipo = tipo;
    }

    @Column(name = "observacoes", length = 512)
    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Transient
    public boolean isPlacaCadastrada() {
        return getId() != null;
    }
}
