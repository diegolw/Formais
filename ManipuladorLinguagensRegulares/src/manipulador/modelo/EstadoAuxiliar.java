package manipulador.modelo;

import java.util.Iterator;
import java.util.LinkedList;

public class EstadoAuxiliar {

	LinkedList<Estado> inclusos;
	Estado associado;

	public String toString() {
		String string = "{";
		Iterator<Estado> iterador = inclusos.iterator();
		while (iterador.hasNext())
			string += iterador.next().getNome() + ",";
		string += "}";
		return string;
	}

	public EstadoAuxiliar() {
		inclusos = new LinkedList<Estado>();
	}

	public EstadoAuxiliar(Estado ei) {
		inclusos = new LinkedList<Estado>();
		inclusos.add(ei);
	}

	public Estado[] getEstadosInclusos() {
		Iterator<Estado> iterador = inclusos.iterator();
		Estado[] estados = new Estado[inclusos.size()];
		int cont = 0;
		while (iterador.hasNext()) {
			estados[cont] = iterador.next();
			cont++;
		}
		return estados;
	}

	// Adiciona estado associado para final caso algum estado incluso também
	// seja final
	public void setAssociadoComoFinalSeInclusoFor() {
		Iterator<Estado> iterador = inclusos.iterator();
		if (associado != null) {
			associado.setFinal(false);
			while (iterador.hasNext()) {
				if (iterador.next().ehFinal())
					associado.setFinal(true);
			}
		}
	}

	public void setEstadoAssociado(Estado estado) {
		associado = estado;
		setAssociadoComoFinalSeInclusoFor();
		setAssociadoInicialSePrimeiroInclusoFor();
	}

	// Seta estado associado como inicial caso o primeiro estado incluso também
	// for inicial
	private void setAssociadoInicialSePrimeiroInclusoFor() {
		if (inclusos.size() == 1) {
			Estado e = inclusos.get(0);
			if (e.ehInicial())
				associado.setInicial();
		}
	}

	public Estado getEstadoAssociado() {
		return associado;
	}

	public void addEstadoIncluso(Estado estado) {
		if (!possui(estado)) {
			inclusos.add(estado);
		}
	}

	public boolean ehIgual(EstadoAuxiliar auxiliar) {
		Iterator<Estado> iterador = inclusos.iterator();
		while (iterador.hasNext()) {
			if (!auxiliar.possui(iterador.next())) {
				return false;
			}
		}
		iterador = auxiliar.inclusos.iterator();
		while (iterador.hasNext()) {
			if (!possui(iterador.next())) {
				return false;
			}
		}
		return true;
	}

	public boolean possui(Estado e) {
		Iterator<Estado> iterador = inclusos.iterator();
		while (iterador.hasNext()) {
			if (e == iterador.next()) {
				return true;
			}
		}
		return false;
	}

}