package manipulador.model;
import manipulador.model.gramatica.*;

import java.util.Iterator;
import java.util.LinkedList;

//Classe que representa um estado Não deterministico -  que será usado para fazer a determinização do automato
public class EstadoND {						// A-> aX | aY
	private LinkedList<Estado> estados;		// == <X,Y>
	private Estado origemIndeterminizacao; //  == A 
	
	protected EstadoND() {
		estados = new LinkedList<Estado>();
		setOrigemIndeterminizacao(null);
	}
		
//	protected EstadoND(LinkedList<Estado> _es,Estado or) {
//		setEstados(_es);
//		setOrigemIndeterminizacao(or);
//	}
	
	public EstadoND(Estado ei){
		estados = new LinkedList<Estado>();
		estados.add(ei);
	}
	
	public boolean ehIgual(EstadoND eaux){
		Iterator<Estado> it = estados.iterator();
		while(it.hasNext())
			if(!eaux.possui(it.next()))
				return false;
		it = eaux.estados.iterator();
		while(it.hasNext())
			if(!possui(it.next()))
				return false;
		
		return true;
	}
	
	public void setEstadoAssociadoParaFinalCasoAlgumEstadoInclusoSejaTambem(){
		Iterator<Estado> it = estados.iterator();
		if(origemIndeterminizacao != null){
			origemIndeterminizacao.setFinal(false);
			while(it.hasNext()){
				if(it.next().isFinal())
					origemIndeterminizacao.setFinal(true);
			}
		}
	}
	
	public void setEstadoAssociado(Estado e){
		origemIndeterminizacao = e;
		setEstadoAssociadoParaFinalCasoAlgumEstadoInclusoSejaTambem();
		setEstadoAssociadoParaInicialCasoOPrimeiroEstadoInclusoTambemSeja();
	}
	
	private void setEstadoAssociadoParaInicialCasoOPrimeiroEstadoInclusoTambemSeja() {
		if(estados.size() == 1){
			Estado e = estados.get(0);
			if(e.isInicial())
				origemIndeterminizacao.setInicial(true);				
		}
	}
	
	public Estado[] getEstadosInclusos(){
		Iterator<Estado> it = estados.iterator();
		Estado[] es = new Estado[estados.size()];
		int cont = 0;
		while(it.hasNext()){
			es[cont] = it.next();
			cont++;
		}
		return es;
	}
	
	public boolean possui(Estado e){
		Iterator<Estado> it = estados.iterator();
		while(it.hasNext())
			if(e == it.next())
				return true;
		
		return false;
	}
	
	public void addEstado(Estado es){
		if(!possui(es))
			estados.add(es);
	}

	public LinkedList<Estado> getEstados() {
		return estados;
	}

	public void setEstados(LinkedList<Estado> estados) {
		this.estados = estados;
	}

	public Estado getOrigemIndeterminizacao() {
		return origemIndeterminizacao;
	}

	public void setOrigemIndeterminizacao(Estado origemIndeterminizacao) {
		this.origemIndeterminizacao = origemIndeterminizacao;
	}

}
