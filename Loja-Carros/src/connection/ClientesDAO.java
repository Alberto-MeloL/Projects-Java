package connection;

import model.Clientes;
import view.ClienteObserver;
import view.JanelaCarros;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class ClientesDAO {

    private Connection connection;
    private List<Clientes> clientes;
    private List<ClienteObserver> observadores = new ArrayList<>();

    public ClientesDAO() {

        this.connection = ConnectionFactory.getConnection();
    }
    /* Métodos */

    public void adicionarObservador(ClienteObserver observador){
        observadores.add(observador);
    }
    public void notificarObservadores(){
        for (ClienteObserver observador : observadores) {
            observador.atualizarClientes();
        }
    }
    public void criarTabelaCliente() {

        String sqlCriarTabelaCliente = "CREATE TABLE IF NOT EXISTS clientes (ID SERIAL PRIMARY KEY,NOME VARCHAR(255), CPF VARCHAR(255), IDADE VARCHAR(255),TELEFONE VARCHAR(255),ENDERECO VARCHAR(255))";

        try (Statement stmt = this.connection.createStatement()) {
            /* Ejetor de código SQL */

            stmt.execute(sqlCriarTabelaCliente); /* Código a ser executado */
            System.out.println("Tabela criada com sucesso.");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar a tabela:" + e.getMessage(), e);
        } finally {
            /* Fecha a conexão de qualquer maneira(sendo cathc ou não) */
            ConnectionFactory.closeConnection(this.connection);
        }
    }

    public List<String> carregarClienteComboBox() {
        List<Clientes> listarClientes = new ClientesDAO().listarTodosClientes();
        List<String> comboBoxClientes = new ArrayList<>();

        for (Clientes clientes : listarClientes) {
            comboBoxClientes.add(clientes.getNome());
        }
        return comboBoxClientes;
    }
    /* Listar todos */

    public List<Clientes> listarTodosClientes() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        clientes = new ArrayList<>();

        try {
            stmt = connection.prepareStatement("SELECT * FROM clientes");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Clientes cliente = new Clientes(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("idade"),
                        rs.getString("telefone"),
                        rs.getString("endereco"));
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            ConnectionFactory.closeConnection(connection, stmt, rs);
        }
        return clientes;
    }

    public void cadastrarCliente(String nome, String cpf, String idade, String telefone, String endereco) {
        PreparedStatement stmt = null;

        String sqlCadastrarCliente = "INSERT INTO clientes (nome, cpf, idade, telefone, endereco) VALUES (?,?,?,?,?)";

        try {
            stmt = connection.prepareStatement(sqlCadastrarCliente);

            stmt.setString(1, nome);
            stmt.setString(2, cpf);
            stmt.setString(3, idade);
            stmt.setString(4, telefone);
            stmt.setString(5, endereco);
            stmt.executeUpdate();
            notificarObservadores();//Notoficar os observadores ao cadastrar um cliente
            System.out.println("Dados inseridos com sucesso");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir dados.", e);
        } finally {
            ConnectionFactory.closeConnection(connection, stmt);
        }
    }
    /* Atualizar dados no banco */

    public void atualizarCliente(String nome, String cpf, String idade, String telefone, String endereco) {
        PreparedStatement stmt = null;

        String sqlAtualizarDadosCliente = "UPDATE clientes SET nome = ?, endereco = ?, idade = ?, telefone = ? WHERE cpf = ?";

        try {
            stmt = connection.prepareStatement(sqlAtualizarDadosCliente);
            stmt.setString(1, nome);
            stmt.setString(2, cpf);
            stmt.setString(3, idade);
            stmt.setString(4, telefone);
            stmt.setString(5, endereco);
            stmt.executeUpdate();
            connection.setAutoCommit(true);

            System.out.println("Dados atualizados com sucesso");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar dados.", e);
        } finally {
            ConnectionFactory.closeConnection(connection, stmt);
        }
    }

    /* Apagar dados do banco */

    public void apagarCliente(String cpf) {
        String message = "Deseja realmente deletar esse cliente?";
        PreparedStatement stmt = null;

        String sqlApagarPeloCpf = "DELETE FROM clientes WHERE cpf = ?";

        try {
            int escolhaJO = JOptionPane.showConfirmDialog(null, message);
            if (escolhaJO == JOptionPane.YES_OPTION) {
                stmt = connection.prepareStatement(sqlApagarPeloCpf);
                stmt.setString(1, cpf);
                stmt.executeUpdate();
                notificarObservadores();//Notificar observadores aoo apagar um cliente
            }
            if (escolhaJO == JOptionPane.NO_OPTION) {
                return;
            }
            System.out.println("Cpf apagado com sucesso");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar pela placa", e);
        } finally {
            ConnectionFactory.closeConnection(connection, stmt);
        }
    }

    public void insertEmployee(String usuario, String senha) throws SQLException {

        String sqlInsertEmployee = "INSERT INTO employee (USUARIO, SENHA) VALUES (?,?)";
        PreparedStatement stmt = connection.prepareStatement(sqlInsertEmployee);
        try {
            stmt.setString(1, usuario);
            stmt.setString(2, senha);
            stmt.executeUpdate();
            System.out.println("Dados inseridos com sucesso.");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inserir dados.", e);
        } finally {
            ConnectionFactory.closeConnection(connection, stmt);
        }
    }
}

//