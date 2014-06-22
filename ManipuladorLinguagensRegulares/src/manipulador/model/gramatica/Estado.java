package manipulador.model.gramatica;

public class Estado {
	private char estado;
	private boolean isfinal;
	
	protected Estado(char _est, boolean _final){
		setEstado(_est);
		setIsfinal(_final);
	}

	public char getEstado() {
		return estado;
	}

	public void setEstado(char estado) {
		this.estado = estado;
	}

	public boolean isIsfinal() {
		return isfinal;
	}

	public void setIsfinal(boolean isfinal) {
		this.isfinal = isfinal;
	}

}
