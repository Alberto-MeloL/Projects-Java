package view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import connection.CarrosDAO;
import connection.ClientesDAO;
import controller.CarrosControl;
import controller.ClientesControl;
import model.Carros;
import model.Clientes;

import javax.swing.JTable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridLayout;

public class JanelaClientes extends JPanel {
    private JButton cadastrar, apagar, editar;
    private JTextField clienteField, clienteCpfField, clienteIdadeField, clienteTelefoneField, clienteEnderecoField;
    private List<Clientes> clientes;
    private JTable table;
    private DefaultTableModel tableModel;
    private int linhaSelecionada = -1;

    public JanelaClientes() {
        super();
        // entrada de dados
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Cadastro Carros"));
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 1));
        inputPanel.add(new JLabel("Nome"));
        clienteField = new JTextField(20);
        inputPanel.add(clienteField);
        inputPanel.add(new JLabel("CPF"));
        clienteCpfField = new JTextField(20);
        inputPanel.add(clienteCpfField);
        inputPanel.add(new JLabel("Idade"));
        clienteIdadeField = new JTextField(20);
        inputPanel.add(clienteIdadeField);
        inputPanel.add(new JLabel("Telefone"));
        clienteTelefoneField = new JTextField(20);
        inputPanel.add(clienteTelefoneField);
        inputPanel.add(new JLabel("Endereço"));
        clienteEnderecoField = new JTextField(20);
        inputPanel.add(clienteEnderecoField);
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
                new String[] { "Nome", "Cpf", "Idade", "Telefone", "Endereço" });
        table = new JTable(tableModel);
        jSPane.setViewportView(table);

        // Cria a tabela
        new ClientesDAO().criarTabelaCliente();
        atualizarTabela();

        // Tratamento de eventos

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                linhaSelecionada = table.rowAtPoint(evt.getPoint());
                if (linhaSelecionada != -1) {
                    clienteField.setText((String) table.getValueAt(linhaSelecionada, 0));
                    clienteCpfField.setText((String) table.getValueAt(linhaSelecionada, 1));
                    clienteIdadeField.setText((String) table.getValueAt(linhaSelecionada, 2));
                    clienteTelefoneField.setText((String) table.getValueAt(linhaSelecionada, 3));
                    clienteEnderecoField.setText((String) table.getValueAt(linhaSelecionada, 4));

                    // Atualiza a tabela de exibição
                    atualizarTabela();
                    // Atualiza os campos de entrada
                    atualizarCamposEntrada();
                }
            }
        });

        // Cria um objeto operacoes da classe CarrosControl para executar operações no
        // banco de dados
    ClientesControl operacoes = new ClientesControl(clientes, tableModel, table);

        // Configura a ação do botão "cadastrar" para adicionar um novo registro no
        // banco de dados
        cadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chama o método "cadastrar" do objeto operacoes com os valores dos campos de
                // entrada

                operacoes.cadastrar(clienteField.getText(), clienteCpfField.getText(),

                        clienteIdadeField.getText(), clienteTelefoneField.getText(), clienteEnderecoField.getText());
                // Limpa os campos de entrada após a operação de cadastro
                clienteField.setText("");
                clienteCpfField.setText("");
                clienteIdadeField.setText("");
                clienteTelefoneField.setText("");
                clienteEnderecoField.setText("");
                atualizarTabela();

                // Atualiza os campos de entrada
                atualizarCamposEntrada();
            }
        });

        // Configura a ação do botão "editar" para atualizar um registro no banco de
        // dados
        editar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chama o método "atualizar" do objeto operacoes com os valores do campos de
                // entrada

                operacoes.atualizar(clienteField.getText(), clienteCpfField.getText(),

                        clienteIdadeField.getText(), clienteTelefoneField.getText(), clienteEnderecoField.getText());
                // Limpa os campos de entrada após a operação de atualização
                clienteField.setText("");
                clienteCpfField.setText("");
                clienteIdadeField.setText("");
                clienteTelefoneField.setText("");
                clienteEnderecoField.setText("");
                atualizarTabela();
                // Atualiza os campos de entrada
                atualizarCamposEntrada();

            }
        });

        // Configura a ação do botão "apagar" para excluir um registro no banco de dados
        apagar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chama o método "apagar" do objeto operacoes com o valor do campo de entrada
                // "placa"

                operacoes.apagar(clienteCpfField.getText());
                // Limpa os campos de entrada após a operação de exclusão
                clienteField.setText("");
                clienteCpfField.setText("");
                clienteIdadeField.setText("");
                clienteTelefoneField.setText("");
                clienteEnderecoField.setText("");
                atualizarTabela();

                // Atualiza os campos de entrada
                atualizarCamposEntrada();
            }
        });
    }

    // Método para atualizar campos de entrada
    private void atualizarCamposEntrada() {
        if (linhaSelecionada != -1 && linhaSelecionada < clientes.size()) {
            Clientes clientesSelecionados = clientes.get(linhaSelecionada);
            clienteField.setText(clientesSelecionados.getNome());
            clienteCpfField.setText(clientesSelecionados.getCpf());
            clienteIdadeField.setText(clientesSelecionados.getIdade());
            clienteTelefoneField.setText(clientesSelecionados.getTelefone());
            clienteEnderecoField.setText(clientesSelecionados.getEndereco());

        }
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        clientes = new ClientesDAO().listarTodosClientes();

        // Obtém os carros atualizados do banco de dados
        for (Clientes cliente : clientes) {
            // Adiciona os dados de cadda carro como uma nova linha na tabela Swing
            tableModel.addRow(new Object[] { cliente.getNome(), cliente.getCpf(), cliente.getIdade(), cliente.getTelefone(),
                    cliente.getEndereco() });
        }
    }
}
