package manipulador.model.gramatica;

public class Terminal {
	private char terminal;
	
	protected Terminal(char _ter){
		setTerminal(_ter);
	}

	public char getTerminal() {
		return terminal;
	}

	public void setTerminal(char terminal) {
		this.terminal = terminal;
	}
	

}
