import exceptions.ProdutoInvalidoException;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JTextField;

public class TelaCadastrarProduto extends TelaFormulario {
  private final JTextField campoDescricao = new JTextField();
  private final JTextField campoNome = new JTextField();
  private final JTextField campoPrecoCompra = new JTextField();
  private final JTextField campoPrecoVenda = new JTextField();
  private final JTextField campoQtdInicial = new JTextField();
  private final JTextField campoEstoqueMinimo = new JTextField();

  public TelaCadastrarProduto(Dados dados, JDialog dialog, TelaOpcoes telaOpcoes) {
    super(dados, dialog);

    this.dialog.setTitle("Cadastrar produto");
    this.botaoContinuar.setText("Cadastrar");
    this.adicionarCampo("Nome:", this.campoNome);
    this.adicionarCampo("Descrição:", this.campoDescricao);
    this.adicionarCampo("Preço de venda:", this.campoPrecoVenda);
    this.adicionarCampo("Preço de compra:", this.campoPrecoCompra);
    this.adicionarCampo("Quantidade inicial:", this.campoQtdInicial);
    this.adicionarCampo("Estoque mínimo:", this.campoEstoqueMinimo);

    this.mostrar(600);
  }

  @Override
  protected void apertouBotao(ActionEvent event) {
    var nome = this.campoNome.getText();
    var descricao = this.campoDescricao.getText();
    var precoCompra = this.campoPrecoCompra.getText();
    var precoVenda = this.campoPrecoVenda.getText();
    var qtdInicial = this.campoQtdInicial.getText();
    var estoqueMinimo = this.campoEstoqueMinimo.getText();

    try {
      var precoCompraC = Double.parseDouble(precoCompra);
      var precoVendaC = Double.parseDouble(precoVenda);
      var qtdInicialC = Integer.parseUnsignedInt(qtdInicial);
      var estoqueMinimoC = Integer.parseUnsignedInt(estoqueMinimo);

      var produto = new Produto(nome, descricao, precoVendaC, precoCompraC, qtdInicialC, estoqueMinimoC);
      this.dados.estoque.adicionar(produto);
      this.dados.cadastraDao.adicionar(this.dados.funcionario, produto);
      this.dialog.setVisible(false);
    } catch (NumberFormatException | ProdutoInvalidoException e) {
      this.labelErro.setText(e.getMessage());
      this.dialog.pack();
    }
  }
}