package manipulador.model.gramatica;

public class Transicao {
	private Estado origem;
	private Estado destino;
	private Terminal simbolo;
	
	protected Transicao(Estado _ori, Estado _dest, Terminal _simb){
		setOrigem(_ori);
		setDestino(_dest);
		setSimbolo(_simb);
	}
	
	
	public Estado getOrigem() {
		return origem;
	}

	public void setOrigem(Estado origem) {
		this.origem = origem;
	}

	public Estado getDestino() {
		return destino;
	}

	public void setDestino(Estado destino) {
		this.destino = destino;
	}

	public Terminal getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(Terminal simbolo) {
		this.simbolo = simbolo;
	}
	
}
