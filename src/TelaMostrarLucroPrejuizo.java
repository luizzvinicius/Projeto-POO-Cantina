import javax.swing.JDialog;
import javax.swing.JLabel;

public class TelaMostrarLucroPrejuizo extends TelaInformacoesProdutos {
  public TelaMostrarLucroPrejuizo(Dados dados, JDialog dialog, TelaOpcoes telaOpcoes) {
    super(dialog);
    this.dialog.setTitle("Mostrar lucro/prejuízo");

    var produtos = dados.estoque.getProdutos();
    if (produtos.isEmpty()) {
      this.containerProdutos.add(new JLabel("Não há produtos cadastrados"));
      this.mostrar(600);
      return;
    }

    var receitaTotal = 0d;
    var custoTotal = 0d;

    for (var produto : dados.estoque.getProdutos()) {
      var receita = produto.getQtdVendida() * produto.getPrecoVenda();
      var custo = produto.getQtdComprada() * produto.getPrecoCompra();
      custoTotal += custo;
      receitaTotal += receita;

      var lucro = receita - custo;
      var msg = lucro > 0 ? "lucro" : "prejuízo";
      this.containerProdutos
          .add(new JLabel(String.format("%s deu um %s de R$ %.2f\n", produto.getNome(), msg, Math.abs(lucro))));
    }

    var lucroTotal = receitaTotal - custoTotal;
    var msg = lucroTotal > 0 ? "lucro" : "prejuízo";
    this.containerProdutos.add(new JLabel(String.format("Houve um %s total de R$ %.2f\n", msg, Math.abs(lucroTotal))));

    this.mostrar(400);
  }
}