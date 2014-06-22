package manipulador.model.gramatica;

import java.io.Serializable;

public class Transicao implements Serializable{
	
	private Estado origem;
	private Estado destino;
	private Terminal simbolo;
	
	public Transicao(Estado _ori, Estado _dest, Terminal _simb){
		origem = _ori;
		destino = _dest;
		simbolo = _simb;
	}
	
	
	public Estado getOrigem() {
		return origem;
	}
	
	public Estado getDestino() {
		return destino;
	}
	
	public Terminal getSimbolo() {
		return simbolo;
	}
	
	public String toString(){
		String s = origem.toString() + " -" + simbolo.toString() + "----> " + destino.toString();
		return s;
	}
	

	
}
