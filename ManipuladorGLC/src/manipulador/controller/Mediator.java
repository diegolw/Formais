package manipulador.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.swing.JTextField;

import manipulador.model.Constants;
import manipulador.model.GramaticaLivreContexto;
import manipulador.model.NaoTerminal;
import manipulador.model.Producao;
import manipulador.model.Terminal;
import manipulador.model.Termo;
import manipulador.view.GUI;
import manipulador.view.IGUI;

public class Mediator implements IGUI {

	ArrayList<GramaticaLivreContexto> gramaticas = new ArrayList<>();
	GUI gui = null;

	public Mediator(GUI window) {
		gui = window;
		gui.addListener(this);

		// S -> ABC
		// A -> aA | &
		// B -> bB | ACd
		// C -> cC | &

		// GramaticaLivreContexto glc = new GramaticaLivreContexto();
		// glc.setNome("abc");
		// //TERMINAIS
		//
		// //a
		// ArrayList<Character> a0 = new ArrayList<Character>();
		// a0.add('a');
		// Terminal a = new Terminal(a0);
		//
		// //b
		// ArrayList<Character> b0 = new ArrayList<Character>();
		// b0.add('b');
		// Terminal b = new Terminal(b0);
		//
		// //c
		// ArrayList<Character> c0 = new ArrayList<Character>();
		// c0.add('c');
		// Terminal c = new Terminal(c0);
		//
		// //d
		// ArrayList<Character> d0 = new ArrayList<Character>();
		// d0.add('d');
		// Terminal d = new Terminal(d0);
		//
		// //epsilon
		// Terminal epsilon = new Terminal(Constants.epsilon);
		//
		// //NAO-TERMINAIS
		//
		// //S
		// ArrayList<Character> S0 = new ArrayList<Character>();
		// S0.add('S');
		// NaoTerminal S = new NaoTerminal(S0, true);
		//
		// //A
		// ArrayList<Character> A0 = new ArrayList<Character>();
		// A0.add('A');
		// NaoTerminal A = new NaoTerminal(A0, false);
		//
		// //B
		// ArrayList<Character> B0 = new ArrayList<Character>();
		// B0.add('B');
		// NaoTerminal B = new NaoTerminal(B0, false);
		//
		// //C
		// ArrayList<Character> C0 = new ArrayList<Character>();
		// C0.add('C');
		// NaoTerminal C = new NaoTerminal(C0, false);
		//
		// //D
		// ArrayList<Character> D0 = new ArrayList<Character>();
		// D0.add('D');
		// NaoTerminal D = new NaoTerminal(D0, false);
		//
		//
		// // PRODUCOES
		// // 0
		// ArrayList<Termo> producao00 = new ArrayList<Termo>();
		// producao00.add(A);
		// producao00.add(B);
		// producao00.add(C);
		// Producao producao0 = new Producao(producao00);
		//
		// // 1
		// ArrayList<Termo> producao11 = new ArrayList<Termo>();
		// producao11.add(a);
		// producao11.add(A);
		// Producao producao1 = new Producao(producao11);
		//
		// // 2
		// ArrayList<Termo> producao22 = new ArrayList<Termo>();
		// producao22.add(epsilon);
		// Producao producao2 = new Producao(producao22);
		//
		// // 3
		// ArrayList<Termo> producao33 = new ArrayList<Termo>();
		// producao33.add(b);
		// producao33.add(B);
		// Producao producao3 = new Producao(producao33);
		//
		// // 4
		// ArrayList<Termo> producao44 = new ArrayList<Termo>();
		// producao44.add(A);
		// producao44.add(C);
		// producao44.add(d);
		// Producao producao4 = new Producao(producao44);
		//
		// // 5
		// ArrayList<Termo> producao55 = new ArrayList<Termo>();
		// producao55.add(c);
		// producao55.add(C);
		// Producao producao5 = new Producao(producao55);
		//
		// // 6
		// ArrayList<Termo> producao66 = new ArrayList<Termo>();
		// producao66.add(epsilon);
		// Producao producao6 = new Producao(producao66);
		//
		// // Adicionando as producoes a gramatica
		// glc.adicionarProducao(S, producao0);
		// glc.adicionarProducao(A, producao1);
		// glc.adicionarProducao(A, producao2);
		// glc.adicionarProducao(B, producao3);
		// glc.adicionarProducao(B, producao4);
		// glc.adicionarProducao(C, producao5);
		// glc.adicionarProducao(C, producao6);
		//
		// glc.setNumNaoTerminais(4);
		//
		// gramaticas.add(glc);

		carregarDadosLocalmente();
	}

