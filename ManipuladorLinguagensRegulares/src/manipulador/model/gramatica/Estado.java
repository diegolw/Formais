package manipulador.model.gramatica;
import manipulador.model.*;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class Estado implements Serializable{
	private String estado;
	private AF afPai;
	private LinkedList<Transicao> transicoes;
	private boolean isfinal;

	public Estado(String _est, AF afp){
		setEstado(_est);
		setFinal(false);
		afPai = afp;
		transicoes = new LinkedList<Transicao>();
	}

	public void addTransicao(Estado estadoDestino, String simbolo){
		if(!existeTransicao(estadoDestino,simbolo)){
			Transicao t = new Transicao(this, estadoDestino, new Terminal(simbolo));
			transicoes.add(t);
		}
	}
	
	protected boolean existeTransicao(Estado estadoDestino, String simbolo){
		Iterator<Transicao> it = transicoes.iterator();
		while(it.hasNext()){
			Transicao t = it.next();
			if(t.getDestino() == estadoDestino && t.getSimbolo().equals(simbolo))
				return true;
		}
		return false;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public boolean isFinal() {
		return isfinal;
	}

	public void setFinal(boolean isfinal) {
		this.isfinal = isfinal;
	}

	public boolean isInicial() {
		if(afPai.getQ0()!=null)
			return (afPai.getQ0().getEstado() == this.getEstado());
		else return false;
	}

	public void setInicial(boolean isInicial) {
		afPai.setQ0(this);
	}

	public AF getAFPai() {
		return afPai;
	}

	public void setAFPai(AF afPai) {
		this.afPai = afPai;
	}

	public LinkedList<Transicao> getTransicoes() {
		return transicoes;
	}

	public void setTransicoes(LinkedList<Transicao> transicoes) {
		this.transicoes = transicoes;
	}
	
	public LinkedList<Transicao> getTransicoes(Terminal s){
		LinkedList<Transicao> listaTemp = new LinkedList<Transicao>();
		Iterator<Transicao> it = transicoes.iterator();
		//JOptionPane.showMessageDialog(null, " transicoes.size:"+transicoes.size());
		while(it.hasNext()){
			Transicao t = it.next();
			//JOptionPane.showMessageDialog(null, " Transição:"+t.getOrigem().getEstado()+"->"+t.getSimbolo().getTerminal()+"->"+t.getDestino().getEstado());
			if(t.getSimbolo().getTerminal() == s.getTerminal()){
				listaTemp.add(t);
			}
		}
				
		return listaTemp;	
	}
	
	public LinkedList<Transicao> getTransicoesParaSiMesmo(){
		LinkedList<Transicao> transicoesParaSiMesmo = new LinkedList<Transicao>();
		Iterator<Transicao> it = transicoes.iterator();
		while(it.hasNext()){
			Transicao t = it.next();
			if(t.getDestino() == this)
				transicoesParaSiMesmo.add(t);
		}

		
		return transicoesParaSiMesmo;
	}
	
	public LinkedList<Transicao> getTransicoesParaOutroEstado(){
		LinkedList<Transicao> transicoesOutroEstado = new LinkedList<Transicao>();
		Iterator<Transicao> it = transicoes.iterator();
		while(it.hasNext()){
			Transicao t = it.next();
			if(t.getDestino() != this)
				transicoesOutroEstado.add(t);
		}

		
		return transicoesOutroEstado;
	}
	
	public LinkedList<Transicao> getTransicoesQueChegam() {
		LinkedList<Transicao> transicoesRet = new LinkedList<Transicao>();
		LinkedList<Estado> estados = afPai.getK();
		for(int i = 0 ; i < estados.size() ; i++){
			LinkedList<Transicao> transicoes = estados.get(i).getTransicoes();
			for(int j = 0 ; j < transicoes.size() ; j++)
				if(transicoes.get(j).getDestino() == this)
					transicoesRet.add(transicoes.get(j));
			
		}

		
		return transicoesRet;
	}
	
	public void removeTransicoesParaOEstado(Estado e) {
		LinkedList<Transicao> ts = getTransicoes();
		for(int i = 0 ; i < ts.size() ; i++)
			if(ts.get(i).getDestino() == e){
				transicoes.remove(ts.get(i));
			}
		
	}
	
	public String toString(){
		return estado;
	}
	

}
