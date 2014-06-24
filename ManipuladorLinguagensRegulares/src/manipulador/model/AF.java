package manipulador.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import manipulador.model.gramatica.Estado;
import manipulador.model.gramatica.Terminal;
import manipulador.model.gramatica.Transicao;

public class AF implements Serializable{
	private String nome;
	private LinkedList<Estado> K = null;		//conjunto não vazio de estados
	private LinkedList<Terminal> E = null;		//Alfabeto de entrada		
	private LinkedList<Transicao> MP = null;	//Função de Mapeamento
	private Estado q0;							//Estado inicial
	private LinkedList<Estado> F=null;			//Conjunto de Estados Finais
	
	public AF(){
		 K = new LinkedList<Estado>();		
		 E = new LinkedList<Terminal>();				
		 MP = new LinkedList<Transicao>();	
		 q0 = null;	
		 F = new LinkedList<Estado>();				
	}
	public AF(String _nome,LinkedList<Estado> _K, LinkedList<Terminal> _E, LinkedList<Transicao> _MP,Estado _q0,LinkedList<Estado> _F){
		nome = _nome;
		K = _K;		
		 E = _E;				
		 MP =_MP;	
		 q0 =_q0;	
		 F= _F;		
	}

	
	public void addEstado(Estado _est){
        if (!existeEstado(_est.getEstado())) {
        	K.add(_est);
        } else {
            String nomeEst = _est.getEstado();
            while (existeEstado(nomeEst)) {
                nomeEst += "\'";
            }
            _est.setEstado(nomeEst);
            K.add(_est);
        }
	}
	
    public boolean existeEstado(String n) {
        	Estado e = getEstado(n);
            if (e.getEstado() == n) {
                return true;
            }
        return false;
    }
	
	public boolean removeEstado(Estado _est){
		Iterator<Estado> it;
		boolean removeu = false;
		if(_est!=null){
			 it = K.iterator();
			 while (it.hasNext()) {
		            it.next().removeTransicoesParaOEstado(_est);
		        }
				removeu = K.remove(_est);
		        if (_est == q0) {
		            q0 = null;
		        }
		}
		return removeu;
		
	}
	
    public boolean ehDeterministico() {
        Iterator<Estado> it = K.iterator();
        while (it.hasNext()) {
            Estado e = it.next();
            for (int i = 0; i < E.size(); i++) {
            	LinkedList<Transicao> ts = e.getTransicoes(E.get(i));
                if (ts.size() > 1) {
                    return false;
                }
            }
        }
        return true;
    }
	
	public AF determinizar(){
		if(this.ehDeterministico()){
			JOptionPane.showMessageDialog(null, "Nao determ.");
			return new AF();
		}
		if (q0 == null) {
            return null;
        }
		
		LinkedList<EstadoND[]> estadosDeterminizados = new LinkedList<EstadoND[]>();
		LinkedList<EstadoND> tratar = new LinkedList<EstadoND>();
		EstadoND aux = new EstadoND();
		aux.addEstado(this.q0);
		tratar.push(aux);
		while (!tratar.isEmpty()){
			EstadoND[] novo = new EstadoND[E.size()+1]; 
			novo[0] = tratar.pop();
			if(novo[0].getEstados().size()>0)
				estadosDeterminizados.add(novo);
			tratarLinha(estadosDeterminizados, novo, tratar);
			
		}
        AF automatoDeterminizado = new AF();
        automatoDeterminizado.setNome(nome + " determinizado");
        automatoDeterminizado.setE(E);
        
        Iterator<EstadoND[]> itLinhaDaTabela = estadosDeterminizados.iterator();
        int cont = 0;
        while (itLinhaDaTabela.hasNext()) {
            Estado novoEstadoDeterminizado = new Estado("q" + cont, automatoDeterminizado);
            automatoDeterminizado.K.add(novoEstadoDeterminizado);
            itLinhaDaTabela.next()[0].setEstadoAssociado(novoEstadoDeterminizado);
            cont++;
        }
        itLinhaDaTabela = estadosDeterminizados.iterator();
        while (itLinhaDaTabela.hasNext()) {
            EstadoND[] linha = itLinhaDaTabela.next();
            for (int i = 0; i < E.size(); i++) {
                automatoDeterminizado.addTransicao(linha[0].getOrigemIndeterminizacao(), linha[i + 1].getOrigemIndeterminizacao(), E.get(i).getTerminal());
            }
        }		
		return automatoDeterminizado;
	}
	
    private void tratarLinha(LinkedList<EstadoND[]> estadosDeterminizados, EstadoND[] novo, LinkedList<EstadoND> tratar) {
        for (int i = 1; i < novo.length; i++) {
            novo[i] = new EstadoND();
            Estado[] estadosInclusosDoEstadoDeterminizado = novo[0].getEstadosInclusos();
            for (int j = 0; j < estadosInclusosDoEstadoDeterminizado.length; j++) {
                Estado estadoInclusoDoEstadoDeterminizado = estadosInclusosDoEstadoDeterminizado[j];
                LinkedList<Transicao> transicoesDoEstadoIncluso = estadoInclusoDoEstadoDeterminizado.getTransicoes(E.get(i - 1));
                for (int k = 0; k < transicoesDoEstadoIncluso.size(); k++) {
                    novo[i].addEstado(transicoesDoEstadoIncluso.get(k).getDestino());
                }
            }

            EstadoND novoEstadoDeterminizado = getEstadoDeterminizadoDaTabela(novo[i], estadosDeterminizados);
            if (novoEstadoDeterminizado != null) {
                novo[i] = novoEstadoDeterminizado;
            } else {
                novoEstadoDeterminizado = getEstadoDeterminizadoDosNaoTratados(novo[i], tratar);
                if (novoEstadoDeterminizado != null) {
                    novo[i] = novoEstadoDeterminizado;
                } else {
                	tratar.push(novo[i]);
                }
            }

        }
    }
    
