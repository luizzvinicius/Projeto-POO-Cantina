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

  private final JDialog dialog;
  private final Dados dados;
  private final Container container, containerProdutos;
  private final JButton botaoResumir;
  private final JComboBox<String> seletorFormasOrdenacao;

  public TelaResumirEstoque(TelaOpcoes dono, JDialog dialog, Dados dados) {
    this.dados = dados;
    this.dialog = dialog;
    this.dialog.setTitle("Resumir estoque");

    this.container = this.dialog.getContentPane();
    this.containerProdutos = new Container();
    this.botaoResumir = new JButton("Resumir estoque");
    this.botaoResumir.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoResumir.addActionListener(this::handleAction);
    this.seletorFormasOrdenacao = new JComboBox<>(FORMAS);
    this.containerProdutos.setLayout(new BoxLayout(this.containerProdutos, BoxLayout.Y_AXIS));

    this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
    this.container.add(this.seletorFormasOrdenacao);
    this.container.add(this.botaoResumir);
    this.container.add(this.containerProdutos);

    this.dialog.setMinimumSize(new Dimension(600, this.container.getMinimumSize().height));
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