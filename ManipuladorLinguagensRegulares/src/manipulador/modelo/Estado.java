package manipulador.modelo;

import java.util.Iterator;
import java.util.LinkedList;

public class Estado {

	private Automato pai;
	private String nome;
	private LinkedList<Transicao> transicoes;
	private boolean ehFinal;

	protected Estado(String n, Automato ap) {
		pai = ap;
		nome = n;
		transicoes = new LinkedList<Transicao>();
		ehFinal = false;
	}

	public void setNome(String n) {
		this.nome = n;
	}

	public boolean ehFinal() {
		return ehFinal;
	}

	public boolean ehInicial() {
		return pai.getEstadoInicial() == this;
	}

	protected void setInicial() {
		pai.setEstadoInicial(this);
	}

	protected void setFinal(boolean isFinal) {
		ehFinal = isFinal;
	}

	public String getNome() {
		return nome;
	}

	protected Automato getPai() {
		return pai;
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
					&& transicao.getSimbolo().equals(simbolo))
				return true;
		}
		return false;
	}

	public String toString() {
		return nome;
	}

	public Transicao[] getTransicoes() {
		Iterator<Transicao> iterador = transicoes.iterator();
		Transicao[] retornoTransicoes = new Transicao[transicoes.size()];
		int cont = 0;
		while (iterador.hasNext()) {
			retornoTransicoes[cont] = iterador.next();
			cont++;
		}
		return retornoTransicoes;
	}

	public Transicao[] getTransicoesDoSimbolo(String simbolo) {
		LinkedList<Transicao> transicoesTemp = new LinkedList<Transicao>();
		Iterator<Transicao> iterador = transicoes.iterator();
		while (iterador.hasNext()) {
			Transicao transicao = iterador.next();
			if (transicao.getSimbolo().equals(simbolo))
				transicoesTemp.add(transicao);
		}
		Transicao[] retornoTransicoes = new Transicao[transicoesTemp.size()];
		iterador = transicoesTemp.iterator();
		int cont = 0;
		while (iterador.hasNext()) {
			retornoTransicoes[cont] = iterador.next();
			cont++;
		}
		return retornoTransicoes;
	}

	public Transicao[] getTransicoesParaSiMesmo() {
		LinkedList<Transicao> transicoesParaSiMesmo = new LinkedList<Transicao>();
		Iterator<Transicao> iterador = transicoes.iterator();
		while (iterador.hasNext()) {
			Transicao transicao = iterador.next();
			if (transicao.getDestino() == this)
				transicoesParaSiMesmo.add(transicao);
		}
		Transicao[] retornoTransicoes = new Transicao[transicoesParaSiMesmo
				.size()];
		iterador = transicoesParaSiMesmo.iterator();
		for (int i = 0; i < retornoTransicoes.length; i++)
			retornoTransicoes[i] = iterador.next();

		return retornoTransicoes;
	}

	public Transicao[] getTransicoesParaOutroEstado() {
		LinkedList<Transicao> transicoesOutroEstado = new LinkedList<Transicao>();
		Iterator<Transicao> iterador = transicoes.iterator();
		while (iterador.hasNext()) {
			Transicao transicao = iterador.next();
			if (transicao.getDestino() != this)
				transicoesOutroEstado.add(transicao);
		}
		Transicao[] retornoTransicoes = new Transicao[transicoesOutroEstado
				.size()];
		iterador = transicoesOutroEstado.iterator();
		for (int i = 0; i < retornoTransicoes.length; i++)
			retornoTransicoes[i] = iterador.next();

		return retornoTransicoes;
	}

	protected void removeTransicoesParaOEstado(Estado estado) {
		Transicao[] transicoesAtuais = getTransicoes();
		for (int i = 0; i < transicoesAtuais.length; i++)
			if (transicoesAtuais[i].getDestino() == estado) {
				transicoes.remove(transicoesAtuais[i]);
			}
	}

	protected Transicao[] getTransicoesQueChegam() {
		LinkedList<Transicao> retornoTransicoes = new LinkedList<Transicao>();
		Estado[] estados = pai.getEstados();
		for (int i = 0; i < estados.length; i++) {
			Transicao[] transicoes = estados[i].getTransicoes();
			for (int j = 0; j < transicoes.length; j++) {
				if (transicoes[j].getDestino() == this) {
					retornoTransicoes.add(transicoes[j]);
				}
			}

		}
		// Só transfere para um array ao invés de lista
		Transicao[] transicoes = new Transicao[retornoTransicoes.size()];
		Iterator<Transicao> iterador = retornoTransicoes.iterator();
		for (int i = 0; i < transicoes.length; i++) {
			transicoes[i] = iterador.next();
		}
		return transicoes;
	}

	public boolean getFinal() {
		return ehFinal;
	}

}