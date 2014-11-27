package manipulador.view;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class ProducaoPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textFieldDireita;
	private JTextField textFieldEsquerda;

	/**
	 * Create the panel.
	 */
	public ProducaoPanel() {
		setLayout(new MigLayout("", "[]", "[]"));

		textFieldDireita = new JTextField();
		add(textFieldDireita, "flowx,cell 0 0,alignx left");
		textFieldDireita.setColumns(4);

		JLabel label = new JLabel("⇒");
		add(label, "cell 0 0");

		textFieldEsquerda = new JTextField();
		add(textFieldEsquerda, "cell 0 0");
		textFieldEsquerda.setColumns(13);

	}
	
	public void setProducao(String naoTerminal, String derivacoes) {
		textFieldEsquerda.setText(naoTerminal);
		textFieldDireita.setText(derivacoes);
	}

}
