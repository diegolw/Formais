package manipulador.model;
import java.util.Iterator;
import java.util.LinkedList;

public class Estado {
	
	private Automato automatoPai;
	private String nome;
	private LinkedList<Transicao> transicoes;
	private boolean ehEstadoFinal;
	
	protected Estado(String n, Automato ap){
		automatoPai = ap;
		nome = n;
		transicoes = new LinkedList<Transicao>();
		ehEstadoFinal = false;
	}
	
        public void setNome(String n){
            this.nome = n;
        }
	public boolean ehEstadoFinal(){
		return ehEstadoFinal;
	}
	
	public boolean ehEstadoInicial() {
		return automatoPai.getEstadoInicial() == this;
	}
	
	protected void seTorneEstadoInicial(){
		automatoPai.setEstadoInicial(this);
	}
	
	protected void setFinal(boolean b) {
		ehEstadoFinal = b;	
	}
	
	public String getNome(){
		return nome;
	}

	protected Automato getAutomatoPai() {
		return automatoPai;
	}
	
	protected void addTransicao(Estado estadoDestino, String simbolo){
		if(!existeTransicao(estadoDestino,simbolo)){
			Transicao t = new Transicao(this, estadoDestino, simbolo);
			transicoes.add(t);
		}
	}
	
	protected boolean existeTransicao(Estado estadoDestino, String simbolo){
		Iterator<Transicao> it = transicoes.iterator();
		while(it.hasNext()){
			Transicao t = it.next();
			if(t.getEstadoDestino() == estadoDestino && t.getSimbolo().equals(simbolo))
				return true;
		}
		return false;
	}
	
	public String toString(){
		return nome;
	}

	public Transicao[] getTransicoes() {
		Iterator<Transicao> it = transicoes.iterator();
		Transicao[] arrTransicoes = new Transicao[transicoes.size()];
		int cont = 0;
		while(it.hasNext()){
			arrTransicoes[cont] = it.next();
			cont++;
		}
		return arrTransicoes;
	}
	
	public Transicao[] getTransicoesDoSimbolo(String s){
		LinkedList<Transicao> listaTemp = new LinkedList<Transicao>();
		Iterator<Transicao> it = transicoes.iterator();
		while(it.hasNext()){
			Transicao t = it.next();
			if(t.getSimbolo().equals(s))
				listaTemp.add(t);
		}
		Transicao[] arrTransicoes = new Transicao[listaTemp.size()];
		it = listaTemp.iterator();
		int cont = 0;
		while(it.hasNext()){
			arrTransicoes[cont] = it.next();
			cont++;
		}
		
		return arrTransicoes;		
	}
	
	public Transicao[] getTransicoesParaSiMesmo(){
		LinkedList<Transicao> transicoesParaSiMesmo = new LinkedList<Transicao>();
		Iterator<Transicao> it = transicoes.iterator();
		while(it.hasNext()){
			Transicao t = it.next();
			if(t.getEstadoDestino() == this)
				transicoesParaSiMesmo.add(t);
		}
		Transicao[] ts = new Transicao[transicoesParaSiMesmo.size()];
		it = transicoesParaSiMesmo.iterator();
		for(int i = 0 ; i < ts.length ; i++)
			ts[i] = it.next();
		
		return ts;
	}
	
	public Transicao[] getTransicoesParaOutroEstado(){
		LinkedList<Transicao> transicoesOutroEstado = new LinkedList<Transicao>();
		Iterator<Transicao> it = transicoes.iterator();
		while(it.hasNext()){
			Transicao t = it.next();
			if(t.getEstadoDestino() != this)
				transicoesOutroEstado.add(t);
		}
		Transicao[] ts = new Transicao[transicoesOutroEstado.size()];
		it = transicoesOutroEstado.iterator();
		for(int i = 0 ; i < ts.length ; i++)
			ts[i] = it.next();
		
		return ts;
	}

	protected void removeTransicoesParaOEstado(Estado e) {
		Transicao[] ts = getTransicoes();
		for(int i = 0 ; i < ts.length ; i++)
			if(ts[i].getEstadoDestino() == e){
				transicoes.remove(ts[i]);
			}
		
	}

	protected Transicao[] getTransicoesQueChegam() {
		LinkedList<Transicao> transicoesRet = new LinkedList<Transicao>();
		Estado[] estados = automatoPai.getEstados();
		for(int i = 0 ; i < estados.length ; i++){
			Transicao[] transicoes = estados[i].getTransicoes();
			for(int j = 0 ; j < transicoes.length ; j++)
				if(transicoes[j].getEstadoDestino() == this)
					transicoesRet.add(transicoes[j]);
			
		}
		
		Transicao[] ts = new Transicao[transicoesRet.size()];
		Iterator<Transicao> it = transicoesRet.iterator();
		for(int i = 0 ; i < ts.length ; i++)
			ts[i] = it.next();
		
		return ts;
	}

    public boolean getFinal() {
        return ehEstadoFinal;
    }

	

}