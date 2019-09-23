package br.com.maxgontijo.pmgo.planilhasveiculos.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "veiculo", schema = Model.SCHEMA_VEICULOS)
public class Veiculo extends Model<Integer> {
	private static final long serialVersionUID = 1L;

	private String placa;

	@Override
	@Id
	@Column(name = "id", updatable = false, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@SequenceGenerator(sequenceName = "veiculo_seq", name = "seq", schema = Model.SCHEMA_VEICULOS, allocationSize = 1)
	public Integer getId() {
		return this.id;
	}

	@Column(name = "placa", unique = true, length = 6, nullable = false)
	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}
}
