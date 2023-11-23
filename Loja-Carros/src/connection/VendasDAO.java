package connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VendasDAO {
    private Connection connection;

    public VendasDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    
    public void criarTabelaVendas() {

        String sqlCriarTabelaVendas = "CREATE TABLE IF NOT EXISTS vendas (ID SERIAL PRIMARY KEY,PLACA VARCHAR(255), CPF VARCHAR(255), VALOR VARCHAR(255))";

        try (Statement stmt = this.connection.createStatement()) {
            /* Ejetor de código SQL */

            stmt.execute(sqlCriarTabelaVendas); /* Código a ser executado */
            System.out.println("Tabela criada com sucesso.");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar a tabela:" + e.getMessage(), e);
        } finally {
            /* Fecha a conexão de qualquer maneira(sendo cathc ou não) */
            ConnectionFactory.closeConnection(this.connection);
        }
    }

    public void cadastrarVenda(String carroPlaca, String clienteCpf, String valor) {
        String sqlCadastrarVenda = "INSERT INTO vendas (carro_placa, cliente_cpf, valor) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sqlCadastrarVenda)) {
            stmt.setString(1, carroPlaca);
            stmt.setString(2, clienteCpf);
            stmt.setString(3, valor);
            stmt.executeUpdate();
            System.out.println("Venda registrada com sucesso");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar venda.", e);
        } finally {
            ConnectionFactory.closeConnection(connection);
        }
    }
}