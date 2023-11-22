package view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

import model.Carros;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridLayout;

public class JanelaCarros extends JPanel {

    private JButton cadastrar, apagar, editar;
    private JTextField carMarcaField, carModeloField, carAnoField, carPlacaField, carValorField;
    private List<Carros> carros;
    private JTable table;
    private DefaultTableModel tableModel;
    private int linhaSelecionada = -1;

    // Construtor(GUI-JPanel)
public JanelaCarros() {
super();
// entrada de dados
setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
add(new JLabel("Cadastro Carros"));
JPanel inputPanel = new JPanel();
inputPanel.setLayout(new GridLayout(5, 2));
inputPanel.add(new JLabel("Marca"));
carMarcaField = new JTextField(20);
inputPanel.add(carMarcaField);
inputPanel.add(new JLabel("Modelo"));
carModeloField = new JTextField(20);
inputPanel.add(carModeloField);
inputPanel.add(new JLabel("Ano"));
carAnoField = new JTextField(20);
inputPanel.add(carAnoField);
inputPanel.add(new JLabel("Placa"));
carPlacaField = new JTextField(20);
inputPanel.add(carPlacaField);
inputPanel.add(new JLabel("Valor"));
carValorField = new JTextField(20);
inputPanel.add(carValorField);
add(inputPanel);
JPanel botoes = new JPanel();
botoes.add(cadastrar = new JButton("Cadastrar"));
botoes.add(editar = new JButton("Editar"));
botoes.add(apagar = new JButton("Apagar"));
add(botoes);
// tabela de carros
JScrollPane jSPane = new JScrollPane();
add(jSPane);
tableModel = new DefaultTableModel(new Object[][] {},
new String[] { "Marca", "Modelo", "Ano", "Placa", "Valor" });
table = new JTable(tableModel);
jSPane.setViewportView(table);
}
}