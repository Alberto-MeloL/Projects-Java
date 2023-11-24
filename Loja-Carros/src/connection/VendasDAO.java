package connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

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
        String sqlCadastrarVenda = "INSERT INTO vendas (placa, cpf, valor) VALUES (?, ?, ?)";
        // deletar carro da tabela de carros após a venda
        String sqlDeletarCarroVendido = "DELETE FROM carros WHERE placa = ?";
    
        try (PreparedStatement stmtVenda = connection.prepareStatement(sqlCadastrarVenda);
             PreparedStatement stmtDeletarCarroVendido = connection.prepareStatement(sqlDeletarCarroVendido)) {
    
            // Desativa o autoCommit
            connection.setAutoCommit(false);
    
            stmtVenda.setString(1, carroPlaca);
            stmtVenda.setString(2, clienteCpf);
            stmtVenda.setString(3, valor);
            stmtVenda.executeUpdate();
            System.out.println("Venda registrada com sucesso");
    
            // Deletando o carro
            stmtDeletarCarroVendido.setString(1, carroPlaca);
            stmtDeletarCarroVendido.executeUpdate();
    
            System.out.println("Carro deletado com sucesso.");
    
            // Faz o commit manualmente
            connection.commit();
    
        } catch (SQLException e) {
            try {
                // Realiza o rollback em caso de exceção
                connection.rollback();
            } catch (SQLException rollbackException) {
                throw new RuntimeException("Erro ao realizar rollback.", rollbackException);
            }
            throw new RuntimeException("Erro ao cadastrar venda.", e);
        } finally {
            try {
                // Ativa o autoCommit novamente e fecha a conexão
                connection.setAutoCommit(true);
                ConnectionFactory.closeConnection(connection);
            } catch (SQLException closeException) {
                throw new RuntimeException("Erro ao fechar a conexão.", closeException);
            }
        }
    }
    

}