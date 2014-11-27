package manipulador.model;

import java.util.HashSet;
import java.util.Set;

public class EliminarSimbolosInuteis {

	private GramaticaLivreContexto gramatica;

	public EliminarSimbolosInuteis(GramaticaLivreContexto input)
			throws CloneNotSupportedException {
		this.gramatica = (GramaticaLivreContexto) input.clone();
		this.gramatica.setNome(this.gramatica.getNome() + " -si");

		if (!this.gramatica.isValida()) {
			throw new EliminarSimbolosInuteisException("Nao eh gram�tica");
		}

		this.gramatica.adicionarPasso("Eliminando Simbolos Inuteis:");
		eliminarSimbolosNaoFerteis();
		this.gramatica
				.adicionarPasso("Resultado Eliminando Simbolos Nao Ferteis:");
		eliminarSimbolosNaoAlcancaveis();
		this.gramatica
				.adicionarPasso("Resultado Eliminando Simbolos Nao Alcansaveis:");

	}

	private void eliminarSimbolosNaoFerteis() {
		Set<NaoTerminal> nf = new HashSet<NaoTerminal>();

		// definir conjunto inicial nf, ou seja, todos que tem producoes apenas
		// com terminais
		for (NaoTerminal naoTerminal : this.gramatica.getProducoes().keySet()) {
			for (Producao producao : this.gramatica.getProducoes(naoTerminal)) {
				if (producao.isProducaoComApenasTerminais()) {
					nf.add(naoTerminal);
				}
			}
		}
		// determinar todos ferteis
		boolean mudou = true;
		while (mudou) {
			mudou = false;
			for (NaoTerminal naoTerminal : this.gramatica.getProducoes()
					.keySet()) {
				if (!nf.contains(naoTerminal)) {
					for (Producao producao : this.gramatica
							.getProducoes(naoTerminal)) {
						if (producao
								.isProducaoApenasComTerminaisAndTermosPertencentesAoConjunto(nf)) {
							nf.add(naoTerminal);
							mudou = true;
						}
					}
				}
			}
		}
		// determinar os nao ferteis
		Set<NaoTerminal> naoFerteis = new HashSet<NaoTerminal>();
		for (NaoTerminal naoTerminal : this.gramatica.getProducoes().keySet()) {
			if (!nf.contains(naoTerminal)) {
				naoFerteis.add(naoTerminal);
			}
		}
		// eliminar os naoFerteis
		this.gramatica.getProducoes().keySet().removeAll(naoFerteis);
		// eliminar producoes com naoFerteis
		for (NaoTerminal naoTerminal : this.gramatica.getProducoes().keySet()) {
			Set<Producao> producoesParaRemocao = new HashSet<Producao>();
			for (Producao producao : this.gramatica.getProducoes(naoTerminal)) {
				if (producao.contemAlgumTermoDoConjunto(naoFerteis)) {
					producoesParaRemocao.add(producao);
				}
			}
			this.gramatica.getProducoes(naoTerminal).removeAll(
					producoesParaRemocao);
		}

	}

	private void eliminarSimbolosNaoAlcancaveis() {
		// inicializar NA com o simbolo inicial
		Set<Termo> na = new HashSet<Termo>();
		na.add(this.gramatica.getSimboloInicial());

		boolean mudou = true;
		while (mudou) {
			mudou = false;
			OP: for (Termo termo : na) {
				if (termo.isNaoTerminal()) {
					for (Producao producao : this.gramatica
							.getProducoes((NaoTerminal) termo)) {
						if (na.addAll(producao.getProducao())) {
							mudou = true;
							break OP;
						}
					}
				}
			}

		}

		// determinar simbolos naoTerminais nao alcancaveis
		Set<NaoTerminal> naoTerminaisNaoAlcancaveis = new HashSet<NaoTerminal>();
		for (NaoTerminal naoTerminal : this.gramatica.getProducoes().keySet()) {
			if (!na.contains(naoTerminal)) {
				naoTerminaisNaoAlcancaveis.add(naoTerminal);
			}
		}
		// eliminar simbolos naoTerminais nao alcancaveis
		this.gramatica.getProducoes().keySet()
				.removeAll(naoTerminaisNaoAlcancaveis);
		// eliminar producoes com simbolos nao alcancaveis
		for (NaoTerminal naoTerminal : this.gramatica.getProducoes().keySet()) {
			Set<Producao> producoesParaRemocao = new HashSet<Producao>();
			for (Producao producao : this.gramatica.getProducoes(naoTerminal)) {
				if (!producao
						.isProducaoApenasComTermosPertencentesAoConjunto(na)) {
					producoesParaRemocao.add(producao);
				}
			}
			this.gramatica.getProducoes(naoTerminal).removeAll(
					producoesParaRemocao);
		}

	}

	public GramaticaLivreContexto getResultado() {
		return this.gramatica;
	}

	public class EliminarSimbolosInuteisException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public EliminarSimbolosInuteisException(String msg) {
			super(msg);
		}
	}

}
