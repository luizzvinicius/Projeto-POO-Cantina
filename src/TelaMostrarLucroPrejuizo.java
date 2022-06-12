import javax.swing.JDialog;
import javax.swing.JLabel;

public class TelaMostrarLucroPrejuizo extends TelaInformacoesProdutos {
  public TelaMostrarLucroPrejuizo(Dados dados, JDialog dialog, TelaOpcoes telaOpcoes) {
    super(dialog);
    this.dialog.setTitle("Mostrar lucro/prejuízo");

    for (var produto : dados.estoque.getProdutos()) {
      var msg = "%s, descrição: %s, preço de venda: R$ %.2f, quantidade: %d";
      var nome = produto.getNome();
      var descricao = produto.getDescricao();
      var qtdAtual = produto.getQtdAtual();
      var precoVenda = produto.getPrecoVenda();
      this.containerProdutos.add(new JLabel(String.format(msg, nome, descricao, precoVenda, qtdAtual)));
    }

    if (containerProdutos.getComponentCount() == 0) {
      this.containerProdutos.add(new JLabel("Não há produtos em falta"));
    }

    this.mostrar(600);
  }
}