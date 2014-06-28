package manipulador.controller;

import java.util.LinkedList;

import manipulador.model.Automato;
import manipulador.model.Estado;
import manipulador.model.ExpressaoRegular;
import manipulador.model.Transicao;
import manipulador.view.GUI;
import manipulador.view.IGUI;

public class Mediator implements IGUI {

	private GUI window;

	private Automato automato1;
	private Automato automato2;

	public Mediator() {
		window = new GUI();
		window.setVisible(true);
		window.addEventListener(this);
	}

	public void testar() {
		automato1 = new Automato();
		automato2 = new Automato();
		String[] alf2 = { "a", "b" };
		String[] alf3 = { "a", "b", "c" };
		automato1.setAlfabeto(alf2);
		automato1.setNome("AF-um");
		automato1.addEstado("A");
		automato1.addEstado("B");
		automato1.addEstado("C");
		automato1.addTransicao("A", "B", "a");
		automato1.addTransicao("A", "C", "b");
		automato1.addTransicao("B", "A", "a");
		automato1.addTransicao("B", "B", "b");
		automato1.addTransicao("C", "C", "a");
		automato1.addTransicao("C", "B", "b");
		automato1.setEstadoInicial("A");
		automato1.setEstadoFinal("B");
		automato2.setAlfabeto(alf3);
		automato2.setNome("AF-dois");
		automato2.addEstado("C");
		automato2.addEstado("D");
		automato2.addTransicao("C", "D", "a");
		automato2.addTransicao("C", "C", "b");
		automato2.addTransicao("C", "D", "c");
		automato2.addTransicao("D", "C", "c");
		automato2.setEstadoInicial("C");
		automato2.setEstadoFinal("D");

		if (window.ehAutomato1()) {
			atualizarAutomato();
			window.rdbtnAutmato2.setSelected(true);
			atualizarAutomato();
			window.rdbtnAutmato1.setSelected(true);
		} else {
			atualizarAutomato();
			window.rdbtnAutmato1.setSelected(true);
			atualizarAutomato();
			window.rdbtnAutmato2.setSelected(true);
		}
	}

	public void atualizarAutomato() {
		Automato automato = getAutomato();

		Estado[] estados = automato.getEstados();
		String alfabeto[] = automato.getAlfabeto();
		window.setAlfabeto(alfabeto);

		for (int i = 0; i < estados.length; i++) {
			Estado estadoAtual = estados[i];
			String[] linha = new String[alfabeto.length + 1];
			Transicao[] transicoes = estadoAtual.getTransicoes();
			if (automato.getEstadoInicial() == estadoAtual) {
				linha[0] = "->";
			} else {
				linha[0] = "";
			}
			if (estadoAtual.ehEstadoFinal()) {
				linha[0] += "* ";
			}
			linha[0] += estadoAtual.getNome();
			for (int z = 0; z < alfabeto.length; z++) {
				for (int j = 0; j < transicoes.length; j++) {
					if (transicoes[j].getSimbolo().equals(alfabeto[z])) {
						if (linha[z + 1] == null) {
							linha[z + 1] = transicoes[j].getEstadoDestino()
									.getNome();
						} else
							linha[z + 1] += ", "
									+ transicoes[j].getEstadoDestino()
											.getNome();
					}
				}
			}
			window.addRow(linha);
		}
	}

	@Override
	public void setAlfabeto(String[] simbolos) {
		if (window.ehAutomato1()) {
			automato1 = new Automato();
			automato1.setNome("AF-um");
			automato1.setAlfabeto(simbolos);
		} else {
			automato2 = new Automato();
			automato2.setNome("AF-dois");
			automato2.setAlfabeto(simbolos);
		}
	}

	@Override
	public void addEstado(String nome, boolean ehInicial, boolean ehFinal) {
		if (window.ehAutomato1()) {
			automato1.addEstado(nome);
			if (ehInicial) {
				automato1.setEstadoInicial(nome);
			}
			if (ehFinal) {
				automato1.setEstadoFinal(nome);
			}
		} else {
			automato2.addEstado(nome);
			if (ehInicial) {
				automato2.setEstadoInicial(nome);
			}
			if (ehFinal) {
				automato2.setEstadoFinal(nome);
			}
		}
	}

