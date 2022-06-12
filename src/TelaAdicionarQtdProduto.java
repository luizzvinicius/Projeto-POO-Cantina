import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TelaAdicionarQtdProduto {
  private final JDialog dialog;
  private final Dados dados;
  private final Container container, containerCampos;
  private final JButton botaoContinuar;
  private final JTextField campoQtd;
  private final JComboBox<Produto> seletorProduto;
  private final JLabel labelErro;

  public TelaAdicionarQtdProduto(TelaOpcoes dono, JDialog dialog, Dados dados) {
    this.dados = dados;
    this.dialog = dialog;
    this.dialog.setTitle("Adicionar quantidade ao produto");

    this.container = this.dialog.getContentPane();
    this.containerCampos = new Container();
    this.botaoContinuar = new JButton("Continuar");
    this.botaoContinuar.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoContinuar.addActionListener(this::handleAction);
    this.campoQtd = new JTextField();
    this.seletorProduto = new JComboBox<>();
    this.seletorProduto.setRenderer(this::renderizarProduto);
    this.labelErro = new JLabel();
    this.labelErro.setAlignmentX(Component.CENTER_ALIGNMENT);

    for (var produto : this.dados.estoque.getProdutos()) {
      this.seletorProduto.addItem(produto);
    }

    this.containerCampos.setLayout(new GridLayout(0, 2));
    this.containerCampos.add(new JLabel("Produto:"));
    this.containerCampos.add(this.seletorProduto);
    this.containerCampos.add(new JLabel("Quantidade a adicionar:"));
    this.containerCampos.add(this.campoQtd);

    this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
    this.container.add(this.containerCampos);
    this.container.add(this.botaoContinuar);
    this.container.add(this.labelErro);

    this.dialog.setMinimumSize(new Dimension(600, this.container.getMinimumSize().height));
    this.dialog.pack();
    this.dialog.setVisible(true);
  }

  private void handleAction(ActionEvent event) {
    var produto = (Produto) this.seletorProduto.getSelectedItem();
    var qtd = this.campoQtd.getText();

    try {
      var qtdC = Integer.parseUnsignedInt(qtd);
      produto.adicionarQtd(qtdC);
      this.dados.estoque.atualizar(produto);
      this.dialog.setVisible(false);
    } catch (NumberFormatException e) {
      this.labelErro.setText(e.getMessage());
      this.dialog.pack();
    }
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