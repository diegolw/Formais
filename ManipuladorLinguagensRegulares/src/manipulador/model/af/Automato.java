package manipulador.model.af;

import java.util.LinkedList;

public class Automato {

	private String[] alfabeto;
	private LinkedList<Estado> estados;

	public Automato(String[] alfabeto) {
		this.alfabeto = alfabeto;
		estados = new LinkedList<Estado>();
	}

	public void addEstado(Estado estado) {
		estados.add(estado);
	}

	public LinkedList<Estado> getEstados() {
		return estados;
	}

	public void limparEstados() {
		estados = new LinkedList<Estado>();
	}

	public Estado getEstado(String nome) {
		for (Estado estado : estados) {
			if (estado.getNome().equals(nome)) {
				return estado;
			}
		}
		return null;
	}

}
