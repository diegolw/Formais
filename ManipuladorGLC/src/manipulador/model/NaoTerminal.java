package manipulador.model;

import java.io.Serializable;
import java.util.ArrayList;

public class NaoTerminal implements Serializable, Termo {

	private ArrayList<Character> termos;
	private boolean inicial;

	public NaoTerminal() {

	}

	public NaoTerminal(ArrayList<Character> termos) {
		this.termos = termos;
		this.inicial = false;
	}

	public NaoTerminal(ArrayList<Character> termos, boolean inicial) {
		this.termos = termos;
		this.inicial = inicial;
	}

	public NaoTerminal(Character nome) {
		this.termos = new ArrayList<Character>();
		this.termos.add(nome);
		this.inicial = false;
	}

	public NaoTerminal(Character nome, boolean inicial) {
		this.termos = new ArrayList<Character>();
		this.termos.add(nome);
		this.inicial = inicial;
	}

	public void setSimbolo(Character c) {
		this.termos = new ArrayList<Character>();
		this.termos.add(c);
	}

	@Override
	public String getNome() {
		String string = "";
		for (Character c : termos) {
			string = string + c;
		}
		return string;
	}

	@Override
	public int hashCode() {
		int hashcode = 0;
		for (Character termo : termos) {
			hashcode = 31 * hashcode + termo.hashCode();
		}
		return hashcode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NaoTerminal) {
			NaoTerminal outro = (NaoTerminal) obj;
			if (outro.getTermos().size() == this.getTermos().size()) {
				int match = 0;
				for (int x = 0; x < outro.getTermos().size(); x++) {
					if (outro.getTermos().get(x)
							.equals(this.getTermos().get(x))) {
						match++;
					}
				}
				if (match == this.getTermos().size()) {
					return true;
				}
			}

		}
		return false;
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public boolean isNaoTerminal() {
		return true;
	}

	public boolean isInicial() {
		return this.inicial;
	}

	public void setInicial(boolean inicial) {
		this.inicial = inicial;
	}

	@Override
	public Termo clone() {
		NaoTerminal novoNaoTerminal = new NaoTerminal(termos, inicial);
		return novoNaoTerminal;
	}

	@Override
	public ArrayList<Character> getTermos() {
		return termos;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getNome();
	}

}
