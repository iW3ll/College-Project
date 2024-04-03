import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean isRegistering = false;

    public LoginScreen() {
        setTitle("Login");
        setSize(300, 200);
        setResizable(false); // Prevent maximizing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setMaximumSize(new Dimension(300, 400)); // Increase maximum height

        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        contentPanel.add(usernameLabel);

        usernameField = new JTextField();
        contentPanel.add(usernameField);

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
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            // Implement login functionality here
            System.out.println("Logging in with username: " + username + " and password: " + password);
        } else if (command.equals("Register")) {
            if (!isRegistering) {
                addRegistrationFields();
            } else {
                removeRegistrationFields();
            }
            isRegistering = !isRegistering;
            revalidate();
            repaint();
        }
    }

    private void addRegistrationFields() {
        JLabel matriculaLabel = new JLabel("Matricula:");
        JTextField matriculaField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JPanel contentPanel = (JPanel) getContentPane();
        contentPanel.setLayout(new GridLayout(7, 2));
        contentPanel.add(matriculaLabel);
        contentPanel.add(matriculaField);
        contentPanel.add(emailLabel);
        contentPanel.add(emailField);
        contentPanel.add(passwordLabel);
        contentPanel.add(passwordField);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeRegistrationFields();
                isRegistering = false;
                revalidate();
                repaint();
            }
        });
        contentPanel.add(backButton);

        JButton checkButton = new JButton("Check");
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement registration information validation here
                String matricula = matriculaField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                System.out.println("Validating registration details: Matricula - " + matricula + ", Email - " + email + ", Password - " + password);
            }
        });
        contentPanel.add(checkButton);
    }

    private void removeRegistrationFields() {
        JPanel contentPanel = (JPanel) getContentPane();
        contentPanel.setLayout(new GridLayout(3, 2));
        Component[] components = contentPanel.getComponents();
        for (Component component : components) {
            contentPanel.remove(component);
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
    }
}
