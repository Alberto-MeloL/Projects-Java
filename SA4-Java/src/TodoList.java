import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


//clase
public class TodoList extends JFrame {

    // atributos
    private Color corVermelhoClaro = new Color(255, 200, 200);// cor da input(erro)
    private Color corBranca = new Color(255, 255, 255);// voltar ao padrão
    private JPanel mainPanel;// painel principal(janela)
    private JTextField taskInputField;// input para descrição das tarefas
    private JButton addButton;// botão de adicionar tarefas
    private JButton deleteButton;// botão de deletar tarefas
    private JButton markDoneButton;// marcar como concluída
    private JButton clearCompletedButton;// limpa todas as concluídas
    private JComboBox<String> filterComboBox;// filtrar tarefas
    private DefaultListModel<String> listModel;
    private JList<String> taskList;
    private List<Task> tasks;

    // construtor
    public TodoList() {
        super("To-Do List App");
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);// interrompe o comportamento padrão da janela
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int escolhaWindow = JOptionPane.showConfirmDialog(TodoList.this, "Deseja realmente fechar a janela?",
                        "Confirmação", JOptionPane.YES_NO_OPTION);// fecha ou não, conforme a escolha
                if (escolhaWindow == JOptionPane.YES_OPTION) {
                    setVisible(false);
                } else {
                    taskInputField.requestFocus();
                }
            }
        });
        this.setBounds(600, 330, 600, 600);// alihamento/tamanho

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

        // ouvintes do evento do mouse
        taskList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // define o cursor desejado ao passar por cima da lista
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // retorna o cursor padrão ao sair da lista
                setCursor(Cursor.getDefaultCursor());
            }
        });

        // adiciona um ouvinte de teclado para JTextField, para adicionar com a tecla CTRL + B
        //tem que ser dentor do construtor para que seja executada durante a inicialização
        taskInputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_B) {
                    addTask();
                }
            }
        });
        // adicionando à lista ao clicar no botão
        addButton.addActionListener(e -> {
            addTask();
        });
        // Adiciona ouvinte de teclado para deletar com CTRL + DELETE

        TodoList.this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e){
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_DELETE) {
                    deleteTask();
                }
            }
        });
        // deletando da lista
        deleteButton.addActionListener(e -> {
            deleteTask();
        });

        //adiciona um ouvinte de duplo click para marcar como concluída
        

        // marcando como concluida
        markDoneButton.addActionListener(e -> {
            markTaskDone();
        });
        // por tecla
        // deletando concluidas
        clearCompletedButton.addActionListener(e -> {
            clearCompletedTasks();
        });

    }

    // método de adicionar tarefas
    private void addTask() {
        // adiciona uma nova task à lista de tasks
        String taskDescription = taskInputField.getText().trim();// pega o texto da input, e romove o espaço com o
                                                                 // método trim()
        if (taskDescription.length() == 0) {
            JOptionPane.showMessageDialog(null, "O campo não pode estar vazio!");
            taskInputField.requestFocus();
            taskInputField.setBackground(corVermelhoClaro);
            return;// retorna rapidamente se o campo estiver vazio
        } else {
            Task newTask = new Task(taskDescription);
            tasks.add(newTask);
            taskInputField.setText("");
            taskInputField.setBackground(corBranca);
            taskInputField.requestFocus();
            updateTaskList();// atualiza a lista
        }

    }

    // método de deletar tarefas
    private void deleteTask() {
        // excluui a task selecionada da lista de tasks
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < tasks.size()) {
            Task taskRemove = tasks.get(selectedIndex);
            tasks.remove(taskRemove);
            updateTaskList();// atualiza a lista
        }
    }

    // métode de marcar como concluída
    private void markTaskDone() {
        // marca como concluída
        int selectedIndexTask = taskList.getSelectedIndex();
        if (selectedIndexTask >= 0 && selectedIndexTask < tasks.size()) {
            Task selectedTask = tasks.get(selectedIndexTask);
            selectedTask.setDone(true);
            updateTaskList();
        }
        // marca a task selecionada como concluída
        String filter = (String) filterComboBox.getSelectedItem();
        listModel.clear();
        for (Task task : tasks) {
            if (filter.equals("Todas") || (filter.equals("Ativas") && !task.isDone())) {
                listModel.addElement(task.getDescription());
                List<Task> markDone = new ArrayList<>();
                markDone.add(task);
            } else if (filter.equals("Concluídas")) {
                updateTaskList();// atualiza para exibir a lista
            }
        } // por tecla ctrl + s
    }

    // método de limpar as concluídas
    private void clearCompletedTasks() {
        // limpa todas as tasks concluídas da lista
        boolean hasCompletedTasks = false;
        // mensagem de aviso
        String message = "Verifique qual realmente deseja excluír, pois essa ação é permanente.";
        String title = "Aviso";
        // exibe a mensagem
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
        for (Task task : tasks) {
            if (task.isDone()) {
                hasCompletedTasks = true;
                break;// interrompe a ação
            }
        }
        // verifica se realmente tem tarefas à ser excluída
        if (!hasCompletedTasks) {
            JOptionPane.showMessageDialog(null, "Ainda não há tarefas concluídas!");
            taskInputField.requestFocus();
            return;
        }
        // faz a ação conforme a escolha do usuário
        int escolha = JOptionPane.showConfirmDialog(null, "Deseja continuar?", "Confirmação",
                JOptionPane.OK_CANCEL_OPTION);
        if (escolha == JOptionPane.OK_OPTION) {
            List<Task> completedTasks = new ArrayList<>();
            for (Task task : tasks) {
                if (task.isDone()) {
                    completedTasks.add(task);
                }
            }
            tasks.removeAll(completedTasks);
            updateTaskList();// atualiza a lista
        } else if (escolha == JOptionPane.CANCEL_OPTION) {
            taskInputField.requestFocus();// volta o foco na input
        }
    }

    // método que atualiza a lista
    private void updateTaskList() {
        // atualiza a lista de tasks exibida na GUI
        Icon greenCheck = new ImageIcon("./img/check.png");
        listModel.clear();
        for (Task task : tasks) {
            listModel.addElement(task.getDescription() + (task.isDone() ? greenCheck : ""));
        }
    }

    // deixando a janela visivel
    public void run() {
        this.setVisible(true);
    }
}
