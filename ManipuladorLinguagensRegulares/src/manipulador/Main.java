package manipulador;

import java.awt.EventQueue;

import manipulador.controlador.Mediador;

public class Main {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Mediador mediator = new Mediador();
			}
		});
	}

}
