import exceptions.VendaInvalidaException;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import javax.swing.*;

public class TelaVendaProduto extends JDialog implements ActionListener, ListCellRenderer<Produto> {
  private static final String[] FORMAS = new String[] { "Dinheiro", "Cartão de crédito", "Cartão de débito", "Pix" };

  private final Dados dados;
  private final Container container, containerCampos;
  private final JButton botaoVender;
  private final JTextField campoQtd;
  private final JLabel labelErro;
  private final JComboBox<Produto> seletorProduto;
  private final JComboBox<String> seletorFormasPagamento;

  public TelaVendaProduto(TelaOpcoes dono, Dados dados) {
    super(dono, "Vender produto", true);
    this.dados = dados;

    this.container = this.getContentPane();
    this.containerCampos = new Container();
    this.botaoVender = new JButton("Vender");
    this.botaoVender.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoVender.addActionListener(this);
    this.seletorProduto = new JComboBox<>();
    this.seletorProduto.setRenderer(this);
    this.campoQtd = new JTextField();
    this.seletorFormasPagamento = new JComboBox<>(FORMAS);
    this.labelErro = new JLabel();
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

    this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
    this.container.add(this.containerCampos);
    this.container.add(this.botaoVender);
    this.container.add(this.labelErro);

    this.setMinimumSize(new Dimension(600, this.container.getMinimumSize().height));
    this.pack();
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    var produto = (Produto) this.seletorProduto.getSelectedItem();
    var qtd = this.campoQtd.getText();
    var formaPagamento = (String) this.seletorFormasPagamento.getSelectedItem();

    try {
      var qtdC = Integer.parseUnsignedInt(qtd);
      produto.venderQtd(qtdC);
      this.dados.estoque.atualizar(produto);

      var venda = new Venda(this.dados.funcionario.getEmail(), formaPagamento, 0d, produto.getPrecoVenda() * qtdC, LocalDate.now());
      this.dados.vendaDao.adicionar(venda);

      var item = new Item(venda.getCodigo(), produto.getCodigo(), qtdC, produto.getPrecoVenda());
      this.dados.itemDao.adicionar(item);

      this.setVisible(false);
      this.dispose();
    } catch (NumberFormatException | VendaInvalidaException e) {
      this.labelErro.setText(e.getMessage());
      this.pack();
    }
  }

  @Override
  public Component getListCellRendererComponent(JList<? extends Produto> list, Produto value, int index,
      boolean isSelected, boolean cellHasFocus) {
    var msg = "%s, descrição: %s, preço de venda: R$ %.2f, quantidade: %d";
    var nome = value.getNome();
    var descricao = value.getDescricao();
    var qtdAtual = value.getQtdAtual();
    var precoVenda = value.getPrecoVenda();
    return new JLabel(String.format(msg, nome, descricao, precoVenda, qtdAtual));
  }
}