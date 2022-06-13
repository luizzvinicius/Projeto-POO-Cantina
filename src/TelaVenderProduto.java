import exceptions.VendaInvalidaException;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;

public class TelaVenderProduto extends TelaFormulario {
  private static final String[] FORMAS = new String[] { "Dinheiro", "Cartão de crédito", "Cartão de débito", "Pix" };

  private final JTextField campoQtd = new JTextField();
  private final JComboBox<Produto> seletorProduto = new JComboBox<>();
  private final JComboBox<String> seletorFormasPagamento = new JComboBox<>(FORMAS);

  public TelaVenderProduto(Dados dados, JDialog dialog, TelaOpcoes telaOpcoes) {
    super(dados, dialog);

    this.dialog.setTitle("Vender produto");
    this.botaoContinuar.setText("Vender");
    this.seletorProduto.setRenderer(this::renderizarProduto);
    this.adicionarCampo("Produto:", this.seletorProduto);
    this.adicionarCampo("Quantidade:", this.campoQtd);
    this.adicionarCampo("Forma de pagamento:", this.seletorFormasPagamento);

    for (var produto : this.dados.estoque.getProdutos()) {
      if (produto.getQtdAtual() > 0) {
        this.seletorProduto.addItem(produto);
      }
    }

    this.mostrar(600);
  }

  @Override
  protected void apertouBotao(ActionEvent event) {
    var produto = (Produto) this.seletorProduto.getSelectedItem();
    var qtd = this.campoQtd.getText();
    var formaPagamento = (String) this.seletorFormasPagamento.getSelectedItem();

    if (produto == null) {
      this.labelErro.setText("Selecione um produto");
      this.dialog.pack();
      return;
    }

    try {
      var qtdC = Integer.parseUnsignedInt(qtd);
      produto.venderQtd(qtdC);
      this.dados.estoque.atualizar(produto);

      var venda = new Venda(this.dados.funcionario.getEmail(), formaPagamento, 0d, produto.getPrecoVenda() * qtdC,
          LocalDate.now());
      this.dados.vendas.adicionar(venda);

      var item = new Item(venda.getCodigo(), produto.getCodigo(), qtdC, produto.getPrecoVenda());
      this.dados.itens.adicionar(item);

      this.dialog.setVisible(false);
    } catch (NumberFormatException | VendaInvalidaException e) {
      this.labelErro.setText(e.getMessage());
      this.dialog.pack();
    }
  }

  private Component renderizarProduto(JList<? extends Produto> list, Produto value, int index,
      boolean isSelected, boolean cellHasFocus) {
    if (value == null) {
      return new JLabel();
    }

    var msg = "%s, descrição: %s, preço de venda: R$ %.2f, quantidade: %d";
    var nome = value.getNome();
    var descricao = value.getDescricao();
    var qtdAtual = value.getQtdAtual();
    var precoVenda = value.getPrecoVenda();
    return new JLabel(String.format(msg, nome, descricao, precoVenda, qtdAtual));
  }
}