	public void validarGramatica() {
		GramaticaLivreContexto glc = new GramaticaLivreContexto();
		glc.setNome("abc");

		// TERMINAIS

		// a
		ArrayList<Character> a0 = new ArrayList<Character>();
		a0.add('a');
		Terminal a = new Terminal(a0);

		// b
		ArrayList<Character> b0 = new ArrayList<Character>();
		b0.add('b');
		Terminal b = new Terminal(b0);

		// c
		ArrayList<Character> c0 = new ArrayList<Character>();
		c0.add('c');
		Terminal c = new Terminal(c0);

		// d
		ArrayList<Character> d0 = new ArrayList<Character>();
		d0.add('d');
		Terminal d = new Terminal(d0);

		// epsilon
		Terminal epsilon = new Terminal(Constants.epsilon);

		// NAO-TERMINAIS

		// S
		ArrayList<Character> S0 = new ArrayList<Character>();
		S0.add('S');
		NaoTerminal S = new NaoTerminal(S0, true);

		// A
		ArrayList<Character> A0 = new ArrayList<Character>();
		A0.add('A');
		NaoTerminal A = new NaoTerminal(A0, false);

		// B
		ArrayList<Character> B0 = new ArrayList<Character>();
		B0.add('B');
		NaoTerminal B = new NaoTerminal(B0, false);

		// C
		ArrayList<Character> C0 = new ArrayList<Character>();
		C0.add('C');
		NaoTerminal C = new NaoTerminal(C0, false);

		// D
		ArrayList<Character> D0 = new ArrayList<Character>();
		D0.add('D');
		NaoTerminal D = new NaoTerminal(D0, false);

		// PRODUCOES
		// 0
		ArrayList<Termo> producao00 = new ArrayList<Termo>();
		producao00.add(A);
		producao00.add(B);
		producao00.add(C);
		Producao producao0 = new Producao(producao00);

		// 1
		ArrayList<Termo> producao11 = new ArrayList<Termo>();
		producao11.add(a);
		producao11.add(A);
		Producao producao1 = new Producao(producao11);

		// 2
		ArrayList<Termo> producao22 = new ArrayList<Termo>();
		producao22.add(epsilon);
		Producao producao2 = new Producao(producao22);

		// 3
		ArrayList<Termo> producao33 = new ArrayList<Termo>();
		producao33.add(b);
		producao33.add(B);
		Producao producao3 = new Producao(producao33);

		// 4
		ArrayList<Termo> producao44 = new ArrayList<Termo>();
		producao44.add(A);
		producao44.add(C);
		producao44.add(d);
		Producao producao4 = new Producao(producao44);

		// 5
		ArrayList<Termo> producao55 = new ArrayList<Termo>();
		producao55.add(c);
		producao55.add(C);
		Producao producao5 = new Producao(producao55);

		// 6
		ArrayList<Termo> producao66 = new ArrayList<Termo>();
		producao66.add(epsilon);
		Producao producao6 = new Producao(producao66);

		// Adicionando as producoes a gramatica
		glc.adicionarProducao(S, producao0);
		glc.adicionarProducao(A, producao1);
		glc.adicionarProducao(A, producao2);
		glc.adicionarProducao(B, producao3);
		glc.adicionarProducao(B, producao4);
		glc.adicionarProducao(C, producao5);
		glc.adicionarProducao(C, producao6);

		glc.setNumNaoTerminais(4);

		gramaticas.add(glc);
	}

	@Override
	public void adicionar(String nome) {
		GramaticaLivreContexto gramatica = new GramaticaLivreContexto(nome);
		gramaticas.add(gramatica);
	}

	@Override
	public void ler(String nome) {
		GramaticaLivreContexto glc = new GramaticaLivreContexto();
		glc.setNumNaoTerminais(3);
		NaoTerminal naoTerminal = new NaoTerminal();
		Producao producao = null;
		glc.adicionarProducao(naoTerminal, producao);
	}

	@Override
	public void salvar() {
		// TODO Auto-generated method stub

	}

	public GramaticaLivreContexto getGramatica(String nome) {
		for (GramaticaLivreContexto gramatica : gramaticas) {
			if (gramatica.getNome().equals(nome)) {
				return gramatica;
			}
		}
		return null;
	}

