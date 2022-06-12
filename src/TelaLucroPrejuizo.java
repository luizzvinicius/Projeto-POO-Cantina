import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TelaLucroPrejuizo extends JDialog {
  private final Dados dados;
  private final Container container, containerProdutos;
  private final JButton botaoFechar;

  public TelaLucroPrejuizo(TelaOpcoes dono, Dados dados) {
    super(dono.getFrame(), "Mostrar lucro/prejuízo", true);
    this.dados = dados;

    this.container = this.getContentPane();
    this.containerProdutos = new Container();
    this.botaoFechar = new JButton("Fechar");
    this.botaoFechar.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoFechar.addActionListener(this::actionPerformed);

    this.containerProdutos.setLayout(new BoxLayout(this.containerProdutos, BoxLayout.Y_AXIS));

    for (var produto : this.dados.estoque.getProdutos()) {
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

  private void actionPerformed(ActionEvent event) {
    this.setVisible(false);
    this.dispose();
  }
}