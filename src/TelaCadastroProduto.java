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
  private final JDialog dialog;
  private final Dados dados;
  private final Container container, containerCampos;
  private final JButton botaoCadastrar;
  private final JTextField campoDescricao, campoNome, campoPrecoCompra, campoPrecoVenda, campoQtdInicial,
      campoEstoqueMinimo;
  private final JLabel labelErro;

  public TelaCadastroProduto(TelaOpcoes dono, JDialog dialog, Dados dados) {
    this.dados = dados;
    this.dialog = dialog;
    this.dialog.setTitle("Cadastrar produto");

    this.container = this.dialog.getContentPane();
    this.containerCampos = new Container();
    this.botaoCadastrar = new JButton("Cadastrar");
    this.botaoCadastrar.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoCadastrar.addActionListener(this::handleAction);
    this.campoDescricao = new JTextField();
    this.campoNome = new JTextField();
    this.campoPrecoCompra = new JTextField();
    this.campoPrecoVenda = new JTextField();
    this.campoQtdInicial = new JTextField();
    this.campoEstoqueMinimo = new JTextField();
    this.labelErro = new JLabel();
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

    this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
    this.container.add(this.containerCampos);
    this.container.add(this.botaoCadastrar);
    this.container.add(this.labelErro);

    this.dialog.setMinimumSize(new Dimension(600, this.container.getMinimumSize().height));
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