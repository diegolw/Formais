package manipulador.modelo;

import java.util.Iterator;
import java.util.LinkedList;

public class Automato {

	private static int count = 0;
	private String nome;
	private String[] alfabeto;
	private LinkedList<Estado> estados;
	private Estado inicial;
	private LinkedList<Estado> finais;

	public Automato() {
		nome = "Automato " + count++;
		alfabeto = new String[0];
		estados = new LinkedList<Estado>();
		finais = new LinkedList<Estado>();
	}

	// Funções que recebem String vêem da view

	public String[] getAlfabeto() {
		return alfabeto;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setAlfabeto(String[] alfabeto) {
		this.alfabeto = alfabeto;
	}

	public Estado getEstadoInicial() {
		return inicial;
	}

	public void setEstadoInicial(String nome) {
		Estado estado = getEstado(nome);
		if (estado != null) {
			setEstadoInicial(estado);
		}
	}

	public void setEstadoFinal(String nome) {
		Estado estado = getEstado(nome);
		if (estado != null) {
			setEstadoFinal(estado);
		}
	}

	public LinkedList<Estado> getEstadosFinais() {
		return finais;
	}

	public void addEstado(String nome) {
		if (!existeEstado(nome)) {
			Estado estado = new Estado(nome);
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
	
	public LinkedList<Estado> getEstadosList() {
		return estados;
	}
	
	public void setEstadosList(LinkedList<Estado> estados) {
		this.estados = estados;
	}

	public void addTransicao(String origem, String destino, String simbolo) {
		Estado estadoOrigem = getEstado(origem);
		Estado estadoDestino = getEstado(destino);
		if (estadoOrigem != null && estadoDestino != null) {
			addTransicao(estadoOrigem, estadoDestino, simbolo);
		}
	}

	public void addTransicao(Estado estadoOrigem, Estado estadoDestino,
			String simbolo) {
		if (estadoOrigem != null && estadoDestino != null) {
			estadoOrigem.addTransicao(estadoDestino, simbolo);
		}
	}

	protected void setEstadoFinal(Estado estado) {
		estado.setFinal(true);
		finais.add(estado);
	}

	protected void setEstadoInicial(Estado estado) {
		inicial = estado;
	}

	public LinkedList<Transicao> getTransicoes() {
		LinkedList<Transicao> transicoes = new LinkedList<Transicao>();
		for (Estado estado : estados) {
			transicoes.addAll(estado.getTransicoesList());
		}
		return transicoes;
	}

	public void determinizar() {
		Automato _automato = new Automato();
		_automato.setAlfabeto(alfabeto);

		EstadoAuxiliar _inicial = new EstadoAuxiliar(inicial.getNome(), inicial);
		_automato.addEstado(_inicial);
		_automato.setEstadoInicial(_inicial);

		LinkedList<Transicao> transicoes = getTransicoes();
		LinkedList<EstadoAuxiliar> novosEstados = new LinkedList<EstadoAuxiliar>();

		// q0
		// Para cada símbolo
		for (String simbolo : alfabeto) {
			// Simbolo a
			// q0, q0, a
			// q0, q1, a
			//
			// Simbolo b
			// q0, q0, b
			// q1, q2, b
			// q2, q3, b
			LinkedList<Estado> _destinos = new LinkedList<Estado>();
			for (Transicao transicao : transicoes) {

				Estado _origem = transicao.getOrigem();
				Estado _destino = transicao.getDestino();
				String _simbolo = transicao.getSimbolo();

				// Se for as transições do estado q0 e mesmo símbolo
				if (_origem.getNome().equals(_inicial.getNome())
						&& _simbolo.equals(simbolo)) {
					// Adiciona numa lista todos os estados alcançáveis
					if (!_destinos.contains(_destino)) {
						_destinos.add(_destino);
					}
				}
			}
			// Agora cria um estado novo com esses estados, caso não exista
			String nome = "";
			for (Estado _estado : _destinos) {
				nome += _estado.getNome();
			}
			if (!_automato.existeEstado(nome)) {
				EstadoAuxiliar _destino = new EstadoAuxiliar(nome, _destinos);
				_automato.addEstado(_destino);
				novosEstados.add(_destino);
			}
			// E adiciona a transição
			EstadoAuxiliar _destino = (EstadoAuxiliar) _automato
					.getEstado(nome);
			if (_destino != null) {
				_inicial.addTransicao(_destino, simbolo);
			}
		}

		_automato = det(novosEstados, transicoes, _automato);

		// Falta dizer se o estado é inicla ou final

		// Dúvida! Os estados que contém o estado inicial original é também um
		// estado inicial??

		for (Estado estado : _automato.getEstados()) {
			// Como esse automato é um automato de estados auxiliares, esse cast
			// funcionará!
			EstadoAuxiliar auxiliar = (EstadoAuxiliar) estado;

			for (Estado incluso : auxiliar.getEstados()) {
				// Se incluso está contido na lista de finais, então esse é
				// final também!
				if (finais.contains(incluso)) {
					auxiliar.setFinal(true);
					_automato.setEstadoFinal(auxiliar);
				}
			}

		}

		// Agora chama as transições para esse estado novo!

	}

	private Automato det(LinkedList<EstadoAuxiliar> estados,
			LinkedList<Transicao> transicoes, Automato _automato) {
		LinkedList<EstadoAuxiliar> novosEstados = new LinkedList<EstadoAuxiliar>();

		for (EstadoAuxiliar auxiliar : estados) {

			for (String simbolo : alfabeto) {

				LinkedList<Estado> _destinos = new LinkedList<Estado>();
				for (Estado estado : auxiliar.getEstados()) {
					// [q0,q1]
					// q0, a -> q0, q1
					// q1, a -> £

					// q0, b -> q0
					// q1, b -> q2
					for (Transicao transicao : transicoes) {
						Estado _origem = transicao.getOrigem();
						Estado _destino = transicao.getDestino();
						String _simbolo = transicao.getSimbolo();

						// Se for as transições do estado q0 e mesmo símbolo
						if (_origem.getNome().equals(estado.getNome())
								&& _simbolo.equals(simbolo)) {
							// Adiciona numa lista todos os estados alcançáveis
							if (!_destinos.contains(_destino)) {
								_destinos.add(_destino);
							}
						}

					}
				}
				// Agora cria um estado novo com esses estados, caso não exista
				String nome = "";
				for (Estado _estado : _destinos) {
					nome += _estado.getNome();
				}
				if (!nome.equals("") && !_automato.existeEstado(nome)) {
					EstadoAuxiliar _destino = new EstadoAuxiliar(nome,
							_destinos);
					_automato.addEstado(_destino);
					novosEstados.add(_destino);
				}
				// E adiciona a transição
				EstadoAuxiliar _destino = (EstadoAuxiliar) _automato
						.getEstado(nome);
				if (_destino != null) {
					auxiliar.addTransicao(_destino, simbolo);
				}
			}
		}

		if (!novosEstados.isEmpty()) {
			det(novosEstados, transicoes, _automato);
		}
		return _automato;
	}
	
	public Automato complemento() {
		Automato automato = this.clone();
		Automato complemento = new Automato();

		complemento.setAlfabeto(automato.getAlfabeto());
		complemento.setEstadosList(automato.getEstadosList());
		Estado inicial = automato.getEstadoInicial();
		complemento.setEstadoInicial(inicial.getNome());
		Estado[] estados = complemento.getEstados();
		for (int i = 0, max = estados.length; i < max; i++) {
			Estado estado = estados[i];
			if (estado.ehFinal()) {
				estado.setFinal(false);
			} else {
				estado.setFinal(true);
			}
		}
		LinkedList<Transicao> transicoes = automato.getTransicoes();
		for (Transicao transicao : transicoes) {
			complemento.addTransicao(transicao.getOrigem(),
					transicao.getDestino(), transicao.getSimbolo());
		}

		// Criar epsilon transições
		boolean temEstadoErro = false;
		Estado erro = null;
		for (int i = 0, max = estados.length; i < max; i++) {
			// Eh completo?
			Estado estado = estados[i];
			String[] alfabeto = complemento.getAlfabeto();
			for (int j = 0; j < alfabeto.length; j++) {
				// Se não tem transição com esse símbolo,
				// então cria transição para o estado de erro
				if (!estado.temTransicaoComEsseSimbolo(alfabeto[j])) {
					if (!temEstadoErro) {
						erro = new Estado("erro");
						erro.setFinal(true);
						for (int k = 0; k < alfabeto.length; k++) {
							complemento.addTransicao(erro, erro, alfabeto[k]);
						}
						complemento.addEstado(erro);
						temEstadoErro = true;
					}
					complemento.addTransicao(estado, erro, alfabeto[j]);
				}
			}
			if (estado.ehFinal()) {
				estado.setFinal(false);
			} else {
				estado.setFinal(true);
			}
		}
		return complemento;
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
			Estado estadoClone = new Estado(estado.getNome());
			clone.addEstado(estadoClone);
		}
		clone.setEstadoInicial(inicial);
		Iterator<Estado> iteradorFinais = finais.iterator();
		while (iteradorFinais.hasNext()) {
			Estado estado = iteradorFinais.next();
			clone.setEstadoFinal(estado);
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
}
