package br.com.maxgontijo.pmgo.planilhasveiculos.dto;

public class Filtro {
	private String att;
	private String op;
	private Object val;

	public Filtro() {
	}

	public Filtro(String att, String op, Object val) {
		this.att = att;
		this.op = op;
		this.val = val;
	}

	public String getAtt() {
		return att;
	}

	public void setAtt(String att) {
		this.att = att;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public Object getVal() {
		return val;
	}

	public void setVal(Object val) {
		this.val = val;
	}
}
