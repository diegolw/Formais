package manipulador.controlador;

import java.util.LinkedList;

import manipulador.modelo.Automato;
import manipulador.modelo.Estado;
import manipulador.modelo.EstadoAuxiliar;
import manipulador.modelo.Transicao;
import manipulador.visao.GUI;
import manipulador.visao.IGUI;

public class Mediador implements IGUI {

	private GUI window;

	private Automato automato1;
	private Automato automato2;

	public Mediador() {
		window = new GUI();
		window.setVisible(true);
		window.addEventListener(this);
	}

	public void determinizarTest() {
		automato1 = new Automato();
		String[] alfabeto = { "a", "b" };
		automato1.setAlfabeto(alfabeto);
		automato1.addEstado("q0");
		automato1.addEstado("q1");
		automato1.addEstado("q2");
		automato1.addEstado("q3");
		automato1.setEstadoInicial("q0");
		automato1.setEstadoFinal("q3");
		automato1.addTransicao("q0", "q0", "a");
		automato1.addTransicao("q0", "q1", "a");
		automato1.addTransicao("q0", "q0", "b");
		automato1.addTransicao("q1", "q2", "b");
		automato1.addTransicao("q2", "q3", "b");
		automato1.determinizar();

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

	public void testar() {
		automato1 = new Automato();
		automato2 = new Automato();
		String[] alf2 = { "a", "b" };
		String[] alf3 = { "a", "b", "c" };
		automato1.setAlfabeto(alf2);
		automato1.setNome("AF-um");
		automato1.addEstado("A");
		automato1.addEstado("B");
		// automato1.addEstado("C");
		automato1.addTransicao("A", "B", "a");
		// automato1.addTransicao("A", "C", "b");
		automato1.addTransicao("B", "A", "a");
		automato1.addTransicao("B", "B", "b");
		// automato1.addTransicao("C", "C", "a");
		// automato1.addTransicao("C", "B", "b");
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
		automato1 = complemento();

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
			if (estadoAtual.ehFinal()) {
				linha[0] += "* ";
			}
			linha[0] += estadoAtual.getNome();
			for (int z = 0; z < alfabeto.length; z++) {
				for (int j = 0; j < transicoes.length; j++) {
					if (transicoes[j].getSimbolo().equals(alfabeto[z])) {
						if (linha[z + 1] == null) {
							linha[z + 1] = transicoes[j].getDestino().getNome();
						} else
							linha[z + 1] += ", "
									+ transicoes[j].getDestino().getNome();
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
	public void expressaoRegular(String expressao) {
		// Expressão já vem formatada pela View!

	}

	@Override
	public void minimizar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void igualdade() {
		// TODO Auto-generated method stub

	}

	@Override
	public void interseccao() {
		// TODO Auto-generated method stub

	}

	@Override
	public void diferenca() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ocorrencias() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enumerar(int num) {
		// TODO Auto-generated method stub

	}

	//

	private Automato complemento() {
		Automato automato = getAutomato();
		Automato complemento = new Automato();

		complemento.setAlfabeto(automato.getAlfabeto());
		complemento.setEstadosList(automato.getEstadosList());
		Estado inicial = automato.getEstadoInicial();
		complemento.setEstadoInicial(inicial.getNome());
		Estado[] estados = complemento.getEstados();
		for (int i = 0, max = estados.length; i < max; i++) {
			Estado estado = estados[i];
			if (estado.ehFinal()) {
				estado.setFinal(false);
			} else {
				estado.setFinal(true);
			}
		}
		LinkedList<Transicao> transicoes = automato.getTransicoes();
		for (Transicao transicao : transicoes) {
			complemento.addTransicao(transicao.getOrigem(),
					transicao.getDestino(), transicao.getSimbolo());
		}

		// Criar epsilon transições
		boolean temEstadoErro = false;
		Estado erro = null;
		for (int i = 0, max = estados.length; i < max; i++) {
			// Eh completo?
			Estado estado = estados[i];
			String[] alfabeto = complemento.getAlfabeto();
			for (int j = 0; j < alfabeto.length; j++) {
				// Se não tem transição com esse símbolo,
				// então cria transição para o estado de erro
				if (!estado.temTransicaoComEsseSimbolo(alfabeto[j])) {
					if (!temEstadoErro) {
						erro = new Estado("erro");
						erro.setFinal(true);
						for (int k = 0; k < alfabeto.length; k++) {
							complemento.addTransicao(erro, erro, alfabeto[k]);
						}
						complemento.addEstado(erro);
						temEstadoErro = true;
					}
					complemento.addTransicao(estado, erro, alfabeto[j]);
				}
			}
			if (estado.ehFinal()) {
				estado.setFinal(false);
			} else {
				estado.setFinal(true);
			}
		}
		return complemento;
	}
}