	public Automato getAutomato() {
		return window.ehAutomato1() ? automato1 : automato2;
	}

	@Override
	public void addTransicao(String origem, String destino, String simbolo) {
		System.out.println("Adicionar transição de " + origem + " para "
				+ destino + " por " + simbolo);
		if (window.ehAutomato1()) {
			automato1.addTransicao(origem, destino, simbolo);
		} else {
			automato2.addTransicao(origem, destino, simbolo);
		}
	}

	@Override
	public void minimizar() {
		if (window.ehAutomato1()) {
			automato1 = automato1.getAutomatoMinimizado();
		} else {
			automato2 = automato2.getAutomatoMinimizado();
		}
		atualizarAutomato();
	}

	@Override
	public void complemento() {
		if (window.ehAutomato1()) {
			automato1 = automato1.getComplemento();
		} else {
			automato2 = automato2.getComplemento();
		}
		atualizarAutomato();
	}

	@Override
	public void reverso() {
		if (window.ehAutomato1()) {
			automato1 = automato1.getReverso();
		} else {
			automato2 = automato2.getReverso();
		}
		atualizarAutomato();
	}

	@Override
	public void enumerar(int num) {
		Automato automato = getAutomato();
		LinkedList<String> sentencas = automato.getSentencasPorTamanho(num,
				automato.getEstadoInicial());

		String retorno = sentencas.size() + " sentenças de tamanho " + num
				+ " \n";
		for (String sAtual : sentencas) {
			retorno += sAtual + " - ";
		}
		window.setResultado(retorno);
	}

	@Override
	public void igualdade() {
		boolean ehIgual = automato1.ehIgual(automato2);
		window.ehIgual(ehIgual);
	}

	@Override
	public void interseccao() {
		Automato automato = automato1.getInterseccao(automato2);
		atualizarAutomatoResultado(automato);
	}

	@Override
	public void uniao() {
		Automato automato = automato1.getUniao(automato2);
		atualizarAutomatoResultado(automato);
	}

	public void atualizarAutomatoResultado(Automato automato) {
		Estado[] estados = automato.getEstados();
		String alfabeto[] = automato.getAlfabeto();
		window.setAlfabetoResultado(alfabeto);

		for (int i = 0; i < estados.length; i++) {
			Estado estadoAtual = estados[i];
			String[] linha = new String[alfabeto.length + 1];
			Transicao[] transicoes = estadoAtual.getTransicoes();
			if (automato.getEstadoInicial() == estadoAtual) {
				linha[0] = "->";
			} else {
				linha[0] = "";
			}
			if (estadoAtual.ehEstadoFinal()) {
				linha[0] += "* ";
			}
			linha[0] += estadoAtual.getNome();
			for (int z = 0; z < alfabeto.length; z++) {
				for (int j = 0; j < transicoes.length; j++) {
					if (transicoes[j].getSimbolo().equals(alfabeto[z])) {
						if (linha[z + 1] == null) {
							linha[z + 1] = transicoes[j].getEstadoDestino()
									.getNome();
						} else
							linha[z + 1] += ", "
									+ transicoes[j].getEstadoDestino()
											.getNome();
					}
				}
			}
			window.addRowResultado(linha);
		}
	}

	@Override
	public void gramatica() {
		Automato automato = getAutomato();
		String retorno = automato.getGramaticaRegular();
		window.setResultado(retorno);
	}

	@Override
	public void expressaoRegular() {
		Automato automato = getAutomato();
		ExpressaoRegular expReg = automato.getExpressaoRegular();
		String retorno = expReg.getDescricao();
		window.setResultado(retorno);
	}

	@Override
	public void vaziaInfinitaFinita() {
		// TODO Auto-generated method stub

	}

}
