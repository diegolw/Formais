package manipulador.model;
import manipulador.model.gramatica.*;
import java.util.LinkedList;

//Classe que representa um estado Não deterministico -  que será usado para fazer a determinização do automato
public class EstadoND {						// A-> aX | aY
	private LinkedList<Estado> estados;		// == <X,Y>
	private Estado origemIndeterminizacao; //  == A 
	
	protected EstadoND() {
		setEstados(null);
		setOrigemIndeterminizacao(null);
	}
		
	protected EstadoND(LinkedList<Estado> _es,Estado or) {
		setEstados(_es);
		setOrigemIndeterminizacao(or);
	}
	public void addEstado(Estado es){
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
