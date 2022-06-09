import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Tela extends JFrame implements ActionListener {
  private JButton[] botoes;
  private Container container;
  private Main main;

  public Tela() {
    this.main = new Main(new Entrada(this), this);

    botoes = new JButton[Main.DESCRICOES_OPCOES.length];
    this.setSize(820, 600);
    this.setTitle("Cantina IFAL");
    this.setVisible(true);
    this.setLayout(new GridLayout(3, 1, 10, 10));
    this.container = getContentPane();

    var x = 150;
    var y = 50;
    for (var i = 0; i < Main.DESCRICOES_OPCOES.length; i++) {
      var botao = new JButton(Main.DESCRICOES_OPCOES[i]);
      botao.setSize(230, 30);
      botao.setLocation(x, y);
      botao.addActionListener(this);
      botao.setActionCommand(Integer.toString(i));
      botao.setBackground(new Color(149, 1, 1));
      container.add(botao);
      botoes[i] = botao;

      x += 10;

    }

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Main.FUNCS_OPCOES[Integer.parseInt(e.getActionCommand())].accept(this.main);
  }
}
