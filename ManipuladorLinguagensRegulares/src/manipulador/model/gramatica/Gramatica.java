package manipulador.model.gramatica;

import java.util.List;

public class Gramatica {
	
	public List<Estado> Vn = null;
	public List<Terminal> Vt = null;
	public List<Transicao> P = null;
	public Estado S;

	/**
	 * 
	 * @param Vn, símbolos não terminais
	 * @param Vt, símbolos terminais
	 * @param P, produções
	 * @param S, símbolo inicial
	 */
	public Gramatica(List<Estado> Vn, List<Terminal> Vt,
			List<Transicao> P, Estado S) {
		this.Vn = Vn;
		this.Vt = Vt;
		this.P = P;
		this.S = S;
	}
	
}
