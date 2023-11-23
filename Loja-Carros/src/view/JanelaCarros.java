package view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
import java.awt.GridLayout;

public class JanelaCarros extends JPanel implements ClienteObserver{
    @Override
        public void atualizarClientes(){
            //Atualiza a lista de clientes aqui na interface gráfica
            //Chame o método para carregar os clientes no JComboBox
            List<String>listarClientes = new ClientesDAO().carregarClienteComboBox();

            clientesComboBox.removeAllItems();

            for (String cliente : listarClientes) {
                clientesComboBox.addItem(cliente);
            }
        }

    private JButton cadastrar, apagar, editar, vender;
    private JComboBox<String> clientesComboBox;
    private JTextField carMarcaField, carModeloField, carAnoField, carPlacaField, carValorField;
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
        clientesComboBox = new JComboBox<>();
        inputPanel.add(new JLabel("Cliente"));
        List<String> listarClientes = new ClientesDAO().carregarClienteComboBox();
        for (String cliente : listarClientes) {
            clientesComboBox.addItem(cliente);
        }

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
        // tabela de carros
        JScrollPane jSPane = new JScrollPane();
        add(jSPane);
        tableModel = new DefaultTableModel(new Object[][] {},
                new String[] { "Marca", "Modelo", "Ano", "Placa", "Valor" });
        table = new JTable(tableModel);
        jSPane.setViewportView(table);

        //Cria a tabela 
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

                    //Atualiza a tabela de exibição
                    atualizarTabela();
                    //Atualiza os campos de entrada
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
            public void actionPerformed(ActionEvent e){
                String carroPlaca = carPlacaField.getText();
                String clienteCpf = (String) clientesComboBox.getSelectedItem();
                String valor = carValorField.getText();
                operacoes.vender(carroPlaca, clienteCpf, valor);

                carPlacaField.setText("");
                carValorField.setText("");
                atualizarTabela();
                atualizarCamposEntrada();
            }
        });
        // Configura a ação do botão "cadastrar" para adicionar um novo registro no banco de dados
        cadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chama o método "cadastrar" do objeto operacoes com os valores dos campos de entrada

                operacoes.cadastrar(carMarcaField.getText(), carModeloField.getText(),

                        carAnoField.getText(), carPlacaField.getText(), carValorField.getText());
                // Limpa os campos de entrada após a operação de cadastro
                carMarcaField.setText("");
                carModeloField.setText("");
                carAnoField.setText("");
                carPlacaField.setText("");
                carValorField.setText("");
                atualizarTabela();

                //Atualiza os campos de entrada
                atualizarCamposEntrada();
            }
        });

        // Configura a ação do botão "editar" para atualizar um registro no banco de dados
        editar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chama o método "atualizar" do objeto operacoes com os valores do campos de entrada

                operacoes.atualizar(carMarcaField.getText(), carModeloField.getText(),

                        carAnoField.getText(), carPlacaField.getText(), carValorField.getText());
                // Limpa os campos de entrada após a operação de atualização
                carMarcaField.setText("");
                carModeloField.setText("");
                carAnoField.setText("");
                carPlacaField.setText("");
                carValorField.setText("");
                atualizarTabela();
                //Atualiza os campos de entrada
               atualizarCamposEntrada();

            }
        });

        // Configura a ação do botão "apagar" para excluir um registro no banco de dados
        apagar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chama o método "apagar" do objeto operacoes com o valor do campo de entrada "placa"

                operacoes.apagar(carPlacaField.getText());
                // Limpa os campos de entrada após a operação de exclusão
                carMarcaField.setText("");
                carModeloField.setText("");
                carAnoField.setText("");
                carPlacaField.setText("");
                carValorField.setText("");
                atualizarTabela();

                 //Atualiza os campos de entrada
                atualizarCamposEntrada();
            }
        });

    
    }

    //Método para atualizar campos de entrada
        private void atualizarCamposEntrada(){
            if (linhaSelecionada != -1 && linhaSelecionada < carros.size()) {
                Carros carrosSelecionados = carros.get(linhaSelecionada);
                carMarcaField.setText(carrosSelecionados.getMarca());
                carModeloField.setText(carrosSelecionados.getModelo());
                carAnoField.setText(carrosSelecionados.getAno());
                carPlacaField.setText(carrosSelecionados.getPlaca());
                carValorField.setText(carrosSelecionados.getValor());
                
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