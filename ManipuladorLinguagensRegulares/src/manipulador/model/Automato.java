package manipulador.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class Automato {

	private String nome;
	private String[] alfabeto;
	private LinkedList<Estado> estados;
	private Estado q0;

	public Automato() {
		nome = "";
		alfabeto = new String[0];
		estados = new LinkedList<Estado>();
	}

	public String[] getAlfabeto() {
		return alfabeto;
	}

	public void setNome(String n) {
		nome = n;
	}

	public void setAlfabeto(String[] a) {
		boolean ok = true;
		for (int i = 0; i < a.length; i++) {
			String s = new String(a[i]);
			s = s.trim();
			for (int j = 0; j < s.length(); j++) {
				if (s.charAt(j) == ' ') {
					ok = false;
				}
			}
		}
		if (ok) {
			alfabeto = a;
		}
	}

	public Estado getEstadoInicial() {
		return q0;
	}

	protected void setEstadoInicial(Estado e) {
		if (e.getAutomatoPai() == this) {
			q0 = e;
		}
	}

	public void setEstadoInicial(String n) {
		Estado e = getEstado(n);
		if (e != null) {
			setEstadoInicial(e);
		}
	}

	private EstadoAuxiliar getEstadoDeterminizadoDaTabela(
			EstadoAuxiliar novaLinha,
			LinkedList<EstadoAuxiliar[]> tabelaDeEstadosDeterminizados) {
		Iterator<EstadoAuxiliar[]> it = tabelaDeEstadosDeterminizados
				.iterator();
		while (it.hasNext()) {
			EstadoAuxiliar[] estadosAux = it.next();
			if (estadosAux[0].ehIgual(novaLinha)) {
				return estadosAux[0];
			}
		}
		return null;
	}

	public Automato getAutomatoMinimizado() {
		if (q0 == null) {
			return null;
		}

		Automato automatoMinimo = getAutomatoDeterminizado();
		if (automatoMinimo.ehCompletoETodosEstadosSaoFinais()) {
			Automato retorno = new Automato();
			retorno.setAlfabeto(automatoMinimo.getAlfabeto());
			retorno.addEstado("q0");
			Estado e = retorno.getEstado("q0");
			e.setFinal(true);
			retorno.setEstadoInicial("q0");
			String[] alf = retorno.getAlfabeto();
			for (int i = 0; i < alf.length; i++) {
				e.addTransicao(e, alf[i]);
			}
			return retorno;
		}

		automatoMinimo.eliminarEstadosInalcancaveis();
		automatoMinimo.eliminarEstadosMortos();
		automatoMinimo.normatizaTransicoesNulas();
		automatoMinimo.reduzirPorEquivalencia();
		automatoMinimo.eliminarEstadosMortos();
		return automatoMinimo;
	}

	public boolean ehCompletoETodosEstadosSaoFinais() {
		Estado[] e = this.getEstados();
		for (int i = 0; i < e.length; i++) {
			if (!e[i].ehEstadoFinal()) {
				return false;
			}
			Transicao[] t = e[i].getTransicoes();
			if (t.length != this.getAlfabeto().length) {
				return false;
			}
		}

		return true;
	}

	public void setEstadoFinal(String s) {
		Estado e = getEstado(s);
		if (e != null) {
			setEstadoFinal(e);
		}
	}

	protected void setEstadoFinal(Estado e) {
		e.setFinal(true);
	}

	public void addEstado(String n) {
		if (!existeEstado(n)) {
			Estado e = new Estado(n, this);
			estados.add(e);
		}
	}

	public void addEstado(Estado e) {
		if (!existeEstado(e.getNome())) {
			estados.add(e);
		} else {
			String nomeEst = e.getNome();
			while (existeEstado(nomeEst)) {
				nomeEst += "\'";
			}
			e.setNome(nomeEst);
			estados.add(e);
		}
	}

	public boolean existeEstado(String n) {
		Iterator<Estado> it = estados.iterator();
		while (it.hasNext()) {
			if (it.next().getNome().equals(n)) {
				return true;
			}
		}
		return false;
	}

	public Estado getEstado(String n) {
		Iterator<Estado> it = estados.iterator();
		while (it.hasNext()) {
			Estado e = it.next();
			if (e.getNome().equals(n)) {
				return e;
			}
		}
		return null;
	}

	protected void removeEstado(Estado e) {
		if (e != null) {
			Iterator<Estado> it = estados.iterator();
			while (it.hasNext()) {
				it.next().removeTransicoesParaOEstado(e);
			}

			if (e == q0) {
				q0 = null;
			}
		}
	}

	public void removeEstado(String n) {
		Estado e = getEstado(n);
		removeEstado(e);
	}

	public Estado[] getEstados() {
		Estado[] est = new Estado[estados.size()];
		int i = 0;
		Iterator<Estado> it = estados.iterator();
		while (it.hasNext()) {
			est[i] = it.next();
			i++;
		}
		return est;
	}

	public void addTransicao(String se1, String se2, String simbolo) {
		Estado e1 = getEstado(se1);
		Estado e2 = getEstado(se2);
		if (e1 != null && e2 != null) {
			addTransicao(e1, e2, simbolo);
		}
	}

	protected void addTransicao(Estado e1, Estado e2, String simbolo) {
		if (e1 != null && e2 != null) {
			e1.addTransicao(e2, simbolo);
		}
	}

	public String[] enumerarSentencasDeTamanho(int n) {
		Automato ad = getAutomatoDeterminizado();
		return ad.getSentencasAPartirDoEstado(ad.q0, n);
	}

	private String[] getSentencasAPartirDoEstado(Estado e, int n) {
		String[] arr_sentenca;
		if (n == 0) {
			if (e.ehEstadoFinal()) {
				arr_sentenca = new String[1];
				arr_sentenca[0] = "";
			} else {
				arr_sentenca = new String[0];
			}
		} else {
			String[][] arrDeArrDeSentencas = new String[alfabeto.length][];
			int cont = 0;
			for (int i = 0; i < alfabeto.length; i++) {
				arrDeArrDeSentencas[i] = getSentencasAPartirDoEstado(
						e.getTransicoesDoSimbolo(alfabeto[i])[0]
								.getEstadoDestino(),
						n - 1);
				cont += arrDeArrDeSentencas[i].length;
				for (int j = 0; j < arrDeArrDeSentencas[i].length; j++) {
					arrDeArrDeSentencas[i][j] = alfabeto[i]
							+ arrDeArrDeSentencas[i][j];
				}
			}
			arr_sentenca = new String[cont];
			cont = 0;
			for (int i = 0; i < arrDeArrDeSentencas.length; i++) {
				for (int j = 0; j < arrDeArrDeSentencas[i].length; j++) {
					arr_sentenca[cont] = arrDeArrDeSentencas[i][j];
					cont++;
				}
			}
		}
		return arr_sentenca;
	}

	public Automato clone() {
		Automato automatoClone = new Automato();
		automatoClone.setNome(new String(nome));
		String[] alfabetoClone = new String[alfabeto.length];
		for (int i = 0; i < alfabeto.length; i++) {
			alfabetoClone[i] = new String(alfabeto[i]);
		}
		automatoClone.setAlfabeto(alfabetoClone);

		Iterator<Estado> itEstado = estados.iterator();
		while (itEstado.hasNext()) {
			Estado e = itEstado.next();
			Estado ec = new Estado(new String(e.getNome()), automatoClone);
			automatoClone.estados.add(ec);
			if (e.ehEstadoInicial()) {
				ec.seTorneEstadoInicial();
			}
			if (e.ehEstadoFinal()) {
				ec.setFinal(true);
			}
		}

		itEstado = estados.iterator();
		while (itEstado.hasNext()) {
			Estado e = itEstado.next();
			Transicao[] ts = e.getTransicoes();
			for (int i = 0; i < ts.length; i++) {
				automatoClone.addTransicao(e.getNome(), ts[i]
						.getEstadoDestino().getNome(), ts[i].getSimbolo());
			}

		}

		return automatoClone;
	}

	public boolean ehDeterministico() {
		Iterator<Estado> it = estados.iterator();
		while (it.hasNext()) {
			Estado e = it.next();
			for (int i = 0; i < alfabeto.length; i++) {
				Transicao[] ts = e.getTransicoesDoSimbolo(alfabeto[i]);
				if (ts.length != 1) {
					return false;
				}
			}
		}
		return true;
	}

	public Automato getAutomatoDeterminizado() {

		if (ehDeterministico()) {
			return clone();
		}

		if (q0 == null) {
			return null;
		}

		LinkedList<EstadoAuxiliar[]> tabelaDeEstadosDeterminizados = new LinkedList<EstadoAuxiliar[]>();
		LinkedList<EstadoAuxiliar> estadosDeterminizadosParaTratar = new LinkedList<EstadoAuxiliar>();
		estadosDeterminizadosParaTratar.push(new EstadoAuxiliar(q0));

		while (!estadosDeterminizadosParaTratar.isEmpty()) {
			EstadoAuxiliar[] novaLinha = new EstadoAuxiliar[alfabeto.length + 1];
			novaLinha[0] = estadosDeterminizadosParaTratar.pop();
			tabelaDeEstadosDeterminizados.add(novaLinha);
			tratarLinha(tabelaDeEstadosDeterminizados, novaLinha,
					estadosDeterminizadosParaTratar);
		}

		Automato automatoDeterminizado = new Automato();
		automatoDeterminizado.setNome(nome + " determinizado");
		automatoDeterminizado.setAlfabeto(alfabeto);

		Iterator<EstadoAuxiliar[]> itLinhaDaTabela = tabelaDeEstadosDeterminizados
				.iterator();
		int cont = 0;
		while (itLinhaDaTabela.hasNext()) {
			Estado novoEstadoDeterminizado = new Estado("q" + cont,
					automatoDeterminizado);
			automatoDeterminizado.estados.add(novoEstadoDeterminizado);
			itLinhaDaTabela.next()[0]
					.setEstadoAssociado(novoEstadoDeterminizado);
			cont++;
		}

		itLinhaDaTabela = tabelaDeEstadosDeterminizados.iterator();
		while (itLinhaDaTabela.hasNext()) {
			EstadoAuxiliar[] linha = itLinhaDaTabela.next();
			for (int i = 0; i < alfabeto.length; i++) {
				automatoDeterminizado.addTransicao(
						linha[0].getEstadoAssociado(),
						linha[i + 1].getEstadoAssociado(), alfabeto[i]);
			}

		}

		return automatoDeterminizado;
	}

	private void tratarLinha(
			LinkedList<EstadoAuxiliar[]> tabelaDeEstadosDeterminizados,
			EstadoAuxiliar[] novaLinha,
			LinkedList<EstadoAuxiliar> estadosDeterminizadosParaTratar) {

		for (int i = 1; i < novaLinha.length; i++) {
			novaLinha[i] = new EstadoAuxiliar();
			Estado[] estadosInclusosDoEstadoDeterminizado = novaLinha[0]
					.getEstadosInclusos();
			for (int j = 0; j < estadosInclusosDoEstadoDeterminizado.length; j++) {
				Estado estadoInclusoDoEstadoDeterminizado = estadosInclusosDoEstadoDeterminizado[j];
				Transicao[] transicoesDoEstadoIncluso = estadoInclusoDoEstadoDeterminizado
						.getTransicoesDoSimbolo(alfabeto[i - 1]);
				for (int k = 0; k < transicoesDoEstadoIncluso.length; k++) {
					novaLinha[i].addEstadoIncluso(transicoesDoEstadoIncluso[k]
							.getEstadoDestino());
				}

			}

			EstadoAuxiliar novoEstadoDeterminizado = getEstadoDeterminizadoDaTabela(
					novaLinha[i], tabelaDeEstadosDeterminizados);
			if (novoEstadoDeterminizado != null) {
				novaLinha[i] = novoEstadoDeterminizado;
			} else {
				novoEstadoDeterminizado = getEstadoDeterminizadoDosNaoTratados(
						novaLinha[i], estadosDeterminizadosParaTratar);
				if (novoEstadoDeterminizado != null) {
					novaLinha[i] = novoEstadoDeterminizado;
				} else {
					estadosDeterminizadosParaTratar.push(novaLinha[i]);
				}
			}

		}
	}

	private EstadoAuxiliar getEstadoDeterminizadoDosNaoTratados(
			EstadoAuxiliar estadoAuxiliar,
			LinkedList<EstadoAuxiliar> estadosDeterminizadosParaTratar) {
		Iterator<EstadoAuxiliar> it = estadosDeterminizadosParaTratar
				.iterator();
		while (it.hasNext()) {
			EstadoAuxiliar eAux = it.next();
			if (eAux.ehIgual(estadoAuxiliar)) {
				return eAux;
			}
		}
		return null;
	}

	private void normatizaTransicoesNulas() {
		Estado erro = new Estado("erro", this);
		boolean coloca = false;
		Estado[] es = getEstados();
		for (int i = 0; i < es.length; i++) {
			for (int j = 0; j < alfabeto.length; j++) {
				Transicao[] ts = es[i].getTransicoesDoSimbolo(alfabeto[j]);
				if (ts.length == 0) {
					coloca = true;
					es[i].addTransicao(erro, alfabeto[j]);
				}
			}
		}
		if (coloca) {
			for (int i = 0; i < alfabeto.length; i++) {
				erro.addTransicao(erro, alfabeto[i]);
			}

			estados.add(erro);
		}
	}

	private void reduzirPorEquivalencia() {
		LinkedList<EstadoAuxiliar> classesDeEquivalencia = new LinkedList<EstadoAuxiliar>();
		EstadoAuxiliar estadosFinais = new EstadoAuxiliar();
		EstadoAuxiliar estadosNaoFinais = new EstadoAuxiliar();
		Iterator<Estado> it = estados.iterator();
		while (it.hasNext()) {
			Estado e = it.next();
			if (e.ehEstadoFinal()) {
				estadosFinais.addEstadoIncluso(e);
			} else {
				estadosNaoFinais.addEstadoIncluso(e);
			}
		}

		classesDeEquivalencia.add(estadosFinais);
		classesDeEquivalencia.add(estadosNaoFinais);

		boolean acabouDeDividir = false;

		while (!acabouDeDividir) {

			LinkedList<EstadoAuxiliar> classesDeEquivalenciaComparativa = dividirClassesDeEquivalencia(classesDeEquivalencia);

			if (classesDeEquivalenciaComparativa == classesDeEquivalencia) {
				acabouDeDividir = true;
			} else {
				classesDeEquivalencia = classesDeEquivalenciaComparativa;
			}

		}

		montarAFReduzido(classesDeEquivalencia);

	}

	private LinkedList<EstadoAuxiliar> dividirClassesDeEquivalencia(
			LinkedList<EstadoAuxiliar> classesDeEquivalencia) {
		boolean mudou = false;
		LinkedList<EstadoAuxiliar> novasClassesDeEquivalencia = new LinkedList<EstadoAuxiliar>();
		Iterator<EstadoAuxiliar> itClasses = classesDeEquivalencia.iterator();
		while (itClasses.hasNext()) {
			EstadoAuxiliar classe = itClasses.next();
			if (classe.getEstadosInclusos().length > 1) {
				Estado[] estadosDaClasse = classe.getEstadosInclusos();
				LinkedList<EstadoAuxiliar> classesTemporarias = new LinkedList<EstadoAuxiliar>();
				EstadoAuxiliar novaClasse = new EstadoAuxiliar(
						estadosDaClasse[0]);
				classesTemporarias.add(novaClasse);
				for (int i = 1; i < estadosDaClasse.length; i++) {
					Iterator<EstadoAuxiliar> itClassesTemp = classesTemporarias
							.iterator();
					EstadoAuxiliar classeEquivalente = null;
					while (itClassesTemp.hasNext() && classeEquivalente == null) {
						EstadoAuxiliar classeTemporaria = itClassesTemp.next();
						if (ehEquivalente(estadosDaClasse[i],
								classeTemporaria.getEstadosInclusos()[0],
								classesDeEquivalencia)) {
							classeEquivalente = classeTemporaria;
						}
					}
					if (classeEquivalente != null) {
						classeEquivalente.addEstadoIncluso(estadosDaClasse[i]);
					} else {
						classesTemporarias.add(new EstadoAuxiliar(
								estadosDaClasse[i]));
						mudou = true;
					}
				}

				Iterator<EstadoAuxiliar> itClassesTemp = classesTemporarias
						.iterator();
				while (itClassesTemp.hasNext()) {
					novasClassesDeEquivalencia.add(itClassesTemp.next());
				}
			} else {
				novasClassesDeEquivalencia.add(classe);
			}
		}

		if (mudou) {
			return novasClassesDeEquivalencia;
		} else {
			return classesDeEquivalencia;
		}
	}

	private boolean ehEquivalente(Estado e1, Estado e2,
			LinkedList<EstadoAuxiliar> classesDeEquivalencia) {
		for (int i = 0; i < alfabeto.length; i++) {
			EstadoAuxiliar ex1 = retornaEstadoAuxQuePossuiEstado(
					classesDeEquivalencia,
					e1.getTransicoesDoSimbolo(alfabeto[i])[0]
							.getEstadoDestino());
			EstadoAuxiliar ex2 = retornaEstadoAuxQuePossuiEstado(
					classesDeEquivalencia,
					e2.getTransicoesDoSimbolo(alfabeto[i])[0]
							.getEstadoDestino());
			if (ex1 != ex2) {
				return false;
			}
		}
		return true;
	}

	private EstadoAuxiliar retornaEstadoAuxQuePossuiEstado(
			LinkedList<EstadoAuxiliar> classesDeEquivalencia, Estado e) {
		Iterator<EstadoAuxiliar> it = classesDeEquivalencia.iterator();
		while (it.hasNext()) {
			EstadoAuxiliar x = it.next();
			if (x.possui(e)) {
				return x;
			}
		}
		return null;
	}

	private void montarAFReduzido(
			LinkedList<EstadoAuxiliar> classesDeEquivalencia) {
		LinkedList<Estado> estadosReduzidos = new LinkedList<Estado>();
		Iterator<EstadoAuxiliar> it = classesDeEquivalencia.iterator();
		int cont = 0;
		while (it.hasNext()) {
			EstadoAuxiliar er = it.next();
			Estado estadoReduzido = new Estado("q" + cont, this);
			er.setEstadoAssociado(estadoReduzido);
			er.setEstadoAssociadoParaFinalCasoAlgumEstadoInclusoSejaTambem();
			estadosReduzidos.add(estadoReduzido);
			cont++;
		}
		it = classesDeEquivalencia.iterator();
		while (it.hasNext()) {
			EstadoAuxiliar ex = it.next();
			Estado estadoReduzido = ex.getEstadoAssociado();
			Estado umEstadoIncluso = ex.getEstadosInclusos()[0];
			for (int i = 0; i < alfabeto.length; i++) {
				EstadoAuxiliar transicaoAux = retornaEstadoAuxQuePossuiEstado(
						classesDeEquivalencia,
						umEstadoIncluso.getTransicoesDoSimbolo(alfabeto[i])[0]
								.getEstadoDestino());
				addTransicao(estadoReduzido, transicaoAux.getEstadoAssociado(),
						alfabeto[i]);
			}
		}

		EstadoAuxiliar ex = retornaEstadoAuxQuePossuiEstado(
				classesDeEquivalencia, q0);
		if (ex != null) {
			ex.getEstadoAssociado().seTorneEstadoInicial();
		}

		estados = estadosReduzidos;

	}

	public void eliminarEstadosMortos() {
		LinkedList<Estado> listaDeEstadosNaoFinais = new LinkedList<Estado>();
		LinkedList<Estado> listaDeEstadosFinaisEAlcancaveis = new LinkedList<Estado>();

		// Colocar estadosAF finais e não finais em suas devidas listas
		Iterator<Estado> it = estados.iterator();
		while (it.hasNext()) {
			Estado e = it.next();
			if (e.ehEstadoFinal()) {
				listaDeEstadosFinaisEAlcancaveis.add(e);
			} else {
				listaDeEstadosNaoFinais.add(e);
			}
		}

		// Se o estado alcança um estado final ele muda para a lista de
		// estadosAF finais ou alcançados
		it = listaDeEstadosNaoFinais.iterator();
		while (it.hasNext()) {
			Estado e = it.next();
			if (alcancaEstadoFinal(e)) {
				listaDeEstadosFinaisEAlcancaveis.add(e);
			}
		}
		it = listaDeEstadosFinaisEAlcancaveis.iterator();
		while (it.hasNext()) {
			listaDeEstadosNaoFinais.remove(it.next());
		}

		// Remove os estadosAF que estão na lista de estadosAF não finais do
		// autômato
		it = listaDeEstadosNaoFinais.iterator();
		while (it.hasNext()) {
			Estado e = it.next();
			removeEstado(e);
		}

	}

	private boolean alcancaEstadoFinal(Estado e) {
		LinkedList<Estado> pilhaDeEstados = new LinkedList<Estado>();
		LinkedList<Estado> listaDeEstadosJaVisitados = new LinkedList<Estado>();
		pilhaDeEstados.push(e);
		while (!pilhaDeEstados.isEmpty()) {
			Estado estadoPop = pilhaDeEstados.pop();
			if (estadoPop.ehEstadoFinal()) {
				return true;
			}
			listaDeEstadosJaVisitados.push(estadoPop);
			Transicao[] transicoesDoEstadoPop = estadoPop.getTransicoes();
			for (int i = 0; i < transicoesDoEstadoPop.length; i++) {
				Estado estado = transicoesDoEstadoPop[i].getEstadoDestino();
				if (!pilhaDeEstados.contains(estado)
						&& !listaDeEstadosJaVisitados.contains(estado)) {
					pilhaDeEstados.push(estado);
				}
			}
		}
		return false;
	}

	public void eliminarEstadosInalcancaveis() {
		LinkedList<Estado> pilhaParaVisitar = new LinkedList<Estado>();
		LinkedList<Estado> listaEstadosAlcancaveis = new LinkedList<Estado>();
		pilhaParaVisitar.push(q0);
		while (!pilhaParaVisitar.isEmpty()) {
			Estado e = pilhaParaVisitar.pop();
			listaEstadosAlcancaveis.add(e);
			Transicao[] ts = e.getTransicoes();
			for (int i = 0; i < ts.length; i++) {
				Estado et = ts[i].getEstadoDestino();
				if (!pilhaParaVisitar.contains(et)
						&& !listaEstadosAlcancaveis.contains(et)) {
					pilhaParaVisitar.push(et);
				}
			}
		}
		estados = new LinkedList<Estado>();
		Iterator<Estado> it = listaEstadosAlcancaveis.iterator();
		while (it.hasNext()) {
			estados.add(it.next());
		}
	}

	public ExpressaoRegular getExpressaoRegular() {

		Automato automatoAuxiliar = getAutomatoMinimizado();
		automatoAuxiliar.eliminarEstadosInalcancaveis();
		automatoAuxiliar.eliminarEstadosMortos();

		Estado inicio = new Estado("In", automatoAuxiliar);
		automatoAuxiliar.estados.addLast(inicio);
		inicio.addTransicao(automatoAuxiliar.q0, "");
		automatoAuxiliar.q0 = inicio;

		Estado fynal = new Estado("Fi", automatoAuxiliar);
		Iterator<Estado> it = automatoAuxiliar.estados.iterator();
		while (it.hasNext()) {
			Estado e = it.next();
			if (e.ehEstadoFinal()) {
				e.setFinal(false);
				e.addTransicao(fynal, "");
			}
		}
		fynal.setFinal(true);
		automatoAuxiliar.estados.addLast(fynal);

		automatoAuxiliar.removeEstadosInternos();
		return automatoAuxiliar.crieExpressaoRegular();
	}

	private ExpressaoRegular crieExpressaoRegular() {
		String exp = "";
		Estado in = getEstadoInicial();
		Transicao[] ts = in.getTransicoes();
		for (int i = 0; i < ts.length; i++) {
			exp += ts[i].getSimbolo();
			if (i != ts.length - 1) {
				exp += " | ";
			}
		}

		ExpressaoRegular expressao = new ExpressaoRegular();
		expressao.setDescricao(exp);
		return expressao;
	}

	private void removeEstadosInternos() {
		int ne = estados.size() - 2;
		for (int i = 0; i < ne; i++) {

			Estado e = estados.getFirst();
			Transicao[] transicoesQueChegam = e.getTransicoesQueChegam();
			Transicao[] transicoesQuePartem = e.getTransicoesParaOutroEstado();
			Transicao[] transicoesParaSi = e.getTransicoesParaSiMesmo();

			String transicaoEstrela = "";
			if (transicoesParaSi.length > 0) {
				transicaoEstrela += "(";
			}
			for (int j = 0; j < transicoesParaSi.length; j++) {
				transicaoEstrela += transicoesParaSi[j].getSimbolo();
				if (j != transicoesParaSi.length - 1) {
					transicaoEstrela += "|";
				}
			}
			if (transicoesParaSi.length > 0) {
				transicaoEstrela += ")*";
			}

			for (int j = 0; j < transicoesQueChegam.length; j++) {
				for (int k = 0; k < transicoesQuePartem.length; k++) {
					String simbolo = transicoesQueChegam[j].getSimbolo()
							+ transicaoEstrela
							+ transicoesQuePartem[k].getSimbolo();
					addTransicao(transicoesQueChegam[j].getEstadoOrigem(),
							transicoesQuePartem[k].getEstadoDestino(), simbolo);
				}
			}

			removeEstado(e);
		}

	}

	public Automato getComplemento() {
		Automato complemento = this.getAutomatoMinimizado();
		complemento.normatizaTransicoesNulas();
		Estado[] estado = complemento.getEstados();
		for (int i = 0; i < estado.length; i++) {
			Estado e = estado[i];
			e.setFinal(!e.ehEstadoFinal());
		}
		return complemento;
	}

	public String getGramaticaRegular() {
		Automato automatoAuxiliar = this.getAutomatoMinimizado();
		Estado[] est = automatoAuxiliar.getEstados();
		String retorno = "";
		String sComEpslon = "";
		for (int i = 0; i < est.length; i++) {
			// faz parte X->
			if (est[i].ehEstadoInicial()) {
				if (est[i].ehEstadoFinal()) {
					retorno += "[" + est[i].getNome() + "] -> ";
					sComEpslon += "[" + est[i].getNome()
							+ "](inicial) -> epslon|";
				} else {
					retorno += "[" + est[i].getNome() + "](inicial) -> ";
				}
			} else {
				retorno += "[" + est[i].getNome() + "] -> ";
			}
			// faz parte -> aB
			Transicao[] t = est[i].getTransicoes();
			for (int j = 0; j < t.length; j++) {
				if (est[i].ehEstadoInicial()) {
					if (est[i].ehEstadoFinal()) {
						retorno += t[j].getSimbolo() + "["
								+ t[j].getEstadoDestino().getNome() + "] | ";
						sComEpslon += t[j].getSimbolo() + "["
								+ t[j].getEstadoDestino().getNome() + "] | ";
						if (t[j].getEstadoDestino().ehEstadoFinal()) {
							retorno += t[j].getSimbolo() + " | ";
							sComEpslon += t[j].getSimbolo() + " | ";
						}
					} else {
						retorno += t[j].getSimbolo() + "["
								+ t[j].getEstadoDestino().getNome() + "] | ";
						if (t[j].getEstadoDestino().ehEstadoFinal()) {
							retorno += t[j].getSimbolo() + " | ";
						}
					}
				} else {
					retorno += t[j].getSimbolo() + "["
							+ t[j].getEstadoDestino().getNome() + "] | ";
					if (t[j].getEstadoDestino().ehEstadoFinal()) {
						retorno += t[j].getSimbolo() + " | ";
					}
				}
			}
			int tamanhoRetorno = retorno.length();
			int tamanhoScomEpslon = sComEpslon.length();
			if (retorno.charAt(tamanhoRetorno - 2) == '|') {
				retorno = retorno.substring(0, retorno.length() - 2);
				if (tamanhoScomEpslon > 0) {
					sComEpslon = sComEpslon.substring(0,
							sComEpslon.length() - 2);
				}
			}
			retorno += "\n";
		}
		if (!"".equals(sComEpslon)) {
			sComEpslon += "\n";
		}
		return sComEpslon + retorno;
	}

	public LinkedList<String> getSentencasPorTamanho(int t, Estado estado) {
		LinkedList<String> resultado = new LinkedList<String>();
		LinkedList<String> sentencas;
		Transicao[] transicoes;
		String simbolo;
		transicoes = estado.getTransicoes();

		if (t == 1) {
			for (Transicao transicao : transicoes) {
				if (transicao.getEstadoDestino().ehEstadoFinal()) {
					simbolo = transicao.getSimbolo();
					resultado.add(simbolo);
				}
			}
		} else {
			for (Transicao transicao : transicoes) {
				sentencas = getSentencasPorTamanho(t - 1,
						transicao.getEstadoDestino());
				for (String sentencaAtual : sentencas) {
					resultado.add(transicao.getSimbolo() + sentencaAtual);
				}
			}
		}
		return resultado;
	}

	public Automato getUniao(Automato auto2) {
		if (auto2 == null) {
			return null;
		}

		Automato uniao = new Automato();
		Estado novoInicial = new Estado("novoInicial", uniao);
		uniao.addEstado(novoInicial);
		uniao.setEstadoInicial(novoInicial.getNome());
		Automato a1 = this.getAutomatoMinimizado();
		Automato a2 = auto2.getAutomatoMinimizado();

		Estado inicialA1 = a1.getEstadoInicial();
		Estado inicialA2 = a2.getEstadoInicial();
		if (inicialA1.ehEstadoFinal() || inicialA2.ehEstadoFinal()) {
			novoInicial.setFinal(true);
		}
		// setando alfabeto
		String[] alf = uneAlfabeto(a1, a2);
		uniao.setAlfabeto(alf);
		// adicionando as transições do estado inicial do automato 1 para o novo
		// estado inicial
		Transicao[] t1 = inicialA1.getTransicoes();
		for (int i = 0; i < t1.length; i++) {
			Transicao t = t1[i];
			Estado dest = t.getEstadoDestino();
			uniao.addEstado("a1" + dest.getNome());
			Estado novoDest = uniao.getEstado("a1" + dest.getNome());
			novoInicial.addTransicao(novoDest, t.getSimbolo());
		}

		// adicionando as transições do estado inicial do automato 2 para o novo
		// estado inicial
		Transicao[] t2 = inicialA2.getTransicoes();
		for (int i = 0; i < t2.length; i++) {
			Transicao t = t2[i];
			Estado dest = t.getEstadoDestino();
			uniao.addEstado("a2" + dest.getNome());
			Estado novoDest = uniao.getEstado("a2" + dest.getNome());
			novoInicial.addTransicao(novoDest, t.getSimbolo());
		}
		// adicionando os outros estadosAF com nomes diferentes
		Estado[] est = a1.getEstados();
		for (int i = 0; i < est.length; i++) {
			uniao.addEstado("a1" + est[i].getNome());
			if (est[i].ehEstadoFinal()) {
				uniao.getEstado("a1" + est[i].getNome()).setFinal(true);
			}
		}
		est = a2.getEstados();
		for (int i = 0; i < est.length; i++) {
			uniao.addEstado("a2" + est[i].getNome());
			if (est[i].ehEstadoFinal()) {
				uniao.getEstado("a2" + est[i].getNome()).setFinal(true);
			}
		}
		// Adicionando Transições
		est = a1.getEstados();
		for (int i = 0; i < est.length; i++) {
			Estado aux = uniao.getEstado("a1" + est[i]);
			Transicao[] t = est[i].getTransicoes();
			for (int j = 0; j < t.length; j++) {
				aux.addTransicao(
						uniao.getEstado("a1"
								+ t[j].getEstadoDestino().getNome()),
						t[j].getSimbolo());
			}
		}
		est = a2.getEstados();
		for (int i = 0; i < est.length; i++) {
			Estado aux = uniao.getEstado("a2" + est[i]);
			Transicao[] t = est[i].getTransicoes();
			for (int j = 0; j < t.length; j++) {
				aux.addTransicao(
						uniao.getEstado("a2"
								+ t[j].getEstadoDestino().getNome()),
						t[j].getSimbolo());
			}
		}

		return uniao.getAutomatoMinimizado();
	}

	public String[] uneAlfabeto(Automato a1, Automato a2) {
		HashSet<String> a = new HashSet<String>();
		a.addAll(Arrays.asList(a1.getAlfabeto()));
		a.addAll(Arrays.asList(a2.getAlfabeto()));
		String[] retorno = new String[a.size()];
		a.toArray(retorno);
		return retorno;
	}

	public Automato getInterseccao(Automato auto2) {
		Automato a1Complementado = this.getComplemento();
		Automato a2Complementado = auto2.getComplemento();
		Automato uniaoDeComplementos = a1Complementado
				.getUniao(a2Complementado);
		Automato aux = uniaoDeComplementos.getComplemento();
		aux.eliminarEstadosMortos();
		return aux;
	}

	public boolean estaContido(Automato af2) {
		Automato af2Complemento = af2.getComplemento();
		// novo
		Automato afIntersessao;
		afIntersessao = this.getAutomatoMinimizado().getInterseccao(
				af2Complemento);
		if (afIntersessao.getEstados().length == 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean ehIgual(Automato af2) {
		if (this.estaContido(af2) && af2.estaContido(this)) {
			return true;
		} else {
			return false;
		}

	}

	public Automato getReverso() {
		Automato af = this.getAutomatoMinimizado();
		Automato reverso = new Automato();
		reverso.setNome("af-reverso");
		reverso.setAlfabeto(af.getAlfabeto());
		reverso.addEstado("novoInicial");
		reverso.setEstadoInicial("novoInicial");
		Estado[] estadosAF = af.getEstados();
		Transicao[] transicoes;
		// add estados
		for (Estado e1 : estadosAF) {
			reverso.addEstado(e1.getNome());
		}
		// inverte transicoes
		for (Estado e : estadosAF) {
			transicoes = e.getTransicoes();
			for (Transicao t : transicoes) {
				reverso.addTransicao(t.getEstadoDestino().getNome(), t
						.getEstadoOrigem().getNome(), t.getSimbolo());
				if (t.getEstadoDestino().ehEstadoFinal()) {
					reverso.addTransicao("novoInicial", t.getEstadoOrigem()
							.getNome(), t.getSimbolo());
				}

			}
		}

		reverso.setEstadoFinal(af.getEstadoInicial().getNome());
		return reverso.getAutomatoMinimizado();
	}
}
