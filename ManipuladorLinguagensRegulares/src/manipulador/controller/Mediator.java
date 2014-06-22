package manipulador.controller;

import java.util.Iterator;

import javax.transaction.TransactionRequiredException;

import manipulador.model.af.Automato;
import manipulador.model.af.Estado;
import manipulador.model.af.Transicao;
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

	@Override
	public void setAlfabeto(String[] simbolos) {
		if (window.ehAutomato1()) {
			automato1 = new Automato(simbolos);
		} else {
			automato2 = new Automato(simbolos);
		}

	}

	@Override
	public void addEstado(String nome, boolean ehInicial, boolean ehFinal) {
		System.out.println("Mediador.addEstado(" + nome + ")");
		Estado estado = new Estado(nome, ehInicial, ehFinal);
		if (window.ehAutomato1()) {
			if (!existeEstado(nome)) {
				automato1.addEstado(estado);
			}
		} else {
			if (!existeEstado(nome)) {
				automato2.addEstado(estado);
			}
		}
	}

	private Automato getAutomato() {
		return window.ehAutomato1() ? automato1 : automato2;
	}

	private boolean existeEstado(String nome) {
		Automato automato = getAutomato();
		Iterator<Estado> it = automato.getEstados().iterator();
		while (it.hasNext()) {
			if (it.next().getNome().equals(nome)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void limparEstados() {
		if (window.ehAutomato1()) {
			automato1.limparEstados();
		} else {
			automato2.limparEstados();
		}

	}

	@Override
	public void addTransicao(String origem, String destino, String simbolo) {
		Automato automato = getAutomato();
		Estado eOrigem = automato.getEstado(origem);
		Estado eDestino = automato.getEstado(destino);
		eOrigem.addTransicao(eDestino, simbolo);
	}

	@Override
	public void minimizar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void complemento() {
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
	public void uniao() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reverso() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enumerar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void vaziaInfinitaFinita() {
		// TODO Auto-generated method stub

	}

}
