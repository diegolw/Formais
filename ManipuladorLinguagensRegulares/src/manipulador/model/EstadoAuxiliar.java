package manipulador.model;

import java.util.Iterator;
import java.util.LinkedList;

public class EstadoAuxiliar {

	LinkedList<Estado> estadosInclusos;
	Estado estadoAssociado;

	public String toString() {
		String s = "{";
		Iterator<Estado> it = estadosInclusos.iterator();
		while (it.hasNext())
			s += it.next().getNome() + ",";
		s += "}";
		return s;
	}

	public EstadoAuxiliar() {
		estadosInclusos = new LinkedList<Estado>();
	}

	public EstadoAuxiliar(Estado ei) {
		estadosInclusos = new LinkedList<Estado>();
		estadosInclusos.add(ei);
	}

	public Estado[] getEstadosInclusos() {
		Iterator<Estado> it = estadosInclusos.iterator();
		Estado[] es = new Estado[estadosInclusos.size()];
		int cont = 0;
		while (it.hasNext()) {
			es[cont] = it.next();
			cont++;
		}
		return es;
	}

	public void setEstadoAssociadoParaFinalCasoAlgumEstadoInclusoSejaTambem() {
		Iterator<Estado> it = estadosInclusos.iterator();
		if (estadoAssociado != null) {
			estadoAssociado.setFinal(false);
			while (it.hasNext()) {
				if (it.next().ehEstadoFinal())
					estadoAssociado.setFinal(true);
			}
		}
	}

	public void setEstadoAssociado(Estado e) {
		estadoAssociado = e;
		setEstadoAssociadoParaFinalCasoAlgumEstadoInclusoSejaTambem();
		setEstadoAssociadoParaInicialCasoOPrimeiroEstadoInclusoTambemSeja();
	}

	private void setEstadoAssociadoParaInicialCasoOPrimeiroEstadoInclusoTambemSeja() {
		if (estadosInclusos.size() == 1) {
			Estado e = estadosInclusos.get(0);
			if (e.ehEstadoInicial())
				estadoAssociado.seTorneEstadoInicial();
		}
	}

	public Estado getEstadoAssociado() {
		return estadoAssociado;
	}

	public void addEstadoIncluso(Estado e) {
		if (!possui(e))
			estadosInclusos.add(e);
	}

	public boolean ehIgual(EstadoAuxiliar eaux) {
		Iterator<Estado> it = estadosInclusos.iterator();
		while (it.hasNext())
			if (!eaux.possui(it.next()))
				return false;
		it = eaux.estadosInclusos.iterator();
		while (it.hasNext())
			if (!possui(it.next()))
				return false;

		return true;
	}

	public boolean possui(Estado e) {
		Iterator<Estado> it = estadosInclusos.iterator();
		while (it.hasNext())
			if (e == it.next())
				return true;

		return false;
	}

}