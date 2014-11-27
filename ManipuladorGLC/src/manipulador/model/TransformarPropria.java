package manipulador.model;

public class TransformarPropria {

	private GramaticaLivreContexto gramatica;

	public TransformarPropria(GramaticaLivreContexto input) throws Exception {
		this.gramatica = (GramaticaLivreContexto) input.clone();
		this.gramatica.setNome(this.gramatica.getNome() + " -pro");

		if (!gramatica.isPropria()) {
			try {
				gramatica = new EliminarSimbolosInuteis(this.gramatica)
						.getResultado();
			} catch (Exception e) {
				throw new TransformarPropriaException(
						"Erro ao eliminar simbolos inuteis:" + e.getMessage(),
						e);
			}
			if (gramatica.hasCiclos()) {
				try {
					gramatica = new EliminarProducoesSimples(this.gramatica)
							.getResultado();
				} catch (Exception e) {
					throw new TransformarPropriaException(
							"Erro ao eliminar producoes simples"
									+ e.getMessage(), e);
				}
			}
			if (gramatica.contemProducaoEpsilonNaoInicial()) {
				try {
					gramatica = new EliminarEpsilon(this.gramatica)
							.getResultado();
				} catch (Exception e) {
					throw new TransformarPropriaException(
							"Erro ao eliminar epsilon" + e.getMessage(), e);
				}
			}
		}
	}

	public GramaticaLivreContexto getResultado() {
		return this.gramatica;
	}

	public class TransformarPropriaException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TransformarPropriaException(String msg, Throwable e) {
			super(msg, e);
		}
	}
}
