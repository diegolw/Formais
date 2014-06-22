package manipulador.view;

public interface IGUI {
	
	void addTransicao(String origem, String destino, String simbolo);

	void setAlfabeto(String[] simbolos);

	void addEstado(String estado, boolean ehInicial, boolean ehFinal);

	void limparEstados();
	
	void minimizar();
	
	void complemento();
	
	void igualdade();
	
	void interseccao();
	
	void uniao();
	
	void reverso();
	
	void enumerar();
	
	void vaziaInfinitaFinita();

}
