package manipulador.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GUI {

	private JFrame frmManipuladorDeLinguagens;

	private List<IGUI> listeners;
	private JTextField txtAlfabeto;

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
				new MigLayout("", "[grow][grow][grow]", "[300px,grow][][300,grow][16.00]"));

		JPanel panelAutomato1 = new JPanel();
		panelAutomato1.setBorder(new TitledBorder(null, "Aut\u00F4mato 1",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmManipuladorDeLinguagens.getContentPane().add(panelAutomato1,
				"cell 0 0,grow");
		panelAutomato1.setLayout(new MigLayout("", "[1px][grow]", "[grow]"));

		JScrollPane scrollPaneAutomato1 = new JScrollPane();
		panelAutomato1.add(scrollPaneAutomato1, "cell 1 0,grow");

		JList listAutmato1 = new JList();
		scrollPaneAutomato1.setViewportView(listAutmato1);

		JPanel panelAutomato2 = new JPanel();
		panelAutomato2.setBorder(new TitledBorder(null, "Aut\u00F4mato 2",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmManipuladorDeLinguagens.getContentPane().add(panelAutomato2,
				"cell 1 0,grow");
		panelAutomato2.setLayout(new MigLayout("", "[1px][grow]", "[grow]"));

		JScrollPane scrollPaneAutomato2 = new JScrollPane();
		panelAutomato2.add(scrollPaneAutomato2, "cell 1 0,grow");

		JList listAutmato2 = new JList();
		scrollPaneAutomato2.setViewportView(listAutmato2);

		JPanel panelOperacoes = new JPanel();
		panelOperacoes.setBorder(new TitledBorder(null, "Opera\u00E7\u00F5es",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmManipuladorDeLinguagens.getContentPane().add(panelOperacoes,
				"cell 2 0 1 3,grow");
		panelOperacoes.setLayout(new MigLayout("", "[]", "[][][][][][][][]"));

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

		JPanel panelEditar = new JPanel();
		panelEditar.setBorder(new TitledBorder(null, "Editar",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmManipuladorDeLinguagens.getContentPane().add(panelEditar,
				"cell 0 1 2 1,growx,aligny bottom");
		panelEditar.setLayout(new MigLayout("", "[][][grow]", "[]"));

		JRadioButton rdbtnAutmato = new JRadioButton("Autômato 1");
		panelEditar.add(rdbtnAutmato, "cell 0 0");
		rdbtnAutmato.setSelected(true);

		JRadioButton rdbtnAutmato2 = new JRadioButton("Autômato 2");
		panelEditar.add(rdbtnAutmato2, "cell 1 0");

		ButtonGroup groupAutomato = new ButtonGroup();
		groupAutomato.add(rdbtnAutmato);
		groupAutomato.add(rdbtnAutmato2);
		
				JButton btnNovoAlfabeto = new JButton("Novo ");
				btnNovoAlfabeto.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						for (IGUI listener : listeners) {
							listener.novoAlfabeto();
						}
					}
				});
				panelEditar.add(btnNovoAlfabeto, "flowx,cell 2 0,alignx right");
		
		txtAlfabeto = new JTextField();
		panelEditar.add(txtAlfabeto, "cell 2 0,alignx right");
		txtAlfabeto.setColumns(10);

//		JPanel panelTransicao = new JPanel();
//		panelTransicao.setToolTipText("");
//		panelTransicao.setBorder(new TitledBorder(null, "Transi\u00E7\u00E3o",
//				TitledBorder.LEADING, TitledBorder.TOP, null, null));
//		panelEditar.add(panelTransicao, "cell 0 2 4 1,grow");
//		panelTransicao
//				.setLayout(new MigLayout("", "[grow][grow][grow][]", "[]"));
//
//		JComboBox comboBoxOrigem = new JComboBox();
//		panelTransicao.add(comboBoxOrigem, "cell 0 0,growx");
//
//		JComboBox comboBoxDestino = new JComboBox();
//		panelTransicao.add(comboBoxDestino, "flowx,cell 1 0,growx");
//
//		JComboBox comboBoxSimbolo = new JComboBox();
//		panelTransicao.add(comboBoxSimbolo, "flowx,cell 2 0,growx");
//
//		JButton btnAdicionarTrans = new JButton("Adicionar");
//		btnAdicionarTrans.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				for (IGUI listener : listeners) {
//					listener.adicionarTransicao();
//				}
//			}
//		});
//		panelTransicao.add(btnAdicionarTrans, "cell 3 0");

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

	public void setVisible(boolean b) {
		frmManipuladorDeLinguagens.setVisible(b);
	}

	public void addEventListener(IGUI listener) {
		listeners.add(listener);
	}
}
