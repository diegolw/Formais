package manipulador.model.af;

public class Transicao {

	private Estado origem;
	private Estado destino;
	private String simbolo;
	
	public Transicao(Estado origem, Estado destino, String simbolo) {
		this.origem = origem;
		this.destino = destino;
		this.simbolo = simbolo;
	}

}
