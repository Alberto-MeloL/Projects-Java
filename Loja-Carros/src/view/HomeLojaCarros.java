package view;

import javax.swing.JFrame;

public class HomeLojaCarros extends JFrame {// Janela principal

    // Construtor
    public HomeLojaCarros() {
        super("Loja de Carros");
        this.setBounds(550, 100, 1000, 900);
        this.setDefaultCloseOperation(2);
    }

    //Método para deixar a janela visível
    public void run() {
        this.setVisible(true);
    }
}
