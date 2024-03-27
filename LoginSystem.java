import javax.swing.*;
import java.sql.*;

public class LoginSystem {
    private static final String DB_URL = "linkToDB";
    private static final String DB_USER = "username";
    private static final String DB_PASSWORD = "password";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginSystem::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(usernameLabel)
                .addComponent(usernameField)
                .addComponent(passwordLabel)
                .addComponent(passwordField)
                .addComponent(loginButton)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(usernameLabel)
                .addComponent(usernameField)
                .addComponent(passwordLabel)
                .addComponent(passwordField)
                .addComponent(loginButton)
        );

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (login(username, password)) {
                JOptionPane.showMessageDialog(frame, "Login successful!", "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }

    private static boolean login(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE username = ? AND password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // If any row matches, return true (valid login)
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; // Return false in case of any database error
        }
    }
}
