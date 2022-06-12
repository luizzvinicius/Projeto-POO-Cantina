import exceptions.VendaInvalidaException;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;

public class TelaVenderProduto {
  private static final String[] FORMAS = new String[] { "Dinheiro", "Cartão de crédito", "Cartão de débito", "Pix" };

  private final Dados dados;
  private final JDialog dialog;

  private final Container containerCampos = new Container();
  private final JButton botaoVender = new JButton("Vender");
  private final JTextField campoQtd = new JTextField();
  private final JLabel labelErro = new JLabel();
  private final JComboBox<Produto> seletorProduto = new JComboBox<>();
  private final JComboBox<String> seletorFormasPagamento = new JComboBox<>(FORMAS);

  public TelaVenderProduto(Dados dados, JDialog dialog, TelaOpcoes dono) {
    this.dados = dados;
    this.dialog = dialog;
    this.dialog.setTitle("Vender produto");

    this.botaoVender.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoVender.addActionListener(this::handleAction);
    this.seletorProduto.setRenderer(this::renderizarProduto);
    this.labelErro.setAlignmentX(Component.CENTER_ALIGNMENT);

    for (var produto : this.dados.estoque.getProdutos()) {
      this.seletorProduto.addItem(produto);
    }

    this.containerCampos.setLayout(new GridLayout(0, 2));
    this.containerCampos.add(new JLabel("Produto:"));
    this.containerCampos.add(this.seletorProduto);
    this.containerCampos.add(new JLabel("Quantidade:"));
    this.containerCampos.add(this.campoQtd);
    this.containerCampos.add(new JLabel("Forma de pagamento:"));
    this.containerCampos.add(this.seletorFormasPagamento);

    var container = this.dialog.getContentPane();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    container.add(this.containerCampos);
    container.add(this.botaoVender);
    container.add(this.labelErro);

    this.dialog.setMinimumSize(new Dimension(600, container.getMinimumSize().height));
    this.dialog.pack();
    this.dialog.setVisible(true);
  }

  private void handleAction(ActionEvent event) {
    var produto = (Produto) this.seletorProduto.getSelectedItem();
    var qtd = this.campoQtd.getText();
    var formaPagamento = (String) this.seletorFormasPagamento.getSelectedItem();

    try {
      var qtdC = Integer.parseUnsignedInt(qtd);
      produto.venderQtd(qtdC);
      this.dados.estoque.atualizar(produto);

      var venda = new Venda(this.dados.funcionario.getEmail(), formaPagamento, 0d, produto.getPrecoVenda() * qtdC,
          LocalDate.now());
      this.dados.vendaDao.adicionar(venda);

      var item = new Item(venda.getCodigo(), produto.getCodigo(), qtdC, produto.getPrecoVenda());
      this.dados.itemDao.adicionar(item);

      this.dialog.setVisible(false);
    } catch (NumberFormatException | VendaInvalidaException e) {
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