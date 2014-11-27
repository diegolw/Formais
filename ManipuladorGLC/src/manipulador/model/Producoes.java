package manipulador.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

public class Producoes implements Serializable, Termo {

	private NaoTerminal naoTerminal;
	private Set<Producao> producoes;

	public Producoes(NaoTerminal naoTerminal, Set<Producao> producoes) {
		this.naoTerminal = naoTerminal;
		this.producoes = producoes;
	}

	public Producoes() {

	}

	public NaoTerminal getNaoTerminal() {
		return naoTerminal;
	}

	public Set<Producao> getProducoes() {
		return producoes;
	}

	public void setProducoes(Set<Producao> producoes) {
		this.producoes = producoes;
	}

	@Override
	public String toString() {
		String string;
		if (naoTerminal.isInicial()) {
			string = "*" + naoTerminal.getNome() + "> ";
		} else {
			string = naoTerminal.getNome() + "> ";
		}
		for (Producao producao : producoes) {
			string = string.concat(producao.toString());
			string = string.concat(" | ");
		}
		return string;
	}

	@Override
	public String getNome() {
		return toString();
	}

	@Override
	public ArrayList<Character> getTermos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTerminal() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNaoTerminal() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Termo clone() {
		return new Producoes(naoTerminal, producoes);
	}

}
