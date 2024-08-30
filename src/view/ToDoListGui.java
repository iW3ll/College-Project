package view;

import controller.TaskComponent;
import model.DatabaseManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

// Lida com os botões
public class ToDoListGui extends JFrame implements ActionListener {
    private JPanel taskComponentPanel;
    private JTextField taskInputField; // Caixa de texto para a nova tarefa
    private JButton addTaskButton; // Botão para adicionar a nova tarefa
    private JScrollPane scrollPane; // Referência ao JScrollPane para a rolagem

    public ToDoListGui() {
        super("To Do List Application");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Definir o tamanho padrão da janela
        setSize(400, 700);
        setResizable(false); // Desbloquear opção de maximizar

        getContentPane().setLayout(new BorderLayout());

        // Painel do banner (topo da janela)
        JPanel bannerPanel = new JPanel();
        bannerPanel.setPreferredSize(new Dimension(300, 50)); // Ajuste o tamanho do painel do banner se necessário
        bannerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel bannerLabel = new JLabel("To Do List");
        bannerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        bannerPanel.add(bannerLabel);

        // Painel de componentes de tarefa
        taskComponentPanel = new JPanel();
        taskComponentPanel.setLayout(new BoxLayout(taskComponentPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(taskComponentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Exibe a barra de rolagem quando necessário
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Nunca exibe a barra de rolagem horizontal
        scrollPane.setPreferredSize(new Dimension(300, 600)); // Ajuste o tamanho do painel de tarefas se necessário

        // Caixa de texto para inserir nova tarefa
        taskInputField = new JTextField();
        taskInputField.setPreferredSize(new Dimension(270, 30)); // Ajusta a largura e altura da caixa de texto

        // Botão para adicionar nova tarefa
        addTaskButton = new JButton("Adicionar");
        addTaskButton.setPreferredSize(new Dimension(100, 30));
        addTaskButton.setBackground(Color.WHITE); // Define a cor de fundo do adicionar
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

        setLocationRelativeTo(null); // Centraliza a janela na tela
    }

    // Carrega as tarefas salvas no banco de dados
    private void loadTasksFromDatabase() {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tasks");
             ResultSet rs = stmt.executeQuery()) {

            SimpleDateFormat databaseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yy 'às' HH:mm:ss");

            while (rs.next()) {
                TaskComponent taskComponent = new TaskComponent(taskComponentPanel);
                String taskText = rs.getString("task");
                boolean completed = rs.getBoolean("completed");
                String createdAt = rs.getString("created_at");

                // Converte a data do formato do banco de dados para o formato de exibição
                try {
                    Date date = databaseFormat.parse(createdAt);
                    String formattedDate = displayFormat.format(date);
                    taskComponent.setTaskText(taskText, completed, formattedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                taskComponent.setId(rs.getInt("id"));
                taskComponentPanel.add(taskComponent);
            }

            taskComponentPanel.revalidate(); // Atualiza o painel de componentes
            taskComponentPanel.repaint(); // Repaint para garantir a atualização visual
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lida com as funções da GUI
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addTaskButton) {
            String taskText = taskInputField.getText().trim();
            if (!taskText.isEmpty()) {
                TaskComponent taskComponent = new TaskComponent(taskComponentPanel);
                taskComponent.setTaskText(taskText, false, new SimpleDateFormat("dd/MM/yy 'às' HH:mm").format(new Date()));
                taskComponent.addTaskToDatabase(taskText);
                taskComponentPanel.add(taskComponent);
                taskComponentPanel.revalidate(); // Atualiza o painel de componentes
                taskComponentPanel.repaint(); // Repaint para garantir a atualização visual

                // Ajusta a rolagem para o final
                SwingUtilities.invokeLater(() -> {
                    JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
                    verticalScrollBar.setValue(verticalScrollBar.getMaximum());
                });

                taskInputField.setText(""); // Limpa a caixa de texto após adicionar a tarefa
            }
        }
    }
}
