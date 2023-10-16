import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.Timer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//clase
public class TodoList extends JFrame {

    // declarando cores para serem usadas para estilização
    private Color corVermelhoClaro = new Color(255, 200, 200);
    private Color corBranca = new Color(255, 255, 255);
    private Color corVerdeCobre = new Color(82, 127, 118);
    private Color corVerdeClara = new Color(144, 238, 144);
    private Color corAzul = new Color(0, 0, 37);
    private Color corAmarela = new Color(238, 223, 0);
    private Color corCinzaclaro = new Color(205, 205, 205);
    // atributos
    private JPanel mainPanel;// painel principal(janela)
    private JTextField taskInputField;// input para descrição das tarefas
    private JButton addButton;// botão de adicionar tarefas
    private JButton deleteButton;// botão de deletar tarefas
    private JButton markDoneButton;// marcar como concluída
    private JButton clearCompletedButton;// limpa todas as concluídas
    private JButton shortcutsButton;// exibe os atalhos
    private JComboBox<String> filterComboBox;// filtrar tarefas
    private DefaultListModel<String> listModel;
    private JList<String> taskList;
    private List<Task> tasks;

    // construtor
    public TodoList() {
        super("To-Do List App");
        SwingUtilities.invokeLater(() -> {
            setGlobalFont();
            setGlobalTextColor(corAzul);
        });
        this.setBounds(600, 330, 600, 600);// alihamento/tamanho

        // intruções
        this.shortcutsButton();
        // tratando fechamentos da janela
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

        // estilização filterComboBox
        filterComboBox.setBackground(corBranca);

        addButton = new JButton("Adicionar");
        // estilização addButton
        addButton.setBackground(corAmarela);

        deleteButton = new JButton("Excluir");
        // estilização deleteButton
        deleteButton.setBackground(corVermelhoClaro);

        clearCompletedButton = new JButton("Limpar Concluídas");
        // estilização clearCompletedButton
        clearCompletedButton.setBackground(corVerdeCobre);

        markDoneButton = new JButton("Concluir");
        // estilização markDoneButton
        markDoneButton.setBackground(corVerdeClara);

        shortcutsButton = new JButton("Atalhos");
        // estilização shortcutsButton
        shortcutsButton.setBackground(corCinzaclaro);

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
        buttonPanel.add(shortcutsButton);

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

        // adiciona um ouvinte de teclado para JTextField, para adicionar com a tecla
        // CTRL + B
        // tem que ser dentro do construtor para que seja executada durante a
        // inicialização
        taskInputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_B) {
                    addTask();
                }
            }
        });

        // marcando como concluída com duplo click
        taskList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    markTaskDone();
                }
            }
        });

        // marcando como concluida por botão
        markDoneButton.addActionListener(e -> {
            markTaskDone();
        });

        // adicionando à lista ao clicar no botão
        addButton.addActionListener(e -> {
            addTask();
        });

        // deletando da lista por botão
        deleteButton.addActionListener(e -> {
            deleteTask();
        });

        // deletando da lista por CTRL + D
        taskList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_D) {
                    deleteTask();
                }
            }
        });

        // deletando concluidas por botão
        clearCompletedButton.addActionListener(e -> {
            clearCompletedTasks();
        });

        // deletando concluídas por CTRL + A
        taskList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_A) {
                    clearCompletedTasks();
                }
            }
        });

        // atalhos
        shortcutsButton.addActionListener(e -> {
            shortcutsButton();
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
        // ouvinte do ComboBox
        filterComboBox.addActionListener(e -> {
            String selectedFilter = (String) filterComboBox.getSelectedItem();
            listModel.clear();// limpa o modelo da lista

            for (Task task : tasks) {
                if (selectedFilter.equals("Todas") || (selectedFilter.equals("Ativas") && !task.isDone())
                        || (selectedFilter.equals("Concluídas")) && task.isDone()) {
                    listModel.addElement(task.getDescription());
                    updateTaskList();
                }
            }
        });
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
        }
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
        String selected = (String) filterComboBox.getSelectedItem();

        listModel.clear();

        for (Task task : tasks) {
            boolean taskFilter = false;

            if (selected.equals("Todas") ||
                    (selected.equals("Ativas") && !task.isDone()) ||
                    (selected.equals("Concluídas") && task.isDone())) {
                taskFilter = true;
            }
            if (taskFilter) {
                // adicione ✓ à descrição apenas para tarefas concluídas
                if (task.isDone() && selected.equals("Concluídas")) {
                    listModel.addElement(task.getDescription() + "  ✓");
                } else {
                    listModel.addElement(task.getDescription());
                }
            }
        }

    }

    // instruções
    private void shortcutsButton() {

        String messageShortcuts = "Adicionar tarefa:\nCTRL + B\n\nMarcar como conluída:\nDuplo click sobre uma tarefa\n\nDeletar tarefa:\nApós selecionada, pressione CTRL + D\n\nDeletar todas as concluídas:\nSelecione qualquer item da lista e pressione CTRL + A";
        String titleShortcuts = "Atalhos";

        JDialog dialog = new JDialog();
        JOptionPane.showMessageDialog(dialog, messageShortcuts, titleShortcuts, JOptionPane.WARNING_MESSAGE);

        int delay = 5000; // 5 segundos
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        timer.setRepeats(false); // para que seja executado apenas uma vez
        timer.start();
    }

    // definido globalmente
    private static void setGlobalFont() {
        Font customFont = new Font("Arial", Font.PLAIN, 16);

        // configura a fonte global para os componentes Swing
        UIManager.put("Button.font", customFont);
        UIManager.put("Label.font", customFont);
        UIManager.put("TextField.font", customFont);
        UIManager.put("TextArea.font", customFont);
        UIManager.put("ComboBox.font", customFont);

        // recarrega o UI
        for (Component c : JFrame.getFrames()) {
            SwingUtilities.updateComponentTreeUI(c);
        }
    }

    private static void setGlobalTextColor(Color color) {
        UIManager.put("Button.foreground", color);
        UIManager.put("Label.foreground", color);
        UIManager.put("TextField.foreground", color);
        UIManager.put("TextArea.foreground", color);
        UIManager.put("ComboBox.foreground", color);

        // recarrega o UI
        for (Component c : JFrame.getFrames()) {
            SwingUtilities.updateComponentTreeUI(c);
        }

    }

    // deixando a janela visivel
    public void run() {
        this.setVisible(true);
    }
}