    private EstadoND getEstadoDeterminizadoDaTabela(EstadoND novaLinha, LinkedList<EstadoND[]> tabelaDeEstadosDeterminizados) {
        Iterator<EstadoND[]> it = tabelaDeEstadosDeterminizados.iterator();
        while (it.hasNext()) {
        	EstadoND[] estadosAux = it.next();
            if (estadosAux[0].ehIgual(novaLinha)) {
                return estadosAux[0];
            }
        }
        return null;
    }
    
    private EstadoND getEstadoDeterminizadoDosNaoTratados(EstadoND estadoAuxiliar, LinkedList<EstadoND> estadosDeterminizadosParaTratar) {
        Iterator<EstadoND> it = estadosDeterminizadosParaTratar.iterator();
        while (it.hasNext()) {
        	EstadoND eAux = it.next();        	
            if (eAux.ehIgual(estadoAuxiliar)) {
                return eAux;
            }
        }
        return null;
    }    
    

    protected void addTransicao(Estado e1, Estado e2, String simbolo) {
        if (e1 != null && e2 != null) {
            e1.addTransicao(e2, simbolo);
            this.MP.add(new Transicao(e1,e2, new Terminal(simbolo)));
        }
    }
    
    public void eliminarEstadosInalcancaveis() {
    	
        LinkedList<Estado> visitar = new LinkedList<Estado>();
        LinkedList<Estado> alcancaveis = new LinkedList<Estado>();
        LinkedList<Transicao> trans = new LinkedList<Transicao>();
        visitar.push(q0);
        
        while(!visitar.isEmpty()){
        	Estado est = visitar.pop();
        	alcancaveis.add(est);
        	Iterator<Transicao> tra = est.getTransicoes().iterator();
        	while(tra.hasNext()){
        		Transicao transicoes = tra.next();
        		trans.add(transicoes);
        		Estado destino = transicoes.getDestino();
        		if(!visitar.contains(destino) && !alcancaveis.contains(destino)){
        			visitar.add(destino);
        		}        		
        	}
        }
        K = alcancaveis;
        MP = trans;
    }
    
    public void eliminarEstadosMortos(){
    	boolean efinal = false;
    	LinkedList<Estado> vivos = new LinkedList<Estado>();
    	LinkedList<Transicao> transicoes = new LinkedList<Transicao>();
    	Iterator<Estado> iit = K.iterator();
    	while(iit.hasNext()){
    		efinal=false;
    		Estado e = iit.next();
            if (F.contains(e)) {
                vivos.push(e);
                efinal = true;
            } else {
                if (alcancaFinal(e)) {
                    vivos.add(e);
                    efinal = true;                    
                }
            }
            if(efinal){
            	Iterator<Transicao> itt = e.getTransicoes().iterator();
                while(itt.hasNext()){
                	Transicao t = itt.next();
                	transicoes.push(t);
                }
            }
            else{
            	Iterator<Transicao> itt = e.getTransicoes().iterator();
                while(itt.hasNext()){
                	Transicao t = itt.next();
                	MP.remove(t);
                }
                removerTransicoesPara(e);            	
            }
                        
        }
    	
    	K = vivos;
    	MP = transicoes;
}
    
    
    public boolean alcancaFinal(Estado e){
    	LinkedList<Estado> visitar = new LinkedList<Estado>();    	
		LinkedList<Estado> jaVisitado = new LinkedList<Estado>();
		visitar.push(e);
    	while(!visitar.isEmpty()){
    		Estado est = visitar.pop();
    		if(F.contains(est)){
    			return true;
    		}
    		jaVisitado.push(est);
    		Iterator<Transicao> itr = est.getTransicoes().iterator();
    		while(itr.hasNext()){
    			Transicao tr = itr.next();
    			Estado destino = tr.getDestino();
    			if(!visitar.contains(destino) && !jaVisitado.contains(destino))
    				visitar.push(destino);
    		}
    		
    	}
    	return false;
    }
    public void removerTransicoesPara(Estado e){
    	Iterator<Estado> ite = K.iterator();
    	while(ite.hasNext()){
    		Estado es = ite.next();
			es.removeTransicoesParaOEstado(e);
    	}
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
	
    public Estado getEstado(String n) {
        Iterator<Estado> it = K.iterator();
        while (it.hasNext()) {
            Estado e = it.next();
            if (e.getEstado().equals(n)) {
                return e;
            }
        }
        return null;
    }
	
    public void setEstadoFinal(String s) {
        Estado e = getEstado(s);
        if (e != null) {
            setEstadoFinal(e);
        }
    }

    public void setEstadoFinal(Estado e) {
        e.setFinal(true);
    }
    


}
