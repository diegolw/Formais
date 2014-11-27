package manipulador.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Terminal implements Serializable, Termo {

	private ArrayList<Character> nome;

	public Terminal() {

	}

	public Terminal(Character nome) {
		this.nome = new ArrayList<Character>();
		this.nome.add(nome);
	}

	public Terminal(ArrayList<Character> nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		int hashcode = 0;
		for (Character termo : nome) {
			hashcode = 31 * hashcode + termo.hashCode();
		}
		return hashcode;
	}

	@Override
	public String getNome() {
		String a = "";
		for (Character c : this.nome) {
			a = a.concat(c.toString());
		}
		return a;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Terminal) {
			if (((Terminal) obj).getNome().equals(this.getNome())) {
				return true;
			}

		}
		return false;
	}

	@Override
	public boolean isTerminal() {
		return true;
	}

	@Override
	public boolean isNaoTerminal() {
		return false;
	}

	@Override
	public ArrayList<Character> getTermos() {
		return nome;
	}

	@Override
	public Termo clone() {
		Terminal novoTerminal = new Terminal(
				(ArrayList<Character>) nome.clone());
		return novoTerminal;
	}

	@Override
	public String toString() {
		String string = "";

		for (Character c : this.nome) {
			string = string + c.toString();
		}

		return string;
	}

}
