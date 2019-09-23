package br.com.maxgontijo.pmgo.planilhasveiculos.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "radar", schema = Model.SCHEMA_VEICULOS)
public class Radar extends Model<Integer> {
	private static final long serialVersionUID = 1L;

	private String codigo;

	@Override
	@Id
	@Column(name = "id", updatable = false, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@SequenceGenerator(sequenceName = "radar_seq", name = "seq", schema = Model.SCHEMA_VEICULOS, allocationSize = 1)
	public Integer getId() {
		return this.id;
	}

	@Column(name = "codigo", unique = true, length = 30, nullable = false)
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
}
