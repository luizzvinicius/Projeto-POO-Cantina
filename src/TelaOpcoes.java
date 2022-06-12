import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TelaOpcoes {
  private static final String[] DESCRICOES_OPCOES_DESLOGADO = new String[] { "Entrar", "Cadastrar" };

  private static final Opcao[] FUNCOES_OPCOES_DESLOGADO = new Opcao[] {
      TelaLoginFuncionario::new, TelaCadastroFuncionario::new
  };

  private static final String[] DESCRICOES_OPCOES = new String[] {
      "Cadastrar produto", "Vender produto", "Adicionar quantidade ao produto", "Remover produto", "Resumir estoque",
      "Ver produtos em falta", "Mostrar lucro/prejuízo", "Sair da conta"
  };

  private static final Opcao[] FUNCOES_OPCOES = new Opcao[] {
      TelaCadastroProduto::new, TelaVendaProduto::new, TelaAdicionarQtdProduto::new, TelaRemocaoProduto::new,
      TelaResumirEstoque::new, TelaProdutosEmFalta::new, TelaLucroPrejuizo::new, TelaOpcoes::sairDaConta
  };

  private final JFrame frame;
  private final JDialog dialog;
  private final Dados dados;
  private final Container container;
  private String[] descricoesOpcoes;
  private Opcao[] funcoesOpcoes;

  public TelaOpcoes() {
    this.dados = new Dados();
    this.frame = new JFrame("Cantina IFAL");
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.dialog = new JDialog(this.frame, true);
    this.dialog.addWindowListener(new WindowListenerLimpador(this.dialog));

    this.container = this.frame.getContentPane();
    this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
    this.atualizarBotoes();

    var dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) ((dimension.getWidth() - this.frame.getWidth()) / 2);
    int y = (int) ((dimension.getHeight() - this.frame.getHeight()) / 2);
    this.frame.setLocation(x, y);
    this.frame.setVisible(true);
  }

  public void atualizarBotoes() {
    this.container.removeAll();

    if (this.dados.funcionario != null) {
      var label = new JLabel("Logado como: " + this.dados.funcionario.getNome());
      label.setAlignmentX(Component.CENTER_ALIGNMENT);
      this.container.add(label);

      this.descricoesOpcoes = DESCRICOES_OPCOES;
      this.funcoesOpcoes = FUNCOES_OPCOES;
    } else {
      this.descricoesOpcoes = DESCRICOES_OPCOES_DESLOGADO;
      this.funcoesOpcoes = FUNCOES_OPCOES_DESLOGADO;
    }

    for (var i = 0; i < this.descricoesOpcoes.length; i++) {
      var botao = new JButton(this.descricoesOpcoes[i]);
      botao.setAlignmentX(Component.CENTER_ALIGNMENT);
      botao.setMaximumSize(new Dimension(300, this.frame.getMaximumSize().height));
      botao.addActionListener(this::handleAction);
      botao.setActionCommand(Integer.toString(i));
      this.container.add(botao);
    }

    this.frame.setMinimumSize(new Dimension(300, this.frame.getMinimumSize().height));
    this.frame.pack();
  }

  private static void sairDaConta(TelaOpcoes telaOpcoes, JDialog dialog, Dados dados) {
    telaOpcoes.dados.funcionario = null;
    telaOpcoes.atualizarBotoes();
  }

  private void handleAction(ActionEvent event) {
    var opcao = Integer.parseInt(event.getActionCommand());
    this.funcoesOpcoes[opcao].rodar(this, this.dialog, this.dados);
  }

  @FunctionalInterface
  private interface Opcao {
    void rodar(TelaOpcoes telaOpcoes, JDialog dialog, Dados dados);
  }
}