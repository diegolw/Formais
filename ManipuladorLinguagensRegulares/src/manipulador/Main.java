package manipulador;

import java.awt.EventQueue;

import manipulador.controller.Mediator;
import manipulador.model.*; //teste
import manipulador.model.gramatica.*;

import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class Main {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mediator mediator = new Mediator();
					
					AF af1= new AF();
					af1.setNome("AF1");
					Estado q0 = new Estado("q0",af1);
					Estado q1 = new Estado("q1",af1);
					Estado q2 = new Estado("q2",af1);
					Estado q3 = new Estado("q3",af1);
					
					Terminal a = new Terminal("a");
					Terminal b =new Terminal("b");
					
					LinkedList<Estado> K = new LinkedList<Estado>();
					K.add(q0);
					q0.setInicial(true);
					K.add(q1);
					K.add(q2);
					K.add(q3);
					LinkedList<Terminal> E = new LinkedList<Terminal>();
					E.add( a);
					E.add( b);
					LinkedList<Transicao> MP = new LinkedList<Transicao>();
					MP.add(new Transicao(q0,q0,a));
					MP.add(new Transicao(q0,q1,a));
					MP.add(new Transicao(q0,q0,b));
					q0.addTransicao(q0, a.getTerminal());
					q0.addTransicao(q1, a.getTerminal());
					q0.addTransicao(q0, b.getTerminal());					
					MP.add(new Transicao(q1,q2,b));
					q1.addTransicao(q2, b.getTerminal());
					MP.add(new Transicao(q2,q3,b));
					q2.addTransicao(q3, b.getTerminal());
					LinkedList<Estado> F = new LinkedList<Estado>();
					F.add(q3);
					af1.setNome("AF1");
					af1.setK(K);
					af1.setE(E);
					af1.setMP(MP);
					af1.setQ0(q0);
					af1.setF(F);

					Iterator<Transicao> it = af1.getMP().iterator();
					String att="AF1:\n";
					while(it.hasNext()){
						Transicao t = it.next();
						att += "Estado Origem:"+t.getOrigem().getEstado()+" Simbolo:"+t.getSimbolo().getTerminal()+" Estado Destino:"+t.getDestino().getEstado()+"\n";
					}
					//JOptionPane.showMessageDialog(null, att);
					boolean rt = af1.ehDeterministico();
					att += "\nAF1 e deterministico?"+rt;

					
					AF af2 = af1.determinizar();
			
					it = af2.getMP().iterator();
					att +="\nAF2:\n";
					while(it.hasNext()){
						Transicao t = it.next();
						att += "Estado Origem:"+t.getOrigem().getEstado()+" Simbolo:"+t.getSimbolo().getTerminal()+" Estado Destino:"+t.getDestino().getEstado()+"\n";
					}
					
					
					rt = af2.ehDeterministico();
					att +="\nAF2 e deterministico?"+rt;
					JOptionPane.showMessageDialog(null, att);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
