import exceptions.ProdutoInvalidoException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TelaCadastroProduto extends JDialog implements ActionListener {
  private final Dados dados;
  private final Container container, containerCampos;
  private final JButton botaoCadastrar;
  private final JTextField campoDescricao, campoNome, campoPrecoCompra, campoPrecoVenda, campoQtdInicial,
      campoEstoqueMinimo;
  private final JLabel labelErro;

  public TelaCadastroProduto(TelaOpcoes dono, Dados dados) {
    super(dono, "Cadastrar produto", true);
    this.dados = dados;

    this.container = this.getContentPane();
    this.containerCampos = new Container();
    this.botaoCadastrar = new JButton("Cadastrar");
    this.botaoCadastrar.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoCadastrar.addActionListener(this);
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

    this.setMinimumSize(new Dimension(600, this.container.getMinimumSize().height));
    this.pack();
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent event) {
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
      var codProduto = this.dados.estoque.adicionar(produto);
      this.dados.cadastraDao.adicionar(this.dados.funcionario, codProduto);
      this.setVisible(false);
      this.dispose();
    } catch (NumberFormatException | ProdutoInvalidoException e) {
      this.labelErro.setText(e.getMessage());
      this.pack();
    }
  }
}