import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;

public class TelaAdicionarQtdProduto extends TelaFormulario {
  private final JTextField campoQtd = new JTextField();
  private final JComboBox<Produto> seletorProduto = new JComboBox<>();

  public TelaAdicionarQtdProduto(Dados dados, JDialog dialog, TelaOpcoes telaOpcoes) {
    super(dados, dialog);

    this.dialog.setTitle("Adicionar quantidade de produto");
    this.botaoContinuar.setText("Adicionar");
    this.seletorProduto.setRenderer(this::renderizarProduto);

    this.adicionarCampo("Produto:", this.seletorProduto);
    this.adicionarCampo("Quantidade:", this.campoQtd);

    for (var produto : this.dados.estoque.getProdutos()) {
      this.seletorProduto.addItem(produto);
    }

    this.mostrar(600);
  }

  @Override
  protected void apertouBotao(ActionEvent event) {
    var produto = (Produto) this.seletorProduto.getSelectedItem();
    var qtd = this.campoQtd.getText();

    if (produto == null) {
      this.labelErro.setText("Selecione um produto");
      this.dialog.pack();
      return;
    }

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