package controller;

import model.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskComponent extends JPanel implements ActionListener {
    private JCheckBox checkBox;
    private JTextPane taskField;
    private JLabel dateTimeLabel;
    private JButton deleteButton;
    private JButton editButton;
    private int id;

    public TaskComponent(JPanel parentPanel) {
        // Adiciona o campo de data e hora
        dateTimeLabel = new JLabel();
        dateTimeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        updateDateTimeLabel();

        // task field inicializa o campo de texto para a tarefa
        taskField = new JTextPane();
        taskField.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); // Remove o contorno
        taskField.setPreferredSize(new Dimension(250, 20)); // Ajusta o tamanho do campo de texto
        taskField.setContentType("text/html");
        taskField.setEditable(false);
        setBackground(Color.WHITE);
        taskField.setBackground(getBackground());

        // checkbox inicializa o checkbox
        checkBox = new JCheckBox();
        checkBox.setPreferredSize(new Dimension(20, 20));
        checkBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        checkBox.setBackground(Color.WHITE); // Define a cor de fundo do checkbox
        checkBox.addActionListener(this);

        // delete button
        deleteButton = new JButton("X");
        deleteButton.setPreferredSize(new Dimension(60, 20));
        deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deleteButton.setBackground(Color.WHITE); // Define a cor de fundo do delete
        deleteButton.addActionListener(this);

        // edit button
        editButton = new JButton("Editar");
        editButton.setPreferredSize(new Dimension(70, 20));
        editButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        editButton.setBackground(Color.WHITE); // Define a cor de fundo do editar
        editButton.addActionListener(this);

        // Configura o layout GridBagLayout para alinhar os componentes
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Adiciona espaço entre os componentes

        // Adiciona o label da data e hora
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        add(dateTimeLabel, gbc);

        // Adiciona o campo de texto da tarefa
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4; // Ajusta para ocupar mais espaço
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(taskField, gbc);

        // Adiciona a linha horizontal
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4; // Ajusta para ocupar mais espaço
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JSeparator line = new JSeparator(SwingConstants.HORIZONTAL);
        add(line, gbc);

        // Adiciona o checkbox, o botão editar e o botão excluir ao lado do texto
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(checkBox, gbc);

        gbc.gridx = 1;
        add(editButton, gbc);

        gbc.gridx = 2;
        add(deleteButton, gbc);
    }

    // Atualiza o label que exibe a data e hora atual.
    private void updateDateTimeLabel() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm"); // Cria um objeto SimpleDateFormat com o formato desejado para a data e hora
        String dateTime = dateFormat.format(new Date()); // Obtém a data e hora atual usando new Date(), Formata a data e hora atual usando o SimpleDateFormat
        dateTimeLabel.setText(dateTime); // Define o texto do dateTimeLabel para o valor formatado.
    }

    // Define o texto do dateTimeLabel com uma data e hora específica
    public void setDateTimeLabel(String dateTime) { // Recebe um String contendo a data e hora no formato desejado
        dateTimeLabel.setText(dateTime); // Define o texto do dateTimeLabel para esse valor
    }

    // Define se o campo de texto da tarefa deve ser editável e altera sua aparência com base nisso
    public void setEditable(boolean editable) {
        taskField.setEditable(editable); // Define se o campo de texto (taskField) é editável ou não usando setEditable(editable)
        if (editable) {
            taskField.setBackground(Color.WHITE);
        } else {
            taskField.setBackground(Color.LIGHT_GRAY);
        }
    }

    // Atualiza o texto da tarefa, o estado do checkbox e a data/hora exibida.
    public void setTaskText(String text, boolean completed, String dateTime) {
        taskField.setText(completed ? "<html><s>" + text + "</s></html>" : text); // Define o texto do campo de tarefa (taskField). Se a tarefa estiver completa (completed), o texto é formatado com uma linha cruzada usando HTML
        checkBox.setSelected(completed); // Define o estado do checkbox (checkBox) com base na conclusão da tarefa.
        dateTimeLabel.setText(dateTime); // Define o texto do dateTimeLabel com a data e hora fornecidas
    }

    // Retorna o campo de texto da tarefa
    public JTextPane getTaskField() {
        return taskField; // Retorna o objeto JTextPane que representa o campo de texto da tarefa
    }

    // Adiciona uma nova tarefa ao banco de dados
    public void addTaskToDatabase(String taskText) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO tasks (task, completed, created_at) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, taskText);
            stmt.setBoolean(2, checkBox.isSelected());
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis())); // Adiciona a data e hora atual
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.id = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        // Atualiza uma tarefa existente no banco de dados
    public void updateTaskInDatabase() {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE tasks SET task = ?, completed = ?, created_at = ? WHERE id = ?")) {
            stmt.setString(1, taskField.getText().replaceAll("<[^>]*>", ""));
            stmt.setBoolean(2, checkBox.isSelected());
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis())); // Atualiza com a data e hora atuais
            stmt.setInt(4, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lida com os eventos de ação dos botões de edição, exclusão e checkbox
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == editButton) {
            boolean isEditable = taskField.isEditable();
            setEditable(!isEditable);

            if (isEditable) {
                updateTaskInDatabase();
                editButton.setText("Editar");
            } else {
                editButton.setText("Salvar");
            }
        } else if (e.getSource() == deleteButton) {
            deleteTaskFromDatabase();
            JPanel parentPanel = (JPanel) this.getParent();
            parentPanel.remove(this);
            parentPanel.revalidate();
            parentPanel.repaint();
        } else if (e.getSource() == checkBox) {
            String taskText = taskField.getText().replaceAll("<[^>]*>", "");
            if (checkBox.isSelected()) {
                taskField.setText("<html><s>" + taskText + "</s></html>");
            } else {
                taskField.setText(taskText);
            }
            updateTaskInDatabase();
        }
    }

    // Define o ID da tarefa
    public void setId(int id) {
        this.id = id;
    }

    // emove uma tarefa do banco de dados com base no seu ID
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
