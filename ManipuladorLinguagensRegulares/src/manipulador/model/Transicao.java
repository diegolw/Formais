package manipulador.model;


public class Transicao {

	private Estado estadoOrigem;
	private Estado estadoDestino;
	private String simbolo;

	protected Transicao(Estado eo, Estado ed, String s) {
		estadoOrigem = eo;
		estadoDestino = ed;
		simbolo = s;
	}

	public Estado getEstadoOrigem() {
		return estadoOrigem;
	}

	public Estado getEstadoDestino() {
		return estadoDestino;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public String toString() {
		String s = estadoOrigem.toString() + " -" + simbolo + "----> "
				+ estadoDestino.toString();
		return s;
	}

}
