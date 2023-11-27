package view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

import connection.CarrosDAO;
import connection.ClientesDAO;
import connection.VendasDAO;
import controller.CarrosControl;

import javax.swing.JTable;

import model.Carros;

import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.GridLayout;

public class JanelaCarros extends JPanel {

    private JButton cadastrar, apagar, editar, vender, buscar;
    private JComboBox<String> clientesComboBox;
    private JTextField carMarcaField, carModeloField, carAnoField, carPlacaField, carValorField, pesquisaCarrosField;
    private List<Carros> carros;
    private JTable table;
    private DefaultTableModel tableModel;
    private int linhaSelecionada = -1;

    public JanelaCarros() {// Construtor(GUI-JPanel)
        super();
        // entrada de dados
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Cadastro Carros"));
        JPanel inputPanel = new JPanel();
        JPanel barraPesquisaPanel = new JPanel();
        clientesComboBox = new JComboBox<>();
        inputPanel.add(new JLabel("Cliente"));
        List<String> listarClientes = new ClientesDAO().carregarClienteComboBox();
        for (String cliente : listarClientes) {
            clientesComboBox.addItem(cliente);
        }
        //Barra de ferramentas
        JToolBar toolBar = new JToolBar();
        pesquisaCarrosField = new JTextField(20);
        toolBar.add(pesquisaCarrosField);
        toolBar.add(buscar = new JButton("Buscar..."));
        inputPanel.add(clientesComboBox);
        inputPanel.setLayout(new GridLayout(3, 3));
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
        botoes.add(vender = new JButton("Vender"));
        add(botoes);
        add(toolBar, BorderLayout.SOUTH);

        // tabela de carros
        JScrollPane jSPane = new JScrollPane();
        add(jSPane);
        tableModel = new DefaultTableModel(new Object[][] {},
                new String[] { "Marca", "Modelo", "Ano", "Placa", "Valor" });
        table = new JTable(tableModel);
        jSPane.setViewportView(table);

        // Cria a tabela
        new CarrosDAO().criarTabela();
        new VendasDAO().criarTabelaVendas();
        atualizarTabela();

        // Tratamento de eventos

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                linhaSelecionada = table.rowAtPoint(evt.getPoint());
                if (linhaSelecionada != -1) {
                    carMarcaField.setText((String) table.getValueAt(linhaSelecionada, 0));
                    carModeloField.setText((String) table.getValueAt(linhaSelecionada, 1));
                    carAnoField.setText((String) table.getValueAt(linhaSelecionada, 2));
                    carPlacaField.setText((String) table.getValueAt(linhaSelecionada, 3));
                    carValorField.setText((String) table.getValueAt(linhaSelecionada, 4));

                    // Atualiza a tabela de exibição
                    atualizarTabela();
                    // Atualiza os campos de entrada
                    atualizarCamposEntrada();
                }
            }
        });

        // Cria um objeto operacoes da classe CarrosControl para executar operações no
        // banco de dados
        CarrosControl operacoes = new CarrosControl(carros, tableModel, table, clientesComboBox);

        // Configura a ação do botão "vender" para registrar uma venda
        vender.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (linhaSelecionada != -1 && linhaSelecionada < carros.size()) {
                    String carroPlaca = carPlacaField.getText();
                    String clienteCpf = (String) clientesComboBox.getSelectedItem();
                    String valor = (String) carValorField.getText();
                    operacoes.vender(carroPlaca, clienteCpf, valor);

                    // Limpa as inputs
                    carMarcaField.setText("");
                    carModeloField.setText("");
                    carAnoField.setText("");
                    carPlacaField.setText("");
                    carValorField.setText("");

                    atualizarTabela();

                    System.out.println("Venda concluída. Tabela carros atualizada.");
                }

            }
        });

        // Configura a ação do botão "cadastrar" para adicionar um novo registro no
        // banco de dados
        cadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verifica se algum dos campos está vazio
                if (carModeloField.getText().isEmpty() || carMarcaField.getText().isEmpty()
                        || carPlacaField.getText().isEmpty()
                        || carAnoField.getText().isEmpty() || carValorField.getText().isEmpty()) {
                    // Verifica qual campo específico está vazio e mostra a mensagem de aviso
                    if (carModeloField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "O campo 'Modelo' não pode estar vazio.");
                        carModeloField.requestFocus();
                    } else if (carMarcaField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "O campo 'Marca' não pode estar vazio.");
                        carMarcaField.requestFocus();
                    } else if (carPlacaField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "O campo 'Placa' não pode estar vazio.");
                        carPlacaField.requestFocus();
                    } else if (carAnoField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "O campo 'Ano' não pode estar vazio.");
                        carAnoField.requestFocus();
                    } else if (carValorField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "O campo 'Valor' não pode estar vazio.");
                        carValorField.requestFocus();
                    }
                    return; // Sai do método se algum campo estiver vazio
                }

                // Verificando se há dados em seus devidos lugares
                try {
                    // Tratando o campo 'Modelo'
                    String modelo = carModeloField.getText();
                    // Restante do bloco try para o campo 'Modelo'
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "O campo 'Modelo' deve conter apenas letras.");
                }

                try {
                    // Tratando o campo 'Marca'
                    String marca = carMarcaField.getText();
                    // Restante do bloco try para o campo 'Marca'
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "O campo 'Marca' deve conter apenas letras.");
                }

                try {
                    // Tratando o campo 'Placa'
                    int placa = Integer.parseInt(carPlacaField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "O campo 'Placa' deve conter apenas números");
                    carPlacaField.requestFocus();
                    return;
                }

                try {
                    // Tratando o campo 'Ano'
                    int ano = Integer.parseInt(carAnoField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "O campo 'Ano' deve conter apenas números");
                    carAnoField.requestFocus();
                    return;
                }

                try {
                    // Tratando o campo 'Valor'
                    int valor = Integer.parseInt(carValorField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "O campo 'Valor' deve conter apenas números");
                    carValorField.requestFocus();
                    return;
                }

                // Chama o método "cadastrar" do objeto operacoes com os valores dos campos de
                // entrada
                operacoes.cadastrar(carMarcaField.getText(), carModeloField.getText(), carAnoField.getText(),
                        (String)carPlacaField.getText(), (String)carValorField.getText());

                // Limpa os campos de entrada após a operação de cadastro
                carMarcaField.setText("");
                carModeloField.setText("");
                carAnoField.setText("");
                carPlacaField.setText("");
                carValorField.setText("");

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
                // Verifica se algum dos campos está vazio
                if (carMarcaField.getText().isEmpty() || carModeloField.getText().isEmpty()
                        || carAnoField.getText().isEmpty() || carPlacaField.getText().isEmpty()
                        || carValorField.getText().isEmpty()) {
                    // Verifica qual campo específico está vazio e mostra a mensagem de aviso
                    // correspondente
                    if (carMarcaField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "O campo 'Marca' não pode estar vazio.");
                    } else if (carModeloField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "O campo 'Modelo' não pode estar vazio.");
                    } else if (carAnoField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "O campo 'Ano' não pode estar vazio.");
                    } else if (carPlacaField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "O campo 'Placa' não pode estar vazio.");
                    } else if (carValorField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "O campo 'Valor' não pode estar vazio.");
                    }
                    return; // Sai do método se algum campo estiver vazio
                }
                // Chama o método "atualizar" do objeto operacoes com os valores do campos de
                // entrada

                operacoes.atualizar(carMarcaField.getText(), carModeloField.getText(),

                        carAnoField.getText(), carPlacaField.getText(), carValorField.getText());
                // Limpa os campos de entrada após a operação de atualização
                carMarcaField.setText("");
                carModeloField.setText("");
                carAnoField.setText("");
                carPlacaField.setText("");
                carValorField.setText("");
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

                operacoes.apagar(carPlacaField.getText());
                // Limpa os campos de entrada após a operação de exclusão
                carMarcaField.setText("");
                carModeloField.setText("");
                carAnoField.setText("");
                carPlacaField.setText("");
                carValorField.setText("");
                atualizarTabela();
                
                // Atualiza os campos de entrada
                atualizarCamposEntrada();
            }
        });

        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtenha o termo de pesquisa da caixa de pesquisa
                String termoPesquisa = pesquisaCarrosField.getText();
        
                // Verifique se o termo não está vazio
                if (!termoPesquisa.isEmpty()) {
                    List<Carros> carrosEncontrados = new CarrosDAO().buscarCarros(termoPesquisa);
                    atualizarTabelaPesquisa(carrosEncontrados);
                } else {
                    // Se o termo estiver vazio, atualize a tabela com todos os carros
                    atualizarTabela();
                    JOptionPane.showMessageDialog(null, "Digite um termo de pesquisa.");
                }
            }
        });
        

    }

    // Método para atualizar campos de entrada
    private void atualizarCamposEntrada() {
        if (linhaSelecionada != -1 && linhaSelecionada < carros.size()) {
            Carros carrosSelecionados = carros.get(linhaSelecionada);
            carMarcaField.setText(carrosSelecionados.getMarca());
            carModeloField.setText(carrosSelecionados.getModelo());
            carAnoField.setText(carrosSelecionados.getAno());
            carPlacaField.setText(carrosSelecionados.getPlaca());
            carValorField.setText(carrosSelecionados.getValor());

        }
    }

    //Método para atualizar a tabela conforme o busca
    private void atualizarTabelaPesquisa(List<Carros> carros) {
tableModel.setRowCount(0);;

for (Carros carros2 : carros) {
    tableModel.addRow(new Object[]{
        carros2.getMarca(), carros2.getModelo(), carros2.getAno(), carros2.getPlaca(), carros2.getValor()
    });
}
    }
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        carros = new CarrosDAO().listarTodos();

        // Obtém os carros atualizados do banco de dados
        for (Carros carro : carros) {
            // Adiciona os dados de cadda carro como uma nova linha na tabela Swing
            tableModel.addRow(new Object[] { carro.getMarca(), carro.getModelo(), carro.getAno(), carro.getValor(),
                    carro.getPlaca() });
        }
    }
}