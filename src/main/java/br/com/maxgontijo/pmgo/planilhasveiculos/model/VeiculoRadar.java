package br.com.maxgontijo.pmgo.planilhasveiculos.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "veiculo_radar", schema = Model.SCHEMA_VEICULOS, uniqueConstraints = @UniqueConstraint(columnNames = { "fk_veiculo", "fk_radar", "data_hora" }))
public class VeiculoRadar extends Model<Integer> {
	private static final long serialVersionUID = 1L;

	private Veiculo veiculo;
	private Radar radar;
	private Date dataHora;
	private String sentido;

	@Override
	@Id
	@Column(name = "id", updatable = false, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@SequenceGenerator(sequenceName = "veiculo_radar_seq", name = "seq", schema = Model.SCHEMA_VEICULOS, allocationSize = 1)
	public Integer getId() {
		return this.id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_veiculo", nullable = false, updatable = false)
	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_radar", nullable = false, updatable = false)
	public Radar getRadar() {
		return radar;
	}

	public void setRadar(Radar radar) {
		this.radar = radar;
	}

	@Column(name = "data_hora", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	@Column(name = "sentido", length = 256)
	public String getSentido() {
		return sentido;
	}

	public void setSentido(String sentido) {
		this.sentido = sentido;
	}
}
