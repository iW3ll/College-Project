import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TaskComponent extends JPanel implements ActionListener {
    private JCheckBox checkBox;
    private JTextPane taskField;
    private JButton deleteButton;
    private JButton saveButton; // Novo botão
    private int id; // ID da tarefa no banco de dados

    public JTextPane getTaskField() {
        return taskField;
    }

    public JCheckBox getCheckBox() {
        return checkBox;
    }

    public TaskComponent(JPanel parentPanel) {
        // task field
        taskField = new JTextPane();
        taskField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        taskField.setPreferredSize(CommonConstants.TASKFIELD_SIZE);
        taskField.setContentType("text/html");
        taskField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                taskField.setBackground(Color.WHITE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                taskField.setBackground(null);
            }
        });

        // checkbox
        checkBox = new JCheckBox();
        checkBox.setPreferredSize(CommonConstants.CHECKBOX_SIZE);
        checkBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        checkBox.addActionListener(this);

        // delete button
        deleteButton = new JButton("X");
        deleteButton.setPreferredSize(new Dimension(45, 20)); // ajusta tamanho do botão
        deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(this);

        // save button
        saveButton = new JButton("Ok");
        saveButton.setPreferredSize(new Dimension(50,20));
        saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(this);

        // add to this taskcomponent
        add(checkBox);
        add(taskField);
        add(saveButton); // Adiciona o botão "Save"
        add(deleteButton);
    }

    // Adiciona a tarefa ao banco de dados
    public void addTaskToDatabase(String taskText) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO tasks (task, completed) VALUES (?, ?)")) {
            stmt.setString(1, taskText);
            stmt.setBoolean(2, checkBox.isSelected());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Atualiza a tarefa no banco de dados
    public void updateTaskInDatabase() {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE tasks SET task = ?, completed = ? WHERE id = ?")) {
            stmt.setString(1, taskField.getText());
            stmt.setBoolean(2, checkBox.isSelected());
            stmt.setInt(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Deleta a tarefa do banco de dados
    public void deleteTaskFromDatabase() {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM tasks WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Adiciona e remove tanto do BD quanto o GUI, atualizando na mesma hora no GUI
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            String taskText = taskField.getText().replaceAll("<[^>]*>", "");
            if (id == 0) { // Se id == 0, é uma nova tarefa
                addTaskToDatabase(taskText);
            } else {
                updateTaskInDatabase();
            }
        } else if (e.getSource() == deleteButton) {
            deleteTaskFromDatabase(); // Remove a tarefa do banco de dados
            JPanel parentPanel = (JPanel) this.getParent(); // Obtém o painel pai
            parentPanel.remove(this); // Remove o componente da tarefa do painel pai
            parentPanel.revalidate(); // Revalida o painel pai
            parentPanel.repaint(); // Repainta o painel pai
        } else {
            if (checkBox.isSelected()) {
                String taskText = taskField.getText().replaceAll("<[^>]*>", "");
                taskField.setText("<html><s>" + taskText + "</s></html>");
            } else {
                String taskText = taskField.getText().replaceAll("<[^>]*>", "");
                taskField.setText(taskText);
            }
        }
    }

    // Define o ID da tarefa
    public void setId(int id) {
        this.id = id;
    }
}
