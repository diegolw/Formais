package manipulador.modelo;

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

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setAlfabeto(String[] alfabeto) {
		boolean naoVazio = true;
		for (int i = 0; i < alfabeto.length; i++) {
			String s = new String(alfabeto[i]);
			s = s.trim();
			for (int j = 0; j < s.length(); j++) {
				if (s.charAt(j) == ' ') {
					// Se o caracter é vazio
					naoVazio = false;
				}
			}
		}
		if (naoVazio) {
			this.alfabeto = alfabeto;
		}
	}

	public Estado getEstadoInicial() {
		return q0;
	}

	protected void setEstadoInicial(Estado estado) {
		if (estado.getPai() == this) {
			q0 = estado;
		}
	}

	public void setEstadoInicial(String nome) {
		Estado estado = getEstado(nome);
		if (estado != null) {
			setEstadoInicial(estado);
		}
	}

	// Retorna estado determinizado da tabela
	private EstadoAuxiliar getEstadoDeterminizado(EstadoAuxiliar novaLinha,
			LinkedList<EstadoAuxiliar[]> estadosDeterminizados) {
		Iterator<EstadoAuxiliar[]> it = estadosDeterminizados.iterator();
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

		// Retorna automato determinizado
		Automato automatoMinimo = getDeterminizado();
		if (automatoMinimo.ehCompletoComTodosEstadosFinais()) {
			Automato retorno = new Automato();
			retorno.setAlfabeto(automatoMinimo.getAlfabeto());
			retorno.addEstado("q0");
			Estado estado = retorno.getEstado("q0");
			estado.setFinal(true);
			retorno.setEstadoInicial("q0");
			String[] alf = retorno.getAlfabeto();
			for (int i = 0; i < alf.length; i++) {
				estado.addTransicao(estado, alf[i]);
			}
			return retorno;
		}

		automatoMinimo.eliminarEstadosInalcancaveis();
		automatoMinimo.eliminarEstadosMortos();
		automatoMinimo.normalizarTransicoesNulas();
		automatoMinimo.reduzirViaEquivalencia();
		automatoMinimo.eliminarEstadosMortos();
		return automatoMinimo;
	}

	// Retorna true se é completo e todos estados são finais
	public boolean ehCompletoComTodosEstadosFinais() {
		Estado[] estado = this.getEstados();
		for (int i = 0; i < estado.length; i++) {
			if (!estado[i].ehFinal()) {
				return false;
			}
			Transicao[] t = estado[i].getTransicoes();
			if (t.length != this.getAlfabeto().length) {
				return false;
			}
		}

		return true;
	}

	public void setEstadoFinal(String nome) {
		Estado estado = getEstado(nome);
		if (estado != null) {
			setEstadoFinal(estado);
		}
	}

	protected void setEstadoFinal(Estado estado) {
		estado.setFinal(true);
	}

	public void addEstado(String nome) {
		if (!existeEstado(nome)) {
			Estado estado = new Estado(nome, this);
			estados.add(estado);
		}
	}

	public void addEstado(Estado estado) {
		if (!existeEstado(estado.getNome())) {
			estados.add(estado);
		} else {
			String nomeEstado = estado.getNome();
			while (existeEstado(nomeEstado)) {
				nomeEstado += "\'";
			}
			estado.setNome(nomeEstado);
			estados.add(estado);
		}
	}

	public boolean existeEstado(String nome) {
		Iterator<Estado> iterador = estados.iterator();
		while (iterador.hasNext()) {
			if (iterador.next().getNome().equals(nome)) {
				return true;
			}
		}
		return false;
	}

	public Estado getEstado(String nome) {
		Iterator<Estado> iterador = estados.iterator();
		while (iterador.hasNext()) {
			Estado estado = iterador.next();
			if (estado.getNome().equals(nome)) {
				return estado;
			}
		}
		return null;
	}

	protected void removeEstado(Estado estado) {
		if (estado != null) {
			Iterator<Estado> iterador = estados.iterator();
			while (iterador.hasNext()) {
				iterador.next().removeTransicoesParaOEstado(estado);
			}

			if (estado == q0) {
				q0 = null;
			}
		}
	}

	public void removeEstado(String nome) {
		Estado estado = getEstado(nome);
		removeEstado(estado);
	}

	public Estado[] getEstados() {
		Estado[] retornoEstados = new Estado[estados.size()];
		int i = 0;
		Iterator<Estado> iterador = estados.iterator();
		while (iterador.hasNext()) {
			retornoEstados[i] = iterador.next();
			i++;
		}
		return retornoEstados;
	}

	public void addTransicao(String origem, String destino, String simbolo) {
		Estado estadoOrigem = getEstado(origem);
		Estado estadoDestino = getEstado(destino);
		if (estadoOrigem != null && estadoDestino != null) {
			addTransicao(estadoOrigem, estadoDestino, simbolo);
		}
	}

	protected void addTransicao(Estado estadoOrigem, Estado estadoDestino,
			String simbolo) {
		if (estadoOrigem != null && estadoDestino != null) {
			estadoOrigem.addTransicao(estadoDestino, simbolo);
		}
	}

	// Enumerar sentenças de tamanho n
	public String[] enumerarSentencas(int tamanho) {
		Automato automatoDeterminizado = getDeterminizado();
		return automatoDeterminizado.getSentencasAPartirDo(
				automatoDeterminizado.q0, tamanho);
	}

	// Pega sentenças a partir do estado e
	private String[] getSentencasAPartirDo(Estado estado, int tamanho) {
		String[] sentencas;
		if (tamanho == 0) {
			if (estado.ehFinal()) {
				sentencas = new String[1];
				sentencas[0] = "";
			} else {
				sentencas = new String[0];
			}
		} else {
			// Se o tamnho for maior que 0
			String[][] sentencasDeSentencas = new String[alfabeto.length][];
			int cont = 0;
			for (int i = 0; i < alfabeto.length; i++) {
				sentencasDeSentencas[i] = getSentencasAPartirDo(
						estado.getTransicoesDoSimbolo(alfabeto[i])[0]
								.getDestino(),
						tamanho - 1);
				cont += sentencasDeSentencas[i].length;
				for (int j = 0; j < sentencasDeSentencas[i].length; j++) {
					sentencasDeSentencas[i][j] = alfabeto[i]
							+ sentencasDeSentencas[i][j];
				}
			}
			sentencas = new String[cont];
			cont = 0;
			for (int i = 0; i < sentencasDeSentencas.length; i++) {
				for (int j = 0; j < sentencasDeSentencas[i].length; j++) {
					sentencas[cont] = sentencasDeSentencas[i][j];
					cont++;
				}
			}
		}
		return sentencas;
	}

	public Automato clone() {
		Automato clone = new Automato();
		clone.setNome(new String(nome));
		String[] alfabetoClone = new String[alfabeto.length];
		for (int i = 0; i < alfabeto.length; i++) {
			alfabetoClone[i] = new String(alfabeto[i]);
		}
		clone.setAlfabeto(alfabetoClone);

		Iterator<Estado> iterador = estados.iterator();
		while (iterador.hasNext()) {
			Estado estado = iterador.next();
			Estado estadoClone = new Estado(new String(estado.getNome()), clone);
			clone.estados.add(estadoClone);
			if (estado.ehInicial()) {
				estadoClone.setInicial();
			}
			if (estado.ehFinal()) {
				estadoClone.setFinal(true);
			}
		}

		iterador = estados.iterator();
		while (iterador.hasNext()) {
			Estado estado = iterador.next();
			Transicao[] transicao = estado.getTransicoes();
			for (int i = 0; i < transicao.length; i++) {
				clone.addTransicao(estado.getNome(), transicao[i].getDestino()
						.getNome(), transicao[i].getSimbolo());
			}

		}
		return clone;
	}

	public boolean ehDeterministico() {
		Iterator<Estado> iterador = estados.iterator();
		while (iterador.hasNext()) {
			Estado e = iterador.next();
			for (int i = 0; i < alfabeto.length; i++) {
				Transicao[] ts = e.getTransicoesDoSimbolo(alfabeto[i]);
				if (ts.length != 1) {
					return false;
				}
			}
		}
		return true;
	}

	public Automato getDeterminizado() {
		if (ehDeterministico()) {
			return clone();
		}

		if (q0 == null) {
			return null;
		}

		// Tabela de estados determinizados
		LinkedList<EstadoAuxiliar[]> estadosDeterminizados = new LinkedList<EstadoAuxiliar[]>();

		// Tabela de estados determinizados auxiliar que devemos tratar
		LinkedList<EstadoAuxiliar> estadosDeterminizadosAuxiliar = new LinkedList<EstadoAuxiliar>();
		estadosDeterminizadosAuxiliar.push(new EstadoAuxiliar(q0));

		while (!estadosDeterminizadosAuxiliar.isEmpty()) {
			EstadoAuxiliar[] novaLinha = new EstadoAuxiliar[alfabeto.length + 1];
			novaLinha[0] = estadosDeterminizadosAuxiliar.pop();
			estadosDeterminizados.add(novaLinha);
			tratarLinha(estadosDeterminizados, novaLinha,
					estadosDeterminizadosAuxiliar);
		}

		Automato determinizado = new Automato();
		determinizado.setNome(nome + " determinizado");
		determinizado.setAlfabeto(alfabeto);

		Iterator<EstadoAuxiliar[]> linhaTabela = estadosDeterminizados
				.iterator();
		int cont = 0;
		while (linhaTabela.hasNext()) {
			Estado novoDeterminizado = new Estado("q" + cont, determinizado);
			determinizado.estados.add(novoDeterminizado);
			linhaTabela.next()[0].setEstadoAssociado(novoDeterminizado);
			cont++;
		}

		linhaTabela = estadosDeterminizados.iterator();
		while (linhaTabela.hasNext()) {
			EstadoAuxiliar[] linha = linhaTabela.next();
			for (int i = 0; i < alfabeto.length; i++) {
				determinizado.addTransicao(linha[0].getEstadoAssociado(),
						linha[i + 1].getEstadoAssociado(), alfabeto[i]);
			}
		}

		return determinizado;
	}

	private void tratarLinha(
			LinkedList<EstadoAuxiliar[]> estadosDeterminizados,
			EstadoAuxiliar[] novaLinha,
			LinkedList<EstadoAuxiliar> determinizadosParaTratar) {

		for (int i = 1; i < novaLinha.length; i++) {
			novaLinha[i] = new EstadoAuxiliar();
			// Estados que foram inclusos pelo estado determinizado
			Estado[] inclusosDoEstadoDeterminizado = novaLinha[0]
					.getEstadosInclusos();
			for (int j = 0; j < inclusosDoEstadoDeterminizado.length; j++) {
				Estado estadoInclusoDoEstadoDeterminizado = inclusosDoEstadoDeterminizado[j];
				// Transições inclusas pelo estado incluso
				Transicao[] transicoesDoEstadoIncluso = estadoInclusoDoEstadoDeterminizado
						.getTransicoesDoSimbolo(alfabeto[i - 1]);
				for (int k = 0; k < transicoesDoEstadoIncluso.length; k++) {
					novaLinha[i].addEstadoIncluso(transicoesDoEstadoIncluso[k]
							.getDestino());
				}
			}

			// Novo estado determinizado
			EstadoAuxiliar determinizado = getEstadoDeterminizado(novaLinha[i],
					estadosDeterminizados);
			if (determinizado != null) {
				novaLinha[i] = determinizado;
			} else {
				determinizado = getEstadoDeterminizadoNaoTratados(novaLinha[i],
						determinizadosParaTratar);
				if (determinizado != null) {
					novaLinha[i] = determinizado;
				} else {
					determinizadosParaTratar.push(novaLinha[i]);
				}
			}
		}
	}

	// Obtém estado determinizado dos estados não tratados
	private EstadoAuxiliar getEstadoDeterminizadoNaoTratados(
			EstadoAuxiliar auxiliar,
			LinkedList<EstadoAuxiliar> determinizadosParaTratar) {
		Iterator<EstadoAuxiliar> iterador = determinizadosParaTratar.iterator();
		while (iterador.hasNext()) {
			EstadoAuxiliar eAux = iterador.next();
			if (eAux.ehIgual(auxiliar)) {
				return eAux;
			}
		}
		return null;
	}

	// Normalizar transicoes nulas
	private void normalizarTransicoesNulas() {
		Estado erro = new Estado("erro", this);
		boolean inserir = false;
		Estado[] estadosAtuais = getEstados();
		for (int i = 0; i < estadosAtuais.length; i++) {
			for (int j = 0; j < alfabeto.length; j++) {
				Transicao[] transicoes = estadosAtuais[i]
						.getTransicoesDoSimbolo(alfabeto[j]);
				if (transicoes.length == 0) {
					inserir = true;
					estadosAtuais[i].addTransicao(erro, alfabeto[j]);
				}
			}
		}
		if (inserir) {
			for (int i = 0; i < alfabeto.length; i++) {
				erro.addTransicao(erro, alfabeto[i]);
			}
			estados.add(erro);
		}
	}

	// Reduzir por equivalencia
	private void reduzirViaEquivalencia() {
		// Classes de equivalencia
		LinkedList<EstadoAuxiliar> equivalencias = new LinkedList<EstadoAuxiliar>();
		EstadoAuxiliar finais = new EstadoAuxiliar();
		EstadoAuxiliar naoFinais = new EstadoAuxiliar();
		Iterator<Estado> iterador = estados.iterator();
		while (iterador.hasNext()) {
			Estado estado = iterador.next();
			if (estado.ehFinal()) {
				finais.addEstadoIncluso(estado);
			} else {
				naoFinais.addEstadoIncluso(estado);
			}
		}
		equivalencias.add(finais);
		equivalencias.add(naoFinais);

		boolean dividiu = false;
		while (!dividiu) {
			// Equivalencia comparativa
			LinkedList<EstadoAuxiliar> equivalenciaComparativa = dividirEquivalencias(equivalencias);
			if (equivalenciaComparativa == equivalencias) {
				dividiu = true;
			} else {
				equivalencias = equivalenciaComparativa;
			}
		}
		montarAFReduzido(equivalencias);
	}

	// Divide classes de equivalência
	private LinkedList<EstadoAuxiliar> dividirEquivalencias(
			LinkedList<EstadoAuxiliar> equivalencias) {
		boolean alterado = false;
		LinkedList<EstadoAuxiliar> novasEquivalencias = new LinkedList<EstadoAuxiliar>();
		Iterator<EstadoAuxiliar> iterador = equivalencias.iterator();
		while (iterador.hasNext()) {
			EstadoAuxiliar equivalencia = iterador.next();
			if (equivalencia.getEstadosInclusos().length > 1) {
				Estado[] estadosDaClasse = equivalencia.getEstadosInclusos();
				LinkedList<EstadoAuxiliar> temp = new LinkedList<EstadoAuxiliar>();
				EstadoAuxiliar novaEquivalencia = new EstadoAuxiliar(
						estadosDaClasse[0]);
				temp.add(novaEquivalencia);
				for (int i = 1; i < estadosDaClasse.length; i++) {
					Iterator<EstadoAuxiliar> iteradorTemp = temp.iterator();
					EstadoAuxiliar equivalente = null;
					while (iteradorTemp.hasNext() && equivalente == null) {
						EstadoAuxiliar classeTemp = iteradorTemp.next();
						if (ehEquivalente(estadosDaClasse[i],
								classeTemp.getEstadosInclusos()[0],
								equivalencias)) {
							equivalente = classeTemp;
						}
					}
					if (equivalente != null) {
						equivalente.addEstadoIncluso(estadosDaClasse[i]);
					} else {
						temp.add(new EstadoAuxiliar(estadosDaClasse[i]));
						alterado = true;
					}
				}

				Iterator<EstadoAuxiliar> iteradorTemp = temp.iterator();
				while (iteradorTemp.hasNext()) {
					novasEquivalencias.add(iteradorTemp.next());
				}
			} else {
				novasEquivalencias.add(equivalencia);
			}
		}

		if (alterado) {
			return novasEquivalencias;
		} else {
			return equivalencias;
		}
	}

	private boolean ehEquivalente(Estado estado1, Estado estado2,
			LinkedList<EstadoAuxiliar> equivalencia) {
		for (int i = 0; i < alfabeto.length; i++) {
			EstadoAuxiliar aux1 = possuiAuxiliarQuePossuiEstado(equivalencia,
					estado1.getTransicoesDoSimbolo(alfabeto[i])[0]
							.getDestino());
			EstadoAuxiliar aux2 = possuiAuxiliarQuePossuiEstado(equivalencia,
					estado2.getTransicoesDoSimbolo(alfabeto[i])[0]
							.getDestino());
			if (aux1 != aux2) {
				return false;
			}
		}
		return true;
	}

	private EstadoAuxiliar possuiAuxiliarQuePossuiEstado(
			LinkedList<EstadoAuxiliar> equivalencia, Estado estado) {
		Iterator<EstadoAuxiliar> iterador = equivalencia.iterator();
		while (iterador.hasNext()) {
			EstadoAuxiliar auxiliar = iterador.next();
			if (auxiliar.possui(estado)) {
				return auxiliar;
			}
		}
		return null;
	}

	private void montarAFReduzido(LinkedList<EstadoAuxiliar> equivalentes) {
		LinkedList<Estado> reduzidos = new LinkedList<Estado>();
		Iterator<EstadoAuxiliar> iterador = equivalentes.iterator();
		int cont = 0;
		while (iterador.hasNext()) {
			EstadoAuxiliar auxiliar = iterador.next();
			Estado estadoReduzido = new Estado("q" + cont, this);
			auxiliar.setEstadoAssociado(estadoReduzido);
			auxiliar.setAssociadoComoFinalSeInclusoFor();
			reduzidos.add(estadoReduzido);
			cont++;
		}
		iterador = equivalentes.iterator();
		while (iterador.hasNext()) {
			EstadoAuxiliar auxiliar = iterador.next();
			Estado reduzido = auxiliar.getEstadoAssociado();
			Estado incluso = auxiliar.getEstadosInclusos()[0];
			for (int i = 0; i < alfabeto.length; i++) {
				EstadoAuxiliar transAuxiliar = possuiAuxiliarQuePossuiEstado(
						equivalentes,
						incluso.getTransicoesDoSimbolo(alfabeto[i])[0]
								.getDestino());
				addTransicao(reduzido, transAuxiliar.getEstadoAssociado(),
						alfabeto[i]);
			}
		}
		EstadoAuxiliar auxiliar = possuiAuxiliarQuePossuiEstado(equivalentes,
				q0);
		if (auxiliar != null) {
			auxiliar.getEstadoAssociado().setInicial();
		}
		estados = reduzidos;
	}

	public void eliminarEstadosMortos() {
		LinkedList<Estado> estadosNaoFinais = new LinkedList<Estado>();
		LinkedList<Estado> finaisEAlcancaveis = new LinkedList<Estado>();

		// Colocar estados finais e não finais em suas devidas listas
		Iterator<Estado> iterador = estados.iterator();
		while (iterador.hasNext()) {
			Estado e = iterador.next();
			if (e.ehFinal()) {
				finaisEAlcancaveis.add(e);
			} else {
				estadosNaoFinais.add(e);
			}
		}

		// Se o estado alcança um estado final ele muda para a lista de
		// estados finais ou alcançáveis
		iterador = estadosNaoFinais.iterator();
		while (iterador.hasNext()) {
			Estado estado = iterador.next();
			if (alcancaEstadoFinal(estado)) {
				finaisEAlcancaveis.add(estado);
			}
		}
		iterador = finaisEAlcancaveis.iterator();
		while (iterador.hasNext()) {
			estadosNaoFinais.remove(iterador.next());
		}

		// Remove os estados que estão na lista de estados não finais do
		// autômato
		iterador = estadosNaoFinais.iterator();
		while (iterador.hasNext()) {
			Estado estado = iterador.next();
			removeEstado(estado);
		}
	}

	private boolean alcancaEstadoFinal(Estado e) {
		// Vai funcionar como uma pilha
		LinkedList<Estado> estados = new LinkedList<Estado>();
		LinkedList<Estado> jaVisitados = new LinkedList<Estado>();
		estados.push(e);
		while (!estados.isEmpty()) {
			Estado estadoDesempilhado = estados.pop();
			if (estadoDesempilhado.ehFinal()) {
				return true;
			}
			jaVisitados.push(estadoDesempilhado);
			Transicao[] transicoesDoEstadoPop = estadoDesempilhado
					.getTransicoes();
			for (int i = 0; i < transicoesDoEstadoPop.length; i++) {
				Estado estado = transicoesDoEstadoPop[i].getDestino();
				if (!estados.contains(estado) && !jaVisitados.contains(estado)) {
					estados.push(estado);
				}
			}
		}
		return false;
	}

	public void eliminarEstadosInalcancaveis() {
		// Vai funcionar como uma pilha
		LinkedList<Estado> visitar = new LinkedList<Estado>();
		LinkedList<Estado> alcancaveis = new LinkedList<Estado>();
		visitar.push(q0);
		while (!visitar.isEmpty()) {
			Estado estadoDesempilhado = visitar.pop();
			alcancaveis.add(estadoDesempilhado);
			Transicao[] transicoes = estadoDesempilhado.getTransicoes();
			for (int i = 0; i < transicoes.length; i++) {
				Estado estadoTransicao = transicoes[i].getDestino();
				if (!visitar.contains(estadoTransicao)
						&& !alcancaveis.contains(estadoTransicao)) {
					visitar.push(estadoTransicao);
				}
			}
		}
		estados = new LinkedList<Estado>();
		Iterator<Estado> iterador = alcancaveis.iterator();
		while (iterador.hasNext()) {
			estados.add(iterador.next());
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

		Estado estadoFinal = new Estado("Fi", automatoAuxiliar);
		Iterator<Estado> iterador = automatoAuxiliar.estados.iterator();
		while (iterador.hasNext()) {
			Estado estado = iterador.next();
			if (estado.ehFinal()) {
				estado.setFinal(false);
				estado.addTransicao(estadoFinal, "");
			}
		}
		estadoFinal.setFinal(true);
		automatoAuxiliar.estados.addLast(estadoFinal);

		automatoAuxiliar.removeEstadosInternos();
		return automatoAuxiliar.criarExpressaoRegular();
	}

	private ExpressaoRegular criarExpressaoRegular() {
		String exp = "";
		Estado inicial = getEstadoInicial();
		Transicao[] transicao = inicial.getTransicoes();
		for (int i = 0; i < transicao.length; i++) {
			exp += transicao[i].getSimbolo();
			if (i != transicao.length - 1) {
				exp += " | ";
			}
		}

		ExpressaoRegular expressao = new ExpressaoRegular();
		expressao.setDescricao(exp);
		return expressao;
	}

	private void removeEstadosInternos() {
		int numEstados = estados.size() - 2;
		for (int i = 0; i < numEstados; i++) {
			Estado e = estados.getFirst();
			// Transições que chegam
			Transicao[] chegando = e.getTransicoesQueChegam();
			// Transições que saem
			Transicao[] saem = e.getTransicoesParaOutroEstado();
			// Transições pra si mesmo
			Transicao[] loop = e.getTransicoesParaSiMesmo();

			String transicaoEstrela = "";
			if (loop.length > 0) {
				transicaoEstrela += "(";
			}
			for (int j = 0; j < loop.length; j++) {
				transicaoEstrela += loop[j].getSimbolo();
				if (j != loop.length - 1) {
					transicaoEstrela += "|";
				}
			}
			if (loop.length > 0) {
				transicaoEstrela += ")*";
			}

			for (int j = 0; j < chegando.length; j++) {
				for (int k = 0; k < saem.length; k++) {
					String simbolo = chegando[j].getSimbolo()
							+ transicaoEstrela + saem[k].getSimbolo();
					addTransicao(chegando[j].getOrigem(),
							saem[k].getDestino(), simbolo);
				}
			}
			removeEstado(e);
		}

	}

	public Automato getComplemento() {
		Automato complemento = this.getAutomatoMinimizado();
		complemento.normalizarTransicoesNulas();
		Estado[] estado = complemento.getEstados();
		for (int i = 0; i < estado.length; i++) {
			Estado e = estado[i];
			e.setFinal(!e.ehFinal());
		}
		return complemento;
	}

	public String getGramaticaRegular() {
		Automato auxiliar = this.getAutomatoMinimizado();
		Estado[] estados = auxiliar.getEstados();
		String retorno = "";
		String comEpslon = "";
		for (int i = 0; i < estados.length; i++) {
			// Faz parte X->
			if (estados[i].ehInicial()) {
				if (estados[i].ehFinal()) {
					retorno += "[" + estados[i].getNome() + "] -> ";
					comEpslon += "[" + estados[i].getNome()
							+ "](inicial) -> epslon|";
				} else {
					retorno += "[" + estados[i].getNome() + "](inicial) -> ";
				}
			} else {
				retorno += "[" + estados[i].getNome() + "] -> ";
			}
			// Faz parte -> aB
			Transicao[] transicoes = estados[i].getTransicoes();
			for (int j = 0; j < transicoes.length; j++) {
				if (estados[i].ehInicial()) {
					if (estados[i].ehFinal()) {
						retorno += transicoes[j].getSimbolo() + "["
								+ transicoes[j].getDestino().getNome()
								+ "] | ";
						comEpslon += transicoes[j].getSimbolo() + "["
								+ transicoes[j].getDestino().getNome()
								+ "] | ";
						if (transicoes[j].getDestino().ehFinal()) {
							retorno += transicoes[j].getSimbolo() + " | ";
							comEpslon += transicoes[j].getSimbolo() + " | ";
						}
					} else {
						retorno += transicoes[j].getSimbolo() + "["
								+ transicoes[j].getDestino().getNome()
								+ "] | ";
						if (transicoes[j].getDestino().ehFinal()) {
							retorno += transicoes[j].getSimbolo() + " | ";
						}
					}
				} else {
					retorno += transicoes[j].getSimbolo() + "["
							+ transicoes[j].getDestino().getNome()
							+ "] | ";
					if (transicoes[j].getDestino().ehFinal()) {
						retorno += transicoes[j].getSimbolo() + " | ";
					}
				}
			}
			int tamanhoRetorno = retorno.length();
			// Tamanho da string com epslon
			int tamComEpslon = comEpslon.length();
			if (retorno.charAt(tamanhoRetorno - 2) == '|') {
				retorno = retorno.substring(0, retorno.length() - 2);
				if (tamComEpslon > 0) {
					comEpslon = comEpslon.substring(0, comEpslon.length() - 2);
				}
			}
			retorno += "\n";
		}
		if (!"".equals(comEpslon)) {
			comEpslon += "\n";
		}
		return comEpslon + retorno;
	}

	// Retorna senteças pelo tamanho
	public LinkedList<String> getSentencas(int tamanho, Estado estado) {
		LinkedList<String> resultado = new LinkedList<String>();
		LinkedList<String> sentencas;
		Transicao[] transicoes;
		String simbolo;
		transicoes = estado.getTransicoes();

		if (tamanho == 1) {
			for (Transicao transicao : transicoes) {
				if (transicao.getDestino().ehFinal()) {
					simbolo = transicao.getSimbolo();
					resultado.add(simbolo);
				}
			}
		} else {
			for (Transicao transicao : transicoes) {
				sentencas = getSentencas(tamanho - 1,
						transicao.getDestino());
				for (String sentencaAtual : sentencas) {
					resultado.add(transicao.getSimbolo() + sentencaAtual);
				}
			}
		}
		return resultado;
	}

	public Automato getUniao(Automato automato2) {
		if (automato2 == null) {
			return null;
		}

		Automato uniao = new Automato();
		Estado novoInicial = new Estado("novoInicial", uniao);
		uniao.addEstado(novoInicial);
		uniao.setEstadoInicial(novoInicial.getNome());
		Automato a1 = this.getAutomatoMinimizado();
		Automato a2 = automato2.getAutomatoMinimizado();

		Estado inicialA1 = a1.getEstadoInicial();
		Estado inicialA2 = a2.getEstadoInicial();
		if (inicialA1.ehFinal() || inicialA2.ehFinal()) {
			novoInicial.setFinal(true);
		}
		// Seta o alfabeto
		String[] alfabeto = unirAlfabeto(a1, a2);
		uniao.setAlfabeto(alfabeto);
		// Adiciona as transições do estado inicial do automato 1 para o novo
		// estado inicial
		Transicao[] t1 = inicialA1.getTransicoes();
		for (int i = 0; i < t1.length; i++) {
			Transicao t = t1[i];
			Estado dest = t.getDestino();
			uniao.addEstado("a1" + dest.getNome());
			Estado novoDest = uniao.getEstado("a1" + dest.getNome());
			novoInicial.addTransicao(novoDest, t.getSimbolo());
		}

		// Adiciona as transições do estado inicial do automato 2 para o novo
		// estado inicial
		Transicao[] t2 = inicialA2.getTransicoes();
		for (int i = 0; i < t2.length; i++) {
			Transicao t = t2[i];
			Estado dest = t.getDestino();
			uniao.addEstado("a2" + dest.getNome());
			Estado novoDest = uniao.getEstado("a2" + dest.getNome());
			novoInicial.addTransicao(novoDest, t.getSimbolo());
		}
		// Adiciona os outros estados do automato com nomes diferentes
		Estado[] estados = a1.getEstados();
		for (int i = 0; i < estados.length; i++) {
			uniao.addEstado("a1" + estados[i].getNome());
			if (estados[i].ehFinal()) {
				uniao.getEstado("a1" + estados[i].getNome()).setFinal(true);
			}
		}
		estados = a2.getEstados();
		for (int i = 0; i < estados.length; i++) {
			uniao.addEstado("a2" + estados[i].getNome());
			if (estados[i].ehFinal()) {
				uniao.getEstado("a2" + estados[i].getNome()).setFinal(true);
			}
		}

		// Adiciona as transições
		estados = a1.getEstados();
		for (int i = 0; i < estados.length; i++) {
			Estado aux = uniao.getEstado("a1" + estados[i]);
			Transicao[] t = estados[i].getTransicoes();
			for (int j = 0; j < t.length; j++) {
				aux.addTransicao(
						uniao.getEstado("a1"
								+ t[j].getDestino().getNome()),
						t[j].getSimbolo());
			}
		}
		estados = a2.getEstados();
		for (int i = 0; i < estados.length; i++) {
			Estado aux = uniao.getEstado("a2" + estados[i]);
			Transicao[] t = estados[i].getTransicoes();
			for (int j = 0; j < t.length; j++) {
				aux.addTransicao(
						uniao.getEstado("a2"
								+ t[j].getDestino().getNome()),
						t[j].getSimbolo());
			}
		}
		return uniao.getAutomatoMinimizado();
	}

	public String[] unirAlfabeto(Automato automato1, Automato automato2) {
		HashSet<String> a = new HashSet<String>();
		a.addAll(Arrays.asList(automato1.getAlfabeto()));
		a.addAll(Arrays.asList(automato2.getAlfabeto()));
		String[] retorno = new String[a.size()];
		a.toArray(retorno);
		return retorno;
	}

	public Automato getInterseccao(Automato automato2) {
		Automato a1Complemento = this.getComplemento();
		Automato a2Complemento = automato2.getComplemento();
		Automato uniaoDeComplementos = a1Complemento.getUniao(a2Complemento);
		Automato aux = uniaoDeComplementos.getComplemento();
		aux.eliminarEstadosMortos();
		return aux;
	}

	public boolean estaContido(Automato automato2) {
		Automato af2Complemento = automato2.getComplemento();
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
		Automato automato = this.getAutomatoMinimizado();
		Automato reverso = new Automato();
		reverso.setNome("af-reverso");
		reverso.setAlfabeto(automato.getAlfabeto());
		reverso.addEstado("novoInicial");
		reverso.setEstadoInicial("novoInicial");
		Estado[] estadosAF = automato.getEstados();
		Transicao[] transicoes;
		// Adiciona os estados
		for (Estado e : estadosAF) {
			reverso.addEstado(e.getNome());
		}
		// Inverter as transições
		for (Estado e : estadosAF) {
			transicoes = e.getTransicoes();
			for (Transicao t : transicoes) {
				reverso.addTransicao(t.getDestino().getNome(), t
						.getOrigem().getNome(), t.getSimbolo());
				if (t.getDestino().ehFinal()) {
					reverso.addTransicao("novoInicial", t.getOrigem()
							.getNome(), t.getSimbolo());
				}

			}
		}
		reverso.setEstadoFinal(automato.getEstadoInicial().getNome());
		return reverso.getAutomatoMinimizado();
	}
}
