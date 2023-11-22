package view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridLayout;

public class JanelaClientes extends JPanel{
    private JButton cadastrar, apagar, editar;
    private JTextField clienteField, clienteCpfField, clienteIdadeField, clienteTelefoneField, clienteEnderecoField;
    private JTable table;
    private DefaultTableModel tableModel;
    private int linhaSelecionada = -1;

    public JanelaClientes() {
        super();
        // entrada de dados
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Cadastro Carros"));
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Marca"));
        clienteField = new JTextField(20);
        inputPanel.add(clienteField);
        inputPanel.add(new JLabel("Modelo"));
        clienteCpfField = new JTextField(20);
        inputPanel.add(clienteCpfField);
        inputPanel.add(new JLabel("Ano"));
        clienteIdadeField = new JTextField(20);
        inputPanel.add(clienteIdadeField);
        inputPanel.add(new JLabel("Placa"));
        clienteTelefoneField = new JTextField(20);
        inputPanel.add(clienteTelefoneField);
        inputPanel.add(new JLabel("Valor"));
        clienteEnderecoField = new JTextField(20);
        inputPanel.add(clienteEnderecoField);
        add(inputPanel);
        JPanel botoes = new JPanel();
        botoes.add(cadastrar = new JButton("Cadastrar"));
        botoes.add(editar = new JButton("Editar"));
        botoes.add(apagar = new JButton("Apagar"));
        add(botoes);
        // tabela de carros
    }
}
