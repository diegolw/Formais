package manipulador.model.af;

import java.util.LinkedList;

public class Estado {

	private String nome;
	private LinkedList<Transicao> transicoes;
	private boolean ehInicial;
	private boolean ehFinal;
	private Automato pai;

	public Estado(String nome) {
		this.nome = nome;
		this.transicoes = new LinkedList<>();
	}

	public Estado(String nome, boolean ehInicial, boolean ehFinal) {
		this.nome = nome;
		this.transicoes = new LinkedList<>();
		this.ehInicial = ehInicial;
		this.ehFinal = ehFinal;
	}

	public String getNome() {
		return nome;
	}

	public void addTransicao(Estado destino, String simbolo) {
		Transicao t = new Transicao(this, destino, simbolo);
		transicoes.add(t);
	}

}
