package manipulador.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import manipulador.model.af.Automato;
import net.miginfocom.swing.MigLayout;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI {

	private JFrame frmManipuladorDeLinguagens;

	private List<IGUI> listeners;
	private JTextField txtAlfabeto;

	private DefaultTableModel modeloAF1 = null;
	private DefaultTableModel modeloAF2 = null;
	private DefaultTableModel model;
	private JTable jTable1;
	private JTable jTable2;

	private JRadioButton rdbtnAutmato1;
	private JRadioButton rdbtnAutmato2;

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();

		listeners = new ArrayList<IGUI>();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmManipuladorDeLinguagens = new JFrame();
		frmManipuladorDeLinguagens
				.setTitle("Manipulador de Linguagens Regulares");
		frmManipuladorDeLinguagens.setBounds(100, 100, 800, 800);
		frmManipuladorDeLinguagens
				.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmManipuladorDeLinguagens.getContentPane().setLayout(
				new MigLayout("", "[grow][grow][grow]",
						"[300px,grow][][300,grow][16.00]"));

		JPanel panelAutomato1 = new JPanel();
		panelAutomato1.setBorder(new TitledBorder(null, "Aut\u00F4mato 1",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmManipuladorDeLinguagens.getContentPane().add(panelAutomato1,
				"cell 0 0,grow");
		panelAutomato1.setLayout(new MigLayout("", "[1px][grow]", "[grow]"));

		JScrollPane scrollPaneAutomato1 = new JScrollPane();
		scrollPaneAutomato1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String[] linha = {};
				modeloAF1.addRow(linha);
			}
		});
		panelAutomato1.add(scrollPaneAutomato1, "cell 1 0,grow");

		modeloAF1 = new DefaultTableModel();
		model = modeloAF1;
		jTable1 = new JTable();
		jTable1.setModel(modeloAF1);
		scrollPaneAutomato1.setViewportView(jTable1);

		JPanel panelAutomato2 = new JPanel();
		panelAutomato2.setBorder(new TitledBorder(null, "Aut\u00F4mato 2",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmManipuladorDeLinguagens.getContentPane().add(panelAutomato2,
				"cell 1 0,grow");
		panelAutomato2.setLayout(new MigLayout("", "[1px][grow]", "[grow]"));

		JScrollPane scrollPaneAutomato2 = new JScrollPane();
		scrollPaneAutomato2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String[] linha = {};
				modeloAF2.addRow(linha);
			}
		});
		panelAutomato2.add(scrollPaneAutomato2, "cell 1 0,grow");

		modeloAF2 = new DefaultTableModel();
		jTable2 = new JTable();
		jTable2.setModel(modeloAF2);
		scrollPaneAutomato2.setViewportView(jTable2);

		JPanel panelOperacoes = new JPanel();
		panelOperacoes.setBorder(new TitledBorder(null, "Opera\u00E7\u00F5es",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmManipuladorDeLinguagens.getContentPane().add(panelOperacoes,
				"cell 2 0 1 3,grow");
		panelOperacoes.setLayout(new MigLayout("", "[]", "[][][][][][][][][]"));

		JButton btnMinimizar = new JButton("Minimizar");
		btnMinimizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (IGUI listener : listeners) {
					listener.minimizar();
				}
			}
		});
		panelOperacoes.add(btnMinimizar, "cell 0 0,growx");

		JButton btnComplemento = new JButton("Complemento");
		panelOperacoes.add(btnComplemento, "cell 0 1,growx");

		JButton btnLL_1 = new JButton("L1 ∩ L2");
		panelOperacoes.add(btnLL_1, "cell 0 3,growx");

		JButton btnLL_2 = new JButton("L1 ∪ L2");
		panelOperacoes.add(btnLL_2, "cell 0 4,growx");

		JButton btnLL = new JButton("L1 = L2?");
		panelOperacoes.add(btnLL, "cell 0 2,growx");

		JButton btnReverso = new JButton("Reverso");
		panelOperacoes.add(btnReverso, "cell 0 5,growx");

		JButton btnEnumerar = new JButton("Enumerar");
		panelOperacoes.add(btnEnumerar, "cell 0 6,growx");

		JButton btnVazia = new JButton("Vazia, infinita ou finita?");
		btnVazia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panelOperacoes.add(btnVazia, "cell 0 7,growx");

		JButton btnValidar = new JButton("Validar");
		btnValidar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				validar();
			}
		});
		panelOperacoes.add(btnValidar, "cell 0 8,growx");

		JPanel panelEditar = new JPanel();
		panelEditar.setBorder(new TitledBorder(null, "Editar",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmManipuladorDeLinguagens.getContentPane().add(panelEditar,
				"cell 0 1 2 1,growx,aligny bottom");
		panelEditar.setLayout(new MigLayout("", "[][][grow]", "[]"));

		rdbtnAutmato1 = new JRadioButton("Autômato 1");
		rdbtnAutmato1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model = modeloAF1;
				estadoInicial = estadoInicial1;
			}
		});
		panelEditar.add(rdbtnAutmato1, "cell 0 0");
		rdbtnAutmato1.setSelected(true);

		rdbtnAutmato2 = new JRadioButton("Autômato 2");
		rdbtnAutmato2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model = modeloAF2;
				estadoInicial = estadoInicial2;
			}
		});
		panelEditar.add(rdbtnAutmato2, "cell 1 0");

		ButtonGroup groupAutomato = new ButtonGroup();
		groupAutomato.add(rdbtnAutmato1);
		groupAutomato.add(rdbtnAutmato2);

		JButton btnNovoAlfabeto = new JButton("Novo ");
		btnNovoAlfabeto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (IGUI listener : listeners) {
					String alfabeto = txtAlfabeto.getText();
					String[] simbolos = alfabeto.split(" ");

					for (int i = 0; i < simbolos.length; i++) {
						if (!simbolos[i]
								.matches("[a-z0-9 _]*[a-z0-9][a-z0-9 _]*")
								|| simbolos[i].length() > 1) {
							JOptionPane
									.showMessageDialog(null,
											"Ei, use apenas uma letra minúscula ou um número, separados por espaço, ok?");
							return;
						}
					}

					// Envia para o mediador
					listener.setAlfabeto(simbolos);

					// Modifica a jTable
					setAlfabeto(simbolos);
				}
			}
		});
		panelEditar.add(btnNovoAlfabeto, "flowx,cell 2 0,alignx right");

		txtAlfabeto = new JTextField();
		panelEditar.add(txtAlfabeto, "cell 2 0,alignx right");
		txtAlfabeto.setColumns(10);

		// JPanel panelTransicao = new JPanel();
		// panelTransicao.setToolTipText("");
		// panelTransicao.setBorder(new TitledBorder(null,
		// "Transi\u00E7\u00E3o",
		// TitledBorder.LEADING, TitledBorder.TOP, null, null));
		// panelEditar.add(panelTransicao, "cell 0 2 4 1,grow");
		// panelTransicao
		// .setLayout(new MigLayout("", "[grow][grow][grow][]", "[]"));
		//
		// JComboBox comboBoxOrigem = new JComboBox();
		// panelTransicao.add(comboBoxOrigem, "cell 0 0,growx");
		//
		// JComboBox comboBoxDestino = new JComboBox();
		// panelTransicao.add(comboBoxDestino, "flowx,cell 1 0,growx");
		//
		// JComboBox comboBoxSimbolo = new JComboBox();
		// panelTransicao.add(comboBoxSimbolo, "flowx,cell 2 0,growx");
		//
		// JButton btnAdicionarTrans = new JButton("Adicionar");
		// btnAdicionarTrans.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// for (IGUI listener : listeners) {
		// listener.adicionarTransicao();
		// }
		// }
		// });
		// panelTransicao.add(btnAdicionarTrans, "cell 3 0");

		JPanel panelResultado = new JPanel();
		panelResultado.setBorder(new TitledBorder(null, "Resultado",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmManipuladorDeLinguagens.getContentPane().add(panelResultado,
				"cell 0 2 2 1,grow");
		panelResultado
				.setLayout(new MigLayout("", "[grow][3px]", "[grow][3px]"));

		JScrollPane scrollPaneResultado = new JScrollPane();
		panelResultado.add(scrollPaneResultado, "cell 0 0,grow");

		JList listResultado = new JList();
		scrollPaneResultado.setViewportView(listResultado);

		JMenuBar menuBar = new JMenuBar();
		frmManipuladorDeLinguagens.setJMenuBar(menuBar);

		JMenu mnArquivo = new JMenu("Arquivo");
		menuBar.add(mnArquivo);

		JMenuItem mntmHelp = new JMenuItem("Help");
		mnArquivo.add(mntmHelp);
	}

	private void validar() {
		ArrayList<String> estados = getEstados();
		String[][] producoes = new String[estados.size()][model
				.getColumnCount()];
		ArrayList<String> estadosInvalidos = new ArrayList<String>();

		for (int i = 0; i < estados.size(); i++) {
			String[] producao = new String[model.getColumnCount()];
			for (int j = 0; j < model.getColumnCount(); j++) {
				if (model.getValueAt(i, j) != null) {
					String nTerminal = (String) model.getValueAt(i, j);
					if (nTerminal.contains("->")) {
						nTerminal = nTerminal.replace("->", "");
					} else if (nTerminal.contains("*")) {
						nTerminal = nTerminal.replace("*", "");
					}
					boolean upperFound = false;
					for (char c : nTerminal.toCharArray()) {
						if (!Character.isUpperCase(c)) {
							upperFound = true;
							break;
						}
					}
					if (upperFound) {
						JOptionPane
								.showMessageDialog(null,
										"Bah.. Que tal usar apenas letras maíscula como estados?");
						return;
					}
					producao[0] = nTerminal;

					if (!estados.contains(nTerminal)) {
						estadosInvalidos.add(nTerminal);
					}
				}
			}
			producoes[i] = producao;
		}
		if (!estadosInvalidos.isEmpty()) {
			String pertence = "pertence";
			if (estadosInvalidos.size() > 1) {
				pertence += "m";
			}
			String naoTerminais = "";
			for (int k = 0; k < estadosInvalidos.size(); k++) {
				naoTerminais += estadosInvalidos.get(k) + ", ";
			}
			JOptionPane.showMessageDialog(null, naoTerminais + " não "
					+ pertence + " a Vn!");
			return;
		}
		for (int i = 0; i < producoes.length; i++) {
			String[] producao = producoes[i];
			for (int j = 0; j < producao.length; j++) {
				for (IGUI listener : listeners) {
					listener.addTransicao(producao[0], producao[1], producao[2]);
				}
			}
		}
	}

	String estadoInicial1;
	String estadoInicial2;
	String estadoInicial;

	private ArrayList<String> getEstados() {
		ArrayList<String> estados = new ArrayList<>();
		for (IGUI listener : listeners) {
			listener.limparEstados();
		}

		for (int i = 0; i < model.getRowCount(); i++) {
			if (model.getValueAt(i, 0) != null) {
				String estado = (String) model.getValueAt(i, 0);
				boolean ehInicial = false;
				boolean ehFinal = false;
				if (estado.contains("->")) {
					estado = estado.replace("->", "");
					if (estadoInicial != null) {
						JOptionPane
								.showMessageDialog(null,
										"Pera aê! Tem dois estados inicias! Volta lá e corrige, blz?");
						break;
					}
					estadoInicial = estado;
					ehInicial = true;
				} else if (estado.contains("*")) {
					estado = estado.replace("*", "");
					ehFinal = true;
				}
				estados.add(estado);
				for (IGUI listener : listeners) {
					listener.addEstado(estado, ehInicial, ehFinal);
				}
			}
		}
		return estados;
	}

	public void setVisible(boolean b) {
		frmManipuladorDeLinguagens.setVisible(b);
	}

	public void addEventListener(IGUI listener) {
		listeners.add(listener);
	}

	public void setAlfabeto(String[] simbolos) {
		// Renderiza alfabeto na tabela
		model.setColumnCount(0);
		model.setRowCount(0);
		model.addColumn("Estado");
		for (int z = 0; z < simbolos.length; z++) {
			model.addColumn(simbolos[z]);
		}
	}

	public boolean ehAutomato1() {
		return rdbtnAutmato1.isSelected();
	}
}
