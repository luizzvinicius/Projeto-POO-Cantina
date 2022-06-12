import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class TelaLucroPrejuizo {
  private final Dados dados;
  private final JDialog dialog;

  private final Container containerProdutos = new Container();
  private final JButton botaoFechar = new JButton("Fechar");

  public TelaLucroPrejuizo(Dados dados, JDialog dialog, TelaOpcoes dono) {
    this.dados = dados;
    this.dialog = dialog;
    this.dialog.setTitle("Mostrar lucro/prejuízo");

    this.botaoFechar.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoFechar.addActionListener(this::handleAction);

    this.containerProdutos.setLayout(new BoxLayout(this.containerProdutos, BoxLayout.Y_AXIS));

    for (var produto : this.dados.estoque.getProdutos()) {
      var msg = "%s, descrição: %s, preço de venda: R$ %.2f, quantidade: %d";
      var nome = produto.getNome();
      var descricao = produto.getDescricao();
      var qtdAtual = produto.getQtdAtual();
      var precoVenda = produto.getPrecoVenda();
      this.containerProdutos.add(new JLabel(String.format(msg, nome, descricao, precoVenda, qtdAtual)));
    }

    var container = this.dialog.getContentPane();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    container.add(this.containerProdutos);
    container.add(this.botaoFechar);

    this.dialog.setMinimumSize(new Dimension(600, container.getMinimumSize().height));
    this.dialog.pack();
    this.dialog.setVisible(true);
  }

  private void handleAction(ActionEvent event) {
    this.dialog.setVisible(false);
  }
}