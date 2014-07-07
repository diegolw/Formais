package manipulador.modelo;

public class ExpressaoRegular {

	private String descricao;

	public boolean setDescricao(String descricao) {
		if (validarDescricao(descricao)) {
			this.descricao = descricao;
			return true;
		}
		return false;
	}

	public boolean validarDescricao(String descricao) {
		return true;
	}

	public String getDescricao() {
		return new String(descricao);
	}

	public String toString() {
		return new String(descricao);
	}

}
