package manipulador.controlador;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;

//import javax.swing.text.html.HTMLDocument.Iterator;

import manipulador.modelo.Automato;
import manipulador.modelo.Estado;
import manipulador.modelo.EstadoAuxiliar;
import manipulador.modelo.Transicao;
import manipulador.visao.GUI;
import manipulador.visao.IGUI;

public class Mediador implements IGUI {

	private GUI window;

	private Automato automato1;
	private Automato automato2;

	public Mediador() {
		window = new GUI();
		window.setVisible(true);
		window.addEventListener(this);
	}

	public void determinizarTest() {
		automato1 = new Automato();
		String[] alfabeto = { "a", "b" };
		automato1.setAlfabeto(alfabeto);
		automato1.addEstado("q0");
		automato1.addEstado("q1");
		automato1.addEstado("q2");
		automato1.addEstado("q3");
		automato1.setEstadoInicial("q0");
		automato1.setEstadoFinal("q3");
		automato1.addTransicao("q0", "q0", "a");
		automato1.addTransicao("q0", "q1", "a");
		automato1.addTransicao("q0", "q0", "b");
		automato1.addTransicao("q1", "q2", "b");
		automato1.addTransicao("q2", "q3", "b");
		automato1.determinizar();

		if (window.ehAutomato1()) {
			atualizarAutomato();
			window.rdbtnAutmato2.setSelected(true);
			atualizarAutomato();
			window.rdbtnAutmato1.setSelected(true);
		} else {
			atualizarAutomato();
			window.rdbtnAutmato1.setSelected(true);
			atualizarAutomato();
			window.rdbtnAutmato2.setSelected(true);
		}
	}
	
	
	public void testar(){
		automato1 = new Automato();
		String[] alfabeto = { "a", "b" };
		automato1.setAlfabeto(alfabeto);
		automato1.addEstado("q0");
		automato1.addEstado("q1");
		automato1.addEstado("q2");
		automato1.addEstado("q3");
		automato1.addEstado("q4");
		automato1.setEstadoInicial("q0");
		automato1.setEstadoFinal("q2");
		automato1.addTransicao("q0", "q1", "a");
		automato1.addTransicao("q0", "q4", "b");
		automato1.addTransicao("q1", "q1", "a");
		automato1.addTransicao("q1", "q2", "b");
		automato1.addTransicao("q2", "q2", "b");
		automato1.addTransicao("q3", "q2", "b");
		automato1.addTransicao("q4", "q4", "b");
				
		/*boolean completo = automato1.ehCompletoETodosEstadosSaoFinais();
		String str = "Não completo";
		if(completo)
			str = "Completo";
		System.out.println("Automato 1 e completo?" + str);*/
		
		printAF(automato1);
		automato1.minimizar();
		printAF(automato1);
		
	}
	
