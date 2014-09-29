package manipulador.modelo;

import java.util.Iterator;
import java.util.LinkedList;

public class EstadoAuxiliar extends Estado {

	private LinkedList<Estado> inclusos;
	
	public EstadoAuxiliar(String nome, Estado estado) {
		super(nome);
		inclusos = new LinkedList<Estado>();
		inclusos.add(estado);
	}
	
	public EstadoAuxiliar(String nome, LinkedList<Estado> estados) {
		super(nome);
		inclusos = new LinkedList<Estado>();
		for (Estado estado : estados) {
			addEstadoIncluso(estado);
		}
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

	public boolean possui(Estado estado) {
		Iterator<Estado> iterador = inclusos.iterator();
		while (iterador.hasNext()) {
			if (estado == iterador.next()) {
				return true;
			}
		}
		return false;
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

	public LinkedList<Estado> getEstados() {
		return inclusos;
	}

}