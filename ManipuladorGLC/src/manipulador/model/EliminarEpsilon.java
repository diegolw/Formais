package manipulador.model;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class EliminarEpsilon {

	private GramaticaLivreContexto gramatica;

	public EliminarEpsilon(GramaticaLivreContexto input) throws Exception {
		this.gramatica = (GramaticaLivreContexto) input.clone();

		if (!this.gramatica.isValida()) {
			throw new EliminarEpsilonException("Gramatica nao possui producoes");
		}

		if (this.gramatica.contemProducaoEpsilonNaoInicial()) {
			this.gramatica.setNome(this.gramatica.getNome() + " -ep");
			this.gramatica.adicionarPasso("Eliminando Epsilon da Gramatica:");
			eliminar();
			this.gramatica
					.adicionarPasso("Resultado da Eliminacao de Epsilon:");
		}

	}

	private void eliminar() throws Exception {
		// criar o conjunto NE, que corresponde a todos que vao para Epsilon em
		// 1 ou mais passos
		Set<NaoTerminal> ne = new HashSet<NaoTerminal>();
		// determinar o conjunto NE inicial, ou
		// seja, todos naoTerminais que vao para Epsilon em 1 passo
		for (NaoTerminal naoTerminal : this.gramatica.getProducoes().keySet()) {
			for (Producao producao : this.gramatica.getProducoes(naoTerminal)) {
				if (producao.equals(new Producao(
						new Terminal(Constants.epsilon)))) {
					ne.add(naoTerminal);
				}
			}
		}

		// para todo naoTerminal, verificar se ele possui producao com elementos
		// do NE, se tiver, adicionar a NE
		boolean mudou = true;
		while (mudou) {
			mudou = false;
			for (NaoTerminal naoTerminal : this.gramatica.getProducoes()
					.keySet()) {
				for (Producao producao : this.gramatica
						.getProducoes(naoTerminal)) {
					if (producao
							.isProducaoApenasComTermosPertencentesAoConjunto(new HashSet<Termo>(
									ne))) {
						if (ne.add(naoTerminal)) {
							mudou = true;
						}
					}
				}
			}
		}

		// para todas producoes de um naoTerminal, se tiver producao com algum
		// pertencente ao NE, criar novas producoes
		for (NaoTerminal naoTerminal : this.gramatica.getProducoes().keySet()) {
			Set<Producao> novasProducoes = new HashSet<Producao>();
			for (Producao producao : this.gramatica.getProducoes(naoTerminal)) {
				if (!producao.isProducaoSimples()) {
					if (producao.contemAlgumTermoDoConjunto(ne)) {
						novasProducoes
								.addAll(gerarNovasProducoes(producao, ne));
					}
				}
			}
			// adicionar novas producoes criadas
			this.gramatica.getProducoes(naoTerminal).addAll(novasProducoes);
		}

		// verificar se simbolo inicial pertence a NE, se pertencer, adicionar
		// novo simbolo inicial com producao epsilon
		NaoTerminal simboloInicial = this.gramatica.getSimboloInicial();
		if (ne.contains(simboloInicial)
				&& !this.gramatica.getProducoes(
						this.gramatica.getSimboloInicial()).contains(
						new Producao(new Terminal(Constants.epsilon)))) {
			NaoTerminal novoSimboloInicial = new NaoTerminal('$', true);
			simboloInicial.setInicial(false);
			this.gramatica.adicionarProducao(novoSimboloInicial, new Producao(
					simboloInicial));
			this.gramatica.adicionarProducao(novoSimboloInicial, new Producao(
					new Terminal(Constants.epsilon)));

		}

		// eliminar as producoes epsilon
		for (NaoTerminal naoTerminal : this.gramatica.getProducoes().keySet()) {
			// achar producoes para remover
			Set<Producao> producoesParaRemover = new HashSet<Producao>();
			// remove epsilon somente se o naoTerminal n�o for o inicial
			if (!naoTerminal.isInicial()) {
				producoesParaRemover.add(new Producao(new Terminal(
						Constants.epsilon)));
			}
			// remover as producoes
			if (!naoTerminal.equals(new NaoTerminal('$', true))) {
				this.gramatica.getProducoes(naoTerminal).removeAll(
						producoesParaRemover);
			}
		}

	}

	private Set<Producao> gerarNovasProducoes(Producao producao,
			Set<NaoTerminal> ne) {
		// gerar o conjunto potencia de todos ne existentes na producao
		Set<NaoTerminalSuporte> neExistentes = new TreeSet<NaoTerminalSuporte>();
		for (NaoTerminal naoTerminal : ne) {
			int count = producao.contemTermo(naoTerminal);
			if (count > 0) {
				for (int x = 0; x < count; x++) {
					neExistentes.add(new NaoTerminalSuporte(naoTerminal));
				}
			}
		}
		PowerSet<NaoTerminalSuporte> pset = new PowerSet<NaoTerminalSuporte>(
				neExistentes);
		// gerar as novas producoes ao remover os conjuntos potencia da producao
		Set<Producao> novasProducoes = new HashSet<Producao>();
		for (Set<NaoTerminalSuporte> set : pset) {
			Producao novaProducao = (Producao) producao.clone();
			if (set.size() > 0) {
				for (NaoTerminalSuporte naoTerminal : set) {
					novaProducao = novaProducao
							.gerarProducaoRemovendoTermo(naoTerminal);
				}
			}
			if (novaProducao.getProducao().size() > 0) {
				novasProducoes.add(novaProducao);
			}
		}

		return novasProducoes;

	}

	public GramaticaLivreContexto getResultado() {
		return this.gramatica;
	}

	private class NaoTerminalSuporte extends NaoTerminal implements
			Comparable<NaoTerminalSuporte> {

		private String id;

		public NaoTerminalSuporte(NaoTerminal naoTerminal) {
			super(naoTerminal.getTermos());
			this.id = UUID.randomUUID().toString();
		}

		public String getId() {
			return this.id;
		}

		@Override
		public boolean equals(Object obj) {
			if (super.equals(obj)) {
				if (((NaoTerminalSuporte) obj).getId().equals(this.id)) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}

		@Override
		public int compareTo(NaoTerminalSuporte o) {
			return this.id.compareTo(o.getId());
		}

	}

	public class EliminarEpsilonException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public EliminarEpsilonException(String msg) {
			super(msg);
		}
	}

}
