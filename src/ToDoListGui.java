import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class ToDoListGui extends JFrame implements ActionListener {
    private JPanel taskPanel, taskComponentPanel;
    private JTextField taskInputField; // Caixa de texto para a nova tarefa
    private JButton addTaskButton; // Botão para adicionar a nova tarefa

    public ToDoListGui() {
        super("To Do List Application");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(CommonConstants.GUI_SIZE);
        getContentPane().setLayout(new BorderLayout());

        // Painel do banner (topo da janela)
        JPanel bannerPanel = new JPanel();
        bannerPanel.setPreferredSize(CommonConstants.BANNER_SIZE);
        bannerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel bannerLabel = new JLabel("To Do List");
        bannerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        bannerPanel.add(bannerLabel);
        

        // Painel de componentes de tarefa
        taskComponentPanel = new JPanel();
        taskComponentPanel.setLayout(new BoxLayout(taskComponentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(taskComponentPanel);
        scrollPane.setPreferredSize(CommonConstants.TASKPANEL_SIZE);

        // Caixa de texto para inserir nova tarefa
        taskInputField = new JTextField();
        taskInputField.setPreferredSize(new Dimension(400, 30));

        // Botão para adicionar nova tarefa
        addTaskButton = new JButton("Add Task");
        addTaskButton.setPreferredSize(new Dimension(100, 30));
        addTaskButton.addActionListener(this);

        // Painel para o campo de entrada e o botão "Add Task"
        JPanel addTaskPanel = new JPanel();
        addTaskPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        addTaskPanel.add(taskInputField);
        addTaskPanel.add(addTaskButton);

        // Adiciona os painéis ao frame principal
        getContentPane().add(bannerPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(addTaskPanel, BorderLayout.SOUTH);

        // Carrega as tarefas salvas no banco de dados
        loadTasksFromDatabase();

        pack();
        setLocationRelativeTo(null); // Centraliza a janela na tela
    }

    // Carrega as tarefas salvas no banco de dados
    private void loadTasksFromDatabase() {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tasks");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TaskComponent taskComponent = new TaskComponent(taskComponentPanel);
                taskComponent.setTaskText(rs.getString("task"), rs.getBoolean("completed"));
                taskComponent.setId(rs.getInt("id"));
                taskComponentPanel.add(taskComponent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addTaskButton) {
            String taskText = taskInputField.getText().trim();
            if (!taskText.isEmpty()) {
                TaskComponent taskComponent = new TaskComponent(taskComponentPanel);
                taskComponent.setTaskText(taskText, false);
                taskComponent.addTaskToDatabase(taskText);
                taskComponentPanel.add(taskComponent);
                taskComponentPanel.revalidate();
                taskComponentPanel.repaint();
                taskComponent.scrollRectToVisible(taskComponent.getBounds());
                taskInputField.setText(""); // Limpa a caixa de texto após adicionar a tarefa
            }
        }
    }
}
