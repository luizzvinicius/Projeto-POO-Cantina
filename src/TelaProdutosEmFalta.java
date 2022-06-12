import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TelaProdutosEmFalta extends JDialog implements ActionListener {
  private final Dados dados;
  private final Container container, containerProdutos;
  private final JButton botaoFechar;

  public TelaProdutosEmFalta(TelaOpcoes dono, Dados dados) {
    super(dono, "Mostrar produtos em falta", true);
    this.dados = dados;

    this.container = this.getContentPane();
    this.containerProdutos = new Container();
    this.botaoFechar = new JButton("Fechar");
    this.botaoFechar.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoFechar.addActionListener(this);

    this.containerProdutos.setLayout(new BoxLayout(this.containerProdutos, BoxLayout.Y_AXIS));

    for (var produto : this.dados.estoque.getProdutos()) {
      if (produto.getQtdAtual() > produto.getEstoqueMinimo()) {
        continue;
      }

      var msg = "%s, descrição: %s, preço de venda: R$ %.2f, quantidade: %d";
      var nome = produto.getNome();
      var descricao = produto.getDescricao();
      var qtdAtual = produto.getQtdAtual();
      var precoVenda = produto.getPrecoVenda();
      this.containerProdutos.add(new JLabel(String.format(msg, nome, descricao, precoVenda, qtdAtual)));
    }

    this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
    this.container.add(this.containerProdutos);
    this.container.add(this.botaoFechar);

    this.setMinimumSize(new Dimension(600, this.container.getMinimumSize().height));
    this.pack();
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    this.setVisible(false);
    this.dispose();
  }
}