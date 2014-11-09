package manipulador.modelo;

import java.util.Iterator;
import java.util.LinkedList;

public class Estado {

	private String nome;
	private LinkedList<Transicao> transicoes;
	private boolean ehFinal;
	private boolean ehInicial;

	public Estado(String nome) {
		this.nome = nome;
		transicoes = new LinkedList<Transicao>();
		ehInicial = false;
		ehFinal = false;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

	public boolean ehFinal() {
		return ehFinal;
	}

	public boolean ehInicial() {
		return ehInicial;
	}

	public void setInicial(boolean _inicial) {
		this.ehInicial = _inicial;
	}

	public void setFinal(boolean _final) {
		this.ehFinal = _final;
	}
	
	public boolean getFinal() {
		return ehFinal;
	}

	protected void addTransicao(Estado destino, String simbolo) {
		if (!existeTransicao(destino, simbolo)) {
			Transicao transicao = new Transicao(this, destino, simbolo);
			transicoes.add(transicao);
		}
	}

	protected boolean existeTransicao(Estado destino, String simbolo) {
		Iterator<Transicao> iterador = transicoes.iterator();
		while (iterador.hasNext()) {
			Transicao transicao = iterador.next();
			if (transicao.getDestino() == destino
					&& transicao.getSimbolo().equals(simbolo)) {
				return true;				
			}
		}
		return false;
	}

	public LinkedList<Transicao> getTransicoesList() {
		return transicoes;
	}
	
	public Transicao[] getTransicoes() {
		Transicao[] arrayTransicoes = new Transicao[transicoes.size()];
		for (int i = 0, max = transicoes.size(); i < max; i++) {
			arrayTransicoes[i] = transicoes.get(i);
		}
		return arrayTransicoes;
	}

	public boolean temTransicaoComEsseSimbolo(String simbolo) {
		Iterator<Transicao> iterador = transicoes.iterator();
		while (iterador.hasNext()) {
			Transicao transicao = iterador.next();
			if (transicao.getSimbolo().equals(simbolo)) {
				return true;				
			}
		}
		return false;
	}
	
	public Transicao[] getTransicoesDoSimbolo(String s){
		LinkedList<Transicao> listaTemp = new LinkedList<Transicao>();
		Iterator<Transicao> it = transicoes.iterator();
		while(it.hasNext()){
			Transicao t = it.next();
			if(t.getSimbolo().equals(s))
				listaTemp.add(t);
		}
		Transicao[] arrTransicoes = new Transicao[listaTemp.size()];
		it = listaTemp.iterator();
		int cont = 0;
		while(it.hasNext()){
			arrTransicoes[cont] = it.next();
			cont++;
		}
		return arrTransicoes;		
	}
	
	protected void removeTransicoesParaOEstado(Estado e) {
		Transicao[] ts = getTransicoes();
		for(int i = 0 ; i < ts.length ; i++)
			if(ts[i].getDestino() == e){
				transicoes.remove(ts[i]);
			}
		
	}

	
}