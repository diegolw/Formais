package manipulador.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class GramaticaLivreContexto implements Serializable, Cloneable {

	private LinkedHashMap<NaoTerminal, Set<Producao>> producoes;
	private String nome;
	private String id;
	private ArrayList<String> passos = new ArrayList<String>();
	private int numeroNaoTerminais;

	public void setNumNaoTerminais(int numero) {
		numeroNaoTerminais = numero;
	}

	public int getNumNaoTerminais() {
		return numeroNaoTerminais;
	}

	public GramaticaLivreContexto() {
		this.id = UUID.randomUUID().toString();
		this.producoes = new LinkedHashMap<NaoTerminal, Set<Producao>>();
	}

	public GramaticaLivreContexto(String nome) {
		this.nome = nome;
		this.producoes = new LinkedHashMap<NaoTerminal, Set<Producao>>();
		this.id = UUID.randomUUID().toString();
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getId() {
		return this.id;
	}

	public void adicionarPasso(String titulo) {
		String string = titulo + "\n";
		String passo = this.producoes.toString();
		string = string + passo;
		passos.add(string);
	}

	public String getPassos() {
		String string = "";
		if (passos.size() > 0) {
			for (String passo : passos) {
				string = string + passo + "\n" + "\n";
			}
		}
		return string;
	}

	public void setPassos(ArrayList<String> passos) {
		this.passos = passos;
	}

	public void adicionarNaoTerminal(NaoTerminal naoTerminal) {
		this.producoes.put(naoTerminal, new HashSet<Producao>());
	}

	public void adicionarProducao(NaoTerminal naoTerminal, Producao producao) {
		if (!producoes.containsKey(naoTerminal)) {
			producoes.put(naoTerminal, new LinkedHashSet<Producao>());
		}
		producoes.get(naoTerminal).add(producao);

	}

	public void adicionarProducoes(NaoTerminal naoTerminal,
			Set<Producao> producoes) {
		for (Producao producao : producoes) {
			adicionarProducao(naoTerminal, producao);
		}
	}

	public Set<Producao> getProducoes(NaoTerminal naoTerminal) {
		return producoes.get(naoTerminal);
	}

	public LinkedHashMap<NaoTerminal, Set<Producao>> getProducoes() {
		return producoes;
	}

	public LinkedHashSet<Producoes> getProducoesObjects() {
		LinkedHashSet<Producoes> prods = new LinkedHashSet<Producoes>();

		for (NaoTerminal naoTerminal : this.producoes.keySet()) {
			if (naoTerminal.isInicial()) {
				prods.add(new Producoes(naoTerminal, this.producoes
						.get(naoTerminal)));
			}
		}

		for (NaoTerminal naoTerminal : this.producoes.keySet()) {
			if (!naoTerminal.isInicial()) {
				prods.add(new Producoes(naoTerminal, this.producoes
						.get(naoTerminal)));
			}
		}

		return prods;

	}

	// public NaoTerminal getNaoTerminal(Character nome) {
	// NaoTerminal comparador = new NaoTerminal(nome);
	// for (NaoTerminal naoTerminal : this.producoes.keySet()) {
	// if (naoTerminal.equals(comparador)) {
	// return naoTerminal;
	// }
	// }
	// return null;
	// }

	public NaoTerminal getNaoTerminal(NaoTerminal comparador) {
		for (NaoTerminal naoTerminal : this.producoes.keySet()) {
			if (naoTerminal.equals(comparador)) {
				return naoTerminal;
			}
		}
		return null;
	}

	public NaoTerminal getNaoTerminal(Set<Producao> set) {
		for (NaoTerminal naoTerminal : this.getProducoes().keySet()) {
			if (this.getProducoes(naoTerminal) != null
					&& this.getProducoes(naoTerminal).size() > 0) {
				int match = 0;
				for (Producao prod : this.getProducoes(naoTerminal)) {
					OP: for (Producao outraProd : set) {
						if (prod.equals(outraProd)) {
							match++;
							break OP;
						}
					}
				}
				if (match == this.getProducoes(naoTerminal).size()) {
					return naoTerminal;
				}
			}
		}

		return null;

	}

	public Set<Terminal> getTerminais() {
		Set<Terminal> terminais = new HashSet<Terminal>();
		for (NaoTerminal naoTerminal : this.producoes.keySet()) {
			for (Producao producao : this.getProducoes(naoTerminal)) {
				for (Termo termo : producao.getProducao()) {
					if (termo.isTerminal()) {
						terminais.add((Terminal) termo);
					}
				}
			}
		}
		return terminais;
	}

	public NaoTerminal getSimboloInicial() {
		for (NaoTerminal naoTerminal : this.producoes.keySet()) {
			if (naoTerminal.isInicial()) {
				return naoTerminal;
			}
		}
		return null;
	}

	public Set<Terminal> getFirst(Producao input, Set<NaoTerminal> jaVisitados)
			throws GramaticaLivreContextoException {
		Producao producao = null;
		if (input == null) {
			return null;
		} else {
			producao = input.clone();
		}

		if (jaVisitados == null) {
			jaVisitados = new HashSet<NaoTerminal>();
		} else {
			jaVisitados = new HashSet<NaoTerminal>(jaVisitados);
		}

		Set<Terminal> first = new LinkedHashSet<Terminal>();
		// se a producao tem apenas um termo, decidir se o termo e terminal ou
		// nao terminal
		if (producao.getProducao().size() == 1) {
			Termo termo = producao.getProducao().get(0);
			if (termo.isNaoTerminal()) {
				// adiciona na lista dos visitados, ou, se ja foi visitado,
				// entao n precisa olhar denovo
				if (jaVisitados.contains(termo)) {
					return first;
				} else {
					jaVisitados.add((NaoTerminal) termo);
				}
				// se o termo � nao terminal, entao tem que pegar o first de
				// todas producoes desse nao terminal
				NaoTerminal naoTerminal = (NaoTerminal) termo;
				if (this.producoes.containsKey(naoTerminal)) {
					if (this.producoes.get(naoTerminal) != null
							&& this.producoes.get(naoTerminal).size() > 0) {
						for (Producao prod : this.producoes.get(naoTerminal)) {
							first.addAll(getFirst(prod, jaVisitados));
						}
					} else {
						throw new GramaticaLivreContextoException(
								"Nao Terminal " + naoTerminal.getNome()
										+ " nao possui producoes na Gramatica "
										+ this.nome);
					}

				} else {
					throw new GramaticaLivreContextoException("Nao Terminal "
							+ naoTerminal.getNome()
							+ " nao existe na Gramatica " + this.nome);
				}
			} else {
				// se o termo eh terminal, entao o first e ele mesmo
				first.add((Terminal) termo);
			}
		} else {
			// caso a producao seja uma producao com multiplos termos
			// pega o primeiro termo e seu first
			Termo termo = producao.getProducao().remove(0);
			// adiciona na lista dos visitados, ou, se ja foi visitado,
			// entao n precisa olhar denovo
			if (termo.isTerminal() || !jaVisitados.contains(termo)) {
				// if (termo.isNaoTerminal()) {
				// jaVisitados.add((NaoTerminal) termo);
				// }
				first = getFirst(new Producao(termo), jaVisitados);
			}

			// caso o first do primeiro termo tenha epsilon,
			if (first.contains(new Terminal(Constants.epsilon))) {
				if (producao.getProducao().size() > 0) {
					first.remove(new Terminal(Constants.epsilon));
					first.addAll(getFirst(producao, jaVisitados));
				}
			}
		}

		return first;

	}

	public HashMap<NaoTerminal, Set<Terminal>> getFollows() throws Exception {
		// TODO: fazer esquema pra evitar ciclos (parece que ja tem!)
		// inicializar os conjuntos follow de todos nao terminais da gramatica
		HashMap<NaoTerminal, Set<Terminal>> follows = new HashMap<NaoTerminal, Set<Terminal>>();
		for (NaoTerminal naoTerminal : this.producoes.keySet()) {
			follows.put(naoTerminal, new HashSet<Terminal>());
			// se o nao terminal � inicial, adicionar $ ao conjunto follow
			if (naoTerminal.isInicial()) {
				follows.get(naoTerminal).add(new Terminal(Constants.dollar));
			}
		}

		// adicionar os first dos "betas" que aparecem depois do "termo" no
		// conjunto follow do "termo"
		for (NaoTerminal naoTerminal : this.producoes.keySet()) {
			for (Producao producao : this.producoes.get(naoTerminal)) {
				int posicao_termo = 0;
				for (Termo termo : producao.getProducao()) {
					if (termo.isNaoTerminal()) {
						Set<Terminal> first = getFirst(
								producao.getSubProducao(termo, posicao_termo),
								null);
						if (first != null) {
							follows.get(termo).addAll(first);
						}
					}
					posicao_termo++;
				}
			}
		}

		// adicionar os follows nos follows
		boolean mudou = true;
		while (mudou) {
			mudou = false;
			// para cada naoTerminal, caso tenha uma producao que termine com um
			// naoTerminal "termo", adicionar o follow do naoTerminal ao "termo"
			for (NaoTerminal naoTerminal : this.producoes.keySet()) {
				for (Producao producao : this.producoes.get(naoTerminal)) {
					// enquanto a producao muda, ou seja, o first do ultimo
					// termo naoTerminal da producao contem Epsilon
					boolean producaomudou = true;
					Producao novaProducao = (Producao) producao.clone();
					while (producaomudou) {
						producaomudou = false;
						Termo ultimoTermo = novaProducao.getUltimoTermo();
						if (ultimoTermo.isNaoTerminal()) {
							// adicionar o follow do NaoTerminal ao Termo
							if (follows.get(ultimoTermo).addAll(
									follows.get(naoTerminal))) {
								mudou = true;
							}
							// se first do ultimo naoTerminal contem epsilon,
							// entao
							// tem que fazer novamente o follow para o penultimo
							// naoTerminal
							Set<Terminal> firstUltimo = getFirst(new Producao(
									ultimoTermo), null);
							if (firstUltimo.contains(new Terminal(
									Constants.epsilon))) {
								// muda a producao
								novaProducao = novaProducao
										.getProducaoSemUltimo();
								if (novaProducao != null) {
									producaomudou = true;
								}
							}
						}
					}
				}
			}
		}

		// eliminar epsilon dos conjuntos follow
		for (NaoTerminal naoTerminal : follows.keySet()) {
			follows.get(naoTerminal).remove(new Terminal(Constants.epsilon));
		}

		return follows;

	}

	public boolean isVazioFirstInterseccaoFollow() throws Exception {
		HashMap<NaoTerminal, Set<Terminal>> interseccoes = getFirstInterseccaoFollow();
		for (NaoTerminal naoTerminal : interseccoes.keySet()) {
			if (possuiProducaoEpsilon(naoTerminal)) {
				Set<Terminal> interseccao = interseccoes.get(naoTerminal);
				if (interseccao != null && !interseccao.isEmpty()) {
					return false;
				}
			}
		}

		return true;
	}

	private boolean possuiProducaoEpsilon(NaoTerminal naoTerminal) {
		for (Producao producao : this.getProducoes(naoTerminal)) {
			if (producao.equals(new Producao(new Terminal(Constants.epsilon)))) {
				return true;
			}
		}
		return false;
	}

	public HashMap<NaoTerminal, Set<Terminal>> getFirsts() {
		HashMap<NaoTerminal, Set<Terminal>> firsts = new HashMap<NaoTerminal, Set<Terminal>>();
		for (NaoTerminal naoTerminal : this.producoes.keySet()) {
			firsts.put(naoTerminal, getFirst(new Producao(naoTerminal), null));
		}
		return firsts;
	}

	public HashMap<NaoTerminal, Set<Terminal>> getFirstInterseccaoFollow()
			throws Exception {
		// calcular o first e o follow de todos naoTerminais
		HashMap<NaoTerminal, Set<Terminal>> follows = getFollows();
		HashMap<NaoTerminal, Set<Terminal>> firsts = new HashMap<NaoTerminal, Set<Terminal>>();
		for (NaoTerminal naoTerminal : this.producoes.keySet()) {
			firsts.put(naoTerminal, getFirst(new Producao(naoTerminal), null));
		}
		// calcular as interseccoes
		HashMap<NaoTerminal, Set<Terminal>> interseccoes = new HashMap<NaoTerminal, Set<Terminal>>();
		for (NaoTerminal naoTerminal : this.producoes.keySet()) {
			Set<Terminal> first = (HashSet<Terminal>) new HashSet<Terminal>(
					firsts.get(naoTerminal)).clone();
			first.retainAll(follows.get(naoTerminal));
			interseccoes.put(naoTerminal, first);
		}

		return interseccoes;
	}

	public boolean isFatorada() {

		for (NaoTerminal naoTerminal : this.producoes.keySet()) {
			HashMap<Integer, Set<Terminal>> firsts = new HashMap<Integer, Set<Terminal>>();
			// determinar os firsts de todas producoes do termo naoTerminal
			int indice = 0;

			for (Producao producao : this.producoes.get(naoTerminal)) {
				firsts.put(indice, getFirst(producao, null));
				indice++;
			}

			// verificar se existe uma indeterminacao, ou seja, se a interseccao
			// de firsts � diferente de vazio
			for (Integer x : firsts.keySet()) {
				for (Integer y : firsts.keySet()) {
					if (x != y) {
						Set<Terminal> firstX = (HashSet<Terminal>) new HashSet<Terminal>(
								firsts.get(x)).clone();
						firstX.retainAll(firsts.get(y));
						if (firstX != null && !firstX.isEmpty()) {
							return false;
						}
					}
				}
			}

		}

		return true;
	}

	public boolean hasSimbolosInuteis() {
		return false;
	}

	public boolean hasCiclos() throws Exception {
		// eliminar epsilon
		GramaticaLivreContexto gramatica = new EliminarEpsilon(this)
				.getResultado();

		// utilizando o algoritmo de producoes simples, construir os NA
		HashMap<NaoTerminal, HashSet<NaoTerminal>> na = new HashMap<NaoTerminal, HashSet<NaoTerminal>>();
		for (NaoTerminal naoTerminal : gramatica.getProducoes().keySet()) {
			na.put(naoTerminal, new HashSet<NaoTerminal>());
			for (Producao producao : gramatica.getProducoes().get(naoTerminal)) {
				if (producao.isProducaoSimples()) {
					na.get(naoTerminal).add(
							(NaoTerminal) producao.getProducao().get(0));
				}
			}
		}

		// verificar se existe ciclos, ou seja, verificar se o conjunto na dos
		// naoTerminais pertencentes ao na do atual possuem o atual
		for (NaoTerminal naoTerminal : na.keySet()) {
			for (NaoTerminal simples : na.get(naoTerminal)) {
				if (na.get(simples).contains(naoTerminal)) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean hasRecursaoEsquerda() {
		for (NaoTerminal naoTerminal : this.producoes.keySet()) {
			for (Producao producao : this.getProducoes(naoTerminal)) {
				if (producao.getPrimeiroTermo().equals(naoTerminal)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Se a gramatica nao tiver naoTerminais ou tiver algum naoTerminal sem
	 * producao, retorna false
	 * 
	 * @return
	 */
	public boolean isValida() {
		if (this.producoes != null && this.producoes.size() > 0) {
			for (NaoTerminal naoTerminal : this.producoes.keySet()) {
				if (this.producoes.get(naoTerminal) == null
						|| this.producoes.get(naoTerminal).isEmpty()) {
					return false;
				}
			}
			if (getSimboloInicial() == null) {
				return false;
			}
			return true;
		}

		return false;

	}

	public boolean contemProducaoEpsilonNaoInicial() {
		for (NaoTerminal naoTerminal : this.producoes.keySet()) {
			if (!naoTerminal.isInicial()) {
				for (Producao producao : this.getProducoes(naoTerminal)) {
					if (producao.equals(new Producao(new Terminal(
							Constants.epsilon)))) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isPropria() throws Exception {
		if (hasCiclos()) {
			return false;
		}

		if (contemProducaoEpsilonNaoInicial()) {
			return false;
		}

		if (hasSimbolosInuteis()) {
			return false;
		}
		return true;

	}

	public boolean isLL1() throws Exception {
		GramaticaLivreContexto input = (GramaticaLivreContexto) this.clone();

		if (input.hasRecursaoEsquerda()) {
			return false;
		}

		if (!input.isFatorada()) {
			return false;
		}

		if (!input.isVazioFirstInterseccaoFollow()) {
			return false;
		}

		return true;

	}

	@Override
	public Object clone() {
		GramaticaLivreContexto gramatica = new GramaticaLivreContexto(
				this.getNome());

		for (NaoTerminal naoTerminal : this.producoes.keySet()) {
			gramatica.adicionarNaoTerminal((NaoTerminal) naoTerminal.clone());
			for (Producao producao : this.producoes.get(naoTerminal)) {
				gramatica.adicionarProducao(naoTerminal, producao.clone());
			}
		}

		gramatica.setPassos(this.passos);
		return gramatica;

	}

	public class GramaticaLivreContextoException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public GramaticaLivreContextoException(String msg) {
			super(msg);
		}
	}

	public void removerNaoTerminal(NaoTerminal naoTerminal) {
		this.producoes.remove(naoTerminal);
		for (NaoTerminal atual : this.producoes.keySet()) {
			Set<Producao> producoesParaRemover = new HashSet<Producao>();
			for (Producao producao : this.producoes.get(atual)) {
				if (producao.contemTermo(naoTerminal) > 0) {
					producoesParaRemover.add(producao);
				}
			}
			this.producoes.get(atual).removeAll(producoesParaRemover);
		}
	}

	@Override
	public String toString() {
		return nome;
	}

	public void setInicial(NaoTerminal naoTerminal) {
		for (NaoTerminal nt : this.getProducoes().keySet()) {
			if (nt.equals(naoTerminal)) {
				nt.setInicial(true);
			} else {
				nt.setInicial(false);
			}
		}

	}

}
