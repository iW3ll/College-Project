package controller;

import java.awt.*;

public class CommonConstants {
    // frame config define o tamanho da interface gráfica principal
    public static final Dimension GUI_SIZE = new Dimension(500, 700);

    // banner config define o tamanho do banner (painel superior)
    public static final Dimension BANNER_SIZE = new Dimension(GUI_SIZE.width, 50);

    // task panel config define o tamanho do painel de tarefas, ajustado pelo tamanho da GUI
    public static final Dimension TASKPANEL_SIZE = new Dimension(GUI_SIZE.width - 30, GUI_SIZE.height - 175);

    // add task button config define o tamanho do botão de adicionar tarefa
    public static final Dimension ADDTASK_BUTTON_SIZE = new Dimension(GUI_SIZE.width, 50);

    // taskcomponent configs define o tamanho do campo de tarefa, checkbox e botão de exclusão
    public static final Dimension TASKFIELD_SIZE = new Dimension((int)(TASKPANEL_SIZE.width * 0.70), 60); // Ajusta tamanho da tarefa
    public static final Dimension CHECKBOX_SIZE = new Dimension((int)(TASKFIELD_SIZE.width * 0.05), 30);
    public static final Dimension DELETE_BUTTON_SIZE = new Dimension((int)(TASKFIELD_SIZE.width * 0.5), 20);
}
