package manipulador.modelo;

public class Transicao {

	private Estado origem;
	private Estado destino;
	private String simbolo;

	protected Transicao(Estado origem, Estado destino, String simbolo) {
		this.origem = origem;
		this.destino = destino;
		this.simbolo = simbolo;
	}

	public Estado getOrigem() {
		return origem;
	}

	public Estado getDestino() {
		return destino;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public String toString() {
		String string = origem.toString() + " -" + simbolo + "----> "
				+ destino.toString();
		return string;
	}

}
