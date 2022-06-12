import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class TelaResumirEstoque {
  private final String[] FORMAS = new String[] { "Pelo nome", "Pela descrição", "Pela quantidade (decrescente)" };

  private final Dados dados;
  private final JDialog dialog;

  private final Container containerProdutos = new Container();
  private final JButton botaoContinuar = new JButton("Continuar");
  private final JComboBox<String> seletorFormasOrdenacao = new JComboBox<>(FORMAS);

  public TelaResumirEstoque(Dados dados, JDialog dialog, TelaOpcoes dono) {
    this.dados = dados;
    this.dialog = dialog;
    this.dialog.setTitle("Resumir estoque");

    this.botaoContinuar.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoContinuar.addActionListener(this::handleAction);

    this.containerProdutos.setLayout(new BoxLayout(this.containerProdutos, BoxLayout.Y_AXIS));

    var container = this.dialog.getContentPane();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    container.add(this.seletorFormasOrdenacao);
    container.add(this.botaoContinuar);
    container.add(this.containerProdutos);

    this.dialog.setMinimumSize(new Dimension(600, container.getMinimumSize().height));
    this.dialog.pack();
    this.dialog.setVisible(true);
  }

  private void handleAction(ActionEvent e) {
    var escolha = this.seletorFormasOrdenacao.getSelectedIndex();

    String propriedade = null;
    var decrescente = false;

    if (escolha == 0) {
      propriedade = "nome";
    } else if (escolha == 1) {
      propriedade = "descricao";
    } else if (escolha == 2) {
      propriedade = "qtd_atual";
      decrescente = true;
    }

    var produtos = this.dados.estoque.getProdutos(propriedade, decrescente);
    this.containerProdutos.removeAll();

    for (var produto : produtos) {
      var msg = "Nome: %s, descrição: %s, quantidade: %d, preço de compra: %.2f, preço de venda: R$ %.2f";
      var nome = produto.getNome();
      var descricao = produto.getDescricao();
      var qtdAtual = produto.getQtdAtual();
      var precoVenda = produto.getPrecoVenda();
      var precoCompra = produto.getPrecoCompra();
      this.containerProdutos.add(new JLabel(String.format(msg, nome, descricao, qtdAtual, precoVenda, precoCompra)));
    }

    this.dialog.pack();
  }
}