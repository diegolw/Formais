package manipulador.visao;

public interface IGUI {

	void addTransicao(String origem, String destino, String simbolo);

	void setAlfabeto(String[] simbolos);

	void addEstado(String estado, boolean ehInicial, boolean ehFinal);

	void minimizar();

	void complemento();

	void reverso();

	void enumerar(int num);

	void vaziaInfinitaFinita();

	void igualdade();

	void interseccao();

	void uniao();

	void gramatica();

	void expressaoRegular();

	void testar();

}
