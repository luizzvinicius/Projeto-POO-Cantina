import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;

public class TelaRemoverProduto extends TelaFormulario {
  private final JComboBox<Produto> seletorProduto = new JComboBox<>();

  public TelaRemoverProduto(Dados dados, JDialog dialog, TelaOpcoes telaOpcoes) {
    super(dados, dialog);

    this.dialog.setTitle("Remover produto");
    this.botaoContinuar.setText("Remover");
    this.seletorProduto.setRenderer(this::renderizarProduto);
    this.adicionarCampo("Produto:", this.seletorProduto);

    for (var produto : this.dados.estoque.getProdutos()) {
      this.seletorProduto.addItem(produto);
    }

    this.mostrar(600);
  }

  @Override
  protected void apertouBotao(ActionEvent event) {
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