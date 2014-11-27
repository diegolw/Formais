package manipulador.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class EliminarRecursaoEsquerda {

	private GramaticaLivreContexto gramatica;
	private Set<Character> nomesSimbolosUtilizados;

	private static int MENOR = -1;
	private static int MAIOR = 1;
	private static int IGUAL = 0;
	private static int NADA = -2;

	public EliminarRecursaoEsquerda(GramaticaLivreContexto input)
			throws Exception {
		this.gramatica = (GramaticaLivreContexto) input.clone();

		if (!this.gramatica.isValida()) {
			throw new EliminarRecursaoEsquerdaException("Nao eh gram�tica");
		}

		if (!this.gramatica.isPropria()) {
			this.gramatica = new TransformarPropria(this.gramatica)
					.getResultado();
		}
		if (!this.gramatica.isPropria()) {
			throw new EliminarRecursaoEsquerdaException(
					"Gramatica nao eh propria");
		}
		if (this.gramatica.hasRecursaoEsquerda()) {
			this.gramatica.setNome(this.gramatica.getNome() + " -re");
			this.gramatica
					.adicionarPasso("Eliminando Recursao a Esquerda da Gramatica:");
			determinarNomesSimbolosUtilizados();

			Set<NaoTerminalSuporte> naoTerminaisOrdenados = ordenarNaoTerminais();
			ordenarProcessarProducoes(naoTerminaisOrdenados);
			this.gramatica
					.adicionarPasso("Resultado da Eliminacao de Recursao a Esquerda:");
		}

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

	private void ordenarProcessarProducoes(
			Set<NaoTerminalSuporte> naoTerminaisOrdenados) {

		boolean mudou = true;
		int passo = 0;
		while (mudou) {
			mudou = false;

			// nesse bloco vai eliminar recurs�o esquerda direta se achar
			OP: for (NaoTerminalSuporte naoTerminalSuporte : naoTerminaisOrdenados) {
				if (possuiRecursaoEsquerdaDireta(naoTerminalSuporte,
						this.gramatica.getProducoes(naoTerminalSuporte))) {
					NaoTerminal novoTerminal = new NaoTerminal(
							gerarNovoNomeSimbolo());
					eliminarRecursaoEsquerdaDireta(naoTerminalSuporte,
							novoTerminal,
							this.gramatica.getProducoes(naoTerminalSuporte));
					this.gramatica.adicionarPasso("RE - Passo " + passo + " :");
					passo++;
					mudou = true;
					break OP;
				}
			}

			// nesse bloco vai detectar qual a melhor substitui��o para fazer
			// com que i < j ou i == j para depois eliminar no bloco anterior
			if (!mudou) {
				NaoTerminalSuporte naoTerminalSuporte = determinarQuemSubstituirDosIndiretos(naoTerminaisOrdenados);
				if (naoTerminalSuporte != null) {
					OP: for (Producao producao : this.gramatica
							.getProducoes(naoTerminalSuporte)) {
						if (producao.getPrimeiroTermo().isNaoTerminal()) {
							NaoTerminalSuporte outroNaoTerminal = getNaoTerminalSuporte(
									(NaoTerminal) producao.getPrimeiroTermo(),
									naoTerminaisOrdenados);
							if (outroNaoTerminal != null
									&& naoTerminalSuporte
											.compareTo(outroNaoTerminal) > 0) {
								substituirProducao(naoTerminalSuporte,
										producao, outroNaoTerminal);
								this.gramatica.adicionarPasso("RE - Passo "
										+ passo + " :");
								passo++;
								mudou = true;
								break OP;
							}
						}
					}
				}

			}

		}

	}

	private NaoTerminalSuporte determinarQuemSubstituirDosIndiretos(
			Set<NaoTerminalSuporte> naoTerminaisOrdenados) {

		HashMap<Integer, Set<NaoTerminalSuporte>> substituicoesPossiveis = new HashMap<Integer, Set<NaoTerminalSuporte>>();

		// determinar os niveis de substituicao de todos
		for (NaoTerminalSuporte naoTerminalSuporte : naoTerminaisOrdenados) {
			Set<Integer> niveisNaoTerminal = new HashSet<Integer>();
			for (Producao producao : this.gramatica
					.getProducoes(naoTerminalSuporte)) {
				if (producao.getPrimeiroTermo().isNaoTerminal()) {
					NaoTerminalSuporte outroNaoTerminal = getNaoTerminalSuporte(
							(NaoTerminal) producao.getPrimeiroTermo(),
							naoTerminaisOrdenados);
					if (outroNaoTerminal != null
							&& naoTerminalSuporte.compareTo(outroNaoTerminal) > 0) {
						niveisNaoTerminal.add(determinarNivelSubstituicao(
								outroNaoTerminal, naoTerminaisOrdenados,
								naoTerminalSuporte));
					}
				}
			}
			if (!niveisNaoTerminal.isEmpty()) {
				int indice = NADA;
				if (niveisNaoTerminal.contains(MENOR)) {
					indice = MENOR;
				} else if (niveisNaoTerminal.contains(IGUAL)) {
					indice = IGUAL;
				} else if (niveisNaoTerminal.contains(MAIOR)) {
					indice = MAIOR;
				}
				if (indice != NADA
						&& !substituicoesPossiveis.containsKey(indice)) {
					substituicoesPossiveis.put(indice,
							new HashSet<NaoTerminalSuporte>());
				}
				if (indice != NADA) {
					substituicoesPossiveis.get(indice).add(naoTerminalSuporte);
				}
			}

		}

		// determinar quem que deve ser o primeiro a substituir

		if (substituicoesPossiveis.containsKey(MENOR)) {
			return substituicoesPossiveis.get(MENOR).iterator().next();
		} else if (substituicoesPossiveis.containsKey(IGUAL)) {
			return substituicoesPossiveis.get(IGUAL).iterator().next();
		} else if (substituicoesPossiveis.containsKey(MAIOR)) {
			return substituicoesPossiveis.get(MAIOR).iterator().next();
		}

		return null;
	}

	private int determinarNivelSubstituicao(NaoTerminalSuporte naoTerminal,
			Set<NaoTerminalSuporte> naoTerminaisOrdenados,
			NaoTerminalSuporte naoTerminalQueRecebe) {
		boolean hasIgual = false;
		boolean hasMenor = false;
		boolean hasMaior = false;

		for (Producao producao : this.gramatica.getProducoes(naoTerminal)) {
			if (producao.getPrimeiroTermo().isNaoTerminal()) {
				NaoTerminalSuporte outroNaoTerminal = getNaoTerminalSuporte(
						(NaoTerminal) producao.getPrimeiroTermo(),
						naoTerminaisOrdenados);
				if (outroNaoTerminal != null
						&& naoTerminalQueRecebe.compareTo(outroNaoTerminal) > 0) {
					hasMaior = true;
				} else if (outroNaoTerminal != null
						&& naoTerminalQueRecebe.compareTo(outroNaoTerminal) == 0) {
					hasIgual = true;
				} else if (outroNaoTerminal != null
						&& naoTerminalQueRecebe.compareTo(outroNaoTerminal) < 0) {
					hasMenor = true;
				}
			}
		}

		if (hasMenor) {
			return MENOR;
		} else if (hasIgual) {
			return IGUAL;
		} else if (hasMaior) {
			return MAIOR;
		}

		return NADA;
	}

	private void eliminarRecursaoEsquerdaDireta(NaoTerminalSuporte atual,
			NaoTerminal novo, Set<Producao> producoes) {
		Set<Producao> producoesComRecursao = new HashSet<Producao>();
		Set<Producao> producoesSemRecursao = new HashSet<Producao>();

		for (Producao prod : producoes) {
			Producao producao = prod.clone();
			if (possuiRecursaoEsquerdaDireta(atual, producao)) {
				producoesComRecursao.add(producao);
			} else {
				producoesSemRecursao.add(producao);
			}
		}

		Set<Producao> producoesAtual = new HashSet<Producao>();
		Set<Producao> producoesNovo = new HashSet<Producao>();

		// setar producoes do naoTerminal atual
		for (Producao producao : producoesSemRecursao) {
			ArrayList<Termo> sequencia;
			// se a producao for epsilon
			if (producao.getProducao().size() == 1
					&& producao.getPrimeiroTermo().equals(
							new Terminal(Constants.epsilon))) {
				sequencia = new ArrayList<Termo>();
			} else { // se a producao nao for epsilon
				sequencia = (ArrayList<Termo>) producao.getProducao().clone();
			}
			sequencia.add(novo);
			producoesAtual.add(new Producao(sequencia));
		}

		this.gramatica.getProducoes(atual).clear();
		this.gramatica.getProducoes(atual).addAll(producoesAtual);

		// setar producoes do novo naoTerminal, e mais a producao epsilon

		for (Producao producao : producoesComRecursao) {
			ArrayList<Termo> sequencia = new ArrayList<Termo>();
			sequencia.addAll(producao.getProducao().subList(1,
					producao.getProducao().size()));
			sequencia.add(novo);
			producoesNovo.add(new Producao(sequencia));
		}

		producoesNovo.add(new Producao(new Terminal(Constants.epsilon)));
		this.gramatica.adicionarProducoes(novo, producoesNovo);

	}

	private boolean possuiRecursaoEsquerdaDireta(NaoTerminal naoTerminal,
			Set<Producao> producoes) {
		for (Producao producao : producoes) {
			if (possuiRecursaoEsquerdaDireta(naoTerminal, producao)) {
				return true;
			}
		}

		return false;
	}

	private boolean possuiRecursaoEsquerdaDireta(NaoTerminal naoTerminal,
			Producao producao) {
		if (producao.getPrimeiroTermo().equals(naoTerminal)) {
			return true;
		}

		return false;
	}

	private void substituirProducao(NaoTerminalSuporte naoTerminalSuporte,
			Producao producao, NaoTerminalSuporte outroTerminalSuporte) {
		// para toda producao do outroNaoTerminalSuporte, criar novas producoes,
		// adicionar a gramatica e depois remover a producao atual

		// pegar a producao sem o primeiro termo, que vai ser substituido pelas
		// producoes do outroTerminal
		Set<Producao> novasProducoes = new HashSet<Producao>();
		ArrayList<Termo> sequenciaSem1oTermo;
		if (producao.getProducao().size() > 1) {
			sequenciaSem1oTermo = new ArrayList<Termo>(producao.getProducao()
					.subList(1, producao.getProducao().size()));
		} else {
			sequenciaSem1oTermo = new ArrayList<Termo>();
		}

		for (Producao outraProducao : this.gramatica
				.getProducoes(outroTerminalSuporte)) {
			ArrayList<Termo> sequencia = (ArrayList<Termo>) outraProducao
					.getProducao().clone();
			sequencia.addAll((ArrayList<Termo>) sequenciaSem1oTermo.clone());
			Producao novaProducao = new Producao(sequencia);
			novasProducoes.add(novaProducao);
		}

		// deletar producao atual
		this.gramatica.getProducoes(naoTerminalSuporte).remove(producao);
		// adicionar novas producoes
		this.gramatica.getProducoes(naoTerminalSuporte).addAll(novasProducoes);

	}

	private NaoTerminalSuporte getNaoTerminalSuporte(NaoTerminal naoTerminal,
			Set<NaoTerminalSuporte> naoTerminaisOrdenados) {
		for (NaoTerminalSuporte naoTerminalSuporte : naoTerminaisOrdenados) {
			if (naoTerminalSuporte.equals(naoTerminal)) {
				return naoTerminalSuporte;
			}
		}
		return null;
	}

	private Set<NaoTerminalSuporte> ordenarNaoTerminais() {
		Set<NaoTerminalSuporte> naoTerminaisOrdenados = new HashSet<EliminarRecursaoEsquerda.NaoTerminalSuporte>();
		Set<NaoTerminal> naoTerminaisAnalisados = new HashSet<NaoTerminal>();
		int ordem = 0;

		Stack<NaoTerminal> stack = new Stack<NaoTerminal>();
		stack.add(gramatica.getSimboloInicial());

		while (!stack.isEmpty()) {
			NaoTerminal atual = stack.pop();
			naoTerminaisOrdenados.add(new NaoTerminalSuporte(atual, ordem));
			ordem++;
			for (Producao producao : this.gramatica.getProducoes(atual)) {
				for (Termo termo : producao.getProducao()) {
					if (termo.isNaoTerminal()) {
						NaoTerminal outro = (NaoTerminal) termo;
						if (!naoTerminaisAnalisados.contains(outro)) {
							stack.push(outro);
							naoTerminaisAnalisados.add(outro);
						}
					}
				}
			}

		}

		return naoTerminaisOrdenados;
	}

	public GramaticaLivreContexto getResultado() {
		return this.gramatica;
	}

	public class NaoTerminalSuporte extends NaoTerminal implements
			Comparable<NaoTerminalSuporte> {

		private int ordem;

		public NaoTerminalSuporte(Character nome) {
			super(nome);
		}

		public NaoTerminalSuporte(NaoTerminal naoTerminal, int ordem) {
			super(naoTerminal.getTermos());
			this.ordem = ordem;
		}

		public int getOrdem() {
			return this.ordem;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof NaoTerminalSuporte) {
				if (((NaoTerminalSuporte) obj).getOrdem() == this.ordem) {
					return true;
				} else {
					return false;
				}
			} else if (obj instanceof NaoTerminal) {
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
		public int compareTo(NaoTerminalSuporte o) {
			if (this.getOrdem() > o.getOrdem()) {
				return 1;
			} else if (this.getOrdem() == o.getOrdem()) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	public class EliminarRecursaoEsquerdaException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public EliminarRecursaoEsquerdaException(String msg) {
			super(msg);
		}
	}

}
