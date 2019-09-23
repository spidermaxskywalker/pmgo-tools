package br.com.maxgontijo.pmgo.planilhasveiculos.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "arquivo", schema = Model.SCHEMA_VEICULOS)
public class Arquivo extends Model<Integer> {
	private static final long serialVersionUID = 1L;

	private String nome;
	private Date dataHoraUpload;

	@Override
	@Id
	@Column(name = "id", updatable = false, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@SequenceGenerator(sequenceName = "radar_seq", name = "seq", schema = Model.SCHEMA_VEICULOS, allocationSize = 1)
	public Integer getId() {
		return this.id;
	}

	@Column(name = "nome", length = 40, nullable = false)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "data_hora_upload", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataHoraUpload() {
		return dataHoraUpload;
	}

	public void setDataHoraUpload(Date dataHoraUpload) {
		this.dataHoraUpload = dataHoraUpload;
	}
}
