package manipulador.model;

public class ExpressaoRegular {

	private String descricao;

	public boolean setDescricao(String d) {
		if (validarDescricao(d)) {
			descricao = d;
			return true;
		}
		return false;
	}

	public boolean validarDescricao(String d) {
		return true;
	}

	public String getDescricao() {
		return new String(descricao);
	}

	public String toString() {
		return new String(descricao);
	}

}