	@Override
	public void validar(String nome, ArrayList<String> producoes) {
		GramaticaLivreContexto gramatica = getGramatica(nome);

		int cont = producoes.size() / 2;
		for (int i = 1; i < cont; i++) {
			String nomeNaoTerminal = producoes.get(i - 1);
			String listaDerivacoes = producoes.get(i);

			// Cria um não terminal
			ArrayList<Character> producao = new ArrayList<Character>();
			for (char c : nomeNaoTerminal.toCharArray()) {
				producao.add(c);
			}
			NaoTerminal naoTerminal = new NaoTerminal(producao);
			if (i == 1) {
				naoTerminal.setInicial(true);
			}

			ArrayList<Termo> termosDerivacao;
			// Para cada derivação do não terminal cria uma produção
			String derivacoes[] = listaDerivacoes.split("\\|");
			for (String derivacao : derivacoes) {

				String prodc[] = derivacao.trim().split(" ");

				termosDerivacao = new ArrayList<Termo>();

				for (String pr : prodc) {
					boolean ehNaoTerminal = false;

					ArrayList<Character> list = new ArrayList<Character>();
					for (char c : pr.toCharArray()) {
						list.add(c);

						if (Character.isUpperCase(c))
							ehNaoTerminal = true;
					}

					if (ehNaoTerminal) {
						NaoTerminal meuNt = new NaoTerminal(list);
						termosDerivacao.add(meuNt);
					}

					else {
						Terminal t = new Terminal(list);
						termosDerivacao.add(t);
					}

				}

				if (termosDerivacao != null) {
					Producao prodFinalPronta = new Producao(termosDerivacao);
					gramatica.adicionarProducao(naoTerminal, prodFinalPronta);
				}
			}
		}
	}

	@Override
	public void fatorar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void propria() {
		// TODO Auto-generated method stub

	}

	@Override
	public void eliminarRecursao() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ehVaziaFinitaInfinita() {
		// TODO Auto-generated method stub

	}

	@Override
	public void exibir(String nome) {
		GramaticaLivreContexto gramatica = null;
		for (GramaticaLivreContexto glc : gramaticas) {
			if (glc.getNome().equals(nome)) {
				gramatica = glc;
			}
		}

		// limparTabelaParsing();
		// GramaticaLivreContexto glc = gramaticas.get(pos);
		// limparGramatica();
		// int numLinhas = glc.getProducoes().size();
		int numLinhas = gramatica.getNumNaoTerminais();
		// preparaParaNovaGLC(numLinhas);

		int i = 0;

		ArrayList<String> producoes = new ArrayList<>();
		for (Map.Entry<NaoTerminal, Set<Producao>> entry : gramatica
				.getProducoes().entrySet()) {
			JTextField naoTerminal;
			// naoTerminal = (JTextField) jPanel1.getComponent(i);
			String nomeNaoTerminal = entry.getKey().toString();
			// naoTerminal.setText(nomeNaoTerminal);
			producoes.add(nomeNaoTerminal);

			// JTextField derivacoes;
			// derivacoes = (JTextField) jPanel1.getComponent(i+2);
			String conjuntoDeProducoes = entry.getValue().toString();
			conjuntoDeProducoes = conjuntoDeProducoes.replace('[', ' ');
			conjuntoDeProducoes = conjuntoDeProducoes.replace(']', ' ');
			conjuntoDeProducoes = conjuntoDeProducoes.replace(',', '|');
			// derivacoes.setText(conjuntoDeProducoes);

			producoes.add(conjuntoDeProducoes);

			i = i + 3;
		}

		gui.exibirGramatica(numLinhas, producoes);

		// jPanel1.validate();
		// jPanel1.repaint();
		//
	}

	public void carregarDadosLocalmente() {
		try {

			FileInputStream glcFile = new FileInputStream("GLC.dat");
			ObjectInputStream gramaticasO = new ObjectInputStream(glcFile);

			gramaticas = (ArrayList<GramaticaLivreContexto>) gramaticasO
					.readObject();

			gramaticasO.close();

			for (GramaticaLivreContexto gramatica : gramaticas) {
				// list1.add(gramatica.getNome());
				gui.inserirGramatica(gramatica.getNome());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void salvarDadosLocalmente() {
		try {

			if (gramaticas.size() > 0) {
				FileOutputStream glcFile = new FileOutputStream("GLC.dat");
				ObjectOutputStream gramaticas0 = new ObjectOutputStream(glcFile);

				gramaticas0.writeObject(gramaticas);

				gramaticas0.flush();
				gramaticas0.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
