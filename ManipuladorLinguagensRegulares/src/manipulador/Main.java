package manipulador;

import java.awt.EventQueue;

import manipulador.controller.Mediator;

public class Main {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mediator mediator = new Mediator();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
