import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TelaRemocaoProduto {
  private final JDialog dialog;
  private final Dados dados;
  private final Container container, containerCampos;
  private final JButton botaoContinuar;
  private final JComboBox<Produto> seletorProduto;
  private final JLabel labelErro;

  public TelaRemocaoProduto(TelaOpcoes dono, JDialog dialog, Dados dados) {
    this.dados = dados;
    this.dialog = dialog;
    this.dialog.setTitle("Remover produto");

    this.container = this.dialog.getContentPane();
    this.containerCampos = new Container();
    this.botaoContinuar = new JButton("Continuar");
    this.botaoContinuar.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoContinuar.addActionListener(this::handleAction);
    this.seletorProduto = new JComboBox<>();
    this.seletorProduto.setRenderer(this::renderizarProduto);
    this.labelErro = new JLabel();
    this.labelErro.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.containerCampos.setLayout(new GridLayout(0, 2));
    this.containerCampos.add(new JLabel("Produto:"));
    this.containerCampos.add(this.seletorProduto);

    this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
    this.container.add(this.containerCampos);
    this.container.add(this.botaoContinuar);
    this.container.add(this.labelErro);

    for (var produto : this.dados.estoque.getProdutos()) {
      this.seletorProduto.addItem(produto);
    }

    this.dialog.setMinimumSize(new Dimension(600, this.container.getMinimumSize().height));
    this.dialog.pack();
    this.dialog.setVisible(true);
  }

  private void handleAction(ActionEvent event) {
    var produto = (Produto) this.seletorProduto.getSelectedItem();
    this.dados.estoque.remover(produto);
    this.dialog.setVisible(false);
  }

  private Component renderizarProduto(JList<? extends Produto> list, Produto value, int index,
      boolean isSelected, boolean cellHasFocus) {
    var msg = "%s, descrição: %s, preço de venda: R$ %.2f, quantidade: %d";
    var nome = value.getNome();
    var descricao = value.getDescricao();
    var qtdAtual = value.getQtdAtual();
    var precoVenda = value.getPrecoVenda();
    return new JLabel(String.format(msg, nome, descricao, precoVenda, qtdAtual));
  }
}