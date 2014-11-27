package manipulador.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Fatorar {

	private GramaticaLivreContexto gramatica;
	private Set<Character> nomesSimbolosUtilizados;
	private int numero_max_passos = 0;

	public Fatorar(GramaticaLivreContexto input, int numero_max_passos)
			throws Exception {
		this.gramatica = (GramaticaLivreContexto) input.clone();

		this.numero_max_passos = numero_max_passos;
		if (!this.gramatica.isValida()) {
			throw new FatorarException("Nao eh gramatica");
		}

		if (this.gramatica.hasRecursaoEsquerda()) {
			this.gramatica = new EliminarRecursaoEsquerda(this.gramatica)
					.getResultado();
		}

		// if (this.gramatica.contemProducaoEpsilonNaoInicial()) {
		// this.gramatica = new EliminarEpsilon(this.gramatica).getResultado();
		// }

		// this.gramatica = new
		// EliminarProducoesSimples(this.gramatica).getResultado();

		this.gramatica.adicionarPasso("Fatorando Gramatica:");
		determinarNomesSimbolosUtilizados();
		processar();

		this.gramatica.setNome(this.gramatica.getNome() + " -f");

		this.gramatica = new EliminarSimbolosInuteis(this.gramatica)
				.getResultado();

	}

	private void determinarNomesSimbolosUtilizados() {
		nomesSimbolosUtilizados = new HashSet<Character>();
		for (NaoTerminal naoTerminal : this.gramatica.getProducoes().keySet()) {
			if (naoTerminal.getTermos().size() == 1) {
				nomesSimbolosUtilizados.add(naoTerminal.getTermos().iterator()
						.next());
			}
		}
	}

	private Character gerarNovoNomeSimbolo() {
		char nome = 'A';
		while (nomesSimbolosUtilizados.contains(nome)) {
			nome++;
		}
		nomesSimbolosUtilizados.add(nome);
		return nome;
	}

	private void processar() {
		boolean mudou = true;
		int passo = 0;
		while (mudou) {
			mudou = false;
			if (numero_max_passos > 0) {
				if (passo >= numero_max_passos - 1) {
					throw new FatorarException("Nao terminou de processar em"
							+ numero_max_passos + " passos");
				}
			}
			// determinar os firsts de cada producao de um naoTerminal, e
			// processar para ver se existe nao determinismo
			OP: for (NaoTerminal naoTerminal : gramatica.getProducoes()
					.keySet()) {
				// popular o hash mapeado de um elemento do first para o
				// conjunto de producoes que contem esse no first
				HashMap<Terminal, Set<Producao>> firsts = new HashMap<Terminal, Set<Producao>>();
				for (Producao producao : gramatica.getProducoes(naoTerminal)) {
					Set<Terminal> first = gramatica.getFirst(producao, null);
					for (Terminal terminal : first) {
						if (!firsts.containsKey(terminal)) {
							firsts.put(terminal, new HashSet<Producao>());
						}
						firsts.get(terminal).add(producao);
					}
				}
				mudou = processar(naoTerminal, firsts);
				if (mudou) {
					this.gramatica.adicionarPasso("Fatorando Passo " + passo
							+ ":");
					break OP;
				}
			}
			passo++;
		}

	}

	private boolean processar(NaoTerminal naoTerminal,
			HashMap<Terminal, Set<Producao>> firsts) {
		// retorna true se houve um processamento, ou seja, se nao houver
		// nenhum problema de nao determinismo
		for (Terminal terminal : firsts.keySet()) {
			if (!terminal.equals(new Terminal(Constants.epsilon))) {
				if (firsts.get(terminal).size() > 1) {
					// eliminar o naodeterminismo indireto
					// aqui simplesmente vai tirar o nao determinismo indireto,
					// fazendo ele virar nao determinismo direto (mas pode haver
					// um
					// outro nao determinismo, entao aqui vai rodar denovo pra
					// resolver quando voltar)
					if (eliminarNaoDeterminismoIndireto(firsts.get(terminal),
							naoTerminal)) {
						return true;
					}
					// se passou por aqui sem retornar que mudou, quer dizer que
					// nao
					// tem determinismo indireto

					// eliminar nao determinismo direto
					eliminarNaoDeterminismoDireto(naoTerminal, terminal,
							firsts.get(terminal));
					return true;

				}
			}
		}

		return false;
	}

	private boolean eliminarNaoDeterminismoIndireto(Set<Producao> producoes,
			NaoTerminal naoTerminal) {
		// verifcar se o primeiro termo � n�o terminal de todas producoes
		int count = 0;
		NaoTerminal comparador = null;
		for (Producao producao : producoes) {
			if (producao.getPrimeiroTermo().isNaoTerminal()) {
				if (comparador == null) {
					comparador = (NaoTerminal) producao.getPrimeiroTermo();
					count++;
				} else {
					if (producao.getPrimeiroTermo().equals(comparador)) {
						count++;
					}
				}
			}
		}

		// fatorar diretamente j� que o primeiro termo de todas producoes �
		// naoTerminal
		if (count == producoes.size()) {
			eliminarNaoDeterminismoDiretoParaNaoTerminais(naoTerminal,
					comparador, producoes);
			return true;
		} else {
			for (Producao producao : producoes) {
				if (producao.getPrimeiroTermo().isNaoTerminal()) {
					// producao com naoDeterminismo indireto, entao,
					// adiciona as producoes do PrimeiroTermo ao naoTerminal
					Set<Producao> novasProducoes = new HashSet<Producao>();
					Producao producaoBase = producao.getProducaoSemPrimeiro();
					for (Producao producaoDoPrimeiro : gramatica
							.getProducoes((NaoTerminal) producao
									.getPrimeiroTermo())) {
						novasProducoes.add(producaoDoPrimeiro
								.concat(producaoBase));
					}
					// adiciona as novas no lugar da antiga
					this.gramatica.getProducoes(naoTerminal).addAll(
							novasProducoes);
					this.gramatica.getProducoes(naoTerminal).remove(producao);
					return true;
				}
			}
		}
		return false;
	}

	private void eliminarNaoDeterminismoDireto(NaoTerminal naoTerminal,
			Terminal terminal, Set<Producao> set) {
		// gerar nova producao para o NaoTerminal atual, sem o nao determinismo
		ArrayList<Termo> novaProd = new ArrayList<Termo>();
		// pega um novo NaoTerminal
		NaoTerminal novoNaoTerminal = new NaoTerminal(Constants.dollar);
		novaProd.add(terminal);
		novaProd.add(novoNaoTerminal);
		Producao novaProducao = new Producao(novaProd);

		// adicionar novas producoes do novoNaoTerminal
		Set<Producao> producoesNovoNaoTerminal = new HashSet<Producao>();
		for (Producao producao : set) {
			// se a producao sem primeiro for nula, significa que a nova
			// producao deve ser um epsilon
			Producao temp = producao.getProducaoSemPrimeiro();
			if (temp != null) {
				producoesNovoNaoTerminal.add(temp);
			} else {
				producoesNovoNaoTerminal.add(new Producao(new Terminal(
						Constants.epsilon)));
			}
		}
		// adicionar a gramatica se eh um novo nao terminal (verifica se ja nao
		// existe alguem igual)
		NaoTerminal novo;
		if (this.gramatica.getNaoTerminal(producoesNovoNaoTerminal) == null) {
			novo = new NaoTerminal(gerarNovoNomeSimbolo());
			this.gramatica.getProducoes().put(novo, producoesNovoNaoTerminal);
		} else {
			novo = this.gramatica.getNaoTerminal(producoesNovoNaoTerminal);
		}
		// editar a nova producao para substituir o dollar pelo simbolo correto
		// do NaoTerminal
		novaProducao.substituirTermo(new NaoTerminal(Constants.dollar), novo);

		// remover producoes do naoTerminal atual
		this.gramatica.getProducoes(naoTerminal).removeAll(set);
		// adicionar nova producao ao naoTerminal atual
		this.gramatica.getProducoes(naoTerminal).add(novaProducao);

	}

	private void eliminarNaoDeterminismoDiretoParaNaoTerminais(
			NaoTerminal naoTerminal, NaoTerminal primeiroTermo,
			Set<Producao> set) {
		// gerar nova producao para o NaoTerminal atual, sem o nao determinismo
		ArrayList<Termo> novaProd = new ArrayList<Termo>();
		// pega um novo NaoTerminal
		NaoTerminal novoNaoTerminal = new NaoTerminal(Constants.dollar);
		novaProd.add(primeiroTermo);
		novaProd.add(novoNaoTerminal);
		Producao novaProducao = new Producao(novaProd);

		// adicionar novas producoes do novoNaoTerminal
		Set<Producao> producoesNovoNaoTerminal = new HashSet<Producao>();
		for (Producao producao : set) {
			// se a producao sem primeiro for nula, significa que a nova
			// producao deve ser um epsilon
			Producao temp = producao.getProducaoSemPrimeiro();
			if (temp != null) {
				producoesNovoNaoTerminal.add(temp);
			} else {
				producoesNovoNaoTerminal.add(new Producao(new Terminal(
						Constants.epsilon)));
			}
		}
		// adicionar a gramatica se � um novo nao terminal (verifica se ja nao
		// existe alguem igual)
		NaoTerminal novo;
		if (this.gramatica.getNaoTerminal(producoesNovoNaoTerminal) == null) {
			novo = new NaoTerminal(gerarNovoNomeSimbolo());
			this.gramatica.getProducoes().put(novo, producoesNovoNaoTerminal);
		} else {
			novo = this.gramatica.getNaoTerminal(producoesNovoNaoTerminal);
		}
		// editar a nova producao para substituir o dollar pelo simbolo correto
		// do NaoTerminal
		novaProducao.substituirTermo(new NaoTerminal(Constants.dollar), novo);

		// remover producoes do naoTerminal atual
		this.gramatica.getProducoes(naoTerminal).removeAll(set);
		// adicionar nova producao ao naoTerminal atual
		this.gramatica.getProducoes(naoTerminal).add(novaProducao);
	}

	public GramaticaLivreContexto getResultado() {
		return this.gramatica;
	}

	public class FatorarException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public FatorarException(String msg) {
			super(msg);
		}
	}

}
