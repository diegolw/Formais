package manipulador.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Producao implements Serializable {

	private ArrayList<Termo> producao;

	public Producao() {
		this.producao = new ArrayList<Termo>();
	}

	public Producao(Termo termo) {
		this.producao = new ArrayList<Termo>();
		this.producao.add(termo);
	}

	public Producao(ArrayList<Termo> producao) {
		this.producao = producao;
	}

	public ArrayList<Termo> getProducao() {
		return producao;
	}

	@Override
	public String toString() {
		String string = "";
		for (Termo termo : producao) {
			string = string + termo.toString() + " ";
		}
		return string;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Producao) {
			Producao other = (Producao) obj;

			if (other.getProducao().size() == this.getProducao().size()) {
				for (int x = 0; x < other.getProducao().size(); x++) {
					if (!other.getProducao().get(x)
							.equals(this.getProducao().get(x))) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashcode = 0;
		for (Termo termo : producao) {
			hashcode = 31 * hashcode + termo.hashCode();
		}
		return hashcode;
	}

	/**
	 * Pegar o restante da producao a partir de um termo especifico que deve
	 * estar presente na producao
	 * 
	 * @param termo
	 * @return
	 */
	public Producao getSubProducao(Termo termo, int posicao) {
		boolean podeAdicionar = false;
		ArrayList<Termo> novaProducao = new ArrayList<Termo>();
		List<Termo> subProducao = this.producao.subList(posicao,
				this.producao.size());
		for (Termo atual : subProducao) {
			if (!podeAdicionar && atual.equals(termo)) {
				podeAdicionar = true;
			} else if (podeAdicionar) {
				novaProducao.add(atual);
			}

		}
		if (novaProducao.size() > 0) {
			return new Producao(novaProducao);
		} else {
			return null;
		}
	}

	public Termo getPrimeiroTermo() {
		if (this.producao.size() > 0) {
			return this.producao.get(0);
		}
		return null;
	}

	public Termo getUltimoTermo() {
		if (this.producao.size() > 0) {
			return this.producao.get(this.producao.size() - 1);
		}
		return null;
	}

	@Override
	public Producao clone() {

		ArrayList<Termo> termos = new ArrayList<Termo>();

		for (Termo termoAtual : this.getProducao()) {
			termos.add(termoAtual.clone());
		}

		Producao novaProd = new Producao(termos);

		return novaProd;

	}

	public Producao getProducaoSemUltimo() {
		Producao novaProducao = (Producao) this.clone();

		if (this.producao.size() > 1) {
			novaProducao.getProducao().remove(this.producao.size() - 1);
			return novaProducao;
		} else {
			return null;
		}
	}

	public Producao getProducaoSemPrimeiro() {
		Producao novaProducao = (Producao) this.clone();
		if (this.producao.size() > 1) {
			novaProducao.getProducao().remove(0);
			return novaProducao;
		} else {
			return null;
		}
	}

	public boolean isProducaoSimples() {
		if (this.producao.size() == 1 && this.producao.get(0).isNaoTerminal()) {
			return true;
		}
		return false;
	}

	public boolean isProducaoApenasComTermosPertencentesAoConjunto(Set<Termo> ne) {
		for (Termo termo : this.producao) {
			if (!ne.contains(termo)) {
				return false;
			}
		}

		return true;
	}

	public boolean isProducaoApenasComTerminaisAndTermosPertencentesAoConjunto(
			Set<NaoTerminal> ne) {
		for (Termo termo : this.producao) {
			if (termo.isNaoTerminal()) {
				if (!ne.contains((NaoTerminal) termo)) {
					return false;
				}
			}
		}

		return true;
	}

	public boolean contemAlgumTermoDoConjunto(Set<NaoTerminal> ne) {
		for (Termo termo : this.producao) {
			if (termo.isNaoTerminal()) {
				if (ne.contains((NaoTerminal) termo)) {
					return true;
				}
			}
		}

		return false;
	}

	public int contemTermo(NaoTerminal naoTerminal) {
		int count = 0;
		for (Termo termo : this.producao) {
			if (termo.isNaoTerminal()) {
				if (naoTerminal.equals(termo)) {
					count++;
				}
			}
		}

		return count;
	}

	public Producao gerarProducaoRemovendoTermo(NaoTerminal naoTerminal) {
		Producao novaProducao = (Producao) this.clone();
		int pos = 0;
		for (Termo termo : novaProducao.getProducao()) {
			if (termo.equals(naoTerminal)) {
				novaProducao.getProducao().remove(pos);
				break;
			}
			pos++;
		}
		return novaProducao;
	}

	public boolean isProducaoComApenasTerminais() {
		if (this.producao.size() > 0) {
			for (Termo termo : this.producao) {
				if (termo.isNaoTerminal()) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public Producao concat(Producao outraProd) {
		Producao novaProducao = (Producao) this.clone();

		if (outraProd != null && outraProd.getProducao() != null
				&& !outraProd.getProducao().isEmpty()) {
			for (Termo termo : outraProd.getProducao()) {
				novaProducao.getProducao().add(termo);
			}
		}

		return novaProducao;
	}

	public void substituirTermo(Termo velho, Termo novo) {
		int idx = 0;
		for (Termo termo : this.producao) {
			if (termo.equals(velho)) {
				this.producao.set(idx, novo);
			}
			idx++;
		}

	}

}
