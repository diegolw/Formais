package manipulador.visao;

public interface IGUI {

	void addTransicao(String origem, String destino, String simbolo);

	void setAlfabeto(String[] simbolos);

	void addEstado(String estado, boolean ehInicial, boolean ehFinal);
	
	void expressaoRegular(String retorno);
	
	void minimizar();

	void igualdade();
	
	void interseccao();
	
	void diferenca();
	
	void ocorrencias();

	void enumerar(int num);

	void testar();

}
