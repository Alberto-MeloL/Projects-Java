package controller;

import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import connection.ClientesDAO;
import model.Clientes;

public class ClientesControl {

    // Atributos
    private List<Clientes> clientes;
    private DefaultTableModel tableModel;
    private JTable table;

    public ClientesControl(List<Clientes> clientes, DefaultTableModel tableModel, JTable table) {

        this.clientes = clientes;
        this.tableModel = tableModel;
        this.table = table;
    }

    // Método para atualizar a tabela de exibição com dados do banco
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        clientes = new ClientesDAO().listarTodosClientes();

        // Obtém os carros atualizados do banco de dados
        for (Clientes cliente : clientes) {
            // Adiciona os dados de cadda carro como uma nova linha na tabela Swing
            tableModel.addRow(new Object[] { cliente.getNome(), cliente.getCpf(), cliente.getIdade(), cliente.getTelefone(), cliente.getEndereco() });
        }
    }

    // Método para cadastrar um novo carro no banco de dados
    public void cadastrar(String nome, String cpf, String idade, String telefone,
            String endereco) {
        new ClientesDAO().cadastrarCliente(nome, cpf, idade, telefone, endereco);
        
        // Chama o método de cadastro no banco de dados

        atualizarTabela();// Atuailza a table de exibição
    }

    // Método para atualizar os dados de um carro no banco de dados
    public void atualizar(String nome, String cpf, String idade, String telefone,
            String endereco) {
        new ClientesDAO().atualizarCliente(nome, cpf, idade, telefone, endereco);

        atualizarTabela();// Tabela de exibição
    }

    //Método para apagar um carro no banco de dados

    public void apagar(String cpf){
        new ClientesDAO().apagarCliente(cpf);

        atualizarTabela();//Tabela de exibição
    }
}
