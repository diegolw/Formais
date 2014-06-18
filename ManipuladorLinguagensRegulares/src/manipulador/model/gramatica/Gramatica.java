package manipulador.model.gramatica;

import java.util.List;

public class Gramatica {
	
	public List<Simbolo> Vn = null;
	public List<Terminal> Vt = null;
	public List<Producao> P = null;
	public Simbolo S;

	/**
	 * 
	 * @param Vn, símbolos não terminais
	 * @param Vt, símbolos terminais
	 * @param P, produções
	 * @param S, símbolo inicial
	 */
	public Gramatica(List<Simbolo> Vn, List<Terminal> Vt,
			List<Producao> P, Simbolo S) {
		this.Vn = Vn;
		this.Vt = Vt;
		this.P = P;
		this.S = S;
	}
	
}
