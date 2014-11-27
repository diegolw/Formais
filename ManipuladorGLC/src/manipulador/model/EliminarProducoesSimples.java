package manipulador.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class EliminarProducoesSimples {

	private GramaticaLivreContexto gramatica;

	public EliminarProducoesSimples(GramaticaLivreContexto input)
			throws Exception {
		this.gramatica = (GramaticaLivreContexto) input.clone();

		if (!this.gramatica.isValida()) {
			throw new EliminarProducoesSimplesException(
					"Gramatica nao possui producoes");
		}

		// eliminar Epsilon
		if (this.gramatica.contemProducaoEpsilonNaoInicial()) {
			this.gramatica = new EliminarEpsilon(this.gramatica).getResultado();
		}
		// eliminar producoes simples
		this.gramatica.setNome(this.gramatica.getNome() + " -ps");
		this.gramatica
				.adicionarPasso("Eliminando Producoes Simples da Gramatica:");
		processar();
		this.gramatica
				.adicionarPasso("Resultado Eliminacao Produçoes Simples:");

	}

	private void processar() {
		// construir conjuntos NA
		HashMap<NaoTerminal, Set<NaoTerminal>> na = new HashMap<NaoTerminal, Set<NaoTerminal>>();
		for (NaoTerminal naoTerminal : gramatica.getProducoes().keySet()) {
			na.put(naoTerminal, new HashSet<NaoTerminal>());
			na.get(naoTerminal).add(naoTerminal);
		}
		// colocar as producoes simples diretas
		for (NaoTerminal naoTerminal : gramatica.getProducoes().keySet()) {
			for (Producao producao : gramatica.getProducoes(naoTerminal)) {
				if (producao.isProducaoSimples()) {
					na.get(naoTerminal).add(
							(NaoTerminal) producao.getPrimeiroTermo());
				}
			}
		}
		// determinar as producoes indiretas
		for (NaoTerminal naoTerminal : na.keySet()) {
			Set<NaoTerminal> simplesIndiretas = new HashSet<NaoTerminal>();
			for (NaoTerminal simples : na.get(naoTerminal)) {
				if (!naoTerminal.equals(simples)) {
					simplesIndiretas.addAll(na.get(simples));
				}
			}
			na.get(naoTerminal).addAll(simplesIndiretas);
		}
		// adicionar novas producoes
		HashMap<NaoTerminal, Set<Producao>> producoes = new HashMap<NaoTerminal, Set<Producao>>();
		for (NaoTerminal naoTerminal : na.keySet()) {
			Set<Producao> producoesParaAdicionar = new HashSet<Producao>();
			for (NaoTerminal simples : na.get(naoTerminal)) {
				if (!naoTerminal.equals(simples)) {
					for (Producao producao : gramatica.getProducoes(simples)) {
						if (!producao.isProducaoSimples()) {
							producoesParaAdicionar.add(producao);
						}
					}
				}
			}
			producoes.put(naoTerminal, producoesParaAdicionar);
		}
		// adicionar
		for (NaoTerminal naoTerminal : producoes.keySet()) {
			gramatica.getProducoes(naoTerminal).addAll(
					producoes.get(naoTerminal));
		}
		// eliminar producoes simples
		for (NaoTerminal naoTerminal : gramatica.getProducoes().keySet()) {
			Set<Producao> producoesParaEliminar = new HashSet<Producao>();
			for (Producao producao : gramatica.getProducoes(naoTerminal)) {
				if (producao.isProducaoSimples()) {
					producoesParaEliminar.add(producao);
				}
			}
			gramatica.getProducoes(naoTerminal)
					.removeAll(producoesParaEliminar);
		}

	}

	public GramaticaLivreContexto getResultado() {
		return this.gramatica;
	}

	public class EliminarProducoesSimplesException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public EliminarProducoesSimplesException(String msg) {
			super(msg);
		}
	}

}
