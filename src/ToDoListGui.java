import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ToDoListGui extends JFrame implements ActionListener {
    private JPanel taskPanel, taskComponentPanel;

    public ToDoListGui() {
        super("To Do List Application");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(540, 760));
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        addGuiComponents();
        loadTasksFromDatabase(); // Carrega as tarefas existentes
    }

    private void addGuiComponents() {
        // banner text
        JLabel bannerLabel = new JLabel("To Do List");
        bannerLabel.setFont(createFont("LEMONMILK-Light.otf", 36f)); // Usa fonte padrão se não encontrar o arquivo
        bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(bannerLabel, BorderLayout.NORTH);

        // taskpanel
        taskPanel = new JPanel();
        taskPanel.setLayout(new BorderLayout());

        // taskcomponentpanel
        taskComponentPanel = new JPanel();
        taskComponentPanel.setLayout(new BoxLayout(taskComponentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(taskComponentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        taskPanel.add(scrollPane, BorderLayout.CENTER);

        // add task button
        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.setFont(createFont("LEMONMILK-Light.otf", 18f)); // Usa fonte padrão se não encontrar o arquivo
        addTaskButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addTaskButton.addActionListener(this);
        add(taskPanel, BorderLayout.CENTER);
        add(addTaskButton, BorderLayout.SOUTH);
    }

    private Font createFont(String resource, float size) {
        URL resourceURL = getClass().getClassLoader().getResource(resource);
        if (resourceURL == null) {
            System.err.println("Resource not found: " + resource);
            return new Font("SansSerif", Font.PLAIN, (int) size); // Retorna uma fonte padrão em caso de erro
        }

        String filePath = resourceURL.getPath();
        if (filePath.contains("%20")) {
            filePath = filePath.replace("%20", " ");
        }

        try {
            File customFontFile = new File(filePath);
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, customFontFile).deriveFont(size);
            return customFont;
        } catch (Exception e) {
            System.err.println("Error loading font: " + e.getMessage());
        }
        return new Font("SansSerif", Font.PLAIN, (int) size); // Retorna uma fonte padrão em caso de erro
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equalsIgnoreCase("Add Task")) {
            // Cria um componente de tarefa
            TaskComponent taskComponent = new TaskComponent(taskComponentPanel);
            taskComponentPanel.add(taskComponent);

            // Atualiza a interface
            taskComponentPanel.revalidate();
            taskComponentPanel.repaint();
        }
    }



    // Carrega tarefas existentes do banco de dados
    private void loadTasksFromDatabase() {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tasks");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String taskText = rs.getString("task");
                boolean completed = rs.getBoolean("completed");
                int id = rs.getInt("id");

                TaskComponent taskComponent = new TaskComponent(taskComponentPanel);
                taskComponent.getTaskField().setText(taskText);
                if (completed) {
                    taskComponent.getCheckBox().setSelected(true);
                }
                taskComponent.setId(id); // Define o ID da tarefa
                taskComponentPanel.add(taskComponent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
