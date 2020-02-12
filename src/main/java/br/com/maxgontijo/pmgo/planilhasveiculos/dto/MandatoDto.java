package br.com.maxgontijo.pmgo.planilhasveiculos.dto;

public class MandatoDto extends ModelDto {
	private String numero;
	private MonitoradoDto monitorado;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public MonitoradoDto getMonitorado() {
		return monitorado;
	}

	public void setMonitorado(MonitoradoDto monitorado) {
		this.monitorado = monitorado;
	}
}
