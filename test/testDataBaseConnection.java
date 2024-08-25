package test;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        try (Connection conn = DatabaseManager.getConnection()) {
            if (conn != null) {
                System.out.println("Conexão com o banco de dados bem-sucedida!");
            } else {
                System.out.println("Falha na conexão com o banco de dados.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
