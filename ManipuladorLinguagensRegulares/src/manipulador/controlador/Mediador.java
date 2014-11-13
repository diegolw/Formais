package manipulador.controlador;

import java.util.ArrayList;
import java.util.LinkedList;

import manipulador.visao.GUI;
import manipulador.visao.IGUI;
//import javax.swing.text.html.HTMLDocument.Iterator;
import manipulador.modelo.Automato;
import manipulador.modelo.Estado;
import manipulador.modelo.Transicao;
import manipulador.modelo.Run;
import manipulador.modelo.Fragmento;
import manipulador.modelo.State;

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
		//automato1.determinizar();
		
		atualizarAutomatoResultado(automato1.determinizar());
//
//		if (window.ehAutomato1()) {
//			atualizarAutomato();
//			window.rdbtnAutmato2.setSelected(true);
//			atualizarAutomato();
//			window.rdbtnAutmato1.setSelected(true);
//		} else {
//			atualizarAutomato();
//			window.rdbtnAutmato1.setSelected(true);
//			atualizarAutomato();
//			window.rdbtnAutmato2.setSelected(true);
//		}
	}
	
	public void testar(){
		
		String test = "(a|b).a*";
		System.out.println("ER:"+test);
		Run myTest = new Run();
		String postfix = myTest.re2post(test);
		System.out.println("ER postfixed:"+postfix);
		State start = myTest.post2dfa(postfix);
		System.out.println("pos re2postfa"+start.tranform);
}
	
	public void testar4(){
		System.out.println("Aqui");
		automato1 = new Automato();
		automato2 = new Automato();
		String[] alf2 = { "a" };
		String[] alf3 = { "b" };
		automato1.setAlfabeto(alf2);
		automato1.setNome("AF-um");
		Estado estado = new Estado("A");
		estado.setInicial(true);
		automato1.addEstado(estado);

		automato1.addTransicao("A", "A", "a");

		automato1.setEstadoInicial(estado.getNome());
		automato1.setEstadoFinal("A");
		
		
		automato2.setAlfabeto(alf3);
		automato2.setNome("AF-dois");
		Estado estado2 = new Estado("B");
		estado2.setInicial(true);
		automato2.addEstado(estado2);
		automato2.addTransicao("B", "B", "b");

		automato2.setEstadoInicial("B");
		automato2.setEstadoFinal("B");
		
		
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
	
	public void testar3(){
		automato1 = new Automato();
		automato2 = new Automato();
		String[] alf2 = { "a", "b" };
		String[] alf3 = { "a", "b", "c" };
		automato1.setAlfabeto(alf2);
		automato1.setNome("AF-um");
		Estado estado = new Estado("A");
		estado.setInicial(true);
		automato1.addEstado(estado);
		automato1.addEstado("B");
		// automato1.addEstado("C");
		automato1.addTransicao("A", "B", "a");
		// automato1.addTransicao("A", "C", "b");
		automato1.addTransicao("B", "A", "a");
		automato1.addTransicao("B", "B", "b");
		// automato1.addTransicao("C", "C", "a");
		// automato1.addTransicao("C", "B", "b");
		automato1.setEstadoInicial(estado.getNome());
		automato1.setEstadoFinal("B");
		
		
		automato2.setAlfabeto(alf3);
		automato2.setNome("AF-dois");
		Estado estado2 = new Estado("C");
		estado2.setInicial(true);
		automato2.addEstado(estado2);
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
		Estado estado = new Estado("A");
		estado.setInicial(true);
		automato1.addEstado(estado);
		automato1.addEstado("B");
		// automato1.addEstado("C");
		automato1.addTransicao("A", "B", "a");
		// automato1.addTransicao("A", "C", "b");
		automato1.addTransicao("B", "A", "a");
		automato1.addTransicao("B", "B", "b");
		// automato1.addTransicao("C", "C", "a");
		// automato1.addTransicao("C", "B", "b");
		automato1.setEstadoInicial(estado.getNome());
		automato1.setEstadoFinal("B");
		
		
		automato2.setAlfabeto(alf3);
		automato2.setNome("AF-dois");
		Estado estado2 = new Estado("C");
		estado2.setInicial(true);
		automato2.addEstado(estado2);
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
//		Automato a = uniao();
//		a.determinizar();
//		atualizarAutomatoResultado(a);

	}

	@Override
	public void igualdade() {
		// L1 - L2 = !((!L1 U L2) U (!L2 U L1))
		
		// !L1
		Automato complemeto1 = automato1.complemento();
		
		// !L2
		Automato complemeto2 = automato2.complemento();
			
		// !L1 U L2
		Automato uniao1 = uniao(complemeto1, automato2);
		uniao1.determinizar();
		
		// !L2 U L1
		Automato uniao2 = uniao(complemeto2, automato1);
		uniao2.determinizar();
		
		// (!L1 U L2) U (!L2 U L1)
		Automato uniao = uniao(uniao1, uniao2);
		Automato determinizado = uniao.determinizar();
		
		// !((!L1 U L2) U (!L2 U L1))
		Automato complemento = determinizado.complemento();
		
		String message = "";
		boolean ehVazio = complemento.ehVazio();
		if (ehVazio) {
			message += "Sim! L1 e L2 são iguais!";
		} else {
			message += "Não! L1 e L2 diferentes!";
		}
		window.alert(message);
	}

	@Override
	public void interseccao() {
		// TODO Auto-generated method stub
		determinizarTest();
	}

	@Override
	public void diferenca() {
		// L1 - L2 = !((!L1 U L2) U (!L2 U L1))
		
		// !L1
		Automato complemeto1 = automato1.complemento();
		
		// !L2
		Automato complemeto2 = automato2.complemento();
			
		// !L1 U L2
		Automato uniao1 = uniao(complemeto1, automato2);
		uniao1.determinizar();
		
		// !L2 U L1
		Automato uniao2 = uniao(complemeto2, automato1);
		uniao2.determinizar();
		
		// (!L1 U L2) U (!L2 U L1)
		Automato uniao = uniao(uniao1, uniao2);
		Automato determinizado = uniao.determinizar();
		
		// !((!L1 U L2) U (!L2 U L1))
		Automato complemento = determinizado.complemento();
		
		atualizarAutomatoResultado(complemento);
	}

	@Override
	public void ocorrencias() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enumerar(int num) {
		Automato automato = getAutomato();
		LinkedList<String> sentencas = automato.getSentencas(num,
				automato.getEstadoInicial());
		String retorno = sentencas.size() + " sentenças de tamanho " + num
				+ " \n";
		for (String sAtual : sentencas) {
			retorno += sAtual + " - ";
		}
		window.setResultado(retorno);
	}

	//

	// XXX: E se tiver estados com nomes iguais?
	private Automato uniao(Automato clone1, Automato clone2) {
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
