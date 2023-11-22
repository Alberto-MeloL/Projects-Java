package connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import model.Carros;

/*  Essa classe manipula conexões como banco de dados
-Gerencia a conexão com o SQL*/
public class CarrosDAO {

    private Connection connection;
    private List<Carros> carros;

    /* Cria um objeto da ConnectionFactory(injetar códigos SQL) */

    public CarrosDAO() {

        /*
         * Crio a conexão dentro do objeto, pois sempre que eu instancia-lo, tenho que
         * ter a conexão
         */

        this.connection = ConnectionFactory.getConnection();

    }

    /* Métodos */

    public void criarTabela() {

        String sqlCriarTabela = "CREATE TABLE IF NOT EXISTS carros (ID SERIAL PRIMARY KEY,MARCA VARCHAR(255), MODELO VARCHAR(255), ANO VARCHAR(255),PLACA VARCHAR(255),VALOR VARCHAR(255))";

        try (Statement stmt = this.connection.createStatement()) {
            /* Ejetor de código SQL */

            stmt.execute(sqlCriarTabela); /* Código a ser executado */
            System.out.println("Tabela criada com sucesso.");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar a tabela:" + e.getMessage(), e);
        } finally {
            /* Fecha a conexão de qualquer maneira(sendo cathc ou não) */
            ConnectionFactory.closeConnection(this.connection);
        }
    }

    /* Listar todos */

    public List<Carros> listarTodos(){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        carros = new ArrayList<>();

        try {
            stmt = connection.prepareStatement("SELECT * FROM carros_loja");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Carros carro = new Carros(
                rs.getString("marca"),
                rs.getString("modelo"),
                rs.getString("ano"),
                rs.getString("placa"),
                rs.getString("valor")
                );
                carros.add(carro);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }finally{
            ConnectionFactory.closeConnection(connection, stmt, rs);
        }
        return carros;
    }

    public void cadastrar(String marca, String modelo, String ano, String placa, String valor) {
        PreparedStatement stmt = null;

        String sqlCadastrarCarro = "INSERT INTO carros (marca, modelo, ano, placa, valor) VALUES (?,?,?,?,?)";

        try {
            stmt = connection.prepareStatement(sqlCadastrarCarro);

            stmt.setString(1, marca);
            stmt.setString(2, modelo);
            stmt.setString(3, ano);
            stmt.setString(4, placa);
            stmt.setString(5,valor);
            stmt.executeUpdate();
            System.out.println("Dados inseridos com sucesso");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir dados.", e);
        } finally {
            ConnectionFactory.closeConnection(connection, stmt);
        }
    }
    /* Atualizar dados no banco */

    public void atualizar(String marca, String modelo, String ano, String placa, String valor) {
        PreparedStatement stmt = null;

        String sqlAtualizarDados = "UPDATE carros_loja SET marca = ?, modelo = ?, ano = ?, valor = ?, WHERE placa = ?";

        try {
            stmt = connection.prepareStatement(sqlAtualizarDados);
            stmt.setString(1, marca);
            stmt.setString(2, modelo);
            stmt.setString(3, ano);
            stmt.setString(4, placa);
            stmt.setString(5, valor);
            stmt.executeUpdate();

            System.out.println("Dados atualizados com sucesso");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar dados.", e);
        } finally {
            ConnectionFactory.closeConnection(connection, stmt);
        }
    }

    /* Apagar dados do banco */

    public void apagar(String placa) {
        String message = "Deseja realmente deletar esse carro?";
        PreparedStatement stmt = null;

        String sqlApagarPelaPlaca = "DELETE FROM carros_loja WHERE placa = ?";

        try {
            int escolhaJO = JOptionPane.showConfirmDialog(null, message);
            if (escolhaJO == JOptionPane.YES_OPTION) {
                  stmt = connection.prepareStatement(sqlApagarPelaPlaca);
            stmt.setString(1, placa);
            stmt.executeUpdate();
            }if (escolhaJO == JOptionPane.NO_OPTION) {
                return;
            }
            System.out.println("Placa apagada com sucesso");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar pela placa", e);
        } finally {
            ConnectionFactory.closeConnection(connection, stmt);
        }
    }

    public void insertEmployee(String usuario, String senha) throws SQLException
    {

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
