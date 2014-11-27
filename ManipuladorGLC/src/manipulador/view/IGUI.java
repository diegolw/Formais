package manipulador.view;

import java.util.ArrayList;

public interface IGUI {

	void adicionar(String nome);

	void ler(String nome);

	void salvar();

	void fatorar();

	void propria();

	void eliminarRecursao();

	void ehVaziaFinitaInfinita();

	void exibir(String nome);

	void validar(String nome, ArrayList<String> producoes);

}
