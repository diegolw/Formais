package manipulador.modelo;

public class Costura {
	private No origem;
	private No destino;
	
	public Costura(No or, No dest){
		this.origem = or;
		this.destino = dest;
	}
	
	public No getOrigem() {
		return origem;
	}
	public void setOrigem(No origem) {
		this.origem = origem;
	}
	public No getDestino() {
		return destino;
	}
	public void setDestino(No destino) {
		this.destino = destino;
	}
	

}
