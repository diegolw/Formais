package manipulador.modelo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Automato {

	private static final Estado NULL = null;
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
		estado.setInicial(true);
	}

	public LinkedList<Transicao> getTransicoes() {
		LinkedList<Transicao> transicoes = new LinkedList<Transicao>();
		for (Estado estado : estados) {
			transicoes.addAll(estado.getTransicoesList());
		}
		return transicoes;
	}

	public Automato determinizar() {
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
			boolean ehFinal = false;
			for (Estado _estado : _destinos) {
				nome += _estado.getNome();
				if (_estado.ehFinal()) {
					ehFinal = true;
				}
			}
			if (!nome.equals("")) {
				if (!_automato.existeEstado(nome)) {
					EstadoAuxiliar _destino = new EstadoAuxiliar(nome, _destinos);
					_automato.addEstado(_destino);
					if (ehFinal) {
						_destino.setFinal(true);
						_automato.setEstadoFinal(_destino.getNome());
					}
					novosEstados.add(_destino);
				}
				// E adiciona a transição
				EstadoAuxiliar _destino = (EstadoAuxiliar) _automato
						.getEstado(nome);
				if (_destino != null) {
					_inicial.addTransicao(_destino, simbolo);
				}
			}
		}

		_automato = det(novosEstados, transicoes, _automato);

		// Falta dizer se o estado é inicla ou final

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
		
		return _automato;
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
				boolean ehFinal = false;
				for (Estado _estado : _destinos) {
					nome += _estado.getNome();
					if (_estado.ehFinal()) {
						ehFinal = true;
					}
				}
				if (!nome.equals("") && !_automato.existeEstado(nome)) {
					EstadoAuxiliar _destino = new EstadoAuxiliar(nome,
							_destinos);
					_automato.addEstado(_destino);
					if (ehFinal) {
						_destino.setFinal(ehFinal);
						_automato.setEstadoFinal(_destino.getNome());						
					}
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
			estado.setFinal(!estado.ehFinal());
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
		}
		return complemento;
	}
	
	public LinkedList<Estado> getFinais() {
		return finais;
	}
	
	public void setFinais(LinkedList<Estado> finais) {
		this.finais = finais;
	}

	public static void setCount(int count) {
		Automato.count = count;
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
			estadoClone.setInicial(estado.ehInicial());
			estadoClone.setFinal(estado.ehFinal());
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
	
	public void minimizar(){
				
		this.eliminarEstadosInalcancaveis();
		
		this.eliminarEstadosMortos();
		
		this.normatizaTransicoesNulas();
		
		this.reduzirPorEquivalencia();
		
		this.eliminarEstadosMortos();
		
		
		
	}
	
	public void reduzirPorEquivalencia() {
        LinkedList<EstadoAuxiliar> classesDeEquivalencia = new LinkedList<EstadoAuxiliar>();
        EstadoAuxiliar estadosFinais = new EstadoAuxiliar("Finais");
        EstadoAuxiliar estadosNaoFinais = new EstadoAuxiliar("Não Finais");
        Iterator<Estado> it = estados.iterator();
        while (it.hasNext()) {
            Estado e = it.next();
            if (e.ehFinal()) {
                estadosFinais.addEstadoIncluso(e);
            } else {
                estadosNaoFinais.addEstadoIncluso(e);
            }
        }
       // System.out.println("Estados finais="+estadosFinais.getEstadosInclusos().length);
       // System.out.println("Estados Nao finais="+estadosNaoFinais.getEstadosInclusos().length);


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
       // System.out.println("Classes de equivalencia");
       // printLLEA(classesDeEquivalencia);
       
        montarAFReduzido(classesDeEquivalencia);
    }
	
	 private void montarAFReduzido(LinkedList<EstadoAuxiliar> classesDeEquivalencia) {
	        LinkedList<Estado> estadosReduzidos = new LinkedList<Estado>();
	        Iterator<EstadoAuxiliar> it = classesDeEquivalencia.iterator();
	        int cont = 0;
	        while (it.hasNext()) {
	            EstadoAuxiliar er = it.next();
	            Estado estadoReduzido = new Estado("q" + cont);
	            this.addEstado(estadoReduzido);
	            er.setEstadoAssociado(estadoReduzido);
	            er.setEstadoAssociadoParaFinalCasoAlgumEstadoInclusoSejaTambem();
	            er.setEstadoAssociadoParaInicialCasoAlgumEstadoInclusoSejaTambem();
	            estadosReduzidos.add(estadoReduzido);
	            cont++;
	        }
	        it = classesDeEquivalencia.iterator();
	        while (it.hasNext()) {
	            EstadoAuxiliar ex = it.next();
	            Estado estadoReduzido = ex.getEstadoAssociado();
	            Estado umEstadoIncluso = ex.getEstadosInclusos()[0];
	            for (int i = 0; i < alfabeto.length; i++) {
	                EstadoAuxiliar transicaoAux = retornaEstadoAuxQuePossuiEstado(classesDeEquivalencia, umEstadoIncluso.getTransicoesDoSimbolo(alfabeto[i])[0].getDestino());
	                addTransicao(estadoReduzido, transicaoAux.getEstadoAssociado(), alfabeto[i]);
	            }
	        }

	        EstadoAuxiliar ex = retornaEstadoAuxQuePossuiEstado(classesDeEquivalencia, inicial);
	        if (ex != null) {
	           this.setEstadoInicial(ex);
	        }

	        estados = estadosReduzidos;
	        

	    }
	
	private LinkedList<EstadoAuxiliar> dividirClassesDeEquivalencia(LinkedList<EstadoAuxiliar> classesDeEquivalencia) {
        boolean mudou = false;
        LinkedList<EstadoAuxiliar> novasClassesDeEquivalencia = new LinkedList<EstadoAuxiliar>();
        Iterator<EstadoAuxiliar> itClasses = classesDeEquivalencia.iterator();
        while (itClasses.hasNext()) { //Para cada classe de equivalência faça:
            EstadoAuxiliar classe = itClasses.next();
            if (classe.getEstadosInclusos().length > 1) { //Se tiver mais de um estado nesta classe de eqv faça:
                Estado[] estadosDaClasse = classe.getEstadosInclusos();
                LinkedList<EstadoAuxiliar> classesTemporarias = new LinkedList<EstadoAuxiliar>();
                EstadoAuxiliar novaClasse = new EstadoAuxiliar("",estadosDaClasse[0]);
                classesTemporarias.add(novaClasse);
                for (int i = 1; i < estadosDaClasse.length; i++) {
                    Iterator<EstadoAuxiliar> itClassesTemp = classesTemporarias.iterator();
                    EstadoAuxiliar classeEquivalente = null;
                    while (itClassesTemp.hasNext() && classeEquivalente == null) {
                        EstadoAuxiliar classeTemporaria = itClassesTemp.next();
                        if (ehEquivalente(estadosDaClasse[i], classeTemporaria.getEstadosInclusos()[0], classesDeEquivalencia)) {
                            classeEquivalente = classeTemporaria;
                        }
                    }
                    if (classeEquivalente != null) {
                        classeEquivalente.addEstadoIncluso(estadosDaClasse[i]);
                    } else {
                        classesTemporarias.add(new EstadoAuxiliar("",estadosDaClasse[i]));
                        mudou = true;
                    }
                }

                Iterator<EstadoAuxiliar> itClassesTemp = classesTemporarias.iterator();
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
	
    private boolean ehEquivalente(Estado e1, Estado e2, LinkedList<EstadoAuxiliar> classesDeEquivalencia) {
        for (int i = 0; i < alfabeto.length; i++) {
            EstadoAuxiliar ex1 = retornaEstadoAuxQuePossuiEstado(classesDeEquivalencia, e1.getTransicoesDoSimbolo(alfabeto[i])[0].getDestino());
            EstadoAuxiliar ex2 = retornaEstadoAuxQuePossuiEstado(classesDeEquivalencia, e2.getTransicoesDoSimbolo(alfabeto[i])[0].getDestino());
            if (ex1 != ex2) {
                return false;
            }
        }
        return true;
    }
    
    private EstadoAuxiliar retornaEstadoAuxQuePossuiEstado(LinkedList<EstadoAuxiliar> classesDeEquivalencia, Estado e) {
        Iterator<EstadoAuxiliar> it = classesDeEquivalencia.iterator();
        while (it.hasNext()) {
            EstadoAuxiliar x = it.next();
            if (x.possui(e)) {
                return x;
            }
        }
        return null;
    }
	
    public void normatizaTransicoesNulas(){
    	Estado erro = getEstado("erro");
    	if(erro == null){
    		erro = new Estado("erro");
    		this.addEstado(erro);
    		for (int i = 0; i < alfabeto.length; i++) {
                erro.addTransicao(erro, alfabeto[i]);
            }  
    	}
        Estado[] es = getEstados();
        for (int i = 0; i < es.length; i++) {
            for (int j = 0; j < alfabeto.length; j++) {
                Transicao[] ts = es[i].getTransicoesDoSimbolo(alfabeto[j]);
                if (ts.length == 0) {
                    es[i].addTransicao(erro, alfabeto[j]);
                }
            }
        }
    }
	
    private boolean alcancaEstadoFinal(Estado e) {
        LinkedList<Estado> pilhaDeEstados = new LinkedList<Estado>();
        LinkedList<Estado> listaDeEstadosJaVisitados = new LinkedList<Estado>();
        pilhaDeEstados.push(e);
        while (!pilhaDeEstados.isEmpty()) {
            Estado estadoPop = pilhaDeEstados.pop();
            if (estadoPop.ehFinal()) {
                return true;
            }
            listaDeEstadosJaVisitados.push(estadoPop);
            Transicao[] transicoesDoEstadoPop = estadoPop.getTransicoes();
            for (int i = 0; i < transicoesDoEstadoPop.length; i++) {
                Estado estado = transicoesDoEstadoPop[i].getDestino();
                if (!pilhaDeEstados.contains(estado) && !listaDeEstadosJaVisitados.contains(estado)) {
                    pilhaDeEstados.push(estado);
                }
            }
        }
        return false;
    }
	
	public void eliminarEstadosMortos() {
        LinkedList<Estado> listaDeEstadosNaoFinais = new LinkedList<Estado>();
        LinkedList<Estado> listaDeEstadosFinaisEAlcancaveis = new LinkedList<Estado>();

        //Colocar estadosAF finais e não finais em suas devidas listas
        Iterator<Estado> it = estados.iterator();
        while (it.hasNext()) {
            Estado e = it.next();
            if (e.ehFinal()) {
                listaDeEstadosFinaisEAlcancaveis.add(e);
            } else {
                listaDeEstadosNaoFinais.add(e);
            }
        }

        //Se o estado alcança um estado final ele muda para a lista de estadosAF finais ou alcançados
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

        //Remove os estadosAF que estão na lista de estadosAF não finais do autômato
        it = listaDeEstadosNaoFinais.iterator();
        while (it.hasNext()) {
            Estado e = it.next();
            removeEstado(e);
        }

    }
	
	protected void removeEstado(Estado e) {
        if (e != null) {
            Iterator<Estado> it = estados.iterator();
            while (it.hasNext()) {
                it.next().removeTransicoesParaOEstado(e);
            }

            boolean removeu = estados.remove(e);
            if (e == inicial) {
                inicial = null;
            }
        }
    }

	
    public void eliminarEstadosInalcancaveis() {
        LinkedList<Estado> pilhaParaVisitar = new LinkedList<Estado>();
        LinkedList<Estado> listaEstadosAlcancaveis = new LinkedList<Estado>();
        pilhaParaVisitar.push(inicial);
        while (!pilhaParaVisitar.isEmpty()) {
            Estado e = pilhaParaVisitar.pop();
            listaEstadosAlcancaveis.add(e);
            Transicao[] ts = e.getTransicoes();
            for (int i = 0; i < ts.length; i++) {
                Estado et = ts[i].getDestino();
                if (!pilhaParaVisitar.contains(et) && !listaEstadosAlcancaveis.contains(et)) {
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
	
    public boolean ehCompletoETodosEstadosSaoFinais() {
        Estado[] e = this.getEstados();
        for (int i = 0; i < e.length; i++) {
            if (!e[i].ehFinal()) {
                return false;
            }
            Transicao[] t = e[i].getTransicoes();
            if (t.length != this.getAlfabeto().length) {
                return false;
            }
        }

        return true;
    }
    
	public void print(){
		
		String str = "Automato" + this.getNome()+"\n";
		Estado[] es = this.getEstados();
		for(int i =0 ; i< es.length;i++){
			Transicao[] tr = es[i].getTransicoes();
			if(es[i].ehFinal())
				str+= "*";
			if(es[i].ehInicial())
				str += "->";
			str += es[i].getNome()+ " : " ;
			for( int j=0; j<tr.length ; j++){
				str += tr[j].getSimbolo()+" "+ tr[j].getDestino().getNome();
				if(j <tr.length-1)
					str+= ",";
				else
					str += ";\n";
				
			}
			
		}
		
		System.out.println(str);
	}
	
	public void printLLEA(LinkedList<EstadoAuxiliar> lista){
		Iterator<EstadoAuxiliar> itt = lista.iterator();
    	String str = "";
    	int cc = 0;
    	while(itt.hasNext()){
    		cc++;
    		EstadoAuxiliar est = itt.next();
    		Estado[] associ = est.getEstadosInclusos();
    		str += "Classe"+cc+" : ";
    		for(int i=0 ; i < associ.length ; i++){
    			if(associ[i].ehFinal())
    				str+="*";
    			if(associ[i].ehInicial())
    				str+="->";
    			str+= associ[i].getNome()+" ,";
    		}
    		System.out.println(str);
    	}
	}

	public boolean ehVazio() {
		// TODO Auto-generated method stub
		return false;
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
	
	public Automato uniao(Automato clone1, Automato clone2) {
		Automato uniao = new Automato();
		
		// O alfabeto da união é:
		ArrayList<String> alfabeto = new ArrayList<String>();
		String[] alfabeto1 = clone1.getAlfabeto();
		for (int i = 0; i < alfabeto1.length; i++) {
			if (!alfabeto.contains(alfabeto1[i])) {
				alfabeto.add(alfabeto1[i]);
			}
		}
		String[] alfabeto2 = clone2.getAlfabeto();
		for (int i = 0; i < alfabeto2.length; i++) {
			if (!alfabeto.contains(alfabeto2[i])) {
				alfabeto.add(alfabeto2[i]);
			}
		}
		String[] alfabetoUniao = new String[alfabeto.size()];
		for (int i = 0; i < alfabeto.size(); i++) {
			alfabetoUniao[i] = alfabeto.get(i);
		}
		uniao.setAlfabeto(alfabetoUniao);
		
		// 
		Estado inicial = new Estado("q0");
		inicial.setInicial(true);
		if (clone1.getEstadoInicial().ehFinal() || clone2.getEstadoInicial().ehFinal()) {
			inicial.setFinal(true);
			uniao.setEstadoFinal(inicial.getNome());
		}
		uniao.addEstado(inicial);
		uniao.setEstadoInicial(inicial.getNome());
		
		// União dos estados
		Estado[] estados = clone1.getEstados();
		for (int i = 0, max = estados.length; i < max; i++) {
			uniao.addEstado(estados[i]);
			if (estados[i].ehFinal()) {
				uniao.setEstadoFinal(estados[i].getNome());
			}
		}
		estados = clone2.getEstados();
		for (int i = 0, max = estados.length; i < max; i++) {
			uniao.addEstado(estados[i]);
			if (estados[i].ehFinal()) {
				uniao.setEstadoFinal(estados[i].getNome());
			}
		}
		
		// União das transições
		Transicao[] transicoesIniciais = clone1.getEstadoInicial().getTransicoes();
		for (Transicao transicao : transicoesIniciais) {
			uniao.addTransicao(inicial, transicao.getDestino(), transicao.getSimbolo());
		}
		LinkedList<Transicao> transicoes = clone1.getTransicoes();
		for (Transicao transicao : transicoes) {
			uniao.addTransicao(transicao.getOrigem(),
					transicao.getDestino(), transicao.getSimbolo());
		}
		transicoesIniciais = clone2.getEstadoInicial().getTransicoes();
		for (Transicao transicao : transicoesIniciais) {
			uniao.addTransicao(inicial, transicao.getDestino(), transicao.getSimbolo());
		}
		transicoes = clone2.getTransicoes();
		for (Transicao transicao : transicoes) {
			uniao.addTransicao(transicao.getOrigem(),
					transicao.getDestino(), transicao.getSimbolo());
		}
		return uniao;
	}
	
	public Automato concatenar(Automato af1, Automato af2){
		Automato concat = new Automato();
		String[] alf = {};
		ArrayList<String> alfabeto = new ArrayList<String>();
		String[] alfabeto1 = af1.getAlfabeto();
		for (int i = 0; i < alfabeto1.length; i++) {
			if (!alfabeto.contains(alfabeto1[i])) {
				alfabeto.add(alfabeto1[i]);
			}
		}
		String[] alfabeto2 = af2.getAlfabeto();
		for (int i = 0; i < alfabeto2.length; i++) {
			if (!alfabeto.contains(alfabeto2[i])) {
				alfabeto.add(alfabeto2[i]);
			}
		}
		String[] alfabetoUniao = new String[alfabeto.size()];
		for (int i = 0; i < alfabeto.size(); i++) {
			alfabetoUniao[i] = alfabeto.get(i);
		}
		concat.setAlfabeto(alfabetoUniao);
		
		Estado inicial = new Estado("q0");
		inicial.setInicial(true);
		if (af1.getEstadoInicial().ehFinal()) {
			inicial.setFinal(true);
			concat.setEstadoFinal(inicial.getNome());
		}
		concat.addEstado(inicial);
		concat.setEstadoInicial(inicial.getNome());
		
		Estado[] estados = af1.getEstados();
		for (int i = 0, max = estados.length; i < max; i++) {
			concat.addEstado(estados[i]);
			if(estados[i].ehFinal()){
				estados[i].setFinal(false);
				for(int j = 0; j < inicial.getTransicoesList().size();j++){
					estados[j].addTransicao(af2.getEstadoInicial().getTransicoesList().get(j).getDestino(), getTransicoes().get(j).getSimbolo());
				}				
			}
		}
		estados = af2.getEstados();
		for (int i = 0, max = estados.length; i < max; i++) {
			concat.addEstado(estados[i]);
			if (estados[i].ehFinal()) {
				concat.setEstadoFinal(estados[i].getNome());
				estados[i].setFinal(true);
			}
		}
		
		Transicao[] transicoesIniciais = af1.getEstadoInicial().getTransicoes();
		for (Transicao transicao : transicoesIniciais) {
			concat.addTransicao(inicial, transicao.getDestino(), transicao.getSimbolo());
		}
		LinkedList<Transicao> transicoes = af1.getTransicoes();
		for (Transicao transicao : transicoes) {
			concat.addTransicao(transicao.getOrigem(),
					transicao.getDestino(), transicao.getSimbolo());
		}
		af2.getEstadoInicial().setInicial(false);

		transicoes = af2.getTransicoes();
		for (Transicao transicao : transicoes) {
			concat.addTransicao(transicao.getOrigem(),
					transicao.getDestino(), transicao.getSimbolo());
		}
		//concat.eliminarEstadosInalcancaveis();
		//concat.eliminarEstadosMortos();
		af1.print();
		af2.print();
		return concat;
	}

}
