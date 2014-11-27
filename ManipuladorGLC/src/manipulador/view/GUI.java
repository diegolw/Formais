package manipulador.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

public class GUI {

	public JFrame frame;
	private DefaultListModel<String> listModel;
	private JList<String> listGramaticas;
	private ArrayList<IGUI> listeners;
	private JPanel panelEditar;

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
		menu.setLayout(new MigLayout("", "[grow]", "[grow][][][][][][][]"));

		listModel = new DefaultListModel<String>();
		listGramaticas = new JList<String>();
		listGramaticas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String nome = listGramaticas.getSelectedValue();
				carregarGramatica(nome);
			}
		});

		listGramaticas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					String selected = listGramaticas.getSelectedValue();
					int index = listGramaticas.getSelectedIndex();
					listModel.removeElementAt(index);
					listGramaticas.updateUI();
				}
			}
		});
		// listGramaticas.addListSelectionListener(new ListSelectionListener() {
		// public void valueChanged(ListSelectionEvent e) {
		// String selected = listGramaticas.getSelectedValue();
		// if (selected == null) {
		// return;
		// }
		// }
		// });

		listGramaticas.setModel(listModel);
		menu.add(listGramaticas, "cell 0 0,grow");

		JButton btnLer = new JButton("Ler");
		btnLer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (IGUI listener : listeners) {
					String nome = "";
					listener.ler(nome);
				}
			}
		});

		JButton btnNovaGramtica = new JButton("Nova gramática");
		btnNovaGramtica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nome = JOptionPane
						.showInputDialog("Qual o nome da gramática?");
				String producoes = JOptionPane
						.showInputDialog("E quantas produções ela terá?");
				int numProducoes = Integer.parseInt(producoes);
				panelEditar.removeAll();
				for (int i = 0; i < numProducoes; i++) {
					panelEditar.add(new ProducaoPanel(), "cell 0 " + i
							+ ",alignx center,growy");
				}
				frame.setVisible(true);
				listModel.addElement(nome);
				for (IGUI listener : listeners) {
					listener.adicionar(nome);
				}
			}
		});
		menu.add(btnNovaGramtica, "cell 0 1,growx");
		menu.add(btnLer, "cell 0 2,growx");

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				salvarGLC();
			}
		});
		menu.add(btnSalvar, "cell 0 3,growx");

		JButton btnFatorar = new JButton("Fatorar");
		btnFatorar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (IGUI listener : listeners) {
					listener.fatorar();
				}
			}
		});
		menu.add(btnFatorar, "cell 0 4,growx");

		JButton btnEliminarRecurso = new JButton("Eliminar Recursão à ESQ");
		btnEliminarRecurso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (IGUI listener : listeners) {
					listener.eliminarRecursao();
				}
			}
		});

		JButton btnTransformarEmPrpria = new JButton("Transformar em Própria");
		btnTransformarEmPrpria.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (IGUI listener : listeners) {
					listener.propria();
				}
			}
		});
		menu.add(btnTransformarEmPrpria, "cell 0 5,growx");
		menu.add(btnEliminarRecurso, "cell 0 6,growx");

		JButton btnVaziaFinita = new JButton("É vazia, finita ou infinita?");
		btnVaziaFinita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (IGUI listener : listeners) {
					listener.ehVaziaFinitaInfinita();
				}
			}
		});
		menu.add(btnVaziaFinita, "cell 0 7,growx");

		panelEditar = new JPanel();
		panelEditar.setBorder(new TitledBorder(null,
				"Gram\u00E1tica Livre de Contexto", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		window.add(panelEditar, "cell 1 0,grow");
		panelEditar.setLayout(new MigLayout("", "[grow]", "[]"));
	}

	public void carregarGramaticas() {
		// listModel.addElement("abc");

	}

	public void carregarGramatica(String nome) {
		for (IGUI listener : listeners) {
			listener.exibir(nome);
		}
	}

	// Produção = Não Terminal -> Derivações
	public void exibirGramatica(int numProducoes, ArrayList<String> producoes) {
		panelEditar.removeAll();

		for (int i = 1; i < producoes.size(); i = i + 2) {
			ProducaoPanel producaoPanel = new ProducaoPanel();
			String naoTerminal = producoes.get(i - 1);
			String derivacoes = producoes.get(i);
			producaoPanel.setProducao(naoTerminal, derivacoes);

			panelEditar.add(producaoPanel, "cell 0 " + numProducoes
					+ ",alignx center,growy");
			numProducoes++;
		}

		frame.setVisible(true);
	}

	public void inserirGramatica(String nome) {
		listModel.addElement(nome);
	}

	public void salvarGLC() {
		int numProducoes = panelEditar.getComponentCount();
		
		ArrayList<String> producoes = new ArrayList<>();

		for (int i = 0; i < numProducoes; i++) {
			ProducaoPanel producaoPanel = (ProducaoPanel) panelEditar.getComponent(i);
			
			JTextField naoTerminal;
			naoTerminal = (JTextField) producaoPanel.getComponent(0);
			String nomeNaoTerminal = naoTerminal.getText().trim();

			JTextField derivacoes;
			derivacoes = (JTextField) producaoPanel.getComponent(2);
			String listaDerivacoes = derivacoes.getText().trim();
			
			producoes.add(nomeNaoTerminal);
			producoes.add(listaDerivacoes);
		}
		for (IGUI listener : listeners) {
			String nome = listGramaticas.getSelectedValue();
			listener.validar(nome, producoes);
		}
	}
}
