import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TaskComponent extends JPanel implements ActionListener {
    private JCheckBox checkBox;
    private JTextPane taskField;
    private JButton deleteButton;
    private JButton editButton; // Botão de editar
    private int id; // ID da tarefa no banco de dados

    public TaskComponent(JPanel parentPanel) {
        // task field
        taskField = new JTextPane();
        taskField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        taskField.setPreferredSize(CommonConstants.TASKFIELD_SIZE);
        taskField.setContentType("text/html");
        taskField.setEditable(false); // Inicialmente não editável

        // checkbox
        checkBox = new JCheckBox();
        checkBox.setPreferredSize(CommonConstants.CHECKBOX_SIZE);
        checkBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        checkBox.addActionListener(this);

        // delete button
        deleteButton = new JButton("X");
        deleteButton.setPreferredSize(new Dimension(55, 20)); // Ajusta tamanho do botão
        deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(this);

        // edit button
        editButton = new JButton("Editar");
        editButton.setPreferredSize(new Dimension(70, 20));
        editButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        editButton.addActionListener(this);

        // Adiciona componentes ao painel da tarefa
        add(checkBox);
        add(taskField);
        add(editButton); // Adiciona o botão "Edit"
        add(deleteButton);
    }

    // Habilita ou desabilita a edição do campo de tarefa
    public void setEditable(boolean editable) {
        taskField.setEditable(editable);
        if (editable) {
            taskField.setBackground(Color.WHITE); // Define o fundo como branco ao editar
        } else {
            taskField.setBackground(Color.LIGHT_GRAY); // Define o fundo como cinza quando não editável
        }
    }

    // Aplica a formatação HTML
    public void setTaskText(String text, boolean completed) {
        // Define o texto com formatação HTML para linha tachada se a tarefa estiver concluída
        taskField.setText(completed ? "<html><s>" + text + "</s></html>" : text);
        checkBox.setSelected(completed); // Define o estado do checkbox
    }

    // Retorna o campo de texto da tarefa
    public JTextPane getTaskField() {
        return taskField;
    }

    // Adiciona a tarefa ao banco de dados
    public void addTaskToDatabase(String taskText) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO tasks (task, completed) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, taskText);
            stmt.setBoolean(2, checkBox.isSelected());
            stmt.executeUpdate();

            // Obtém o ID gerado e o armazena
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.id = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Atualiza a tarefa no banco de dados
    public void updateTaskInDatabase() {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE tasks SET task = ?, completed = ? WHERE id = ?")) {
            stmt.setString(1, taskField.getText().replaceAll("<[^>]*>", ""));
            stmt.setBoolean(2, checkBox.isSelected());
            stmt.setInt(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == editButton) {
            boolean isEditable = taskField.isEditable();
            setEditable(!isEditable); // Alterna entre editável e não editável

            if (isEditable) {
                updateTaskInDatabase(); // Salva as mudanças no banco de dados
                editButton.setText("Editar");
            } else {
                editButton.setText("Salvar");
            }
        } else if (e.getSource() == deleteButton) {
            deleteTaskFromDatabase(); // Remove a tarefa do banco de dados
            JPanel parentPanel = (JPanel) this.getParent(); // Obtém o painel pai
            parentPanel.remove(this); // Remove o componente da tarefa do painel pai
            parentPanel.revalidate(); // Revalida o painel pai
            parentPanel.repaint(); // Repainta o painel pai
        } else if (e.getSource() == checkBox) {
            String taskText = taskField.getText().replaceAll("<[^>]*>", ""); // Remove formatação HTML
            if (checkBox.isSelected()) {
                taskField.setText("<html><s>" + taskText + "</s></html>"); // Adiciona linha tachada
            } else {
                taskField.setText(taskText); // Remove linha tachada
            }
            updateTaskInDatabase(); // Atualiza o banco de dados com o novo estado
        }
    }

    // Define o ID da tarefa
    public void setId(int id) {
        this.id = id;
    }

    // Remove a tarefa do banco de dados
    private void deleteTaskFromDatabase() {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM tasks WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
