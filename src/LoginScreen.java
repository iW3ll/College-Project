import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame implements ActionListener {
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginScreen() {
        setTitle("Login");
        setSize(300, 200);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new GridLayout(3, 2));

        JLabel emailLabel = new JLabel("Email:");
        contentPanel.add(emailLabel);

        emailField = new JTextField();
        contentPanel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        contentPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        contentPanel.add(passwordField);

        add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        buttonPanel.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        buttonPanel.add(registerButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Login")) {
            // Handle login button click
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            // Implement login functionality here
            System.out.println("Logging in with email: " + email + " and password: " + password);
        } else if (command.equals("Register")) {
            new RegisterScreen(this);
            setVisible(false);
        }
    }
}
