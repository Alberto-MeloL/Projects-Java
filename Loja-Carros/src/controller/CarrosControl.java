package controller;

import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import connection.CarrosDAO;
import model.Carros;

public class CarrosControl {

    // Atributos
    private List<Carros> carros;
    private DefaultTableModel tableModel;
    private JTable table;

    public CarrosControl(List<Carros> carros, DefaultTableModel tableModel, JTable table) {

        this.carros = carros;
        this.tableModel = tableModel;
        this.table = table;
    }

    // Método para atualizar a tabela de exibição com dados do banco
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        carros = new CarrosDAO().listarTodos();

        // Obtém os carros atualizados do banco de dados
        for (Carros carro : carros) {
            // Adiciona os dados de cadda carro como uma nova linha na tabela Swing
            tableModel.addRow(new Object[] { carro.getMarca(), carro.getModelo(), carro.getAno(),carro.getPlaca() });
        }
    }

    // Método para cadastrar um novo carro no banco de dados
    public void cadastrar(String marca, String modelo, String ano, String placa,
            String valor) {
        new CarrosDAO().cadastrar(marca, modelo, ano, placa, valor);
        
        // Chama o método de cadastro no banco de dados

        atualizarTabela();// Atuailza a table de exibição
    }

    // Método para atualizar os dados de um carro no banco de dados
    public void atualizar(String marca, String modelo, String ano, String placa,
            String valor) {
        new CarrosDAO().atualizar(marca, modelo, ano, placa, valor);

        atualizarTabela();// Tabela de exibição
    }

    //Método para apagar um carro no banco de dados

    public void apagar(String placa){
        new CarrosDAO().apagar(placa);

        atualizarTabela();//Tabela de exibição
    }
}
