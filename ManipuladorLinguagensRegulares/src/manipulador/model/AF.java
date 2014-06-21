package manipulador.model;

import java.util.LinkedList;
import java.util.Iterator;

import manipulador.model.gramatica.Estado;
import manipulador.model.gramatica.Terminal;
import manipulador.model.gramatica.Transicao;

public class AF {
	private String nome;
	private LinkedList<Estado> K = null;		//conjunto não vazio de estados
	private LinkedList<Terminal> E = null;		//Alfabeto de entrada		
	private LinkedList<Transicao> MP = null;	//Função de Mapeamento
	private Estado q0;							//Estado inicial
	private LinkedList<Estado> F=null;			//Conjunto de Estados Finais
	
	protected AF(){
		 K = null;		
		 E = null;				
		 MP = null;	
		 q0 =null;	
		 F=null;
		
	}
	protected AF(LinkedList<Estado> _K, LinkedList<Terminal> _E, LinkedList<Transicao> _MP,Estado _q0,LinkedList<Estado> _F){
		 K = _K;		
		 E = _E;				
		 MP =_MP;	
		 q0 =_q0;	
		 F= _F;		
	}
	
	public void addEstado(Estado _est){
		K.add(_est);
	}
	public boolean removeEstado(Estado _est){
		Iterator<Estado> it;
		boolean removeu = false;
		if(_est!=null){
			 it = K.iterator();
			 while (it.hasNext()) {
		            //it.next().removeTransicoesParaOEstado(e);
		        }
				removeu = K.remove(_est);
		        if (_est == q0) {
		            q0 = null;
		        }
		}
		return removeu;
		
	}
	
	public LinkedList<Transicao> getTransicoes(Terminal t){
		LinkedList<Transicao> listaTemp = new LinkedList<Transicao>();
		Iterator<Transicao> it = MP.iterator();
		while(it.hasNext()){
			Transicao tt = it.next();
			if(tt.getSimbolo().getTerminal() == t.getTerminal())
				listaTemp.add(tt);
		}
			
		return listaTemp;		
	}
	
	public AF determinizar(){
		LinkedList<EstadoND[]> estadosDeterminizados = new LinkedList<EstadoND[]>();
		LinkedList<EstadoND> tratar = new LinkedList<EstadoND>();
		EstadoND aux = new EstadoND();
		aux.addEstado(this.q0);
		tratar.add(aux);
		while (!tratar.isEmpty()){
			EstadoND[] novo = new EstadoND[E.size()+1]; 
			novo[0] = tratar.pop();
			estadosDeterminizados.add(novo);
			//continuar aqui
		}
		AF ret = new AF();
		return ret;
	}
	
	public LinkedList<Estado> getK() {
		return K;
	}
	public void setK(LinkedList<Estado> k) {
		K = k;
	}
	public LinkedList<Terminal> getE() {
		return E;
	}
	public void setE(LinkedList<Terminal> e) {
		E = e;
	}
	public LinkedList<Estado> getF() {
		return F;
	}
	public void setF(LinkedList<Estado> f) {
		F = f;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public LinkedList<Transicao> getMP() {
		return MP;
	}
	public void setMP(LinkedList<Transicao> mP) {
		MP = mP;
	}
	public Estado getQ0() {
		return q0;
	}
	public void setQ0(Estado q0) {
		this.q0 = q0;
	}

}
