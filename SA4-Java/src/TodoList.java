import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class TodoList extends JFrame {

    // atributos
    private JPanel mainPanel;
    private JTextField taskInputField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton markDoneButton;
    private JButton clearCompletedButton;
    private JComboBox<String> filterComboBox;
    private DefaultListModel<String> listModel;
    private JList<String> taskList;
    private List<Task> tasks;

    // construtor
    public TodoList() {
        super("To-Do List App");
        this.setDefaultCloseOperation(2);
        this.setBounds(600, 330, 600, 600);

        // inicializa a painel principal
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // inicializa a lista de tasks e a lista de tasks concluídas
        tasks = new ArrayList<>();
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);

        // inicializa campos de entrada, botões e JCcomboBox
        taskInputField = new JTextField();
        filterComboBox = new JComboBox<>(new String[] { "Todas", "Ativas", "Concluídas" });
        addButton = new JButton("Adicionar");
        deleteButton = new JButton("Excluir");
        clearCompletedButton = new JButton("Limpar Concluídas");
        markDoneButton = new JButton("Concluir");

        // configuração do painel de entrada
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(taskInputField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

        // configuração do painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(deleteButton);
        buttonPanel.add(markDoneButton);
        buttonPanel.add(filterComboBox);
        buttonPanel.add(clearCompletedButton);

        // adiciona os componente à janela principal
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        // adiciona o painel à janela
        this.add(mainPanel);

        // configuração de listener para os eventos
        addButton.addActionListener(e -> {
            if (taskInputField.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(null, "O campo não pode estar vazio!");
            } else if (!taskInputField.getText().isEmpty()) {
            }
        });
    }

    private void addTask() {
        // adiciona uma nova task à lista de tasks
        String taskDescription = taskInputField.getText().trim();

        if (!taskDescription.isEmpty()) {
            Task newTask = new Task(taskDescription);
            tasks.add(newTask);
            updateTaskList();
            taskInputField.setText("");
        }
    }

    private void deleteTask() {
        // excluui a task selecionada da lista de tasks
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < tasks.size()) {
            updateTaskList();
        }
    }

    private void markTaskDone() {
        // marca a task selecionada como concluída
        String filter = (String) filterComboBox.getSelectedItem();
        listModel.clear();
        for (Task task : tasks) {
            if (filter.equals("Todas") || (filter.equals("Ativas") &&
                    !task.isDone()) || (filter.equals("Concluídas") && task.isDone())) {
                listModel.addElement(task.getDescription());
            }
        }
    }

    private void clearCompletedTasks() {
        // limpa todas as tasks concluídas da lista
        List<Task> completedTasks = new ArrayList<>();
        for (Task task : completedTasks) {
            if (task.isDone()) {
                completedTasks.add(task);
            }
        }
        tasks.removeAll(completedTasks);
        updateTaskList();
    }

    private void updateTaskList() {
        // atualiza a lista de tasks exibida na GUI
        listModel.clear();
        for (Task task : tasks) {
            listModel.addElement(task.getDescription() + (task.isDone() ? "Concluída" : ""));
        }
    }

    // deixando a janela visivel
    public void run() {
        this.setVisible(true);
    }
}
