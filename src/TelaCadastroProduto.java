import exceptions.ProdutoInvalidoException;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class TelaCadastroProduto {
  private final Dados dados;
  private final JDialog dialog;

  private final Container containerCampos = new Container();
  private final JButton botaoContinuar = new JButton("Continuar");
  private final JTextField campoDescricao = new JTextField();
  private final JTextField campoNome = new JTextField();
  private final JTextField campoPrecoCompra = new JTextField();
  private final JTextField campoPrecoVenda = new JTextField();
  private final JTextField campoQtdInicial = new JTextField();
  private final JTextField campoEstoqueMinimo = new JTextField();
  private final JLabel labelErro = new JLabel();

  public TelaCadastroProduto(Dados dados, JDialog dialog, TelaOpcoes dono) {
    this.dados = dados;
    this.dialog = dialog;
    this.dialog.setTitle("Cadastrar produto");

    this.botaoContinuar.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoContinuar.addActionListener(this::handleAction);
    this.labelErro.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.containerCampos.setLayout(new GridLayout(0, 2));
    this.containerCampos.add(new JLabel("Nome:"));
    this.containerCampos.add(this.campoNome);
    this.containerCampos.add(new JLabel("Descrição:"));
    this.containerCampos.add(this.campoDescricao);
    this.containerCampos.add(new JLabel("Preço de compra:"));
    this.containerCampos.add(this.campoPrecoCompra);
    this.containerCampos.add(new JLabel("Preço de venda:"));
    this.containerCampos.add(this.campoPrecoVenda);
    this.containerCampos.add(new JLabel("Quantidade inicial:"));
    this.containerCampos.add(this.campoQtdInicial);
    this.containerCampos.add(new JLabel("Estoque mínimo:"));
    this.containerCampos.add(this.campoEstoqueMinimo);

    var container = this.dialog.getContentPane();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    container.add(this.containerCampos);
    container.add(this.botaoContinuar);
    container.add(this.labelErro);

    this.dialog.setMinimumSize(new Dimension(600, container.getMinimumSize().height));
    this.dialog.pack();
    this.dialog.setVisible(true);
  }

  private void handleAction(ActionEvent event) {
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