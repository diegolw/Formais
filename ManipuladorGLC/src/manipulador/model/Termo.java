package manipulador.model;

import java.util.ArrayList;

public interface Termo {

	public String getNome();
	
	public ArrayList<Character> getTermos();

	public boolean isTerminal();

	public boolean isNaoTerminal();
	
	public Termo clone();

}
