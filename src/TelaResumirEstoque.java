import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TelaResumirEstoque extends JDialog implements ActionListener {
  private final Dados dados;
  private final Container container, containerProdutos;
  private final JButton botaoResumir;
  private final JComboBox<String> seletorFormasOrdenacao;
  private final String[] FORMAS = new String[] { "Nome", "Descrição", "Quantidade" };

  public TelaResumirEstoque(TelaOpcoes dono, Dados dados) {
    super(dono, "Resumir estoque", true);

    this.dados = dados;
    this.container = this.getContentPane();
    this.containerProdutos = new Container();
    this.botaoResumir = new JButton("Resumir estoque");
    this.botaoResumir.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoResumir.addActionListener(this);
    this.seletorFormasOrdenacao = new JComboBox<>(FORMAS);
    this.containerProdutos.setLayout(new BoxLayout(this.containerProdutos, BoxLayout.Y_AXIS));

    this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
    this.container.add(this.seletorFormasOrdenacao);
    this.container.add(this.botaoResumir);
    this.container.add(this.containerProdutos);

    this.setMinimumSize(new Dimension(600, this.container.getMinimumSize().height));
    this.pack();
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
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

    this.pack();
  }
}