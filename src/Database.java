import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    private static final String DB_HOST = "127.0.0.1";
    private static final int DB_PORT = 3306;
    private static final String DB_NAME = "collegeSubjects";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    // Add data in database users table 
    public static void insertUser(String fullName, String email, String password) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME, DB_USER, DB_PASSWORD)) {
            String insertSQL = "INSERT INTO users (name, email, password, semester) VALUES (?, ?, ?, 1)";
            try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                stmt.setString(1, fullName);
                stmt.setString(2, email);
                stmt.setString(3, password);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User inserted successfully!");
                } else {
                    System.out.println("Failed to insert user");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error: Failed to connect to database");
        }
    }
}
