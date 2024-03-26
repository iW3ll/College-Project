//Testing login and pass to connect using fake bd with GUI

import javax.swing.*;

public class LoginSystem {
    public static void main(String[] args) {
        String username = JOptionPane.showInputDialog("Enter username:");
        String password = JOptionPane.showInputDialog("Enter password:");

        if (login(username, password)) {
            JOptionPane.showMessageDialog(null, "Login successful!");
        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password");
        }
    }

    private static boolean login(String username, String password) {
        
        String validUsername = "admin";
        String validPassword = "password";

        return username.equals(validUsername) && password.equals(validPassword);
    }
}
