package connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*  Essa classe manipula conexões como banco de dados
-Gerencia a conexão com o SQL*/
public class ConnectionDAO {
    
    private Connection connection;

/*  Cria um objeto da ConnectionFactory(injetar códigos SQL) */

public ConnectionDAO() {

/*  Crio a conexão dentro do objeto, pois sempre que eu instancia-lo, tenho que ter a conexão */

    this.connection = ConnectionFactory.getConnection();

    }

/*  Métodos */

public void createTable() {

    String sqlCreateTable = "CREATE TABLE IF NOT EXISTS cars (ID SERIAL PRIMARY KEY,BRAND VARCHAR(255), MODEL VARCHAR(255), YEAR INTEGER CHECK (YEAR >= 1000 AND YEAR <= 9999), COLOR VARCHAR(255),PLATE VARCHAR(255),TYPE VARCHAR(255), PRICE VARCHAR(255))";
     

try (Statement stmt = this.connection.createStatement()){
/*  Ejetor de código SQL */

stmt.execute(sqlCreateTable); /* Código a ser executado */
System.out.println("Tabela criada com sucesso.");
} catch (Exception e) {
throw new RuntimeException("Erro ao criar a tabela:" + e.getMessage(), e);
}finally{
/*  Fecha a conexão de qualquer maneira(sendo cathc ou não) */
ConnectionFactory.closeConnection(this.connection);
}
    }

    public void deleteTable(){
        /*Deletar tabela se necessário */

        String sqlDeleteTable = "DROP TABLE cars";/*Exemplo, pode ser quaquel outra tabela */

        try (Statement stmt = this.connection.createStatement()){
            stmt.execute(sqlDeleteTable);/*Código a ser executado */
            System.out.println("Tabela deletada com sucesso.");
        } catch (Exception e) {
            
    throw new RuntimeException("Erro ao apagar a tabela." + e.getMessage(), e);
        }finally{
            ConnectionFactory.closeConnection(this.connection);
        }
    }

    public void insertEmployee(String usuario, String senha) throws SQLException{

    String sqlInsertEmployee = "INSERT INTO employee (USUARIO, SENHA) VALUES (?, ?)";
PreparedStatement stmt = connection.prepareStatement(sqlInsertEmployee);
    try {
        stmt.setString(1, usuario);
        stmt.setString(2, senha);
        stmt.executeUpdate();
        System.out.println("Dados inseridos com sucesso.");
    } catch (Exception e) {
        throw new RuntimeException("Erro ao inserir dados.", e);
    }finally{
        ConnectionFactory.closeConnection(connection, stmt);
    }
    }
}