	public void printAF(Automato AF){
		
		String str = "Automato" + AF.getNome()+"\n";
		Estado[] es = AF.getEstados();
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

	public void testar2() {
		automato1 = new Automato();
		automato2 = new Automato();
		String[] alf2 = { "a", "b" };
		String[] alf3 = { "a", "b", "c" };
		automato1.setAlfabeto(alf2);
		automato1.setNome("AF-um");
		automato1.addEstado("A");
		automato1.addEstado("B");
		// automato1.addEstado("C");
		automato1.addTransicao("A", "B", "a");
		// automato1.addTransicao("A", "C", "b");
		automato1.addTransicao("B", "A", "a");
		automato1.addTransicao("B", "B", "b");
		// automato1.addTransicao("C", "C", "a");
		// automato1.addTransicao("C", "B", "b");
		automato1.setEstadoInicial("A");
		automato1.setEstadoFinal("B");
		
		
		automato2.setAlfabeto(alf3);
		automato2.setNome("AF-dois");
		automato2.addEstado("C");
		automato2.addEstado("D");
		automato2.addTransicao("C", "D", "a");
		automato2.addTransicao("C", "C", "b");
		automato2.addTransicao("C", "D", "c");
		automato2.addTransicao("D", "C", "c");
		automato2.setEstadoInicial("C");
		automato2.setEstadoFinal("D");
		
		
		if (window.ehAutomato1()) {
			atualizarAutomato();
			window.rdbtnAutmato2.setSelected(true);
			atualizarAutomato();
			window.rdbtnAutmato1.setSelected(true);
		} else {
			atualizarAutomato();
			window.rdbtnAutmato1.setSelected(true);
			atualizarAutomato();
			window.rdbtnAutmato2.setSelected(true);
		}
	}

	public void atualizarAutomato() {
		Automato automato = getAutomato();

		Estado[] estados = automato.getEstados();
		String alfabeto[] = automato.getAlfabeto();
		window.setAlfabeto(alfabeto);

		for (int i = 0; i < estados.length; i++) {
			Estado estadoAtual = estados[i];
			String[] linha = new String[alfabeto.length + 1];
			Transicao[] transicoes = estadoAtual.getTransicoes();
			if (automato.getEstadoInicial() == estadoAtual) {
				linha[0] = "->";
			} else {
				linha[0] = "";
			}
			if (estadoAtual.ehFinal()) {
				linha[0] += "* ";
			}
			linha[0] += estadoAtual.getNome();
			for (int z = 0; z < alfabeto.length; z++) {
				for (int j = 0; j < transicoes.length; j++) {
					if (transicoes[j].getSimbolo().equals(alfabeto[z])) {
						if (linha[z + 1] == null) {
							linha[z + 1] = transicoes[j].getDestino().getNome();
						} else
							linha[z + 1] += ", "
									+ transicoes[j].getDestino().getNome();
					}
				}
			}
			window.addRow(linha);
		}
	}
	
	public void atualizarAutomatoResultado(Automato automato) {
		Estado[] estados = automato.getEstados();
		String alfabeto[] = automato.getAlfabeto();
		window.setAlfabetoResultado(alfabeto);

		for (int i = 0; i < estados.length; i++) {
			Estado estadoAtual = estados[i];
			String[] linha = new String[alfabeto.length + 1];
			Transicao[] transicoes = estadoAtual.getTransicoes();
			if (automato.getEstadoInicial() == estadoAtual) {
				linha[0] = "->";
			} else {
				linha[0] = "";
			}
			if (estadoAtual.ehFinal()) {
				linha[0] += "* ";
			}
			linha[0] += estadoAtual.getNome();
			for (int z = 0; z < alfabeto.length; z++) {
				for (int j = 0; j < transicoes.length; j++) {
					if (transicoes[j].getSimbolo().equals(alfabeto[z])) {
						if (linha[z + 1] == null) {
							linha[z + 1] = transicoes[j].getDestino()
									.getNome();
						} else
							linha[z + 1] += ", "
									+ transicoes[j].getDestino()
											.getNome();
					}
				}
			}
			window.addRowResultado(linha);
		}
	}

	@Override
	public void setAlfabeto(String[] simbolos) {
		if (window.ehAutomato1()) {
			automato1 = new Automato();
			automato1.setNome("AF-um");
			automato1.setAlfabeto(simbolos);
		} else {
			automato2 = new Automato();
			automato2.setNome("AF-dois");
			automato2.setAlfabeto(simbolos);
		}
	}

	@Override
	public void addEstado(String nome, boolean ehInicial, boolean ehFinal) {
		if (window.ehAutomato1()) {
			automato1.addEstado(nome);
			if (ehInicial) {
				automato1.setEstadoInicial(nome);
			}
			if (ehFinal) {
				automato1.setEstadoFinal(nome);
			}
		} else {
			automato2.addEstado(nome);
			if (ehInicial) {
				automato2.setEstadoInicial(nome);
			}
			if (ehFinal) {
				automato2.setEstadoFinal(nome);
			}
		}
	}

	public Automato getAutomato() {
		return window.ehAutomato1() ? automato1 : automato2;
	}

	@Override
	public void addTransicao(String origem, String destino, String simbolo) {
		System.out.println("Adicionar transição de " + origem + " para "
				+ destino + " por " + simbolo);
		if (window.ehAutomato1()) {
			automato1.addTransicao(origem, destino, simbolo);
		} else {
			automato2.addTransicao(origem, destino, simbolo);
		}
	}

	@Override
	public void expressaoRegular(String expressao) {
		// Expressão já vem formatada pela View!

	}

	@Override
	public void minimizar(){
		// TODO Auto-generated method stub
		Automato a = uniao();
		a.determinizar();
		atualizarAutomatoResultado(a);

	}

	@Override
	public void igualdade() {
		// TODO Auto-generated method stub

	}

	@Override
	public void interseccao() {
		// TODO Auto-generated method stub

	}

	@Override
	public void diferenca() {
		// L1 - L2 = !((!L1 U L1) U (!L2 U L1))
		atualizarAutomatoResultado(uniao());
		
	}

	@Override
	public void ocorrencias() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enumerar(int num) {
		// TODO Auto-generated method stub

	}

	//

	// XXX: E se tiver estados com nomes iguais?
	private Automato uniao() {
		Automato uniao = new Automato();
		
		// O alfabeto da união é:
		ArrayList<String> alfabeto = new ArrayList<String>();
		String[] alfabeto1 = automato1.getAlfabeto();
		for (int i = 0; i < alfabeto1.length; i++) {
			if (!alfabeto.contains(alfabeto1[i])) {
				alfabeto.add(alfabeto1[i]);
			}
		}
		String[] alfabeto2 = automato2.getAlfabeto();
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
		uniao.addEstado(inicial);
		uniao.setEstadoInicial(inicial.getNome());
		
		// União dos estados
		Estado[] estados = automato1.getEstados();
		for (int i = 0, max = estados.length; i < max; i++) {
			uniao.addEstado(estados[i]);
			if (estados[i].ehFinal()) {
				uniao.setEstadoFinal(estados[i].getNome());
			}
		}
		estados = automato2.getEstados();
		for (int i = 0, max = estados.length; i < max; i++) {
			uniao.addEstado(estados[i]);
			if (estados[i].ehFinal()) {
				uniao.setEstadoFinal(estados[i].getNome());
			}
		}
		
		// União das transições
		Transicao[] transicoesIniciais = automato1.getEstadoInicial().getTransicoes();
		for (Transicao transicao : transicoesIniciais) {
			uniao.addTransicao(inicial, transicao.getDestino(), transicao.getSimbolo());
		}
		LinkedList<Transicao> transicoes = automato1.getTransicoes();
		for (Transicao transicao : transicoes) {
			uniao.addTransicao(transicao.getOrigem(),
					transicao.getDestino(), transicao.getSimbolo());
		}
		transicoesIniciais = automato2.getEstadoInicial().getTransicoes();
		for (Transicao transicao : transicoesIniciais) {
			uniao.addTransicao(inicial, transicao.getDestino(), transicao.getSimbolo());
		}
		transicoes = automato2.getTransicoes();
		for (Transicao transicao : transicoes) {
			uniao.addTransicao(transicao.getOrigem(),
					transicao.getDestino(), transicao.getSimbolo());
		}
		return uniao;
	}
	
	private Automato complemento() {
		Automato automato = getAutomato();
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
}
