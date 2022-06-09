import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TelaOpcoes extends JFrame implements ActionListener {
  private static final String[] DESCRICOES_OPCOES_DESLOGADO = new String[] { "Entrar", "Cadastrar" };
  private static final Opcao[] FUNCOES_OPCOES = new Opcao[] { TelaLogin::new, TelaCadastro::new };

  private final Dados dados;
  private final Main main;
  private final Container container;
  private String[] descricoesOpcoes;
  private Main.Opcao[] funcoesOpcoes;

  public static void main(String[] args) {
    new TelaOpcoes();
  }

  public TelaOpcoes() {
    super("Cantina IFAL");
    this.dados = new Dados();
    this.main = new Main(new Entrada(this), dados, this);
    this.container = this.getContentPane();
    this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
    this.atualizarBotoes();
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }

  public void atualizarBotoes() {
    this.container.removeAll();

    if (this.dados.nomeFuncionario != null) {
      var label = new JLabel("Logado como: " + this.dados.nomeFuncionario);
      label.setAlignmentX(Component.CENTER_ALIGNMENT);
      this.container.add(label);

      this.descricoesOpcoes = Main.DESCRICOES_OPCOES;
      this.funcoesOpcoes = Main.FUNCOES_OPCOES;
    } else {
      this.descricoesOpcoes = DESCRICOES_OPCOES_DESLOGADO;
      this.funcoesOpcoes = null;
    }

    for (var i = 0; i < this.descricoesOpcoes.length; i++) {
      var botao = new JButton(this.descricoesOpcoes[i]);
      botao.setAlignmentX(Component.CENTER_ALIGNMENT);
      botao.setMaximumSize(new Dimension(300, this.getMaximumSize().height));
      botao.addActionListener(this);
      botao.setActionCommand(Integer.toString(i));
      this.container.add(botao);
    }

    this.setMinimumSize(new Dimension(300, this.getMinimumSize().height));
    this.pack();
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    var opcao = Integer.parseInt(event.getActionCommand());
    if (this.descricoesOpcoes == DESCRICOES_OPCOES_DESLOGADO) {
      FUNCOES_OPCOES[opcao].rodar(this, this.dados);
    } else {
      this.funcoesOpcoes[opcao].accept(this.main);
    }
  }

  @FunctionalInterface
  private interface Opcao {
    void rodar(TelaOpcoes telaOpcoes, Dados dados);
  }
}