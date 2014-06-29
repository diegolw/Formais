package manipulador.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

public class GUI {

	public JFrame frame;
	private DefaultListModel<Object> listModel;
	private ArrayList<IGUI> listeners;

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
		listeners = new ArrayList<>();
	}
	
	
	public void addListener(IGUI listener) {
		listeners.add(listener);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Manipulador de Gramáticas Livre de Contexto");
		frame.setBounds(100, 100, 700, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[grow]"));

		JPanel window = new JPanel();
		frame.getContentPane().add(window, "cell 0 0,grow");
		window.setLayout(new MigLayout("", "[grow][161.00,grow]", "[grow]"));

		JPanel menu = new JPanel();
		menu.setBorder(new TitledBorder(null, "Opera\u00E7\u00F5es",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		window.add(menu, "cell 0 0,grow");
		menu.setLayout(new MigLayout("", "[grow]", "[grow][][][][][]"));

		listModel = new DefaultListModel<>();
		JList<Object> listGramaticas = new JList<Object>();
		listGramaticas.setModel(listModel);
		listModel.addElement("Gramática do IF");
		menu.add(listGramaticas, "cell 0 0,grow");

		JButton btnLer = new JButton("Ler");
		btnLer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (IGUI listener : listeners) {
					listener.ler();
				}
			}
		});
		menu.add(btnLer, "cell 0 1,growx");

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (IGUI listener : listeners) {
					listener.salvar();
				}
			}
		});
		menu.add(btnSalvar, "cell 0 2,growx");

		JButton btnFatorar = new JButton("Fatorar");
		btnFatorar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (IGUI listener : listeners) {
					listener.fatorar();
				}
			}
		});
		menu.add(btnFatorar, "cell 0 3,growx");

		JButton btnEliminarRecurso = new JButton("Eliminar Recursão à ESQ");
		btnEliminarRecurso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (IGUI listener : listeners) {
					listener.eliminarRecursao();
				}
			}
		});
		menu.add(btnEliminarRecurso, "cell 0 4,growx");

		JButton btnVaziaFinita = new JButton("É vazia, finita ou infinita?");
		btnVaziaFinita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (IGUI listener : listeners) {
					listener.ehVaziaFinitaInfinita();
				}
			}
		});
		menu.add(btnVaziaFinita, "cell 0 5,growx");

		JPanel panelEditar = new JPanel();
		panelEditar.setBorder(new TitledBorder(null,
				"Gram\u00E1tica Livre de Contexto", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		window.add(panelEditar, "cell 1 0,grow");
		panelEditar.setLayout(new MigLayout("", "[grow]", "[][][]"));

		ProducaoPanel producao1 = new ProducaoPanel();
		ProducaoPanel producao2 = new ProducaoPanel();
		ProducaoPanel producao3 = new ProducaoPanel();

		panelEditar.add(producao1, "cell 0 0,alignx center,growy");
		panelEditar.add(producao2, "cell 0 1,alignx center,growy");
		panelEditar.add(producao3, "cell 0 2,alignx center,growy");
	}

}
