import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;

public class TelaAdicionarQtdProduto {
  private final Dados dados;
  private final JDialog dialog;

  private final Container containerCampos = new Container();
  private final JButton botaoAdicionar = new JButton("Adicionar");
  private final JTextField campoQtd = new JTextField();
  private final JComboBox<Produto> seletorProduto = new JComboBox<>();
  private final JLabel labelErro = new JLabel();

  public TelaAdicionarQtdProduto(Dados dados, JDialog dialog, TelaOpcoes dono) {
    this.dados = dados;
    this.dialog = dialog;
    this.dialog.setTitle("Adicionar quantidade ao produto");

    this.botaoAdicionar.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoAdicionar.addActionListener(this::handleAction);
    this.seletorProduto.setRenderer(this::renderizarProduto);
    this.labelErro.setAlignmentX(Component.CENTER_ALIGNMENT);

    for (var produto : this.dados.estoque.getProdutos()) {
      this.seletorProduto.addItem(produto);
    }

    this.containerCampos.setLayout(new GridLayout(0, 2));
    this.containerCampos.add(new JLabel("Produto:"));
    this.containerCampos.add(this.seletorProduto);
    this.containerCampos.add(new JLabel("Quantidade a adicionar:"));
    this.containerCampos.add(this.campoQtd);

    var container = this.dialog.getContentPane();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    container.add(this.containerCampos);
    container.add(this.botaoAdicionar);
    container.add(this.labelErro);

    this.dialog.setMinimumSize(new Dimension(600, container.getMinimumSize().height));
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