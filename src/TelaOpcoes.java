import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class TelaOpcoes {
  private static final String[] DESCRICOES_OPCOES_DESLOGADO = new String[] { "Entrar", "Cadastrar" };

  private static final Opcao[] FUNCOES_OPCOES_DESLOGADO = new Opcao[] {
      TelaEntrarComoFuncionario::new, TelaCadastrarFuncionario::new
  };

  private static final String[] DESCRICOES_OPCOES = new String[] {
      "Cadastrar produto", "Vender produto", "Adicionar quantidade de produto", "Remover produto", "Resumir estoque",
      "Mostrar produtos em falta", "Mostrar lucro/prejuízo", "Sair da conta"
  };

  private static final Opcao[] FUNCOES_OPCOES = new Opcao[] {
      TelaCadastrarProduto::new, TelaVenderProduto::new, TelaAdicionarQtdProduto::new, TelaRemoverProduto::new,
      TelaResumirEstoque::new, TelaMostrarProdutosEmFalta::new, TelaMostrarLucroPrejuizo::new, TelaOpcoes::sairDaConta
  };

  private final Dados dados;
  private String[] descricoesOpcoes;
  private Opcao[] funcoesOpcoes;

  private final JFrame frame;
  private final JDialog dialog;
  private final Container container;

  public TelaOpcoes() {
    this.dados = new Dados();
    this.frame = new JFrame("Cantina IFAL");
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.dialog = new JDialog(this.frame, true);
    this.dialog.addWindowListener(new WindowListenerLimpador(this.dialog));

    this.container = this.frame.getContentPane();
    this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
    this.atualizarBotoes();
    this.frame.setLocationRelativeTo(null);
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

  private static void sairDaConta(Dados dados, JDialog dialog, TelaOpcoes telaOpcoes) {
    telaOpcoes.dados.funcionario = null;
    telaOpcoes.atualizarBotoes();
  }

  private void handleAction(ActionEvent event) {
    var opcao = Integer.parseInt(event.getActionCommand());
    this.funcoesOpcoes[opcao].rodar(this.dados, this.dialog, this);
  }

  @FunctionalInterface
  private interface Opcao {
    void rodar(Dados dados, JDialog dialog, TelaOpcoes telaOpcoes);
  }
}