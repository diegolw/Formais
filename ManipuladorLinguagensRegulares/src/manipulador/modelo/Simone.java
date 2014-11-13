package manipulador.modelo;

public class Simone {
	private int index;
	private String erro;
	private No raiz;
	private int nro_grupo;
	private String expressao;
	
	public Simone(String exp){
		
		this.index = 0;
		this.erro = "";
		this.nro_grupo = 1;
		this.expressao = exp;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	public No getRaiz() {
		return raiz;
	}

	public void setRaiz(No raiz) {
		this.raiz = raiz;
	}

	public int getNro_grupo() {
		return nro_grupo;
	}

	public void setNro_grupo(int nro_grupo) {
		this.nro_grupo = nro_grupo;
	}

	public String getExpressao() {
		return expressao;
	}

	public void setExpressao(String expressao) {
		this.expressao = expressao;
	}
		
	
}
