package view;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class JanelaPrincipal extends JFrame {
   // criação do tabbedPane para incluir as tabs
   private JTabbedPane jTPane;

   public JanelaPrincipal() {
      jTPane = new JTabbedPane();
      add(jTPane);
      // criandos as tabs
      // tab1 carros
      JanelaCarros tab1 = new JanelaCarros();
      jTPane.add("Carros", tab1);
      setBounds(100, 100, 600, 600);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }

   // métodos para tornar a janela visível
   public void run() {
      try {
         this.setVisible(true);

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
