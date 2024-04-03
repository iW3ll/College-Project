import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterScreen extends JFrame implements ActionListener {
    private JTextField fullNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private LoginScreen loginScreen;

    public RegisterScreen(LoginScreen loginScreen) {
        this.loginScreen = loginScreen;
        setTitle("Register");
        setSize(300, 200);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setMaximumSize(new Dimension(300, 400));

        JPanel registrationPanel = new JPanel(new GridLayout(4, 2));
        registrationPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel fullNameLabel = new JLabel("Full Name:");
        registrationPanel.add(fullNameLabel);

        fullNameField = new JTextField();
        registrationPanel.add(fullNameField);

        JLabel emailLabel = new JLabel("Email:");
        registrationPanel.add(emailLabel);

        emailField = new JTextField();
        registrationPanel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        registrationPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        registrationPanel.add(passwordField);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                loginScreen.setVisible(true);
            }
        });

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(this);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(backButton);
        buttonPanel.add(registerButton);

        add(registrationPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        Database.insertUser(fullName, email, password);
        setVisible(false);
        loginScreen.setVisible(true);
    }
}
